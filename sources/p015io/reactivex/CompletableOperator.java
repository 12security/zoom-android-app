package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableOperator */
public interface CompletableOperator {
    @NonNull
    CompletableObserver apply(@NonNull CompletableObserver completableObserver) throws Exception;
}
