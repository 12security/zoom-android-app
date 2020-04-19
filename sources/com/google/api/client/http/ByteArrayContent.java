package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StringUtils;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class ByteArrayContent extends AbstractInputStreamContent {
    private final byte[] byteArray;
    private final int length;
    private final int offset;

    public boolean retrySupported() {
        return true;
    }

    public ByteArrayContent(String str, byte[] bArr) {
        this(str, bArr, 0, bArr.length);
    }

    public ByteArrayContent(String str, byte[] bArr, int i, int i2) {
        super(str);
        this.byteArray = (byte[]) Preconditions.checkNotNull(bArr);
        Preconditions.checkArgument(i >= 0 && i2 >= 0 && i + i2 <= bArr.length, "offset %s, length %s, array length %s", Integer.valueOf(i), Integer.valueOf(i2), Integer.valueOf(bArr.length));
        this.offset = i;
        this.length = i2;
    }

    public static ByteArrayContent fromString(String str, String str2) {
        return new ByteArrayContent(str, StringUtils.getBytesUtf8(str2));
    }

    public long getLength() {
        return (long) this.length;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.byteArray, this.offset, this.length);
    }

    public ByteArrayContent setType(String str) {
        return (ByteArrayContent) super.setType(str);
    }

    public ByteArrayContent setCloseInputStream(boolean z) {
        return (ByteArrayContent) super.setCloseInputStream(z);
    }
}
