package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.common.primitives.Ints;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.util.Collection;
import javax.annotation.Nullable;

@GwtCompatible(serializable = true)
class RegularImmutableMultiset<E> extends ImmutableMultiset<E> {
    static final RegularImmutableMultiset<Object> EMPTY = new RegularImmutableMultiset<>(ImmutableList.m113of());
    @LazyInit
    private transient ImmutableSet<E> elementSet;
    /* access modifiers changed from: private */
    public final transient ImmutableEntry<E>[] entries;
    private final transient int hashCode;
    private final transient ImmutableEntry<E>[] hashTable;
    private final transient int size;

    private final class ElementSet extends Indexed<E> {
        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return true;
        }

        private ElementSet() {
        }

        /* access modifiers changed from: 0000 */
        public E get(int i) {
            return RegularImmutableMultiset.this.entries[i].getElement();
        }

        public boolean contains(@Nullable Object obj) {
            return RegularImmutableMultiset.this.contains(obj);
        }

        public int size() {
            return RegularImmutableMultiset.this.entries.length;
        }
    }

    private static final class NonTerminalEntry<E> extends ImmutableEntry<E> {
        private final ImmutableEntry<E> nextInBucket;

        NonTerminalEntry(E e, int i, ImmutableEntry<E> immutableEntry) {
            super(e, i);
            this.nextInBucket = immutableEntry;
        }

        public ImmutableEntry<E> nextInBucket() {
            return this.nextInBucket;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return false;
    }

    RegularImmutableMultiset(Collection<? extends Entry<? extends E>> collection) {
        ImmutableEntry<E> immutableEntry;
        int size2 = collection.size();
        ImmutableEntry<E>[] immutableEntryArr = new ImmutableEntry[size2];
        if (size2 == 0) {
            this.entries = immutableEntryArr;
            this.hashTable = null;
            this.size = 0;
            this.hashCode = 0;
            this.elementSet = ImmutableSet.m155of();
            return;
        }
        int closedTableSize = Hashing.closedTableSize(size2, 1.0d);
        int i = closedTableSize - 1;
        ImmutableEntry<E>[] immutableEntryArr2 = new ImmutableEntry[closedTableSize];
        long j = 0;
        int i2 = 0;
        int i3 = 0;
        for (Entry entry : collection) {
            Object checkNotNull = Preconditions.checkNotNull(entry.getElement());
            int count = entry.getCount();
            int hashCode2 = checkNotNull.hashCode();
            int smear = Hashing.smear(hashCode2) & i;
            ImmutableEntry<E> immutableEntry2 = immutableEntryArr2[smear];
            if (immutableEntry2 == null) {
                immutableEntry = (entry instanceof ImmutableEntry) && !(entry instanceof NonTerminalEntry) ? (ImmutableEntry) entry : new ImmutableEntry<>(checkNotNull, count);
            } else {
                immutableEntry = new NonTerminalEntry<>(checkNotNull, count, immutableEntry2);
            }
            i2 += hashCode2 ^ count;
            int i4 = i3 + 1;
            immutableEntryArr[i3] = immutableEntry;
            immutableEntryArr2[smear] = immutableEntry;
            j += (long) count;
            i3 = i4;
        }
        this.entries = immutableEntryArr;
        this.hashTable = immutableEntryArr2;
        this.size = Ints.saturatedCast(j);
        this.hashCode = i2;
    }

    public int count(@Nullable Object obj) {
        ImmutableEntry<E>[] immutableEntryArr = this.hashTable;
        if (obj == null || immutableEntryArr == null) {
            return 0;
        }
        for (ImmutableEntry<E> immutableEntry = immutableEntryArr[Hashing.smearedHash(obj) & (immutableEntryArr.length - 1)]; immutableEntry != null; immutableEntry = immutableEntry.nextInBucket()) {
            if (Objects.equal(obj, immutableEntry.getElement())) {
                return immutableEntry.getCount();
            }
        }
        return 0;
    }

    public int size() {
        return this.size;
    }

    public ImmutableSet<E> elementSet() {
        ImmutableSet<E> immutableSet = this.elementSet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ElementSet elementSet2 = new ElementSet();
        this.elementSet = elementSet2;
        return elementSet2;
    }

    /* access modifiers changed from: 0000 */
    public Entry<E> getEntry(int i) {
        return this.entries[i];
    }

    public int hashCode() {
        return this.hashCode;
    }
}
