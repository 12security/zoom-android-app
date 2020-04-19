package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.j2objc.annotations.RetainedWith;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true)
public final class HashBiMap<K, V> extends IteratorBasedAbstractMap<K, V> implements BiMap<K, V>, Serializable {
    private static final double LOAD_FACTOR = 1.0d;
    @GwtIncompatible
    private static final long serialVersionUID = 0;
    /* access modifiers changed from: private */
    public transient BiEntry<K, V> firstInKeyInsertionOrder;
    private transient BiEntry<K, V>[] hashTableKToV;
    private transient BiEntry<K, V>[] hashTableVToK;
    @RetainedWith
    private transient BiMap<V, K> inverse;
    private transient BiEntry<K, V> lastInKeyInsertionOrder;
    private transient int mask;
    /* access modifiers changed from: private */
    public transient int modCount;
    /* access modifiers changed from: private */
    public transient int size;

    private static final class BiEntry<K, V> extends ImmutableEntry<K, V> {
        final int keyHash;
        @Nullable
        BiEntry<K, V> nextInKToVBucket;
        @Nullable
        BiEntry<K, V> nextInKeyInsertionOrder;
        @Nullable
        BiEntry<K, V> nextInVToKBucket;
        @Nullable
        BiEntry<K, V> prevInKeyInsertionOrder;
        final int valueHash;

        BiEntry(K k, int i, V v, int i2) {
            super(k, v);
            this.keyHash = i;
            this.valueHash = i2;
        }
    }

    private final class Inverse extends AbstractMap<V, K> implements BiMap<V, K>, Serializable {

        private final class InverseKeySet extends KeySet<V, K> {
            InverseKeySet() {
                super(Inverse.this);
            }

            public boolean remove(@Nullable Object obj) {
                BiEntry access$400 = HashBiMap.this.seekByValue(obj, Hashing.smearedHash(obj));
                if (access$400 == null) {
                    return false;
                }
                HashBiMap.this.delete(access$400);
                return true;
            }

            public Iterator<V> iterator() {
                return new Itr<V>() {
                    {
                        HashBiMap hashBiMap = HashBiMap.this;
                    }

                    /* access modifiers changed from: 0000 */
                    public V output(BiEntry<K, V> biEntry) {
                        return biEntry.value;
                    }
                };
            }
        }

        private Inverse() {
        }

        /* access modifiers changed from: 0000 */
        public BiMap<K, V> forward() {
            return HashBiMap.this;
        }

        public int size() {
            return HashBiMap.this.size;
        }

        public void clear() {
            forward().clear();
        }

        public boolean containsKey(@Nullable Object obj) {
            return forward().containsValue(obj);
        }

        public K get(@Nullable Object obj) {
            return Maps.keyOrNull(HashBiMap.this.seekByValue(obj, Hashing.smearedHash(obj)));
        }

        public K put(@Nullable V v, @Nullable K k) {
            return HashBiMap.this.putInverse(v, k, false);
        }

        public K forcePut(@Nullable V v, @Nullable K k) {
            return HashBiMap.this.putInverse(v, k, true);
        }

        public K remove(@Nullable Object obj) {
            BiEntry access$400 = HashBiMap.this.seekByValue(obj, Hashing.smearedHash(obj));
            if (access$400 == null) {
                return null;
            }
            HashBiMap.this.delete(access$400);
            access$400.prevInKeyInsertionOrder = null;
            access$400.nextInKeyInsertionOrder = null;
            return access$400.key;
        }

        public BiMap<K, V> inverse() {
            return forward();
        }

        public Set<V> keySet() {
            return new InverseKeySet();
        }

        public Set<K> values() {
            return forward().keySet();
        }

        public Set<Entry<V, K>> entrySet() {
            return new EntrySet<V, K>() {
                /* access modifiers changed from: 0000 */
                public Map<V, K> map() {
                    return Inverse.this;
                }

                public Iterator<Entry<V, K>> iterator() {
                    return new Itr<Entry<V, K>>() {

                        /* renamed from: com.google.common.collect.HashBiMap$Inverse$1$1$InverseEntry */
                        class InverseEntry extends AbstractMapEntry<V, K> {
                            BiEntry<K, V> delegate;

                            InverseEntry(BiEntry<K, V> biEntry) {
                                this.delegate = biEntry;
                            }

                            public V getKey() {
                                return this.delegate.value;
                            }

                            public K getValue() {
                                return this.delegate.key;
                            }

                            public K setValue(K k) {
                                K k2 = this.delegate.key;
                                int smearedHash = Hashing.smearedHash(k);
                                if (smearedHash == this.delegate.keyHash && Objects.equal(k, k2)) {
                                    return k;
                                }
                                Preconditions.checkArgument(HashBiMap.this.seekByKey(k, smearedHash) == null, "value already present: %s", (Object) k);
                                HashBiMap.this.delete(this.delegate);
                                BiEntry<K, V> biEntry = new BiEntry<>(k, smearedHash, this.delegate.value, this.delegate.valueHash);
                                this.delegate = biEntry;
                                HashBiMap.this.insert(biEntry, null);
                                C11951 r6 = C11951.this;
                                r6.expectedModCount = HashBiMap.this.modCount;
                                return k2;
                            }
                        }

                        {
                            HashBiMap hashBiMap = HashBiMap.this;
                        }

                        /* access modifiers changed from: 0000 */
                        public Entry<V, K> output(BiEntry<K, V> biEntry) {
                            return new InverseEntry(biEntry);
                        }
                    };
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public Object writeReplace() {
            return new InverseSerializedForm(HashBiMap.this);
        }
    }

    private static final class InverseSerializedForm<K, V> implements Serializable {
        private final HashBiMap<K, V> bimap;

        InverseSerializedForm(HashBiMap<K, V> hashBiMap) {
            this.bimap = hashBiMap;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return this.bimap.inverse();
        }
    }

    abstract class Itr<T> implements Iterator<T> {
        int expectedModCount = HashBiMap.this.modCount;
        BiEntry<K, V> next = HashBiMap.this.firstInKeyInsertionOrder;
        BiEntry<K, V> toRemove = null;

        /* access modifiers changed from: 0000 */
        public abstract T output(BiEntry<K, V> biEntry);

        Itr() {
        }

        public boolean hasNext() {
            if (HashBiMap.this.modCount == this.expectedModCount) {
                return this.next != null;
            }
            throw new ConcurrentModificationException();
        }

        public T next() {
            if (hasNext()) {
                BiEntry<K, V> biEntry = this.next;
                this.next = biEntry.nextInKeyInsertionOrder;
                this.toRemove = biEntry;
                return output(biEntry);
            }
            throw new NoSuchElementException();
        }

        public void remove() {
            if (HashBiMap.this.modCount == this.expectedModCount) {
                CollectPreconditions.checkRemove(this.toRemove != null);
                HashBiMap.this.delete(this.toRemove);
                this.expectedModCount = HashBiMap.this.modCount;
                this.toRemove = null;
                return;
            }
            throw new ConcurrentModificationException();
        }
    }

    private final class KeySet extends KeySet<K, V> {
        KeySet() {
            super(HashBiMap.this);
        }

        public Iterator<K> iterator() {
            return new Itr<K>() {
                {
                    HashBiMap hashBiMap = HashBiMap.this;
                }

                /* access modifiers changed from: 0000 */
                public K output(BiEntry<K, V> biEntry) {
                    return biEntry.key;
                }
            };
        }

        public boolean remove(@Nullable Object obj) {
            BiEntry access$300 = HashBiMap.this.seekByKey(obj, Hashing.smearedHash(obj));
            if (access$300 == null) {
                return false;
            }
            HashBiMap.this.delete(access$300);
            access$300.prevInKeyInsertionOrder = null;
            access$300.nextInKeyInsertionOrder = null;
            return true;
        }
    }

    public /* bridge */ /* synthetic */ Set entrySet() {
        return super.entrySet();
    }

    public static <K, V> HashBiMap<K, V> create() {
        return create(16);
    }

    public static <K, V> HashBiMap<K, V> create(int i) {
        return new HashBiMap<>(i);
    }

    public static <K, V> HashBiMap<K, V> create(Map<? extends K, ? extends V> map) {
        HashBiMap<K, V> create = create(map.size());
        create.putAll(map);
        return create;
    }

    private HashBiMap(int i) {
        init(i);
    }

    private void init(int i) {
        CollectPreconditions.checkNonnegative(i, "expectedSize");
        int closedTableSize = Hashing.closedTableSize(i, LOAD_FACTOR);
        this.hashTableKToV = createTable(closedTableSize);
        this.hashTableVToK = createTable(closedTableSize);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        this.size = 0;
        this.mask = closedTableSize - 1;
        this.modCount = 0;
    }

    /* access modifiers changed from: private */
    public void delete(BiEntry<K, V> biEntry) {
        int i = biEntry.keyHash & this.mask;
        BiEntry<K, V> biEntry2 = null;
        for (BiEntry<K, V> biEntry3 = this.hashTableKToV[i]; biEntry3 != biEntry; biEntry3 = biEntry3.nextInKToVBucket) {
            biEntry2 = biEntry3;
        }
        if (biEntry2 == null) {
            this.hashTableKToV[i] = biEntry.nextInKToVBucket;
        } else {
            biEntry2.nextInKToVBucket = biEntry.nextInKToVBucket;
        }
        int i2 = biEntry.valueHash & this.mask;
        BiEntry<K, V> biEntry4 = this.hashTableVToK[i2];
        BiEntry<K, V> biEntry5 = null;
        while (biEntry4 != biEntry) {
            BiEntry<K, V> biEntry6 = biEntry4;
            biEntry4 = biEntry4.nextInVToKBucket;
            biEntry5 = biEntry6;
        }
        if (biEntry5 == null) {
            this.hashTableVToK[i2] = biEntry.nextInVToKBucket;
        } else {
            biEntry5.nextInVToKBucket = biEntry.nextInVToKBucket;
        }
        if (biEntry.prevInKeyInsertionOrder == null) {
            this.firstInKeyInsertionOrder = biEntry.nextInKeyInsertionOrder;
        } else {
            biEntry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = biEntry.nextInKeyInsertionOrder;
        }
        if (biEntry.nextInKeyInsertionOrder == null) {
            this.lastInKeyInsertionOrder = biEntry.prevInKeyInsertionOrder;
        } else {
            biEntry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = biEntry.prevInKeyInsertionOrder;
        }
        this.size--;
        this.modCount++;
    }

    /* access modifiers changed from: private */
    public void insert(BiEntry<K, V> biEntry, @Nullable BiEntry<K, V> biEntry2) {
        int i = biEntry.keyHash & this.mask;
        BiEntry<K, V>[] biEntryArr = this.hashTableKToV;
        biEntry.nextInKToVBucket = biEntryArr[i];
        biEntryArr[i] = biEntry;
        int i2 = biEntry.valueHash & this.mask;
        BiEntry<K, V>[] biEntryArr2 = this.hashTableVToK;
        biEntry.nextInVToKBucket = biEntryArr2[i2];
        biEntryArr2[i2] = biEntry;
        if (biEntry2 == null) {
            BiEntry<K, V> biEntry3 = this.lastInKeyInsertionOrder;
            biEntry.prevInKeyInsertionOrder = biEntry3;
            biEntry.nextInKeyInsertionOrder = null;
            if (biEntry3 == null) {
                this.firstInKeyInsertionOrder = biEntry;
            } else {
                biEntry3.nextInKeyInsertionOrder = biEntry;
            }
            this.lastInKeyInsertionOrder = biEntry;
        } else {
            biEntry.prevInKeyInsertionOrder = biEntry2.prevInKeyInsertionOrder;
            if (biEntry.prevInKeyInsertionOrder == null) {
                this.firstInKeyInsertionOrder = biEntry;
            } else {
                biEntry.prevInKeyInsertionOrder.nextInKeyInsertionOrder = biEntry;
            }
            biEntry.nextInKeyInsertionOrder = biEntry2.nextInKeyInsertionOrder;
            if (biEntry.nextInKeyInsertionOrder == null) {
                this.lastInKeyInsertionOrder = biEntry;
            } else {
                biEntry.nextInKeyInsertionOrder.prevInKeyInsertionOrder = biEntry;
            }
        }
        this.size++;
        this.modCount++;
    }

    /* access modifiers changed from: private */
    public BiEntry<K, V> seekByKey(@Nullable Object obj, int i) {
        for (BiEntry<K, V> biEntry = this.hashTableKToV[this.mask & i]; biEntry != null; biEntry = biEntry.nextInKToVBucket) {
            if (i == biEntry.keyHash && Objects.equal(obj, biEntry.key)) {
                return biEntry;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public BiEntry<K, V> seekByValue(@Nullable Object obj, int i) {
        for (BiEntry<K, V> biEntry = this.hashTableVToK[this.mask & i]; biEntry != null; biEntry = biEntry.nextInVToKBucket) {
            if (i == biEntry.valueHash && Objects.equal(obj, biEntry.value)) {
                return biEntry;
            }
        }
        return null;
    }

    public boolean containsKey(@Nullable Object obj) {
        return seekByKey(obj, Hashing.smearedHash(obj)) != null;
    }

    public boolean containsValue(@Nullable Object obj) {
        return seekByValue(obj, Hashing.smearedHash(obj)) != null;
    }

    @Nullable
    public V get(@Nullable Object obj) {
        return Maps.valueOrNull(seekByKey(obj, Hashing.smearedHash(obj)));
    }

    @CanIgnoreReturnValue
    public V put(@Nullable K k, @Nullable V v) {
        return put(k, v, false);
    }

    @CanIgnoreReturnValue
    public V forcePut(@Nullable K k, @Nullable V v) {
        return put(k, v, true);
    }

    private V put(@Nullable K k, @Nullable V v, boolean z) {
        int smearedHash = Hashing.smearedHash(k);
        int smearedHash2 = Hashing.smearedHash(v);
        BiEntry seekByKey = seekByKey(k, smearedHash);
        if (seekByKey != null && smearedHash2 == seekByKey.valueHash && Objects.equal(v, seekByKey.value)) {
            return v;
        }
        BiEntry seekByValue = seekByValue(v, smearedHash2);
        if (seekByValue != null) {
            if (z) {
                delete(seekByValue);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("value already present: ");
                sb.append(v);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        BiEntry biEntry = new BiEntry(k, smearedHash, v, smearedHash2);
        if (seekByKey != null) {
            delete(seekByKey);
            insert(biEntry, seekByKey);
            seekByKey.prevInKeyInsertionOrder = null;
            seekByKey.nextInKeyInsertionOrder = null;
            rehashIfNecessary();
            return seekByKey.value;
        }
        insert(biEntry, null);
        rehashIfNecessary();
        return null;
    }

    /* access modifiers changed from: private */
    @Nullable
    public K putInverse(@Nullable V v, @Nullable K k, boolean z) {
        int smearedHash = Hashing.smearedHash(v);
        int smearedHash2 = Hashing.smearedHash(k);
        BiEntry seekByValue = seekByValue(v, smearedHash);
        if (seekByValue != null && smearedHash2 == seekByValue.keyHash && Objects.equal(k, seekByValue.key)) {
            return k;
        }
        BiEntry seekByKey = seekByKey(k, smearedHash2);
        if (seekByKey != null) {
            if (z) {
                delete(seekByKey);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("value already present: ");
                sb.append(k);
                throw new IllegalArgumentException(sb.toString());
            }
        }
        if (seekByValue != null) {
            delete(seekByValue);
        }
        insert(new BiEntry(k, smearedHash2, v, smearedHash), seekByKey);
        if (seekByKey != null) {
            seekByKey.prevInKeyInsertionOrder = null;
            seekByKey.nextInKeyInsertionOrder = null;
        }
        rehashIfNecessary();
        return Maps.keyOrNull(seekByValue);
    }

    private void rehashIfNecessary() {
        BiEntry<K, V>[] biEntryArr = this.hashTableKToV;
        if (Hashing.needsResizing(this.size, biEntryArr.length, LOAD_FACTOR)) {
            int length = biEntryArr.length * 2;
            this.hashTableKToV = createTable(length);
            this.hashTableVToK = createTable(length);
            this.mask = length - 1;
            this.size = 0;
            for (BiEntry<K, V> biEntry = this.firstInKeyInsertionOrder; biEntry != null; biEntry = biEntry.nextInKeyInsertionOrder) {
                insert(biEntry, biEntry);
            }
            this.modCount++;
        }
    }

    private BiEntry<K, V>[] createTable(int i) {
        return new BiEntry[i];
    }

    @CanIgnoreReturnValue
    public V remove(@Nullable Object obj) {
        BiEntry seekByKey = seekByKey(obj, Hashing.smearedHash(obj));
        if (seekByKey == null) {
            return null;
        }
        delete(seekByKey);
        seekByKey.prevInKeyInsertionOrder = null;
        seekByKey.nextInKeyInsertionOrder = null;
        return seekByKey.value;
    }

    public void clear() {
        this.size = 0;
        Arrays.fill(this.hashTableKToV, null);
        Arrays.fill(this.hashTableVToK, null);
        this.firstInKeyInsertionOrder = null;
        this.lastInKeyInsertionOrder = null;
        this.modCount++;
    }

    public int size() {
        return this.size;
    }

    public Set<K> keySet() {
        return new KeySet();
    }

    public Set<V> values() {
        return inverse().keySet();
    }

    /* access modifiers changed from: 0000 */
    public Iterator<Entry<K, V>> entryIterator() {
        return new Itr<Entry<K, V>>() {

            /* renamed from: com.google.common.collect.HashBiMap$1$MapEntry */
            class MapEntry extends AbstractMapEntry<K, V> {
                BiEntry<K, V> delegate;

                MapEntry(BiEntry<K, V> biEntry) {
                    this.delegate = biEntry;
                }

                public K getKey() {
                    return this.delegate.key;
                }

                public V getValue() {
                    return this.delegate.value;
                }

                public V setValue(V v) {
                    V v2 = this.delegate.value;
                    int smearedHash = Hashing.smearedHash(v);
                    if (smearedHash == this.delegate.valueHash && Objects.equal(v, v2)) {
                        return v;
                    }
                    Preconditions.checkArgument(HashBiMap.this.seekByValue(v, smearedHash) == null, "value already present: %s", (Object) v);
                    HashBiMap.this.delete(this.delegate);
                    BiEntry<K, V> biEntry = new BiEntry<>(this.delegate.key, this.delegate.keyHash, v, smearedHash);
                    HashBiMap.this.insert(biEntry, this.delegate);
                    BiEntry<K, V> biEntry2 = this.delegate;
                    biEntry2.prevInKeyInsertionOrder = null;
                    biEntry2.nextInKeyInsertionOrder = null;
                    C11931 r6 = C11931.this;
                    r6.expectedModCount = HashBiMap.this.modCount;
                    if (C11931.this.toRemove == this.delegate) {
                        C11931.this.toRemove = biEntry;
                    }
                    this.delegate = biEntry;
                    return v2;
                }
            }

            /* access modifiers changed from: 0000 */
            public Entry<K, V> output(BiEntry<K, V> biEntry) {
                return new MapEntry(biEntry);
            }
        };
    }

    public BiMap<V, K> inverse() {
        BiMap<V, K> biMap = this.inverse;
        if (biMap != null) {
            return biMap;
        }
        Inverse inverse2 = new Inverse();
        this.inverse = inverse2;
        return inverse2;
    }

    @GwtIncompatible
    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        Serialization.writeMap(this, objectOutputStream);
    }

    @GwtIncompatible
    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        init(16);
        Serialization.populateMap(this, objectInputStream, Serialization.readCount(objectInputStream));
    }
}
