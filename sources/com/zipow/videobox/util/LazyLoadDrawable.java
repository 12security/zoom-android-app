package com.zipow.videobox.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import p021us.zoom.androidlib.util.OsUtil;

public class LazyLoadDrawable extends Drawable {
    private final String TAG;
    private int mAlpha;
    @Nullable
    private Bitmap mBitmap;
    private ColorFilter mColorFilter;
    @Nullable
    private String mImagePath;
    private int mIntrinsicHeight;
    private int mIntrinsicWidth;
    private int mMaxArea;
    private boolean mbValid;
    private int safeMaxArea;

    public int getOpacity() {
        return 0;
    }

    public LazyLoadDrawable(String str) {
        this(str, 0);
    }

    public LazyLoadDrawable(@Nullable String str, int i) {
        this.TAG = LazyLoadDrawable.class.getSimpleName();
        this.mAlpha = -1;
        boolean z = false;
        this.mbValid = false;
        this.safeMaxArea = MediaHttpUploader.DEFAULT_CHUNK_SIZE;
        if (str != null) {
            this.mImagePath = str;
            Options options = new Options();
            options.inJustDecodeBounds = true;
            try {
                BitmapFactory.decodeFile(this.mImagePath, options);
                this.mIntrinsicWidth = options.outWidth;
                this.mIntrinsicHeight = options.outHeight;
                if (this.mIntrinsicWidth > 0 && this.mIntrinsicHeight > 0) {
                    z = true;
                }
                this.mbValid = z;
                this.mMaxArea = i;
            } catch (Exception unused) {
            }
        }
    }

    public void setMaxArea(int i) {
        this.mMaxArea = i;
    }

    public boolean isValidDrawable() {
        return this.mbValid;
    }

    public int getIntrinsicWidth() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            return bitmap.getWidth();
        }
        return this.mIntrinsicWidth;
    }

    public int getIntrinsicHeight() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            return bitmap.getHeight();
        }
        return this.mIntrinsicHeight;
    }

    public void draw(@NonNull Canvas canvas) {
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            int i = this.mMaxArea;
            if (i <= 0) {
                this.mBitmap = ZMBitmapFactory.decodeFile(this.mImagePath);
            } else {
                this.mBitmap = ZMBitmapFactory.decodeFile(this.mImagePath, i);
            }
        }
        if (this.mBitmap != null) {
            Rect bounds = getBounds();
            Paint paint = new Paint();
            int i2 = this.mAlpha;
            if (i2 >= 0 && i2 <= 255) {
                paint.setAlpha(i2);
            }
            ColorFilter colorFilter = this.mColorFilter;
            if (colorFilter != null) {
                paint.setColorFilter(colorFilter);
            }
            while (true) {
                try {
                    canvas.drawBitmap(this.mBitmap, new Rect(0, 0, this.mBitmap.getWidth(), this.mBitmap.getHeight()), bounds, paint);
                    break;
                } catch (Exception unused) {
                    if (this.safeMaxArea <= 1 || !OsUtil.isAtLeastKLP()) {
                        break;
                    }
                    Bitmap bitmap2 = this.mBitmap;
                    if (bitmap2 != null && bitmap2.getAllocationByteCount() < this.safeMaxArea) {
                        this.safeMaxArea = this.mBitmap.getAllocationByteCount() / 2;
                    }
                    this.mBitmap = ZMBitmapFactory.decodeFile(this.mImagePath, this.safeMaxArea);
                    this.safeMaxArea /= 2;
                }
            }
        }
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }
}
