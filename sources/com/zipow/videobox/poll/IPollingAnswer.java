package com.zipow.videobox.poll;

import androidx.annotation.Nullable;

public interface IPollingAnswer {
    @Nullable
    String getAnswerId();

    @Nullable
    String getAnswerText();

    int getSelectedCount();

    boolean isChecked();

    void setChecked(boolean z);
}
