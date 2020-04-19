package p021us.zoom.androidlib.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.util.DeviceInfoUtil;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.LanguageUtil;
import p021us.zoom.androidlib.util.ListenerList;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;

/* renamed from: us.zoom.androidlib.app.ZMActivity */
public class ZMActivity extends FragmentActivity implements IUIElement {
    private static ArrayList<ZMActivity> sActivityStack = new ArrayList<>();
    private static ZMActivity sFrontActivity = null;
    private static ListenerList sGlobalActivityListenerList = new ListenerList();
    private static boolean sHasActivityCreated = false;
    private boolean mActive = false;
    private AtomicInteger mAutoIncrementIndex = new AtomicInteger(0);
    private boolean mDestroyed = false;
    private boolean mDisableGestureFinish = false;
    private List<View> mDisableGestureFinishView = new ArrayList();
    private GestureDetector mGestureDetector;
    private final SimpleOnGestureListener mGestureListener = new SimpleOnGestureListener() {
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            return ZMActivity.this.onFling(motionEvent, motionEvent2, f, f2);
        }
    };
    private Handler mHandler = new Handler();
    private SparseArray<WeakReference<Fragment>> mPendingPermissionFragments = new SparseArray<>();
    private RetainedFragment mRetainedFragment = null;
    private final String mTag = getClass().getSimpleName();
    private EventTaskManager mTaskMgr = null;

    /* renamed from: us.zoom.androidlib.app.ZMActivity$GlobalActivityListener */
    public interface GlobalActivityListener extends IListener {
        void onActivityMoveToFront(ZMActivity zMActivity);

        void onUIMoveToBackground();

        void onUserActivityOnUI();
    }

    /* renamed from: us.zoom.androidlib.app.ZMActivity$RetainedFragment */
    public static class RetainedFragment extends Fragment {
        EventTaskManager mTaskMgr = new EventTaskManager();

        public RetainedFragment() {
            setRetainInstance(true);
        }
    }

    public static void addGlobalActivityListener(GlobalActivityListener globalActivityListener) {
        if (globalActivityListener != null) {
            IListener[] all = sGlobalActivityListenerList.getAll();
            for (int i = 0; i < all.length; i++) {
                if (all[i].getClass() == globalActivityListener.getClass()) {
                    removeGlobalActivityListener((GlobalActivityListener) all[i]);
                }
            }
            sGlobalActivityListenerList.add(globalActivityListener);
        }
    }

    public static void removeGlobalActivityListener(GlobalActivityListener globalActivityListener) {
        sGlobalActivityListenerList.remove(globalActivityListener);
    }

    public final boolean isActive() {
        return this.mActive && !isFinishing() && !isActivityDestroyed(this);
    }

    @Nullable
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

    public static ZMActivity getFrontActivity() {
        return sFrontActivity;
    }

    public static boolean hasActivityCreated() {
        return sHasActivityCreated;
    }

    public static void setHasActivityCreated(boolean z) {
        sHasActivityCreated = z;
    }

    public static int getInProcessActivityCountInStack() {
        return sActivityStack.size();
    }

    public static ZMActivity getInProcessActivityInStackAt(int i) {
        if (i < 0 || i >= sActivityStack.size()) {
            return null;
        }
        return (ZMActivity) sActivityStack.get(i);
    }

    public static boolean isActivityDestroyed(Activity activity) {
        if (activity == null) {
            return false;
        }
        if (OsUtil.isAtLeastJB_MR1()) {
            return activity.isDestroyed();
        }
        try {
            try {
                Boolean bool = (Boolean) activity.getClass().getMethod("isDestroyed", new Class[0]).invoke(activity, new Object[0]);
                if (bool != null) {
                    return bool.booleanValue();
                }
                return false;
            } catch (Exception unused) {
                return false;
            }
        } catch (NoSuchMethodException unused2) {
            return false;
        }
    }

    public void addDisableGestureFinishView(View view) {
        if (view != null && !this.mDisableGestureFinishView.contains(view)) {
            this.mDisableGestureFinishView.add(view);
        }
    }

    public void removeDisableGestureFinishView(View view) {
        if (view != null) {
            this.mDisableGestureFinishView.remove(view);
        }
    }

    public boolean isInMultiWindowMode() {
        if (OsUtil.isAtLeastN()) {
            return super.isInMultiWindowMode();
        }
        return false;
    }

    public void disableFinishActivityByGesture(boolean z) {
        this.mDisableGestureFinish = z;
    }

    private boolean isMotionEventInDisableGestureFinishView(MotionEvent motionEvent) {
        if (this.mDisableGestureFinishView.size() == 0 || motionEvent == null) {
            return false;
        }
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        Rect rect = new Rect();
        for (View view : this.mDisableGestureFinishView) {
            if (view.isShown()) {
                view.getGlobalVisibleRect(rect);
                if (rect.contains((int) rawX, (int) rawY)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!(this.mDisableGestureFinish || motionEvent == null || motionEvent2 == null)) {
            float abs = Math.abs(motionEvent.getRawX() - motionEvent2.getRawX());
            float abs2 = Math.abs(motionEvent.getRawY() - motionEvent2.getRawY());
            if (f > 3000.0f && abs > 0.0f && Math.abs(abs2) < ((float) UIUtil.dip2px(this, 50.0f))) {
                onBackPressed();
                return true;
            }
        }
        return false;
    }

    public boolean isDestroyed() {
        if (OsUtil.isAtLeastJB_MR1()) {
            return super.isDestroyed();
        }
        return this.mDestroyed;
    }

    public void finishActivityFromFragment(Fragment fragment, int i) {
        if (fragment != null) {
            if (i == -1) {
                finishActivity(-1);
            } else if ((-65536 & i) == 0) {
                int fragmentIndex = getFragmentIndex(fragment);
                if (fragmentIndex >= 0) {
                    finishActivity(((fragmentIndex + 1) << 16) + (i & 65535));
                }
            } else {
                throw new IllegalArgumentException("Can only use lower 16 bits for requestCode");
            }
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (((i >> 16) & 65535) != 0) {
            onRequestPermissionsResultForFragment(i, strArr, iArr);
        }
    }

    private void onRequestPermissionsResultForFragment(int i, String[] strArr, int[] iArr) {
        int i2 = (i >> 16) & 65535;
        if (i2 != 0) {
            int i3 = i2 - 1;
            Fragment fragment = null;
            WeakReference weakReference = (WeakReference) this.mPendingPermissionFragments.get(i3);
            if (weakReference != null) {
                fragment = (Fragment) weakReference.get();
                this.mPendingPermissionFragments.remove(i3);
            }
            if (fragment != null) {
                if (fragment instanceof ZMFragment) {
                    ((ZMFragment) fragment).onRequestPermissionsResult(i & 65535, strArr, iArr);
                } else if (fragment instanceof ZMDialogFragment) {
                    ((ZMDialogFragment) fragment).onRequestPermissionsResult(i & 65535, strArr, iArr);
                }
            }
        }
    }

    public void zm_requestPermissions(String[] strArr, int i) {
        ZMActivityCompat.requestPermissionsCompat(this, strArr, i);
    }

    public int zm_checkSelfPermission(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return -1;
        }
        try {
            return checkPermission(str, Process.myPid(), Process.myUid());
        } catch (Throwable unused) {
            return -1;
        }
    }

    /* access modifiers changed from: protected */
    public int getFragmentIndex(Fragment fragment) {
        if (fragment == null) {
            return -1;
        }
        int incrementAndGet = this.mAutoIncrementIndex.incrementAndGet();
        this.mPendingPermissionFragments.put(incrementAndGet, new WeakReference(fragment));
        return incrementAndGet;
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void onCreate(Bundle bundle) {
        checkOrientation();
        setHasActivityCreated(true);
        this.mDestroyed = false;
        super.onCreate(bundle);
        Locale appLocale = LanguageUtil.getAppLocale(this);
        if (appLocale != null && !StringUtil.isEmptyOrNull(appLocale.getLanguage())) {
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = resources.getConfiguration();
            configuration.locale = appLocale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        initRetainedFragment();
        this.mTaskMgr = getRetainedFragment().mTaskMgr;
        sActivityStack.add(this);
        UIUtil.renderStatueBar(this, false, C4409R.color.zm_title_bar_dark_bg);
        this.mGestureDetector = new GestureDetector(this, this.mGestureListener);
    }

    public void onConfigurationChanged(Configuration configuration) {
        Locale appLocale = LanguageUtil.getAppLocale(this);
        if (appLocale != null && !StringUtil.isEmptyOrNull(appLocale.getLanguage())) {
            Resources resources = getResources();
            DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration2 = resources.getConfiguration();
            configuration2.locale = appLocale;
            resources.updateConfiguration(configuration2, displayMetrics);
        }
        super.onConfigurationChanged(configuration);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
        notifiyUserActivityOnUI();
        return super.dispatchGenericMotionEvent(motionEvent);
    }

    @SuppressLint({"RestrictedApi"})
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        notifiyUserActivityOnUI();
        return super.dispatchKeyEvent(keyEvent);
    }

    @SuppressLint({"RestrictedApi"})
    public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
        notifiyUserActivityOnUI();
        return super.dispatchKeyShortcutEvent(keyEvent);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        notifiyUserActivityOnUI();
        return super.dispatchPopulateAccessibilityEvent(accessibilityEvent);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!isMotionEventInDisableGestureFinishView(motionEvent) && !this.mDisableGestureFinish && this.mGestureDetector.onTouchEvent(motionEvent)) {
            return true;
        }
        notifiyUserActivityOnUI();
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (Exception unused) {
            return false;
        }
    }

    public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
        notifiyUserActivityOnUI();
        return super.dispatchTrackballEvent(motionEvent);
    }

    private void initRetainedFragment() {
        this.mRetainedFragment = getRetainedFragment();
        if (this.mRetainedFragment == null && !isFinishing()) {
            this.mRetainedFragment = new RetainedFragment();
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            RetainedFragment retainedFragment = this.mRetainedFragment;
            StringBuilder sb = new StringBuilder();
            sb.append(getClass().getName());
            sb.append(":");
            sb.append(RetainedFragment.class.getName());
            beginTransaction.add((Fragment) retainedFragment, sb.toString()).commit();
        }
    }

    private RetainedFragment getRetainedFragment() {
        RetainedFragment retainedFragment = this.mRetainedFragment;
        if (retainedFragment != null) {
            return retainedFragment;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getName());
        sb.append(":");
        sb.append(RetainedFragment.class.getName());
        return (RetainedFragment) supportFragmentManager.findFragmentByTag(sb.toString());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        this.mActive = false;
        if (sFrontActivity == this) {
            sFrontActivity = null;
        }
        this.mTaskMgr.onUIDestroy(this);
        if (isFinishing()) {
            this.mTaskMgr.destroy();
        }
        super.onDestroy();
        sActivityStack.remove(this);
        this.mDestroyed = true;
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        if (!isInMultiWindowMode()) {
            this.mActive = false;
            this.mTaskMgr.onPause(this);
        }
        super.onPause();
        if (!isInMultiWindowMode()) {
            performMoveToBackground();
        }
    }

    private void performMoveToBackground() {
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                ZMActivity frontActivity = ZMActivity.getFrontActivity();
                if (frontActivity == null || !frontActivity.isActive()) {
                    ZMActivity.this.notifyMoveToBackground();
                }
            }
        }, 500);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mActive = true;
        sFrontActivity = this;
        if (!isInMultiWindowMode()) {
            performMoveToFront();
        }
        UIUtil.setBadgeNumForEMUI(this, 0);
    }

    private void performMoveToFront() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (ZMActivity.this.isActive()) {
                    ZMActivity.this.notifyMoveToFront();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void notifyMoveToFront() {
        this.mTaskMgr.onResume(this);
        ForegroundTaskManager.getInstance().onActivityMoveToFront(this);
        IListener[] all = sGlobalActivityListenerList.getAll();
        for (IListener iListener : all) {
            ((GlobalActivityListener) iListener).onActivityMoveToFront(this);
        }
    }

    /* access modifiers changed from: private */
    public void notifyMoveToBackground() {
        IListener[] all = sGlobalActivityListenerList.getAll();
        for (IListener iListener : all) {
            ((GlobalActivityListener) iListener).onUIMoveToBackground();
        }
    }

    private void notifiyUserActivityOnUI() {
        IListener[] all = sGlobalActivityListenerList.getAll();
        for (IListener iListener : all) {
            ((GlobalActivityListener) iListener).onUserActivityOnUI();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        this.mActive = true;
        super.onStart();
        this.mTaskMgr.onStart(this);
        if (isInMultiWindowMode()) {
            sFrontActivity = this;
            performMoveToFront();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mActive = false;
        this.mTaskMgr.onStop(this);
        if (isInMultiWindowMode()) {
            performMoveToBackground();
        }
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        this.mActive = false;
        if (bundle != null) {
            bundle.putString("WORKAROUND_FOR_BUG_19917_KEY", "WORKAROUND_FOR_BUG_19917_VALUE");
        }
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
    }

    public void onBackPressed() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager == null || supportFragmentManager.getBackStackEntryCount() != 0) {
            try {
                super.onBackPressed();
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("Exception in ZMActivity.onBackPressed(). class=");
                sb.append(getClass().getName());
                throw new RuntimeException(sb.toString(), e);
            }
        } else {
            finish();
        }
    }

    public void setRequestedOrientation(int i) {
        if (!(VERSION.SDK_INT == 26 && windowIsTranslucent() && (i == 1 || i == 0)) && !DeviceInfoUtil.isARHeadset()) {
            super.setRequestedOrientation(i);
        }
    }

    public boolean windowIsTranslucent() {
        boolean z = false;
        try {
            Class cls = Class.forName("com.android.internal.R$styleable");
            Field field = cls.getField("Window");
            field.setAccessible(true);
            Object obj = field.get(this);
            if (obj == null) {
                return false;
            }
            TypedArray obtainStyledAttributes = obtainStyledAttributes((int[]) obj);
            Field field2 = cls.getField("Window_windowIsTranslucent");
            field2.setAccessible(true);
            z = obtainStyledAttributes.getBoolean(field2.getInt(field2.get(this)), false);
            obtainStyledAttributes.recycle();
            return z;
        } catch (Exception unused) {
        }
    }

    private void checkOrientation() {
        if (VERSION.SDK_INT == 26 && windowIsTranslucent()) {
            try {
                Field declaredField = Class.forName("android.app.Activity").getDeclaredField("mActivityInfo");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(this);
                if (obj != null) {
                    Field declaredField2 = obj.getClass().getDeclaredField("screenOrientation");
                    declaredField2.setAccessible(true);
                    declaredField2.setInt(obj, -1);
                }
            } catch (Exception unused) {
            }
        }
    }

    public static ZMActivity getActivity(String str) {
        Iterator it = sActivityStack.iterator();
        while (it.hasNext()) {
            ZMActivity zMActivity = (ZMActivity) it.next();
            if (zMActivity.getClass().getName().equals(str)) {
                return zMActivity;
            }
        }
        return null;
    }

    public static boolean isAppInForeground() {
        ZMActivity zMActivity = sFrontActivity;
        return zMActivity != null && zMActivity.isActive();
    }
}
