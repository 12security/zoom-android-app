package p015io.reactivex.internal.operators.maybe;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import p015io.reactivex.Maybe;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.disposables.Disposables;
import p015io.reactivex.exceptions.Exceptions;

/* renamed from: io.reactivex.internal.operators.maybe.MaybeFromFuture */
public final class MaybeFromFuture<T> extends Maybe<T> {
    final Future<? extends T> future;
    final long timeout;
    final TimeUnit unit;

    public MaybeFromFuture(Future<? extends T> future2, long j, TimeUnit timeUnit) {
        this.future = future2;
        this.timeout = j;
        this.unit = timeUnit;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super T> maybeObserver) {
        Object obj;
        Disposable empty = Disposables.empty();
        maybeObserver.onSubscribe(empty);
        if (!empty.isDisposed()) {
            try {
                if (this.timeout <= 0) {
                    obj = this.future.get();
                } else {
                    obj = this.future.get(this.timeout, this.unit);
                }
                if (!empty.isDisposed()) {
                    if (obj == null) {
                        maybeObserver.onComplete();
                    } else {
                        maybeObserver.onSuccess(obj);
                    }
                }
            } catch (Throwable th) {
                th = th;
                if (th instanceof ExecutionException) {
                    th = th.getCause();
                }
                Exceptions.throwIfFatal(th);
                if (!empty.isDisposed()) {
                    maybeObserver.onError(th);
                }
            }
        }
    }
}
