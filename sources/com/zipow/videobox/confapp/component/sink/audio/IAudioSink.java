package com.zipow.videobox.confapp.component.sink.audio;

public interface IAudioSink {
    void sinkConfKmsKeyNotReady();

    void sinkPreemptionAudio(int i);
}
