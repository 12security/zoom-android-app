package com.microsoft.aad.adal;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.security.KeyChainException;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.ClientCertRequest;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.gson.Gson;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import com.microsoft.aad.adal.AuthenticationResult.AuthenticationStatus;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Locale;
import java.util.UUID;

@SuppressLint({"SetJavaScriptEnabled", "ClickableViewAccessibility"})
public class AuthenticationActivity extends Activity {
    static final int BACK_PRESSED_CANCEL_DIALOG_STEPS = -2;
    private static final String TAG = "AuthenticationActivity";
    private AccountAuthenticatorResponse mAccountAuthenticatorResponse = null;
    /* access modifiers changed from: private */
    public AuthenticationRequest mAuthRequest;
    private Bundle mAuthenticatorResultBundle = null;
    /* access modifiers changed from: private */
    public String mCallingPackage;
    /* access modifiers changed from: private */
    public int mCallingUID;
    /* access modifiers changed from: private */
    public final IJWSBuilder mJWSBuilder = new JWSBuilder();
    /* access modifiers changed from: private */
    public boolean mPkeyAuthRedirect = false;
    private ActivityBroadcastReceiver mReceiver = null;
    /* access modifiers changed from: private */
    public String mRedirectUrl;
    private boolean mRegisterReceiver = false;
    private ProgressDialog mSpinner;
    private String mStartUrl;
    /* access modifiers changed from: private */
    public StorageHelper mStorageHelper;
    /* access modifiers changed from: private */
    public UIEvent mUIEvent = null;
    /* access modifiers changed from: private */
    public int mWaitingRequestId;
    /* access modifiers changed from: private */
    public final IWebRequestHandler mWebRequestHandler = new WebRequestHandler();
    /* access modifiers changed from: private */
    public WebView mWebView;

    private class ActivityBroadcastReceiver extends BroadcastReceiver {
        /* access modifiers changed from: private */
        public int mWaitingRequestId;

        private ActivityBroadcastReceiver() {
            this.mWaitingRequestId = -1;
        }

        public void onReceive(Context context, Intent intent) {
            Logger.m236v("AuthenticationActivity:onReceive", "ActivityBroadcastReceiver onReceive");
            if (intent.getAction().equalsIgnoreCase(Browser.ACTION_CANCEL)) {
                Logger.m236v("AuthenticationActivity:onReceive", "ActivityBroadcastReceiver onReceive action is for cancelling Authentication Activity");
                if (intent.getIntExtra(Browser.REQUEST_ID, 0) == this.mWaitingRequestId) {
                    Logger.m236v("AuthenticationActivity:onReceive", "Waiting requestId is same and cancelling this activity");
                    AuthenticationActivity.this.finish();
                }
            }
        }
    }

    class CustomWebViewClient extends BasicWebViewClient {
        CustomWebViewClient() {
            super(AuthenticationActivity.this, AuthenticationActivity.this.mRedirectUrl, AuthenticationActivity.this.mAuthRequest, AuthenticationActivity.this.mUIEvent);
        }

        public void processRedirectUrl(WebView webView, String str) {
            AuthenticationActivity authenticationActivity = AuthenticationActivity.this;
            if (!authenticationActivity.isBrokerRequest(authenticationActivity.getIntent())) {
                Logger.m234i("AuthenticationActivity:processRedirectUrl", "It is not a broker request", "");
                Intent intent = new Intent();
                intent.putExtra(Browser.RESPONSE_FINAL_URL, str);
                intent.putExtra(Browser.RESPONSE_REQUEST_INFO, AuthenticationActivity.this.mAuthRequest);
                AuthenticationActivity.this.returnToCaller(2003, intent);
                webView.stopLoading();
                return;
            }
            Logger.m234i("AuthenticationActivity:processRedirectUrl", "It is a broker request", "");
            AuthenticationActivity authenticationActivity2 = AuthenticationActivity.this;
            authenticationActivity2.displaySpinnerWithMessage(authenticationActivity2.getText(authenticationActivity2.getResources().getIdentifier("broker_processing", "string", AuthenticationActivity.this.getPackageName())));
            webView.stopLoading();
            AuthenticationActivity authenticationActivity3 = AuthenticationActivity.this;
            TokenTask tokenTask = new TokenTask(authenticationActivity3.mWebRequestHandler, AuthenticationActivity.this.mAuthRequest, AuthenticationActivity.this.mCallingPackage, AuthenticationActivity.this.mCallingUID);
            tokenTask.execute(new String[]{str});
        }

        public boolean processInvalidUrl(WebView webView, String str) {
            AuthenticationActivity authenticationActivity = AuthenticationActivity.this;
            if (authenticationActivity.isBrokerRequest(authenticationActivity.getIntent()) && str.startsWith(Broker.REDIRECT_PREFIX)) {
                Logger.m231e("AuthenticationActivity:processInvalidUrl", "The RedirectUri is not as expected.", String.format("Received %s and expected %s", new Object[]{str, AuthenticationActivity.this.mRedirectUrl}), ADALError.DEVELOPER_REDIRECTURI_INVALID);
                AuthenticationActivity.this.returnError(ADALError.DEVELOPER_REDIRECTURI_INVALID, String.format("The RedirectUri is not as expected. Received %s and expected %s", new Object[]{str, AuthenticationActivity.this.mRedirectUrl}));
                webView.stopLoading();
                return true;
            } else if (str.toLowerCase(Locale.US).equals(BasicWebViewClient.BLANK_PAGE)) {
                Logger.m236v("AuthenticationActivity:processInvalidUrl", "It is an blank page request");
                return true;
            } else if (str.toLowerCase(Locale.US).startsWith("https://")) {
                return false;
            } else {
                Logger.m231e("AuthenticationActivity:processInvalidUrl", "The webview was redirected to an unsafe URL.", "", ADALError.WEBVIEW_REDIRECTURL_NOT_SSL_PROTECTED);
                AuthenticationActivity.this.returnError(ADALError.WEBVIEW_REDIRECTURL_NOT_SSL_PROTECTED, "The webview was redirected to an unsafe URL.");
                webView.stopLoading();
                return true;
            }
        }

        public void showSpinner(boolean z) {
            AuthenticationActivity.this.displaySpinner(z);
        }

        public void sendResponse(int i, Intent intent) {
            AuthenticationActivity.this.returnToCaller(i, intent);
        }

        public void cancelWebViewRequest() {
            AuthenticationActivity.this.cancelRequest();
        }

        public void prepareForBrokerResumeRequest() {
            AuthenticationActivity.this.prepareForBrokerResume();
        }

        public void setPKeyAuthStatus(boolean z) {
            AuthenticationActivity.this.mPkeyAuthRedirect = z;
        }

        public void postRunnable(Runnable runnable) {
            AuthenticationActivity.this.mWebView.post(runnable);
        }

        @TargetApi(21)
        public void onReceivedClientCertRequest(WebView webView, final ClientCertRequest clientCertRequest) {
            Logger.m236v("AuthenticationActivity:onReceivedClientCertRequest", "Webview receives client TLS request.");
            Principal[] principals = clientCertRequest.getPrincipals();
            if (principals != null) {
                for (Principal name : principals) {
                    if (name.getName().contains("CN=MS-Organization-Access")) {
                        Logger.m236v("AuthenticationActivity:onReceivedClientCertRequest", "Cancelling the TLS request, not respond to TLS challenge triggered by device authentication.");
                        clientCertRequest.cancel();
                        return;
                    }
                }
            }
            KeyChain.choosePrivateKeyAlias(AuthenticationActivity.this, new KeyChainAliasCallback() {
                public void alias(String str) {
                    if (str == null) {
                        Logger.m236v("AuthenticationActivity:onReceivedClientCertRequest", "No certificate chosen by user, cancelling the TLS request.");
                        clientCertRequest.cancel();
                        return;
                    }
                    try {
                        X509Certificate[] certificateChain = KeyChain.getCertificateChain(AuthenticationActivity.this.getApplicationContext(), str);
                        PrivateKey privateKey = KeyChain.getPrivateKey(CustomWebViewClient.this.getCallingContext(), str);
                        Logger.m236v("AuthenticationActivity:onReceivedClientCertRequest", "Certificate is chosen by user, proceed with TLS request.");
                        clientCertRequest.proceed(privateKey, certificateChain);
                    } catch (KeyChainException e) {
                        Logger.m233e("AuthenticationActivity:onReceivedClientCertRequest", "KeyChain exception", e);
                        clientCertRequest.cancel();
                    } catch (InterruptedException e2) {
                        Logger.m233e("AuthenticationActivity:onReceivedClientCertRequest", "InterruptedException exception", e2);
                        clientCertRequest.cancel();
                    }
                }
            }, clientCertRequest.getKeyTypes(), clientCertRequest.getPrincipals(), clientCertRequest.getHost(), clientCertRequest.getPort(), null);
        }
    }

    class TokenTask extends AsyncTask<String, String, TokenTaskResult> {
        private AccountManager mAccountManager;
        private int mAppCallingUID;
        private String mPackageName;
        private AuthenticationRequest mRequest;
        private IWebRequestHandler mRequestHandler;

        public TokenTask() {
        }

        public TokenTask(IWebRequestHandler iWebRequestHandler, AuthenticationRequest authenticationRequest, String str, int i) {
            this.mRequestHandler = iWebRequestHandler;
            this.mRequest = authenticationRequest;
            this.mPackageName = str;
            this.mAppCallingUID = i;
            this.mAccountManager = AccountManager.get(AuthenticationActivity.this);
        }

        /* access modifiers changed from: protected */
        public TokenTaskResult doInBackground(String... strArr) {
            Oauth2 oauth2 = new Oauth2(this.mRequest, this.mRequestHandler, AuthenticationActivity.this.mJWSBuilder);
            TokenTaskResult tokenTaskResult = new TokenTaskResult();
            try {
                tokenTaskResult.mTaskResult = oauth2.getToken(strArr[0]);
                Logger.m237v(AuthenticationActivity.TAG, "Process result returned from TokenTask. ", this.mRequest.getLogInfo(), null);
            } catch (AuthenticationException | IOException e) {
                Logger.m232e(AuthenticationActivity.TAG, "Error in processing code to get a token. ", this.mRequest.getLogInfo(), ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, e);
                tokenTaskResult.mTaskException = e;
            }
            if (!(tokenTaskResult.mTaskResult == null || tokenTaskResult.mTaskResult.getAccessToken() == null)) {
                Logger.m237v(AuthenticationActivity.TAG, "Token task successfully returns access token.", this.mRequest.getLogInfo(), null);
                try {
                    setAccount(tokenTaskResult);
                } catch (IOException | GeneralSecurityException e2) {
                    Logger.m232e(AuthenticationActivity.TAG, "Error in setting the account", this.mRequest.getLogInfo(), ADALError.BROKER_ACCOUNT_SAVE_FAILED, e2);
                    tokenTaskResult.mTaskException = e2;
                }
            }
            return tokenTaskResult;
        }

        private String getBrokerAppCacheKey(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            StringBuilder sb = new StringBuilder();
            sb.append(Broker.USERDATA_UID_KEY);
            sb.append(this.mAppCallingUID);
            sb.append(str);
            String createHash = StringExtensions.createHash(sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Key hash is:");
            sb2.append(createHash);
            sb2.append(" calling app UID:");
            sb2.append(this.mAppCallingUID);
            sb2.append(" Key is: ");
            sb2.append(str);
            Logger.m237v(AuthenticationActivity.TAG, "Get broker app cache key.", sb2.toString(), null);
            return createHash;
        }

        private void appendAppUIDToAccount(Account account) throws GeneralSecurityException, IOException {
            String str;
            String userData = this.mAccountManager.getUserData(account, Broker.ACCOUNT_UID_CACHES);
            if (userData == null) {
                str = "";
            } else {
                try {
                    str = AuthenticationActivity.this.mStorageHelper.decrypt(userData);
                } catch (IOException | GeneralSecurityException e) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("appIdList:");
                    sb.append(userData);
                    Logger.m232e("AuthenticationActivity:appendAppUIDToAccount", "appUIDList failed to decrypt", sb.toString(), ADALError.ENCRYPTION_FAILED, e);
                    str = "";
                    Logger.m234i("AuthenticationActivity:appendAppUIDToAccount", "Reset the appUIDlist", "");
                }
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("App UID: ");
            sb2.append(this.mAppCallingUID);
            sb2.append("appIdList:");
            sb2.append(str);
            Logger.m235i("AuthenticationActivity:appendAppUIDToAccount", "Add calling UID. ", sb2.toString(), null);
            StringBuilder sb3 = new StringBuilder();
            sb3.append(Broker.USERDATA_UID_KEY);
            sb3.append(this.mAppCallingUID);
            if (!str.contains(sb3.toString())) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("App UID: ");
                sb4.append(this.mAppCallingUID);
                Logger.m235i("AuthenticationActivity:appendAppUIDToAccount", "Account has new calling UID. ", sb4.toString(), null);
                StorageHelper access$2000 = AuthenticationActivity.this.mStorageHelper;
                StringBuilder sb5 = new StringBuilder();
                sb5.append(str);
                sb5.append(Broker.USERDATA_UID_KEY);
                sb5.append(this.mAppCallingUID);
                this.mAccountManager.setUserData(account, Broker.ACCOUNT_UID_CACHES, access$2000.encrypt(sb5.toString()));
            }
        }

        private void setAccount(TokenTaskResult tokenTaskResult) throws GeneralSecurityException, IOException {
            String brokerAccountName = this.mRequest.getBrokerAccountName();
            Account[] accountsByType = this.mAccountManager.getAccountsByType(Broker.BROKER_ACCOUNT_TYPE);
            if (accountsByType.length != 1) {
                tokenTaskResult.mTaskResult = null;
                tokenTaskResult.mTaskException = new AuthenticationException(ADALError.BROKER_SINGLE_USER_EXPECTED);
                return;
            }
            Account account = accountsByType[0];
            UserInfo userInfo = tokenTaskResult.mTaskResult.getUserInfo();
            if (userInfo == null || StringExtensions.isNullOrBlank(userInfo.getUserId())) {
                Logger.m234i("AuthenticationActivity:setAccount", "Set userinfo from account", "");
                AuthenticationResult access$1800 = tokenTaskResult.mTaskResult;
                UserInfo userInfo2 = new UserInfo(brokerAccountName, brokerAccountName, "", "", brokerAccountName);
                access$1800.setUserInfo(userInfo2);
                this.mRequest.setLoginHint(brokerAccountName);
            } else {
                Logger.m234i("AuthenticationActivity:setAccount", "Saving userinfo to account", "");
                this.mAccountManager.setUserData(account, Broker.ACCOUNT_USERINFO_USERID, userInfo.getUserId());
                this.mAccountManager.setUserData(account, Broker.ACCOUNT_USERINFO_GIVEN_NAME, userInfo.getGivenName());
                this.mAccountManager.setUserData(account, Broker.ACCOUNT_USERINFO_FAMILY_NAME, userInfo.getFamilyName());
                this.mAccountManager.setUserData(account, Broker.ACCOUNT_USERINFO_IDENTITY_PROVIDER, userInfo.getIdentityProvider());
                this.mAccountManager.setUserData(account, Broker.ACCOUNT_USERINFO_USERID_DISPLAYABLE, userInfo.getDisplayableId());
            }
            tokenTaskResult.mAccountName = brokerAccountName;
            StringBuilder sb = new StringBuilder();
            sb.append("Package: ");
            sb.append(this.mPackageName);
            sb.append(" calling app UID:");
            sb.append(this.mAppCallingUID);
            sb.append(" Account name: ");
            sb.append(brokerAccountName);
            Logger.m234i("AuthenticationActivity:setAccount", "Setting account in account manager. ", sb.toString());
            Gson gson = new Gson();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("app context:");
            sb2.append(AuthenticationActivity.this.getApplicationContext().getPackageName());
            sb2.append(" context:");
            sb2.append(AuthenticationActivity.this.getPackageName());
            sb2.append(" calling packagename:");
            sb2.append(AuthenticationActivity.this.getCallingPackage());
            Logger.m234i("AuthenticationActivity:setAccount", sb2.toString(), "");
            if (AuthenticationSettings.INSTANCE.getSecretKeyData() == null) {
                Logger.m234i("AuthenticationActivity:setAccount", "Calling app doesn't provide the secret key.", "");
            }
            String encrypt = AuthenticationActivity.this.mStorageHelper.encrypt(gson.toJson((Object) TokenCacheItem.createRegularTokenCacheItem(this.mRequest.getAuthority(), this.mRequest.getResource(), this.mRequest.getClientId(), tokenTaskResult.mTaskResult)));
            String createCacheKeyForRTEntry = CacheKey.createCacheKeyForRTEntry(AuthenticationActivity.this.mAuthRequest.getAuthority(), AuthenticationActivity.this.mAuthRequest.getResource(), AuthenticationActivity.this.mAuthRequest.getClientId(), null);
            saveCacheKey(createCacheKeyForRTEntry, account, this.mAppCallingUID);
            this.mAccountManager.setUserData(account, getBrokerAppCacheKey(createCacheKeyForRTEntry), encrypt);
            if (tokenTaskResult.mTaskResult.getIsMultiResourceRefreshToken()) {
                String encrypt2 = AuthenticationActivity.this.mStorageHelper.encrypt(gson.toJson((Object) TokenCacheItem.createMRRTTokenCacheItem(this.mRequest.getAuthority(), this.mRequest.getClientId(), tokenTaskResult.mTaskResult)));
                String createCacheKeyForMRRT = CacheKey.createCacheKeyForMRRT(AuthenticationActivity.this.mAuthRequest.getAuthority(), AuthenticationActivity.this.mAuthRequest.getClientId(), null);
                saveCacheKey(createCacheKeyForMRRT, account, this.mAppCallingUID);
                this.mAccountManager.setUserData(account, getBrokerAppCacheKey(createCacheKeyForMRRT), encrypt2);
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Set calling uid:");
            sb3.append(this.mAppCallingUID);
            Logger.m234i("AuthenticationActivity:setAccount", sb3.toString(), "");
            appendAppUIDToAccount(account);
        }

        private void saveCacheKey(String str, Account account, int i) {
            Logger.m236v("AuthenticationActivity:saveCacheKey", "Get CacheKeys for account");
            AccountManager accountManager = this.mAccountManager;
            StringBuilder sb = new StringBuilder();
            sb.append(Broker.USERDATA_CALLER_CACHEKEYS);
            sb.append(i);
            String userData = accountManager.getUserData(account, sb.toString());
            if (userData == null) {
                userData = "";
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(Broker.CALLER_CACHEKEY_PREFIX);
            sb2.append(str);
            if (!userData.contains(sb2.toString())) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("callerUID: ");
                sb3.append(i);
                sb3.append("The key to be saved is: ");
                sb3.append(str);
                Logger.m237v("AuthenticationActivity:saveCacheKey", "Account does not have the cache key. Saving it to account for the caller. ", sb3.toString(), null);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(userData);
                sb4.append(Broker.CALLER_CACHEKEY_PREFIX);
                sb4.append(str);
                String sb5 = sb4.toString();
                AccountManager accountManager2 = this.mAccountManager;
                StringBuilder sb6 = new StringBuilder();
                sb6.append(Broker.USERDATA_CALLER_CACHEKEYS);
                sb6.append(i);
                accountManager2.setUserData(account, sb6.toString(), sb5);
                StringBuilder sb7 = new StringBuilder();
                sb7.append("keylist:");
                sb7.append(sb5);
                Logger.m237v("AuthenticationActivity:saveCacheKey", "Cache key saved into key list for the caller.", sb7.toString(), null);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(TokenTaskResult tokenTaskResult) {
            Logger.m236v(AuthenticationActivity.TAG, "Token task returns the result");
            AuthenticationActivity.this.displaySpinner(false);
            Intent intent = new Intent();
            if (tokenTaskResult.mTaskResult == null) {
                Logger.m236v(AuthenticationActivity.TAG, "Token task has exception");
                AuthenticationActivity.this.returnError(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, tokenTaskResult.mTaskException.getMessage());
                return;
            }
            if (tokenTaskResult.mTaskResult.getStatus().equals(AuthenticationStatus.Succeeded)) {
                intent.putExtra(Browser.REQUEST_ID, AuthenticationActivity.this.mWaitingRequestId);
                intent.putExtra(Broker.ACCOUNT_ACCESS_TOKEN, tokenTaskResult.mTaskResult.getAccessToken());
                intent.putExtra(Broker.ACCOUNT_NAME, tokenTaskResult.mAccountName);
                if (tokenTaskResult.mTaskResult.getExpiresOn() != null) {
                    intent.putExtra(Broker.ACCOUNT_EXPIREDATE, tokenTaskResult.mTaskResult.getExpiresOn().getTime());
                }
                if (tokenTaskResult.mTaskResult.getTenantId() != null) {
                    intent.putExtra(Broker.ACCOUNT_USERINFO_TENANTID, tokenTaskResult.mTaskResult.getTenantId());
                }
                UserInfo userInfo = tokenTaskResult.mTaskResult.getUserInfo();
                if (userInfo != null) {
                    intent.putExtra(Broker.ACCOUNT_USERINFO_USERID, userInfo.getUserId());
                    intent.putExtra(Broker.ACCOUNT_USERINFO_GIVEN_NAME, userInfo.getGivenName());
                    intent.putExtra(Broker.ACCOUNT_USERINFO_FAMILY_NAME, userInfo.getFamilyName());
                    intent.putExtra(Broker.ACCOUNT_USERINFO_IDENTITY_PROVIDER, userInfo.getIdentityProvider());
                    intent.putExtra(Broker.ACCOUNT_USERINFO_USERID_DISPLAYABLE, userInfo.getDisplayableId());
                }
                AuthenticationActivity.this.returnResult(2004, intent);
            } else {
                AuthenticationActivity.this.returnError(ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, tokenTaskResult.mTaskResult.getErrorDescription());
            }
        }
    }

    class TokenTaskResult {
        /* access modifiers changed from: private */
        public String mAccountName;
        /* access modifiers changed from: private */
        public Exception mTaskException;
        /* access modifiers changed from: private */
        public AuthenticationResult mTaskResult;

        TokenTaskResult() {
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled"})
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getResources().getIdentifier("activity_authentication", "layout", getPackageName()));
        CookieSyncManager.createInstance(getApplicationContext());
        CookieSyncManager.getInstance().sync();
        CookieManager.getInstance().setAcceptCookie(true);
        Logger.m236v("AuthenticationActivity:onCreate", "AuthenticationActivity was created.");
        this.mAuthRequest = getAuthenticationRequestFromIntent(getIntent());
        AuthenticationRequest authenticationRequest = this.mAuthRequest;
        if (authenticationRequest == null) {
            Logger.m230d("AuthenticationActivity:onCreate", "Intent for Authentication Activity doesn't have the request details, returning to caller");
            Intent intent = new Intent();
            intent.putExtra(Browser.RESPONSE_ERROR_CODE, Browser.WEBVIEW_INVALID_REQUEST);
            intent.putExtra(Browser.RESPONSE_ERROR_MESSAGE, "Intent does not have request details");
            returnToCaller(2002, intent);
        } else if (authenticationRequest.getAuthority() == null || this.mAuthRequest.getAuthority().isEmpty()) {
            returnError(ADALError.ARGUMENT_EXCEPTION, Broker.ACCOUNT_AUTHORITY);
        } else if (this.mAuthRequest.getResource() == null || this.mAuthRequest.getResource().isEmpty()) {
            returnError(ADALError.ARGUMENT_EXCEPTION, Broker.ACCOUNT_RESOURCE);
        } else if (this.mAuthRequest.getClientId() == null || this.mAuthRequest.getClientId().isEmpty()) {
            returnError(ADALError.ARGUMENT_EXCEPTION, Broker.ACCOUNT_CLIENTID_KEY);
        } else if (this.mAuthRequest.getRedirectUri() == null || this.mAuthRequest.getRedirectUri().isEmpty()) {
            returnError(ADALError.ARGUMENT_EXCEPTION, Broker.ACCOUNT_REDIRECT);
        } else {
            this.mRedirectUrl = this.mAuthRequest.getRedirectUri();
            Telemetry.getInstance().startEvent(this.mAuthRequest.getTelemetryRequestId(), "Microsoft.ADAL.ui_event");
            this.mUIEvent = new UIEvent("Microsoft.ADAL.ui_event");
            this.mUIEvent.setRequestId(this.mAuthRequest.getTelemetryRequestId());
            this.mUIEvent.setCorrelationId(this.mAuthRequest.getCorrelationId().toString());
            this.mWebView = (WebView) findViewById(getResources().getIdentifier("webView1", "id", getPackageName()));
            if (!AuthenticationSettings.INSTANCE.getDisableWebViewHardwareAcceleration()) {
                this.mWebView.setLayerType(1, null);
                Logger.m230d("AuthenticationActivity:onCreate", "Hardware acceleration is disabled in WebView");
            }
            this.mStartUrl = BasicWebViewClient.BLANK_PAGE;
            try {
                this.mStartUrl = new Oauth2(this.mAuthRequest).getCodeRequestUrl();
                StringBuilder sb = new StringBuilder();
                sb.append("Init broadcastReceiver with request. RequestId:");
                sb.append(this.mAuthRequest.getRequestId());
                Logger.m237v("AuthenticationActivity:onCreate", sb.toString(), this.mAuthRequest.getLogInfo(), null);
                this.mReceiver = new ActivityBroadcastReceiver();
                this.mReceiver.mWaitingRequestId = this.mAuthRequest.getRequestId();
                LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, new IntentFilter(Browser.ACTION_CANCEL));
                String userAgentString = this.mWebView.getSettings().getUserAgentString();
                WebSettings settings = this.mWebView.getSettings();
                StringBuilder sb2 = new StringBuilder();
                sb2.append(userAgentString);
                sb2.append(Broker.CLIENT_TLS_NOT_SUPPORTED);
                settings.setUserAgentString(sb2.toString());
                String userAgentString2 = this.mWebView.getSettings().getUserAgentString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("UserAgent:");
                sb3.append(userAgentString2);
                Logger.m237v("AuthenticationActivity:onCreate", "", sb3.toString(), null);
                if (isBrokerRequest(getIntent())) {
                    this.mCallingPackage = getCallingPackage();
                    if (this.mCallingPackage == null) {
                        Logger.m236v("AuthenticationActivity:onCreate", "Calling package is null, startActivityForResult is not used to call this activity");
                        Intent intent2 = new Intent();
                        intent2.putExtra(Browser.RESPONSE_ERROR_CODE, Browser.WEBVIEW_INVALID_REQUEST);
                        intent2.putExtra(Browser.RESPONSE_ERROR_MESSAGE, "startActivityForResult is not used to call this activity");
                        returnToCaller(2002, intent2);
                        return;
                    }
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("It is a broker request for package:");
                    sb4.append(this.mCallingPackage);
                    Logger.m234i("AuthenticationActivity:onCreate", sb4.toString(), "");
                    this.mAccountAuthenticatorResponse = (AccountAuthenticatorResponse) getIntent().getParcelableExtra("accountAuthenticatorResponse");
                    AccountAuthenticatorResponse accountAuthenticatorResponse = this.mAccountAuthenticatorResponse;
                    if (accountAuthenticatorResponse != null) {
                        accountAuthenticatorResponse.onRequestContinued();
                    }
                    PackageHelper packageHelper = new PackageHelper(this);
                    this.mCallingPackage = getCallingPackage();
                    this.mCallingUID = packageHelper.getUIDForPackage(this.mCallingPackage);
                    String currentSignatureForPackage = packageHelper.getCurrentSignatureForPackage(this.mCallingPackage);
                    this.mStartUrl = getBrokerStartUrl(this.mStartUrl, this.mCallingPackage, currentSignatureForPackage);
                    if (!isCallerBrokerInstaller()) {
                        Logger.m236v("AuthenticationActivity:onCreate", "Caller needs to be verified using special redirectUri");
                        this.mRedirectUrl = PackageHelper.getBrokerRedirectUrl(this.mCallingPackage, currentSignatureForPackage);
                    }
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Broker redirectUrl: ");
                    sb5.append(this.mRedirectUrl);
                    sb5.append(" The calling package is: ");
                    sb5.append(this.mCallingPackage);
                    sb5.append(" Signature hash for calling package is: ");
                    sb5.append(currentSignatureForPackage);
                    sb5.append(" Current context package: ");
                    sb5.append(getPackageName());
                    sb5.append(" Start url: ");
                    sb5.append(this.mStartUrl);
                    Logger.m237v("AuthenticationActivity:onCreate", "", sb5.toString(), null);
                } else {
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("Non-broker request for package ");
                    sb6.append(getCallingPackage());
                    String sb7 = sb6.toString();
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(" Start url: ");
                    sb8.append(this.mStartUrl);
                    Logger.m237v("AuthenticationActivity:onCreate", sb7, sb8.toString(), null);
                }
                this.mRegisterReceiver = false;
                final String str = this.mStartUrl;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Device info:");
                sb9.append(VERSION.RELEASE);
                sb9.append(OAuth.SCOPE_DELIMITER);
                sb9.append(Build.MANUFACTURER);
                sb9.append(Build.MODEL);
                Logger.m234i("AuthenticationActivity:onCreate", sb9.toString(), "");
                this.mStorageHelper = new StorageHelper(getApplicationContext());
                setupWebView();
                if (this.mAuthRequest.getCorrelationId() == null) {
                    Logger.m236v("AuthenticationActivity:onCreate", "Null correlation id in the request.");
                } else {
                    StringBuilder sb10 = new StringBuilder();
                    sb10.append("Correlation id for request sent is:");
                    sb10.append(this.mAuthRequest.getCorrelationId().toString());
                    Logger.m236v("AuthenticationActivity:onCreate", sb10.toString());
                }
                if (bundle == null) {
                    this.mWebView.post(new Runnable() {
                        public void run() {
                            Logger.m236v("AuthenticationActivity:onCreate", "Launching webview for acquiring auth code.");
                            AuthenticationActivity.this.mWebView.loadUrl(BasicWebViewClient.BLANK_PAGE);
                            AuthenticationActivity.this.mWebView.loadUrl(str);
                        }
                    });
                } else {
                    Logger.m236v("AuthenticationActivity:onCreate", "Reuse webview");
                }
            } catch (UnsupportedEncodingException e) {
                Logger.m237v("AuthenticationActivity:onCreate", "Encoding format is not supported. ", e.getMessage(), null);
                Intent intent3 = new Intent();
                intent3.putExtra(Browser.RESPONSE_REQUEST_INFO, this.mAuthRequest);
                returnToCaller(2002, intent3);
            }
        }
    }

    private boolean isCallerBrokerInstaller() {
        PackageHelper packageHelper = new PackageHelper(this);
        String callingPackage = getCallingPackage();
        boolean z = false;
        if (StringExtensions.isNullOrBlank(callingPackage)) {
            return false;
        }
        if (callingPackage.equals(AuthenticationSettings.INSTANCE.getBrokerPackageName())) {
            Logger.m236v("AuthenticationActivity:isCallerBrokerInstaller", "Same package as broker.");
            return true;
        }
        String currentSignatureForPackage = packageHelper.getCurrentSignatureForPackage(callingPackage);
        StringBuilder sb = new StringBuilder();
        sb.append("Check signature for ");
        sb.append(callingPackage);
        sb.append(" signature:");
        sb.append(currentSignatureForPackage);
        sb.append(" brokerSignature:");
        sb.append(AuthenticationSettings.INSTANCE.getBrokerSignature());
        Logger.m237v("AuthenticationActivity:isCallerBrokerInstaller", "Checking broker signature. ", sb.toString(), null);
        if (currentSignatureForPackage.equals(AuthenticationSettings.INSTANCE.getBrokerSignature()) || currentSignatureForPackage.equals(Broker.AZURE_AUTHENTICATOR_APP_SIGNATURE)) {
            z = true;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        this.mWebView.saveState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mWebView.restoreState(bundle);
    }

    private void setupWebView() {
        this.mWebView.getSettings().setJavaScriptEnabled(true);
        this.mWebView.requestFocus(130);
        this.mWebView.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                if ((action == 0 || action == 1) && !view.hasFocus()) {
                    view.requestFocus();
                }
                return false;
            }
        });
        this.mWebView.getSettings().setLoadWithOverviewMode(true);
        this.mWebView.getSettings().setDomStorageEnabled(true);
        this.mWebView.getSettings().setUseWideViewPort(true);
        this.mWebView.getSettings().setBuiltInZoomControls(true);
        this.mWebView.setWebViewClient(new CustomWebViewClient());
        this.mWebView.setVisibility(4);
    }

    private AuthenticationRequest getAuthenticationRequestFromIntent(Intent intent) {
        UUID uuid;
        if (isBrokerRequest(intent)) {
            Logger.m236v("AuthenticationActivity:getAuthenticationRequestFromIntent", "It is a broker request. Get request info from bundle extras.");
            String stringExtra = intent.getStringExtra(Broker.ACCOUNT_AUTHORITY);
            String stringExtra2 = intent.getStringExtra(Broker.ACCOUNT_RESOURCE);
            String stringExtra3 = intent.getStringExtra(Broker.ACCOUNT_REDIRECT);
            String stringExtra4 = intent.getStringExtra(Broker.ACCOUNT_LOGIN_HINT);
            String stringExtra5 = intent.getStringExtra(Broker.ACCOUNT_NAME);
            String stringExtra6 = intent.getStringExtra(Broker.ACCOUNT_CLIENTID_KEY);
            String stringExtra7 = intent.getStringExtra(Broker.ACCOUNT_CORRELATIONID);
            String stringExtra8 = intent.getStringExtra(Broker.ACCOUNT_PROMPT);
            PromptBehavior valueOf = !StringExtensions.isNullOrBlank(stringExtra8) ? PromptBehavior.valueOf(stringExtra8) : PromptBehavior.Auto;
            this.mWaitingRequestId = intent.getIntExtra(Browser.REQUEST_ID, 0);
            if (!StringExtensions.isNullOrBlank(stringExtra7)) {
                try {
                    uuid = UUID.fromString(stringExtra7);
                } catch (IllegalArgumentException unused) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("CorrelationId is malformed: ");
                    sb.append(stringExtra7);
                    Logger.m231e("AuthenticationActivity:getAuthenticationRequestFromIntent", sb.toString(), "", ADALError.CORRELATION_ID_FORMAT);
                    uuid = null;
                }
            } else {
                uuid = null;
            }
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(stringExtra, stringExtra2, stringExtra6, stringExtra3, stringExtra4, uuid, false);
            authenticationRequest.setBrokerAccountName(stringExtra5);
            authenticationRequest.setPrompt(valueOf);
            authenticationRequest.setRequestId(this.mWaitingRequestId);
            return authenticationRequest;
        }
        Serializable serializableExtra = intent.getSerializableExtra(Browser.REQUEST_MESSAGE);
        if (serializableExtra instanceof AuthenticationRequest) {
            return (AuthenticationRequest) serializableExtra;
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void returnError(ADALError aDALError, String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Argument error:");
        sb.append(str);
        Logger.m238w(str2, sb.toString());
        Intent intent = new Intent();
        intent.putExtra(Browser.RESPONSE_ERROR_CODE, aDALError.name());
        intent.putExtra(Browser.RESPONSE_ERROR_MESSAGE, str);
        if (this.mAuthRequest != null) {
            intent.putExtra(Browser.REQUEST_ID, this.mWaitingRequestId);
            intent.putExtra(Browser.RESPONSE_REQUEST_INFO, this.mAuthRequest);
        }
        setResult(2002, intent);
        finish();
    }

    private String getBrokerStartUrl(String str, String str2, String str3) {
        if (!StringExtensions.isNullOrBlank(str2) && !StringExtensions.isNullOrBlank(str3)) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append("&package_name=");
                sb.append(URLEncoder.encode(str2, "UTF_8"));
                sb.append("&signature=");
                sb.append(URLEncoder.encode(str3, "UTF_8"));
                return sb.toString();
            } catch (UnsupportedEncodingException e) {
                Logger.m233e(TAG, "Encoding", e);
            }
        }
        return str;
    }

    /* access modifiers changed from: private */
    public boolean isBrokerRequest(Intent intent) {
        return intent != null && !StringExtensions.isNullOrBlank(intent.getStringExtra(Broker.BROKER_REQUEST));
    }

    /* access modifiers changed from: private */
    public void returnToCaller(int i, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Return To Caller:");
        sb.append(i);
        Logger.m236v("AuthenticationActivity:returnToCaller", sb.toString());
        displaySpinner(false);
        if (intent == null) {
            intent = new Intent();
        }
        if (this.mAuthRequest == null) {
            Logger.m239w("AuthenticationActivity:returnToCaller", "Request object is null", "", ADALError.ACTIVITY_REQUEST_INTENT_DATA_IS_NULL);
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Set request id related to response. REQUEST_ID for caller returned to:");
            sb2.append(this.mAuthRequest.getRequestId());
            Logger.m236v("AuthenticationActivity:returnToCaller", sb2.toString());
            intent.putExtra(Browser.REQUEST_ID, this.mAuthRequest.getRequestId());
        }
        setResult(i, intent);
        finish();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        Logger.m236v("AuthenticationActivity:onPause", "AuthenticationActivity onPause unregister receiver");
        super.onPause();
        if (this.mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.mReceiver);
        }
        this.mRegisterReceiver = true;
        if (this.mSpinner != null) {
            Logger.m236v("AuthenticationActivity:onPause", "Spinner at onPause will dismiss");
            this.mSpinner.dismiss();
        }
        hideKeyBoard();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mRegisterReceiver) {
            StringBuilder sb = new StringBuilder();
            sb.append("StartUrl: ");
            sb.append(this.mStartUrl);
            Logger.m237v("AuthenticationActivity:onResume", "Webview onResume will register receiver. ", sb.toString(), null);
            if (this.mReceiver != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Webview onResume register broadcast receiver for request. RequestId: ");
                sb2.append(this.mReceiver.mWaitingRequestId);
                Logger.m236v("AuthenticationActivity:onResume", sb2.toString());
                LocalBroadcastManager.getInstance(this).registerReceiver(this.mReceiver, new IntentFilter(Browser.ACTION_CANCEL));
            }
        }
        this.mRegisterReceiver = false;
        this.mSpinner = new ProgressDialog(this);
        this.mSpinner.requestWindowFeature(1);
        this.mSpinner.setMessage(getText(getResources().getIdentifier("app_loading", "string", getPackageName())));
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        Logger.m236v(TAG, "AuthenticationActivity onRestart");
        super.onRestart();
        this.mRegisterReceiver = true;
    }

    public void onBackPressed() {
        Logger.m236v(TAG, "Back button is pressed");
        if (this.mPkeyAuthRedirect || !this.mWebView.canGoBackOrForward(-2)) {
            cancelRequest();
        } else {
            this.mWebView.goBack();
        }
    }

    /* access modifiers changed from: private */
    public void cancelRequest() {
        Logger.m236v(TAG, "Sending intent to cancel authentication activity");
        Intent intent = new Intent();
        UIEvent uIEvent = this.mUIEvent;
        if (uIEvent != null) {
            uIEvent.setUserCancel();
        }
        returnToCaller(2001, intent);
    }

    /* access modifiers changed from: private */
    public void prepareForBrokerResume() {
        Logger.m236v("AuthenticationActivity:prepareForBrokerResume", "Return to caller with BROKER_REQUEST_RESUME, and waiting for result.");
        returnToCaller(2006, new Intent());
    }

    private void hideKeyBoard() {
        if (this.mWebView != null) {
            ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(this.mWebView.getApplicationWindowToken(), 0);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mUIEvent != null) {
            Telemetry.getInstance().stopEvent(this.mAuthRequest.getTelemetryRequestId(), this.mUIEvent, "Microsoft.ADAL.ui_event");
        }
    }

    /* access modifiers changed from: private */
    public void displaySpinner(boolean z) {
        if (!isFinishing() && !isChangingConfigurations() && this.mSpinner != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("DisplaySpinner:");
            sb.append(z);
            sb.append(" showing:");
            sb.append(this.mSpinner.isShowing());
            Logger.m236v("AuthenticationActivity:displaySpinner", sb.toString());
            if (z && !this.mSpinner.isShowing()) {
                this.mSpinner.show();
            }
            if (!z && this.mSpinner.isShowing()) {
                this.mSpinner.dismiss();
            }
        }
    }

    /* access modifiers changed from: private */
    public void displaySpinnerWithMessage(CharSequence charSequence) {
        if (!isFinishing()) {
            ProgressDialog progressDialog = this.mSpinner;
            if (progressDialog != null) {
                progressDialog.show();
                this.mSpinner.setMessage(charSequence);
            }
        }
    }

    /* access modifiers changed from: private */
    public void returnResult(int i, Intent intent) {
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(i, intent);
        finish();
    }

    public void finish() {
        if (isBrokerRequest(getIntent()) && this.mAccountAuthenticatorResponse != null) {
            Logger.m236v(TAG, "It is a broker request");
            Bundle bundle = this.mAuthenticatorResultBundle;
            if (bundle == null) {
                this.mAccountAuthenticatorResponse.onError(4, "canceled");
            } else {
                this.mAccountAuthenticatorResponse.onResult(bundle);
            }
            this.mAccountAuthenticatorResponse = null;
        }
        super.finish();
    }

    private void setAccountAuthenticatorResult(Bundle bundle) {
        this.mAuthenticatorResultBundle = bundle;
    }
}
