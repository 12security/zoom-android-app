package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;

@Deprecated
/* renamed from: com.zipow.videobox.ptapp.mm.ZoomNewFriendData */
public class ZoomNewFriendData {
    private static final String TAG = "ZoomNewFriendData";
    private long mNativeHandle = 0;

    private native long getPendingRequestAtImpl(long j, int i, SubscribeRequestInfo subscribeRequestInfo);

    private native int getPendingRequestCountImpl(long j);

    public ZoomNewFriendData(long j) {
        this.mNativeHandle = j;
    }

    public int getPendingRequestCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getPendingRequestCountImpl(j);
    }

    @Nullable
    public ZoomBuddy getPendingRequestAt(int i, SubscribeRequestInfo subscribeRequestInfo) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long pendingRequestAtImpl = getPendingRequestAtImpl(j, i, subscribeRequestInfo);
        if (pendingRequestAtImpl == 0) {
            return null;
        }
        return new ZoomBuddy(pendingRequestAtImpl);
    }
}
