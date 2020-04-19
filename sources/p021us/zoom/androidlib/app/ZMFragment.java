package p021us.zoom.androidlib.app;

import android.app.Dialog;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: us.zoom.androidlib.app.ZMFragment */
public class ZMFragment extends Fragment implements IUIElement {
    private static final String TAG = "ZMFragment";
    private Handler mHandler = new Handler();
    private RetainedFragment mRetainedFragment = null;
    /* access modifiers changed from: private */
    public EventTaskManager mTaskMgr = null;

    /* renamed from: us.zoom.androidlib.app.ZMFragment$ContentViewHelper */
    public static class ContentViewHelper {
        @Nullable
        public static View getFragmentContentView(Fragment fragment) {
            if (fragment == null) {
                return null;
            }
            View view = fragment.getView();
            if (view != null) {
                if ("androidx.fragment.app.Fragment".equals(ZMFragment.class.getSuperclass().getName()) && (view instanceof ViewGroup)) {
                    view = ((ViewGroup) view).getChildAt(0);
                }
            } else if (fragment instanceof DialogFragment) {
                Dialog dialog = ((DialogFragment) fragment).getDialog();
                if (dialog != null) {
                    Window window = dialog.getWindow();
                    if (window != null) {
                        view = window.getDecorView();
                    }
                }
            }
            return view;
        }
    }

    /* renamed from: us.zoom.androidlib.app.ZMFragment$RetainedFragment */
    public static class RetainedFragment extends Fragment {
        EventTaskManager mTaskMgr = new EventTaskManager();

        public RetainedFragment() {
            setRetainInstance(true);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
    }

    public final EventTaskManager getEventTaskManager() {
        RetainedFragment retainedFragment = getRetainedFragment();
        if (retainedFragment != null) {
            return retainedFragment.mTaskMgr;
        }
        return null;
    }

    @NonNull
    public final EventTaskManager getNonNullEventTaskManagerOrThrowException() {
        RetainedFragment retainedFragment = getRetainedFragment();
        if (retainedFragment != null) {
            return retainedFragment.mTaskMgr;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Exception in getNonNullEventTaskManagerOrThrowException. class=");
        sb.append(getClass().getName());
        throw new NullPointerException(sb.toString());
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initRetainedFragment();
        this.mTaskMgr = getRetainedFragment().mTaskMgr;
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        if (this.mRetainedFragment == null) {
            try {
                this.mRetainedFragment = new RetainedFragment();
                FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
                RetainedFragment retainedFragment = this.mRetainedFragment;
                StringBuilder sb = new StringBuilder();
                sb.append(getClass().getName());
                sb.append(":");
                sb.append(RetainedFragment.class.getName());
                beginTransaction.add((Fragment) retainedFragment, sb.toString()).commitAllowingStateLoss();
            } catch (Exception unused) {
            }
        }
    }

    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        FragmentManager fragmentManager = getFragmentManager();
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(":");
        sb.append(RetainedFragment.class.getName());
        return (RetainedFragment) fragmentManager.findFragmentByTag(sb.toString());
    }

    public void finishActivity(int i) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            zMActivity.finishActivityFromFragment(this, i);
        }
    }

    public void zm_requestPermissions(String[] strArr, int i) {
        if (((ZMActivity) getActivity()) != null) {
            ZMActivityCompat.requestPermissionsFromFragment(this, strArr, i);
        }
    }

    public int checkSelfPermission(String str) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity == null) {
            return -1;
        }
        if (!StringUtil.isEmptyOrNull(str)) {
            try {
                return zMActivity.checkPermission(str, Process.myPid(), Process.myUid());
            } catch (Throwable unused) {
                return -1;
            }
        } else {
            throw new IllegalArgumentException("permission is null");
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventTaskManager eventTaskManager = this.mTaskMgr;
        if (eventTaskManager != null) {
            eventTaskManager.onUIDestroy(this);
            FragmentActivity activity = getActivity();
            if ((activity != null && activity.isFinishing()) || isRemoving()) {
                this.mTaskMgr.destroy();
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (!isInMultWindowMode()) {
            EventTaskManager eventTaskManager = this.mTaskMgr;
            if (eventTaskManager != null) {
                eventTaskManager.onPause(this);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (!isInMultWindowMode()) {
            performResume();
        }
    }

    private void performResume() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (((!ZMFragment.this.isInMultWindowMode() && ZMFragment.this.isResumed()) || (ZMFragment.this.isInMultWindowMode() && ZMFragment.this.isVisible())) && ZMFragment.this.mTaskMgr != null) {
                    ZMFragment.this.mTaskMgr.onResume(ZMFragment.this);
                }
            }
        });
    }

    public void onStart() {
        try {
            super.onStart();
            EventTaskManager eventTaskManager = this.mTaskMgr;
            if (eventTaskManager != null) {
                eventTaskManager.onStart(this);
            }
            if (isInMultWindowMode()) {
                performResume();
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Exception in ZMFragment.onStart(). class=");
            sb.append(getClass().getName());
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void onStop() {
        super.onStop();
        EventTaskManager eventTaskManager = this.mTaskMgr;
        if (eventTaskManager != null) {
            eventTaskManager.onStop(this);
        }
    }

    public View getContentView() {
        return ContentViewHelper.getFragmentContentView(this);
    }

    public boolean isInMultWindowMode() {
        FragmentActivity activity = getActivity();
        if (activity != null && VERSION.SDK_INT >= 24) {
            return activity.isInMultiWindowMode();
        }
        return false;
    }
}
