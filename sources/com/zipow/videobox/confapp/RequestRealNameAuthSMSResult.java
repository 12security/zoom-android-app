package com.zipow.videobox.confapp;

public interface RequestRealNameAuthSMSResult {
    public static final int RequestRealNameAuthSMSResult_BypassVerify = 6;
    public static final int RequestRealNameAuthSMSResult_InvalidPhoneNum = 3;
    public static final int RequestRealNameAuthSMSResult_PhoneNumAlreadyBound = 4;
    public static final int RequestRealNameAuthSMSResult_PhoneNumSendTooFrequent = 5;
    public static final int RequestRealNameAuthSMSResult_RequestFailed = 2;
    public static final int RequestRealNameAuthSMSResult_SendSMSFailed = 1;
    public static final int RequestRealNameAuthSMSResult_Succ = 0;
}
