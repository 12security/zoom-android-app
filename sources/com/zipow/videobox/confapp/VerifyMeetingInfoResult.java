package com.zipow.videobox.confapp;

public interface VerifyMeetingInfoResult {
    public static final int VMIR_RealNameAuthBypassVerify = 3;
    public static final int VMIR_RealNameAuthErroIdentifyCode = 1;
    public static final int VMIR_RealNameAuthIdentifyCodeExpired = 2;
    public static final int VMIR_RealNameAuthUnknownError = 4;
    public static final int VMIR_Succ = 0;
}
