package com.zipow.videobox.ptapp;

public class UpdateAppParam {
    public boolean isForceUpdateByWeb;
    public boolean isJoin;
    public String meetPassword;
    public long meetingNo;
    public String minClientVersion;
    public String webClientLink;

    public UpdateAppParam(long j, boolean z, boolean z2, String str, String str2, String str3) {
        this.meetingNo = j;
        this.isJoin = z;
        this.isForceUpdateByWeb = z2;
        this.webClientLink = str;
        this.minClientVersion = str2;
        this.meetPassword = str3;
    }
}
