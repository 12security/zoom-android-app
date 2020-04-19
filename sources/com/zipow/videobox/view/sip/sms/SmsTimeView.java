package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.util.AttributeSet;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.util.TimeFormatUtil;

public class SmsTimeView extends SmsSystemView {
    public SmsTimeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public SmsTimeView(Context context) {
        super(context);
    }

    public void setSmsItem(@NonNull PBXMessageItem pBXMessageItem) {
        String formatDateTime = TimeFormatUtil.formatDateTime(VideoBoxApplication.getNonNullInstance(), pBXMessageItem.getTimestamp(), true);
        if (formatDateTime == null || formatDateTime.contains("null")) {
            formatDateTime = "Monday, 00:00 am";
        }
        if (this.mTxtMessage != null) {
            this.mTxtMessage.setText(formatDateTime);
        }
    }
}
