package p015io.reactivex.internal.operators.single;

import p015io.reactivex.Notification;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.annotations.Experimental;
import p015io.reactivex.internal.operators.mixed.MaterializeSingleObserver;

@Experimental
/* renamed from: io.reactivex.internal.operators.single.SingleMaterialize */
public final class SingleMaterialize<T> extends Single<Notification<T>> {
    final Single<T> source;

    public SingleMaterialize(Single<T> single) {
        this.source = single;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Notification<T>> singleObserver) {
        this.source.subscribe((SingleObserver<? super T>) new MaterializeSingleObserver<Object>(singleObserver));
    }
}
