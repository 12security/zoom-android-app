package com.zipow.videobox.ptapp;

public interface PresentToRoomStatus {
    public static final int PresentToRoomStatus_CannotInputTwice_NotSameNetwork = 28;
    public static final int PresentToRoomStatus_ConnectedToConf = 21;
    public static final int PresentToRoomStatus_ConnectedToLocalProjector = 16;
    public static final int PresentToRoomStatus_ConnectingToConf = 20;
    public static final int PresentToRoomStatus_ConnectingToLocalProjector = 15;
    public static final int PresentToRoomStatus_DetectingUltraSound = 1;
    public static final int PresentToRoomStatus_Idle = 0;
    public static final int PresentToRoomStatus_InputNewParingCode = 35;
    public static final int PresentToRoomStatus_NotifyZRToRefreshSharingCode = 17;
    public static final int PresentToRoomStatus_Preparing = 5;
    public static final int PresentToRoomStatus_QueryingIPAddress = 10;
    public static final int PresentToRoomStatus_TargetVersionIsTooold = 40;
    public static final int PresentToRoomStatus_Terminated = 50;
    public static final int PresentToRoomStatus_UltraSoundDetectFailed = 2;
    public static final int PresentToRoomStatus_WSFailed_VerifyMeetingID = 24;
    public static final int PresentToRoomStatus_WSFailed_VerifyPairingCode = 23;
    public static final int PresentToRoomStatus_WrongMeetingID = 26;
    public static final int PresentToRoomStatus_WrongPairingCode = 25;
    public static final int PresentToRoomStatus_WrongPairingCode_NotSameAccount = 27;
}
