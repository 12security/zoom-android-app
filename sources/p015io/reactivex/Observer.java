package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.disposables.Disposable;

/* renamed from: io.reactivex.Observer */
public interface Observer<T> {
    void onComplete();

    void onError(@NonNull Throwable th);

    void onNext(@NonNull T t);

    void onSubscribe(@NonNull Disposable disposable);
}
