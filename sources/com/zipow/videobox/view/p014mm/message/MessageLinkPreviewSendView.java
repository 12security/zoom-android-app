package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageLinkPreviewSendView */
public class MessageLinkPreviewSendView extends MessageLinkPreviewView {
    public MessageLinkPreviewSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageLinkPreviewSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_preview_send, this);
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        MMChatMessageBgDrawable mMChatMessageBgDrawable = new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false, false);
        return mMChatMessageBgDrawable;
    }
}
