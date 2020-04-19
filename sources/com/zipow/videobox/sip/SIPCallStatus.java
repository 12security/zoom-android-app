package com.zipow.videobox.sip;

public interface SIPCallStatus {
    public static final int SIPCallStatus_Accepted = 26;
    public static final int SIPCallStatus_Busy = 22;
    public static final int SIPCallStatus_CallOutFailed = 5;
    public static final int SIPCallStatus_Declined = 23;
    public static final int SIPCallStatus_Hold = 27;
    public static final int SIPCallStatus_InCall = 28;
    public static final int SIPCallStatus_Incoming = 15;
    public static final int SIPCallStatus_Init = 0;
    public static final int SIPCallStatus_NotAvailable = 24;
    public static final int SIPCallStatus_NotFound = 21;
    public static final int SIPCallStatus_Ringing = 20;
    public static final int SIPCallStatus_Terminated = 29;
    public static final int SIPCallStatus_Timeout = 25;
}
