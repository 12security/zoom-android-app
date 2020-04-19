package com.onedrive.sdk.authentication;

import com.microsoft.services.msa.LiveConnectSession;
import com.onedrive.sdk.logger.ILogger;

public class MSAAccountInfo implements IAccountInfo {
    public static final String ONE_DRIVE_PERSONAL_SERVICE_ROOT = "https://api.onedrive.com/v1.0";
    private final MSAAuthenticator mAuthenticator;
    private final ILogger mLogger;
    private LiveConnectSession mSession;

    public String getServiceRoot() {
        return ONE_DRIVE_PERSONAL_SERVICE_ROOT;
    }

    public MSAAccountInfo(MSAAuthenticator mSAAuthenticator, LiveConnectSession liveConnectSession, ILogger iLogger) {
        this.mAuthenticator = mSAAuthenticator;
        this.mSession = liveConnectSession;
        this.mLogger = iLogger;
    }

    public AccountType getAccountType() {
        return AccountType.MicrosoftAccount;
    }

    public String getAccessToken() {
        return this.mSession.getAccessToken();
    }

    public boolean isExpired() {
        return this.mSession.isExpired();
    }

    public void refresh() {
        this.mLogger.logDebug("Refreshing access token...");
        this.mSession = ((MSAAccountInfo) this.mAuthenticator.loginSilent()).mSession;
    }
}
