package com.zipow.videobox.share;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.annotate.AnnoDataMgr;
import p021us.zoom.androidlib.util.UIUtil;

public class ColorTable extends View {
    public static final int COLOR_ALPHA = 255;
    private static final int DFT_COLORS_CIRCLE_SIZE = 26;
    private static final int DFT_COLORS_CIRCLE_SPACE = 5;
    private int circleSize;
    private IColorChangedListener listner;
    private int[] mColors;
    private Context mContext;
    private Paint mPaint;
    private int space;

    public ColorTable(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public ColorTable(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mContext = context;
        init();
    }

    private void init() {
        this.mColors = AnnoDataMgr.getInstance().getColorList();
        this.mPaint = new Paint(1);
        this.circleSize = UIUtil.dip2px(this.mContext, 26.0f);
        this.space = UIUtil.dip2px(this.mContext, 5.0f);
    }

    /* access modifiers changed from: protected */
    public void onDraw(@Nullable Canvas canvas) {
        if (this.mColors != null) {
            this.mPaint.setStyle(Style.FILL);
            if (getWidth() != 0) {
                int length = this.circleSize * this.mColors.length;
                if (length <= getWidth()) {
                    this.space = (getWidth() - length) / (this.mColors.length + 1);
                } else {
                    if (this.space * (this.mColors.length + 1) > getWidth()) {
                        this.space = 0;
                    }
                    int width = getWidth();
                    int i = this.space;
                    int[] iArr = this.mColors;
                    this.circleSize = (width - (i * (iArr.length + 1))) / iArr.length;
                }
                int i2 = this.circleSize / 2;
                StringBuilder sb = new StringBuilder();
                sb.append("space is ");
                sb.append(this.space);
                Log.e("View", sb.toString());
                int i3 = this.space + i2;
                int height = getHeight();
                for (int color : this.mColors) {
                    this.mPaint.setStyle(Style.FILL);
                    this.mPaint.setColor(color);
                    int i4 = height / 2;
                    canvas.drawCircle((float) i3, (float) i4, (float) i2, this.mPaint);
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("draw x is ");
                    sb2.append(i3);
                    sb2.append(" draw y is ");
                    sb2.append(i4);
                    Log.e("View", sb2.toString());
                    i3 += this.circleSize + this.space;
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
    }

    public boolean onTouchEvent(@NonNull MotionEvent motionEvent) {
        int i = 0;
        if (this.mColors == null) {
            return false;
        }
        if (motionEvent.getAction() != 1) {
            return true;
        }
        int x = (int) ((motionEvent.getX() * ((float) this.mColors.length)) / ((float) getWidth()));
        int[] iArr = this.mColors;
        if (x > iArr.length - 1) {
            i = iArr.length - 1;
        } else if (x >= 0) {
            i = x;
        }
        this.listner.onColorPicked(this.mColors[i]);
        return true;
    }

    public void setOnColorChangedListener(IColorChangedListener iColorChangedListener) {
        this.listner = iColorChangedListener;
    }
}
