package com.zipow.videobox.ptapp;

public interface IMLocalStatus {
    public static final int IM_LOCAL_STATUS_AUTHENTICATING = 3;
    public static final int IM_LOCAL_STATUS_CONNECTING = 1;
    public static final int IM_LOCAL_STATUS_CONNECT_FAILED = 5;
    public static final int IM_LOCAL_STATUS_NEGOTIATING = 2;
    public static final int IM_LOCAL_STATUS_SIGNEDON = 4;
    public static final int IM_LOCAL_STATUS_SIGNEDOUT = 0;
}
