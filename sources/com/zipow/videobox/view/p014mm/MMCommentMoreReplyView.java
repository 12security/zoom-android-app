package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMCommentMoreReplyView */
public class MMCommentMoreReplyView extends LinearLayout {
    public TextView moreReply;
    public MMMessageItem msgItem;
    public TextView txtAtAll;
    public TextView txtAtMe;
    public TextView txtMarkUnread;
    public TextView txtNoteBubble;
    public View unreadBubble;

    public MMCommentMoreReplyView(Context context) {
        super(context);
        init();
    }

    public MMCommentMoreReplyView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMCommentMoreReplyView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_comments_more_reply, this);
        this.moreReply = (TextView) findViewById(C4558R.C4560id.more_reply);
        this.txtNoteBubble = (TextView) findViewById(C4558R.C4560id.txtNoteBubble);
        this.unreadBubble = findViewById(C4558R.C4560id.unreadBubble);
        this.txtMarkUnread = (TextView) findViewById(C4558R.C4560id.txtMarkUnread);
        this.txtAtMe = (TextView) findViewById(C4558R.C4560id.txtAtMe);
        this.txtAtAll = (TextView) findViewById(C4558R.C4560id.txtAtAll);
    }
}
