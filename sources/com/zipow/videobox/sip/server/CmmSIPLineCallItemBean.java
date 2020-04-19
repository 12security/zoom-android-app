package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLineCallItem;

public class CmmSIPLineCallItemBean {
    private String anotherMergedLineCallItemID;
    private boolean isItBelongToMe;
    private boolean isMergedLineCallHost;
    private boolean isMergedLineCallMember;
    private String lineCallID;
    private String lineID;
    private String ownerDisplayName;
    private String ownerDisplayNumber;
    private String ownerName;
    private String ownerNumber;
    private String peerDisplayName;
    private String peerDisplayNumber;
    private String peerName;
    private String peerNumber;
    private int previousStatus;
    private String relatedLocalCallID;
    private int status;
    private String userID;

    public CmmSIPLineCallItemBean(CmmSIPLineCallItem cmmSIPLineCallItem) {
        this.lineCallID = cmmSIPLineCallItem.getLineCallID();
        this.lineID = cmmSIPLineCallItem.getLineID();
        this.userID = cmmSIPLineCallItem.getUserID();
        this.peerName = cmmSIPLineCallItem.getPeerName();
        this.peerNumber = cmmSIPLineCallItem.getPeerNumber();
        this.peerDisplayName = cmmSIPLineCallItem.getPeerDisplayName();
        this.peerDisplayNumber = cmmSIPLineCallItem.getPeerDisplayNumber();
        this.ownerName = cmmSIPLineCallItem.getOwnerName();
        this.ownerNumber = cmmSIPLineCallItem.getOwnerNumber();
        this.ownerDisplayName = cmmSIPLineCallItem.getOwnerDisplayName();
        this.ownerDisplayNumber = cmmSIPLineCallItem.getOwnerDisplayNumber();
        this.status = cmmSIPLineCallItem.getStatus();
        this.previousStatus = cmmSIPLineCallItem.getPreviousStatus();
        this.isItBelongToMe = cmmSIPLineCallItem.getIsItBelongToMe();
        this.relatedLocalCallID = cmmSIPLineCallItem.getRelatedLocalCallID();
        this.isMergedLineCallMember = cmmSIPLineCallItem.getIsMergedLineCallMember();
        this.isMergedLineCallHost = cmmSIPLineCallItem.getIsMergedLineCallHost();
        this.anotherMergedLineCallItemID = cmmSIPLineCallItem.getAnotherMergedLineCallItemID();
    }

    @Nullable
    public String getLineCallID() {
        return this.lineCallID;
    }

    @Nullable
    public String getLineID() {
        return this.lineID;
    }

    @Nullable
    public String getUserID() {
        return this.userID;
    }

    @Nullable
    public String getPeerName() {
        return this.peerName;
    }

    @Nullable
    public String getPeerNumber() {
        return this.peerNumber;
    }

    @Nullable
    public String getPeerDisplayName() {
        return this.peerDisplayName;
    }

    @Nullable
    public String getPeerDisplayNumber() {
        return this.peerDisplayNumber;
    }

    @Nullable
    public String getOwnerName() {
        return this.ownerName;
    }

    @Nullable
    public String getOwnerNumber() {
        return this.ownerNumber;
    }

    @Nullable
    public String getOwnerDisplayName() {
        return this.ownerDisplayName;
    }

    @Nullable
    public String getOwnerDisplayNumber() {
        return this.ownerDisplayNumber;
    }

    public int getStatus() {
        return this.status;
    }

    public int getPreviousStatus() {
        return this.previousStatus;
    }

    public boolean isItBelongToMe() {
        return this.isItBelongToMe;
    }

    @Nullable
    public String getRelatedLocalCallID() {
        return this.relatedLocalCallID;
    }

    public boolean isMergedLineCallMember() {
        return this.isMergedLineCallMember;
    }

    public boolean isMergedLineCallHost() {
        return this.isMergedLineCallHost;
    }

    @Nullable
    public String getAnotherMergedLineCallItemID() {
        return this.anotherMergedLineCallItemID;
    }
}
