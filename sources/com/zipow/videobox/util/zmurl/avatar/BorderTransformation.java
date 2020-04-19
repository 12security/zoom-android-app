package com.zipow.videobox.util.zmurl.avatar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Build.VERSION;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import com.zipow.videobox.util.BitmapTransformation;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class BorderTransformation extends BitmapTransformation {

    /* renamed from: ID */
    private static final String f336ID = "com.bumptech.glide.load.resource.bitmap.BorderTransformation";
    private static final byte[] ID_BYTES = f336ID.getBytes(CHARSET);
    private static final String TAG = "BorderTransformation";
    private int borderColor;
    private int borderSize;
    private int radius;

    public BorderTransformation(int i, @ColorInt int i2, int i3) {
        this.borderSize = i;
        this.borderColor = i2;
        this.radius = i3;
    }

    /* access modifiers changed from: protected */
    public Bitmap transform(@NonNull Context context, @NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap, int i, int i2) {
        try {
            Config alphaSafeConfig = getAlphaSafeConfig(bitmap);
            Bitmap alphaSafeBitmap = getAlphaSafeBitmap(bitmapPool, bitmap);
            Bitmap bitmap2 = bitmapPool.get(alphaSafeBitmap.getWidth(), alphaSafeBitmap.getHeight(), alphaSafeConfig);
            bitmap2.setHasAlpha(true);
            BitmapShader bitmapShader = new BitmapShader(bitmap, TileMode.CLAMP, TileMode.CLAMP);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setShader(bitmapShader);
            RectF rectF = new RectF(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) bitmap2.getHeight());
            Canvas canvas = new Canvas(bitmap2);
            canvas.drawColor(0, Mode.CLEAR);
            canvas.drawRoundRect(rectF, (float) this.radius, (float) this.radius, paint);
            Paint paint2 = new Paint();
            paint2.setDither(true);
            paint2.setAntiAlias(true);
            paint2.setColor(this.borderColor);
            paint2.setStyle(Style.STROKE);
            paint2.setStrokeWidth((float) this.borderSize);
            float f = ((float) this.radius) - (((float) this.borderSize) / 2.0f);
            canvas.drawRoundRect(rectF, f, f, paint2);
            canvas.setBitmap(null);
            if (!alphaSafeBitmap.equals(bitmap)) {
                bitmapPool.put(alphaSafeBitmap);
            }
            return bitmap2;
        } catch (OutOfMemoryError unused) {
            return bitmap;
        } catch (Exception unused2) {
            return bitmap;
        }
    }

    public boolean equals(Object obj) {
        if (obj instanceof BorderTransformation) {
            BorderTransformation borderTransformation = (BorderTransformation) obj;
            if (borderTransformation.borderSize == this.borderSize && borderTransformation.borderColor == this.borderColor && borderTransformation.radius == this.radius) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.borderSize);
        sb.append(this.borderColor);
        sb.append(this.radius);
        return Util.hashCode(f336ID.hashCode(), Util.hashCode(sb.toString().hashCode()));
    }

    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
        StringBuilder sb = new StringBuilder();
        sb.append(this.borderSize);
        sb.append(this.borderColor);
        sb.append(this.radius);
        messageDigest.update(ByteBuffer.allocate(4).putInt(sb.toString().hashCode()).array());
    }

    private static Bitmap getAlphaSafeBitmap(@NonNull BitmapPool bitmapPool, @NonNull Bitmap bitmap) {
        Config alphaSafeConfig = getAlphaSafeConfig(bitmap);
        if (alphaSafeConfig.equals(bitmap.getConfig())) {
            return bitmap;
        }
        Bitmap bitmap2 = bitmapPool.get(bitmap.getWidth(), bitmap.getHeight(), alphaSafeConfig);
        new Canvas(bitmap2).drawBitmap(bitmap, 0.0f, 0.0f, null);
        return bitmap2;
    }

    @NonNull
    private static Config getAlphaSafeConfig(@NonNull Bitmap bitmap) {
        if (VERSION.SDK_INT < 26 || !Config.RGBA_F16.equals(bitmap.getConfig())) {
            return Config.ARGB_8888;
        }
        return Config.RGBA_F16;
    }
}
