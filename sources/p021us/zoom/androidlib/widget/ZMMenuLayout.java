package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import com.google.common.primitives.Ints;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMMenuLayout */
public class ZMMenuLayout extends ViewGroup {
    public ZMMenuLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ZMMenuLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMMenuLayout(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int mode = MeasureSpec.getMode(i);
        int size = (MeasureSpec.getSize(i) - paddingLeft) - paddingRight;
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = (MeasureSpec.getSize(i2) - paddingTop) - paddingBottom;
        if (size2 == 0) {
            size2 = UIUtil.getDisplayHeight(getContext());
        }
        int childCount = getChildCount();
        int i7 = 0;
        int i8 = 0;
        while (true) {
            i3 = 8;
            if (i7 >= childCount) {
                break;
            }
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() == 8) {
                i4 = size;
            } else {
                LayoutParams layoutParams = childAt.getLayoutParams();
                if (layoutParams.width == -2) {
                    i5 = MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
                } else if (layoutParams.width == -1) {
                    i5 = MeasureSpec.makeMeasureSpec(size, Ints.MAX_POWER_OF_TWO);
                } else {
                    i5 = MeasureSpec.makeMeasureSpec(layoutParams.width, Ints.MAX_POWER_OF_TWO);
                }
                i4 = size;
                if (layoutParams.height == -2) {
                    i6 = MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE);
                } else if (layoutParams.height == -1) {
                    i6 = MeasureSpec.makeMeasureSpec(size2, Ints.MAX_POWER_OF_TWO);
                } else {
                    i6 = MeasureSpec.makeMeasureSpec(layoutParams.height, Ints.MAX_POWER_OF_TWO);
                }
                measureChild(childAt, i5, i6);
                int measuredWidth = childAt.getMeasuredWidth();
                if (i8 <= measuredWidth) {
                    i8 = measuredWidth;
                }
            }
            i7++;
            size = i4;
        }
        int i9 = size;
        int i10 = 0;
        int i11 = 0;
        while (i10 < childCount) {
            View childAt2 = getChildAt(i10);
            if (childAt2.getVisibility() != i3) {
                childAt2.measure(MeasureSpec.makeMeasureSpec(i8, Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(childAt2.getMeasuredHeight(), Ints.MAX_POWER_OF_TWO));
                i11 += childAt2.getMeasuredHeight();
            }
            i10++;
            i3 = 8;
        }
        if (mode != 1073741824) {
            i9 = i8;
        }
        int i12 = i9 + paddingLeft + paddingRight;
        if (mode2 != 1073741824) {
            size2 = i11;
        }
        setMeasuredDimension(i12, size2 + paddingTop + paddingBottom);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        int paddingTop = getPaddingTop();
        int paddingLeft = getPaddingLeft();
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt.getVisibility() != 8) {
                int measuredHeight = childAt.getMeasuredHeight() + paddingTop;
                childAt.layout(paddingLeft, paddingTop, childAt.getMeasuredWidth() + paddingLeft, measuredHeight);
                paddingTop = measuredHeight;
            }
        }
    }
}
