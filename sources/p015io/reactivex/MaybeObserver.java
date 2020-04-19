package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.MaybeObserver */
public interface MaybeObserver<T> {
    void onComplete();

    void onError(@NonNull Throwable th);

    void onSubscribe(@NonNull Disposable disposable);

    void onSuccess(@NonNull T t);
}
