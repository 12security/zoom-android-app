package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class StartPageToken extends GenericJson {
    @Key
    private String kind;
    @Key
    private String startPageToken;

    public String getKind() {
        return this.kind;
    }

    public StartPageToken setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getStartPageToken() {
        return this.startPageToken;
    }

    public StartPageToken setStartPageToken(String str) {
        this.startPageToken = str;
        return this;
    }

    public StartPageToken set(String str, Object obj) {
        return (StartPageToken) super.set(str, obj);
    }

    public StartPageToken clone() {
        return (StartPageToken) super.clone();
    }
}
