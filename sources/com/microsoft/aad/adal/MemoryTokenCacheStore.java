package com.microsoft.aad.adal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MemoryTokenCacheStore implements ITokenCacheStore {
    private static final String TAG = "MemoryTokenCacheStore";
    private static final long serialVersionUID = 3465700945655867086L;
    private final Map<String, TokenCacheItem> mCache = new HashMap();
    private transient Object mCacheLock = new Object();

    public TokenCacheItem getItem(String str) {
        TokenCacheItem tokenCacheItem;
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Key:");
            sb.append(str);
            Logger.m234i(TAG, "Get Item from cache. ", sb.toString());
            synchronized (this.mCacheLock) {
                tokenCacheItem = (TokenCacheItem) this.mCache.get(str);
            }
            return tokenCacheItem;
        }
        throw new IllegalArgumentException("The input key is null.");
    }

    public void setItem(String str, TokenCacheItem tokenCacheItem) {
        if (tokenCacheItem == null) {
            throw new IllegalArgumentException("item");
        } else if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Key: ");
            sb.append(str);
            Logger.m234i(TAG, "Set Item to cache. ", sb.toString());
            synchronized (this.mCacheLock) {
                this.mCache.put(str, tokenCacheItem);
            }
        } else {
            throw new IllegalArgumentException("key");
        }
    }

    public void removeItem(String str) {
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Key:");
            sb.append(str.hashCode());
            Logger.m234i(TAG, "Remove Item from cache. ", sb.toString());
            synchronized (this.mCacheLock) {
                this.mCache.remove(str);
            }
            return;
        }
        throw new IllegalArgumentException("key");
    }

    public void removeAll() {
        Logger.m236v(TAG, "Remove all items from cache.");
        synchronized (this.mCacheLock) {
            this.mCache.clear();
        }
    }

    private synchronized void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        this.mCacheLock = new Object();
    }

    public boolean contains(String str) {
        boolean z;
        if (str != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("Key: ");
            sb.append(str);
            Logger.m234i(TAG, "contains Item from cache.", sb.toString());
            synchronized (this.mCacheLock) {
                z = this.mCache.get(str) != null;
            }
            return z;
        }
        throw new IllegalArgumentException("key");
    }

    public Iterator<TokenCacheItem> getAll() {
        Iterator<TokenCacheItem> it;
        Logger.m236v(TAG, "Retrieving all items from cache. ");
        synchronized (this.mCacheLock) {
            it = this.mCache.values().iterator();
        }
        return it;
    }
}
