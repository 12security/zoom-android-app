package com.zipow.videobox.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import com.zipow.videobox.util.TimeFormatUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMMessageItem {
    public static final int ITEM_TYPE_CALLHANGUP_RECEIVED = 4;
    public static final int ITEM_TYPE_CALLHANGUP_SENT = 5;
    public static final int ITEM_TYPE_CALLINVITATION_RECEIVED = 2;
    public static final int ITEM_TYPE_CALLINVITATION_SENT = 3;
    public static final int ITEM_TYPE_DATE = 10;
    public static final int ITEM_TYPE_NORMAL_RECEIVED = 0;
    public static final int ITEM_TYPE_NORMAL_SENT = 1;
    public static final int ITEM_TYPE_SHAREHANGUP_RECEIVED = 8;
    public static final int ITEM_TYPE_SHAREHANGUP_SENT = 9;
    public static final int ITEM_TYPE_SHAREINVITATION_RECEIVED = 6;
    public static final int ITEM_TYPE_SHAREINVITATION_SENT = 7;
    public String fromJid;
    public String fromScreenName;
    public String message;
    public long messageTime;
    public int messageType = 0;
    public long nativeHandle = 0;
    public String toJid;
    public String toScreenName;

    @Nullable
    public View getView(@NonNull Context context, View view) {
        switch (this.messageType) {
            case 0:
            case 2:
            case 4:
            case 6:
            case 8:
                return createNormalItemView(context, view, true);
            case 1:
            case 3:
            case 5:
            case 7:
            case 9:
                return createNormalItemView(context, view, false);
            case 10:
                return createDateItemView(context, view);
            default:
                return null;
        }
    }

    private void bindItemView(IMMessageView iMMessageView) {
        iMMessageView.setMessageItem(this);
    }

    @NonNull
    private View createNormalItemView(Context context, View view, boolean z) {
        IMMessageView iMMessageView;
        String str = z ? "IncomingMessage" : "OutMessage";
        if (!(view instanceof IMMessageView) || !str.equals(view.getTag())) {
            iMMessageView = new IMMessageView(context, z);
            iMMessageView.setTag(str);
        } else {
            iMMessageView = (IMMessageView) view;
        }
        bindItemView(iMMessageView);
        return iMMessageView;
    }

    @NonNull
    private View createDateItemView(@NonNull Context context, @Nullable View view) {
        if (view == null || !ExifInterface.TAG_DATETIME.equals(view.getTag())) {
            view = View.inflate(context, C4558R.layout.zm_im_message_item_date, null);
            view.setTag(ExifInterface.TAG_DATETIME);
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtMessage);
        if (textView != null) {
            String formatDateTime = TimeFormatUtil.formatDateTime(context, this.messageTime, false);
            if (formatDateTime == null || formatDateTime.indexOf("null") >= 0) {
                formatDateTime = "Monday, 00:00 am";
            }
            textView.setText(formatDateTime);
        }
        return view;
    }
}
