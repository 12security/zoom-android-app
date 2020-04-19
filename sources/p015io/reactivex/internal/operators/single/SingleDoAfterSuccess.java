package p015io.reactivex.internal.operators.single;

import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleSource;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDoAfterSuccess */
public final class SingleDoAfterSuccess<T> extends Single<T> {
    final Consumer<? super T> onAfterSuccess;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoAfterSuccess$DoAfterObserver */
    static final class DoAfterObserver<T> implements SingleObserver<T>, Disposable {
        final SingleObserver<? super T> downstream;
        final Consumer<? super T> onAfterSuccess;
        Disposable upstream;

        DoAfterObserver(SingleObserver<? super T> singleObserver, Consumer<? super T> consumer) {
            this.downstream = singleObserver;
            this.onAfterSuccess = consumer;
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.downstream.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            this.downstream.onSuccess(t);
            try {
                this.onAfterSuccess.accept(t);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                RxJavaPlugins.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }

        public void dispose() {
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }
    }

    public SingleDoAfterSuccess(SingleSource<T> singleSource, Consumer<? super T> consumer) {
        this.source = singleSource;
        this.onAfterSuccess = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new DoAfterObserver(singleObserver, this.onAfterSuccess));
    }
}
