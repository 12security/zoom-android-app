package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    static final RegularImmutableBiMap<Object, Object> EMPTY;
    static final double MAX_LOAD_FACTOR = 1.2d;
    /* access modifiers changed from: private */
    public final transient Entry<K, V>[] entries;
    /* access modifiers changed from: private */
    public final transient int hashCode;
    @RetainedWith
    @LazyInit
    private transient ImmutableBiMap<V, K> inverse;
    private final transient ImmutableMapEntry<K, V>[] keyTable;
    /* access modifiers changed from: private */
    public final transient int mask;
    /* access modifiers changed from: private */
    public final transient ImmutableMapEntry<K, V>[] valueTable;

    private final class Inverse extends ImmutableBiMap<V, K> {

        final class InverseEntrySet extends ImmutableMapEntrySet<V, K> {
            /* access modifiers changed from: 0000 */
            public boolean isHashCodeFast() {
                return true;
            }

            InverseEntrySet() {
            }

            /* access modifiers changed from: 0000 */
            public ImmutableMap<V, K> map() {
                return Inverse.this;
            }

            public int hashCode() {
                return RegularImmutableBiMap.this.hashCode;
            }

            public UnmodifiableIterator<Entry<V, K>> iterator() {
                return asList().iterator();
            }

            /* access modifiers changed from: 0000 */
            public ImmutableList<Entry<V, K>> createAsList() {
                return new ImmutableAsList<Entry<V, K>>() {
                    public Entry<V, K> get(int i) {
                        Entry entry = RegularImmutableBiMap.this.entries[i];
                        return Maps.immutableEntry(entry.getValue(), entry.getKey());
                    }

                    /* access modifiers changed from: 0000 */
                    public ImmutableCollection<Entry<V, K>> delegateCollection() {
                        return InverseEntrySet.this;
                    }
                };
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return false;
        }

        private Inverse() {
        }

        public int size() {
            return inverse().size();
        }

        public ImmutableBiMap<K, V> inverse() {
            return RegularImmutableBiMap.this;
        }

        public K get(@Nullable Object obj) {
            if (obj == null || RegularImmutableBiMap.this.valueTable == null) {
                return null;
            }
            for (ImmutableMapEntry immutableMapEntry = RegularImmutableBiMap.this.valueTable[Hashing.smear(obj.hashCode()) & RegularImmutableBiMap.this.mask]; immutableMapEntry != null; immutableMapEntry = immutableMapEntry.getNextInValueBucket()) {
                if (obj.equals(immutableMapEntry.getValue())) {
                    return immutableMapEntry.getKey();
                }
            }
            return null;
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSet<Entry<V, K>> createEntrySet() {
            return new InverseEntrySet();
        }

        /* access modifiers changed from: 0000 */
        public Object writeReplace() {
            return new InverseSerializedForm(RegularImmutableBiMap.this);
        }
    }

    private static class InverseSerializedForm<K, V> implements Serializable {
        private static final long serialVersionUID = 1;
        private final ImmutableBiMap<K, V> forward;

        InverseSerializedForm(ImmutableBiMap<K, V> immutableBiMap) {
            this.forward = immutableBiMap;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return this.forward.inverse();
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isHashCodeFast() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return false;
    }

    static {
        RegularImmutableBiMap regularImmutableBiMap = new RegularImmutableBiMap(null, null, (Entry[]) ImmutableMap.EMPTY_ENTRY_ARRAY, 0, 0);
        EMPTY = regularImmutableBiMap;
    }

    static <K, V> RegularImmutableBiMap<K, V> fromEntries(Entry<K, V>... entryArr) {
        return fromEntryArray(entryArr.length, entryArr);
    }

    static <K, V> RegularImmutableBiMap<K, V> fromEntryArray(int i, Entry<K, V>[] entryArr) {
        Entry<K, V>[] entryArr2;
        ImmutableMapEntry immutableMapEntry;
        int i2 = i;
        Entry<K, V>[] entryArr3 = entryArr;
        Preconditions.checkPositionIndex(i2, entryArr3.length);
        int closedTableSize = Hashing.closedTableSize(i2, MAX_LOAD_FACTOR);
        int i3 = closedTableSize - 1;
        ImmutableMapEntry[] createEntryArray = ImmutableMapEntry.createEntryArray(closedTableSize);
        ImmutableMapEntry[] createEntryArray2 = ImmutableMapEntry.createEntryArray(closedTableSize);
        if (i2 == entryArr3.length) {
            entryArr2 = entryArr3;
        } else {
            entryArr2 = ImmutableMapEntry.createEntryArray(i);
        }
        int i4 = 0;
        int i5 = 0;
        while (i4 < i2) {
            Entry<K, V> entry = entryArr3[i4];
            Object key = entry.getKey();
            Object value = entry.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int hashCode2 = key.hashCode();
            int hashCode3 = value.hashCode();
            int smear = Hashing.smear(hashCode2) & i3;
            int smear2 = Hashing.smear(hashCode3) & i3;
            ImmutableMapEntry immutableMapEntry2 = createEntryArray[smear];
            RegularImmutableMap.checkNoConflictInKeyBucket(key, entry, immutableMapEntry2);
            ImmutableMapEntry immutableMapEntry3 = createEntryArray2[smear2];
            checkNoConflictInValueBucket(value, entry, immutableMapEntry3);
            if (immutableMapEntry3 == null && immutableMapEntry2 == null) {
                immutableMapEntry = (entry instanceof ImmutableMapEntry) && ((ImmutableMapEntry) entry).isReusable() ? (ImmutableMapEntry) entry : new ImmutableMapEntry(key, value);
            } else {
                immutableMapEntry = new NonTerminalImmutableBiMapEntry(key, value, immutableMapEntry2, immutableMapEntry3);
            }
            createEntryArray[smear] = immutableMapEntry;
            createEntryArray2[smear2] = immutableMapEntry;
            entryArr2[i4] = immutableMapEntry;
            i5 += hashCode2 ^ hashCode3;
            i4++;
            i2 = i;
        }
        RegularImmutableBiMap regularImmutableBiMap = new RegularImmutableBiMap(createEntryArray, createEntryArray2, entryArr2, i3, i5);
        return regularImmutableBiMap;
    }

    private RegularImmutableBiMap(ImmutableMapEntry<K, V>[] immutableMapEntryArr, ImmutableMapEntry<K, V>[] immutableMapEntryArr2, Entry<K, V>[] entryArr, int i, int i2) {
        this.keyTable = immutableMapEntryArr;
        this.valueTable = immutableMapEntryArr2;
        this.entries = entryArr;
        this.mask = i;
        this.hashCode = i2;
    }

    private static void checkNoConflictInValueBucket(Object obj, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            checkNoConflict(!obj.equals(immutableMapEntry.getValue()), "value", entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInValueBucket();
        }
    }

    @Nullable
    public V get(@Nullable Object obj) {
        ImmutableMapEntry<K, V>[] immutableMapEntryArr = this.keyTable;
        if (immutableMapEntryArr == null) {
            return null;
        }
        return RegularImmutableMap.get(obj, immutableMapEntryArr, this.mask);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<Entry<K, V>> createEntrySet() {
        return isEmpty() ? ImmutableSet.m155of() : new RegularEntrySet(this, this.entries);
    }

    public int hashCode() {
        return this.hashCode;
    }

    public int size() {
        return this.entries.length;
    }

    public ImmutableBiMap<V, K> inverse() {
        if (isEmpty()) {
            return ImmutableBiMap.m105of();
        }
        ImmutableBiMap<V, K> immutableBiMap = this.inverse;
        if (immutableBiMap == null) {
            immutableBiMap = new Inverse<>();
            this.inverse = immutableBiMap;
        }
        return immutableBiMap;
    }
}
