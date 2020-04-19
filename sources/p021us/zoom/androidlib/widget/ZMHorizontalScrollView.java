package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/* renamed from: us.zoom.androidlib.widget.ZMHorizontalScrollView */
public class ZMHorizontalScrollView extends HorizontalScrollView {
    private OnZMScrollChangedListener onZMScrollChangedListener;

    public ZMHorizontalScrollView(Context context) {
        super(context);
    }

    public ZMHorizontalScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setOnZMScrollChangedListener(OnZMScrollChangedListener onZMScrollChangedListener2) {
        this.onZMScrollChangedListener = onZMScrollChangedListener2;
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnZMScrollChangedListener onZMScrollChangedListener2 = this.onZMScrollChangedListener;
        if (onZMScrollChangedListener2 != null) {
            onZMScrollChangedListener2.onScrollChange(this, i, i2, i3, i4);
        }
    }
}
