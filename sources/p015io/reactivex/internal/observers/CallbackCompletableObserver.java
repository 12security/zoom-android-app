package p015io.reactivex.internal.observers;

import java.util.concurrent.atomic.AtomicReference;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.exceptions.OnErrorNotImplementedException;
import p015io.reactivex.functions.Action;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.observers.LambdaConsumerIntrospection;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.observers.CallbackCompletableObserver */
public final class CallbackCompletableObserver extends AtomicReference<Disposable> implements CompletableObserver, Disposable, Consumer<Throwable>, LambdaConsumerIntrospection {
    private static final long serialVersionUID = -4361286194466301354L;
    final Action onComplete;
    final Consumer<? super Throwable> onError;

    public CallbackCompletableObserver(Action action) {
        this.onError = this;
        this.onComplete = action;
    }

    public CallbackCompletableObserver(Consumer<? super Throwable> consumer, Action action) {
        this.onError = consumer;
        this.onComplete = action;
    }

    public void accept(Throwable th) {
        RxJavaPlugins.onError(new OnErrorNotImplementedException(th));
    }

    public void onComplete() {
        try {
            this.onComplete.run();
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            RxJavaPlugins.onError(th);
        }
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onError(Throwable th) {
        try {
            this.onError.accept(th);
        } catch (Throwable th2) {
            Exceptions.throwIfFatal(th2);
            RxJavaPlugins.onError(th2);
        }
        lazySet(DisposableHelper.DISPOSED);
    }

    public void onSubscribe(Disposable disposable) {
        DisposableHelper.setOnce(this, disposable);
    }

    public void dispose() {
        DisposableHelper.dispose(this);
    }

    public boolean isDisposed() {
        return get() == DisposableHelper.DISPOSED;
    }

    public boolean hasCustomOnError() {
        return this.onError != this;
    }
}
