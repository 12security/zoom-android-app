package com.zipow.videobox.confapp.component.sink.video;

public interface IVideoUIInteractionSink extends IConfCamera {
    void sinkInClickBtnVideo();

    void sinkInFeccUserApproved(long j);

    void sinkInMuteVideo(boolean z);

    void sinkInOrientationChanged();

    void sinkInRefreshFeccUI();

    void sinkInResumeVideo();

    void sinkInStopVideo();
}
