package p015io.reactivex.internal.operators.maybe;

import p015io.reactivex.Maybe;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.Notification;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.annotations.Experimental;
import p015io.reactivex.internal.operators.mixed.MaterializeSingleObserver;

@Experimental
/* renamed from: io.reactivex.internal.operators.maybe.MaybeMaterialize */
public final class MaybeMaterialize<T> extends Single<Notification<T>> {
    final Maybe<T> source;

    public MaybeMaterialize(Maybe<T> maybe) {
        this.source = maybe;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super Notification<T>> singleObserver) {
        this.source.subscribe((MaybeObserver<? super T>) new MaterializeSingleObserver<Object>(singleObserver));
    }
}
