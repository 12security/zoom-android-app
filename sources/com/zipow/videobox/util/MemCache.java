package com.zipow.videobox.util;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.HashMap;
import java.util.LinkedList;

public class MemCache<Key, Value> {
    @NonNull
    private HashMap<Key, Value> mCacheMap = new HashMap<>();
    private int mCacheSize = 0;
    @NonNull
    private LinkedList<Key> mKeyList = new LinkedList<>();

    public MemCache(int i) {
        this.mCacheSize = i;
    }

    public void cacheItem(Key key, Value value) {
        this.mKeyList.remove(key);
        this.mCacheMap.put(key, value);
        this.mKeyList.add(key);
        if (this.mKeyList.size() > this.mCacheSize) {
            Object removeFirst = this.mKeyList.removeFirst();
            if (removeFirst != null) {
                this.mCacheMap.remove(removeFirst);
            }
        }
    }

    public void removeItem(Key key) {
        this.mKeyList.remove(key);
        this.mCacheMap.remove(key);
    }

    @Nullable
    public Value getCachedItem(Key key) {
        Value value = this.mCacheMap.get(key);
        if (value != null) {
            this.mKeyList.remove(key);
            this.mKeyList.add(key);
        }
        return value;
    }

    public void clear() {
        this.mKeyList.clear();
        this.mCacheMap.clear();
    }
}
