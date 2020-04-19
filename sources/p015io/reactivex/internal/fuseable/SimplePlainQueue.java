package p015io.reactivex.internal.fuseable;

import p015io.reactivex.annotations.Nullable;

/* renamed from: io.reactivex.internal.fuseable.SimplePlainQueue */
public interface SimplePlainQueue<T> extends SimpleQueue<T> {
    @Nullable
    T poll();
}
