package p015io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.BooleanSupplier;
import p015io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRepeatUntil */
public final class ObservableRepeatUntil<T> extends AbstractObservableWithUpstream<T, T> {
    final BooleanSupplier until;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRepeatUntil$RepeatUntilObserver */
    static final class RepeatUntilObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> downstream;
        final ObservableSource<? extends T> source;
        final BooleanSupplier stop;
        final SequentialDisposable upstream;

        RepeatUntilObserver(Observer<? super T> observer, BooleanSupplier booleanSupplier, SequentialDisposable sequentialDisposable, ObservableSource<? extends T> observableSource) {
            this.downstream = observer;
            this.upstream = sequentialDisposable;
            this.source = observableSource;
            this.stop = booleanSupplier;
        }

        public void onSubscribe(Disposable disposable) {
            this.upstream.replace(disposable);
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }

        public void onComplete() {
            try {
                if (this.stop.getAsBoolean()) {
                    this.downstream.onComplete();
                } else {
                    subscribeNext();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.downstream.onError(th);
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                do {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                } while (i != 0);
            }
        }
    }

    public ObservableRepeatUntil(Observable<T> observable, BooleanSupplier booleanSupplier) {
        super(observable);
        this.until = booleanSupplier;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        new RepeatUntilObserver(observer, this.until, sequentialDisposable, this.source).subscribeNext();
    }
}
