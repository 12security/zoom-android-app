package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.zipow.videobox.view.p014mm.AbsMessageView;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.CommentSplitView */
public class CommentSplitView extends AbsMessageView {
    public MMMessageItem getMessageItem() {
        return null;
    }

    public Rect getMessageLocationOnScreen() {
        return null;
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
    }

    public void setMessageItem(MMMessageItem mMMessageItem, boolean z) {
    }

    public CommentSplitView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public CommentSplitView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_comment_split, this);
    }
}
