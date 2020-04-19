package com.zipow.videobox.ptapp.p013mm;

/* renamed from: com.zipow.videobox.ptapp.mm.MessageState */
public interface MessageState {
    public static final int MessageState_Canceled = 6;
    public static final int MessageState_E2E_Decoded = 7;
    public static final int MessageState_E2E_FailToDecode = 8;
    public static final int MessageState_E2E_PolicyViolation = 9;
    public static final int MessageState_E2E_WrongState = 10;
    public static final int MessageState_E2Ev2_KeyBindingFailed = 12;
    public static final int MessageState_E2Ev2_NoOnlineBuddy = 13;
    public static final int MessageState_E2Ev2_WaitingKeyTimeout = 11;
    public static final int MessageState_NotInGroup = 5;
    public static final int MessageState_Received = 3;
    public static final int MessageState_SendFail = 4;
    public static final int MessageState_Sending = 1;
    public static final int MessageState_Sent = 2;
    public static final int MessageState_Unknown = 0;
}
