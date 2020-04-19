package com.google.common.base;

import com.google.common.annotations.GwtCompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import javax.annotation.Nullable;

@GwtCompatible
public interface Function<F, T> {
    @CanIgnoreReturnValue
    @Nullable
    T apply(@Nullable F f);

    boolean equals(@Nullable Object obj);
}
