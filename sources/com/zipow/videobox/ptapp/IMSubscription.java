package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class IMSubscription {
    private String buddySN;
    private String jid;
    private int subscriptionType;

    public IMSubscription(String str, String str2, int i) {
        this.jid = str;
        this.buddySN = str2;
        this.subscriptionType = i;
    }

    public String getJid() {
        return this.jid;
    }

    public String getBuddySN() {
        return this.buddySN;
    }

    public int getSubscriptionType() {
        return this.subscriptionType;
    }

    public boolean equals(@Nullable Object obj) {
        boolean z = false;
        if (!(obj instanceof IMSubscription)) {
            return false;
        }
        IMSubscription iMSubscription = (IMSubscription) obj;
        if ((iMSubscription.jid == null && this.jid != null) || (iMSubscription.buddySN == null && this.buddySN != null)) {
            return false;
        }
        if (StringUtil.isSameString(iMSubscription.jid, this.jid) && StringUtil.isSameString(iMSubscription.buddySN, this.buddySN) && iMSubscription.subscriptionType == this.subscriptionType) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return this.subscriptionType;
    }
}
