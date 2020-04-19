package com.zipow.videobox.confapp.poll;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.poll.PollingUI.IPollingUIListener;
import com.zipow.videobox.confapp.poll.PollingUI.SimplePollingUIListener;
import com.zipow.videobox.poll.AbsPollingMgr;
import com.zipow.videobox.poll.IPollingDoc;
import com.zipow.videobox.poll.PollingRole;

public class PollingMgr extends AbsPollingMgr {
    private long mNativeHandle = 0;
    @NonNull
    private IPollingUIListener mUIListener = new SimplePollingUIListener() {
        public void onPollingActionResult(int i, String str, int i2) {
            PollingMgr.this.processPollingActionResult(i, str, i2);
        }

        public void onPollingStatusChanged(int i, String str) {
            PollingMgr.this.processPollingStatusChanged(i, str);
        }
    };

    private native long getPollingAtIdxImpl(long j, int i);

    private native int getPollingCountImpl(long j);

    private native long getPollingDocByIdImpl(long j, String str);

    private native boolean isAttendeeofPollingImpl(long j);

    private native boolean isHostofPollingImpl(long j);

    private native boolean isPanelistofPollingImpl(long j);

    private native void setPollingUIImpl(long j, long j2);

    private native boolean submitPollImpl(long j, String str);

    public PollingMgr(long j) {
        this.mNativeHandle = j;
    }

    public void setPollingUI(@Nullable PollingUI pollingUI) {
        long j = this.mNativeHandle;
        if (j != 0 && pollingUI != null) {
            setPollingUIImpl(j, pollingUI.getNativeHandle());
            pollingUI.addListener(this.mUIListener);
        }
    }

    public int getPollingCount() {
        return getPollingCountImpl(this.mNativeHandle);
    }

    @Nullable
    public IPollingDoc getPollingAtIdx(int i) {
        if (i < 0) {
            return null;
        }
        long pollingAtIdxImpl = getPollingAtIdxImpl(this.mNativeHandle, i);
        if (pollingAtIdxImpl == 0) {
            return null;
        }
        return new PollingDoc(pollingAtIdxImpl);
    }

    @Nullable
    public IPollingDoc getPollingDocById(@Nullable String str) {
        if (str == null) {
            return null;
        }
        long pollingDocByIdImpl = getPollingDocByIdImpl(this.mNativeHandle, str);
        if (pollingDocByIdImpl == 0) {
            return null;
        }
        return new PollingDoc(pollingDocByIdImpl);
    }

    public boolean submitPoll(@Nullable String str) {
        if (str == null) {
            return false;
        }
        return submitPollImpl(this.mNativeHandle, str);
    }

    @NonNull
    public PollingRole getPollingRole() {
        if (isAttendeeofPolling()) {
            return PollingRole.Attendee;
        }
        if (isHostofPolling()) {
            return PollingRole.Host;
        }
        return PollingRole.Panelist;
    }

    /* access modifiers changed from: private */
    public void processPollingActionResult(int i, String str, int i2) {
        if (i == 3) {
            notifySubmitResult(str, i2);
        }
    }

    /* access modifiers changed from: private */
    public void processPollingStatusChanged(int i, String str) {
        notifyPollingStatusChanged(str, i);
    }

    public boolean isHostofPolling() {
        return isHostofPollingImpl(this.mNativeHandle);
    }

    public boolean isPanelistofPolling() {
        return isPanelistofPollingImpl(this.mNativeHandle);
    }

    public boolean isAttendeeofPolling() {
        return isAttendeeofPollingImpl(this.mNativeHandle);
    }
}
