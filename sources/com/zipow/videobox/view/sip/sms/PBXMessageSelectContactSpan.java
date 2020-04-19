package com.zipow.videobox.view.sip.sms;

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

public class PBXMessageSelectContactSpan extends ReplacementSpan {
    private int mBackgroundColor;
    private int mInnerPaddingBottom = 0;
    private int mInnerPaddingLeft = 0;
    private int mInnerPaddingRight = 0;
    private int mInnerPaddingTop = 0;
    private int mInterval = 0;
    private PBXMessageContact mItem;
    private int mRadius = 0;
    private String mText;
    private int mTextColor;

    public PBXMessageSelectContactSpan(@NonNull Context context, @NonNull PBXMessageContact pBXMessageContact, boolean z) {
        this.mItem = pBXMessageContact;
        this.mTextColor = context.getResources().getColor(z ? C4558R.color.zm_pbx_contact_span_text_color : C4558R.color.zm_pbx_contact_span_text_color_invalid);
        this.mBackgroundColor = context.getResources().getColor(z ? C4558R.color.zm_pbx_contact_span_bg_color : C4558R.color.zm_pbx_contact_span_bg_color_invalid);
        this.mRadius = UIUtil.dip2px(context, 8.0f);
        this.mInterval = UIUtil.dip2px(context, 2.0f);
        this.mInnerPaddingLeft = UIUtil.dip2px(context, 12.0f);
        this.mInnerPaddingRight = this.mInnerPaddingLeft;
        this.mInnerPaddingTop = UIUtil.dip2px(context, 6.0f);
        this.mInnerPaddingBottom = this.mInnerPaddingTop;
    }

    public String getText() {
        return this.mText;
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
        return ((int) paint.measureText(subSequence, 0, subSequence.length())) + (this.mInterval * 2) + this.mInnerPaddingLeft + this.mInnerPaddingRight;
    }

    public void draw(@NonNull Canvas canvas, @Nullable CharSequence charSequence, int i, int i2, float f, int i3, int i4, int i5, @NonNull Paint paint) {
        int i6 = i;
        float f2 = f;
        if (charSequence != null && charSequence.length() > 0) {
            int length = charSequence.length();
            if (i6 < length) {
                int i7 = i2;
                int i8 = i7 > length ? length : i7;
                int i9 = this.mRadius;
                RoundRectShape roundRectShape = new RoundRectShape(new float[]{(float) i9, (float) i9, (float) i9, (float) i9, (float) i9, (float) i9, (float) i9, (float) i9}, null, null);
                CharSequence subSequence = charSequence.subSequence(i6, i8);
                FontMetricsInt fontMetricsInt = paint.getFontMetricsInt();
                int i10 = ((int) f2) + this.mInterval;
                int size = (int) ((((float) getSize(paint, charSequence, i, i8, fontMetricsInt)) + f2) - ((float) this.mInterval));
                int i11 = i3 - this.mInnerPaddingTop;
                int i12 = i3 + (fontMetricsInt.bottom - fontMetricsInt.top) + this.mInnerPaddingBottom;
                int i13 = i3 - fontMetricsInt.top;
                ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
                shapeDrawable.getPaint().setColor(this.mBackgroundColor);
                shapeDrawable.setBounds(i10, i11, size, i12);
                Canvas canvas2 = canvas;
                shapeDrawable.draw(canvas);
                paint.setColor(this.mTextColor);
                canvas.drawText(subSequence, 0, subSequence.length(), ((float) this.mInnerPaddingLeft) + f2 + ((float) this.mInterval), (float) i13, paint);
            }
        }
    }

    @NonNull
    public PBXMessageContact getItem() {
        return this.mItem;
    }

    public void setItem(@NonNull PBXMessageContact pBXMessageContact) {
        this.mItem = pBXMessageContact;
    }
}
