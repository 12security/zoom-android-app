package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.dialog.conf.ZmInMeetingSettingDialog;
import com.zipow.videobox.mainboard.Mainboard;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class InMeetingSettingsActivity extends ZMActivity {
    @NonNull
    private Handler mHandler = new Handler();

    public static void show(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, InMeetingSettingsActivity.class);
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
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        Window window = getWindow();
        if (window != null) {
            window.setSoftInputMode(19);
        }
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().add(16908290, ZmInMeetingSettingDialog.newInstance(), ZmInMeetingSettingDialog.class.getName()).commit();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
    }

    public void onClickBack() {
        finish();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return false;
        }
        onClickBack();
        return super.onKeyDown(i, keyEvent);
    }
}
