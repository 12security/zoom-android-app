package p015io.reactivex.internal.operators.single;

import org.reactivestreams.Subscriber;
import p015io.reactivex.Flowable;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.SingleSource;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.internal.disposables.DisposableHelper;
import p015io.reactivex.internal.subscriptions.DeferredScalarSubscription;

/* renamed from: io.reactivex.internal.operators.single.SingleToFlowable */
public final class SingleToFlowable<T> extends Flowable<T> {
    final SingleSource<? extends T> source;

    /* renamed from: io.reactivex.internal.operators.single.SingleToFlowable$SingleToFlowableObserver */
    static final class SingleToFlowableObserver<T> extends DeferredScalarSubscription<T> implements SingleObserver<T> {
        private static final long serialVersionUID = 187782011903685568L;
        Disposable upstream;

        SingleToFlowableObserver(Subscriber<? super T> subscriber) {
            super(subscriber);
        }

        public void onSubscribe(Disposable disposable) {
            if (DisposableHelper.validate(this.upstream, disposable)) {
                this.upstream = disposable;
                this.downstream.onSubscribe(this);
            }
        }

        public void onSuccess(T t) {
            complete(t);
        }

        public void onError(Throwable th) {
            this.downstream.onError(th);
        }

        public void cancel() {
            super.cancel();
            this.upstream.dispose();
        }
    }

    public SingleToFlowable(SingleSource<? extends T> singleSource) {
        this.source = singleSource;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.source.subscribe(new SingleToFlowableObserver(subscriber));
    }
}
