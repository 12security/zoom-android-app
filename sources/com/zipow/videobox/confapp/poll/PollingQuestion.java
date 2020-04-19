package com.zipow.videobox.confapp.poll;

import androidx.annotation.Nullable;
import com.zipow.videobox.poll.IPollingAnswer;
import com.zipow.videobox.poll.IPollingQuestion;

public class PollingQuestion implements IPollingQuestion {
    private long mNativeHandle = 0;

    private native long getAnswerAtImpl(long j, int i);

    private native long getAnswerByIdImpl(long j, String str);

    private native int getAnswerCountImpl(long j);

    @Nullable
    private native String getQuestionIdImpl(long j);

    @Nullable
    private native String getQuestionTextImpl(long j);

    private native int getQuestionTypeImpl(long j);

    protected PollingQuestion(long j) {
        this.mNativeHandle = j;
    }

    public int getQuestionType() {
        return getQuestionTypeImpl(this.mNativeHandle);
    }

    @Nullable
    public String getQuestionId() {
        return getQuestionIdImpl(this.mNativeHandle);
    }

    @Nullable
    public String getQuestionText() {
        return getQuestionTextImpl(this.mNativeHandle);
    }

    public int getAnswerCount() {
        return getAnswerCountImpl(this.mNativeHandle);
    }

    @Nullable
    public IPollingAnswer getAnswerAt(int i) {
        long answerAtImpl = getAnswerAtImpl(this.mNativeHandle, i);
        if (answerAtImpl == 0) {
            return null;
        }
        return new PollingAnswer(answerAtImpl);
    }

    @Nullable
    public IPollingAnswer getAnswerById(String str) {
        long answerByIdImpl = getAnswerByIdImpl(this.mNativeHandle, str);
        if (answerByIdImpl == 0) {
            return null;
        }
        return new PollingAnswer(answerByIdImpl);
    }
}
