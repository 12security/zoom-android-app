package com.google.api.client.testing.http.javanet;

import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Beta
public class MockHttpURLConnection extends HttpURLConnection {
    @Deprecated
    public static final byte[] ERROR_BUF = new byte[5];
    @Deprecated
    public static final byte[] INPUT_BUF = new byte[1];
    private boolean doOutputCalled;
    private InputStream errorStream = null;
    private Map<String, List<String>> headers = new LinkedHashMap();
    private InputStream inputStream = null;
    private OutputStream outputStream = new ByteArrayOutputStream(0);

    public void connect() throws IOException {
    }

    public void disconnect() {
    }

    public boolean usingProxy() {
        return false;
    }

    public MockHttpURLConnection(URL url) {
        super(url);
    }

    public int getResponseCode() throws IOException {
        return this.responseCode;
    }

    public void setDoOutput(boolean z) {
        this.doOutputCalled = true;
    }

    public OutputStream getOutputStream() throws IOException {
        OutputStream outputStream2 = this.outputStream;
        if (outputStream2 != null) {
            return outputStream2;
        }
        return super.getOutputStream();
    }

    public final boolean doOutputCalled() {
        return this.doOutputCalled;
    }

    public MockHttpURLConnection setOutputStream(OutputStream outputStream2) {
        this.outputStream = outputStream2;
        return this;
    }

    public MockHttpURLConnection setResponseCode(int i) {
        Preconditions.checkArgument(i >= -1);
        this.responseCode = i;
        return this;
    }

    public MockHttpURLConnection addHeader(String str, String str2) {
        Preconditions.checkNotNull(str);
        Preconditions.checkNotNull(str2);
        if (this.headers.containsKey(str)) {
            ((List) this.headers.get(str)).add(str2);
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str2);
            this.headers.put(str, arrayList);
        }
        return this;
    }

    public MockHttpURLConnection setInputStream(InputStream inputStream2) {
        Preconditions.checkNotNull(inputStream2);
        if (this.inputStream == null) {
            this.inputStream = inputStream2;
        }
        return this;
    }

    public MockHttpURLConnection setErrorStream(InputStream inputStream2) {
        Preconditions.checkNotNull(inputStream2);
        if (this.errorStream == null) {
            this.errorStream = inputStream2;
        }
        return this;
    }

    public InputStream getInputStream() throws IOException {
        if (this.responseCode < 400) {
            return this.inputStream;
        }
        throw new IOException();
    }

    public InputStream getErrorStream() {
        return this.errorStream;
    }

    public Map<String, List<String>> getHeaderFields() {
        return this.headers;
    }

    public String getHeaderField(String str) {
        List list = (List) this.headers.get(str);
        if (list == null) {
            return null;
        }
        return (String) list.get(0);
    }
}
