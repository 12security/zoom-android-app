package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;

public class CmmSIPLineCallItem {
    private long mNativeHandle;

    private native String getAnotherMergedLineCallItemIDImpl(long j);

    private native int getDurationTimeImpl(long j);

    private native String getLineCallIDImpl(long j);

    private native String getLineIDImpl(long j);

    private native String getOwnerDisplayNameImpl(long j);

    private native String getOwnerDisplayNumberImpl(long j);

    private native String getOwnerNameImpl(long j);

    private native String getOwnerNumberImpl(long j);

    private native String getPeerDisplayNameImpl(long j);

    private native String getPeerDisplayNumberImpl(long j);

    private native String getPeerNameImpl(long j);

    private native String getPeerNumberImpl(long j);

    private native int getPreviousStatusImpl(long j);

    private native String getRelatedLocalCallIDImpl(long j);

    private native int getStatusImpl(long j);

    private native String getUserIDImpl(long j);

    private native boolean isItBelongToMeImpl(long j);

    private native boolean isMergedLineCallHostImpl(long j);

    private native boolean isMergedLineCallMemberImpl(long j);

    public CmmSIPLineCallItem(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getLineCallID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLineCallIDImpl(j);
    }

    @Nullable
    public String getLineID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLineIDImpl(j);
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
    public String getPeerName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerNameImpl(j);
    }

    @Nullable
    public String getPeerNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerNumberImpl(j);
    }

    @Nullable
    public String getPeerDisplayName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerDisplayNameImpl(j);
    }

    @Nullable
    public String getPeerDisplayNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerDisplayNumberImpl(j);
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
    public String getOwnerDisplayName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerDisplayNameImpl(j);
    }

    @Nullable
    public String getOwnerDisplayNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerDisplayNumberImpl(j);
    }

    public int getStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getStatusImpl(j);
    }

    public int getPreviousStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getPreviousStatusImpl(j);
    }

    public int getDurationTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getDurationTimeImpl(j);
    }

    public boolean isItBelongToMe() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isItBelongToMeImpl(j);
    }

    @Nullable
    public String getRelatedLocalCallID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRelatedLocalCallIDImpl(j);
    }

    public boolean isMergedLineCallMember() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMergedLineCallMemberImpl(j);
    }

    public boolean isMergedLineCallHost() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMergedLineCallHostImpl(j);
    }

    @Nullable
    public String getAnotherMergedLineCallItemID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getAnotherMergedLineCallItemIDImpl(j);
    }
}
