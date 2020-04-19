package com.onedrive.sdk.core;

import com.onedrive.sdk.authentication.IAuthenticator;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.serializer.ISerializer;

public class BaseClient implements IBaseClient {
    private IAuthenticator mAuthenticator;
    private IExecutors mExecutors;
    private IHttpProvider mHttpProvider;
    private ILogger mLogger;
    private ISerializer mSerializer;

    public IAuthenticator getAuthenticator() {
        return this.mAuthenticator;
    }

    public String getServiceRoot() {
        return getAuthenticator().getAccountInfo().getServiceRoot();
    }

    public IExecutors getExecutors() {
        return this.mExecutors;
    }

    public IHttpProvider getHttpProvider() {
        return this.mHttpProvider;
    }

    public ILogger getLogger() {
        return this.mLogger;
    }

    public ISerializer getSerializer() {
        return this.mSerializer;
    }

    public void validate() {
        if (this.mAuthenticator == null) {
            throw new NullPointerException("Authenticator");
        } else if (this.mExecutors == null) {
            throw new NullPointerException("Executors");
        } else if (this.mHttpProvider == null) {
            throw new NullPointerException("HttpProvider");
        } else if (this.mSerializer == null) {
            throw new NullPointerException("Serializer");
        }
    }

    /* access modifiers changed from: protected */
    public void setLogger(ILogger iLogger) {
        this.mLogger = iLogger;
    }

    /* access modifiers changed from: protected */
    public void setExecutors(IExecutors iExecutors) {
        this.mExecutors = iExecutors;
    }

    /* access modifiers changed from: protected */
    public void setAuthenticator(IAuthenticator iAuthenticator) {
        this.mAuthenticator = iAuthenticator;
    }

    /* access modifiers changed from: protected */
    public void setHttpProvider(IHttpProvider iHttpProvider) {
        this.mHttpProvider = iHttpProvider;
    }

    public void setSerializer(ISerializer iSerializer) {
        this.mSerializer = iSerializer;
    }
}
