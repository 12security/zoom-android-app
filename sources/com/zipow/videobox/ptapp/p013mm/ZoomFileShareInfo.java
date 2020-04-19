package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomFileShareInfo */
public class ZoomFileShareInfo {
    private long mNativeHandle = 0;

    private native long getShareActionCountImpl(long j);

    private native long getShareActionImpl(long j, long j2);

    public ZoomFileShareInfo(long j) {
        this.mNativeHandle = j;
    }

    public long getShareActionCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getShareActionCountImpl(j);
    }

    @Nullable
    public ZoomShareAction getShareAction(long j) {
        long j2 = this.mNativeHandle;
        if (j2 == 0) {
            return null;
        }
        long shareActionImpl = getShareActionImpl(j2, j);
        if (shareActionImpl == 0) {
            return null;
        }
        return new ZoomShareAction(shareActionImpl);
    }
}
