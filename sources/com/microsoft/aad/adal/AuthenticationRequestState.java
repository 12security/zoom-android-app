package com.microsoft.aad.adal;

class AuthenticationRequestState {
    private final APIEvent mAPIEvent;
    private boolean mCancelled = false;
    private AuthenticationCallback<AuthenticationResult> mDelegate = null;
    private AuthenticationRequest mRequest = null;
    private int mRequestId = 0;

    AuthenticationRequestState(int i, AuthenticationRequest authenticationRequest, AuthenticationCallback<AuthenticationResult> authenticationCallback, APIEvent aPIEvent) {
        this.mRequestId = i;
        this.mDelegate = authenticationCallback;
        this.mRequest = authenticationRequest;
        this.mAPIEvent = aPIEvent;
    }

    public int getRequestId() {
        return this.mRequestId;
    }

    public void setRequestId(int i) {
        this.mRequestId = i;
    }

    public AuthenticationCallback<AuthenticationResult> getDelegate() {
        return this.mDelegate;
    }

    public void setDelegate(AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        this.mDelegate = authenticationCallback;
    }

    public boolean isCancelled() {
        return this.mCancelled;
    }

    public void setCancelled(boolean z) {
        this.mCancelled = z;
    }

    public AuthenticationRequest getRequest() {
        return this.mRequest;
    }

    public void setRequest(AuthenticationRequest authenticationRequest) {
        this.mRequest = authenticationRequest;
    }

    /* access modifiers changed from: 0000 */
    public APIEvent getAPIEvent() {
        return this.mAPIEvent;
    }
}
