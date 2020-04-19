package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.ConfChatFragmentOld;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatActivityOld extends ZMActivity {
    public static final String ARG_USERID = "userId";

    public static void show(@Nullable ZMActivity zMActivity, long j, int i) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, ConfChatActivityOld.class);
            intent.setFlags(131072);
            intent.putExtra("userId", j);
            ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
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
            ConfChatFragmentOld.showActivity(this, getIntent().getLongExtra("userId", 0));
        }
    }

    public void onDestroy() {
        if (isFinishing()) {
            UIUtil.closeSoftKeyboard(this, getWindow().getDecorView());
        }
        super.onDestroy();
    }
}
