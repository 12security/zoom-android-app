package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.fragment.PListFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.view.NormalMessageTip;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.videomeetings.C4558R;

public class PListActivity extends ZMActivity {
    @NonNull
    private Handler mHandler = new Handler();

    public static void show(@Nullable ZMActivity zMActivity, int i) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, PListActivity.class);
            intent.setFlags(131072);
            zMActivity.startActivityForResult(intent, i);
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
            getSupportFragmentManager().beginTransaction().add(16908290, new PListFragment(), PListFragment.class.getName()).commit();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        this.mHandler.removeCallbacksAndMessages(null);
        if (isFinishing()) {
            finishActivity(1000);
        }
    }

    public boolean onSearchRequested() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return super.onSearchRequested();
        }
        PListFragment pListFragment = PListFragment.getPListFragment(supportFragmentManager);
        if (pListFragment != null) {
            return pListFragment.onSearchRequested();
        }
        return super.onSearchRequested();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1001 && i2 == -1) {
            onSentInvitationDone(intent);
        }
    }

    private void onSentInvitationDone(@Nullable Intent intent) {
        if (intent != null) {
            final int intExtra = intent.getIntExtra("invitations_count", 0);
            if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
                this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        PListActivity.this.showTipInvitationsSent(intExtra);
                    }
                }, 1000);
            } else {
                showTipInvitationsSent(intExtra);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showTipInvitationsSent(int i) {
        NormalMessageTip.show(getSupportFragmentManager(), "tip_invitations_sent", null, getResources().getQuantityString(C4558R.plurals.zm_msg_invitations_sent, i, new Object[]{Integer.valueOf(i)}), C4558R.C4559drawable.zm_ic_tick, 0, 0, 3000);
    }
}
