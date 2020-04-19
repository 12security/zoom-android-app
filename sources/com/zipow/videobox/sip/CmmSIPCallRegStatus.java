package com.zipow.videobox.sip;

public interface CmmSIPCallRegStatus {
    public static final int kSIPCallRegStatus_Idle = 0;
    public static final int kSIPCallRegStatus_RegFailed = 5;
    public static final int kSIPCallRegStatus_Registered = 6;
    public static final int kSIPCallRegStatus_Registering = 4;
    public static final int kSIPCallRegStatus_UnRegistering = 7;
}
