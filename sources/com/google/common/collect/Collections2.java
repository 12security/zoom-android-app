package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;

@GwtCompatible
public final class Collections2 {
    static final Joiner STANDARD_JOINER = Joiner.m82on(", ").useForNull("null");

    static class FilteredCollection<E> extends AbstractCollection<E> {
        final Predicate<? super E> predicate;
        final Collection<E> unfiltered;

        FilteredCollection(Collection<E> collection, Predicate<? super E> predicate2) {
            this.unfiltered = collection;
            this.predicate = predicate2;
        }

        /* access modifiers changed from: 0000 */
        public FilteredCollection<E> createCombined(Predicate<? super E> predicate2) {
            return new FilteredCollection<>(this.unfiltered, Predicates.and(this.predicate, predicate2));
        }

        public boolean add(E e) {
            Preconditions.checkArgument(this.predicate.apply(e));
            return this.unfiltered.add(e);
        }

        public boolean addAll(Collection<? extends E> collection) {
            for (Object apply : collection) {
                Preconditions.checkArgument(this.predicate.apply(apply));
            }
            return this.unfiltered.addAll(collection);
        }

        public void clear() {
            Iterables.removeIf(this.unfiltered, this.predicate);
        }

        public boolean contains(@Nullable Object obj) {
            if (Collections2.safeContains(this.unfiltered, obj)) {
                return this.predicate.apply(obj);
            }
            return false;
        }

        public boolean containsAll(Collection<?> collection) {
            return Collections2.containsAllImpl(this, collection);
        }

        public boolean isEmpty() {
            return !Iterables.any(this.unfiltered, this.predicate);
        }

        public Iterator<E> iterator() {
            return Iterators.filter(this.unfiltered.iterator(), this.predicate);
        }

        public boolean remove(Object obj) {
            return contains(obj) && this.unfiltered.remove(obj);
        }

        public boolean removeAll(Collection<?> collection) {
            return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.m87in(collection)));
        }

        public boolean retainAll(Collection<?> collection) {
            return Iterables.removeIf(this.unfiltered, Predicates.and(this.predicate, Predicates.not(Predicates.m87in(collection))));
        }

        public int size() {
            return Iterators.size(iterator());
        }

        public Object[] toArray() {
            return Lists.newArrayList(iterator()).toArray();
        }

        public <T> T[] toArray(T[] tArr) {
            return Lists.newArrayList(iterator()).toArray(tArr);
        }
    }

    private static final class OrderedPermutationCollection<E> extends AbstractCollection<List<E>> {
        final Comparator<? super E> comparator;
        final ImmutableList<E> inputList;
        final int size;

        public boolean isEmpty() {
            return false;
        }

        OrderedPermutationCollection(Iterable<E> iterable, Comparator<? super E> comparator2) {
            this.inputList = Ordering.from(comparator2).immutableSortedCopy(iterable);
            this.comparator = comparator2;
            this.size = calculateSize(this.inputList, comparator2);
        }

        private static <E> int calculateSize(List<E> list, Comparator<? super E> comparator2) {
            long j = 1;
            int i = 1;
            int i2 = 1;
            while (i < list.size()) {
                if (comparator2.compare(list.get(i - 1), list.get(i)) < 0) {
                    j *= LongMath.binomial(i, i2);
                    i2 = 0;
                    if (!Collections2.isPositiveInt(j)) {
                        return Integer.MAX_VALUE;
                    }
                }
                i++;
                i2++;
            }
            long binomial = j * LongMath.binomial(i, i2);
            if (!Collections2.isPositiveInt(binomial)) {
                return Integer.MAX_VALUE;
            }
            return (int) binomial;
        }

        public int size() {
            return this.size;
        }

        public Iterator<List<E>> iterator() {
            return new OrderedPermutationIterator(this.inputList, this.comparator);
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof List)) {
                return false;
            }
            return Collections2.isPermutation(this.inputList, (List) obj);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("orderedPermutationCollection(");
            sb.append(this.inputList);
            sb.append(")");
            return sb.toString();
        }
    }

    private static final class OrderedPermutationIterator<E> extends AbstractIterator<List<E>> {
        final Comparator<? super E> comparator;
        List<E> nextPermutation;

        OrderedPermutationIterator(List<E> list, Comparator<? super E> comparator2) {
            this.nextPermutation = Lists.newArrayList((Iterable<? extends E>) list);
            this.comparator = comparator2;
        }

        /* access modifiers changed from: protected */
        public List<E> computeNext() {
            List<E> list = this.nextPermutation;
            if (list == null) {
                return (List) endOfData();
            }
            ImmutableList copyOf = ImmutableList.copyOf((Collection<? extends E>) list);
            calculateNextPermutation();
            return copyOf;
        }

        /* access modifiers changed from: 0000 */
        public void calculateNextPermutation() {
            int findNextJ = findNextJ();
            if (findNextJ == -1) {
                this.nextPermutation = null;
                return;
            }
            Collections.swap(this.nextPermutation, findNextJ, findNextL(findNextJ));
            Collections.reverse(this.nextPermutation.subList(findNextJ + 1, this.nextPermutation.size()));
        }

        /* access modifiers changed from: 0000 */
        public int findNextJ() {
            for (int size = this.nextPermutation.size() - 2; size >= 0; size--) {
                if (this.comparator.compare(this.nextPermutation.get(size), this.nextPermutation.get(size + 1)) < 0) {
                    return size;
                }
            }
            return -1;
        }

        /* access modifiers changed from: 0000 */
        public int findNextL(int i) {
            Object obj = this.nextPermutation.get(i);
            for (int size = this.nextPermutation.size() - 1; size > i; size--) {
                if (this.comparator.compare(obj, this.nextPermutation.get(size)) < 0) {
                    return size;
                }
            }
            throw new AssertionError("this statement should be unreachable");
        }
    }

    private static final class PermutationCollection<E> extends AbstractCollection<List<E>> {
        final ImmutableList<E> inputList;

        public boolean isEmpty() {
            return false;
        }

        PermutationCollection(ImmutableList<E> immutableList) {
            this.inputList = immutableList;
        }

        public int size() {
            return IntMath.factorial(this.inputList.size());
        }

        public Iterator<List<E>> iterator() {
            return new PermutationIterator(this.inputList);
        }

        public boolean contains(@Nullable Object obj) {
            if (!(obj instanceof List)) {
                return false;
            }
            return Collections2.isPermutation(this.inputList, (List) obj);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("permutations(");
            sb.append(this.inputList);
            sb.append(")");
            return sb.toString();
        }
    }

    private static class PermutationIterator<E> extends AbstractIterator<List<E>> {

        /* renamed from: c */
        final int[] f253c;

        /* renamed from: j */
        int f254j = Integer.MAX_VALUE;
        final List<E> list;

        /* renamed from: o */
        final int[] f255o;

        PermutationIterator(List<E> list2) {
            this.list = new ArrayList(list2);
            int size = list2.size();
            this.f253c = new int[size];
            this.f255o = new int[size];
            Arrays.fill(this.f253c, 0);
            Arrays.fill(this.f255o, 1);
        }

        /* access modifiers changed from: protected */
        public List<E> computeNext() {
            if (this.f254j <= 0) {
                return (List) endOfData();
            }
            ImmutableList copyOf = ImmutableList.copyOf((Collection<? extends E>) this.list);
            calculateNextPermutation();
            return copyOf;
        }

        /* access modifiers changed from: 0000 */
        public void calculateNextPermutation() {
            this.f254j = this.list.size() - 1;
            if (this.f254j != -1) {
                int i = 0;
                while (true) {
                    int[] iArr = this.f253c;
                    int i2 = this.f254j;
                    int i3 = iArr[i2] + this.f255o[i2];
                    if (i3 >= 0) {
                        if (i3 != i2 + 1) {
                            Collections.swap(this.list, (i2 - iArr[i2]) + i, (i2 - i3) + i);
                            this.f253c[this.f254j] = i3;
                            break;
                        } else if (i2 == 0) {
                            break;
                        } else {
                            i++;
                            switchDirection();
                        }
                    } else {
                        switchDirection();
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public void switchDirection() {
            int[] iArr = this.f255o;
            int i = this.f254j;
            iArr[i] = -iArr[i];
            this.f254j = i - 1;
        }
    }

    static class TransformedCollection<F, T> extends AbstractCollection<T> {
        final Collection<F> fromCollection;
        final Function<? super F, ? extends T> function;

        TransformedCollection(Collection<F> collection, Function<? super F, ? extends T> function2) {
            this.fromCollection = (Collection) Preconditions.checkNotNull(collection);
            this.function = (Function) Preconditions.checkNotNull(function2);
        }

        public void clear() {
            this.fromCollection.clear();
        }

        public boolean isEmpty() {
            return this.fromCollection.isEmpty();
        }

        public Iterator<T> iterator() {
            return Iterators.transform(this.fromCollection.iterator(), this.function);
        }

        public int size() {
            return this.fromCollection.size();
        }
    }

    /* access modifiers changed from: private */
    public static boolean isPositiveInt(long j) {
        return j >= 0 && j <= 2147483647L;
    }

    private Collections2() {
    }

    public static <E> Collection<E> filter(Collection<E> collection, Predicate<? super E> predicate) {
        if (collection instanceof FilteredCollection) {
            return ((FilteredCollection) collection).createCombined(predicate);
        }
        return new FilteredCollection((Collection) Preconditions.checkNotNull(collection), (Predicate) Preconditions.checkNotNull(predicate));
    }

    static boolean safeContains(Collection<?> collection, @Nullable Object obj) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.contains(obj);
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused2) {
            return false;
        }
    }

    static boolean safeRemove(Collection<?> collection, @Nullable Object obj) {
        Preconditions.checkNotNull(collection);
        try {
            return collection.remove(obj);
        } catch (ClassCastException unused) {
            return false;
        } catch (NullPointerException unused2) {
            return false;
        }
    }

    public static <F, T> Collection<T> transform(Collection<F> collection, Function<? super F, T> function) {
        return new TransformedCollection(collection, function);
    }

    static boolean containsAllImpl(Collection<?> collection, Collection<?> collection2) {
        return Iterables.all(collection2, Predicates.m87in(collection));
    }

    static String toStringImpl(final Collection<?> collection) {
        StringBuilder newStringBuilderForCollection = newStringBuilderForCollection(collection.size());
        newStringBuilderForCollection.append('[');
        STANDARD_JOINER.appendTo(newStringBuilderForCollection, Iterables.transform(collection, new Function<Object, Object>() {
            public Object apply(Object obj) {
                return obj == collection ? "(this Collection)" : obj;
            }
        }));
        newStringBuilderForCollection.append(']');
        return newStringBuilderForCollection.toString();
    }

    static StringBuilder newStringBuilderForCollection(int i) {
        CollectPreconditions.checkNonnegative(i, "size");
        return new StringBuilder((int) Math.min(((long) i) * 8, 1073741824));
    }

    static <T> Collection<T> cast(Iterable<T> iterable) {
        return (Collection) iterable;
    }

    @Beta
    public static <E extends Comparable<? super E>> Collection<List<E>> orderedPermutations(Iterable<E> iterable) {
        return orderedPermutations(iterable, Ordering.natural());
    }

    @Beta
    public static <E> Collection<List<E>> orderedPermutations(Iterable<E> iterable, Comparator<? super E> comparator) {
        return new OrderedPermutationCollection(iterable, comparator);
    }

    @Beta
    public static <E> Collection<List<E>> permutations(Collection<E> collection) {
        return new PermutationCollection(ImmutableList.copyOf(collection));
    }

    /* access modifiers changed from: private */
    public static boolean isPermutation(List<?> list, List<?> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        return HashMultiset.create((Iterable<? extends E>) list).equals(HashMultiset.create((Iterable<? extends E>) list2));
    }
}
