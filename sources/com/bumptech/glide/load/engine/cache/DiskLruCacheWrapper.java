package com.bumptech.glide.load.engine.cache;

import android.util.Log;
import com.bumptech.glide.disklrucache.DiskLruCache;
import com.bumptech.glide.disklrucache.DiskLruCache.Editor;
import com.bumptech.glide.disklrucache.DiskLruCache.Value;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.cache.DiskCache.Writer;
import java.io.File;
import java.io.IOException;

public class DiskLruCacheWrapper implements DiskCache {
    private static final int APP_VERSION = 1;
    private static final String TAG = "DiskLruCacheWrapper";
    private static final int VALUE_COUNT = 1;
    private static DiskLruCacheWrapper wrapper;
    private final File directory;
    private DiskLruCache diskLruCache;
    private final long maxSize;
    private final SafeKeyGenerator safeKeyGenerator;
    private final DiskCacheWriteLocker writeLocker = new DiskCacheWriteLocker();

    @Deprecated
    public static synchronized DiskCache get(File file, long j) {
        DiskLruCacheWrapper diskLruCacheWrapper;
        synchronized (DiskLruCacheWrapper.class) {
            if (wrapper == null) {
                wrapper = new DiskLruCacheWrapper(file, j);
            }
            diskLruCacheWrapper = wrapper;
        }
        return diskLruCacheWrapper;
    }

    public static DiskCache create(File file, long j) {
        return new DiskLruCacheWrapper(file, j);
    }

    @Deprecated
    protected DiskLruCacheWrapper(File file, long j) {
        this.directory = file;
        this.maxSize = j;
        this.safeKeyGenerator = new SafeKeyGenerator();
    }

    private synchronized DiskLruCache getDiskCache() throws IOException {
        if (this.diskLruCache == null) {
            this.diskLruCache = DiskLruCache.open(this.directory, 1, 1, this.maxSize);
        }
        return this.diskLruCache;
    }

    public File get(Key key) {
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        if (Log.isLoggable(TAG, 2)) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Get: Obtained: ");
            sb.append(safeKey);
            sb.append(" for for Key: ");
            sb.append(key);
            Log.v(str, sb.toString());
        }
        try {
            Value value = getDiskCache().get(safeKey);
            if (value != null) {
                return value.getFile(0);
            }
            return null;
        } catch (IOException e) {
            if (!Log.isLoggable(TAG, 5)) {
                return null;
            }
            Log.w(TAG, "Unable to get from disk cache", e);
            return null;
        }
    }

    public void put(Key key, Writer writer) {
        Editor edit;
        String safeKey = this.safeKeyGenerator.getSafeKey(key);
        this.writeLocker.acquire(safeKey);
        try {
            if (Log.isLoggable(TAG, 2)) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Put: Obtained: ");
                sb.append(safeKey);
                sb.append(" for for Key: ");
                sb.append(key);
                Log.v(str, sb.toString());
            }
            try {
                DiskLruCache diskCache = getDiskCache();
                if (diskCache.get(safeKey) == null) {
                    edit = diskCache.edit(safeKey);
                    if (edit != null) {
                        if (writer.write(edit.getFile(0))) {
                            edit.commit();
                        }
                        edit.abortUnlessCommitted();
                        this.writeLocker.release(safeKey);
                        return;
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Had two simultaneous puts for: ");
                    sb2.append(safeKey);
                    throw new IllegalStateException(sb2.toString());
                }
            } catch (IOException e) {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Unable to put to disk cache", e);
                }
            } catch (Throwable th) {
                edit.abortUnlessCommitted();
                throw th;
            }
        } finally {
            this.writeLocker.release(safeKey);
        }
    }

    public void delete(Key key) {
        try {
            getDiskCache().remove(this.safeKeyGenerator.getSafeKey(key));
        } catch (IOException e) {
            if (Log.isLoggable(TAG, 5)) {
                Log.w(TAG, "Unable to delete from disk cache", e);
            }
        }
    }

    public synchronized void clear() {
        try {
            getDiskCache().delete();
        } catch (IOException e) {
            try {
                if (Log.isLoggable(TAG, 5)) {
                    Log.w(TAG, "Unable to clear disk cache or disk cache cleared externally", e);
                }
            } catch (Throwable th) {
                resetDiskCache();
                throw th;
            }
        }
        resetDiskCache();
    }

    private synchronized void resetDiskCache() {
        this.diskLruCache = null;
    }
}
