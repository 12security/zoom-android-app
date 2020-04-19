package com.zipow.videobox.sip;

import androidx.annotation.NonNull;

public class CmmCallPeerDataBean {
    private boolean anonymous;
    private int countryCode;
    private String displayNumber;
    private int numberType;
    private String peerName;
    private String peerUri;

    public String getPeerUri() {
        return this.peerUri;
    }

    public void setPeerUri(String str) {
        this.peerUri = str;
    }

    public String getDisplayNumber() {
        return this.displayNumber;
    }

    public void setDisplayNumber(String str) {
        this.displayNumber = str;
    }

    public boolean isAnonymous() {
        return this.anonymous;
    }

    public void setAnonymous(boolean z) {
        this.anonymous = z;
    }

    public int getNumberType() {
        return this.numberType;
    }

    public void setNumberType(int i) {
        this.numberType = i;
    }

    public String getPeerName() {
        return this.peerName;
    }

    public void setPeerName(String str) {
        this.peerName = str;
    }

    public int getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(int i) {
        this.countryCode = i;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CmmCallPeerDataBean{peerUri='");
        sb.append(this.peerUri);
        sb.append('\'');
        sb.append(", displayNumber='");
        sb.append(this.displayNumber);
        sb.append('\'');
        sb.append(", countryCode=");
        sb.append(this.countryCode);
        sb.append(", anonymous=");
        sb.append(this.anonymous);
        sb.append(", numberType=");
        sb.append(this.numberType);
        sb.append(", peerName='");
        sb.append(this.peerName);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }
}
