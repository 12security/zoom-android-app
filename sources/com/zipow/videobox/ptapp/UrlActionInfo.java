package com.zipow.videobox.ptapp;

public class UrlActionInfo {
    private boolean isCnMeeting = false;
    private boolean isCurrHostCnMeeting = false;
    private boolean isStart = false;

    public boolean isCnMeeting() {
        return this.isCnMeeting;
    }

    public void setCnMeeting(boolean z) {
        this.isCnMeeting = z;
    }

    public boolean isStart() {
        return this.isStart;
    }

    public void setStart(boolean z) {
        this.isStart = z;
    }

    public boolean isCurrHostCnMeeting() {
        return this.isCurrHostCnMeeting;
    }

    public void setCurrHostCnMeeting(boolean z) {
        this.isCurrHostCnMeeting = z;
    }
}
