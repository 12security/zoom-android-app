package com.microsoft.aad.adal;

import java.io.Serializable;
import java.util.Iterator;

public interface ITokenCacheStore extends Serializable {
    boolean contains(String str);

    Iterator<TokenCacheItem> getAll();

    TokenCacheItem getItem(String str);

    void removeAll();

    void removeItem(String str);

    void setItem(String str, TokenCacheItem tokenCacheItem);
}
