package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.math.IntMath;
import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;
import javax.annotation.Nullable;

@GwtCompatible
final class CartesianList<E> extends AbstractList<List<E>> implements RandomAccess {
    /* access modifiers changed from: private */
    public final transient ImmutableList<List<E>> axes;
    private final transient int[] axesSizeProduct;

    static <E> List<List<E>> create(List<? extends List<? extends E>> list) {
        Builder builder = new Builder(list.size());
        for (List copyOf : list) {
            ImmutableList copyOf2 = ImmutableList.copyOf((Collection<? extends E>) copyOf);
            if (copyOf2.isEmpty()) {
                return ImmutableList.m113of();
            }
            builder.add((Object) copyOf2);
        }
        return new CartesianList(builder.build());
    }

    CartesianList(ImmutableList<List<E>> immutableList) {
        this.axes = immutableList;
        int[] iArr = new int[(immutableList.size() + 1)];
        iArr[immutableList.size()] = 1;
        try {
            for (int size = immutableList.size() - 1; size >= 0; size--) {
                iArr[size] = IntMath.checkedMultiply(iArr[size + 1], ((List) immutableList.get(size)).size());
            }
            this.axesSizeProduct = iArr;
        } catch (ArithmeticException unused) {
            throw new IllegalArgumentException("Cartesian product too large; must have size at most Integer.MAX_VALUE");
        }
    }

    /* access modifiers changed from: private */
    public int getAxisIndexForProductIndex(int i, int i2) {
        return (i / this.axesSizeProduct[i2 + 1]) % ((List) this.axes.get(i2)).size();
    }

    public ImmutableList<E> get(final int i) {
        Preconditions.checkElementIndex(i, size());
        return new ImmutableList<E>() {
            /* access modifiers changed from: 0000 */
            public boolean isPartialView() {
                return true;
            }

            public int size() {
                return CartesianList.this.axes.size();
            }

            public E get(int i) {
                Preconditions.checkElementIndex(i, size());
                return ((List) CartesianList.this.axes.get(i)).get(CartesianList.this.getAxisIndexForProductIndex(i, i));
            }
        };
    }

    public int size() {
        return this.axesSizeProduct[0];
    }

    public boolean contains(@Nullable Object obj) {
        if (!(obj instanceof List)) {
            return false;
        }
        List list = (List) obj;
        if (list.size() != this.axes.size()) {
            return false;
        }
        ListIterator listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            if (!((List) this.axes.get(listIterator.nextIndex())).contains(listIterator.next())) {
                return false;
            }
        }
        return true;
    }
}
