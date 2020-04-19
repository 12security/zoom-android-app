package com.zipow.videobox.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.widget.ListView;
import androidx.annotation.NonNull;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;
import com.zipow.videobox.ptapp.IMSession;
import com.zipow.videobox.ptapp.PTApp;

public class IMMessageListView extends ListView {
    private IMMessageListAdapter mAdapter;
    private String mBuddyJid;
    private String mBuddyName;
    private String mMyName;
    private long mPrevTime = 0;

    public IMMessageListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public IMMessageListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public IMMessageListView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        this.mAdapter = new IMMessageListAdapter(getContext());
        if (isInEditMode()) {
            _editmode_loadAllMessageItems(this.mAdapter);
        }
        setAdapter(this.mAdapter);
    }

    private void _editmode_loadAllMessageItems(IMMessageListAdapter iMMessageListAdapter) {
        IMMessageItem iMMessageItem = new IMMessageItem();
        iMMessageItem.messageTime = System.currentTimeMillis();
        iMMessageItem.messageType = 10;
        iMMessageListAdapter.addItem(iMMessageItem);
        for (int i = 0; i < 5; i++) {
            IMMessageItem iMMessageItem2 = new IMMessageItem();
            int i2 = i % 2;
            iMMessageItem2.fromScreenName = i2 == 0 ? "Zoom" : "Reed Yang";
            iMMessageItem2.message = "Hi, Zoom! I like you!";
            iMMessageItem2.messageTime = System.currentTimeMillis();
            iMMessageItem2.messageType = i2 == 0 ? 0 : 1;
            iMMessageListAdapter.addItem(iMMessageItem2);
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        final int lastVisiblePosition = getLastVisiblePosition();
        super.onLayout(z, i, i2, i3, i4);
        if (z && lastVisiblePosition >= 0) {
            post(new Runnable() {
                public void run() {
                    IMMessageListView.this.setSelection(lastVisiblePosition);
                }
            });
        }
    }

    public void reloadMessages(String str, String str2, String str3) {
        this.mBuddyJid = str;
        this.mBuddyName = str2;
        this.mMyName = str3;
        if (this.mBuddyJid != null && this.mBuddyName != null && this.mMyName != null) {
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            if (iMHelper != null) {
                IMSession sessionBySessionName = iMHelper.getSessionBySessionName(this.mBuddyJid);
                if (sessionBySessionName != null) {
                    this.mAdapter.clear();
                    this.mPrevTime = 0;
                    for (int i = 0; i < sessionBySessionName.getIMMessageCount(); i++) {
                        IMMessage iMMessageByIndex = sessionBySessionName.getIMMessageByIndex(i);
                        if (iMMessageByIndex != null) {
                            iMHelper.setIMMessageUnread(iMMessageByIndex, false);
                            IMMessageItem protoMessageToMessageItem = protoMessageToMessageItem(iMMessageByIndex);
                            if (protoMessageToMessageItem != null) {
                                long j = protoMessageToMessageItem.messageTime;
                                if (j - this.mPrevTime > 300000) {
                                    IMMessageItem iMMessageItem = new IMMessageItem();
                                    iMMessageItem.messageTime = j;
                                    iMMessageItem.messageType = 10;
                                    this.mAdapter.addItem(iMMessageItem);
                                    this.mPrevTime = iMMessageItem.messageTime;
                                }
                                this.mAdapter.addItem(protoMessageToMessageItem);
                            }
                        }
                    }
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public IMMessageItem protoMessageToMessageItem(@NonNull IMMessage iMMessage) {
        IMMessageItem iMMessageItem = new IMMessageItem();
        iMMessageItem.fromJid = iMMessage.getFromScreenName();
        iMMessageItem.toJid = iMMessage.getToScreenName();
        iMMessageItem.messageTime = iMMessage.getMessageTime() * 1000;
        iMMessageItem.nativeHandle = iMMessage.getNativeHandle();
        if (this.mBuddyJid.equals(iMMessage.getFromScreenName())) {
            iMMessageItem.fromScreenName = this.mBuddyName;
            iMMessageItem.toScreenName = this.mMyName;
        } else {
            iMMessageItem.fromScreenName = this.mMyName;
            iMMessageItem.toScreenName = this.mBuddyName;
        }
        iMMessageItem.messageType = protoMessageType2MessageItemType(iMMessage);
        iMMessageItem.message = iMMessage.getMessage();
        return iMMessageItem;
    }

    private int protoMessageType2MessageItemType(IMMessage iMMessage) {
        switch (iMMessage.getMessageType()) {
            case 0:
                if (this.mBuddyJid.equals(iMMessage.getFromScreenName())) {
                    return 0;
                }
                return 1;
            case 1:
                return this.mBuddyJid.equals(iMMessage.getFromScreenName()) ? 2 : 3;
            case 2:
                return this.mBuddyJid.equals(iMMessage.getFromScreenName()) ? 4 : 5;
            case 3:
                return this.mBuddyJid.equals(iMMessage.getFromScreenName()) ? 6 : 7;
            case 4:
                return this.mBuddyJid.equals(iMMessage.getFromScreenName()) ? 8 : 9;
            default:
                return 0;
        }
    }

    public void onIMReceived(@NonNull IMMessage iMMessage, boolean z) {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null && z) {
            iMHelper.setIMMessageUnread(iMMessage, false);
        }
        IMMessageItem protoMessageToMessageItem = protoMessageToMessageItem(iMMessage);
        if (protoMessageToMessageItem != null) {
            if (protoMessageToMessageItem.messageTime - this.mPrevTime > 300000) {
                IMMessageItem iMMessageItem = new IMMessageItem();
                iMMessageItem.messageTime = protoMessageToMessageItem.messageTime;
                iMMessageItem.messageType = 10;
                this.mAdapter.addItem(iMMessageItem);
                this.mPrevTime = protoMessageToMessageItem.messageTime;
            }
            this.mAdapter.addItem(protoMessageToMessageItem);
            this.mAdapter.notifyDataSetChanged();
            scrollToBottom(false);
        }
    }

    public void scrollToBottom(boolean z) {
        int lastVisiblePosition = getLastVisiblePosition();
        int count = getCount() - 1;
        if (z) {
            setSelection(count);
        } else if (count - lastVisiblePosition >= 5) {
        } else {
            if (VERSION.SDK_INT >= 16) {
                smoothScrollToPosition(count);
            } else {
                setSelection(count);
            }
        }
    }
}
