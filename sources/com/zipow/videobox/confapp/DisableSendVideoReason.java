package com.zipow.videobox.confapp;

public interface DisableSendVideoReason {
    public static final int DisableSendVideoByBandwidthLimit = 4;
    public static final int DisableSendVideoByCompanionMode = 2;
    public static final int DisableSendVideoByHostPermission = 16;
    public static final int DisableSendVideoByITConfig = 1;
    public static final int DisableSendVideoByNone = 0;
    public static final int DisableSendVideoByWebinarAttendee = 8;
}
