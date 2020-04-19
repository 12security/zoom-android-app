package p015io.reactivex.internal.operators.single;

import p015io.reactivex.Maybe;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.Notification;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.annotations.Experimental;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Function;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.functions.ObjectHelper;

@Experimental
/* renamed from: io.reactivex.internal.operators.single.SingleDematerialize */
public final class SingleDematerialize<T, R> extends Maybe<R> {
    final Function<? super T, Notification<R>> selector;
    final Single<T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleDematerialize$DematerializeObserver */
    static final class DematerializeObserver<T, R> implements SingleObserver<T>, Disposable {
        final MaybeObserver<? super R> downstream;
        final Function<? super T, Notification<R>> selector;
        Disposable upstream;

        DematerializeObserver(MaybeObserver<? super R> maybeObserver, Function<? super T, Notification<R>> function) {
            this.downstream = maybeObserver;
            this.selector = function;
        }

        public void dispose() {
            this.upstream.dispose();
        }

        public boolean isDisposed() {
            return this.upstream.isDisposed();
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.downstream.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            try {
                Notification notification = (Notification) ObjectHelper.requireNonNull(this.selector.apply(t), "The selector returned a null Notification");
                if (notification.isOnNext()) {
                    this.downstream.onSuccess(notification.getValue());
                } else if (notification.isOnComplete()) {
                    this.downstream.onComplete();
                } else {
                    this.downstream.onError(notification.getError());
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                this.downstream.onError(th);
            }
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }
    }

    public SingleDematerialize(Single<T> single, Function<? super T, Notification<R>> function) {
        this.source = single;
        this.selector = function;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(MaybeObserver<? super R> maybeObserver) {
        this.source.subscribe((SingleObserver<? super T>) new DematerializeObserver<Object>(maybeObserver, this.selector));
    }
}
