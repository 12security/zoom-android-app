package p021us.zoom.androidlib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/* renamed from: us.zoom.androidlib.widget.ZMButton */
public class ZMButton extends Button {
    public ZMButton(Context context) {
        super(context);
    }

    public ZMButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMButton(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public ZMButton(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public void setBackground(Drawable drawable) {
        Drawable background = getBackground();
        if (background != null) {
            ConstantState constantState = background.getConstantState();
            if (constantState != null) {
                Drawable mutate = constantState.newDrawable().mutate();
                mutate.setColorFilter(805306368, Mode.SRC_ATOP);
                if (background instanceof StateListDrawable) {
                    ((StateListDrawable) background).addState(new int[]{16842919}, mutate);
                } else {
                    StateListDrawable stateListDrawable = new StateListDrawable();
                    stateListDrawable.addState(new int[]{16842919}, mutate);
                    stateListDrawable.addState(new int[0], background);
                    super.setBackground(stateListDrawable);
                }
            }
        }
    }
}
