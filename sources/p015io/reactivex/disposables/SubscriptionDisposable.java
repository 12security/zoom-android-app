package p015io.reactivex.disposables;

import org.reactivestreams.Subscription;
import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.disposables.SubscriptionDisposable */
final class SubscriptionDisposable extends ReferenceDisposable<Subscription> {
    private static final long serialVersionUID = -707001650852963139L;

    SubscriptionDisposable(Subscription subscription) {
        super(subscription);
    }

    /* access modifiers changed from: protected */
    public void onDisposed(@NonNull Subscription subscription) {
        subscription.cancel();
    }
}
