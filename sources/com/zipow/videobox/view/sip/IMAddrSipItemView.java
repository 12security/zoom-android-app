package com.zipow.videobox.view.sip;

import android.content.Context;
import android.util.AttributeSet;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMAddrBookItemView;

public class IMAddrSipItemView extends IMAddrBookItemView {
    public IMAddrSipItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public IMAddrSipItemView(Context context) {
        super(context);
    }

    public void setAddrBookItem(IMAddrBookItem iMAddrBookItem, boolean z, boolean z2, boolean z3, int i) {
        super.setAddrBookItem(iMAddrBookItem, z, z2, z3, i);
        if (this.mPresenceStateView != null) {
            this.mPresenceStateView.setVisibility(8);
        }
        if (this.mTxtCustomMessage == null) {
            return;
        }
        if (this.mItem != null) {
            this.mTxtCustomMessage.setText(this.mItem.getSipPhoneNumber());
            this.mTxtCustomMessage.setVisibility(0);
            return;
        }
        this.mTxtCustomMessage.setVisibility(8);
    }
}
