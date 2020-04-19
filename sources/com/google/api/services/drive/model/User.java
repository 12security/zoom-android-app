package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;

public final class User extends GenericJson {
    @Key
    private String displayName;
    @Key
    private String emailAddress;
    @Key
    private String kind;
    @Key

    /* renamed from: me */
    private Boolean f217me;
    @Key
    private String permissionId;
    @Key
    private String photoLink;

    public String getDisplayName() {
        return this.displayName;
    }

    public User setDisplayName(String str) {
        this.displayName = str;
        return this;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public User setEmailAddress(String str) {
        this.emailAddress = str;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public User setKind(String str) {
        this.kind = str;
        return this;
    }

    public Boolean getMe() {
        return this.f217me;
    }

    public User setMe(Boolean bool) {
        this.f217me = bool;
        return this;
    }

    public String getPermissionId() {
        return this.permissionId;
    }

    public User setPermissionId(String str) {
        this.permissionId = str;
        return this;
    }

    public String getPhotoLink() {
        return this.photoLink;
    }

    public User setPhotoLink(String str) {
        this.photoLink = str;
        return this;
    }

    public User set(String str, Object obj) {
        return (User) super.set(str, obj);
    }

    public User clone() {
        return (User) super.clone();
    }
}
