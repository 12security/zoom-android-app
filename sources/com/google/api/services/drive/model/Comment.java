package com.google.api.services.drive.model;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import java.util.List;

public final class Comment extends GenericJson {
    @Key
    private String anchor;
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
    private String f210id;
    @Key
    private String kind;
    @Key
    private DateTime modifiedTime;
    @Key
    private QuotedFileContent quotedFileContent;
    @Key
    private List<Reply> replies;
    @Key
    private Boolean resolved;

    public static final class QuotedFileContent extends GenericJson {
        @Key
        private String mimeType;
        @Key
        private String value;

        public String getMimeType() {
            return this.mimeType;
        }

        public QuotedFileContent setMimeType(String str) {
            this.mimeType = str;
            return this;
        }

        public String getValue() {
            return this.value;
        }

        public QuotedFileContent setValue(String str) {
            this.value = str;
            return this;
        }

        public QuotedFileContent set(String str, Object obj) {
            return (QuotedFileContent) super.set(str, obj);
        }

        public QuotedFileContent clone() {
            return (QuotedFileContent) super.clone();
        }
    }

    public String getAnchor() {
        return this.anchor;
    }

    public Comment setAnchor(String str) {
        this.anchor = str;
        return this;
    }

    public User getAuthor() {
        return this.author;
    }

    public Comment setAuthor(User user) {
        this.author = user;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Comment setContent(String str) {
        this.content = str;
        return this;
    }

    public DateTime getCreatedTime() {
        return this.createdTime;
    }

    public Comment setCreatedTime(DateTime dateTime) {
        this.createdTime = dateTime;
        return this;
    }

    public Boolean getDeleted() {
        return this.deleted;
    }

    public Comment setDeleted(Boolean bool) {
        this.deleted = bool;
        return this;
    }

    public String getHtmlContent() {
        return this.htmlContent;
    }

    public Comment setHtmlContent(String str) {
        this.htmlContent = str;
        return this;
    }

    public String getId() {
        return this.f210id;
    }

    public Comment setId(String str) {
        this.f210id = str;
        return this;
    }

    public String getKind() {
        return this.kind;
    }

    public Comment setKind(String str) {
        this.kind = str;
        return this;
    }

    public DateTime getModifiedTime() {
        return this.modifiedTime;
    }

    public Comment setModifiedTime(DateTime dateTime) {
        this.modifiedTime = dateTime;
        return this;
    }

    public QuotedFileContent getQuotedFileContent() {
        return this.quotedFileContent;
    }

    public Comment setQuotedFileContent(QuotedFileContent quotedFileContent2) {
        this.quotedFileContent = quotedFileContent2;
        return this;
    }

    public List<Reply> getReplies() {
        return this.replies;
    }

    public Comment setReplies(List<Reply> list) {
        this.replies = list;
        return this;
    }

    public Boolean getResolved() {
        return this.resolved;
    }

    public Comment setResolved(Boolean bool) {
        this.resolved = bool;
        return this;
    }

    public Comment set(String str, Object obj) {
        return (Comment) super.set(str, obj);
    }

    public Comment clone() {
        return (Comment) super.clone();
    }
}
