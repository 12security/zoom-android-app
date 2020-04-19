package com.microsoft.services.msa;

interface ObservableOAuthRequest {
    void addObserver(OAuthRequestObserver oAuthRequestObserver);

    boolean removeObserver(OAuthRequestObserver oAuthRequestObserver);
}
