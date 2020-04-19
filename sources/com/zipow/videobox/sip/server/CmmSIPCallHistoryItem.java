package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;

public class CmmSIPCallHistoryItem {
    private long mNativeHandle;

    private native int getCallDurationImpl(long j);

    private native int getCallHistoryResultTypeImpl(long j);

    private native String getCallIDImpl(long j);

    private native int getCallTypeImpl(long j);

    private native long getCreateTimeImpl(long j);

    private native String getFromExtensionIDImpl(long j);

    @Nullable
    private native String getFromPhoneNumberImpl(long j);

    @Nullable
    private native String getFromUserNameImpl(long j);

    @Nullable
    private native String getIDImpl(long j);

    private native String getInterceptExtensionIDImpl(long j);

    private native String getInterceptPhoneNumberImpl(long j);

    private native String getInterceptUserNameImpl(long j);

    private native String getLineIDImpl(long j);

    private native String getOwnerExtensionIDImpl(long j);

    private native int getOwnerLevelImpl(long j);

    private native String getOwnerNameImpl(long j);

    private native String getOwnerPhoneNumberImpl(long j);

    private native long getRecordingAudioFileItemImpl(long j);

    private native String getToExtensionIDImpl(long j);

    @Nullable
    private native String getToPhoneNumberImpl(long j);

    @Nullable
    private native String getToUserNameImpl(long j);

    private native boolean isDeletePendingImpl(long j);

    private native boolean isInboundCallImpl(long j);

    private native boolean isMissedCallImpl(long j);

    private native boolean isRecordingExistImpl(long j);

    private native boolean isRestrictedCallImpl(long j);

    public CmmSIPCallHistoryItem(long j) {
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

    public long getCreateTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCreateTimeImpl(j);
    }

    public int getCallDuration() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallDurationImpl(j);
    }

    public int getCallHistoryResultType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallHistoryResultTypeImpl(j);
    }

    public boolean isMissedCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMissedCallImpl(j);
    }

    public boolean isInboundCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInboundCallImpl(j);
    }

    @Nullable
    public String getFromUserName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFromUserNameImpl(j);
    }

    @Nullable
    public String getFromPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFromPhoneNumberImpl(j);
    }

    @Nullable
    public String getToUserName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getToUserNameImpl(j);
    }

    @Nullable
    public String getToPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getToPhoneNumberImpl(j);
    }

    public boolean isRecordingExist() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isRecordingExistImpl(j);
    }

    @Nullable
    public CmmSIPAudioFileItem getRecordingAudioFileItem() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long recordingAudioFileItemImpl = getRecordingAudioFileItemImpl(j);
        if (recordingAudioFileItemImpl == 0) {
            return null;
        }
        return new CmmSIPAudioFileItem(recordingAudioFileItemImpl);
    }

    @Nullable
    public String getToExtensionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getToExtensionIDImpl(j);
    }

    @Nullable
    public String getFromExtensionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFromExtensionIDImpl(j);
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
    public String getCallID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCallIDImpl(j);
    }

    @Nullable
    public String getInterceptExtensionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getInterceptExtensionIDImpl(j);
    }

    @Nullable
    public String getInterceptPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getInterceptPhoneNumberImpl(j);
    }

    @Nullable
    public String getInterceptUserName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getInterceptUserNameImpl(j);
    }

    @Nullable
    public String getOwnerPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerPhoneNumberImpl(j);
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
    public String getOwnerExtensionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerExtensionIDImpl(j);
    }

    public int getCallType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallTypeImpl(j);
    }

    public boolean isDeletePending() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDeletePendingImpl(j);
    }

    public int getOwnerLevel() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getOwnerLevelImpl(j);
    }

    public boolean isRestrictedCall() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isRestrictedCallImpl(j);
    }
}
