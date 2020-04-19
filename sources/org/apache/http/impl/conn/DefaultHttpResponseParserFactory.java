package org.apache.http.impl.conn;

import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.MessageConstraints;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.p020io.HttpMessageParser;
import org.apache.http.p020io.HttpMessageParserFactory;
import org.apache.http.p020io.SessionInputBuffer;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class DefaultHttpResponseParserFactory implements HttpMessageParserFactory<HttpResponse> {
    public static final DefaultHttpResponseParserFactory INSTANCE = new DefaultHttpResponseParserFactory();
    private final LineParser lineParser;
    private final HttpResponseFactory responseFactory;

    public DefaultHttpResponseParserFactory(LineParser lineParser2, HttpResponseFactory httpResponseFactory) {
        if (lineParser2 == null) {
            lineParser2 = BasicLineParser.INSTANCE;
        }
        this.lineParser = lineParser2;
        if (httpResponseFactory == null) {
            httpResponseFactory = DefaultHttpResponseFactory.INSTANCE;
        }
        this.responseFactory = httpResponseFactory;
    }

    public DefaultHttpResponseParserFactory(HttpResponseFactory httpResponseFactory) {
        this(null, httpResponseFactory);
    }

    public DefaultHttpResponseParserFactory() {
        this(null, null);
    }

    public HttpMessageParser<HttpResponse> create(SessionInputBuffer sessionInputBuffer, MessageConstraints messageConstraints) {
        return new DefaultHttpResponseParser(sessionInputBuffer, this.lineParser, this.responseFactory, messageConstraints);
    }
}
