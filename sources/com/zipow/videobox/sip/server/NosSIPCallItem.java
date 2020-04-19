package com.zipow.videobox.sip.server;

import java.io.Serializable;

public class NosSIPCallItem implements Serializable {
    private String calledNumber = "";
    private String domainName = "";
    private String extensionId = "";
    private String from = "";
    private String fromName = "";
    private boolean isDuplicateChecked = false;
    private String serverId = "";
    private String sid = "";
    private String siplb = "";
    private String thirdname = "";
    private String thirdnumber = "";
    private int thirdtype = 0;
    private long timestamp = 0;

    /* renamed from: to */
    private String f332to = "";
    private String traceId = "";

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public String getExtensionId() {
        return this.extensionId;
    }

    public void setExtensionId(String str) {
        this.extensionId = str;
    }

    public String getServerId() {
        return this.serverId;
    }

    public void setServerId(String str) {
        this.serverId = str;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String str) {
        this.from = str;
    }

    public String getFromName() {
        return this.fromName;
    }

    public void setFromName(String str) {
        this.fromName = str;
    }

    public String getTo() {
        return this.f332to;
    }

    public void setTo(String str) {
        this.f332to = str;
    }

    public String getSid() {
        return this.sid;
    }

    public void setSid(String str) {
        this.sid = str;
    }

    public String getDomainName() {
        return this.domainName;
    }

    public void setDomainName(String str) {
        this.domainName = str;
    }

    public String getSiplb() {
        return this.siplb;
    }

    public void setSiplb(String str) {
        this.siplb = str;
    }

    public boolean isDuplicateChecked() {
        return this.isDuplicateChecked;
    }

    public void setDuplicateChecked(boolean z) {
        this.isDuplicateChecked = z;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public void setTraceId(String str) {
        this.traceId = str;
    }

    public String getCalledNumber() {
        return this.calledNumber;
    }

    public void setCalledNumber(String str) {
        this.calledNumber = str;
    }

    public int getThirdtype() {
        return this.thirdtype;
    }

    public void setThirdtype(int i) {
        this.thirdtype = i;
    }

    public String getThirdname() {
        return this.thirdname;
    }

    public void setThirdname(String str) {
        this.thirdname = str;
    }

    public String getThirdnumber() {
        return this.thirdnumber;
    }

    public void setThirdnumber(String str) {
        this.thirdnumber = str;
    }

    public static int parseThirdType(String str) {
        if (str == null) {
            return 0;
        }
        if ("executive_assistance".equals(str)) {
            return 1;
        }
        if ("call_queue".equals(str)) {
            return 2;
        }
        if ("auto_receptionist".equals(str)) {
            return 3;
        }
        if ("blind_transfer".equals(str)) {
            return 4;
        }
        if ("share_line_group".equals(str)) {
            return 5;
        }
        if (!"default".equals(str) && "device_forward".equals(str)) {
            return 6;
        }
        return 0;
    }

    public boolean isCallQueue() {
        return getThirdtype() == 2;
    }
}
