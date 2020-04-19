package com.onedrive.sdk.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface IConnection {
    void addRequestHeader(String str, String str2);

    void close();

    Map<String, String> getHeaders();

    InputStream getInputStream() throws IOException;

    OutputStream getOutputStream() throws IOException;

    String getRequestMethod();

    int getResponseCode() throws IOException;

    String getResponseMessage() throws IOException;

    void setContentLength(int i);

    void setFollowRedirects(boolean z);
}
