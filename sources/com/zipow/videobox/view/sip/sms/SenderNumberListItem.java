package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.widget.IZMListItem;

public class SenderNumberListItem implements IZMListItem {
    protected boolean isSelected;
    private String label;
    private String subLabel;

    public void init(Context context) {
    }

    public SenderNumberListItem(String str, boolean z) {
        this.label = str;
        this.isSelected = z;
    }

    public String getLabel() {
        return this.label;
    }

    public String getSubLabel() {
        return this.subLabel;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setLabel(@Nullable String str) {
        this.label = str;
    }

    public void setSubLabel(@Nullable String str) {
        this.subLabel = str;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
