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
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMChatActivity extends ZMActivity {
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_GROUP_ID = "groupId";
    private static final String ARG_IS_GROUP = "isGroup";
    private static final String ARG_SAVE_OPEN_TIME = "saveOpenTime";
    private static final String ARG_SEND_INTENT = "sendIntent";
    private static final String TAG = "MMChatActivity";
    @NonNull
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, final long j) {
            if (i == 0) {
                MMChatActivity.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onWebLogin") {
                    public void run(@NonNull IUIElement iUIElement) {
                        ((MMChatActivity) iUIElement).onWebLogin(j);
                    }
                });
            }
        }
    };

    private static boolean isIMDisable() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return zoomMessenger == null || zoomMessenger.imChatGetOption() == 2;
    }

    public static boolean showAsOneToOneChatWithPhoneNumber(@Nullable ZMActivity zMActivity, @Nullable IMAddrBookItem iMAddrBookItem, boolean z, String str, boolean z2) {
        if (zMActivity == null || iMAddrBookItem == null || PTApp.getInstance().getZoomMessenger() == null) {
            return false;
        }
        String jid = iMAddrBookItem.getJid();
        if (StringUtil.isEmptyOrNull(jid)) {
            return false;
        }
        if (z2) {
            zMActivity.finish();
        }
        IMAddrBookItem iMAddrBookItem2 = new IMAddrBookItem();
        iMAddrBookItem2.setContactId(iMAddrBookItem.getContactId());
        iMAddrBookItem2.setScreenName(iMAddrBookItem.getScreenName());
        iMAddrBookItem2.setSortKey(iMAddrBookItem.getSortKey());
        iMAddrBookItem2.setIsZoomUser(iMAddrBookItem.getIsZoomUser());
        iMAddrBookItem2.addPhoneNumber(str, str);
        iMAddrBookItem2.setJid(jid);
        iMAddrBookItem2.setIsZoomUser(true);
        showAsOneToOneChat(zMActivity, iMAddrBookItem2, jid, z);
        return true;
    }

    public static void showAsOneToOneChat(@Nullable ZMActivity zMActivity, @Nullable IMAddrBookItem iMAddrBookItem, @Nullable String str) {
        showAsOneToOneChat(zMActivity, iMAddrBookItem, str, false);
    }

    public static void showAsOneToOneChat(@Nullable ZMActivity zMActivity, @Nullable IMAddrBookItem iMAddrBookItem, @Nullable String str, boolean z) {
        if (zMActivity != null && iMAddrBookItem != null && str != null && !isIMDisable() && iMAddrBookItem.getAccountStatus() != 2) {
            Intent intent = new Intent(zMActivity, MMChatActivity.class);
            intent.addFlags(536870912);
            intent.putExtra(ARG_IS_GROUP, false);
            intent.putExtra(ARG_CONTACT, iMAddrBookItem);
            intent.putExtra(ARG_BUDDY_ID, str);
            intent.putExtra(ARG_SAVE_OPEN_TIME, z);
            ActivityStartHelper.startActivityForeground(zMActivity, intent);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            forceRefreshVcard(str);
        }
    }

    public static void showAsOneToOneChat(ZMActivity zMActivity, ZoomBuddy zoomBuddy) {
        showAsOneToOneChat(zMActivity, zoomBuddy, (Intent) null);
    }

    public static void showAsOneToOneChat(@Nullable ZMActivity zMActivity, @Nullable ZoomBuddy zoomBuddy, Intent intent) {
        if (zMActivity != null && zoomBuddy != null && !isIMDisable() && zoomBuddy.getAccountStatus() != 2) {
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
            String jid = zoomBuddy.getJid();
            Intent intent2 = new Intent(zMActivity, MMChatActivity.class);
            intent2.addFlags(536870912);
            intent2.putExtra(ARG_IS_GROUP, false);
            intent2.putExtra(ARG_CONTACT, fromZoomBuddy);
            intent2.putExtra(ARG_BUDDY_ID, jid);
            intent2.putExtra(ARG_SEND_INTENT, intent);
            ActivityStartHelper.startActivityForeground(zMActivity, intent2);
            zMActivity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            forceRefreshVcard(jid);
        }
    }

    private static void forceRefreshVcard(String str) {
        forceRefreshVcard(str, true);
    }

    private static void forceRefreshVcard(String str, boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.refreshBuddyVCard(str, true);
        }
    }

    public static void showAsGroupChat(@NonNull ZMActivity zMActivity, String str) {
        showAsGroupChat(zMActivity, str, null);
    }

    public static void showAsGroupChat(@NonNull ZMActivity zMActivity, String str, Intent intent) {
        if (!isIMDisable()) {
            Intent intent2 = new Intent(zMActivity, MMChatActivity.class);
            intent2.addFlags(536870912);
            intent2.putExtra(ARG_IS_GROUP, true);
            intent2.putExtra(ARG_GROUP_ID, str);
            intent2.putExtra(ARG_SEND_INTENT, intent);
            ActivityStartHelper.startActivityForeground(zMActivity, intent2);
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
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(getApplicationContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
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
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) intent.getSerializableExtra(ARG_CONTACT);
            String stringExtra = intent.getStringExtra(ARG_BUDDY_ID);
            String stringExtra2 = intent.getStringExtra(ARG_GROUP_ID);
            boolean booleanExtra = intent.getBooleanExtra(ARG_IS_GROUP, false);
            boolean booleanExtra2 = intent.getBooleanExtra(ARG_SAVE_OPEN_TIME, false);
            Intent intent2 = (Intent) intent.getParcelableExtra(ARG_SEND_INTENT);
            if (booleanExtra) {
                MMThreadsFragment.showAsGroupChatInActivity(this, stringExtra2, booleanExtra2, intent2);
            } else {
                MMThreadsFragment.showAsOneToOneInActivity(this, iMAddrBookItem, stringExtra, booleanExtra2, intent2);
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
        MMThreadsFragment findMMCommentsFragment = MMThreadsFragment.findMMCommentsFragment(getSupportFragmentManager());
        if (findMMCommentsFragment == null || !findMMCommentsFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(@Nullable Intent intent) {
        super.onNewIntent(intent);
        Intent intent2 = getIntent();
        if (intent != null && intent2 != null) {
            String stringExtra = intent2.getStringExtra(ARG_GROUP_ID);
            if (stringExtra == null) {
                stringExtra = intent2.getStringExtra(ARG_BUDDY_ID);
            }
            String stringExtra2 = intent.getStringExtra(ARG_GROUP_ID);
            if (stringExtra2 == null) {
                stringExtra2 = intent.getStringExtra(ARG_BUDDY_ID);
            }
            if (!StringUtil.isSameString(stringExtra, stringExtra2)) {
                IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) intent.getSerializableExtra(ARG_CONTACT);
                boolean booleanExtra = intent.getBooleanExtra(ARG_IS_GROUP, false);
                String stringExtra3 = intent.getStringExtra(ARG_BUDDY_ID);
                String stringExtra4 = intent.getStringExtra(ARG_GROUP_ID);
                Intent intent3 = new Intent(this, MMChatActivity.class);
                intent3.putExtra(ARG_IS_GROUP, booleanExtra);
                intent3.putExtra(ARG_CONTACT, iMAddrBookItem);
                intent3.putExtra(ARG_BUDDY_ID, stringExtra3);
                intent3.putExtra(ARG_GROUP_ID, stringExtra4);
                ActivityStartHelper.startActivityForeground(this, intent3);
                overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }
}
