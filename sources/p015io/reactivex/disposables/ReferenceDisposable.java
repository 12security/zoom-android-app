package p015io.reactivex.disposables;

import java.util.concurrent.atomic.AtomicReference;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.disposables.ReferenceDisposable */
abstract class ReferenceDisposable<T> extends AtomicReference<T> implements Disposable {
    private static final long serialVersionUID = 6537757548749041217L;

    /* access modifiers changed from: protected */
    public abstract void onDisposed(@NonNull T t);

    ReferenceDisposable(T t) {
        super(ObjectHelper.requireNonNull(t, "value is null"));
    }

    public final void dispose() {
        if (get() != null) {
            Object andSet = getAndSet(null);
            if (andSet != null) {
                onDisposed(andSet);
            }
        }
    }

    public final boolean isDisposed() {
        return get() == null;
    }
}
