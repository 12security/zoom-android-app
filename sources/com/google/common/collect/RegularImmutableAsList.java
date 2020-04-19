package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.j2objc.annotations.Weak;

@GwtCompatible(emulated = true)
class RegularImmutableAsList<E> extends ImmutableAsList<E> {
    @Weak
    private final ImmutableCollection<E> delegate;
    private final ImmutableList<? extends E> delegateList;

    RegularImmutableAsList(ImmutableCollection<E> immutableCollection, ImmutableList<? extends E> immutableList) {
        this.delegate = immutableCollection;
        this.delegateList = immutableList;
    }

    RegularImmutableAsList(ImmutableCollection<E> immutableCollection, Object[] objArr) {
        this(immutableCollection, ImmutableList.asImmutableList(objArr));
    }

    /* access modifiers changed from: 0000 */
    public ImmutableCollection<E> delegateCollection() {
        return this.delegate;
    }

    /* access modifiers changed from: 0000 */
    public ImmutableList<? extends E> delegateList() {
        return this.delegateList;
    }

    public UnmodifiableListIterator<E> listIterator(int i) {
        return this.delegateList.listIterator(i);
    }

    /* access modifiers changed from: 0000 */
    @GwtIncompatible
    public int copyIntoArray(Object[] objArr, int i) {
        return this.delegateList.copyIntoArray(objArr, i);
    }

    public E get(int i) {
        return this.delegateList.get(i);
    }
}
