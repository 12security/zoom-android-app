package com.zipow.videobox.sip;

public interface CmmSIPCallMediaStatus {
    public static final int kSIPCallMediaStatusLowMosError = 1003;
    public static final int kSIPCallMediaStatusNormal = 0;
    public static final int kSIPCallMediaStatusRTTError = 1002;
    public static final int kSIPCallMediaStatusRxError = 1000;
    public static final int kSIpCallMedisStatusPacketLossError = 1001;
}
