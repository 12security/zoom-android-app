package com.zipow.videobox.confapp.p009bo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.confapp.bo.BOObject */
public class BOObject {
    private static final String TAG = "BOObject";
    private long mNativeHandle = 0;

    @NonNull
    private native String getBIDImpl(long j);

    @NonNull
    private native String getMeetingNameImpl(long j);

    private native int getMeetingStatusImpl(long j);

    @NonNull
    private native String getMeetingTokenImpl(long j);

    private native long getUserByIndexImpl(long j, int i);

    private native long getUserByUserGUIDImpl(long j, String str);

    private native int getUserCountImpl(long j);

    public BOObject(long j) {
        this.mNativeHandle = j;
    }

    @NonNull
    public String getBID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return "";
        }
        return getBIDImpl(j);
    }

    public int getMeetingStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 5;
        }
        return getMeetingStatusImpl(j);
    }

    @NonNull
    public String getMeetingToken() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return "";
        }
        return getMeetingTokenImpl(j);
    }

    @NonNull
    public String getMeetingName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return "";
        }
        return getMeetingNameImpl(j);
    }

    public int getUserCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getUserCountImpl(j);
    }

    @Nullable
    public BOUser getUserByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long userByIndexImpl = getUserByIndexImpl(j, i);
        if (userByIndexImpl == 0) {
            return null;
        }
        return new BOUser(userByIndexImpl);
    }

    @Nullable
    public BOUser getUserByUserGUID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long userByUserGUIDImpl = getUserByUserGUIDImpl(j, str);
        if (userByUserGUIDImpl == 0) {
            return null;
        }
        return new BOUser(userByUserGUIDImpl);
    }
}
