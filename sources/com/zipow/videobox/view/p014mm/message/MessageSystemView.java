package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageSystemView */
public class MessageSystemView extends AbsMessageView {
    protected TextView mTxtMessage;

    public MMMessageItem getMessageItem() {
        return null;
    }

    public Rect getMessageLocationOnScreen() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MessageSystemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageSystemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mTxtMessage = (TextView) findViewById(C4558R.C4560id.txtMessage);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_system, this);
    }

    private void setMessage(CharSequence charSequence) {
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

    public void setMessageItem(MMMessageItem mMMessageItem) {
        setMessage(mMMessageItem.message);
    }
}
