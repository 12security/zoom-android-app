package org.apache.http.conn.ssl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.util.PublicSuffixMatcherLoader;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.Args;
import org.apache.http.util.TextUtils;

@Contract(threading = ThreadingBehavior.SAFE)
public class SSLConnectionSocketFactory implements LayeredConnectionSocketFactory {
    @Deprecated
    public static final X509HostnameVerifier ALLOW_ALL_HOSTNAME_VERIFIER = AllowAllHostnameVerifier.INSTANCE;
    @Deprecated
    public static final X509HostnameVerifier BROWSER_COMPATIBLE_HOSTNAME_VERIFIER = BrowserCompatHostnameVerifier.INSTANCE;
    public static final String SSL = "SSL";
    public static final String SSLV2 = "SSLv2";
    @Deprecated
    public static final X509HostnameVerifier STRICT_HOSTNAME_VERIFIER = StrictHostnameVerifier.INSTANCE;
    public static final String TLS = "TLS";
    private final HostnameVerifier hostnameVerifier;
    private final Log log;
    private final SSLSocketFactory socketfactory;
    private final String[] supportedCipherSuites;
    private final String[] supportedProtocols;

    /* access modifiers changed from: protected */
    public void prepareSocket(SSLSocket sSLSocket) throws IOException {
    }

    public static HostnameVerifier getDefaultHostnameVerifier() {
        return new DefaultHostnameVerifier(PublicSuffixMatcherLoader.getDefault());
    }

    public static SSLConnectionSocketFactory getSocketFactory() throws SSLInitializationException {
        return new SSLConnectionSocketFactory(SSLContexts.createDefault(), getDefaultHostnameVerifier());
    }

    private static String[] split(String str) {
        if (TextUtils.isBlank(str)) {
            return null;
        }
        return str.split(" *, *");
    }

    public static SSLConnectionSocketFactory getSystemSocketFactory() throws SSLInitializationException {
        return new SSLConnectionSocketFactory((SSLSocketFactory) SSLSocketFactory.getDefault(), split(System.getProperty("https.protocols")), split(System.getProperty("https.cipherSuites")), getDefaultHostnameVerifier());
    }

    public SSLConnectionSocketFactory(SSLContext sSLContext) {
        this(sSLContext, getDefaultHostnameVerifier());
    }

    @Deprecated
    public SSLConnectionSocketFactory(SSLContext sSLContext, X509HostnameVerifier x509HostnameVerifier) {
        this(((SSLContext) Args.notNull(sSLContext, "SSL context")).getSocketFactory(), (String[]) null, (String[]) null, x509HostnameVerifier);
    }

    @Deprecated
    public SSLConnectionSocketFactory(SSLContext sSLContext, String[] strArr, String[] strArr2, X509HostnameVerifier x509HostnameVerifier) {
        this(((SSLContext) Args.notNull(sSLContext, "SSL context")).getSocketFactory(), strArr, strArr2, x509HostnameVerifier);
    }

    @Deprecated
    public SSLConnectionSocketFactory(SSLSocketFactory sSLSocketFactory, X509HostnameVerifier x509HostnameVerifier) {
        this(sSLSocketFactory, (String[]) null, (String[]) null, x509HostnameVerifier);
    }

    @Deprecated
    public SSLConnectionSocketFactory(SSLSocketFactory sSLSocketFactory, String[] strArr, String[] strArr2, X509HostnameVerifier x509HostnameVerifier) {
        this(sSLSocketFactory, strArr, strArr2, (HostnameVerifier) x509HostnameVerifier);
    }

    public SSLConnectionSocketFactory(SSLContext sSLContext, HostnameVerifier hostnameVerifier2) {
        this(((SSLContext) Args.notNull(sSLContext, "SSL context")).getSocketFactory(), (String[]) null, (String[]) null, hostnameVerifier2);
    }

    public SSLConnectionSocketFactory(SSLContext sSLContext, String[] strArr, String[] strArr2, HostnameVerifier hostnameVerifier2) {
        this(((SSLContext) Args.notNull(sSLContext, "SSL context")).getSocketFactory(), strArr, strArr2, hostnameVerifier2);
    }

    public SSLConnectionSocketFactory(SSLSocketFactory sSLSocketFactory, HostnameVerifier hostnameVerifier2) {
        this(sSLSocketFactory, (String[]) null, (String[]) null, hostnameVerifier2);
    }

    public SSLConnectionSocketFactory(SSLSocketFactory sSLSocketFactory, String[] strArr, String[] strArr2, HostnameVerifier hostnameVerifier2) {
        this.log = LogFactory.getLog(getClass());
        this.socketfactory = (SSLSocketFactory) Args.notNull(sSLSocketFactory, "SSL socket factory");
        this.supportedProtocols = strArr;
        this.supportedCipherSuites = strArr2;
        if (hostnameVerifier2 == null) {
            hostnameVerifier2 = getDefaultHostnameVerifier();
        }
        this.hostnameVerifier = hostnameVerifier2;
    }

    public Socket createSocket(HttpContext httpContext) throws IOException {
        return SocketFactory.getDefault().createSocket();
    }

    public Socket connectSocket(int i, Socket socket, HttpHost httpHost, InetSocketAddress inetSocketAddress, InetSocketAddress inetSocketAddress2, HttpContext httpContext) throws IOException {
        Args.notNull(httpHost, "HTTP host");
        Args.notNull(inetSocketAddress, "Remote address");
        if (socket == null) {
            socket = createSocket(httpContext);
        }
        if (inetSocketAddress2 != null) {
            socket.bind(inetSocketAddress2);
        }
        if (i > 0) {
            try {
                if (socket.getSoTimeout() == 0) {
                    socket.setSoTimeout(i);
                }
            } catch (IOException e) {
                try {
                    socket.close();
                } catch (IOException unused) {
                }
                throw e;
            }
        }
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Connecting socket to ");
            sb.append(inetSocketAddress);
            sb.append(" with timeout ");
            sb.append(i);
            log2.debug(sb.toString());
        }
        socket.connect(inetSocketAddress, i);
        if (!(socket instanceof SSLSocket)) {
            return createLayeredSocket(socket, httpHost.getHostName(), inetSocketAddress.getPort(), httpContext);
        }
        SSLSocket sSLSocket = (SSLSocket) socket;
        this.log.debug("Starting handshake");
        sSLSocket.startHandshake();
        verifyHostname(sSLSocket, httpHost.getHostName());
        return socket;
    }

    public Socket createLayeredSocket(Socket socket, String str, int i, HttpContext httpContext) throws IOException {
        SSLSocket sSLSocket = (SSLSocket) this.socketfactory.createSocket(socket, str, i, true);
        String[] strArr = this.supportedProtocols;
        if (strArr != null) {
            sSLSocket.setEnabledProtocols(strArr);
        } else {
            String[] enabledProtocols = sSLSocket.getEnabledProtocols();
            ArrayList arrayList = new ArrayList(enabledProtocols.length);
            for (String str2 : enabledProtocols) {
                if (!str2.startsWith("SSL")) {
                    arrayList.add(str2);
                }
            }
            if (!arrayList.isEmpty()) {
                sSLSocket.setEnabledProtocols((String[]) arrayList.toArray(new String[arrayList.size()]));
            }
        }
        String[] strArr2 = this.supportedCipherSuites;
        if (strArr2 != null) {
            sSLSocket.setEnabledCipherSuites(strArr2);
        }
        if (this.log.isDebugEnabled()) {
            Log log2 = this.log;
            StringBuilder sb = new StringBuilder();
            sb.append("Enabled protocols: ");
            sb.append(Arrays.asList(sSLSocket.getEnabledProtocols()));
            log2.debug(sb.toString());
            Log log3 = this.log;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Enabled cipher suites:");
            sb2.append(Arrays.asList(sSLSocket.getEnabledCipherSuites()));
            log3.debug(sb2.toString());
        }
        prepareSocket(sSLSocket);
        this.log.debug("Starting handshake");
        sSLSocket.startHandshake();
        verifyHostname(sSLSocket, str);
        return sSLSocket;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0127 */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x012f A[Catch:{ IOException -> 0x0168 }, RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:33:0x0130 A[Catch:{ IOException -> 0x0168 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void verifyHostname(javax.net.ssl.SSLSocket r9, java.lang.String r10) throws java.io.IOException {
        /*
            r8 = this;
            javax.net.ssl.SSLSession r0 = r9.getSession()     // Catch:{ IOException -> 0x0168 }
            if (r0 != 0) goto L_0x001a
            java.io.InputStream r0 = r9.getInputStream()     // Catch:{ IOException -> 0x0168 }
            r0.available()     // Catch:{ IOException -> 0x0168 }
            javax.net.ssl.SSLSession r0 = r9.getSession()     // Catch:{ IOException -> 0x0168 }
            if (r0 != 0) goto L_0x001a
            r9.startHandshake()     // Catch:{ IOException -> 0x0168 }
            javax.net.ssl.SSLSession r0 = r9.getSession()     // Catch:{ IOException -> 0x0168 }
        L_0x001a:
            if (r0 == 0) goto L_0x0160
            org.apache.commons.logging.Log r1 = r8.log     // Catch:{ IOException -> 0x0168 }
            boolean r1 = r1.isDebugEnabled()     // Catch:{ IOException -> 0x0168 }
            r2 = 0
            if (r1 == 0) goto L_0x0127
            org.apache.commons.logging.Log r1 = r8.log     // Catch:{ IOException -> 0x0168 }
            java.lang.String r3 = "Secure session established"
            r1.debug(r3)     // Catch:{ IOException -> 0x0168 }
            org.apache.commons.logging.Log r1 = r8.log     // Catch:{ IOException -> 0x0168 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0168 }
            r3.<init>()     // Catch:{ IOException -> 0x0168 }
            java.lang.String r4 = " negotiated protocol: "
            r3.append(r4)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r4 = r0.getProtocol()     // Catch:{ IOException -> 0x0168 }
            r3.append(r4)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x0168 }
            r1.debug(r3)     // Catch:{ IOException -> 0x0168 }
            org.apache.commons.logging.Log r1 = r8.log     // Catch:{ IOException -> 0x0168 }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0168 }
            r3.<init>()     // Catch:{ IOException -> 0x0168 }
            java.lang.String r4 = " negotiated cipher suite: "
            r3.append(r4)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r4 = r0.getCipherSuite()     // Catch:{ IOException -> 0x0168 }
            r3.append(r4)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r3 = r3.toString()     // Catch:{ IOException -> 0x0168 }
            r1.debug(r3)     // Catch:{ IOException -> 0x0168 }
            java.security.cert.Certificate[] r1 = r0.getPeerCertificates()     // Catch:{ Exception -> 0x0127 }
            r1 = r1[r2]     // Catch:{ Exception -> 0x0127 }
            java.security.cert.X509Certificate r1 = (java.security.cert.X509Certificate) r1     // Catch:{ Exception -> 0x0127 }
            javax.security.auth.x500.X500Principal r3 = r1.getSubjectX500Principal()     // Catch:{ Exception -> 0x0127 }
            org.apache.commons.logging.Log r4 = r8.log     // Catch:{ Exception -> 0x0127 }
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0127 }
            r5.<init>()     // Catch:{ Exception -> 0x0127 }
            java.lang.String r6 = " peer principal: "
            r5.append(r6)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0127 }
            r5.append(r3)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r3 = r5.toString()     // Catch:{ Exception -> 0x0127 }
            r4.debug(r3)     // Catch:{ Exception -> 0x0127 }
            java.util.Collection r3 = r1.getSubjectAlternativeNames()     // Catch:{ Exception -> 0x0127 }
            r4 = 1
            if (r3 == 0) goto L_0x00c8
            java.util.ArrayList r5 = new java.util.ArrayList     // Catch:{ Exception -> 0x0127 }
            r5.<init>()     // Catch:{ Exception -> 0x0127 }
            java.util.Iterator r3 = r3.iterator()     // Catch:{ Exception -> 0x0127 }
        L_0x0096:
            boolean r6 = r3.hasNext()     // Catch:{ Exception -> 0x0127 }
            if (r6 == 0) goto L_0x00b2
            java.lang.Object r6 = r3.next()     // Catch:{ Exception -> 0x0127 }
            java.util.List r6 = (java.util.List) r6     // Catch:{ Exception -> 0x0127 }
            boolean r7 = r6.isEmpty()     // Catch:{ Exception -> 0x0127 }
            if (r7 != 0) goto L_0x0096
            java.lang.Object r6 = r6.get(r4)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r6 = (java.lang.String) r6     // Catch:{ Exception -> 0x0127 }
            r5.add(r6)     // Catch:{ Exception -> 0x0127 }
            goto L_0x0096
        L_0x00b2:
            org.apache.commons.logging.Log r3 = r8.log     // Catch:{ Exception -> 0x0127 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0127 }
            r6.<init>()     // Catch:{ Exception -> 0x0127 }
            java.lang.String r7 = " peer alternative names: "
            r6.append(r7)     // Catch:{ Exception -> 0x0127 }
            r6.append(r5)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r5 = r6.toString()     // Catch:{ Exception -> 0x0127 }
            r3.debug(r5)     // Catch:{ Exception -> 0x0127 }
        L_0x00c8:
            javax.security.auth.x500.X500Principal r3 = r1.getIssuerX500Principal()     // Catch:{ Exception -> 0x0127 }
            org.apache.commons.logging.Log r5 = r8.log     // Catch:{ Exception -> 0x0127 }
            java.lang.StringBuilder r6 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0127 }
            r6.<init>()     // Catch:{ Exception -> 0x0127 }
            java.lang.String r7 = " issuer principal: "
            r6.append(r7)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0127 }
            r6.append(r3)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r3 = r6.toString()     // Catch:{ Exception -> 0x0127 }
            r5.debug(r3)     // Catch:{ Exception -> 0x0127 }
            java.util.Collection r1 = r1.getIssuerAlternativeNames()     // Catch:{ Exception -> 0x0127 }
            if (r1 == 0) goto L_0x0127
            java.util.ArrayList r3 = new java.util.ArrayList     // Catch:{ Exception -> 0x0127 }
            r3.<init>()     // Catch:{ Exception -> 0x0127 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ Exception -> 0x0127 }
        L_0x00f5:
            boolean r5 = r1.hasNext()     // Catch:{ Exception -> 0x0127 }
            if (r5 == 0) goto L_0x0111
            java.lang.Object r5 = r1.next()     // Catch:{ Exception -> 0x0127 }
            java.util.List r5 = (java.util.List) r5     // Catch:{ Exception -> 0x0127 }
            boolean r6 = r5.isEmpty()     // Catch:{ Exception -> 0x0127 }
            if (r6 != 0) goto L_0x00f5
            java.lang.Object r5 = r5.get(r4)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r5 = (java.lang.String) r5     // Catch:{ Exception -> 0x0127 }
            r3.add(r5)     // Catch:{ Exception -> 0x0127 }
            goto L_0x00f5
        L_0x0111:
            org.apache.commons.logging.Log r1 = r8.log     // Catch:{ Exception -> 0x0127 }
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0127 }
            r4.<init>()     // Catch:{ Exception -> 0x0127 }
            java.lang.String r5 = " issuer alternative names: "
            r4.append(r5)     // Catch:{ Exception -> 0x0127 }
            r4.append(r3)     // Catch:{ Exception -> 0x0127 }
            java.lang.String r3 = r4.toString()     // Catch:{ Exception -> 0x0127 }
            r1.debug(r3)     // Catch:{ Exception -> 0x0127 }
        L_0x0127:
            javax.net.ssl.HostnameVerifier r1 = r8.hostnameVerifier     // Catch:{ IOException -> 0x0168 }
            boolean r1 = r1.verify(r10, r0)     // Catch:{ IOException -> 0x0168 }
            if (r1 == 0) goto L_0x0130
            return
        L_0x0130:
            java.security.cert.Certificate[] r0 = r0.getPeerCertificates()     // Catch:{ IOException -> 0x0168 }
            r0 = r0[r2]     // Catch:{ IOException -> 0x0168 }
            java.security.cert.X509Certificate r0 = (java.security.cert.X509Certificate) r0     // Catch:{ IOException -> 0x0168 }
            java.util.List r0 = org.apache.http.conn.ssl.DefaultHostnameVerifier.getSubjectAltNames(r0)     // Catch:{ IOException -> 0x0168 }
            javax.net.ssl.SSLPeerUnverifiedException r1 = new javax.net.ssl.SSLPeerUnverifiedException     // Catch:{ IOException -> 0x0168 }
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ IOException -> 0x0168 }
            r2.<init>()     // Catch:{ IOException -> 0x0168 }
            java.lang.String r3 = "Certificate for <"
            r2.append(r3)     // Catch:{ IOException -> 0x0168 }
            r2.append(r10)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r10 = "> doesn't match any "
            r2.append(r10)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r10 = "of the subject alternative names: "
            r2.append(r10)     // Catch:{ IOException -> 0x0168 }
            r2.append(r0)     // Catch:{ IOException -> 0x0168 }
            java.lang.String r10 = r2.toString()     // Catch:{ IOException -> 0x0168 }
            r1.<init>(r10)     // Catch:{ IOException -> 0x0168 }
            throw r1     // Catch:{ IOException -> 0x0168 }
        L_0x0160:
            javax.net.ssl.SSLHandshakeException r10 = new javax.net.ssl.SSLHandshakeException     // Catch:{ IOException -> 0x0168 }
            java.lang.String r0 = "SSL session not available"
            r10.<init>(r0)     // Catch:{ IOException -> 0x0168 }
            throw r10     // Catch:{ IOException -> 0x0168 }
        L_0x0168:
            r10 = move-exception
            r9.close()     // Catch:{ Exception -> 0x016c }
        L_0x016c:
            throw r10
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.http.conn.ssl.SSLConnectionSocketFactory.verifyHostname(javax.net.ssl.SSLSocket, java.lang.String):void");
    }
}
