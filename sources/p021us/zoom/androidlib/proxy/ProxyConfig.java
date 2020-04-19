package p021us.zoom.androidlib.proxy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import org.apache.http.HttpHost;

/* renamed from: us.zoom.androidlib.proxy.ProxyConfig */
public class ProxyConfig {
    private Proxy mProxy;

    public ProxyConfig(Proxy proxy) {
        this.mProxy = proxy;
    }

    @Nullable
    public String getHost() {
        Proxy proxy = this.mProxy;
        if (proxy == null || proxy.type() == Type.DIRECT) {
            return null;
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) this.mProxy.address();
        if (inetSocketAddress != null) {
            return inetSocketAddress.getHostName();
        }
        return null;
    }

    public int getPort() {
        Proxy proxy = this.mProxy;
        if (proxy == null || proxy.type() == Type.DIRECT) {
            return 0;
        }
        InetSocketAddress inetSocketAddress = (InetSocketAddress) this.mProxy.address();
        if (inetSocketAddress != null) {
            return inetSocketAddress.getPort();
        }
        return 0;
    }

    public Type getProxyType() {
        Proxy proxy = this.mProxy;
        if (proxy == null) {
            return Type.DIRECT;
        }
        return proxy.type();
    }

    @NonNull
    public String toString() {
        Type proxyType = getProxyType();
        if (proxyType == Type.DIRECT || this.mProxy == null) {
            return "";
        }
        String str = null;
        if (proxyType == Type.HTTP) {
            str = HttpHost.DEFAULT_SCHEME_NAME;
        } else if (proxyType == Type.SOCKS) {
            str = "socks";
        }
        if (str == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("://");
        sb.append(getHost());
        sb.append(":");
        sb.append(getPort());
        return sb.toString();
    }
}
