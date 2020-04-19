package org.apache.http.impl.p019io;

import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.message.LineFormatter;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.params.HttpParams;

@Deprecated
/* renamed from: org.apache.http.impl.io.HttpResponseWriter */
public class HttpResponseWriter extends AbstractMessageWriter<HttpResponse> {
    public HttpResponseWriter(SessionOutputBuffer sessionOutputBuffer, LineFormatter lineFormatter, HttpParams httpParams) {
        super(sessionOutputBuffer, lineFormatter, httpParams);
    }

    /* access modifiers changed from: protected */
    public void writeHeadLine(HttpResponse httpResponse) throws IOException {
        this.lineFormatter.formatStatusLine(this.lineBuf, httpResponse.getStatusLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
