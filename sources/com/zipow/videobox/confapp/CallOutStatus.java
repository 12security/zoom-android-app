package com.zipow.videobox.confapp;

public interface CallOutStatus {
    public static final int CALLOUT_STATUS_ACCEPTED = 3;
    public static final int CALLOUT_STATUS_BLOCK_HIGH_RATE = 15;
    public static final int CALLOUT_STATUS_BLOCK_NO_HOST = 14;
    public static final int CALLOUT_STATUS_BLOCK_TOO_FREQUENT = 16;
    public static final int CALLOUT_STATUS_BUSY = 4;
    public static final int CALLOUT_STATUS_CALLING = 1;
    public static final int CALLOUT_STATUS_JOIN_SUC = 8;
    public static final int CALLOUT_STATUS_NOT_AVAILABLE = 5;
    public static final int CALLOUT_STATUS_NO_ANSWER = 13;
    public static final int CALLOUT_STATUS_OTHER_FAIL = 7;
    public static final int CALLOUT_STATUS_RINGING = 2;
    public static final int CALLOUT_STATUS_TIMEOUT = 9;
    public static final int CALLOUT_STATUS_UNKNOWN = 0;
    public static final int CALLOUT_STATUS_USER_HANGUP = 6;
    public static final int CALLOUT_STATUS_ZOOM_CALL_CACELED = 11;
    public static final int CALLOUT_STATUS_ZOOM_CANCEL_CALL_FAIL = 12;
    public static final int CALLOUT_STATUS_ZOOM_START_CANCELCALL = 10;
}
