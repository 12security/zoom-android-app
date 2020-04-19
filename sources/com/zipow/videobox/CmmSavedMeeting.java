package com.zipow.videobox;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CmmSavedMeeting implements Parcelable {
    public static final Creator<CmmSavedMeeting> CREATOR = new Creator<CmmSavedMeeting>() {
        public CmmSavedMeeting createFromParcel(@NonNull Parcel parcel) {
            return new CmmSavedMeeting(parcel);
        }

        public CmmSavedMeeting[] newArray(int i) {
            return new CmmSavedMeeting[i];
        }
    };
    @Nullable
    String mConfID;
    @Nullable
    String mConfTopic;

    public int describeContents() {
        return 0;
    }

    public CmmSavedMeeting(@Nullable String str, @Nullable String str2) {
        this.mConfID = str;
        this.mConfTopic = str2;
    }

    @Nullable
    public String getmConfID() {
        return this.mConfID;
    }

    @Nullable
    public String getmConfTopic() {
        return this.mConfTopic;
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(this.mConfID);
        parcel.writeString(this.mConfTopic);
    }

    protected CmmSavedMeeting(Parcel parcel) {
        this.mConfID = parcel.readString();
        this.mConfTopic = parcel.readString();
    }
}
