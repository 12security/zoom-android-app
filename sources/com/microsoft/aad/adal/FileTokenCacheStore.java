package com.microsoft.aad.adal;

import android.content.Context;
import com.zipow.videobox.fragment.FileTransferFragment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

public class FileTokenCacheStore implements ITokenCacheStore {
    private static final String TAG = null;
    private static final long serialVersionUID = -8252291336171327870L;
    private final Object mCacheLock = new Object();
    private final File mFile;
    private final MemoryTokenCacheStore mInMemoryCache;

    public FileTokenCacheStore(Context context, String str) {
        if (context == null) {
            throw new IllegalArgumentException("context");
        } else if (!StringExtensions.isNullOrBlank(str)) {
            File dir = context.getDir(context.getPackageName(), 0);
            if (dir != null) {
                try {
                    this.mFile = new File(dir, str);
                    if (this.mFile.exists()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(TAG);
                        sb.append(":FileTokenCacheStore");
                        Logger.m236v(sb.toString(), "There is previous cache file to load cache. ");
                        FileInputStream fileInputStream = new FileInputStream(this.mFile);
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                        Object readObject = objectInputStream.readObject();
                        fileInputStream.close();
                        objectInputStream.close();
                        if (readObject instanceof MemoryTokenCacheStore) {
                            this.mInMemoryCache = (MemoryTokenCacheStore) readObject;
                            return;
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(TAG);
                        sb2.append(":FileTokenCacheStore");
                        Logger.m239w(sb2.toString(), "Existing cache format is wrong. ", "", ADALError.DEVICE_FILE_CACHE_FORMAT_IS_WRONG);
                        this.mInMemoryCache = new MemoryTokenCacheStore();
                        return;
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(TAG);
                    sb3.append(":FileTokenCacheStore");
                    Logger.m236v(sb3.toString(), "There is not any previous cache file to load cache. ");
                    this.mInMemoryCache = new MemoryTokenCacheStore();
                } catch (IOException | ClassNotFoundException e) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(TAG);
                    sb4.append(":FileTokenCacheStore");
                    Logger.m231e(sb4.toString(), "Exception during cache load. ", ExceptionExtensions.getExceptionMessage(e), ADALError.DEVICE_FILE_CACHE_IS_NOT_LOADED_FROM_FILE);
                    throw new IllegalStateException(e);
                }
            } else {
                throw new IllegalStateException("It could not access the Authorization cache directory");
            }
        } else {
            throw new IllegalArgumentException(FileTransferFragment.ARGS_FILE_NAME);
        }
    }

    public TokenCacheItem getItem(String str) {
        return this.mInMemoryCache.getItem(str);
    }

    public boolean contains(String str) {
        return this.mInMemoryCache.contains(str);
    }

    public void setItem(String str, TokenCacheItem tokenCacheItem) {
        this.mInMemoryCache.setItem(str, tokenCacheItem);
        writeToFile();
    }

    public void removeItem(String str) {
        this.mInMemoryCache.removeItem(str);
        writeToFile();
    }

    public void removeAll() {
        this.mInMemoryCache.removeAll();
        writeToFile();
    }

    private void writeToFile() {
        synchronized (this.mCacheLock) {
            if (!(this.mFile == null || this.mInMemoryCache == null)) {
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(this.mFile);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                    objectOutputStream.writeObject(this.mInMemoryCache);
                    objectOutputStream.flush();
                    objectOutputStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    Logger.m231e(TAG, "Exception during cache flush", ExceptionExtensions.getExceptionMessage(e), ADALError.DEVICE_FILE_CACHE_IS_NOT_WRITING_TO_FILE);
                }
            }
        }
    }

    public Iterator<TokenCacheItem> getAll() {
        return this.mInMemoryCache.getAll();
    }
}
