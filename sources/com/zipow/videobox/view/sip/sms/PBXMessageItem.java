package com.zipow.videobox.view.sip.sms;

import android.content.Context;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.zipow.videobox.ptapp.PTAppProtos.PBXMessageContact;
import com.zipow.videobox.sip.server.IPBXMessage;
import java.util.List;

public class PBXMessageItem {
    public static final int ITEM_TYPE_SYSTEM = 2;
    public static final int ITEM_TYPE_TEXT = 0;
    public static final int ITEM_TYPE_TIME_STAMP = 1;
    private long createTime;
    private int direction;
    private PBXMessageContact fromContact;

    /* renamed from: id */
    private String f353id;
    private List<String> mediaUrls;
    private int messageType;
    private boolean onlyMessageShow;
    private PBXMessageContact ownerContact;
    private int readStatus;
    private int requestStatus;
    private int sendErrorCode;
    private int sendStatus;
    private String sessionID;
    private String text;
    private List<PBXMessageContact> toContacts;
    private long updatedTime;

    public static PBXMessageItem initFromPBXMessage(@NonNull IPBXMessage iPBXMessage) {
        PBXMessageItem pBXMessageItem = new PBXMessageItem();
        pBXMessageItem.f353id = iPBXMessage.getID();
        pBXMessageItem.sessionID = iPBXMessage.getSessionID();
        pBXMessageItem.fromContact = iPBXMessage.getFromContact();
        pBXMessageItem.toContacts = iPBXMessage.getToContacts();
        pBXMessageItem.ownerContact = iPBXMessage.getOwnerContact();
        pBXMessageItem.direction = iPBXMessage.getDirection();
        pBXMessageItem.text = iPBXMessage.getText();
        pBXMessageItem.createTime = iPBXMessage.getCreateTime();
        pBXMessageItem.updatedTime = iPBXMessage.getUpdatedTime();
        pBXMessageItem.mediaUrls = iPBXMessage.getMediaUrls();
        pBXMessageItem.readStatus = iPBXMessage.getReadStatus();
        pBXMessageItem.sendStatus = iPBXMessage.getSendStatus();
        pBXMessageItem.sendErrorCode = iPBXMessage.getSendErrorCode();
        pBXMessageItem.requestStatus = iPBXMessage.getRequestStatus();
        return pBXMessageItem;
    }

    public static PBXMessageItem createTestMsg(int i) {
        PBXMessageItem pBXMessageItem = new PBXMessageItem();
        StringBuilder sb = new StringBuilder();
        sb.append("111");
        sb.append(System.currentTimeMillis());
        pBXMessageItem.f353id = sb.toString();
        pBXMessageItem.sessionID = "1234rfewf";
        pBXMessageItem.toContacts = null;
        pBXMessageItem.direction = i % 2;
        pBXMessageItem.text = "test 11112e234\ndfewfewf";
        pBXMessageItem.createTime = System.currentTimeMillis();
        pBXMessageItem.updatedTime = System.currentTimeMillis();
        return pBXMessageItem;
    }

    public static PBXMessageItem createTimeStampMsg(String str, long j) {
        PBXMessageItem pBXMessageItem = new PBXMessageItem();
        pBXMessageItem.sessionID = str;
        pBXMessageItem.createTime = j;
        pBXMessageItem.messageType = 1;
        StringBuilder sb = new StringBuilder();
        sb.append("time");
        sb.append(j);
        pBXMessageItem.f353id = sb.toString();
        pBXMessageItem.onlyMessageShow = false;
        return pBXMessageItem;
    }

    public static PBXMessageItem createSystemMessage(String str, long j) {
        PBXMessageItem pBXMessageItem = new PBXMessageItem();
        pBXMessageItem.text = str;
        pBXMessageItem.createTime = j;
        pBXMessageItem.messageType = 2;
        StringBuilder sb = new StringBuilder();
        sb.append("system");
        sb.append(j);
        pBXMessageItem.f353id = sb.toString();
        pBXMessageItem.onlyMessageShow = false;
        return pBXMessageItem;
    }

    public void bindViewHolder(ViewHolder viewHolder) {
        if (viewHolder.itemView instanceof AbsSmsView) {
            ((AbsSmsView) viewHolder.itemView).setSmsItem(this);
        }
    }

    @Nullable
    public static AbsSmsView createView(Context context, int i) {
        switch (i) {
            case 0:
                return createMessageTextSendView(context, null);
            case 1:
                return createMessageTimeView(context, null);
            case 2:
                return createSystemMessageView(context, null);
            default:
                return null;
        }
    }

    @NonNull
    private static AbsSmsView createMessageTextSendView(Context context, View view) {
        if ((view instanceof PbxSmsTextItemView) && "PbxSmsTextItem".equals(view.getTag())) {
            return (PbxSmsTextItemView) view;
        }
        PbxSmsTextItemView pbxSmsTextItemView = new PbxSmsTextItemView(context);
        pbxSmsTextItemView.setTag("PbxSmsTextItem");
        return pbxSmsTextItemView;
    }

    @NonNull
    private static AbsSmsView createMessageTimeView(Context context, View view) {
        if ((view instanceof SmsTimeView) && "SmsTimeView".equals(view.getTag())) {
            return (SmsTimeView) view;
        }
        SmsTimeView smsTimeView = new SmsTimeView(context);
        smsTimeView.setTag("SmsTimeView");
        return smsTimeView;
    }

    @NonNull
    private static AbsSmsView createSystemMessageView(Context context, View view) {
        if ((view instanceof SmsSystemView) && "SmsSystemView".equals(view.getTag())) {
            return (SmsSystemView) view;
        }
        SmsSystemView smsSystemView = new SmsSystemView(context);
        smsSystemView.setTag("SmsSystemView");
        return smsSystemView;
    }

    public boolean isSystemMsg() {
        switch (this.messageType) {
            case 1:
            case 2:
                return true;
            default:
                return false;
        }
    }

    public int getMessageType() {
        return this.messageType;
    }

    public boolean isOnlyMessageShow() {
        return this.onlyMessageShow;
    }

    public void setOnlyMessageShow(boolean z) {
        this.onlyMessageShow = z;
    }

    public String getId() {
        return this.f353id;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public PBXMessageContact getFromContact() {
        return this.fromContact;
    }

    public List<PBXMessageContact> getToContacts() {
        return this.toContacts;
    }

    public PBXMessageContact getOwnerContact() {
        return this.ownerContact;
    }

    public int getDirection() {
        return this.direction;
    }

    public String getText() {
        return this.text;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public long getUpdatedTime() {
        return this.updatedTime;
    }

    public List<String> getMediaUrls() {
        return this.mediaUrls;
    }

    public int getReadStatus() {
        return this.readStatus;
    }

    public int getSendStatus() {
        return this.sendStatus;
    }

    public int getSendErrorCode() {
        return this.sendErrorCode;
    }

    public int getRequestStatus() {
        return this.requestStatus;
    }

    public boolean isSentMsg() {
        return this.direction == 1;
    }

    public long getTimestamp() {
        return this.createTime;
    }

    public void setReadStatus(int i) {
        this.readStatus = i;
    }
}
