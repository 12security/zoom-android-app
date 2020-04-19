package p015io.reactivex.parallel;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.parallel.ParallelFlowableConverter */
public interface ParallelFlowableConverter<T, R> {
    @NonNull
    R apply(@NonNull ParallelFlowable<T> parallelFlowable);
}
