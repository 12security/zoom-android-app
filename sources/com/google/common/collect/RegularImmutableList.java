package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;

@GwtCompatible(emulated = true, serializable = true)
class RegularImmutableList<E> extends ImmutableList<E> {
    static final ImmutableList<Object> EMPTY = new RegularImmutableList(ObjectArrays.EMPTY_ARRAY);
    private final transient Object[] array;

    /* access modifiers changed from: 0000 */
    public boolean isPartialView() {
        return false;
    }

    RegularImmutableList(Object[] objArr) {
        this.array = objArr;
    }

    public int size() {
        return this.array.length;
    }

    /* access modifiers changed from: 0000 */
    public int copyIntoArray(Object[] objArr, int i) {
        Object[] objArr2 = this.array;
        System.arraycopy(objArr2, 0, objArr, i, objArr2.length);
        return i + this.array.length;
    }

    public E get(int i) {
        return this.array[i];
    }

    public UnmodifiableListIterator<E> listIterator(int i) {
        Object[] objArr = this.array;
        return Iterators.forArray(objArr, 0, objArr.length, i);
    }
}
