package com.google.api.client.googleapis.notifications;

import com.google.api.client.util.Beta;
import java.io.InputStream;

@Beta
public class UnparsedNotification extends AbstractNotification {
    private InputStream contentStream;
    private String contentType;

    public UnparsedNotification(long j, String str, String str2, String str3, String str4) {
        super(j, str, str2, str3, str4);
    }

    public final String getContentType() {
        return this.contentType;
    }

    public UnparsedNotification setContentType(String str) {
        this.contentType = str;
        return this;
    }

    public final InputStream getContentStream() {
        return this.contentStream;
    }

    public UnparsedNotification setContentStream(InputStream inputStream) {
        this.contentStream = inputStream;
        return this;
    }

    public UnparsedNotification setMessageNumber(long j) {
        return (UnparsedNotification) super.setMessageNumber(j);
    }

    public UnparsedNotification setResourceState(String str) {
        return (UnparsedNotification) super.setResourceState(str);
    }

    public UnparsedNotification setResourceId(String str) {
        return (UnparsedNotification) super.setResourceId(str);
    }

    public UnparsedNotification setResourceUri(String str) {
        return (UnparsedNotification) super.setResourceUri(str);
    }

    public UnparsedNotification setChannelId(String str) {
        return (UnparsedNotification) super.setChannelId(str);
    }

    public UnparsedNotification setChannelExpiration(String str) {
        return (UnparsedNotification) super.setChannelExpiration(str);
    }

    public UnparsedNotification setChannelToken(String str) {
        return (UnparsedNotification) super.setChannelToken(str);
    }

    public UnparsedNotification setChanged(String str) {
        return (UnparsedNotification) super.setChanged(str);
    }

    public String toString() {
        return super.toStringHelper().add("contentType", this.contentType).toString();
    }
}
