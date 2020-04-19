package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.fragment.AddrBookSetNumberFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class AddrBookSetNumberActivity extends ZMActivity {
    public static final int REQUEST_VERIFY_PHONE_NUMBER = 100;

    public static void show(@Nullable ZMActivity zMActivity, int i) {
        if (zMActivity != null) {
            ActivityStartHelper.startActivityForResult((Activity) zMActivity, new Intent(zMActivity, AddrBookSetNumberActivity.class), i);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public static void show(@Nullable Fragment fragment, int i) {
        if (fragment != null) {
            ZMActivity zMActivity = (ZMActivity) fragment.getActivity();
            if (zMActivity != null) {
                ActivityStartHelper.startActivityForResult(fragment, new Intent(zMActivity, AddrBookSetNumberActivity.class), i);
                zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            AddrBookSetNumberFragment.showInActivity(this);
        }
    }

    public void onResume() {
        super.onResume();
        if (!PTApp.getInstance().isWebSignedOn()) {
            setResult(0);
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            setResult(-1, intent);
            finish();
        }
    }
}
