package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ForwardingListenableFuture.SimpleForwardingListenableFuture;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.annotation.concurrent.GuardedBy;

@GwtCompatible(emulated = true)
public final class MoreExecutors {

    @GwtIncompatible
    @VisibleForTesting
    static class Application {
        Application() {
        }

        /* access modifiers changed from: 0000 */
        public final ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor, long j, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(threadPoolExecutor);
            ExecutorService unconfigurableExecutorService = Executors.unconfigurableExecutorService(threadPoolExecutor);
            addDelayedShutdownHook(unconfigurableExecutorService, j, timeUnit);
            return unconfigurableExecutorService;
        }

        /* access modifiers changed from: 0000 */
        public final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, long j, TimeUnit timeUnit) {
            MoreExecutors.useDaemonThreadFactory(scheduledThreadPoolExecutor);
            ScheduledExecutorService unconfigurableScheduledExecutorService = Executors.unconfigurableScheduledExecutorService(scheduledThreadPoolExecutor);
            addDelayedShutdownHook(unconfigurableScheduledExecutorService, j, timeUnit);
            return unconfigurableScheduledExecutorService;
        }

        /* access modifiers changed from: 0000 */
        public final void addDelayedShutdownHook(ExecutorService executorService, long j, TimeUnit timeUnit) {
            Preconditions.checkNotNull(executorService);
            Preconditions.checkNotNull(timeUnit);
            StringBuilder sb = new StringBuilder();
            sb.append("DelayedShutdownHook-for-");
            sb.append(executorService);
            String sb2 = sb.toString();
            final ExecutorService executorService2 = executorService;
            final long j2 = j;
            final TimeUnit timeUnit2 = timeUnit;
            C15401 r1 = new Runnable() {
                public void run() {
                    try {
                        executorService2.shutdown();
                        executorService2.awaitTermination(j2, timeUnit2);
                    } catch (InterruptedException unused) {
                    }
                }
            };
            addShutdownHook(MoreExecutors.newThread(sb2, r1));
        }

        /* access modifiers changed from: 0000 */
        public final ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor) {
            return getExitingExecutorService(threadPoolExecutor, 120, TimeUnit.SECONDS);
        }

        /* access modifiers changed from: 0000 */
        public final ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
            return getExitingScheduledExecutorService(scheduledThreadPoolExecutor, 120, TimeUnit.SECONDS);
        }

        /* access modifiers changed from: 0000 */
        @VisibleForTesting
        public void addShutdownHook(Thread thread) {
            Runtime.getRuntime().addShutdownHook(thread);
        }
    }

    private enum DirectExecutor implements Executor {
        INSTANCE;

        public String toString() {
            return "MoreExecutors.directExecutor()";
        }

        public void execute(Runnable runnable) {
            runnable.run();
        }
    }

    @GwtIncompatible
    private static final class DirectExecutorService extends AbstractListeningExecutorService {
        private final Object lock;
        @GuardedBy("lock")
        private int runningTasks;
        @GuardedBy("lock")
        private boolean shutdown;

        private DirectExecutorService() {
            this.lock = new Object();
            this.runningTasks = 0;
            this.shutdown = false;
        }

        public void execute(Runnable runnable) {
            startTask();
            try {
                runnable.run();
            } finally {
                endTask();
            }
        }

        public boolean isShutdown() {
            boolean z;
            synchronized (this.lock) {
                z = this.shutdown;
            }
            return z;
        }

        public void shutdown() {
            synchronized (this.lock) {
                this.shutdown = true;
                if (this.runningTasks == 0) {
                    this.lock.notifyAll();
                }
            }
        }

        public List<Runnable> shutdownNow() {
            shutdown();
            return Collections.emptyList();
        }

        public boolean isTerminated() {
            boolean z;
            synchronized (this.lock) {
                z = this.shutdown && this.runningTasks == 0;
            }
            return z;
        }

        public boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            long nanos = timeUnit.toNanos(j);
            synchronized (this.lock) {
                while (true) {
                    if (this.shutdown && this.runningTasks == 0) {
                        return true;
                    }
                    if (nanos <= 0) {
                        return false;
                    }
                    long nanoTime = System.nanoTime();
                    TimeUnit.NANOSECONDS.timedWait(this.lock, nanos);
                    nanos -= System.nanoTime() - nanoTime;
                }
            }
        }

        private void startTask() {
            synchronized (this.lock) {
                if (!this.shutdown) {
                    this.runningTasks++;
                } else {
                    throw new RejectedExecutionException("Executor already shutdown");
                }
            }
        }

        private void endTask() {
            synchronized (this.lock) {
                int i = this.runningTasks - 1;
                this.runningTasks = i;
                if (i == 0) {
                    this.lock.notifyAll();
                }
            }
        }
    }

    @GwtIncompatible
    private static class ListeningDecorator extends AbstractListeningExecutorService {
        private final ExecutorService delegate;

        ListeningDecorator(ExecutorService executorService) {
            this.delegate = (ExecutorService) Preconditions.checkNotNull(executorService);
        }

        public final boolean awaitTermination(long j, TimeUnit timeUnit) throws InterruptedException {
            return this.delegate.awaitTermination(j, timeUnit);
        }

        public final boolean isShutdown() {
            return this.delegate.isShutdown();
        }

        public final boolean isTerminated() {
            return this.delegate.isTerminated();
        }

        public final void shutdown() {
            this.delegate.shutdown();
        }

        public final List<Runnable> shutdownNow() {
            return this.delegate.shutdownNow();
        }

        public final void execute(Runnable runnable) {
            this.delegate.execute(runnable);
        }
    }

    @GwtIncompatible
    private static final class ScheduledListeningDecorator extends ListeningDecorator implements ListeningScheduledExecutorService {
        final ScheduledExecutorService delegate;

        private static final class ListenableScheduledTask<V> extends SimpleForwardingListenableFuture<V> implements ListenableScheduledFuture<V> {
            private final ScheduledFuture<?> scheduledDelegate;

            public ListenableScheduledTask(ListenableFuture<V> listenableFuture, ScheduledFuture<?> scheduledFuture) {
                super(listenableFuture);
                this.scheduledDelegate = scheduledFuture;
            }

            public boolean cancel(boolean z) {
                boolean cancel = super.cancel(z);
                if (cancel) {
                    this.scheduledDelegate.cancel(z);
                }
                return cancel;
            }

            public long getDelay(TimeUnit timeUnit) {
                return this.scheduledDelegate.getDelay(timeUnit);
            }

            public int compareTo(Delayed delayed) {
                return this.scheduledDelegate.compareTo(delayed);
            }
        }

        @GwtIncompatible
        private static final class NeverSuccessfulListenableFutureTask extends AbstractFuture<Void> implements Runnable {
            private final Runnable delegate;

            public NeverSuccessfulListenableFutureTask(Runnable runnable) {
                this.delegate = (Runnable) Preconditions.checkNotNull(runnable);
            }

            public void run() {
                try {
                    this.delegate.run();
                } catch (Throwable th) {
                    setException(th);
                    throw Throwables.propagate(th);
                }
            }
        }

        ScheduledListeningDecorator(ScheduledExecutorService scheduledExecutorService) {
            super(scheduledExecutorService);
            this.delegate = (ScheduledExecutorService) Preconditions.checkNotNull(scheduledExecutorService);
        }

        public ListenableScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
            TrustedListenableFutureTask create = TrustedListenableFutureTask.create(runnable, null);
            return new ListenableScheduledTask(create, this.delegate.schedule(create, j, timeUnit));
        }

        public <V> ListenableScheduledFuture<V> schedule(Callable<V> callable, long j, TimeUnit timeUnit) {
            TrustedListenableFutureTask create = TrustedListenableFutureTask.create(callable);
            return new ListenableScheduledTask(create, this.delegate.schedule(create, j, timeUnit));
        }

        public ListenableScheduledFuture<?> scheduleAtFixedRate(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            NeverSuccessfulListenableFutureTask neverSuccessfulListenableFutureTask = new NeverSuccessfulListenableFutureTask(runnable);
            return new ListenableScheduledTask(neverSuccessfulListenableFutureTask, this.delegate.scheduleAtFixedRate(neverSuccessfulListenableFutureTask, j, j2, timeUnit));
        }

        public ListenableScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
            NeverSuccessfulListenableFutureTask neverSuccessfulListenableFutureTask = new NeverSuccessfulListenableFutureTask(runnable);
            return new ListenableScheduledTask(neverSuccessfulListenableFutureTask, this.delegate.scheduleWithFixedDelay(neverSuccessfulListenableFutureTask, j, j2, timeUnit));
        }
    }

    private MoreExecutors() {
    }

    @GwtIncompatible
    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor, long j, TimeUnit timeUnit) {
        return new Application().getExitingExecutorService(threadPoolExecutor, j, timeUnit);
    }

    @GwtIncompatible
    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor, long j, TimeUnit timeUnit) {
        return new Application().getExitingScheduledExecutorService(scheduledThreadPoolExecutor, j, timeUnit);
    }

    @GwtIncompatible
    @Beta
    public static void addDelayedShutdownHook(ExecutorService executorService, long j, TimeUnit timeUnit) {
        new Application().addDelayedShutdownHook(executorService, j, timeUnit);
    }

    @GwtIncompatible
    @Beta
    public static ExecutorService getExitingExecutorService(ThreadPoolExecutor threadPoolExecutor) {
        return new Application().getExitingExecutorService(threadPoolExecutor);
    }

    @GwtIncompatible
    @Beta
    public static ScheduledExecutorService getExitingScheduledExecutorService(ScheduledThreadPoolExecutor scheduledThreadPoolExecutor) {
        return new Application().getExitingScheduledExecutorService(scheduledThreadPoolExecutor);
    }

    /* access modifiers changed from: private */
    @GwtIncompatible
    public static void useDaemonThreadFactory(ThreadPoolExecutor threadPoolExecutor) {
        threadPoolExecutor.setThreadFactory(new ThreadFactoryBuilder().setDaemon(true).setThreadFactory(threadPoolExecutor.getThreadFactory()).build());
    }

    @GwtIncompatible
    @Deprecated
    public static ListeningExecutorService sameThreadExecutor() {
        return new DirectExecutorService();
    }

    @GwtIncompatible
    public static ListeningExecutorService newDirectExecutorService() {
        return new DirectExecutorService();
    }

    public static Executor directExecutor() {
        return DirectExecutor.INSTANCE;
    }

    @GwtIncompatible
    public static ListeningExecutorService listeningDecorator(ExecutorService executorService) {
        if (executorService instanceof ListeningExecutorService) {
            return (ListeningExecutorService) executorService;
        }
        return executorService instanceof ScheduledExecutorService ? new ScheduledListeningDecorator((ScheduledExecutorService) executorService) : new ListeningDecorator(executorService);
    }

    @GwtIncompatible
    public static ListeningScheduledExecutorService listeningDecorator(ScheduledExecutorService scheduledExecutorService) {
        return scheduledExecutorService instanceof ListeningScheduledExecutorService ? (ListeningScheduledExecutorService) scheduledExecutorService : new ScheduledListeningDecorator(scheduledExecutorService);
    }

    @GwtIncompatible
    static <T> T invokeAnyImpl(ListeningExecutorService listeningExecutorService, Collection<? extends Callable<T>> collection, boolean z, long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        long j2;
        ExecutionException executionException;
        long j3;
        int i;
        ListeningExecutorService listeningExecutorService2 = listeningExecutorService;
        Preconditions.checkNotNull(listeningExecutorService);
        Preconditions.checkNotNull(timeUnit);
        int size = collection.size();
        Preconditions.checkArgument(size > 0);
        ArrayList<Future> newArrayListWithCapacity = Lists.newArrayListWithCapacity(size);
        LinkedBlockingQueue newLinkedBlockingQueue = Queues.newLinkedBlockingQueue();
        long nanos = timeUnit.toNanos(j);
        if (z) {
            try {
                j2 = System.nanoTime();
            } catch (ExecutionException e) {
                executionException = e;
            } catch (RuntimeException e2) {
                executionException = new ExecutionException(e2);
            } catch (Throwable th) {
                for (Future cancel : newArrayListWithCapacity) {
                    cancel.cancel(true);
                }
                throw th;
            }
        } else {
            j2 = 0;
        }
        Iterator it = collection.iterator();
        newArrayListWithCapacity.add(submitAndAddQueueListener(listeningExecutorService2, (Callable) it.next(), newLinkedBlockingQueue));
        int i2 = size - 1;
        long j4 = j2;
        executionException = null;
        long j5 = nanos;
        int i3 = 1;
        while (true) {
            Future future = (Future) newLinkedBlockingQueue.poll();
            if (future != null) {
                j3 = j4;
                i = i2;
            } else if (i2 > 0) {
                int i4 = i2 - 1;
                newArrayListWithCapacity.add(submitAndAddQueueListener(listeningExecutorService2, (Callable) it.next(), newLinkedBlockingQueue));
                i3++;
                j3 = j4;
                i = i4;
            } else if (i3 == 0) {
                if (executionException == null) {
                    executionException = new ExecutionException(null);
                }
                throw executionException;
            } else if (z) {
                future = (Future) newLinkedBlockingQueue.poll(j5, TimeUnit.NANOSECONDS);
                if (future != null) {
                    j3 = System.nanoTime();
                    j5 -= j3 - j4;
                    i = i2;
                } else {
                    throw new TimeoutException();
                }
            } else {
                future = (Future) newLinkedBlockingQueue.take();
                j3 = j4;
                i = i2;
            }
            if (future != null) {
                i3--;
                T t = future.get();
                for (Future cancel2 : newArrayListWithCapacity) {
                    cancel2.cancel(true);
                }
                return t;
            }
            i2 = i;
            j4 = j3;
        }
    }

    @GwtIncompatible
    private static <T> ListenableFuture<T> submitAndAddQueueListener(ListeningExecutorService listeningExecutorService, Callable<T> callable, final BlockingQueue<Future<T>> blockingQueue) {
        final ListenableFuture<T> submit = listeningExecutorService.submit(callable);
        submit.addListener(new Runnable() {
            public void run() {
                blockingQueue.add(submit);
            }
        }, directExecutor());
        return submit;
    }

    @GwtIncompatible
    @Beta
    public static ThreadFactory platformThreadFactory() {
        if (!isAppEngine()) {
            return Executors.defaultThreadFactory();
        }
        try {
            return (ThreadFactory) Class.forName("com.google.appengine.api.ThreadManager").getMethod("currentRequestThreadFactory", new Class[0]).invoke(null, new Object[0]);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e);
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e2);
        } catch (NoSuchMethodException e3) {
            throw new RuntimeException("Couldn't invoke ThreadManager.currentRequestThreadFactory", e3);
        } catch (InvocationTargetException e4) {
            throw Throwables.propagate(e4.getCause());
        }
    }

    @GwtIncompatible
    private static boolean isAppEngine() {
        boolean z = false;
        if (System.getProperty("com.google.appengine.runtime.environment") == null) {
            return false;
        }
        try {
            if (Class.forName("com.google.apphosting.api.ApiProxy").getMethod("getCurrentEnvironment", new Class[0]).invoke(null, new Object[0]) != null) {
                z = true;
            }
            return z;
        } catch (ClassNotFoundException unused) {
            return false;
        } catch (InvocationTargetException unused2) {
            return false;
        } catch (IllegalAccessException unused3) {
            return false;
        } catch (NoSuchMethodException unused4) {
            return false;
        }
    }

    @GwtIncompatible
    static Thread newThread(String str, Runnable runnable) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(runnable);
        Thread newThread = platformThreadFactory().newThread(runnable);
        try {
            newThread.setName(str);
        } catch (SecurityException unused) {
        }
        return newThread;
    }

    @GwtIncompatible
    static Executor renamingDecorator(final Executor executor, final Supplier<String> supplier) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(supplier);
        if (isAppEngine()) {
            return executor;
        }
        return new Executor() {
            public void execute(Runnable runnable) {
                executor.execute(Callables.threadRenaming(runnable, supplier));
            }
        };
    }

    @GwtIncompatible
    static ExecutorService renamingDecorator(ExecutorService executorService, final Supplier<String> supplier) {
        Preconditions.checkNotNull(executorService);
        Preconditions.checkNotNull(supplier);
        if (isAppEngine()) {
            return executorService;
        }
        return new WrappingExecutorService(executorService) {
            /* access modifiers changed from: protected */
            public <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, supplier);
            }

            /* access modifiers changed from: protected */
            public Runnable wrapTask(Runnable runnable) {
                return Callables.threadRenaming(runnable, supplier);
            }
        };
    }

    @GwtIncompatible
    static ScheduledExecutorService renamingDecorator(ScheduledExecutorService scheduledExecutorService, final Supplier<String> supplier) {
        Preconditions.checkNotNull(scheduledExecutorService);
        Preconditions.checkNotNull(supplier);
        if (isAppEngine()) {
            return scheduledExecutorService;
        }
        return new WrappingScheduledExecutorService(scheduledExecutorService) {
            /* access modifiers changed from: protected */
            public <T> Callable<T> wrapTask(Callable<T> callable) {
                return Callables.threadRenaming(callable, supplier);
            }

            /* access modifiers changed from: protected */
            public Runnable wrapTask(Runnable runnable) {
                return Callables.threadRenaming(runnable, supplier);
            }
        };
    }

    @GwtIncompatible
    @CanIgnoreReturnValue
    @Beta
    public static boolean shutdownAndAwaitTermination(ExecutorService executorService, long j, TimeUnit timeUnit) {
        long nanos = timeUnit.toNanos(j) / 2;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(nanos, TimeUnit.NANOSECONDS)) {
                executorService.shutdownNow();
                executorService.awaitTermination(nanos, TimeUnit.NANOSECONDS);
            }
        } catch (InterruptedException unused) {
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
        return executorService.isTerminated();
    }

    static Executor rejectionPropagatingExecutor(final Executor executor, final AbstractFuture<?> abstractFuture) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(abstractFuture);
        if (executor == directExecutor()) {
            return executor;
        }
        return new Executor() {
            volatile boolean thrownFromDelegate = true;

            public void execute(final Runnable runnable) {
                try {
                    executor.execute(new Runnable() {
                        public void run() {
                            C15385.this.thrownFromDelegate = false;
                            runnable.run();
                        }
                    });
                } catch (RejectedExecutionException e) {
                    if (this.thrownFromDelegate) {
                        abstractFuture.setException(e);
                    }
                }
            }
        };
    }
}
