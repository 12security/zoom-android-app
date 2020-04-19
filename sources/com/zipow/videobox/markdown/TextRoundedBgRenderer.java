package com.zipow.videobox.markdown;

import android.graphics.Canvas;
import android.text.Layout;
import androidx.annotation.NonNull;

public abstract class TextRoundedBgRenderer {
    protected int horizontalPadding;
    protected int verticalPadding;

    /* access modifiers changed from: 0000 */
    public abstract void draw(Canvas canvas, Layout layout, int i, int i2, int i3, int i4);

    public TextRoundedBgRenderer(int i, int i2) {
        this.horizontalPadding = i;
        this.verticalPadding = i2;
    }

    /* access modifiers changed from: protected */
    public int getLineTop(@NonNull Layout layout, int i) {
        return layout.getLineTop(i) - this.verticalPadding;
    }

    /* access modifiers changed from: protected */
    public int getLineBottom(@NonNull Layout layout, int i) {
        return layout.getLineBottom(i) + this.verticalPadding;
    }
}
