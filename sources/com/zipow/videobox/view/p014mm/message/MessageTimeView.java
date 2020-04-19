package com.zipow.videobox.view.p014mm.message;

import android.content.Context;
import android.util.AttributeSet;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.TimeFormatUtil;
import com.zipow.videobox.view.p014mm.MMMessageItem;
import com.zipow.videobox.view.p014mm.MMMessageSystemView;

/* renamed from: com.zipow.videobox.view.mm.message.MessageTimeView */
public class MessageTimeView extends MMMessageSystemView {
    public MessageTimeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MessageTimeView(Context context) {
        super(context);
    }

    public void setMessageItem(MMMessageItem mMMessageItem) {
        String formatDateTime = TimeFormatUtil.formatDateTime(VideoBoxApplication.getGlobalContext(), mMMessageItem.messageTime, true);
        if (formatDateTime == null || formatDateTime.contains("null")) {
            formatDateTime = "Monday, 00:00 am";
        }
        if (this.mTxtMessage != null) {
            this.mTxtMessage.setText(formatDateTime);
        }
    }
}
