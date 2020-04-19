package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.fragment.AddrBookItemDetailsFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class AddrBookItemDetailsActivity extends ZMActivity {
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_IS_FROM_ONE_TO_ONE_CHAT = "isFromOneToOneChat";
    private static final String ARG_NEED_SAVE_OPEN_TIME = "needSaveOpenTime";
    private static long timeStamp;

    private static boolean checkBesy() {
        long currentTimeMillis = System.currentTimeMillis();
        long j = currentTimeMillis - timeStamp;
        if (j >= 0 && j < 600) {
            return true;
        }
        timeStamp = currentTimeMillis;
        return false;
    }

    public static void show(@NonNull ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, int i) {
        show(zMActivity, iMAddrBookItem, false, i);
    }

    public static void show(@NonNull ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, int i, boolean z) {
        show(zMActivity, iMAddrBookItem, false, z, i);
    }

    public static void show(@NonNull ZMActivity zMActivity, @Nullable IMAddrBookItem iMAddrBookItem, boolean z, int i) {
        show(zMActivity, iMAddrBookItem, z, false, i);
    }

    public static void show(@NonNull ZMActivity zMActivity, @Nullable IMAddrBookItem iMAddrBookItem, boolean z, boolean z2, int i) {
        if ((iMAddrBookItem == null || iMAddrBookItem.getAccountStatus() != 2) && !checkBesy()) {
            Intent intent = new Intent(zMActivity, AddrBookItemDetailsActivity.class);
            intent.putExtra(ARG_CONTACT, iMAddrBookItem);
            intent.putExtra(ARG_IS_FROM_ONE_TO_ONE_CHAT, z);
            intent.putExtra(ARG_NEED_SAVE_OPEN_TIME, z2);
            ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public static void show(@NonNull Fragment fragment, IMAddrBookItem iMAddrBookItem, int i) {
        show(fragment, iMAddrBookItem, false, i);
    }

    public static void show(Fragment fragment, IMAddrBookItem iMAddrBookItem, boolean z, int i) {
        FragmentActivity activity = fragment.getActivity();
        if (activity != null && !checkBesy()) {
            Intent intent = new Intent(activity, AddrBookItemDetailsActivity.class);
            intent.putExtra(ARG_CONTACT, iMAddrBookItem);
            intent.putExtra(ARG_IS_FROM_ONE_TO_ONE_CHAT, z);
            ActivityStartHelper.startActivityForResult(fragment, intent, i);
            activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (UIMgr.isLargeMode(this) && !UIMgr.isDualPaneSupportedInPortraitMode(this)) {
            setRequestedOrientation(0);
        } else if (UIUtil.isTablet(this)) {
            setRequestedOrientation(4);
        } else if (PTApp.getInstance().hasMessenger()) {
            setRequestedOrientation(1);
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            finish();
            return;
        }
        if (bundle == null) {
            Intent intent = getIntent();
            AddrBookItemDetailsFragment.showInActivity(this, intent.getBooleanExtra(ARG_NEED_SAVE_OPEN_TIME, false), (IMAddrBookItem) intent.getSerializableExtra(ARG_CONTACT), intent.getBooleanExtra(ARG_IS_FROM_ONE_TO_ONE_CHAT, false));
        }
    }
}
