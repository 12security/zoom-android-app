package com.microsoft.aad.adal;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;

public class AuthenticationResult implements Serializable {
    private static final long serialVersionUID = 2243372613182536368L;
    private String mAccessToken;
    private String mAuthority;
    private CliTelemInfo mCliTelemInfo;
    private String mCode;
    private String mErrorCode;
    private String mErrorCodes;
    private String mErrorDescription;
    private Date mExpiresOn;
    private Date mExtendedExpiresOn;
    private String mFamilyClientId;
    private HashMap<String, String> mHttpResponseBody;
    private HashMap<String, List<String>> mHttpResponseHeaders;
    private String mIdToken;
    private boolean mInitialRequest;
    private boolean mIsExtendedLifeTimeToken;
    private boolean mIsMultiResourceRefreshToken;
    private String mRefreshToken;
    private int mServiceStatusCode;
    private AuthenticationStatus mStatus;
    private String mTenantId;
    private String mTokenType;
    private UserInfo mUserInfo;

    public enum AuthenticationStatus {
        Cancelled,
        Failed,
        Succeeded
    }

    AuthenticationResult() {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = null;
    }

    AuthenticationResult(String str) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = str;
        this.mStatus = AuthenticationStatus.Succeeded;
        this.mAccessToken = null;
        this.mRefreshToken = null;
    }

    AuthenticationResult(String str, String str2, Date date, boolean z, UserInfo userInfo, String str3, String str4, Date date2) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = null;
        this.mAccessToken = str;
        this.mRefreshToken = str2;
        this.mExpiresOn = date;
        this.mIsMultiResourceRefreshToken = z;
        this.mStatus = AuthenticationStatus.Succeeded;
        this.mUserInfo = userInfo;
        this.mTenantId = str3;
        this.mIdToken = str4;
        this.mExtendedExpiresOn = date2;
    }

    AuthenticationResult(String str, String str2, Date date, boolean z, Date date2) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mCode = null;
        this.mAccessToken = str;
        this.mRefreshToken = str2;
        this.mExpiresOn = date;
        this.mIsMultiResourceRefreshToken = z;
        this.mStatus = AuthenticationStatus.Succeeded;
        this.mExtendedExpiresOn = date2;
    }

    AuthenticationResult(String str, String str2, String str3) {
        this.mStatus = AuthenticationStatus.Failed;
        this.mIsExtendedLifeTimeToken = false;
        this.mHttpResponseBody = null;
        this.mServiceStatusCode = -1;
        this.mHttpResponseHeaders = null;
        this.mErrorCode = str;
        this.mErrorDescription = str2;
        this.mErrorCodes = str3;
        this.mStatus = AuthenticationStatus.Failed;
    }

    static AuthenticationResult createResult(TokenCacheItem tokenCacheItem) {
        if (tokenCacheItem == null) {
            AuthenticationResult authenticationResult = new AuthenticationResult();
            authenticationResult.mStatus = AuthenticationStatus.Failed;
            return authenticationResult;
        }
        AuthenticationResult authenticationResult2 = new AuthenticationResult(tokenCacheItem.getAccessToken(), tokenCacheItem.getRefreshToken(), tokenCacheItem.getExpiresOn(), tokenCacheItem.getIsMultiResourceRefreshToken(), tokenCacheItem.getUserInfo(), tokenCacheItem.getTenantId(), tokenCacheItem.getRawIdToken(), tokenCacheItem.getExtendedExpiresOn());
        return authenticationResult2;
    }

    static AuthenticationResult createResultForInitialRequest() {
        AuthenticationResult authenticationResult = new AuthenticationResult();
        authenticationResult.mInitialRequest = true;
        return authenticationResult;
    }

    static AuthenticationResult createExtendedLifeTimeResult(TokenCacheItem tokenCacheItem) {
        AuthenticationResult createResult = createResult(tokenCacheItem);
        createResult.setExpiresOn(createResult.getExtendedExpiresOn());
        createResult.setIsExtendedLifeTimeToken(true);
        return createResult;
    }

    public String createAuthorizationHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ");
        sb.append(getAccessToken());
        return sb.toString();
    }

    public String getAccessToken() {
        return this.mAccessToken;
    }

    public String getRefreshToken() {
        return this.mRefreshToken;
    }

    public String getAccessTokenType() {
        return this.mTokenType;
    }

    public Date getExpiresOn() {
        return Utility.getImmutableDateObject(this.mExpiresOn);
    }

    public boolean getIsMultiResourceRefreshToken() {
        return this.mIsMultiResourceRefreshToken;
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    /* access modifiers changed from: 0000 */
    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public String getTenantId() {
        return this.mTenantId;
    }

    public AuthenticationStatus getStatus() {
        return this.mStatus;
    }

    /* access modifiers changed from: 0000 */
    public String getCode() {
        return this.mCode;
    }

    /* access modifiers changed from: 0000 */
    public void setCode(String str) {
        this.mCode = str;
    }

    public String getErrorCode() {
        return this.mErrorCode;
    }

    public String getErrorDescription() {
        return this.mErrorDescription;
    }

    public String getErrorLogInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ErrorCode:");
        sb.append(getErrorCode());
        return sb.toString();
    }

    public boolean isExpired() {
        if (this.mIsExtendedLifeTimeToken) {
            return TokenCacheItem.isTokenExpired(getExtendedExpiresOn());
        }
        return TokenCacheItem.isTokenExpired(getExpiresOn());
    }

    public final String getAuthority() {
        return this.mAuthority;
    }

    /* access modifiers changed from: 0000 */
    public String[] getErrorCodes() {
        String str = this.mErrorCodes;
        if (str != null) {
            return str.replaceAll("[\\[\\]]", "").split("([^,]),");
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public boolean isInitialRequest() {
        return this.mInitialRequest;
    }

    public String getIdToken() {
        return this.mIdToken;
    }

    public boolean isExtendedLifeTimeToken() {
        return this.mIsExtendedLifeTimeToken;
    }

    /* access modifiers changed from: 0000 */
    public final void setIsExtendedLifeTimeToken(boolean z) {
        this.mIsExtendedLifeTimeToken = z;
    }

    /* access modifiers changed from: 0000 */
    public final void setExtendedExpiresOn(Date date) {
        this.mExtendedExpiresOn = date;
    }

    /* access modifiers changed from: 0000 */
    public final Date getExtendedExpiresOn() {
        return this.mExtendedExpiresOn;
    }

    /* access modifiers changed from: 0000 */
    public final void setExpiresOn(Date date) {
        this.mExpiresOn = date;
    }

    /* access modifiers changed from: 0000 */
    public void setIdToken(String str) {
        this.mIdToken = str;
    }

    /* access modifiers changed from: 0000 */
    public void setTenantId(String str) {
        this.mTenantId = str;
    }

    /* access modifiers changed from: 0000 */
    public void setRefreshToken(String str) {
        this.mRefreshToken = str;
    }

    /* access modifiers changed from: 0000 */
    public final String getFamilyClientId() {
        return this.mFamilyClientId;
    }

    /* access modifiers changed from: 0000 */
    public final void setFamilyClientId(String str) {
        this.mFamilyClientId = str;
    }

    /* access modifiers changed from: 0000 */
    public final void setAuthority(String str) {
        if (!StringExtensions.isNullOrBlank(str)) {
            this.mAuthority = str;
        }
    }

    /* access modifiers changed from: 0000 */
    public final CliTelemInfo getCliTelemInfo() {
        return this.mCliTelemInfo;
    }

    /* access modifiers changed from: 0000 */
    public final void setCliTelemInfo(CliTelemInfo cliTelemInfo) {
        this.mCliTelemInfo = cliTelemInfo;
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponseBody(HashMap<String, String> hashMap) {
        this.mHttpResponseBody = hashMap;
    }

    public HashMap<String, String> getHttpResponseBody() {
        return this.mHttpResponseBody;
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponseHeaders(HashMap<String, List<String>> hashMap) {
        this.mHttpResponseHeaders = hashMap;
    }

    public HashMap<String, List<String>> getHttpResponseHeaders() {
        return this.mHttpResponseHeaders;
    }

    /* access modifiers changed from: 0000 */
    public void setServiceStatusCode(int i) {
        this.mServiceStatusCode = i;
    }

    public int getServiceStatusCode() {
        return this.mServiceStatusCode;
    }

    /* access modifiers changed from: 0000 */
    public void setHttpResponse(HttpWebResponse httpWebResponse) {
        if (httpWebResponse != null) {
            this.mServiceStatusCode = httpWebResponse.getStatusCode();
            if (httpWebResponse.getResponseHeaders() != null) {
                this.mHttpResponseHeaders = new HashMap<>(httpWebResponse.getResponseHeaders());
            }
            if (httpWebResponse.getBody() != null) {
                try {
                    this.mHttpResponseBody = new HashMap<>(HashMapExtensions.getJsonResponse(httpWebResponse));
                } catch (JSONException e) {
                    Logger.m231e(AuthenticationException.class.getSimpleName(), "Json exception", ExceptionExtensions.getExceptionMessage(e), ADALError.SERVER_INVALID_JSON_RESPONSE);
                }
            }
        }
    }
}
