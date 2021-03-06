package com.bumptech.glide.load.engine.bitmap_recycle;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import androidx.annotation.Nullable;

interface LruPoolStrategy {
    @Nullable
    Bitmap get(int i, int i2, Config config);

    int getSize(Bitmap bitmap);

    String logBitmap(int i, int i2, Config config);

    String logBitmap(Bitmap bitmap);

    void put(Bitmap bitmap);

    @Nullable
    Bitmap removeLast();
}
