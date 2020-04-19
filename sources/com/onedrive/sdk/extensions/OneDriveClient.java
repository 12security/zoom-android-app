package com.onedrive.sdk.extensions;

import android.app.Activity;
import com.onedrive.sdk.authentication.ClientAuthenticatorException;
import com.onedrive.sdk.authentication.IAccountInfo;
import com.onedrive.sdk.authentication.IAuthenticator;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.IClientConfig;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.generated.BaseOneDriveClient;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.serializer.ISerializer;

public class OneDriveClient extends BaseOneDriveClient implements IOneDriveClient {

    public static class Builder {
        /* access modifiers changed from: private */
        public final OneDriveClient mClient = new OneDriveClient();

        public Builder serializer(ISerializer iSerializer) {
            this.mClient.setSerializer(iSerializer);
            return this;
        }

        public OneDriveClient build() {
            return this.mClient;
        }

        public Builder httpProvider(IHttpProvider iHttpProvider) {
            this.mClient.setHttpProvider(iHttpProvider);
            return this;
        }

        public Builder authenticator(IAuthenticator iAuthenticator) {
            this.mClient.setAuthenticator(iAuthenticator);
            return this;
        }

        public Builder executors(IExecutors iExecutors) {
            this.mClient.setExecutors(iExecutors);
            return this;
        }

        private Builder logger(ILogger iLogger) {
            this.mClient.setLogger(iLogger);
            return this;
        }

        public Builder fromConfig(IClientConfig iClientConfig) {
            return authenticator(iClientConfig.getAuthenticator()).executors(iClientConfig.getExecutors()).httpProvider(iClientConfig.getHttpProvider()).logger(iClientConfig.getLogger()).serializer(iClientConfig.getSerializer());
        }

        public void loginAndBuildClient(final Activity activity, final ICallback<IOneDriveClient> iCallback) {
            this.mClient.validate();
            this.mClient.getExecutors().performOnBackground(new Runnable() {
                public void run() {
                    IExecutors executors = Builder.this.mClient.getExecutors();
                    try {
                        executors.performOnForeground(Builder.this.loginAndBuildClient(activity), iCallback);
                    } catch (ClientException e) {
                        executors.performOnForeground(e, iCallback);
                    }
                }
            });
        }

        /* access modifiers changed from: private */
        public IOneDriveClient loginAndBuildClient(Activity activity) throws ClientException {
            IAccountInfo iAccountInfo;
            this.mClient.validate();
            this.mClient.getAuthenticator().init(this.mClient.getExecutors(), this.mClient.getHttpProvider(), activity, this.mClient.getLogger());
            try {
                iAccountInfo = this.mClient.getAuthenticator().loginSilent();
            } catch (Exception unused) {
                iAccountInfo = null;
            }
            if (iAccountInfo != null || this.mClient.getAuthenticator().login(null) != null) {
                return this.mClient;
            }
            throw new ClientAuthenticatorException("Unable to authenticate silently or interactively", OneDriveErrorCodes.AuthenticationFailure);
        }
    }

    protected OneDriveClient() {
    }

    public IDriveRequestBuilder getDrive() {
        StringBuilder sb = new StringBuilder();
        sb.append(getServiceRoot());
        sb.append("/drive");
        return new DriveRequestBuilder(sb.toString(), this, null);
    }

    public void login(final Activity activity, final ICallback<IOneDriveClient> iCallback) throws ClientException {
        validate();
        getExecutors().performOnBackground(new Runnable() {
            public void run() {
                IExecutors executors = OneDriveClient.this.getExecutors();
                try {
                    executors.performOnForeground(OneDriveClient.this.loginAndBuildClient(activity), iCallback);
                } catch (ClientException e) {
                    executors.performOnForeground(e, iCallback);
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public IOneDriveClient loginAndBuildClient(Activity activity) throws ClientException {
        IAccountInfo iAccountInfo;
        validate();
        getAuthenticator().init(getExecutors(), getHttpProvider(), activity, getLogger());
        try {
            iAccountInfo = getAuthenticator().loginSilent();
        } catch (Exception unused) {
            iAccountInfo = null;
        }
        if (iAccountInfo != null || getAuthenticator().login(null) != null) {
            return this;
        }
        throw new ClientAuthenticatorException("Unable to authenticate silently or interactively", OneDriveErrorCodes.AuthenticationFailure);
    }
}
