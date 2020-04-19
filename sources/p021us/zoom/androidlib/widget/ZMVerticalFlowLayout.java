package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import androidx.core.view.GravityCompat;
import java.util.ArrayList;
import p021us.zoom.androidlib.util.OsUtil;

/* renamed from: us.zoom.androidlib.widget.ZMVerticalFlowLayout */
public class ZMVerticalFlowLayout extends LinearLayout {
    private static final String TAG = "ZMVerticalFlowLayout";
    private ArrayList<Integer> mColumnWidths = new ArrayList<>();
    private int mGravityHolder;

    public ZMVerticalFlowLayout(Context context) {
        super(context);
    }

    public ZMVerticalFlowLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMVerticalFlowLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @SuppressLint({"NewApi"})
    public ZMVerticalFlowLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setGravity(int i) {
        super.setGravity(i);
        if (this.mGravityHolder != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravityHolder = i;
        }
    }

    public int getGravity() {
        return this.mGravityHolder;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int mode = MeasureSpec.getMode(i);
        int size = (MeasureSpec.getSize(i) - paddingLeft) - paddingRight;
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = (MeasureSpec.getSize(i2) - paddingTop) - paddingBottom;
        int childCount = getChildCount();
        this.mColumnWidths.clear();
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        int i11 = 0;
        int i12 = 0;
        int i13 = 0;
        while (i8 < childCount) {
            View childAt = getChildAt(i8);
            int i14 = childCount;
            int i15 = paddingBottom;
            if (childAt.getVisibility() == 8) {
                i3 = size;
            } else {
                try {
                    measureChild(childAt, MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE), MeasureSpec.makeMeasureSpec(size2, Integer.MIN_VALUE));
                } catch (Exception unused) {
                }
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams != null) {
                    i5 = layoutParams.leftMargin;
                    i3 = size;
                    i4 = layoutParams.topMargin;
                    i6 = layoutParams.rightMargin;
                    i7 = layoutParams.bottomMargin;
                } else {
                    i3 = size;
                    i7 = 0;
                    i6 = 0;
                    i5 = 0;
                    i4 = 0;
                }
                int measuredWidth = childAt.getMeasuredWidth() + i5 + i6;
                int measuredHeight = childAt.getMeasuredHeight() + i4 + i7;
                if (size2 - i10 < measuredHeight && i13 > 0) {
                    if (i9 <= i10) {
                        i9 = i10;
                    }
                    i11 += i12;
                    this.mColumnWidths.add(Integer.valueOf(i12));
                    i10 = 0;
                    i12 = 0;
                    i13 = 0;
                }
                i13++;
                if (i12 <= measuredWidth) {
                    i12 = measuredWidth;
                }
                i10 += measuredHeight;
            }
            i8++;
            childCount = i14;
            paddingBottom = i15;
            size = i3;
        }
        int i16 = paddingBottom;
        int i17 = size;
        if (i9 > i10) {
            i10 = i9;
        }
        int i18 = i11 + i12;
        this.mColumnWidths.add(Integer.valueOf(i12));
        if (mode != 1073741824) {
            i17 = i18;
        }
        int i19 = i17 + paddingLeft + paddingRight;
        if (mode2 == 1073741824) {
            i10 = size2;
        }
        setMeasuredDimension(i19, i10 + paddingTop + i16);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        ZMVerticalFlowLayout zMVerticalFlowLayout = this;
        int gravity = getGravity();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = ((i4 - i2) - paddingTop) - getPaddingBottom();
        int childCount = getChildCount();
        int i16 = paddingLeft;
        int i17 = paddingTop;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        int i21 = 0;
        int i22 = 0;
        while (i18 < childCount) {
            View childAt = zMVerticalFlowLayout.getChildAt(i18);
            if (childAt.getVisibility() == 8) {
                i8 = gravity;
                i7 = paddingTop;
                i6 = childCount;
                i5 = paddingBottom;
            } else {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                if (layoutParams != null) {
                    i9 = layoutParams.leftMargin;
                    i11 = layoutParams.topMargin;
                    i7 = paddingTop;
                    i12 = layoutParams.rightMargin;
                    i10 = layoutParams.bottomMargin;
                } else {
                    i7 = paddingTop;
                    i12 = 0;
                    i11 = 0;
                    i10 = 0;
                    i9 = 0;
                }
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                i6 = childCount;
                int i23 = i9 + measuredWidth + i12;
                int i24 = i11 + measuredHeight + i10;
                int i25 = i17;
                if (paddingBottom - i19 >= i24 || i20 <= 0) {
                    i13 = i19;
                    i14 = i25;
                    i5 = paddingBottom;
                } else {
                    i16 += i21;
                    i22++;
                    i14 = i7;
                    i5 = paddingBottom;
                    i20 = 0;
                    i21 = 0;
                    i13 = 0;
                }
                i20++;
                if (zMVerticalFlowLayout.mColumnWidths.size() <= i22 || !OsUtil.isAtLeastJB_MR1()) {
                    i8 = gravity;
                    i15 = i9 + i16;
                } else {
                    int intValue = ((Integer) zMVerticalFlowLayout.mColumnWidths.get(i22)).intValue();
                    i8 = gravity;
                    int absoluteGravity = Gravity.getAbsoluteGravity(gravity & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK, getLayoutDirection());
                    i15 = absoluteGravity != 1 ? absoluteGravity != 5 ? i9 + i16 : ((intValue + i16) - i12) - measuredWidth : i16 + i9 + ((((intValue - i9) - i12) - measuredWidth) / 2);
                }
                int i26 = i11 + i14;
                childAt.layout(i15, i26, i15 + measuredWidth, i26 + measuredHeight);
                int i27 = i14 + i24;
                if (i21 <= i23) {
                    i21 = i23;
                }
                int i28 = i13 + i24;
                i17 = i27;
                i19 = i28;
            }
            i18++;
            paddingTop = i7;
            childCount = i6;
            paddingBottom = i5;
            gravity = i8;
            zMVerticalFlowLayout = this;
        }
    }
}
