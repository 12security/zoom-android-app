package com.zipow.videobox.confapp.poll;

import androidx.annotation.Nullable;
import com.zipow.videobox.poll.IPollingDoc;
import com.zipow.videobox.poll.IPollingQuestion;

public class PollingDoc implements IPollingDoc {
    private long mNativeHandle = 0;

    private native int getMyPollingStateImpl(long j);

    @Nullable
    private native String getPollingIdImpl(long j);

    @Nullable
    private native String getPollingNameImpl(long j);

    private native int getPollingStateImpl(long j);

    private native long getQuestionAtImpl(long j, int i);

    private native long getQuestionByIdImpl(long j, String str);

    private native int getQuestionCountImpl(long j);

    private native int getTotalVotedUserCountImpl(long j);

    protected PollingDoc(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getPollingId() {
        return getPollingIdImpl(this.mNativeHandle);
    }

    public int getPollingState() {
        return getPollingStateImpl(this.mNativeHandle);
    }

    public int getMyPollingState() {
        return getMyPollingStateImpl(this.mNativeHandle);
    }

    @Nullable
    public String getPollingName() {
        return getPollingNameImpl(this.mNativeHandle);
    }

    public int getTotalVotedUserCount() {
        return getTotalVotedUserCountImpl(this.mNativeHandle);
    }

    public int getQuestionCount() {
        return getQuestionCountImpl(this.mNativeHandle);
    }

    @Nullable
    public IPollingQuestion getQuestionAt(int i) {
        long questionAtImpl = getQuestionAtImpl(this.mNativeHandle, i);
        if (questionAtImpl == 0) {
            return null;
        }
        return new PollingQuestion(questionAtImpl);
    }

    @Nullable
    public IPollingQuestion getQuestionById(String str) {
        long questionByIdImpl = getQuestionByIdImpl(this.mNativeHandle, str);
        if (questionByIdImpl == 0) {
            return null;
        }
        return new PollingQuestion(questionByIdImpl);
    }
}
