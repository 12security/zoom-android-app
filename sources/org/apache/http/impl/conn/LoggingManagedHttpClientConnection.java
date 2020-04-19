package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.logging.Log;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.p020io.HttpMessageParserFactory;
import org.apache.http.p020io.HttpMessageWriterFactory;

class LoggingManagedHttpClientConnection extends DefaultManagedHttpClientConnection {
    private final Log headerlog;
    private final Log log;
    private final Wire wire;

    public LoggingManagedHttpClientConnection(String str, Log log2, Log log3, Log log4, int i, int i2, CharsetDecoder charsetDecoder, CharsetEncoder charsetEncoder, MessageConstraints messageConstraints, ContentLengthStrategy contentLengthStrategy, ContentLengthStrategy contentLengthStrategy2, HttpMessageWriterFactory<HttpRequest> httpMessageWriterFactory, HttpMessageParserFactory<HttpResponse> httpMessageParserFactory) {
        super(str, i, i2, charsetDecoder, charsetEncoder, messageConstraints, contentLengthStrategy, contentLengthStrategy2, httpMessageWriterFactory, httpMessageParserFactory);
        this.log = log2;
        this.headerlog = log3;
        this.wire = new Wire(log4, str);
    }

    public void close() throws IOException {
        if (super.isOpen()) {
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append(getId());
                sb.append(": Close connection");
                log2.debug(sb.toString());
            }
            super.close();
        }
    }

    public void setSocketTimeout(int i) {
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append(getId());
            sb.append(": set socket timeout to ");
            sb.append(i);
            log2.debug(sb.toString());
        }
        super.setSocketTimeout(i);
    }

    public void shutdown() throws IOException {
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append(getId());
            sb.append(": Shutdown connection");
            log2.debug(sb.toString());
        }
        super.shutdown();
    }

    /* access modifiers changed from: protected */
    public InputStream getSocketInputStream(Socket socket) throws IOException {
        InputStream socketInputStream = super.getSocketInputStream(socket);
        return this.wire.enabled() ? new LoggingInputStream(socketInputStream, this.wire) : socketInputStream;
    }

    /* access modifiers changed from: protected */
    public OutputStream getSocketOutputStream(Socket socket) throws IOException {
        OutputStream socketOutputStream = super.getSocketOutputStream(socket);
        return this.wire.enabled() ? new LoggingOutputStream(socketOutputStream, this.wire) : socketOutputStream;
    }

    /* access modifiers changed from: protected */
    public void onResponseReceived(HttpResponse httpResponse) {
        Header[] allHeaders;
        if (httpResponse != null && this.headerlog.isDebugEnabled()) {
            Log log2 = this.headerlog;
            StringBuilder sb = new StringBuilder();
            sb.append(getId());
            sb.append(" << ");
            sb.append(httpResponse.getStatusLine().toString());
            log2.debug(sb.toString());
            for (Header header : httpResponse.getAllHeaders()) {
                Log log3 = this.headerlog;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getId());
                sb2.append(" << ");
                sb2.append(header.toString());
                log3.debug(sb2.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onRequestSubmitted(HttpRequest httpRequest) {
        Header[] allHeaders;
        if (httpRequest != null && this.headerlog.isDebugEnabled()) {
            Log log2 = this.headerlog;
            StringBuilder sb = new StringBuilder();
            sb.append(getId());
            sb.append(" >> ");
            sb.append(httpRequest.getRequestLine().toString());
            log2.debug(sb.toString());
            for (Header header : httpRequest.getAllHeaders()) {
                Log log3 = this.headerlog;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(getId());
                sb2.append(" >> ");
                sb2.append(header.toString());
                log3.debug(sb2.toString());
            }
        }
    }
}
