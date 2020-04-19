package com.zipow.videobox.sip;

public interface CmmPBXAutoRecordingEvent {
    public static final int kSipCallAutoRecordingInit = 1;
    public static final int kSipCallAutoRecordingPause = 3;
    public static final int kSipCallAutoRecordingStart = 2;
    public static final int kSipCallAutoRecordingStop = 4;
    public static final int kSipCallAutoRecordingUndefined = 0;
}
