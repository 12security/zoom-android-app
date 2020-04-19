package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.core.text.TextUtilsCompat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMFlowLayout */
public class ZMFlowLayout extends ViewGroup {
    private static final int CENTER = 0;
    private static final int LEFT = -1;
    private static final int RIGHT = 1;
    private int lineMargin;
    private List<View> lineViews;
    protected List<List<View>> mAllViews;
    private int mGravity;
    protected List<Integer> mLineHeight;
    protected List<Integer> mLineWidth;

    public ZMFlowLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mAllViews = new ArrayList();
        this.mLineHeight = new ArrayList();
        this.mLineWidth = new ArrayList();
        this.mGravity = -1;
        this.lineViews = new ArrayList();
        if (TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) {
            if (this.mGravity == -1) {
                this.mGravity = 1;
            } else {
                this.mGravity = -1;
            }
        }
        this.lineMargin = UIUtil.dip2px(context, 6.0f);
    }

    public ZMFlowLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ZMFlowLayout(Context context) {
        this(context, null);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int mode = MeasureSpec.getMode(i);
        int size2 = MeasureSpec.getSize(i2);
        int mode2 = MeasureSpec.getMode(i2);
        int childCount = getChildCount();
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                measureChild(childAt, i, i2);
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth() + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                int measuredHeight = childAt.getMeasuredHeight() + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin;
                int i8 = i4 + measuredWidth;
                if (i8 > (size - getPaddingLeft()) - getPaddingRight()) {
                    i3 = Math.max(i3, i4);
                    i6 += i5 + (i6 > 0 ? this.lineMargin : 0);
                    i8 = measuredWidth;
                } else {
                    measuredHeight = Math.max(i5, measuredHeight);
                }
                if (i7 == childCount - 1) {
                    i3 = Math.max(i8, i3);
                    i6 += this.lineMargin + measuredHeight;
                    i5 = measuredHeight;
                    i4 = i8;
                } else {
                    i5 = measuredHeight;
                    i4 = i8;
                }
            } else if (i7 == childCount - 1) {
                i3 = Math.max(i4, i3);
                i6 += this.lineMargin + i5;
                int i9 = i;
                int i10 = i2;
            } else {
                int i11 = i;
                int i12 = i2;
            }
        }
        if (mode != 1073741824) {
            size = getPaddingRight() + i3 + getPaddingLeft();
        }
        if (mode2 != 1073741824) {
            size2 = i6 + getPaddingTop() + getPaddingBottom();
        }
        setMeasuredDimension(size, size2);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mAllViews.clear();
        this.mLineHeight.clear();
        this.mLineWidth.clear();
        this.lineViews.clear();
        int width = getWidth();
        int childCount = getChildCount();
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < childCount; i7++) {
            View childAt = getChildAt(i7);
            if (childAt.getVisibility() != 8) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) childAt.getLayoutParams();
                int measuredWidth = childAt.getMeasuredWidth();
                int measuredHeight = childAt.getMeasuredHeight();
                if (measuredWidth + i6 + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin > (width - getPaddingLeft()) - getPaddingRight()) {
                    List<Integer> list = this.mLineHeight;
                    if (!list.isEmpty()) {
                        i5 += this.lineMargin;
                    }
                    list.add(Integer.valueOf(i5));
                    this.mAllViews.add(this.lineViews);
                    this.mLineWidth.add(Integer.valueOf(i6));
                    i5 = marginLayoutParams.topMargin + measuredHeight + marginLayoutParams.bottomMargin;
                    this.lineViews = new ArrayList();
                    i6 = 0;
                }
                i6 += measuredWidth + marginLayoutParams.leftMargin + marginLayoutParams.rightMargin;
                i5 = Math.max(i5, measuredHeight + marginLayoutParams.topMargin + marginLayoutParams.bottomMargin);
                this.lineViews.add(childAt);
            }
        }
        List<Integer> list2 = this.mLineHeight;
        if (!list2.isEmpty()) {
            i5 += this.lineMargin;
        }
        list2.add(Integer.valueOf(i5));
        this.mLineWidth.add(Integer.valueOf(i6));
        this.mAllViews.add(this.lineViews);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int size = this.mAllViews.size();
        int i8 = paddingTop;
        int i9 = paddingLeft;
        int i10 = 0;
        while (i10 < size) {
            this.lineViews = (List) this.mAllViews.get(i10);
            int intValue = ((Integer) this.mLineHeight.get(i10)).intValue();
            int intValue2 = ((Integer) this.mLineWidth.get(i10)).intValue();
            switch (this.mGravity) {
                case -1:
                    i9 = getPaddingLeft();
                    break;
                case 0:
                    i9 = ((width - intValue2) / 2) + getPaddingLeft();
                    break;
                case 1:
                    i9 = (width - (intValue2 + getPaddingLeft())) - getPaddingRight();
                    Collections.reverse(this.lineViews);
                    break;
            }
            int i11 = i9;
            int i12 = 0;
            while (i12 < this.lineViews.size()) {
                View view = (View) this.lineViews.get(i12);
                if (view.getVisibility() != 8) {
                    MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) view.getLayoutParams();
                    int i13 = (i12 > 0 ? marginLayoutParams2.leftMargin : 0) + i11;
                    int i14 = marginLayoutParams2.topMargin + i8 + (i10 > 0 ? this.lineMargin : 0);
                    view.layout(i13, i14, view.getMeasuredWidth() + i13, view.getMeasuredHeight() + i14);
                    i11 += view.getMeasuredWidth() + (i12 > 0 ? marginLayoutParams2.leftMargin : 0) + marginLayoutParams2.rightMargin;
                }
                i12++;
            }
            i8 += intValue;
            i10++;
            i9 = i11;
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new MarginLayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-2, -2);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(LayoutParams layoutParams) {
        return new MarginLayoutParams(layoutParams);
    }
}
