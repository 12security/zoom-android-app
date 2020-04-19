package p015io.reactivex.observables;

import java.util.concurrent.TimeUnit;
import p015io.reactivex.Observable;
import p015io.reactivex.Scheduler;
import p015io.reactivex.annotations.CheckReturnValue;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.annotations.SchedulerSupport;
import p015io.reactivex.disposables.Disposable;
import p015io.reactivex.functions.Consumer;
import p015io.reactivex.internal.functions.Functions;
import p015io.reactivex.internal.functions.ObjectHelper;
import p015io.reactivex.internal.operators.observable.ObservableAutoConnect;
import p015io.reactivex.internal.operators.observable.ObservableRefCount;
import p015io.reactivex.internal.util.ConnectConsumer;
import p015io.reactivex.plugins.RxJavaPlugins;
import p015io.reactivex.schedulers.Schedulers;

/* renamed from: io.reactivex.observables.ConnectableObservable */
public abstract class ConnectableObservable<T> extends Observable<T> {
    public abstract void connect(@NonNull Consumer<? super Disposable> consumer);

    public final Disposable connect() {
        ConnectConsumer connectConsumer = new ConnectConsumer();
        connect(connectConsumer);
        return connectConsumer.disposable;
    }

    @CheckReturnValue
    @NonNull
    @SchedulerSupport("none")
    public Observable<T> refCount() {
        return RxJavaPlugins.onAssembly((Observable<T>) new ObservableRefCount<T>(this));
    }

    @CheckReturnValue
    @SchedulerSupport("none")
    public final Observable<T> refCount(int i) {
        return refCount(i, 0, TimeUnit.NANOSECONDS, Schedulers.trampoline());
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> refCount(long j, TimeUnit timeUnit) {
        return refCount(1, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable<T> refCount(long j, TimeUnit timeUnit, Scheduler scheduler) {
        return refCount(1, j, timeUnit, scheduler);
    }

    @CheckReturnValue
    @SchedulerSupport("io.reactivex:computation")
    public final Observable<T> refCount(int i, long j, TimeUnit timeUnit) {
        return refCount(i, j, timeUnit, Schedulers.computation());
    }

    @CheckReturnValue
    @SchedulerSupport("custom")
    public final Observable<T> refCount(int i, long j, TimeUnit timeUnit, Scheduler scheduler) {
        ObjectHelper.verifyPositive(i, "subscriberCount");
        ObjectHelper.requireNonNull(timeUnit, "unit is null");
        ObjectHelper.requireNonNull(scheduler, "scheduler is null");
        ObservableRefCount observableRefCount = new ObservableRefCount(this, i, j, timeUnit, scheduler);
        return RxJavaPlugins.onAssembly((Observable<T>) observableRefCount);
    }

    @NonNull
    public Observable<T> autoConnect() {
        return autoConnect(1);
    }

    @NonNull
    public Observable<T> autoConnect(int i) {
        return autoConnect(i, Functions.emptyConsumer());
    }

    @NonNull
    public Observable<T> autoConnect(int i, @NonNull Consumer<? super Disposable> consumer) {
        if (i > 0) {
            return RxJavaPlugins.onAssembly((Observable<T>) new ObservableAutoConnect<T>(this, i, consumer));
        }
        connect(consumer);
        return RxJavaPlugins.onAssembly(this);
    }
}
