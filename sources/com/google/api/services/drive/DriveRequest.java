package com.google.api.services.drive;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Key;

public abstract class DriveRequest<T> extends AbstractGoogleJsonClientRequest<T> {
    @Key
    private String alt;
    @Key
    private String fields;
    @Key
    private String key;
    @Key("oauth_token")
    private String oauthToken;
    @Key
    private Boolean prettyPrint;
    @Key
    private String quotaUser;
    @Key
    private String userIp;

    public DriveRequest(Drive drive, String str, String str2, Object obj, Class<T> cls) {
        super(drive, str, str2, obj, cls);
    }

    public String getAlt() {
        return this.alt;
    }

    public DriveRequest<T> setAlt(String str) {
        this.alt = str;
        return this;
    }

    public String getFields() {
        return this.fields;
    }

    public DriveRequest<T> setFields(String str) {
        this.fields = str;
        return this;
    }

    public String getKey() {
        return this.key;
    }

    public DriveRequest<T> setKey(String str) {
        this.key = str;
        return this;
    }

    public String getOauthToken() {
        return this.oauthToken;
    }

    public DriveRequest<T> setOauthToken(String str) {
        this.oauthToken = str;
        return this;
    }

    public Boolean getPrettyPrint() {
        return this.prettyPrint;
    }

    public DriveRequest<T> setPrettyPrint(Boolean bool) {
        this.prettyPrint = bool;
        return this;
    }

    public String getQuotaUser() {
        return this.quotaUser;
    }

    public DriveRequest<T> setQuotaUser(String str) {
        this.quotaUser = str;
        return this;
    }

    public String getUserIp() {
        return this.userIp;
    }

    public DriveRequest<T> setUserIp(String str) {
        this.userIp = str;
        return this;
    }

    public final Drive getAbstractGoogleClient() {
        return (Drive) super.getAbstractGoogleClient();
    }

    public DriveRequest<T> setDisableGZipContent(boolean z) {
        return (DriveRequest) super.setDisableGZipContent(z);
    }

    public DriveRequest<T> setRequestHeaders(HttpHeaders httpHeaders) {
        return (DriveRequest) super.setRequestHeaders(httpHeaders);
    }

    public DriveRequest<T> set(String str, Object obj) {
        return (DriveRequest) super.set(str, obj);
    }
}
