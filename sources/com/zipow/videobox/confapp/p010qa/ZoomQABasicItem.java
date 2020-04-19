package com.zipow.videobox.confapp.p010qa;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.confapp.qa.ZoomQABasicItem */
public class ZoomQABasicItem {
    protected long mNativeHandle = 0;

    @Nullable
    private native String getDestJIDImpl(long j);

    @Nullable
    private native String getItemIDImpl(long j);

    @Nullable
    private native String getSenderJIDImpl(long j);

    @Nullable
    private native String getSenderNameImpl(long j);

    private native int getStateImpl(long j);

    @Nullable
    private native String getTextImpl(long j);

    private native long getTimeStampImpl(long j);

    public ZoomQABasicItem(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getItemID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getItemIDImpl(j);
    }

    public int getState() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getStateImpl(j);
    }

    @Nullable
    public String getText() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getTextImpl(j);
    }

    @Nullable
    public String getSenderJID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSenderJIDImpl(j);
    }

    @Nullable
    public String getDestJID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getDestJIDImpl(j);
    }

    public long getTimeStamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTimeStampImpl(j) * 1000;
    }
}
