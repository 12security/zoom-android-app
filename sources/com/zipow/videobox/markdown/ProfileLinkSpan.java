package com.zipow.videobox.markdown;

import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import androidx.annotation.NonNull;

public class ProfileLinkSpan extends BackgroundColorSpan {
    private String jid;

    public ProfileLinkSpan(int i, String str) {
        super(i);
        this.jid = str;
    }

    public ProfileLinkSpan(@NonNull Parcel parcel) {
        super(parcel);
        this.jid = parcel.readString();
    }

    public void writeToParcel(@NonNull Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.jid);
    }

    public String getJid() {
        return this.jid;
    }

    public void updateDrawState(@NonNull TextPaint textPaint) {
        super.updateDrawState(textPaint);
    }
}
