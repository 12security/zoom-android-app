package com.zipow.videobox.confapp.component.sink.share;

public interface IShareStatusSink {
    void onBeforeMyStartShare();

    void onBeforeRemoteControlEnabled(boolean z);

    void onMyShareStatueChanged(boolean z);

    void onOtherShareStatueChanged(boolean z, long j);

    void onShareEdit(boolean z);
}
