package p015io.reactivex.internal.observers;

import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.observers.DeferredScalarObserver */
public abstract class DeferredScalarObserver<T, R> extends DeferredScalarDisposable<R> implements Observer<T> {
    private static final long serialVersionUID = -266195175408988651L;
    protected Disposable upstream;

    public DeferredScalarObserver(Observer<? super R> observer) {
        super(observer);
    }

    public void onSubscribe(Disposable disposable) {
        if (DisposableHelper.validate(this.upstream, disposable)) {
            this.upstream = disposable;
            this.downstream.onSubscribe(this);
        }
    }

    public void onError(Throwable th) {
        this.value = null;
        error(th);
    }

    public void onComplete() {
        Object obj = this.value;
        if (obj != null) {
            this.value = null;
            complete(obj);
            return;
        }
        complete();
    }

    public void dispose() {
        super.dispose();
        this.upstream.dispose();
    }
}
