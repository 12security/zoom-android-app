package com.google.api.client.util;

import java.io.IOException;
import java.io.OutputStream;

final class ByteCountingOutputStream extends OutputStream {
    long count;

    ByteCountingOutputStream() {
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.count += (long) i2;
    }

    public void write(int i) throws IOException {
        this.count++;
    }
}
