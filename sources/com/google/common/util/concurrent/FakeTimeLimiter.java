package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@GwtIncompatible
@CanIgnoreReturnValue
@Beta
public final class FakeTimeLimiter implements TimeLimiter {
    public <T> T newProxy(T t, Class<T> cls, long j, TimeUnit timeUnit) {
        Preconditions.checkNotNull(t);
        Preconditions.checkNotNull(cls);
        Preconditions.checkNotNull(timeUnit);
        return t;
    }

    public <T> T callWithTimeout(Callable<T> callable, long j, TimeUnit timeUnit, boolean z) throws Exception {
        Preconditions.checkNotNull(timeUnit);
        return callable.call();
    }
}
