package com.zipow.videobox.sip;

public interface CmmCallActionType {
    public static final int kCallActionType_BlindTransfer = 9;
    public static final int kCallActionType_Callout = 8;
    public static final int kCallActionType_Decline = 4;
    public static final int kCallActionType_EndAndAccept = 3;
    public static final int kCallActionType_Hangup = 7;
    public static final int kCallActionType_Hold = 5;
    public static final int kCallActionType_HoldAndAccept = 2;
    public static final int kCallActionType_None = 0;
    public static final int kCallActionType_NormalAccept = 1;
    public static final int kCallActionType_PickupFromSharedLine = 13;
    public static final int kCallActionType_SwitchToCarrier = 14;
    public static final int kCallActionType_Unhold = 6;
    public static final int kCallActionType_VoicemailTransfer = 12;
    public static final int kCallActionType_WarmTransfer = 10;
    public static final int kCallActionType_WarmTransferComplete = 11;
}
