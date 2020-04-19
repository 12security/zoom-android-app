package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageAudioSendView */
public class MessageAudioSendView extends MessageAudioView {
    private static String TAG = "MessageAudioSendView";

    public MessageAudioSendView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public MessageAudioSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageAudioSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_audio_send, this);
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        super.setMessageItem(mMMessageItem);
        boolean z = false;
        setSending(mMMessageItem.messageState == 1);
        if (mMMessageItem.messageState == 4 || mMMessageItem.messageState == 5) {
            z = true;
        }
        setFailed(z);
        if (mMMessageItem.isPlaying) {
            this.mImgVoice.setImageResource(C4558R.C4559drawable.zm_chatfrom_voice_playing);
        } else {
            this.mImgVoice.setImageResource(C4558R.C4559drawable.zm_chatfrom_voice);
        }
        Drawable drawable = this.mImgVoice.getDrawable();
        if (drawable instanceof AnimationDrawable) {
            ((AnimationDrawable) drawable).start();
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

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        return new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false);
    }
}
