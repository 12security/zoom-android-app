package com.zipow.videobox.thirdparty;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

public class AuthResult implements Parcelable {
    public static final String CMD_PARAM_ASTOKEN = "asToken";
    public static final String CMD_PARAM_ERROR_EXPIRE = "expire";
    public static final String CMD_PARAM_ERROR_MSG = "errormsg";
    public static final String CMD_PARAM_ERROR_NO = "errorno";
    public static final String CMD_PARAM_SNSTOKEN = "token";
    public static final Creator<AuthResult> CREATOR = new Creator<AuthResult>() {
        public AuthResult createFromParcel(Parcel parcel) {
            return new AuthResult(parcel);
        }

        public AuthResult[] newArray(int i) {
            return new AuthResult[i];
        }
    };
    private int action;
    @Nullable
    private String code;
    @Nullable
    private String errorMsg;
    @Nullable
    private String errorNo;
    @Nullable
    private String expire;
    @Nullable
    private String extraToken;
    @Nullable
    private String url;

    public int describeContents() {
        return 0;
    }

    public int getAction() {
        return this.action;
    }

    public boolean isValid() {
        return !StringUtil.isEmptyOrNull(this.code) || !StringUtil.isEmptyOrNull(this.extraToken);
    }

    public void setAction(int i) {
        this.action = i;
    }

    @Nullable
    public String getCode() {
        return this.code;
    }

    public int getErrorCode() {
        String str = this.errorNo;
        if (str == null) {
            return 0;
        }
        int i = -1;
        try {
            i = Integer.parseInt(str);
        } catch (NumberFormatException unused) {
        }
        return i;
    }

    public void setCode(@Nullable String str) {
        this.code = str;
    }

    @Nullable
    public String getExtraToken() {
        return this.extraToken;
    }

    public void setExtraToken(@Nullable String str) {
        this.extraToken = str;
    }

    @Nullable
    public String getErrorNo() {
        return this.errorNo;
    }

    public void setErrorNo(@Nullable String str) {
        this.errorNo = str;
    }

    @Nullable
    public String getErrorMsg() {
        return this.errorMsg;
    }

    public void setErrorMsg(@Nullable String str) {
        this.errorMsg = str;
    }

    @Nullable
    public String getExpire() {
        return this.expire;
    }

    public void setExpire(@Nullable String str) {
        this.expire = str;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthResult{action=");
        sb.append(this.action);
        sb.append(", code='");
        sb.append(this.code);
        sb.append('\'');
        sb.append(", extraToken='");
        sb.append(this.extraToken);
        sb.append('\'');
        sb.append(", errorNo='");
        sb.append(this.errorNo);
        sb.append('\'');
        sb.append(", errorMsg='");
        sb.append(this.errorMsg);
        sb.append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Nullable
    public String getUrl() {
        return this.url;
    }

    public void setUrl(@Nullable String str) {
        this.url = str;
    }

    public AuthResult() {
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.action);
        parcel.writeString(this.url);
        parcel.writeString(this.code);
        parcel.writeString(this.extraToken);
        parcel.writeString(this.errorNo);
        parcel.writeString(this.errorMsg);
        parcel.writeString(this.expire);
    }

    protected AuthResult(Parcel parcel) {
        this.action = parcel.readInt();
        this.url = parcel.readString();
        this.code = parcel.readString();
        this.extraToken = parcel.readString();
        this.errorNo = parcel.readString();
        this.errorMsg = parcel.readString();
        this.expire = parcel.readString();
    }
}
