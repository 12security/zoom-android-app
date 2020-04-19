package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.SingleConverter */
public interface SingleConverter<T, R> {
    @NonNull
    R apply(@NonNull Single<T> single);
}
