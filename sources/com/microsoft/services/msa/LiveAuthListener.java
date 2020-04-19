package com.microsoft.services.msa;

public interface LiveAuthListener {
    void onAuthComplete(LiveStatus liveStatus, LiveConnectSession liveConnectSession, Object obj);

    void onAuthError(LiveAuthException liveAuthException, Object obj);
}
