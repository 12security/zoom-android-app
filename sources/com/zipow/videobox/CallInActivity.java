package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.CallinFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class CallInActivity extends ZMActivity {
    public static void show(@Nullable ZMActivity zMActivity, int i) {
        if (zMActivity != null) {
            ActivityStartHelper.startActivityForResult((Activity) zMActivity, new Intent(zMActivity, CallInActivity.class), i);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
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
            getIntent();
            CallinFragment.showInActivity(this);
        }
    }
}
