package com.zipow.videobox.poll;

import androidx.annotation.Nullable;

public interface IPollingQuestion {
    @Nullable
    IPollingAnswer getAnswerAt(int i);

    @Nullable
    IPollingAnswer getAnswerById(String str);

    int getAnswerCount();

    @Nullable
    String getQuestionId();

    @Nullable
    String getQuestionText();

    int getQuestionType();
}
