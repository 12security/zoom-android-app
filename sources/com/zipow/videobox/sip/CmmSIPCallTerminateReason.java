package com.zipow.videobox.sip;

public interface CmmSIPCallTerminateReason {
    public static final int kSIPCallTermReason_ByAnsweredByOther = 5;
    public static final int kSIPCallTermReason_ByInitAudioDeviceFailed = 4;
    public static final int kSIPCallTermReason_ByLocal = 1;
    public static final int kSIPCallTermReason_ByNetworkBreak = 3;
    public static final int kSIPCallTermReason_ByRemote = 2;
    public static final int kSIPCallTermReason_ByUnknown = 0;
}
