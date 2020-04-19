package com.microsoft.aad.adal;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public interface ITokenStoreQuery {
    void clearTokensForUser(String str);

    Iterator<TokenCacheItem> getAll();

    List<TokenCacheItem> getTokensAboutToExpire();

    List<TokenCacheItem> getTokensForResource(String str);

    List<TokenCacheItem> getTokensForUser(String str);

    Set<String> getUniqueUsersWithTokenCache();
}
