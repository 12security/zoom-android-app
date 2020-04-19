package com.zipow.videobox.sip;

public interface SIPServiceStatus {
    public static final int SIPServiceStatus_CallingOut = 20;
    public static final int SIPServiceStatus_Idle = 0;
    public static final int SIPServiceStatus_InCall = 50;
    public static final int SIPServiceStatus_RegFailed = 5;
    public static final int SIPServiceStatus_Registered = 6;
    public static final int SIPServiceStatus_Registering = 4;
    public static final int SIPServiceStatus_Ringing = 10;
    public static final int SIPServiceStatus_UnRegistering = 7;
}
