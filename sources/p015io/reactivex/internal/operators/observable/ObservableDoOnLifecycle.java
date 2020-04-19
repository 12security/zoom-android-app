package p015io.reactivex.internal.operators.observable;

import p015io.reactivex.Observable;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.functions.Action;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.observers.DisposableLambdaObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableDoOnLifecycle */
public final class ObservableDoOnLifecycle<T> extends AbstractObservableWithUpstream<T, T> {
    private final Action onDispose;
    private final Consumer<? super Disposable> onSubscribe;

    public ObservableDoOnLifecycle(Observable<T> observable, Consumer<? super Disposable> consumer, Action action) {
        super(observable);
        this.onSubscribe = consumer;
        this.onDispose = action;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new DisposableLambdaObserver(observer, this.onSubscribe, this.onDispose));
    }
}
