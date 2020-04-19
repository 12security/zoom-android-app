package com.zipow.videobox.login.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

public abstract class AbstractLoginPanel extends LinearLayout {
    public abstract void initVendorOptions(int i);

    public abstract boolean isEnableLoginType(int i);

    public AbstractLoginPanel(Context context) {
        super(context);
    }

    public AbstractLoginPanel(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public AbstractLoginPanel(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}
