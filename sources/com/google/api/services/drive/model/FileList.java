package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class FileList extends GenericJson {
    @Key
    private List<File> files;
    @Key
    private Boolean incompleteSearch;
    @Key
    private String kind;
    @Key
    private String nextPageToken;

    static {
        Data.nullOf(File.class);
    }

    public List<File> getFiles() {
        return this.files;
    }

    public FileList setFiles(List<File> list) {
        this.files = list;
        return this;
    }

    public Boolean getIncompleteSearch() {
        return this.incompleteSearch;
    }

    public FileList setIncompleteSearch(Boolean bool) {
        this.incompleteSearch = bool;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public FileList setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public FileList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public FileList set(String str, Object obj) {
        return (FileList) super.set(str, obj);
    }

    public FileList clone() {
        return (FileList) super.clone();
    }
}
