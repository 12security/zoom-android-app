package com.zipow.videobox.sip;

public interface PBXMessageSendStatus {
    public static final int kMessageSendCreated = 3;
    public static final int kMessageSendDelivered = 4;
    public static final int kMessageSendFailed = 6;
    public static final int kMessageSendInitial = 0;
    public static final int kMessageSendReceived = 5;
    public static final int kMessageSendToWebFailed = 2;
    public static final int kMessageSendingToWeb = 1;
}
