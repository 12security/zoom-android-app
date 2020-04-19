package com.dropbox.core;

import com.dropbox.core.util.IOUtil;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.Throwable;

public abstract class DbxStreamReader<E extends Throwable> {

    public static final class ByteArrayCopier extends DbxStreamReader<RuntimeException> {
        private final byte[] data;
        private final int length;
        private final int offset;

        public ByteArrayCopier(byte[] bArr, int i, int i2) {
            if (bArr == null) {
                throw new IllegalArgumentException("'data' can't be null");
            } else if (i < 0 || i >= bArr.length) {
                throw new IllegalArgumentException("'offset' is out of bounds");
            } else {
                int i3 = i + i2;
                if (i3 < i || i3 > bArr.length) {
                    throw new IllegalArgumentException("'offset+length' is out of bounds");
                }
                this.data = bArr;
                this.offset = i;
                this.length = i2;
            }
        }

        public ByteArrayCopier(byte[] bArr) {
            this(bArr, 0, bArr.length);
        }

        public void read(NoThrowInputStream noThrowInputStream) {
            noThrowInputStream.read(this.data, this.offset, this.length);
        }
    }

    public static final class OutputStreamCopier extends DbxStreamReader<IOException> {
        private final OutputStream dest;

        public OutputStreamCopier(OutputStream outputStream) {
            this.dest = outputStream;
        }

        public void read(NoThrowInputStream noThrowInputStream) throws IOException {
            IOUtil.copyStreamToStream(noThrowInputStream, this.dest);
        }
    }

    public abstract void read(NoThrowInputStream noThrowInputStream) throws Throwable;
}
