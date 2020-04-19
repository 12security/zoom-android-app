package com.dropbox.core.http;

import androidx.core.app.NotificationCompat;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.google.appengine.api.urlfetch.FetchOptions;
import com.google.appengine.api.urlfetch.FetchOptions.Builder;
import com.google.appengine.api.urlfetch.HTTPHeader;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GoogleAppEngineRequestor extends HttpRequestor {
    private final FetchOptions options;
    private final URLFetchService service;

    private static class FetchServiceUploader extends Uploader {
        private final ByteArrayOutputStream body;
        private HTTPRequest request;
        private final URLFetchService service;

        public FetchServiceUploader(URLFetchService uRLFetchService, HTTPRequest hTTPRequest, ByteArrayOutputStream byteArrayOutputStream) {
            this.service = uRLFetchService;
            this.request = hTTPRequest;
            this.body = byteArrayOutputStream;
        }

        public OutputStream getBody() {
            return this.body;
        }

        public void close() {
            if (this.request != null) {
                try {
                    this.body.close();
                } catch (IOException unused) {
                }
                this.request = null;
            }
        }

        public void abort() {
            if (this.request != null) {
                close();
                return;
            }
            throw new IllegalStateException("Uploader already closed.");
        }

        public Response finish() throws IOException {
            HTTPRequest hTTPRequest = this.request;
            if (hTTPRequest != null) {
                hTTPRequest.setPayload(this.body.toByteArray());
                Response access$000 = GoogleAppEngineRequestor.toRequestorResponse(this.service.fetch(this.request));
                close();
                if (this.progressListener != null) {
                    this.progressListener.onProgress((long) this.request.getPayload().length);
                }
                return access$000;
            }
            throw new IllegalStateException("Uploader already closed.");
        }
    }

    public GoogleAppEngineRequestor() {
        this(newDefaultOptions());
    }

    public GoogleAppEngineRequestor(FetchOptions fetchOptions) {
        this(fetchOptions, URLFetchServiceFactory.getURLFetchService());
    }

    public GoogleAppEngineRequestor(FetchOptions fetchOptions, URLFetchService uRLFetchService) {
        if (fetchOptions == null) {
            throw new NullPointerException("options");
        } else if (uRLFetchService != null) {
            this.options = fetchOptions;
            this.service = uRLFetchService;
        } else {
            throw new NullPointerException(NotificationCompat.CATEGORY_SERVICE);
        }
    }

    public FetchOptions getOptions() {
        return this.options;
    }

    public URLFetchService getService() {
        return this.service;
    }

    public Response doGet(String str, Iterable<Header> iterable) throws IOException {
        return toRequestorResponse(this.service.fetch(newRequest(str, HTTPMethod.GET, iterable)));
    }

    public Uploader startPost(String str, Iterable<Header> iterable) throws IOException {
        return new FetchServiceUploader(this.service, newRequest(str, HTTPMethod.POST, iterable), new ByteArrayOutputStream());
    }

    public Uploader startPut(String str, Iterable<Header> iterable) throws IOException {
        return new FetchServiceUploader(this.service, newRequest(str, HTTPMethod.POST, iterable), new ByteArrayOutputStream());
    }

    private HTTPRequest newRequest(String str, HTTPMethod hTTPMethod, Iterable<Header> iterable) throws IOException {
        HTTPRequest hTTPRequest = new HTTPRequest(new URL(str), hTTPMethod, this.options);
        for (Header header : iterable) {
            hTTPRequest.addHeader(new HTTPHeader(header.getKey(), header.getValue()));
        }
        return hTTPRequest;
    }

    public static FetchOptions newDefaultOptions() {
        return Builder.withDefaults().validateCertificate().doNotFollowRedirects().disallowTruncate().setDeadline(Double.valueOf(((double) (DEFAULT_CONNECT_TIMEOUT_MILLIS + DEFAULT_READ_TIMEOUT_MILLIS)) / 1000.0d));
    }

    /* access modifiers changed from: private */
    public static Response toRequestorResponse(HTTPResponse hTTPResponse) {
        HashMap hashMap = new HashMap();
        for (HTTPHeader hTTPHeader : hTTPResponse.getHeadersUncombined()) {
            List list = (List) hashMap.get(hTTPHeader.getName());
            if (list == null) {
                list = new ArrayList();
                hashMap.put(hTTPHeader.getName(), list);
            }
            list.add(hTTPHeader.getValue());
        }
        return new Response(hTTPResponse.getResponseCode(), new ByteArrayInputStream(hTTPResponse.getContent()), hashMap);
    }
}
