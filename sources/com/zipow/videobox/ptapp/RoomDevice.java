package com.zipow.videobox.ptapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class RoomDevice implements Parcelable {
    public static final Creator<RoomDevice> CREATOR = new Creator<RoomDevice>() {
        public RoomDevice createFromParcel(@NonNull Parcel parcel) {
            return new RoomDevice(parcel);
        }

        public RoomDevice[] newArray(int i) {
            return new RoomDevice[i];
        }
    };
    private int mDeviceType = 1;
    @Nullable
    private String mE164num;
    private int mEncrypt = 2;
    @Nullable
    private String mIp;
    @Nullable
    private String mName;
    private String title;

    public int describeContents() {
        return 0;
    }

    public RoomDevice() {
    }

    public RoomDevice(@Nullable String str, @Nullable String str2, @Nullable String str3, int i, int i2) {
        this.mName = str;
        this.mIp = str2;
        this.mE164num = str3;
        this.mDeviceType = i;
        this.mEncrypt = i2;
    }

    public RoomDevice(String str) {
        this.title = str;
    }

    protected RoomDevice(Parcel parcel) {
        this.mName = parcel.readString();
        this.mIp = parcel.readString();
        this.mE164num = parcel.readString();
        this.mDeviceType = parcel.readInt();
        this.mEncrypt = parcel.readInt();
    }

    @Nullable
    public String getName() {
        return this.mName;
    }

    public void setName(@Nullable String str) {
        this.mName = str;
    }

    @Nullable
    public String getIp() {
        return this.mIp;
    }

    public void setIp(@Nullable String str) {
        this.mIp = str;
    }

    @Nullable
    public String getE164num() {
        return this.mE164num;
    }

    public void setE164num(@Nullable String str) {
        this.mE164num = str;
    }

    public int getDeviceType() {
        return this.mDeviceType;
    }

    public void setDeviceType(int i) {
        this.mDeviceType = i;
    }

    public int getEncrypt() {
        return this.mEncrypt;
    }

    public void setEncrypt(int i) {
        this.mEncrypt = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    @Nullable
    public String getAddress() {
        if (!StringUtil.isEmptyOrNull(this.mIp)) {
            return this.mIp;
        }
        return !StringUtil.isEmptyOrNull(this.mE164num) ? this.mE164num : "";
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.mName);
        parcel.writeString(this.mIp);
        parcel.writeString(this.mE164num);
        parcel.writeInt(this.mDeviceType);
        parcel.writeInt(this.mEncrypt);
    }

    @Nullable
    public String getDisplayName() {
        if (!TextUtils.isEmpty(this.mName)) {
            return this.mName;
        }
        if (!TextUtils.isEmpty(this.mIp)) {
            return this.mIp;
        }
        return this.mE164num;
    }
}
