package com.onedrive.sdk.core;

import com.onedrive.sdk.authentication.ADALAuthenticator;
import com.onedrive.sdk.authentication.AuthorizationInterceptor;
import com.onedrive.sdk.authentication.DisambiguationAuthenticator;
import com.onedrive.sdk.authentication.IAuthenticator;
import com.onedrive.sdk.authentication.MSAAuthenticator;
import com.onedrive.sdk.concurrency.DefaultExecutors;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.http.DefaultHttpProvider;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.http.IRequestInterceptor;
import com.onedrive.sdk.logger.DefaultLogger;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.serializer.DefaultSerializer;
import com.onedrive.sdk.serializer.ISerializer;

public abstract class DefaultClientConfig implements IClientConfig {
    private IAuthenticator mAuthenticator;
    private IExecutors mExecutors;
    private DefaultHttpProvider mHttpProvider;
    private ILogger mLogger;
    private IRequestInterceptor mRequestInterceptor;
    private DefaultSerializer mSerializer;

    public static IClientConfig createWithAuthenticator(IAuthenticator iAuthenticator) {
        C17911 r0 = new DefaultClientConfig() {
        };
        r0.mAuthenticator = iAuthenticator;
        r0.getLogger().logDebug("Using provided authenticator");
        return r0;
    }

    public static IClientConfig createWithAuthenticators(MSAAuthenticator mSAAuthenticator, ADALAuthenticator aDALAuthenticator) {
        C17922 r0 = new DefaultClientConfig() {
        };
        r0.mAuthenticator = new DisambiguationAuthenticator(mSAAuthenticator, aDALAuthenticator);
        r0.getLogger().logDebug("Created DisambiguationAuthenticator");
        return r0;
    }

    public IAuthenticator getAuthenticator() {
        return this.mAuthenticator;
    }

    public IHttpProvider getHttpProvider() {
        if (this.mHttpProvider == null) {
            this.mHttpProvider = new DefaultHttpProvider(getSerializer(), getRequestInterceptor(), getExecutors(), getLogger());
            this.mLogger.logDebug("Created DefaultHttpProvider");
        }
        return this.mHttpProvider;
    }

    public ISerializer getSerializer() {
        if (this.mSerializer == null) {
            this.mSerializer = new DefaultSerializer(getLogger());
            this.mLogger.logDebug("Created DefaultSerializer");
        }
        return this.mSerializer;
    }

    public IExecutors getExecutors() {
        if (this.mExecutors == null) {
            this.mExecutors = new DefaultExecutors(getLogger());
            this.mLogger.logDebug("Created DefaultExecutors");
        }
        return this.mExecutors;
    }

    public ILogger getLogger() {
        if (this.mLogger == null) {
            this.mLogger = new DefaultLogger();
            this.mLogger.logDebug("Created DefaultLogger");
        }
        return this.mLogger;
    }

    private IRequestInterceptor getRequestInterceptor() {
        if (this.mRequestInterceptor == null) {
            this.mRequestInterceptor = new AuthorizationInterceptor(getAuthenticator(), getLogger());
        }
        return this.mRequestInterceptor;
    }
}
