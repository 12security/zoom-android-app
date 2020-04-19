package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;

public class PListButton extends ToolbarButton {
    public PListButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PListButton(Context context) {
        super(context);
    }

    public void setUnreadMessageCount(int i) {
        String str = i == 0 ? null : i < 100 ? String.valueOf(i) : "99+";
        setNoteMessage((CharSequence) str);
    }
}
