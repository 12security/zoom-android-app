package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.AbsMessageView.OnClickMessageListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.ReactionEmojiContextMenuHeaderView */
public class ReactionEmojiContextMenuHeaderView extends FrameLayout {
    private FrameLayout mMessageView;

    public ReactionEmojiContextMenuHeaderView(@NonNull Context context) {
        super(context);
        init();
    }

    public ReactionEmojiContextMenuHeaderView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ReactionEmojiContextMenuHeaderView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_mm_reaction_emoji_context_menu_header_view, this);
        this.mMessageView = (FrameLayout) findViewById(C4558R.C4560id.message_view);
    }

    public void bindData(MMMessageItem mMMessageItem, boolean z, OnClickMessageListener onClickMessageListener) {
        AbsMessageView absMessageView;
        if (mMMessageItem != null) {
            if (mMMessageItem.isComment) {
                absMessageView = MMMessageItem.createCommentView(getContext(), mMMessageItem.messageType);
                if (z) {
                    absMessageView.reSizeTitleBarForReplyPage();
                }
            } else {
                absMessageView = MMMessageItem.createView(getContext(), mMMessageItem.messageType);
            }
            if (absMessageView != null) {
                LayoutParams layoutParams = new LayoutParams(-1, -2);
                absMessageView.setMessageItem(mMMessageItem, true);
                absMessageView.setOnClickMessageListener(onClickMessageListener);
                this.mMessageView.addView(absMessageView, layoutParams);
            }
        }
    }
}
