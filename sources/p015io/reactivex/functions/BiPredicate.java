package p015io.reactivex.functions;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.functions.BiPredicate */
public interface BiPredicate<T1, T2> {
    boolean test(@NonNull T1 t1, @NonNull T2 t2) throws Exception;
}
