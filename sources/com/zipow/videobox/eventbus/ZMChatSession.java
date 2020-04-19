package com.zipow.videobox.eventbus;

public class ZMChatSession {
    public static final int ACTION_CLEAR_HISTORY = 1;
    public static final int ACTION_DELETE_COMMENT = 2;
    public static final int ACTION_UPDATE_REACTION = 3;
    private int action;
    private String messageID;
    private String sessionID;

    public ZMChatSession(String str, int i) {
        this.sessionID = str;
        this.action = i;
    }

    public ZMChatSession(String str, String str2, int i) {
        this.sessionID = str;
        this.messageID = str2;
        this.action = i;
    }

    public int getAction() {
        return this.action;
    }

    public void setAction(int i) {
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
}
