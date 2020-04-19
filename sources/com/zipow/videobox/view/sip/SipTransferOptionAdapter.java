package com.zipow.videobox.view.sip;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class SipTransferOptionAdapter extends ZMMenuAdapter<SipTransferMenuItem> {

    public static class SipTransferMenuItem extends ZMSimpleMenuItem {
        public static final int BLIND_TRANSFER = 0;
        public static final int VOICE_TRANSFER = 2;
        public static final int WARM_TRANSFER = 1;
        private String subLabel;

        public SipTransferMenuItem(int i, String str, String str2) {
            super(i, str);
            this.subLabel = str2;
        }

        public String getSubLabel() {
            return this.subLabel;
        }

        public void setSubLabel(String str) {
            this.subLabel = str;
        }
    }

    public SipTransferOptionAdapter(Context context) {
        this(context, false);
    }

    public SipTransferOptionAdapter(Context context, boolean z) {
        super(context, z);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C4558R.layout.zm_pbx_transfer_option_item;
    }

    /* access modifiers changed from: protected */
    public void onBindView(@NonNull View view, @NonNull SipTransferMenuItem sipTransferMenuItem) {
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtSubLabel);
        textView.setText(sipTransferMenuItem.getLabel());
        textView2.setText(sipTransferMenuItem.getSubLabel());
    }
}
