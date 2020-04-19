package com.microsoft.aad.adal;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import java.io.IOException;

interface IBrokerProxy {
    SwitchToBroker canSwitchToBroker(String str);

    boolean canUseLocalCache(String str);

    AuthenticationResult getAuthTokenInBackground(AuthenticationRequest authenticationRequest, BrokerEvent brokerEvent) throws AuthenticationException;

    String getBrokerAppVersion(String str) throws NameNotFoundException;

    UserInfo[] getBrokerUsers() throws OperationCanceledException, AuthenticatorException, IOException;

    String getCurrentActiveBrokerPackageName();

    String getCurrentUser();

    Intent getIntentForBrokerActivity(AuthenticationRequest authenticationRequest, BrokerEvent brokerEvent) throws AuthenticationException;

    void removeAccounts();

    void saveAccount(String str);

    boolean verifyUser(String str, String str2);
}
