package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.MyProfileFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.util.ActivityStartHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.videomeetings.C4558R;

public class MyProfileActivity extends ZMActivity {
    @NonNull
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, final long j) {
            if (i == 0) {
                MyProfileActivity.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onWebLogin") {
                    public void run(@NonNull IUIElement iUIElement) {
                        ((MyProfileActivity) iUIElement).onWebLogin(j);
                    }
                });
            }
        }
    };

    public static void show(@NonNull ZMActivity zMActivity, int i) {
        ActivityStartHelper.startActivityForResult((Activity) zMActivity, new Intent(zMActivity, MyProfileActivity.class), i);
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
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(getApplicationContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
            PTUI.getInstance().addPTUIListener(this.mPTUIListener);
            PTApp.getInstance().autoSignin();
        }
        if (bundle == null) {
            MyProfileFragment.showInActivity(this);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        PTUI.getInstance().removePTUIListener(this.mPTUIListener);
    }

    /* access modifiers changed from: private */
    public void onWebLogin(long j) {
        if (j != 0) {
            finish();
            return;
        }
        MyProfileFragment findMyProfileFragment = MyProfileFragment.findMyProfileFragment(getSupportFragmentManager());
        if (findMyProfileFragment != null) {
            findMyProfileFragment.onWebLogin();
        }
    }
}
