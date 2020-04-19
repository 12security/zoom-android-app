package p015io.reactivex.internal.operators.flowable;

import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import p015io.reactivex.Single;
import p015io.reactivex.SingleObserver;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.BiFunction;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.internal.operators.flowable.FlowableReduceWithSingle */
public final class FlowableReduceWithSingle<T, R> extends Single<R> {
    final BiFunction<R, ? super T, R> reducer;
    final Callable<R> seedSupplier;
    final Publisher<T> source;

    public FlowableReduceWithSingle(Publisher<T> publisher, Callable<R> callable, BiFunction<R, ? super T, R> biFunction) {
        this.source = publisher;
        this.seedSupplier = callable;
        this.reducer = biFunction;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(SingleObserver<? super R> singleObserver) {
        try {
            this.source.subscribe(new ReduceSeedObserver(singleObserver, this.reducer, ObjectHelper.requireNonNull(this.seedSupplier.call(), "The seedSupplier returned a null value")));
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            EmptyDisposable.error(th, singleObserver);
        }
    }
}
