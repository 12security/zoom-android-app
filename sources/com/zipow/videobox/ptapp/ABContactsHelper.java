package com.zipow.videobox.ptapp;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.CmmTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ABContactsHelper {
    private static final String TAG = "ABContactsHelper";
    private static boolean gABEnabledDone = false;
    private static long gLastMatchTime = 0;
    private static boolean gMatchCalled = false;
    @NonNull
    private static Map<String, Long> mWaitingVerifyNumbers = new HashMap();
    private long mNativeHandle = 0;

    private native int callABContactImpl(long j, int i, List<String> list, String str, String str2);

    private native int getMatchedPhoneNumbersImpl(long j, List<String> list);

    @Nullable
    private native String getVerifiedPhoneNumberImpl(long j);

    private native int inviteABContactsImpl(long j, List<String> list, String str);

    private native int matchPhoneNumbersImpl(long j, List<String> list, boolean z);

    private native boolean needValidatePhoneNumberImpl(long j);

    private native int registerPhoneNumberImpl(long j, String str, String str2, String str3);

    private native int unregisterPhoneNumberImpl(long j, String str, String str2);

    private native boolean updateValidatePhoneNumberImpl(long j, String str);

    private native int verifyPhoneNumberImpl(long j, String str, String str2, String str3);

    public ABContactsHelper(long j) {
        this.mNativeHandle = j;
    }

    public int registerPhoneNumber(@Nullable String str, String str2, @Nullable String str3) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null || str3 == null) {
            return 1;
        }
        int registerPhoneNumberImpl = registerPhoneNumberImpl(j, str, str2, str3);
        if (registerPhoneNumberImpl == 0) {
            addSMSSentSuccess(str, str2);
        }
        return registerPhoneNumberImpl;
    }

    public int unregisterPhoneNumber(@Nullable String str, @Nullable String str2) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null || str2 == null) {
            return 1;
        }
        return unregisterPhoneNumberImpl(j, str, str2);
    }

    public int verifyPhoneNumber(@Nullable String str, @Nullable String str2, @Nullable String str3) {
        long j = this.mNativeHandle;
        if (j == 0 || str == null || str2 == null || str3 == null) {
            return 1;
        }
        return verifyPhoneNumberImpl(j, str, str2, str3);
    }

    public int callABContact(int i, @Nullable List<String> list, @Nullable String str, @Nullable String str2) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null || str == null || str2 == null) {
            return 1;
        }
        return callABContactImpl(j, i, list, str, str2);
    }

    public int matchPhoneNumbers(@Nullable List<String> list, boolean z) {
        if (this.mNativeHandle == 0 || list == null) {
            return 1;
        }
        if (list.size() == 0) {
            return 6;
        }
        if (StringUtil.isEmptyOrNull(getVerifiedPhoneNumber())) {
            return 11;
        }
        setMatchPhoneNumbersCalled(true);
        setLastMatchPhoneNumbersTime(System.currentTimeMillis());
        return 0;
    }

    public static boolean isMatchPhoneNumbersCalled() {
        return gMatchCalled;
    }

    protected static void setMatchPhoneNumbersCalled(boolean z) {
        gMatchCalled = z;
        if (!z) {
            setLastMatchPhoneNumbersTime(0);
        }
    }

    private static void addSMSSentSuccess(@Nullable String str, String str2) {
        String formatSMSPhoneNo = getFormatSMSPhoneNo(str, str2);
        if (formatSMSPhoneNo != null) {
            mWaitingVerifyNumbers.put(formatSMSPhoneNo, Long.valueOf(CmmTime.getMMNow()));
        }
    }

    public static int getRemainSMSTimeInSecond(String str, String str2) {
        Long l = (Long) mWaitingVerifyNumbers.get(getFormatSMSPhoneNo(str, str2));
        if (l == null) {
            return 0;
        }
        long mMNow = 60 - ((CmmTime.getMMNow() - l.longValue()) / 1000);
        if (mMNow < 0) {
            return 0;
        }
        return (int) mMNow;
    }

    private static String getFormatSMSPhoneNo(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        if (str.startsWith("+")) {
            return PhoneNumberUtil.formatNumber(str, str2);
        }
        if (str.startsWith("0")) {
            String substring = str.substring(1);
            StringBuilder sb = new StringBuilder();
            sb.append("+");
            sb.append(str2);
            sb.append(substring);
            return sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("+");
        sb2.append(str2);
        sb2.append(str);
        return sb2.toString();
    }

    public static void setLastMatchPhoneNumbersTime(long j) {
        gLastMatchTime = j;
    }

    public static boolean isTimeToMatchPhoneNumbers() {
        if (isMatchPhoneNumbersCalled() && System.currentTimeMillis() - gLastMatchTime <= 43200000) {
            return false;
        }
        return true;
    }

    public int getMatchedPhoneNumbers(@Nullable List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null) {
            return 1;
        }
        return getMatchedPhoneNumbersImpl(j, list);
    }

    @Nullable
    public String getVerifiedPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getVerifiedPhoneNumberImpl(j);
    }

    public int inviteABContacts(@Nullable List<String> list, @Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0 || list == null || str == null) {
            return 1;
        }
        return inviteABContactsImpl(j, list, str);
    }

    public static void setAddrBookEnabledDone(boolean z) {
        gABEnabledDone = z;
    }

    public static boolean isAddrBookEnabledDone() {
        ABContactsHelper aBContactsHelper = PTApp.getInstance().getABContactsHelper();
        boolean z = false;
        if (aBContactsHelper == null) {
            return false;
        }
        if (gABEnabledDone && !StringUtil.isEmptyOrNull(aBContactsHelper.getVerifiedPhoneNumber())) {
            z = true;
        }
        return z;
    }

    public boolean needValidatePhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return needValidatePhoneNumberImpl(j);
    }

    public boolean updateValidatePhoneNumber(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return updateValidatePhoneNumberImpl(j, str);
    }
}
