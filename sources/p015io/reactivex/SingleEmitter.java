package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.annotations.Nullable;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.functions.Cancellable;

/* renamed from: io.reactivex.SingleEmitter */
public interface SingleEmitter<T> {
    boolean isDisposed();

    void onError(@NonNull Throwable th);

    void onSuccess(@NonNull T t);

    void setCancellable(@Nullable Cancellable cancellable);

    void setDisposable(@Nullable Disposable disposable);

    boolean tryOnError(@NonNull Throwable th);
}
