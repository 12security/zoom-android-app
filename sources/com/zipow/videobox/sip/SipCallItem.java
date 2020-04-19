package com.zipow.videobox.sip;

import androidx.annotation.Nullable;

public class SipCallItem {
    @Nullable
    private String callID;
    private int callStatus;
    private long connectTime;
    private boolean isIncomingCall;
    private int lastCallAction;
    private boolean needRing;
    @Nullable
    private String peerDisplayName;
    @Nullable
    private String peerNumber;
    @Nullable
    private String peerURI;

    public void reset() {
        this.callID = null;
        this.peerURI = null;
        this.peerNumber = null;
        this.peerDisplayName = null;
        this.callStatus = -1;
        this.lastCallAction = -1;
        this.isIncomingCall = false;
        this.connectTime = 0;
    }

    public void setIsIncomingCall(boolean z) {
        this.isIncomingCall = z;
    }

    public void updateCallWithItem(@Nullable SipCallItem sipCallItem) {
        if (sipCallItem != null) {
            updateCallWithParas(sipCallItem.callStatus, sipCallItem.callID, sipCallItem.peerURI, sipCallItem.peerNumber, sipCallItem.peerDisplayName, sipCallItem.isIncomingCall);
        }
    }

    public void updateCallWithParas(int i, String str, String str2, String str3, String str4, boolean z) {
        this.callStatus = i;
        this.callID = str;
        this.peerURI = str2;
        this.peerNumber = str3;
        this.peerDisplayName = str4;
        this.isIncomingCall = z;
    }

    @Nullable
    public String getCallID() {
        return this.callID;
    }

    public void setCallID(@Nullable String str) {
        this.callID = str;
    }

    @Nullable
    public String getPeerURI() {
        return this.peerURI;
    }

    public void setPeerURI(@Nullable String str) {
        this.peerURI = str;
    }

    @Nullable
    public String getPeerNumber() {
        return this.peerNumber;
    }

    public void setPeerNumber(@Nullable String str) {
        this.peerNumber = str;
    }

    @Nullable
    public String getPeerDisplayName() {
        return this.peerDisplayName;
    }

    public void setPeerDisplayName(@Nullable String str) {
        this.peerDisplayName = str;
    }

    public int getCallStatus() {
        return this.callStatus;
    }

    public void setCallStatus(int i) {
        this.callStatus = i;
    }

    public int getLastCallAction() {
        return this.lastCallAction;
    }

    public void setLastCallAction(int i) {
        this.lastCallAction = i;
    }

    public boolean isIncomingCall() {
        return this.isIncomingCall;
    }

    public void setIncomingCall(boolean z) {
        this.isIncomingCall = z;
    }

    public boolean isNeedRing() {
        return this.needRing;
    }

    public void setNeedRing(boolean z) {
        this.needRing = z;
    }

    public long getConnectTime() {
        return this.connectTime;
    }

    public void setConnectTime(long j) {
        this.connectTime = j;
    }
}
