package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.SignupFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class SignupActivity extends ZMActivity {
    private static final String BIRTH = "birth";

    public static void show(@NonNull ZMActivity zMActivity) {
        ActivityStartHelper.startActivityForeground(zMActivity, new Intent(zMActivity, SignupActivity.class));
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
    }

    public static void show(@NonNull ZMActivity zMActivity, String str) {
        Intent intent = new Intent(zMActivity, SignupActivity.class);
        intent.putExtra(BIRTH, str);
        ActivityStartHelper.startActivityForeground(zMActivity, intent);
        zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
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
            SignupFragment.showInActivity(this, getIntent().getStringExtra(BIRTH));
        }
    }
}
