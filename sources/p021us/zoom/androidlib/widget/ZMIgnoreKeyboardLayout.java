package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.LinearLayout;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMIgnoreKeyboardLayout */
public class ZMIgnoreKeyboardLayout extends LinearLayout {
    private int mLastHeight = 0;
    private boolean mbIgnoreKeyboardOpen = true;

    public ZMIgnoreKeyboardLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMIgnoreKeyboardLayout(Context context) {
        super(context);
    }

    public void setIgnoreKeyboardOpen(boolean z) {
        this.mbIgnoreKeyboardOpen = z;
    }

    public boolean getIsIgnoreKeyboardOpen() {
        return this.mbIgnoreKeyboardOpen;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (!getIsIgnoreKeyboardOpen()) {
            super.onMeasure(i, i2);
            return;
        }
        int mode = MeasureSpec.getMode(i2);
        int size = MeasureSpec.getSize(i2);
        int i3 = getResources().getDisplayMetrics().heightPixels;
        if (size < i3 - UIUtil.dip2px(getContext(), 100.0f)) {
            size = this.mLastHeight;
            if (size == 0) {
                size = i3;
            }
        } else {
            this.mLastHeight = size;
        }
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(size, mode));
    }
}
