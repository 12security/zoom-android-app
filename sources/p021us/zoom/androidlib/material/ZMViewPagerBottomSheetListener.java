package p021us.zoom.androidlib.material;

import android.view.View;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.SimpleOnPageChangeListener;

/* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetListener */
public class ZMViewPagerBottomSheetListener extends SimpleOnPageChangeListener {
    /* access modifiers changed from: private */
    public final ZMViewPagerBottomSheetBehavior<View> behavior;
    private final ViewPager viewPager;

    public ZMViewPagerBottomSheetListener(ViewPager viewPager2, View view) {
        this.viewPager = viewPager2;
        this.behavior = ZMViewPagerBottomSheetBehavior.from(view);
    }

    public void onPageSelected(int i) {
        this.viewPager.post(new Runnable() {
            public void run() {
                ZMViewPagerBottomSheetListener.this.behavior.invalidateScrollingChild();
            }
        });
    }
}
