package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

/* renamed from: us.zoom.androidlib.widget.ZMChildListView */
public class ZMChildListView extends ListView {
    public ZMChildListView(Context context) {
        super(context);
    }

    public ZMChildListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMChildListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(536870911, Integer.MIN_VALUE));
    }
}
