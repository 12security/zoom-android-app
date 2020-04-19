package com.google.common.p007io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.annotation.Nullable;

@GwtIncompatible
/* renamed from: com.google.common.io.MultiInputStream */
final class MultiInputStream extends InputStream {

    /* renamed from: in */
    private InputStream f281in;

    /* renamed from: it */
    private Iterator<? extends ByteSource> f282it;

    public boolean markSupported() {
        return false;
    }

    public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
        this.f282it = (Iterator) Preconditions.checkNotNull(it);
        advance();
    }

    public void close() throws IOException {
        InputStream inputStream = this.f281in;
        if (inputStream != null) {
            try {
                inputStream.close();
            } finally {
                this.f281in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.f282it.hasNext()) {
            this.f281in = ((ByteSource) this.f282it.next()).openStream();
        }
    }

    public int available() throws IOException {
        InputStream inputStream = this.f281in;
        if (inputStream == null) {
            return 0;
        }
        return inputStream.available();
    }

    public int read() throws IOException {
        InputStream inputStream = this.f281in;
        if (inputStream == null) {
            return -1;
        }
        int read = inputStream.read();
        if (read != -1) {
            return read;
        }
        advance();
        return read();
    }

    public int read(@Nullable byte[] bArr, int i, int i2) throws IOException {
        InputStream inputStream = this.f281in;
        if (inputStream == null) {
            return -1;
        }
        int read = inputStream.read(bArr, i, i2);
        if (read != -1) {
            return read;
        }
        advance();
        return read(bArr, i, i2);
    }

    public long skip(long j) throws IOException {
        InputStream inputStream = this.f281in;
        if (inputStream == null || j <= 0) {
            return 0;
        }
        long skip = inputStream.skip(j);
        if (skip != 0) {
            return skip;
        }
        if (read() == -1) {
            return 0;
        }
        return this.f281in.skip(j - 1) + 1;
    }
}
