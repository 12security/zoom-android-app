package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageBelowNewCommentView */
public class MessageBelowNewCommentView extends AbsMessageView {
    public MMMessageItem getMessageItem() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public MessageBelowNewCommentView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MessageBelowNewCommentView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_below_new_comment, this);
    }

    public Rect getMessageLocationOnScreen() {
        int[] iArr = new int[2];
        getLocationOnScreen(iArr);
        return new Rect(iArr[0], iArr[1], iArr[0] + getWidth(), iArr[1] + getHeight());
    }
}
