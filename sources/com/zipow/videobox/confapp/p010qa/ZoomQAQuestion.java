package com.zipow.videobox.confapp.p010qa;

import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.confapp.qa.ZoomQAQuestion */
public class ZoomQAQuestion extends ZoomQABasicItem {
    private native boolean amILiveAnsweringImpl(long j);

    private native long getAnswerAtImpl(long j, int i);

    private native int getAnswerCountImpl(long j);

    private native int getLiveAnsweringCountImpl(long j);

    @Nullable
    private native String getLiveAnsweringJIDAtImpl(long j, int i);

    private native long getMostRecentTimeImpl(long j);

    private native int getUpvoteNumImpl(long j);

    private native boolean hasLiveAnswersImpl(long j);

    private native boolean hasTextAnswersImpl(long j);

    private native boolean isAnonymousImpl(long j);

    private native boolean isMarkedAsAnsweredImpl(long j);

    private native boolean isMarkedAsDismissedImpl(long j);

    private native boolean isMySelfUpvotedImpl(long j);

    public ZoomQAQuestion(long j) {
        super(j);
    }

    public int getAnswerCount() {
        if (this.mNativeHandle == 0) {
            return 0;
        }
        return getAnswerCountImpl(this.mNativeHandle);
    }

    @Nullable
    public ZoomQAAnswer getAnswerAt(int i) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        long answerAtImpl = getAnswerAtImpl(this.mNativeHandle, i);
        if (answerAtImpl == 0) {
            return null;
        }
        return new ZoomQAAnswer(answerAtImpl);
    }

    public boolean isMarkedAsAnswered() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return isMarkedAsAnsweredImpl(this.mNativeHandle);
    }

    public long getMostRecentTime() {
        if (this.mNativeHandle == 0) {
            return 0;
        }
        return getMostRecentTimeImpl(this.mNativeHandle);
    }

    public boolean isAnonymous() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return isAnonymousImpl(this.mNativeHandle);
    }

    public int getLiveAnsweringCount() {
        if (this.mNativeHandle == 0) {
            return 0;
        }
        return getLiveAnsweringCountImpl(this.mNativeHandle);
    }

    @Nullable
    public String getLiveAnsweringJIDAt(int i) {
        if (this.mNativeHandle == 0) {
            return null;
        }
        String liveAnsweringJIDAtImpl = getLiveAnsweringJIDAtImpl(this.mNativeHandle, i);
        if (StringUtil.isEmptyOrNull(liveAnsweringJIDAtImpl)) {
            return null;
        }
        return liveAnsweringJIDAtImpl;
    }

    public boolean isMarkedAsDismissed() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return isMarkedAsDismissedImpl(this.mNativeHandle);
    }

    public int getUpvoteNum() {
        if (this.mNativeHandle == 0) {
            return 0;
        }
        return getUpvoteNumImpl(this.mNativeHandle);
    }

    public boolean hasLiveAnswers() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return hasLiveAnswersImpl(this.mNativeHandle);
    }

    public boolean hasTextAnswers() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return hasTextAnswersImpl(this.mNativeHandle);
    }

    public boolean isMySelfUpvoted() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return isMySelfUpvotedImpl(this.mNativeHandle);
    }

    public boolean amILiveAnswering() {
        if (this.mNativeHandle == 0) {
            return false;
        }
        return amILiveAnsweringImpl(this.mNativeHandle);
    }
}
