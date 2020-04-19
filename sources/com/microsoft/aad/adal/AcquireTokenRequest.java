package com.microsoft.aad.adal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.security.KeyChain;
import android.security.KeyChainAliasCallback;
import android.security.KeyChainException;
import android.util.Log;
import android.webkit.ClientCertRequest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.microsoft.aad.adal.AuthenticationConstants.Broker;
import com.microsoft.aad.adal.AuthenticationConstants.Broker.CliTelemInfo;
import com.microsoft.aad.adal.AuthenticationConstants.Browser;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.http.message.TokenParser;

class AcquireTokenRequest {
    /* access modifiers changed from: private */
    public static final String TAG = "AcquireTokenRequest";
    private static final ExecutorService THREAD_EXECUTOR = Executors.newSingleThreadExecutor();
    private static Handler sHandler = null;
    /* access modifiers changed from: private */
    public APIEvent mAPIEvent;
    private final AuthenticationContext mAuthContext;
    private final IBrokerProxy mBrokerProxy;
    /* access modifiers changed from: private */
    public final Context mContext;
    private Discovery mDiscovery = new Discovery(this.mContext);
    /* access modifiers changed from: private */
    public TokenCacheAccessor mTokenCacheAccessor;

    private static class CallbackHandler {
        /* access modifiers changed from: private */
        public AuthenticationCallback<AuthenticationResult> mCallback;
        private Handler mRefHandler;

        CallbackHandler(Handler handler, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
            this.mRefHandler = handler;
            this.mCallback = authenticationCallback;
        }

        public void onError(final AuthenticationException authenticationException) {
            AuthenticationCallback<AuthenticationResult> authenticationCallback = this.mCallback;
            if (authenticationCallback != null) {
                Handler handler = this.mRefHandler;
                if (handler != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            CallbackHandler.this.mCallback.onError(authenticationException);
                        }
                    });
                } else {
                    authenticationCallback.onError(authenticationException);
                }
            }
        }

        public void onSuccess(final AuthenticationResult authenticationResult) {
            AuthenticationCallback<AuthenticationResult> authenticationCallback = this.mCallback;
            if (authenticationCallback != null) {
                Handler handler = this.mRefHandler;
                if (handler != null) {
                    handler.post(new Runnable() {
                        public void run() {
                            CallbackHandler.this.mCallback.onSuccess(authenticationResult);
                        }
                    });
                } else {
                    authenticationCallback.onSuccess(authenticationResult);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public AuthenticationCallback<AuthenticationResult> getCallback() {
            return this.mCallback;
        }
    }

    AcquireTokenRequest(Context context, AuthenticationContext authenticationContext, APIEvent aPIEvent) {
        this.mContext = context;
        this.mAuthContext = authenticationContext;
        if (!(authenticationContext.getCache() == null || aPIEvent == null)) {
            this.mTokenCacheAccessor = new TokenCacheAccessor(authenticationContext.getCache(), authenticationContext.getAuthority(), aPIEvent.getTelemetryRequestId());
        }
        this.mBrokerProxy = new BrokerProxy(context);
        this.mAPIEvent = aPIEvent;
    }

    /* access modifiers changed from: 0000 */
    public void acquireToken(IWindowComponent iWindowComponent, boolean z, AuthenticationRequest authenticationRequest, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        final CallbackHandler callbackHandler = new CallbackHandler(getHandler(), authenticationCallback);
        Logger.setCorrelationId(authenticationRequest.getCorrelationId());
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":acquireToken");
        String sb2 = sb.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("Sending async task from thread:");
        sb3.append(Process.myTid());
        Logger.m236v(sb2, sb3.toString());
        ExecutorService executorService = THREAD_EXECUTOR;
        final AuthenticationRequest authenticationRequest2 = authenticationRequest;
        final IWindowComponent iWindowComponent2 = iWindowComponent;
        final boolean z2 = z;
        C16831 r0 = new Runnable() {
            public void run() {
                StringBuilder sb = new StringBuilder();
                sb.append(AcquireTokenRequest.TAG);
                sb.append(":acquireToken");
                String sb2 = sb.toString();
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Running task in thread:");
                sb3.append(Process.myTid());
                Logger.m236v(sb2, sb3.toString());
                try {
                    AcquireTokenRequest.this.validateAcquireTokenRequest(authenticationRequest2);
                    AcquireTokenRequest.this.performAcquireTokenRequest(callbackHandler, iWindowComponent2, z2, authenticationRequest2);
                } catch (AuthenticationException e) {
                    AcquireTokenRequest.this.mAPIEvent.setWasApiCallSuccessful(false, e);
                    AcquireTokenRequest.this.mAPIEvent.setCorrelationId(authenticationRequest2.getCorrelationId().toString());
                    AcquireTokenRequest.this.mAPIEvent.stopTelemetryAndFlush();
                    callbackHandler.onError(e);
                }
            }
        };
        executorService.execute(r0);
    }

    /* access modifiers changed from: 0000 */
    public void refreshTokenWithoutCache(final String str, final AuthenticationRequest authenticationRequest, AuthenticationCallback<AuthenticationResult> authenticationCallback) {
        Logger.setCorrelationId(authenticationRequest.getCorrelationId());
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":refreshTokenWithoutCache");
        Logger.m236v(sb.toString(), "Refresh token without cache");
        final CallbackHandler callbackHandler = new CallbackHandler(getHandler(), authenticationCallback);
        THREAD_EXECUTOR.execute(new Runnable() {
            public void run() {
                try {
                    AcquireTokenRequest.this.validateAcquireTokenRequest(authenticationRequest);
                    AuthenticationResult acquireTokenWithRefreshToken = new AcquireTokenSilentHandler(AcquireTokenRequest.this.mContext, authenticationRequest, AcquireTokenRequest.this.mTokenCacheAccessor).acquireTokenWithRefreshToken(str);
                    AcquireTokenRequest.this.mAPIEvent.setWasApiCallSuccessful(true, null);
                    AcquireTokenRequest.this.mAPIEvent.setIdToken(acquireTokenWithRefreshToken.getIdToken());
                    callbackHandler.onSuccess(acquireTokenWithRefreshToken);
                } catch (AuthenticationException e) {
                    AcquireTokenRequest.this.mAPIEvent.setWasApiCallSuccessful(false, e);
                    callbackHandler.onError(e);
                } catch (Throwable th) {
                    AcquireTokenRequest.this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                    AcquireTokenRequest.this.mAPIEvent.stopTelemetryAndFlush();
                    throw th;
                }
                AcquireTokenRequest.this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
                AcquireTokenRequest.this.mAPIEvent.stopTelemetryAndFlush();
            }
        });
    }

    /* access modifiers changed from: private */
    public void validateAcquireTokenRequest(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        URL url = StringExtensions.getUrl(authenticationRequest.getAuthority());
        if (url != null) {
            performAuthorityValidation(authenticationRequest, url);
            SwitchToBroker canSwitchToBroker = this.mBrokerProxy.canSwitchToBroker(authenticationRequest.getAuthority());
            if (canSwitchToBroker != SwitchToBroker.CANNOT_SWITCH_TO_BROKER && this.mBrokerProxy.verifyUser(authenticationRequest.getLoginHint(), authenticationRequest.getUserId()) && !authenticationRequest.isSilent()) {
                if (canSwitchToBroker != SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER) {
                    verifyBrokerRedirectUri(authenticationRequest);
                    return;
                }
                throw new UsageAuthenticationException(ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING, "Broker related permissions are missing for GET_ACCOUNTS.");
            }
            return;
        }
        throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL);
    }

    private void performAuthorityValidation(AuthenticationRequest authenticationRequest, URL url) throws AuthenticationException {
        Telemetry.getInstance().startEvent(authenticationRequest.getTelemetryRequestId(), "Microsoft.ADAL.authority_validation");
        APIEvent aPIEvent = new APIEvent("Microsoft.ADAL.authority_validation");
        aPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
        aPIEvent.setRequestId(authenticationRequest.getTelemetryRequestId());
        if (this.mAuthContext.getValidateAuthority()) {
            try {
                validateAuthority(url, authenticationRequest.getUpnSuffix(), authenticationRequest.isSilent(), authenticationRequest.getCorrelationId());
                aPIEvent.setValidationStatus("yes");
                Telemetry.getInstance().stopEvent(authenticationRequest.getTelemetryRequestId(), aPIEvent, "Microsoft.ADAL.authority_validation");
            } catch (AuthenticationException e) {
                if (e.getCode() == null || (!e.getCode().equals(ADALError.DEVICE_CONNECTION_IS_NOT_AVAILABLE) && !e.getCode().equals(ADALError.NO_NETWORK_CONNECTION_POWER_OPTIMIZATION))) {
                    aPIEvent.setValidationStatus("no");
                } else {
                    aPIEvent.setValidationStatus("not_done");
                }
                throw e;
            } catch (Throwable th) {
                Telemetry.getInstance().stopEvent(authenticationRequest.getTelemetryRequestId(), aPIEvent, "Microsoft.ADAL.authority_validation");
                throw th;
            }
        } else {
            if (!UrlExtensions.isADFSAuthority(url) && !AuthorityValidationMetadataCache.containsAuthorityHost(url)) {
                try {
                    this.mDiscovery.validateAuthority(url);
                } catch (AuthenticationException unused) {
                    AuthorityValidationMetadataCache.updateInstanceDiscoveryMap(url.getHost(), new InstanceDiscoveryMetadata(false));
                    StringBuilder sb = new StringBuilder();
                    sb.append(TAG);
                    sb.append(":performAuthorityValidation");
                    Logger.m236v(sb.toString(), "Fail to get authority validation metadata back. Ignore the failure since authority validation is turned off.");
                }
            }
            aPIEvent.setValidationStatus("not_done");
            Telemetry.getInstance().stopEvent(authenticationRequest.getTelemetryRequestId(), aPIEvent, "Microsoft.ADAL.authority_validation");
        }
        InstanceDiscoveryMetadata cachedInstanceDiscoveryMetadata = AuthorityValidationMetadataCache.getCachedInstanceDiscoveryMetadata(url);
        if (cachedInstanceDiscoveryMetadata != null && cachedInstanceDiscoveryMetadata.isValidated()) {
            updatePreferredNetworkLocation(url, authenticationRequest, cachedInstanceDiscoveryMetadata);
        }
    }

    private void updatePreferredNetworkLocation(URL url, AuthenticationRequest authenticationRequest, InstanceDiscoveryMetadata instanceDiscoveryMetadata) throws AuthenticationException {
        if (instanceDiscoveryMetadata != null && instanceDiscoveryMetadata.isValidated() && instanceDiscoveryMetadata.getPreferredNetwork() != null && !url.getHost().equalsIgnoreCase(instanceDiscoveryMetadata.getPreferredNetwork())) {
            try {
                authenticationRequest.setAuthority(Utility.constructAuthorityUrl(url, instanceDiscoveryMetadata.getPreferredNetwork()).toString());
            } catch (MalformedURLException unused) {
                Logger.m234i(TAG, "preferred network is invalid", "use exactly the same authority url that is passed");
            }
        }
    }

    private void validateAuthority(URL url, @Nullable String str, boolean z, UUID uuid) throws AuthenticationException {
        boolean isADFSAuthority = UrlExtensions.isADFSAuthority(url);
        if (!AuthorityValidationMetadataCache.isAuthorityValidated(url) && (!isADFSAuthority || !this.mAuthContext.getIsAuthorityValidated())) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":validateAuthority");
            Logger.m236v(sb.toString(), "Start validating authority");
            this.mDiscovery.setCorrelationId(uuid);
            Discovery.verifyAuthorityValidInstance(url);
            if (z || !isADFSAuthority || str == null) {
                if (z && UrlExtensions.isADFSAuthority(url)) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TAG);
                    sb2.append(":validateAuthority");
                    Logger.m236v(sb2.toString(), "Silent request. Skipping AD FS authority validation");
                }
                this.mDiscovery.validateAuthority(url);
            } else {
                this.mDiscovery.validateAuthorityADFS(url, str);
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append(":validateAuthority");
            Logger.m236v(sb3.toString(), "The passed in authority is valid.");
            this.mAuthContext.setIsAuthorityValidated(true);
        }
    }

    /* access modifiers changed from: private */
    public void performAcquireTokenRequest(CallbackHandler callbackHandler, IWindowComponent iWindowComponent, boolean z, AuthenticationRequest authenticationRequest) throws AuthenticationException {
        AuthenticationResult tryAcquireTokenSilent = tryAcquireTokenSilent(authenticationRequest);
        if (isAccessTokenReturned(tryAcquireTokenSilent)) {
            this.mAPIEvent.setWasApiCallSuccessful(true, null);
            this.mAPIEvent.setCorrelationId(authenticationRequest.getCorrelationId().toString());
            this.mAPIEvent.setIdToken(tryAcquireTokenSilent.getIdToken());
            this.mAPIEvent.stopTelemetryAndFlush();
            callbackHandler.onSuccess(tryAcquireTokenSilent);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":performAcquireTokenRequest");
        Logger.m230d(sb.toString(), "Trying to acquire token interactively.");
        acquireTokenInteractiveFlow(callbackHandler, iWindowComponent, z, authenticationRequest);
    }

    private AuthenticationResult tryAcquireTokenSilent(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        String str;
        if (!shouldTrySilentFlow(authenticationRequest)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":tryAcquireTokenSilent");
        Logger.m236v(sb.toString(), "Try to acquire token silently, return valid AT or use RT in the cache.");
        AuthenticationResult acquireTokenSilentFlow = acquireTokenSilentFlow(authenticationRequest);
        boolean isAccessTokenReturned = isAccessTokenReturned(acquireTokenSilentFlow);
        if (!isAccessTokenReturned && authenticationRequest.isSilent()) {
            if (acquireTokenSilentFlow == null) {
                str = "No result returned from acquireTokenSilent";
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" ErrorCode:");
                sb2.append(acquireTokenSilentFlow.getErrorCode());
                str = sb2.toString();
            }
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append(":tryAcquireTokenSilent");
            String sb4 = sb3.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Prompt is not allowed and failed to get token. ");
            sb5.append(str);
            Logger.m231e(sb4, sb5.toString(), authenticationRequest.getLogInfo(), ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED);
            ADALError aDALError = ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED;
            StringBuilder sb6 = new StringBuilder();
            sb6.append(authenticationRequest.getLogInfo());
            sb6.append(OAuth.SCOPE_DELIMITER);
            sb6.append(str);
            AuthenticationException authenticationException = new AuthenticationException(aDALError, sb6.toString());
            authenticationException.setHttpResponse(acquireTokenSilentFlow);
            throw authenticationException;
        } else if (!isAccessTokenReturned) {
            return acquireTokenSilentFlow;
        } else {
            StringBuilder sb7 = new StringBuilder();
            sb7.append(TAG);
            sb7.append(":tryAcquireTokenSilent");
            Logger.m236v(sb7.toString(), "Token is successfully returned from silent flow. ");
            return acquireTokenSilentFlow;
        }
    }

    private boolean shouldTrySilentFlow(AuthenticationRequest authenticationRequest) {
        return (!Utility.isClaimsChallengePresent(authenticationRequest) && authenticationRequest.getPrompt() == PromptBehavior.Auto) || authenticationRequest.isSilent();
    }

    private AuthenticationResult acquireTokenSilentFlow(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        AuthenticationResult tryAcquireTokenSilentLocally = tryAcquireTokenSilentLocally(authenticationRequest);
        if (isAccessTokenReturned(tryAcquireTokenSilentLocally)) {
            return tryAcquireTokenSilentLocally;
        }
        SwitchToBroker canSwitchToBroker = this.mBrokerProxy.canSwitchToBroker(authenticationRequest.getAuthority());
        if (canSwitchToBroker == SwitchToBroker.CANNOT_SWITCH_TO_BROKER || !this.mBrokerProxy.verifyUser(authenticationRequest.getLoginHint(), authenticationRequest.getUserId())) {
            return tryAcquireTokenSilentLocally;
        }
        if (canSwitchToBroker != SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":acquireTokenSilentFlow");
            Logger.m230d(sb.toString(), "Cannot get AT from local cache, switch to Broker for auth, clear tokens from local cache for the user.");
            removeTokensForUser(authenticationRequest);
            return tryAcquireTokenSilentWithBroker(authenticationRequest);
        }
        throw new UsageAuthenticationException(ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING, "Broker related permissions are missing for GET_ACCOUNTS");
    }

    private AuthenticationResult tryAcquireTokenSilentLocally(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":tryAcquireTokenSilentLocally");
        Logger.m236v(sb.toString(), "Try to silently get token from local cache.");
        return new AcquireTokenSilentHandler(this.mContext, authenticationRequest, this.mTokenCacheAccessor).getAccessToken();
    }

    private AuthenticationResult tryAcquireTokenSilentWithBroker(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        return new AcquireTokenWithBrokerRequest(authenticationRequest, this.mBrokerProxy).acquireTokenWithBrokerSilent();
    }

    private void removeTokensForUser(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        String str;
        if (this.mTokenCacheAccessor != null) {
            if (!StringExtensions.isNullOrBlank(authenticationRequest.getUserId())) {
                str = authenticationRequest.getUserId();
            } else {
                str = authenticationRequest.getLoginHint();
            }
            try {
                TokenCacheItem fRTItem = this.mTokenCacheAccessor.getFRTItem("1", str);
                if (fRTItem != null) {
                    this.mTokenCacheAccessor.removeTokenCacheItem(fRTItem, authenticationRequest.getResource());
                }
                try {
                    TokenCacheItem mRRTItem = this.mTokenCacheAccessor.getMRRTItem(authenticationRequest.getClientId(), str);
                    TokenCacheItem regularRefreshTokenCacheItem = this.mTokenCacheAccessor.getRegularRefreshTokenCacheItem(authenticationRequest.getResource(), authenticationRequest.getClientId(), str);
                    if (mRRTItem != null) {
                        this.mTokenCacheAccessor.removeTokenCacheItem(mRRTItem, authenticationRequest.getResource());
                    } else if (regularRefreshTokenCacheItem != null) {
                        this.mTokenCacheAccessor.removeTokenCacheItem(regularRefreshTokenCacheItem, authenticationRequest.getResource());
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(TAG);
                        sb.append(":removeTokensForUser");
                        Logger.m236v(sb.toString(), "No token items need to be deleted for the user.");
                    }
                } catch (MalformedURLException e) {
                    throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
                }
            } catch (MalformedURLException e2) {
                throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e2.getMessage(), (Throwable) e2);
            }
        }
    }

    private void acquireTokenInteractiveFlow(CallbackHandler callbackHandler, IWindowComponent iWindowComponent, boolean z, AuthenticationRequest authenticationRequest) throws AuthenticationException {
        if (iWindowComponent != null || z) {
            HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
            int hashCode = callbackHandler.getCallback().hashCode();
            authenticationRequest.setRequestId(hashCode);
            this.mAuthContext.putWaitingRequest(hashCode, new AuthenticationRequestState(hashCode, authenticationRequest, callbackHandler.getCallback(), this.mAPIEvent));
            SwitchToBroker canSwitchToBroker = this.mBrokerProxy.canSwitchToBroker(authenticationRequest.getAuthority());
            if (canSwitchToBroker == SwitchToBroker.CANNOT_SWITCH_TO_BROKER || !this.mBrokerProxy.verifyUser(authenticationRequest.getLoginHint(), authenticationRequest.getUserId())) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":acquireTokenInteractiveFlow");
                StringBuilder sb2 = new StringBuilder();
                sb2.append(" Callback is: ");
                sb2.append(callbackHandler.getCallback().hashCode());
                Logger.m237v(sb.toString(), "Starting Authentication Activity for embedded flow. ", sb2.toString(), null);
                new AcquireTokenInteractiveRequest(this.mContext, authenticationRequest, this.mTokenCacheAccessor, this).acquireToken(iWindowComponent, z);
            } else if (canSwitchToBroker != SwitchToBroker.NEED_PERMISSIONS_TO_SWITCH_TO_BROKER) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(TAG);
                sb3.append(":acquireTokenInteractiveFlow");
                StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(callbackHandler.getCallback().hashCode());
                Logger.m237v(sb3.toString(), "Launch activity for interactive authentication via broker with callback. ", sb4.toString(), null);
                new AcquireTokenWithBrokerRequest(authenticationRequest, this.mBrokerProxy).acquireTokenWithBrokerInteractively(iWindowComponent);
            } else {
                throw new UsageAuthenticationException(ADALError.DEVELOPER_BROKER_PERMISSIONS_MISSING, "Broker related permissions are missing for GET_ACCOUNTS");
            }
        } else {
            ADALError aDALError = ADALError.AUTH_REFRESH_FAILED_PROMPT_NOT_ALLOWED;
            StringBuilder sb5 = new StringBuilder();
            sb5.append(authenticationRequest.getLogInfo());
            sb5.append(" Cannot launch webview, acitivity is null.");
            throw new AuthenticationException(aDALError, sb5.toString());
        }
    }

    private void verifyBrokerRedirectUri(AuthenticationRequest authenticationRequest) throws UsageAuthenticationException {
        String redirectUri = authenticationRequest.getRedirectUri();
        String redirectUriForBroker = this.mAuthContext.getRedirectUriForBroker();
        if (StringExtensions.isNullOrBlank(redirectUri)) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":verifyBrokerRedirectUri");
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append("The redirect uri is expected to be:");
            sb3.append(redirectUriForBroker);
            Logger.m231e(sb2, "The redirectUri is null or blank. ", sb3.toString(), ADALError.DEVELOPER_REDIRECTURI_INVALID);
            throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The redirectUri is null or blank.");
        } else if (redirectUri.startsWith(Broker.BROWSER_EXT_INSTALL_PREFIX)) {
            PackageHelper packageHelper = new PackageHelper(this.mContext);
            try {
                String encode = URLEncoder.encode(this.mContext.getPackageName(), "UTF_8");
                String encode2 = URLEncoder.encode(packageHelper.getCurrentSignatureForPackage(this.mContext.getPackageName()), "UTF_8");
                StringBuilder sb4 = new StringBuilder();
                sb4.append(Broker.BROWSER_EXT_INSTALL_PREFIX);
                sb4.append(encode);
                sb4.append("/");
                if (!redirectUri.startsWith(sb4.toString())) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("This apps package name is: ");
                    sb5.append(encode);
                    sb5.append(" so the redirect uri is expected to be: ");
                    sb5.append(redirectUriForBroker);
                    String sb6 = sb5.toString();
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(TAG);
                    sb7.append(":verifyBrokerRedirectUri");
                    Logger.m231e(sb7.toString(), "The base64 url encoded package name component of the redirect uri does not match the expected value. ", sb6, ADALError.DEVELOPER_REDIRECTURI_INVALID);
                    throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The base64 url encoded package name component of the redirect uri does not match the expected value. ");
                } else if (redirectUri.equalsIgnoreCase(redirectUriForBroker)) {
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append(TAG);
                    sb8.append(":verifyBrokerRedirectUri");
                    Logger.m236v(sb8.toString(), "The broker redirect URI is valid.");
                } else {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append("This apps signature is: ");
                    sb9.append(encode2);
                    sb9.append(" so the redirect uri is expected to be: ");
                    sb9.append(redirectUriForBroker);
                    String sb10 = sb9.toString();
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append(TAG);
                    sb11.append(":verifyBrokerRedirectUri");
                    Logger.m231e(sb11.toString(), "The base64 url encoded signature component of the redirect uri does not match the expected value. ", sb10, ADALError.DEVELOPER_REDIRECTURI_INVALID);
                    throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The base64 url encoded signature component of the redirect uri does not match the expected value.");
                }
            } catch (UnsupportedEncodingException e) {
                StringBuilder sb12 = new StringBuilder();
                sb12.append(TAG);
                sb12.append(":verifyBrokerRedirectUri");
                Logger.m232e(sb12.toString(), ADALError.ENCODING_IS_NOT_SUPPORTED.getDescription(), e.getMessage(), ADALError.ENCODING_IS_NOT_SUPPORTED, e);
                throw new UsageAuthenticationException(ADALError.ENCODING_IS_NOT_SUPPORTED, "The verifying BrokerRedirectUri process failed because the base64 url encoding is not supported.", e);
            }
        } else {
            StringBuilder sb13 = new StringBuilder();
            sb13.append(" The valid broker redirect URI prefix: msauth so the redirect uri is expected to be: ");
            sb13.append(redirectUriForBroker);
            String sb14 = sb13.toString();
            StringBuilder sb15 = new StringBuilder();
            sb15.append(TAG);
            sb15.append(":verifyBrokerRedirectUri");
            Logger.m231e(sb15.toString(), "The prefix of the redirect uri does not match the expected value. ", sb14, ADALError.DEVELOPER_REDIRECTURI_INVALID);
            throw new UsageAuthenticationException(ADALError.DEVELOPER_REDIRECTURI_INVALID, "The prefix of the redirect uri does not match the expected value.");
        }
    }

    /* access modifiers changed from: 0000 */
    public void onActivityResult(int i, int i2, Intent intent) {
        int i3 = i2;
        Intent intent2 = intent;
        if (i == 1001) {
            getHandler();
            if (intent2 == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":onActivityResult");
                Logger.m231e(sb.toString(), "BROWSER_FLOW data is null.", "", ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
            } else {
                Bundle extras = intent.getExtras();
                final int i4 = extras.getInt(Browser.REQUEST_ID);
                try {
                    AuthenticationRequestState waitingRequest = this.mAuthContext.getWaitingRequest(i4);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TAG);
                    sb2.append(":onActivityResult");
                    String sb3 = sb2.toString();
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Waiting request found. RequestId:");
                    sb4.append(i4);
                    Logger.m236v(sb3, sb4.toString());
                    String correlationInfoFromWaitingRequest = this.mAuthContext.getCorrelationInfoFromWaitingRequest(waitingRequest);
                    if (i3 == 2004) {
                        String stringExtra = intent2.getStringExtra(Broker.ACCOUNT_ACCESS_TOKEN);
                        this.mBrokerProxy.saveAccount(intent2.getStringExtra(Broker.ACCOUNT_NAME));
                        Date date = new Date(intent2.getLongExtra(Broker.ACCOUNT_EXPIREDATE, 0));
                        String stringExtra2 = intent2.getStringExtra(Broker.ACCOUNT_IDTOKEN);
                        String stringExtra3 = intent2.getStringExtra(Broker.ACCOUNT_USERINFO_TENANTID);
                        UserInfo userInfoFromBrokerResult = UserInfo.getUserInfoFromBrokerResult(intent.getExtras());
                        String stringExtra4 = intent2.getStringExtra(CliTelemInfo.SERVER_ERROR);
                        String stringExtra5 = intent2.getStringExtra(CliTelemInfo.SERVER_SUBERROR);
                        String stringExtra6 = intent2.getStringExtra(CliTelemInfo.RT_AGE);
                        String stringExtra7 = intent2.getStringExtra(CliTelemInfo.SPE_RING);
                        AuthenticationResult authenticationResult = new AuthenticationResult(stringExtra, null, date, false, userInfoFromBrokerResult, stringExtra3, stringExtra2, null);
                        authenticationResult.setAuthority(intent2.getStringExtra(Broker.ACCOUNT_AUTHORITY));
                        CliTelemInfo cliTelemInfo = new CliTelemInfo();
                        cliTelemInfo.setServerErrorCode(stringExtra4);
                        cliTelemInfo.setServerSubErrorCode(stringExtra5);
                        cliTelemInfo.setRefreshTokenAge(stringExtra6);
                        cliTelemInfo.setSpeRing(stringExtra7);
                        authenticationResult.setCliTelemInfo(cliTelemInfo);
                        if (authenticationResult.getAccessToken() != null) {
                            waitingRequest.getAPIEvent().setWasApiCallSuccessful(true, null);
                            waitingRequest.getAPIEvent().setCorrelationId(waitingRequest.getRequest().getCorrelationId().toString());
                            waitingRequest.getAPIEvent().setIdToken(authenticationResult.getIdToken());
                            waitingRequest.getAPIEvent().setServerErrorCode(cliTelemInfo.getServerErrorCode());
                            waitingRequest.getAPIEvent().setServerSubErrorCode(cliTelemInfo.getServerSubErrorCode());
                            waitingRequest.getAPIEvent().setRefreshTokenAge(cliTelemInfo.getRefreshTokenAge());
                            waitingRequest.getAPIEvent().setSpeRing(cliTelemInfo.getSpeRing());
                            waitingRequest.getAPIEvent().stopTelemetryAndFlush();
                            waitingRequest.getDelegate().onSuccess(authenticationResult);
                        }
                    } else if (i3 == 2001) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(TAG);
                        sb5.append(":onActivityResult");
                        String sb6 = sb5.toString();
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("User cancelled the flow. RequestId:");
                        sb7.append(i4);
                        sb7.append(OAuth.SCOPE_DELIMITER);
                        sb7.append(correlationInfoFromWaitingRequest);
                        Logger.m236v(sb6, sb7.toString());
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("User cancelled the flow RequestId:");
                        sb8.append(i4);
                        sb8.append(correlationInfoFromWaitingRequest);
                        waitingRequestOnError(waitingRequest, i4, new AuthenticationCancelError(sb8.toString()));
                    } else if (i3 == 2006) {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append(TAG);
                        sb9.append(":onActivityResult");
                        Logger.m236v(sb9.toString(), "Device needs to have broker installed, we expect the apps to call usback when the broker is installed");
                        waitingRequestOnError(waitingRequest, i4, new AuthenticationException(ADALError.BROKER_APP_INSTALLATION_STARTED));
                    } else if (i3 == 2005) {
                        Serializable serializable = extras.getSerializable(Browser.RESPONSE_AUTHENTICATION_EXCEPTION);
                        if (serializable == null || !(serializable instanceof AuthenticationException)) {
                            waitingRequestOnError(waitingRequest, i4, new AuthenticationException(ADALError.WEBVIEW_RETURNED_INVALID_AUTHENTICATION_EXCEPTION, correlationInfoFromWaitingRequest));
                        } else {
                            AuthenticationException authenticationException = (AuthenticationException) serializable;
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append(TAG);
                            sb10.append(":onActivityResult");
                            Logger.m239w(sb10.toString(), "Webview returned exception.", authenticationException.getMessage(), ADALError.WEBVIEW_RETURNED_AUTHENTICATION_EXCEPTION);
                            waitingRequestOnError(waitingRequest, i4, authenticationException);
                        }
                    } else if (i3 == 2002) {
                        String string = extras.getString(Browser.RESPONSE_ERROR_CODE);
                        String string2 = extras.getString(Browser.RESPONSE_ERROR_MESSAGE);
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append(TAG);
                        sb11.append(":onActivityResult");
                        String sb12 = sb11.toString();
                        StringBuilder sb13 = new StringBuilder();
                        sb13.append("Error info:");
                        sb13.append(string);
                        sb13.append(" for requestId: ");
                        sb13.append(i4);
                        sb13.append(OAuth.SCOPE_DELIMITER);
                        sb13.append(correlationInfoFromWaitingRequest);
                        Logger.m237v(sb12, sb13.toString(), string2, null);
                        ADALError aDALError = ADALError.SERVER_INVALID_REQUEST;
                        StringBuilder sb14 = new StringBuilder();
                        sb14.append(string);
                        sb14.append(OAuth.SCOPE_DELIMITER);
                        sb14.append(string2);
                        sb14.append(correlationInfoFromWaitingRequest);
                        waitingRequestOnError(waitingRequest, i4, new AuthenticationException(aDALError, sb14.toString()));
                    } else if (i3 == 2003) {
                        AuthenticationRequest authenticationRequest = (AuthenticationRequest) extras.getSerializable(Browser.RESPONSE_REQUEST_INFO);
                        String string3 = extras.getString(Browser.RESPONSE_FINAL_URL, "");
                        if (string3.isEmpty()) {
                            StringBuilder sb15 = new StringBuilder("Webview did not reach the redirectUrl. ");
                            if (authenticationRequest != null) {
                                sb15.append(authenticationRequest.getLogInfo());
                            }
                            sb15.append(correlationInfoFromWaitingRequest);
                            AuthenticationException authenticationException2 = new AuthenticationException(ADALError.WEBVIEW_RETURNED_EMPTY_REDIRECT_URL, sb15.toString());
                            StringBuilder sb16 = new StringBuilder();
                            sb16.append(TAG);
                            sb16.append(":onActivityResult");
                            Logger.m231e(sb16.toString(), "", authenticationException2.getMessage(), authenticationException2.getCode());
                            waitingRequestOnError(waitingRequest, i4, authenticationException2);
                        } else {
                            final CallbackHandler callbackHandler = new CallbackHandler(getHandler(), waitingRequest.getDelegate());
                            ExecutorService executorService = THREAD_EXECUTOR;
                            final AuthenticationRequestState authenticationRequestState = waitingRequest;
                            final String str = string3;
                            C16853 r0 = new Runnable() {
                                public void run() {
                                    try {
                                        AuthenticationResult acquireTokenWithAuthCode = new AcquireTokenInteractiveRequest(AcquireTokenRequest.this.mContext, authenticationRequestState.getRequest(), AcquireTokenRequest.this.mTokenCacheAccessor, AcquireTokenRequest.this).acquireTokenWithAuthCode(str);
                                        authenticationRequestState.getAPIEvent().setWasApiCallSuccessful(true, null);
                                        authenticationRequestState.getAPIEvent().setCorrelationId(authenticationRequestState.getRequest().getCorrelationId().toString());
                                        authenticationRequestState.getAPIEvent().setIdToken(acquireTokenWithAuthCode.getIdToken());
                                        authenticationRequestState.getAPIEvent().stopTelemetryAndFlush();
                                        if (authenticationRequestState.getDelegate() != null) {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append(AcquireTokenRequest.TAG);
                                            sb.append(":onActivityResult");
                                            Logger.m237v(sb.toString(), "Sending result to callback. ", authenticationRequestState.getRequest().getLogInfo(), null);
                                            callbackHandler.onSuccess(acquireTokenWithAuthCode);
                                        }
                                    } catch (AuthenticationException e) {
                                        StringBuilder sb2 = new StringBuilder(e.getMessage());
                                        if (e.getCause() != null) {
                                            sb2.append(e.getCause().getMessage());
                                        }
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append(AcquireTokenRequest.TAG);
                                        sb3.append(":onActivityResult");
                                        String sb4 = sb3.toString();
                                        String description = (e.getCode() == null ? ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN : e.getCode()).getDescription();
                                        StringBuilder sb5 = new StringBuilder();
                                        sb5.append(sb2.toString());
                                        sb5.append(TokenParser.f498SP);
                                        sb5.append(ExceptionExtensions.getExceptionMessage(e));
                                        sb5.append(TokenParser.f498SP);
                                        sb5.append(Log.getStackTraceString(e));
                                        Logger.m232e(sb4, description, sb5.toString(), ADALError.AUTHORIZATION_CODE_NOT_EXCHANGED_FOR_TOKEN, null);
                                        AcquireTokenRequest.this.waitingRequestOnError(callbackHandler, authenticationRequestState, i4, null);
                                    }
                                }
                            };
                            executorService.execute(r0);
                        }
                    }
                } catch (AuthenticationException unused) {
                    StringBuilder sb17 = new StringBuilder();
                    sb17.append(TAG);
                    sb17.append(":onActivityResult");
                    String sb18 = sb17.toString();
                    StringBuilder sb19 = new StringBuilder();
                    sb19.append("Failed to find waiting request. RequestId:");
                    sb19.append(i4);
                    Logger.m231e(sb18, sb19.toString(), "", ADALError.ON_ACTIVITY_RESULT_INTENT_NULL);
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    @RequiresApi(api = 21)
    public void processCertification(@NonNull Activity activity, @NonNull final ClientCertRequest clientCertRequest, @NonNull final String str, @NonNull final BasicWebViewClient basicWebViewClient) {
        Principal[] principals = clientCertRequest.getPrincipals();
        if (principals != null) {
            for (Principal name : principals) {
                if (name.getName().contains("CN=MS-Organization-Access")) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TAG);
                    sb.append(str);
                    Logger.m236v(sb.toString(), "Cancelling the TLS request, not respond to TLS challenge triggered by device authentication.");
                    clientCertRequest.cancel();
                    return;
                }
            }
        }
        KeyChain.choosePrivateKeyAlias(activity, new KeyChainAliasCallback() {
            @TargetApi(21)
            public void alias(String str) {
                if (str == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(AcquireTokenRequest.TAG);
                    sb.append(str);
                    Logger.m236v(sb.toString(), "No certificate chosen by user, cancelling the TLS request.");
                    clientCertRequest.cancel();
                    return;
                }
                try {
                    X509Certificate[] certificateChain = KeyChain.getCertificateChain(AcquireTokenRequest.this.mContext.getApplicationContext(), str);
                    PrivateKey privateKey = KeyChain.getPrivateKey(basicWebViewClient.getCallingContext(), str);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(AcquireTokenRequest.TAG);
                    sb2.append(str);
                    Logger.m236v(sb2.toString(), "Certificate is chosen by user, proceed with TLS request.");
                    clientCertRequest.proceed(privateKey, certificateChain);
                } catch (KeyChainException e) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(AcquireTokenRequest.TAG);
                    sb3.append(str);
                    Logger.m233e(sb3.toString(), "KeyChain exception", e);
                    clientCertRequest.cancel();
                } catch (InterruptedException e2) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(AcquireTokenRequest.TAG);
                    sb4.append(str);
                    Logger.m233e(sb4.toString(), "InterruptedException exception", e2);
                    clientCertRequest.cancel();
                }
            }
        }, clientCertRequest.getKeyTypes(), clientCertRequest.getPrincipals(), clientCertRequest.getHost(), clientCertRequest.getPort(), null);
    }

    private boolean isAccessTokenReturned(AuthenticationResult authenticationResult) {
        return authenticationResult != null && !StringExtensions.isNullOrBlank(authenticationResult.getAccessToken());
    }

    private synchronized Handler getHandler() {
        if (sHandler == null) {
            HandlerThread handlerThread = new HandlerThread("AcquireTokenRequestHandlerThread");
            handlerThread.start();
            sHandler = new Handler(handlerThread.getLooper());
        }
        return sHandler;
    }

    private void waitingRequestOnError(AuthenticationRequestState authenticationRequestState, int i, AuthenticationException authenticationException) {
        waitingRequestOnError(null, authenticationRequestState, i, authenticationException);
    }

    /* access modifiers changed from: private */
    public void waitingRequestOnError(CallbackHandler callbackHandler, AuthenticationRequestState authenticationRequestState, int i, AuthenticationException authenticationException) {
        if (authenticationRequestState != null) {
            try {
                if (authenticationRequestState.getDelegate() != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(TAG);
                    sb.append(":waitingRequestOnError");
                    String sb2 = sb.toString();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Sending error to callback");
                    sb3.append(this.mAuthContext.getCorrelationInfoFromWaitingRequest(authenticationRequestState));
                    Logger.m236v(sb2, sb3.toString());
                    authenticationRequestState.getAPIEvent().setWasApiCallSuccessful(false, authenticationException);
                    authenticationRequestState.getAPIEvent().setCorrelationId(authenticationRequestState.getRequest().getCorrelationId().toString());
                    authenticationRequestState.getAPIEvent().stopTelemetryAndFlush();
                    if (callbackHandler != null) {
                        callbackHandler.onError(authenticationException);
                    } else {
                        authenticationRequestState.getDelegate().onError(authenticationException);
                    }
                }
            } catch (Throwable th) {
                if (authenticationException != null) {
                    this.mAuthContext.removeWaitingRequest(i);
                }
                throw th;
            }
        }
        if (authenticationException != null) {
            this.mAuthContext.removeWaitingRequest(i);
        }
    }
}
