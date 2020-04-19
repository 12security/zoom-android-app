package com.zipow.videobox.confapp.p010qa;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.confapp.qa.ZoomQABuddy */
public class ZoomQABuddy {
    protected long mNativeHandle = 0;

    @Nullable
    private native String getEmailImpl(long j);

    @Nullable
    private native String getJIDImpl(long j);

    @Nullable
    private native String getNameImpl(long j);

    private native long getNodeIDImpl(long j);

    private native long getRaiseHandTimestampImpl(long j);

    private native boolean getRaisedHandStatusImpl(long j);

    private native int getRoleImpl(long j);

    private native boolean isAttendeeCanTalkImpl(long j);

    private native boolean isAttendeeSupportTemporarilyFeatureImpl(long j);

    private native boolean isGuestImpl(long j);

    private native boolean isInAttentionModeImpl(long j);

    private native boolean isOfflineUserImpl(long j);

    private native boolean isTelephoneImpl(long j);

    public ZoomQABuddy(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getJID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getJIDImpl(j);
    }

    @Nullable
    public String getName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getNameImpl(j);
    }

    public long getNodeID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getNodeIDImpl(j);
    }

    public int getRole() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRoleImpl(j);
    }

    public boolean getRaiseHandStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return getRaisedHandStatusImpl(j);
    }

    public boolean isOfflineUser() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isOfflineUserImpl(j);
    }

    public boolean isInAttentionMode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInAttentionModeImpl(j);
    }

    @Nullable
    public String getEmail() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getEmailImpl(j);
    }

    public boolean isAttendeeSupportTemporarilyFeature() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAttendeeSupportTemporarilyFeatureImpl(j);
    }

    public boolean isAttendeeCanTalk() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAttendeeCanTalkImpl(j);
    }

    public boolean isGuest() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isGuestImpl(j);
    }

    public long getRaiseHandTimestamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRaiseHandTimestampImpl(j);
    }

    public boolean isTelephone() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isTelephoneImpl(j);
    }
}
