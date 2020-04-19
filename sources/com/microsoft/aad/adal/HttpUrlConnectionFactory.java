package com.microsoft.aad.adal;

import androidx.annotation.VisibleForTesting;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

final class HttpUrlConnectionFactory {
    private static HttpURLConnection sMockedConnection;
    private static URL sMockedConnectionOpenUrl;

    private HttpUrlConnectionFactory() {
    }

    static void setMockedHttpUrlConnection(HttpURLConnection httpURLConnection) {
        sMockedConnection = httpURLConnection;
        if (httpURLConnection == null) {
            sMockedConnectionOpenUrl = null;
        }
    }

    static HttpURLConnection createHttpUrlConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = sMockedConnection;
        if (httpURLConnection == null) {
            return (HttpURLConnection) url.openConnection();
        }
        sMockedConnectionOpenUrl = url;
        return httpURLConnection;
    }

    @VisibleForTesting
    static URL getMockedConnectionOpenUrl() {
        return sMockedConnectionOpenUrl;
    }
}
