package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.util.Collection;

public class GoogleBrowserClientRequestUrl extends BrowserClientRequestUrl {
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleBrowserClientRequestUrl(String str, String str2, Collection<String> collection) {
        super(GoogleOAuthConstants.AUTHORIZATION_SERVER_URL, str);
        setRedirectUri(str2);
        setScopes((Collection) collection);
    }

    public GoogleBrowserClientRequestUrl(GoogleClientSecrets googleClientSecrets, String str, Collection<String> collection) {
        this(googleClientSecrets.getDetails().getClientId(), str, collection);
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleBrowserClientRequestUrl setApprovalPrompt(String str) {
        this.approvalPrompt = str;
        return this;
    }

    public GoogleBrowserClientRequestUrl setResponseTypes(Collection<String> collection) {
        return (GoogleBrowserClientRequestUrl) super.setResponseTypes((Collection) collection);
    }

    public GoogleBrowserClientRequestUrl setRedirectUri(String str) {
        return (GoogleBrowserClientRequestUrl) super.setRedirectUri(str);
    }

    public GoogleBrowserClientRequestUrl setScopes(Collection<String> collection) {
        Preconditions.checkArgument(collection.iterator().hasNext());
        return (GoogleBrowserClientRequestUrl) super.setScopes((Collection) collection);
    }

    public GoogleBrowserClientRequestUrl setClientId(String str) {
        return (GoogleBrowserClientRequestUrl) super.setClientId(str);
    }

    public GoogleBrowserClientRequestUrl setState(String str) {
        return (GoogleBrowserClientRequestUrl) super.setState(str);
    }

    public GoogleBrowserClientRequestUrl set(String str, Object obj) {
        return (GoogleBrowserClientRequestUrl) super.set(str, obj);
    }

    public GoogleBrowserClientRequestUrl clone() {
        return (GoogleBrowserClientRequestUrl) super.clone();
    }
}
