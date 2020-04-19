package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.FlowableSubscriber;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.BiConsumer;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.fuseable.FuseToFlowable;
import p015io.reactivex.internal.subscriptions.SubscriptionHelper;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableCollectSingle */
public final class FlowableCollectSingle<T, U> extends Single<U> implements FuseToFlowable<U> {
    final BiConsumer<? super U, ? super T> collector;
    final Callable<? extends U> initialSupplier;
    final Flowable<T> source;

    /* renamed from: io.reactivex.internal.operators.flowable.FlowableCollectSingle$CollectSubscriber */
    static final class CollectSubscriber<T, U> implements FlowableSubscriber<T>, Disposable {
        final BiConsumer<? super U, ? super T> collector;
        boolean done;
        final SingleObserver<? super U> downstream;

        /* renamed from: u */
        final U f383u;
        Subscription upstream;

        CollectSubscriber(SingleObserver<? super U> singleObserver, U u, BiConsumer<? super U, ? super T> biConsumer) {
            this.downstream = singleObserver;
            this.collector = biConsumer;
            this.f383u = u;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.validate(this.upstream, subscription)) {
                this.upstream = subscription;
                this.downstream.onSubscribe(this);
                subscription.request(Long.MAX_VALUE);
            }
        }

        public void onNext(T t) {
            if (!this.done) {
                try {
                    this.collector.accept(this.f383u, t);
                } catch (Throwable th) {
                    Exceptions.throwIfFatal(th);
                    this.upstream.cancel();
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
            this.upstream = SubscriptionHelper.CANCELLED;
            this.downstream.onError(th);
        }

        public void onComplete() {
            if (!this.done) {
                this.done = true;
                this.upstream = SubscriptionHelper.CANCELLED;
                this.downstream.onSuccess(this.f383u);
            }
        }

        public void dispose() {
            this.upstream.cancel();
            this.upstream = SubscriptionHelper.CANCELLED;
        }

        public boolean isDisposed() {
            return this.upstream == SubscriptionHelper.CANCELLED;
        }
    }

    public FlowableCollectSingle(Flowable<T> flowable, Callable<? extends U> callable, BiConsumer<? super U, ? super T> biConsumer) {
        this.source = flowable;
        this.initialSupplier = callable;
        this.collector = biConsumer;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super U> singleObserver) {
        try {
            this.source.subscribe((FlowableSubscriber<? super T>) new CollectSubscriber<Object>(singleObserver, ObjectHelper.requireNonNull(this.initialSupplier.call(), "The initialSupplier returned a null value"), this.collector));
        } catch (Throwable th) {
            EmptyDisposable.error(th, singleObserver);
        }
    }

    public Flowable<U> fuseToFlowable() {
        return RxJavaPlugins.onAssembly((Flowable<T>) new FlowableCollect<T>(this.source, this.initialSupplier, this.collector));
    }
}
