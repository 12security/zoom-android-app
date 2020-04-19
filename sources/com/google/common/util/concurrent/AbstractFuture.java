package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import sun.misc.Unsafe;

@GwtCompatible(emulated = true)
public abstract class AbstractFuture<V> implements ListenableFuture<V> {
    /* access modifiers changed from: private */
    public static final AtomicHelper ATOMIC_HELPER;
    private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
    private static final Object NULL = new Object();
    private static final long SPIN_THRESHOLD_NANOS = 1000;
    private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
    /* access modifiers changed from: private */
    public volatile Listener listeners;
    /* access modifiers changed from: private */
    public volatile Object value;
    /* access modifiers changed from: private */
    public volatile Waiter waiters;

    private static abstract class AtomicHelper {
        /* access modifiers changed from: 0000 */
        public abstract boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2);

        /* access modifiers changed from: 0000 */
        public abstract boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2);

        /* access modifiers changed from: 0000 */
        public abstract boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2);

        /* access modifiers changed from: 0000 */
        public abstract void putNext(Waiter waiter, Waiter waiter2);

        /* access modifiers changed from: 0000 */
        public abstract void putThread(Waiter waiter, Thread thread);

        private AtomicHelper() {
        }
    }

    private static final class Cancellation {
        @Nullable
        final Throwable cause;
        final boolean wasInterrupted;

        Cancellation(boolean z, @Nullable Throwable th) {
            this.wasInterrupted = z;
            this.cause = th;
        }
    }

    private static final class Failure {
        static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") {
            public synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        final Throwable exception;

        Failure(Throwable th) {
            this.exception = (Throwable) Preconditions.checkNotNull(th);
        }
    }

    private static final class Listener {
        static final Listener TOMBSTONE = new Listener(null, null);
        final Executor executor;
        @Nullable
        Listener next;
        final Runnable task;

        Listener(Runnable runnable, Executor executor2) {
            this.task = runnable;
            this.executor = executor2;
        }
    }

    private static final class SafeAtomicHelper extends AtomicHelper {
        final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;

        SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> atomicReferenceFieldUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> atomicReferenceFieldUpdater2, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> atomicReferenceFieldUpdater3, AtomicReferenceFieldUpdater<AbstractFuture, Listener> atomicReferenceFieldUpdater4, AtomicReferenceFieldUpdater<AbstractFuture, Object> atomicReferenceFieldUpdater5) {
            super();
            this.waiterThreadUpdater = atomicReferenceFieldUpdater;
            this.waiterNextUpdater = atomicReferenceFieldUpdater2;
            this.waitersUpdater = atomicReferenceFieldUpdater3;
            this.listenersUpdater = atomicReferenceFieldUpdater4;
            this.valueUpdater = atomicReferenceFieldUpdater5;
        }

        /* access modifiers changed from: 0000 */
        public void putThread(Waiter waiter, Thread thread) {
            this.waiterThreadUpdater.lazySet(waiter, thread);
        }

        /* access modifiers changed from: 0000 */
        public void putNext(Waiter waiter, Waiter waiter2) {
            this.waiterNextUpdater.lazySet(waiter, waiter2);
        }

        /* access modifiers changed from: 0000 */
        public boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            return this.waitersUpdater.compareAndSet(abstractFuture, waiter, waiter2);
        }

        /* access modifiers changed from: 0000 */
        public boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            return this.listenersUpdater.compareAndSet(abstractFuture, listener, listener2);
        }

        /* access modifiers changed from: 0000 */
        public boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2) {
            return this.valueUpdater.compareAndSet(abstractFuture, obj, obj2);
        }
    }

    private static final class SetFuture<V> implements Runnable {
        final ListenableFuture<? extends V> future;
        final AbstractFuture<V> owner;

        SetFuture(AbstractFuture<V> abstractFuture, ListenableFuture<? extends V> listenableFuture) {
            this.owner = abstractFuture;
            this.future = listenableFuture;
        }

        public void run() {
            if (this.owner.value == this) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, AbstractFuture.getFutureValue(this.future))) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }
    }

    private static final class SynchronizedHelper extends AtomicHelper {
        private SynchronizedHelper() {
            super();
        }

        /* access modifiers changed from: 0000 */
        public void putThread(Waiter waiter, Thread thread) {
            waiter.thread = thread;
        }

        /* access modifiers changed from: 0000 */
        public void putNext(Waiter waiter, Waiter waiter2) {
            waiter.next = waiter2;
        }

        /* access modifiers changed from: 0000 */
        public boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            synchronized (abstractFuture) {
                if (abstractFuture.waiters != waiter) {
                    return false;
                }
                abstractFuture.waiters = waiter2;
                return true;
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            synchronized (abstractFuture) {
                if (abstractFuture.listeners != listener) {
                    return false;
                }
                abstractFuture.listeners = listener2;
                return true;
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2) {
            synchronized (abstractFuture) {
                if (abstractFuture.value != obj) {
                    return false;
                }
                abstractFuture.value = obj2;
                return true;
            }
        }
    }

    static abstract class TrustedFuture<V> extends AbstractFuture<V> {
        TrustedFuture() {
        }

        @CanIgnoreReturnValue
        public final V get() throws InterruptedException, ExecutionException {
            return AbstractFuture.super.get();
        }

        @CanIgnoreReturnValue
        public final V get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return AbstractFuture.super.get(j, timeUnit);
        }

        public final boolean isDone() {
            return AbstractFuture.super.isDone();
        }

        public final boolean isCancelled() {
            return AbstractFuture.super.isCancelled();
        }

        public final void addListener(Runnable runnable, Executor executor) {
            AbstractFuture.super.addListener(runnable, executor);
        }

        @CanIgnoreReturnValue
        public final boolean cancel(boolean z) {
            return AbstractFuture.super.cancel(z);
        }
    }

    private static final class UnsafeAtomicHelper extends AtomicHelper {
        static final long LISTENERS_OFFSET;
        static final Unsafe UNSAFE;
        static final long VALUE_OFFSET;
        static final long WAITERS_OFFSET;
        static final long WAITER_NEXT_OFFSET;
        static final long WAITER_THREAD_OFFSET;

        private UnsafeAtomicHelper() {
            super();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x005f, code lost:
            r0 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:13:0x006b, code lost:
            throw new java.lang.RuntimeException("Could not initialize intrinsics", r0.getCause());
         */
        /* JADX WARNING: Code restructure failed: missing block: B:3:?, code lost:
            r0 = (sun.misc.Unsafe) java.security.AccessController.doPrivileged(new com.google.common.util.concurrent.AbstractFuture.UnsafeAtomicHelper.C14981());
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:2:0x0005 */
        static {
            /*
                sun.misc.Unsafe r0 = sun.misc.Unsafe.getUnsafe()     // Catch:{ SecurityException -> 0x0005 }
                goto L_0x0010
            L_0x0005:
                com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper$1 r0 = new com.google.common.util.concurrent.AbstractFuture$UnsafeAtomicHelper$1     // Catch:{ PrivilegedActionException -> 0x005f }
                r0.<init>()     // Catch:{ PrivilegedActionException -> 0x005f }
                java.lang.Object r0 = java.security.AccessController.doPrivileged(r0)     // Catch:{ PrivilegedActionException -> 0x005f }
                sun.misc.Unsafe r0 = (sun.misc.Unsafe) r0     // Catch:{ PrivilegedActionException -> 0x005f }
            L_0x0010:
                java.lang.Class<com.google.common.util.concurrent.AbstractFuture> r1 = com.google.common.util.concurrent.AbstractFuture.class
                java.lang.String r2 = "waiters"
                java.lang.reflect.Field r2 = r1.getDeclaredField(r2)     // Catch:{ Exception -> 0x0055 }
                long r2 = r0.objectFieldOffset(r2)     // Catch:{ Exception -> 0x0055 }
                WAITERS_OFFSET = r2     // Catch:{ Exception -> 0x0055 }
                java.lang.String r2 = "listeners"
                java.lang.reflect.Field r2 = r1.getDeclaredField(r2)     // Catch:{ Exception -> 0x0055 }
                long r2 = r0.objectFieldOffset(r2)     // Catch:{ Exception -> 0x0055 }
                LISTENERS_OFFSET = r2     // Catch:{ Exception -> 0x0055 }
                java.lang.String r2 = "value"
                java.lang.reflect.Field r1 = r1.getDeclaredField(r2)     // Catch:{ Exception -> 0x0055 }
                long r1 = r0.objectFieldOffset(r1)     // Catch:{ Exception -> 0x0055 }
                VALUE_OFFSET = r1     // Catch:{ Exception -> 0x0055 }
                java.lang.Class<com.google.common.util.concurrent.AbstractFuture$Waiter> r1 = com.google.common.util.concurrent.AbstractFuture.Waiter.class
                java.lang.String r2 = "thread"
                java.lang.reflect.Field r1 = r1.getDeclaredField(r2)     // Catch:{ Exception -> 0x0055 }
                long r1 = r0.objectFieldOffset(r1)     // Catch:{ Exception -> 0x0055 }
                WAITER_THREAD_OFFSET = r1     // Catch:{ Exception -> 0x0055 }
                java.lang.Class<com.google.common.util.concurrent.AbstractFuture$Waiter> r1 = com.google.common.util.concurrent.AbstractFuture.Waiter.class
                java.lang.String r2 = "next"
                java.lang.reflect.Field r1 = r1.getDeclaredField(r2)     // Catch:{ Exception -> 0x0055 }
                long r1 = r0.objectFieldOffset(r1)     // Catch:{ Exception -> 0x0055 }
                WAITER_NEXT_OFFSET = r1     // Catch:{ Exception -> 0x0055 }
                UNSAFE = r0     // Catch:{ Exception -> 0x0055 }
                return
            L_0x0055:
                r0 = move-exception
                com.google.common.base.Throwables.throwIfUnchecked(r0)
                java.lang.RuntimeException r1 = new java.lang.RuntimeException
                r1.<init>(r0)
                throw r1
            L_0x005f:
                r0 = move-exception
                java.lang.RuntimeException r1 = new java.lang.RuntimeException
                java.lang.Throwable r0 = r0.getCause()
                java.lang.String r2 = "Could not initialize intrinsics"
                r1.<init>(r2, r0)
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractFuture.UnsafeAtomicHelper.<clinit>():void");
        }

        /* access modifiers changed from: 0000 */
        public void putThread(Waiter waiter, Thread thread) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, thread);
        }

        /* access modifiers changed from: 0000 */
        public void putNext(Waiter waiter, Waiter waiter2) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, waiter2);
        }

        /* access modifiers changed from: 0000 */
        public boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, WAITERS_OFFSET, waiter, waiter2);
        }

        /* access modifiers changed from: 0000 */
        public boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, LISTENERS_OFFSET, listener, listener2);
        }

        /* access modifiers changed from: 0000 */
        public boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, VALUE_OFFSET, obj, obj2);
        }
    }

    private static final class Waiter {
        static final Waiter TOMBSTONE = new Waiter(false);
        @Nullable
        volatile Waiter next;
        @Nullable
        volatile Thread thread;

        Waiter(boolean z) {
        }

        Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }

        /* access modifiers changed from: 0000 */
        public void setNext(Waiter waiter) {
            AbstractFuture.ATOMIC_HELPER.putNext(this, waiter);
        }

        /* access modifiers changed from: 0000 */
        public void unpark() {
            Thread thread2 = this.thread;
            if (thread2 != null) {
                this.thread = null;
                LockSupport.unpark(thread2);
            }
        }
    }

    /* access modifiers changed from: protected */
    @Beta
    public void afterDone() {
    }

    /* access modifiers changed from: protected */
    public void interruptTask() {
    }

    static {
        AtomicHelper atomicHelper;
        try {
            atomicHelper = new UnsafeAtomicHelper();
        } catch (Throwable th) {
            log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", th);
            log.log(Level.SEVERE, "SafeAtomicHelper is broken!", th);
            atomicHelper = new SynchronizedHelper();
        }
        ATOMIC_HELPER = atomicHelper;
        Class<LockSupport> cls = LockSupport.class;
    }

    private void removeWaiter(Waiter waiter) {
        waiter.thread = null;
        while (true) {
            Waiter waiter2 = this.waiters;
            if (waiter2 != Waiter.TOMBSTONE) {
                Waiter waiter3 = null;
                while (waiter2 != null) {
                    Waiter waiter4 = waiter2.next;
                    if (waiter2.thread != null) {
                        waiter3 = waiter2;
                    } else if (waiter3 != null) {
                        waiter3.next = waiter4;
                        if (waiter3.thread == null) {
                        }
                    } else if (!ATOMIC_HELPER.casWaiters(this, waiter2, waiter4)) {
                    }
                    waiter2 = waiter4;
                }
                return;
            }
            return;
        }
    }

    protected AbstractFuture() {
    }

    @CanIgnoreReturnValue
    public V get(long j, TimeUnit timeUnit) throws InterruptedException, TimeoutException, ExecutionException {
        long nanos = timeUnit.toNanos(j);
        if (!Thread.interrupted()) {
            Object obj = this.value;
            if ((obj != null) && (!(obj instanceof SetFuture))) {
                return getDoneValue(obj);
            }
            long nanoTime = nanos > 0 ? System.nanoTime() + nanos : 0;
            if (nanos >= 1000) {
                Waiter waiter = this.waiters;
                if (waiter != Waiter.TOMBSTONE) {
                    Waiter waiter2 = new Waiter();
                    do {
                        waiter2.setNext(waiter);
                        if (ATOMIC_HELPER.casWaiters(this, waiter, waiter2)) {
                            do {
                                LockSupport.parkNanos(this, nanos);
                                if (!Thread.interrupted()) {
                                    Object obj2 = this.value;
                                    if ((obj2 != null) && (!(obj2 instanceof SetFuture))) {
                                        return getDoneValue(obj2);
                                    }
                                    nanos = nanoTime - System.nanoTime();
                                } else {
                                    removeWaiter(waiter2);
                                    throw new InterruptedException();
                                }
                            } while (nanos >= 1000);
                            removeWaiter(waiter2);
                        } else {
                            waiter = this.waiters;
                        }
                    } while (waiter != Waiter.TOMBSTONE);
                }
                return getDoneValue(this.value);
            }
            while (nanos > 0) {
                Object obj3 = this.value;
                if ((obj3 != null) && (!(obj3 instanceof SetFuture))) {
                    return getDoneValue(obj3);
                }
                if (!Thread.interrupted()) {
                    nanos = nanoTime - System.nanoTime();
                } else {
                    throw new InterruptedException();
                }
            }
            throw new TimeoutException();
        }
        throw new InterruptedException();
    }

    @CanIgnoreReturnValue
    public V get() throws InterruptedException, ExecutionException {
        Object obj;
        if (!Thread.interrupted()) {
            Object obj2 = this.value;
            if ((obj2 != null) && (!(obj2 instanceof SetFuture))) {
                return getDoneValue(obj2);
            }
            Waiter waiter = this.waiters;
            if (waiter != Waiter.TOMBSTONE) {
                Waiter waiter2 = new Waiter();
                do {
                    waiter2.setNext(waiter);
                    if (ATOMIC_HELPER.casWaiters(this, waiter, waiter2)) {
                        do {
                            LockSupport.park(this);
                            if (!Thread.interrupted()) {
                                obj = this.value;
                            } else {
                                removeWaiter(waiter2);
                                throw new InterruptedException();
                            }
                        } while (!((obj != null) & (!(obj instanceof SetFuture))));
                        return getDoneValue(obj);
                    }
                    waiter = this.waiters;
                } while (waiter != Waiter.TOMBSTONE);
            }
            return getDoneValue(this.value);
        }
        throw new InterruptedException();
    }

    private V getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            throw cancellationExceptionWithCause("Task was cancelled.", ((Cancellation) obj).cause);
        } else if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        } else if (obj == NULL) {
            return null;
        } else {
            return obj;
        }
    }

    public boolean isDone() {
        Object obj = this.value;
        boolean z = true;
        boolean z2 = obj != null;
        if (obj instanceof SetFuture) {
            z = false;
        }
        return z2 & z;
    }

    public boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    @CanIgnoreReturnValue
    public boolean cancel(boolean z) {
        Object obj = this.value;
        if (!(obj == null) && !(obj instanceof SetFuture)) {
            return false;
        }
        Cancellation cancellation = new Cancellation(z, GENERATE_CANCELLATION_CAUSES ? new CancellationException("Future.cancel() was called.") : null);
        boolean z2 = false;
        Object obj2 = obj;
        AbstractFuture abstractFuture = this;
        while (true) {
            if (ATOMIC_HELPER.casValue(abstractFuture, obj2, cancellation)) {
                if (z) {
                    abstractFuture.interruptTask();
                }
                complete(abstractFuture);
                if (!(obj2 instanceof SetFuture)) {
                    return true;
                }
                ListenableFuture<? extends V> listenableFuture = ((SetFuture) obj2).future;
                if (listenableFuture instanceof TrustedFuture) {
                    abstractFuture = (AbstractFuture) listenableFuture;
                    obj2 = abstractFuture.value;
                    if (!(obj2 == null) && !(obj2 instanceof SetFuture)) {
                        return true;
                    }
                    z2 = true;
                } else {
                    listenableFuture.cancel(z);
                    return true;
                }
            } else {
                obj2 = abstractFuture.value;
                if (!(obj2 instanceof SetFuture)) {
                    return z2;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final boolean wasInterrupted() {
        Object obj = this.value;
        return (obj instanceof Cancellation) && ((Cancellation) obj).wasInterrupted;
    }

    public void addListener(Runnable runnable, Executor executor) {
        Preconditions.checkNotNull(runnable, "Runnable was null.");
        Preconditions.checkNotNull(executor, "Executor was null.");
        Listener listener = this.listeners;
        if (listener != Listener.TOMBSTONE) {
            Listener listener2 = new Listener(runnable, executor);
            do {
                listener2.next = listener;
                if (!ATOMIC_HELPER.casListeners(this, listener, listener2)) {
                    listener = this.listeners;
                } else {
                    return;
                }
            } while (listener != Listener.TOMBSTONE);
        }
        executeListener(runnable, executor);
    }

    /* access modifiers changed from: protected */
    @CanIgnoreReturnValue
    public boolean set(@Nullable V v) {
        if (v == null) {
            v = NULL;
        }
        if (!ATOMIC_HELPER.casValue(this, null, v)) {
            return false;
        }
        complete(this);
        return true;
    }

    /* access modifiers changed from: protected */
    @CanIgnoreReturnValue
    public boolean setException(Throwable th) {
        if (!ATOMIC_HELPER.casValue(this, null, new Failure((Throwable) Preconditions.checkNotNull(th)))) {
            return false;
        }
        complete(this);
        return true;
    }

    /* access modifiers changed from: protected */
    @CanIgnoreReturnValue
    @Beta
    public boolean setFuture(ListenableFuture<? extends V> listenableFuture) {
        SetFuture setFuture;
        Failure failure;
        Preconditions.checkNotNull(listenableFuture);
        Object obj = this.value;
        if (obj == null) {
            if (listenableFuture.isDone()) {
                if (!ATOMIC_HELPER.casValue(this, null, getFutureValue(listenableFuture))) {
                    return false;
                }
                complete(this);
                return true;
            }
            setFuture = new SetFuture(this, listenableFuture);
            if (ATOMIC_HELPER.casValue(this, null, setFuture)) {
                try {
                    listenableFuture.addListener(setFuture, MoreExecutors.directExecutor());
                } catch (Throwable unused) {
                    failure = Failure.FALLBACK_INSTANCE;
                }
                return true;
            }
            obj = this.value;
        }
        if (obj instanceof Cancellation) {
            listenableFuture.cancel(((Cancellation) obj).wasInterrupted);
        }
        return false;
        ATOMIC_HELPER.casValue(this, setFuture, failure);
        return true;
    }

    /* access modifiers changed from: private */
    public static Object getFutureValue(ListenableFuture<?> listenableFuture) {
        Object obj;
        if (listenableFuture instanceof TrustedFuture) {
            return ((AbstractFuture) listenableFuture).value;
        }
        try {
            Object done = Futures.getDone(listenableFuture);
            if (done == null) {
                done = NULL;
            }
            obj = done;
        } catch (ExecutionException e) {
            obj = new Failure(e.getCause());
        } catch (CancellationException e2) {
            obj = new Cancellation(false, e2);
        } catch (Throwable th) {
            obj = new Failure(th);
        }
        return obj;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Incorrect type for immutable var: ssa=com.google.common.util.concurrent.AbstractFuture<?>, code=com.google.common.util.concurrent.AbstractFuture, for r4v0, types: [com.google.common.util.concurrent.AbstractFuture<?>, com.google.common.util.concurrent.AbstractFuture] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void complete(com.google.common.util.concurrent.AbstractFuture r4) {
        /*
            r0 = 0
        L_0x0001:
            r4.releaseWaiters()
            r4.afterDone()
            com.google.common.util.concurrent.AbstractFuture$Listener r4 = r4.clearListeners(r0)
        L_0x000b:
            if (r4 == 0) goto L_0x0033
            com.google.common.util.concurrent.AbstractFuture$Listener r0 = r4.next
            java.lang.Runnable r1 = r4.task
            boolean r2 = r1 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture
            if (r2 == 0) goto L_0x002c
            com.google.common.util.concurrent.AbstractFuture$SetFuture r1 = (com.google.common.util.concurrent.AbstractFuture.SetFuture) r1
            com.google.common.util.concurrent.AbstractFuture<V> r4 = r1.owner
            java.lang.Object r2 = r4.value
            if (r2 != r1) goto L_0x0031
            com.google.common.util.concurrent.ListenableFuture<? extends V> r2 = r1.future
            java.lang.Object r2 = getFutureValue(r2)
            com.google.common.util.concurrent.AbstractFuture$AtomicHelper r3 = ATOMIC_HELPER
            boolean r1 = r3.casValue(r4, r1, r2)
            if (r1 == 0) goto L_0x0031
            goto L_0x0001
        L_0x002c:
            java.util.concurrent.Executor r4 = r4.executor
            executeListener(r1, r4)
        L_0x0031:
            r4 = r0
            goto L_0x000b
        L_0x0033:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractFuture.complete(com.google.common.util.concurrent.AbstractFuture):void");
    }

    /* access modifiers changed from: 0000 */
    public final Throwable trustedGetException() {
        return ((Failure) this.value).exception;
    }

    /* access modifiers changed from: 0000 */
    public final void maybePropagateCancellation(@Nullable Future<?> future) {
        if ((future != null) && isCancelled()) {
            future.cancel(wasInterrupted());
        }
    }

    private void releaseWaiters() {
        Waiter waiter;
        do {
            waiter = this.waiters;
        } while (!ATOMIC_HELPER.casWaiters(this, waiter, Waiter.TOMBSTONE));
        while (waiter != null) {
            waiter.unpark();
            waiter = waiter.next;
        }
    }

    private Listener clearListeners(Listener listener) {
        Listener listener2;
        do {
            listener2 = this.listeners;
        } while (!ATOMIC_HELPER.casListeners(this, listener2, Listener.TOMBSTONE));
        Listener listener3 = listener2;
        Listener listener4 = listener;
        Listener listener5 = listener3;
        while (listener5 != null) {
            Listener listener6 = listener5.next;
            listener5.next = listener4;
            listener4 = listener5;
            listener5 = listener6;
        }
        return listener4;
    }

    private static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            Logger logger = log;
            Level level = Level.SEVERE;
            StringBuilder sb = new StringBuilder();
            sb.append("RuntimeException while executing runnable ");
            sb.append(runnable);
            sb.append(" with executor ");
            sb.append(executor);
            logger.log(level, sb.toString(), e);
        }
    }

    private static CancellationException cancellationExceptionWithCause(@Nullable String str, @Nullable Throwable th) {
        CancellationException cancellationException = new CancellationException(str);
        cancellationException.initCause(th);
        return cancellationException;
    }
}
