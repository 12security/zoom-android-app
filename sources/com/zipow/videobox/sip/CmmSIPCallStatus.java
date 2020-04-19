package com.zipow.videobox.sip;

public interface CmmSIPCallStatus {
    public static final int kSIPCallStatus_Accepted = 26;
    public static final int kSIPCallStatus_Both_Hold = 31;
    public static final int kSIPCallStatus_Busy = 22;
    public static final int kSIPCallStatus_CallOutFailed = 5;
    public static final int kSIPCallStatus_Declined = 23;
    public static final int kSIPCallStatus_Fail = 32;
    public static final int kSIPCallStatus_InCall = 28;
    public static final int kSIPCallStatus_Incoming = 15;
    public static final int kSIPCallStatus_Init = 0;
    public static final int kSIPCallStatus_Local_Hold = 27;
    public static final int kSIPCallStatus_NotAvailable = 24;
    public static final int kSIPCallStatus_NotFound = 21;
    public static final int kSIPCallStatus_Remote_Hold = 30;
    public static final int kSIPCallStatus_Ringing = 20;
    public static final int kSIPCallStatus_Session_InProgress = 33;
    public static final int kSIPCallStatus_StayOnPhone = 34;
    public static final int kSIPCallStatus_Terminated = 29;
    public static final int kSIPCallStatus_Timeout = 25;
}
