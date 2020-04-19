package com.zipow.videobox.ptapp;

public interface ChatAvailableState {
    public static final int ChatAvailableState_Available = 0;
    public static final int ChatAvailableState_BlockedByContact = 4;
    public static final int ChatAvailableState_CallBlockedByContact = 6;
    public static final int ChatAvailableState_CallBlockedBySelf = 7;
    public static final int ChatAvailableState_CallContactDoesNotExist = 8;
    public static final int ChatAvailableState_ContactDoesNotExist = 5;
    public static final int ChatAvailableState_DeleteMsgBlockedByContact = 9;
    public static final int ChatAvailableState_EditMsgBlockedByContact = 10;
    public static final int ChatAvailableState_IMBlockedByIB = 12;
    public static final int ChatAvailableState_IMDisable = 1;
    public static final int ChatAvailableState_InPeerRestrictDomains = 3;
    public static final int ChatAvailableState_InSelfRestrictDomains = 2;
    public static final int ChatAvailableState_MyIMDisable = 11;
}
