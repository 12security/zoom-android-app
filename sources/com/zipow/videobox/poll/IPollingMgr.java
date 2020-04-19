package com.zipow.videobox.poll;

import androidx.annotation.Nullable;

public interface IPollingMgr {
    void addListener(IPollingListener iPollingListener);

    @Nullable
    IPollingDoc getPollingAtIdx(int i);

    int getPollingCount();

    @Nullable
    IPollingDoc getPollingDocById(String str);

    PollingRole getPollingRole();

    void removeListener(IPollingListener iPollingListener);

    boolean submitPoll(String str);
}
