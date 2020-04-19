package com.zipow.videobox.sip;

import p021us.zoom.androidlib.util.StringUtil;

public class SIPConfiguration {
    public String authName;
    public String domain;
    public int protocol;
    public String proxy;
    public String regServerAddress;
    public int registrationExpiry;
    public int respCode;
    public String respDescription;
    public int status;
    public String userDisplayName;
    public String userName;
    public String userPassword;

    public boolean isSIPCallEnabled() {
        if (!StringUtil.isEmptyOrNull(this.regServerAddress) && !StringUtil.isEmptyOrNull(this.userName) && !StringUtil.isEmptyOrNull(this.userDisplayName)) {
            return true;
        }
        return false;
    }

    public void resetAll() {
        this.regServerAddress = "";
        this.userName = "";
        this.userPassword = "";
        this.userDisplayName = "";
        this.authName = "";
        this.domain = "";
        this.proxy = "";
        this.status = 0;
        this.respCode = 200;
        this.respDescription = "";
        this.protocol = 0;
    }
}
