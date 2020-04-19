package com.microsoft.aad.adal;

import com.microsoft.aad.adal.AuthenticationConstants.AAD;
import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import java.io.Serializable;
import java.util.Locale;

public final class CacheKey implements Serializable {
    static final String FRT_ENTRY_PREFIX = "foci-";
    private static final long serialVersionUID = 8067972995583126404L;
    private String mAuthority;
    private String mClientId;
    private String mFamilyClientId;
    private boolean mIsMultipleResourceRefreshToken;
    private String mResource;
    private String mUserId;

    private CacheKey() {
    }

    public String toString() {
        if (StringExtensions.isNullOrBlank(this.mFamilyClientId)) {
            Locale locale = Locale.US;
            String str = "%s$%s$%s$%s$%s";
            Object[] objArr = new Object[5];
            objArr[0] = this.mAuthority;
            objArr[1] = this.mResource;
            objArr[2] = this.mClientId;
            objArr[3] = this.mIsMultipleResourceRefreshToken ? "y" : "n";
            objArr[4] = this.mUserId;
            return String.format(locale, str, objArr);
        }
        Locale locale2 = Locale.US;
        String str2 = "%s$%s$%s$%s$%s$%s";
        Object[] objArr2 = new Object[6];
        objArr2[0] = this.mAuthority;
        objArr2[1] = this.mResource;
        objArr2[2] = this.mClientId;
        objArr2[3] = this.mIsMultipleResourceRefreshToken ? "y" : "n";
        objArr2[4] = this.mUserId;
        objArr2[5] = this.mFamilyClientId;
        return String.format(locale2, str2, objArr2);
    }

    public static String createCacheKey(String str, String str2, String str3, boolean z, String str4, String str5) {
        if (str == null) {
            throw new IllegalArgumentException(OAuth2.AUTHORITY);
        } else if (str3 == null && str5 == null) {
            throw new IllegalArgumentException("both clientId and familyClientId are null");
        } else {
            CacheKey cacheKey = new CacheKey();
            if (!z) {
                if (str2 != null) {
                    cacheKey.mResource = str2;
                } else {
                    throw new IllegalArgumentException(AAD.RESOURCE);
                }
            }
            cacheKey.mAuthority = str.toLowerCase(Locale.US);
            if (cacheKey.mAuthority.endsWith("/")) {
                String str6 = cacheKey.mAuthority;
                cacheKey.mAuthority = (String) str6.subSequence(0, str6.length() - 1);
            }
            if (str3 != null) {
                cacheKey.mClientId = str3.toLowerCase(Locale.US);
            }
            if (str5 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(FRT_ENTRY_PREFIX);
                sb.append(str5);
                cacheKey.mFamilyClientId = sb.toString().toLowerCase(Locale.US);
            }
            cacheKey.mIsMultipleResourceRefreshToken = z;
            if (!StringExtensions.isNullOrBlank(str4)) {
                cacheKey.mUserId = str4.toLowerCase(Locale.US);
            }
            return cacheKey.toString();
        }
    }

    public static String createCacheKey(TokenCacheItem tokenCacheItem) throws AuthenticationException {
        if (tokenCacheItem != null) {
            String str = null;
            if (tokenCacheItem.getUserInfo() != null) {
                str = tokenCacheItem.getUserInfo().getUserId();
            }
            switch (tokenCacheItem.getTokenEntryType()) {
                case REGULAR_TOKEN_ENTRY:
                    return createCacheKeyForRTEntry(tokenCacheItem.getAuthority(), tokenCacheItem.getResource(), tokenCacheItem.getClientId(), str);
                case MRRT_TOKEN_ENTRY:
                    return createCacheKeyForMRRT(tokenCacheItem.getAuthority(), tokenCacheItem.getClientId(), str);
                case FRT_TOKEN_ENTRY:
                    return createCacheKeyForFRT(tokenCacheItem.getAuthority(), tokenCacheItem.getFamilyClientId(), str);
                default:
                    throw new AuthenticationException(ADALError.INVALID_TOKEN_CACHE_ITEM, "Cannot create cachekey from given token item");
            }
        } else {
            throw new IllegalArgumentException("TokenCacheItem");
        }
    }

    public static String createCacheKeyForRTEntry(String str, String str2, String str3, String str4) {
        return createCacheKey(str, str2, str3, false, str4, null);
    }

    public static String createCacheKeyForMRRT(String str, String str2, String str3) {
        return createCacheKey(str, null, str2, true, str3, null);
    }

    public static String createCacheKeyForFRT(String str, String str2, String str3) {
        return createCacheKey(str, null, null, true, str3, str2);
    }

    public String getAuthority() {
        return this.mAuthority;
    }

    public String getResource() {
        return this.mResource;
    }

    public String getClientId() {
        return this.mClientId;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public boolean getIsMultipleResourceRefreshToken() {
        return this.mIsMultipleResourceRefreshToken;
    }
}
