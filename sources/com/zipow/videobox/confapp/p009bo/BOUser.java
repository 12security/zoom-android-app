package com.zipow.videobox.confapp.p009bo;

import androidx.annotation.NonNull;

/* renamed from: com.zipow.videobox.confapp.bo.BOUser */
public class BOUser {
    private long mNativeHandle = 0;

    @NonNull
    private native String getUserGUIDImpl(long j);

    private native int getUserStatusImpl(long j);

    private native int getUserTypeImpl(long j);

    public BOUser(long j) {
        this.mNativeHandle = j;
    }

    @NonNull
    public String getUserGUID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return "";
        }
        return getUserGUIDImpl(j);
    }

    public int getUserStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 1;
        }
        return getUserStatusImpl(j);
    }
}
