package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class ChangeList extends GenericJson {
    @Key
    private List<Change> changes;
    @Key
    private String kind;
    @Key
    private String newStartPageToken;
    @Key
    private String nextPageToken;

    static {
        Data.nullOf(Change.class);
    }

    public List<Change> getChanges() {
        return this.changes;
    }

    public ChangeList setChanges(List<Change> list) {
        this.changes = list;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public ChangeList setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getNewStartPageToken() {
        return this.newStartPageToken;
    }

    public ChangeList setNewStartPageToken(String str) {
        this.newStartPageToken = str;
        return this;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public ChangeList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public ChangeList set(String str, Object obj) {
        return (ChangeList) super.set(str, obj);
    }

    public ChangeList clone() {
        return (ChangeList) super.clone();
    }
}
