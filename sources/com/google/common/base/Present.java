package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
final class Present<T> extends Optional<T> {
    private static final long serialVersionUID = 0;
    private final T reference;

    public boolean isPresent() {
        return true;
    }

    Present(T t) {
        this.reference = t;
    }

    public T get() {
        return this.reference;
    }

    /* renamed from: or */
    public T mo29274or(T t) {
        Preconditions.checkNotNull(t, "use Optional.orNull() instead of Optional.or(null)");
        return this.reference;
    }

    /* renamed from: or */
    public Optional<T> mo29272or(Optional<? extends T> optional) {
        Preconditions.checkNotNull(optional);
        return this;
    }

    /* renamed from: or */
    public T mo29273or(Supplier<? extends T> supplier) {
        Preconditions.checkNotNull(supplier);
        return this.reference;
    }

    public T orNull() {
        return this.reference;
    }

    public Set<T> asSet() {
        return Collections.singleton(this.reference);
    }

    public <V> Optional<V> transform(Function<? super T, V> function) {
        return new Present(Preconditions.checkNotNull(function.apply(this.reference), "the Function passed to Optional.transform() must not return null."));
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Present)) {
            return false;
        }
        return this.reference.equals(((Present) obj).reference);
    }

    public int hashCode() {
        return this.reference.hashCode() + 1502476572;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Optional.of(");
        sb.append(this.reference);
        sb.append(")");
        return sb.toString();
    }
}
