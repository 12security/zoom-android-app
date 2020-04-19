package org.apache.http.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;

public class InputStreamEntity extends AbstractHttpEntity {
    private final InputStream content;
    private final long length;

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return true;
    }

    public InputStreamEntity(InputStream inputStream) {
        this(inputStream, -1);
    }

    public InputStreamEntity(InputStream inputStream, long j) {
        this(inputStream, j, null);
    }

    public InputStreamEntity(InputStream inputStream, ContentType contentType) {
        this(inputStream, -1, contentType);
    }

    public InputStreamEntity(InputStream inputStream, long j, ContentType contentType) {
        this.content = (InputStream) Args.notNull(inputStream, "Source input stream");
        this.length = j;
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    public long getContentLength() {
        return this.length;
    }

    public InputStream getContent() throws IOException {
        return this.content;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Args.notNull(outputStream, "Output stream");
        InputStream inputStream = this.content;
        try {
            byte[] bArr = new byte[4096];
            if (this.length >= 0) {
                long j = this.length;
                while (true) {
                    if (j <= 0) {
                        break;
                    }
                    int read = inputStream.read(bArr, 0, (int) Math.min(4096, j));
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, read);
                    j -= (long) read;
                }
            } else {
                while (true) {
                    int read2 = inputStream.read(bArr);
                    if (read2 == -1) {
                        break;
                    }
                    outputStream.write(bArr, 0, read2);
                }
            }
        } finally {
            inputStream.close();
        }
    }
}
