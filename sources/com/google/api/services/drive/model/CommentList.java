package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class CommentList extends GenericJson {
    @Key
    private List<Comment> comments;
    @Key
    private String kind;
    @Key
    private String nextPageToken;

    static {
        Data.nullOf(Comment.class);
    }

    public List<Comment> getComments() {
        return this.comments;
    }

    public CommentList setComments(List<Comment> list) {
        this.comments = list;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public CommentList setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public CommentList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public CommentList set(String str, Object obj) {
        return (CommentList) super.set(str, obj);
    }

    public CommentList clone() {
        return (CommentList) super.clone();
    }
}
