package p015io.reactivex;

import p015io.reactivex.annotations.NonNull;

/* renamed from: io.reactivex.CompletableConverter */
public interface CompletableConverter<R> {
    @NonNull
    R apply(@NonNull Completable completable);
}
