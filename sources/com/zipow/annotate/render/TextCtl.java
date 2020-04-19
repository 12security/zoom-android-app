package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;

public class TextCtl extends ISAnnotateDraw {
    @NonNull
    private Paint mPaint = new Paint();
    private String text = "";
    private float textSize = 0.0f;

    /* renamed from: x */
    private float f308x = 0.0f;

    /* renamed from: y */
    private float f309y = 0.0f;

    public TextCtl(float f, int i, int i2) {
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setColor(i);
        this.mPaint.setAlpha(i2);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeJoin(Join.ROUND);
        this.mPaint.setStrokeCap(Cap.ROUND);
    }

    public void setText(String str) {
        this.text = str;
    }

    public void draw(@Nullable Canvas canvas) {
        FontMetrics fontMetrics = this.mPaint.getFontMetrics();
        if (canvas != null) {
            String str = this.text;
            if (str != null) {
                String[] split = str.split(FontStyleHelper.SPLITOR);
                int i = 0;
                while (i < split.length) {
                    int i2 = i + 1;
                    canvas.drawText(split[i], this.f308x, Math.abs(fontMetrics.top * ((float) i2)) + this.f309y + 4.0f, this.mPaint);
                    i = i2;
                }
            }
        }
    }

    public void setTextData(String str, int i, int i2, boolean z, boolean z2, int i3) {
        float f = (float) i3;
        this.textSize = f;
        this.mPaint.setFakeBoldText(z);
        this.mPaint.setTextSkewX(z2 ? -0.25f : 0.0f);
        this.mPaint.setTextSize(f);
        this.f308x = (float) i;
        this.f309y = (float) i2;
        this.text = str;
    }
}
