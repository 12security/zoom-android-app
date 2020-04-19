package com.microsoft.services.msa;

import android.os.AsyncTask;

class TokenRequestAsync extends AsyncTask<Void, Void, Void> implements ObservableOAuthRequest {
    private LiveAuthException exception;
    private final DefaultObservableOAuthRequest observerable;
    private final TokenRequest request;
    private OAuthResponse response;

    public TokenRequestAsync(TokenRequest tokenRequest) {
        if (tokenRequest != null) {
            this.observerable = new DefaultObservableOAuthRequest();
            this.request = tokenRequest;
            return;
        }
        throw new AssertionError();
    }

    public void addObserver(OAuthRequestObserver oAuthRequestObserver) {
        this.observerable.addObserver(oAuthRequestObserver);
    }

    public boolean removeObserver(OAuthRequestObserver oAuthRequestObserver) {
        return this.observerable.removeObserver(oAuthRequestObserver);
    }

    /* access modifiers changed from: protected */
    public Void doInBackground(Void... voidArr) {
        try {
            this.response = this.request.execute();
        } catch (LiveAuthException e) {
            this.exception = e;
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void voidR) {
        super.onPostExecute(voidR);
        OAuthResponse oAuthResponse = this.response;
        if (oAuthResponse != null) {
            this.observerable.notifyObservers(oAuthResponse);
            return;
        }
        LiveAuthException liveAuthException = this.exception;
        if (liveAuthException != null) {
            this.observerable.notifyObservers(liveAuthException);
            return;
        }
        this.observerable.notifyObservers(new LiveAuthException(ErrorMessages.CLIENT_ERROR));
    }
}
