package p015io.reactivex.internal.operators.observable;

import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableTake */
public final class ObservableTake<T> extends AbstractObservableWithUpstream<T, T> {
    final long limit;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableTake$TakeObserver */
    static final class TakeObserver<T> implements Observer<T>, Disposable {
        boolean done;
        final Observer<? super T> downstream;
        long remaining;
        Disposable upstream;

        TakeObserver(Observer<? super T> observer, long j) {
            this.downstream = observer;
            this.remaining = j;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                if (this.remaining == 0) {
                    this.done = true;
                    disposable.dispose();
                    EmptyDisposable.complete(this.downstream);
                    return;
                }
                this.downstream.onSubscribe(this);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                long j = this.remaining;
                this.remaining = j - 1;
                if (j > 0) {
                    boolean z = this.remaining == 0;
                    this.downstream.onNext(t);
                    if (z) {
                        onComplete();
                    }
                }
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.upstream.dispose();
            this.downstream.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.upstream.dispose();
                this.downstream.onComplete();
            }
        }

        public void dispose() {
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }
    }

    public ObservableTake(ObservableSource<T> observableSource, long j) {
        super(observableSource);
        this.limit = j;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Observer<? super T> observer) {
        this.source.subscribe(new TakeObserver(observer, this.limit));
    }
}
