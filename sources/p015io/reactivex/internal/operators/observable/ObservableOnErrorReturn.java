package p015io.reactivex.internal.operators.observable;

import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.CompositeException;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.disposables.DisposableHelper;

/* renamed from: io.reactivex.internal.operators.observable.ObservableOnErrorReturn */
public final class ObservableOnErrorReturn<T> extends AbstractObservableWithUpstream<T, T> {
    final Function<? super Throwable, ? extends T> valueSupplier;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableOnErrorReturn$OnErrorReturnObserver */
    static final class OnErrorReturnObserver<T> implements Observer<T>, Disposable {
        final Observer<? super T> downstream;
        Disposable upstream;
        final Function<? super Throwable, ? extends T> valueSupplier;

        OnErrorReturnObserver(Observer<? super T> observer, Function<? super Throwable, ? extends T> function) {
            this.downstream = observer;
            this.valueSupplier = function;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.downstream.onSubscribe(this);
            }
        }

        public void dispose() {
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable th) {
            try {
                Object apply = this.valueSupplier.apply(th);
                if (apply == null) {
                    NullPointerException nullPointerException = new NullPointerException("The supplied value is null");
                    nullPointerException.initCause(th);
                    this.downstream.onError(nullPointerException);
                    return;
                }
                this.downstream.onNext(apply);
                this.downstream.onComplete();
            } catch (Throwable th2) {
                Exceptions.throwIfFatal(th2);
                this.downstream.onError(new CompositeException(th, th2));
            }
        }

        public void onComplete() {
            this.downstream.onComplete();
        }
    }

    public ObservableOnErrorReturn(ObservableSource<T> observableSource, Function<? super Throwable, ? extends T> function) {
        super(observableSource);
        this.valueSupplier = function;
    }

    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new OnErrorReturnObserver(observer, this.valueSupplier));
    }
}
