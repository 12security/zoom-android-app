package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.CmmCallPeerDataBean;

public class CmmSIPCallItemLocal extends CmmSIPCallItem {
    private String callID;
    private CmmCallPeerDataBean callPeerData;

    public int getCallElapsedTime() {
        return 0;
    }

    public int getCallGenerate() {
        return 1;
    }

    public long getCallGenerateTime() {
        return 0;
    }

    public int getCallGenerateType() {
        return 1;
    }

    public int getCallRecordingStatus() {
        return 0;
    }

    public long getCallStartTime() {
        return 0;
    }

    public int getCallStatus() {
        return 20;
    }

    public int getCountryCode() {
        return Integer.MIN_VALUE;
    }

    public int getLastActionReason() {
        return 10;
    }

    public int getLastActionType() {
        return 8;
    }

    @Nullable
    public String getRelatedCallID() {
        return null;
    }

    public boolean isExecutingAction() {
        return true;
    }

    public boolean isIncomingCall() {
        return false;
    }

    public boolean isNeedRing() {
        return true;
    }

    public CmmSIPCallItemLocal(@NonNull CmmCallPeerDataBean cmmCallPeerDataBean) {
        super(0);
        this.callID = CmmSIPCallItem.generateLocalId(cmmCallPeerDataBean.getPeerUri());
        this.callPeerData = cmmCallPeerDataBean;
    }

    public String getCallID() {
        return this.callID;
    }

    public String getPeerURI() {
        return this.callPeerData.getPeerUri();
    }

    public CmmCallPeerDataBean getCallPeerData() {
        return this.callPeerData;
    }

    public String getPeerNumber() {
        return getPeerURI();
    }

    public String getPeerFormatNumber() {
        return getPeerURI();
    }

    public String getPeerDisplayName() {
        return this.callPeerData.getPeerName();
    }

    public void setCallID(String str) {
        this.callID = str;
    }

    public void setPeerURI(String str) {
        this.callPeerData.setPeerUri(str);
    }

    public void setPeerDisplayName(String str) {
        this.callPeerData.setPeerName(str);
    }
}
