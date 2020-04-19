package com.zipow.videobox.confapp.component.sink.video;

public interface IVideoSink {
    void sinkAutoStartVideo(long j);

    void sinkBandwidthLimitStatusChanged();

    void sinkCompanionModeChanged();

    void sinkConfVideoSendingStatusChanged();

    void sinkInControlCameraTypeChanged(long j);

    boolean sinkInDeviceStatusChanged(int i, int i2);

    void sinkLeaderShipModeChanged();

    void sinkMyShareStatueChanged(boolean z);

    void sinkReceiveVideoPrivilegeChanged();

    void sinkSendVideoPrivilegeChanged();

    void sinkUserActiveVideo(long j);

    void sinkUserActiveVideoForDeck(long j);

    void sinkUserTalkingVideo(long j);

    void sinkUserVideoDataSizeChanged(long j);

    void sinkUserVideoMutedByHost(long j);

    void sinkUserVideoParticipantUnmuteLater(long j);

    void sinkUserVideoQualityChanged(long j);

    void sinkUserVideoRequestUnmuteByHost(long j);

    void sinkUserVideoStatus(long j);

    void sinkVideoLeaderShipModeOnOff();
}
