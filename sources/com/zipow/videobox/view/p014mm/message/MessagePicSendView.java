package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessagePicSendView */
public class MessagePicSendView extends MessagePicView {
    public static String TAG = "MessagePicSendView";

    public MessagePicSendView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MessagePicSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessagePicSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_pic_sned, this);
    }

    /* access modifiers changed from: protected */
    public int getBubbleImageRes() {
        return C4558R.C4559drawable.zm_chatto_bg;
    }

    /* access modifiers changed from: protected */
    public Drawable getProgressBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 4, this.mMessageItem.onlyMessageShow, false);
    }

    /* access modifiers changed from: protected */
    public int[] getImgRadius() {
        int dip2px = UIUtil.dip2px(getContext(), 10.0f);
        return new int[]{dip2px, dip2px, dip2px, dip2px};
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
        if (mMMessageItem.fileDownloadResultCode != 5061) {
            setFailed(mMMessageItem.messageState == 4 || mMMessageItem.messageState == 5 || mMMessageItem.messageState == 6);
        }
        if (mMMessageItem.messageState != 1 || mMMessageItem.fileRatio < 0 || mMMessageItem.fileRatio > 100) {
            clearRatio();
        } else {
            setRatio(mMMessageItem.fileRatio);
        }
    }

    public void setSending(boolean z) {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(z ? 0 : 8);
        }
    }

    public void setFailed(boolean z) {
        setStatusImage(z, C4558R.C4559drawable.zm_mm_msg_state_fail);
    }
}
