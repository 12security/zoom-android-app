package com.davemorrissey.labs.subscaleview.decoder;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.regex.Pattern;

public class SkiaPooledImageRegionDecoder implements ImageRegionDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private static final String TAG = "SkiaPooledImageRegionDecoder";
    private static boolean debug = false;
    private final Config bitmapConfig;
    private Context context;
    private final ReadWriteLock decoderLock;
    /* access modifiers changed from: private */
    public DecoderPool decoderPool;
    /* access modifiers changed from: private */
    public long fileLength;
    private final Point imageDimensions;
    private final AtomicBoolean lazyInited;
    private Uri uri;

    private static class DecoderPool {
        private final Semaphore available;
        private final Map<BitmapRegionDecoder, Boolean> decoders;

        private DecoderPool() {
            this.available = new Semaphore(0, true);
            this.decoders = new ConcurrentHashMap();
        }

        /* access modifiers changed from: private */
        public synchronized boolean isEmpty() {
            return this.decoders.isEmpty();
        }

        /* access modifiers changed from: private */
        public synchronized int size() {
            return this.decoders.size();
        }

        /* access modifiers changed from: private */
        public BitmapRegionDecoder acquire() {
            this.available.acquireUninterruptibly();
            return getNextAvailable();
        }

        /* access modifiers changed from: private */
        public void release(BitmapRegionDecoder bitmapRegionDecoder) {
            if (markAsUnused(bitmapRegionDecoder)) {
                this.available.release();
            }
        }

        /* access modifiers changed from: private */
        public synchronized void add(BitmapRegionDecoder bitmapRegionDecoder) {
            this.decoders.put(bitmapRegionDecoder, Boolean.valueOf(false));
            this.available.release();
        }

        /* access modifiers changed from: private */
        public synchronized void recycle() {
            while (!this.decoders.isEmpty()) {
                BitmapRegionDecoder acquire = acquire();
                acquire.recycle();
                this.decoders.remove(acquire);
            }
        }

        private synchronized BitmapRegionDecoder getNextAvailable() {
            for (Entry entry : this.decoders.entrySet()) {
                if (!((Boolean) entry.getValue()).booleanValue()) {
                    entry.setValue(Boolean.valueOf(true));
                    return (BitmapRegionDecoder) entry.getKey();
                }
            }
            return null;
        }

        private synchronized boolean markAsUnused(BitmapRegionDecoder bitmapRegionDecoder) {
            for (Entry entry : this.decoders.entrySet()) {
                if (bitmapRegionDecoder == entry.getKey()) {
                    if (!((Boolean) entry.getValue()).booleanValue()) {
                        return false;
                    }
                    entry.setValue(Boolean.valueOf(false));
                    return true;
                }
            }
            return false;
        }
    }

    @Keep
    public SkiaPooledImageRegionDecoder() {
        this(null);
    }

    public SkiaPooledImageRegionDecoder(@Nullable Config config) {
        this.decoderPool = new DecoderPool();
        this.decoderLock = new ReentrantReadWriteLock(true);
        this.fileLength = Long.MAX_VALUE;
        this.imageDimensions = new Point(0, 0);
        this.lazyInited = new AtomicBoolean(false);
        Config preferredBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (config != null) {
            this.bitmapConfig = config;
        } else if (preferredBitmapConfig != null) {
            this.bitmapConfig = preferredBitmapConfig;
        } else {
            this.bitmapConfig = Config.RGB_565;
        }
    }

    @Keep
    public static void setDebug(boolean z) {
        debug = z;
    }

    @NonNull
    public Point init(Context context2, @NonNull Uri uri2) throws Exception {
        this.context = context2;
        this.uri = uri2;
        initialiseDecoder();
        return this.imageDimensions;
    }

    private void lazyInit() {
        if (this.lazyInited.compareAndSet(false, true) && this.fileLength < Long.MAX_VALUE) {
            debug("Starting lazy init of additional decoders");
            new Thread() {
                public void run() {
                    while (SkiaPooledImageRegionDecoder.this.decoderPool != null) {
                        SkiaPooledImageRegionDecoder skiaPooledImageRegionDecoder = SkiaPooledImageRegionDecoder.this;
                        if (skiaPooledImageRegionDecoder.allowAdditionalDecoder(skiaPooledImageRegionDecoder.decoderPool.size(), SkiaPooledImageRegionDecoder.this.fileLength)) {
                            try {
                                if (SkiaPooledImageRegionDecoder.this.decoderPool != null) {
                                    long currentTimeMillis = System.currentTimeMillis();
                                    SkiaPooledImageRegionDecoder.this.debug("Starting decoder");
                                    SkiaPooledImageRegionDecoder.this.initialiseDecoder();
                                    long currentTimeMillis2 = System.currentTimeMillis();
                                    SkiaPooledImageRegionDecoder skiaPooledImageRegionDecoder2 = SkiaPooledImageRegionDecoder.this;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("Started decoder, took ");
                                    sb.append(currentTimeMillis2 - currentTimeMillis);
                                    sb.append("ms");
                                    skiaPooledImageRegionDecoder2.debug(sb.toString());
                                }
                            } catch (Exception e) {
                                SkiaPooledImageRegionDecoder skiaPooledImageRegionDecoder3 = SkiaPooledImageRegionDecoder.this;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Failed to start decoder: ");
                                sb2.append(e.getMessage());
                                skiaPooledImageRegionDecoder3.debug(sb2.toString());
                            }
                        } else {
                            return;
                        }
                    }
                }
            }.start();
        }
    }

    /* access modifiers changed from: private */
    public void initialiseDecoder() throws Exception {
        BitmapRegionDecoder bitmapRegionDecoder;
        Resources resources;
        int i;
        String uri2 = this.uri.toString();
        long j = Long.MAX_VALUE;
        if (uri2.startsWith(RESOURCE_PREFIX)) {
            String authority = this.uri.getAuthority();
            if (this.context.getPackageName().equals(authority)) {
                resources = this.context.getResources();
            } else {
                resources = this.context.getPackageManager().getResourcesForApplication(authority);
            }
            List pathSegments = this.uri.getPathSegments();
            int size = pathSegments.size();
            if (size != 2 || !((String) pathSegments.get(0)).equals("drawable")) {
                if (size == 1 && TextUtils.isDigitsOnly((CharSequence) pathSegments.get(0))) {
                    try {
                        i = Integer.parseInt((String) pathSegments.get(0));
                    } catch (NumberFormatException unused) {
                    }
                }
                i = 0;
            } else {
                i = resources.getIdentifier((String) pathSegments.get(1), "drawable", authority);
            }
            try {
                j = this.context.getResources().openRawResourceFd(i).getLength();
            } catch (Exception unused2) {
            }
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(this.context.getResources().openRawResource(i), false);
        } else if (uri2.startsWith(ASSET_PREFIX)) {
            String substring = uri2.substring(22);
            try {
                j = this.context.getAssets().openFd(substring).getLength();
            } catch (Exception unused3) {
            }
            bitmapRegionDecoder = BitmapRegionDecoder.newInstance(this.context.getAssets().open(substring, 1), false);
        } else if (uri2.startsWith(FILE_PREFIX)) {
            BitmapRegionDecoder newInstance = BitmapRegionDecoder.newInstance(uri2.substring(7), false);
            try {
                File file = new File(uri2);
                if (file.exists()) {
                    j = file.length();
                }
            } catch (Exception unused4) {
            }
            bitmapRegionDecoder = newInstance;
        } else {
            AssetFileDescriptor assetFileDescriptor = null;
            try {
                ContentResolver contentResolver = this.context.getContentResolver();
                InputStream openInputStream = contentResolver.openInputStream(this.uri);
                BitmapRegionDecoder newInstance2 = BitmapRegionDecoder.newInstance(openInputStream, false);
                try {
                    assetFileDescriptor = contentResolver.openAssetFileDescriptor(this.uri, "r");
                    if (assetFileDescriptor != null) {
                        j = assetFileDescriptor.getLength();
                    }
                } catch (Exception unused5) {
                }
                if (openInputStream != null) {
                    try {
                        openInputStream.close();
                    } catch (Exception unused6) {
                    }
                }
                bitmapRegionDecoder = newInstance2;
            } finally {
                if (assetFileDescriptor != null) {
                    try {
                        assetFileDescriptor.close();
                    } catch (Exception unused7) {
                    }
                }
            }
        }
        this.fileLength = j;
        this.imageDimensions.set(bitmapRegionDecoder.getWidth(), bitmapRegionDecoder.getHeight());
        this.decoderLock.writeLock().lock();
        try {
            if (this.decoderPool != null) {
                this.decoderPool.add(bitmapRegionDecoder);
            }
        } finally {
            this.decoderLock.writeLock().unlock();
        }
    }

    @NonNull
    public Bitmap decodeRegion(@NonNull Rect rect, int i) {
        BitmapRegionDecoder access$700;
        StringBuilder sb = new StringBuilder();
        sb.append("Decode region ");
        sb.append(rect);
        sb.append(" on thread ");
        sb.append(Thread.currentThread().getName());
        debug(sb.toString());
        if (rect.width() < this.imageDimensions.x || rect.height() < this.imageDimensions.y) {
            lazyInit();
        }
        this.decoderLock.readLock().lock();
        try {
            if (this.decoderPool != null) {
                access$700 = this.decoderPool.acquire();
                if (access$700 != null) {
                    if (!access$700.isRecycled()) {
                        Options options = new Options();
                        options.inSampleSize = i;
                        options.inPreferredConfig = this.bitmapConfig;
                        Bitmap decodeRegion = access$700.decodeRegion(rect, options);
                        if (decodeRegion != null) {
                            if (access$700 != null) {
                                this.decoderPool.release(access$700);
                            }
                            this.decoderLock.readLock().unlock();
                            return decodeRegion;
                        }
                        throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
                    }
                }
                if (access$700 != null) {
                    this.decoderPool.release(access$700);
                }
            }
            throw new IllegalStateException("Cannot decode region after decoder has been recycled");
        } catch (Throwable th) {
            this.decoderLock.readLock().unlock();
            throw th;
        }
    }

    public synchronized boolean isReady() {
        return this.decoderPool != null && !this.decoderPool.isEmpty();
    }

    public synchronized void recycle() {
        this.decoderLock.writeLock().lock();
        try {
            if (this.decoderPool != null) {
                this.decoderPool.recycle();
                this.decoderPool = null;
                this.context = null;
                this.uri = null;
            }
        } finally {
            this.decoderLock.writeLock().unlock();
        }
    }

    /* access modifiers changed from: protected */
    public boolean allowAdditionalDecoder(int i, long j) {
        if (i >= 4) {
            debug("No additional decoders allowed, reached hard limit (4)");
            return false;
        }
        long j2 = ((long) i) * j;
        if (j2 > 20971520) {
            debug("No additional encoders allowed, reached hard memory limit (20Mb)");
            return false;
        } else if (i >= getNumberOfCores()) {
            StringBuilder sb = new StringBuilder();
            sb.append("No additional encoders allowed, limited by CPU cores (");
            sb.append(getNumberOfCores());
            sb.append(")");
            debug(sb.toString());
            return false;
        } else if (isLowMemory()) {
            debug("No additional encoders allowed, memory is low");
            return false;
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Additional decoder allowed, current count is ");
            sb2.append(i);
            sb2.append(", estimated native memory ");
            sb2.append(j2 / 1048576);
            sb2.append("Mb");
            debug(sb2.toString());
            return true;
        }
    }

    private int getNumberOfCores() {
        if (VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }
        return getNumCoresOldPhones();
    }

    private int getNumCoresOldPhones() {
        try {
            return new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File file) {
                    return Pattern.matches("cpu[0-9]+", file.getName());
                }
            }).length;
        } catch (Exception unused) {
            return 1;
        }
    }

    private boolean isLowMemory() {
        ActivityManager activityManager = (ActivityManager) this.context.getSystemService("activity");
        if (activityManager == null) {
            return true;
        }
        MemoryInfo memoryInfo = new MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.lowMemory;
    }

    /* access modifiers changed from: private */
    public void debug(String str) {
        if (debug) {
            Log.d(TAG, str);
        }
    }
}
