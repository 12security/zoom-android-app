package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.view.p014mm.MMChatMessageBgDrawable;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageFileIntegrationSendView */
public class MessageFileIntegrationSendView extends MessageFileIntegrationView {
    private static boolean isUploadState(int i) {
        switch (i) {
            case 1:
            case 2:
            case 3:
            case 4:
                return true;
            default:
                return false;
        }
    }

    public MessageFileIntegrationSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageFileIntegrationSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_file_integration_send, this);
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        Drawable drawable = getResources().getDrawable((mMMessageItem.transferInfo == null || !isUploadState(mMMessageItem.transferInfo.state)) ? C4558R.C4559drawable.zm_downloading_percent_ondark : C4558R.C4559drawable.zm_uploading_percent);
        drawable.setBounds(this.mDownloadPercent.getProgressDrawable().getBounds());
        this.mDownloadPercent.setProgressDrawable(drawable);
        boolean z = false;
        this.mDownloadPercent.setProgress(0);
        super.setMessageItem(mMMessageItem);
        setSending(mMMessageItem.messageState == 1);
        FileTransferInfo fileTransferInfo = mMMessageItem.transferInfo;
        boolean z2 = fileTransferInfo != null ? fileTransferInfo.state == 2 || fileTransferInfo.state == 18 : false;
        if (z2 || mMMessageItem.messageState == 4 || mMMessageItem.messageState == 5) {
            z = true;
        }
        setFailed(z);
    }

    public void setSending(boolean z) {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(z ? 0 : 8);
        }
    }

    public void setFailed(boolean z) {
        setStatusImage(z, C4558R.C4559drawable.zm_mm_msg_state_fail);
        if (z) {
            setSending(false);
        }
    }

    /* access modifiers changed from: protected */
    public Drawable getMesageBackgroudDrawable() {
        MMChatMessageBgDrawable mMChatMessageBgDrawable = new MMChatMessageBgDrawable(getContext(), 0, this.mMessageItem.onlyMessageShow, false, 0, 0, 0, 0);
        return mMChatMessageBgDrawable;
    }
}
