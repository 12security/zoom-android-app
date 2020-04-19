package org.apache.http.impl.p019io;

import java.io.IOException;
import org.apache.http.HttpRequest;
import org.apache.http.message.LineFormatter;
import org.apache.http.p020io.SessionOutputBuffer;

/* renamed from: org.apache.http.impl.io.DefaultHttpRequestWriter */
public class DefaultHttpRequestWriter extends AbstractMessageWriter<HttpRequest> {
    public DefaultHttpRequestWriter(SessionOutputBuffer sessionOutputBuffer, LineFormatter lineFormatter) {
        super(sessionOutputBuffer, lineFormatter);
    }

    public DefaultHttpRequestWriter(SessionOutputBuffer sessionOutputBuffer) {
        this(sessionOutputBuffer, null);
    }

    /* access modifiers changed from: protected */
    public void writeHeadLine(HttpRequest httpRequest) throws IOException {
        this.lineFormatter.formatRequestLine(this.lineBuf, httpRequest.getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
