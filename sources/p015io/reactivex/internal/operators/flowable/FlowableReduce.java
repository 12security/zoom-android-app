package p015io.reactivex.internal.operators.flowable;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.BiFunction;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduce */
public final class FlowableReduce<T> extends AbstractFlowableWithUpstream<T, T> {
    final BiFunction<T, T, T> reducer;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableReduce$ReduceSubscriber */
    static final class ReduceSubscriber<T> extends DeferredScalarSubscription<T> implements FlowableSubscriber<T> {
        private static final long serialVersionUID = -4663883003264602070L;
        final BiFunction<T, T, T> reducer;
        Subscription upstream;

        ReduceSubscriber(Subscriber<? super T> subscriber, BiFunction<T, T, T> biFunction) {
            super(subscriber);
            this.reducer = biFunction;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.upstream, subscription)) {
                this.upstream = subscription;
                this.downstream.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (this.upstream != SubscriptionHelper.CANCELLED) {
                Object obj = this.value;
                if (obj == null) {
                    this.value = t;
                } else {
                    try {
                        this.value = ObjectHelper.requireNonNull(this.reducer.apply(obj, t), "The reducer returned a null value");
                    } catch (Throwable th) {
                        Exceptions.throwIfFatal(th);
                        this.upstream.cancel();
                        onError(th);
                    }
                }
            }
        }

        public void onError(Throwable th) {
            if (this.upstream == SubscriptionHelper.CANCELLED) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.upstream = SubscriptionHelper.CANCELLED;
            this.downstream.onError(th);
        }

        public void onComplete() {
            if (this.upstream != SubscriptionHelper.CANCELLED) {
                this.upstream = SubscriptionHelper.CANCELLED;
                Object obj = this.value;
                if (obj != null) {
                    complete(obj);
                } else {
                    this.downstream.onComplete();
                }
            }
        }

        public void cancel() {
            super.cancel();
            this.upstream.cancel();
            this.upstream = SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableReduce(Flowable<T> flowable, BiFunction<T, T, T> biFunction) {
        super(flowable);
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe((FlowableSubscriber<? super T>) new ReduceSubscriber<Object>(subscriber, this.reducer));
    }
}
