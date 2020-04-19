package com.zipow.videobox;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.fragment.MMChatInfoFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class MMChatInfoActivity extends ZMActivity {
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_GROUP_ID = "groupId";
    private static final String ARG_IS_GROUP = "isGroup";
    private static final int REQUEST_CHAT_INFO = 102;
    public static final int REQUEST_SELECT_CONTACTS = 100;
    public static final String RESULT_ARG_IS_HISTORY_CLEARED = "isHistoryCleared";
    public static final String RESULT_ARG_IS_QUIT_GROUP = "isQuitGroup";
    private boolean mIsHistoryCleared = false;

    public static void showAsOneToOneChat(@Nullable ZMActivity zMActivity, IMAddrBookItem iMAddrBookItem, String str, int i) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, MMChatInfoActivity.class);
            intent.putExtra(ARG_IS_GROUP, false);
            intent.putExtra(ARG_CONTACT, iMAddrBookItem);
            intent.putExtra(ARG_BUDDY_ID, str);
            ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
        }
    }

    public static void showAsGroupChat(@Nullable ZMActivity zMActivity, String str, int i) {
        if (zMActivity != null) {
            Intent intent = new Intent(zMActivity, MMChatInfoActivity.class);
            intent.putExtra(ARG_IS_GROUP, true);
            intent.putExtra(ARG_GROUP_ID, str);
            ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, i);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            freshGroupInfo(str);
        }
    }

    public static void showAsGroupChat(@Nullable Fragment fragment, String str, int i) {
        if (fragment != null) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                Intent intent = new Intent(activity, MMChatInfoActivity.class);
                intent.putExtra(ARG_IS_GROUP, true);
                intent.putExtra(ARG_GROUP_ID, str);
                ActivityStartHelper.startActivityForResult(fragment, intent, i);
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
                freshGroupInfo(str);
            }
        }
    }

    private static void freshGroupInfo(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.refreshGroupInfo(str);
        }
    }

    public void finish() {
        finish(false);
    }

    private void finish(boolean z) {
        if (!z) {
            Intent intent = new Intent();
            intent.putExtra(RESULT_ARG_IS_HISTORY_CLEARED, this.mIsHistoryCleared);
            setResult(-1, intent);
        }
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
            Intent intent = getIntent();
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) intent.getSerializableExtra(ARG_CONTACT);
            String stringExtra = intent.getStringExtra(ARG_BUDDY_ID);
            String stringExtra2 = intent.getStringExtra(ARG_GROUP_ID);
            if (intent.getBooleanExtra(ARG_IS_GROUP, false)) {
                MMChatInfoFragment.showAsGroupChatInActivity(this, stringExtra2);
            } else {
                MMChatInfoFragment.showAsOneToOneInActivity(this, iMAddrBookItem, stringExtra);
            }
        }
    }

    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(@Nullable Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            bundle.putBoolean(RESULT_ARG_IS_HISTORY_CLEARED, this.mIsHistoryCleared);
        }
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(@Nullable Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        if (bundle != null) {
            this.mIsHistoryCleared = bundle.getBoolean(RESULT_ARG_IS_HISTORY_CLEARED);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100) {
            if (i2 == -1 && intent != null) {
                ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
                if (arrayList != null) {
                    MMChatInfoFragment findMMChatInfoFragment = MMChatInfoFragment.findMMChatInfoFragment(getSupportFragmentManager());
                    if (findMMChatInfoFragment != null) {
                        findMMChatInfoFragment.onContactsSelected(arrayList);
                    }
                }
            }
        } else if (i == 102 && i2 == -1 && intent != null && intent.getBooleanExtra(RESULT_ARG_IS_QUIT_GROUP, false)) {
            onQuitGroup();
        }
    }

    public void onQuitGroup() {
        Intent intent = new Intent();
        intent.putExtra(RESULT_ARG_IS_QUIT_GROUP, true);
        setResult(-1, intent);
        finish(true);
    }

    public void onHistoryCleared() {
        this.mIsHistoryCleared = true;
    }
}
