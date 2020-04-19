package com.zipow.videobox.sip;

public interface CmmSIPCallRecordingStatus {
    public static final int kSIPCallRecording_Paused = 4;
    public static final int kSIPCallRecording_Started = 1;
    public static final int kSIPCallRecording_Starting = 2;
    public static final int kSIPCallRecording_Stopped = 0;
    public static final int kSIPCallRecording_Stopping = 3;
}
