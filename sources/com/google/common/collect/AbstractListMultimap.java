package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractListMultimap<K, V> extends AbstractMapBasedMultimap<K, V> implements ListMultimap<K, V> {
    private static final long serialVersionUID = 6588350623831699109L;

    /* access modifiers changed from: 0000 */
    public abstract List<V> createCollection();

    protected AbstractListMultimap(Map<K, Collection<V>> map) {
        super(map);
    }

    /* access modifiers changed from: 0000 */
    public List<V> createUnmodifiableEmptyCollection() {
        return ImmutableList.m113of();
    }

    public List<V> get(@Nullable K k) {
        return (List) super.get(k);
    }

    @CanIgnoreReturnValue
    public List<V> removeAll(@Nullable Object obj) {
        return (List) super.removeAll(obj);
    }

    @CanIgnoreReturnValue
    public List<V> replaceValues(@Nullable K k, Iterable<? extends V> iterable) {
        return (List) super.replaceValues(k, iterable);
    }

    @CanIgnoreReturnValue
    public boolean put(@Nullable K k, @Nullable V v) {
        return super.put(k, v);
    }

    public Map<K, Collection<V>> asMap() {
        return super.asMap();
    }

    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }
}
