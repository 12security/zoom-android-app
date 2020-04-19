package com.microsoft.services.msa;

import java.util.ArrayList;
import java.util.List;

class DefaultObservableOAuthRequest implements ObservableOAuthRequest {
    private final List<OAuthRequestObserver> observers = new ArrayList();

    public void addObserver(OAuthRequestObserver oAuthRequestObserver) {
        this.observers.add(oAuthRequestObserver);
    }

    public void notifyObservers(LiveAuthException liveAuthException) {
        for (OAuthRequestObserver onException : this.observers) {
            onException.onException(liveAuthException);
        }
    }

    public void notifyObservers(OAuthResponse oAuthResponse) {
        for (OAuthRequestObserver onResponse : this.observers) {
            onResponse.onResponse(oAuthResponse);
        }
    }

    public boolean removeObserver(OAuthRequestObserver oAuthRequestObserver) {
        return this.observers.remove(oAuthRequestObserver);
    }
}
