package p015io.reactivex.functions;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.functions.Function3 */
public interface Function3<T1, T2, T3, R> {
    @NonNull
    R apply(@NonNull T1 t1, @NonNull T2 t2, @NonNull T3 t3) throws Exception;
}
