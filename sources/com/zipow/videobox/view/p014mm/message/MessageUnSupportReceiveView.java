package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageUnSupportReceiveView */
public class MessageUnSupportReceiveView extends MessageUnSupportView {
    public MessageUnSupportReceiveView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageUnSupportReceiveView(Context context) {
        super(context);
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_text_receive, this);
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, true);
    }
}
