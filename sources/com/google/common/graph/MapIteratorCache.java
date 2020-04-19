package com.google.common.graph;

import com.google.common.base.Preconditions;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

class MapIteratorCache<K, V> {
    /* access modifiers changed from: private */
    public final Map<K, V> backingMap;
    /* access modifiers changed from: private */
    @Nullable
    public transient Entry<K, V> entrySetCache;

    MapIteratorCache(Map<K, V> map) {
        this.backingMap = (Map) Preconditions.checkNotNull(map);
    }

    @CanIgnoreReturnValue
    public V put(@Nullable K k, @Nullable V v) {
        clearCache();
        return this.backingMap.put(k, v);
    }

    @CanIgnoreReturnValue
    public V remove(@Nullable Object obj) {
        clearCache();
        return this.backingMap.remove(obj);
    }

    public void clear() {
        clearCache();
        this.backingMap.clear();
    }

    public V get(@Nullable Object obj) {
        V ifCached = getIfCached(obj);
        return ifCached != null ? ifCached : getWithoutCaching(obj);
    }

    public final V getWithoutCaching(@Nullable Object obj) {
        return this.backingMap.get(obj);
    }

    public final boolean containsKey(@Nullable Object obj) {
        return getIfCached(obj) != null || this.backingMap.containsKey(obj);
    }

    public final Set<K> unmodifiableKeySet() {
        return new AbstractSet<K>() {
            public UnmodifiableIterator<K> iterator() {
                final Iterator it = MapIteratorCache.this.backingMap.entrySet().iterator();
                return new UnmodifiableIterator<K>() {
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    public K next() {
                        Entry entry = (Entry) it.next();
                        MapIteratorCache.this.entrySetCache = entry;
                        return entry.getKey();
                    }
                };
            }

            public int size() {
                return MapIteratorCache.this.backingMap.size();
            }

            public boolean contains(@Nullable Object obj) {
                return MapIteratorCache.this.containsKey(obj);
            }
        };
    }

    /* access modifiers changed from: protected */
    public V getIfCached(@Nullable Object obj) {
        Entry<K, V> entry = this.entrySetCache;
        if (entry == null || entry.getKey() != obj) {
            return null;
        }
        return entry.getValue();
    }

    /* access modifiers changed from: protected */
    public void clearCache() {
        this.entrySetCache = null;
    }
}