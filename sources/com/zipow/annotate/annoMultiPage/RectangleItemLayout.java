package com.zipow.annotate.annoMultiPage;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.RelativeLayout;
import com.google.common.primitives.Ints;

public class RectangleItemLayout extends RelativeLayout {
    public RectangleItemLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public RectangleItemLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public RectangleItemLayout(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        setMeasuredDimension(getDefaultSize(0, i), getDefaultSize(0, i2));
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), Ints.MAX_POWER_OF_TWO);
        super.onMeasure(makeMeasureSpec, (makeMeasureSpec * 3) / 4);
    }
}
