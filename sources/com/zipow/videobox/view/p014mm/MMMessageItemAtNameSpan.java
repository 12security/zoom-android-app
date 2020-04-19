package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.ContextWrapper;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.eventbus.ZMAtBuddyClickEvent;
import com.zipow.videobox.view.AtNameInfo;
import org.greenrobot.eventbus.EventBus;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.IZMSpan;

/* renamed from: com.zipow.videobox.view.mm.MMMessageItemAtNameSpan */
public class MMMessageItemAtNameSpan extends ClickableSpan implements IZMSpan {
    public int color;
    public int end;
    public String jid;
    public MMMessageItem messageItem;
    public int start;
    public int type = 0;

    public int getSpanType() {
        return 1;
    }

    public MMMessageItemAtNameSpan(@ColorInt int i, int i2, int i3, String str, MMMessageItem mMMessageItem) {
        this.start = i2;
        this.end = i3;
        this.color = i;
        this.jid = str;
        this.messageItem = mMMessageItem;
    }

    public MMMessageItemAtNameSpan(@ColorInt int i, @NonNull AtNameInfo atNameInfo, MMMessageItem mMMessageItem) {
        this.start = atNameInfo.getStartIndex();
        this.end = atNameInfo.getEndIndex();
        this.color = i;
        this.type = atNameInfo.getType();
        this.jid = atNameInfo.getJid();
        this.messageItem = mMMessageItem;
    }

    public void onClick(@NonNull View view) {
        if (this.type == 3) {
            for (Context context = view.getContext(); context instanceof ContextWrapper; context = ((ContextWrapper) context).getBaseContext()) {
                if (context instanceof ZMActivity) {
                    MMChatActivity.showAsGroupChat((ZMActivity) context, this.jid);
                    return;
                }
            }
            return;
        }
        EventBus.getDefault().post(new ZMAtBuddyClickEvent(this.jid, this.messageItem));
    }

    public void updateDrawState(TextPaint textPaint) {
        textPaint.setColor(this.color);
        textPaint.setUnderlineText(false);
    }
}
