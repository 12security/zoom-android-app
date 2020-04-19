package com.onedrive.sdk.authentication;

import android.app.Activity;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;

public interface IAuthenticator {
    IAccountInfo getAccountInfo();

    void init(IExecutors iExecutors, IHttpProvider iHttpProvider, Activity activity, ILogger iLogger);

    IAccountInfo login(String str) throws ClientException;

    void login(String str, ICallback<IAccountInfo> iCallback);

    IAccountInfo loginSilent() throws ClientException;

    void loginSilent(ICallback<IAccountInfo> iCallback);

    void logout() throws ClientException;

    void logout(ICallback<Void> iCallback);
}
