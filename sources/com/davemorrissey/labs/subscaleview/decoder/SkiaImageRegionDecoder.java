package com.davemorrissey.labs.subscaleview.decoder;

import android.content.Context;
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
import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SkiaImageRegionDecoder implements ImageRegionDecoder {
    private static final String ASSET_PREFIX = "file:///android_asset/";
    private static final String FILE_PREFIX = "file://";
    private static final String RESOURCE_PREFIX = "android.resource://";
    private final Config bitmapConfig;
    private BitmapRegionDecoder decoder;
    private final ReadWriteLock decoderLock;

    @Keep
    public SkiaImageRegionDecoder() {
        this(null);
    }

    public SkiaImageRegionDecoder(@Nullable Config config) {
        this.decoderLock = new ReentrantReadWriteLock(true);
        Config preferredBitmapConfig = SubsamplingScaleImageView.getPreferredBitmapConfig();
        if (config != null) {
            this.bitmapConfig = config;
        } else if (preferredBitmapConfig != null) {
            this.bitmapConfig = preferredBitmapConfig;
        } else {
            this.bitmapConfig = Config.RGB_565;
        }
    }

    @NonNull
    public Point init(Context context, @NonNull Uri uri) throws Exception {
        Resources resources;
        int i;
        String uri2 = uri.toString();
        if (uri2.startsWith(RESOURCE_PREFIX)) {
            String authority = uri.getAuthority();
            if (context.getPackageName().equals(authority)) {
                resources = context.getResources();
            } else {
                resources = context.getPackageManager().getResourcesForApplication(authority);
            }
            List pathSegments = uri.getPathSegments();
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
            this.decoder = BitmapRegionDecoder.newInstance(context.getResources().openRawResource(i), false);
        } else if (uri2.startsWith(ASSET_PREFIX)) {
            this.decoder = BitmapRegionDecoder.newInstance(context.getAssets().open(uri2.substring(22), 1), false);
        } else if (uri2.startsWith(FILE_PREFIX)) {
            this.decoder = BitmapRegionDecoder.newInstance(uri2.substring(7), false);
        } else {
            InputStream inputStream = null;
            try {
                inputStream = context.getContentResolver().openInputStream(uri);
                this.decoder = BitmapRegionDecoder.newInstance(inputStream, false);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception unused2) {
                    }
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception unused3) {
                    }
                }
            }
        }
        return new Point(this.decoder.getWidth(), this.decoder.getHeight());
    }

    @NonNull
    public Bitmap decodeRegion(@NonNull Rect rect, int i) {
        getDecodeLock().lock();
        try {
            if (this.decoder == null || this.decoder.isRecycled()) {
                throw new IllegalStateException("Cannot decode region after decoder has been recycled");
            }
            Options options = new Options();
            options.inSampleSize = i;
            options.inPreferredConfig = this.bitmapConfig;
            Bitmap decodeRegion = this.decoder.decodeRegion(rect, options);
            if (decodeRegion != null) {
                return decodeRegion;
            }
            throw new RuntimeException("Skia image decoder returned null bitmap - image format may not be supported");
        } finally {
            getDecodeLock().unlock();
        }
    }

    public synchronized boolean isReady() {
        return this.decoder != null && !this.decoder.isRecycled();
    }

    public synchronized void recycle() {
        this.decoderLock.writeLock().lock();
        try {
            this.decoder.recycle();
            this.decoder = null;
        } finally {
            this.decoderLock.writeLock().unlock();
        }
    }

    private Lock getDecodeLock() {
        if (VERSION.SDK_INT < 21) {
            return this.decoderLock.writeLock();
        }
        return this.decoderLock.readLock();
    }
}
