package p015io.reactivex.internal.operators.observable;

import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.ArrayCompositeDisposable;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.observers.SerializedObserver;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil */
public final class ObservableSkipUntil<T, U> extends AbstractObservableWithUpstream<T, T> {
    final ObservableSource<U> other;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil$SkipUntil */
    final class SkipUntil implements Observer<U> {
        final ArrayCompositeDisposable frc;
        final SerializedObserver<T> serial;
        final SkipUntilObserver<T> sus;
        Disposable upstream;

        SkipUntil(ArrayCompositeDisposable arrayCompositeDisposable, SkipUntilObserver<T> skipUntilObserver, SerializedObserver<T> serializedObserver) {
            this.frc = arrayCompositeDisposable;
            this.sus = skipUntilObserver;
            this.serial = serializedObserver;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.frc.setResource(1, disposable);
            }
        }

        public void onNext(U u) {
            this.upstream.dispose();
            this.sus.notSkipping = true;
        }

        public void onError(Throwable th) {
            this.frc.dispose();
            this.serial.onError(th);
        }

        public void onComplete() {
            this.sus.notSkipping = true;
        }
    }

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSkipUntil$SkipUntilObserver */
    static final class SkipUntilObserver<T> implements Observer<T> {
        final Observer<? super T> downstream;
        final ArrayCompositeDisposable frc;
        volatile boolean notSkipping;
        boolean notSkippingLocal;
        Disposable upstream;

        SkipUntilObserver(Observer<? super T> observer, ArrayCompositeDisposable arrayCompositeDisposable) {
            this.downstream = observer;
            this.frc = arrayCompositeDisposable;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.frc.setResource(0, disposable);
            }
        }

        public void onNext(T t) {
            if (this.notSkippingLocal) {
                this.downstream.onNext(t);
            } else if (this.notSkipping) {
                this.notSkippingLocal = true;
                this.downstream.onNext(t);
            }
        }

        public void onError(Throwable th) {
            this.frc.dispose();
            this.downstream.onError(th);
        }

        public void onComplete() {
            this.frc.dispose();
            this.downstream.onComplete();
        }
    }

    public ObservableSkipUntil(ObservableSource<T> observableSource, ObservableSource<U> observableSource2) {
        super(observableSource);
        this.other = observableSource2;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SerializedObserver serializedObserver = new SerializedObserver(observer);
        ArrayCompositeDisposable arrayCompositeDisposable = new ArrayCompositeDisposable(2);
        serializedObserver.onSubscribe(arrayCompositeDisposable);
        SkipUntilObserver skipUntilObserver = new SkipUntilObserver(serializedObserver, arrayCompositeDisposable);
        this.other.subscribe(new SkipUntil(arrayCompositeDisposable, skipUntilObserver, serializedObserver));
        this.source.subscribe(skipUntilObserver);
    }
}
