package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMap<K, V> implements Map<K, V>, Serializable {
    static final Entry<?, ?>[] EMPTY_ENTRY_ARRAY = new Entry[0];
    @LazyInit
    private transient ImmutableSet<Entry<K, V>> entrySet;
    @LazyInit
    private transient ImmutableSet<K> keySet;
    @LazyInit
    private transient ImmutableSetMultimap<K, V> multimapView;
    @LazyInit
    private transient ImmutableCollection<V> values;

    public static class Builder<K, V> {
        ImmutableMapEntry<K, V>[] entries;
        boolean entriesUsed;
        int size;
        Comparator<? super V> valueComparator;

        public Builder() {
            this(4);
        }

        Builder(int i) {
            this.entries = new ImmutableMapEntry[i];
            this.size = 0;
            this.entriesUsed = false;
        }

        private void ensureCapacity(int i) {
            ImmutableMapEntry<K, V>[] immutableMapEntryArr = this.entries;
            if (i > immutableMapEntryArr.length) {
                this.entries = (ImmutableMapEntry[]) ObjectArrays.arraysCopyOf(immutableMapEntryArr, com.google.common.collect.ImmutableCollection.Builder.expandedCapacity(immutableMapEntryArr.length, i));
                this.entriesUsed = false;
            }
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(K k, V v) {
            ensureCapacity(this.size + 1);
            ImmutableMapEntry<K, V> entryOf = ImmutableMap.entryOf(k, v);
            ImmutableMapEntry<K, V>[] immutableMapEntryArr = this.entries;
            int i = this.size;
            this.size = i + 1;
            immutableMapEntryArr[i] = entryOf;
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> put(Entry<? extends K, ? extends V> entry) {
            return put(entry.getKey(), entry.getValue());
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(Map<? extends K, ? extends V> map) {
            return putAll((Iterable<? extends Entry<? extends K, ? extends V>>) map.entrySet());
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> putAll(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
            if (iterable instanceof Collection) {
                ensureCapacity(this.size + ((Collection) iterable).size());
            }
            for (Entry put : iterable) {
                put(put);
            }
            return this;
        }

        @CanIgnoreReturnValue
        @Beta
        public Builder<K, V> orderEntriesByValue(Comparator<? super V> comparator) {
            Preconditions.checkState(this.valueComparator == null, "valueComparator was already set");
            this.valueComparator = (Comparator) Preconditions.checkNotNull(comparator, "valueComparator");
            return this;
        }

        public ImmutableMap<K, V> build() {
            int i = this.size;
            boolean z = false;
            switch (i) {
                case 0:
                    return ImmutableMap.m132of();
                case 1:
                    return ImmutableMap.m133of(this.entries[0].getKey(), this.entries[0].getValue());
                default:
                    if (this.valueComparator != null) {
                        if (this.entriesUsed) {
                            this.entries = (ImmutableMapEntry[]) ObjectArrays.arraysCopyOf(this.entries, i);
                        }
                        Arrays.sort(this.entries, 0, this.size, Ordering.from(this.valueComparator).onResultOf(Maps.valueFunction()));
                    }
                    if (this.size == this.entries.length) {
                        z = true;
                    }
                    this.entriesUsed = z;
                    return RegularImmutableMap.fromEntryArray(this.size, this.entries);
            }
        }
    }

    static abstract class IteratorBasedImmutableMap<K, V> extends ImmutableMap<K, V> {
        /* access modifiers changed from: 0000 */
        public abstract UnmodifiableIterator<Entry<K, V>> entryIterator();

        IteratorBasedImmutableMap() {
        }

        public /* bridge */ /* synthetic */ Set entrySet() {
            return ImmutableMap.super.entrySet();
        }

        public /* bridge */ /* synthetic */ Set keySet() {
            return ImmutableMap.super.keySet();
        }

        public /* bridge */ /* synthetic */ Collection values() {
            return ImmutableMap.super.values();
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSet<Entry<K, V>> createEntrySet() {
            return new ImmutableMapEntrySet<K, V>() {
                /* access modifiers changed from: 0000 */
                public ImmutableMap<K, V> map() {
                    return IteratorBasedImmutableMap.this;
                }

                public UnmodifiableIterator<Entry<K, V>> iterator() {
                    return IteratorBasedImmutableMap.this.entryIterator();
                }
            };
        }
    }

    private final class MapViewOfValuesAsSingletonSets extends IteratorBasedImmutableMap<K, ImmutableSet<V>> {
        private MapViewOfValuesAsSingletonSets() {
        }

        public int size() {
            return ImmutableMap.this.size();
        }

        public ImmutableSet<K> keySet() {
            return ImmutableMap.this.keySet();
        }

        public boolean containsKey(@Nullable Object obj) {
            return ImmutableMap.this.containsKey(obj);
        }

        public ImmutableSet<V> get(@Nullable Object obj) {
            Object obj2 = ImmutableMap.this.get(obj);
            if (obj2 == null) {
                return null;
            }
            return ImmutableSet.m156of(obj2);
        }

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return ImmutableMap.this.isPartialView();
        }

        public int hashCode() {
            return ImmutableMap.this.hashCode();
        }

        /* access modifiers changed from: 0000 */
        public boolean isHashCodeFast() {
            return ImmutableMap.this.isHashCodeFast();
        }

        /* access modifiers changed from: 0000 */
        public UnmodifiableIterator<Entry<K, ImmutableSet<V>>> entryIterator() {
            final UnmodifiableIterator it = ImmutableMap.this.entrySet().iterator();
            return new UnmodifiableIterator<Entry<K, ImmutableSet<V>>>() {
                public boolean hasNext() {
                    return it.hasNext();
                }

                public Entry<K, ImmutableSet<V>> next() {
                    final Entry entry = (Entry) it.next();
                    return new AbstractMapEntry<K, ImmutableSet<V>>() {
                        public K getKey() {
                            return entry.getKey();
                        }

                        public ImmutableSet<V> getValue() {
                            return ImmutableSet.m156of(entry.getValue());
                        }
                    };
                }
            };
        }
    }

    static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        private final Object[] keys;
        private final Object[] values;

        SerializedForm(ImmutableMap<?, ?> immutableMap) {
            this.keys = new Object[immutableMap.size()];
            this.values = new Object[immutableMap.size()];
            Iterator it = immutableMap.entrySet().iterator();
            int i = 0;
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                this.keys[i] = entry.getKey();
                this.values[i] = entry.getValue();
                i++;
            }
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return createMap(new Builder(this.keys.length));
        }

        /* access modifiers changed from: 0000 */
        public Object createMap(Builder<Object, Object> builder) {
            int i = 0;
            while (true) {
                Object[] objArr = this.keys;
                if (i >= objArr.length) {
                    return builder.build();
                }
                builder.put(objArr[i], this.values[i]);
                i++;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract ImmutableSet<Entry<K, V>> createEntrySet();

    public abstract V get(@Nullable Object obj);

    /* access modifiers changed from: 0000 */
    public boolean isHashCodeFast() {
        return false;
    }

    /* access modifiers changed from: 0000 */
    public abstract boolean isPartialView();

    /* renamed from: of */
    public static <K, V> ImmutableMap<K, V> m132of() {
        return ImmutableBiMap.m105of();
    }

    /* renamed from: of */
    public static <K, V> ImmutableMap<K, V> m133of(K k, V v) {
        return ImmutableBiMap.m106of(k, v);
    }

    /* renamed from: of */
    public static <K, V> ImmutableMap<K, V> m134of(K k, V v, K k2, V v2) {
        return RegularImmutableMap.fromEntries(entryOf(k, v), entryOf(k2, v2));
    }

    /* renamed from: of */
    public static <K, V> ImmutableMap<K, V> m135of(K k, V v, K k2, V v2, K k3, V v3) {
        return RegularImmutableMap.fromEntries(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3));
    }

    /* renamed from: of */
    public static <K, V> ImmutableMap<K, V> m136of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4) {
        return RegularImmutableMap.fromEntries(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4));
    }

    /* renamed from: of */
    public static <K, V> ImmutableMap<K, V> m137of(K k, V v, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        return RegularImmutableMap.fromEntries(entryOf(k, v), entryOf(k2, v2), entryOf(k3, v3), entryOf(k4, v4), entryOf(k5, v5));
    }

    static <K, V> ImmutableMapEntry<K, V> entryOf(K k, V v) {
        return new ImmutableMapEntry<>(k, v);
    }

    public static <K, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    static void checkNoConflict(boolean z, String str, Entry<?, ?> entry, Entry<?, ?> entry2) {
        if (!z) {
            StringBuilder sb = new StringBuilder();
            sb.append("Multiple entries with same ");
            sb.append(str);
            sb.append(": ");
            sb.append(entry);
            sb.append(" and ");
            sb.append(entry2);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public static <K, V> ImmutableMap<K, V> copyOf(Map<? extends K, ? extends V> map) {
        if ((map instanceof ImmutableMap) && !(map instanceof ImmutableSortedMap)) {
            ImmutableMap<K, V> immutableMap = (ImmutableMap) map;
            if (!immutableMap.isPartialView()) {
                return immutableMap;
            }
        } else if (map instanceof EnumMap) {
            return copyOfEnumMap((EnumMap) map);
        }
        return copyOf((Iterable<? extends Entry<? extends K, ? extends V>>) map.entrySet());
    }

    @Beta
    public static <K, V> ImmutableMap<K, V> copyOf(Iterable<? extends Entry<? extends K, ? extends V>> iterable) {
        Entry[] entryArr = (Entry[]) Iterables.toArray(iterable, (T[]) EMPTY_ENTRY_ARRAY);
        switch (entryArr.length) {
            case 0:
                return m132of();
            case 1:
                Entry entry = entryArr[0];
                return m133of(entry.getKey(), entry.getValue());
            default:
                return RegularImmutableMap.fromEntries(entryArr);
        }
    }

    private static <K extends Enum<K>, V> ImmutableMap<K, V> copyOfEnumMap(EnumMap<K, ? extends V> enumMap) {
        EnumMap enumMap2 = new EnumMap(enumMap);
        for (Entry entry : enumMap2.entrySet()) {
            CollectPreconditions.checkEntryNotNull(entry.getKey(), entry.getValue());
        }
        return ImmutableEnumMap.asImmutable(enumMap2);
    }

    ImmutableMap() {
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final V remove(Object obj) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void putAll(Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public final void clear() {
        throw new UnsupportedOperationException();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean containsKey(@Nullable Object obj) {
        return get(obj) != null;
    }

    public boolean containsValue(@Nullable Object obj) {
        return values().contains(obj);
    }

    public ImmutableSet<Entry<K, V>> entrySet() {
        ImmutableSet<Entry<K, V>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<Entry<K, V>> createEntrySet = createEntrySet();
        this.entrySet = createEntrySet;
        return createEntrySet;
    }

    public ImmutableSet<K> keySet() {
        ImmutableSet<K> immutableSet = this.keySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<K> createKeySet = createKeySet();
        this.keySet = createKeySet;
        return createKeySet;
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<K> createKeySet() {
        return isEmpty() ? ImmutableSet.m155of() : new ImmutableMapKeySet(this);
    }

    /* access modifiers changed from: 0000 */
    public UnmodifiableIterator<K> keyIterator() {
        final UnmodifiableIterator it = entrySet().iterator();
        return new UnmodifiableIterator<K>() {
            public boolean hasNext() {
                return it.hasNext();
            }

            public K next() {
                return ((Entry) it.next()).getKey();
            }
        };
    }

    public ImmutableCollection<V> values() {
        ImmutableCollection<V> immutableCollection = this.values;
        if (immutableCollection != null) {
            return immutableCollection;
        }
        ImmutableCollection<V> createValues = createValues();
        this.values = createValues;
        return createValues;
    }

    /* access modifiers changed from: 0000 */
    public ImmutableCollection<V> createValues() {
        return new ImmutableMapValues(this);
    }

    public ImmutableSetMultimap<K, V> asMultimap() {
        if (isEmpty()) {
            return ImmutableSetMultimap.m162of();
        }
        ImmutableSetMultimap<K, V> immutableSetMultimap = this.multimapView;
        if (immutableSetMultimap == null) {
            immutableSetMultimap = new ImmutableSetMultimap<>(new MapViewOfValuesAsSingletonSets(), size(), null);
            this.multimapView = immutableSetMultimap;
        }
        return immutableSetMultimap;
    }

    public boolean equals(@Nullable Object obj) {
        return Maps.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    public String toString() {
        return Maps.toStringImpl(this);
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(this);
    }
}
