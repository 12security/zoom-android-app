package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;

public class CmmAttentionTrackMgr {
    private long mNativeHandle = 0;

    private native boolean changeMyAttentionStatusImpl(long j, boolean z);

    private native boolean enableConfAttentionTrackImpl(long j, boolean z);

    private native boolean isConfAttentionTrackEnabledImpl(long j);

    private native boolean isWebAttentionTrackEnabledImpl(long j);

    private native void setEventSinkImpl(long j, long j2);

    public CmmAttentionTrackMgr(long j) {
        this.mNativeHandle = j;
    }

    public boolean isWebAttentionTrackEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isWebAttentionTrackEnabledImpl(j);
    }

    public boolean isConfAttentionTrackEnabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isConfAttentionTrackEnabledImpl(j);
    }

    public boolean changeMyAttentionStatus(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return changeMyAttentionStatusImpl(j, z);
    }

    public boolean enableConfAttentionTrack(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return enableConfAttentionTrackImpl(j, z);
    }

    public void setEventSink(@Nullable AttentionTrackEventSinkUI attentionTrackEventSinkUI) {
        if (attentionTrackEventSinkUI != null) {
            setEventSinkImpl(this.mNativeHandle, attentionTrackEventSinkUI.getNativeHandle());
        }
    }
}
