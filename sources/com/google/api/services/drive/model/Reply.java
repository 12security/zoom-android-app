package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;

public final class Reply extends GenericJson {
    @Key
    private String action;
    @Key
    private User author;
    @Key
    private String content;
    @Key
    private DateTime createdTime;
    @Key
    private Boolean deleted;
    @Key
    private String htmlContent;
    @Key

    /* renamed from: id */
    private String f213id;
    @Key
    private String kind;
    @Key
    private DateTime modifiedTime;

    public String getAction() {
        return this.action;
    }

    public Reply setAction(String str) {
        this.action = str;
        return this;
    }

    public User getAuthor() {
        return this.author;
    }

    public Reply setAuthor(User user) {
        this.author = user;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Reply setContent(String str) {
        this.content = str;
        return this;
    }

    public DateTime getCreatedTime() {
        return this.createdTime;
    }

    public Reply setCreatedTime(DateTime dateTime) {
        this.createdTime = dateTime;
        return this;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Reply setDeleted(Boolean bool) {
        this.deleted = bool;
        return this;
    }

    public String getHtmlContent() {
        return this.htmlContent;
    }

    public Reply setHtmlContent(String str) {
        this.htmlContent = str;
        return this;
    }

    public String getId() {
        return this.f213id;
    }

    public Reply setId(String str) {
        this.f213id = str;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public Reply setKind(String str) {
        this.kind = str;
        return this;
    }

    public DateTime getModifiedTime() {
        return this.modifiedTime;
    }

    public Reply setModifiedTime(DateTime dateTime) {
        this.modifiedTime = dateTime;
        return this;
    }

    public Reply set(String str, Object obj) {
        return (Reply) super.set(str, obj);
    }

    public Reply clone() {
        return (Reply) super.clone();
    }
}
