package p015io.reactivex.internal.operators.observable;

import java.util.concurrent.atomic.AtomicInteger;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.CompositeException;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Predicate;
import p015io.reactivex.internal.disposables.SequentialDisposable;

/* renamed from: io.reactivex.internal.operators.observable.ObservableRetryPredicate */
public final class ObservableRetryPredicate<T> extends AbstractObservableWithUpstream<T, T> {
    final long count;
    final Predicate<? super Throwable> predicate;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableRetryPredicate$RepeatObserver */
    static final class RepeatObserver<T> extends AtomicInteger implements Observer<T> {
        private static final long serialVersionUID = -7098360935104053232L;
        final Observer<? super T> downstream;
        final Predicate<? super Throwable> predicate;
        long remaining;
        final ObservableSource<? extends T> source;
        final SequentialDisposable upstream;

        RepeatObserver(Observer<? super T> observer, long j, Predicate<? super Throwable> predicate2, SequentialDisposable sequentialDisposable, ObservableSource<? extends T> observableSource) {
            this.downstream = observer;
            this.upstream = sequentialDisposable;
            this.source = observableSource;
            this.predicate = predicate2;
            this.remaining = j;
        }

        public void onSubscribe(Disposable disposable) {
            this.upstream.replace(disposable);
        }

        public void onNext(T t) {
            this.downstream.onNext(t);
        }

        public void onError(Throwable th) {
            long j = this.remaining;
            if (j != Long.MAX_VALUE) {
                this.remaining = j - 1;
            }
            if (j == 0) {
                this.downstream.onError(th);
            } else {
                try {
                    if (!this.predicate.test(th)) {
                        this.downstream.onError(th);
                        return;
                    }
                    subscribeNext();
                } catch (Throwable th2) {
                    Exceptions.throwIfFatal(th2);
                    this.downstream.onError(new CompositeException(th, th2));
                }
            }
        }

        public void onComplete() {
            this.downstream.onComplete();
        }

        /* access modifiers changed from: 0000 */
        public void subscribeNext() {
            if (getAndIncrement() == 0) {
                int i = 1;
                while (!this.upstream.isDisposed()) {
                    this.source.subscribe(this);
                    i = addAndGet(-i);
                    if (i == 0) {
                    }
                }
            }
        }
    }

    public ObservableRetryPredicate(Observable<T> observable, long j, Predicate<? super Throwable> predicate2) {
        super(observable);
        this.predicate = predicate2;
        this.count = j;
    }

    public void subscribeActual(Observer<? super T> observer) {
        SequentialDisposable sequentialDisposable = new SequentialDisposable();
        observer.onSubscribe(sequentialDisposable);
        RepeatObserver repeatObserver = new RepeatObserver(observer, this.count, this.predicate, sequentialDisposable, this.source);
        repeatObserver.subscribeNext();
    }
}
