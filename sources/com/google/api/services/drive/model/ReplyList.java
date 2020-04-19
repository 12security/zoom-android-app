package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;
import java.util.List;

public final class ReplyList extends GenericJson {
    @Key
    private String kind;
    @Key
    private String nextPageToken;
    @Key
    private List<Reply> replies;

    static {
        Data.nullOf(Reply.class);
    }

    public String getKind() {
        return this.kind;
    }

    public ReplyList setKind(String str) {
        this.kind = str;
        return this;
    }

    public String getNextPageToken() {
        return this.nextPageToken;
    }

    public ReplyList setNextPageToken(String str) {
        this.nextPageToken = str;
        return this;
    }

    public List<Reply> getReplies() {
        return this.replies;
    }

    public ReplyList setReplies(List<Reply> list) {
        this.replies = list;
        return this;
    }

    public ReplyList set(String str, Object obj) {
        return (ReplyList) super.set(str, obj);
    }

    public ReplyList clone() {
        return (ReplyList) super.clone();
    }
}
