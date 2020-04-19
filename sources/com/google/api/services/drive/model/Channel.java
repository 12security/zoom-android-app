package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.json.JsonString;
import com.google.api.client.util.Key;
import java.util.Map;

public final class Channel extends GenericJson {
    @Key
    private String address;
    @JsonString
    @Key
    private Long expiration;
    @Key

    /* renamed from: id */
    private String f209id;
    @Key
    private String kind;
    @Key
    private Map<String, String> params;
    @Key
    private Boolean payload;
    @Key
    private String resourceId;
    @Key
    private String resourceUri;
    @Key
    private String token;
    @Key
    private String type;

    public String getAddress() {
        return this.address;
    }

    public Channel setAddress(String str) {
        this.address = str;
        return this;
    }

    public Long getExpiration() {
        return this.expiration;
    }

    public Channel setExpiration(Long l) {
        this.expiration = l;
        return this;
    }

    public String getId() {
        return this.f209id;
    }

    public Channel setId(String str) {
        this.f209id = str;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public Channel setKind(String str) {
        this.kind = str;
        return this;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public Channel setParams(Map<String, String> map) {
        this.params = map;
        return this;
    }

    public Boolean getPayload() {
        return this.payload;
    }

    public Channel setPayload(Boolean bool) {
        this.payload = bool;
        return this;
    }

    public String getResourceId() {
        return this.resourceId;
    }

    public Channel setResourceId(String str) {
        this.resourceId = str;
        return this;
    }

    public String getResourceUri() {
        return this.resourceUri;
    }

    public Channel setResourceUri(String str) {
        this.resourceUri = str;
        return this;
    }

    public String getToken() {
        return this.token;
    }

    public Channel setToken(String str) {
        this.token = str;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Channel setType(String str) {
        this.type = str;
        return this;
    }

    public Channel set(String str, Object obj) {
        return (Channel) super.set(str, obj);
    }

    public Channel clone() {
        return (Channel) super.clone();
    }
}
