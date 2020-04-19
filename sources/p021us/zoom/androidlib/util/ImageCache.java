package p021us.zoom.androidlib.util;

import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LruCache;

/* renamed from: us.zoom.androidlib.util.ImageCache */
public class ImageCache implements ComponentCallbacks2 {
    private static ImageCache cache;
    private LruCache<Key, Bitmap> mCaches = new LruCache<Key, Bitmap>(((int) (Runtime.getRuntime().maxMemory() / 1024)) / 8) {
        /* access modifiers changed from: protected */
        public int sizeOf(@NonNull Key key, @NonNull Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }

        /* access modifiers changed from: protected */
        public void entryRemoved(boolean z, @NonNull Key key, @NonNull Bitmap bitmap, Bitmap bitmap2) {
            super.entryRemoved(z, key, bitmap, bitmap2);
        }
    };

    /* renamed from: us.zoom.androidlib.util.ImageCache$Key */
    public static class Key {
        private static Key flyWeightInstance = new Key(null, null, 0);
        String path;
        String subKey;
        long timestamp = 0;

        public Key(String str, String str2, long j) {
            this.path = str;
            this.subKey = str2;
            this.timestamp = j;
        }

        public boolean equals(Object obj) {
            boolean z = true;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Key)) {
                return false;
            }
            Key key = (Key) obj;
            if (!StringUtil.isSameString(this.path, key.path) || !StringUtil.isSameString(this.subKey, key.subKey) || this.timestamp != key.timestamp) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (int) this.timestamp;
        }

        public static Key getFlyweightInstance(String str, String str2, long j) {
            Key key = flyWeightInstance;
            key.path = str;
            key.subKey = str2;
            key.timestamp = j;
            return key;
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onLowMemory() {
    }

    public static synchronized ImageCache getInstance() {
        ImageCache imageCache;
        synchronized (ImageCache.class) {
            if (cache == null) {
                cache = new ImageCache();
            }
            imageCache = cache;
        }
        return imageCache;
    }

    private ImageCache() {
    }

    public void putBitmap(Key key, Bitmap bitmap) {
        if (key != null && bitmap != null) {
            this.mCaches.put(key, bitmap);
        }
    }

    @Nullable
    public Bitmap getBitmap(Key key) {
        if (key == null) {
            return null;
        }
        Bitmap bitmap = (Bitmap) this.mCaches.get(key);
        if (bitmap == null || !bitmap.isRecycled()) {
            return bitmap;
        }
        this.mCaches.remove(key);
        return null;
    }

    public void onTrimMemory(int i) {
        if (i >= 60) {
            this.mCaches.evictAll();
        } else if (i >= 40) {
            LruCache<Key, Bitmap> lruCache = this.mCaches;
            lruCache.trimToSize(lruCache.size() / 2);
        }
    }
}
