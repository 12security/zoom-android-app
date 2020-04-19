package com.microsoft.aad.adal;

import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.util.SparseArray;
import androidx.annotation.Nullable;
import androidx.exifinterface.media.ExifInterface;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

public class AuthenticationContext {
    private static final SparseArray<AuthenticationRequestState> DELEGATE_MAP = new SparseArray<>();
    private static final int EXCLUDE_INDEX = 8;
    private static final String REQUEST_ID = "requestId:";
    private static final String TAG = "AuthenticationContext";
    private String mAuthority;
    private BrokerProxy mBrokerProxy = null;
    private Context mContext;
    private boolean mExtendedLifetimeEnabled = false;
    private boolean mIsAuthorityValidated;
    private UUID mRequestCorrelationId = null;
    private ITokenCacheStore mTokenCacheStore;
    private boolean mValidateAuthority;

    static final class SettableFuture<V> extends FutureTask<V> {
        SettableFuture() {
            super(new Callable<V>() {
                public V call() throws Exception {
                    return null;
                }
            });
        }

        public void set(V v) {
            super.set(v);
        }

        public void setException(Throwable th) {
            super.setException(th);
        }
    }

    public static String getVersionName() {
        return "1.14.0";
    }

    public AuthenticationContext(Context context, String str, boolean z) {
        PRNGFixes.apply();
        initialize(context, str, new DefaultTokenCacheStore(context), z, true);
    }

    public AuthenticationContext(Context context, String str, boolean z, ITokenCacheStore iTokenCacheStore) {
        initialize(context, str, iTokenCacheStore, z, false);
    }

    public AuthenticationContext(Context context, String str, ITokenCacheStore iTokenCacheStore) {
        initialize(context, str, iTokenCacheStore, true, false);
    }

    private void initialize(Context context, String str, ITokenCacheStore iTokenCacheStore, boolean z, boolean z2) {
        if (context == null) {
            throw new IllegalArgumentException("appContext");
        } else if (str != null) {
            this.mBrokerProxy = new BrokerProxy(context);
            if (z2 || this.mBrokerProxy.canUseLocalCache(str)) {
                this.mContext = context;
                checkInternetPermission();
                this.mAuthority = extractAuthority(str);
                this.mValidateAuthority = z;
                this.mTokenCacheStore = iTokenCacheStore;
                return;
            }
            throw new UnsupportedOperationException("Local cache is not supported for broker usage");
        } else {
            throw new IllegalArgumentException(OAuth2.AUTHORITY);
        }
    }

    public ITokenCacheStore getCache() {
        return this.mTokenCacheStore;
    }

    public boolean getExtendedLifetimeEnabled() {
        return this.mExtendedLifetimeEnabled;
    }

    public void setExtendedLifetimeEnabled(boolean z) {
        this.mExtendedLifetimeEnabled = z;
    }

    public String getAuthority() {
        return this.mAuthority;
    }

    public boolean getValidateAuthority() {
        return this.mValidateAuthority;
    }

    public String getBrokerUser() {
        BrokerProxy brokerProxy = this.mBrokerProxy;
        if (brokerProxy != null) {
            return brokerProxy.getCurrentUser();
        }
        return null;
    }

    public UserInfo[] getBrokerUsers() throws OperationCanceledException, AuthenticatorException, IOException {
        BrokerProxy brokerProxy = this.mBrokerProxy;
        if (brokerProxy != null) {
            return brokerProxy.getBrokerUsers();
        }
        return null;
    }

    public String getRedirectUriForBroker() {
        PackageHelper packageHelper = new PackageHelper(this.mContext);
        String packageName = this.mContext.getPackageName();
        String currentSignatureForPackage = packageHelper.getCurrentSignatureForPackage(packageName);
        String brokerRedirectUrl = PackageHelper.getBrokerRedirectUrl(packageName, currentSignatureForPackage);
        StringBuilder sb = new StringBuilder();
        sb.append("Broker redirectUri:");
        sb.append(brokerRedirectUrl);
        sb.append(" packagename:");
        sb.append(packageName);
        sb.append(" signatureDigest:");
        sb.append(currentSignatureForPackage);
        Logger.m237v("AuthenticationContext:getRedirectUriForBroker", "Get expected redirect Uri. ", sb.toString(), null);
        return brokerRedirectUrl;
    }

    public void acquireToken(Activity activity, String str, String str2, @Nullable String str3, @Nullable String str4, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str5 = str2;
        String str6 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str5, authenticationCallback2) && checkADFSValidationRequirements(str6, authenticationCallback2)) {
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str5, registerNewRequest, "100");
            createApiEvent.setLoginHint(str6);
            String str7 = str;
            String str8 = str2;
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str7, str8, getRedirectUri(str3), str4, PromptBehavior.Auto, null, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(wrapActivity(activity), false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(Activity activity, String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable String str5, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str6 = str2;
        String str7 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str6, authenticationCallback2) && checkADFSValidationRequirements(str7, authenticationCallback2)) {
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str6, registerNewRequest, "104");
            createApiEvent.setLoginHint(str7);
            String str8 = str;
            String str9 = str2;
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str8, str9, getRedirectUri(str3), str4, PromptBehavior.Auto, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(wrapActivity(activity), false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(Activity activity, String str, String str2, @Nullable String str3, @Nullable PromptBehavior promptBehavior, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str4 = str2;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str4, authenticationCallback2) && checkADFSValidationRequirements(null, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str4, registerNewRequest, "108");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, null, promptBehavior, null, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(wrapActivity(activity), false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(Activity activity, String str, String str2, @Nullable String str3, @Nullable PromptBehavior promptBehavior, @Nullable String str4, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str5 = str2;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str5, authenticationCallback2) && checkADFSValidationRequirements(null, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str5, registerNewRequest, "111");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, null, promptBehavior, str4, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(wrapActivity(activity), false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(Activity activity, String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable PromptBehavior promptBehavior, @Nullable String str5, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str6 = str2;
        String str7 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str6, authenticationCallback2) && checkADFSValidationRequirements(str7, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str6, registerNewRequest, "115");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            createApiEvent.setLoginHint(str7);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, str4, promptBehavior, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(wrapActivity(activity), false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(Activity activity, String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable PromptBehavior promptBehavior, @Nullable String str5, @Nullable String str6, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str7 = str2;
        String str8 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        throwIfClaimsInBothExtraQpAndClaimsParameter(str6, str5);
        if (checkPreRequirements(str, str7, authenticationCallback2) && checkADFSValidationRequirements(str8, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str7, registerNewRequest, "118");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            createApiEvent.setLoginHint(str8);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, str4, promptBehavior, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), str6);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(wrapActivity(activity), false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(IWindowComponent iWindowComponent, String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable PromptBehavior promptBehavior, @Nullable String str5, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str6 = str2;
        String str7 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str6, authenticationCallback2) && checkADFSValidationRequirements(str7, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str6, registerNewRequest, "116");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            createApiEvent.setLoginHint(str7);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, str4, promptBehavior, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(iWindowComponent, false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(IWindowComponent iWindowComponent, String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable PromptBehavior promptBehavior, @Nullable String str5, @Nullable String str6, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str7 = str2;
        String str8 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        throwIfClaimsInBothExtraQpAndClaimsParameter(str6, str5);
        if (checkPreRequirements(str, str7, authenticationCallback2) && checkADFSValidationRequirements(str8, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str7, registerNewRequest, "119");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            createApiEvent.setLoginHint(str8);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, str4, promptBehavior, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), str6);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(iWindowComponent, false, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable PromptBehavior promptBehavior, @Nullable String str5, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str6 = str2;
        String str7 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        if (checkPreRequirements(str, str6, authenticationCallback2) && checkADFSValidationRequirements(str7, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str6, registerNewRequest, "117");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            createApiEvent.setLoginHint(str7);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, str4, promptBehavior, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), null);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(null, true, authenticationRequest, authenticationCallback2);
        }
    }

    public void acquireToken(String str, String str2, @Nullable String str3, @Nullable String str4, @Nullable PromptBehavior promptBehavior, @Nullable String str5, @Nullable String str6, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        String str7 = str2;
        String str8 = str4;
        AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
        throwIfClaimsInBothExtraQpAndClaimsParameter(str6, str5);
        if (checkPreRequirements(str, str7, authenticationCallback2) && checkADFSValidationRequirements(str8, authenticationCallback2)) {
            String redirectUri = getRedirectUri(str3);
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str7, registerNewRequest, "120");
            createApiEvent.setPromptBehavior(promptBehavior.toString());
            createApiEvent.setLoginHint(str8);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, redirectUri, str4, promptBehavior, str5, getRequestCorrelationId(), getExtendedLifetimeEnabled(), str6);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.LoginHint);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(null, false, authenticationRequest, authenticationCallback2);
        }
    }

    public AuthenticationResult acquireTokenSilentSync(String str, String str2, String str3) throws AuthenticationException, InterruptedException {
        checkPreRequirements(str, str2);
        checkADFSValidationRequirements(null);
        final AtomicReference atomicReference = new AtomicReference();
        final AtomicReference atomicReference2 = new AtomicReference();
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        String registerNewRequest = Telemetry.registerNewRequest();
        String str4 = str2;
        APIEvent createApiEvent = createApiEvent(this.mContext, str4, registerNewRequest, "1");
        createApiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
        AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str4, str3, getRequestCorrelationId(), getExtendedLifetimeEnabled());
        authenticationRequest.setSilent(true);
        authenticationRequest.setPrompt(PromptBehavior.Auto);
        authenticationRequest.setUserIdentifierType(UserIdentifierType.UniqueId);
        authenticationRequest.setTelemetryRequestId(registerNewRequest);
        Looper myLooper = Looper.myLooper();
        if (myLooper != null && myLooper == this.mContext.getMainLooper()) {
            Logger.m233e("AuthenticationContext:acquireTokenSilentSync", "Sync network calls must not be invoked in main thread. This method will throw android.os.NetworkOnMainThreadException in next major release", new NetworkOnMainThreadException());
        }
        createAcquireTokenRequest(createApiEvent).acquireToken(null, false, authenticationRequest, new AuthenticationCallback<AuthenticationResult>() {
            public void onSuccess(AuthenticationResult authenticationResult) {
                atomicReference.set(authenticationResult);
                countDownLatch.countDown();
            }

            public void onError(Exception exc) {
                atomicReference2.set(exc);
                countDownLatch.countDown();
            }
        });
        countDownLatch.await();
        Exception exc = (Exception) atomicReference2.get();
        if (exc == null) {
            return (AuthenticationResult) atomicReference.get();
        }
        if (exc instanceof AuthenticationException) {
            throw ((AuthenticationException) exc);
        } else if (exc instanceof RuntimeException) {
            throw ((RuntimeException) exc);
        } else if (exc.getCause() == null) {
            throw new AuthenticationException(ADALError.ERROR_SILENT_REQUEST, exc.getMessage(), (Throwable) exc);
        } else if (exc.getCause() instanceof AuthenticationException) {
            throw ((AuthenticationException) exc.getCause());
        } else if (exc.getCause() instanceof RuntimeException) {
            throw ((RuntimeException) exc.getCause());
        } else {
            throw new AuthenticationException(ADALError.ERROR_SILENT_REQUEST, exc.getCause().getMessage(), exc.getCause());
        }
    }

    @Deprecated
    public Future<AuthenticationResult> acquireTokenSilent(String str, String str2, String str3, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        SettableFuture settableFuture = new SettableFuture();
        try {
            checkPreRequirements(str, str2);
            checkADFSValidationRequirements(null);
            String registerNewRequest = Telemetry.registerNewRequest();
            final APIEvent createApiEvent = createApiEvent(this.mContext, str2, registerNewRequest, "2");
            createApiEvent.setIsDeprecated(true);
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, str3, getRequestCorrelationId(), getExtendedLifetimeEnabled());
            authenticationRequest.setSilent(true);
            authenticationRequest.setPrompt(PromptBehavior.Auto);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.UniqueId);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            AcquireTokenRequest createAcquireTokenRequest = createAcquireTokenRequest(createApiEvent);
            final AuthenticationRequest authenticationRequest2 = authenticationRequest;
            final AuthenticationCallback<AuthenticationResult> authenticationCallback2 = authenticationCallback;
            AuthenticationRequest authenticationRequest3 = authenticationRequest;
            final SettableFuture settableFuture2 = settableFuture;
            C16932 r1 = new AuthenticationCallback<AuthenticationResult>() {
                public void onSuccess(AuthenticationResult authenticationResult) {
                    createApiEvent.setWasApiCallSuccessful(true, null);
                    createApiEvent.setCorrelationId(authenticationRequest2.getCorrelationId().toString());
                    createApiEvent.setIdToken(authenticationResult.getIdToken());
                    createApiEvent.stopTelemetryAndFlush();
                    AuthenticationCallback authenticationCallback = authenticationCallback2;
                    if (authenticationCallback != null) {
                        authenticationCallback.onSuccess(authenticationResult);
                    }
                    settableFuture2.set(authenticationResult);
                }

                public void onError(Exception exc) {
                    createApiEvent.setWasApiCallSuccessful(false, exc);
                    createApiEvent.setCorrelationId(authenticationRequest2.getCorrelationId().toString());
                    createApiEvent.stopTelemetryAndFlush();
                    AuthenticationCallback authenticationCallback = authenticationCallback2;
                    if (authenticationCallback != null) {
                        authenticationCallback.onError(exc);
                    }
                    settableFuture2.setException(exc);
                }
            };
            createAcquireTokenRequest.acquireToken(null, false, authenticationRequest3, r1);
            return settableFuture;
        } catch (AuthenticationException e) {
            authenticationCallback.onError(e);
            settableFuture.setException(e);
            return settableFuture;
        }
    }

    public void acquireTokenSilentAsync(String str, String str2, String str3, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (checkPreRequirements(str, str2, authenticationCallback) && checkADFSValidationRequirements(null, authenticationCallback)) {
            String registerNewRequest = Telemetry.registerNewRequest();
            APIEvent createApiEvent = createApiEvent(this.mContext, str2, registerNewRequest, ExifInterface.GPS_MEASUREMENT_3D);
            createApiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
            AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str, str2, str3, getRequestCorrelationId(), getExtendedLifetimeEnabled());
            authenticationRequest.setSilent(true);
            authenticationRequest.setPrompt(PromptBehavior.Auto);
            authenticationRequest.setUserIdentifierType(UserIdentifierType.UniqueId);
            authenticationRequest.setTelemetryRequestId(registerNewRequest);
            createAcquireTokenRequest(createApiEvent).acquireToken(null, false, authenticationRequest, authenticationCallback);
        }
    }

    @Deprecated
    public void acquireTokenByRefreshToken(String str, String str2, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (checkADFSValidationRequirements(null, authenticationCallback)) {
            if (StringExtensions.isNullOrBlank(str)) {
                throw new IllegalArgumentException("Refresh token is not provided");
            } else if (StringExtensions.isNullOrBlank(str2)) {
                throw new IllegalArgumentException("ClientId is not provided");
            } else if (authenticationCallback != null) {
                String registerNewRequest = Telemetry.registerNewRequest();
                APIEvent createApiEvent = createApiEvent(this.mContext, str2, registerNewRequest, "4");
                createApiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
                createApiEvent.setIsDeprecated(true);
                AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, null, str2, getRequestCorrelationId(), getExtendedLifetimeEnabled());
                authenticationRequest.setSilent(true);
                authenticationRequest.setTelemetryRequestId(registerNewRequest);
                createAcquireTokenRequest(createApiEvent).refreshTokenWithoutCache(str, authenticationRequest, authenticationCallback);
            } else {
                throw new IllegalArgumentException("Callback is not provided");
            }
        }
    }

    @Deprecated
    public void acquireTokenByRefreshToken(String str, String str2, String str3, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (checkADFSValidationRequirements(null, authenticationCallback)) {
            if (StringExtensions.isNullOrBlank(str)) {
                throw new IllegalArgumentException("Refresh token is not provided");
            } else if (StringExtensions.isNullOrBlank(str2)) {
                throw new IllegalArgumentException("ClientId is not provided");
            } else if (authenticationCallback != null) {
                String registerNewRequest = Telemetry.registerNewRequest();
                APIEvent createApiEvent = createApiEvent(this.mContext, str2, registerNewRequest, "5");
                createApiEvent.setPromptBehavior(PromptBehavior.Auto.toString());
                createApiEvent.setIsDeprecated(true);
                AuthenticationRequest authenticationRequest = new AuthenticationRequest(this.mAuthority, str3, str2, getRequestCorrelationId(), getExtendedLifetimeEnabled());
                authenticationRequest.setTelemetryRequestId(registerNewRequest);
                authenticationRequest.setSilent(true);
                createAcquireTokenRequest(createApiEvent).refreshTokenWithoutCache(str, authenticationRequest, authenticationCallback);
            } else {
                throw new IllegalArgumentException("Callback is not provided");
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        AuthenticationRequestState authenticationRequestState;
        if (i == 1001) {
            if (intent == null) {
                Logger.m231e("AuthenticationContext:onActivityResult", "onActivityResult BROWSER_FLOW data is null.", "", ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
                return;
            }
            int i3 = intent.getExtras().getInt(Browser.REQUEST_ID);
            synchronized (DELEGATE_MAP) {
                authenticationRequestState = (AuthenticationRequestState) DELEGATE_MAP.get(i3);
            }
            if (authenticationRequestState != null) {
                new AcquireTokenRequest(this.mContext, this, authenticationRequestState.getAPIEvent()).onActivityResult(i, i2, intent);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("onActivityResult did not find the waiting request. requestId:");
                sb.append(i3);
                Logger.m231e("AuthenticationContext:onActivityResult", sb.toString(), null, ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
            }
        }
    }

    public boolean cancelAuthenticationActivity(int i) throws AuthenticationException {
        String str;
        AuthenticationRequestState waitingRequest = getWaitingRequest(i);
        if (waitingRequest == null || waitingRequest.getDelegate() == null) {
            Logger.m236v("AuthenticationContext:cancelAuthenticationActivity", "Current callback is empty. There is not any active authentication.");
            return true;
        }
        if (waitingRequest.getRequest() != null) {
            str = String.format(" CorrelationId: %s", new Object[]{waitingRequest.getRequest().getCorrelationId().toString()});
        } else {
            str = "No correlation id associated with waiting request";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Current callback is not empty. There is an active authentication Activity.");
        sb.append(str);
        Logger.m236v("AuthenticationContext:cancelAuthenticationActivity", sb.toString());
        Intent intent = new Intent(Browser.ACTION_CANCEL);
        intent.putExtras(new Bundle());
        intent.putExtra(Browser.REQUEST_ID, i);
        boolean sendBroadcast = LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
        if (sendBroadcast) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Cancel broadcast message was successful.");
            sb2.append(str);
            Logger.m236v("AuthenticationContext:cancelAuthenticationActivity", sb2.toString());
            waitingRequest.setCancelled(true);
            waitingRequest.getDelegate().onError(new AuthenticationCancelError("Cancel broadcast message was successful."));
        } else {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Cancel broadcast message was not successful.");
            sb3.append(str);
            Logger.m239w("AuthenticationContext:cancelAuthenticationActivity", sb3.toString(), "", ADALError.BROADCAST_CANCEL_NOT_SUCCESSFUL);
        }
        return sendBroadcast;
    }

    public UUID getRequestCorrelationId() {
        UUID uuid = this.mRequestCorrelationId;
        return uuid == null ? UUID.randomUUID() : uuid;
    }

    public void setRequestCorrelationId(UUID uuid) {
        this.mRequestCorrelationId = uuid;
        Logger.setCorrelationId(uuid);
    }

    private IWindowComponent wrapActivity(final Activity activity) {
        if (activity != null) {
            return new IWindowComponent() {
                private Activity mRefActivity = activity;

                public void startActivityForResult(Intent intent, int i) {
                    Activity activity = this.mRefActivity;
                    if (activity != null) {
                        activity.startActivityForResult(intent, i);
                    }
                }
            };
        }
        throw new IllegalArgumentException("activity");
    }

    private boolean checkPreRequirements(String str, String str2) throws AuthenticationException {
        if (this.mContext != null) {
            if (AuthenticationSettings.INSTANCE.getUseBroker()) {
                this.mBrokerProxy.verifyBrokerPermissionsAPI22AndLess();
            }
            if (StringExtensions.isNullOrBlank(str)) {
                throw new IllegalArgumentException(AAD.RESOURCE);
            } else if (!StringExtensions.isNullOrBlank(str2)) {
                return true;
            } else {
                throw new IllegalArgumentException("clientId");
            }
        } else {
            throw new IllegalArgumentException("context", new AuthenticationException(ADALError.DEVELOPER_CONTEXT_IS_NOT_PROVIDED));
        }
    }

    private boolean checkPreRequirements(String str, String str2, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        if (authenticationCallback != null) {
            try {
                return checkPreRequirements(str, str2);
            } catch (AuthenticationException e) {
                authenticationCallback.onError(e);
                return false;
            }
        } else {
            throw new IllegalArgumentException(QueryParameters.CALLBACK);
        }
    }

    private boolean checkADFSValidationRequirements(@Nullable String str) throws AuthenticationException {
        URL url = StringExtensions.getUrl(this.mAuthority);
        if (this.mAuthority == null || url == null) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL);
        } else if (!UrlExtensions.isADFSAuthority(url) || !this.mValidateAuthority || this.mIsAuthorityValidated || str != null) {
            return true;
        } else {
            ADALError aDALError = ADALError.DEVELOPER_AUTHORITY_CAN_NOT_BE_VALIDED;
            StringBuilder sb = new StringBuilder();
            sb.append("AD FS validation requires a loginHint be provided or an ");
            sb.append(getClass().getSimpleName());
            sb.append(" in which the current authority has previously been validated.");
            throw new AuthenticationException(aDALError, sb.toString());
        }
    }

    private boolean checkADFSValidationRequirements(@Nullable String str, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        try {
            return checkADFSValidationRequirements(str);
        } catch (AuthenticationException e) {
            authenticationCallback.onError(e);
            return false;
        }
    }

    private String getRedirectUri(String str) {
        return StringExtensions.isNullOrBlank(str) ? this.mContext.getApplicationContext().getPackageName() : str;
    }

    private AcquireTokenRequest createAcquireTokenRequest(APIEvent aPIEvent) {
        return new AcquireTokenRequest(this.mContext, this, aPIEvent);
    }

    private static String extractAuthority(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            int indexOf = str.indexOf(47, 8);
            if (indexOf >= 0 && indexOf != str.length() - 1) {
                int i = indexOf + 1;
                int indexOf2 = str.indexOf("/", i);
                if (indexOf2 < 0 || indexOf2 > i) {
                    return indexOf2 >= 0 ? str.substring(0, indexOf2) : str;
                }
            }
        }
        throw new IllegalArgumentException(OAuth2.AUTHORITY);
    }

    private void checkInternetPermission() {
        if (this.mContext.getPackageManager().checkPermission("android.permission.INTERNET", this.mContext.getPackageName()) != 0) {
            throw new IllegalStateException(new AuthenticationException(ADALError.DEVELOPER_INTERNET_PERMISSION_MISSING));
        }
    }

    /* access modifiers changed from: 0000 */
    public String serialize(String str) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(str)) {
            throw new IllegalArgumentException("uniqueUserId");
        } else if (this.mBrokerProxy.canSwitchToBroker(this.mAuthority) == SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
            try {
                TokenCacheItem fRTItem = new TokenCacheAccessor(this.mTokenCacheStore, getAuthority(), Telemetry.registerNewRequest()).getFRTItem("1", str);
                if (fRTItem == null) {
                    Logger.m234i("AuthenticationContext:serialize", "Cannot find the family token cache item for this userID", "");
                    throw new UsageAuthenticationException(ADALError.FAIL_TO_EXPORT, "Failed to export the FID because no family token cache item is found.");
                } else if (!StringExtensions.isNullOrBlank(fRTItem.getFamilyClientId())) {
                    return SSOStateSerializer.serialize(fRTItem);
                } else {
                    throw new UsageAuthenticationException(ADALError.FAIL_TO_EXPORT, "tokenItem does not contain family refresh token");
                }
            } catch (MalformedURLException e) {
                throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
            }
        } else {
            throw new UsageAuthenticationException(ADALError.FAIL_TO_EXPORT, "Failed to export the family refresh token cache item because broker is enabled.");
        }
    }

    /* access modifiers changed from: 0000 */
    public void deserialize(String str) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(str)) {
            throw new IllegalArgumentException("serializedBlob");
        } else if (this.mBrokerProxy.canSwitchToBroker(this.mAuthority) == SwitchToBroker.CANNOT_SWITCH_TO_BROKER) {
            TokenCacheItem deserialize = SSOStateSerializer.deserialize(str);
            getCache().setItem(CacheKey.createCacheKey(deserialize), deserialize);
        } else {
            throw new UsageAuthenticationException(ADALError.FAIL_TO_IMPORT, "Failed to import the serialized blob because broker is enabled.");
        }
    }

    /* access modifiers changed from: 0000 */
    public void setIsAuthorityValidated(boolean z) {
        this.mIsAuthorityValidated = z;
    }

    /* access modifiers changed from: 0000 */
    public boolean getIsAuthorityValidated() {
        return this.mIsAuthorityValidated;
    }

    /* access modifiers changed from: 0000 */
    public AuthenticationRequestState getWaitingRequest(int i) throws AuthenticationException {
        AuthenticationRequestState authenticationRequestState;
        StringBuilder sb = new StringBuilder();
        sb.append("Get waiting request. requestId:");
        sb.append(i);
        Logger.m236v("AuthenticationContext:getWaitingRequest", sb.toString());
        synchronized (DELEGATE_MAP) {
            authenticationRequestState = (AuthenticationRequestState) DELEGATE_MAP.get(i);
        }
        if (authenticationRequestState != null) {
            return authenticationRequestState;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Request callback is not available. requestId:");
        sb2.append(i);
        Logger.m231e("AuthenticationContext:getWaitingRequest", sb2.toString(), null, ADALError.CALLBACK_IS_NOT_FOUND);
        ADALError aDALError = ADALError.CALLBACK_IS_NOT_FOUND;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Request callback is not available for requestId:");
        sb3.append(i);
        throw new AuthenticationException(aDALError, sb3.toString());
    }

    /* access modifiers changed from: 0000 */
    public void putWaitingRequest(int i, AuthenticationRequestState authenticationRequestState) {
        if (authenticationRequestState != null) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Put waiting request. requestId:");
            sb.append(i);
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(getCorrelationInfoFromWaitingRequest(authenticationRequestState));
            Logger.m236v(str, sb.toString());
            synchronized (DELEGATE_MAP) {
                DELEGATE_MAP.put(i, authenticationRequestState);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeWaitingRequest(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("Remove waiting request. requestId:");
        sb.append(i);
        Logger.m236v(str, sb.toString());
        synchronized (DELEGATE_MAP) {
            DELEGATE_MAP.remove(i);
        }
    }

    /* access modifiers changed from: 0000 */
    public String getCorrelationInfoFromWaitingRequest(AuthenticationRequestState authenticationRequestState) {
        UUID requestCorrelationId = getRequestCorrelationId();
        if (authenticationRequestState.getRequest() != null) {
            requestCorrelationId = authenticationRequestState.getRequest().getCorrelationId();
        }
        return String.format(" CorrelationId: %s", new Object[]{requestCorrelationId.toString()});
    }

    private APIEvent createApiEvent(Context context, String str, String str2, String str3) {
        APIEvent aPIEvent = new APIEvent("Microsoft.ADAL.api_event", context, str);
        aPIEvent.setRequestId(str2);
        aPIEvent.setAPIId(str3);
        aPIEvent.setAuthority(getAuthority());
        Telemetry.getInstance().startEvent(str2, aPIEvent.getEventName());
        return aPIEvent;
    }

    private void throwIfClaimsInBothExtraQpAndClaimsParameter(String str, String str2) {
        if (!StringExtensions.isNullOrBlank(str) && !StringExtensions.isNullOrBlank(str2) && str2.contains("claims")) {
            throw new IllegalArgumentException("claims cannot be sent in claims parameter and extra qp.");
        }
    }
}
