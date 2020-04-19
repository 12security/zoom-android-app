package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
public abstract class ImmutableSet<E> extends ImmutableCollection<E> implements Set<E> {
    private static final int CUTOFF = 751619276;
    private static final double DESIRED_LOAD_FACTOR = 0.7d;
    static final int MAX_TABLE_SIZE = 1073741824;
    @LazyInit
    private transient ImmutableList<E> asList;

    public static class Builder<E> extends ArrayBasedBuilder<E> {
        public Builder() {
            this(4);
        }

        Builder(int i) {
            super(i);
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E e) {
            super.add(e);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> add(E... eArr) {
            super.add(eArr);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterable<? extends E> iterable) {
            super.addAll(iterable);
            return this;
        }

        @CanIgnoreReturnValue
        public Builder<E> addAll(Iterator<? extends E> it) {
            super.addAll(it);
            return this;
        }

        public ImmutableSet<E> build() {
            ImmutableSet<E> access$000 = ImmutableSet.construct(this.size, this.contents);
            this.size = access$000.size();
            return access$000;
        }
    }

    static abstract class Indexed<E> extends ImmutableSet<E> {
        /* access modifiers changed from: 0000 */
        public abstract E get(int i);

        Indexed() {
        }

        public UnmodifiableIterator<E> iterator() {
            return asList().iterator();
        }

        /* access modifiers changed from: 0000 */
        public ImmutableList<E> createAsList() {
            return new ImmutableAsList<E>() {
                public E get(int i) {
                    return Indexed.this.get(i);
                }

                /* access modifiers changed from: 0000 */
                public Indexed<E> delegateCollection() {
                    return Indexed.this;
                }
            };
        }
    }

    private static class SerializedForm implements Serializable {
        private static final long serialVersionUID = 0;
        final Object[] elements;

        SerializedForm(Object[] objArr) {
            this.elements = objArr;
        }

        /* access modifiers changed from: 0000 */
        public Object readResolve() {
            return ImmutableSet.copyOf((E[]) this.elements);
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean isHashCodeFast() {
        return false;
    }

    public abstract UnmodifiableIterator<E> iterator();

    /* renamed from: of */
    public static <E> ImmutableSet<E> m155of() {
        return RegularImmutableSet.EMPTY;
    }

    /* renamed from: of */
    public static <E> ImmutableSet<E> m156of(E e) {
        return new SingletonImmutableSet(e);
    }

    /* renamed from: of */
    public static <E> ImmutableSet<E> m157of(E e, E e2) {
        return construct(2, e, e2);
    }

    /* renamed from: of */
    public static <E> ImmutableSet<E> m158of(E e, E e2, E e3) {
        return construct(3, e, e2, e3);
    }

    /* renamed from: of */
    public static <E> ImmutableSet<E> m159of(E e, E e2, E e3, E e4) {
        return construct(4, e, e2, e3, e4);
    }

    /* renamed from: of */
    public static <E> ImmutableSet<E> m160of(E e, E e2, E e3, E e4, E e5) {
        return construct(5, e, e2, e3, e4, e5);
    }

    @SafeVarargs
    /* renamed from: of */
    public static <E> ImmutableSet<E> m161of(E e, E e2, E e3, E e4, E e5, E e6, E... eArr) {
        Object[] objArr = new Object[(eArr.length + 6)];
        objArr[0] = e;
        objArr[1] = e2;
        objArr[2] = e3;
        objArr[3] = e4;
        objArr[4] = e5;
        objArr[5] = e6;
        System.arraycopy(eArr, 0, objArr, 6, eArr.length);
        return construct(objArr.length, objArr);
    }

    /* access modifiers changed from: private */
    public static <E> ImmutableSet<E> construct(int i, Object... objArr) {
        switch (i) {
            case 0:
                return m155of();
            case 1:
                return m156of(objArr[0]);
            default:
                int chooseTableSize = chooseTableSize(i);
                Object[] objArr2 = new Object[chooseTableSize];
                int i2 = chooseTableSize - 1;
                int i3 = 0;
                int i4 = 0;
                for (int i5 = 0; i5 < i; i5++) {
                    Object checkElementNotNull = ObjectArrays.checkElementNotNull(objArr[i5], i5);
                    int hashCode = checkElementNotNull.hashCode();
                    int smear = Hashing.smear(hashCode);
                    while (true) {
                        int i6 = smear & i2;
                        Object obj = objArr2[i6];
                        if (obj == null) {
                            int i7 = i3 + 1;
                            objArr[i3] = checkElementNotNull;
                            objArr2[i6] = checkElementNotNull;
                            i4 += hashCode;
                            i3 = i7;
                        } else if (!obj.equals(checkElementNotNull)) {
                            smear++;
                        }
                    }
                }
                Arrays.fill(objArr, i3, i, null);
                if (i3 == 1) {
                    return new SingletonImmutableSet(objArr[0], i4);
                }
                if (chooseTableSize != chooseTableSize(i3)) {
                    return construct(i3, objArr);
                }
                if (i3 < objArr.length) {
                    objArr = ObjectArrays.arraysCopyOf(objArr, i3);
                }
                return new RegularImmutableSet(objArr, i4, objArr2, i2);
        }
    }

    @VisibleForTesting
    static int chooseTableSize(int i) {
        boolean z = true;
        if (i < CUTOFF) {
            int highestOneBit = Integer.highestOneBit(i - 1) << 1;
            while (((double) highestOneBit) * DESIRED_LOAD_FACTOR < ((double) i)) {
                highestOneBit <<= 1;
            }
            return highestOneBit;
        }
        if (i >= 1073741824) {
            z = false;
        }
        Preconditions.checkArgument(z, "collection too large");
        return 1073741824;
    }

    public static <E> ImmutableSet<E> copyOf(Collection<? extends E> collection) {
        if ((collection instanceof ImmutableSet) && !(collection instanceof ImmutableSortedSet)) {
            ImmutableSet<E> immutableSet = (ImmutableSet) collection;
            if (!immutableSet.isPartialView()) {
                return immutableSet;
            }
        } else if (collection instanceof EnumSet) {
            return copyOfEnumSet((EnumSet) collection);
        }
        Object[] array = collection.toArray();
        return construct(array.length, array);
    }

    public static <E> ImmutableSet<E> copyOf(Iterable<? extends E> iterable) {
        return iterable instanceof Collection ? copyOf((Collection) iterable) : copyOf(iterable.iterator());
    }

    public static <E> ImmutableSet<E> copyOf(Iterator<? extends E> it) {
        if (!it.hasNext()) {
            return m155of();
        }
        Object next = it.next();
        if (!it.hasNext()) {
            return m156of(next);
        }
        return new Builder().add(next).addAll((Iterator) it).build();
    }

    public static <E> ImmutableSet<E> copyOf(E[] eArr) {
        switch (eArr.length) {
            case 0:
                return m155of();
            case 1:
                return m156of(eArr[0]);
            default:
                return construct(eArr.length, (Object[]) eArr.clone());
        }
    }

    private static ImmutableSet copyOfEnumSet(EnumSet enumSet) {
        return ImmutableEnumSet.asImmutable(EnumSet.copyOf(enumSet));
    }

    ImmutableSet() {
    }

    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ImmutableSet) || !isHashCodeFast() || !((ImmutableSet) obj).isHashCodeFast() || hashCode() == obj.hashCode()) {
            return Sets.equalsImpl(this, obj);
        }
        return false;
    }

    public int hashCode() {
        return Sets.hashCodeImpl(this);
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
        return new RegularImmutableAsList((ImmutableCollection<E>) this, toArray());
    }

    /* access modifiers changed from: 0000 */
    public Object writeReplace() {
        return new SerializedForm(toArray());
    }

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }
}
