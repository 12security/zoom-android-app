package com.zipow.videobox.ptapp;

public interface TPV2PresenceExpireType {
    public static final int PresenceExpireType_None = 0;
    public static final int PresenceExpireType_ReachBackendLimitation = 4;
    public static final int PresenceExpireType_ReachDuration = 3;
    public static final int PresenceExpireType_RequestTimeOut = 5;
    public static final int PresenceExpireType_SubAvailable = 1;
    public static final int PresenceExpireType_SubSuspend = 2;
    public static final int PresenceExpireType_TTLDisabled = 6;
}
