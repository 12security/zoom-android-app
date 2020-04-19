package com.zipow.videobox.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.ConfChatMessage;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.view.ConfChatItemView.ConfChatPrivateItemView;
import com.zipow.videobox.view.ConfChatItemView.ConfChatPublicItemView;

public class ConfChatItem {
    @Nullable
    public String content = "";

    /* renamed from: id */
    public String f337id = "";
    public boolean isNotification;
    public boolean isSelfSend;
    public int msgType = -1;
    public long receiver;
    public String receiverJid;
    public String receiverName = "";
    public long sender;
    public String senderJid;
    public String senderName = "";
    public long time;
    public int type;

    public ConfChatItem() {
    }

    public static ConfChatItem getConfChatItemFromMsgID(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ConfMgr instance = ConfMgr.getInstance();
        ConfChatMessage chatMessageItemByID = instance.getChatMessageItemByID(str);
        if (chatMessageItemByID == null) {
            return null;
        }
        if (z) {
            instance.setChatMessageAsReaded(str);
        }
        return new ConfChatItem(chatMessageItemByID);
    }

    public ConfChatItem(ConfChatMessage confChatMessage) {
        update(confChatMessage.getMessageID(), confChatMessage.getSenderID(), confChatMessage.getSenderDisplayName(), confChatMessage.getReceiverID(), confChatMessage.getReceiverDisplayName(), confChatMessage.getMessageContent(), confChatMessage.getTimeStamp(), confChatMessage.isSelfSend(), confChatMessage.getMsgType(), confChatMessage.getSenderJid(), confChatMessage.getRecieverJid());
    }

    @NonNull
    private ConfChatItem update(String str, long j, String str2, long j2, String str3, String str4, long j3, boolean z, int i, String str5, String str6) {
        this.f337id = str;
        this.sender = j;
        this.receiver = j2;
        this.senderName = str2;
        this.receiverName = str3;
        this.content = str4;
        this.time = j3;
        this.isSelfSend = z;
        this.msgType = i;
        if (j2 != 0) {
            this.type = 1;
        } else {
            this.type = 0;
        }
        this.senderJid = str5;
        this.receiverJid = str6;
        return this;
    }

    @Nullable
    public View getView(Context context, View view) {
        switch (this.type) {
            case 0:
                return getPublicItemView(context, view);
            case 1:
                return getPrivateItemView(context, view);
            default:
                return null;
        }
    }

    @NonNull
    private View getPublicItemView(Context context, View view) {
        ConfChatPublicItemView confChatPublicItemView;
        if (view instanceof ConfChatPublicItemView) {
            confChatPublicItemView = (ConfChatPublicItemView) view;
        } else {
            confChatPublicItemView = new ConfChatPublicItemView(context);
        }
        bindView(confChatPublicItemView);
        return confChatPublicItemView;
    }

    @NonNull
    private View getPrivateItemView(Context context, View view) {
        ConfChatPrivateItemView confChatPrivateItemView;
        if (view instanceof ConfChatPrivateItemView) {
            confChatPrivateItemView = (ConfChatPrivateItemView) view;
        } else {
            confChatPrivateItemView = new ConfChatPrivateItemView(context);
        }
        bindView(confChatPrivateItemView);
        return confChatPrivateItemView;
    }

    private void bindView(ConfChatItemView confChatItemView) {
        confChatItemView.setChatItem(this);
    }
}
