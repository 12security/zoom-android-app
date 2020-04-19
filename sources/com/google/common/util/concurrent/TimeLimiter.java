package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
@Beta
public interface TimeLimiter {
    @CanIgnoreReturnValue
    <T> T callWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit, boolean z) throws Exception;

    <T> T newProxy(T t, Class<T> cls, long j, TimeUnit timeUnit);
}
