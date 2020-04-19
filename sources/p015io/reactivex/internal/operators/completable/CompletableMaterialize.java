package p015io.reactivex.internal.operators.completable;

import p015io.reactivex.Completable;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.Notification;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.annotations.Experimental;
import p015io.reactivex.internal.operators.mixed.MaterializeSingleObserver;

@Experimental
/* renamed from: io.reactivex.internal.operators.completable.CompletableMaterialize */
public final class CompletableMaterialize<T> extends Single<Notification<T>> {
    final Completable source;

    public CompletableMaterialize(Completable completable) {
        this.source = completable;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Notification<T>> singleObserver) {
        this.source.subscribe((CompletableObserver) new MaterializeSingleObserver(singleObserver));
    }
}
