package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckedTextView;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.AccessibilityUtil;

/* renamed from: us.zoom.androidlib.widget.ZMCheckedTextView */
public class ZMCheckedTextView extends CheckedTextView {
    public ZMCheckedTextView(Context context) {
        super(context);
    }

    public ZMCheckedTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMCheckedTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setChecked(boolean z) {
        Context context = getContext();
        if (context != null) {
            setContentDescription(context.getString(z ? C4409R.string.zm_accessibility_checked_switch_49169 : C4409R.string.zm_accessibility_not_checked_switch_49169));
        }
        if (isChecked() != z && AccessibilityUtil.isSpokenFeedbackEnabled(getContext()) && !AccessibilityUtil.isTalkBack(getContext())) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this, z ? C4409R.string.zm_accessibility_checked_42381 : C4409R.string.zm_accessibility_not_checked_42381);
        }
        super.setChecked(z);
    }
}
