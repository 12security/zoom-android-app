package com.google.common.p007io;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.io.IOException;

@GwtIncompatible
@Beta
/* renamed from: com.google.common.io.ByteProcessor */
public interface ByteProcessor<T> {
    T getResult();

    @CanIgnoreReturnValue
    boolean processBytes(byte[] bArr, int i, int i2) throws IOException;
}
