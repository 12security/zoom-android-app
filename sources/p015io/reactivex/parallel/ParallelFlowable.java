package p015io.reactivex.parallel;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import p015io.reactivex.Flowable;
import p015io.reactivex.Scheduler;
import p015io.reactivex.annotations.BackpressureKind;
import p015io.reactivex.annotations.BackpressureSupport;
import p015io.reactivex.annotations.CheckReturnValue;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.annotations.SchedulerSupport;
import p015io.reactivex.exceptions.Exceptions;
import p015io.reactivex.functions.Action;
import p015io.reactivex.functions.BiConsumer;
import p015io.reactivex.functions.BiFunction;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.functions.Function;
import p015io.reactivex.functions.LongConsumer;
import p015io.reactivex.functions.Predicate;
import p015io.reactivex.internal.functions.Functions;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.operators.parallel.ParallelCollect;
import p015io.reactivex.internal.operators.parallel.ParallelConcatMap;
import p015io.reactivex.internal.operators.parallel.ParallelDoOnNextTry;
import p015io.reactivex.internal.operators.parallel.ParallelFilter;
import p015io.reactivex.internal.operators.parallel.ParallelFilterTry;
import p015io.reactivex.internal.operators.parallel.ParallelFlatMap;
import p015io.reactivex.internal.operators.parallel.ParallelFromArray;
import p015io.reactivex.internal.operators.parallel.ParallelFromPublisher;
import p015io.reactivex.internal.operators.parallel.ParallelJoin;
import p015io.reactivex.internal.operators.parallel.ParallelMap;
import p015io.reactivex.internal.operators.parallel.ParallelMapTry;
import p015io.reactivex.internal.operators.parallel.ParallelPeek;
import p015io.reactivex.internal.operators.parallel.ParallelReduce;
import p015io.reactivex.internal.operators.parallel.ParallelReduceFull;
import p015io.reactivex.internal.operators.parallel.ParallelRunOn;
import p015io.reactivex.internal.operators.parallel.ParallelSortedJoin;
import p015io.reactivex.internal.subscriptions.EmptySubscription;
import p015io.reactivex.internal.util.ErrorMode;
import p015io.reactivex.internal.util.ExceptionHelper;
import p015io.reactivex.internal.util.ListAddBiConsumer;
import p015io.reactivex.internal.util.MergerBiFunction;
import p015io.reactivex.internal.util.SorterFunction;
import p015io.reactivex.plugins.RxJavaPlugins;

/* renamed from: io.reactivex.parallel.ParallelFlowable */
public abstract class ParallelFlowable<T> {
    public abstract int parallelism();

    public abstract void subscribe(@NonNull Subscriber<? super T>[] subscriberArr);

    /* access modifiers changed from: protected */
    public final boolean validate(@NonNull Subscriber<?>[] subscriberArr) {
        int parallelism = parallelism();
        if (subscriberArr.length == parallelism) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("parallelism = ");
        sb.append(parallelism);
        sb.append(", subscribers = ");
        sb.append(subscriberArr.length);
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException(sb.toString());
        for (Subscriber<?> error : subscriberArr) {
            EmptySubscription.error(illegalArgumentException, error);
        }
        return false;
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> publisher) {
        return from(publisher, Runtime.getRuntime().availableProcessors(), Flowable.bufferSize());
    }

    @CheckReturnValue
    public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> publisher, int i) {
        return from(publisher, i, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public static <T> ParallelFlowable<T> from(@NonNull Publisher<? extends T> publisher, int i, int i2) {
        ObjectHelper.requireNonNull(publisher, "source");
        ObjectHelper.verifyPositive(i, "parallelism");
        ObjectHelper.verifyPositive(i2, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFromPublisher<T>(publisher, i, i2));
    }

    @CheckReturnValue
    @NonNull
    /* renamed from: as */
    public final <R> R mo57457as(@NonNull ParallelFlowableConverter<T, R> parallelFlowableConverter) {
        return ((ParallelFlowableConverter) ObjectHelper.requireNonNull(parallelFlowableConverter, "converter is null")).apply(this);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> function) {
        ObjectHelper.requireNonNull(function, "mapper");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelMap<T>(this, function));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> function, @NonNull ParallelFailureHandling parallelFailureHandling) {
        ObjectHelper.requireNonNull(function, "mapper");
        ObjectHelper.requireNonNull(parallelFailureHandling, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelMapTry<T>(this, function, parallelFailureHandling));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> map(@NonNull Function<? super T, ? extends R> function, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction) {
        ObjectHelper.requireNonNull(function, "mapper");
        ObjectHelper.requireNonNull(biFunction, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelMapTry<T>(this, function, biFunction));
    }

    @CheckReturnValue
    public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFilter<T>(this, predicate));
    }

    @CheckReturnValue
    public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate, @NonNull ParallelFailureHandling parallelFailureHandling) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        ObjectHelper.requireNonNull(parallelFailureHandling, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFilterTry<T>(this, predicate, parallelFailureHandling));
    }

    @CheckReturnValue
    public final ParallelFlowable<T> filter(@NonNull Predicate<? super T> predicate, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction) {
        ObjectHelper.requireNonNull(predicate, "predicate");
        ObjectHelper.requireNonNull(biFunction, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFilterTry<T>(this, predicate, biFunction));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> runOn(@NonNull Scheduler scheduler) {
        return runOn(scheduler, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> runOn(@NonNull Scheduler scheduler, int i) {
        ObjectHelper.requireNonNull(scheduler, "scheduler");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelRunOn<T>(this, scheduler, i));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<T> reduce(@NonNull BiFunction<T, T, T> biFunction) {
        ObjectHelper.requireNonNull(biFunction, "reducer");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelReduceFull<T>(this, biFunction));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> reduce(@NonNull Callable<R> callable, @NonNull BiFunction<R, ? super T, R> biFunction) {
        ObjectHelper.requireNonNull(callable, "initialSupplier");
        ObjectHelper.requireNonNull(biFunction, "reducer");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelReduce<T>(this, callable, biFunction));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @SchedulerSupport("none")
    public final Flowable<T> sequential() {
        return sequential(Flowable.bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable<T> sequential(int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelJoin<T>(this, i, false));
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable<T> sequentialDelayError() {
        return sequentialDelayError(Flowable.bufferSize());
    }

    @CheckReturnValue
    @BackpressureSupport(BackpressureKind.FULL)
    @NonNull
    @SchedulerSupport("none")
    public final Flowable<T> sequentialDelayError(int i) {
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelJoin<T>(this, i, true));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<T> sorted(@NonNull Comparator<? super T> comparator) {
        return sorted(comparator, 16);
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<T> sorted(@NonNull Comparator<? super T> comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly((Flowable<T>) new ParallelSortedJoin<T>(reduce(Functions.createArrayList((i / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)), comparator));
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<List<T>> toSortedList(@NonNull Comparator<? super T> comparator) {
        return toSortedList(comparator, 16);
    }

    @CheckReturnValue
    @NonNull
    public final Flowable<List<T>> toSortedList(@NonNull Comparator<? super T> comparator, int i) {
        ObjectHelper.requireNonNull(comparator, "comparator is null");
        ObjectHelper.verifyPositive(i, "capacityHint");
        return RxJavaPlugins.onAssembly(reduce(Functions.createArrayList((i / parallelism()) + 1), ListAddBiConsumer.instance()).map(new SorterFunction(comparator)).reduce(new MergerBiFunction(comparator)));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, consumer, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> consumer, @NonNull ParallelFailureHandling parallelFailureHandling) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(parallelFailureHandling, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelDoOnNextTry<T>(this, consumer, parallelFailureHandling));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnNext(@NonNull Consumer<? super T> consumer, @NonNull BiFunction<? super Long, ? super Throwable, ParallelFailureHandling> biFunction) {
        ObjectHelper.requireNonNull(consumer, "onNext is null");
        ObjectHelper.requireNonNull(biFunction, "errorHandler is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelDoOnNextTry<T>(this, consumer, biFunction));
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doAfterNext(@NonNull Consumer<? super T> consumer) {
        ObjectHelper.requireNonNull(consumer, "onAfterNext is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), consumer, Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnError(@NonNull Consumer<Throwable> consumer) {
        ObjectHelper.requireNonNull(consumer, "onError is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), consumer, Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnComplete(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "onComplete is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), action, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doAfterTerminated(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "onAfterTerminate is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, action, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnSubscribe(@NonNull Consumer<? super Subscription> consumer) {
        ObjectHelper.requireNonNull(consumer, "onSubscribe is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, consumer, Functions.EMPTY_LONG_CONSUMER, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnRequest(@NonNull LongConsumer longConsumer) {
        ObjectHelper.requireNonNull(longConsumer, "onRequest is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), longConsumer, Functions.EMPTY_ACTION);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final ParallelFlowable<T> doOnCancel(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "onCancel is null");
        ParallelPeek parallelPeek = new ParallelPeek(this, Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.emptyConsumer(), Functions.EMPTY_ACTION, Functions.EMPTY_ACTION, Functions.emptyConsumer(), Functions.EMPTY_LONG_CONSUMER, action);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelPeek);
    }

    @CheckReturnValue
    @NonNull
    public final <C> ParallelFlowable<C> collect(@NonNull Callable<? extends C> callable, @NonNull BiConsumer<? super C, ? super T> biConsumer) {
        ObjectHelper.requireNonNull(callable, "collectionSupplier is null");
        ObjectHelper.requireNonNull(biConsumer, "collector is null");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelCollect<T>(this, callable, biConsumer));
    }

    @CheckReturnValue
    @NonNull
    public static <T> ParallelFlowable<T> fromArray(@NonNull Publisher<T>... publisherArr) {
        if (publisherArr.length != 0) {
            return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelFromArray<T>(publisherArr));
        }
        throw new IllegalArgumentException("Zero publishers not supported");
    }

    @CheckReturnValue
    @NonNull
    /* renamed from: to */
    public final <U> U mo57494to(@NonNull Function<? super ParallelFlowable<T>, U> function) {
        try {
            return ((Function) ObjectHelper.requireNonNull(function, "converter is null")).apply(this);
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    @CheckReturnValue
    @NonNull
    public final <U> ParallelFlowable<U> compose(@NonNull ParallelTransformer<T, U> parallelTransformer) {
        return RxJavaPlugins.onAssembly(((ParallelTransformer) ObjectHelper.requireNonNull(parallelTransformer, "composer is null")).apply(this));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> function) {
        return flatMap(function, false, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> function, boolean z) {
        return flatMap(function, z, Integer.MAX_VALUE, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> function, boolean z, int i) {
        return flatMap(function, z, i, Flowable.bufferSize());
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> flatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> function, boolean z, int i, int i2) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "maxConcurrency");
        ObjectHelper.verifyPositive(i2, "prefetch");
        ParallelFlatMap parallelFlatMap = new ParallelFlatMap(this, function, z, i, i2);
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) parallelFlatMap);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> function) {
        return concatMap(function, 2);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMap(@NonNull Function<? super T, ? extends Publisher<? extends R>> function, int i) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelConcatMap<T>(this, function, i, ErrorMode.IMMEDIATE));
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMapDelayError(@NonNull Function<? super T, ? extends Publisher<? extends R>> function, boolean z) {
        return concatMapDelayError(function, 2, z);
    }

    @CheckReturnValue
    @NonNull
    public final <R> ParallelFlowable<R> concatMapDelayError(@NonNull Function<? super T, ? extends Publisher<? extends R>> function, int i, boolean z) {
        ObjectHelper.requireNonNull(function, "mapper is null");
        ObjectHelper.verifyPositive(i, "prefetch");
        return RxJavaPlugins.onAssembly((ParallelFlowable<T>) new ParallelConcatMap<T>(this, function, i, z ? ErrorMode.END : ErrorMode.BOUNDARY));
    }
}
