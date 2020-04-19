package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageUnSupportSendView */
public class MessageUnSupportSendView extends MessageUnSupportView {
    public MessageUnSupportSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageUnSupportSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_text_send, this);
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
    }

    /* access modifiers changed from: protected */
    public int getTextColor() {
        return getResources().getColor(C4558R.color.zm_text_on_dark);
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false);
    }
}
