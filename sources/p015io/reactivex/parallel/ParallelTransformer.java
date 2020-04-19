package p015io.reactivex.parallel;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.parallel.ParallelTransformer */
public interface ParallelTransformer<Upstream, Downstream> {
    @NonNull
    ParallelFlowable<Downstream> apply(@NonNull ParallelFlowable<Upstream> parallelFlowable);
}
