package org.apache.http.entity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.util.Args;

public class ByteArrayEntity extends AbstractHttpEntity implements Cloneable {

    /* renamed from: b */
    private final byte[] f480b;
    @Deprecated
    protected final byte[] content;
    private final int len;
    private final int off;

    public boolean isRepeatable() {
        return true;
    }

    public boolean isStreaming() {
        return false;
    }

    public ByteArrayEntity(byte[] bArr, ContentType contentType) {
        Args.notNull(bArr, "Source byte array");
        this.content = bArr;
        this.f480b = bArr;
        this.off = 0;
        this.len = this.f480b.length;
        if (contentType != null) {
            setContentType(contentType.toString());
        }
    }

    public ByteArrayEntity(byte[] bArr, int i, int i2, ContentType contentType) {
        Args.notNull(bArr, "Source byte array");
        if (i >= 0 && i <= bArr.length && i2 >= 0) {
            int i3 = i + i2;
            if (i3 >= 0 && i3 <= bArr.length) {
                this.content = bArr;
                this.f480b = bArr;
                this.off = i;
                this.len = i2;
                if (contentType != null) {
                    setContentType(contentType.toString());
                    return;
                }
                return;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("off: ");
        sb.append(i);
        sb.append(" len: ");
        sb.append(i2);
        sb.append(" b.length: ");
        sb.append(bArr.length);
        throw new IndexOutOfBoundsException(sb.toString());
    }

    public ByteArrayEntity(byte[] bArr) {
        this(bArr, null);
    }

    public ByteArrayEntity(byte[] bArr, int i, int i2) {
        this(bArr, i, i2, null);
    }

    public long getContentLength() {
        return (long) this.len;
    }

    public InputStream getContent() {
        return new ByteArrayInputStream(this.f480b, this.off, this.len);
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        Args.notNull(outputStream, "Output stream");
        outputStream.write(this.f480b, this.off, this.len);
        outputStream.flush();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
