package p015io.reactivex.internal.operators.mixed;

import p015io.reactivex.CompletableObserver;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.Notification;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.annotations.Experimental;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;

@Experimental
/* renamed from: io.reactivex.internal.operators.mixed.MaterializeSingleObserver */
public final class MaterializeSingleObserver<T> implements SingleObserver<T>, MaybeObserver<T>, CompletableObserver, Disposable {
    final SingleObserver<? super Notification<T>> downstream;
    Disposable upstream;

    public MaterializeSingleObserver(SingleObserver<? super Notification<T>> singleObserver) {
        this.downstream = singleObserver;
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.upstream, disposable)) {
            this.upstream = disposable;
            this.downstream.onSubscribe(this);
        }
    }

    public void onComplete() {
        this.downstream.onSuccess(Notification.createOnComplete());
    }

    public void onSuccess(T t) {
        this.downstream.onSuccess(Notification.createOnNext(t));
    }

    public void onError(Throwable th) {
        this.downstream.onSuccess(Notification.createOnError(th));
    }

    public boolean isDisposed() {
        return this.upstream.isDisposed();
    }

    public void dispose() {
        this.upstream.dispose();
    }
}
