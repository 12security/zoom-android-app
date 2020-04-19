package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.annotations.Nullable;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.functions.Cancellable;

/* renamed from: io.reactivex.FlowableEmitter */
public interface FlowableEmitter<T> extends Emitter<T> {
    boolean isCancelled();

    long requested();

    @NonNull
    FlowableEmitter<T> serialize();

    void setCancellable(@Nullable Cancellable cancellable);

    void setDisposable(@Nullable Disposable disposable);

    boolean tryOnError(@NonNull Throwable th);
}
