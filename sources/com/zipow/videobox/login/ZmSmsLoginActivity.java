package com.zipow.videobox.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.IAgeGatingCallback;
import com.zipow.videobox.util.UIMgr;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZmSmsLoginActivity extends ZMActivity implements IAgeGatingCallback {
    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, ZmSmsLoginActivity.class);
            intent.setFlags(131072);
            zMActivity.startActivity(intent);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        disableFinishActivityByGesture(true);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        if (UIMgr.isLargeMode(this) && !UIMgr.isDualPaneSupportedInPortraitMode(this)) {
            setRequestedOrientation(0);
        } else if (!UIMgr.isLargeMode(this) && UIUtil.getDisplayMinWidthInDip(this) < 500.0f) {
            setRequestedOrientation(1);
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().add(16908290, new ZmSmsLoginFragment(), ZmSmsLoginFragment.class.getName()).commit();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onCancelAgeGating() {
        dismissWaitDialog();
    }

    public void onConfirmAgeFailed(int i) {
        dismissWaitDialog();
    }

    private void dismissWaitDialog() {
        Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(ZmSmsLoginFragment.class.getName());
        if (findFragmentByTag != null) {
            ((ZmSmsLoginFragment) findFragmentByTag).dismissWaiting();
        }
    }
}
