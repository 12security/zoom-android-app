package com.zipow.videobox;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BitmapCacheMgr {
    @NonNull
    private static BitmapCacheMgr mBitmapCacheMgr = new BitmapCacheMgr();
    private SparseArray<Bitmap> mBitmapCache = new SparseArray<>();

    public void init() {
    }

    @NonNull
    public static BitmapCacheMgr getInstance() {
        return mBitmapCacheMgr;
    }

    @Nullable
    public Bitmap getBitmap(int i) {
        Bitmap bitmap = (Bitmap) this.mBitmapCache.get(i);
        return bitmap == null ? createBitmap(i) : bitmap;
    }

    private Bitmap createBitmap(int i) {
        Drawable drawable = VideoBoxApplication.getNonNullInstance().getResources().getDrawable(i);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public void uninit() {
        for (int i = 0; i < this.mBitmapCache.size(); i++) {
            Bitmap bitmap = (Bitmap) this.mBitmapCache.get(i);
            if (bitmap != null) {
                bitmap.recycle();
            }
        }
    }
}
