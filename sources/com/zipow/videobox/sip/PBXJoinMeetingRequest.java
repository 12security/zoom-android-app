package com.zipow.videobox.sip;

import java.io.Serializable;

public class PBXJoinMeetingRequest implements Serializable {
    private static final long serialVersionUID = 1;
    private String callId;
    private long meetingNum;
    private String pwd;

    public PBXJoinMeetingRequest(String str, long j, String str2) {
        this.callId = str;
        this.meetingNum = j;
        this.pwd = str2;
    }

    public String getCallId() {
        return this.callId;
    }

    public void setCallId(String str) {
        this.callId = str;
    }

    public long getMeetingNum() {
        return this.meetingNum;
    }

    public void setMeetingNum(long j) {
        this.meetingNum = j;
    }

    public String getPwd() {
        return this.pwd;
    }

    public void setPwd(String str) {
        this.pwd = str;
    }
}
