package com.zipow.videobox.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TextDrawable extends Drawable {
    private int mAlpha = -1;
    private ColorFilter mColorFilter;
    private StaticLayout mLayout;
    private int mPaddingBottom = 0;
    private int mPaddingLeft = 0;
    private int mPaddingRight = 0;
    private int mPaddingTop = 0;
    @Nullable
    private String mText;
    private int mTextColor;
    private TextPaint mTextPaint;
    private float mTextSize;
    private Typeface mTypeface;

    public int getOpacity() {
        return 0;
    }

    public TextDrawable(Context context, @Nullable String str, Typeface typeface, float f, int i) {
        if (str == null) {
            str = "";
        }
        this.mText = str;
        this.mTypeface = typeface;
        this.mTextSize = f;
        this.mTextColor = i;
        this.mTextPaint = new TextPaint();
        Typeface typeface2 = this.mTypeface;
        if (typeface2 != null) {
            this.mTextPaint.setTypeface(typeface2);
        }
        this.mTextPaint.setTextSize(this.mTextSize);
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setAlpha(this.mAlpha);
        this.mTextPaint.setAntiAlias(true);
        StaticLayout staticLayout = new StaticLayout(this.mText, this.mTextPaint, (int) (StaticLayout.getDesiredWidth(this.mText, this.mTextPaint) + 0.5f), Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        this.mLayout = staticLayout;
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingTop = i2;
        this.mPaddingRight = i3;
        this.mPaddingBottom = i4;
    }

    public int getIntrinsicWidth() {
        return this.mLayout.getWidth() + this.mPaddingLeft + this.mPaddingRight;
    }

    public int getIntrinsicHeight() {
        return this.mLayout.getHeight() + this.mPaddingTop + this.mPaddingBottom;
    }

    public void draw(@NonNull Canvas canvas) {
        ColorFilter colorFilter = this.mColorFilter;
        if (colorFilter != null) {
            this.mTextPaint.setColorFilter(colorFilter);
        }
        this.mTextPaint.setColor(this.mTextColor);
        this.mTextPaint.setAlpha(this.mAlpha);
        canvas.save();
        canvas.translate((float) this.mPaddingLeft, (float) this.mPaddingTop);
        this.mLayout.draw(canvas);
        canvas.restore();
    }

    public void setAlpha(int i) {
        this.mAlpha = i;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
    }
}
