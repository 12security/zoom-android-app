package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMMessageSystemView */
public class MMMessageSystemView extends AbsMessageView {
    protected TextView mTxtMessage;

    public MMMessageItem getMessageItem() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MMMessageSystemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMMessageSystemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_mm_message_system, this);
    }

    private void setMessage(@Nullable CharSequence charSequence) {
        TextView textView = this.mTxtMessage;
        if (textView == null) {
            return;
        }
        if (charSequence != null) {
            textView.setText(charSequence);
        } else {
            textView.setText("");
        }
    }

    public void setMessageItem(@Nullable MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            setMessage(mMMessageItem.message);
        }
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + getHeight());
    }
}
