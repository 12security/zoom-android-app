package org.apache.http.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.http.ConnectionClosedException;
import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpConnectionMetrics;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpInetConnection;
import org.apache.http.HttpMessage;
import org.apache.http.config.MessageConstraints;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.impl.entity.LaxContentLengthStrategy;
import org.apache.http.impl.entity.StrictContentLengthStrategy;
import org.apache.http.impl.p019io.ChunkedInputStream;
import org.apache.http.impl.p019io.ChunkedOutputStream;
import org.apache.http.impl.p019io.ContentLengthInputStream;
import org.apache.http.impl.p019io.ContentLengthOutputStream;
import org.apache.http.impl.p019io.EmptyInputStream;
import org.apache.http.impl.p019io.HttpTransportMetricsImpl;
import org.apache.http.impl.p019io.IdentityInputStream;
import org.apache.http.impl.p019io.IdentityOutputStream;
import org.apache.http.impl.p019io.SessionInputBufferImpl;
import org.apache.http.impl.p019io.SessionOutputBufferImpl;
import org.apache.http.p020io.SessionInputBuffer;
import org.apache.http.p020io.SessionOutputBuffer;
import org.apache.http.util.Args;
import org.apache.http.util.NetUtils;

public class BHttpConnectionBase implements HttpConnection, HttpInetConnection {
    private final HttpConnectionMetricsImpl connMetrics;
    private final SessionInputBufferImpl inbuffer;
    private final ContentLengthStrategy incomingContentStrategy;
    private final MessageConstraints messageConstraints;
    private final SessionOutputBufferImpl outbuffer;
    private final ContentLengthStrategy outgoingContentStrategy;
    private final AtomicReference<Socket> socketHolder;

    protected BHttpConnectionBase(int i, int i2, CharsetDecoder charsetDecoder, CharsetEncoder charsetEncoder, MessageConstraints messageConstraints2, ContentLengthStrategy contentLengthStrategy, ContentLengthStrategy contentLengthStrategy2) {
        ContentLengthStrategy contentLengthStrategy3;
        ContentLengthStrategy contentLengthStrategy4;
        int i3 = i;
        MessageConstraints messageConstraints3 = messageConstraints2;
        Args.positive(i, "Buffer size");
        HttpTransportMetricsImpl httpTransportMetricsImpl = new HttpTransportMetricsImpl();
        HttpTransportMetricsImpl httpTransportMetricsImpl2 = new HttpTransportMetricsImpl();
        SessionInputBufferImpl sessionInputBufferImpl = new SessionInputBufferImpl(httpTransportMetricsImpl, i, -1, messageConstraints3 != null ? messageConstraints3 : MessageConstraints.DEFAULT, charsetDecoder);
        this.inbuffer = sessionInputBufferImpl;
        int i4 = i2;
        this.outbuffer = new SessionOutputBufferImpl(httpTransportMetricsImpl2, i, i2, charsetEncoder);
        this.messageConstraints = messageConstraints3;
        this.connMetrics = new HttpConnectionMetricsImpl(httpTransportMetricsImpl, httpTransportMetricsImpl2);
        if (contentLengthStrategy != null) {
            contentLengthStrategy3 = contentLengthStrategy;
        } else {
            contentLengthStrategy3 = LaxContentLengthStrategy.INSTANCE;
        }
        this.incomingContentStrategy = contentLengthStrategy3;
        if (contentLengthStrategy2 != null) {
            contentLengthStrategy4 = contentLengthStrategy2;
        } else {
            contentLengthStrategy4 = StrictContentLengthStrategy.INSTANCE;
        }
        this.outgoingContentStrategy = contentLengthStrategy4;
        this.socketHolder = new AtomicReference<>();
    }

    /* access modifiers changed from: protected */
    public void ensureOpen() throws IOException {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket != null) {
            if (!this.inbuffer.isBound()) {
                this.inbuffer.bind(getSocketInputStream(socket));
            }
            if (!this.outbuffer.isBound()) {
                this.outbuffer.bind(getSocketOutputStream(socket));
                return;
            }
            return;
        }
        throw new ConnectionClosedException("Connection is closed");
    }

    /* access modifiers changed from: protected */
    public InputStream getSocketInputStream(Socket socket) throws IOException {
        return socket.getInputStream();
    }

    /* access modifiers changed from: protected */
    public OutputStream getSocketOutputStream(Socket socket) throws IOException {
        return socket.getOutputStream();
    }

    /* access modifiers changed from: protected */
    public void bind(Socket socket) throws IOException {
        Args.notNull(socket, "Socket");
        this.socketHolder.set(socket);
        this.inbuffer.bind(null);
        this.outbuffer.bind(null);
    }

    /* access modifiers changed from: protected */
    public SessionInputBuffer getSessionInputBuffer() {
        return this.inbuffer;
    }

    /* access modifiers changed from: protected */
    public SessionOutputBuffer getSessionOutputBuffer() {
        return this.outbuffer;
    }

    /* access modifiers changed from: protected */
    public void doFlush() throws IOException {
        this.outbuffer.flush();
    }

    public boolean isOpen() {
        return this.socketHolder.get() != null;
    }

    /* access modifiers changed from: protected */
    public Socket getSocket() {
        return (Socket) this.socketHolder.get();
    }

    /* access modifiers changed from: protected */
    public OutputStream createOutputStream(long j, SessionOutputBuffer sessionOutputBuffer) {
        if (j == -2) {
            return new ChunkedOutputStream(2048, sessionOutputBuffer);
        }
        if (j == -1) {
            return new IdentityOutputStream(sessionOutputBuffer);
        }
        return new ContentLengthOutputStream(sessionOutputBuffer, j);
    }

    /* access modifiers changed from: protected */
    public OutputStream prepareOutput(HttpMessage httpMessage) throws HttpException {
        return createOutputStream(this.outgoingContentStrategy.determineLength(httpMessage), this.outbuffer);
    }

    /* access modifiers changed from: protected */
    public InputStream createInputStream(long j, SessionInputBuffer sessionInputBuffer) {
        if (j == -2) {
            return new ChunkedInputStream(sessionInputBuffer, this.messageConstraints);
        }
        if (j == -1) {
            return new IdentityInputStream(sessionInputBuffer);
        }
        if (j == 0) {
            return EmptyInputStream.INSTANCE;
        }
        return new ContentLengthInputStream(sessionInputBuffer, j);
    }

    /* access modifiers changed from: protected */
    public HttpEntity prepareInput(HttpMessage httpMessage) throws HttpException {
        BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
        long determineLength = this.incomingContentStrategy.determineLength(httpMessage);
        InputStream createInputStream = createInputStream(determineLength, this.inbuffer);
        if (determineLength == -2) {
            basicHttpEntity.setChunked(true);
            basicHttpEntity.setContentLength(-1);
            basicHttpEntity.setContent(createInputStream);
        } else if (determineLength == -1) {
            basicHttpEntity.setChunked(false);
            basicHttpEntity.setContentLength(-1);
            basicHttpEntity.setContent(createInputStream);
        } else {
            basicHttpEntity.setChunked(false);
            basicHttpEntity.setContentLength(determineLength);
            basicHttpEntity.setContent(createInputStream);
        }
        Header firstHeader = httpMessage.getFirstHeader("Content-Type");
        if (firstHeader != null) {
            basicHttpEntity.setContentType(firstHeader);
        }
        Header firstHeader2 = httpMessage.getFirstHeader("Content-Encoding");
        if (firstHeader2 != null) {
            basicHttpEntity.setContentEncoding(firstHeader2);
        }
        return basicHttpEntity;
    }

    public InetAddress getLocalAddress() {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket != null) {
            return socket.getLocalAddress();
        }
        return null;
    }

    public int getLocalPort() {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket != null) {
            return socket.getLocalPort();
        }
        return -1;
    }

    public InetAddress getRemoteAddress() {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket != null) {
            return socket.getInetAddress();
        }
        return null;
    }

    public int getRemotePort() {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket != null) {
            return socket.getPort();
        }
        return -1;
    }

    public void setSocketTimeout(int i) {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket != null) {
            try {
                socket.setSoTimeout(i);
            } catch (SocketException unused) {
            }
        }
    }

    public int getSocketTimeout() {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket == null) {
            return -1;
        }
        try {
            return socket.getSoTimeout();
        } catch (SocketException unused) {
            return -1;
        }
    }

    public void shutdown() throws IOException {
        Socket socket = (Socket) this.socketHolder.getAndSet(null);
        if (socket != null) {
            try {
                socket.setSoLinger(true, 0);
            } catch (IOException unused) {
            } catch (Throwable th) {
                socket.close();
                throw th;
            }
            socket.close();
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0018 */
    /* JADX WARNING: Removed duplicated region for block: B:9:? A[ExcHandler: UnsupportedOperationException (unused java.lang.UnsupportedOperationException), SYNTHETIC, Splitter:B:6:0x0018] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void close() throws java.io.IOException {
        /*
            r2 = this;
            java.util.concurrent.atomic.AtomicReference<java.net.Socket> r0 = r2.socketHolder
            r1 = 0
            java.lang.Object r0 = r0.getAndSet(r1)
            java.net.Socket r0 = (java.net.Socket) r0
            if (r0 == 0) goto L_0x0024
            org.apache.http.impl.io.SessionInputBufferImpl r1 = r2.inbuffer     // Catch:{ all -> 0x001f }
            r1.clear()     // Catch:{ all -> 0x001f }
            org.apache.http.impl.io.SessionOutputBufferImpl r1 = r2.outbuffer     // Catch:{ all -> 0x001f }
            r1.flush()     // Catch:{ all -> 0x001f }
            r0.shutdownOutput()     // Catch:{ IOException -> 0x0018 }
        L_0x0018:
            r0.shutdownInput()     // Catch:{ UnsupportedOperationException -> 0x001b, UnsupportedOperationException -> 0x001b }
        L_0x001b:
            r0.close()
            goto L_0x0024
        L_0x001f:
            r1 = move-exception
            r0.close()
            throw r1
        L_0x0024:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.BHttpConnectionBase.close():void");
    }

    private int fillInputBuffer(int i) throws IOException {
        Socket socket = (Socket) this.socketHolder.get();
        int soTimeout = socket.getSoTimeout();
        try {
            socket.setSoTimeout(i);
            return this.inbuffer.fillBuffer();
        } finally {
            socket.setSoTimeout(soTimeout);
        }
    }

    /* access modifiers changed from: protected */
    public boolean awaitInput(int i) throws IOException {
        if (this.inbuffer.hasBufferedData()) {
            return true;
        }
        fillInputBuffer(i);
        return this.inbuffer.hasBufferedData();
    }

    public boolean isStale() {
        if (!isOpen()) {
            return true;
        }
        boolean z = false;
        try {
            if (fillInputBuffer(1) < 0) {
                z = true;
            }
            return z;
        } catch (SocketTimeoutException unused) {
            return false;
        } catch (IOException unused2) {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void incrementRequestCount() {
        this.connMetrics.incrementRequestCount();
    }

    /* access modifiers changed from: protected */
    public void incrementResponseCount() {
        this.connMetrics.incrementResponseCount();
    }

    public HttpConnectionMetrics getMetrics() {
        return this.connMetrics;
    }

    public String toString() {
        Socket socket = (Socket) this.socketHolder.get();
        if (socket == null) {
            return "[Not bound]";
        }
        StringBuilder sb = new StringBuilder();
        SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
        SocketAddress localSocketAddress = socket.getLocalSocketAddress();
        if (!(remoteSocketAddress == null || localSocketAddress == null)) {
            NetUtils.formatAddress(sb, localSocketAddress);
            sb.append("<->");
            NetUtils.formatAddress(sb, remoteSocketAddress);
        }
        return sb.toString();
    }
}
