package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.TimeUnit;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.Scheduler;
import p015io.reactivex.Scheduler.Worker;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.subscribers.SerializedSubscriber;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay */
public final class FlowableDelay<T> extends AbstractFlowableWithUpstream<T, T> {
    final long delay;
    final boolean delayError;
    final Scheduler scheduler;
    final TimeUnit unit;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber */
    static final class DelaySubscriber<T> implements FlowableSubscriber<T>, Subscription {
        final long delay;
        final boolean delayError;
        final Subscriber<? super T> downstream;
        final TimeUnit unit;
        Subscription upstream;

        /* renamed from: w */
        final Worker f384w;

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber$OnComplete */
        final class OnComplete implements Runnable {
            OnComplete() {
            }

            public void run() {
                try {
                    DelaySubscriber.this.downstream.onComplete();
                } finally {
                    DelaySubscriber.this.f384w.dispose();
                }
            }
        }

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber$OnError */
        final class OnError implements Runnable {

            /* renamed from: t */
            private final Throwable f385t;

            OnError(Throwable th) {
                this.f385t = th;
            }

            public void run() {
                try {
                    DelaySubscriber.this.downstream.onError(this.f385t);
                } finally {
                    DelaySubscriber.this.f384w.dispose();
                }
            }
        }

        /* renamed from: io.reactivex.internal.operators.flowable.FlowableDelay$DelaySubscriber$OnNext */
        final class OnNext implements Runnable {

            /* renamed from: t */
            private final T f386t;

            OnNext(T t) {
                this.f386t = t;
            }

            public void run() {
                DelaySubscriber.this.downstream.onNext(this.f386t);
            }
        }

        DelaySubscriber(Subscriber<? super T> subscriber, long j, TimeUnit timeUnit, Worker worker, boolean z) {
            this.downstream = subscriber;
            this.delay = j;
            this.unit = timeUnit;
            this.f384w = worker;
            this.delayError = z;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.upstream, subscription)) {
                this.upstream = subscription;
                this.downstream.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            this.f384w.schedule(new OnNext(t), this.delay, this.unit);
        }

        public void onError(Throwable th) {
            this.f384w.schedule(new OnError(th), this.delayError ? this.delay : 0, this.unit);
        }

        public void onComplete() {
            this.f384w.schedule(new OnComplete(), this.delay, this.unit);
        }

        public void request(long j) {
            this.upstream.request(j);
        }

        public void cancel() {
            this.upstream.cancel();
            this.f384w.dispose();
        }
    }

    public FlowableDelay(Flowable<T> flowable, long j, TimeUnit timeUnit, Scheduler scheduler2, boolean z) {
        super(flowable);
        this.delay = j;
        this.unit = timeUnit;
        this.scheduler = scheduler2;
        this.delayError = z;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        Subscriber<? super T> subscriber2;
        if (this.delayError) {
            subscriber2 = subscriber;
        } else {
            subscriber2 = new SerializedSubscriber<>(subscriber);
        }
        Worker createWorker = this.scheduler.createWorker();
        Flowable flowable = this.source;
        DelaySubscriber delaySubscriber = new DelaySubscriber(subscriber2, this.delay, this.unit, createWorker, this.delayError);
        flowable.subscribe((FlowableSubscriber<? super T>) delaySubscriber);
    }
}
