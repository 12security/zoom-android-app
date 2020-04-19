package com.zipow.videobox.util;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.text.style.ReplacementSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextSpan extends ReplacementSpan {
    private String lable;

    public TextSpan(@Nullable String str) {
        if (str == null) {
            str = "";
        }
        this.lable = str;
    }

    public int getSize(@NonNull Paint paint, CharSequence charSequence, int i, int i2, @Nullable FontMetricsInt fontMetricsInt) {
        if (fontMetricsInt != null) {
            paint.getFontMetricsInt(fontMetricsInt);
        }
        String str = this.lable;
        return (int) paint.measureText(str, 0, str.length());
    }

    public void draw(@NonNull Canvas canvas, CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, @NonNull Paint paint) {
        String str = this.lable;
        Canvas canvas2 = canvas;
        canvas2.drawText(str, 0, str.length(), f, (float) i4, paint);
    }
}
