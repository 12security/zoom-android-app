package p021us.zoom.androidlib.material;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

/* renamed from: us.zoom.androidlib.material.ZMViewPagerBottomSheetDialogFragment */
public class ZMViewPagerBottomSheetDialogFragment extends AppCompatDialogFragment {
    public Dialog onCreateDialog(Bundle bundle) {
        return new ZMViewPagerBottomSheetDialog(getContext(), getTheme());
    }

    public void setupViewPager(ViewPager viewPager) {
        View findBottomSheetParent = findBottomSheetParent(viewPager);
        if (findBottomSheetParent != null) {
            viewPager.addOnPageChangeListener(new ZMViewPagerBottomSheetListener(viewPager, findBottomSheetParent));
        }
    }

    private View findBottomSheetParent(View view) {
        while (view != null) {
            LayoutParams layoutParams = view.getLayoutParams();
            if ((layoutParams instanceof CoordinatorLayout.LayoutParams) && (((CoordinatorLayout.LayoutParams) layoutParams).getBehavior() instanceof ZMViewPagerBottomSheetBehavior)) {
                return view;
            }
            ViewParent parent = view.getParent();
            view = (parent == null || !(parent instanceof View)) ? null : (View) parent;
        }
        return null;
    }
}
