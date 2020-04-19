package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMCommentAddReplyView */
public class MMCommentAddReplyView extends LinearLayout {
    public MMMessageItem msgItem;

    public MMCommentAddReplyView(Context context) {
        super(context);
        init();
    }

    public MMCommentAddReplyView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMCommentAddReplyView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_comments_add_reply, this);
    }
}
