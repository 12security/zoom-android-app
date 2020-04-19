package com.zipow.videobox.poll;

import androidx.annotation.Nullable;

public interface IPollingDoc {
    int getMyPollingState();

    @Nullable
    String getPollingId();

    @Nullable
    String getPollingName();

    int getPollingState();

    @Nullable
    IPollingQuestion getQuestionAt(int i);

    @Nullable
    IPollingQuestion getQuestionById(String str);

    int getQuestionCount();

    int getTotalVotedUserCount();
}
