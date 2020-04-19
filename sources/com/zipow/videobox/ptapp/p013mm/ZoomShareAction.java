package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomShareAction */
public class ZoomShareAction {
    private long mNativeHandle = 0;

    private native long getShareTimeImpl(long j);

    @Nullable
    private native String getShareeImpl(long j);

    @Nullable
    private native String getWebFileIDImpl(long j);

    public ZoomShareAction(long j) {
        this.mNativeHandle = j;
    }

    public long getShareTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getShareTimeImpl(j);
    }

    @Nullable
    public String getSharee() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getShareeImpl(j);
    }

    @Nullable
    public String getWebFileID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getWebFileIDImpl(j);
    }
}
