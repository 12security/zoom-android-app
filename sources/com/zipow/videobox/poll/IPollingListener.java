package com.zipow.videobox.poll;

import p021us.zoom.androidlib.util.IListener;

public interface IPollingListener extends IListener {
    void onPollingStatusChanged(String str, int i);

    void onPollingSubmitResult(String str, int i);
}
