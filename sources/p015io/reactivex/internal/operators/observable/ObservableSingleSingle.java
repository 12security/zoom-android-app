package p015io.reactivex.internal.operators.observable;

import java.util.NoSuchElementException;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableSingleSingle */
public final class ObservableSingleSingle<T> extends Single<T> {
    final T defaultValue;
    final ObservableSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableSingleSingle$SingleElementObserver */
    static final class SingleElementObserver<T> implements Observer<T>, Disposable {
        final T defaultValue;
        boolean done;
        final SingleObserver<? super T> downstream;
        Disposable upstream;
        T value;

        SingleElementObserver(SingleObserver<? super T> singleObserver, T t) {
            this.downstream = singleObserver;
            this.defaultValue = t;
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
            if (!this.done) {
                if (this.value != null) {
                    this.done = true;
                    this.upstream.dispose();
                    this.downstream.onError(new IllegalArgumentException("Sequence contains more than one element!"));
                    return;
                }
                this.value = t;
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.done = true;
            this.downstream.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                T t = this.value;
                this.value = null;
                if (t == null) {
                    t = this.defaultValue;
                }
                if (t != null) {
                    this.downstream.onSuccess(t);
                } else {
                    this.downstream.onError(new NoSuchElementException());
                }
            }
        }
    }

    public ObservableSingleSingle(ObservableSource<? extends T> observableSource, T t) {
        this.source = observableSource;
        this.defaultValue = t;
    }

    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new SingleElementObserver(singleObserver, this.defaultValue));
    }
}
