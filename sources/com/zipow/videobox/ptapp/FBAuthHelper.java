package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;

public class FBAuthHelper {
    private static final String TAG = "FBAuthHelper";
    private long mNativeHandle = 0;

    @Nullable
    private native String decryptGoogleAuthCodeImpl(long j, String str);

    @Nullable
    private native String generateFBLoginURLImpl(long j, boolean z);

    @Nullable
    private native String generateGoogleLoginURLImpl(long j, boolean z);

    protected FBAuthHelper(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String generateGoogleLoginURL() {
        return generateGoogleLoginURLImpl(this.mNativeHandle, true);
    }

    @Nullable
    public String generateFBLoginURL() {
        return generateFBLoginURLImpl(this.mNativeHandle, true);
    }

    @Nullable
    public String decryptGoogleAuthCode(String str) {
        return decryptGoogleAuthCodeImpl(this.mNativeHandle, str);
    }
}
