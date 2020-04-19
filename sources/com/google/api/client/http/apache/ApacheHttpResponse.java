package com.google.api.client.http.apache;

import com.google.api.client.http.LowLevelHttpResponse;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpRequestBase;

final class ApacheHttpResponse extends LowLevelHttpResponse {
    private final Header[] allHeaders;
    private final HttpRequestBase request;
    private final HttpResponse response;

    ApacheHttpResponse(HttpRequestBase httpRequestBase, HttpResponse httpResponse) {
        this.request = httpRequestBase;
        this.response = httpResponse;
        this.allHeaders = httpResponse.getAllHeaders();
    }

    public int getStatusCode() {
        StatusLine statusLine = this.response.getStatusLine();
        if (statusLine == null) {
            return 0;
        }
        return statusLine.getStatusCode();
    }

    public InputStream getContent() throws IOException {
        HttpEntity entity = this.response.getEntity();
        if (entity == null) {
            return null;
        }
        return entity.getContent();
    }

    public String getContentEncoding() {
        HttpEntity entity = this.response.getEntity();
        if (entity != null) {
            Header contentEncoding = entity.getContentEncoding();
            if (contentEncoding != null) {
                return contentEncoding.getValue();
            }
        }
        return null;
    }

    public long getContentLength() {
        HttpEntity entity = this.response.getEntity();
        if (entity == null) {
            return -1;
        }
        return entity.getContentLength();
    }

    public String getContentType() {
        HttpEntity entity = this.response.getEntity();
        if (entity != null) {
            Header contentType = entity.getContentType();
            if (contentType != null) {
                return contentType.getValue();
            }
        }
        return null;
    }

    public String getReasonPhrase() {
        StatusLine statusLine = this.response.getStatusLine();
        if (statusLine == null) {
            return null;
        }
        return statusLine.getReasonPhrase();
    }

    public String getStatusLine() {
        StatusLine statusLine = this.response.getStatusLine();
        if (statusLine == null) {
            return null;
        }
        return statusLine.toString();
    }

    public String getHeaderValue(String str) {
        return this.response.getLastHeader(str).getValue();
    }

    public int getHeaderCount() {
        return this.allHeaders.length;
    }

    public String getHeaderName(int i) {
        return this.allHeaders[i].getName();
    }

    public String getHeaderValue(int i) {
        return this.allHeaders[i].getValue();
    }

    public void disconnect() {
        this.request.abort();
    }
}
