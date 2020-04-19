package com.microsoft.services.msa;

interface OAuthRequestObserver {
    void onException(LiveAuthException liveAuthException);

    void onResponse(OAuthResponse oAuthResponse);
}
