package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSortedSet<E> extends ImmutableSortedSet<E> {
    static final RegularImmutableSortedSet<Comparable> NATURAL_EMPTY_SET = new RegularImmutableSortedSet<>(ImmutableList.m113of(), Ordering.natural());
    private final transient ImmutableList<E> elements;

    RegularImmutableSortedSet(ImmutableList<E> immutableList, Comparator<? super E> comparator) {
        super(comparator);
        this.elements = immutableList;
    }

    public UnmodifiableIterator<E> iterator() {
        return this.elements.iterator();
    }

    @GwtIncompatible
    public UnmodifiableIterator<E> descendingIterator() {
        return this.elements.reverse().iterator();
    }

    public int size() {
        return this.elements.size();
    }

    public boolean contains(@Nullable Object obj) {
        boolean z = false;
        if (obj != null) {
            try {
                if (unsafeBinarySearch(obj) >= 0) {
                    z = true;
                }
            } catch (ClassCastException unused) {
                return false;
            }
        }
        return z;
    }

    public boolean containsAll(Collection<?> collection) {
        if (collection instanceof Multiset) {
            collection = ((Multiset) collection).elementSet();
        }
        if (!SortedIterables.hasSameComparator(comparator(), collection) || collection.size() <= 1) {
            return super.containsAll(collection);
        }
        PeekingIterator peekingIterator = Iterators.peekingIterator((Iterator<? extends T>) iterator());
        Iterator it = collection.iterator();
        Object next = it.next();
        while (peekingIterator.hasNext()) {
            try {
                int unsafeCompare = unsafeCompare(peekingIterator.peek(), next);
                if (unsafeCompare < 0) {
                    peekingIterator.next();
                } else if (unsafeCompare == 0) {
                    if (!it.hasNext()) {
                        return true;
                    }
                    next = it.next();
                } else if (unsafeCompare > 0) {
                    return false;
                }
            } catch (NullPointerException unused) {
                return false;
            } catch (ClassCastException unused2) {
                return false;
            }
        }
        return false;
    }

    private int unsafeBinarySearch(Object obj) throws ClassCastException {
        return Collections.binarySearch(this.elements, obj, unsafeComparator());
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return this.elements.isPartialView();
    }

    /* access modifiers changed from: 0000 */
    public int copyIntoArray(Object[] objArr, int i) {
        return this.elements.copyIntoArray(objArr, i);
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Set set = (Set) obj;
        if (size() != set.size()) {
            return false;
        }
        if (isEmpty()) {
            return true;
        }
        if (!SortedIterables.hasSameComparator(this.comparator, set)) {
            return containsAll(set);
        }
        Iterator it = set.iterator();
        try {
            UnmodifiableIterator it2 = iterator();
            while (it2.hasNext()) {
                Object next = it2.next();
                Object next2 = it.next();
                if (next2 != null) {
                    if (unsafeCompare(next, next2) != 0) {
                    }
                }
                return false;
            }
            return true;
        } catch (ClassCastException unused) {
            return false;
        } catch (NoSuchElementException unused2) {
            return false;
        }
    }

    public E first() {
        if (!isEmpty()) {
            return this.elements.get(0);
        }
        throw new NoSuchElementException();
    }

    public E last() {
        if (!isEmpty()) {
            return this.elements.get(size() - 1);
        }
        throw new NoSuchElementException();
    }

    public E lower(E e) {
        int headIndex = headIndex(e, false) - 1;
        if (headIndex == -1) {
            return null;
        }
        return this.elements.get(headIndex);
    }

    public E floor(E e) {
        int headIndex = headIndex(e, true) - 1;
        if (headIndex == -1) {
            return null;
        }
        return this.elements.get(headIndex);
    }

    public E ceiling(E e) {
        int tailIndex = tailIndex(e, true);
        if (tailIndex == size()) {
            return null;
        }
        return this.elements.get(tailIndex);
    }

    public E higher(E e) {
        int tailIndex = tailIndex(e, false);
        if (tailIndex == size()) {
            return null;
        }
        return this.elements.get(tailIndex);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> headSetImpl(E e, boolean z) {
        return getSubSet(0, headIndex(e, z));
    }

    /* access modifiers changed from: 0000 */
    public int headIndex(E e, boolean z) {
        return SortedLists.binarySearch((List<? extends E>) this.elements, Preconditions.checkNotNull(e), comparator(), z ? KeyPresentBehavior.FIRST_AFTER : KeyPresentBehavior.FIRST_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> subSetImpl(E e, boolean z, E e2, boolean z2) {
        return tailSetImpl(e, z).headSetImpl(e2, z2);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> tailSetImpl(E e, boolean z) {
        return getSubSet(tailIndex(e, z), size());
    }

    /* access modifiers changed from: 0000 */
    public int tailIndex(E e, boolean z) {
        return SortedLists.binarySearch((List<? extends E>) this.elements, Preconditions.checkNotNull(e), comparator(), z ? KeyPresentBehavior.FIRST_PRESENT : KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
    }

    /* access modifiers changed from: 0000 */
    public Comparator<Object> unsafeComparator() {
        return this.comparator;
    }

    /* access modifiers changed from: 0000 */
    public RegularImmutableSortedSet<E> getSubSet(int i, int i2) {
        if (i == 0 && i2 == size()) {
            return this;
        }
        if (i < i2) {
            return new RegularImmutableSortedSet<>(this.elements.subList(i, i2), this.comparator);
        }
        return emptySet(this.comparator);
    }

    /* access modifiers changed from: 0000 */
    public int indexOf(@Nullable Object obj) {
        if (obj == null) {
            return -1;
        }
        try {
            int binarySearch = SortedLists.binarySearch((List<? extends E>) this.elements, obj, unsafeComparator(), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.INVERTED_INSERTION_INDEX);
            if (binarySearch < 0) {
                binarySearch = -1;
            }
            return binarySearch;
        } catch (ClassCastException unused) {
            return -1;
        }
    }

    /* access modifiers changed from: 0000 */
    public ImmutableList<E> createAsList() {
        return size() <= 1 ? this.elements : new ImmutableSortedAsList(this, this.elements);
    }

    /* access modifiers changed from: 0000 */
    public ImmutableSortedSet<E> createDescendingSet() {
        Ordering reverse = Ordering.from(this.comparator).reverse();
        return isEmpty() ? emptySet(reverse) : new RegularImmutableSortedSet(this.elements.reverse(), reverse);
    }
}
