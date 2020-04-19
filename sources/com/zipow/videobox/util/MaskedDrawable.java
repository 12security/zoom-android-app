package com.zipow.videobox.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MaskedDrawable extends Drawable {
    private static final String TAG = "MaskedDrawable";
    private int mAlpha = -1;
    @NonNull
    private final Bitmap mBitmap;
    private ColorFilter mColorFilter;
    @NonNull
    private Drawable mMaskDrawable;
    private float mScale = 1.0f;

    public int getOpacity() {
        return 0;
    }

    public MaskedDrawable(@Nullable Drawable drawable, @Nullable Bitmap bitmap, float f) {
        if (drawable == null) {
            throw new NullPointerException("maskDrawable is null");
        } else if (bitmap != null) {
            this.mMaskDrawable = drawable;
            this.mBitmap = bitmap;
            this.mBitmap.setDensity(1);
            this.mScale = f;
        } else {
            throw new NullPointerException("bitmap is null");
        }
    }

    public int getIntrinsicWidth() {
        return (int) (((float) this.mBitmap.getWidth()) * this.mScale);
    }

    public int getIntrinsicHeight() {
        return (int) (((float) this.mBitmap.getHeight()) * this.mScale);
    }

    public void draw(@NonNull Canvas canvas) {
        Rect bounds = getBounds();
        this.mMaskDrawable.setBounds(bounds);
        try {
            Config config = this.mBitmap.getConfig();
            if (config == null) {
                config = Config.RGB_565;
            }
            Bitmap createBitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), config);
            if (createBitmap != null) {
                Canvas canvas2 = new Canvas(createBitmap);
                Paint paint = new Paint();
                int i = this.mAlpha;
                if (i >= 0 && i <= 255) {
                    paint.setAlpha(i);
                }
                ColorFilter colorFilter = this.mColorFilter;
                if (colorFilter != null) {
                    paint.setColorFilter(colorFilter);
                }
                this.mMaskDrawable.draw(canvas2);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
                Bitmap bitmap = this.mBitmap;
                canvas2.drawBitmap(bitmap, new Rect(0, 0, bitmap.getWidth(), this.mBitmap.getHeight()), new Rect(0, 0, bounds.width(), bounds.height()), paint);
                canvas.drawBitmap(createBitmap, 0.0f, 0.0f, new Paint());
                createBitmap.recycle();
            }
        } catch (OutOfMemoryError unused) {
        } catch (Exception unused2) {
            Bitmap bitmap2 = this.mBitmap;
            canvas.drawBitmap(bitmap2, new Rect(0, 0, bitmap2.getWidth(), this.mBitmap.getHeight()), new Rect(0, 0, bounds.width(), bounds.height()), new Paint());
        }
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }
}
