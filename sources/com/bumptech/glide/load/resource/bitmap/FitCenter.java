package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class FitCenter extends BitmapTransformation {

    /* renamed from: ID */
    private static final String f59ID = "com.bumptech.glide.load.resource.bitmap.FitCenter";
    private static final byte[] ID_BYTES = f59ID.getBytes(CHARSET);

    /* access modifiers changed from: protected */
    public Bitmap transform(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int i, int i2) {
        return TransformationUtils.fitCenter(bitmapPool, bitmap, i, i2);
    }

    public boolean equals(Object obj) {
        return obj instanceof FitCenter;
    }

    public int hashCode() {
        return f59ID.hashCode();
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
