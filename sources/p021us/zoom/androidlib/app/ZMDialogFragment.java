package p021us.zoom.androidlib.app;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.util.SparseArray;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import p021us.zoom.androidlib.app.ZMFragment.ContentViewHelper;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;

/* renamed from: us.zoom.androidlib.app.ZMDialogFragment */
public class ZMDialogFragment extends DialogFragment implements IUIElement {
    public static final String PARAMS = "dialog_fragment_parameters";
    private static final String TAG = "ZMDialogFragment";
    private Runnable mDismissRunnable = new Runnable() {
        public void run() {
            ZMDialogFragment.this.dismiss();
        }
    };
    private Handler mHandler = new Handler();
    private boolean mIsEmptyDialog;
    private RetainedFragment mRetainedFragment = null;
    /* access modifiers changed from: private */
    public EventTaskManager mTaskMgr = null;

    /* renamed from: us.zoom.androidlib.app.ZMDialogFragment$RetainedFragment */
    public static class RetainedFragment extends Fragment {
        EventTaskManager mTaskMgr = new EventTaskManager();

        public RetainedFragment() {
            setRetainInstance(true);
        }
    }

    /* renamed from: us.zoom.androidlib.app.ZMDialogFragment$ZMDialogParam */
    public static class ZMDialogParam implements Parcelable {
        public static final Creator<ZMDialogParam> CREATOR = new Creator<ZMDialogParam>() {
            public ZMDialogParam createFromParcel(Parcel parcel) {
                return new ZMDialogParam(parcel.readInt(), parcel.readLong(), parcel.readString());
            }

            public ZMDialogParam[] newArray(int i) {
                return new ZMDialogParam[i];
            }
        };
        public int intParam;
        public long longParam;
        public String strParam;

        public int describeContents() {
            return 0;
        }

        public ZMDialogParam() {
            this(Integer.MIN_VALUE, Long.MIN_VALUE, "{[(void)]}");
        }

        public ZMDialogParam(int i) {
            this(i, Long.MIN_VALUE, "{[(void)]}");
        }

        public ZMDialogParam(long j) {
            this(Integer.MIN_VALUE, j, "{[(void)]}");
        }

        public ZMDialogParam(String str) {
            this(Integer.MIN_VALUE, Long.MIN_VALUE, str);
        }

        public ZMDialogParam(int i, long j) {
            this(i, j, "{[(void)]}");
        }

        public ZMDialogParam(int i, long j, String str) {
            this.intParam = i;
            this.longParam = j;
            this.strParam = str;
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof ZMDialogParam)) {
                return false;
            }
            ZMDialogParam zMDialogParam = (ZMDialogParam) obj;
            if (this.intParam == zMDialogParam.intParam && this.longParam == zMDialogParam.longParam && this.strParam.equals(zMDialogParam.strParam)) {
                z = true;
            }
            return z;
        }

        public void writeToParcel(@NonNull Parcel parcel, int i) {
            parcel.writeInt(this.intParam);
            parcel.writeLong(this.longParam);
            parcel.writeString(this.strParam);
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

    public static boolean shouldShow(FragmentManager fragmentManager, String str, Parcelable parcelable) {
        if (fragmentManager == null || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag(str);
        if (zMDialogFragment == null) {
            return true;
        }
        if (!zMDialogFragment.isAdded() || zMDialogFragment.isHidden()) {
            zMDialogFragment.dismiss();
            return true;
        }
        Bundle arguments = zMDialogFragment.getArguments();
        if (arguments != null) {
            Parcelable parcelable2 = arguments.getParcelable(PARAMS);
            if (parcelable2 == null) {
                zMDialogFragment.dismiss();
                return true;
            } else if (parcelable == null) {
                zMDialogFragment.dismiss();
                return true;
            } else if (parcelable2.equals(parcelable)) {
                return false;
            } else {
                zMDialogFragment.dismiss();
                return true;
            }
        } else if (parcelable == null) {
            return false;
        } else {
            zMDialogFragment.dismiss();
            return true;
        }
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
        try {
            super.onActivityCreated(bundle);
            if (bundle != null) {
                onRestoreInstanceState(bundle);
            }
            initRetainedFragment();
            RetainedFragment retainedFragment = getRetainedFragment();
            if (retainedFragment != null) {
                this.mTaskMgr = retainedFragment.mTaskMgr;
            }
        } catch (Exception e) {
            String str = null;
            boolean z = false;
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                str = zMActivity.getClass().getName();
                z = zMActivity.isActive();
                if (zMActivity.isFinishing() || ZMActivity.isActivityDestroyed(zMActivity)) {
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Exception in onActivityCreated. class=");
            sb.append(getClass().getName());
            sb.append(", activityClass=");
            sb.append(str);
            sb.append(", isActive=");
            sb.append(z);
            throw new RuntimeException(sb.toString(), e);
        }
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

    /* access modifiers changed from: protected */
    public boolean isInMultWindowMode() {
        FragmentActivity activity = getActivity();
        if (activity != null && VERSION.SDK_INT >= 24) {
            return activity.isInMultiWindowMode();
        }
        return false;
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

    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        RetainedFragment retainedFragment2 = null;
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getName());
            sb.append(":");
            sb.append(RetainedFragment.class.getName());
            retainedFragment2 = (RetainedFragment) fragmentManager.findFragmentByTag(sb.toString());
        }
        return retainedFragment2;
    }

    public void finishActivity(int i) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            zMActivity.finishActivityFromFragment(this, i);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        EventTaskManager eventTaskManager = this.mTaskMgr;
        if (eventTaskManager != null) {
            eventTaskManager.onUIDestroy(this);
        }
        FragmentActivity activity = getActivity();
        if (this.mTaskMgr == null) {
            return;
        }
        if ((activity != null && activity.isFinishing()) || isRemoving()) {
            this.mTaskMgr.destroy();
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mTaskMgr != null && !isInMultWindowMode()) {
            this.mTaskMgr.onPause(this);
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
                if (((!ZMDialogFragment.this.isInMultWindowMode() && ZMDialogFragment.this.isResumed()) || (ZMDialogFragment.this.isInMultWindowMode() && ZMDialogFragment.this.isVisible())) && ZMDialogFragment.this.mTaskMgr != null) {
                    ZMDialogFragment.this.mTaskMgr.onResume(ZMDialogFragment.this);
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
            if (this.mIsEmptyDialog) {
                dismiss();
            }
        } catch (Exception e) {
            String str = null;
            boolean z = false;
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                str = zMActivity.getClass().getName();
                z = zMActivity.isActive();
                if (zMActivity.isFinishing() || ZMActivity.isActivityDestroyed(zMActivity)) {
                    return;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Exception in ZMDialogFragment.onStart(). class=");
            sb.append(getClass().getName());
            sb.append(", activityClass=");
            sb.append(str);
            sb.append(", isActive=");
            sb.append(z);
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public void onStop() {
        super.onStop();
        Handler handler = this.mHandler;
        if (handler != null) {
            Runnable runnable = this.mDismissRunnable;
            if (runnable != null) {
                handler.removeCallbacks(runnable);
            }
        }
        EventTaskManager eventTaskManager = this.mTaskMgr;
        if (eventTaskManager != null) {
            eventTaskManager.onStop(this);
        }
    }

    public void showNow(FragmentManager fragmentManager, String str) {
        if (!isStateSaved() && !isAdded()) {
            super.showNow(fragmentManager, str);
        }
    }

    public void show(FragmentManager fragmentManager, String str) {
        FragmentActivity activity = getActivity();
        if (activity == null || (!activity.isFinishing() && !ZMActivity.isActivityDestroyed(activity))) {
            try {
                super.show(fragmentManager, str);
            } catch (Exception unused) {
            }
        }
    }

    public int show(FragmentTransaction fragmentTransaction, String str) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            return (!activity.isFinishing() && ZMActivity.isActivityDestroyed(activity)) ? -1 : -1;
        }
        try {
            return super.show(fragmentTransaction, str);
        } catch (Exception unused) {
            return -1;
        }
    }

    public void dismiss() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (activity.getSupportFragmentManager().isStateSaved()) {
                dismissAllowingStateLoss();
            } else {
                super.dismiss();
            }
        }
    }

    public void postDismiss() {
        Handler handler = this.mHandler;
        if (handler != null) {
            Runnable runnable = this.mDismissRunnable;
            if (runnable != null) {
                handler.post(runnable);
            }
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        View contentView = getContentView();
        if (contentView != null) {
            SparseArray sparseArray = new SparseArray();
            contentView.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("saasbee_contentViewState", sparseArray);
        }
    }

    /* access modifiers changed from: protected */
    public void finishFragment(boolean z) {
        if (getShowsDialog()) {
            super.dismissAllowingStateLoss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
            if (!z) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finishFragment(int i) {
        if (getShowsDialog()) {
            super.dismissAllowingStateLoss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setResult(i);
            activity.finish();
        }
    }

    /* access modifiers changed from: protected */
    public void finishFragment(int i, Intent intent) {
        if (getShowsDialog()) {
            super.dismissAllowingStateLoss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (intent == null) {
                activity.setResult(i);
            } else {
                activity.setResult(i, intent);
            }
            activity.finish();
        }
    }

    private void onRestoreInstanceState(Bundle bundle) {
        View contentView = getContentView();
        if (contentView != null) {
            SparseArray sparseParcelableArray = bundle.getSparseParcelableArray("saasbee_contentViewState");
            if (sparseParcelableArray != null) {
                try {
                    contentView.restoreHierarchyState(sparseParcelableArray);
                } catch (Exception unused) {
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public Dialog createEmptyDialog() {
        this.mIsEmptyDialog = true;
        return new Builder(getActivity()).create();
    }

    public View getContentView() {
        return ContentViewHelper.getFragmentContentView(this);
    }
}
