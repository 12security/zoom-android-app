package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.ListView;
import com.google.common.primitives.Ints;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMMenuListView */
public class ZMMenuListView extends ListView {
    private boolean mAutoComputeWidth = false;

    public ZMMenuListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    public ZMMenuListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public ZMMenuListView(Context context) {
        super(context);
        init(context, null, 0);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (!this.mAutoComputeWidth) {
            super.onMeasure(i, i2);
        } else {
            super.onMeasure(MeasureSpec.makeMeasureSpec(getMaxWidthOfChildren() + getPaddingLeft() + getPaddingRight(), Ints.MAX_POWER_OF_TWO), i2);
        }
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        if (context != null && attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMMenuListView);
            if (obtainStyledAttributes != null) {
                this.mAutoComputeWidth = obtainStyledAttributes.getBoolean(C4409R.styleable.ZMMenuListView_zm_auto_compute_width, false);
                obtainStyledAttributes.recycle();
            }
        }
    }

    private int getMaxWidthOfChildren() {
        int count = getAdapter().getCount();
        View view = null;
        int i = 0;
        for (int i2 = 0; i2 < count; i2++) {
            view = getAdapter().getView(i2, view, this);
            view.measure(0, 0);
            if (view.getMeasuredWidth() > i) {
                i = view.getMeasuredWidth();
            }
        }
        return i;
    }
}
