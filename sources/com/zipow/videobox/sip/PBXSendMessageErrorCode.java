package com.zipow.videobox.sip;

public interface PBXSendMessageErrorCode {
    public static final int kSendMessageErrorFromCarrier = 7009;
    public static final int kSendMessageErrorInValidPhoneNumber = 7015;
    public static final int kSendMessageErrorNumberLocked = 7010;
    public static final int kSendMessageErrorReachMaxAmountPerDay = 7016;
    public static final int kSendMessageErrorSessionDeleted = 7004;
    public static final int kSendMessageErrorSessionIdVerifiedFailed = 7011;
    public static final int kSendMessageErrorSuccess = 0;
    public static final int kSendMessageErrorTextLimited = 7014;
    public static final int kSendMessageErrorUnSupportNumber = 7017;
}
