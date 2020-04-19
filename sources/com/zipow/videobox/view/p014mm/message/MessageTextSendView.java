package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageTextSendView */
public class MessageTextSendView extends MessageTextView {
    private static String TAG = "MessageTextSendView";

    public MessageTextSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageTextSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_text_send, this);
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
        boolean z = false;
        setSending(mMMessageItem.messageState == 1 || (mMMessageItem.isE2E && mMMessageItem.messageState == 3));
        if (mMMessageItem.messageState == 4 || mMMessageItem.messageState == 5 || mMMessageItem.messageState == 8 || mMMessageItem.messageState == 12 || mMMessageItem.messageState == 11 || mMMessageItem.messageState == 13) {
            z = true;
        }
        setFailed(z);
    }

    public void setSending(boolean z) {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(z ? 0 : 8);
        }
        if (this.mTxtMessage != null) {
            this.mTxtMessage.setClickable(!z);
        }
        if (this.mPanelMessage != null) {
            this.mPanelMessage.setClickable(!z);
        }
    }

    /* access modifiers changed from: protected */
    public int getTextColor() {
        int i;
        if (!this.mMessageItem.isE2E) {
            i = C4558R.color.zm_text_on_light;
        } else if (this.mMessageItem.messageState == 9 || this.mMessageItem.messageState == 8) {
            i = C4558R.color.zm_chat_msg_txt_e2e_warn;
        } else if (this.mMessageItem.messageState == 3 || this.mMessageItem.messageState == 11 || this.mMessageItem.messageState == 13) {
            i = C4558R.color.zm_text_on_light;
        } else {
            i = C4558R.color.zm_text_on_light;
        }
        return getResources().getColor(i);
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false);
    }

    public void setFailed(boolean z) {
        setStatusImage(z, C4558R.C4559drawable.zm_mm_msg_state_fail);
    }
}
