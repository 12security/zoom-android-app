package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;

/* renamed from: us.zoom.androidlib.widget.ZMVerticalImageTextButton */
public class ZMVerticalImageTextButton extends ZMImageTextButton {
    public ZMVerticalImageTextButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ZMVerticalImageTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ZMVerticalImageTextButton(Context context) {
        super(context);
        init();
    }

    private void init() {
        setImageTextOrientation(1);
        setBackgroundResource(0);
        setPadding(0, 0, 0, 0);
    }

    public void setImageTextOrientation(int i) {
        if (i == 1) {
            super.setImageTextOrientation(i);
            return;
        }
        throw new RuntimeException("VERTICAL is supported only.");
    }
}
