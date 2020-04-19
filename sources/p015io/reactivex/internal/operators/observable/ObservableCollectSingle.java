package p015io.reactivex.internal.operators.observable;

import java.util.concurrent.Callable;
import p015io.reactivex.Observable;
import p015io.reactivex.ObservableSource;
import p015io.reactivex.Observer;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.functions.BiConsumer;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.fuseable.FuseToObservable;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.observable.ObservableCollectSingle */
public final class ObservableCollectSingle<T, U> extends Single<U> implements FuseToObservable<U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;
    final ObservableSource<T> source;

    /* renamed from: io.reactivex.internal.operators.observable.ObservableCollectSingle$CollectObserver */
    static final class CollectObserver<T, U> implements Observer<T>, Disposable {
        final BiConsumer<? super U, ? super T> collector;
        boolean done;
        final SingleObserver<? super U> downstream;

        /* renamed from: u */
        final U f416u;
        Disposable upstream;

        CollectObserver(SingleObserver<? super U> singleObserver, U u, BiConsumer<? super U, ? super T> biConsumer) {
            this.downstream = singleObserver;
            this.collector = biConsumer;
            this.f416u = u;
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
                try {
                    this.collector.accept(this.f416u, t);
                } catch (Throwable th) {
                    this.upstream.dispose();
                    onError(th);
                }
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
                this.downstream.onSuccess(this.f416u);
            }
        }
    }

    public ObservableCollectSingle(ObservableSource<T> observableSource, Callable<? extends U> callable, BiConsumer<? super U, ? super T> biConsumer) {
        this.source = observableSource;
        this.initialSupplier = callable;
        this.collector = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> singleObserver) {
        try {
            this.source.subscribe(new CollectObserver(singleObserver, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.collector));
        } catch (Throwable th) {
            EmptyDisposable.error(th, singleObserver);
        }
    }

    public Observable<U> fuseToObservable() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableCollect<T>(this.source, this.initialSupplier, this.collector));
    }
}
