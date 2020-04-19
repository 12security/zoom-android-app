package org.apache.http.impl.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.OperatedClientConnection;
import org.apache.http.impl.SocketHttpClientConnection;
import org.apache.http.message.LineParser;
import org.apache.http.p020io.HttpMessageParser;
import org.apache.http.p020io.SessionInputBuffer;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Deprecated
public class DefaultClientConnection extends SocketHttpClientConnection implements OperatedClientConnection, ManagedHttpClientConnection, HttpContext {
    private final Map<String, Object> attributes = new HashMap();
    private boolean connSecure;
    private final Log headerLog = LogFactory.getLog("org.apache.http.headers");
    private final Log log = LogFactory.getLog(getClass());
    private volatile boolean shutdown;
    private volatile Socket socket;
    private HttpHost targetHost;
    private final Log wireLog = LogFactory.getLog("org.apache.http.wire");

    public String getId() {
        return null;
    }

    public final HttpHost getTargetHost() {
        return this.targetHost;
    }

    public final boolean isSecure() {
        return this.connSecure;
    }

    public final Socket getSocket() {
        return this.socket;
    }

    public SSLSession getSSLSession() {
        if (this.socket instanceof SSLSocket) {
            return ((SSLSocket) this.socket).getSession();
        }
        return null;
    }

    public void opening(Socket socket2, HttpHost httpHost) throws IOException {
        assertNotOpen();
        this.socket = socket2;
        this.targetHost = httpHost;
        if (this.shutdown) {
            socket2.close();
            throw new InterruptedIOException("Connection already shutdown");
        }
    }

    public void openCompleted(boolean z, HttpParams httpParams) throws IOException {
        Args.notNull(httpParams, "Parameters");
        assertNotOpen();
        this.connSecure = z;
        bind(this.socket, httpParams);
    }

    public void shutdown() throws IOException {
        this.shutdown = true;
        try {
            super.shutdown();
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Connection ");
                sb.append(this);
                sb.append(" shut down");
                log2.debug(sb.toString());
            }
            Socket socket2 = this.socket;
            if (socket2 != null) {
                socket2.close();
            }
        } catch (IOException e) {
            this.log.debug("I/O error shutting down connection", e);
        }
    }

    public void close() throws IOException {
        try {
            super.close();
            if (this.log.isDebugEnabled()) {
                Log log2 = this.log;
                StringBuilder sb = new StringBuilder();
                sb.append("Connection ");
                sb.append(this);
                sb.append(" closed");
                log2.debug(sb.toString());
            }
        } catch (IOException e) {
            this.log.debug("I/O error closing connection", e);
        }
    }

    /* access modifiers changed from: protected */
    public SessionInputBuffer createSessionInputBuffer(Socket socket2, int i, HttpParams httpParams) throws IOException {
        if (i <= 0) {
            i = 8192;
        }
        SessionInputBuffer createSessionInputBuffer = super.createSessionInputBuffer(socket2, i, httpParams);
        return this.wireLog.isDebugEnabled() ? new LoggingSessionInputBuffer(createSessionInputBuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(httpParams)) : createSessionInputBuffer;
    }

    /* access modifiers changed from: protected */
    public SessionOutputBuffer createSessionOutputBuffer(Socket socket2, int i, HttpParams httpParams) throws IOException {
        if (i <= 0) {
            i = 8192;
        }
        SessionOutputBuffer createSessionOutputBuffer = super.createSessionOutputBuffer(socket2, i, httpParams);
        return this.wireLog.isDebugEnabled() ? new LoggingSessionOutputBuffer(createSessionOutputBuffer, new Wire(this.wireLog), HttpProtocolParams.getHttpElementCharset(httpParams)) : createSessionOutputBuffer;
    }

    /* access modifiers changed from: protected */
    public HttpMessageParser<HttpResponse> createResponseParser(SessionInputBuffer sessionInputBuffer, HttpResponseFactory httpResponseFactory, HttpParams httpParams) {
        return new DefaultHttpResponseParser(sessionInputBuffer, (LineParser) null, httpResponseFactory, httpParams);
    }

    public void bind(Socket socket2) throws IOException {
        bind(socket2, new BasicHttpParams());
    }

    public void update(Socket socket2, HttpHost httpHost, boolean z, HttpParams httpParams) throws IOException {
        assertOpen();
        Args.notNull(httpHost, "Target host");
        Args.notNull(httpParams, "Parameters");
        if (socket2 != null) {
            this.socket = socket2;
            bind(socket2, httpParams);
        }
        this.targetHost = httpHost;
        this.connSecure = z;
    }

    public HttpResponse receiveResponseHeader() throws HttpException, IOException {
        Header[] allHeaders;
        HttpResponse receiveResponseHeader = super.receiveResponseHeader();
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Receiving response: ");
            sb.append(receiveResponseHeader.getStatusLine());
            log2.debug(sb.toString());
        }
        if (this.headerLog.isDebugEnabled()) {
            Log log3 = this.headerLog;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("<< ");
            sb2.append(receiveResponseHeader.getStatusLine().toString());
            log3.debug(sb2.toString());
            for (Header header : receiveResponseHeader.getAllHeaders()) {
                Log log4 = this.headerLog;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("<< ");
                sb3.append(header.toString());
                log4.debug(sb3.toString());
            }
        }
        return receiveResponseHeader;
    }

    public void sendRequestHeader(HttpRequest httpRequest) throws HttpException, IOException {
        Header[] allHeaders;
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Sending request: ");
            sb.append(httpRequest.getRequestLine());
            log2.debug(sb.toString());
        }
        super.sendRequestHeader(httpRequest);
        if (this.headerLog.isDebugEnabled()) {
            Log log3 = this.headerLog;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(">> ");
            sb2.append(httpRequest.getRequestLine().toString());
            log3.debug(sb2.toString());
            for (Header header : httpRequest.getAllHeaders()) {
                Log log4 = this.headerLog;
                StringBuilder sb3 = new StringBuilder();
                sb3.append(">> ");
                sb3.append(header.toString());
                log4.debug(sb3.toString());
            }
        }
    }

    public Object getAttribute(String str) {
        return this.attributes.get(str);
    }

    public Object removeAttribute(String str) {
        return this.attributes.remove(str);
    }

    public void setAttribute(String str, Object obj) {
        this.attributes.put(str, obj);
    }
}
