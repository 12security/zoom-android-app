package com.google.api.client.auth.oauth2;

import java.util.Collection;
import java.util.Collections;

public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl {
    public AuthorizationCodeRequestUrl(String str, String str2) {
        super(str, str2, Collections.singleton("code"));
    }

    public AuthorizationCodeRequestUrl setResponseTypes(Collection<String> collection) {
        return (AuthorizationCodeRequestUrl) super.setResponseTypes(collection);
    }

    public AuthorizationCodeRequestUrl setRedirectUri(String str) {
        return (AuthorizationCodeRequestUrl) super.setRedirectUri(str);
    }

    public AuthorizationCodeRequestUrl setScopes(Collection<String> collection) {
        return (AuthorizationCodeRequestUrl) super.setScopes(collection);
    }

    public AuthorizationCodeRequestUrl setClientId(String str) {
        return (AuthorizationCodeRequestUrl) super.setClientId(str);
    }

    public AuthorizationCodeRequestUrl setState(String str) {
        return (AuthorizationCodeRequestUrl) super.setState(str);
    }

    public AuthorizationCodeRequestUrl set(String str, Object obj) {
        return (AuthorizationCodeRequestUrl) super.set(str, obj);
    }

    public AuthorizationCodeRequestUrl clone() {
        return (AuthorizationCodeRequestUrl) super.clone();
    }
}
