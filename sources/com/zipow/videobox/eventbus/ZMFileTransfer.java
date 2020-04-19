package com.zipow.videobox.eventbus;

public class ZMFileTransfer {
    private String msgID;
    private String sessionID;

    public ZMFileTransfer(String str, String str2) {
        this.msgID = str;
        this.sessionID = str2;
    }

    public String getMsgID() {
        return this.msgID;
    }

    public void setMsgID(String str) {
        this.msgID = str;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }
}
