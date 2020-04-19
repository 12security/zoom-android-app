package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.util.Key;
import com.google.api.client.util.Preconditions;
import java.util.Collection;

public class GoogleAuthorizationCodeRequestUrl extends AuthorizationCodeRequestUrl {
    @Key("access_type")
    private String accessType;
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleAuthorizationCodeRequestUrl(String str, String str2, Collection<String> collection) {
        this(GoogleOAuthConstants.AUTHORIZATION_SERVER_URL, str, str2, collection);
    }

    public GoogleAuthorizationCodeRequestUrl(String str, String str2, String str3, Collection<String> collection) {
        super(str, str2);
        setRedirectUri(str3);
        setScopes((Collection) collection);
    }

    public GoogleAuthorizationCodeRequestUrl(GoogleClientSecrets googleClientSecrets, String str, Collection<String> collection) {
        this(googleClientSecrets.getDetails().getClientId(), str, collection);
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleAuthorizationCodeRequestUrl setApprovalPrompt(String str) {
        this.approvalPrompt = str;
        return this;
    }

    public final String getAccessType() {
        return this.accessType;
    }

    public GoogleAuthorizationCodeRequestUrl setAccessType(String str) {
        this.accessType = str;
        return this;
    }

    public GoogleAuthorizationCodeRequestUrl setResponseTypes(Collection<String> collection) {
        return (GoogleAuthorizationCodeRequestUrl) super.setResponseTypes(collection);
    }

    public GoogleAuthorizationCodeRequestUrl setRedirectUri(String str) {
        Preconditions.checkNotNull(str);
        return (GoogleAuthorizationCodeRequestUrl) super.setRedirectUri(str);
    }

    public GoogleAuthorizationCodeRequestUrl setScopes(Collection<String> collection) {
        Preconditions.checkArgument(collection.iterator().hasNext());
        return (GoogleAuthorizationCodeRequestUrl) super.setScopes(collection);
    }

    public GoogleAuthorizationCodeRequestUrl setClientId(String str) {
        return (GoogleAuthorizationCodeRequestUrl) super.setClientId(str);
    }

    public GoogleAuthorizationCodeRequestUrl setState(String str) {
        return (GoogleAuthorizationCodeRequestUrl) super.setState(str);
    }

    public GoogleAuthorizationCodeRequestUrl set(String str, Object obj) {
        return (GoogleAuthorizationCodeRequestUrl) super.set(str, obj);
    }

    public GoogleAuthorizationCodeRequestUrl clone() {
        return (GoogleAuthorizationCodeRequestUrl) super.clone();
    }
}
