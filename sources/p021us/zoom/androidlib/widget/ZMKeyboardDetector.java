package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.widget.ZMKeyboardDetector */
public class ZMKeyboardDetector extends View {
    private int mKeyboardHeight = 0;
    private boolean mKeyboardOpen = false;
    private KeyboardListener mListener;
    private boolean mbFirstMeasure = true;

    /* renamed from: us.zoom.androidlib.widget.ZMKeyboardDetector$KeyboardListener */
    public interface KeyboardListener {
        void onKeyboardClosed();

        void onKeyboardOpen();
    }

    public ZMKeyboardDetector(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ZMKeyboardDetector(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMKeyboardDetector(Context context) {
        super(context);
    }

    public boolean isKeyboardOpen() {
        return this.mKeyboardOpen;
    }

    public void setKeyboardListener(KeyboardListener keyboardListener) {
        this.mListener = keyboardListener;
    }

    public int getKeyboardHeight() {
        if (this.mKeyboardOpen) {
            int i = this.mKeyboardHeight;
            if (i != 0) {
                return i;
            }
        }
        return 0;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mListener != null) {
            int size = MeasureSpec.getSize(i2);
            int i3 = getResources().getDisplayMetrics().heightPixels;
            if (size < i3 - UIUtil.dip2px(getContext(), 100.0f)) {
                if (!this.mKeyboardOpen || this.mbFirstMeasure) {
                    Rect rect = new Rect();
                    getGlobalVisibleRect(rect);
                    this.mKeyboardHeight = i3 - rect.bottom;
                    if (this.mKeyboardHeight == 0) {
                        this.mKeyboardHeight = (i3 - size) - rect.top;
                    }
                    this.mKeyboardOpen = true;
                    this.mListener.onKeyboardOpen();
                }
            } else if (this.mKeyboardOpen || this.mbFirstMeasure) {
                this.mKeyboardOpen = false;
                this.mListener.onKeyboardClosed();
            }
            this.mbFirstMeasure = false;
        }
    }
}
