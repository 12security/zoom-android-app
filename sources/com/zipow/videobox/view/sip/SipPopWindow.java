package com.zipow.videobox.view.sip;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.widget.ZMPopupWindow;
import p021us.zoom.videomeetings.C4558R;

public class SipPopWindow extends ZMPopupWindow {
    private TextView mTxtMsg;

    public SipPopWindow() {
    }

    public SipPopWindow(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public SipPopWindow(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public SipPopWindow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SipPopWindow(Context context) {
        super(context);
    }

    public SipPopWindow(int i, int i2) {
        super(i, i2);
    }

    /* access modifiers changed from: protected */
    public void init() {
        super.init();
        LayoutInflater layoutInflater = (LayoutInflater) VideoBoxApplication.getNonNullInstance().getSystemService("layout_inflater");
        if (layoutInflater != null) {
            View inflate = layoutInflater.inflate(C4558R.layout.zm_sip_pop, null);
            setContentView(inflate);
            this.mTxtMsg = (TextView) inflate.findViewById(C4558R.C4560id.tvMsg);
        }
        setBackgroundDrawable(new ColorDrawable());
    }

    public void setText(String str) {
        if (!TextUtils.isEmpty(str)) {
            TextView textView = this.mTxtMsg;
            if (textView != null) {
                textView.setText(str);
            }
        }
    }
}
