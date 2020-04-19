package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class SmsSystemView extends AbsSmsView {
    protected TextView mTxtMessage;

    public PBXMessageItem getSmsItem() {
        return null;
    }

    public SmsSystemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public SmsSystemView(Context context) {
        super(context);
        initView();
    }

    public void setSmsItem(@NonNull PBXMessageItem pBXMessageItem) {
        setMessage(pBXMessageItem.getText());
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
}
