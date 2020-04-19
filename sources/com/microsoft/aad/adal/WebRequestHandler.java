package com.microsoft.aad.adal;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.UUID;

public class WebRequestHandler implements IWebRequestHandler {
    public static final String HEADER_ACCEPT = "Accept";
    public static final String HEADER_ACCEPT_JSON = "application/json";
    private static final String TAG = "WebRequestHandler";
    private UUID mRequestCorrelationId = null;

    public HttpWebResponse sendGet(URL url, Map<String, String> map) throws IOException {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("WebRequestHandler thread");
        sb.append(Process.myTid());
        Logger.m236v(str, sb.toString());
        return new HttpWebRequest(url, "GET", updateHeaders(map)).send();
    }

    public HttpWebResponse sendPost(URL url, Map<String, String> map, byte[] bArr, String str) throws IOException {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("WebRequestHandler thread");
        sb.append(Process.myTid());
        Logger.m236v(str2, sb.toString());
        HttpWebRequest httpWebRequest = new HttpWebRequest(url, "POST", updateHeaders(map), bArr, str);
        return httpWebRequest.send();
    }

    private Map<String, String> updateHeaders(Map<String, String> map) {
        UUID uuid = this.mRequestCorrelationId;
        if (uuid != null) {
            map.put(AAD.CLIENT_REQUEST_ID, uuid.toString());
        }
        map.put(AAD.ADAL_ID_PLATFORM, AAD.ADAL_ID_PLATFORM_VALUE);
        map.put(AAD.ADAL_ID_VERSION, AuthenticationContext.getVersionName());
        String str = AAD.ADAL_ID_OS_VER;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(VERSION.SDK_INT);
        map.put(str, sb.toString());
        map.put(AAD.ADAL_ID_DM, Build.MODEL);
        return map;
    }

    public void setRequestCorrelationId(UUID uuid) {
        this.mRequestCorrelationId = uuid;
    }
}
