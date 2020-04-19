package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;

/* renamed from: us.zoom.androidlib.widget.ZMLinearLayoutButton */
public class ZMLinearLayoutButton extends LinearLayout {
    public ZMLinearLayoutButton(Context context) {
        super(context);
    }

    public ZMLinearLayoutButton(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMLinearLayoutButton(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }
}
