package com.zipow.videobox.confapp;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import org.apache.http.message.TokenParser;

public class ConfChatMessage {
    private long mNativeHandle = 0;

    @Nullable
    private native String getMessageContentImpl(long j);

    @Nullable
    private native String getMessageIDImpl(long j);

    private native int getMsgTypeImpl(long j);

    @Nullable
    private native String getReceiverDisplayNameImpl(long j);

    private native long getReceiverIDImpl(long j);

    @Nullable
    private native String getRecieverJidImpl(long j);

    @Nullable
    private native String getSenderDisplayNameImpl(long j);

    private native long getSenderIDImpl(long j);

    @Nullable
    private native String getSenderJidImpl(long j);

    private native long getTimeStampImpl(long j);

    private native boolean isSelfSendImpl(long j);

    private native boolean isXMPPMsgImpl(long j);

    public ConfChatMessage(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getMessageID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMessageIDImpl(j);
    }

    @Nullable
    public String getMessageContent() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String messageContentImpl = getMessageContentImpl(j);
        if (TextUtils.isEmpty(messageContentImpl)) {
            return messageContentImpl;
        }
        return messageContentImpl.replace("\r\n", FontStyleHelper.SPLITOR).replace(TokenParser.f495CR, 10);
    }

    public boolean isSelfSend() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSelfSendImpl(j);
    }

    public int getMsgType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getMsgTypeImpl(j);
    }

    public long getSenderID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSenderIDImpl(j);
    }

    public long getReceiverID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getReceiverIDImpl(j);
    }

    @Nullable
    public String getSenderDisplayName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSenderDisplayNameImpl(j);
    }

    @Nullable
    public String getReceiverDisplayName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getReceiverDisplayNameImpl(j);
    }

    public long getTimeStamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTimeStampImpl(j);
    }

    @Nullable
    public String getSenderJid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSenderJidImpl(j);
    }

    @Nullable
    public String getRecieverJid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRecieverJidImpl(j);
    }
}
