package com.zipow.videobox.sip;

public interface CallHistoryState {
    public static final int CALL_STATE_ACCEPTED = 2;
    public static final int CALL_STATE_DECLINED = 3;
    public static final int CALL_STATE_MISSED = 1;
    public static final int CALL_STATE_MISSED_CLEAR = 4;
    public static final int CALL_STATE_NOT_SET = 0;
    public static final int CALL_STATE_NOT_SHOW = 5;
}
