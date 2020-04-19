package com.zipow.videobox.confapp;

import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import java.util.ArrayList;
import java.util.List;

public class ZoomRaiseHandInWebinar {
    protected long mNativeHandle = 0;

    @Nullable
    private native long[] getRaisedHandAttendeesImpl(long j);

    private native int getRaisedHandCountImpl(long j);

    private native boolean getRaisedHandStatusImpl(long j, String str);

    private native boolean lowerAllHandImpl(long j);

    private native boolean lowerHandImpl(long j, String str);

    private native boolean raiseHandImpl(long j);

    public ZoomRaiseHandInWebinar(long j) {
        this.mNativeHandle = j;
    }

    public boolean raiseHand() {
        return raiseHandImpl(this.mNativeHandle);
    }

    public boolean lowerHand(String str) {
        return lowerHandImpl(this.mNativeHandle, str);
    }

    public boolean lowerAllHand() {
        return lowerAllHandImpl(this.mNativeHandle);
    }

    public int getRaisedHandCount() {
        return getRaisedHandCountImpl(this.mNativeHandle);
    }

    public boolean getRaisedHandStatus(String str) {
        return getRaisedHandStatusImpl(this.mNativeHandle, str);
    }

    @Nullable
    public List<ZoomQABuddy> getRaisedHandAttendees() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long[] raisedHandAttendeesImpl = getRaisedHandAttendeesImpl(j);
        if (raisedHandAttendeesImpl == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (long valueOf : raisedHandAttendeesImpl) {
            arrayList.add(new ZoomQABuddy(Long.valueOf(valueOf).longValue()));
        }
        return arrayList;
    }
}
