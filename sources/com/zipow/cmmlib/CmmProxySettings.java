package com.zipow.cmmlib;

import android.content.Context;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.proxy.ProxyConfig;
import p021us.zoom.androidlib.proxy.ProxySettings;

public class CmmProxySettings {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final String TAG = "CmmProxySettings";
    @Nullable
    private static Context s_context;

    public static void initialize(@Nullable Context context) {
        s_context = context;
    }

    public static String getProxyConfigsStringForUri(String str) {
        Context context = s_context;
        if (context == null) {
            return "";
        }
        try {
            return ProxySettings.getProxyConfigsStringForUri(context, str);
        } catch (Throwable unused) {
            return "";
        }
    }

    public static boolean isProxyServer(@Nullable String str, int i) {
        if (str == null) {
            return false;
        }
        ProxyConfig[] proxyConfigsForUri = ProxySettings.getProxyConfigsForUri(s_context, "http://aafxbcfyfsghwcwu");
        if (proxyConfigsForUri == null || proxyConfigsForUri.length == 0) {
            return false;
        }
        for (ProxyConfig proxyConfig : proxyConfigsForUri) {
            if (str.equals(proxyConfig.getHost()) && i == proxyConfig.getPort()) {
                return true;
            }
        }
        return false;
    }
}
