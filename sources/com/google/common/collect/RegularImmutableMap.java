package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.Weak;
import java.io.Serializable;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableMap<K, V> extends ImmutableMap<K, V> {
    private static final double MAX_LOAD_FACTOR = 1.2d;
    private static final long serialVersionUID = 0;
    /* access modifiers changed from: private */
    public final transient Entry<K, V>[] entries;
    private final transient int mask;
    private final transient ImmutableMapEntry<K, V>[] table;

    @GwtCompatible(emulated = true)
    private static final class KeySet<K, V> extends Indexed<K> {
        @Weak
        private final RegularImmutableMap<K, V> map;

        @GwtIncompatible
        private static class SerializedForm<K> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<K, ?> map;

            SerializedForm(ImmutableMap<K, ?> immutableMap) {
                this.map = immutableMap;
            }

            /* access modifiers changed from: 0000 */
            public Object readResolve() {
                return this.map.keySet();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return true;
        }

        KeySet(RegularImmutableMap<K, V> regularImmutableMap) {
            this.map = regularImmutableMap;
        }

        /* access modifiers changed from: 0000 */
        public K get(int i) {
            return this.map.entries[i].getKey();
        }

        public boolean contains(Object obj) {
            return this.map.containsKey(obj);
        }

        public int size() {
            return this.map.size();
        }

        /* access modifiers changed from: 0000 */
        @GwtIncompatible
        public Object writeReplace() {
            return new SerializedForm(this.map);
        }
    }

    @GwtCompatible(emulated = true)
    private static final class Values<K, V> extends ImmutableList<V> {
        @Weak
        final RegularImmutableMap<K, V> map;

        @GwtIncompatible
        private static class SerializedForm<V> implements Serializable {
            private static final long serialVersionUID = 0;
            final ImmutableMap<?, V> map;

            SerializedForm(ImmutableMap<?, V> immutableMap) {
                this.map = immutableMap;
            }

            /* access modifiers changed from: 0000 */
            public Object readResolve() {
                return this.map.values();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return true;
        }

        Values(RegularImmutableMap<K, V> regularImmutableMap) {
            this.map = regularImmutableMap;
        }

        public V get(int i) {
            return this.map.entries[i].getValue();
        }

        public int size() {
            return this.map.size();
        }

        /* access modifiers changed from: 0000 */
        @GwtIncompatible
        public Object writeReplace() {
            return new SerializedForm(this.map);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return false;
    }

    static <K, V> RegularImmutableMap<K, V> fromEntries(Entry<K, V>... entryArr) {
        return fromEntryArray(entryArr.length, entryArr);
    }

    static <K, V> RegularImmutableMap<K, V> fromEntryArray(int i, Entry<K, V>[] entryArr) {
        Entry<K, V>[] entryArr2;
        ImmutableMapEntry immutableMapEntry;
        Preconditions.checkPositionIndex(i, entryArr.length);
        if (i == entryArr.length) {
            entryArr2 = entryArr;
        } else {
            entryArr2 = ImmutableMapEntry.createEntryArray(i);
        }
        int closedTableSize = Hashing.closedTableSize(i, MAX_LOAD_FACTOR);
        ImmutableMapEntry[] createEntryArray = ImmutableMapEntry.createEntryArray(closedTableSize);
        int i2 = closedTableSize - 1;
        for (int i3 = 0; i3 < i; i3++) {
            ImmutableMapEntry immutableMapEntry2 = entryArr[i3];
            Object key = immutableMapEntry2.getKey();
            Object value = immutableMapEntry2.getValue();
            CollectPreconditions.checkEntryNotNull(key, value);
            int smear = Hashing.smear(key.hashCode()) & i2;
            ImmutableMapEntry immutableMapEntry3 = createEntryArray[smear];
            if (immutableMapEntry3 == null) {
                immutableMapEntry = (immutableMapEntry2 instanceof ImmutableMapEntry) && immutableMapEntry2.isReusable() ? immutableMapEntry2 : new ImmutableMapEntry(key, value);
            } else {
                immutableMapEntry = new NonTerminalImmutableMapEntry(key, value, immutableMapEntry3);
            }
            createEntryArray[smear] = immutableMapEntry;
            entryArr2[i3] = immutableMapEntry;
            checkNoConflictInKeyBucket(key, immutableMapEntry, immutableMapEntry3);
        }
        return new RegularImmutableMap<>(entryArr2, createEntryArray, i2);
    }

    private RegularImmutableMap(Entry<K, V>[] entryArr, ImmutableMapEntry<K, V>[] immutableMapEntryArr, int i) {
        this.entries = entryArr;
        this.table = immutableMapEntryArr;
        this.mask = i;
    }

    static void checkNoConflictInKeyBucket(Object obj, Entry<?, ?> entry, @Nullable ImmutableMapEntry<?, ?> immutableMapEntry) {
        while (immutableMapEntry != null) {
            checkNoConflict(!obj.equals(immutableMapEntry.getKey()), "key", entry, immutableMapEntry);
            immutableMapEntry = immutableMapEntry.getNextInKeyBucket();
        }
    }

    public V get(@Nullable Object obj) {
        return get(obj, this.table, this.mask);
    }

    @Nullable
    static <V> V get(@Nullable Object obj, ImmutableMapEntry<?, V>[] immutableMapEntryArr, int i) {
        if (obj == null) {
            return null;
        }
        for (ImmutableMapEntry<?, V> immutableMapEntry = immutableMapEntryArr[i & Hashing.smear(obj.hashCode())]; immutableMapEntry != null; immutableMapEntry = immutableMapEntry.getNextInKeyBucket()) {
            if (obj.equals(immutableMapEntry.getKey())) {
                return immutableMapEntry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return this.entries.length;
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<Entry<K, V>> createEntrySet() {
        return new RegularEntrySet(this, this.entries);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<K> createKeySet() {
        return new KeySet(this);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableCollection<V> createValues() {
        return new Values(this);
    }
}
