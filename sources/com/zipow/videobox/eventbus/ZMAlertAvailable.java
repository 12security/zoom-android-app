package com.zipow.videobox.eventbus;

public class ZMAlertAvailable {
    private String jid;

    public ZMAlertAvailable(String str) {
        this.jid = str;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }
}
