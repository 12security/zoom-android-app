package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.view.ViewStub.OnInflateListener;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.AddrBookSetNumberActivity;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.MMSelectContactsActivity;
import com.zipow.videobox.MMSelectContactsActivity.SelectContactsParamter;
import com.zipow.videobox.StarredActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.eventbus.ZMAlertAvailable;
import com.zipow.videobox.eventbus.ZMChatsSession;
import com.zipow.videobox.fragment.IMAddrBookListFragment.ContactsPermissionFailedDialog;
import com.zipow.videobox.fragment.p012mm.MMPhoneContactsInZoomFragment;
import com.zipow.videobox.ptapp.ABContactsCache;
import com.zipow.videobox.ptapp.ABContactsCache.IABContactsCacheListener;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfo;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfoMap;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.MakeGroupResult;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimplePTUIListener;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr.IMeetingStatusListener;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr.IPTUIStatusListener;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr.SourceMeetingList;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.MMZoomBuddyGroup;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.ZMBuddyListListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMView.OnFragmentShowListener;
import com.zipow.videobox.view.MeetingToolbar;
import com.zipow.videobox.view.ZMSearchBar;
import com.zipow.videobox.view.p014mm.MMChatsListView;
import com.zipow.videobox.view.p014mm.MMContactSearchFragment;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.app.ZMStorageUtil;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class MMChatsListFragment extends ZMFragment implements OnClickListener, IABContactsCacheListener, IPTUIStatusListener, IMeetingStatusListener, OnLongClickListener, IConfProcessListener, OnFragmentShowListener {
    public static final String EXTRA_BUDDY_IN_CUSTOM_GROUP = "EXTRA_BUDDY_IN_CUSTOM_GROUP";
    public static final long MIN_DISK_FREE_SPACE = 524288000;
    private static final int PERMISSION_REQUEST_ENABLE_ADDRBOOK = 1000;
    public static final int REQUEST_COPY_BUDDY_CONTACT_GROUP = 101;
    private static final int REQUEST_ENABLE_PHONE_MATCH = 102;
    public static final int REQUEST_MM_NEW_CHAT = 100;
    private final String TAG = MMChatsListFragment.class.getSimpleName();
    /* access modifiers changed from: private */
    public View mAddContactsFTEView;
    private View mBtnCloseDiskFullAlert;
    private View mBtnNewChat;
    private View mBtnSettings;
    @NonNull
    private ZMBuddyListListener mBuddyListLisener = new ZMBuddyListListener() {
        public void onBuddyListUpdate() {
            MMChatsListFragment.this.onBuddyListUpdate();
        }

        public void onBuddyInfoUpdate(List<String> list, List<String> list2) {
            if (!CollectionsUtil.isListEmpty(list)) {
                for (String access$900 : list) {
                    MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(access$900);
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public MMChatsListView mChatsListView;
    @Nullable
    private Drawable mDimmedForground = null;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    private FrameLayout mListContainer;
    private View mLlContent;
    private MeetingToolbar mMeetingToolbar;
    /* access modifiers changed from: private */
    public View mMyNoteFTEView;
    @Nullable
    private IPTUIListener mNetworkStateReceiver;
    @Nullable
    private Runnable mNewMsgRunnable;
    @NonNull
    private INotificationSettingUIListener mNotificationSettingUIListener = new SimpleNotificationSettingUIListener() {
        public void OnUnreadOnTopSettingUpdated() {
            MMChatsListFragment.this.OnUnreadOnTopSettingUpdated();
        }

        public void OnUnreadBadgeSettingUpdated() {
            MMChatsListFragment.this.onUnreadBadgeSettingUpdated();
        }
    };
    private View mPanelConnectionAlert;
    private View mPanelDiskFullAlert;
    private View mPanelTitleBar;
    /* access modifiers changed from: private */
    public final Runnable mRunnableHandleBuddyUpdate = new Runnable() {
        public void run() {
            if (MMChatsListFragment.this.mUpdatedBuddyJids.size() > 10) {
                MMChatsListFragment.this.mChatsListView.refresh();
            } else if (MMChatsListFragment.this.mChatsListView != null) {
                Iterator it = MMChatsListFragment.this.mUpdatedBuddyJids.iterator();
                while (it.hasNext()) {
                    MMChatsListFragment.this.handleIndicateBuddyInfoUpdatedWithJID((String) it.next());
                }
            }
            MMChatsListFragment.this.updatePanelNoItemMsg();
            MMChatsListFragment.this.mUpdatedBuddyJids.clear();
            MMChatsListFragment.this.mHandler.postDelayed(MMChatsListFragment.this.mRunnableHandleBuddyUpdate, 2000);
        }
    };
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mRunnableUpdateSessionList;
    private View mSearchBarDivideLine;
    /* access modifiers changed from: private */
    @NonNull
    public Set<String> mSessionsHasNewMsg = new HashSet();
    private View mStarredBtn;
    private TextView mTxtTitle;
    /* access modifiers changed from: private */
    @NonNull
    public ArrayList<String> mUpdatedBuddyJids = new ArrayList<>();
    @NonNull
    private ZMPTIMeetingMgr mZMPTIMeetingMgr = ZMPTIMeetingMgr.getInstance();
    private ZMSearchBar mZMSearchBar;
    @NonNull
    private IZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onBeginConnect() {
            MMChatsListFragment.this.onBeginConnect();
        }

        public void onConnectReturn(int i) {
            MMChatsListFragment.this.onConnectReturn(i);
        }

        public void onIndicateBuddyInfoUpdated(String str) {
            MMChatsListFragment.this.onIndicateBuddyInfoUpdated(str);
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(str);
        }

        public void onIndicateBuddyListUpdated() {
            MMChatsListFragment.this.onIndicateBuddyListUpdated();
        }

        public void onGroupAction(int i, GroupAction groupAction, String str) {
            MMChatsListFragment.this.onGroupAction(i, groupAction, str);
        }

        public void onNotify_ChatSessionListUpdate() {
            MMChatsListFragment.this.onNotify_ChatSessionListUpdate();
        }

        public void Indicate_OnlineBuddies(@Nullable List<String> list) {
            if (list != null) {
                for (String access$900 : list) {
                    MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(access$900);
                }
            }
        }

        public void Indicate_GetContactsPresence(@Nullable List<String> list, @Nullable List<String> list2) {
            if (list != null) {
                for (String access$900 : list) {
                    MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(access$900);
                }
            }
            if (list2 != null) {
                for (String access$9002 : list2) {
                    MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(access$9002);
                }
            }
        }

        public void Indicate_BuddyPresenceChanged(String str) {
            MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(str);
        }

        public void onConfirm_MessageSent(@NonNull String str, String str2, int i) {
            MMChatsListFragment.this.onConfirm_MessageSent(str, str2, i);
        }

        public boolean onIndicateMessageReceived(String str, String str2, String str3) {
            return MMChatsListFragment.this.onIndicateMessageReceived(str, str2, str3);
        }

        public boolean onNotifySubscribeRequest(String str, String str2) {
            return MMChatsListFragment.this.onNotifySubscribeRequest();
        }

        public boolean onNotifySubscriptionAccepted(String str) {
            return MMChatsListFragment.this.onNotifySubscribeRequest();
        }

        public boolean onNotifySubscriptionDenied(String str) {
            return MMChatsListFragment.this.onNotifySubscribeRequest();
        }

        public void onNotify_MUCGroupInfoUpdatedImpl(@NonNull String str) {
            MMChatsListFragment.this.onNotify_MUCGroupInfoUpdatedImpl(str);
        }

        public void onNotifySubscribeRequestUpdated(String str) {
            MMChatsListFragment.this.refreshSubcribeRequest();
        }

        public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
            MMChatsListFragment.this.On_DestroyGroup(i, str, str2, str3, j);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMChatsListFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }

        public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, long j, long j2, boolean z) {
            MMChatsListFragment.this.Indicate_RevokeMessageResult(str, str2, str3, str4, z);
        }

        public void Indicate_EditMessageResultIml(String str, @NonNull String str2, String str3, long j, long j2, boolean z) {
            MMChatsListFragment.this.Indicate_EditMessageResultIml(str, str2, str3, j, j2, z);
        }

        public void Notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
            MMChatsListFragment.this.notify_ChatSessionMarkUnreadUpdate(sessionMessageInfoMap);
        }

        public void onNotify_ChatSessionUnreadUpdate(@NonNull String str) {
            MMChatsListFragment.this.refreshChatSessionUnread(str);
        }

        public void onNotify_ChatSessionUpdate(String str) {
            MMChatsListFragment.this.onNotify_ChatSessionUpdate(str);
        }

        public void Notify_DBLoadSessionLastMessagesDone() {
            MMChatsListFragment.this.notify_DBLoadSessionLastMessagesDone();
        }

        public void notify_ChatSessionResetUnreadCount(String str) {
            MMChatsListFragment.this.notify_ChatSessionResetUnreadCount(str);
        }

        public void onRemoveBuddy(@NonNull String str, int i) {
            MMChatsListFragment.this.onRemoveBuddy(str, i);
        }

        public void Indicate_BuddyGroupMembersUpdated(String str, @NonNull List<String> list) {
            if (!CollectionsUtil.isListEmpty(list)) {
                for (String access$900 : list) {
                    MMChatsListFragment.this.onIndicateBuddyInfoUpdatedWithJID(access$900);
                }
            }
        }

        public void Indicate_AvailableAlert(String str, String str2) {
            MMChatsListFragment.this.mChatsListView.notifyDataSetChanged();
        }

        public void Indicate_BuddyAccountStatusChange(String str, int i) {
            if (i == 2 || i == 3) {
                MMChatsListFragment.this.mChatsListView.deleteSessionById(str);
            }
        }

        public void On_BroadcastUpdate(int i, String str, boolean z) {
            if (i == 3 && MMChatsListFragment.this.mChatsListView != null) {
                MMChatsListFragment.this.mChatsListView.loadData(false, true);
                MMChatsListFragment.this.mChatsListView.notifyDataSetChanged();
            }
        }

        public void indicate_BuddyBlockedByIB(List<String> list) {
            if (!CollectionsUtil.isListEmpty(list) && MMChatsListFragment.this.mChatsListView != null) {
                for (String deleteSessionById : list) {
                    MMChatsListFragment.this.mChatsListView.deleteSessionById(deleteSessionById);
                }
            }
        }
    };

    private static class AddContactMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_ADD_CONTACT_BY_EMAIL = 0;
        public static final int ACTION_ADD_CONTACT_FROM_PHONE_CONTACT = 1;

        public AddContactMenuItem(String str, int i) {
            super(i, str);
        }
    }

    private boolean onLongClickTxtTitle() {
        return true;
    }

    private void pushBuddyUpdateEventToQueue(@Nullable String str) {
        if (str != null) {
            this.mUpdatedBuddyJids.add(str);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        boolean z = false;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_chats_list, viewGroup, false);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mChatsListView = (MMChatsListView) inflate.findViewById(C4558R.C4560id.chatsListView);
        this.mBtnNewChat = inflate.findViewById(C4558R.C4560id.btnNewChat);
        this.mZMSearchBar = (ZMSearchBar) inflate.findViewById(C4558R.C4560id.panelSearch);
        this.mSearchBarDivideLine = inflate.findViewById(C4558R.C4560id.searchBarDivideLine);
        this.mPanelConnectionAlert = inflate.findViewById(C4558R.C4560id.panelConnectionAlert);
        this.mPanelDiskFullAlert = inflate.findViewById(C4558R.C4560id.panelDiskFullAlert);
        this.mBtnCloseDiskFullAlert = inflate.findViewById(C4558R.C4560id.btnCloseDiskFullAlert);
        this.mPanelTitleBar = inflate.findViewById(C4558R.C4560id.panelTitleBar);
        this.mListContainer = (FrameLayout) inflate.findViewById(C4558R.C4560id.listContainer);
        this.mBtnSettings = inflate.findViewById(C4558R.C4560id.btnSettings);
        this.mMeetingToolbar = (MeetingToolbar) inflate.findViewById(C4558R.C4560id.meetingToolbar);
        this.mLlContent = inflate.findViewById(C4558R.C4560id.llContent);
        this.mStarredBtn = inflate.findViewById(C4558R.C4560id.btnStarred);
        this.mChatsListView.setParentFragment(this);
        this.mMeetingToolbar.setParentFragment(this);
        this.mBtnNewChat.setOnClickListener(this);
        this.mBtnSettings.setOnClickListener(this);
        this.mBtnCloseDiskFullAlert.setOnClickListener(this);
        this.mStarredBtn.setOnClickListener(this);
        this.mZMSearchBar.setOnClickListener(this);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        ZMBuddySyncInstance.getInsatance().addListener(this.mBuddyListLisener);
        this.mDimmedForground = new ColorDrawable(getResources().getColor(C4558R.color.zm_dimmed_forground));
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z2 = zoomMessenger == null || zoomMessenger.imChatGetOption() == 2;
        if (zoomMessenger == null || zoomMessenger.e2eGetMyOption() == 2) {
            z = true;
        }
        if (!PTApp.getInstance().hasZoomMessenger()) {
            if (z2) {
                this.mZMSearchBar.setVisibility(8);
                this.mSearchBarDivideLine.setVisibility(8);
            }
            this.mStarredBtn.setVisibility(8);
        } else if ((z || z2) && z2) {
            this.mStarredBtn.setVisibility(8);
        }
        EventBus.getDefault().register(this);
        return inflate;
    }

    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        ZMBuddySyncInstance.getInsatance().removeListener(this.mBuddyListLisener);
        super.onDestroyView();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.getMyself() != null && !ZmPtUtils.isEnableIM() && ZmPtUtils.isIMChatOptionChanaged() && (getActivity() instanceof ZMActivity)) {
            DialogUtils.showAlertDialog((ZMActivity) getActivity(), getString(C4558R.string.zm_mm_msg_chat_disable_dialog_title_83185), getString(C4558R.string.zm_mm_msg_chat_disable_dialog_content_83185), C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAlertAvailableEvent(ZMAlertAvailable zMAlertAvailable) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.notifyDataSetChanged();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeChatsSessionEvent(ZMChatsSession zMChatsSession) {
        disableFTE();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001c, code lost:
        if (isDiskFull() != false) goto L_0x0020;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updatePanelDiskFullAlert() {
        /*
            r3 = this;
            android.view.View r0 = r3.mPanelDiskFullAlert
            if (r0 == 0) goto L_0x002a
            java.lang.String r0 = "out_of_storage_alert"
            r1 = 1
            boolean r0 = com.zipow.videobox.util.PreferenceUtil.readBooleanValue(r0, r1)
            r2 = 0
            if (r0 != 0) goto L_0x001f
            android.view.View r0 = r3.mPanelConnectionAlert
            if (r0 == 0) goto L_0x001f
            int r0 = r0.getVisibility()
            if (r0 == 0) goto L_0x001f
            boolean r0 = r3.isDiskFull()
            if (r0 == 0) goto L_0x001f
            goto L_0x0020
        L_0x001f:
            r1 = 0
        L_0x0020:
            android.view.View r0 = r3.mPanelDiskFullAlert
            if (r1 == 0) goto L_0x0025
            goto L_0x0027
        L_0x0025:
            r2 = 8
        L_0x0027:
            r0.setVisibility(r2)
        L_0x002a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMChatsListFragment.updatePanelDiskFullAlert():void");
    }

    /* access modifiers changed from: private */
    public void updatePanelConnectionAlert() {
        updatePanelDiskFullAlert();
    }

    private boolean isDiskFull() {
        try {
            return ZMStorageUtil.getAvailableMemSize(ZMStorageUtil.getInternalStoragePath()) < MIN_DISK_FREE_SPACE;
        } catch (Exception unused) {
            return false;
        }
    }

    private void updateNewChatBtn() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        boolean z = false;
        int chatSessionCount = zoomMessenger != null ? zoomMessenger.getChatSessionCount() : 0;
        if (!ZMIMUtils.isContactEmpty() || isFTEShowed() || chatSessionCount > 0) {
            z = true;
        }
        this.mBtnNewChat.setClickable(z);
        this.mBtnNewChat.setPressed(!z);
    }

    public void onResume() {
        super.onResume();
        if (ZmPtUtils.isEnableIM()) {
            this.mLlContent.setVisibility(0);
            this.mMeetingToolbar.setVisibility(8);
            this.mBtnNewChat.setVisibility(0);
            updateNewChatBtn();
        } else {
            this.mLlContent.setVisibility(8);
            this.mMeetingToolbar.setVisibility(0);
            this.mBtnNewChat.setVisibility(4);
            this.mMeetingToolbar.refresh();
        }
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onParentFragmentResume();
        }
        NotificationMgr.removeMessageNotificationMM(getActivity());
        updateTitle();
        updatePanelConnectionAlert();
        updatePanelDiskFullAlert();
        startToListenNetworkEvent();
        VideoBoxApplication.getInstance().addConfProcessListener(this);
        this.mZMPTIMeetingMgr.addMySelfToPTUIListener();
        this.mZMPTIMeetingMgr.addMySelfToMeetingMgrListener();
        this.mZMPTIMeetingMgr.addIMeetingStatusListener(this);
        this.mZMPTIMeetingMgr.addIPTUIStatusListener(this);
        this.mHandler.post(this.mRunnableHandleBuddyUpdate);
        this.mZMPTIMeetingMgr.pullCalendarIntegrationConfig();
    }

    public void onStart() {
        super.onStart();
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onParentFragmentStart();
            this.mMeetingToolbar.refresh();
        }
        NotificationSettingUI.getInstance().addListener(this.mNotificationSettingUIListener);
    }

    public void onStop() {
        NotificationSettingUI.getInstance().removeListener(this.mNotificationSettingUIListener);
        super.onStop();
    }

    private void updateTitle() {
        if (!PTApp.getInstance().hasZoomMessenger()) {
            TextView textView = this.mTxtTitle;
            if (textView != null) {
                textView.setText(C4558R.string.zm_tab_meeting);
            }
            return;
        }
        switch (ZoomMessengerUI.getInstance().getConnectionStatus()) {
            case -1:
            case 0:
            case 1:
                if (this.mTxtTitle != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (!(zoomMessenger == null || zoomMessenger.imChatGetOption() == 2)) {
                        this.mTxtTitle.setText(C4558R.string.zm_mm_title_chats);
                        break;
                    } else {
                        this.mTxtTitle.setText(C4558R.string.zm_app_full_name);
                        break;
                    }
                }
                break;
            case 2:
                TextView textView2 = this.mTxtTitle;
                if (textView2 != null) {
                    textView2.setText(C4558R.string.zm_mm_title_chats_connecting);
                    break;
                }
                break;
        }
        TextView textView3 = this.mTxtTitle;
        if (textView3 != null) {
            textView3.getParent().requestLayout();
        }
    }

    private void inflateAddContactsFTEView() {
        ViewStub viewStub = (ViewStub) getView().findViewById(C4558R.C4560id.addContactsFTEViewStub);
        viewStub.setOnInflateListener(new OnInflateListener() {
            public void onInflate(ViewStub viewStub, View view) {
                MMChatsListFragment.this.mAddContactsFTEView = view;
                MMChatsListFragment.this.mAddContactsFTEView.findViewById(C4558R.C4560id.btn_add_contacts).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        MMChatsListFragment.this.showAddContactContextMenu();
                    }
                });
            }
        });
        viewStub.inflate();
    }

    private void updateListViewHight(boolean z) {
        LayoutParams layoutParams = this.mChatsListView.getLayoutParams();
        if (layoutParams != null) {
            int i = z ? -2 : -1;
            if (i != layoutParams.height) {
                layoutParams.height = i;
                this.mChatsListView.setLayoutParams(layoutParams);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showMyNote() {
        View view = this.mMyNoteFTEView;
        if (view != null) {
            view.setVisibility(8);
        }
        PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_FTE), true);
        PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_MY_NOTE), true);
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.loadData(false, false);
        }
    }

    private void inflateMyNoteFTEView() {
        ViewStub viewStub = (ViewStub) getView().findViewById(C4558R.C4560id.myNoteFTEViewStub);
        viewStub.setOnInflateListener(new OnInflateListener() {
            public void onInflate(ViewStub viewStub, View view) {
                MMChatsListFragment.this.mMyNoteFTEView = view;
                MMChatsListFragment.this.mMyNoteFTEView.findViewById(C4558R.C4560id.btn_show_me).setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        MMChatsListFragment.this.showMyNote();
                    }
                });
            }
        });
        viewStub.inflate();
    }

    private void showMyNoteFTEView(boolean z) {
        if (z) {
            View view = this.mMyNoteFTEView;
            if (view == null) {
                inflateMyNoteFTEView();
            } else {
                view.setVisibility(0);
            }
        } else {
            View view2 = this.mMyNoteFTEView;
            if (view2 != null) {
                view2.setVisibility(8);
            }
        }
    }

    private void showAddContactsFTEView(boolean z) {
        if (z) {
            View view = this.mAddContactsFTEView;
            if (view == null) {
                inflateAddContactsFTEView();
            } else {
                view.setVisibility(0);
            }
        } else {
            View view2 = this.mAddContactsFTEView;
            if (view2 != null) {
                view2.setVisibility(8);
            }
        }
    }

    /* access modifiers changed from: private */
    public void showAddContactContextMenu() {
        Context context = getContext();
        if (context != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            ArrayList arrayList = new ArrayList();
            arrayList.add(new AddContactMenuItem(getString(C4558R.string.zm_lbl_add_contact_by_email_79032), 0));
            arrayList.add(new AddContactMenuItem(getString(C4558R.string.zm_lbl_add_contact_from_phone_contact_79032), 1));
            zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
            ZMAlertDialog create = new Builder(context).setTitle(C4558R.string.zm_lbl_add_contacts_79032).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AddContactMenuItem addContactMenuItem = (AddContactMenuItem) zMMenuAdapter.getItem(i);
                    if (addContactMenuItem.getAction() == 0) {
                        MMAddBuddyNewFragment.showAsActivity(MMChatsListFragment.this, false);
                    } else if (addContactMenuItem.getAction() == 1) {
                        MMChatsListFragment.this.showPhoneContactsInZoom();
                    }
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    public void showPhoneContactsInZoom() {
        if (!PTApp.getInstance().isPhoneNumberRegistered()) {
            showPhoneMatchFragment();
        } else if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_ADD_CONTACTS), true);
            MMPhoneContactsInZoomFragment.showAsActivity((ZMActivity) getContext(), 0);
        } else {
            zm_requestPermissions(new String[]{"android.permission.READ_CONTACTS"}, 1000);
        }
    }

    private void showPhoneMatchFragment() {
        AddrBookSetNumberActivity.show((Fragment) this, 102);
    }

    private void onContactsPermissionRequestByPhoneMatch() {
        if (checkSelfPermission("android.permission.READ_CONTACTS") == 0) {
            showPhoneContactsInZoom();
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                ContactsPermissionFailedDialog.show(fragmentManager);
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            if (i == 1000) {
                onContactsPermissionRequestByPhoneMatch();
            }
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.READ_CONTACTS".equals(strArr[i2]) && iArr[i2] == 0) {
                    ABContactsCache instance = ABContactsCache.getInstance();
                    instance.registerContentObserver();
                    if (instance.needReloadAll()) {
                        instance.reloadAllContacts();
                    }
                }
            }
        }
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((MMChatsListFragment) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onBuddyListUpdate() {
        if (!isFTEShowed() && (!ZMIMUtils.isContactEmpty())) {
            if (this.mAddContactsFTEView == null || this.mMyNoteFTEView != null || PreferenceUtil.readBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_MY_NOTE), false) || !ZMIMUtils.isMyNoteOn()) {
                PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_FTE), true);
                updateListViewHight(false);
                View view = this.mAddContactsFTEView;
                if (view != null) {
                    view.setVisibility(8);
                }
                View view2 = this.mMyNoteFTEView;
                if (view2 != null) {
                    view2.setVisibility(8);
                }
            } else {
                this.mAddContactsFTEView.setVisibility(8);
                showMyNoteFTEView(true);
                return;
            }
        }
        onIndicateBuddyListUpdated();
        updateNewChatBtn();
    }

    /* access modifiers changed from: private */
    public void updatePanelNoItemMsg() {
        if (isFTEShowed()) {
            updateListViewHight(false);
            return;
        }
        boolean z = !ZMIMUtils.isContactEmpty();
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        int chatSessionCount = zoomMessenger != null ? zoomMessenger.getChatSessionCount() : 0;
        if (ZMIMUtils.isAddContactDisable() || chatSessionCount > 0 || (z && this.mAddContactsFTEView == null)) {
            disableFTE();
            return;
        }
        boolean readBooleanValue = PreferenceUtil.readBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_ADD_CONTACTS), false);
        boolean readBooleanValue2 = PreferenceUtil.readBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_MY_NOTE), false);
        if (readBooleanValue) {
            View view = this.mAddContactsFTEView;
            if (view != null) {
                view.setVisibility(8);
                if (readBooleanValue2 || !ZMIMUtils.isMyNoteOn()) {
                    disableFTE();
                } else if (this.mMyNoteFTEView == null) {
                    updateListViewHight(true);
                    showMyNoteFTEView(true);
                }
            } else {
                disableFTE();
            }
        } else if (this.mAddContactsFTEView == null) {
            updateListViewHight(true);
            showAddContactsFTEView(true);
        }
    }

    private boolean isFTEShowed() {
        return PreferenceUtil.readBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_FTE), false);
    }

    private void disableFTE() {
        if (!isFTEShowed()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            int chatSessionCount = zoomMessenger != null ? zoomMessenger.getChatSessionCount() : 0;
            if (!ZMIMUtils.isContactEmpty() || chatSessionCount != 0) {
                View view = this.mAddContactsFTEView;
                if (view != null) {
                    view.setVisibility(8);
                }
                View view2 = this.mMyNoteFTEView;
                if (view2 != null) {
                    view2.setVisibility(8);
                }
                PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_FTE), true);
                PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_ADD_CONTACTS), true);
                PreferenceUtil.saveBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_MY_NOTE), true);
                updateListViewHight(false);
            }
        }
    }

    private void startToListenNetworkEvent() {
        if (this.mNetworkStateReceiver == null) {
            this.mNetworkStateReceiver = new SimplePTUIListener() {
                public void onDataNetworkStatusChanged(boolean z) {
                    MMChatsListFragment.this.updatePanelConnectionAlert();
                }
            };
            PTUI.getInstance().addPTUIListener(this.mNetworkStateReceiver);
        }
    }

    private void stopToListenNetworkEvent() {
        if (this.mNetworkStateReceiver != null) {
            PTUI.getInstance().removePTUIListener(this.mNetworkStateReceiver);
            this.mNetworkStateReceiver = null;
        }
    }

    public void onContactsCacheUpdated() {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.loadData(false, true);
            this.mChatsListView.notifyDataSetChanged(true);
        }
    }

    public void onPause() {
        super.onPause();
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onParentFragmentPause();
        }
        stopToListenNetworkEvent();
        VideoBoxApplication.getInstance().removeConfProcessListener(this);
        this.mZMPTIMeetingMgr.removeIPTUIStatusListener(this);
        this.mZMPTIMeetingMgr.removeMySelfFromPTUIListener();
        this.mZMPTIMeetingMgr.removeIMeetingStatusListener(this);
        this.mZMPTIMeetingMgr.removeMySelfFromMeetingMgrListener();
        this.mHandler.removeCallbacks(this.mRunnableHandleBuddyUpdate);
    }

    public void onDestroy() {
        super.onDestroy();
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onActivityResult(i, i2, intent);
        }
        if (i == 100 && i2 == -1 && intent != null) {
            ArrayList arrayList = (ArrayList) intent.getSerializableExtra("selectedItems");
            if (arrayList != null && arrayList.size() != 0) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        if (arrayList.size() == 1) {
                            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(((IMAddrBookItem) arrayList.get(0)).getJid());
                            if (buddyWithJID != null) {
                                startOneToOneChat(zMActivity, buddyWithJID);
                            }
                        } else {
                            makeGroup(zoomMessenger, arrayList, "");
                        }
                    }
                }
            }
        } else if (i == 101 && i2 == -1) {
            onCopyBuddyInCustomGroup(intent);
        } else if (i == 102 && i2 == -1 && PTApp.getInstance().isPhoneNumberRegistered()) {
            showPhoneContactsInZoom();
        }
    }

    private void onCopyBuddyInCustomGroup(@Nullable Intent intent) {
        if (intent != null) {
            IMAddrBookItem iMAddrBookItem = (IMAddrBookItem) intent.getSerializableExtra(EXTRA_BUDDY_IN_CUSTOM_GROUP);
            MMZoomBuddyGroup mMZoomBuddyGroup = (MMZoomBuddyGroup) intent.getSerializableExtra(SelectCustomGroupFragment.RESULT_GROUP);
            if (iMAddrBookItem != null && mMZoomBuddyGroup != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(iMAddrBookItem.getJid());
                    zoomMessenger.addBuddyToPersonalBuddyGroup(arrayList, mMZoomBuddyGroup.getXmppGroupID());
                }
            }
        }
    }

    public void makeGroup(@Nullable ZoomMessenger zoomMessenger, @NonNull ArrayList<IMAddrBookItem> arrayList, String str) {
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                String jid = myself.getJid();
                if (!StringUtil.isEmptyOrNull(jid)) {
                    ArrayList arrayList2 = new ArrayList();
                    for (int i = 0; i < arrayList.size(); i++) {
                        String jid2 = ((IMAddrBookItem) arrayList.get(i)).getJid();
                        if (!StringUtil.isEmptyOrNull(jid2)) {
                            arrayList2.add(jid2);
                        }
                    }
                    if (!arrayList2.contains(jid)) {
                        arrayList2.add(jid);
                    }
                    if (arrayList2.size() != 0) {
                        if (!zoomMessenger.isConnectionGood()) {
                            showConnectionError();
                            return;
                        }
                        MakeGroupResult makeGroup = zoomMessenger.makeGroup(arrayList2, str, 80);
                        if (makeGroup == null || !makeGroup.getResult()) {
                            showMakeGroupFailureMessage(1, null);
                        } else if (makeGroup.getValid()) {
                            String reusableGroupId = makeGroup.getReusableGroupId();
                            ZMActivity zMActivity = (ZMActivity) getActivity();
                            if (zMActivity != null && !StringUtil.isEmptyOrNull(reusableGroupId)) {
                                startGroupChat(zMActivity, reusableGroupId);
                            }
                        } else {
                            showWaitingMakeGroupDialog();
                        }
                    }
                }
            }
        }
    }

    private static void startOneToOneChat(ZMActivity zMActivity, ZoomBuddy zoomBuddy) {
        MMChatActivity.showAsOneToOneChat(zMActivity, zoomBuddy);
    }

    private static void startGroupChat(@NonNull ZMActivity zMActivity, String str) {
        MMChatActivity.showAsGroupChat(zMActivity, str);
    }

    public void onClick(View view) {
        if (view == this.mBtnNewChat) {
            startNewChat();
        } else if (view == this.mZMSearchBar) {
            ZoomLogEventTracking.eventTrackHostSearch(false);
            onClickBtnSearch();
        } else if (view == this.mBtnSettings) {
            onClickBtnSettings();
        } else if (view == this.mBtnCloseDiskFullAlert) {
            onClickBtnCloseDiskFullAlert();
        } else if (view == this.mStarredBtn) {
            onClickBtnStarred();
        }
    }

    private void onClickBtnStarred() {
        Context context = getContext();
        if (context != null) {
            StarredActivity.launch(context);
        }
    }

    private void onClickBtnCloseDiskFullAlert() {
        this.mPanelDiskFullAlert.setVisibility(8);
        PreferenceUtil.saveBooleanValue(PreferenceUtil.IM_OUT_OF_STORAGE_ALERT, true);
    }

    private void onClickBtnSettings() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            SettingFragment.showAsActivity(zMActivity, 0, true);
        }
    }

    private void onClickBtnSearch() {
        if (PTApp.getInstance().isWebSignedOn()) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || zoomMessenger.e2eGetMyOption() == 2) {
                MMContactSearchFragment.showAsFragment(this);
            } else {
                IMSearchFragment.showAsFragment(this, 0);
            }
        }
    }

    private void startNewChat() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                String string = zMActivity.getString(C4558R.string.zm_mm_title_new_chat);
                String string2 = zMActivity.getString(C4558R.string.zm_mm_btn_start_chat);
                String string3 = zMActivity.getString(C4558R.string.zm_msg_select_buddies_to_chat_instructions);
                SelectContactsParamter selectContactsParamter = new SelectContactsParamter();
                selectContactsParamter.title = string;
                selectContactsParamter.btnOkText = string2;
                selectContactsParamter.instructionMessage = string3;
                selectContactsParamter.isAnimBottomTop = true;
                selectContactsParamter.isOnlySameOrganization = false;
                selectContactsParamter.maxSelectCount = zoomMessenger.getGroupLimitCount(false) - 1;
                selectContactsParamter.isContainsAllInGroup = false;
                selectContactsParamter.includeRobot = false;
                MMSelectContactsActivity.show((Fragment) this, selectContactsParamter, 100, (Bundle) null);
                ZoomLogEventTracking.eventTrackStartNewChat();
            }
        }
    }

    private void showWaitingMakeGroupDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(true);
            newInstance.show(fragmentManager, "WaitingMakeGroupDialog");
        }
    }

    private boolean dismissWaitingMakeGroupDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            return false;
        }
        ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingMakeGroupDialog");
        if (zMDialogFragment == null) {
            return false;
        }
        zMDialogFragment.dismissAllowingStateLoss();
        return true;
    }

    private void showMakeGroupFailureMessage(int i, @Nullable GroupAction groupAction) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            if (i == 8) {
                Toast.makeText(activity, C4558R.string.zm_mm_msg_make_group_failed_too_many_buddies_59554, 1).show();
            } else {
                String string = activity.getString(C4558R.string.zm_mm_msg_make_group_failed_59554, new Object[]{Integer.valueOf(i)});
                if (i == 40 && groupAction != null && groupAction.getMaxAllowed() > 0) {
                    string = activity.getString(C4558R.string.zm_mm_msg_max_allowed_buddies_50731, new Object[]{Integer.valueOf(groupAction.getMaxAllowed())});
                }
                Toast.makeText(activity, string, 1).show();
            }
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    /* access modifiers changed from: private */
    public void onBeginConnect() {
        if (NetworkUtil.hasDataNetwork(getActivity()) && isResumed()) {
            updateTitle();
            updatePanelConnectionAlert();
        }
    }

    /* access modifiers changed from: private */
    public void onUnreadBadgeSettingUpdated() {
        if (this.mChatsListView != null && isResumed()) {
            this.mChatsListView.refreshChatSessionUnread();
        }
    }

    /* access modifiers changed from: private */
    public void OnUnreadOnTopSettingUpdated() {
        if (this.mChatsListView != null && isResumed()) {
            this.mChatsListView.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void onConnectReturn(int i) {
        if (PTApp.getInstance().getZoomMessenger() != null) {
            MMChatsListView mMChatsListView = this.mChatsListView;
            if (mMChatsListView != null) {
                mMChatsListView.getChatsPresence();
                this.mChatsListView.onConnectReturn(i);
            }
            if (isResumed()) {
                updateTitle();
                updatePanelConnectionAlert();
                MMChatsListView mMChatsListView2 = this.mChatsListView;
                if (mMChatsListView2 != null) {
                    mMChatsListView2.notifyDataSetChanged(true);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyInfoUpdated(String str) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onIndicateBuddyInfoUpdated(str);
        }
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyInfoUpdatedWithJID(String str) {
        pushBuddyUpdateEventToQueue(str);
    }

    /* access modifiers changed from: private */
    public void handleIndicateBuddyInfoUpdatedWithJID(String str) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onIndicateBuddyInfoUpdatedWithJID(str);
        }
    }

    /* access modifiers changed from: private */
    public void onGroupAction(final int i, @Nullable final GroupAction groupAction, String str) {
        if (groupAction != null) {
            this.mChatsListView.onGroupAction(i, groupAction, str);
            if (isResumed()) {
                updatePanelNoItemMsg();
            }
            if (groupAction.getActionType() == 0) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy myself = zoomMessenger.getMyself();
                    if (myself == null || StringUtil.isSameString(myself.getJid(), groupAction.getActionOwnerId())) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                            public void run(IUIElement iUIElement) {
                                MMChatsListFragment mMChatsListFragment = (MMChatsListFragment) iUIElement;
                                if (mMChatsListFragment != null) {
                                    mMChatsListFragment.handleGroupActionMakeGroup(i, groupAction);
                                }
                            }
                        });
                    }
                }
            } else if (groupAction.getActionType() == 4) {
                if (groupAction.isMeInBuddies()) {
                    getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                        public void run(IUIElement iUIElement) {
                            MMChatsListFragment mMChatsListFragment = (MMChatsListFragment) iUIElement;
                            if (mMChatsListFragment != null) {
                                mMChatsListFragment.handleGroupActionDeleteGroup(i, groupAction);
                            }
                        }
                    });
                }
            } else if (groupAction.getActionType() == 5) {
                ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger2 != null) {
                    ZoomBuddy myself2 = zoomMessenger2.getMyself();
                    if (myself2 != null && StringUtil.isSameString(myself2.getJid(), groupAction.getActionOwnerId())) {
                        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
                            public void run(IUIElement iUIElement) {
                                MMChatsListFragment mMChatsListFragment = (MMChatsListFragment) iUIElement;
                                if (mMChatsListFragment != null) {
                                    mMChatsListFragment.handleGroupActionDeleteGroup(i, groupAction);
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionDeleteGroup(int i, @NonNull GroupAction groupAction) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            zoomMessenger.deleteSession(groupAction.getGroupId());
            this.mChatsListView.refresh();
        }
    }

    /* access modifiers changed from: private */
    public void handleGroupActionMakeGroup(int i, @NonNull GroupAction groupAction) {
        if (dismissWaitingMakeGroupDialog()) {
            if (i == 0) {
                String groupId = groupAction.getGroupId();
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null && !StringUtil.isEmptyOrNull(groupId)) {
                    startGroupChat(zMActivity, groupId);
                }
            } else {
                showMakeGroupFailureMessage(i, groupAction);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_ChatSessionListUpdate() {
        if (this.mRunnableUpdateSessionList == null) {
            this.mRunnableUpdateSessionList = new Runnable() {
                public void run() {
                    if (MMChatsListFragment.this.mChatsListView != null) {
                        MMChatsListFragment.this.mChatsListView.loadData(false, true);
                        MMChatsListFragment.this.mChatsListView.subscribeChatsPresence();
                        if (MMChatsListFragment.this.isResumed()) {
                            MMChatsListFragment.this.mChatsListView.notifyDataSetChanged(true);
                            MMChatsListFragment.this.updatePanelNoItemMsg();
                        }
                    }
                    MMChatsListFragment.this.mRunnableUpdateSessionList = null;
                }
            };
            this.mHandler.postDelayed(this.mRunnableUpdateSessionList, 500);
        }
        updateNewChatBtn();
    }

    /* access modifiers changed from: private */
    public void onIndicateBuddyListUpdated() {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.loadData(false, false);
            if (isResumed()) {
                this.mChatsListView.notifyDataSetChanged(true);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onRemoveBuddy(@NonNull String str, int i) {
        if (i == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (!(zoomMessenger == null || zoomMessenger.findSessionById(str) == null)) {
                onChatItemDelete(str);
            }
        }
    }

    public void onChatItemDelete(@NonNull String str) {
        FragmentActivity activity = getActivity();
        if (activity instanceof IMActivity) {
            ((IMActivity) activity).onMMSessionDeleted(str);
        }
    }

    public void onSessionDeleted(String str) {
        if (isResumed()) {
            this.mChatsListView.refresh();
            updatePanelNoItemMsg();
        }
    }

    /* access modifiers changed from: private */
    public void onConfirm_MessageSent(@Nullable String str, String str2, int i) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onConfirm_MessageSent(str, str2, i);
        }
        if (isResumed()) {
            updatePanelNoItemMsg();
        }
    }

    /* access modifiers changed from: private */
    public boolean onIndicateMessageReceived(String str, String str2, String str3) {
        Looper.myLooper();
        Looper.getMainLooper();
        delayRefreshSessionNewMessage(str2);
        return false;
    }

    /* access modifiers changed from: private */
    public void onNotify_MUCGroupInfoUpdatedImpl(@NonNull String str) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onNotify_MUCGroupInfoUpdatedImpl(str);
        }
        if (isResumed()) {
            updatePanelNoItemMsg();
        }
    }

    /* access modifiers changed from: private */
    public void refreshSubcribeRequest() {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onNotifySubscribeRequest();
        }
        if (isResumed()) {
            updatePanelNoItemMsg();
        }
    }

    /* access modifiers changed from: private */
    public void refreshChatSessionUnread(@NonNull String str) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.refreshChatSessionUnread(str);
        }
    }

    /* access modifiers changed from: private */
    public void notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
        if (this.mChatsListView != null && sessionMessageInfoMap != null) {
            ArrayList arrayList = new ArrayList();
            for (SessionMessageInfo session : sessionMessageInfoMap.getInfosList()) {
                arrayList.add(session.getSession());
            }
            this.mChatsListView.refreshChatSessionsUnread(arrayList);
        }
    }

    /* access modifiers changed from: private */
    public void notify_DBLoadSessionLastMessagesDone() {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.notify_DBLoadSessionLastMessagesDone();
        }
    }

    /* access modifiers changed from: private */
    public void notify_ChatSessionResetUnreadCount(String str) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.notify_ChatSessionResetUnreadCount(str);
        }
    }

    /* access modifiers changed from: private */
    public void onNotify_ChatSessionUpdate(String str) {
        onNotify_ChatSessionListUpdate();
    }

    /* access modifiers changed from: private */
    public void On_DestroyGroup(int i, String str, String str2, String str3, long j) {
        if (i == 0) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("DestroyGroup") {
                public void run(IUIElement iUIElement) {
                    if (MMChatsListFragment.this.mChatsListView != null) {
                        MMChatsListFragment.this.mChatsListView.refresh();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void Indicate_EditMessageResultIml(String str, @NonNull String str2, String str3, long j, long j2, boolean z) {
        this.mChatsListView.Indicate_EditMessageResultIml(str, str2, str3, j, j2, z);
    }

    /* access modifiers changed from: private */
    public void Indicate_RevokeMessageResult(String str, String str2, String str3, String str4, boolean z) {
        if (z) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("RevokeMessageResult") {
                public void run(IUIElement iUIElement) {
                    if (MMChatsListFragment.this.mChatsListView != null) {
                        MMChatsListFragment.this.mChatsListView.refresh();
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, final String str2, long j) {
        getNonNullEventTaskManagerOrThrowException().push(new EventAction("NotifyGroupDestroy") {
            public void run(IUIElement iUIElement) {
                if (MMChatsListFragment.this.mChatsListView != null) {
                    MMChatsListFragment.this.mChatsListView.refresh();
                }
                FragmentActivity activity = MMChatsListFragment.this.getActivity();
                if (activity != null) {
                    SimpleMessageDialog.newInstance(activity.getString(C4558R.string.zm_mm_msg_group_disbanded_by_admin_59554, new Object[]{str2}), false).show(MMChatsListFragment.this.getFragmentManager(), SimpleMessageDialog.class.getName());
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean onNotifySubscribeRequest() {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SYSTEM_NOTIFICATION_DELETE_FLAG, false);
        refreshSubcribeRequest();
        return false;
    }

    public void onCalendarConfigReady(long j) {
        this.mZMPTIMeetingMgr.pullCloudMeetings();
    }

    public void onCallStatusChanged(long j) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.refresh();
        }
    }

    public void onWebLogin(long j) {
        MMChatsListView mMChatsListView = this.mChatsListView;
        if (mMChatsListView != null) {
            mMChatsListView.onWebLogin(j);
            this.mMeetingToolbar.refresh();
        }
    }

    public void onRefreshMyNotes() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                handleIndicateBuddyInfoUpdatedWithJID(myself.getJid());
            }
        }
    }

    public boolean onLongClick(View view) {
        if (view == this.mTxtTitle) {
            return onLongClickTxtTitle();
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:59:0x009a, code lost:
        r11 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:60:0x009b, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:64:0x009f, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:0x00a0, code lost:
        r9 = r1;
        r1 = r11;
        r11 = r9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:74:0x00b2, code lost:
        r11 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:75:0x00b3, code lost:
        r1 = null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x00b7, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:80:0x00b8, code lost:
        r9 = r1;
        r1 = r11;
        r11 = r9;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x009a A[ExcHandler: all (th java.lang.Throwable), Splitter:B:27:0x0060] */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x00b2 A[ExcHandler: all (th java.lang.Throwable), Splitter:B:25:0x005b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void copyFile(@androidx.annotation.NonNull java.lang.String r11, @androidx.annotation.NonNull java.lang.String r12) {
        /*
            r10 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r11)
            java.io.File r11 = new java.io.File
            r11.<init>(r12)
            boolean r1 = r0.exists()
            if (r1 != 0) goto L_0x0011
            return
        L_0x0011:
            boolean r1 = r11.exists()
            if (r1 == 0) goto L_0x001a
            r10.deleteFile(r12)
        L_0x001a:
            boolean r1 = r0.isDirectory()
            if (r1 == 0) goto L_0x004a
            boolean r11 = r11.mkdirs()
            if (r11 != 0) goto L_0x0027
            return
        L_0x0027:
            java.io.File[] r11 = r0.listFiles()
            if (r11 == 0) goto L_0x007f
            int r0 = r11.length
            r1 = 0
        L_0x002f:
            if (r1 >= r0) goto L_0x007f
            r2 = r11[r1]
            java.lang.String r3 = r2.getAbsolutePath()
            java.io.File r4 = new java.io.File
            java.lang.String r2 = r2.getName()
            r4.<init>(r12, r2)
            java.lang.String r2 = r4.getAbsolutePath()
            r10.copyFile(r3, r2)
            int r1 = r1 + 1
            goto L_0x002f
        L_0x004a:
            boolean r12 = r11.createNewFile()     // Catch:{ IOException -> 0x00e1 }
            if (r12 != 0) goto L_0x0051
            return
        L_0x0051:
            java.io.FileInputStream r12 = new java.io.FileInputStream     // Catch:{ Exception -> 0x00e0 }
            r12.<init>(r0)     // Catch:{ Exception -> 0x00e0 }
            r0 = 0
            java.nio.channels.FileChannel r7 = r12.getChannel()     // Catch:{ Throwable -> 0x00ce }
            java.io.FileOutputStream r8 = new java.io.FileOutputStream     // Catch:{ Throwable -> 0x00b5, all -> 0x00b2 }
            r8.<init>(r11)     // Catch:{ Throwable -> 0x00b5, all -> 0x00b2 }
            java.nio.channels.FileChannel r11 = r8.getChannel()     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            r3 = 0
            long r5 = r7.size()     // Catch:{ Throwable -> 0x0083, all -> 0x0080 }
            r1 = r11
            r2 = r7
            r1.transferFrom(r2, r3, r5)     // Catch:{ Throwable -> 0x0083, all -> 0x0080 }
            if (r11 == 0) goto L_0x0074
            r11.close()     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
        L_0x0074:
            r8.close()     // Catch:{ Throwable -> 0x00b5, all -> 0x00b2 }
            if (r7 == 0) goto L_0x007c
            r7.close()     // Catch:{ Throwable -> 0x00ce }
        L_0x007c:
            r12.close()     // Catch:{ Exception -> 0x00e0 }
        L_0x007f:
            return
        L_0x0080:
            r1 = move-exception
            r2 = r0
            goto L_0x0089
        L_0x0083:
            r1 = move-exception
            throw r1     // Catch:{ all -> 0x0085 }
        L_0x0085:
            r2 = move-exception
            r9 = r2
            r2 = r1
            r1 = r9
        L_0x0089:
            if (r11 == 0) goto L_0x0099
            if (r2 == 0) goto L_0x0096
            r11.close()     // Catch:{ Throwable -> 0x0091, all -> 0x009a }
            goto L_0x0099
        L_0x0091:
            r11 = move-exception
            r2.addSuppressed(r11)     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
            goto L_0x0099
        L_0x0096:
            r11.close()     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
        L_0x0099:
            throw r1     // Catch:{ Throwable -> 0x009d, all -> 0x009a }
        L_0x009a:
            r11 = move-exception
            r1 = r0
            goto L_0x00a3
        L_0x009d:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x009f }
        L_0x009f:
            r1 = move-exception
            r9 = r1
            r1 = r11
            r11 = r9
        L_0x00a3:
            if (r1 == 0) goto L_0x00ae
            r8.close()     // Catch:{ Throwable -> 0x00a9, all -> 0x00b2 }
            goto L_0x00b1
        L_0x00a9:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch:{ Throwable -> 0x00b5, all -> 0x00b2 }
            goto L_0x00b1
        L_0x00ae:
            r8.close()     // Catch:{ Throwable -> 0x00b5, all -> 0x00b2 }
        L_0x00b1:
            throw r11     // Catch:{ Throwable -> 0x00b5, all -> 0x00b2 }
        L_0x00b2:
            r11 = move-exception
            r1 = r0
            goto L_0x00bb
        L_0x00b5:
            r11 = move-exception
            throw r11     // Catch:{ all -> 0x00b7 }
        L_0x00b7:
            r1 = move-exception
            r9 = r1
            r1 = r11
            r11 = r9
        L_0x00bb:
            if (r7 == 0) goto L_0x00cb
            if (r1 == 0) goto L_0x00c8
            r7.close()     // Catch:{ Throwable -> 0x00c3 }
            goto L_0x00cb
        L_0x00c3:
            r2 = move-exception
            r1.addSuppressed(r2)     // Catch:{ Throwable -> 0x00ce }
            goto L_0x00cb
        L_0x00c8:
            r7.close()     // Catch:{ Throwable -> 0x00ce }
        L_0x00cb:
            throw r11     // Catch:{ Throwable -> 0x00ce }
        L_0x00cc:
            r11 = move-exception
            goto L_0x00d1
        L_0x00ce:
            r11 = move-exception
            r0 = r11
            throw r0     // Catch:{ all -> 0x00cc }
        L_0x00d1:
            if (r0 == 0) goto L_0x00dc
            r12.close()     // Catch:{ Throwable -> 0x00d7 }
            goto L_0x00df
        L_0x00d7:
            r12 = move-exception
            r0.addSuppressed(r12)     // Catch:{ Exception -> 0x00e0 }
            goto L_0x00df
        L_0x00dc:
            r12.close()     // Catch:{ Exception -> 0x00e0 }
        L_0x00df:
            throw r11     // Catch:{ Exception -> 0x00e0 }
        L_0x00e0:
            return
        L_0x00e1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.fragment.MMChatsListFragment.copyFile(java.lang.String, java.lang.String):void");
    }

    private void deleteFile(@NonNull String str) {
        File file = new File(str);
        if (file.exists()) {
            if (file.isDirectory()) {
                String[] list = file.list();
                if (list != null) {
                    for (String deleteFile : list) {
                        deleteFile(deleteFile);
                    }
                }
            }
            file.delete();
        }
    }

    private void delayRefreshSessionNewMessage(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mSessionsHasNewMsg.add(str);
            if (this.mNewMsgRunnable == null) {
                this.mNewMsgRunnable = new Runnable() {
                    public void run() {
                        if (MMChatsListFragment.this.mChatsListView != null) {
                            MMChatsListFragment.this.mChatsListView.onReceiveMessage(MMChatsListFragment.this.mSessionsHasNewMsg);
                            MMChatsListFragment.this.mSessionsHasNewMsg.clear();
                        }
                        if (MMChatsListFragment.this.isResumed()) {
                            MMChatsListFragment.this.updatePanelNoItemMsg();
                        }
                    }
                };
                this.mHandler.postDelayed(this.mNewMsgRunnable, 1000);
            }
        }
    }

    public void onMeetingListLoadDone(SourceMeetingList sourceMeetingList) {
        if (this.mChatsListView != null && ZmPtUtils.isEnableIM()) {
            this.mChatsListView.onCallStatusChanged();
        }
    }

    public void onConfProcessStarted() {
        MeetingToolbar meetingToolbar = this.mMeetingToolbar;
        if (meetingToolbar == null || meetingToolbar.getVisibility() != 0) {
            MMChatsListView mMChatsListView = this.mChatsListView;
            if (mMChatsListView != null) {
                mMChatsListView.refresh();
                return;
            }
            return;
        }
        this.mMeetingToolbar.refresh();
    }

    public void onConfProcessStopped() {
        MeetingToolbar meetingToolbar = this.mMeetingToolbar;
        if (meetingToolbar == null || meetingToolbar.getVisibility() != 0) {
            MMChatsListView mMChatsListView = this.mChatsListView;
            if (mMChatsListView != null) {
                mMChatsListView.refresh();
                return;
            }
            return;
        }
        this.mMeetingToolbar.refresh();
    }

    public void onShow() {
        updatePanelDiskFullAlert();
    }
}
