package org.apache.http.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpServerConnection;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.DisallowIdentityContentLengthStrategy;
import org.apache.http.impl.p019io.DefaultHttpRequestParserFactory;
import org.apache.http.impl.p019io.DefaultHttpResponseWriterFactory;
import org.apache.http.p020io.HttpMessageParser;
import org.apache.http.p020io.HttpMessageParserFactory;
import org.apache.http.p020io.HttpMessageWriter;
import org.apache.http.p020io.HttpMessageWriterFactory;
import org.apache.http.util.Args;

public class DefaultBHttpServerConnection extends BHttpConnectionBase implements HttpServerConnection {
    private final HttpMessageParser<HttpRequest> requestParser;
    private final HttpMessageWriter<HttpResponse> responseWriter;

    /* access modifiers changed from: protected */
    public void onRequestReceived(HttpRequest httpRequest) {
    }

    /* access modifiers changed from: protected */
    public void onResponseSubmitted(HttpResponse httpResponse) {
    }

    public DefaultBHttpServerConnection(int i, int i2, CharsetDecoder charsetDecoder, CharsetEncoder charsetEncoder, MessageConstraints messageConstraints, ContentLengthStrategy contentLengthStrategy, ContentLengthStrategy contentLengthStrategy2, HttpMessageParserFactory<HttpRequest> httpMessageParserFactory, HttpMessageWriterFactory<HttpResponse> httpMessageWriterFactory) {
        HttpMessageParserFactory<HttpRequest> httpMessageParserFactory2;
        HttpMessageWriterFactory<HttpResponse> httpMessageWriterFactory2;
        super(i, i2, charsetDecoder, charsetEncoder, messageConstraints, contentLengthStrategy != null ? contentLengthStrategy : DisallowIdentityContentLengthStrategy.INSTANCE, contentLengthStrategy2);
        if (httpMessageParserFactory != null) {
            httpMessageParserFactory2 = httpMessageParserFactory;
        } else {
            httpMessageParserFactory2 = DefaultHttpRequestParserFactory.INSTANCE;
        }
        MessageConstraints messageConstraints2 = messageConstraints;
        this.requestParser = httpMessageParserFactory2.create(getSessionInputBuffer(), messageConstraints);
        if (httpMessageWriterFactory != null) {
            httpMessageWriterFactory2 = httpMessageWriterFactory;
        } else {
            httpMessageWriterFactory2 = DefaultHttpResponseWriterFactory.INSTANCE;
        }
        this.responseWriter = httpMessageWriterFactory2.create(getSessionOutputBuffer());
    }

    public DefaultBHttpServerConnection(int i, CharsetDecoder charsetDecoder, CharsetEncoder charsetEncoder, MessageConstraints messageConstraints) {
        this(i, i, charsetDecoder, charsetEncoder, messageConstraints, null, null, null, null);
    }

    public DefaultBHttpServerConnection(int i) {
        this(i, i, null, null, null, null, null, null, null);
    }

    public void bind(Socket socket) throws IOException {
        super.bind(socket);
    }

    public HttpRequest receiveRequestHeader() throws HttpException, IOException {
        ensureOpen();
        HttpRequest httpRequest = (HttpRequest) this.requestParser.parse();
        onRequestReceived(httpRequest);
        incrementRequestCount();
        return httpRequest;
    }

    public void receiveRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException {
        Args.notNull(httpEntityEnclosingRequest, "HTTP request");
        ensureOpen();
        httpEntityEnclosingRequest.setEntity(prepareInput(httpEntityEnclosingRequest));
    }

    public void sendResponseHeader(HttpResponse httpResponse) throws HttpException, IOException {
        Args.notNull(httpResponse, "HTTP response");
        ensureOpen();
        this.responseWriter.write(httpResponse);
        onResponseSubmitted(httpResponse);
        if (httpResponse.getStatusLine().getStatusCode() >= 200) {
            incrementResponseCount();
        }
    }

    public void sendResponseEntity(HttpResponse httpResponse) throws HttpException, IOException {
        Args.notNull(httpResponse, "HTTP response");
        ensureOpen();
        HttpEntity entity = httpResponse.getEntity();
        if (entity != null) {
            OutputStream prepareOutput = prepareOutput(httpResponse);
            entity.writeTo(prepareOutput);
            prepareOutput.close();
        }
    }

    public void flush() throws IOException {
        ensureOpen();
        doFlush();
    }
}
