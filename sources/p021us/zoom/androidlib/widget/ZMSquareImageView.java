package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/* renamed from: us.zoom.androidlib.widget.ZMSquareImageView */
public class ZMSquareImageView extends ImageView {
    public ZMSquareImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ZMSquareImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMSquareImageView(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (measuredWidth > measuredHeight) {
            measuredWidth = measuredHeight;
        }
        setMeasuredDimension(measuredWidth, measuredWidth);
    }
}
