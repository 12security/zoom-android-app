package com.google.common.p007io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;

@GwtIncompatible
@Beta
/* renamed from: com.google.common.io.LineProcessor */
public interface LineProcessor<T> {
    T getResult();

    @CanIgnoreReturnValue
    boolean processLine(String str) throws IOException;
}
