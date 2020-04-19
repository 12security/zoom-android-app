package p021us.zoom.androidlib.app;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.util.HashMap;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.androidlib.widget.ZMTipLayer;

/* renamed from: us.zoom.androidlib.app.ZMTipFragment */
public class ZMTipFragment extends ZMFragment {
    private static final String TAG = "ZMTipFragment";
    private static HashMap<String, Boolean> sTipShownMap = new HashMap<>();
    protected boolean mAutoFocus = true;
    private boolean mCanDismiss = false;
    private Runnable mDismissRunnable;
    private Handler mHandler = new Handler();
    private boolean mShowsTip = false;
    private String mTag = "";
    private ZMTip mTip;
    private boolean mbRemoved = true;
    private long mlDuration = 0;
    private long mlShowTime = 0;

    @Nullable
    public ZMTip onCreateTip(Context context, LayoutInflater layoutInflater, Bundle bundle) {
        return null;
    }

    public void onActivityCreated(Bundle bundle) {
        SparseArray sparseArray;
        super.onActivityCreated(bundle);
        if (bundle != null) {
            sparseArray = bundle.getSparseParcelableArray("tipState");
            this.mlDuration = bundle.getLong("duration");
            this.mShowsTip = bundle.getBoolean("showsTip");
        } else {
            sparseArray = null;
        }
        View view = getView();
        if (!(view == null || sparseArray == null)) {
            view.restoreHierarchyState(sparseArray);
        }
        if (this.mShowsTip) {
            ZMTip onCreateTip = onCreateTip(getActivity(), getLayoutInflater(bundle), bundle);
            if (onCreateTip == null) {
                if (view != null) {
                    onCreateTip = new ZMTip(getActivity());
                    onCreateTip.addView(view);
                } else {
                    return;
                }
            } else if (view == null && sparseArray != null) {
                onCreateTip.restoreHierarchyState(sparseArray);
            }
            this.mTip = onCreateTip;
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        Runnable runnable = this.mDismissRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
        this.mlDuration -= System.currentTimeMillis() - this.mlShowTime;
        SparseArray sparseArray = new SparseArray();
        View view = getView();
        if (view != null) {
            view.saveHierarchyState(sparseArray);
        } else {
            ZMTip zMTip = this.mTip;
            if (zMTip != null) {
                zMTip.saveHierarchyState(sparseArray);
            }
        }
        bundle.putSparseParcelableArray("tipState", sparseArray);
        bundle.putLong("duration", this.mlDuration);
        bundle.putBoolean("showsTip", this.mShowsTip);
        super.onSaveInstanceState(bundle);
        this.mCanDismiss = false;
    }

    public void onResume() {
        super.onResume();
        if (!isInMultWindowMode()) {
            performResume();
        }
    }

    private void performResume() {
        if (this.mShowsTip) {
            if (this.mTip != null) {
                ZMTipLayer tipLayer = getTipLayer();
                if (tipLayer != null) {
                    this.mTip.show(tipLayer);
                    this.mbRemoved = false;
                    this.mCanDismiss = true;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("cannot find a ZPTipLayer width id: ");
                    sb.append(C4409R.class.getName());
                    sb.append(".id.tipLayer");
                    throw new RuntimeException(sb.toString());
                }
            }
            this.mlShowTime = System.currentTimeMillis();
            if (this.mlDuration > 0) {
                this.mDismissRunnable = new Runnable() {
                    public void run() {
                        ZMTipFragment.this.dismiss();
                    }
                };
                this.mHandler.postDelayed(this.mDismissRunnable, this.mlDuration);
            }
            FragmentActivity activity = getActivity();
            if (activity != null && this.mAutoFocus && AccessibilityUtil.isSpokenFeedbackEnabled(activity)) {
                try {
                    if (this.mTip != null) {
                        this.mTip.sendAccessibilityEvent(8);
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    public void onStart() {
        super.onStart();
        if (isInMultWindowMode()) {
            performResume();
        }
    }

    private ZMTipLayer getTipLayer() {
        Resources resources = getResources();
        int identifier = resources.getIdentifier("tipLayer", "id", resources.getResourcePackageName(C4409R.C4411id.zm_used_for_package_name_retrieval));
        if (identifier == 0) {
            return null;
        }
        return getZMTipLayer(identifier);
    }

    /* access modifiers changed from: protected */
    public ZMTipLayer getZMTipLayer(int i) {
        return (ZMTipLayer) getActivity().getWindow().getDecorView().findViewById(i);
    }

    public void show(FragmentManager fragmentManager, String str) {
        show(fragmentManager, str, 0);
    }

    public void show(FragmentManager fragmentManager, String str, long j) {
        if (this.mbRemoved) {
            this.mShowsTip = true;
            this.mbRemoved = false;
            this.mlDuration = j;
            this.mTag = StringUtil.safeString(str);
            sTipShownMap.put(this.mTag, Boolean.valueOf(true));
            try {
                fragmentManager.beginTransaction().add((Fragment) this, str).commitAllowingStateLoss();
            } catch (Exception e) {
                ZMLog.m281e(TAG, e, "", new Object[0]);
            }
            this.mCanDismiss = true;
        }
    }

    public void show(FragmentTransaction fragmentTransaction, String str) {
        show(fragmentTransaction, str, 0);
    }

    public void show(FragmentTransaction fragmentTransaction, String str, long j) {
        if (this.mbRemoved) {
            this.mShowsTip = true;
            this.mbRemoved = false;
            this.mlDuration = j;
            this.mTag = StringUtil.safeString(str);
            sTipShownMap.put(this.mTag, Boolean.valueOf(true));
            try {
                fragmentTransaction.add((Fragment) this, str).commitAllowingStateLoss();
            } catch (Exception e) {
                ZMLog.m281e(TAG, e, "", new Object[0]);
            }
            this.mCanDismiss = true;
        }
    }

    public void dismiss() {
        if (!this.mbRemoved && this.mCanDismiss) {
            this.mShowsTip = false;
            this.mbRemoved = true;
            sTipShownMap.put(this.mTag, Boolean.valueOf(false));
            ZMTip zMTip = this.mTip;
            if (zMTip != null) {
                zMTip.dismiss();
            }
            this.mTip = null;
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                fragmentManager.beginTransaction().remove(this).commitAllowingStateLoss();
            }
        }
    }

    public ZMTip getTip() {
        return this.mTip;
    }

    public boolean getShowsTip() {
        return this.mShowsTip;
    }

    public static boolean isTipShown(String str) {
        Boolean bool = (Boolean) sTipShownMap.get(StringUtil.safeString(str));
        return bool != null && bool.booleanValue();
    }
}
