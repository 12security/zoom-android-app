package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.util.AttributeSet;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageCodeSnippetSendView */
public class MessageCodeSnippetSendView extends MessageCodeSnippetReceiveView {
    public MessageCodeSnippetSendView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MessageCodeSnippetSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageCodeSnippetSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public int getLayoutId() {
        return C4558R.layout.zm_message_code_snippet_msg_send;
    }
}
