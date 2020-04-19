package com.microsoft.aad.adal;

import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.MalformedURLException;

class AcquireTokenSilentHandler {
    private static final String TAG = "AcquireTokenSilentHandler";
    private boolean mAttemptedWithMRRT = false;
    private final AuthenticationRequest mAuthRequest;
    private final Context mContext;
    private TokenCacheItem mMrrtTokenCacheItem;
    private final TokenCacheAccessor mTokenCacheAccessor;
    private IWebRequestHandler mWebRequestHandler = null;

    AcquireTokenSilentHandler(Context context, AuthenticationRequest authenticationRequest, TokenCacheAccessor tokenCacheAccessor) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        } else if (authenticationRequest != null) {
            this.mContext = context;
            this.mAuthRequest = authenticationRequest;
            this.mTokenCacheAccessor = tokenCacheAccessor;
            this.mWebRequestHandler = new WebRequestHandler();
        } else {
            throw new IllegalArgumentException("authRequest");
        }
    }

    /* access modifiers changed from: 0000 */
    public AuthenticationResult getAccessToken() throws AuthenticationException {
        TokenCacheAccessor tokenCacheAccessor = this.mTokenCacheAccessor;
        if (tokenCacheAccessor == null) {
            return null;
        }
        TokenCacheItem aTFromCache = tokenCacheAccessor.getATFromCache(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
        if (aTFromCache == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":getAccessToken");
            Logger.m236v(sb.toString(), "No valid access token exists, try with refresh token.");
            return tryRT();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(TAG);
        sb2.append(":getAccessToken");
        Logger.m236v(sb2.toString(), "Return AT from cache.");
        return AuthenticationResult.createResult(aTFromCache);
    }

    /* access modifiers changed from: 0000 */
    public AuthenticationResult acquireTokenWithRefreshToken(String str) throws AuthenticationException {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":acquireTokenWithRefreshToken");
        Logger.m237v(sb.toString(), "Try to get new access token with the found refresh token.", this.mAuthRequest.getLogInfo(), null);
        HttpWebRequest.throwIfNetworkNotAvailable(this.mContext);
        try {
            AuthenticationResult refreshToken = new Oauth2(this.mAuthRequest, this.mWebRequestHandler, new JWSBuilder()).refreshToken(str);
            if (refreshToken != null && StringExtensions.isNullOrBlank(refreshToken.getRefreshToken())) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(TAG);
                sb2.append(":acquireTokenWithRefreshToken");
                Logger.m234i(sb2.toString(), "Refresh token is not returned or empty", "");
                refreshToken.setRefreshToken(str);
            }
            return refreshToken;
        } catch (ServerRespondingWithRetryableException e) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append(":acquireTokenWithRefreshToken");
            String sb4 = sb3.toString();
            StringBuilder sb5 = new StringBuilder();
            sb5.append("The server is not responding after the retry with error code: ");
            sb5.append(e.getCode());
            Logger.m234i(sb4, sb5.toString(), "");
            TokenCacheItem staleToken = this.mTokenCacheAccessor.getStaleToken(this.mAuthRequest);
            if (staleToken != null) {
                AuthenticationResult createExtendedLifeTimeResult = AuthenticationResult.createExtendedLifeTimeResult(staleToken);
                StringBuilder sb6 = new StringBuilder();
                sb6.append(TAG);
                sb6.append(":acquireTokenWithRefreshToken");
                Logger.m234i(sb6.toString(), "The result with stale access token is returned.", "");
                return createExtendedLifeTimeResult;
            }
            StringBuilder sb7 = new StringBuilder();
            sb7.append(TAG);
            sb7.append(":acquireTokenWithRefreshToken");
            String sb8 = sb7.toString();
            StringBuilder sb9 = new StringBuilder();
            sb9.append("Request: ");
            sb9.append(this.mAuthRequest.getLogInfo());
            sb9.append(OAuth.SCOPE_DELIMITER);
            sb9.append(ExceptionExtensions.getExceptionMessage(e));
            sb9.append(OAuth.SCOPE_DELIMITER);
            sb9.append(Log.getStackTraceString(e));
            Logger.m232e(sb8, "Error in refresh token for request. ", sb9.toString(), ADALError.AUTH_FAILED_NO_TOKEN, null);
            throw new AuthenticationException(ADALError.AUTH_FAILED_NO_TOKEN, ExceptionExtensions.getExceptionMessage(e), (Throwable) new AuthenticationException(ADALError.SERVER_ERROR, e.getMessage(), (Throwable) e));
        } catch (AuthenticationException | IOException e2) {
            StringBuilder sb10 = new StringBuilder();
            sb10.append(TAG);
            sb10.append(":acquireTokenWithRefreshToken");
            String sb11 = sb10.toString();
            StringBuilder sb12 = new StringBuilder();
            sb12.append("Request: ");
            sb12.append(this.mAuthRequest.getLogInfo());
            sb12.append(OAuth.SCOPE_DELIMITER);
            sb12.append(ExceptionExtensions.getExceptionMessage(e2));
            sb12.append(OAuth.SCOPE_DELIMITER);
            sb12.append(Log.getStackTraceString(e2));
            Logger.m232e(sb11, "Error in refresh token for request.", sb12.toString(), ADALError.AUTH_FAILED_NO_TOKEN, null);
            throw new AuthenticationException(ADALError.AUTH_FAILED_NO_TOKEN, ExceptionExtensions.getExceptionMessage(e2), (Throwable) new AuthenticationException(ADALError.SERVER_ERROR, e2.getMessage(), (Throwable) e2));
        }
    }

    /* access modifiers changed from: 0000 */
    public void setWebRequestHandler(IWebRequestHandler iWebRequestHandler) {
        this.mWebRequestHandler = iWebRequestHandler;
    }

    private AuthenticationResult tryRT() throws AuthenticationException {
        try {
            TokenCacheItem regularRefreshTokenCacheItem = this.mTokenCacheAccessor.getRegularRefreshTokenCacheItem(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
            if (regularRefreshTokenCacheItem == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":tryRT");
                Logger.m236v(sb.toString(), "Regular token cache entry does not exist, try with MRRT.");
                return tryMRRT();
            } else if (regularRefreshTokenCacheItem.getIsMultiResourceRefreshToken() || isMRRTEntryExisted()) {
                String str = regularRefreshTokenCacheItem.getIsMultiResourceRefreshToken() ? "Found RT and it's also a MRRT, retry with MRRT" : "RT is found and there is a MRRT entry existed, try with MRRT";
                StringBuilder sb2 = new StringBuilder();
                sb2.append(TAG);
                sb2.append(":tryRT");
                Logger.m236v(sb2.toString(), str);
                return tryMRRT();
            } else if (!StringExtensions.isNullOrBlank(this.mAuthRequest.getUserFromRequest()) || !this.mTokenCacheAccessor.isMultipleRTsMatchingGivenAppAndResource(this.mAuthRequest.getClientId(), this.mAuthRequest.getResource())) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(TAG);
                sb3.append(":tryRT");
                Logger.m236v(sb3.toString(), "Send request to use regular RT for new AT.");
                return acquireTokenWithCachedItem(regularRefreshTokenCacheItem);
            } else {
                throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH, "Multiple refresh tokens exists for the given client id and resource");
            }
        } catch (MalformedURLException e) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
        }
    }

    private AuthenticationResult tryMRRT() throws AuthenticationException {
        String str;
        try {
            this.mMrrtTokenCacheItem = this.mTokenCacheAccessor.getMRRTItem(this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
            TokenCacheItem tokenCacheItem = this.mMrrtTokenCacheItem;
            if (tokenCacheItem == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":tryMRRT");
                Logger.m236v(sb.toString(), "MRRT token does not exist, try with FRT");
                return tryFRT("1", null);
            } else if (tokenCacheItem.isFamilyToken()) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(TAG);
                sb2.append(":tryMRRT");
                Logger.m236v(sb2.toString(), "MRRT item exists but it's also a FRT, try with FRT.");
                return tryFRT(this.mMrrtTokenCacheItem.getFamilyClientId(), null);
            } else {
                AuthenticationResult useMRRT = useMRRT();
                if (isTokenRequestFailed(useMRRT)) {
                    if (StringExtensions.isNullOrBlank(this.mMrrtTokenCacheItem.getFamilyClientId())) {
                        str = "1";
                    } else {
                        str = this.mMrrtTokenCacheItem.getFamilyClientId();
                    }
                    useMRRT = tryFRT(str, useMRRT);
                }
                return useMRRT;
            }
        } catch (MalformedURLException e) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
        }
    }

    private AuthenticationResult tryFRT(String str, AuthenticationResult authenticationResult) throws AuthenticationException {
        try {
            TokenCacheItem fRTItem = this.mTokenCacheAccessor.getFRTItem(str, this.mAuthRequest.getUserFromRequest());
            if (fRTItem != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":tryFRT");
                Logger.m236v(sb.toString(), "Send request to use FRT for new AT.");
                AuthenticationResult acquireTokenWithCachedItem = acquireTokenWithCachedItem(fRTItem);
                if (isTokenRequestFailed(acquireTokenWithCachedItem) && !this.mAttemptedWithMRRT) {
                    AuthenticationResult useMRRT = useMRRT();
                    if (useMRRT != null) {
                        acquireTokenWithCachedItem = useMRRT;
                    }
                }
                return acquireTokenWithCachedItem;
            } else if (this.mAttemptedWithMRRT) {
                return authenticationResult;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(TAG);
                sb2.append(":tryFRT");
                Logger.m236v(sb2.toString(), "FRT cache item does not exist, fall back to try MRRT.");
                return useMRRT();
            }
        } catch (MalformedURLException e) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
        }
    }

    private AuthenticationResult useMRRT() throws AuthenticationException {
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":useMRRT");
        Logger.m236v(sb.toString(), "Send request to use MRRT for new AT.");
        this.mAttemptedWithMRRT = true;
        TokenCacheItem tokenCacheItem = this.mMrrtTokenCacheItem;
        if (tokenCacheItem != null) {
            return acquireTokenWithCachedItem(tokenCacheItem);
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(TAG);
        sb2.append(":useMRRT");
        Logger.m236v(sb2.toString(), "MRRT does not exist, cannot proceed with MRRT for new AT.");
        return null;
    }

    private AuthenticationResult acquireTokenWithCachedItem(TokenCacheItem tokenCacheItem) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(tokenCacheItem.getRefreshToken())) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":acquireTokenWithCachedItem");
            Logger.m237v(sb.toString(), "Token cache item contains empty refresh token, cannot continue refresh token request", this.mAuthRequest.getLogInfo(), null);
            return null;
        }
        AuthenticationResult acquireTokenWithRefreshToken = acquireTokenWithRefreshToken(tokenCacheItem.getRefreshToken());
        if (acquireTokenWithRefreshToken != null && !acquireTokenWithRefreshToken.isExtendedLifeTimeToken()) {
            this.mTokenCacheAccessor.updateCachedItemWithResult(this.mAuthRequest.getResource(), this.mAuthRequest.getClientId(), acquireTokenWithRefreshToken, tokenCacheItem);
        }
        return acquireTokenWithRefreshToken;
    }

    private boolean isMRRTEntryExisted() throws AuthenticationException {
        try {
            TokenCacheItem mRRTItem = this.mTokenCacheAccessor.getMRRTItem(this.mAuthRequest.getClientId(), this.mAuthRequest.getUserFromRequest());
            return mRRTItem != null && !StringExtensions.isNullOrBlank(mRRTItem.getRefreshToken());
        } catch (MalformedURLException e) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
        }
    }

    private boolean isTokenRequestFailed(AuthenticationResult authenticationResult) {
        return authenticationResult != null && !StringExtensions.isNullOrBlank(authenticationResult.getErrorCode());
    }
}
