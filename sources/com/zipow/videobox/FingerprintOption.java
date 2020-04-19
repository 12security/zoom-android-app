package com.zipow.videobox;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.HashMap;
import java.util.HashSet;
import p021us.zoom.androidlib.util.StringUtil;

public class FingerprintOption implements Parcelable {
    public static final Creator<FingerprintOption> CREATOR = new Creator<FingerprintOption>() {
        public FingerprintOption createFromParcel(@NonNull Parcel parcel) {
            return new FingerprintOption(parcel);
        }

        public FingerprintOption[] newArray(int i) {
            return new FingerprintOption[i];
        }
    };
    private static final String KEY = "FingerprintOption";
    private boolean mEnableFingerprint;
    @Nullable
    private String mUser;
    @Nullable
    private String mVar2;

    public int describeContents() {
        return 0;
    }

    public boolean ismEnableFingerprint() {
        return this.mEnableFingerprint;
    }

    @Nullable
    public String getmUser() {
        return this.mUser;
    }

    @Nullable
    public String getmVar2() {
        return this.mVar2;
    }

    public void setmEnableFingerprint(boolean z) {
        this.mEnableFingerprint = z;
    }

    public void setmUser(@Nullable String str) {
        this.mUser = str;
    }

    public void setmVar2(@Nullable String str) {
        this.mVar2 = str;
    }

    public boolean isEnableFingerprintWithUserInfo() {
        return this.mEnableFingerprint && !StringUtil.isEmptyOrNull(this.mUser) && !StringUtil.isEmptyOrNull(this.mVar2);
    }

    public boolean isDisableFingerprintWithUserInfo() {
        return !this.mEnableFingerprint && !StringUtil.isEmptyOrNull(this.mUser) && !StringUtil.isEmptyOrNull(this.mVar2);
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeByte(this.mEnableFingerprint ? (byte) 1 : 0);
        parcel.writeString(this.mUser);
        parcel.writeString(this.mVar2);
    }

    public FingerprintOption() {
    }

    public void savePreference() {
        HashMap hashMap = new HashMap();
        hashMap.put("FingerprintOptionmUser", this.mUser);
        hashMap.put("FingerprintOptionmVar2", this.mVar2);
        hashMap.put("FingerprintOptionmEnableFingerprint", String.valueOf(this.mEnableFingerprint));
        PreferenceUtil.saveMapStringValues(hashMap);
    }

    public static FingerprintOption readFromPreference() {
        if (PreferenceUtil.containsKey("FingerprintOptionmUserName")) {
            HashSet hashSet = new HashSet();
            String str = "FingerprintOptionmUserName";
            String str2 = "FingerprintOptionmPassword";
            hashSet.add(str);
            hashSet.add(str2);
            HashMap readMapStringValues = PreferenceUtil.readMapStringValues(hashSet, null);
            if (readMapStringValues != null && readMapStringValues.size() > 0) {
                readMapStringValues.put("FingerprintOptionmUser", readMapStringValues.get(str));
                readMapStringValues.put("FingerprintOptionmVar2", readMapStringValues.get(str2));
                readMapStringValues.remove(str);
                readMapStringValues.remove(str2);
                PreferenceUtil.saveMapStringValues(readMapStringValues);
            }
            PreferenceUtil.clearKeys("FingerprintOptionmUserName", "FingerprintOptionmPassword");
        }
        HashSet hashSet2 = new HashSet();
        hashSet2.add("FingerprintOptionmUser");
        hashSet2.add("FingerprintOptionmVar2");
        hashSet2.add("FingerprintOptionmEnableFingerprint");
        HashMap readMapStringValues2 = PreferenceUtil.readMapStringValues(hashSet2, null);
        if (readMapStringValues2 == null) {
            return null;
        }
        FingerprintOption fingerprintOption = new FingerprintOption();
        String str3 = (String) readMapStringValues2.get("FingerprintOptionmEnableFingerprint");
        fingerprintOption.mEnableFingerprint = StringUtil.isEmptyOrNull(str3) ? false : Boolean.parseBoolean(str3);
        fingerprintOption.mUser = (String) readMapStringValues2.get("FingerprintOptionmUser");
        fingerprintOption.mVar2 = (String) readMapStringValues2.get("FingerprintOptionmVar2");
        return fingerprintOption;
    }

    protected FingerprintOption(Parcel parcel) {
        this.mEnableFingerprint = parcel.readByte() != 0;
        this.mUser = parcel.readString();
        this.mVar2 = parcel.readString();
    }
}
