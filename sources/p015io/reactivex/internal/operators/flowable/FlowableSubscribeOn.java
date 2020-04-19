package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.Scheduler;
import p015io.reactivex.Scheduler.Worker;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.internal.util.BackpressureHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableSubscribeOn */
public final class FlowableSubscribeOn<T> extends AbstractFlowableWithUpstream<T, T> {
    final boolean nonScheduledRequests;
    final Scheduler scheduler;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableSubscribeOn$SubscribeOnSubscriber */
    static final class SubscribeOnSubscriber<T> extends AtomicReference<Thread> implements FlowableSubscriber<T>, Subscription, Runnable {
        private static final long serialVersionUID = 8094547886072529208L;
        final Subscriber<? super T> downstream;
        final boolean nonScheduledRequests;
        final AtomicLong requested = new AtomicLong();
        Publisher<T> source;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();
        final Worker worker;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableSubscribeOn$SubscribeOnSubscriber$Request */
        static final class Request implements Runnable {

            /* renamed from: n */
            final long f404n;
            final Subscription upstream;

            Request(Subscription subscription, long j) {
                this.upstream = subscription;
                this.f404n = j;
            }

            public void run() {
                this.upstream.request(this.f404n);
            }
        }

        SubscribeOnSubscriber(Subscriber<? super T> subscriber, Worker worker2, Publisher<T> publisher, boolean z) {
            this.downstream = subscriber;
            this.worker = worker2;
            this.source = publisher;
            this.nonScheduledRequests = !z;
        }

        public void run() {
            lazySet(Thread.currentThread());
            Publisher<T> publisher = this.source;
            this.source = null;
            publisher.subscribe(this);
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.upstream, subscription)) {
                long andSet = this.requested.getAndSet(0);
                if (andSet != 0) {
                    requestUpstream(andSet, subscription);
                }
            }
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
            this.worker.dispose();
        }

        public void onComplete() {
            this.downstream.onComplete();
            this.worker.dispose();
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                Subscription subscription = (Subscription) this.upstream.get();
                if (subscription != null) {
                    requestUpstream(j, subscription);
                    return;
                }
                BackpressureHelper.add(this.requested, j);
                Subscription subscription2 = (Subscription) this.upstream.get();
                if (subscription2 != null) {
                    long andSet = this.requested.getAndSet(0);
                    if (andSet != 0) {
                        requestUpstream(andSet, subscription2);
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void requestUpstream(long j, Subscription subscription) {
            if (this.nonScheduledRequests || Thread.currentThread() == get()) {
                subscription.request(j);
            } else {
                this.worker.schedule(new Request(subscription, j));
            }
        }

        public void cancel() {
            SubscriptionHelper.cancel(this.upstream);
            this.worker.dispose();
        }
    }

    public FlowableSubscribeOn(Flowable<T> flowable, Scheduler scheduler2, boolean z) {
        super(flowable);
        this.scheduler = scheduler2;
        this.nonScheduledRequests = z;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        Worker createWorker = this.scheduler.createWorker();
        SubscribeOnSubscriber subscribeOnSubscriber = new SubscribeOnSubscriber(subscriber, createWorker, this.source, this.nonScheduledRequests);
        subscriber.onSubscribe(subscribeOnSubscriber);
        createWorker.schedule(subscribeOnSubscriber);
    }
}
