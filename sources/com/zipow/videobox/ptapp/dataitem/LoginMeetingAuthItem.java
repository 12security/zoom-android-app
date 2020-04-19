package com.zipow.videobox.ptapp.dataitem;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AuthProto;
import com.zipow.videobox.ptapp.PTAppProtos.LoginMeetingAuthProto;

public class LoginMeetingAuthItem implements Parcelable {
    public static final Creator<LoginMeetingAuthItem> CREATOR = new Creator<LoginMeetingAuthItem>() {
        public LoginMeetingAuthItem createFromParcel(Parcel parcel) {
            return new LoginMeetingAuthItem(parcel);
        }

        public LoginMeetingAuthItem[] newArray(int i) {
            return new LoginMeetingAuthItem[i];
        }
    };
    private String authDomain;
    private String authId;
    private String authName;
    private int authType;
    private boolean uiSelect;

    public int describeContents() {
        return 0;
    }

    public LoginMeetingAuthItem(AuthProto authProto) {
        this.authId = authProto.getAuthId();
        this.authName = authProto.getAuthName();
        this.authType = authProto.getAuthType();
        this.authDomain = authProto.getAuthDomain();
    }

    public LoginMeetingAuthItem(LoginMeetingAuthProto loginMeetingAuthProto) {
        this.authId = loginMeetingAuthProto.getAuthId();
        this.authName = loginMeetingAuthProto.getAuthName();
        this.authType = loginMeetingAuthProto.getAuthType();
        this.authDomain = loginMeetingAuthProto.getAuthDomain();
        this.uiSelect = loginMeetingAuthProto.getDefaultAuth();
    }

    protected LoginMeetingAuthItem(Parcel parcel) {
        this.authId = parcel.readString();
        this.authName = parcel.readString();
        this.authType = parcel.readInt();
        this.authDomain = parcel.readString();
        this.uiSelect = parcel.readByte() != 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.authId);
        parcel.writeString(this.authName);
        parcel.writeInt(this.authType);
        parcel.writeString(this.authDomain);
        parcel.writeByte(this.uiSelect ? (byte) 1 : 0);
    }

    public String getAuthId() {
        return this.authId;
    }

    public void setAuthId(String str) {
        this.authId = str;
    }

    public String getAuthName() {
        return this.authName;
    }

    public void setAuthName(String str) {
        this.authName = str;
    }

    public int getAuthType() {
        return this.authType;
    }

    public void setAuthType(int i) {
        this.authType = i;
    }

    public String getAuthDomain() {
        return this.authDomain;
    }

    public void setAuthDomain(String str) {
        this.authDomain = str;
    }

    public boolean isUiSelect() {
        return this.uiSelect;
    }

    public void setUiSelect(boolean z) {
        this.uiSelect = z;
    }
}
