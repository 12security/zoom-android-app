package p015io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRepeat */
public final class ObservableRepeat<T> extends AbstractObservableWithUpstream<T, T> {
    final long count;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRepeat$RepeatObserver */
    static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> downstream;
        long remaining;

        /* renamed from: sd */
        final SequentialDisposable f424sd;
        final ObservableSource<? extends T> source;

        RepeatObserver(Observer<? super T> observer, long j, SequentialDisposable sequentialDisposable, ObservableSource<? extends T> observableSource) {
            this.downstream = observer;
            this.f424sd = sequentialDisposable;
            this.source = observableSource;
            this.remaining = j;
        }

        public void onSubscribe(Disposable disposable) {
            this.f424sd.replace(disposable);
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }

        public void onComplete() {
            long j = this.remaining;
            if (j != Long.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j != 0) {
                subscribeNext();
            } else {
                this.downstream.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.f424sd.isDisposed()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public ObservableRepeat(Observable<T> observable, long j) {
        super(observable);
        this.count = j;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        long j = this.count;
        long j2 = Long.MAX_VALUE;
        if (j != Long.MAX_VALUE) {
            j2 = j - 1;
        }
        RepeatObserver repeatObserver = new RepeatObserver(observer, j2, sequentialDisposable, this.source);
        repeatObserver.subscribeNext();
    }
}
