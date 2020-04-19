package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.SingleObserver */
public interface SingleObserver<T> {
    void onError(@NonNull Throwable th);

    void onSubscribe(@NonNull Disposable disposable);

    void onSuccess(@NonNull T t);
}
