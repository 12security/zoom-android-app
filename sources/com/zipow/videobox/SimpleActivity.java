package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.androidlib.widget.ZMIgnoreKeyboardLayout;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector;
import p021us.zoom.androidlib.widget.ZMKeyboardDetector.KeyboardListener;
import p021us.zoom.androidlib.widget.ZMTipLayer;
import p021us.zoom.videomeetings.C4558R;

public class SimpleActivity extends ZMActivity implements KeyboardListener {
    public static final int ALPHA_ANIMA = 2;
    private static final String ARG_FRAGMENT_ANIMA_TYPE = "animType";
    private static final String ARG_FRAGMENT_ARGUMENTS = "fragmentArguments";
    private static final String ARG_FRAGMENT_CLASS = "fragmentClass";
    private static final String ARG_LAYOUT_IGNORE_KEYBORAD = "layoutIgnoreKeyboard";
    public static final int BOTTOM_TOP_ANIMA = 1;
    public static final int LEFT_RIGHT_ANIMA = 0;
    public static final int PROCESS_AUTO = 0;
    public static final int PROCESS_CONF = 2;
    public static final int PROCESS_PT = 1;
    private static final String TAG = "SimpleActivity";
    private int animType;
    private ZMIgnoreKeyboardLayout mFragmentContent;
    @Nullable
    private String mFragmentTag = null;
    private ZMKeyboardDetector mKeyboardDetector;
    private ZMTipLayer mTipLayer;

    public interface ExtListener {
        boolean onBackPressed();

        void onKeyboardClosed();

        void onKeyboardOpen();

        boolean onSearchRequested();

        boolean onTipLayerTouched();
    }

    @NonNull
    private static Class<?> getSimpleActivityClass(int i) {
        if (i == 1) {
            return SimpleActivity.class;
        }
        if (i == 2) {
            return SimpleInMeetingActivity.class;
        }
        if (VideoBoxApplication.getInstance().isConfApp()) {
            return SimpleInMeetingActivity.class;
        }
        return SimpleActivity.class;
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, Bundle bundle, int i) {
        show(zMActivity, str, bundle, i, 0);
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, Bundle bundle, int i, int i2) {
        Intent intent = new Intent(zMActivity, getSimpleActivityClass(i2));
        intent.putExtra(ARG_FRAGMENT_CLASS, str);
        intent.putExtra(ARG_FRAGMENT_ARGUMENTS, bundle);
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, Bundle bundle, int i, boolean z) {
        show(zMActivity, str, bundle, i, z, 0);
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, Bundle bundle, int i, boolean z, int i2) {
        show(zMActivity, str, bundle, i, z, false, i2);
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, Bundle bundle, int i, boolean z, boolean z2) {
        show(zMActivity, str, bundle, i, z, z2, 0);
    }

    public static void show(@NonNull ZMActivity zMActivity, String str, Bundle bundle, int i, boolean z, boolean z2, int i2) {
        Intent intent = new Intent(zMActivity, getSimpleActivityClass(i2));
        intent.putExtra(ARG_FRAGMENT_CLASS, str);
        intent.putExtra(ARG_FRAGMENT_ARGUMENTS, bundle);
        intent.putExtra(ARG_LAYOUT_IGNORE_KEYBORAD, z2);
        if (z) {
            intent.putExtra(ARG_FRAGMENT_ANIMA_TYPE, 1);
        } else {
            intent.putExtra(ARG_FRAGMENT_ANIMA_TYPE, 0);
        }
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
        if (z) {
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
        } else {
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public static void show(Fragment fragment, String str, Bundle bundle, int i, boolean z) {
        show(fragment, str, bundle, i, z, 0);
    }

    public static void show(Fragment fragment, String str, Bundle bundle, int i, boolean z, int i2) {
        show(fragment, str, bundle, i, z, false, i2);
    }

    public static void show(Fragment fragment, String str, Bundle bundle, int i, boolean z, boolean z2) {
        show(fragment, str, bundle, i, z, z2, 0);
    }

    public static void show(@Nullable Fragment fragment, String str, Bundle bundle, int i, boolean z, boolean z2, int i2) {
        if (fragment != null) {
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent(zMActivity, getSimpleActivityClass(i2));
                intent.putExtra(ARG_FRAGMENT_CLASS, str);
                intent.putExtra(ARG_FRAGMENT_ARGUMENTS, bundle);
                intent.putExtra(ARG_LAYOUT_IGNORE_KEYBORAD, z2);
                if (z) {
                    intent.putExtra(ARG_FRAGMENT_ANIMA_TYPE, 1);
                } else {
                    intent.putExtra(ARG_FRAGMENT_ANIMA_TYPE, 0);
                }
                ActivityStartHelper.startActivityForResult(fragment, intent, i);
                if (z) {
                    zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
                } else {
                    zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
                }
            }
        }
    }

    public static void show(Fragment fragment, String str, Bundle bundle, int i, int i2) {
        show(fragment, str, bundle, i, i2, 0);
    }

    public static void show(Fragment fragment, String str, Bundle bundle, int i) {
        show(fragment, str, bundle, i, 0);
    }

    public static void show(@Nullable Fragment fragment, String str, Bundle bundle, int i, int i2, int i3) {
        if (fragment != null) {
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                Intent intent = new Intent(zMActivity, getSimpleActivityClass(i3));
                intent.putExtra(ARG_FRAGMENT_CLASS, str);
                intent.putExtra(ARG_FRAGMENT_ARGUMENTS, bundle);
                intent.putExtra(ARG_FRAGMENT_ANIMA_TYPE, i2);
                ActivityStartHelper.startActivityForResult(fragment, intent, i);
                switch (i2) {
                    case 1:
                        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_bottom, C4558R.anim.zm_fade_out);
                        break;
                    case 2:
                        zMActivity.overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_fade_out);
                        break;
                    default:
                        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
                        break;
                }
            }
        }
    }

    public void finish() {
        super.finish();
        int i = this.animType;
        if (i == 0) {
            overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
        } else if (i == 2) {
            overridePendingTransition(C4558R.anim.zm_fade_in, C4558R.anim.zm_fade_out);
        } else {
            overridePendingTransition(0, C4558R.anim.zm_slide_out_bottom);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        setContentView(C4558R.layout.zm_simple_activity);
        this.mTipLayer = (ZMTipLayer) findViewById(C4558R.C4560id.tipLayer);
        this.mKeyboardDetector = (ZMKeyboardDetector) findViewById(C4558R.C4560id.keyboardDetector);
        this.mFragmentContent = (ZMIgnoreKeyboardLayout) findViewById(C4558R.C4560id.fragmentContent);
        this.mKeyboardDetector.setKeyboardListener(this);
        initTipLayer();
        Intent intent = getIntent();
        if (intent != null) {
            this.animType = intent.getIntExtra(ARG_FRAGMENT_ANIMA_TYPE, 0);
            if (bundle == null) {
                String stringExtra = intent.getStringExtra(ARG_FRAGMENT_CLASS);
                Bundle bundleExtra = intent.getBundleExtra(ARG_FRAGMENT_ARGUMENTS);
                try {
                    Class cls = Class.forName(stringExtra);
                    Fragment fragment = (Fragment) cls.newInstance();
                    if (bundleExtra != null) {
                        fragment.setArguments(bundleExtra);
                    }
                    this.mFragmentTag = cls.getName();
                    getSupportFragmentManager().beginTransaction().add(C4558R.C4560id.fragmentContent, fragment, this.mFragmentTag).commit();
                } catch (Exception e) {
                    ZMLog.m281e(TAG, e, "create SimpleActivity failed. fragmentClass=%s", stringExtra);
                }
            }
            if (this.animType == 1) {
                disableFinishActivityByGesture(true);
            }
            this.mFragmentContent.setIgnoreKeyboardOpen(intent.getBooleanExtra(ARG_LAYOUT_IGNORE_KEYBORAD, false));
        }
    }

    private void initTipLayer() {
        ZMTipLayer zMTipLayer = this.mTipLayer;
        if (zMTipLayer != null) {
            zMTipLayer.setOnTouchListener(new OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    return SimpleActivity.this.onTipLayerTouched();
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(SimpleActivity.class.getName());
            sb.append(".mFragmentTag");
            bundle.putString(sb.toString(), this.mFragmentTag);
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(@Nullable Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(SimpleActivity.class.getName());
            sb.append(".mFragmentTag");
            this.mFragmentTag = bundle.getString(sb.toString());
        }
    }

    public boolean onSearchRequested() {
        Fragment findMainFragment = findMainFragment();
        if (!(findMainFragment instanceof ExtListener) || !((ExtListener) findMainFragment).onSearchRequested()) {
            return super.onSearchRequested();
        }
        return true;
    }

    public void onBackPressed() {
        Fragment findMainFragment = findMainFragment();
        if (!(findMainFragment instanceof ExtListener) || !((ExtListener) findMainFragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Nullable
    public Fragment findMainFragment() {
        if (this.mFragmentTag == null) {
            return null;
        }
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return null;
        }
        return supportFragmentManager.findFragmentByTag(this.mFragmentTag);
    }

    public boolean isKeyboardOpen() {
        ZMKeyboardDetector zMKeyboardDetector = this.mKeyboardDetector;
        if (zMKeyboardDetector == null) {
            return false;
        }
        return zMKeyboardDetector.isKeyboardOpen();
    }

    public ZMKeyboardDetector getKeyboardDetector() {
        return this.mKeyboardDetector;
    }

    public void onKeyboardOpen() {
        Fragment findMainFragment = findMainFragment();
        if (findMainFragment instanceof ExtListener) {
            ((ExtListener) findMainFragment).onKeyboardOpen();
        }
    }

    public void onKeyboardClosed() {
        Fragment findMainFragment = findMainFragment();
        if (findMainFragment instanceof ExtListener) {
            ((ExtListener) findMainFragment).onKeyboardClosed();
        }
    }

    /* access modifiers changed from: protected */
    public boolean onTipLayerTouched() {
        Fragment findMainFragment = findMainFragment();
        if (findMainFragment instanceof ExtListener) {
            return ((ExtListener) findMainFragment).onTipLayerTouched();
        }
        return false;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("<");
        sb.append(this.mFragmentTag);
        sb.append(">");
        return sb.toString();
    }
}
