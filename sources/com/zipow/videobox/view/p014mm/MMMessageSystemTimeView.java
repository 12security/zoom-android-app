package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.TimeFormatUtil;

/* renamed from: com.zipow.videobox.view.mm.MMMessageSystemTimeView */
public class MMMessageSystemTimeView extends MMMessageSystemView {
    public MMMessageSystemTimeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public MMMessageSystemTimeView(Context context) {
        super(context);
    }

    public void setMessageItem(@Nullable MMMessageItem mMMessageItem) {
        if (mMMessageItem != null) {
            String formatDateTime = TimeFormatUtil.formatDateTime(VideoBoxApplication.getGlobalContext(), mMMessageItem.messageTime, false);
            if (formatDateTime == null || formatDateTime.contains("null")) {
                formatDateTime = "Monday, 00:00 am";
            }
            if (this.mTxtMessage != null) {
                this.mTxtMessage.setText(formatDateTime);
            }
        }
    }
}
