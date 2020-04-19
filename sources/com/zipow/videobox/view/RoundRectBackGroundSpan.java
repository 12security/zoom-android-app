package com.zipow.videobox.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.text.style.ReplacementSpan;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class RoundRectBackGroundSpan extends ReplacementSpan {
    private int mContentSize;
    private Context mContext;
    private int mInterval = 0;
    private int mPaddingBottom = 3;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingTop = 3;
    private int mRadius = 0;
    private String mText;

    public RoundRectBackGroundSpan(Context context) {
        this.mContext = context;
    }

    public String getText() {
        return this.mText;
    }

    public void setInterval(int i) {
        this.mInterval = i;
    }

    public int getInvterval() {
        return this.mInterval;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingRight = i2;
        this.mPaddingTop = i3;
        this.mPaddingBottom = i4;
    }

    public int getSize(@NonNull Paint paint, @Nullable CharSequence charSequence, int i, int i2, @Nullable FontMetricsInt fontMetricsInt) {
        if (charSequence == null || charSequence.length() <= 0) {
            return 0;
        }
        int length = charSequence.length();
        if (i >= length) {
            return 0;
        }
        if (i2 > length) {
            i2 = length;
        }
        CharSequence subSequence = charSequence.subSequence(i, i2);
        this.mText = subSequence.toString();
        if (fontMetricsInt == null) {
            fontMetricsInt = paint.getFontMetricsInt();
        }
        int dip2px = UIUtil.dip2px(this.mContext, 30.0f);
        if (fontMetricsInt != null) {
            dip2px = fontMetricsInt.bottom - fontMetricsInt.top;
        }
        this.mRadius = dip2px / 2;
        this.mContentSize = ((int) paint.measureText(subSequence, 0, subSequence.length())) + dip2px + (this.mInterval * 2) + this.mPaddingLeft + this.mPaddingRight;
        return this.mContentSize;
    }

    public void draw(@NonNull Canvas canvas, @Nullable CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, @NonNull Paint paint) {
        int i6;
        int i7;
        CharSequence charSequence2 = charSequence;
        int i8 = i;
        float f2 = f;
        int i9 = i3;
        if (charSequence2 != null && charSequence.length() > 0) {
            int length = charSequence.length();
            if (i8 < length) {
                int i10 = i2;
                int i11 = i10 > length ? length : i10;
                int i12 = this.mRadius;
                RoundRectShape roundRectShape = new RoundRectShape(new float[]{(float) i12, (float) i12, (float) i12, (float) i12, (float) i12, (float) i12, (float) i12, (float) i12}, null, null);
                CharSequence subSequence = charSequence2.subSequence(i8, i11);
                ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
                shapeDrawable.getPaint().setColor(this.mContext.getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB));
                int i13 = ((int) f2) + this.mInterval + this.mPaddingLeft;
                FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
                int size = (int) (((((float) getSize(paint, charSequence, i, i11, fontMetricsInt)) + f2) - ((float) this.mInterval)) - ((float) this.mPaddingRight));
                if (fontMetricsInt != null) {
                    int i14 = fontMetricsInt.bottom - fontMetricsInt.top;
                    int i15 = i4 + fontMetricsInt.top;
                    int i16 = i4 + fontMetricsInt.bottom;
                    int i17 = this.mPaddingTop;
                    if (i15 - i17 >= i9) {
                        i6 = i16 + this.mPaddingBottom;
                        i9 = i15 - i17;
                        i7 = i4;
                    } else {
                        i6 = i14 + i9 + i17 + this.mPaddingBottom;
                        i7 = (i9 + i17) - fontMetricsInt.top;
                    }
                } else {
                    i9++;
                    i6 = i5 - 1;
                    i7 = i4;
                }
                shapeDrawable.setBounds(i13, i9, size, i6);
                Canvas canvas2 = canvas;
                shapeDrawable.draw(canvas);
                paint.setColor(-1);
                canvas.drawText(subSequence, 0, subSequence.length(), ((float) this.mRadius) + f2 + ((float) this.mInterval), (float) i7, paint);
            }
        }
    }
}
