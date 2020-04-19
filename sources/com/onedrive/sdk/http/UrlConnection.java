package com.onedrive.sdk.http;

import com.google.api.client.googleapis.MethodOverride;
import com.onedrive.sdk.options.HeaderOption;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.util.HashMap;
import java.util.Map;

public class UrlConnection implements IConnection {
    private final HttpURLConnection mConnection;
    private HashMap<String, String> mHeaders;

    public UrlConnection(IHttpRequest iHttpRequest) throws IOException {
        this.mConnection = (HttpURLConnection) iHttpRequest.getRequestUrl().openConnection();
        for (HeaderOption headerOption : iHttpRequest.getHeaders()) {
            this.mConnection.addRequestProperty(headerOption.getName(), headerOption.getValue());
        }
        try {
            this.mConnection.setRequestMethod(iHttpRequest.getHttpMethod().toString());
        } catch (ProtocolException unused) {
            this.mConnection.setRequestMethod(HttpMethod.POST.toString());
            this.mConnection.addRequestProperty(MethodOverride.HEADER, iHttpRequest.getHttpMethod().toString());
            this.mConnection.addRequestProperty("X-HTTP-Method", iHttpRequest.getHttpMethod().toString());
        }
    }

    public void setFollowRedirects(boolean z) {
        this.mConnection.setInstanceFollowRedirects(z);
    }

    public void addRequestHeader(String str, String str2) {
        this.mConnection.addRequestProperty(str, str2);
    }

    public OutputStream getOutputStream() throws IOException {
        this.mConnection.setDoOutput(true);
        return this.mConnection.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        if (this.mConnection.getResponseCode() >= 400) {
            return this.mConnection.getErrorStream();
        }
        return this.mConnection.getInputStream();
    }

    public int getResponseCode() throws IOException {
        return this.mConnection.getResponseCode();
    }

    public String getResponseMessage() throws IOException {
        return this.mConnection.getResponseMessage();
    }

    public void close() {
        this.mConnection.disconnect();
    }

    public Map<String, String> getHeaders() {
        if (this.mHeaders == null) {
            this.mHeaders = getResponseHeaders(this.mConnection);
        }
        return this.mHeaders;
    }

    public String getRequestMethod() {
        return this.mConnection.getRequestMethod();
    }

    public void setContentLength(int i) {
        this.mConnection.setFixedLengthStreamingMode(i);
    }

    private static HashMap<String, String> getResponseHeaders(HttpURLConnection httpURLConnection) {
        HashMap<String, String> hashMap = new HashMap<>();
        int i = 0;
        while (true) {
            String headerFieldKey = httpURLConnection.getHeaderFieldKey(i);
            String headerField = httpURLConnection.getHeaderField(i);
            if (headerFieldKey == null && headerField == null) {
                return hashMap;
            }
            hashMap.put(headerFieldKey, headerField);
            i++;
        }
    }
}
