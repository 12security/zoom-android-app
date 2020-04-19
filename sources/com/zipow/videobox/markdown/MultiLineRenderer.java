package com.zipow.videobox.markdown;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import androidx.annotation.NonNull;

public class MultiLineRenderer extends TextRoundedBgRenderer {
    private Drawable mDrawableLeft;
    private Drawable mDrawableMid;
    private Drawable mDrawableRight;

    public MultiLineRenderer(int i, int i2, @NonNull Drawable drawable, @NonNull Drawable drawable2, @NonNull Drawable drawable3) {
        super(i, i2);
        this.mDrawableLeft = drawable;
        this.mDrawableMid = drawable2;
        this.mDrawableRight = drawable3;
    }

    /* access modifiers changed from: 0000 */
    public void draw(@NonNull Canvas canvas, @NonNull Layout layout, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int paragraphDirection = layout.getParagraphDirection(i);
        if (paragraphDirection == -1) {
            i5 = ((int) layout.getLineLeft(i)) - this.horizontalPadding;
        } else {
            i5 = ((int) layout.getLineRight(i)) + this.horizontalPadding;
        }
        int lineBottom = getLineBottom(layout, i);
        int lineTop = getLineTop(layout, i);
        layout.getLineLeft(i);
        layout.getLineRight(i);
        layout.getParagraphLeft(i);
        layout.getParagraphRight(i);
        drawStart(canvas, i3, lineTop, i5, lineBottom);
        while (true) {
            i++;
            if (i >= i2) {
                break;
            }
            int lineTop2 = getLineTop(layout, i);
            int lineBottom2 = getLineBottom(layout, i);
            int lineLeft = (int) layout.getLineLeft(i);
            int lineRight = (int) layout.getLineRight(i);
            int paragraphLeft = layout.getParagraphLeft(i);
            layout.getParagraphRight(i);
            this.mDrawableMid.setBounds((lineLeft + paragraphLeft) - this.horizontalPadding, lineTop2, lineRight + this.horizontalPadding, lineBottom2);
            this.mDrawableMid.draw(canvas);
        }
        int lineLeft2 = (int) layout.getLineLeft(i2);
        int lineRight2 = (int) layout.getLineRight(i2);
        int paragraphLeft2 = layout.getParagraphLeft(i2);
        if (paragraphDirection == -1) {
            i6 = lineRight2 + this.horizontalPadding;
        } else {
            i6 = (lineLeft2 + paragraphLeft2) - this.horizontalPadding;
        }
        Canvas canvas2 = canvas;
        drawEnd(canvas2, i6, getLineTop(layout, i2), i4, getLineBottom(layout, i2));
    }

    private void drawStart(@NonNull Canvas canvas, int i, int i2, int i3, int i4) {
        if (i > i3) {
            this.mDrawableRight.setBounds(i3, i2, i, i4);
            this.mDrawableRight.draw(canvas);
            return;
        }
        this.mDrawableLeft.setBounds(i, i2, i3, i4);
        this.mDrawableLeft.draw(canvas);
    }

    private void drawEnd(@NonNull Canvas canvas, int i, int i2, int i3, int i4) {
        if (i > i3) {
            this.mDrawableLeft.setBounds(i3, i2, i, i4);
            this.mDrawableLeft.draw(canvas);
            return;
        }
        this.mDrawableRight.setBounds(i, i2, i3, i4);
        this.mDrawableRight.draw(canvas);
    }
}
