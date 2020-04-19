package com.microsoft.aad.adal;

import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class TokenCacheItem implements Serializable {
    private static final String TAG = "TokenCacheItem";
    private static final long serialVersionUID = 1;
    private String mAccessToken;
    private String mAuthority;
    private String mClientId;
    private Date mExpiresOn;
    private Date mExtendedExpiresOn;
    private String mFamilyClientId;
    private boolean mIsMultiResourceRefreshToken;
    private String mRawIdToken;
    private String mRefreshtoken;
    private String mResource;
    private String mSpeRing;
    private String mTenantId;
    private UserInfo mUserInfo;

    public TokenCacheItem() {
    }

    TokenCacheItem(TokenCacheItem tokenCacheItem) {
        this.mAuthority = tokenCacheItem.getAuthority();
        this.mResource = tokenCacheItem.getResource();
        this.mClientId = tokenCacheItem.getClientId();
        this.mAccessToken = tokenCacheItem.getAccessToken();
        this.mRefreshtoken = tokenCacheItem.getRefreshToken();
        this.mRawIdToken = tokenCacheItem.getRawIdToken();
        this.mUserInfo = tokenCacheItem.getUserInfo();
        this.mExpiresOn = tokenCacheItem.getExpiresOn();
        this.mIsMultiResourceRefreshToken = tokenCacheItem.getIsMultiResourceRefreshToken();
        this.mTenantId = tokenCacheItem.getTenantId();
        this.mFamilyClientId = tokenCacheItem.getFamilyClientId();
        this.mExtendedExpiresOn = tokenCacheItem.getExtendedExpiresOn();
        this.mSpeRing = tokenCacheItem.getSpeRing();
    }

    private TokenCacheItem(String str, AuthenticationResult authenticationResult) {
        if (authenticationResult == null) {
            throw new IllegalArgumentException("authenticationResult");
        } else if (!StringExtensions.isNullOrBlank(str)) {
            this.mAuthority = str;
            this.mExpiresOn = authenticationResult.getExpiresOn();
            this.mIsMultiResourceRefreshToken = authenticationResult.getIsMultiResourceRefreshToken();
            this.mTenantId = authenticationResult.getTenantId();
            this.mUserInfo = authenticationResult.getUserInfo();
            this.mRawIdToken = authenticationResult.getIdToken();
            this.mRefreshtoken = authenticationResult.getRefreshToken();
            this.mFamilyClientId = authenticationResult.getFamilyClientId();
            this.mExtendedExpiresOn = authenticationResult.getExtendedExpiresOn();
            if (authenticationResult.getCliTelemInfo() != null) {
                this.mSpeRing = authenticationResult.getCliTelemInfo().getSpeRing();
            }
        } else {
            throw new IllegalArgumentException(OAuth2.AUTHORITY);
        }
    }

    public static TokenCacheItem createRegularTokenCacheItem(String str, String str2, String str3, AuthenticationResult authenticationResult) {
        TokenCacheItem tokenCacheItem = new TokenCacheItem(str, authenticationResult);
        tokenCacheItem.setClientId(str3);
        tokenCacheItem.setResource(str2);
        tokenCacheItem.setAccessToken(authenticationResult.getAccessToken());
        return tokenCacheItem;
    }

    public static TokenCacheItem createMRRTTokenCacheItem(String str, String str2, AuthenticationResult authenticationResult) {
        TokenCacheItem tokenCacheItem = new TokenCacheItem(str, authenticationResult);
        tokenCacheItem.setClientId(str2);
        return tokenCacheItem;
    }

    public static TokenCacheItem createFRRTTokenCacheItem(String str, AuthenticationResult authenticationResult) {
        return new TokenCacheItem(str, authenticationResult);
    }

    public UserInfo getUserInfo() {
        return this.mUserInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.mUserInfo = userInfo;
    }

    public String getResource() {
        return this.mResource;
    }

    public void setResource(String str) {
        this.mResource = str;
    }

    public String getAuthority() {
        return this.mAuthority;
    }

    public void setAuthority(String str) {
        this.mAuthority = str;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public void setClientId(String str) {
        this.mClientId = str;
    }

    public String getAccessToken() {
        return this.mAccessToken;
    }

    public void setAccessToken(String str) {
        this.mAccessToken = str;
    }

    public String getRefreshToken() {
        return this.mRefreshtoken;
    }

    public void setRefreshToken(String str) {
        this.mRefreshtoken = str;
    }

    public Date getExpiresOn() {
        return Utility.getImmutableDateObject(this.mExpiresOn);
    }

    public void setExpiresOn(Date date) {
        this.mExpiresOn = Utility.getImmutableDateObject(date);
    }

    public boolean getIsMultiResourceRefreshToken() {
        return this.mIsMultiResourceRefreshToken;
    }

    public void setIsMultiResourceRefreshToken(boolean z) {
        this.mIsMultiResourceRefreshToken = z;
    }

    public String getTenantId() {
        return this.mTenantId;
    }

    public void setTenantId(String str) {
        this.mTenantId = str;
    }

    public String getRawIdToken() {
        return this.mRawIdToken;
    }

    public void setRawIdToken(String str) {
        this.mRawIdToken = str;
    }

    public final String getFamilyClientId() {
        return this.mFamilyClientId;
    }

    public final void setFamilyClientId(String str) {
        this.mFamilyClientId = str;
    }

    public final void setExtendedExpiresOn(Date date) {
        this.mExtendedExpiresOn = Utility.getImmutableDateObject(date);
    }

    public final Date getExtendedExpiresOn() {
        return Utility.getImmutableDateObject(this.mExtendedExpiresOn);
    }

    public final boolean isExtendedLifetimeValid() {
        if (this.mExtendedExpiresOn == null || StringExtensions.isNullOrBlank(this.mAccessToken)) {
            return false;
        }
        return !isTokenExpired(this.mExtendedExpiresOn);
    }

    public static boolean isTokenExpired(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.add(13, AuthenticationSettings.INSTANCE.getExpirationBuffer());
        Date time = instance.getTime();
        StringBuilder sb = new StringBuilder();
        sb.append("expiresOn:");
        sb.append(date);
        sb.append(" timeWithBuffer:");
        sb.append(instance.getTime());
        sb.append(" Buffer:");
        sb.append(AuthenticationSettings.INSTANCE.getExpirationBuffer());
        Logger.m234i(TAG, "Check token expiration time.", sb.toString());
        return date != null && date.before(time);
    }

    /* access modifiers changed from: 0000 */
    public TokenEntryType getTokenEntryType() {
        if (!StringExtensions.isNullOrBlank(getResource())) {
            return TokenEntryType.REGULAR_TOKEN_ENTRY;
        }
        if (StringExtensions.isNullOrBlank(getClientId())) {
            return TokenEntryType.FRT_TOKEN_ENTRY;
        }
        return TokenEntryType.MRRT_TOKEN_ENTRY;
    }

    /* access modifiers changed from: 0000 */
    public boolean isFamilyToken() {
        return !StringExtensions.isNullOrBlank(this.mFamilyClientId);
    }

    /* access modifiers changed from: 0000 */
    public String getSpeRing() {
        return this.mSpeRing;
    }

    /* access modifiers changed from: 0000 */
    public void setSpeRing(String str) {
        this.mSpeRing = str;
    }
}
