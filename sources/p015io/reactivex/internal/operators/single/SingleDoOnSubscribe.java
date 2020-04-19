package p015io.reactivex.internal.operators.single;

import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleSource;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.single.SingleDoOnSubscribe */
public final class SingleDoOnSubscribe<T> extends Single<T> {
    final Consumer<? super Disposable> onSubscribe;
    final SingleSource<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDoOnSubscribe$DoOnSubscribeSingleObserver */
    static final class DoOnSubscribeSingleObserver<T> implements SingleObserver<T> {
        boolean done;
        final SingleObserver<? super T> downstream;
        final Consumer<? super Disposable> onSubscribe;

        DoOnSubscribeSingleObserver(SingleObserver<? super T> singleObserver, Consumer<? super Disposable> consumer) {
            this.downstream = singleObserver;
            this.onSubscribe = consumer;
        }

        public void onSubscribe(Disposable disposable) {
            try {
                this.onSubscribe.accept(disposable);
                this.downstream.onSubscribe(disposable);
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.done = true;
                disposable.dispose();
                EmptyDisposable.error(th, this.downstream);
            }
        }

        public void onSuccess(T t) {
            if (!this.done) {
                this.downstream.onSuccess(t);
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
            } else {
                this.downstream.onError(th);
            }
        }
    }

    public SingleDoOnSubscribe(SingleSource<T> singleSource, Consumer<? super Disposable> consumer) {
        this.source = singleSource;
        this.onSubscribe = consumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super T> singleObserver) {
        this.source.subscribe(new DoOnSubscribeSingleObserver(singleObserver, this.onSubscribe));
    }
}
