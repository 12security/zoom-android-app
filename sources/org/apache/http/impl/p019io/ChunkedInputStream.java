package org.apache.http.impl.p019io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.MalformedChunkCodingException;
import org.apache.http.TruncatedChunkException;
import org.apache.http.config.MessageConstraints;
import org.apache.http.p020io.BufferInfo;
import org.apache.http.p020io.SessionInputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

/* renamed from: org.apache.http.impl.io.ChunkedInputStream */
public class ChunkedInputStream extends InputStream {
    private static final int BUFFER_SIZE = 2048;
    private static final int CHUNK_CRLF = 3;
    private static final int CHUNK_DATA = 2;
    private static final int CHUNK_INVALID = Integer.MAX_VALUE;
    private static final int CHUNK_LEN = 1;
    private final CharArrayBuffer buffer;
    private long chunkSize;
    private boolean closed;
    private final MessageConstraints constraints;
    private boolean eof;
    private Header[] footers;

    /* renamed from: in */
    private final SessionInputBuffer f492in;
    private long pos;
    private int state;

    public ChunkedInputStream(SessionInputBuffer sessionInputBuffer, MessageConstraints messageConstraints) {
        this.eof = false;
        this.closed = false;
        this.footers = new Header[0];
        this.f492in = (SessionInputBuffer) Args.notNull(sessionInputBuffer, "Session input buffer");
        this.pos = 0;
        this.buffer = new CharArrayBuffer(16);
        if (messageConstraints == null) {
            messageConstraints = MessageConstraints.DEFAULT;
        }
        this.constraints = messageConstraints;
        this.state = 1;
    }

    public ChunkedInputStream(SessionInputBuffer sessionInputBuffer) {
        this(sessionInputBuffer, null);
    }

    public int available() throws IOException {
        SessionInputBuffer sessionInputBuffer = this.f492in;
        if (sessionInputBuffer instanceof BufferInfo) {
            return (int) Math.min((long) ((BufferInfo) sessionInputBuffer).length(), this.chunkSize - this.pos);
        }
        return 0;
    }

    public int read() throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.eof) {
            return -1;
        } else {
            if (this.state != 2) {
                nextChunk();
                if (this.eof) {
                    return -1;
                }
            }
            int read = this.f492in.read();
            if (read != -1) {
                this.pos++;
                if (this.pos >= this.chunkSize) {
                    this.state = 3;
                }
            }
            return read;
        }
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        if (this.closed) {
            throw new IOException("Attempted read from closed stream.");
        } else if (this.eof) {
            return -1;
        } else {
            if (this.state != 2) {
                nextChunk();
                if (this.eof) {
                    return -1;
                }
            }
            int read = this.f492in.read(bArr, i, (int) Math.min((long) i2, this.chunkSize - this.pos));
            if (read != -1) {
                this.pos += (long) read;
                if (this.pos >= this.chunkSize) {
                    this.state = 3;
                }
                return read;
            }
            this.eof = true;
            StringBuilder sb = new StringBuilder();
            sb.append("Truncated chunk ( expected size: ");
            sb.append(this.chunkSize);
            sb.append("; actual size: ");
            sb.append(this.pos);
            sb.append(")");
            throw new TruncatedChunkException(sb.toString());
        }
    }

    public int read(byte[] bArr) throws IOException {
        return read(bArr, 0, bArr.length);
    }

    private void nextChunk() throws IOException {
        if (this.state != Integer.MAX_VALUE) {
            try {
                this.chunkSize = getChunkSize();
                if (this.chunkSize >= 0) {
                    this.state = 2;
                    this.pos = 0;
                    if (this.chunkSize == 0) {
                        this.eof = true;
                        parseTrailerHeaders();
                        return;
                    }
                    return;
                }
                throw new MalformedChunkCodingException("Negative chunk size");
            } catch (MalformedChunkCodingException e) {
                this.state = Integer.MAX_VALUE;
                throw e;
            }
        } else {
            throw new MalformedChunkCodingException("Corrupt data stream");
        }
    }

    private long getChunkSize() throws IOException {
        int i = this.state;
        if (i != 1) {
            if (i == 3) {
                this.buffer.clear();
                if (this.f492in.readLine(this.buffer) == -1) {
                    throw new MalformedChunkCodingException("CRLF expected at end of chunk");
                } else if (this.buffer.isEmpty()) {
                    this.state = 1;
                } else {
                    throw new MalformedChunkCodingException("Unexpected content at the end of chunk");
                }
            } else {
                throw new IllegalStateException("Inconsistent codec state");
            }
        }
        this.buffer.clear();
        if (this.f492in.readLine(this.buffer) != -1) {
            int indexOf = this.buffer.indexOf(59);
            if (indexOf < 0) {
                indexOf = this.buffer.length();
            }
            String substringTrimmed = this.buffer.substringTrimmed(0, indexOf);
            try {
                return Long.parseLong(substringTrimmed, 16);
            } catch (NumberFormatException unused) {
                StringBuilder sb = new StringBuilder();
                sb.append("Bad chunk header: ");
                sb.append(substringTrimmed);
                throw new MalformedChunkCodingException(sb.toString());
            }
        } else {
            throw new ConnectionClosedException("Premature end of chunk coded message body: closing chunk expected");
        }
    }

    private void parseTrailerHeaders() throws IOException {
        try {
            this.footers = AbstractMessageParser.parseHeaders(this.f492in, this.constraints.getMaxHeaderCount(), this.constraints.getMaxLineLength(), null);
        } catch (HttpException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Invalid footer: ");
            sb.append(e.getMessage());
            MalformedChunkCodingException malformedChunkCodingException = new MalformedChunkCodingException(sb.toString());
            malformedChunkCodingException.initCause(e);
            throw malformedChunkCodingException;
        }
    }

    public void close() throws IOException {
        if (!this.closed) {
            try {
                if (!this.eof && this.state != Integer.MAX_VALUE) {
                    do {
                    } while (read(new byte[2048]) >= 0);
                }
            } finally {
                this.eof = true;
                this.closed = true;
            }
        }
    }

    public Header[] getFooters() {
        return (Header[]) this.footers.clone();
    }
}
