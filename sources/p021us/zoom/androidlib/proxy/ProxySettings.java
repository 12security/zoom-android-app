package p021us.zoom.androidlib.proxy;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.Secure;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.proxy.ProxySettings */
public class ProxySettings {
    private static final String TAG = "us.zoom.androidlib.proxy.ProxySettings";

    public static ProxyConfig[] getHttpProxyConfigs(Context context) {
        return getProxyConfigsForUri(context, "http://aafxbcfyfsghwcwu");
    }

    public static ProxyConfig[] getHttpsProxyConfigs(Context context) {
        return getProxyConfigsForUri(context, "https://aafxbcfyfsghwcwu");
    }

    public static ProxyConfig[] getFtpProxyConfigs(Context context) {
        return getProxyConfigsForUri(context, "ftp://aafxbcfyfsghwcwu");
    }

    public static ProxyConfig[] getSocketProxyConfigs(Context context) {
        return getProxyConfigsForUri(context, "socket://aafxbcfyfsghwcwu");
    }

    public static ProxyConfig[] getProxyConfigsForUri(Context context, String str) {
        return getProxySelectorConfigs(context, URI.create(str));
    }

    public static String getProxyConfigsStringForUri(Context context, String str) {
        ProxyConfig[] proxyConfigsForUri = getProxyConfigsForUri(context, str);
        if (proxyConfigsForUri == null || proxyConfigsForUri.length == 0) {
            return "";
        }
        String proxyConfig = proxyConfigsForUri[0].toString();
        for (int i = 1; i < proxyConfigsForUri.length; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(proxyConfig);
            sb.append(";");
            sb.append(proxyConfigsForUri[i].toString());
            proxyConfig = sb.toString();
        }
        return proxyConfig;
    }

    private static ProxyConfig[] getProxySelectorConfigs(Context context, URI uri) {
        List select = ProxySelector.getDefault().select(uri);
        if (select.size() <= 0) {
            return null;
        }
        ProxyConfig[] proxyConfigArr = new ProxyConfig[select.size()];
        for (int i = 0; i < select.size(); i++) {
            proxyConfigArr[i] = new ProxyConfig((Proxy) select.get(i));
        }
        return proxyConfigArr;
    }

    private static ProxyConfig[] getProxyConfigsBelowOS31(Context context) {
        ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) {
            return null;
        }
        String string = Secure.getString(contentResolver, "http_proxy");
        if (!StringUtil.isEmptyOrNull(string) && string.contains(":")) {
            String[] split = string.split(":");
            if (split.length == 2) {
                String str = split[0];
                try {
                    return new ProxyConfig[]{new ProxyConfig(new Proxy(Type.HTTP, new InetSocketAddress(str, Integer.valueOf(Integer.parseInt(split[1])).intValue())))};
                } catch (Exception unused) {
                }
            }
        }
        return null;
    }
}
