package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;

public class IMSession {
    private static final String TAG = "IMSession";
    private long mNativeHandle = 0;

    @Nullable
    private native byte[] getIMMessageByIndexImpl(long j, int i);

    private native int getIMMessageCountImpl(long j);

    @Nullable
    private native String getSessionNameImpl(long j);

    private native int getUnreadMessageCountImpl(long j);

    public IMSession(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getSessionName() {
        return getSessionNameImpl(this.mNativeHandle);
    }

    public int getIMMessageCount() {
        return getIMMessageCountImpl(this.mNativeHandle);
    }

    @Nullable
    public IMMessage getIMMessageByIndex(int i) {
        byte[] iMMessageByIndexImpl = getIMMessageByIndexImpl(this.mNativeHandle, i);
        IMMessage iMMessage = null;
        if (iMMessageByIndexImpl == null) {
            return null;
        }
        try {
            iMMessage = IMMessage.parseFrom(iMMessageByIndexImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return iMMessage;
    }

    public int getUnreadMessageCount() {
        return getUnreadMessageCountImpl(this.mNativeHandle);
    }
}
