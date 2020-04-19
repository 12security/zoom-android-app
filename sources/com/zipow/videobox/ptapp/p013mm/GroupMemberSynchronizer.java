package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMCallbackUI;

/* renamed from: com.zipow.videobox.ptapp.mm.GroupMemberSynchronizer */
public class GroupMemberSynchronizer {
    private long mNativeHandle;

    private native boolean needReadGroupMemberFromDBImpl(long j, String str);

    private native boolean needSyncGroupMemberFromXmppImpl(long j, String str);

    private native void registerUICallbackImpl(long j, long j2);

    private native boolean startAsynReadGroupMemberFromDBImpl(long j, String str);

    private native boolean startAsyncGroupMemberFromXmppImpl(long j, String str);

    private native boolean syncReadGroupMemberFromDBImpl(long j, String str);

    public GroupMemberSynchronizer(long j) {
        this.mNativeHandle = j;
    }

    public void registerUICallback(@Nullable IMCallbackUI iMCallbackUI) {
        long j = this.mNativeHandle;
        if (j != 0 && iMCallbackUI != null) {
            registerUICallbackImpl(j, iMCallbackUI.getAsynReadGroupMemberHandle());
        }
    }

    public boolean startAsynReadGroupMemberFromDB(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return startAsynReadGroupMemberFromDBImpl(j, str);
    }

    public boolean syncReadGroupMemberFromDB(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return syncReadGroupMemberFromDBImpl(j, str);
    }

    public boolean startAsyncGroupMemberFromXmpp(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return startAsyncGroupMemberFromXmppImpl(j, str);
    }

    public boolean needReadGroupMemberFromDB(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return needReadGroupMemberFromDBImpl(j, str);
    }

    public boolean needSyncGroupMemberFromXmpp(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return needSyncGroupMemberFromXmppImpl(j, str);
    }

    public boolean safeSyncGroupMemberFromXmpp(String str) {
        if (this.mNativeHandle != 0 && needSyncGroupMemberFromXmpp(str)) {
            return startAsyncGroupMemberFromXmpp(str);
        }
        return false;
    }
}
