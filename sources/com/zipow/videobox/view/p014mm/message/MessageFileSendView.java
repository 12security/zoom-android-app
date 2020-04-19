package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.message.MessageFileSendView */
public class MessageFileSendView extends MessageFileView {
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

    public MessageFileSendView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageFileSendView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_message_file_send, this);
    }

    public void setMessageItem(@NonNull MMMessageItem mMMessageItem) {
        boolean z;
        super.setMessageItem(mMMessageItem);
        FileTransferInfo fileTransferInfo = mMMessageItem.transferInfo;
        boolean z2 = false;
        if (fileTransferInfo != null) {
            setSending(mMMessageItem.messageState == 1 && fileTransferInfo.state == 1);
            z = fileTransferInfo.state == 2 || !(fileTransferInfo.state != 18 || mMMessageItem.messageState == 3 || mMMessageItem.messageState == 2 || mMMessageItem.messageState == 7);
        } else {
            z = false;
        }
        if (z || mMMessageItem.messageState == 4 || mMMessageItem.messageState == 5) {
            z2 = true;
        }
        setFailed(z2);
    }

    public void setSending(boolean z) {
        ProgressBar progressBar = this.mProgressBar;
    }

    public void setFailed(boolean z) {
        setStatusImage(z, C4558R.C4559drawable.zm_mm_msg_state_fail);
        if (z) {
            setSending(false);
        }
    }
}
