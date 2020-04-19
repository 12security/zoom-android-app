package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscriber;
import p015io.reactivex.Flowable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.subscriptions.DeferredScalarSubscription;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableFromCallable */
public final class FlowableFromCallable<T> extends Flowable<T> implements Callable<T> {
    final Callable<? extends T> callable;

    public FlowableFromCallable(Callable<? extends T> callable2) {
        this.callable = callable2;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        DeferredScalarSubscription deferredScalarSubscription = new DeferredScalarSubscription(subscriber);
        subscriber.onSubscribe(deferredScalarSubscription);
        try {
            deferredScalarSubscription.complete(ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value"));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            if (deferredScalarSubscription.isCancelled()) {
                RxJavaPlugins.onError(th);
            } else {
                subscriber.onError(th);
            }
        }
    }

    public T call() throws Exception {
        return ObjectHelper.requireNonNull(this.callable.call(), "The callable returned a null value");
    }
}
