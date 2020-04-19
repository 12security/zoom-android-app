package com.google.common.collect;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

@GwtIncompatible
public abstract class ImmutableSortedMultiset<E> extends ImmutableSortedMultisetFauxverideShim<E> implements SortedMultiset<E> {
    @LazyInit
    transient ImmutableSortedMultiset<E> descendingMultiset;

    public static class Builder<E> extends com.google.common.collect.ImmutableMultiset.Builder<E> {
        public Builder(Comparator<? super E> comparator) {
            super(TreeMultiset.create((Comparator) Preconditions.checkNotNull(comparator)));
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E e) {
            super.add((Object) e);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addCopies(E e, int i) {
            super.addCopies(e, i);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> setCount(E e, int i) {
            super.setCount(e, i);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... eArr) {
            super.add((Object[]) eArr);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll((Iterable) iterable);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll((Iterator) it);
            return this;
        }

        public ImmutableSortedMultiset<E> build() {
            return ImmutableSortedMultiset.copyOfSorted((SortedMultiset) this.contents);
        }
    }

    private static final class SerializedForm<E> implements Serializable {
        final Comparator<? super E> comparator;
        final int[] counts;
        final E[] elements;

        SerializedForm(SortedMultiset<E> sortedMultiset) {
            this.comparator = sortedMultiset.comparator();
            int size = sortedMultiset.entrySet().size();
            this.elements = (Object[]) new Object[size];
            this.counts = new int[size];
            int i = 0;
            for (Entry entry : sortedMultiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            int length = this.elements.length;
            Builder builder = new Builder(this.comparator);
            for (int i = 0; i < length; i++) {
                builder.addCopies((Object) this.elements[i], this.counts[i]);
            }
            return builder.build();
        }
    }

    public abstract ImmutableSortedSet<E> elementSet();

    public abstract ImmutableSortedMultiset<E> headMultiset(E e, BoundType boundType);

    public abstract ImmutableSortedMultiset<E> tailMultiset(E e, BoundType boundType);

    /* renamed from: of */
    public static <E> ImmutableSortedMultiset<E> m180of() {
        return RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
    }

    /* renamed from: of */
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> m181of(E e) {
        return new RegularImmutableSortedMultiset((RegularImmutableSortedSet) ImmutableSortedSet.m194of(e), new long[]{0, 1}, 0, 1);
    }

    /* renamed from: of */
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> m182of(E e, E e2) {
        return copyOf((Comparator<? super E>) Ordering.natural(), (Iterable<? extends E>) Arrays.asList(new Comparable[]{e, e2}));
    }

    /* renamed from: of */
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> m183of(E e, E e2, E e3) {
        return copyOf((Comparator<? super E>) Ordering.natural(), (Iterable<? extends E>) Arrays.asList(new Comparable[]{e, e2, e3}));
    }

    /* renamed from: of */
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> m184of(E e, E e2, E e3, E e4) {
        return copyOf((Comparator<? super E>) Ordering.natural(), (Iterable<? extends E>) Arrays.asList(new Comparable[]{e, e2, e3, e4}));
    }

    /* renamed from: of */
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> m185of(E e, E e2, E e3, E e4, E e5) {
        return copyOf((Comparator<? super E>) Ordering.natural(), (Iterable<? extends E>) Arrays.asList(new Comparable[]{e, e2, e3, e4, e5}));
    }

    /* renamed from: of */
    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> m186of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        ArrayList newArrayListWithCapacity = Lists.newArrayListWithCapacity(eArr.length + 6);
        Collections.addAll(newArrayListWithCapacity, new Comparable[]{e, e2, e3, e4, e5, e6});
        Collections.addAll(newArrayListWithCapacity, eArr);
        return copyOf((Comparator<? super E>) Ordering.natural(), (Iterable<? extends E>) newArrayListWithCapacity);
    }

    public static <E extends Comparable<? super E>> ImmutableSortedMultiset<E> copyOf(E[] eArr) {
        return copyOf((Comparator<? super E>) Ordering.natural(), (Iterable<? extends E>) Arrays.asList(eArr));
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterable<? extends E> iterable) {
        return copyOf((Comparator<? super E>) Ordering.natural(), iterable);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Iterator<? extends E> it) {
        return copyOf((Comparator<? super E>) Ordering.natural(), it);
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterator<? extends E> it) {
        Preconditions.checkNotNull(comparator);
        return new Builder(comparator).addAll((Iterator) it).build();
    }

    public static <E> ImmutableSortedMultiset<E> copyOf(Comparator<? super E> comparator, Iterable<? extends E> iterable) {
        if (iterable instanceof ImmutableSortedMultiset) {
            ImmutableSortedMultiset<E> immutableSortedMultiset = (ImmutableSortedMultiset) iterable;
            if (comparator.equals(immutableSortedMultiset.comparator())) {
                return immutableSortedMultiset.isPartialView() ? copyOfSortedEntries(comparator, immutableSortedMultiset.entrySet().asList()) : immutableSortedMultiset;
            }
        }
        ArrayList newArrayList = Lists.newArrayList(iterable);
        TreeMultiset create = TreeMultiset.create((Comparator) Preconditions.checkNotNull(comparator));
        Iterables.addAll(create, newArrayList);
        return copyOfSortedEntries(comparator, create.entrySet());
    }

    public static <E> ImmutableSortedMultiset<E> copyOfSorted(SortedMultiset<E> sortedMultiset) {
        return copyOfSortedEntries(sortedMultiset.comparator(), Lists.newArrayList((Iterable<? extends E>) sortedMultiset.entrySet()));
    }

    private static <E> ImmutableSortedMultiset<E> copyOfSortedEntries(Comparator<? super E> comparator, Collection<Entry<E>> collection) {
        if (collection.isEmpty()) {
            return emptyMultiset(comparator);
        }
        com.google.common.collect.ImmutableList.Builder builder = new com.google.common.collect.ImmutableList.Builder(collection.size());
        long[] jArr = new long[(collection.size() + 1)];
        int i = 0;
        for (Entry entry : collection) {
            builder.add(entry.getElement());
            int i2 = i + 1;
            jArr[i2] = jArr[i] + ((long) entry.getCount());
            i = i2;
        }
        return new RegularImmutableSortedMultiset(new RegularImmutableSortedSet(builder.build(), comparator), jArr, 0, collection.size());
    }

    static <E> ImmutableSortedMultiset<E> emptyMultiset(Comparator<? super E> comparator) {
        if (Ordering.natural().equals(comparator)) {
            return RegularImmutableSortedMultiset.NATURAL_EMPTY_MULTISET;
        }
        return new RegularImmutableSortedMultiset(comparator);
    }

    ImmutableSortedMultiset() {
    }

    public final Comparator<? super E> comparator() {
        return elementSet().comparator();
    }

    public ImmutableSortedMultiset<E> descendingMultiset() {
        ImmutableSortedMultiset<E> immutableSortedMultiset = this.descendingMultiset;
        if (immutableSortedMultiset != null) {
            return immutableSortedMultiset;
        }
        ImmutableSortedMultiset<E> emptyMultiset = isEmpty() ? emptyMultiset(Ordering.from(comparator()).reverse()) : new DescendingImmutableSortedMultiset<>(this);
        this.descendingMultiset = emptyMultiset;
        return emptyMultiset;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final Entry<E> pollFirstEntry() {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final Entry<E> pollLastEntry() {
        throw new UnsupportedOperationException();
    }

    public ImmutableSortedMultiset<E> subMultiset(E e, BoundType boundType, E e2, BoundType boundType2) {
        Preconditions.checkArgument(comparator().compare(e, e2) <= 0, "Expected lowerBound <= upperBound but %s > %s", (Object) e, (Object) e2);
        return tailMultiset(e, boundType).headMultiset(e2, boundType2);
    }

    public static <E> Builder<E> orderedBy(Comparator<E> comparator) {
        return new Builder<>(comparator);
    }

    public static <E extends Comparable<?>> Builder<E> reverseOrder() {
        return new Builder<>(Ordering.natural().reverse());
    }

    public static <E extends Comparable<?>> Builder<E> naturalOrder() {
        return new Builder<>(Ordering.natural());
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(this);
    }
}
