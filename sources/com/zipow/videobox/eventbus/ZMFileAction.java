package com.zipow.videobox.eventbus;

public class ZMFileAction {
    public static final int ACTION_CANCEL = 2;
    public static final int ACTION_DOWNLOAED = 3;
    public static final int ACTION_PAUSE = 1;
    private int action;
    private String messageID;
    private String sessionID;

    public ZMFileAction(String str, String str2, int i) {
        this.sessionID = str;
        this.messageID = str2;
        this.action = i;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public String getMessageID() {
        return this.messageID;
    }

    public void setMessageID(String str) {
        this.messageID = str;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int i) {
        this.action = i;
    }
}
