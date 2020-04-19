package com.onedrive.sdk.authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationCancelError;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationException;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.AuthenticationResult.AuthenticationStatus;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.onedrivesdk.BuildConfig;
import com.onedrive.sdk.concurrency.ICallback;
import com.onedrive.sdk.concurrency.IExecutors;
import com.onedrive.sdk.concurrency.SimpleWaiter;
import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.http.IHttpProvider;
import com.onedrive.sdk.logger.ILogger;
import com.onedrive.sdk.options.HeaderOption;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ADALAuthenticator implements IAuthenticator {
    private static final String ADAL_AUTHENTICATOR_PREFS = "ADALAuthenticatorPrefs";
    private static final String DISCOVERY_SERVICE_URL = "https://api.office.com/discovery/v2.0/me/Services";
    private static final String DISCOVER_SERVICE_RESOURCE_ID = "https://api.office.com/discovery/";
    private static final String LOGIN_AUTHORITY = "https://login.windows.net/common/oauth2/authorize";
    private static final String RESOURCE_URL_KEY = "resourceUrl";
    private static final String SERVICE_INFO_KEY = "serviceInfo";
    private static final String USER_ID_KEY = "userId";
    private static final boolean VALIDATE_AUTHORITY = true;
    private static final String VERSION_CODE_KEY = "versionCode";
    private final AtomicReference<IAccountInfo> mAccountInfo = new AtomicReference<>();
    private Activity mActivity;
    private AuthenticationContext mAdalContext;
    /* access modifiers changed from: private */
    public IExecutors mExecutors;
    private IHttpProvider mHttpProvider;
    private boolean mInitialized;
    /* access modifiers changed from: private */
    public ILogger mLogger;
    private final AtomicReference<ServiceInfo> mOneDriveServiceInfo = new AtomicReference<>();
    private final AtomicReference<String> mResourceUrl = new AtomicReference<>();
    private final AtomicReference<String> mUserId = new AtomicReference<>();

    /* access modifiers changed from: protected */
    public abstract String getClientId();

    /* access modifiers changed from: protected */
    public abstract String getRedirectUrl();

    public IAccountInfo getAccountInfo() {
        return (IAccountInfo) this.mAccountInfo.get();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a9, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void init(com.onedrive.sdk.concurrency.IExecutors r2, com.onedrive.sdk.http.IHttpProvider r3, android.app.Activity r4, com.onedrive.sdk.logger.ILogger r5) {
        /*
            r1 = this;
            monitor-enter(r1)
            boolean r0 = r1.mInitialized     // Catch:{ all -> 0x00bc }
            if (r0 == 0) goto L_0x0007
            monitor-exit(r1)
            return
        L_0x0007:
            r1.mExecutors = r2     // Catch:{ all -> 0x00bc }
            r1.mHttpProvider = r3     // Catch:{ all -> 0x00bc }
            r1.mActivity = r4     // Catch:{ all -> 0x00bc }
            r1.mLogger = r5     // Catch:{ all -> 0x00bc }
            com.onedrive.sdk.authentication.adal.BrokerPermissionsChecker r2 = new com.onedrive.sdk.authentication.adal.BrokerPermissionsChecker     // Catch:{ all -> 0x00bc }
            android.app.Activity r3 = r1.mActivity     // Catch:{ all -> 0x00bc }
            com.onedrive.sdk.logger.ILogger r5 = r1.mLogger     // Catch:{ all -> 0x00bc }
            r2.<init>(r3, r5)     // Catch:{ all -> 0x00bc }
            r2.check()     // Catch:{ all -> 0x00bc }
            com.microsoft.aad.adal.AuthenticationContext r2 = new com.microsoft.aad.adal.AuthenticationContext     // Catch:{ ClientAuthenticatorException -> 0x00aa }
            java.lang.String r3 = "https://login.windows.net/common/oauth2/authorize"
            r5 = 1
            r2.<init>(r4, r3, r5)     // Catch:{ ClientAuthenticatorException -> 0x00aa }
            r1.mAdalContext = r2     // Catch:{ ClientAuthenticatorException -> 0x00aa }
            android.content.SharedPreferences r2 = r1.getSharedPreferences()     // Catch:{ all -> 0x00bc }
            java.util.concurrent.atomic.AtomicReference<java.lang.String> r3 = r1.mUserId     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = "userId"
            r0 = 0
            java.lang.String r4 = r2.getString(r4, r0)     // Catch:{ all -> 0x00bc }
            r3.set(r4)     // Catch:{ all -> 0x00bc }
            java.util.concurrent.atomic.AtomicReference<java.lang.String> r3 = r1.mResourceUrl     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = "resourceUrl"
            java.lang.String r4 = r2.getString(r4, r0)     // Catch:{ all -> 0x00bc }
            r3.set(r4)     // Catch:{ all -> 0x00bc }
            java.lang.String r3 = "serviceInfo"
            java.lang.String r2 = r2.getString(r3, r0)     // Catch:{ all -> 0x00bc }
            if (r2 == 0) goto L_0x0060
            com.onedrive.sdk.http.IHttpProvider r3 = r1.mHttpProvider     // Catch:{ Exception -> 0x0058 }
            com.onedrive.sdk.serializer.ISerializer r3 = r3.getSerializer()     // Catch:{ Exception -> 0x0058 }
            java.lang.Class<com.onedrive.sdk.authentication.ServiceInfo> r4 = com.onedrive.sdk.authentication.ServiceInfo.class
            java.lang.Object r2 = r3.deserializeObject(r2, r4)     // Catch:{ Exception -> 0x0058 }
            com.onedrive.sdk.authentication.ServiceInfo r2 = (com.onedrive.sdk.authentication.ServiceInfo) r2     // Catch:{ Exception -> 0x0058 }
            r0 = r2
            goto L_0x0060
        L_0x0058:
            r2 = move-exception
            com.onedrive.sdk.logger.ILogger r3 = r1.mLogger     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = "Unable to parse serviceInfo from saved preferences"
            r3.logError(r4, r2)     // Catch:{ all -> 0x00bc }
        L_0x0060:
            java.util.concurrent.atomic.AtomicReference<com.onedrive.sdk.authentication.ServiceInfo> r2 = r1.mOneDriveServiceInfo     // Catch:{ all -> 0x00bc }
            r2.set(r0)     // Catch:{ all -> 0x00bc }
            r1.mInitialized = r5     // Catch:{ all -> 0x00bc }
            java.util.concurrent.atomic.AtomicReference<java.lang.String> r2 = r1.mUserId     // Catch:{ all -> 0x00bc }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x00bc }
            if (r2 != 0) goto L_0x007f
            java.util.concurrent.atomic.AtomicReference<java.lang.String> r2 = r1.mResourceUrl     // Catch:{ all -> 0x00bc }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x00bc }
            if (r2 != 0) goto L_0x007f
            java.util.concurrent.atomic.AtomicReference<com.onedrive.sdk.authentication.ServiceInfo> r2 = r1.mOneDriveServiceInfo     // Catch:{ all -> 0x00bc }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x00bc }
            if (r2 == 0) goto L_0x00a8
        L_0x007f:
            com.onedrive.sdk.logger.ILogger r2 = r1.mLogger     // Catch:{ all -> 0x00bc }
            java.lang.String r3 = "Found existing login information"
            r2.logDebug(r3)     // Catch:{ all -> 0x00bc }
            java.util.concurrent.atomic.AtomicReference<java.lang.String> r2 = r1.mUserId     // Catch:{ all -> 0x00bc }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x00bc }
            if (r2 == 0) goto L_0x009e
            java.util.concurrent.atomic.AtomicReference<java.lang.String> r2 = r1.mResourceUrl     // Catch:{ all -> 0x00bc }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x00bc }
            if (r2 == 0) goto L_0x009e
            java.util.concurrent.atomic.AtomicReference<com.onedrive.sdk.authentication.ServiceInfo> r2 = r1.mOneDriveServiceInfo     // Catch:{ all -> 0x00bc }
            java.lang.Object r2 = r2.get()     // Catch:{ all -> 0x00bc }
            if (r2 != 0) goto L_0x00a8
        L_0x009e:
            com.onedrive.sdk.logger.ILogger r2 = r1.mLogger     // Catch:{ all -> 0x00bc }
            java.lang.String r3 = "Existing login information was incompletely, flushing sign in state"
            r2.logDebug(r3)     // Catch:{ all -> 0x00bc }
            r1.logout()     // Catch:{ all -> 0x00bc }
        L_0x00a8:
            monitor-exit(r1)
            return
        L_0x00aa:
            r2 = move-exception
            com.onedrive.sdk.authentication.ClientAuthenticatorException r3 = new com.onedrive.sdk.authentication.ClientAuthenticatorException     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = "Unable to access required cryptographic libraries for ADAL"
            com.onedrive.sdk.core.OneDriveErrorCodes r5 = com.onedrive.sdk.core.OneDriveErrorCodes.AuthenticationFailure     // Catch:{ all -> 0x00bc }
            r3.<init>(r4, r2, r5)     // Catch:{ all -> 0x00bc }
            com.onedrive.sdk.logger.ILogger r2 = r1.mLogger     // Catch:{ all -> 0x00bc }
            java.lang.String r4 = "Problem creating the AuthenticationContext for ADAL"
            r2.logError(r4, r3)     // Catch:{ all -> 0x00bc }
            throw r3     // Catch:{ all -> 0x00bc }
        L_0x00bc:
            r2 = move-exception
            monitor-exit(r1)
            throw r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.onedrive.sdk.authentication.ADALAuthenticator.init(com.onedrive.sdk.concurrency.IExecutors, com.onedrive.sdk.http.IHttpProvider, android.app.Activity, com.onedrive.sdk.logger.ILogger):void");
    }

    public void login(final String str, final ICallback<IAccountInfo> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting login async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        iCallback.success(ADALAuthenticator.this.login(str));
                    } catch (ClientException e) {
                        iCallback.failure(e);
                    }
                }
            });
        } else {
            throw new IllegalArgumentException("loginCallback");
        }
    }

    public synchronized IAccountInfo login(String str) throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting login");
            AuthenticationResult discoveryServiceAuthResult = getDiscoveryServiceAuthResult(str);
            if (discoveryServiceAuthResult.getStatus() == AuthenticationStatus.Succeeded) {
                ServiceInfo oneDriveServiceInfoFromDiscoveryService = getOneDriveServiceInfoFromDiscoveryService(discoveryServiceAuthResult.getAccessToken());
                AuthenticationResult oneDriveServiceAuthResult = getOneDriveServiceAuthResult(oneDriveServiceInfoFromDiscoveryService);
                String serializeObject = this.mHttpProvider.getSerializer().serializeObject(oneDriveServiceInfoFromDiscoveryService);
                this.mLogger.logDebug("Successful login, saving information for silent re-auth");
                SharedPreferences sharedPreferences = getSharedPreferences();
                this.mResourceUrl.set(oneDriveServiceInfoFromDiscoveryService.serviceEndpointUri);
                this.mUserId.set(discoveryServiceAuthResult.getUserInfo().getUserId());
                this.mOneDriveServiceInfo.set(oneDriveServiceInfoFromDiscoveryService);
                sharedPreferences.edit().putString(RESOURCE_URL_KEY, (String) this.mResourceUrl.get()).putString("userId", (String) this.mUserId.get()).putString(SERVICE_INFO_KEY, serializeObject).putInt("versionCode", BuildConfig.VERSION_CODE).apply();
                this.mLogger.logDebug("Successfully retrieved login information");
                ILogger iLogger = this.mLogger;
                StringBuilder sb = new StringBuilder();
                sb.append("   Resource Url: ");
                sb.append((String) this.mResourceUrl.get());
                iLogger.logDebug(sb.toString());
                ILogger iLogger2 = this.mLogger;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("   User ID: ");
                sb2.append((String) this.mUserId.get());
                iLogger2.logDebug(sb2.toString());
                ILogger iLogger3 = this.mLogger;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("   Service Info: ");
                sb3.append(serializeObject);
                iLogger3.logDebug(sb3.toString());
                this.mAccountInfo.set(new ADALAccountInfo(this, oneDriveServiceAuthResult, oneDriveServiceInfoFromDiscoveryService, this.mLogger));
            } else {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Unable to authenticate user with ADAL, Error Code: ");
                sb4.append(discoveryServiceAuthResult.getErrorCode());
                sb4.append(" Error Message");
                sb4.append(discoveryServiceAuthResult.getErrorDescription());
                ClientAuthenticatorException clientAuthenticatorException = new ClientAuthenticatorException(sb4.toString(), OneDriveErrorCodes.AuthenticationFailure);
                this.mLogger.logError("Unsuccessful login attempt", clientAuthenticatorException);
                throw clientAuthenticatorException;
            }
        } else {
            throw new IllegalStateException("init must be called");
        }
        return (IAccountInfo) this.mAccountInfo.get();
    }

    public void loginSilent(final ICallback<IAccountInfo> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting login silent async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        ADALAuthenticator.this.mExecutors.performOnForeground(ADALAuthenticator.this.loginSilent(), iCallback);
                    } catch (ClientException e) {
                        ADALAuthenticator.this.mExecutors.performOnForeground(e, iCallback);
                    }
                }
            });
        } else {
            throw new IllegalArgumentException("loginCallback");
        }
    }

    public synchronized IAccountInfo loginSilent() throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting login silent");
            if (this.mResourceUrl.get() == null) {
                this.mLogger.logDebug("No login information found for silent authentication");
                return null;
            }
            final SimpleWaiter simpleWaiter = new SimpleWaiter();
            final AtomicReference atomicReference = new AtomicReference();
            final AtomicReference atomicReference2 = new AtomicReference();
            this.mAdalContext.acquireTokenSilent(((ServiceInfo) this.mOneDriveServiceInfo.get()).serviceResourceId, getClientId(), (String) this.mUserId.get(), new AuthenticationCallback<AuthenticationResult>() {
                public void onSuccess(AuthenticationResult authenticationResult) {
                    String str;
                    if (authenticationResult.getUserInfo() == null) {
                        str = "Invalid User Id";
                    } else {
                        str = authenticationResult.getUserInfo().getUserId();
                    }
                    String tenantId = authenticationResult.getTenantId();
                    ADALAuthenticator.this.mLogger.logDebug(String.format("Successful silent auth for user id '%s', tenant id '%s'", new Object[]{str, tenantId}));
                    atomicReference.set(authenticationResult);
                    simpleWaiter.signal();
                }

                public void onError(Exception exc) {
                    String str = "Silent authentication failure from ADAL";
                    if (exc instanceof AuthenticationException) {
                        str = String.format("%s; Code %s", new Object[]{str, ((AuthenticationException) exc).getCode().getDescription()});
                    }
                    ADALAuthenticator.this.mLogger.logDebug(str);
                    atomicReference2.set(new ClientAuthenticatorException(str, exc, OneDriveErrorCodes.AuthenticationFailure));
                    simpleWaiter.signal();
                }
            });
            simpleWaiter.waitForSignal();
            if (atomicReference2.get() == null) {
                this.mAccountInfo.set(new ADALAccountInfo(this, (AuthenticationResult) atomicReference.get(), (ServiceInfo) this.mOneDriveServiceInfo.get(), this.mLogger));
                return (IAccountInfo) this.mAccountInfo.get();
            }
            throw ((ClientException) atomicReference2.get());
        }
        throw new IllegalStateException("init must be called");
    }

    public void logout(final ICallback<Void> iCallback) {
        if (!this.mInitialized) {
            throw new IllegalStateException("init must be called");
        } else if (iCallback != null) {
            this.mLogger.logDebug("Starting logout async");
            this.mExecutors.performOnBackground(new Runnable() {
                public void run() {
                    try {
                        ADALAuthenticator.this.logout();
                        ADALAuthenticator.this.mExecutors.performOnForeground(null, iCallback);
                    } catch (ClientException unused) {
                        ADALAuthenticator.this.mExecutors.performOnForeground(null, iCallback);
                    }
                }
            });
        } else {
            throw new IllegalArgumentException("logoutCallback");
        }
    }

    public synchronized void logout() throws ClientException {
        if (this.mInitialized) {
            this.mLogger.logDebug("Starting logout");
            this.mLogger.logDebug("Clearing ADAL cache");
            this.mAdalContext.getCache().removeAll();
            this.mLogger.logDebug("Clearing all webview cookies");
            CookieSyncManager.createInstance(this.mActivity);
            CookieManager.getInstance().removeAllCookie();
            CookieSyncManager.getInstance().sync();
            this.mLogger.logDebug("Clearing all ADAL Authenticator shared preferences");
            getSharedPreferences().edit().clear().putInt("versionCode", BuildConfig.VERSION_CODE).apply();
            this.mUserId.set(null);
            this.mResourceUrl.set(null);
        } else {
            throw new IllegalStateException("init must be called");
        }
    }

    private AuthenticationResult getDiscoveryServiceAuthResult(String str) {
        final SimpleWaiter simpleWaiter = new SimpleWaiter();
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        C17665 r10 = new AuthenticationCallback<AuthenticationResult>() {
            public void onSuccess(AuthenticationResult authenticationResult) {
                String str;
                if (authenticationResult.getUserInfo() == null) {
                    str = "Invalid User Id";
                } else {
                    str = authenticationResult.getUserInfo().getUserId();
                }
                String tenantId = authenticationResult.getTenantId();
                ADALAuthenticator.this.mLogger.logDebug(String.format("Successful response from the discover service for user id '%s', tenant id '%s'", new Object[]{str, tenantId}));
                atomicReference2.set(authenticationResult);
                simpleWaiter.signal();
            }

            public void onError(Exception exc) {
                OneDriveErrorCodes oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationFailure;
                if (exc instanceof AuthenticationCancelError) {
                    oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationCancelled;
                }
                String str = "Error while retrieving the discovery service auth token";
                if (exc instanceof AuthenticationException) {
                    str = String.format("%s; Code %s", new Object[]{str, ((AuthenticationException) exc).getCode().getDescription()});
                }
                atomicReference.set(new ClientAuthenticatorException(str, exc, oneDriveErrorCodes));
                ADALAuthenticator.this.mLogger.logError("Error while attempting to login interactively", (Throwable) atomicReference.get());
                simpleWaiter.signal();
            }
        };
        this.mLogger.logDebug("Starting interactive login for the discover service access token");
        this.mAdalContext.acquireToken(DISCOVER_SERVICE_RESOURCE_ID, getClientId(), getRedirectUrl(), str, PromptBehavior.Auto, (String) null, (AuthenticationCallback<AuthenticationResult>) r10);
        this.mLogger.logDebug("Waiting for interactive login to complete");
        simpleWaiter.waitForSignal();
        if (atomicReference.get() == null) {
            return (AuthenticationResult) atomicReference2.get();
        }
        throw ((ClientException) atomicReference.get());
    }

    private SharedPreferences getSharedPreferences() {
        return this.mActivity.getSharedPreferences(ADAL_AUTHENTICATOR_PREFS, 0);
    }

    private ServiceInfo getOneDriveApiService(ServiceInfo[] serviceInfoArr) {
        for (ServiceInfo serviceInfo : serviceInfoArr) {
            this.mLogger.logDebug(String.format("Service info resource id%s capabilities %s version %s", new Object[]{serviceInfo.serviceResourceId, serviceInfo.capability, serviceInfo.serviceApiVersion}));
            if (serviceInfo.capability.equalsIgnoreCase("MyFiles") && serviceInfo.serviceApiVersion.equalsIgnoreCase("v2.0")) {
                return serviceInfo;
            }
        }
        throw new ClientAuthenticatorException("Unable to file the files services from the directory provider", OneDriveErrorCodes.AuthenticationFailure);
    }

    private ServiceInfo getOneDriveServiceInfoFromDiscoveryService(String str) {
        ArrayList arrayList = new ArrayList();
        StringBuilder sb = new StringBuilder();
        sb.append(AuthorizationInterceptor.OAUTH_BEARER_PREFIX);
        sb.append(str);
        arrayList.add(new HeaderOption("Authorization", sb.toString()));
        this.mLogger.logDebug("Starting discovery service request");
        C17676 r0 = new BaseRequest(DISCOVERY_SERVICE_URL, null, arrayList, null) {
        };
        r0.setHttpMethod(HttpMethod.GET);
        return getOneDriveApiService(((DiscoveryServiceResponse) this.mHttpProvider.send(r0, DiscoveryServiceResponse.class, null)).services);
    }

    private AuthenticationResult getOneDriveServiceAuthResult(ServiceInfo serviceInfo) {
        final SimpleWaiter simpleWaiter = new SimpleWaiter();
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        C17687 r9 = new AuthenticationCallback<AuthenticationResult>() {
            public void onSuccess(AuthenticationResult authenticationResult) {
                ADALAuthenticator.this.mLogger.logDebug("Successful refreshed the OneDrive service authentication token");
                atomicReference2.set(authenticationResult);
                simpleWaiter.signal();
            }

            public void onError(Exception exc) {
                String str = "Error while retrieving the service specific auth token";
                OneDriveErrorCodes oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationFailure;
                if (exc instanceof AuthenticationCancelError) {
                    oneDriveErrorCodes = OneDriveErrorCodes.AuthenticationCancelled;
                }
                if (exc instanceof AuthenticationException) {
                    str = String.format("%s; Code %s", new Object[]{str, ((AuthenticationException) exc).getCode().getDescription()});
                }
                atomicReference.set(new ClientAuthenticatorException(str, exc, oneDriveErrorCodes));
                ADALAuthenticator.this.mLogger.logError("Unable to refresh token into OneDrive service access token", (Throwable) atomicReference.get());
                simpleWaiter.signal();
            }
        };
        this.mLogger.logDebug("Starting OneDrive resource refresh token request");
        this.mAdalContext.acquireToken(this.mActivity, serviceInfo.serviceResourceId, getClientId(), getRedirectUrl(), PromptBehavior.Auto, (AuthenticationCallback<AuthenticationResult>) r9);
        this.mLogger.logDebug("Waiting for token refresh");
        simpleWaiter.waitForSignal();
        if (atomicReference.get() == null) {
            return (AuthenticationResult) atomicReference2.get();
        }
        throw ((ClientException) atomicReference.get());
    }
}
