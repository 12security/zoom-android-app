package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

/* renamed from: us.zoom.androidlib.widget.ZMFillFixRatioImageView */
public class ZMFillFixRatioImageView extends ImageView {
    private void initView() {
    }

    public ZMFillFixRatioImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ZMFillFixRatioImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ZMFillFixRatioImageView(Context context) {
        super(context);
        initView();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            super.onMeasure(i, i2);
            return;
        }
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        if ((mode == Integer.MIN_VALUE && mode2 == Integer.MIN_VALUE) || (mode == 1073741824 && mode2 == 1073741824)) {
            setScaleType(ScaleType.FIT_CENTER);
            super.onMeasure(i, i2);
            return;
        }
        if (mode == 1073741824) {
            setScaleType(ScaleType.FIT_XY);
            setMeasuredDimension(size, (int) (((double) size) / ((((double) drawable.getIntrinsicWidth()) * 1.0d) / ((double) drawable.getIntrinsicHeight()))));
        } else if (mode2 == 1073741824) {
            setScaleType(ScaleType.FIT_XY);
            setMeasuredDimension((int) (((double) size2) * ((((double) drawable.getIntrinsicWidth()) * 1.0d) / ((double) drawable.getIntrinsicHeight()))), size2);
        } else {
            setScaleType(ScaleType.FIT_CENTER);
            super.onMeasure(i, i2);
        }
    }
}
