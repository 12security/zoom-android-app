package p021us.zoom.androidlib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import com.google.common.primitives.Ints;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMDialogRootLayout */
public class ZMDialogRootLayout extends LinearLayout {
    private static int MAX_TOTAL_WIDTH = 340;
    private static int MIN_FRAME_WIDTH = 10;

    @SuppressLint({"NewApi"})
    public ZMDialogRootLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ZMDialogRootLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMDialogRootLayout(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        int displayWidth = UIUtil.getDisplayWidth(getContext()) - (UIUtil.dip2px(getContext(), (float) MIN_FRAME_WIDTH) * 2);
        int displayHeight = UIUtil.getDisplayHeight(getContext()) - (UIUtil.dip2px(getContext(), (float) MIN_FRAME_WIDTH) * 2);
        int dip2px = UIUtil.dip2px(getContext(), (float) MAX_TOTAL_WIDTH);
        if (displayWidth > dip2px) {
            displayWidth = dip2px;
        }
        boolean z = true;
        boolean z2 = measuredWidth > displayWidth;
        if (measuredHeight <= displayHeight) {
            z = false;
        }
        if (z2 || z) {
            if (z2) {
                measuredWidth = displayWidth;
            }
            if (z) {
                measuredHeight = displayHeight;
            }
            int makeMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth, Ints.MAX_POWER_OF_TWO);
            if (z) {
                i2 = MeasureSpec.makeMeasureSpec(measuredHeight, Ints.MAX_POWER_OF_TWO);
            }
            super.onMeasure(makeMeasureSpec, i2);
        }
    }
}
