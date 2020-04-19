package com.zipow.videobox.confapp.meeting;

public class ConfUserInfoEvent {
    private int flag;
    private long userId;

    public long getUserId() {
        return this.userId;
    }

    public int getFlag() {
        return this.flag;
    }

    public ConfUserInfoEvent(long j, int i) {
        this.userId = j;
        this.flag = i;
    }
}
