package com.zipow.videobox.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.util.UIUtil;

public class RoundedBackgroundSpan extends ReplacementSpan {
    private int CORNER_RADIUS = 10;
    private int backgroundColor = 0;
    private int padding;
    private int textColor = 0;

    public RoundedBackgroundSpan(int i, int i2) {
        this.backgroundColor = i;
        this.textColor = i2;
        this.CORNER_RADIUS = UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 8.0f);
        this.padding = UIUtil.dip2px(VideoBoxApplication.getGlobalContext(), 5.0f);
    }

    public int getSize(@NonNull Paint paint, CharSequence charSequence, int i, int i2, FontMetricsInt fontMetricsInt) {
        return Math.round(paint.measureText(charSequence, i, i2));
    }

    public void draw(@NonNull Canvas canvas, @NonNull CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, @NonNull Paint paint) {
        float f2 = f;
        int i6 = i4;
        Paint paint2 = paint;
        CharSequence charSequence2 = charSequence;
        int i7 = i;
        int i8 = i2;
        RectF rectF = new RectF(f2, (float) i3, measureText(paint2, charSequence, i, i2) + f2, (float) (this.padding + i6));
        paint2.setColor(this.backgroundColor);
        int i9 = this.CORNER_RADIUS;
        Canvas canvas2 = canvas;
        canvas.drawRoundRect(rectF, (float) i9, (float) i9, paint2);
        paint2.setColor(this.textColor);
        canvas.drawText(charSequence, i, i2, f2, (float) i6, paint2);
    }

    private float measureText(Paint paint, CharSequence charSequence, int i, int i2) {
        return paint.measureText(charSequence, i, i2);
    }
}
