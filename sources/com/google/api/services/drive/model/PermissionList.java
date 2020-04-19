package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class PermissionList extends GenericJson {
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private List<Permission> permissions;

    static {
        Data.nullOf(Permission.class);
    }

    public String getKind() {
        return this.kind;
    }

    public PermissionList setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public PermissionList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public List<Permission> getPermissions() {
        return this.permissions;
    }

    public PermissionList setPermissions(List<Permission> list) {
        this.permissions = list;
        return this;
    }

    public PermissionList set(String str, Object obj) {
        return (PermissionList) super.set(str, obj);
    }

    public PermissionList clone() {
        return (PermissionList) super.clone();
    }
}
