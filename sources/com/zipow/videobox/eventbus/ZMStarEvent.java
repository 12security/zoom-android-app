package com.zipow.videobox.eventbus;

public class ZMStarEvent {
    public boolean isStar;
    public long msgSvr;
    public String sessionId;

    public ZMStarEvent(String str, long j, boolean z) {
        this.msgSvr = j;
        this.isStar = z;
        this.sessionId = str;
    }
}
