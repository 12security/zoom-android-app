package p015io.reactivex.functions;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.functions.Function */
public interface Function<T, R> {
    R apply(@NonNull T t) throws Exception;
}
