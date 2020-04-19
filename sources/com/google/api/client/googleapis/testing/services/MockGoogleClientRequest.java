package com.google.api.client.googleapis.testing.services;

import com.google.api.client.googleapis.services.AbstractGoogleClient;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Beta;

@Beta
public class MockGoogleClientRequest<T> extends AbstractGoogleClientRequest<T> {
    public MockGoogleClientRequest(AbstractGoogleClient abstractGoogleClient, String str, String str2, HttpContent httpContent, Class<T> cls) {
        super(abstractGoogleClient, str, str2, httpContent, cls);
    }

    public MockGoogleClientRequest<T> setDisableGZipContent(boolean z) {
        return (MockGoogleClientRequest) super.setDisableGZipContent(z);
    }

    public MockGoogleClientRequest<T> setRequestHeaders(HttpHeaders httpHeaders) {
        return (MockGoogleClientRequest) super.setRequestHeaders(httpHeaders);
    }

    public MockGoogleClientRequest<T> set(String str, Object obj) {
        return (MockGoogleClientRequest) super.set(str, obj);
    }
}
