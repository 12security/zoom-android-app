package com.zipow.videobox.confapp.poll;

import androidx.annotation.NonNull;
import com.zipow.videobox.poll.IPollingAnswer;

public class PollingAnswer implements IPollingAnswer {
    private long mNativeHandle = 0;

    @NonNull
    private native String getAnswerIdImpl(long j);

    @NonNull
    private native String getAnswerTextImpl(long j);

    private native int getSelectedCountImpl(long j);

    private native boolean isCheckedImpl(long j);

    private native void setCheckedImpl(long j, boolean z);

    protected PollingAnswer(long j) {
        this.mNativeHandle = j;
    }

    @NonNull
    public String getAnswerId() {
        return getAnswerIdImpl(this.mNativeHandle);
    }

    @NonNull
    public String getAnswerText() {
        return getAnswerTextImpl(this.mNativeHandle);
    }

    public int getSelectedCount() {
        return getSelectedCountImpl(this.mNativeHandle);
    }

    public boolean isChecked() {
        return isCheckedImpl(this.mNativeHandle);
    }

    public void setChecked(boolean z) {
        setCheckedImpl(this.mNativeHandle, z);
    }
}
