package com.zipow.videobox.confapp.meeting;

import p021us.zoom.androidlib.util.StringUtil;

public class SelectAlterHostItem {
    private String email;
    private String firstName;
    private String hostID;
    private String lastName;
    private String picUrl;
    private long pmi;

    public String getHostID() {
        return this.hostID;
    }

    public void setHostID(String str) {
        this.hostID = str;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String str) {
        this.email = str;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String str) {
        this.firstName = str;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String str) {
        this.lastName = str;
    }

    public String getPicUrl() {
        return this.picUrl;
    }

    public void setPicUrl(String str) {
        this.picUrl = str;
    }

    public long getPmi() {
        return this.pmi;
    }

    public void setPmi(long j) {
        this.pmi = j;
    }

    public String getScreenName() {
        StringBuilder sb = new StringBuilder();
        sb.append(StringUtil.safeString(this.firstName));
        sb.append(StringUtil.safeString(this.lastName));
        String sb2 = sb.toString();
        return sb2.length() == 0 ? this.email : sb2;
    }
}
