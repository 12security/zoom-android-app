package com.google.common.collect;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;

@GwtCompatible
interface Constraint<E> {
    @CanIgnoreReturnValue
    E checkElement(E e);

    String toString();
}
