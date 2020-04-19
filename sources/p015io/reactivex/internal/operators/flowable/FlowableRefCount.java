package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.Scheduler;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.flowables.ConnectableFlowable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.disposables.ResettableConnectable;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.plugins.RxJavaPlugins;
import p015io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableRefCount */
public final class FlowableRefCount<T> extends Flowable<T> {
    RefConnection connection;

    /* renamed from: n */
    final int f393n;
    final Scheduler scheduler;
    final ConnectableFlowable<T> source;
    final long timeout;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRefCount$RefConnection */
    static final class RefConnection extends AtomicReference<Disposable> implements Runnable, Consumer<Disposable> {
        private static final long serialVersionUID = -4552101107598366241L;
        boolean connected;
        boolean disconnectedEarly;
        final FlowableRefCount<?> parent;
        long subscriberCount;
        Disposable timer;

        RefConnection(FlowableRefCount<?> flowableRefCount) {
            this.parent = flowableRefCount;
        }

        public void run() {
            this.parent.timeout(this);
        }

        public void accept(Disposable disposable) throws Exception {
            DisposableHelper.replace(this, disposable);
            synchronized (this.parent) {
                if (this.disconnectedEarly) {
                    ((ResettableConnectable) this.parent.source).resetIf(disposable);
                }
            }
        }
    }

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableRefCount$RefCountSubscriber */
    static final class RefCountSubscriber<T> extends AtomicBoolean implements FlowableSubscriber<T>, Subscription {
        private static final long serialVersionUID = -7419642935409022375L;
        final RefConnection connection;
        final Subscriber<? super T> downstream;
        final FlowableRefCount<T> parent;
        Subscription upstream;

        RefCountSubscriber(Subscriber<? super T> subscriber, FlowableRefCount<T> flowableRefCount, RefConnection refConnection) {
            this.downstream = subscriber;
            this.parent = flowableRefCount;
            this.connection = refConnection;
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable th) {
            if (compareAndSet(false, true)) {
                this.parent.terminated(this.connection);
                this.downstream.onError(th);
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            if (compareAndSet(false, true)) {
                this.parent.terminated(this.connection);
                this.downstream.onComplete();
            }
        }

        public void request(long j) {
            this.upstream.request(j);
        }

        public void cancel() {
            this.upstream.cancel();
            if (compareAndSet(false, true)) {
                this.parent.cancel(this.connection);
            }
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.upstream, subscription)) {
                this.upstream = subscription;
                this.downstream.onSubscribe(this);
            }
        }
    }

    public FlowableRefCount(ConnectableFlowable<T> connectableFlowable) {
        this(connectableFlowable, 1, 0, TimeUnit.NANOSECONDS, Schedulers.trampoline());
    }

    public FlowableRefCount(ConnectableFlowable<T> connectableFlowable, int i, long j, TimeUnit timeUnit, Scheduler scheduler2) {
        this.source = connectableFlowable;
        this.f393n = i;
        this.timeout = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        RefConnection refConnection;
        boolean z;
        synchronized (this) {
            refConnection = this.connection;
            if (refConnection == null) {
                refConnection = new RefConnection(this);
                this.connection = refConnection;
            }
            long j = refConnection.subscriberCount;
            if (j == 0 && refConnection.timer != null) {
                refConnection.timer.dispose();
            }
            long j2 = j + 1;
            refConnection.subscriberCount = j2;
            z = true;
            if (refConnection.connected || j2 != ((long) this.f393n)) {
                z = false;
            } else {
                refConnection.connected = true;
            }
        }
        this.source.subscribe((FlowableSubscriber<? super T>) new RefCountSubscriber<Object>(subscriber, this, refConnection));
        if (z) {
            this.source.connect(refConnection);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x0040, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void cancel(p015io.reactivex.internal.operators.flowable.FlowableRefCount.RefConnection r6) {
        /*
            r5 = this;
            monitor-enter(r5)
            io.reactivex.internal.operators.flowable.FlowableRefCount$RefConnection r0 = r5.connection     // Catch:{ all -> 0x0041 }
            if (r0 == 0) goto L_0x003f
            io.reactivex.internal.operators.flowable.FlowableRefCount$RefConnection r0 = r5.connection     // Catch:{ all -> 0x0041 }
            if (r0 == r6) goto L_0x000a
            goto L_0x003f
        L_0x000a:
            long r0 = r6.subscriberCount     // Catch:{ all -> 0x0041 }
            r2 = 1
            long r0 = r0 - r2
            r6.subscriberCount = r0     // Catch:{ all -> 0x0041 }
            r2 = 0
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x003d
            boolean r0 = r6.connected     // Catch:{ all -> 0x0041 }
            if (r0 != 0) goto L_0x001c
            goto L_0x003d
        L_0x001c:
            long r0 = r5.timeout     // Catch:{ all -> 0x0041 }
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0027
            r5.timeout(r6)     // Catch:{ all -> 0x0041 }
            monitor-exit(r5)     // Catch:{ all -> 0x0041 }
            return
        L_0x0027:
            io.reactivex.internal.disposables.SequentialDisposable r0 = new io.reactivex.internal.disposables.SequentialDisposable     // Catch:{ all -> 0x0041 }
            r0.<init>()     // Catch:{ all -> 0x0041 }
            r6.timer = r0     // Catch:{ all -> 0x0041 }
            monitor-exit(r5)     // Catch:{ all -> 0x0041 }
            io.reactivex.Scheduler r1 = r5.scheduler
            long r2 = r5.timeout
            java.util.concurrent.TimeUnit r4 = r5.unit
            io.reactivex.disposables.Disposable r6 = r1.scheduleDirect(r6, r2, r4)
            r0.replace(r6)
            return
        L_0x003d:
            monitor-exit(r5)     // Catch:{ all -> 0x0041 }
            return
        L_0x003f:
            monitor-exit(r5)     // Catch:{ all -> 0x0041 }
            return
        L_0x0041:
            r6 = move-exception
            monitor-exit(r5)     // Catch:{ all -> 0x0041 }
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: p015io.reactivex.internal.operators.flowable.FlowableRefCount.cancel(io.reactivex.internal.operators.flowable.FlowableRefCount$RefConnection):void");
    }

    /* access modifiers changed from: 0000 */
    public void terminated(RefConnection refConnection) {
        synchronized (this) {
            if (this.connection != null && this.connection == refConnection) {
                this.connection = null;
                if (refConnection.timer != null) {
                    refConnection.timer.dispose();
                }
            }
            long j = refConnection.subscriberCount - 1;
            refConnection.subscriberCount = j;
            if (j == 0) {
                if (this.source instanceof Disposable) {
                    ((Disposable) this.source).dispose();
                } else if (this.source instanceof ResettableConnectable) {
                    ((ResettableConnectable) this.source).resetIf((Disposable) refConnection.get());
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void timeout(RefConnection refConnection) {
        synchronized (this) {
            if (refConnection.subscriberCount == 0 && refConnection == this.connection) {
                this.connection = null;
                Disposable disposable = (Disposable) refConnection.get();
                DisposableHelper.dispose(refConnection);
                if (this.source instanceof Disposable) {
                    ((Disposable) this.source).dispose();
                } else if (this.source instanceof ResettableConnectable) {
                    if (disposable == null) {
                        refConnection.disconnectedEarly = true;
                    } else {
                        ((ResettableConnectable) this.source).resetIf(disposable);
                    }
                }
            }
        }
    }
}
