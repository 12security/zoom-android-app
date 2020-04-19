package com.zipow.videobox;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.fragment.MMThreadsFragment;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.IMProtos.MessageInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.p014mm.MMCommentsFragment;
import java.io.Serializable;
import java.util.ArrayList;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMCommentActivity extends ZMActivity {
    private static final String ARG_BUDDY_ID = "buddyId";
    private static final String ARG_CONTACT = "contact";
    private static final String ARG_GROUP_ID = "groupId";
    private static final String ARG_IS_GROUP = "isGroup";
    private static final String ARG_SEND_INTENT = "sendIntent";
    private static final String ARG_THREAD_ID = "threadId";
    private static final String ARG_THREAD_SVR = "threadSvr";
    private static final String ARG_THREAD_UNREAD_INFO = "ThreadUnreadInfo";
    public static final String RESULT_ARG_ANCHOR_MSG = "anchorMsg";
    public static final String RESULT_ARG_MARK_UNREAD_MESSAGES = "UNREADMSGS";
    public static final String RESULT_ARG_THREAD_ID = "threadId";
    private static final String TAG = "MMCommentActivity";
    private IPTUIListener mPTUIListener = new SimplePTUIListener() {
        public void onPTAppEvent(int i, final long j) {
            if (i == 0) {
                MMCommentActivity.this.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onWebLogin") {
                    public void run(IUIElement iUIElement) {
                        ((MMCommentActivity) iUIElement).onWebLogin(j);
                    }
                });
            }
        }
    };

    public static class ThreadUnreadInfo implements Serializable {
        public boolean autoOpenKeyboard;
        public ArrayList<String> mAtAllMsgIds;
        public ArrayList<String> mAtMeMsgIds;
        public ArrayList<String> mAtMsgIds;
        public ArrayList<MessageInfo> mMarkUnreadMsgs;
        public long readTime;
        public int unreadCount;
    }

    private static boolean isIMDisable() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        return zoomMessenger == null || zoomMessenger.imChatGetOption() == 2;
    }

    public static void showAsOneToOneChat(@NonNull Fragment fragment, IMAddrBookItem iMAddrBookItem, String str, String str2, ThreadUnreadInfo threadUnreadInfo, int i) {
        showAsOneToOneChat(fragment, iMAddrBookItem, str, str2, 0, threadUnreadInfo, i);
    }

    public static void showAsOneToOneChat(@NonNull Fragment fragment, IMAddrBookItem iMAddrBookItem, String str, String str2, long j, ThreadUnreadInfo threadUnreadInfo, int i) {
        if (iMAddrBookItem != null && str != null && ((!TextUtils.isEmpty(str2) || j != 0) && !isIMDisable() && iMAddrBookItem.getAccountStatus() != 2)) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                Intent intent = new Intent(activity, MMCommentActivity.class);
                intent.addFlags(536870912);
                intent.putExtra(ARG_IS_GROUP, false);
                intent.putExtra(ARG_CONTACT, iMAddrBookItem);
                intent.putExtra(ARG_BUDDY_ID, str);
                intent.putExtra("threadId", str2);
                intent.putExtra(ARG_THREAD_SVR, j);
                if (threadUnreadInfo != null) {
                    intent.putExtra(ARG_THREAD_UNREAD_INFO, threadUnreadInfo);
                }
                fragment.startActivityForResult(intent, i);
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
                forceRefreshVcard(str);
            }
        }
    }

    public static void showAsOneToOneChat(@NonNull Fragment fragment, ZoomBuddy zoomBuddy, Intent intent, String str, ThreadUnreadInfo threadUnreadInfo, int i) {
        showAsOneToOneChat(fragment, zoomBuddy, intent, str, 0, threadUnreadInfo, i);
    }

    public static void showAsOneToOneChat(@NonNull Fragment fragment, ZoomBuddy zoomBuddy, Intent intent, String str, long j, ThreadUnreadInfo threadUnreadInfo, int i) {
        if (zoomBuddy != null && !TextUtils.isEmpty(str) && !isIMDisable() && zoomBuddy.getAccountStatus() != 2) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
                String jid = zoomBuddy.getJid();
                Intent intent2 = new Intent(activity, MMCommentActivity.class);
                intent2.addFlags(536870912);
                intent2.putExtra(ARG_IS_GROUP, false);
                intent2.putExtra(ARG_CONTACT, fromZoomBuddy);
                intent2.putExtra("threadId", str);
                intent2.putExtra(ARG_THREAD_SVR, j);
                intent2.putExtra(ARG_BUDDY_ID, jid);
                intent2.putExtra(ARG_SEND_INTENT, intent);
                if (threadUnreadInfo != null) {
                    intent2.putExtra(ARG_THREAD_UNREAD_INFO, threadUnreadInfo);
                }
                fragment.startActivityForResult(intent2, i);
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
                forceRefreshVcard(jid);
            }
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

    public static void showAsGroupChat(@NonNull Fragment fragment, String str, String str2) {
        showAsGroupChat(fragment, str, str2, null, null, 0);
    }

    public static void showAsGroupChat(@NonNull Fragment fragment, String str, String str2, Intent intent, ThreadUnreadInfo threadUnreadInfo, int i) {
        showAsGroupChat(fragment, str, str2, 0, intent, threadUnreadInfo, i);
    }

    public static void showAsGroupChat(@NonNull Fragment fragment, String str, String str2, long j, Intent intent, ThreadUnreadInfo threadUnreadInfo, int i) {
        if (!isIMDisable()) {
            FragmentActivity activity = fragment.getActivity();
            if (activity != null) {
                Intent intent2 = new Intent(activity, MMCommentActivity.class);
                intent2.addFlags(536870912);
                intent2.putExtra(ARG_IS_GROUP, true);
                intent2.putExtra(ARG_GROUP_ID, str);
                intent2.putExtra(ARG_SEND_INTENT, intent);
                intent2.putExtra("threadId", str2);
                intent2.putExtra(ARG_THREAD_SVR, j);
                if (threadUnreadInfo != null) {
                    intent2.putExtra(ARG_THREAD_UNREAD_INFO, threadUnreadInfo);
                }
                fragment.startActivityForResult(intent2, i);
                activity.overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(C4558R.anim.zm_slide_in_left, C4558R.anim.zm_slide_out_right);
    }

    public void onCreate(Bundle bundle) {
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
            String stringExtra3 = intent.getStringExtra("threadId");
            long longExtra = intent.getLongExtra(ARG_THREAD_SVR, 0);
            Intent intent2 = (Intent) intent.getParcelableExtra(ARG_SEND_INTENT);
            ThreadUnreadInfo threadUnreadInfo = (ThreadUnreadInfo) intent.getSerializableExtra(ARG_THREAD_UNREAD_INFO);
            if (intent.getBooleanExtra(ARG_IS_GROUP, false)) {
                MMCommentsFragment.showAsGroupChatInActivity(this, stringExtra2, stringExtra3, longExtra, intent2, threadUnreadInfo);
            } else {
                MMCommentsFragment.showAsOneToOneInActivity(this, iMAddrBookItem, stringExtra, stringExtra3, longExtra, intent2, threadUnreadInfo);
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
        MMCommentsFragment findMMCommentsFragment = MMCommentsFragment.findMMCommentsFragment(getSupportFragmentManager());
        if (findMMCommentsFragment == null || !findMMCommentsFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
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
                Intent intent3 = new Intent(this, MMCommentActivity.class);
                intent3.putExtra(ARG_IS_GROUP, booleanExtra);
                intent3.putExtra(ARG_CONTACT, iMAddrBookItem);
                intent3.putExtra(ARG_BUDDY_ID, stringExtra3);
                intent3.putExtra(ARG_GROUP_ID, stringExtra4);
                startActivity(intent3);
                overridePendingTransition(C4558R.anim.zm_slide_in_right, C4558R.anim.zm_slide_out_left);
            }
        }
    }
}
