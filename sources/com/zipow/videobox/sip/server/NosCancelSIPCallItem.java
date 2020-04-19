package com.zipow.videobox.sip.server;

public class NosCancelSIPCallItem {
    private String platformInstanceId;
    private String platformType;
    private String reason;
    private String sid;
    private long timestamp;
    private String traceId;

    public String getSid() {
        return this.sid;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public String getReason() {
        return this.reason;
    }

    public void setReason(String str) {
        this.reason = str;
    }

    public String getPlatformType() {
        return this.platformType;
    }

    public void setPlatformType(String str) {
        this.platformType = str;
    }

    public String getPlatformInstanceId() {
        return this.platformInstanceId;
    }

    public void setPlatformInstanceId(String str) {
        this.platformInstanceId = str;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String str) {
        this.traceId = str;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }
}
