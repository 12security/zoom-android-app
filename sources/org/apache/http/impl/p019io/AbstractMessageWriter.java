package org.apache.http.impl.p019io;

import java.io.IOException;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.p020io.HttpMessageWriter;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.CharArrayBuffer;

/* renamed from: org.apache.http.impl.io.AbstractMessageWriter */
public abstract class AbstractMessageWriter<T extends HttpMessage> implements HttpMessageWriter<T> {
    protected final CharArrayBuffer lineBuf;
    protected final LineFormatter lineFormatter;
    protected final SessionOutputBuffer sessionBuffer;

    /* access modifiers changed from: protected */
    public abstract void writeHeadLine(T t) throws IOException;

    @Deprecated
    public AbstractMessageWriter(SessionOutputBuffer sessionOutputBuffer, LineFormatter lineFormatter2, HttpParams httpParams) {
        Args.notNull(sessionOutputBuffer, "Session input buffer");
        this.sessionBuffer = sessionOutputBuffer;
        this.lineBuf = new CharArrayBuffer(128);
        if (lineFormatter2 == null) {
            lineFormatter2 = BasicLineFormatter.INSTANCE;
        }
        this.lineFormatter = lineFormatter2;
    }

    public AbstractMessageWriter(SessionOutputBuffer sessionOutputBuffer, LineFormatter lineFormatter2) {
        this.sessionBuffer = (SessionOutputBuffer) Args.notNull(sessionOutputBuffer, "Session input buffer");
        if (lineFormatter2 == null) {
            lineFormatter2 = BasicLineFormatter.INSTANCE;
        }
        this.lineFormatter = lineFormatter2;
        this.lineBuf = new CharArrayBuffer(128);
    }

    public void write(T t) throws IOException, HttpException {
        Args.notNull(t, "HTTP message");
        writeHeadLine(t);
        HeaderIterator headerIterator = t.headerIterator();
        while (headerIterator.hasNext()) {
            this.sessionBuffer.writeLine(this.lineFormatter.formatHeader(this.lineBuf, headerIterator.nextHeader()));
        }
        this.lineBuf.clear();
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
