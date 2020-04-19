package com.zipow.videobox.confapp.component.sink.share;

public interface IShareSink {
    void onAnnotateShutDown();

    void onAnnotateStartedUp(boolean z, long j);

    void onAnnotateViewSizeChanged();

    void onUserGetRemoteControlPrivilege(long j);

    void onWBPageChanged(int i, int i2, int i3, int i4);

    void remoteControlStarted(long j);

    void sinkShareActiveUser(long j);

    void sinkShareDataSizeChanged(long j);

    void sinkShareUserReceivingStatus(long j);

    void sinkShareUserSendingStatus(long j);
}
