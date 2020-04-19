package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
@Deprecated
@Beta
public final class MapConstraints {

    static class ConstrainedAsMapEntries<K, V> extends ForwardingSet<Entry<K, Collection<V>>> {
        /* access modifiers changed from: private */
        public final MapConstraint<? super K, ? super V> constraint;
        private final Set<Entry<K, Collection<V>>> entries;

        ConstrainedAsMapEntries(Set<Entry<K, Collection<V>>> set, MapConstraint<? super K, ? super V> mapConstraint) {
            this.entries = set;
            this.constraint = mapConstraint;
        }

        /* access modifiers changed from: protected */
        public Set<Entry<K, Collection<V>>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, Collection<V>>> iterator() {
            return new TransformedIterator<Entry<K, Collection<V>>, Entry<K, Collection<V>>>(this.entries.iterator()) {
                /* access modifiers changed from: 0000 */
                public Entry<K, Collection<V>> transform(Entry<K, Collection<V>> entry) {
                    return MapConstraints.constrainedAsMapEntry(entry, ConstrainedAsMapEntries.this.constraint);
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }

        public boolean contains(Object obj) {
            return Maps.containsEntryImpl(delegate(), obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return standardContainsAll(collection);
        }

        public boolean equals(@Nullable Object obj) {
            return standardEquals(obj);
        }

        public int hashCode() {
            return standardHashCode();
        }

        public boolean remove(Object obj) {
            return Maps.removeEntryImpl(delegate(), obj);
        }

        public boolean removeAll(Collection<?> collection) {
            return standardRemoveAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return standardRetainAll(collection);
        }
    }

    private static class ConstrainedAsMapValues<K, V> extends ForwardingCollection<Collection<V>> {
        final Collection<Collection<V>> delegate;
        final Set<Entry<K, Collection<V>>> entrySet;

        ConstrainedAsMapValues(Collection<Collection<V>> collection, Set<Entry<K, Collection<V>>> set) {
            this.delegate = collection;
            this.entrySet = set;
        }

        /* access modifiers changed from: protected */
        public Collection<Collection<V>> delegate() {
            return this.delegate;
        }

        public Iterator<Collection<V>> iterator() {
            final Iterator it = this.entrySet.iterator();
            return new Iterator<Collection<V>>() {
                public boolean hasNext() {
                    return it.hasNext();
                }

                public Collection<V> next() {
                    return (Collection) ((Entry) it.next()).getValue();
                }

                public void remove() {
                    it.remove();
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }

        public boolean contains(Object obj) {
            return standardContains(obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return standardContainsAll(collection);
        }

        public boolean remove(Object obj) {
            return standardRemove(obj);
        }

        public boolean removeAll(Collection<?> collection) {
            return standardRemoveAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return standardRetainAll(collection);
        }
    }

    private static class ConstrainedEntries<K, V> extends ForwardingCollection<Entry<K, V>> {
        final MapConstraint<? super K, ? super V> constraint;
        final Collection<Entry<K, V>> entries;

        ConstrainedEntries(Collection<Entry<K, V>> collection, MapConstraint<? super K, ? super V> mapConstraint) {
            this.entries = collection;
            this.constraint = mapConstraint;
        }

        /* access modifiers changed from: protected */
        public Collection<Entry<K, V>> delegate() {
            return this.entries;
        }

        public Iterator<Entry<K, V>> iterator() {
            return new TransformedIterator<Entry<K, V>, Entry<K, V>>(this.entries.iterator()) {
                /* access modifiers changed from: 0000 */
                public Entry<K, V> transform(Entry<K, V> entry) {
                    return MapConstraints.constrainedEntry(entry, ConstrainedEntries.this.constraint);
                }
            };
        }

        public Object[] toArray() {
            return standardToArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return standardToArray(tArr);
        }

        public boolean contains(Object obj) {
            return Maps.containsEntryImpl(delegate(), obj);
        }

        public boolean containsAll(Collection<?> collection) {
            return standardContainsAll(collection);
        }

        public boolean remove(Object obj) {
            return Maps.removeEntryImpl(delegate(), obj);
        }

        public boolean removeAll(Collection<?> collection) {
            return standardRemoveAll(collection);
        }

        public boolean retainAll(Collection<?> collection) {
            return standardRetainAll(collection);
        }
    }

    static class ConstrainedEntrySet<K, V> extends ConstrainedEntries<K, V> implements Set<Entry<K, V>> {
        ConstrainedEntrySet(Set<Entry<K, V>> set, MapConstraint<? super K, ? super V> mapConstraint) {
            super(set, mapConstraint);
        }

        public boolean equals(@Nullable Object obj) {
            return Sets.equalsImpl(this, obj);
        }

        public int hashCode() {
            return Sets.hashCodeImpl(this);
        }
    }

    private static class ConstrainedListMultimap<K, V> extends ConstrainedMultimap<K, V> implements ListMultimap<K, V> {
        ConstrainedListMultimap(ListMultimap<K, V> listMultimap, MapConstraint<? super K, ? super V> mapConstraint) {
            super(listMultimap, mapConstraint);
        }

        public List<V> get(K k) {
            return (List) super.get(k);
        }

        public List<V> removeAll(Object obj) {
            return (List) super.removeAll(obj);
        }

        public List<V> replaceValues(K k, Iterable<? extends V> iterable) {
            return (List) super.replaceValues(k, iterable);
        }
    }

    static class ConstrainedMap<K, V> extends ForwardingMap<K, V> {
        final MapConstraint<? super K, ? super V> constraint;
        private final Map<K, V> delegate;
        private transient Set<Entry<K, V>> entrySet;

        ConstrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> mapConstraint) {
            this.delegate = (Map) Preconditions.checkNotNull(map);
            this.constraint = (MapConstraint) Preconditions.checkNotNull(mapConstraint);
        }

        /* access modifiers changed from: protected */
        public Map<K, V> delegate() {
            return this.delegate;
        }

        public Set<Entry<K, V>> entrySet() {
            Set<Entry<K, V>> set = this.entrySet;
            if (set != null) {
                return set;
            }
            Set<Entry<K, V>> access$000 = MapConstraints.constrainedEntrySet(this.delegate.entrySet(), this.constraint);
            this.entrySet = access$000;
            return access$000;
        }

        @CanIgnoreReturnValue
        public V put(K k, V v) {
            this.constraint.checkKeyValue(k, v);
            return this.delegate.put(k, v);
        }

        public void putAll(Map<? extends K, ? extends V> map) {
            this.delegate.putAll(MapConstraints.checkMap(map, this.constraint));
        }
    }

    private static class ConstrainedMultimap<K, V> extends ForwardingMultimap<K, V> implements Serializable {
        transient Map<K, Collection<V>> asMap;
        final MapConstraint<? super K, ? super V> constraint;
        final Multimap<K, V> delegate;
        transient Collection<Entry<K, V>> entries;

        public ConstrainedMultimap(Multimap<K, V> multimap, MapConstraint<? super K, ? super V> mapConstraint) {
            this.delegate = (Multimap) Preconditions.checkNotNull(multimap);
            this.constraint = (MapConstraint) Preconditions.checkNotNull(mapConstraint);
        }

        /* access modifiers changed from: protected */
        public Multimap<K, V> delegate() {
            return this.delegate;
        }

        public Map<K, Collection<V>> asMap() {
            Map<K, Collection<V>> map = this.asMap;
            if (map != null) {
                return map;
            }
            final Map asMap2 = this.delegate.asMap();
            AnonymousClass1AsMap r1 = new ForwardingMap<K, Collection<V>>() {
                Set<Entry<K, Collection<V>>> entrySet;
                Collection<Collection<V>> values;

                /* access modifiers changed from: protected */
                public Map<K, Collection<V>> delegate() {
                    return asMap2;
                }

                public Set<Entry<K, Collection<V>>> entrySet() {
                    Set<Entry<K, Collection<V>>> set = this.entrySet;
                    if (set != null) {
                        return set;
                    }
                    Set<Entry<K, Collection<V>>> access$200 = MapConstraints.constrainedAsMapEntries(asMap2.entrySet(), ConstrainedMultimap.this.constraint);
                    this.entrySet = access$200;
                    return access$200;
                }

                public Collection<V> get(Object obj) {
                    try {
                        Collection<V> collection = ConstrainedMultimap.this.get(obj);
                        if (collection.isEmpty()) {
                            collection = null;
                        }
                        return collection;
                    } catch (ClassCastException unused) {
                        return null;
                    }
                }

                public Collection<Collection<V>> values() {
                    Collection<Collection<V>> collection = this.values;
                    if (collection != null) {
                        return collection;
                    }
                    ConstrainedAsMapValues constrainedAsMapValues = new ConstrainedAsMapValues(delegate().values(), entrySet());
                    this.values = constrainedAsMapValues;
                    return constrainedAsMapValues;
                }

                public boolean containsValue(Object obj) {
                    return values().contains(obj);
                }
            };
            this.asMap = r1;
            return r1;
        }

        public Collection<Entry<K, V>> entries() {
            Collection<Entry<K, V>> collection = this.entries;
            if (collection != null) {
                return collection;
            }
            Collection<Entry<K, V>> access$300 = MapConstraints.constrainedEntries(this.delegate.entries(), this.constraint);
            this.entries = access$300;
            return access$300;
        }

        public Collection<V> get(final K k) {
            return Constraints.constrainedTypePreservingCollection(this.delegate.get(k), new Constraint<V>() {
                public V checkElement(V v) {
                    ConstrainedMultimap.this.constraint.checkKeyValue(k, v);
                    return v;
                }
            });
        }

        public boolean put(K k, V v) {
            this.constraint.checkKeyValue(k, v);
            return this.delegate.put(k, v);
        }

        public boolean putAll(K k, Iterable<? extends V> iterable) {
            return this.delegate.putAll(k, MapConstraints.checkValues(k, iterable, this.constraint));
        }

        public boolean putAll(Multimap<? extends K, ? extends V> multimap) {
            boolean z = false;
            for (Entry entry : multimap.entries()) {
                z |= put(entry.getKey(), entry.getValue());
            }
            return z;
        }

        public Collection<V> replaceValues(K k, Iterable<? extends V> iterable) {
            return this.delegate.replaceValues(k, MapConstraints.checkValues(k, iterable, this.constraint));
        }
    }

    private MapConstraints() {
    }

    public static <K, V> Map<K, V> constrainedMap(Map<K, V> map, MapConstraint<? super K, ? super V> mapConstraint) {
        return new ConstrainedMap(map, mapConstraint);
    }

    public static <K, V> ListMultimap<K, V> constrainedListMultimap(ListMultimap<K, V> listMultimap, MapConstraint<? super K, ? super V> mapConstraint) {
        return new ConstrainedListMultimap(listMultimap, mapConstraint);
    }

    /* access modifiers changed from: private */
    public static <K, V> Entry<K, V> constrainedEntry(final Entry<K, V> entry, final MapConstraint<? super K, ? super V> mapConstraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(mapConstraint);
        return new ForwardingMapEntry<K, V>() {
            /* access modifiers changed from: protected */
            public Entry<K, V> delegate() {
                return entry;
            }

            public V setValue(V v) {
                mapConstraint.checkKeyValue(getKey(), v);
                return entry.setValue(v);
            }
        };
    }

    /* access modifiers changed from: private */
    public static <K, V> Entry<K, Collection<V>> constrainedAsMapEntry(final Entry<K, Collection<V>> entry, final MapConstraint<? super K, ? super V> mapConstraint) {
        Preconditions.checkNotNull(entry);
        Preconditions.checkNotNull(mapConstraint);
        return new ForwardingMapEntry<K, Collection<V>>() {
            /* access modifiers changed from: protected */
            public Entry<K, Collection<V>> delegate() {
                return entry;
            }

            public Collection<V> getValue() {
                return Constraints.constrainedTypePreservingCollection((Collection) entry.getValue(), new Constraint<V>() {
                    public V checkElement(V v) {
                        mapConstraint.checkKeyValue(C12572.this.getKey(), v);
                        return v;
                    }
                });
            }
        };
    }

    /* access modifiers changed from: private */
    public static <K, V> Set<Entry<K, Collection<V>>> constrainedAsMapEntries(Set<Entry<K, Collection<V>>> set, MapConstraint<? super K, ? super V> mapConstraint) {
        return new ConstrainedAsMapEntries(set, mapConstraint);
    }

    /* access modifiers changed from: private */
    public static <K, V> Collection<Entry<K, V>> constrainedEntries(Collection<Entry<K, V>> collection, MapConstraint<? super K, ? super V> mapConstraint) {
        if (collection instanceof Set) {
            return constrainedEntrySet((Set) collection, mapConstraint);
        }
        return new ConstrainedEntries(collection, mapConstraint);
    }

    /* access modifiers changed from: private */
    public static <K, V> Set<Entry<K, V>> constrainedEntrySet(Set<Entry<K, V>> set, MapConstraint<? super K, ? super V> mapConstraint) {
        return new ConstrainedEntrySet(set, mapConstraint);
    }

    /* access modifiers changed from: private */
    public static <K, V> Collection<V> checkValues(K k, Iterable<? extends V> iterable, MapConstraint<? super K, ? super V> mapConstraint) {
        ArrayList<Object> newArrayList = Lists.newArrayList(iterable);
        for (Object checkKeyValue : newArrayList) {
            mapConstraint.checkKeyValue(k, checkKeyValue);
        }
        return newArrayList;
    }

    /* access modifiers changed from: private */
    public static <K, V> Map<K, V> checkMap(Map<? extends K, ? extends V> map, MapConstraint<? super K, ? super V> mapConstraint) {
        LinkedHashMap linkedHashMap = new LinkedHashMap(map);
        for (Entry entry : linkedHashMap.entrySet()) {
            mapConstraint.checkKeyValue(entry.getKey(), entry.getValue());
        }
        return linkedHashMap;
    }
}
