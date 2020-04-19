package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPLine;

public class CmmSIPLineBean {

    /* renamed from: ID */
    private String f329ID;
    private String areaCode;
    private boolean canAnswerIncomingCall;
    private boolean canPickUpCall;
    private boolean canPlaceCall;
    private String countryCode;
    private String countryName;
    private boolean isShared;
    private String ownerName;
    private String ownerNumber;
    private long permission;
    private String userID;

    public CmmSIPLineBean(CmmSIPLine cmmSIPLine) {
        this.f329ID = cmmSIPLine.getID();
        this.userID = cmmSIPLine.getUserID();
        this.ownerName = cmmSIPLine.getOwnerName();
        this.ownerNumber = cmmSIPLine.getOwnerNumber();
        this.countryCode = cmmSIPLine.getCountryCode();
        this.countryName = cmmSIPLine.getCountryName();
        this.areaCode = cmmSIPLine.getAreaCode();
        this.permission = cmmSIPLine.getPermission();
        this.isShared = cmmSIPLine.getIsShared();
        this.canPickUpCall = cmmSIPLine.getCanPickUpCall();
        this.canAnswerIncomingCall = cmmSIPLine.getCanPickUpCall();
        this.canPlaceCall = cmmSIPLine.getCanPlaceCall();
    }

    @Nullable
    public String getID() {
        return this.f329ID;
    }

    @Nullable
    public String getUserID() {
        return this.userID;
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
    public String getCountryCode() {
        return this.countryCode;
    }

    @Nullable
    public String getCountryName() {
        return this.countryName;
    }

    @Nullable
    public String getAreaCode() {
        return this.areaCode;
    }

    public long getPermission() {
        return this.permission;
    }

    public boolean isShared() {
        return this.isShared;
    }

    public boolean canAnswerIncomingCall() {
        return this.canAnswerIncomingCall;
    }

    public boolean canPickUpCall() {
        return this.canPickUpCall;
    }

    public boolean canPlaceCall() {
        return this.canPlaceCall;
    }
}
