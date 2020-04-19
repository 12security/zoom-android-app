package p015io.reactivex.observers;

import java.util.concurrent.atomic.AtomicReference;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.util.EndConsumerHelper;

/* renamed from: io.reactivex.observers.DisposableSingleObserver */
public abstract class DisposableSingleObserver<T> implements SingleObserver<T>, Disposable {
    final AtomicReference<Disposable> upstream = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    public final void onSubscribe(@NonNull Disposable disposable) {
        if (EndConsumerHelper.setOnce(this.upstream, disposable, getClass())) {
            onStart();
        }
    }

    public final boolean isDisposed() {
        return this.upstream.get() == DisposableHelper.DISPOSED;
    }

    public final void dispose() {
        DisposableHelper.dispose(this.upstream);
    }
}
