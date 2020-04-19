package com.zipow.videobox.view.sip;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;

public class PhonePBXBlockReasonItem extends ZMSimpleMenuItem implements Parcelable {
    public static final Creator<PhonePBXBlockReasonItem> CREATOR = new Creator<PhonePBXBlockReasonItem>() {
        public PhonePBXBlockReasonItem createFromParcel(Parcel parcel) {
            return new PhonePBXBlockReasonItem(parcel);
        }

        public PhonePBXBlockReasonItem[] newArray(int i) {
            return new PhonePBXBlockReasonItem[i];
        }
    };

    public int describeContents() {
        return 0;
    }

    public PhonePBXBlockReasonItem() {
    }

    protected PhonePBXBlockReasonItem(Parcel parcel) {
        setLabel(parcel.readString());
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getLabel());
    }
}
