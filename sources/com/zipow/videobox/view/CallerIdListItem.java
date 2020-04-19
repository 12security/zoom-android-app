package com.zipow.videobox.view;

import android.content.Context;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.widget.IZMListItem;

public class CallerIdListItem implements IZMListItem {
    protected boolean isSelected;
    protected String label;
    protected String sublabel;

    public void init(Context context) {
    }

    public String getLabel() {
        return this.label;
    }

    public String getSubLabel() {
        return this.sublabel;
    }

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setLabel(@Nullable String str) {
        this.label = str;
    }

    public void setSublabel(@Nullable String str) {
        this.sublabel = str;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
