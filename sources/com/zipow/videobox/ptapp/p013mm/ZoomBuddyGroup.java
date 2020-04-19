package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomBuddyGroup */
public class ZoomBuddyGroup {
    private long mNativeHandle = 0;

    private native boolean canEditImpl(long j);

    private native boolean containsBuddyImpl(long j, String str);

    private native long getBuddyAtImpl(long j, int i);

    private native int getBuddyCountImpl(long j);

    @Nullable
    private native String getBuddyJidAtImpl(long j, int i);

    private native int getGroupTypeImpl(long j);

    @Nullable
    private native String getIDImpl(long j);

    @Nullable
    private native String getNameImpl(long j);

    @Nullable
    private native String getXmppGroupIDImpl(long j);

    private native boolean hasBuddyImpl(long j, String str);

    private native boolean isDirectoryGroupImpl(long j);

    private native boolean isZoomRoomGroupImpl(long j);

    public ZoomBuddyGroup(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIDImpl(j);
    }

    @Nullable
    public String getName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getNameImpl(j);
    }

    public int getBuddyCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyCountImpl(j);
    }

    @Nullable
    public ZoomBuddy getBuddyAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyAtImpl = getBuddyAtImpl(j, i);
        if (buddyAtImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyAtImpl);
    }

    @Nullable
    public String getBuddyJidAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getBuddyJidAtImpl(j, i);
    }

    public int getGroupType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getGroupTypeImpl(j);
    }

    public boolean canEdit() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canEditImpl(j);
    }

    @Nullable
    public String getXmppGroupID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getXmppGroupIDImpl(j);
    }

    public boolean hasBuddy(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasBuddyImpl(j, str);
    }

    public boolean isDirectoryGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDirectoryGroupImpl(j);
    }

    public boolean isZoomRoomGroup() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isZoomRoomGroupImpl(j);
    }

    public boolean containsBuddy(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return containsBuddyImpl(j, str);
    }
}
