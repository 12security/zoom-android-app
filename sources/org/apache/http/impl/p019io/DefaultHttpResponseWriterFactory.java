package org.apache.http.impl.p019io;

import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.message.BasicLineFormatter;
import org.apache.http.message.LineFormatter;
import org.apache.http.p020io.HttpMessageWriter;
import org.apache.http.p020io.HttpMessageWriterFactory;
import org.apache.http.p020io.SessionOutputBuffer;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
/* renamed from: org.apache.http.impl.io.DefaultHttpResponseWriterFactory */
public class DefaultHttpResponseWriterFactory implements HttpMessageWriterFactory<HttpResponse> {
    public static final DefaultHttpResponseWriterFactory INSTANCE = new DefaultHttpResponseWriterFactory();
    private final LineFormatter lineFormatter;

    public DefaultHttpResponseWriterFactory(LineFormatter lineFormatter2) {
        if (lineFormatter2 == null) {
            lineFormatter2 = BasicLineFormatter.INSTANCE;
        }
        this.lineFormatter = lineFormatter2;
    }

    public DefaultHttpResponseWriterFactory() {
        this(null);
    }

    public HttpMessageWriter<HttpResponse> create(SessionOutputBuffer sessionOutputBuffer) {
        return new DefaultHttpResponseWriter(sessionOutputBuffer, this.lineFormatter);
    }
}
