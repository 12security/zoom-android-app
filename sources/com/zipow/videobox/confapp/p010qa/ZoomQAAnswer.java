package com.zipow.videobox.confapp.p010qa;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.confapp.qa.ZoomQAAnswer */
public class ZoomQAAnswer extends ZoomQABasicItem {
    @Nullable
    private native String getQuestionIDImpl(long j);

    private native boolean isLiveAnswerImpl(long j);

    private native boolean isPrivateImpl(long j);

    public ZoomQAAnswer(long j) {
        super(j);
    }

    @Nullable
    public String getQuestionID() {
        if (this.mNativeHandle == 0) {
            return null;
        }
        return getQuestionIDImpl(this.mNativeHandle);
    }

    public boolean isPrivate() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return isPrivateImpl(this.mNativeHandle);
    }

    public boolean isLiveAnswer() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return isLiveAnswerImpl(this.mNativeHandle);
    }
}
