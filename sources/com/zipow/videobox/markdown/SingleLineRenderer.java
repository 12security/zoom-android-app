package com.zipow.videobox.markdown;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import androidx.annotation.NonNull;

public class SingleLineRenderer extends TextRoundedBgRenderer {
    @NonNull
    private final Drawable mDrawable;

    public SingleLineRenderer(int i, int i2, @NonNull Drawable drawable) {
        super(i, i2);
        this.mDrawable = drawable;
    }

    /* access modifiers changed from: 0000 */
    public void draw(@NonNull Canvas canvas, @NonNull Layout layout, int i, int i2, int i3, int i4) {
        int lineTop = getLineTop(layout, i);
        int lineBottom = getLineBottom(layout, i);
        this.mDrawable.setBounds(Math.min(i3, i4), lineTop, Math.max(i3, i4), lineBottom);
        this.mDrawable.draw(canvas);
    }
}
