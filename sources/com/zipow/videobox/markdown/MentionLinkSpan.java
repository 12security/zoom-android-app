package com.zipow.videobox.markdown;

import android.os.Parcel;
import android.text.style.ForegroundColorSpan;
import androidx.annotation.NonNull;

public class MentionLinkSpan extends ForegroundColorSpan {
    private String jid;

    public MentionLinkSpan(int i, String str) {
        super(i);
        this.jid = str;
    }

    public MentionLinkSpan(@NonNull Parcel parcel) {
        super(parcel);
        parcel.readString();
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.jid);
    }

    public String getJid() {
        return this.jid;
    }
}
