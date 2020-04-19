package com.onedrive.sdk.authentication;

import com.onedrive.sdk.http.IHttpRequest;
import com.onedrive.sdk.http.IRequestInterceptor;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.options.HeaderOption;

public class AuthorizationInterceptor implements IRequestInterceptor {
    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    public static final String OAUTH_BEARER_PREFIX = "bearer ";
    private final IAuthenticator mAuthenticator;
    private final ILogger mLogger;

    public AuthorizationInterceptor(IAuthenticator iAuthenticator, ILogger iLogger) {
        this.mAuthenticator = iAuthenticator;
        this.mLogger = iLogger;
    }

    public void intercept(IHttpRequest iHttpRequest) {
        ILogger iLogger = this.mLogger;
        StringBuilder sb = new StringBuilder();
        sb.append("Intercepting request, ");
        sb.append(iHttpRequest.getRequestUrl());
        iLogger.logDebug(sb.toString());
        for (HeaderOption name : iHttpRequest.getHeaders()) {
            if (name.getName().equals("Authorization")) {
                this.mLogger.logDebug("Found an existing authorization header!");
                return;
            }
        }
        if (this.mAuthenticator.getAccountInfo() != null) {
            this.mLogger.logDebug("Found account information");
            if (this.mAuthenticator.getAccountInfo().isExpired()) {
                this.mLogger.logDebug("Account access token is expired, refreshing");
                this.mAuthenticator.getAccountInfo().refresh();
            }
            String accessToken = this.mAuthenticator.getAccountInfo().getAccessToken();
            StringBuilder sb2 = new StringBuilder();
            sb2.append(OAUTH_BEARER_PREFIX);
            sb2.append(accessToken);
            iHttpRequest.addHeader("Authorization", sb2.toString());
        } else {
            this.mLogger.logDebug("No active account found, skipping writing auth header");
        }
    }
}
