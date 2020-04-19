package com.zipow.videobox.markdown;

import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class URLDrawable extends BitmapDrawable {
    protected Drawable drawable;

    public URLDrawable(Drawable drawable2) {
        this.drawable = drawable2;
    }

    public void draw(Canvas canvas) {
        Drawable drawable2 = this.drawable;
        if (drawable2 != null) {
            drawable2.draw(canvas);
        }
    }
}
