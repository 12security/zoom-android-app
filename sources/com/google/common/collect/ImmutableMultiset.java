package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.Multiset.Entry;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableMultiset<E> extends ImmutableCollection<E> implements Multiset<E> {
    @LazyInit
    private transient ImmutableList<E> asList;
    @LazyInit
    private transient ImmutableSet<Entry<E>> entrySet;

    public static class Builder<E> extends com.google.common.collect.ImmutableCollection.Builder<E> {
        final Multiset<E> contents;

        public Builder() {
            this(LinkedHashMultiset.create());
        }

        Builder(Multiset<E> multiset) {
            this.contents = multiset;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E e) {
            this.contents.add(Preconditions.checkNotNull(e));
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addCopies(E e, int i) {
            this.contents.add(Preconditions.checkNotNull(e), i);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> setCount(E e, int i) {
            this.contents.setCount(Preconditions.checkNotNull(e), i);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... eArr) {
            super.add(eArr);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            if (iterable instanceof Multiset) {
                for (Entry entry : Multisets.cast(iterable).entrySet()) {
                    addCopies(entry.getElement(), entry.getCount());
                }
            } else {
                super.addAll(iterable);
            }
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll(it);
            return this;
        }

        public ImmutableMultiset<E> build() {
            return ImmutableMultiset.copyOf((Iterable<? extends E>) this.contents);
        }
    }

    private final class EntrySet extends Indexed<Entry<E>> {
        private static final long serialVersionUID = 0;

        private EntrySet() {
        }

        /* access modifiers changed from: 0000 */
        public boolean isPartialView() {
            return ImmutableMultiset.this.isPartialView();
        }

        /* access modifiers changed from: 0000 */
        public Entry<E> get(int i) {
            return ImmutableMultiset.this.getEntry(i);
        }

        public int size() {
            return ImmutableMultiset.this.elementSet().size();
        }

        public boolean contains(Object obj) {
            boolean z = false;
            if (!(obj instanceof Entry)) {
                return false;
            }
            Entry entry = (Entry) obj;
            if (entry.getCount() <= 0) {
                return false;
            }
            if (ImmutableMultiset.this.count(entry.getElement()) == entry.getCount()) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            return ImmutableMultiset.this.hashCode();
        }

        /* access modifiers changed from: 0000 */
        public Object writeReplace() {
            return new EntrySetSerializedForm(ImmutableMultiset.this);
        }
    }

    static class EntrySetSerializedForm<E> implements Serializable {
        final ImmutableMultiset<E> multiset;

        EntrySetSerializedForm(ImmutableMultiset<E> immutableMultiset) {
            this.multiset = immutableMultiset;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return this.multiset.entrySet();
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final int[] counts;
        final Object[] elements;

        SerializedForm(Multiset<?> multiset) {
            int size = multiset.entrySet().size();
            this.elements = new Object[size];
            this.counts = new int[size];
            int i = 0;
            for (Entry entry : multiset.entrySet()) {
                this.elements[i] = entry.getElement();
                this.counts[i] = entry.getCount();
                i++;
            }
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            LinkedHashMultiset create = LinkedHashMultiset.create(this.elements.length);
            int i = 0;
            while (true) {
                Object[] objArr = this.elements;
                if (i >= objArr.length) {
                    return ImmutableMultiset.copyOf((Iterable<? extends E>) create);
                }
                create.add(objArr[i], this.counts[i]);
                i++;
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract Entry<E> getEntry(int i);

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m144of() {
        return RegularImmutableMultiset.EMPTY;
    }

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m145of(E e) {
        return copyFromElements(e);
    }

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m146of(E e, E e2) {
        return copyFromElements(e, e2);
    }

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m147of(E e, E e2, E e3) {
        return copyFromElements(e, e2, e3);
    }

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m148of(E e, E e2, E e3, E e4) {
        return copyFromElements(e, e2, e3, e4);
    }

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m149of(E e, E e2, E e3, E e4, E e5) {
        return copyFromElements(e, e2, e3, e4, e5);
    }

    /* renamed from: of */
    public static <E> ImmutableMultiset<E> m150of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        return new Builder().add((Object) e).add((Object) e2).add((Object) e3).add((Object) e4).add((Object) e5).add((Object) e6).add((Object[]) eArr).build();
    }

    public static <E> ImmutableMultiset<E> copyOf(E[] eArr) {
        return copyFromElements(eArr);
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterable<? extends E> iterable) {
        if (iterable instanceof ImmutableMultiset) {
            ImmutableMultiset<E> immutableMultiset = (ImmutableMultiset) iterable;
            if (!immutableMultiset.isPartialView()) {
                return immutableMultiset;
            }
        }
        return copyFromEntries((iterable instanceof Multiset ? Multisets.cast(iterable) : LinkedHashMultiset.create(iterable)).entrySet());
    }

    private static <E> ImmutableMultiset<E> copyFromElements(E... eArr) {
        LinkedHashMultiset create = LinkedHashMultiset.create();
        Collections.addAll(create, eArr);
        return copyFromEntries(create.entrySet());
    }

    static <E> ImmutableMultiset<E> copyFromEntries(Collection<? extends Entry<? extends E>> collection) {
        if (collection.isEmpty()) {
            return m144of();
        }
        return new RegularImmutableMultiset(collection);
    }

    public static <E> ImmutableMultiset<E> copyOf(Iterator<? extends E> it) {
        LinkedHashMultiset create = LinkedHashMultiset.create();
        Iterators.addAll(create, it);
        return copyFromEntries(create.entrySet());
    }

    ImmutableMultiset() {
    }

    public UnmodifiableIterator<E> iterator() {
        final UnmodifiableIterator it = entrySet().iterator();
        return new UnmodifiableIterator<E>() {
            E element;
            int remaining;

            public boolean hasNext() {
                return this.remaining > 0 || it.hasNext();
            }

            public E next() {
                if (this.remaining <= 0) {
                    Entry entry = (Entry) it.next();
                    this.element = entry.getElement();
                    this.remaining = entry.getCount();
                }
                this.remaining--;
                return this.element;
            }
        };
    }

    public ImmutableList<E> asList() {
        ImmutableList<E> immutableList = this.asList;
        if (immutableList != null) {
            return immutableList;
        }
        ImmutableList<E> createAsList = createAsList();
        this.asList = createAsList;
        return createAsList;
    }

    /* access modifiers changed from: 0000 */
    public ImmutableList<E> createAsList() {
        if (isEmpty()) {
            return ImmutableList.m113of();
        }
        return new RegularImmutableAsList((ImmutableCollection<E>) this, toArray());
    }

    public boolean contains(@Nullable Object obj) {
        return count(obj) > 0;
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final int add(E e, int i) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final int remove(Object obj, int i) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final int setCount(E e, int i) {
        throw new UnsupportedOperationException();
    }

    @CanIgnoreReturnValue
    @Deprecated
    public final boolean setCount(E e, int i, int i2) {
        throw new UnsupportedOperationException();
    }

    /* access modifiers changed from: 0000 */
    @GwtIncompatible
    public int copyIntoArray(Object[] objArr, int i) {
        Iterator it = entrySet().iterator();
        while (it.hasNext()) {
            Entry entry = (Entry) it.next();
            Arrays.fill(objArr, i, entry.getCount() + i, entry.getElement());
            i += entry.getCount();
        }
        return i;
    }

    public boolean equals(@Nullable Object obj) {
        return Multisets.equalsImpl(this, obj);
    }

    public int hashCode() {
        return Sets.hashCodeImpl(entrySet());
    }

    public String toString() {
        return entrySet().toString();
    }

    public ImmutableSet<Entry<E>> entrySet() {
        ImmutableSet<Entry<E>> immutableSet = this.entrySet;
        if (immutableSet != null) {
            return immutableSet;
        }
        ImmutableSet<Entry<E>> createEntrySet = createEntrySet();
        this.entrySet = createEntrySet;
        return createEntrySet;
    }

    private final ImmutableSet<Entry<E>> createEntrySet() {
        return isEmpty() ? ImmutableSet.m155of() : new EntrySet();
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(this);
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }
}
