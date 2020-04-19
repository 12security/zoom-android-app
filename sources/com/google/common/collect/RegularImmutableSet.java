package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Nullable;

@GwtCompatible(emulated = true, serializable = true)
final class RegularImmutableSet<E> extends Indexed<E> {
    static final RegularImmutableSet<Object> EMPTY = new RegularImmutableSet<>(ObjectArrays.EMPTY_ARRAY, 0, null, 0);
    private final transient Object[] elements;
    private final transient int hashCode;
    private final transient int mask;
    @VisibleForTesting
    final transient Object[] table;

    /* access modifiers changed from: 0000 */
    public boolean isHashCodeFast() {
        return true;
    }

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return false;
    }

    RegularImmutableSet(Object[] objArr, int i, Object[] objArr2, int i2) {
        this.elements = objArr;
        this.table = objArr2;
        this.mask = i2;
        this.hashCode = i;
    }

    public boolean contains(@Nullable Object obj) {
        Object[] objArr = this.table;
        if (obj == null || objArr == null) {
            return false;
        }
        int smearedHash = Hashing.smearedHash(obj);
        while (true) {
            int i = smearedHash & this.mask;
            Object obj2 = objArr[i];
            if (obj2 == null) {
                return false;
            }
            if (obj2.equals(obj)) {
                return true;
            }
            smearedHash = i + 1;
        }
    }

    public int size() {
        return this.elements.length;
    }

    /* access modifiers changed from: 0000 */
    public E get(int i) {
        return this.elements[i];
    }

    /* access modifiers changed from: 0000 */
    public int copyIntoArray(Object[] objArr, int i) {
        Object[] objArr2 = this.elements;
        System.arraycopy(objArr2, 0, objArr, i, objArr2.length);
        return i + this.elements.length;
    }

    /* access modifiers changed from: 0000 */
    public ImmutableList<E> createAsList() {
        return this.table == null ? ImmutableList.m113of() : new RegularImmutableAsList((ImmutableCollection<E>) this, this.elements);
    }

    public int hashCode() {
        return this.hashCode;
    }
}
