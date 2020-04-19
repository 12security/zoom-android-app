package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMIOSStyleTitlebarLayout */
public class ZMIOSStyleTitlebarLayout extends LinearLayout {
    private boolean mFileOthers = false;
    private int mLeftButton = 0;
    private int mRightButton = 0;
    private int mTitle = 0;
    private View mvLeftButton = null;
    private View mvRightButton = null;

    @SuppressLint({"NewApi"})
    public ZMIOSStyleTitlebarLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }

    public ZMIOSStyleTitlebarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    public ZMIOSStyleTitlebarLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    private void init(Context context, AttributeSet attributeSet, int i) {
        if (context != null && attributeSet != null) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMIOSStyleTitlebarLayout);
            this.mLeftButton = obtainStyledAttributes.getResourceId(C4409R.styleable.ZMIOSStyleTitlebarLayout_zm_leftButton, 0);
            this.mRightButton = obtainStyledAttributes.getResourceId(C4409R.styleable.ZMIOSStyleTitlebarLayout_zm_rightButton, 0);
            this.mTitle = obtainStyledAttributes.getResourceId(C4409R.styleable.ZMIOSStyleTitlebarLayout_zm_title, 0);
            this.mFileOthers = obtainStyledAttributes.getBoolean(C4409R.styleable.ZMIOSStyleTitlebarLayout_zm_fillOthers, false);
            obtainStyledAttributes.recycle();
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.mvLeftButton = null;
        this.mvRightButton = null;
        View view = null;
        for (int i5 = 0; i5 < getChildCount(); i5++) {
            View childAt = getChildAt(i5);
            int id = childAt.getId();
            if (id == this.mLeftButton || (isInEditMode() && i5 == 0)) {
                this.mvLeftButton = childAt;
                layoutLeftButton(childAt, i, i2, i3, i4);
            } else if (id == this.mRightButton || (isInEditMode() && getChildCount() >= 3 && i5 == 1)) {
                this.mvRightButton = childAt;
                layoutRightButton(childAt, i, i2, i3, i4);
            } else if (id == this.mTitle || (isInEditMode() && ((getChildCount() >= 3 && i5 == 2) || (getChildCount() < 3 && i5 == 1)))) {
                view = childAt;
            } else if (this.mFileOthers) {
                layoutOthersFill(childAt, i, i2, i3, i4);
            } else {
                layoutOthers(childAt, i, i2, i3, i4);
            }
        }
        if (view != null) {
            layoutTitle(view, i, i2, i3, i4);
        }
    }

    private void layoutLeftButton(View view, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i5 = layoutParams.leftMargin;
        int i6 = layoutParams.topMargin;
        int i7 = paddingLeft + i5;
        int measuredHeight = paddingTop + i6 + (((((((i4 - i2) - paddingTop) - i6) - paddingBottom) - layoutParams.bottomMargin) - view.getMeasuredHeight()) / 2);
        view.layout(i7, measuredHeight, view.getMeasuredWidth() + i7, view.getMeasuredHeight() + measuredHeight);
    }

    private void layoutRightButton(View view, int i, int i2, int i3, int i4) {
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i5 = layoutParams.topMargin;
        int i6 = ((i3 - i) - paddingRight) - layoutParams.rightMargin;
        int measuredHeight = paddingTop + i5 + (((((((i4 - i2) - paddingTop) - i5) - paddingBottom) - layoutParams.bottomMargin) - view.getMeasuredHeight()) / 2);
        view.layout(i6 - view.getMeasuredWidth(), measuredHeight, i6, view.getMeasuredHeight() + measuredHeight);
    }

    private void layoutOthers(View view, int i, int i2, int i3, int i4) {
        layoutTitle(view, i, i2, i3, i4);
    }

    private void layoutTitle(View view, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i5 = layoutParams.leftMargin;
        int i6 = layoutParams.topMargin;
        int i7 = layoutParams.rightMargin;
        int i8 = layoutParams.bottomMargin;
        View view2 = this.mvLeftButton;
        int right = view2 != null ? view2.getRight() : 0;
        int i9 = i3 - i;
        View view3 = this.mvRightButton;
        int left = view3 != null ? view3.getLeft() : i9;
        int measuredWidth = ((((((i9 - paddingLeft) - i5) - paddingRight) - i7) - view.getMeasuredWidth()) / 2) + paddingLeft + i5;
        int measuredWidth2 = view.getMeasuredWidth() + measuredWidth;
        int measuredHeight = paddingTop + i6 + (((((((i4 - i2) - paddingTop) - i6) - paddingBottom) - i8) - view.getMeasuredHeight()) / 2);
        int measuredHeight2 = view.getMeasuredHeight() + measuredHeight;
        if (left - right >= view.getMeasuredWidth()) {
            if (measuredWidth < right) {
                left = view.getMeasuredWidth() + right;
            } else if (measuredWidth2 > left) {
                right = left - view.getMeasuredWidth();
            } else {
                right = measuredWidth;
                left = measuredWidth2;
            }
        }
        view.layout(right, measuredHeight, left, measuredHeight2);
    }

    private void layoutOthersFill(View view, int i, int i2, int i3, int i4) {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
        int i5 = layoutParams.leftMargin;
        int i6 = layoutParams.topMargin;
        int i7 = layoutParams.rightMargin;
        int i8 = layoutParams.bottomMargin;
        View view2 = this.mvLeftButton;
        int i9 = 0;
        int right = view2 != null ? view2.getRight() : 0;
        View view3 = this.mvRightButton;
        if (view3 != null) {
            i9 = view3.getLeft();
        }
        view.layout(right + paddingLeft + i5 + view.getPaddingLeft(), i2 + i6 + paddingTop + view.getPaddingTop(), i9 - ((paddingRight + i7) + view.getPaddingRight()), ((i4 - i8) - paddingBottom) - view.getPaddingBottom());
    }
}
