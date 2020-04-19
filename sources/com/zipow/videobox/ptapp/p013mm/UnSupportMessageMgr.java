package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMCallbackUI;
import java.util.List;

/* renamed from: com.zipow.videobox.ptapp.mm.UnSupportMessageMgr */
public class UnSupportMessageMgr {
    private long mNativeHandle;

    @Nullable
    private native String SearchUnSupportMessageImpl(long j, String str, String str2);

    @Nullable
    private native String SearchUnSupportMessagesImpl(long j, String str, List<String> list);

    private native void setMsgUIImpl(long j, long j2);

    public UnSupportMessageMgr(long j) {
        this.mNativeHandle = j;
    }

    public void setMsgUI(@Nullable IMCallbackUI iMCallbackUI) {
        long j = this.mNativeHandle;
        if (j != 0 && iMCallbackUI != null) {
            setMsgUIImpl(j, iMCallbackUI.getUnSupportHandle());
        }
    }

    @Nullable
    public String searchUnSupportMessage(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return SearchUnSupportMessageImpl(j, str, str2);
    }

    @Nullable
    public String searchUnSupportMessages(String str, List<String> list) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return SearchUnSupportMessagesImpl(j, str, list);
    }
}
