package org.apache.http.impl.conn;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.protocol.HttpContext;

@Contract(threading = ThreadingBehavior.IMMUTABLE_CONDITIONAL)
public class SystemDefaultRoutePlanner extends DefaultRoutePlanner {
    private final ProxySelector proxySelector;

    /* renamed from: org.apache.http.impl.conn.SystemDefaultRoutePlanner$1 */
    static /* synthetic */ class C43911 {
        static final /* synthetic */ int[] $SwitchMap$java$net$Proxy$Type = new int[Type.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        static {
            /*
                java.net.Proxy$Type[] r0 = java.net.Proxy.Type.values()
                int r0 = r0.length
                int[] r0 = new int[r0]
                $SwitchMap$java$net$Proxy$Type = r0
                int[] r0 = $SwitchMap$java$net$Proxy$Type     // Catch:{ NoSuchFieldError -> 0x0014 }
                java.net.Proxy$Type r1 = java.net.Proxy.Type.DIRECT     // Catch:{ NoSuchFieldError -> 0x0014 }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x0014 }
                r2 = 1
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x0014 }
            L_0x0014:
                int[] r0 = $SwitchMap$java$net$Proxy$Type     // Catch:{ NoSuchFieldError -> 0x001f }
                java.net.Proxy$Type r1 = java.net.Proxy.Type.HTTP     // Catch:{ NoSuchFieldError -> 0x001f }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x001f }
                r2 = 2
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x001f }
            L_0x001f:
                int[] r0 = $SwitchMap$java$net$Proxy$Type     // Catch:{ NoSuchFieldError -> 0x002a }
                java.net.Proxy$Type r1 = java.net.Proxy.Type.SOCKS     // Catch:{ NoSuchFieldError -> 0x002a }
                int r1 = r1.ordinal()     // Catch:{ NoSuchFieldError -> 0x002a }
                r2 = 3
                r0[r1] = r2     // Catch:{ NoSuchFieldError -> 0x002a }
            L_0x002a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.http.impl.conn.SystemDefaultRoutePlanner.C43911.<clinit>():void");
        }
    }

    public SystemDefaultRoutePlanner(SchemePortResolver schemePortResolver, ProxySelector proxySelector2) {
        super(schemePortResolver);
        this.proxySelector = proxySelector2;
    }

    public SystemDefaultRoutePlanner(ProxySelector proxySelector2) {
        this(null, proxySelector2);
    }

    /* access modifiers changed from: protected */
    public HttpHost determineProxy(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) throws HttpException {
        try {
            URI uri = new URI(httpHost.toURI());
            ProxySelector proxySelector2 = this.proxySelector;
            if (proxySelector2 == null) {
                proxySelector2 = ProxySelector.getDefault();
            }
            HttpHost httpHost2 = null;
            if (proxySelector2 == null) {
                return null;
            }
            Proxy chooseProxy = chooseProxy(proxySelector2.select(uri));
            if (chooseProxy.type() == Type.HTTP) {
                if (chooseProxy.address() instanceof InetSocketAddress) {
                    InetSocketAddress inetSocketAddress = (InetSocketAddress) chooseProxy.address();
                    httpHost2 = new HttpHost(getHost(inetSocketAddress), inetSocketAddress.getPort());
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unable to handle non-Inet proxy address: ");
                    sb.append(chooseProxy.address());
                    throw new HttpException(sb.toString());
                }
            }
            return httpHost2;
        } catch (URISyntaxException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Cannot convert host to URI: ");
            sb2.append(httpHost);
            throw new HttpException(sb2.toString(), e);
        }
    }

    private String getHost(InetSocketAddress inetSocketAddress) {
        return inetSocketAddress.isUnresolved() ? inetSocketAddress.getHostName() : inetSocketAddress.getAddress().getHostAddress();
    }

    private Proxy chooseProxy(List<Proxy> list) {
        Proxy proxy = null;
        int i = 0;
        while (proxy == null && i < list.size()) {
            Proxy proxy2 = (Proxy) list.get(i);
            switch (C43911.$SwitchMap$java$net$Proxy$Type[proxy2.type().ordinal()]) {
                case 1:
                case 2:
                    proxy = proxy2;
                    break;
            }
            i++;
        }
        return proxy == null ? Proxy.NO_PROXY : proxy;
    }
}
