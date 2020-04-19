package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Lookup;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionOperator;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.UnsupportedSchemeException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class DefaultHttpClientConnectionOperator implements HttpClientConnectionOperator {
    static final String SOCKET_FACTORY_REGISTRY = "http.socket-factory-registry";
    private final DnsResolver dnsResolver;
    private final Log log = LogFactory.getLog(getClass());
    private final SchemePortResolver schemePortResolver;
    private final Lookup<ConnectionSocketFactory> socketFactoryRegistry;

    public DefaultHttpClientConnectionOperator(Lookup<ConnectionSocketFactory> lookup, SchemePortResolver schemePortResolver2, DnsResolver dnsResolver2) {
        Args.notNull(lookup, "Socket factory registry");
        this.socketFactoryRegistry = lookup;
        if (schemePortResolver2 == null) {
            schemePortResolver2 = DefaultSchemePortResolver.INSTANCE;
        }
        this.schemePortResolver = schemePortResolver2;
        if (dnsResolver2 == null) {
            dnsResolver2 = SystemDefaultDnsResolver.INSTANCE;
        }
        this.dnsResolver = dnsResolver2;
    }

    private Lookup<ConnectionSocketFactory> getSocketFactoryRegistry(HttpContext httpContext) {
        Lookup<ConnectionSocketFactory> lookup = (Lookup) httpContext.getAttribute("http.socket-factory-registry");
        return lookup == null ? this.socketFactoryRegistry : lookup;
    }

    /* JADX WARNING: Removed duplicated region for block: B:46:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x0135 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void connect(org.apache.http.conn.ManagedHttpClientConnection r21, org.apache.http.HttpHost r22, java.net.InetSocketAddress r23, int r24, org.apache.http.config.SocketConfig r25, org.apache.http.protocol.HttpContext r26) throws java.io.IOException {
        /*
            r20 = this;
            r1 = r20
            r2 = r21
            r10 = r22
            r11 = r26
            org.apache.http.config.Lookup r0 = r1.getSocketFactoryRegistry(r11)
            java.lang.String r3 = r22.getSchemeName()
            java.lang.Object r0 = r0.lookup(r3)
            r12 = r0
            org.apache.http.conn.socket.ConnectionSocketFactory r12 = (org.apache.http.conn.socket.ConnectionSocketFactory) r12
            if (r12 == 0) goto L_0x0142
            java.net.InetAddress r0 = r22.getAddress()
            r13 = 0
            r14 = 1
            if (r0 == 0) goto L_0x002a
            java.net.InetAddress[] r0 = new java.net.InetAddress[r14]
            java.net.InetAddress r3 = r22.getAddress()
            r0[r13] = r3
            goto L_0x0034
        L_0x002a:
            org.apache.http.conn.DnsResolver r0 = r1.dnsResolver
            java.lang.String r3 = r22.getHostName()
            java.net.InetAddress[] r0 = r0.resolve(r3)
        L_0x0034:
            r15 = r0
            org.apache.http.conn.SchemePortResolver r0 = r1.schemePortResolver
            int r9 = r0.resolve(r10)
            r8 = 0
        L_0x003c:
            int r0 = r15.length
            if (r8 >= r0) goto L_0x0141
            r0 = r15[r8]
            int r3 = r15.length
            int r3 = r3 - r14
            if (r8 != r3) goto L_0x0048
            r16 = 1
            goto L_0x004a
        L_0x0048:
            r16 = 0
        L_0x004a:
            java.net.Socket r5 = r12.createSocket(r11)
            int r3 = r25.getSoTimeout()
            r5.setSoTimeout(r3)
            boolean r3 = r25.isSoReuseAddress()
            r5.setReuseAddress(r3)
            boolean r3 = r25.isTcpNoDelay()
            r5.setTcpNoDelay(r3)
            boolean r3 = r25.isSoKeepAlive()
            r5.setKeepAlive(r3)
            int r3 = r25.getRcvBufSize()
            if (r3 <= 0) goto L_0x0077
            int r3 = r25.getRcvBufSize()
            r5.setReceiveBufferSize(r3)
        L_0x0077:
            int r3 = r25.getSndBufSize()
            if (r3 <= 0) goto L_0x0084
            int r3 = r25.getSndBufSize()
            r5.setSendBufferSize(r3)
        L_0x0084:
            int r3 = r25.getSoLinger()
            if (r3 < 0) goto L_0x008d
            r5.setSoLinger(r14, r3)
        L_0x008d:
            r2.bind(r5)
            java.net.InetSocketAddress r7 = new java.net.InetSocketAddress
            r7.<init>(r0, r9)
            org.apache.commons.logging.Log r0 = r1.log
            boolean r0 = r0.isDebugEnabled()
            if (r0 == 0) goto L_0x00b3
            org.apache.commons.logging.Log r0 = r1.log
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Connecting to "
            r3.append(r4)
            r3.append(r7)
            java.lang.String r3 = r3.toString()
            r0.debug(r3)
        L_0x00b3:
            r3 = r12
            r4 = r24
            r6 = r22
            r17 = r7
            r18 = r8
            r8 = r23
            r19 = r9
            r9 = r26
            java.net.Socket r0 = r3.connectSocket(r4, r5, r6, r7, r8, r9)     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            r2.bind(r0)     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            org.apache.commons.logging.Log r0 = r1.log     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            boolean r0 = r0.isDebugEnabled()     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            if (r0 == 0) goto L_0x00e7
            org.apache.commons.logging.Log r0 = r1.log     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            r3.<init>()     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            java.lang.String r4 = "Connection established "
            r3.append(r4)     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            r3.append(r2)     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            java.lang.String r3 = r3.toString()     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
            r0.debug(r3)     // Catch:{ SocketTimeoutException -> 0x0108, ConnectException -> 0x00ed, NoRouteToHostException -> 0x00e8 }
        L_0x00e7:
            return
        L_0x00e8:
            r0 = move-exception
            if (r16 != 0) goto L_0x00ec
            goto L_0x010b
        L_0x00ec:
            throw r0
        L_0x00ed:
            r0 = move-exception
            if (r16 == 0) goto L_0x010b
            java.lang.String r2 = r0.getMessage()
            java.lang.String r3 = "Connection timed out"
            boolean r2 = r3.equals(r2)
            if (r2 == 0) goto L_0x0102
            org.apache.http.conn.ConnectTimeoutException r2 = new org.apache.http.conn.ConnectTimeoutException
            r2.<init>(r0, r10, r15)
            throw r2
        L_0x0102:
            org.apache.http.conn.HttpHostConnectException r2 = new org.apache.http.conn.HttpHostConnectException
            r2.<init>(r0, r10, r15)
            throw r2
        L_0x0108:
            r0 = move-exception
            if (r16 != 0) goto L_0x013b
        L_0x010b:
            org.apache.commons.logging.Log r0 = r1.log
            boolean r0 = r0.isDebugEnabled()
            if (r0 == 0) goto L_0x0135
            org.apache.commons.logging.Log r0 = r1.log
            java.lang.StringBuilder r3 = new java.lang.StringBuilder
            r3.<init>()
            java.lang.String r4 = "Connect to "
            r3.append(r4)
            r4 = r17
            r3.append(r4)
            java.lang.String r4 = " timed out. "
            r3.append(r4)
            java.lang.String r4 = "Connection will be retried using another IP address"
            r3.append(r4)
            java.lang.String r3 = r3.toString()
            r0.debug(r3)
        L_0x0135:
            int r8 = r18 + 1
            r9 = r19
            goto L_0x003c
        L_0x013b:
            org.apache.http.conn.ConnectTimeoutException r2 = new org.apache.http.conn.ConnectTimeoutException
            r2.<init>(r0, r10, r15)
            throw r2
        L_0x0141:
            return
        L_0x0142:
            org.apache.http.conn.UnsupportedSchemeException r0 = new org.apache.http.conn.UnsupportedSchemeException
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = r22.getSchemeName()
            r2.append(r3)
            java.lang.String r3 = " protocol is not supported"
            r2.append(r3)
            java.lang.String r2 = r2.toString()
            r0.<init>(r2)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.DefaultHttpClientConnectionOperator.connect(org.apache.http.conn.ManagedHttpClientConnection, org.apache.http.HttpHost, java.net.InetSocketAddress, int, org.apache.http.config.SocketConfig, org.apache.http.protocol.HttpContext):void");
    }

    public void upgrade(ManagedHttpClientConnection managedHttpClientConnection, HttpHost httpHost, HttpContext httpContext) throws IOException {
        ConnectionSocketFactory connectionSocketFactory = (ConnectionSocketFactory) getSocketFactoryRegistry(HttpClientContext.adapt(httpContext)).lookup(httpHost.getSchemeName());
        if (connectionSocketFactory == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(httpHost.getSchemeName());
            sb.append(" protocol is not supported");
            throw new UnsupportedSchemeException(sb.toString());
        } else if (connectionSocketFactory instanceof LayeredConnectionSocketFactory) {
            managedHttpClientConnection.bind(((LayeredConnectionSocketFactory) connectionSocketFactory).createLayeredSocket(managedHttpClientConnection.getSocket(), httpHost.getHostName(), this.schemePortResolver.resolve(httpHost), httpContext));
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(httpHost.getSchemeName());
            sb2.append(" protocol does not support connection upgrade");
            throw new UnsupportedSchemeException(sb2.toString());
        }
    }
}
