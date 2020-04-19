package p021us.zoom.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.viewpager.widget.ViewPager;

/* renamed from: us.zoom.androidlib.widget.ZMViewPager */
public class ZMViewPager extends ViewPager {
    private boolean mDisableScroll = false;

    /* renamed from: us.zoom.androidlib.widget.ZMViewPager$Page */
    public interface Page {
        boolean canScrollHorizontal(int i, int i2, int i3);
    }

    public ZMViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ZMViewPager(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public boolean canScroll(View view, boolean z, int i, int i2, int i3) {
        if (this.mDisableScroll) {
            return true;
        }
        if (!(view instanceof Page)) {
            return super.canScroll(view, z, i, i2, i3);
        }
        return ((Page) view).canScrollHorizontal(i, i2, i3);
    }

    public boolean isDisableScroll() {
        return this.mDisableScroll;
    }

    public void setDisableScroll(boolean z) {
        this.mDisableScroll = z;
    }
}
