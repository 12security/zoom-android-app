package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.BuddyInviteFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class BuddyInviteActivity extends ZMActivity {
    public static final String ARG_EMAILS = "emails";

    public static void show(@NonNull ZMActivity zMActivity, int i, @Nullable String str) {
        Intent intent = new Intent(zMActivity, BuddyInviteActivity.class);
        if (str != null) {
            intent.putExtra("emails", str);
        }
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
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
            BuddyInviteFragment.show(this, getIntent().getStringExtra("emails"));
        }
    }
}
