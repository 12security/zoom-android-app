package com.google.api.client.auth.oauth2;

import java.util.Collection;
import java.util.Collections;

public class BrowserClientRequestUrl extends AuthorizationRequestUrl {
    public BrowserClientRequestUrl(String str, String str2) {
        super(str, str2, Collections.singleton("token"));
    }

    public BrowserClientRequestUrl setResponseTypes(Collection<String> collection) {
        return (BrowserClientRequestUrl) super.setResponseTypes(collection);
    }

    public BrowserClientRequestUrl setRedirectUri(String str) {
        return (BrowserClientRequestUrl) super.setRedirectUri(str);
    }

    public BrowserClientRequestUrl setScopes(Collection<String> collection) {
        return (BrowserClientRequestUrl) super.setScopes(collection);
    }

    public BrowserClientRequestUrl setClientId(String str) {
        return (BrowserClientRequestUrl) super.setClientId(str);
    }

    public BrowserClientRequestUrl setState(String str) {
        return (BrowserClientRequestUrl) super.setState(str);
    }

    public BrowserClientRequestUrl set(String str, Object obj) {
        return (BrowserClientRequestUrl) super.set(str, obj);
    }

    public BrowserClientRequestUrl clone() {
        return (BrowserClientRequestUrl) super.clone();
    }
}
