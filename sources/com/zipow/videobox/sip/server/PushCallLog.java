package com.zipow.videobox.sip.server;

import java.io.Serializable;

public class PushCallLog implements Serializable {
    private String fail;
    private long nRecvPushElapse = 0;
    private String sid;
    private String time;
    private String traceId;
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String str) {
        this.time = str;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String str) {
        this.traceId = str;
    }

    public String getFail() {
        return this.fail;
    }

    public void setFail(String str) {
        this.fail = str;
    }

    public long getnRecvPushElapse() {
        return this.nRecvPushElapse;
    }

    public void setnRecvPushElapse(long j) {
        this.nRecvPushElapse = j;
    }
}
