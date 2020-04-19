package com.onedrive.sdk.authentication;

import com.microsoft.aad.adal.AuthenticationResult;
import com.onedrive.sdk.logger.ILogger;

public class ADALAccountInfo implements IAccountInfo {
    private AuthenticationResult mAuthenticationResult;
    private final ADALAuthenticator mAuthenticator;
    private final ILogger mLogger;
    private final ServiceInfo mOneDriveServiceInfo;

    public ADALAccountInfo(ADALAuthenticator aDALAuthenticator, AuthenticationResult authenticationResult, ServiceInfo serviceInfo, ILogger iLogger) {
        this.mAuthenticator = aDALAuthenticator;
        this.mAuthenticationResult = authenticationResult;
        this.mOneDriveServiceInfo = serviceInfo;
        this.mLogger = iLogger;
    }

    public AccountType getAccountType() {
        return AccountType.ActiveDirectory;
    }

    public String getAccessToken() {
        return this.mAuthenticationResult.getAccessToken();
    }

    public String getServiceRoot() {
        return this.mOneDriveServiceInfo.serviceEndpointUri;
    }

    public boolean isExpired() {
        return this.mAuthenticationResult.isExpired();
    }

    public void refresh() {
        this.mLogger.logDebug("Refreshing access token...");
        this.mAuthenticationResult = ((ADALAccountInfo) this.mAuthenticator.loginSilent()).mAuthenticationResult;
    }
}
