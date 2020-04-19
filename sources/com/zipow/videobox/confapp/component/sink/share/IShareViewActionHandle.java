package com.zipow.videobox.confapp.component.sink.share;

public interface IShareViewActionHandle {
    void onAnnotateShutDown();

    void onAnnotateStartedUp(boolean z, long j);

    void pause();

    void resume();

    void start();

    void stop();
}
