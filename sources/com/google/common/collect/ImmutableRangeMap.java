package com.google.common.collect;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.SortedLists.KeyAbsentBehavior;
import com.google.common.collect.SortedLists.KeyPresentBehavior;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

@GwtIncompatible
@Beta
public class ImmutableRangeMap<K extends Comparable<?>, V> implements RangeMap<K, V>, Serializable {
    private static final ImmutableRangeMap<Comparable<?>, Object> EMPTY = new ImmutableRangeMap<>(ImmutableList.m113of(), ImmutableList.m113of());
    private static final long serialVersionUID = 0;
    /* access modifiers changed from: private */
    public final transient ImmutableList<Range<K>> ranges;
    private final transient ImmutableList<V> values;

    public static final class Builder<K extends Comparable<?>, V> {
        private final RangeSet<K> keyRanges = TreeRangeSet.create();
        private final RangeMap<K, V> rangeMap = TreeRangeMap.create();

        @CanIgnoreReturnValue
        public Builder<K, V> put(Range<K> range, V v) {
            Preconditions.checkNotNull(range);
            Preconditions.checkNotNull(v);
            Preconditions.checkArgument(!range.isEmpty(), "Range must not be empty, but was %s", (Object) range);
            if (!this.keyRanges.complement().encloses(range)) {
                for (Entry entry : this.rangeMap.asMapOfRanges().entrySet()) {
                    Range range2 = (Range) entry.getKey();
                    if (range2.isConnected(range) && !range2.intersection(range).isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Overlapping ranges: range ");
                        sb.append(range);
                        sb.append(" overlaps with entry ");
                        sb.append(entry);
                        throw new IllegalArgumentException(sb.toString());
                    }
                }
            }
            this.keyRanges.add(range);
            this.rangeMap.put(range, v);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<K, V> putAll(RangeMap<K, ? extends V> rangeMap2) {
            for (Entry entry : rangeMap2.asMapOfRanges().entrySet()) {
                put((Range) entry.getKey(), entry.getValue());
            }
            return this;
        }

        public ImmutableRangeMap<K, V> build() {
            Map asMapOfRanges = this.rangeMap.asMapOfRanges();
            com.google.common.collect.ImmutableList.Builder builder = new com.google.common.collect.ImmutableList.Builder(asMapOfRanges.size());
            com.google.common.collect.ImmutableList.Builder builder2 = new com.google.common.collect.ImmutableList.Builder(asMapOfRanges.size());
            for (Entry entry : asMapOfRanges.entrySet()) {
                builder.add(entry.getKey());
                builder2.add(entry.getValue());
            }
            return new ImmutableRangeMap<>(builder.build(), builder2.build());
        }
    }

    private static class SerializedForm<K extends Comparable<?>, V> implements Serializable {
        private static final long serialVersionUID = 0;
        private final ImmutableMap<Range<K>, V> mapOfRanges;

        SerializedForm(ImmutableMap<Range<K>, V> immutableMap) {
            this.mapOfRanges = immutableMap;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            if (this.mapOfRanges.isEmpty()) {
                return ImmutableRangeMap.m151of();
            }
            return createRangeMap();
        }

        /* access modifiers changed from: 0000 */
        public Object createRangeMap() {
            Builder builder = new Builder();
            Iterator it = this.mapOfRanges.entrySet().iterator();
            while (it.hasNext()) {
                Entry entry = (Entry) it.next();
                builder.put((Range) entry.getKey(), entry.getValue());
            }
            return builder.build();
        }
    }

    /* renamed from: of */
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> m151of() {
        return EMPTY;
    }

    /* renamed from: of */
    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> m152of(Range<K> range, V v) {
        return new ImmutableRangeMap<>(ImmutableList.m114of(range), ImmutableList.m114of(v));
    }

    public static <K extends Comparable<?>, V> ImmutableRangeMap<K, V> copyOf(RangeMap<K, ? extends V> rangeMap) {
        if (rangeMap instanceof ImmutableRangeMap) {
            return (ImmutableRangeMap) rangeMap;
        }
        Map asMapOfRanges = rangeMap.asMapOfRanges();
        com.google.common.collect.ImmutableList.Builder builder = new com.google.common.collect.ImmutableList.Builder(asMapOfRanges.size());
        com.google.common.collect.ImmutableList.Builder builder2 = new com.google.common.collect.ImmutableList.Builder(asMapOfRanges.size());
        for (Entry entry : asMapOfRanges.entrySet()) {
            builder.add(entry.getKey());
            builder2.add(entry.getValue());
        }
        return new ImmutableRangeMap<>(builder.build(), builder2.build());
    }

    public static <K extends Comparable<?>, V> Builder<K, V> builder() {
        return new Builder<>();
    }

    ImmutableRangeMap(ImmutableList<Range<K>> immutableList, ImmutableList<V> immutableList2) {
        this.ranges = immutableList;
        this.values = immutableList2;
    }

    @Nullable
    public V get(K k) {
        int binarySearch = SortedLists.binarySearch((List<E>) this.ranges, Range.lowerBoundFn(), Cut.belowValue(k), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        V v = null;
        if (binarySearch == -1) {
            return null;
        }
        if (((Range) this.ranges.get(binarySearch)).contains(k)) {
            v = this.values.get(binarySearch);
        }
        return v;
    }

    @Nullable
    public Entry<Range<K>, V> getEntry(K k) {
        int binarySearch = SortedLists.binarySearch((List<E>) this.ranges, Range.lowerBoundFn(), Cut.belowValue(k), KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_LOWER);
        Entry<Range<K>, V> entry = null;
        if (binarySearch == -1) {
            return null;
        }
        Range range = (Range) this.ranges.get(binarySearch);
        if (range.contains(k)) {
            entry = Maps.immutableEntry(range, this.values.get(binarySearch));
        }
        return entry;
    }

    public Range<K> span() {
        if (!this.ranges.isEmpty()) {
            Range range = (Range) this.ranges.get(0);
            ImmutableList<Range<K>> immutableList = this.ranges;
            return Range.create(range.lowerBound, ((Range) immutableList.get(immutableList.size() - 1)).upperBound);
        }
        throw new NoSuchElementException();
    }

    @Deprecated
    public void put(Range<K> range, V v) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void putAll(RangeMap<K, V> rangeMap) {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Deprecated
    public void remove(Range<K> range) {
        throw new UnsupportedOperationException();
    }

    public ImmutableMap<Range<K>, V> asMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.m132of();
        }
        return new ImmutableSortedMap(new RegularImmutableSortedSet(this.ranges, Range.RANGE_LEX_ORDERING), this.values);
    }

    public ImmutableMap<Range<K>, V> asDescendingMapOfRanges() {
        if (this.ranges.isEmpty()) {
            return ImmutableMap.m132of();
        }
        return new ImmutableSortedMap(new RegularImmutableSortedSet(this.ranges.reverse(), Range.RANGE_LEX_ORDERING.reverse()), this.values.reverse());
    }

    public ImmutableRangeMap<K, V> subRangeMap(final Range<K> range) {
        if (((Range) Preconditions.checkNotNull(range)).isEmpty()) {
            return m151of();
        }
        if (this.ranges.isEmpty() || range.encloses(span())) {
            return this;
        }
        final int binarySearch = SortedLists.binarySearch((List<E>) this.ranges, Range.upperBoundFn(), range.lowerBound, KeyPresentBehavior.FIRST_AFTER, KeyAbsentBehavior.NEXT_HIGHER);
        int binarySearch2 = SortedLists.binarySearch((List<E>) this.ranges, Range.lowerBoundFn(), range.upperBound, KeyPresentBehavior.ANY_PRESENT, KeyAbsentBehavior.NEXT_HIGHER);
        if (binarySearch >= binarySearch2) {
            return m151of();
        }
        final int i = binarySearch2 - binarySearch;
        final Range<K> range2 = range;
        C12112 r3 = new ImmutableRangeMap<K, V>(new ImmutableList<Range<K>>() {
            /* access modifiers changed from: 0000 */
            public boolean isPartialView() {
                return true;
            }

            public int size() {
                return i;
            }

            public Range<K> get(int i) {
                Preconditions.checkElementIndex(i, i);
                if (i == 0 || i == i - 1) {
                    return ((Range) ImmutableRangeMap.this.ranges.get(i + binarySearch)).intersection(range);
                }
                return (Range) ImmutableRangeMap.this.ranges.get(i + binarySearch);
            }
        }, this.values.subList(binarySearch, binarySearch2)) {
            public /* bridge */ /* synthetic */ Map asDescendingMapOfRanges() {
                return ImmutableRangeMap.super.asDescendingMapOfRanges();
            }

            public /* bridge */ /* synthetic */ Map asMapOfRanges() {
                return ImmutableRangeMap.super.asMapOfRanges();
            }

            public ImmutableRangeMap<K, V> subRangeMap(Range<K> range) {
                if (range2.isConnected(range)) {
                    return this.subRangeMap(range.intersection(range2));
                }
                return ImmutableRangeMap.m151of();
            }
        };
        return r3;
    }

    public int hashCode() {
        return asMapOfRanges().hashCode();
    }

    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof RangeMap)) {
            return false;
        }
        return asMapOfRanges().equals(((RangeMap) obj).asMapOfRanges());
    }

    public String toString() {
        return asMapOfRanges().toString();
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(asMapOfRanges());
    }
}
