package com.microsoft.aad.adal;

import android.content.Context;
import android.os.Debug;
import android.os.Process;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.http.HttpHost;

class HttpWebRequest {
    private static final int CONNECT_TIME_OUT = AuthenticationSettings.INSTANCE.getConnectTimeOut();
    private static final int DEBUG_SIMULATE_DELAY = 0;
    private static final int READ_TIME_OUT = AuthenticationSettings.INSTANCE.getReadTimeOut();
    static final String REQUEST_METHOD_GET = "GET";
    static final String REQUEST_METHOD_POST = "POST";
    private static final String TAG = "HttpWebRequest";
    private final byte[] mRequestContent;
    private final String mRequestContentType;
    private final Map<String, String> mRequestHeaders;
    private final String mRequestMethod;
    private final URL mUrl;

    HttpWebRequest(URL url, String str, Map<String, String> map) {
        this(url, str, map, null, null);
    }

    HttpWebRequest(URL url, String str, Map<String, String> map, byte[] bArr, String str2) {
        this.mUrl = url;
        this.mRequestMethod = str;
        this.mRequestHeaders = new HashMap();
        URL url2 = this.mUrl;
        if (url2 != null) {
            this.mRequestHeaders.put("Host", url2.getAuthority());
        }
        this.mRequestHeaders.putAll(map);
        this.mRequestContent = bArr;
        this.mRequestContentType = str2;
    }

    private HttpURLConnection setupConnection() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Thread:");
        sb.append(Process.myTid());
        Logger.m237v("HttpWebRequest:setupConnection", "HttpWebRequest setupConnection.", sb.toString(), null);
        URL url = this.mUrl;
        if (url == null) {
            throw new IllegalArgumentException("requestURL");
        } else if (url.getProtocol().equalsIgnoreCase(HttpHost.DEFAULT_SCHEME_NAME) || this.mUrl.getProtocol().equalsIgnoreCase("https")) {
            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection createHttpUrlConnection = HttpUrlConnectionFactory.createHttpUrlConnection(this.mUrl);
            createHttpUrlConnection.setConnectTimeout(CONNECT_TIME_OUT);
            createHttpUrlConnection.setRequestProperty("Connection", "close");
            for (Entry entry : this.mRequestHeaders.entrySet()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Header: ");
                sb2.append((String) entry.getKey());
                Logger.m237v("HttpWebRequest:setupConnection", "Setting header. ", sb2.toString(), null);
                createHttpUrlConnection.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
            }
            createHttpUrlConnection.setReadTimeout(READ_TIME_OUT);
            createHttpUrlConnection.setInstanceFollowRedirects(true);
            createHttpUrlConnection.setUseCaches(false);
            createHttpUrlConnection.setRequestMethod(this.mRequestMethod);
            createHttpUrlConnection.setDoInput(true);
            setRequestBody(createHttpUrlConnection, this.mRequestContent, this.mRequestContentType);
            return createHttpUrlConnection;
        } else {
            throw new IllegalArgumentException("requestURL");
        }
    }

    public HttpWebResponse send() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(" Thread: ");
        sb.append(Process.myTid());
        InputStream inputStream = null;
        Logger.m237v("HttpWebRequest:send", "HttpWebRequest send. ", sb.toString(), null);
        HttpURLConnection httpURLConnection = setupConnection();
        try {
            inputStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            Logger.m231e("HttpWebRequest:send", "IOException is thrown when sending the request. ", e.getMessage(), ADALError.SERVER_ERROR);
            inputStream = httpURLConnection.getErrorStream();
            if (inputStream == null) {
                throw e;
            }
        } catch (Throwable th) {
            safeCloseStream(inputStream);
            throw th;
        }
        int responseCode = httpURLConnection.getResponseCode();
        String convertStreamToString = convertStreamToString(inputStream);
        Debug.isDebuggerConnected();
        Logger.m236v("HttpWebRequest:send", "Response is received.");
        HttpWebResponse httpWebResponse = new HttpWebResponse(responseCode, convertStreamToString, httpURLConnection.getHeaderFields());
        safeCloseStream(inputStream);
        return httpWebResponse;
    }

    static void throwIfNetworkNotAvailable(Context context) throws AuthenticationException {
        DefaultConnectionService defaultConnectionService = new DefaultConnectionService(context);
        if (defaultConnectionService.isConnectionAvailable()) {
            return;
        }
        if (defaultConnectionService.isNetworkDisabledFromOptimizations()) {
            ADALError aDALError = ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION;
            StringBuilder sb = new StringBuilder();
            sb.append("Connection is not available to refresh token because power optimization is enabled. And the device is in doze mode or the app is standby");
            sb.append(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription());
            AuthenticationException authenticationException = new AuthenticationException(aDALError, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Connection is not available to refresh token because power optimization is enabled. And the device is in doze mode or the app is standby");
            sb2.append(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription());
            Logger.m239w(TAG, sb2.toString(), "", ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION);
            throw authenticationException;
        }
        AuthenticationException authenticationException2 = new AuthenticationException(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE, "Connection is not available to refresh token");
        Logger.m239w(TAG, "Connection is not available to refresh token", "", ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE);
        throw authenticationException2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0033  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.String convertStreamToString(java.io.InputStream r3) throws java.io.IOException {
        /*
            r0 = 0
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ all -> 0x002f }
            java.io.InputStreamReader r2 = new java.io.InputStreamReader     // Catch:{ all -> 0x002f }
            r2.<init>(r3)     // Catch:{ all -> 0x002f }
            r1.<init>(r2)     // Catch:{ all -> 0x002f }
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch:{ all -> 0x002d }
            r3.<init>()     // Catch:{ all -> 0x002d }
        L_0x0010:
            java.lang.String r0 = r1.readLine()     // Catch:{ all -> 0x002d }
            if (r0 == 0) goto L_0x0025
            int r2 = r3.length()     // Catch:{ all -> 0x002d }
            if (r2 <= 0) goto L_0x0021
            r2 = 10
            r3.append(r2)     // Catch:{ all -> 0x002d }
        L_0x0021:
            r3.append(r0)     // Catch:{ all -> 0x002d }
            goto L_0x0010
        L_0x0025:
            java.lang.String r3 = r3.toString()     // Catch:{ all -> 0x002d }
            r1.close()
            return r3
        L_0x002d:
            r3 = move-exception
            goto L_0x0031
        L_0x002f:
            r3 = move-exception
            r1 = r0
        L_0x0031:
            if (r1 == 0) goto L_0x0036
            r1.close()
        L_0x0036:
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.microsoft.aad.adal.HttpWebRequest.convertStreamToString(java.io.InputStream):java.lang.String");
    }

    private static void setRequestBody(HttpURLConnection httpURLConnection, byte[] bArr, String str) throws IOException {
        if (bArr != null) {
            httpURLConnection.setDoOutput(true);
            if (str != null && !str.isEmpty()) {
                httpURLConnection.setRequestProperty("Content-Type", str);
            }
            httpURLConnection.setRequestProperty("Content-Length", Integer.toString(bArr.length));
            httpURLConnection.setFixedLengthStreamingMode(bArr.length);
            OutputStream outputStream = null;
            try {
                outputStream = httpURLConnection.getOutputStream();
                outputStream.write(bArr);
            } finally {
                safeCloseStream(outputStream);
            }
        }
    }

    private static void safeCloseStream(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                Logger.m232e(TAG, "Failed to close the stream. ", "", ADALError.IO_EXCEPTION, e);
            }
        }
    }
}
