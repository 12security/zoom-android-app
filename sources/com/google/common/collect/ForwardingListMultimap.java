package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public abstract class ForwardingListMultimap<K, V> extends ForwardingMultimap<K, V> implements ListMultimap<K, V> {
    /* access modifiers changed from: protected */
    public abstract ListMultimap<K, V> delegate();

    protected ForwardingListMultimap() {
    }

    public List<V> get(@Nullable K k) {
        return delegate().get(k);
    }

    @CanIgnoreReturnValue
    public List<V> removeAll(@Nullable Object obj) {
        return delegate().removeAll(obj);
    }

    @CanIgnoreReturnValue
    public List<V> replaceValues(K k, Iterable<? extends V> iterable) {
        return delegate().replaceValues(k, iterable);
    }
}
