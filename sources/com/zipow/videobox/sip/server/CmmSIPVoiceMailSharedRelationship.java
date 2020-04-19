package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;

public class CmmSIPVoiceMailSharedRelationship {
    private long nativeHandle;

    @Nullable
    private native String getExtensionIDImpl(long j);

    private native int getExtensionLevelImpl(long j);

    @Nullable
    private native String getExtensionNameImpl(long j);

    private native boolean isCheckedImpl(long j);

    public CmmSIPVoiceMailSharedRelationship(long j) {
        this.nativeHandle = j;
    }

    @Nullable
    public String getExtensionID() {
        long j = this.nativeHandle;
        if (j == 0) {
            return null;
        }
        return getExtensionIDImpl(j);
    }

    @Nullable
    public String getExtensionName() {
        long j = this.nativeHandle;
        if (j == 0) {
            return null;
        }
        return getExtensionNameImpl(j);
    }

    public int getExtensionLevel() {
        long j = this.nativeHandle;
        if (j == 0) {
            return 0;
        }
        return getExtensionLevelImpl(j);
    }

    public boolean isChecked() {
        long j = this.nativeHandle;
        if (j == 0) {
            return false;
        }
        return isCheckedImpl(j);
    }
}
