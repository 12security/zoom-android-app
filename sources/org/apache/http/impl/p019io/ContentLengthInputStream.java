package org.apache.http.impl.p019io;

import android.support.p000v4.media.session.PlaybackStateCompat;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.p020io.BufferInfo;
import org.apache.http.p020io.SessionInputBuffer;
import org.apache.http.util.Args;

/* renamed from: org.apache.http.impl.io.ContentLengthInputStream */
public class ContentLengthInputStream extends InputStream {
    private static final int BUFFER_SIZE = 2048;
    private boolean closed = false;
    private final long contentLength;

    /* renamed from: in */
    private SessionInputBuffer f493in = null;
    private long pos = 0;

    public ContentLengthInputStream(SessionInputBuffer sessionInputBuffer, long j) {
        this.f493in = (SessionInputBuffer) Args.notNull(sessionInputBuffer, "Session input buffer");
        this.contentLength = Args.notNegative(j, "Content length");
    }

    public void close() throws IOException {
        if (!this.closed) {
            try {
                if (this.pos < this.contentLength) {
                    do {
                    } while (read(new byte[2048]) >= 0);
                }
            } finally {
                this.closed = true;
            }
        }
    }

    public int available() throws IOException {
        SessionInputBuffer sessionInputBuffer = this.f493in;
        if (sessionInputBuffer instanceof BufferInfo) {
            return Math.min(((BufferInfo) sessionInputBuffer).length(), (int) (this.contentLength - this.pos));
        }
        return 0;
    }

    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.pos >= this.contentLength) {
            return -1;
        } else {
            int read = this.f493in.read();
            if (read != -1) {
                this.pos++;
            } else if (this.pos < this.contentLength) {
                StringBuilder sb = new StringBuilder();
                sb.append("Premature end of Content-Length delimited message body (expected: ");
                sb.append(this.contentLength);
                sb.append("; received: ");
                sb.append(this.pos);
                throw new ConnectionClosedException(sb.toString());
            }
            return read;
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (!this.closed) {
            long j = this.pos;
            long j2 = this.contentLength;
            if (j >= j2) {
                return -1;
            }
            if (((long) i2) + j > j2) {
                i2 = (int) (j2 - j);
            }
            int read = this.f493in.read(bArr, i, i2);
            if (read != -1 || this.pos >= this.contentLength) {
                if (read > 0) {
                    this.pos += (long) read;
                }
                return read;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Premature end of Content-Length delimited message body (expected: ");
            sb.append(this.contentLength);
            sb.append("; received: ");
            sb.append(this.pos);
            throw new ConnectionClosedException(sb.toString());
        }
        throw new IOException("Attempted read from closed stream.");
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    public long skip(long j) throws IOException {
        if (j <= 0) {
            return 0;
        }
        byte[] bArr = new byte[2048];
        long min = Math.min(j, this.contentLength - this.pos);
        long j2 = 0;
        while (min > 0) {
            int read = read(bArr, 0, (int) Math.min(PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH, min));
            if (read == -1) {
                break;
            }
            long j3 = (long) read;
            j2 += j3;
            min -= j3;
        }
        return j2;
    }
}
