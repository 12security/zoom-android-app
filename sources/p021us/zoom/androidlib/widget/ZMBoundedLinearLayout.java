package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMBoundedLinearLayout */
public class ZMBoundedLinearLayout extends LinearLayout {
    private int mBoundedHeight;
    private int mBoundedWidth;

    @SuppressLint({"NewApi"})
    public ZMBoundedLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init(context, attributeSet);
    }

    @SuppressLint({"NewApi"})
    public ZMBoundedLinearLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public ZMBoundedLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ZMBoundedLinearLayout(Context context) {
        super(context);
        init(context, null);
    }

    private void init(Context context, AttributeSet attributeSet) {
        this.mBoundedHeight = 0;
        this.mBoundedWidth = 0;
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMBoundedLinearLayout);
            this.mBoundedWidth = obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMBoundedLinearLayout_zm_bounded_width, 0);
            this.mBoundedHeight = obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMBoundedLinearLayout_zm_bounded_height, 0);
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int i3 = this.mBoundedWidth;
        if (i3 > 0 && i3 < size) {
            i = MeasureSpec.makeMeasureSpec(this.mBoundedWidth, MeasureSpec.getMode(i));
        }
        int size2 = MeasureSpec.getSize(i2);
        int i4 = this.mBoundedHeight;
        if (i4 > 0 && i4 < size2) {
            i2 = MeasureSpec.makeMeasureSpec(this.mBoundedHeight, MeasureSpec.getMode(i2));
        }
        super.onMeasure(i, i2);
    }
}
