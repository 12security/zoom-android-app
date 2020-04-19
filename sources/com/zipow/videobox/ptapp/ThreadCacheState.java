package com.zipow.videobox.ptapp;

public interface ThreadCacheState {
    public static final int THREAD_CACHE_STATE_DIRTY_PAGE = 2;
    public static final int THREAD_CACHE_STATE_LOADING_DB = 4;
    public static final int THREAD_CACHE_STATE_NOT_SET = 0;
    public static final int THREAD_CACHE_STATE_READY = 1;
    public static final int THREAD_CACHE_STATE_SYNCING = 8;
    public static final int THREAD_CACHE_STATE_SYNC_ERROR = 16;
}
