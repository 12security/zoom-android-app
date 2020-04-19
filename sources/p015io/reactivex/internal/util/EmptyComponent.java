package p015io.reactivex.internal.util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.CompletableObserver;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.MaybeObserver;
import p015io.reactivex.Observer;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.util.EmptyComponent */
public enum EmptyComponent implements FlowableSubscriber<Object>, Observer<Object>, MaybeObserver<Object>, SingleObserver<Object>, CompletableObserver, Subscription, Disposable {
    INSTANCE;

    public void cancel() {
    }

    public void dispose() {
    }

    public boolean isDisposed() {
        return true;
    }

    public void onComplete() {
    }

    public void onNext(Object obj) {
    }

    public void onSuccess(Object obj) {
    }

    public void request(long j) {
    }

    public static <T> Subscriber<T> asSubscriber() {
        return INSTANCE;
    }

    public static <T> Observer<T> asObserver() {
        return INSTANCE;
    }

    public void onSubscribe(Disposable disposable) {
        disposable.dispose();
    }

    public void onSubscribe(Subscription subscription) {
        subscription.cancel();
    }

    public void onError(Throwable th) {
        RxJavaPlugins.onError(th);
    }
}
