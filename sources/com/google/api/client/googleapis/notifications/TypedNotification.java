package com.google.api.client.googleapis.notifications;

import com.google.api.client.util.Beta;
import com.google.firebase.analytics.FirebaseAnalytics.Param;

@Beta
public class TypedNotification<T> extends AbstractNotification {
    private T content;

    public TypedNotification(long j, String str, String str2, String str3, String str4) {
        super(j, str, str2, str3, str4);
    }

    public TypedNotification(UnparsedNotification unparsedNotification) {
        super(unparsedNotification);
    }

    public final T getContent() {
        return this.content;
    }

    public TypedNotification<T> setContent(T t) {
        this.content = t;
        return this;
    }

    public TypedNotification<T> setMessageNumber(long j) {
        return (TypedNotification) super.setMessageNumber(j);
    }

    public TypedNotification<T> setResourceState(String str) {
        return (TypedNotification) super.setResourceState(str);
    }

    public TypedNotification<T> setResourceId(String str) {
        return (TypedNotification) super.setResourceId(str);
    }

    public TypedNotification<T> setResourceUri(String str) {
        return (TypedNotification) super.setResourceUri(str);
    }

    public TypedNotification<T> setChannelId(String str) {
        return (TypedNotification) super.setChannelId(str);
    }

    public TypedNotification<T> setChannelExpiration(String str) {
        return (TypedNotification) super.setChannelExpiration(str);
    }

    public TypedNotification<T> setChannelToken(String str) {
        return (TypedNotification) super.setChannelToken(str);
    }

    public TypedNotification<T> setChanged(String str) {
        return (TypedNotification) super.setChanged(str);
    }

    public String toString() {
        return super.toStringHelper().add(Param.CONTENT, this.content).toString();
    }
}
