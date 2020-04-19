package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.concurrent.LazyInit;
import com.google.j2objc.annotations.RetainedWith;
import java.util.Map.Entry;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class SingletonImmutableBiMap<K, V> extends ImmutableBiMap<K, V> {
    @RetainedWith
    @LazyInit
    transient ImmutableBiMap<V, K> inverse;
    final transient K singleKey;
    final transient V singleValue;

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return false;
    }

    public int size() {
        return 1;
    }

    SingletonImmutableBiMap(K k, V v) {
        CollectPreconditions.checkEntryNotNull(k, v);
        this.singleKey = k;
        this.singleValue = v;
    }

    private SingletonImmutableBiMap(K k, V v, ImmutableBiMap<V, K> immutableBiMap) {
        this.singleKey = k;
        this.singleValue = v;
        this.inverse = immutableBiMap;
    }

    public V get(@Nullable Object obj) {
        if (this.singleKey.equals(obj)) {
            return this.singleValue;
        }
        return null;
    }

    public boolean containsKey(@Nullable Object obj) {
        return this.singleKey.equals(obj);
    }

    public boolean containsValue(@Nullable Object obj) {
        return this.singleValue.equals(obj);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<Entry<K, V>> createEntrySet() {
        return ImmutableSet.m156of(Maps.immutableEntry(this.singleKey, this.singleValue));
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSet<K> createKeySet() {
        return ImmutableSet.m156of(this.singleKey);
    }

    public ImmutableBiMap<V, K> inverse() {
        ImmutableBiMap<V, K> immutableBiMap = this.inverse;
        if (immutableBiMap != null) {
            return immutableBiMap;
        }
        SingletonImmutableBiMap singletonImmutableBiMap = new SingletonImmutableBiMap(this.singleValue, this.singleKey, this);
        this.inverse = singletonImmutableBiMap;
        return singletonImmutableBiMap;
    }
}
