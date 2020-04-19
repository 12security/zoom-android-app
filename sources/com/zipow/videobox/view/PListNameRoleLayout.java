package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import androidx.core.view.ViewCompat;

public class PListNameRoleLayout extends ViewGroup {
    private static final String TAG = "PListNameRoleLayout";

    public PListNameRoleLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PListNameRoleLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PListNameRoleLayout(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int mode = MeasureSpec.getMode(i);
        int size = (MeasureSpec.getSize(i) - paddingLeft) - paddingRight;
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = (MeasureSpec.getSize(i2) - paddingTop) - paddingBottom;
        int childCount = getChildCount() - 1;
        int i4 = 0;
        int i5 = 0;
        while (childCount >= 0) {
            View childAt = getChildAt(childCount);
            if (childAt.getVisibility() == 8) {
                int i6 = i2;
                i3 = size;
            } else {
                int i7 = size - i4;
                int i8 = 0;
                int i9 = 0;
                while (i8 < childCount) {
                    View childAt2 = getChildAt(i8);
                    int i10 = size;
                    if (childAt2.getVisibility() != 8) {
                        i9 += ViewCompat.getMinimumWidth(childAt2);
                    }
                    i8++;
                    size = i10;
                }
                i3 = size;
                int i11 = i7 - i9;
                if (i11 < 0) {
                    i11 = 0;
                }
                try {
                    try {
                        measureChild(childAt, MeasureSpec.makeMeasureSpec(i11, Integer.MIN_VALUE), i2);
                    } catch (Exception unused) {
                    }
                } catch (Exception unused2) {
                    int i12 = i2;
                }
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                i4 += measuredWidth;
                if (i5 <= measuredHeight) {
                    i5 = measuredHeight;
                }
            }
            childCount--;
            size = i3;
        }
        int i13 = size;
        if (mode != 1073741824) {
            i13 = i4;
        }
        int i14 = i13 + paddingLeft + paddingRight;
        if (mode2 != 1073741824) {
            size2 = i5;
        }
        setMeasuredDimension(i14, size2 + paddingTop + paddingBottom);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                i5 += childAt.getMeasuredWidth();
            }
        }
        int paddingLeft = getPaddingLeft();
        int paddingRight = (i3 - i) - getPaddingRight();
        if (i5 < paddingRight - paddingLeft) {
            paddingRight = paddingLeft + i5;
        }
        for (int i7 = childCount - 1; i7 >= 0; i7--) {
            View childAt2 = getChildAt(i7);
            if (childAt2.getVisibility() != 8) {
                int measuredWidth = paddingRight - childAt2.getMeasuredWidth();
                if (measuredWidth < paddingLeft) {
                    measuredWidth = paddingLeft;
                }
                int measuredHeight = ((i4 - i2) - childAt2.getMeasuredHeight()) / 2;
                childAt2.layout(measuredWidth, measuredHeight, paddingRight, childAt2.getMeasuredHeight() + measuredHeight);
                paddingRight = measuredWidth;
            }
        }
    }
}
