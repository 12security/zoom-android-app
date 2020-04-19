package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRegData;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRegResult;

public class CmmSIPLine {
    private long mNativeHandle;

    private native boolean canAnswerIncomingCallImpl(long j);

    private native boolean canPickUpCallImpl(long j);

    private native boolean canPlaceCallImpl(long j);

    private native String getAreaCodeImpl(long j);

    private native String getCountryCodeImpl(long j);

    private native String getCountryNameImpl(long j);

    private native String getIDImpl(long j);

    private native String getOwnerNameImpl(long j);

    private native String getOwnerNumberImpl(long j);

    private native long getPermissionImpl(long j);

    private native byte[] getRegisterDataImpl(long j);

    private native byte[] getRegisterResultImpl(long j);

    private native String getUserIDImpl(long j);

    private native boolean isSharedImpl(long j);

    public CmmSIPLine(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIDImpl(j);
    }

    @Nullable
    public String getUserID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getUserIDImpl(j);
    }

    @Nullable
    public String getOwnerName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerNameImpl(j);
    }

    @Nullable
    public String getOwnerNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerNumberImpl(j);
    }

    @Nullable
    public String getCountryCode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCountryCodeImpl(j);
    }

    @Nullable
    public String getCountryName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCountryNameImpl(j);
    }

    @Nullable
    public String getAreaCode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAreaCodeImpl(j);
    }

    public long getPermission() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getPermissionImpl(j);
    }

    public boolean isShared() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSharedImpl(j);
    }

    public boolean canAnswerIncomingCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canAnswerIncomingCallImpl(j);
    }

    public boolean canPickUpCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canPickUpCallImpl(j);
    }

    public boolean canPlaceCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canPlaceCallImpl(j);
    }

    @Nullable
    public CmmSIPCallRegData getRegisterData() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] registerDataImpl = getRegisterDataImpl(j);
        if (registerDataImpl == null || registerDataImpl.length <= 0) {
            return null;
        }
        try {
            return CmmSIPCallRegData.parseFrom(registerDataImpl);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public CmmSIPCallRegResult getRegisterResult() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] registerResultImpl = getRegisterResultImpl(j);
        if (registerResultImpl == null || registerResultImpl.length <= 0) {
            return null;
        }
        try {
            return CmmSIPCallRegResult.parseFrom(registerResultImpl);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isLineRegistered() {
        CmmSIPCallRegResult registerResult = getRegisterResult();
        return registerResult != null && registerResult.getRegStatus() == 6;
    }
}
