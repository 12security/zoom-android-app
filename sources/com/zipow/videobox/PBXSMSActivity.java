package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.MMThreadsFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.sip.sms.PbxSmsFragment;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PBXSMSActivity extends ZMActivity {
    private static final String ARG_SESSION_ID = "sessionid";
    private static final String ARG_TO_NUMBERS = "toNumbers";
    private static final String TAG = "PBXSMSActivity";
    @NonNull
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, final long j) {
            if (i == 0) {
                PBXSMSActivity.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onWebLogin") {
                    public void run(@NonNull IUIElement iUIElement) {
                        ((PBXSMSActivity) iUIElement).onWebLogin(j);
                    }
                });
            }
        }
    };

    public static void showAsSession(@NonNull ZMActivity zMActivity, @NonNull String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Intent intent = new Intent(zMActivity, PBXSMSActivity.class);
            intent.addFlags(67108864);
            intent.putExtra(ARG_SESSION_ID, str);
            ActivityStartHelper.startActivityForeground(zMActivity, intent);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public static void showAsToNumbers(@NonNull ZMActivity zMActivity, @Nullable ArrayList<String> arrayList) {
        Intent intent = new Intent(zMActivity, PBXSMSActivity.class);
        intent.addFlags(67108864);
        if (arrayList == null) {
            arrayList = new ArrayList<>();
        }
        intent.putExtra(ARG_TO_NUMBERS, arrayList);
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
            VideoBoxApplication.getNonNullInstance().initPTMainboard();
            PTUI.getInstance().addPTUIListener(this.mPTUIListener);
            PTApp.getInstance().autoSignin();
        }
        if (UIMgr.isLargeMode(this) && !UIMgr.isDualPaneSupportedInPortraitMode(this)) {
            setRequestedOrientation(0);
        } else if (UIUtil.isTablet(this)) {
            setRequestedOrientation(4);
        } else if (PTApp.getInstance().hasMessenger()) {
            setRequestedOrientation(1);
        }
        if (bundle == null) {
            Intent intent = getIntent();
            String stringExtra = intent.getStringExtra(ARG_SESSION_ID);
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra(ARG_TO_NUMBERS);
            if (!StringUtil.isEmptyOrNull(stringExtra)) {
                PbxSmsFragment.showAsSession(this, stringExtra);
            } else {
                PbxSmsFragment.showAsToNumbers(this, arrayList);
            }
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
        MMThreadsFragment findMMCommentsFragment = MMThreadsFragment.findMMCommentsFragment(getSupportFragmentManager());
        if (findMMCommentsFragment != null) {
            findMMCommentsFragment.onWebLogin();
        }
    }

    public void onBackPressed() {
        PbxSmsFragment findPBXSMSFragment = PbxSmsFragment.findPBXSMSFragment(getSupportFragmentManager());
        if (findPBXSMSFragment == null || !findPBXSMSFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(@Nullable Intent intent) {
        super.onNewIntent(intent);
        Intent intent2 = getIntent();
        if (intent != null && intent2 != null) {
            String stringExtra = intent2.getStringExtra(ARG_SESSION_ID);
            if (stringExtra == null) {
                stringExtra = intent2.getStringExtra(ARG_SESSION_ID);
            }
            String stringExtra2 = intent.getStringExtra(ARG_SESSION_ID);
            if (stringExtra2 == null) {
                stringExtra2 = intent.getStringExtra(ARG_SESSION_ID);
            }
            if (!StringUtil.isSameString(stringExtra, stringExtra2)) {
                Intent intent3 = new Intent(this, PBXSMSActivity.class);
                intent3.putExtra(ARG_SESSION_ID, stringExtra2);
                ActivityStartHelper.startActivityForeground(this, intent3);
                overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }
}
