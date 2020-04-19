package com.microsoft.aad.adal;

import com.microsoft.aad.adal.AuthenticationConstants.OAuth2;
import com.microsoft.aad.adal.AuthenticationResult.AuthenticationStatus;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class TokenCacheAccessor {
    private static final String TAG = "TokenCacheAccessor";
    private String mAuthority;
    private final String mTelemetryRequestId;
    private final ITokenCacheStore mTokenCacheStore;

    TokenCacheAccessor(ITokenCacheStore iTokenCacheStore, String str, String str2) {
        if (iTokenCacheStore == null) {
            throw new IllegalArgumentException("tokenCacheStore");
        } else if (StringExtensions.isNullOrBlank(str)) {
            throw new IllegalArgumentException(OAuth2.AUTHORITY);
        } else if (!StringExtensions.isNullOrBlank(str2)) {
            this.mTokenCacheStore = iTokenCacheStore;
            this.mAuthority = str;
            this.mTelemetryRequestId = str2;
        } else {
            throw new IllegalArgumentException("requestId");
        }
    }

    /* access modifiers changed from: 0000 */
    public TokenCacheItem getATFromCache(String str, String str2, String str3) throws AuthenticationException {
        try {
            TokenCacheItem regularRefreshTokenCacheItem = getRegularRefreshTokenCacheItem(str, str2, str3);
            if (regularRefreshTokenCacheItem == null) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":getATFromCache");
                Logger.m236v(sb.toString(), "No access token exists.");
                return null;
            }
            throwIfMultipleATExisted(str2, str, str3);
            if (!StringExtensions.isNullOrBlank(regularRefreshTokenCacheItem.getAccessToken())) {
                if (TokenCacheItem.isTokenExpired(regularRefreshTokenCacheItem.getExpiresOn())) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(TAG);
                    sb2.append(":getATFromCache");
                    Logger.m236v(sb2.toString(), "Access token exists, but already expired.");
                    return null;
                } else if (isUserMisMatch(str3, regularRefreshTokenCacheItem)) {
                    throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH);
                }
            }
            return regularRefreshTokenCacheItem;
        } catch (MalformedURLException e) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
        }
    }

    /* access modifiers changed from: 0000 */
    public TokenCacheItem getRegularRefreshTokenCacheItem(String str, String str2, String str3) throws MalformedURLException {
        CacheEvent startCacheTelemetryRequest = startCacheTelemetryRequest("Microsoft.ADAL.rt");
        TokenCacheItem item = this.mTokenCacheStore.getItem(CacheKey.createCacheKeyForRTEntry(getAuthorityUrlWithPreferredCache(), str, str2, str3));
        if (item == null) {
            item = performAdditionalCacheLookup(str, str2, null, str3, TokenEntryType.REGULAR_TOKEN_ENTRY);
        }
        if (item != null) {
            startCacheTelemetryRequest.setTokenTypeRT(true);
            startCacheTelemetryRequest.setSpeRing(item.getSpeRing());
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
        return item;
    }

    /* access modifiers changed from: 0000 */
    public TokenCacheItem getMRRTItem(String str, String str2) throws MalformedURLException {
        CacheEvent startCacheTelemetryRequest = startCacheTelemetryRequest("Microsoft.ADAL.mrrt");
        TokenCacheItem item = this.mTokenCacheStore.getItem(CacheKey.createCacheKeyForMRRT(getAuthorityUrlWithPreferredCache(), str, str2));
        if (item == null) {
            item = performAdditionalCacheLookup(null, str, null, str2, TokenEntryType.MRRT_TOKEN_ENTRY);
        }
        if (item != null) {
            startCacheTelemetryRequest.setTokenTypeMRRT(true);
            startCacheTelemetryRequest.setTokenTypeFRT(item.isFamilyToken());
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
        return item;
    }

    /* access modifiers changed from: 0000 */
    public TokenCacheItem getFRTItem(String str, String str2) throws MalformedURLException {
        CacheEvent startCacheTelemetryRequest = startCacheTelemetryRequest("Microsoft.ADAL.frt");
        if (StringExtensions.isNullOrBlank(str2)) {
            Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
            return null;
        }
        TokenCacheItem item = this.mTokenCacheStore.getItem(CacheKey.createCacheKeyForFRT(getAuthorityUrlWithPreferredCache(), str, str2));
        if (item == null) {
            item = performAdditionalCacheLookup(null, null, str, str2, TokenEntryType.FRT_TOKEN_ENTRY);
        }
        if (item != null) {
            startCacheTelemetryRequest.setTokenTypeFRT(true);
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, startCacheTelemetryRequest, "Microsoft.ADAL.token_cache_lookup");
        return item;
    }

    /* access modifiers changed from: 0000 */
    public TokenCacheItem getStaleToken(AuthenticationRequest authenticationRequest) throws AuthenticationException {
        try {
            TokenCacheItem regularRefreshTokenCacheItem = getRegularRefreshTokenCacheItem(authenticationRequest.getResource(), authenticationRequest.getClientId(), authenticationRequest.getUserFromRequest());
            if (regularRefreshTokenCacheItem == null || StringExtensions.isNullOrBlank(regularRefreshTokenCacheItem.getAccessToken()) || regularRefreshTokenCacheItem.getExtendedExpiresOn() == null || TokenCacheItem.isTokenExpired(regularRefreshTokenCacheItem.getExtendedExpiresOn())) {
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":getStaleToken");
                Logger.m234i(sb.toString(), "The stale access token is not found.", "");
                return null;
            }
            throwIfMultipleATExisted(authenticationRequest.getClientId(), authenticationRequest.getResource(), authenticationRequest.getUserFromRequest());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":getStaleToken");
            Logger.m234i(sb2.toString(), "The stale access token is returned.", "");
            return regularRefreshTokenCacheItem;
        } catch (MalformedURLException e) {
            throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateCachedItemWithResult(String str, String str2, AuthenticationResult authenticationResult, TokenCacheItem tokenCacheItem) throws AuthenticationException {
        if (authenticationResult == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(TAG);
            sb.append(":updateCachedItemWithResult");
            Logger.m236v(sb.toString(), "AuthenticationResult is null, cannot update cache.");
            throw new IllegalArgumentException("result");
        } else if (authenticationResult.getStatus() == AuthenticationStatus.Succeeded) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":updateCachedItemWithResult");
            Logger.m236v(sb2.toString(), "Save returned AuthenticationResult into cache.");
            if (!(tokenCacheItem == null || tokenCacheItem.getUserInfo() == null || authenticationResult.getUserInfo() != null)) {
                authenticationResult.setUserInfo(tokenCacheItem.getUserInfo());
                authenticationResult.setIdToken(tokenCacheItem.getRawIdToken());
                authenticationResult.setTenantId(tokenCacheItem.getTenantId());
            }
            try {
                updateTokenCache(str, str2, authenticationResult);
            } catch (MalformedURLException e) {
                throw new AuthenticationException(ADALError.DEVELOPER_AUTHORITY_IS_NOT_VALID_URL, e.getMessage(), (Throwable) e);
            }
        } else if ("invalid_grant".equalsIgnoreCase(authenticationResult.getErrorCode())) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append(":updateCachedItemWithResult");
            Logger.m236v(sb3.toString(), "Received INVALID_GRANT error code, remove existing cache entry.");
            removeTokenCacheItem(tokenCacheItem, str);
        }
    }

    /* access modifiers changed from: 0000 */
    public void updateTokenCache(String str, String str2, AuthenticationResult authenticationResult) throws MalformedURLException {
        if (authenticationResult != null && !StringExtensions.isNullOrBlank(authenticationResult.getAccessToken())) {
            if (authenticationResult.getUserInfo() != null) {
                if (!StringExtensions.isNullOrBlank(authenticationResult.getUserInfo().getDisplayableId())) {
                    setItemToCacheForUser(str, str2, authenticationResult, authenticationResult.getUserInfo().getDisplayableId());
                }
                if (!StringExtensions.isNullOrBlank(authenticationResult.getUserInfo().getUserId())) {
                    setItemToCacheForUser(str, str2, authenticationResult, authenticationResult.getUserInfo().getUserId());
                }
            }
            setItemToCacheForUser(str, str2, authenticationResult, null);
        }
    }

    /* access modifiers changed from: 0000 */
    public void removeTokenCacheItem(TokenCacheItem tokenCacheItem, String str) throws AuthenticationException {
        List<String> list;
        CacheEvent cacheEvent = new CacheEvent("Microsoft.ADAL.token_cache_delete");
        cacheEvent.setRequestId(this.mTelemetryRequestId);
        Telemetry.getInstance().startEvent(this.mTelemetryRequestId, "Microsoft.ADAL.token_cache_delete");
        switch (tokenCacheItem.getTokenEntryType()) {
            case REGULAR_TOKEN_ENTRY:
                cacheEvent.setTokenTypeRT(true);
                StringBuilder sb = new StringBuilder();
                sb.append(TAG);
                sb.append(":removeTokenCacheItem");
                Logger.m236v(sb.toString(), "Regular RT was used to get access token, remove entries for regular RT entries.");
                list = getKeyListToRemoveForRT(tokenCacheItem);
                break;
            case MRRT_TOKEN_ENTRY:
                cacheEvent.setTokenTypeMRRT(true);
                StringBuilder sb2 = new StringBuilder();
                sb2.append(TAG);
                sb2.append(":removeTokenCacheItem");
                Logger.m236v(sb2.toString(), "MRRT was used to get access token, remove entries for both MRRT entries and regular RT entries.");
                List keyListToRemoveForMRRT = getKeyListToRemoveForMRRT(tokenCacheItem);
                TokenCacheItem tokenCacheItem2 = new TokenCacheItem(tokenCacheItem);
                tokenCacheItem2.setResource(str);
                keyListToRemoveForMRRT.addAll(getKeyListToRemoveForRT(tokenCacheItem2));
                list = keyListToRemoveForMRRT;
                break;
            case FRT_TOKEN_ENTRY:
                cacheEvent.setTokenTypeFRT(true);
                StringBuilder sb3 = new StringBuilder();
                sb3.append(TAG);
                sb3.append(":removeTokenCacheItem");
                Logger.m236v(sb3.toString(), "FRT was used to get access token, remove entries for FRT entries.");
                list = getKeyListToRemoveForFRT(tokenCacheItem);
                break;
            default:
                throw new AuthenticationException(ADALError.INVALID_TOKEN_CACHE_ITEM);
        }
        for (String removeItem : list) {
            this.mTokenCacheStore.removeItem(removeItem);
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, cacheEvent, "Microsoft.ADAL.token_cache_delete");
    }

    /* access modifiers changed from: 0000 */
    public boolean isMultipleRTsMatchingGivenAppAndResource(String str, String str2) {
        Iterator all = this.mTokenCacheStore.getAll();
        ArrayList arrayList = new ArrayList();
        while (all.hasNext()) {
            TokenCacheItem tokenCacheItem = (TokenCacheItem) all.next();
            if (tokenCacheItem.getAuthority().equalsIgnoreCase(this.mAuthority) && str.equalsIgnoreCase(tokenCacheItem.getClientId()) && str2.equalsIgnoreCase(tokenCacheItem.getResource()) && !tokenCacheItem.getIsMultiResourceRefreshToken()) {
                arrayList.add(tokenCacheItem);
            }
        }
        return arrayList.size() > 1;
    }

    /* access modifiers changed from: 0000 */
    public boolean isMultipleMRRTsMatchingGivenApp(String str) {
        Iterator all = this.mTokenCacheStore.getAll();
        ArrayList arrayList = new ArrayList();
        while (all.hasNext()) {
            TokenCacheItem tokenCacheItem = (TokenCacheItem) all.next();
            if (tokenCacheItem.getAuthority().equalsIgnoreCase(this.mAuthority) && tokenCacheItem.getClientId().equalsIgnoreCase(str)) {
                if (tokenCacheItem.getIsMultiResourceRefreshToken() || StringExtensions.isNullOrBlank(tokenCacheItem.getResource())) {
                    arrayList.add(tokenCacheItem);
                }
            }
        }
        return arrayList.size() > 1;
    }

    private void setItemToCacheForUser(String str, String str2, AuthenticationResult authenticationResult, String str3) throws MalformedURLException {
        logReturnedToken(authenticationResult);
        StringBuilder sb = new StringBuilder();
        sb.append(TAG);
        sb.append(":setItemToCacheForUser");
        Logger.m236v(sb.toString(), "Save regular token into cache.");
        CacheEvent cacheEvent = new CacheEvent("Microsoft.ADAL.token_cache_write");
        cacheEvent.setRequestId(this.mTelemetryRequestId);
        Telemetry.getInstance().startEvent(this.mTelemetryRequestId, "Microsoft.ADAL.token_cache_write");
        if (!StringExtensions.isNullOrBlank(authenticationResult.getAuthority())) {
            this.mAuthority = authenticationResult.getAuthority();
        }
        this.mTokenCacheStore.setItem(CacheKey.createCacheKeyForRTEntry(getAuthorityUrlWithPreferredCache(), str, str2, str3), TokenCacheItem.createRegularTokenCacheItem(getAuthorityUrlWithPreferredCache(), str, str2, authenticationResult));
        cacheEvent.setTokenTypeRT(true);
        if (authenticationResult.getIsMultiResourceRefreshToken()) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(TAG);
            sb2.append(":setItemToCacheForUser");
            Logger.m236v(sb2.toString(), "Save Multi Resource Refresh token to cache.");
            this.mTokenCacheStore.setItem(CacheKey.createCacheKeyForMRRT(getAuthorityUrlWithPreferredCache(), str2, str3), TokenCacheItem.createMRRTTokenCacheItem(getAuthorityUrlWithPreferredCache(), str2, authenticationResult));
            cacheEvent.setTokenTypeMRRT(true);
        }
        if (!StringExtensions.isNullOrBlank(authenticationResult.getFamilyClientId()) && !StringExtensions.isNullOrBlank(str3)) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(TAG);
            sb3.append(":setItemToCacheForUser");
            Logger.m236v(sb3.toString(), "Save Family Refresh token into cache.");
            this.mTokenCacheStore.setItem(CacheKey.createCacheKeyForFRT(getAuthorityUrlWithPreferredCache(), authenticationResult.getFamilyClientId(), str3), TokenCacheItem.createFRRTTokenCacheItem(getAuthorityUrlWithPreferredCache(), authenticationResult));
            cacheEvent.setTokenTypeFRT(true);
        }
        Telemetry.getInstance().stopEvent(this.mTelemetryRequestId, cacheEvent, "Microsoft.ADAL.token_cache_write");
    }

    private List<String> getKeyListToRemoveForRT(TokenCacheItem tokenCacheItem) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(CacheKey.createCacheKeyForRTEntry(this.mAuthority, tokenCacheItem.getResource(), tokenCacheItem.getClientId(), null));
        if (tokenCacheItem.getUserInfo() != null) {
            arrayList.add(CacheKey.createCacheKeyForRTEntry(this.mAuthority, tokenCacheItem.getResource(), tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getDisplayableId()));
            arrayList.add(CacheKey.createCacheKeyForRTEntry(this.mAuthority, tokenCacheItem.getResource(), tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getUserId()));
        }
        return arrayList;
    }

    private List<String> getKeyListToRemoveForMRRT(TokenCacheItem tokenCacheItem) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(CacheKey.createCacheKeyForMRRT(this.mAuthority, tokenCacheItem.getClientId(), null));
        if (tokenCacheItem.getUserInfo() != null) {
            arrayList.add(CacheKey.createCacheKeyForMRRT(this.mAuthority, tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getDisplayableId()));
            arrayList.add(CacheKey.createCacheKeyForMRRT(this.mAuthority, tokenCacheItem.getClientId(), tokenCacheItem.getUserInfo().getUserId()));
        }
        return arrayList;
    }

    private List<String> getKeyListToRemoveForFRT(TokenCacheItem tokenCacheItem) {
        ArrayList arrayList = new ArrayList();
        if (tokenCacheItem.getUserInfo() != null) {
            arrayList.add(CacheKey.createCacheKeyForFRT(this.mAuthority, tokenCacheItem.getFamilyClientId(), tokenCacheItem.getUserInfo().getDisplayableId()));
            arrayList.add(CacheKey.createCacheKeyForFRT(this.mAuthority, tokenCacheItem.getFamilyClientId(), tokenCacheItem.getUserInfo().getUserId()));
        }
        return arrayList;
    }

    private boolean isUserMisMatch(String str, TokenCacheItem tokenCacheItem) {
        boolean z = false;
        if (StringExtensions.isNullOrBlank(str) || tokenCacheItem.getUserInfo() == null) {
            return false;
        }
        if (!str.equalsIgnoreCase(tokenCacheItem.getUserInfo().getDisplayableId()) && !str.equalsIgnoreCase(tokenCacheItem.getUserInfo().getUserId())) {
            z = true;
        }
        return z;
    }

    private void throwIfMultipleATExisted(String str, String str2, String str3) throws AuthenticationException {
        if (StringExtensions.isNullOrBlank(str3) && isMultipleRTsMatchingGivenAppAndResource(str, str2)) {
            throw new AuthenticationException(ADALError.AUTH_FAILED_USER_MISMATCH, "No user is provided and multiple access tokens exist for the given app and resource.");
        }
    }

    private void logReturnedToken(AuthenticationResult authenticationResult) {
        if (authenticationResult != null && authenticationResult.getAccessToken() != null) {
            Logger.m234i(TAG, "Access tokenID and refresh tokenID returned. ", null);
        }
    }

    private CacheEvent startCacheTelemetryRequest(String str) {
        CacheEvent cacheEvent = new CacheEvent("Microsoft.ADAL.token_cache_lookup");
        cacheEvent.setTokenType(str);
        cacheEvent.setRequestId(this.mTelemetryRequestId);
        Telemetry.getInstance().startEvent(this.mTelemetryRequestId, "Microsoft.ADAL.token_cache_lookup");
        return cacheEvent;
    }

    private TokenCacheItem performAdditionalCacheLookup(String str, String str2, String str3, String str4, TokenEntryType tokenEntryType) throws MalformedURLException {
        TokenCacheItem tokenCacheItemFromPassedInAuthority = getTokenCacheItemFromPassedInAuthority(str, str2, str3, str4, tokenEntryType);
        return tokenCacheItemFromPassedInAuthority == null ? getTokenCacheItemFromAliasedHost(str, str2, str3, str4, tokenEntryType) : tokenCacheItemFromPassedInAuthority;
    }

    private TokenCacheItem getTokenCacheItemFromPassedInAuthority(String str, String str2, String str3, String str4, TokenEntryType tokenEntryType) throws MalformedURLException {
        if (getAuthorityUrlWithPreferredCache().equalsIgnoreCase(this.mAuthority)) {
            return null;
        }
        return this.mTokenCacheStore.getItem(getCacheKey(this.mAuthority, str, str2, str4, str3, tokenEntryType));
    }

    private TokenCacheItem getTokenCacheItemFromAliasedHost(String str, String str2, String str3, String str4, TokenEntryType tokenEntryType) throws MalformedURLException {
        InstanceDiscoveryMetadata instanceDiscoveryMetadata = getInstanceDiscoveryMetadata();
        TokenCacheItem tokenCacheItem = null;
        if (instanceDiscoveryMetadata == null) {
            return null;
        }
        Iterator it = instanceDiscoveryMetadata.getAliases().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String constructAuthorityUrl = constructAuthorityUrl((String) it.next());
            if (!constructAuthorityUrl.equalsIgnoreCase(this.mAuthority) && !constructAuthorityUrl.equalsIgnoreCase(getAuthorityUrlWithPreferredCache())) {
                TokenCacheItem item = this.mTokenCacheStore.getItem(getCacheKey(constructAuthorityUrl, str, str2, str4, str3, tokenEntryType));
                if (item != null) {
                    tokenCacheItem = item;
                    break;
                }
            }
        }
        return tokenCacheItem;
    }

    private String getCacheKey(String str, String str2, String str3, String str4, String str5, TokenEntryType tokenEntryType) {
        String str6;
        switch (tokenEntryType) {
            case REGULAR_TOKEN_ENTRY:
                str6 = CacheKey.createCacheKeyForRTEntry(str, str2, str3, str4);
                break;
            case MRRT_TOKEN_ENTRY:
                str6 = CacheKey.createCacheKeyForMRRT(str, str3, str4);
                break;
            case FRT_TOKEN_ENTRY:
                str6 = CacheKey.createCacheKeyForFRT(str, str5, str4);
                break;
            default:
                return null;
        }
        return str6;
    }

    /* access modifiers changed from: 0000 */
    public String getAuthorityUrlWithPreferredCache() throws MalformedURLException {
        InstanceDiscoveryMetadata instanceDiscoveryMetadata = getInstanceDiscoveryMetadata();
        if (instanceDiscoveryMetadata == null || !instanceDiscoveryMetadata.isValidated()) {
            return this.mAuthority;
        }
        return constructAuthorityUrl(instanceDiscoveryMetadata.getPreferredCache());
    }

    private String constructAuthorityUrl(String str) throws MalformedURLException {
        URL url = new URL(this.mAuthority);
        if (url.getHost().equalsIgnoreCase(str)) {
            return this.mAuthority;
        }
        return Utility.constructAuthorityUrl(url, str).toString();
    }

    private InstanceDiscoveryMetadata getInstanceDiscoveryMetadata() throws MalformedURLException {
        return AuthorityValidationMetadataCache.getCachedInstanceDiscoveryMetadata(new URL(this.mAuthority));
    }
}
