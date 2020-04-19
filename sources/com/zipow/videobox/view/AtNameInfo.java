package com.zipow.videobox.view;

public class AtNameInfo {
    private String displayName;
    private int endIndex;
    private String jid;
    private int startIndex;
    private int type;

    public AtNameInfo(String str, int i, int i2, int i3, String str2) {
        this.displayName = str;
        this.startIndex = i;
        this.endIndex = i2;
        this.type = i3;
        this.jid = str2;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public int getStartIndex() {
        return this.startIndex;
    }

    public void setStartIndex(int i) {
        this.startIndex = i;
    }

    public int getEndIndex() {
        return this.endIndex;
    }

    public void setEndIndex(int i) {
        this.endIndex = i;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getJid() {
        return this.jid;
    }

    public void setJid(String str) {
        this.jid = str;
    }
}
