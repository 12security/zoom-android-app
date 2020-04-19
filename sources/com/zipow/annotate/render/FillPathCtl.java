package com.zipow.annotate.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.Path;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class FillPathCtl extends ISAnnotateDraw {
    @NonNull
    private Paint mPaint = new Paint();

    public FillPathCtl(float f, int i, int i2) {
        int i3 = 255;
        if (i2 <= 255) {
            i3 = i2;
        }
        this.mPaint.setColor(i);
        this.mPaint.setAlpha(i3);
        this.mPaint.setAntiAlias(true);
        this.mPaint.setDither(true);
        this.mPaint.setStyle(Style.FILL);
        this.mPaint.setStrokeJoin(Join.MITER);
        this.mPaint.setStrokeCap(Cap.ROUND);
        this.mPaint.setStrokeWidth(4.0f);
    }

    public void draw(@Nullable Canvas canvas, @NonNull Path path) {
        if (canvas != null) {
            canvas.drawPath(path, this.mPaint);
        }
    }
}
