package p021us.zoom.androidlib.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.annotation.Nullable;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.widget.ZMEllipsisTextView */
public class ZMEllipsisTextView extends TextView {
    private static final String TAG = "ZMEllipsisTextView";

    public ZMEllipsisTextView(Context context) {
        super(context);
    }

    public ZMEllipsisTextView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMEllipsisTextView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @TargetApi(21)
    public ZMEllipsisTextView(Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public String ellipsizeAtMiddle(String str, String str2) {
        if (StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return str;
        }
        TextPaint paint = getPaint();
        int width = ((getWidth() - getPaddingLeft()) - getPaddingRight()) - ((int) Math.ceil((double) paint.measureText(str2)));
        if (width <= 0) {
            return str;
        }
        return (String) TextUtils.ellipsize(str, paint, (float) width, TruncateAt.END);
    }

    public void setEllipsisText(String str, int i) {
        if (i == 0) {
            setText(str);
            return;
        }
        try {
            setText(getResources().getString(i, new Object[]{ellipsizeAtMiddle(str, getResources().getString(i, new Object[]{""}))}));
        } catch (Exception unused) {
            setText(getResources().getString(i, new Object[]{str}));
        }
    }
}
