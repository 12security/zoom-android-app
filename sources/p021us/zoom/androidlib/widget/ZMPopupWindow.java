package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.PopupWindow;

/* renamed from: us.zoom.androidlib.widget.ZMPopupWindow */
public class ZMPopupWindow extends PopupWindow {
    /* access modifiers changed from: private */
    public boolean mbDismissOnTouchOutside = false;

    public ZMPopupWindow() {
        init();
    }

    public ZMPopupWindow(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    public ZMPopupWindow(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ZMPopupWindow(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ZMPopupWindow(Context context) {
        super(context);
        init();
    }

    public ZMPopupWindow(int i, int i2) {
        super(i, i2);
        init();
    }

    public ZMPopupWindow(View view, int i, int i2, boolean z) {
        super(view, i, i2, z);
        init();
    }

    public ZMPopupWindow(View view, int i, int i2) {
        super(view, i, i2);
        init();
    }

    public ZMPopupWindow(View view) {
        super(view);
        init();
    }

    /* access modifiers changed from: protected */
    public void init() {
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new BitmapDrawable());
        setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (ZMPopupWindow.this.mbDismissOnTouchOutside && ZMPopupWindow.this.isOutside(motionEvent)) {
                    ZMPopupWindow.this.dismiss();
                }
                return false;
            }
        });
    }

    public void setDismissOnTouchOutside(boolean z) {
        this.mbDismissOnTouchOutside = z;
    }

    /* access modifiers changed from: private */
    public boolean isOutside(MotionEvent motionEvent) {
        boolean z = false;
        if (motionEvent == null) {
            return false;
        }
        View contentView = getContentView();
        if (contentView == null) {
            return false;
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        float width = (float) contentView.getWidth();
        float height = (float) contentView.getHeight();
        if (x < 0.0f || x > width || y < 0.0f || y > height) {
            z = true;
        }
        return z;
    }
}
