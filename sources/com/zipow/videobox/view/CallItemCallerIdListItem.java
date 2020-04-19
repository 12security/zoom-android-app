package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.widget.IZMListItem;

public class CallItemCallerIdListItem implements IZMListItem {
    @Nullable
    private String label;
    @Nullable
    private String subLabel;

    public void init(Context context) {
    }

    public boolean isSelected() {
        return false;
    }

    public void init(Context context, String str, String str2) {
        init(context);
        this.label = str;
        if (TextUtils.isEmpty(str2)) {
            this.subLabel = null;
        } else {
            this.subLabel = str2;
        }
    }

    @Nullable
    public String getLabel() {
        return this.label;
    }

    @Nullable
    public String getSubLabel() {
        return this.subLabel;
    }
}
