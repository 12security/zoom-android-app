package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;

/* renamed from: us.zoom.androidlib.widget.ZMTextButton */
public class ZMTextButton extends TextView {
    public ZMTextButton(Context context) {
        super(context);
    }

    public ZMTextButton(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMTextButton(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }
}
