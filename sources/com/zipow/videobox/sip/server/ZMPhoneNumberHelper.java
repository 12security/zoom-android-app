package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMPhoneNumberHelper {
    private static final int MIN_LENGTH = 3;
    private static final String TAG = ISIPCallAPI.class.getSimpleName();
    private long mNativeHandle;

    private native String formatCalloutPeerUriVanityNumberImpl(long j, String str);

    @Nullable
    private native String formatPhoneNumberAsE164Impl(long j, String str, String str2, String str3);

    @Nullable
    private native String formatPhoneNumberImpl(long j, String str, String str2, String str3, boolean z);

    private native boolean isE164FormatImpl(long j, String str);

    private native boolean isExtensionImpl(long j, String str);

    private native boolean isNumberMatchedImpl(long j, String str, String str2, boolean z);

    private native boolean isValidPhoneNumberImpl(long j, String str, String str2, String str3);

    public static boolean isInvalidPhoneNumberLength(@Nullable String str) {
        if (str == null || str.length() < 3) {
            return true;
        }
        if (!str.startsWith("+") || str.length() >= 4) {
            return false;
        }
        return true;
    }

    public ZMPhoneNumberHelper(long j) {
        this.mNativeHandle = j;
    }

    public boolean isValidPhoneNumber(String str) {
        return isValidPhoneNumber(str, "", "");
    }

    public boolean isValidPhoneNumbers(@Nullable List<String> list) {
        if (list == null) {
            return false;
        }
        for (String isValidPhoneNumber : list) {
            if (!isValidPhoneNumber(isValidPhoneNumber, "", "")) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidPhoneNumber(String str, String str2, String str3) {
        if (this.mNativeHandle == 0 || isInvalidPhoneNumberLength(str)) {
            return false;
        }
        return isValidPhoneNumberImpl(this.mNativeHandle, StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3));
    }

    public boolean isExtension(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isExtensionImpl(j, StringUtil.safeString(str));
    }

    public boolean isNumberMatched(String str, String str2) {
        return isNumberMatched(str, str2, true);
    }

    public boolean isNumberMatched(String str, String str2, boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isNumberMatchedImpl(j, StringUtil.safeString(str), StringUtil.safeString(str2), z);
    }

    @Nullable
    public String formatPhoneNumberAsE164(@Nullable String str, @Nullable String str2, @Nullable String str3) {
        if (this.mNativeHandle == 0 || str == null) {
            return null;
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str3 == null) {
            str3 = "";
        }
        return formatPhoneNumberAsE164Impl(this.mNativeHandle, StringUtil.safeString(str), StringUtil.safeString(str2), StringUtil.safeString(str3));
    }

    @Nullable
    public String formatPhoneNumber(@Nullable String str, @Nullable String str2, @Nullable String str3, boolean z) {
        if (this.mNativeHandle == 0 || str == null) {
            return null;
        }
        return formatPhoneNumberImpl(this.mNativeHandle, str, str2 == null ? "" : str2, str3 == null ? "" : str3, z);
    }

    @Nullable
    public String formatCalloutPeerUriVanityNumber(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null) {
            return null;
        }
        return formatCalloutPeerUriVanityNumberImpl(j, StringUtil.safeString(str));
    }

    public boolean isE164Format(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isE164FormatImpl(j, StringUtil.safeString(str));
    }
}
