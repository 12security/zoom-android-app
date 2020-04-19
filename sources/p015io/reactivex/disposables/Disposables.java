package p015io.reactivex.disposables;

import java.util.concurrent.Future;
import org.reactivestreams.Subscription;
import p015io.reactivex.annotations.NonNull;
import p015io.reactivex.functions.Action;
import p015io.reactivex.internal.disposables.EmptyDisposable;
import p015io.reactivex.internal.functions.Functions;
import p015io.reactivex.internal.functions.ObjectHelper;

/* renamed from: io.reactivex.disposables.Disposables */
public final class Disposables {
    private Disposables() {
        throw new IllegalStateException("No instances!");
    }

    @NonNull
    public static Disposable fromRunnable(@NonNull Runnable runnable) {
        ObjectHelper.requireNonNull(runnable, "run is null");
        return new RunnableDisposable(runnable);
    }

    @NonNull
    public static Disposable fromAction(@NonNull Action action) {
        ObjectHelper.requireNonNull(action, "run is null");
        return new ActionDisposable(action);
    }

    @NonNull
    public static Disposable fromFuture(@NonNull Future<?> future) {
        ObjectHelper.requireNonNull(future, "future is null");
        return fromFuture(future, true);
    }

    @NonNull
    public static Disposable fromFuture(@NonNull Future<?> future, boolean z) {
        ObjectHelper.requireNonNull(future, "future is null");
        return new FutureDisposable(future, z);
    }

    @NonNull
    public static Disposable fromSubscription(@NonNull Subscription subscription) {
        ObjectHelper.requireNonNull(subscription, "subscription is null");
        return new SubscriptionDisposable(subscription);
    }

    @NonNull
    public static Disposable empty() {
        return fromRunnable(Functions.EMPTY_RUNNABLE);
    }

    @NonNull
    public static Disposable disposed() {
        return EmptyDisposable.INSTANCE;
    }
}
