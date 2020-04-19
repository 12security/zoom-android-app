package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableBiMap<K, V> extends ImmutableMap<K, V> implements BiMap<K, V> {

    public static final class Builder<K, V> extends com.google.common.collect.ImmutableMap.Builder<K, V> {
        public Builder() {
        }

        Builder(int i) {
            super(i);
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K k, V v) {
            super.put(k, v);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            super.put(entry);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            super.putAll(map);
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            super.putAll(iterable);
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> comparator) {
            super.orderEntriesByValue(comparator);
            return this;
        }

        public ImmutableBiMap<K, V> build() {
            boolean z = false;
            switch (this.size) {
                case 0:
                    return ImmutableBiMap.m105of();
                case 1:
                    return ImmutableBiMap.m106of(this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = (ImmutableMapEntry[]) ObjectArrays.arraysCopyOf(this.entries, this.size);
                        }
                        Arrays.sort(this.entries, 0, this.size, Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
                    }
                    if (this.size == this.entries.length) {
                        z = true;
                    }
                    this.entriesUsed = z;
                    return RegularImmutableBiMap.fromEntryArray(this.size, this.entries);
            }
        }
    }

    private static class SerializedForm extends SerializedForm {
        private static final long serialVersionUID = 0;

        SerializedForm(ImmutableBiMap<?, ?> immutableBiMap) {
            super(immutableBiMap);
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return createMap(new Builder());
        }
    }

    public abstract ImmutableBiMap<V, K> inverse();

    /* renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m105of() {
        return RegularImmutableBiMap.EMPTY;
    }

    /* renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m106of(K k, V v) {
        return new SingletonImmutableBiMap(k, v);
    }

    /* renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m107of(K k, V v, K k2, V v2) {
        return RegularImmutableBiMap.fromEntries(entryOf(k, v), entryOf(k2, v2));
    }

    /* renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m108of(K k, V v, K k2, V v2, K k3, V v3) {
        return RegularImmutableBiMap.fromEntries(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3));
    }

    /* renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m109of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return RegularImmutableBiMap.fromEntries(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    /* renamed from: of */
    public static <K, V> ImmutableBiMap<K, V> m110of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return RegularImmutableBiMap.fromEntries(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    public static <K, V> ImmutableBiMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if (map instanceof ImmutableBiMap) {
            ImmutableBiMap<K, V> immutableBiMap = (ImmutableBiMap) map;
            if (!immutableBiMap.isPartialView()) {
                return immutableBiMap;
            }
        }
        return copyOf((Iterable<? extends Entry<? extends K, ? extends V>>) map.entrySet());
    }

    @Beta
    public static <K, V> ImmutableBiMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        Entry[] entryArr = (Entry[]) Iterables.toArray(iterable, (T[]) EMPTY_ENTRY_ARRAY);
        switch (entryArr.length) {
            case 0:
                return m105of();
            case 1:
                Entry entry = entryArr[0];
                return m106of(entry.getKey(), entry.getValue());
            default:
                return RegularImmutableBiMap.fromEntries(entryArr);
        }
    }

    ImmutableBiMap() {
    }

    public ImmutableSet<V> values() {
        return inverse().keySet();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public V forcePut(K k, V v) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(this);
    }
}
