package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import com.google.common.primitives.Ints;
import p021us.zoom.androidlib.C4409R;

/* renamed from: us.zoom.androidlib.widget.ZMToolbarLayout */
public class ZMToolbarLayout extends ViewGroup {
    private static final String TAG = "ZMToolbarLayout";
    int showChildNumber = -1;

    public ZMToolbarLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet);
    }

    public ZMToolbarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet);
    }

    public ZMToolbarLayout(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attributeSet) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C4409R.styleable.ZMToolbarLayout);
        this.showChildNumber = obtainStyledAttributes.getInt(C4409R.styleable.ZMToolbarLayout_zm_show_child_number, -1);
        obtainStyledAttributes.recycle();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int mode = MeasureSpec.getMode(i);
        int size = (MeasureSpec.getSize(i) - paddingLeft) - paddingRight;
        int mode2 = MeasureSpec.getMode(i2);
        int size2 = (MeasureSpec.getSize(i2) - paddingTop) - paddingBottom;
        int childCount = getChildCount();
        int i13 = 0;
        int i14 = 0;
        while (true) {
            i3 = 8;
            if (i13 >= childCount) {
                break;
            }
            if (getChildAt(i13).getVisibility() != 8) {
                i14++;
            }
            i13++;
        }
        if (this.showChildNumber > 0) {
            if (size == 0) {
                size = ((ViewGroup) getParent()).getWidth();
            }
            int i15 = size / i14;
            int i16 = this.showChildNumber;
            if (i16 <= 0 || i14 <= i16) {
                i9 = i15;
                i10 = i9;
            } else {
                i9 = i15;
                i10 = (int) (((double) size) / (((double) i16) + 0.5d));
            }
            int i17 = 0;
            int i18 = 0;
            i6 = 0;
            while (i17 < childCount) {
                int i19 = paddingBottom;
                View childAt = getChildAt(i17);
                int i20 = paddingTop;
                if (childAt.getVisibility() != i3) {
                    try {
                        measureChild(childAt, MeasureSpec.makeMeasureSpec(i10, mode), MeasureSpec.makeMeasureSpec(size2, mode2));
                    } catch (Exception unused) {
                    }
                    int measuredWidth = childAt.getMeasuredWidth();
                    int measuredHeight = childAt.getMeasuredHeight();
                    if (i18 <= measuredWidth) {
                        i18 = measuredWidth;
                    }
                    if (i6 <= measuredHeight) {
                        i6 = measuredHeight;
                    }
                }
                i17++;
                paddingBottom = i19;
                paddingTop = i20;
                i3 = 8;
            }
            i4 = paddingTop;
            i5 = paddingBottom;
            if (i18 * i14 <= size) {
                i11 = i9;
                i12 = Ints.MAX_POWER_OF_TWO;
            } else {
                float f = (((float) size) * 1.0f) / ((float) i18);
                this.showChildNumber = size / i18;
                int i21 = this.showChildNumber;
                if (((double) (f - ((float) i21))) >= 0.5d) {
                    i11 = (int) (((double) size) / (((double) i21) + 0.5d));
                    i12 = Ints.MAX_POWER_OF_TWO;
                } else {
                    i11 = (int) (((double) size) / (((double) i21) - 0.5d));
                    i12 = Ints.MAX_POWER_OF_TWO;
                }
            }
            if (mode2 == i12) {
                i6 = size2;
            }
            i8 = i11;
            i7 = Ints.MAX_POWER_OF_TWO;
        } else {
            i4 = paddingTop;
            i5 = paddingBottom;
            i8 = i14 != 0 ? size / i14 : size;
            int i22 = 0;
            int i23 = 0;
            for (int i24 = 0; i24 < childCount; i24++) {
                View childAt2 = getChildAt(i24);
                if (childAt2.getVisibility() != 8) {
                    try {
                        measureChild(childAt2, MeasureSpec.makeMeasureSpec(i8, mode), MeasureSpec.makeMeasureSpec(size2, mode2));
                    } catch (Exception unused2) {
                    }
                    int measuredWidth2 = childAt2.getMeasuredWidth();
                    int measuredHeight2 = childAt2.getMeasuredHeight();
                    if (i22 > measuredWidth2) {
                        measuredWidth2 = i22;
                    }
                    if (i23 <= measuredHeight2) {
                        i23 = measuredHeight2;
                    }
                    i22 = measuredWidth2;
                }
            }
            i7 = Ints.MAX_POWER_OF_TWO;
            if (mode != 1073741824) {
                i8 = i22;
            }
            i6 = mode2 != 1073741824 ? i23 : size2;
        }
        int i25 = 0;
        while (i25 < childCount) {
            View childAt3 = getChildAt(i25);
            if (childAt3.getVisibility() != 8) {
                try {
                    childAt3.measure(MeasureSpec.makeMeasureSpec(i8, i7), MeasureSpec.makeMeasureSpec(i6, i7));
                } catch (Exception unused3) {
                }
            }
            i25++;
            i7 = Ints.MAX_POWER_OF_TWO;
        }
        if (mode != i7) {
            size = i8 * i14;
        }
        int i26 = size + paddingLeft + paddingRight;
        if (mode2 == i7) {
            i6 = size2;
        }
        setMeasuredDimension(i26, i6 + i4 + i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int childCount = getChildCount();
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int width = getWidth() - getPaddingRight();
        int i5 = childCount - 1;
        while (i5 >= 0 && getChildAt(i5).getVisibility() == 8) {
            i5--;
        }
        int i6 = 0;
        while (i6 < childCount) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                int measuredWidth = childAt.getMeasuredWidth() + paddingLeft;
                childAt.layout(paddingLeft, paddingTop, i5 == i6 ? width : measuredWidth, childAt.getMeasuredHeight() + paddingTop);
                paddingLeft = measuredWidth;
            }
            i6++;
        }
    }
}
