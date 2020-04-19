package com.microsoft.aad.adal;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorDescription;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Base64;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.microsoft.aad.adal.AuthenticationConstants.Broker.CliTelemInfo;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import org.apache.http.message.TokenParser;

@TargetApi(14)
class BrokerProxy implements IBrokerProxy {
    private static final int ACCOUNT_MANAGER_ERROR_CODE_BAD_AUTHENTICATION = 9;
    private static final String AUTHENTICATOR_CANCELS_REQUEST = "Authenticator cancels the request";
    public static final String DATA_USER_INFO = "com.microsoft.workaccount.user.info";
    private static final String KEY_ACCOUNT_LIST_DELIM = "|";
    private static final String KEY_APP_ACCOUNTS_FOR_TOKEN_REMOVAL = "AppAccountsForTokenRemoval";
    private static final String KEY_SHARED_PREF_ACCOUNT_LIST = "com.microsoft.aad.adal.account.list";
    private static final String TAG = "BrokerProxy";
    private AccountManager mAcctManager;
    private final String mBrokerTag;
    /* access modifiers changed from: private */
    public Context mContext;
    private Handler mHandler;

    enum SwitchToBroker {
        CAN_SWITCH_TO_BROKER,
        CANNOT_SWITCH_TO_BROKER,
        NEED_PERMISSIONS_TO_SWITCH_TO_BROKER
    }

    BrokerProxy() {
        this.mBrokerTag = AuthenticationSettings.INSTANCE.getBrokerSignature();
    }

    BrokerProxy(Context context) {
        this.mContext = context;
        this.mAcctManager = AccountManager.get(this.mContext);
        this.mHandler = new Handler(this.mContext.getMainLooper());
        this.mBrokerTag = AuthenticationSettings.INSTANCE.getBrokerSignature();
    }

    public SwitchToBroker canSwitchToBroker(String str) {
        try {
            URL url = new URL(str);
            String packageName = this.mContext.getPackageName();
            boolean z = true;
            boolean z2 = AuthenticationSettings.INSTANCE.getUseBroker() && !packageName.equalsIgnoreCase(AuthenticationSettings.INSTANCE.getBrokerPackageName()) && !packageName.equalsIgnoreCase(Broker.AZURE_AUTHENTICATOR_APP_PACKAGE_NAME) && verifyAuthenticator(this.mAcctManager) && !UrlExtensions.isADFSAuthority(url);
            if (!z2) {
                Logger.m236v("BrokerProxy:canSwitchToBroker", "Broker auth is turned off or no valid broker is available on the device, cannot switch to broker.");
                return SwitchToBroker.CANNOT_SWITCH_TO_BROKER;
            }
            if (!isBrokerAccountServiceSupported()) {
                if (!z2 || !checkAccount(this.mAcctManager, "", "")) {
                    z = false;
                }
                if (!z) {
                    Logger.m236v("BrokerProxy:canSwitchToBroker", "No valid account existed in broker, cannot switch to broker for auth.");
                    return SwitchToBroker.CANNOT_SWITCH_TO_BROKER;
                }
                try {
                    verifyBrokerPermissionsAPI23AndHigher();
                } catch (UsageAuthenticationException unused) {
                    Logger.m236v("BrokerProxy:canSwitchToBroker", "Missing GET_ACCOUNTS permission, cannot switch to broker.");
                    return SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER;
                }
            }
            return SwitchToBroker.CAN_SWITCH_TO_BROKER;
        } catch (MalformedURLException unused2) {
            throw new IllegalArgumentException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL.name());
        }
    }

    public boolean verifyUser(String str, String str2) {
        if (!isBrokerAccountServiceSupported()) {
            return checkAccount(this.mAcctManager, str, str2);
        }
        return true;
    }

    public boolean canUseLocalCache(String str) {
        if (canSwitchToBroker(str) == SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
            Logger.m236v("BrokerProxy:canUseLocalCache", "It does not use broker");
            return true;
        } else if (!verifySignature(this.mContext.getPackageName())) {
            return false;
        } else {
            Logger.m236v("BrokerProxy:canUseLocalCache", "Broker installer can use local cache");
            return true;
        }
    }

    public boolean verifyBrokerPermissionsAPI22AndLess() throws UsageAuthenticationException {
        StringBuilder sb = new StringBuilder();
        if (VERSION.SDK_INT < 23) {
            sb.append(checkPermission("android.permission.GET_ACCOUNTS"));
            sb.append(checkPermission("android.permission.MANAGE_ACCOUNTS"));
            sb.append(checkPermission("android.permission.USE_CREDENTIALS"));
            if (sb.length() == 0) {
                return true;
            }
            ADALError aDALError = ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Broker related permissions are missing for ");
            sb2.append(sb.toString());
            throw new UsageAuthenticationException(aDALError, sb2.toString());
        }
        Logger.m236v(TAG, "Device runs on 23 and above, skip the check for 22 and below.");
        return true;
    }

    @TargetApi(23)
    public boolean verifyBrokerPermissionsAPI23AndHigher() throws UsageAuthenticationException {
        StringBuilder sb = new StringBuilder();
        if (VERSION.SDK_INT >= 23) {
            sb.append(checkPermission("android.permission.GET_ACCOUNTS"));
            if (sb.length() == 0) {
                return true;
            }
            ADALError aDALError = ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Broker related permissions are missing for ");
            sb2.append(sb.toString());
            throw new UsageAuthenticationException(aDALError, sb2.toString());
        }
        Logger.m236v(TAG, "Device is lower than 23, skip the GET_ACCOUNTS permission check.");
        return true;
    }

    private String checkPermission(String str) {
        if (this.mContext.getPackageManager().checkPermission(str, this.mContext.getPackageName()) == 0) {
            return "";
        }
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Broker related permissions are missing for ");
        sb.append(str);
        Logger.m239w(str2, sb.toString(), "", ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING);
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(TokenParser.f498SP);
        return sb2.toString();
    }

    private void verifyNotOnMainThread() {
        Looper myLooper = Looper.myLooper();
        if (myLooper != null && myLooper == this.mContext.getMainLooper()) {
            IllegalStateException illegalStateException = new IllegalStateException("calling this from your main thread can lead to deadlock");
            Logger.m232e(TAG, "calling this from your main thread can lead to deadlock and/or ANRs", "", ADALError.DEVELOPER_CALLING_ON_MAIN_THREAD, illegalStateException);
            if (this.mContext.getApplicationInfo().targetSdkVersion >= 8) {
                throw illegalStateException;
            }
        }
    }

    private Account findAccount(String str, Account[] accountArr) {
        if (accountArr != null) {
            for (Account account : accountArr) {
                if (account != null && account.name != null && account.name.equalsIgnoreCase(str)) {
                    return account;
                }
            }
        }
        return null;
    }

    private UserInfo findUserInfo(String str, UserInfo[] userInfoArr) {
        if (userInfoArr != null) {
            for (UserInfo userInfo : userInfoArr) {
                if (userInfo != null && !TextUtils.isEmpty(userInfo.getUserId()) && userInfo.getUserId().equalsIgnoreCase(str)) {
                    return userInfo;
                }
            }
        }
        return null;
    }

    public AuthenticationResult getAuthTokenInBackground(AuthenticationRequest authenticationRequest, BrokerEvent brokerEvent) throws AuthenticationException {
        Bundle bundle;
        verifyNotOnMainThread();
        Bundle brokerOptions = getBrokerOptions(authenticationRequest);
        if (isBrokerAccountServiceSupported()) {
            bundle = BrokerAccountServiceHandler.getInstance().getAuthToken(this.mContext, brokerOptions, brokerEvent);
        } else {
            bundle = getAuthTokenFromAccountManager(authenticationRequest, brokerOptions);
        }
        if (bundle != null) {
            return getResultFromBrokerResponse(bundle, authenticationRequest);
        }
        Logger.m236v(TAG, "No bundle result returned from broker for silent request.");
        return null;
    }

    private Bundle getAuthTokenFromAccountManager(AuthenticationRequest authenticationRequest, Bundle bundle) throws AuthenticationException {
        Account targetAccount = getTargetAccount(authenticationRequest);
        if (targetAccount != null) {
            try {
                AccountManagerFuture authToken = this.mAcctManager.getAuthToken(targetAccount, Broker.AUTHTOKEN_TYPE, bundle, false, null, this.mHandler);
                Logger.m236v("BrokerProxy:getAuthTokenFromAccountManager", "Received result from broker");
                Bundle bundle2 = (Bundle) authToken.getResult();
                Logger.m236v("BrokerProxy:getAuthTokenFromAccountManager", "Returning result from broker");
                return bundle2;
            } catch (OperationCanceledException e) {
                Logger.m232e("BrokerProxy:getAuthTokenFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "", ADALError.AUTH_FAILED_CANCELLED, e);
                throw new AuthenticationException(ADALError.AUTH_FAILED_CANCELLED, e.getMessage(), (Throwable) e);
            } catch (AuthenticatorException e2) {
                if (StringExtensions.isNullOrBlank(e2.getMessage()) || !e2.getMessage().contains("invalid_grant")) {
                    Logger.m231e("BrokerProxy:getAuthTokenFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "", ADALError.BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN);
                    throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN, e2.getMessage());
                }
                Logger.m231e("BrokerProxy:getAuthTokenFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "Acquire token failed with 'invalid grant' error, cannot proceed with silent request.", ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED);
                throw new AuthenticationException(ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED, e2.getMessage());
            } catch (IOException e3) {
                Logger.m231e("BrokerProxy:getAuthTokenFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "", ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION);
                if (e3.getMessage() != null && e3.getMessage().contains(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE.getDescription())) {
                    ADALError aDALError = ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Received error from broker, errorCode: ");
                    sb.append(e3.getMessage());
                    throw new AuthenticationException(aDALError, sb.toString());
                } else if (e3.getMessage() == null || !e3.getMessage().contains(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription())) {
                    throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION, e3.getMessage(), (Throwable) e3);
                } else {
                    ADALError aDALError2 = ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Received error from broker, errorCode: ");
                    sb2.append(e3.getMessage());
                    throw new AuthenticationException(aDALError2, sb2.toString());
                }
            }
        } else {
            Logger.m236v("BrokerProxy:getAuthTokenFromAccountManager", "Target account is not found");
            return null;
        }
    }

    /* access modifiers changed from: private */
    public boolean isBrokerAccountServiceSupported() {
        return isServiceSupported(this.mContext, BrokerAccountServiceHandler.getIntentForBrokerAccountService(this.mContext));
    }

    private boolean isServiceSupported(Context context, Intent intent) {
        boolean z = false;
        if (intent == null) {
            return false;
        }
        List queryIntentServices = context.getPackageManager().queryIntentServices(intent, 0);
        if (queryIntentServices != null && queryIntentServices.size() > 0) {
            z = true;
        }
        return z;
    }

    private Account getTargetAccount(AuthenticationRequest authenticationRequest) {
        Account[] accountsByType = this.mAcctManager.getAccountsByType(Broker.BROKER_ACCOUNT_TYPE);
        if (!TextUtils.isEmpty(authenticationRequest.getBrokerAccountName())) {
            return findAccount(authenticationRequest.getBrokerAccountName(), accountsByType);
        }
        try {
            UserInfo findUserInfo = findUserInfo(authenticationRequest.getUserId(), getBrokerUsers());
            if (findUserInfo != null) {
                return findAccount(findUserInfo.getDisplayableId(), accountsByType);
            }
            return null;
        } catch (AuthenticatorException | OperationCanceledException | IOException e) {
            Logger.m232e("BrokerProxy:getTargetAccount", "Exception is thrown when trying to get target account.", e.getMessage(), ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION, e);
            return null;
        }
    }

    private AuthenticationResult getResultFromBrokerResponse(Bundle bundle, AuthenticationRequest authenticationRequest) throws AuthenticationException {
        Date date;
        ADALError aDALError;
        if (bundle != null) {
            int i = bundle.getInt("errorCode");
            String string = bundle.getString("errorMessage");
            String string2 = bundle.getString("error");
            String string3 = bundle.getString("error_description");
            if (!StringExtensions.isNullOrBlank(string)) {
                switch (i) {
                    case 3:
                        if (!string.contains(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION.getDescription())) {
                            if (!string.contains(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE.getDescription())) {
                                aDALError = ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION;
                                break;
                            } else {
                                aDALError = ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE;
                                break;
                            }
                        } else {
                            aDALError = ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION;
                            break;
                        }
                    case 4:
                        aDALError = ADALError.AUTH_FAILED_CANCELLED;
                        break;
                    case 6:
                        aDALError = ADALError.BROKER_AUTHENTICATOR_UNSUPPORTED_OPERATION;
                        break;
                    case 7:
                        aDALError = ADALError.BROKER_AUTHENTICATOR_BAD_ARGUMENTS;
                        break;
                    case 9:
                        aDALError = ADALError.BROKER_AUTHENTICATOR_BAD_AUTHENTICATION;
                        break;
                    default:
                        aDALError = ADALError.BROKER_AUTHENTICATOR_ERROR_GETAUTHTOKEN;
                        break;
                }
                throw new AuthenticationException(aDALError, string);
            } else if (!StringExtensions.isNullOrBlank(string2) && authenticationRequest.isSilent()) {
                ADALError aDALError2 = ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED;
                StringBuilder sb = new StringBuilder();
                sb.append("Received error from broker, errorCode: ");
                sb.append(string2);
                sb.append("; ErrorDescription: ");
                sb.append(string3);
                AuthenticationException authenticationException = new AuthenticationException(aDALError2, sb.toString());
                Serializable serializable = bundle.getSerializable(OAuth2.HTTP_RESPONSE_BODY);
                Serializable serializable2 = bundle.getSerializable(OAuth2.HTTP_RESPONSE_HEADER);
                if (serializable != null && (serializable instanceof HashMap)) {
                    authenticationException.setHttpResponseBody((HashMap) serializable);
                }
                if (serializable2 != null && (serializable2 instanceof HashMap)) {
                    authenticationException.setHttpResponseHeaders((HashMap) serializable2);
                }
                authenticationException.setServiceStatusCode(bundle.getInt(OAuth2.HTTP_STATUS_CODE));
                throw authenticationException;
            } else if (bundle.getBoolean(Broker.ACCOUNT_INITIAL_REQUEST)) {
                return AuthenticationResult.createResultForInitialRequest();
            } else {
                UserInfo userInfoFromBrokerResult = UserInfo.getUserInfoFromBrokerResult(bundle);
                String string4 = bundle.getString(Broker.ACCOUNT_USERINFO_TENANTID, "");
                if (bundle.getLong(Broker.ACCOUNT_EXPIREDATE) == 0) {
                    Logger.m236v("BrokerProxy:getResultFromBrokerResponse", "Broker doesn't return expire date, set it current date plus one hour");
                    GregorianCalendar gregorianCalendar = new GregorianCalendar();
                    gregorianCalendar.add(13, AuthenticationConstants.DEFAULT_EXPIRATION_TIME_SEC);
                    date = gregorianCalendar.getTime();
                } else {
                    date = new Date(bundle.getLong(Broker.ACCOUNT_EXPIREDATE));
                }
                AuthenticationResult authenticationResult = new AuthenticationResult(bundle.getString("authtoken"), "", date, false, userInfoFromBrokerResult, string4, "", null);
                CliTelemInfo cliTelemInfo = new CliTelemInfo();
                cliTelemInfo.setServerErrorCode(bundle.getString(CliTelemInfo.SERVER_ERROR));
                cliTelemInfo.setServerSubErrorCode(bundle.getString(CliTelemInfo.SERVER_SUBERROR));
                cliTelemInfo.setRefreshTokenAge(bundle.getString(CliTelemInfo.RT_AGE));
                cliTelemInfo.setSpeRing(bundle.getString(CliTelemInfo.SPE_RING));
                authenticationResult.setCliTelemInfo(cliTelemInfo);
                return authenticationResult;
            }
        } else {
            throw new IllegalArgumentException("bundleResult");
        }
    }

    public void saveAccount(String str) {
        if (str != null && !str.isEmpty()) {
            SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(KEY_SHARED_PREF_ACCOUNT_LIST, 0);
            String string = sharedPreferences.getString(KEY_APP_ACCOUNTS_FOR_TOKEN_REMOVAL, "");
            StringBuilder sb = new StringBuilder();
            sb.append("|");
            sb.append(str);
            if (!string.contains(sb.toString())) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(string);
                sb2.append("|");
                sb2.append(str);
                String sb3 = sb2.toString();
                Editor edit = sharedPreferences.edit();
                edit.putString(KEY_APP_ACCOUNTS_FOR_TOKEN_REMOVAL, sb3);
                edit.apply();
            }
        }
    }

    public void removeAccounts() {
        new Thread(new Runnable() {
            public void run() {
                if (BrokerProxy.this.isBrokerAccountServiceSupported()) {
                    BrokerAccountServiceHandler.getInstance().removeAccounts(BrokerProxy.this.mContext);
                } else {
                    BrokerProxy.this.removeAccountFromAccountManager();
                }
            }
        }).start();
    }

    /* access modifiers changed from: private */
    public void removeAccountFromAccountManager() {
        Logger.m236v("BrokerProxy:removeAccountFromAccountManager", "Try to remove account from account manager.");
        Account[] accountsByType = this.mAcctManager.getAccountsByType(Broker.BROKER_ACCOUNT_TYPE);
        if (accountsByType.length != 0) {
            for (Account account : accountsByType) {
                StringBuilder sb = new StringBuilder();
                sb.append("Account: ");
                sb.append(account.name);
                Logger.m237v("BrokerProxy:removeAccountFromAccountManager", "Remove tokens for account. ", sb.toString(), null);
                Bundle bundle = new Bundle();
                bundle.putString(Broker.ACCOUNT_REMOVE_TOKENS, Broker.ACCOUNT_REMOVE_TOKENS_VALUE);
                this.mAcctManager.getAuthToken(account, Broker.AUTHTOKEN_TYPE, bundle, false, null, this.mHandler);
            }
        }
    }

    public Intent getIntentForBrokerActivity(AuthenticationRequest authenticationRequest, BrokerEvent brokerEvent) throws AuthenticationException {
        Intent intent;
        Bundle brokerOptions = getBrokerOptions(authenticationRequest);
        if (isBrokerAccountServiceSupported()) {
            intent = BrokerAccountServiceHandler.getInstance().getIntentForInteractiveRequest(this.mContext, brokerEvent);
            if (intent != null) {
                intent.putExtras(brokerOptions);
            } else {
                Logger.m231e(TAG, "Received null intent from broker interactive request.", null, ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING);
                throw new AuthenticationException(ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, "Received null intent from broker interactive request.");
            }
        } else {
            intent = getIntentForBrokerActivityFromAccountManager(brokerOptions);
        }
        if (intent != null) {
            intent.putExtra(Broker.BROKER_REQUEST, Broker.BROKER_REQUEST);
            if (!isBrokerWithPRTSupport(intent) && PromptBehavior.FORCE_PROMPT == authenticationRequest.getPrompt()) {
                Logger.m236v("BrokerProxy:getIntentForBrokerActivity", "FORCE_PROMPT is set for broker auth via old version of broker app, reset to ALWAYS.");
                intent.putExtra(Broker.ACCOUNT_PROMPT, PromptBehavior.Always.name());
            }
        }
        return intent;
    }

    private Intent getIntentForBrokerActivityFromAccountManager(Bundle bundle) {
        try {
            return (Intent) ((Bundle) this.mAcctManager.addAccount(Broker.BROKER_ACCOUNT_TYPE, Broker.AUTHTOKEN_TYPE, null, bundle, null, null, this.mHandler).getResult()).getParcelable("intent");
        } catch (OperationCanceledException e) {
            Logger.m232e("BrokerProxy:getIntentForBrokerActivityFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "", ADALError.AUTH_FAILED_CANCELLED, e);
        } catch (AuthenticatorException e2) {
            Logger.m232e("BrokerProxy:getIntentForBrokerActivityFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "", ADALError.BROKER_AUTHENTICATOR_NOT_RESPONDING, e2);
        } catch (IOException e3) {
            Logger.m232e("BrokerProxy:getIntentForBrokerActivityFromAccountManager", AUTHENTICATOR_CANCELS_REQUEST, "", ADALError.BROKER_AUTHENTICATOR_IO_EXCEPTION, e3);
        }
        return null;
    }

    public String getCurrentActiveBrokerPackageName() {
        AuthenticatorDescription[] authenticatorTypes;
        for (AuthenticatorDescription authenticatorDescription : this.mAcctManager.getAuthenticatorTypes()) {
            if (authenticatorDescription.type.equals(Broker.BROKER_ACCOUNT_TYPE)) {
                return authenticatorDescription.packageName;
            }
        }
        return null;
    }

    public String getBrokerAppVersion(String str) throws NameNotFoundException {
        PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(str, 0);
        StringBuilder sb = new StringBuilder();
        sb.append("VersionName=");
        sb.append(packageInfo.versionName);
        sb.append(";VersonCode=");
        sb.append(packageInfo.versionCode);
        sb.append(".");
        return sb.toString();
    }

    private Bundle getBrokerOptions(AuthenticationRequest authenticationRequest) {
        Bundle bundle = new Bundle();
        bundle.putInt(Browser.REQUEST_ID, authenticationRequest.getRequestId());
        bundle.putInt(Broker.EXPIRATION_BUFFER, AuthenticationSettings.INSTANCE.getExpirationBuffer());
        bundle.putString(Broker.ACCOUNT_AUTHORITY, authenticationRequest.getAuthority());
        bundle.putString(Broker.ACCOUNT_RESOURCE, authenticationRequest.getResource());
        bundle.putString(Broker.ACCOUNT_REDIRECT, authenticationRequest.getRedirectUri());
        bundle.putString(Broker.ACCOUNT_CLIENTID_KEY, authenticationRequest.getClientId());
        bundle.putString(Broker.ADAL_VERSION_KEY, authenticationRequest.getVersion());
        bundle.putString(Broker.ACCOUNT_USERINFO_USERID, authenticationRequest.getUserId());
        bundle.putString(Broker.ACCOUNT_EXTRA_QUERY_PARAM, authenticationRequest.getExtraQueryParamsAuthentication());
        if (authenticationRequest.getCorrelationId() != null) {
            bundle.putString(Broker.ACCOUNT_CORRELATIONID, authenticationRequest.getCorrelationId().toString());
        }
        String brokerAccountName = authenticationRequest.getBrokerAccountName();
        if (StringExtensions.isNullOrBlank(brokerAccountName)) {
            brokerAccountName = authenticationRequest.getLoginHint();
        }
        bundle.putString(Broker.ACCOUNT_LOGIN_HINT, brokerAccountName);
        bundle.putString(Broker.ACCOUNT_NAME, brokerAccountName);
        if (authenticationRequest.getPrompt() != null) {
            bundle.putString(Broker.ACCOUNT_PROMPT, authenticationRequest.getPrompt().name());
        }
        if (Utility.isClaimsChallengePresent(authenticationRequest)) {
            bundle.putString(Broker.BROKER_SKIP_CACHE, Boolean.toString(true));
            bundle.putString(Broker.ACCOUNT_CLAIMS, authenticationRequest.getClaimsChallenge());
        }
        return bundle;
    }

    private boolean isBrokerWithPRTSupport(Intent intent) {
        if (intent != null) {
            return Broker.BROKER_PROTOCOL_VERSION.equalsIgnoreCase(intent.getStringExtra(Broker.BROKER_VERSION));
        }
        throw new IllegalArgumentException("intent");
    }

    public String getCurrentUser() {
        String str = null;
        if (isBrokerAccountServiceSupported()) {
            verifyNotOnMainThread();
            try {
                UserInfo[] brokerUsers = BrokerAccountServiceHandler.getInstance().getBrokerUsers(this.mContext);
                if (brokerUsers.length != 0) {
                    str = brokerUsers[0].getDisplayableId();
                }
                return str;
            } catch (IOException e) {
                Logger.m232e("BrokerProxy:getCurrentUser", "No current user could be retrieved.", "", null, e);
                return null;
            }
        } else {
            Account[] accountsByType = this.mAcctManager.getAccountsByType(Broker.BROKER_ACCOUNT_TYPE);
            if (accountsByType.length > 0) {
                return accountsByType[0].name;
            }
            return null;
        }
    }

    private boolean checkAccount(AccountManager accountManager, String str, String str2) {
        AuthenticatorDescription[] authenticatorTypes;
        for (AuthenticatorDescription authenticatorDescription : accountManager.getAuthenticatorTypes()) {
            if (authenticatorDescription.type.equals(Broker.BROKER_ACCOUNT_TYPE)) {
                Account[] accountsByType = this.mAcctManager.getAccountsByType(Broker.BROKER_ACCOUNT_TYPE);
                if (authenticatorDescription.packageName.equalsIgnoreCase(Broker.AZURE_AUTHENTICATOR_APP_PACKAGE_NAME) || authenticatorDescription.packageName.equalsIgnoreCase(Broker.COMPANY_PORTAL_APP_PACKAGE_NAME) || authenticatorDescription.packageName.equalsIgnoreCase(AuthenticationSettings.INSTANCE.getBrokerPackageName())) {
                    if (hasSupportToAddUserThroughBroker(authenticatorDescription.packageName)) {
                        return true;
                    }
                    if (accountsByType.length > 0) {
                        return verifyAccount(accountsByType, str, str2);
                    }
                }
            }
        }
        return false;
    }

    private boolean verifyAccount(Account[] accountArr, String str, String str2) {
        if (!StringExtensions.isNullOrBlank(str)) {
            return str.equalsIgnoreCase(accountArr[0].name);
        }
        boolean z = true;
        if (StringExtensions.isNullOrBlank(str2)) {
            return true;
        }
        try {
            if (findUserInfo(str2, getBrokerUsers()) == null) {
                z = false;
            }
            return z;
        } catch (AuthenticatorException | OperationCanceledException | IOException e) {
            Logger.m232e("BrokerProxy:verifyAccount", "Exception thrown when verifying accounts in broker. ", e.getMessage(), ADALError.BROKER_AUTHENTICATOR_EXCEPTION, e);
            Logger.m236v("BrokerProxy:verifyAccount", "It could not check the uniqueid from broker. It is not using broker");
            return false;
        }
    }

    private boolean hasSupportToAddUserThroughBroker(String str) {
        Intent intent = new Intent();
        intent.setPackage(str);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".ui.AccountChooserActivity");
        intent.setClassName(str, sb.toString());
        if (this.mContext.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            return true;
        }
        return false;
    }

    private boolean verifySignature(String str) {
        try {
            List readCertDataForBrokerApp = readCertDataForBrokerApp(str);
            verifySignatureHash(readCertDataForBrokerApp);
            if (readCertDataForBrokerApp.size() > 1) {
                verifyCertificateChain(readCertDataForBrokerApp);
            }
            return true;
        } catch (NameNotFoundException unused) {
            Logger.m231e("BrokerProxy:verifySignature", "Broker related package does not exist", "", ADALError.BROKER_PACKAGE_NAME_NOT_FOUND);
            return false;
        } catch (NoSuchAlgorithmException unused2) {
            Logger.m231e("BrokerProxy:verifySignature", "Digest SHA algorithm does not exists", "", ADALError.DEVICE_NO_SUCH_ALGORITHM);
            return false;
        } catch (AuthenticationException | IOException | GeneralSecurityException e) {
            Logger.m232e("BrokerProxy:verifySignature", ADALError.BROKER_VERIFICATION_FAILED.getDescription(), e.getMessage(), ADALError.BROKER_VERIFICATION_FAILED, e);
            return false;
        }
    }

    private void verifySignatureHash(List<X509Certificate> list) throws NoSuchAlgorithmException, CertificateEncodingException, AuthenticationException {
        for (X509Certificate x509Certificate : list) {
            MessageDigest instance = MessageDigest.getInstance("SHA");
            instance.update(x509Certificate.getEncoded());
            String encodeToString = Base64.encodeToString(instance.digest(), 2);
            if (this.mBrokerTag.equals(encodeToString)) {
                return;
            }
            if (Broker.AZURE_AUTHENTICATOR_APP_SIGNATURE.equals(encodeToString)) {
                return;
            }
        }
        throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED);
    }

    @SuppressLint({"PackageManagerGetSignatures"})
    private List<X509Certificate> readCertDataForBrokerApp(String str) throws NameNotFoundException, AuthenticationException, IOException, GeneralSecurityException {
        PackageInfo packageInfo = this.mContext.getPackageManager().getPackageInfo(str, 64);
        if (packageInfo == null) {
            throw new AuthenticationException(ADALError.APP_PACKAGE_NAME_NOT_FOUND, "No broker package existed.");
        } else if (packageInfo.signatures == null || packageInfo.signatures.length == 0) {
            throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED, "No signature associated with the broker package.");
        } else {
            ArrayList arrayList = new ArrayList(packageInfo.signatures.length);
            Signature[] signatureArr = packageInfo.signatures;
            int length = signatureArr.length;
            int i = 0;
            while (i < length) {
                try {
                    arrayList.add((X509Certificate) CertificateFactory.getInstance("X509").generateCertificate(new ByteArrayInputStream(signatureArr[i].toByteArray())));
                    i++;
                } catch (CertificateException unused) {
                    throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED);
                }
            }
            return arrayList;
        }
    }

    private void verifyCertificateChain(List<X509Certificate> list) throws GeneralSecurityException, AuthenticationException {
        PKIXParameters pKIXParameters = new PKIXParameters(Collections.singleton(new TrustAnchor(getSelfSignedCert(list), null)));
        pKIXParameters.setRevocationEnabled(false);
        CertPathValidator.getInstance("PKIX").validate(CertificateFactory.getInstance("X.509").generateCertPath(list), pKIXParameters);
    }

    private X509Certificate getSelfSignedCert(List<X509Certificate> list) throws AuthenticationException {
        int i = 0;
        X509Certificate x509Certificate = null;
        for (X509Certificate x509Certificate2 : list) {
            if (x509Certificate2.getSubjectDN().equals(x509Certificate2.getIssuerDN())) {
                i++;
                x509Certificate = x509Certificate2;
            }
        }
        if (i <= 1 && x509Certificate != null) {
            return x509Certificate;
        }
        throw new AuthenticationException(ADALError.BROKER_APP_VERIFICATION_FAILED, "Multiple self signed certs found or no self signed cert existed.");
    }

    private boolean verifyAuthenticator(AccountManager accountManager) {
        AuthenticatorDescription[] authenticatorTypes;
        for (AuthenticatorDescription authenticatorDescription : accountManager.getAuthenticatorTypes()) {
            if (authenticatorDescription.type.equals(Broker.BROKER_ACCOUNT_TYPE) && verifySignature(authenticatorDescription.packageName)) {
                return true;
            }
        }
        return false;
    }

    public UserInfo[] getBrokerUsers() throws OperationCanceledException, AuthenticatorException, IOException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalArgumentException("Calling getBrokerUsers on main thread");
        } else if (isBrokerAccountServiceSupported()) {
            return BrokerAccountServiceHandler.getInstance().getBrokerUsers(this.mContext);
        } else {
            return getUserInfoFromAccountManager();
        }
    }

    private UserInfo[] getUserInfoFromAccountManager() throws OperationCanceledException, AuthenticatorException, IOException {
        Account[] accountsByType = this.mAcctManager.getAccountsByType(Broker.BROKER_ACCOUNT_TYPE);
        Bundle bundle = new Bundle();
        bundle.putBoolean(DATA_USER_INFO, true);
        StringBuilder sb = new StringBuilder();
        sb.append("Retrieve all the accounts from account manager with broker account type, and the account length is: ");
        sb.append(accountsByType.length);
        Logger.m236v("BrokerProxy:getUserInfoFromAccountManager", sb.toString());
        UserInfo[] userInfoArr = new UserInfo[accountsByType.length];
        for (int i = 0; i < accountsByType.length; i++) {
            AccountManagerFuture updateCredentials = this.mAcctManager.updateCredentials(accountsByType[i], Broker.AUTHTOKEN_TYPE, bundle, null, null, null);
            Logger.m236v("BrokerProxy:getUserInfoFromAccountManager", "Waiting for userinfo retrieval result from Broker.");
            Bundle bundle2 = (Bundle) updateCredentials.getResult();
            UserInfo userInfo = new UserInfo(bundle2.getString(Broker.ACCOUNT_USERINFO_USERID), bundle2.getString(Broker.ACCOUNT_USERINFO_GIVEN_NAME), bundle2.getString(Broker.ACCOUNT_USERINFO_FAMILY_NAME), bundle2.getString(Broker.ACCOUNT_USERINFO_IDENTITY_PROVIDER), bundle2.getString(Broker.ACCOUNT_USERINFO_USERID_DISPLAYABLE));
            userInfoArr[i] = userInfo;
        }
        return userInfoArr;
    }
}
