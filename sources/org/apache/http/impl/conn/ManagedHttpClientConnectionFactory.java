package org.apache.http.impl.conn;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.p019io.DefaultHttpRequestWriterFactory;
import org.apache.http.p020io.HttpMessageParserFactory;
import org.apache.http.p020io.HttpMessageWriterFactory;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class ManagedHttpClientConnectionFactory implements HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> {
    private static final AtomicLong COUNTER = new AtomicLong();
    public static final ManagedHttpClientConnectionFactory INSTANCE = new ManagedHttpClientConnectionFactory();
    private final Log headerlog;
    private final ContentLengthStrategy incomingContentStrategy;
    private final Log log;
    private final ContentLengthStrategy outgoingContentStrategy;
    private final HttpMessageWriterFactory<HttpRequest> requestWriterFactory;
    private final HttpMessageParserFactory<HttpResponse> responseParserFactory;
    private final Log wirelog;

    public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> httpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> httpMessageParserFactory, ContentLengthStrategy contentLengthStrategy, ContentLengthStrategy contentLengthStrategy2) {
        this.log = LogFactory.getLog(DefaultManagedHttpClientConnection.class);
        this.headerlog = LogFactory.getLog("org.apache.http.headers");
        this.wirelog = LogFactory.getLog("org.apache.http.wire");
        if (httpMessageWriterFactory == null) {
            httpMessageWriterFactory = DefaultHttpRequestWriterFactory.INSTANCE;
        }
        this.requestWriterFactory = httpMessageWriterFactory;
        if (httpMessageParserFactory == null) {
            httpMessageParserFactory = DefaultHttpResponseParserFactory.INSTANCE;
        }
        this.responseParserFactory = httpMessageParserFactory;
        if (contentLengthStrategy == null) {
            contentLengthStrategy = LaxContentLengthStrategy.INSTANCE;
        }
        this.incomingContentStrategy = contentLengthStrategy;
        if (contentLengthStrategy2 == null) {
            contentLengthStrategy2 = StrictContentLengthStrategy.INSTANCE;
        }
        this.outgoingContentStrategy = contentLengthStrategy2;
    }

    public ManagedHttpClientConnectionFactory(HttpMessageWriterFactory<HttpRequest> httpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> httpMessageParserFactory) {
        this(httpMessageWriterFactory, httpMessageParserFactory, null, null);
    }

    public ManagedHttpClientConnectionFactory(HttpMessageParserFactory<HttpResponse> httpMessageParserFactory) {
        this(null, httpMessageParserFactory);
    }

    public ManagedHttpClientConnectionFactory() {
        this(null, null);
    }

    public ManagedHttpClientConnection create(HttpRoute httpRoute, ConnectionConfig connectionConfig) {
        CharsetEncoder charsetEncoder;
        CharsetDecoder charsetDecoder;
        ConnectionConfig connectionConfig2 = connectionConfig != null ? connectionConfig : ConnectionConfig.DEFAULT;
        Charset charset = connectionConfig2.getCharset();
        CodingErrorAction malformedInputAction = connectionConfig2.getMalformedInputAction() != null ? connectionConfig2.getMalformedInputAction() : CodingErrorAction.REPORT;
        CodingErrorAction unmappableInputAction = connectionConfig2.getUnmappableInputAction() != null ? connectionConfig2.getUnmappableInputAction() : CodingErrorAction.REPORT;
        if (charset != null) {
            CharsetDecoder newDecoder = charset.newDecoder();
            newDecoder.onMalformedInput(malformedInputAction);
            newDecoder.onUnmappableCharacter(unmappableInputAction);
            CharsetEncoder newEncoder = charset.newEncoder();
            newEncoder.onMalformedInput(malformedInputAction);
            newEncoder.onUnmappableCharacter(unmappableInputAction);
            charsetEncoder = newEncoder;
            charsetDecoder = newDecoder;
        } else {
            charsetDecoder = null;
            charsetEncoder = null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("http-outgoing-");
        sb.append(Long.toString(COUNTER.getAndIncrement()));
        LoggingManagedHttpClientConnection loggingManagedHttpClientConnection = new LoggingManagedHttpClientConnection(sb.toString(), this.log, this.headerlog, this.wirelog, connectionConfig2.getBufferSize(), connectionConfig2.getFragmentSizeHint(), charsetDecoder, charsetEncoder, connectionConfig2.getMessageConstraints(), this.incomingContentStrategy, this.outgoingContentStrategy, this.requestWriterFactory, this.responseParserFactory);
        return loggingManagedHttpClientConnection;
    }
}
