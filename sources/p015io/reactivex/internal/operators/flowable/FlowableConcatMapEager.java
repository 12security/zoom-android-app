package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.exceptions.MissingBackpressureException;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.queue.SpscLinkedArrayQueue;
import p015io.reactivex.internal.subscribers.InnerQueuedSubscriber;
import p015io.reactivex.internal.subscribers.InnerQueuedSubscriberSupport;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.internal.util.AtomicThrowable;
import p015io.reactivex.internal.util.BackpressureHelper;
import p015io.reactivex.internal.util.ErrorMode;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapEager */
public final class FlowableConcatMapEager<T, R> extends AbstractFlowableWithUpstream<T, R> {
    final ErrorMode errorMode;
    final Function<? super T, ? extends Publisher<? extends R>> mapper;
    final int maxConcurrency;
    final int prefetch;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableConcatMapEager$ConcatMapEagerDelayErrorSubscriber */
    static final class ConcatMapEagerDelayErrorSubscriber<T, R> extends AtomicInteger implements FlowableSubscriber<T>, Subscription, InnerQueuedSubscriberSupport<R> {
        private static final long serialVersionUID = -4255299542215038287L;
        volatile boolean cancelled;
        volatile InnerQueuedSubscriber<R> current;
        volatile boolean done;
        final Subscriber<? super R> downstream;
        final ErrorMode errorMode;
        final AtomicThrowable errors = new AtomicThrowable();
        final Function<? super T, ? extends Publisher<? extends R>> mapper;
        final int maxConcurrency;
        final int prefetch;
        final AtomicLong requested = new AtomicLong();
        final SpscLinkedArrayQueue<InnerQueuedSubscriber<R>> subscribers;
        Subscription upstream;

        ConcatMapEagerDelayErrorSubscriber(Subscriber<? super R> subscriber, Function<? super T, ? extends Publisher<? extends R>> function, int i, int i2, ErrorMode errorMode2) {
            this.downstream = subscriber;
            this.mapper = function;
            this.maxConcurrency = i;
            this.prefetch = i2;
            this.errorMode = errorMode2;
            this.subscribers = new SpscLinkedArrayQueue<>(Math.min(i2, i));
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.upstream, subscription)) {
                this.upstream = subscription;
                this.downstream.onSubscribe(this);
                int i = this.maxConcurrency;
                subscription.request(i == Integer.MAX_VALUE ? Long.MAX_VALUE : (long) i);
            }
        }

        public void onNext(T t) {
            try {
                Publisher publisher = (Publisher) ObjectHelper.requireNonNull(this.mapper.apply(t), "The mapper returned a null Publisher");
                InnerQueuedSubscriber innerQueuedSubscriber = new InnerQueuedSubscriber(this, this.prefetch);
                if (!this.cancelled) {
                    this.subscribers.offer(innerQueuedSubscriber);
                    publisher.subscribe(innerQueuedSubscriber);
                    if (this.cancelled) {
                        innerQueuedSubscriber.cancel();
                        drainAndCancel();
                    }
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.upstream.cancel();
                onError(th);
            }
        }

        public void onError(Throwable th) {
            if (this.errors.addThrowable(th)) {
                this.done = true;
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        public void cancel() {
            if (!this.cancelled) {
                this.cancelled = true;
                this.upstream.cancel();
                drainAndCancel();
            }
        }

        /* access modifiers changed from: 0000 */
        public void drainAndCancel() {
            if (getAndIncrement() == 0) {
                do {
                    cancelAll();
                } while (decrementAndGet() != 0);
            }
        }

        /* access modifiers changed from: 0000 */
        public void cancelAll() {
            while (true) {
                InnerQueuedSubscriber innerQueuedSubscriber = (InnerQueuedSubscriber) this.subscribers.poll();
                if (innerQueuedSubscriber != null) {
                    innerQueuedSubscriber.cancel();
                } else {
                    return;
                }
            }
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.add(this.requested, j);
                drain();
            }
        }

        public void innerNext(InnerQueuedSubscriber<R> innerQueuedSubscriber, R r) {
            if (innerQueuedSubscriber.queue().offer(r)) {
                drain();
                return;
            }
            innerQueuedSubscriber.cancel();
            innerError(innerQueuedSubscriber, new MissingBackpressureException());
        }

        public void innerError(InnerQueuedSubscriber<R> innerQueuedSubscriber, Throwable th) {
            if (this.errors.addThrowable(th)) {
                innerQueuedSubscriber.setDone();
                if (this.errorMode != ErrorMode.END) {
                    this.upstream.cancel();
                }
                drain();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void innerComplete(InnerQueuedSubscriber<R> innerQueuedSubscriber) {
            innerQueuedSubscriber.setDone();
            drain();
        }

        /* JADX WARNING: Removed duplicated region for block: B:80:0x0130  */
        /* JADX WARNING: Removed duplicated region for block: B:81:0x0134  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void drain() {
            /*
                r20 = this;
                r1 = r20
                int r0 = r20.getAndIncrement()
                if (r0 == 0) goto L_0x0009
                return
            L_0x0009:
                io.reactivex.internal.subscribers.InnerQueuedSubscriber<R> r0 = r1.current
                org.reactivestreams.Subscriber<? super R> r2 = r1.downstream
                io.reactivex.internal.util.ErrorMode r3 = r1.errorMode
                r5 = 1
            L_0x0010:
                java.util.concurrent.atomic.AtomicLong r6 = r1.requested
                long r6 = r6.get()
                if (r0 != 0) goto L_0x0056
                io.reactivex.internal.util.ErrorMode r0 = p015io.reactivex.internal.util.ErrorMode.END
                if (r3 == r0) goto L_0x0033
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Object r0 = r0.get()
                java.lang.Throwable r0 = (java.lang.Throwable) r0
                if (r0 == 0) goto L_0x0033
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Throwable r0 = r0.terminate()
                r2.onError(r0)
                return
            L_0x0033:
                boolean r0 = r1.done
                io.reactivex.internal.queue.SpscLinkedArrayQueue<io.reactivex.internal.subscribers.InnerQueuedSubscriber<R>> r8 = r1.subscribers
                java.lang.Object r8 = r8.poll()
                io.reactivex.internal.subscribers.InnerQueuedSubscriber r8 = (p015io.reactivex.internal.subscribers.InnerQueuedSubscriber) r8
                if (r0 == 0) goto L_0x0051
                if (r8 != 0) goto L_0x0051
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Throwable r0 = r0.terminate()
                if (r0 == 0) goto L_0x004d
                r2.onError(r0)
                goto L_0x0050
            L_0x004d:
                r2.onComplete()
            L_0x0050:
                return
            L_0x0051:
                if (r8 == 0) goto L_0x0057
                r1.current = r8
                goto L_0x0057
            L_0x0056:
                r8 = r0
            L_0x0057:
                r9 = 0
                r11 = 0
                if (r8 == 0) goto L_0x0115
                io.reactivex.internal.fuseable.SimpleQueue r12 = r8.queue()
                if (r12 == 0) goto L_0x0112
                r16 = r5
                r13 = r9
            L_0x0065:
                r4 = 1
                int r17 = (r13 > r6 ? 1 : (r13 == r6 ? 0 : -1))
                if (r17 == 0) goto L_0x00cd
                boolean r0 = r1.cancelled
                if (r0 == 0) goto L_0x0073
                r20.cancelAll()
                return
            L_0x0073:
                io.reactivex.internal.util.ErrorMode r0 = p015io.reactivex.internal.util.ErrorMode.IMMEDIATE
                if (r3 != r0) goto L_0x0093
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Object r0 = r0.get()
                java.lang.Throwable r0 = (java.lang.Throwable) r0
                if (r0 == 0) goto L_0x0093
                r1.current = r11
                r8.cancel()
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Throwable r0 = r0.terminate()
                r2.onError(r0)
                return
            L_0x0093:
                boolean r0 = r8.isDone()
                java.lang.Object r15 = r12.poll()     // Catch:{ Throwable -> 0x00bc }
                if (r15 != 0) goto L_0x00a0
                r19 = 1
                goto L_0x00a2
            L_0x00a0:
                r19 = 0
            L_0x00a2:
                if (r0 == 0) goto L_0x00b1
                if (r19 == 0) goto L_0x00b1
                r1.current = r11
                org.reactivestreams.Subscription r0 = r1.upstream
                r0.request(r4)
                r8 = r11
                r18 = 1
                goto L_0x00cf
            L_0x00b1:
                if (r19 == 0) goto L_0x00b4
                goto L_0x00cd
            L_0x00b4:
                r2.onNext(r15)
                long r13 = r13 + r4
                r8.requestOne()
                goto L_0x0065
            L_0x00bc:
                r0 = move-exception
                r3 = r0
                p015io.reactivex.exceptions.Exceptions.throwIfFatal(r3)
                r1.current = r11
                r8.cancel()
                r20.cancelAll()
                r2.onError(r3)
                return
            L_0x00cd:
                r18 = 0
            L_0x00cf:
                if (r17 != 0) goto L_0x0110
                boolean r0 = r1.cancelled
                if (r0 == 0) goto L_0x00d9
                r20.cancelAll()
                return
            L_0x00d9:
                io.reactivex.internal.util.ErrorMode r0 = p015io.reactivex.internal.util.ErrorMode.IMMEDIATE
                if (r3 != r0) goto L_0x00f9
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Object r0 = r0.get()
                java.lang.Throwable r0 = (java.lang.Throwable) r0
                if (r0 == 0) goto L_0x00f9
                r1.current = r11
                r8.cancel()
                r20.cancelAll()
                io.reactivex.internal.util.AtomicThrowable r0 = r1.errors
                java.lang.Throwable r0 = r0.terminate()
                r2.onError(r0)
                return
            L_0x00f9:
                boolean r0 = r8.isDone()
                boolean r12 = r12.isEmpty()
                if (r0 == 0) goto L_0x0110
                if (r12 == 0) goto L_0x0110
                r1.current = r11
                org.reactivestreams.Subscription r0 = r1.upstream
                r0.request(r4)
                r0 = r11
                r18 = 1
                goto L_0x011b
            L_0x0110:
                r0 = r8
                goto L_0x011b
            L_0x0112:
                r16 = r5
                goto L_0x0117
            L_0x0115:
                r16 = r5
            L_0x0117:
                r0 = r8
                r13 = r9
                r18 = 0
            L_0x011b:
                int r4 = (r13 > r9 ? 1 : (r13 == r9 ? 0 : -1))
                if (r4 == 0) goto L_0x012e
                r4 = 9223372036854775807(0x7fffffffffffffff, double:NaN)
                int r4 = (r6 > r4 ? 1 : (r6 == r4 ? 0 : -1))
                if (r4 == 0) goto L_0x012e
                java.util.concurrent.atomic.AtomicLong r4 = r1.requested
                long r5 = -r13
                r4.addAndGet(r5)
            L_0x012e:
                if (r18 == 0) goto L_0x0134
                r5 = r16
                goto L_0x0010
            L_0x0134:
                r4 = r16
                int r4 = -r4
                int r5 = r1.addAndGet(r4)
                if (r5 != 0) goto L_0x0010
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: p015io.reactivex.internal.operators.flowable.FlowableConcatMapEager.ConcatMapEagerDelayErrorSubscriber.drain():void");
        }
    }

    public FlowableConcatMapEager(Flowable<T> flowable, Function<? super T, ? extends Publisher<? extends R>> function, int i, int i2, ErrorMode errorMode2) {
        super(flowable);
        this.mapper = function;
        this.maxConcurrency = i;
        this.prefetch = i2;
        this.errorMode = errorMode2;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super R> subscriber) {
        Flowable flowable = this.source;
        ConcatMapEagerDelayErrorSubscriber concatMapEagerDelayErrorSubscriber = new ConcatMapEagerDelayErrorSubscriber(subscriber, this.mapper, this.maxConcurrency, this.prefetch, this.errorMode);
        flowable.subscribe((FlowableSubscriber<? super T>) concatMapEagerDelayErrorSubscriber);
    }
}
