package com.box.androidsdk.content.models;

import java.util.ArrayList;

public class BoxSharedLinkSession extends BoxSession {
    protected ArrayList<OnSharedLinkResponseListener> mListeners = new ArrayList<>();
    String mPassword;
    String mSharedLink;

    public interface OnSharedLinkResponseListener {
        void onResponse(String str, String str2, Exception exc);
    }

    public BoxSharedLinkSession(String str, BoxSession boxSession) {
        super(boxSession);
        this.mSharedLink = str;
    }

    public String getSharedLink() {
        return this.mSharedLink;
    }

    public BoxSharedLinkSession setSharedLink(String str) {
        this.mSharedLink = str;
        return this;
    }

    public String getPassword() {
        return this.mPassword;
    }

    public BoxSharedLinkSession setPassword(String str) {
        this.mPassword = str;
        return this;
    }

    public synchronized BoxSharedLinkSession addOnSharedLinkResponseListener(OnSharedLinkResponseListener onSharedLinkResponseListener) {
        this.mListeners.add(onSharedLinkResponseListener);
        return this;
    }
}
