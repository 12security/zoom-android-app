package com.zipow.videobox.ptapp;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.IMMessage;

public class IMHelper {
    private static final String TAG = "IMHelper";
    private int mLocalStatus = 0;
    private long mNativeHandle = 0;

    private native boolean acknowledgeSubscriptionImpl(long j, String str, boolean z);

    private native boolean cancelSubscriptionImpl(long j, String str);

    @Nullable
    private native String getJIDMyselfImpl(long j);

    private native long getSessionBySessionNameImpl(long j, String str);

    @Nullable
    private native IMSubscription[] getUnhandledSubscriptionsImpl(long j);

    private native boolean isIMDisconnectedImpl(long j);

    private native boolean isIMLoggingInImpl(long j);

    private native boolean isIMSignedOnImpl(long j);

    private native int sendIMMessageImpl(long j, String str, String str2, boolean z);

    private native void setIMMessageUnreadImpl(long j, long j2, boolean z);

    private native boolean subscribeBuddyImpl(long j, String str);

    private native boolean unsubscribeBuddyImpl(long j, String str);

    public IMHelper(long j) {
        this.mNativeHandle = j;
    }

    public boolean isIMSignedOn() {
        return isIMSignedOnImpl(this.mNativeHandle);
    }

    public int sendIMMessage(String str, String str2, boolean z) {
        return sendIMMessageImpl(this.mNativeHandle, str, str2, z);
    }

    @Nullable
    public String getJIDMyself() {
        return getJIDMyselfImpl(this.mNativeHandle);
    }

    @Nullable
    public IMSession getSessionBySessionName(String str) {
        long sessionBySessionNameImpl = getSessionBySessionNameImpl(this.mNativeHandle, str);
        if (sessionBySessionNameImpl != 0) {
            return new IMSession(sessionBySessionNameImpl);
        }
        return null;
    }

    public void setIMMessageUnread(@Nullable IMMessage iMMessage, boolean z) {
        if (iMMessage != null) {
            setIMMessageUnreadImpl(this.mNativeHandle, iMMessage.getNativeHandle(), z);
        }
    }

    public boolean isIMLoggingIn() {
        return isIMLoggingInImpl(this.mNativeHandle);
    }

    public boolean isIMDisconnected() {
        return isIMDisconnectedImpl(this.mNativeHandle);
    }

    public int getUnreadMsgCount() {
        PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
        if (buddyHelper == null) {
            return 0;
        }
        int buddyItemCount = buddyHelper.getBuddyItemCount();
        int i = 0;
        for (int i2 = 0; i2 < buddyItemCount; i2++) {
            IMSession sessionBySessionName = getSessionBySessionName(buddyHelper.getBuddyItemJid(i2));
            if (sessionBySessionName != null) {
                i += sessionBySessionName.getUnreadMessageCount();
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public void setIMLocalStatus(int i) {
        this.mLocalStatus = i;
    }

    public int getIMLocalStatus() {
        return this.mLocalStatus;
    }

    public boolean subscribeBuddy(String str) {
        return subscribeBuddyImpl(this.mNativeHandle, str);
    }

    public boolean unsubscribeBuddy(String str) {
        return unsubscribeBuddyImpl(this.mNativeHandle, str);
    }

    public boolean acknowledgeSubscription(String str, boolean z) {
        return acknowledgeSubscriptionImpl(this.mNativeHandle, str, z);
    }

    public boolean cancelSubscription(String str) {
        return cancelSubscriptionImpl(this.mNativeHandle, str);
    }

    @Nullable
    public IMSubscription[] getUnhandledSubscriptions() {
        return getUnhandledSubscriptionsImpl(this.mNativeHandle);
    }
}
