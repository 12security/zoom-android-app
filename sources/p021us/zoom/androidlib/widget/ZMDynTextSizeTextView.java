package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMDynTextSizeTextView */
public class ZMDynTextSizeTextView extends TextView {
    private int mMaxReduce = 12;
    private float mMaxTextSize = 0.0f;
    private boolean mSetTextSizeCalledFromOnMeasure = false;
    private int mTextSizeUnit = 0;

    public ZMDynTextSizeTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context, attributeSet);
    }

    public ZMDynTextSizeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context, attributeSet);
    }

    public ZMDynTextSizeTextView(Context context) {
        super(context);
        initView(context, null);
    }

    private void initView(Context context, AttributeSet attributeSet) {
        this.mMaxTextSize = getTextSize();
        if (attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMDynTextSizeTextView);
            if (obtainStyledAttributes != null) {
                this.mMaxReduce = obtainStyledAttributes.getDimensionPixelSize(C4409R.styleable.ZMDynTextSizeTextView_zm_maxReduce, this.mMaxReduce);
                obtainStyledAttributes.recycle();
            }
        }
    }

    public void setTextSize(int i, float f) {
        super.setTextSize(i, f);
        if (!this.mSetTextSizeCalledFromOnMeasure) {
            this.mTextSizeUnit = i;
            this.mMaxTextSize = f;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(100000, Integer.MIN_VALUE);
        int makeMeasureSpec2 = MeasureSpec.makeMeasureSpec(10, Integer.MIN_VALUE);
        float f = this.mMaxTextSize;
        while (true) {
            this.mSetTextSizeCalledFromOnMeasure = true;
            float f2 = f - 1.0f;
            setTextSize(this.mTextSizeUnit, f);
            this.mSetTextSizeCalledFromOnMeasure = false;
            super.onMeasure(makeMeasureSpec, makeMeasureSpec2);
            if (getMeasuredWidth() <= size || this.mMaxTextSize - f2 >= ((float) this.mMaxReduce)) {
                super.onMeasure(i, i2);
            } else {
                f = f2;
            }
        }
        super.onMeasure(i, i2);
    }
}
