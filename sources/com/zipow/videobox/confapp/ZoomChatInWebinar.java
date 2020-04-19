package com.zipow.videobox.confapp;

import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import java.util.ArrayList;
import java.util.List;

public class ZoomChatInWebinar {
    private long mNativeHandle = 0;

    private native int getChattedAttendeeCountImpl(long j);

    @Nullable
    private native long[] getChattedAttendeesImpl(long j);

    private native boolean sendWebinarChatToAllPanelistsImpl(long j, String str);

    private native boolean sendWebinarChatToIndividualImpl(long j, String str, String str2, boolean z);

    public ZoomChatInWebinar(long j) {
        this.mNativeHandle = j;
    }

    public boolean sendWebinarChatToAllPanelists(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return sendWebinarChatToAllPanelistsImpl(j, str);
    }

    public boolean sendWebinarChatToIndividual(String str, String str2, boolean z) {
        if (this.mNativeHandle == 0 || TextUtils.isEmpty(str2)) {
            return false;
        }
        return sendWebinarChatToIndividualImpl(this.mNativeHandle, str, str2, z);
    }

    public int getChattedAttendeeCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getChattedAttendeeCountImpl(j);
    }

    @Nullable
    public List<ZoomQABuddy> getChattedAttendees() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long[] chattedAttendeesImpl = getChattedAttendeesImpl(j);
        if (chattedAttendeesImpl == null || chattedAttendeesImpl.length == 0) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (long zoomQABuddy : chattedAttendeesImpl) {
            arrayList.add(new ZoomQABuddy(zoomQABuddy));
        }
        return arrayList;
    }
}
