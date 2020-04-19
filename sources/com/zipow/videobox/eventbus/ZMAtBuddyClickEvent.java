package com.zipow.videobox.eventbus;

import com.zipow.videobox.view.p014mm.MMMessageItem;

public class ZMAtBuddyClickEvent {
    private String jid;
    private MMMessageItem messageItem;

    public ZMAtBuddyClickEvent(String str, MMMessageItem mMMessageItem) {
        this.jid = str;
        this.messageItem = mMMessageItem;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }

    public MMMessageItem getMessageItem() {
        return this.messageItem;
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
        this.messageItem = mMMessageItem;
    }
}
