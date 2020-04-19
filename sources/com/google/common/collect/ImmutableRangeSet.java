package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtIncompatible
@Beta
public final class ImmutableRangeSet<C extends Comparable> extends AbstractRangeSet<C> implements Serializable {
    private static final ImmutableRangeSet<Comparable<?>> ALL = new ImmutableRangeSet<>(ImmutableList.m114of(Range.all()));
    private static final ImmutableRangeSet<Comparable<?>> EMPTY = new ImmutableRangeSet<>(ImmutableList.m113of());
    @LazyInit
    private transient ImmutableRangeSet<C> complement;
    /* access modifiers changed from: private */
    public final transient ImmutableList<Range<C>> ranges;

    private final class AsSet extends ImmutableSortedSet<C> {
        /* access modifiers changed from: private */
        public final DiscreteDomain<C> domain;
        private transient Integer size;

        AsSet(DiscreteDomain<C> discreteDomain) {
            super(Ordering.natural());
            this.domain = discreteDomain;
        }

        public int size() {
            Integer num = this.size;
            if (num == null) {
                long j = 0;
                Iterator it = ImmutableRangeSet.this.ranges.iterator();
                while (it.hasNext()) {
                    j += (long) ContiguousSet.create((Range) it.next(), this.domain).size();
                    if (j >= 2147483647L) {
                        break;
                    }
                }
                num = Integer.valueOf(Ints.saturatedCast(j));
                this.size = num;
            }
            return num.intValue();
        }

        public UnmodifiableIterator<C> iterator() {
            return new AbstractIterator<C>() {
                Iterator<C> elemItr = Iterators.emptyIterator();
                final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.iterator();

                /* access modifiers changed from: protected */
                public C computeNext() {
                    while (!this.elemItr.hasNext()) {
                        if (!this.rangeItr.hasNext()) {
                            return (Comparable) endOfData();
                        }
                        this.elemItr = ContiguousSet.create((Range) this.rangeItr.next(), AsSet.this.domain).iterator();
                    }
                    return (Comparable) this.elemItr.next();
                }
            };
        }

        @GwtIncompatible("NavigableSet")
        public UnmodifiableIterator<C> descendingIterator() {
            return new AbstractIterator<C>() {
                Iterator<C> elemItr = Iterators.emptyIterator();
                final Iterator<Range<C>> rangeItr = ImmutableRangeSet.this.ranges.reverse().iterator();

                /* access modifiers changed from: protected */
                public C computeNext() {
                    while (!this.elemItr.hasNext()) {
                        if (!this.rangeItr.hasNext()) {
                            return (Comparable) endOfData();
                        }
                        this.elemItr = ContiguousSet.create((Range) this.rangeItr.next(), AsSet.this.domain).descendingIterator();
                    }
                    return (Comparable) this.elemItr.next();
                }
            };
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSortedSet<C> subSet(Range<C> range) {
            return ImmutableRangeSet.this.subRangeSet(range).asSet(this.domain);
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSortedSet<C> headSetImpl(C c, boolean z) {
            return subSet(Range.upTo(c, BoundType.forBoolean(z)));
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSortedSet<C> subSetImpl(C c, boolean z, C c2, boolean z2) {
            if (z || z2 || Range.compareOrThrow(c, c2) != 0) {
                return subSet(Range.range(c, BoundType.forBoolean(z), c2, BoundType.forBoolean(z2)));
            }
            return ImmutableSortedSet.m193of();
        }

        /* access modifiers changed from: 0000 */
        public ImmutableSortedSet<C> tailSetImpl(C c, boolean z) {
            return subSet(Range.downTo(c, BoundType.forBoolean(z)));
        }

        public boolean contains(@Nullable Object obj) {
            if (obj == null) {
                return false;
            }
            try {
                return ImmutableRangeSet.this.contains((Comparable) obj);
            } catch (ClassCastException unused) {
                return false;
            }
        }

        /* access modifiers changed from: 0000 */
        public int indexOf(Object obj) {
            if (!contains(obj)) {
                return -1;
            }
            Comparable comparable = (Comparable) obj;
            long j = 0;
            Iterator it = ImmutableRangeSet.this.ranges.iterator();
            while (it.hasNext()) {
                Range range = (Range) it.next();
                if (range.contains(comparable)) {
                    return Ints.saturatedCast(j + ((long) ContiguousSet.create(range, this.domain).indexOf(comparable)));
                }
                j += (long) ContiguousSet.create(range, this.domain).size();
            }
            throw new AssertionError("impossible");
        }

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return ImmutableRangeSet.this.ranges.isPartialView();
        }

        public String toString() {
            return ImmutableRangeSet.this.ranges.toString();
        }

        /* access modifiers changed from: 0000 */
        public Object writeReplace() {
            return new AsSetSerializedForm(ImmutableRangeSet.this.ranges, this.domain);
        }
    }

    private static class AsSetSerializedForm<C extends Comparable> implements Serializable {
        private final DiscreteDomain<C> domain;
        private final ImmutableList<Range<C>> ranges;

        AsSetSerializedForm(ImmutableList<Range<C>> immutableList, DiscreteDomain<C> discreteDomain) {
            this.ranges = immutableList;
            this.domain = discreteDomain;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return new ImmutableRangeSet(this.ranges).asSet(this.domain);
        }
    }

    public static class Builder<C extends Comparable<?>> {
        private final RangeSet<C> rangeSet = TreeRangeSet.create();

        @CanIgnoreReturnValue
        public Builder<C> add(Range<C> range) {
            if (range.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("range must not be empty, but was ");
                sb.append(range);
                throw new IllegalArgumentException(sb.toString());
            } else if (!this.rangeSet.complement().encloses(range)) {
                for (Range range2 : this.rangeSet.asRanges()) {
                    Preconditions.checkArgument(!range2.isConnected(range) || range2.intersection(range).isEmpty(), "Ranges may not overlap, but received %s and %s", (Object) range2, (Object) range);
                }
                throw new AssertionError("should have thrown an IAE above");
            } else {
                this.rangeSet.add(range);
                return this;
            }
        }

        @CanIgnoreReturnValue
        public Builder<C> addAll(RangeSet<C> rangeSet2) {
            for (Range add : rangeSet2.asRanges()) {
                add(add);
            }
            return this;
        }

        public ImmutableRangeSet<C> build() {
            return ImmutableRangeSet.copyOf(this.rangeSet);
        }
    }

    private final class ComplementRanges extends ImmutableList<Range<C>> {
        private final boolean positiveBoundedAbove;
        private final boolean positiveBoundedBelow;
        private final int size;

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return true;
        }

        ComplementRanges() {
            this.positiveBoundedBelow = ((Range) ImmutableRangeSet.this.ranges.get(0)).hasLowerBound();
            this.positiveBoundedAbove = ((Range) Iterables.getLast(ImmutableRangeSet.this.ranges)).hasUpperBound();
            int size2 = ImmutableRangeSet.this.ranges.size() - 1;
            if (this.positiveBoundedBelow) {
                size2++;
            }
            if (this.positiveBoundedAbove) {
                size2++;
            }
            this.size = size2;
        }

        public int size() {
            return this.size;
        }

        public Range<C> get(int i) {
            Cut<C> cut;
            Preconditions.checkElementIndex(i, this.size);
            Cut<C> cut2 = this.positiveBoundedBelow ? i == 0 ? Cut.belowAll() : ((Range) ImmutableRangeSet.this.ranges.get(i - 1)).upperBound : ((Range) ImmutableRangeSet.this.ranges.get(i)).upperBound;
            if (!this.positiveBoundedAbove || i != this.size - 1) {
                cut = ((Range) ImmutableRangeSet.this.ranges.get(i + (this.positiveBoundedBelow ^ true ? 1 : 0))).lowerBound;
            } else {
                cut = Cut.aboveAll();
            }
            return Range.create(cut2, cut);
        }
    }

    private static final class SerializedForm<C extends Comparable> implements Serializable {
        private final ImmutableList<Range<C>> ranges;

        SerializedForm(ImmutableList<Range<C>> immutableList) {
            this.ranges = immutableList;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            if (this.ranges.isEmpty()) {
                return ImmutableRangeSet.m153of();
            }
            if (this.ranges.equals(ImmutableList.m114of(Range.all()))) {
                return ImmutableRangeSet.all();
            }
            return new ImmutableRangeSet(this.ranges);
        }
    }

    public /* bridge */ /* synthetic */ void clear() {
        super.clear();
    }

    public /* bridge */ /* synthetic */ boolean contains(Comparable comparable) {
        return super.contains(comparable);
    }

    public /* bridge */ /* synthetic */ boolean enclosesAll(RangeSet rangeSet) {
        return super.enclosesAll(rangeSet);
    }

    public /* bridge */ /* synthetic */ boolean equals(Object obj) {
        return super.equals(obj);
    }

    /* renamed from: of */
    public static <C extends Comparable> ImmutableRangeSet<C> m153of() {
        return EMPTY;
    }

    static <C extends Comparable> ImmutableRangeSet<C> all() {
        return ALL;
    }

    /* renamed from: of */
    public static <C extends Comparable> ImmutableRangeSet<C> m154of(Range<C> range) {
        Preconditions.checkNotNull(range);
        if (range.isEmpty()) {
            return m153of();
        }
        if (range.equals(Range.all())) {
            return all();
        }
        return new ImmutableRangeSet<>(ImmutableList.m114of(range));
    }

    public static <C extends Comparable> ImmutableRangeSet<C> copyOf(RangeSet<C> rangeSet) {
        Preconditions.checkNotNull(rangeSet);
        if (rangeSet.isEmpty()) {
            return m153of();
        }
        if (rangeSet.encloses(Range.all())) {
            return all();
        }
        if (rangeSet instanceof ImmutableRangeSet) {
            ImmutableRangeSet<C> immutableRangeSet = (ImmutableRangeSet) rangeSet;
            if (!immutableRangeSet.isPartialView()) {
                return immutableRangeSet;
            }
        }
        return new ImmutableRangeSet<>(ImmutableList.copyOf((Collection<? extends E>) rangeSet.asRanges()));
    }

    ImmutableRangeSet(ImmutableList<Range<C>> immutableList) {
        this.ranges = immutableList;
    }

    private ImmutableRangeSet(ImmutableList<Range<C>> immutableList, ImmutableRangeSet<C> immutableRangeSet) {
        this.ranges = immutableList;
        this.complement = immutableRangeSet;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x005f, code lost:
        if (((com.google.common.collect.Range) r6.ranges.get(r0)).intersection(r7).isEmpty() == false) goto L_0x0063;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean intersects(com.google.common.collect.Range<C> r7) {
        /*
            r6 = this;
            com.google.common.collect.ImmutableList<com.google.common.collect.Range<C>> r0 = r6.ranges
            com.google.common.base.Function r1 = com.google.common.collect.Range.lowerBoundFn()
            com.google.common.collect.Cut<C> r2 = r7.lowerBound
            com.google.common.collect.Ordering r3 = com.google.common.collect.Ordering.natural()
            com.google.common.collect.SortedLists$KeyPresentBehavior r4 = com.google.common.collect.SortedLists.KeyPresentBehavior.ANY_PRESENT
            com.google.common.collect.SortedLists$KeyAbsentBehavior r5 = com.google.common.collect.SortedLists.KeyAbsentBehavior.NEXT_HIGHER
            int r0 = com.google.common.collect.SortedLists.binarySearch(r0, r1, r2, r3, r4, r5)
            com.google.common.collect.ImmutableList<com.google.common.collect.Range<C>> r1 = r6.ranges
            int r1 = r1.size()
            r2 = 1
            if (r0 >= r1) goto L_0x003e
            com.google.common.collect.ImmutableList<com.google.common.collect.Range<C>> r1 = r6.ranges
            java.lang.Object r1 = r1.get(r0)
            com.google.common.collect.Range r1 = (com.google.common.collect.Range) r1
            boolean r1 = r1.isConnected(r7)
            if (r1 == 0) goto L_0x003e
            com.google.common.collect.ImmutableList<com.google.common.collect.Range<C>> r1 = r6.ranges
            java.lang.Object r1 = r1.get(r0)
            com.google.common.collect.Range r1 = (com.google.common.collect.Range) r1
            com.google.common.collect.Range r1 = r1.intersection(r7)
            boolean r1 = r1.isEmpty()
            if (r1 != 0) goto L_0x003e
            return r2
        L_0x003e:
            if (r0 <= 0) goto L_0x0062
            com.google.common.collect.ImmutableList<com.google.common.collect.Range<C>> r1 = r6.ranges
            int r0 = r0 - r2
            java.lang.Object r1 = r1.get(r0)
            com.google.common.collect.Range r1 = (com.google.common.collect.Range) r1
            boolean r1 = r1.isConnected(r7)
            if (r1 == 0) goto L_0x0062
            com.google.common.collect.ImmutableList<com.google.common.collect.Range<C>> r1 = r6.ranges
            java.lang.Object r0 = r1.get(r0)
            com.google.common.collect.Range r0 = (com.google.common.collect.Range) r0
            com.google.common.collect.Range r7 = r0.intersection(r7)
            boolean r7 = r7.isEmpty()
            if (r7 != 0) goto L_0x0062
            goto L_0x0063
        L_0x0062:
            r2 = 0
        L_0x0063:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.collect.ImmutableRangeSet.intersects(com.google.common.collect.Range):boolean");
    }

    public boolean encloses(Range<C> range) {
        int binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), range.lowerBound, Ordering.natural(), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        return binarySearch != -1 && ((Range) this.ranges.get(binarySearch)).encloses(range);
    }

    public Range<C> rangeContaining(C c) {
        int binarySearch = SortedLists.binarySearch(this.ranges, Range.lowerBoundFn(), Cut.belowValue(c), Ordering.natural(), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        if (binarySearch == -1) {
            return null;
        }
        Range<C> range = (Range) this.ranges.get(binarySearch);
        if (!range.contains(c)) {
            range = null;
        }
        return range;
    }

    public Range<C> span() {
        if (!this.ranges.isEmpty()) {
            Cut<C> cut = ((Range) this.ranges.get(0)).lowerBound;
            ImmutableList<Range<C>> immutableList = this.ranges;
            return Range.create(cut, ((Range) immutableList.get(immutableList.size() - 1)).upperBound);
        }
        throw new NoSuchElementException();
    }

    public boolean isEmpty() {
        return this.ranges.isEmpty();
    }

    @Deprecated
    public void add(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void addAll(RangeSet<C> rangeSet) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void remove(Range<C> range) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void removeAll(RangeSet<C> rangeSet) {
        throw new UnsupportedOperationException();
    }

    public ImmutableSet<Range<C>> asRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableSet.m155of();
        }
        return new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING);
    }

    public ImmutableSet<Range<C>> asDescendingSetOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableSet.m155of();
        }
        return new RegularImmutableSortedSet(this.ranges.reverse(), Range.RANGE_LEX_ORDERING.reverse());
    }

    public ImmutableRangeSet<C> complement() {
        ImmutableRangeSet<C> immutableRangeSet = this.complement;
        if (immutableRangeSet != null) {
            return immutableRangeSet;
        }
        if (this.ranges.isEmpty()) {
            ImmutableRangeSet<C> all = all();
            this.complement = all;
            return all;
        } else if (this.ranges.size() != 1 || !((Range) this.ranges.get(0)).equals(Range.all())) {
            ImmutableRangeSet<C> immutableRangeSet2 = new ImmutableRangeSet<>(new ComplementRanges(), this);
            this.complement = immutableRangeSet2;
            return immutableRangeSet2;
        } else {
            ImmutableRangeSet<C> of = m153of();
            this.complement = of;
            return of;
        }
    }

    private ImmutableList<Range<C>> intersectRanges(final Range<C> range) {
        int i;
        if (this.ranges.isEmpty() || range.isEmpty()) {
            return ImmutableList.m113of();
        }
        if (range.encloses(span())) {
            return this.ranges;
        }
        final int binarySearch = range.hasLowerBound() ? SortedLists.binarySearch((List<E>) this.ranges, Range.upperBoundFn(), range.lowerBound, KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER) : 0;
        if (range.hasUpperBound()) {
            i = SortedLists.binarySearch((List<E>) this.ranges, Range.lowerBoundFn(), range.upperBound, KeyPresentBehavior.FIRST_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
        } else {
            i = this.ranges.size();
        }
        final int i2 = i - binarySearch;
        if (i2 == 0) {
            return ImmutableList.m113of();
        }
        return new ImmutableList<Range<C>>() {
            /* access modifiers changed from: 0000 */
            public boolean isPartialView() {
                return true;
            }

            public int size() {
                return i2;
            }

            public Range<C> get(int i) {
                Preconditions.checkElementIndex(i, i2);
                if (i == 0 || i == i2 - 1) {
                    return ((Range) ImmutableRangeSet.this.ranges.get(i + binarySearch)).intersection(range);
                }
                return (Range) ImmutableRangeSet.this.ranges.get(i + binarySearch);
            }
        };
    }

    public ImmutableRangeSet<C> subRangeSet(Range<C> range) {
        if (!isEmpty()) {
            Range span = span();
            if (range.encloses(span)) {
                return this;
            }
            if (range.isConnected(span)) {
                return new ImmutableRangeSet<>(intersectRanges(range));
            }
        }
        return m153of();
    }

    public ImmutableSortedSet<C> asSet(DiscreteDomain<C> discreteDomain) {
        Preconditions.checkNotNull(discreteDomain);
        if (isEmpty()) {
            return ImmutableSortedSet.m193of();
        }
        Range canonical = span().canonical(discreteDomain);
        if (canonical.hasLowerBound()) {
            if (!canonical.hasUpperBound()) {
                try {
                    discreteDomain.maxValue();
                } catch (NoSuchElementException unused) {
                    throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded above");
                }
            }
            return new AsSet(discreteDomain);
        }
        throw new IllegalArgumentException("Neither the DiscreteDomain nor this range set are bounded below");
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return this.ranges.isPartialView();
    }

    public static <C extends Comparable<?>> Builder<C> builder() {
        return new Builder<>();
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(this.ranges);
    }
}
