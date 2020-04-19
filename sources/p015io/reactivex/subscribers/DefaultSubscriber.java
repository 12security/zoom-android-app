package p015io.reactivex.subscribers;

import org.reactivestreams.Subscription;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.subscribers.DefaultSubscriber */
public abstract class DefaultSubscriber<T> implements FlowableSubscriber<T> {
    Subscription upstream;

    public final void onSubscribe(Subscription subscription) {
        if (EndConsumerHelper.validate(this.upstream, subscription, getClass())) {
            this.upstream = subscription;
            onStart();
        }
    }

    /* access modifiers changed from: protected */
    public final void request(long j) {
        Subscription subscription = this.upstream;
        if (subscription != null) {
            subscription.request(j);
        }
    }

    /* access modifiers changed from: protected */
    public final void cancel() {
        Subscription subscription = this.upstream;
        this.upstream = SubscriptionHelper.CANCELLED;
        subscription.cancel();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        request(Long.MAX_VALUE);
    }
}
