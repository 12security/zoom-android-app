package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.collect.Multiset.Entry;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible
abstract class AbstractMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
    private transient Set<E> elementSet;
    private transient Set<Entry<E>> entrySet;

    class ElementSet extends ElementSet<E> {
        ElementSet() {
        }

        /* access modifiers changed from: 0000 */
        public Multiset<E> multiset() {
            return AbstractMultiset.this;
        }
    }

    class EntrySet extends EntrySet<E> {
        EntrySet() {
        }

        /* access modifiers changed from: 0000 */
        public Multiset<E> multiset() {
            return AbstractMultiset.this;
        }

        public Iterator<Entry<E>> iterator() {
            return AbstractMultiset.this.entryIterator();
        }

        public int size() {
            return AbstractMultiset.this.distinctElements();
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract int distinctElements();

    /* access modifiers changed from: 0000 */
    public abstract Iterator<Entry<E>> entryIterator();

    AbstractMultiset() {
    }

    public int size() {
        return Multisets.sizeImpl(this);
    }

    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    public boolean contains(@Nullable Object obj) {
        return count(obj) > 0;
    }

    public Iterator<E> iterator() {
        return Multisets.iteratorImpl(this);
    }

    public int count(@Nullable Object obj) {
        for (Entry entry : entrySet()) {
            if (Objects.equal(entry.getElement(), obj)) {
                return entry.getCount();
            }
        }
        return 0;
    }

    @CanIgnoreReturnValue
    public boolean add(@Nullable E e) {
        add(e, 1);
        return true;
    }

    @CanIgnoreReturnValue
    public int add(@Nullable E e, int i) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    public boolean remove(@Nullable Object obj) {
        return remove(obj, 1) > 0;
    }

    @CanIgnoreReturnValue
    public int remove(@Nullable Object obj, int i) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    public int setCount(@Nullable E e, int i) {
        return Multisets.setCountImpl(this, e, i);
    }

    @CanIgnoreReturnValue
    public boolean setCount(@Nullable E e, int i, int i2) {
        return Multisets.setCountImpl(this, e, i, i2);
    }

    @CanIgnoreReturnValue
    public boolean addAll(Collection<? extends E> collection) {
        return Multisets.addAllImpl(this, collection);
    }

    @CanIgnoreReturnValue
    public boolean removeAll(Collection<?> collection) {
        return Multisets.removeAllImpl(this, collection);
    }

    @CanIgnoreReturnValue
    public boolean retainAll(Collection<?> collection) {
        return Multisets.retainAllImpl(this, collection);
    }

    public void clear() {
        Iterators.clear(entryIterator());
    }

    public Set<E> elementSet() {
        Set<E> set = this.elementSet;
        if (set != null) {
            return set;
        }
        Set<E> createElementSet = createElementSet();
        this.elementSet = createElementSet;
        return createElementSet;
    }

    /* access modifiers changed from: 0000 */
    public Set<E> createElementSet() {
        return new ElementSet();
    }

    public Set<Entry<E>> entrySet() {
        Set<Entry<E>> set = this.entrySet;
        if (set != null) {
            return set;
        }
        Set<Entry<E>> createEntrySet = createEntrySet();
        this.entrySet = createEntrySet;
        return createEntrySet;
    }

    /* access modifiers changed from: 0000 */
    public Set<Entry<E>> createEntrySet() {
        return new EntrySet();
    }

    public boolean equals(@Nullable Object obj) {
        return Multisets.equalsImpl(this, obj);
    }

    public int hashCode() {
        return entrySet().hashCode();
    }

    public String toString() {
        return entrySet().toString();
    }
}
