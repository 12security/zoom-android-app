package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.AccessibilityDelegate;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.LayoutManager;
import androidx.recyclerview.widget.RecyclerView.OnScrollListener;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.MeetingInfoActivity;
import com.zipow.videobox.fragment.MMChatsListFragment;
import com.zipow.videobox.fragment.SelectCustomGroupFragment;
import com.zipow.videobox.fragment.SystemNotificationFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomSubscribeRequest;
import com.zipow.videobox.util.AlertWhenAvailableHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.ChatMeetToolbar;
import com.zipow.videobox.view.ChatMeetToolbar.IUpComingMeetingCallback;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.ScheduledMeetingItem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.recyclerview.OnRecyclerViewListener;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMChatsListView */
public class MMChatsListView extends RecyclerView implements IUpComingMeetingCallback, OnRecyclerViewListener {
    public static final int REQUEST_SCHEDULE = 1002;
    private static final String TAG = "MMChatsListView";
    /* access modifiers changed from: private */
    public MMChatsListAdapter mAdapter;
    /* access modifiers changed from: private */
    public ChatMeetToolbar mChatMeetToolbar;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsSearchLastMsg4SyncLoaded = false;
    private boolean mLoaded = false;
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (i == 0) {
                MMChatsListView.this.searchSessionLastMessageCtx();
                MMChatsListView.this.refreshBuddyVCard();
                LayoutManager layoutManager = recyclerView.getLayoutManager();
                if ((layoutManager instanceof LinearLayoutManager) && ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition() == 0 && layoutManager.getChildCount() > 0) {
                    MMChatsListView.this.refreshBuddyVCard();
                }
                if (MMChatsListView.this.mAdapter != null) {
                    MMChatsListView.this.mAdapter.clearLoadedItemCache();
                }
            }
        }
    };
    private MMChatsListFragment mParentFragment;
    @NonNull
    private Runnable mRefreshUpcomingMeetingRunnable = new Runnable() {
        public void run() {
            if (MMChatsListView.this.mChatMeetToolbar != null) {
                MMChatsListView.this.mChatMeetToolbar.refresh();
            }
        }
    };
    @Nullable
    private Runnable mTaskLazyNotifyDataSetChanged = null;

    /* renamed from: com.zipow.videobox.view.mm.MMChatsListView$ChatsListContextMenuItem */
    public static class ChatsListContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_ALERT = 2;
        public static final int ACTION_COPY_GROUP = 1;
        public static final int ACTION_DELETE = 0;
        public static final int ACTION_MARD_AS_READ = 5;
        public static final int ACTION_MARD_AS_UNREAD = 6;
        public static final int ACTION_STAR = 3;
        public static final int ACTION_UNSTAR = 4;

        public ChatsListContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public MMChatsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMChatsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMChatsListView(Context context) {
        super(context);
        initView();
    }

    public void setParentFragment(MMChatsListFragment mMChatsListFragment) {
        this.mParentFragment = mMChatsListFragment;
        this.mChatMeetToolbar.setParentFragment(mMChatsListFragment);
    }

    public boolean isParentFragmentResumed() {
        MMChatsListFragment mMChatsListFragment = this.mParentFragment;
        boolean z = false;
        if (mMChatsListFragment == null) {
            return false;
        }
        if (mMChatsListFragment.isResumed() || this.mParentFragment.isInMultWindowMode()) {
            z = true;
        }
        return z;
    }

    public void onParentFragmentResume() {
        ChatMeetToolbar chatMeetToolbar = this.mChatMeetToolbar;
        if (chatMeetToolbar != null) {
            chatMeetToolbar.setmIUpComingMeetingCallback(this);
        }
        refresh();
    }

    public void refresh() {
        loadData(false, true);
        this.mChatMeetToolbar.refresh();
        this.mAdapter.notifyDataSetChanged();
    }

    public void onParentFragmentStart() {
        this.mAdapter.notifyDataSetChanged();
        this.mChatMeetToolbar.refresh();
    }

    public void onParentFragmentPause() {
        this.mHandler.removeCallbacks(this.mRefreshUpcomingMeetingRunnable);
        ChatMeetToolbar chatMeetToolbar = this.mChatMeetToolbar;
        if (chatMeetToolbar != null) {
            chatMeetToolbar.setmIUpComingMeetingCallback(null);
        }
    }

    public void onCallStatusChanged() {
        this.mChatMeetToolbar.refresh();
        this.mAdapter.notifyDataSetChanged();
    }

    public void onWebLogin(long j) {
        this.mChatMeetToolbar.refresh();
        this.mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAdapter = new MMChatsListAdapter(getContext());
        if (isInEditMode()) {
            _editmode_loadData(this.mAdapter);
        }
        View inflate = View.inflate(getContext(), C4558R.layout.zm_mm_chat_meet_header, null);
        this.mAdapter.addHeaderView(inflate);
        this.mChatMeetToolbar = (ChatMeetToolbar) inflate.findViewById(C4558R.C4560id.chatMeetToolbar);
        setAdapter(this.mAdapter);
        this.mAdapter.setOnRecyclerViewListener(this);
        removeOnScrollListener(this.mOnScrollListener);
        addOnScrollListener(this.mOnScrollListener);
        this.mChatMeetToolbar.setAccessibilityDelegate(new AccessibilityDelegate() {
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setCollectionInfo(CollectionInfo.obtain(0, MMChatsListView.this.mChatMeetToolbar.getVisibilityBtnCount(), false, 0));
            }
        });
        this.mChatMeetToolbar.setImportantForAccessibility(1);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        CollectionInfo collectionInfo = accessibilityNodeInfo.getCollectionInfo();
        if (collectionInfo != null) {
            accessibilityNodeInfo.setCollectionInfo(CollectionInfo.obtain(this.mAdapter.getChatsItemsCount(), collectionInfo.getColumnCount(), collectionInfo.isHierarchical(), collectionInfo.getSelectionMode()));
        }
    }

    private void _editmode_loadData(@NonNull MMChatsListAdapter mMChatsListAdapter) {
        int i = 0;
        while (i < 5) {
            MMChatsListItem mMChatsListItem = new MMChatsListItem();
            mMChatsListItem.setSessionId(String.valueOf(i));
            StringBuilder sb = new StringBuilder();
            sb.append("Buddy ");
            int i2 = i + 1;
            sb.append(i2);
            mMChatsListItem.setTitle(sb.toString());
            mMChatsListItem.setLatestMessage("Hello!");
            mMChatsListItem.setIsGroup(false);
            mMChatsListItem.setUnreadMessageCount(i == 0 ? 10 : 0);
            mMChatsListAdapter.addItem(mMChatsListItem);
            i = i2;
        }
    }

    public void refreshSystemNotificationSession() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.imChatGetOption() != 2) {
            MMChatsListItem systemNotificationSessionItem = getSystemNotificationSessionItem();
            if (systemNotificationSessionItem != null) {
                this.mAdapter.addItem(systemNotificationSessionItem);
            }
        }
    }

    public void subscribeChatsPresence() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isAnyBuddyGroupLarge()) {
            int chatSessionCount = zoomMessenger.getChatSessionCount();
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < chatSessionCount; i++) {
                ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                if (sessionAt != null && !sessionAt.isGroup()) {
                    arrayList.add(sessionAt.getSessionId());
                }
            }
            if (arrayList.size() > 0) {
                zoomMessenger.subBuddyTempPresence(arrayList);
            }
        }
    }

    public void getChatsPresence() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ArrayList arrayList = new ArrayList();
            int chatSessionCount = zoomMessenger.getChatSessionCount();
            for (int i = 0; i < chatSessionCount; i++) {
                ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                if (sessionAt != null && !sessionAt.isGroup()) {
                    arrayList.add(sessionAt.getSessionId());
                }
            }
            zoomMessenger.subBuddyTempPresence(arrayList);
        }
    }

    public void loadData(boolean z, boolean z2) {
        if (!this.mLoaded || !z) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy myself = zoomMessenger.getMyself();
                if (myself != null) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(myself.getJid());
                    if (sessionById != null) {
                        int itemCount = this.mAdapter.getItemCount();
                        this.mAdapter.clear();
                        refreshSystemNotificationSession();
                        int chatSessionCount = zoomMessenger.getChatSessionCount();
                        boolean readBooleanValue = PreferenceUtil.readBooleanValue(PreferenceUtil.getPreferenceName(PreferenceUtil.FTE_CHATS_LIST_FTE), false);
                        if (chatSessionCount > 0 || readBooleanValue || ZMIMUtils.isAddContactDisable()) {
                            addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, false);
                        }
                        for (int i = 0; i < chatSessionCount; i++) {
                            ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                            if (sessionAt != null) {
                                addSessionToAdapter(this.mAdapter, sessionAt, zoomMessenger, false);
                            }
                        }
                        if (itemCount != this.mAdapter.getItemCount()) {
                            notifyDataSetChanged();
                        }
                        this.mLoaded = true;
                        return;
                    }
                    return;
                }
                return;
            }
            return;
        }
        if (z2) {
            updateAllUnreadMsgCount();
        }
    }

    private MMChatsListItem getSystemNotificationSessionItem() {
        ZoomSubscribeRequest zoomSubscribeRequest;
        String str;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        long latestRequestTimeStamp = zoomMessenger.getLatestRequestTimeStamp();
        if (latestRequestTimeStamp == 0) {
            return null;
        }
        Resources resources = getResources();
        if (resources == null || PreferenceUtil.readBooleanValue(PreferenceUtil.SYSTEM_NOTIFICATION_DELETE_FLAG, false)) {
            return null;
        }
        int subscribeRequestCount = zoomMessenger.getSubscribeRequestCount();
        if (zoomMessenger.getMyself() == null) {
            return null;
        }
        ZoomBuddy zoomBuddy = null;
        int i = 0;
        while (true) {
            if (i >= subscribeRequestCount) {
                zoomSubscribeRequest = null;
                break;
            }
            zoomSubscribeRequest = zoomMessenger.getSubscribeRequestAt(i);
            if (zoomSubscribeRequest != null && (zoomSubscribeRequest.getRequestType() == 0 || (zoomSubscribeRequest.getRequestType() == 1 && (zoomSubscribeRequest.getRequestStatus() == 1 || zoomSubscribeRequest.getRequestStatus() == 2)))) {
                zoomBuddy = zoomMessenger.getBuddyWithJID(zoomSubscribeRequest.getRequestJID());
                if (zoomBuddy != null) {
                    break;
                }
            }
            i++;
        }
        if (zoomSubscribeRequest == null) {
            return null;
        }
        final String requestJID = zoomSubscribeRequest.getRequestJID();
        if (StringUtil.isEmptyOrNull(requestJID) || zoomBuddy == null) {
            return null;
        }
        MMChatsListItem mMChatsListItem = new MMChatsListItem();
        mMChatsListItem.setIsGroup(true);
        mMChatsListItem.setTimeStamp(latestRequestTimeStamp);
        mMChatsListItem.setSessionId(zoomMessenger.getContactRequestsSessionID());
        mMChatsListItem.setTitle(resources.getString(C4558R.string.zm_contact_requests_83123));
        mMChatsListItem.setUnreadMessageCount(zoomMessenger.getUnreadRequestCount());
        int requestStatus = zoomSubscribeRequest.getRequestType() == 0 ? 0 : zoomSubscribeRequest.getRequestStatus();
        String email = ((zoomBuddy.isPending() || zoomSubscribeRequest.getRequestStatus() == 2) && zoomSubscribeRequest.getRequestType() != 0) ? zoomBuddy.getEmail() : zoomBuddy.getScreenName();
        if (StringUtil.isEmptyOrNull(email)) {
            email = zoomBuddy.getScreenName();
        }
        if (StringUtil.isEmptyOrNull(email)) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        zoomMessenger.refreshBuddyVCard(requestJID, true);
                        MMChatsListView.this.lazyNotifyDataSetChanged();
                    }
                }
            }, 3000);
        }
        switch (requestStatus) {
            case 0:
                str = resources.getString(C4558R.string.zm_session_recive_contact_request_107052, new Object[]{email});
                break;
            case 1:
                str = resources.getString(C4558R.string.zm_session_contact_request_accept_byother, new Object[]{email});
                break;
            case 2:
                str = resources.getString(C4558R.string.zm_session_contact_request_decline_byother, new Object[]{email});
                break;
            default:
                return null;
        }
        mMChatsListItem.setLatestMessage(str);
        return mMChatsListItem;
    }

    public void notifyDataSetChanged() {
        notifyDataSetChanged(false);
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mAdapter.setLazyLoadAvatarDisabled(true);
            postDelayed(new Runnable() {
                public void run() {
                    MMChatsListView.this.mAdapter.setLazyLoadAvatarDisabled(false);
                }
            }, 1000);
        }
        this.mAdapter.notifyDataSetChanged();
        if (!loadLastMessages4Sync(false)) {
            searchSessionLastMessageCtx();
        }
    }

    public void onConnectReturn(int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && zoomMessenger.isConnectionGood()) {
            this.mIsSearchLastMsg4SyncLoaded = false;
        }
    }

    public void onIndicateBuddyInfoUpdated(String str) {
        if (!isParentFragmentResumed()) {
            loadData(false, false);
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int chatsItemsCount = this.mAdapter.getChatsItemsCount();
            boolean z = false;
            for (int i = 0; i < chatsItemsCount; i++) {
                MMChatsListItem chatsItem = this.mAdapter.getChatsItem(i);
                if (chatsItem != null && chatsItem.isBuddyWithPhoneNumberInSession(str)) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(chatsItem.getSessionId());
                    if (sessionById != null) {
                        addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, true);
                    }
                    z = true;
                }
            }
            if (z && isParentFragmentResumed()) {
                lazyNotifyDataSetChanged();
            }
        }
    }

    public void onIndicateBuddyInfoUpdatedWithJID(String str) {
        if (!isParentFragmentResumed()) {
            loadData(false, false);
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int chatsItemsCount = this.mAdapter.getChatsItemsCount();
            boolean z = false;
            for (int i = 0; i < chatsItemsCount; i++) {
                MMChatsListItem chatsItem = this.mAdapter.getChatsItem(i);
                if (chatsItem != null && chatsItem.isBuddyWithJIDInSession(str)) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(chatsItem.getSessionId());
                    if (sessionById != null) {
                        addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, true);
                    }
                    z = true;
                }
            }
            if (z && isParentFragmentResumed()) {
                lazyNotifyDataSetChanged();
            }
        }
    }

    /* access modifiers changed from: private */
    public void lazyNotifyDataSetChanged() {
        if (this.mTaskLazyNotifyDataSetChanged == null) {
            this.mTaskLazyNotifyDataSetChanged = new Runnable() {
                public void run() {
                    if (MMChatsListView.this.isParentFragmentResumed()) {
                        MMChatsListView.this.notifyDataSetChanged(true);
                    }
                }
            };
        }
        this.mHandler.removeCallbacks(this.mTaskLazyNotifyDataSetChanged);
        this.mHandler.postDelayed(this.mTaskLazyNotifyDataSetChanged, 1000);
    }

    private void updateAllUnreadMsgCount() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int chatSessionCount = zoomMessenger.getChatSessionCount();
            for (int i = 0; i < chatSessionCount; i++) {
                ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                if (sessionAt != null) {
                    updateUnreadMsgCountForSession(sessionAt);
                }
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void updateUnreadMsgCountForSession(@Nullable ZoomChatSession zoomChatSession) {
        if (zoomChatSession != null) {
            MMChatsListItem itemBySessionId = this.mAdapter.getItemBySessionId(zoomChatSession.getSessionId());
            if (itemBySessionId != null) {
                itemBySessionId.setUnreadMessageCount(zoomChatSession.getUnreadMessageCount());
                itemBySessionId.setMarkUnreadMessageCount(zoomChatSession.getMarkUnreadMessageCount());
                itemBySessionId.setUnreadMessageCountBySetting(zoomChatSession.getUnreadMessageCountBySetting());
            }
        }
    }

    private void addSessionToAdapter(@NonNull MMChatsListAdapter mMChatsListAdapter, @Nullable ZoomChatSession zoomChatSession, @Nullable ZoomMessenger zoomMessenger, boolean z) {
        if (zoomMessenger != null && zoomMessenger.imChatGetOption() != 2 && zoomChatSession != null) {
            boolean isMyNotes = UIMgr.isMyNotes(zoomChatSession.getSessionId());
            boolean isAnnouncement = ZMIMUtils.isAnnouncement(zoomChatSession.getSessionId());
            if (zoomMessenger.myNotesGetOption() == 1 || !isMyNotes) {
                MMChatsListItem fromZoomChatSession = MMChatsListItem.fromZoomChatSession(zoomChatSession, zoomMessenger, getContext());
                if (fromZoomChatSession != null && (!StringUtil.isEmptyOrNull(fromZoomChatSession.getTitle()) || fromZoomChatSession.getUnreadMessageCount() != 0 || isAnnouncement)) {
                    if (zoomChatSession.getLastMessage() != null || isMyNotes || isAnnouncement || zoomChatSession.getUnreadMessageCount() != 0 || zoomChatSession.getMarkUnreadMessageCount() != 0) {
                        if (mMChatsListAdapter.getItemBySessionId(fromZoomChatSession.getSessionId()) == null && !zoomChatSession.isGroup() && zoomMessenger.isAnyBuddyGroupLarge()) {
                            ArrayList arrayList = new ArrayList();
                            arrayList.add(zoomChatSession.getSessionId());
                            zoomMessenger.subBuddyTempPresence(arrayList);
                        }
                        mMChatsListAdapter.addItem(fromZoomChatSession);
                    }
                }
            }
        }
    }

    public void onConfirm_MessageSent(@Nullable String str, String str2, int i) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                this.mAdapter.removeItem(str);
                addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, false);
                if (isParentFragmentResumed()) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onReceiveMessage(@Nullable Set<String> set) {
        if (!CollectionsUtil.isCollectionEmpty(set)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                for (String str : set) {
                    if (!StringUtil.isEmptyOrNull(str)) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                        if (sessionById != null) {
                            this.mAdapter.removeItem(str);
                            addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, false);
                        }
                    }
                }
                if (isParentFragmentResumed()) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onItemClick(View view, int i) {
        MMChatsListItem item = this.mAdapter.getItem(i);
        if (item instanceof MMChatsListItem) {
            onClickChatItem(item);
        }
    }

    private void onClickChatItem(@Nullable MMChatsListItem mMChatsListItem) {
        if (mMChatsListItem != null) {
            ZMActivity zMActivity = (ZMActivity) getContext();
            if (zMActivity != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    String contactRequestsSessionID = zoomMessenger.getContactRequestsSessionID();
                    if (contactRequestsSessionID == null || !contactRequestsSessionID.equals(mMChatsListItem.getSessionId())) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(mMChatsListItem.getSessionId());
                        if (sessionById != null) {
                            if (sessionById.isGroup()) {
                                ZoomGroup sessionGroup = sessionById.getSessionGroup();
                                if (sessionGroup != null) {
                                    String groupID = sessionGroup.getGroupID();
                                    if (!StringUtil.isEmptyOrNull(groupID)) {
                                        startGroupChat(zMActivity, groupID);
                                    } else {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                                if (sessionBuddy == null) {
                                    sessionBuddy = zoomMessenger.getMyself();
                                    if (sessionBuddy == null || !TextUtils.equals(sessionBuddy.getJid(), sessionById.getSessionId())) {
                                        return;
                                    }
                                }
                                startOneToOneChat(zMActivity, sessionBuddy);
                            }
                            return;
                        }
                        return;
                    }
                    SystemNotificationFragment.showAsActivity(zMActivity, 0);
                }
            }
        }
    }

    public boolean onItemLongClick(View view, int i) {
        String str;
        String str2;
        String str3;
        if (!PTApp.getInstance().isWebSignedOn()) {
            return false;
        }
        final MMChatsListItem item = this.mAdapter.getItem(i);
        if (item == null) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || UIMgr.isMyNotes(item.getSessionId())) {
            return false;
        }
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity == null) {
            return false;
        }
        final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
        ArrayList arrayList = new ArrayList();
        String contactRequestsSessionID = zoomMessenger.getContactRequestsSessionID();
        if (item.isGroup()) {
            if (contactRequestsSessionID != null && contactRequestsSessionID.equals(item.getSessionId())) {
                str = zMActivity.getString(C4558R.string.zm_contact_requests_83123);
                str3 = zMActivity.getString(C4558R.string.zm_delete_contact_requests_83123);
            } else if (item.isRoom()) {
                str = zMActivity.getString(C4558R.string.zm_mm_title_chatslist_context_menu_channel_chat_59554);
                str3 = zMActivity.getString(C4558R.string.zm_mm_lbl_delete_channel_chat_59554);
            } else {
                str = zMActivity.getString(C4558R.string.zm_mm_title_chatslist_context_menu_muc_chat_59554);
                str3 = zMActivity.getString(C4558R.string.zm_mm_lbl_delete_muc_chat_59554);
            }
            arrayList.add(new ChatsListContextMenuItem(str3, 0));
        } else {
            str = item.getTitle();
            arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_delete_chat_20762), 0));
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(item.getSessionId());
        if (sessionById != null && isConnectionGood()) {
            if (sessionById.getUnreadMessageCount() > 0 || sessionById.getMarkUnreadMessageCount() > 0) {
                arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_mark_as_read_95574), 5));
            } else {
                ZoomMessage lastTextMessage = sessionById.getLastTextMessage();
                if (lastTextMessage != null && (!lastTextMessage.isE2EMessage() || lastTextMessage.getMessageState() == 7)) {
                    arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_mm_lbl_mark_as_unread_95574), 6));
                }
            }
        }
        if (!item.isGroup() && AlertWhenAvailableHelper.getInstance().showAlertWhenAvailable(item)) {
            arrayList.add(new ChatsListContextMenuItem(AlertWhenAvailableHelper.getInstance().getMenuString(item), 2));
        }
        int i2 = 3;
        if (contactRequestsSessionID != null && contactRequestsSessionID.equals(item.getSessionId())) {
            if (zoomMessenger.isUnstarredContactRequests()) {
                str2 = zMActivity.getString(C4558R.string.zm_star_contact_requests_83123);
            } else {
                str2 = zMActivity.getString(C4558R.string.zm_unstar_contact_requests_83123);
                i2 = 4;
            }
            arrayList.add(new ChatsListContextMenuItem(str2, i2));
        } else if (zoomMessenger.isStarSession(item.getSessionId())) {
            String string = zMActivity.getString(C4558R.string.zm_msg_unstar_contact_68451);
            if (item.isGroup()) {
                if (item.isRoom()) {
                    string = zMActivity.getString(C4558R.string.zm_msg_unstar_channel_78010);
                } else {
                    string = zMActivity.getString(C4558R.string.zm_msg_unstar_chat_78010);
                }
            }
            arrayList.add(new ChatsListContextMenuItem(string, 4));
        } else {
            String string2 = zMActivity.getString(C4558R.string.zm_msg_star_contact_68451);
            if (item.isGroup()) {
                if (item.isRoom()) {
                    string2 = zMActivity.getString(C4558R.string.zm_msg_star_channel_78010);
                } else {
                    string2 = zMActivity.getString(C4558R.string.zm_msg_star_chat_78010);
                }
            }
            arrayList.add(new ChatsListContextMenuItem(string2, 3));
        }
        if (!item.isRoom() && !item.isGroup() && zoomMessenger.personalGroupGetOption() == 1) {
            arrayList.add(new ChatsListContextMenuItem(zMActivity.getString(C4558R.string.zm_msg_add_contact_group_68451), 1));
        }
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) str).setAdapter(zMMenuAdapter, new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMChatsListView.this.onSelectContextMenuItem(item, (ChatsListContextMenuItem) zMMenuAdapter.getItem(i));
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }

    private boolean isConnectionGood() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        return zoomMessenger.isConnectionGood();
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@NonNull MMChatsListItem mMChatsListItem, ChatsListContextMenuItem chatsListContextMenuItem) {
        if (chatsListContextMenuItem.getAction() == 0) {
            deleteSessionById(mMChatsListItem.getSessionId());
        } else if (chatsListContextMenuItem.getAction() == 1) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(MMChatsListFragment.EXTRA_BUDDY_IN_CUSTOM_GROUP, IMAddrBookItem.fromZoomBuddy(zoomMessenger.getBuddyWithJID(mMChatsListItem.getSessionId())));
                SelectCustomGroupFragment.showAsActivity(this.mParentFragment, getContext().getString(C4558R.string.zm_msg_copy_contact_68451), bundle, 101, mMChatsListItem.getSessionId());
            }
        } else if (chatsListContextMenuItem.getAction() == 2) {
            AlertWhenAvailableHelper.getInstance().checkAndAddToAlertQueen((ZMActivity) this.mParentFragment.getActivity(), mMChatsListItem);
            notifyDataSetChanged();
        } else if (chatsListContextMenuItem.getAction() == 3) {
            ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger2 != null) {
                if (zoomMessenger2.isContactRequestsSession(mMChatsListItem.getSessionId())) {
                    zoomMessenger2.starSessionSetStar(mMChatsListItem.getSessionId(), false);
                } else {
                    zoomMessenger2.starSessionSetStar(mMChatsListItem.getSessionId(), true);
                }
                notifyDataSetChanged();
            }
        } else if (chatsListContextMenuItem.getAction() == 4) {
            ZoomMessenger zoomMessenger3 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger3 != null) {
                if (zoomMessenger3.isContactRequestsSession(mMChatsListItem.getSessionId())) {
                    zoomMessenger3.starSessionSetStar(mMChatsListItem.getSessionId(), true);
                } else {
                    zoomMessenger3.starSessionSetStar(mMChatsListItem.getSessionId(), false);
                }
                notifyDataSetChanged();
            }
        } else if (chatsListContextMenuItem.getAction() == 5) {
            ZoomMessenger zoomMessenger4 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger4 != null) {
                ZoomChatSession sessionById = zoomMessenger4.getSessionById(mMChatsListItem.getSessionId());
                if (sessionById != null) {
                    sessionById.clearAllMarkedUnreadMessage();
                    sessionById.cleanUnreadMessageCount();
                    refresh();
                }
            }
        } else if (chatsListContextMenuItem.getAction() == 6) {
            ZoomMessenger zoomMessenger5 = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger5 != null) {
                ZoomChatSession sessionById2 = zoomMessenger5.getSessionById(mMChatsListItem.getSessionId());
                if (sessionById2 != null) {
                    ZoomMessage lastTextMessage = sessionById2.getLastTextMessage();
                    if (lastTextMessage != null) {
                        sessionById2.markMessageAsUnread(lastTextMessage.getMessageXMPPGuid());
                        refresh();
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

    public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            String groupId = groupAction.getGroupId();
            if (!StringUtil.isEmptyOrNull(groupId)) {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(groupId);
                if (sessionById == null) {
                    this.mAdapter.removeItem(groupId);
                } else {
                    addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, true);
                }
                if (isParentFragmentResumed()) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void onNotifySubscribeRequest() {
        refreshSystemNotificationSession();
        this.mAdapter.notifyDataSetChanged();
    }

    public void Indicate_EditMessageResultIml(String str, @Nullable String str2, String str3, long j, long j2, boolean z) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
            if (sessionById != null) {
                addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, false);
            } else if (str2 != null) {
                this.mAdapter.removeItem(str2);
            }
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshChatSessionUnread() {
        loadData(true, true);
    }

    public void refreshChatSessionUnread(@NonNull String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById == null) {
                this.mAdapter.removeItem(str);
            } else {
                addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, false);
            }
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void refreshChatSessionsUnread(@NonNull List<String> list) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            HashSet<String> hashSet = new HashSet<>(list);
            for (int i = 0; i < this.mAdapter.getChatsItemsCount(); i++) {
                MMChatsListItem chatsItem = this.mAdapter.getChatsItem(i);
                if (chatsItem != null && !TextUtils.isEmpty(chatsItem.getSessionId())) {
                    boolean contains = list.contains(chatsItem.getSessionId());
                    if (!contains) {
                        contains = chatsItem.getMarkUnreadMessageCount() > 0;
                    } else {
                        hashSet.remove(chatsItem.getSessionId());
                    }
                    if (contains) {
                        ZoomChatSession sessionById = zoomMessenger.getSessionById(chatsItem.getSessionId());
                        if (sessionById != null) {
                            updateUnreadMsgCountForSession(sessionById);
                        }
                    }
                }
            }
            if (hashSet.size() > 0) {
                for (String str : hashSet) {
                    if (!StringUtil.isEmptyOrNull(str)) {
                        ZoomChatSession sessionById2 = zoomMessenger.getSessionById(str);
                        if (sessionById2 != null) {
                            addSessionToAdapter(this.mAdapter, sessionById2, zoomMessenger, true);
                        }
                    }
                }
            }
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void notify_DBLoadSessionLastMessagesDone() {
        loadLastMessages4Sync(true);
    }

    public void notify_ChatSessionResetUnreadCount(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById != null) {
                updateUnreadMsgCountForSession(sessionById);
                if (isParentFragmentResumed()) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    public boolean loadLastMessages4Sync(boolean z) {
        int i = 0;
        if (!z && this.mIsSearchLastMsg4SyncLoaded) {
            return false;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        if (z) {
            while (i < zoomMessenger.getChatSessionCount()) {
                ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                if (sessionAt != null) {
                    if (!StringUtil.isEmptyOrNull(sessionAt.getSessionId())) {
                        arrayList.add(sessionAt.getSessionId());
                    }
                    if (arrayList.size() >= 20) {
                        break;
                    }
                }
                i++;
            }
        } else {
            while (i < this.mAdapter.getChatsItemsCount() && arrayList.size() < 20) {
                MMChatsListItem chatsItem = this.mAdapter.getChatsItem(i);
                if (chatsItem != null && !StringUtil.isEmptyOrNull(chatsItem.getSessionId())) {
                    arrayList.add(chatsItem.getSessionId());
                }
                i++;
            }
        }
        if (arrayList.isEmpty()) {
            return true;
        }
        zoomMessenger.searchSessionLastMessageCtx(arrayList);
        this.mIsSearchLastMsg4SyncLoaded = true;
        return true;
    }

    public void onNotify_MUCGroupInfoUpdatedImpl(@NonNull String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null && !StringUtil.isEmptyOrNull(str)) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
            if (sessionById == null) {
                this.mAdapter.removeItem(str);
            } else {
                addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, true);
            }
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (i == 1002 && i2 == -1 && intent != null) {
            onScheduleSuccess((ScheduledMeetingItem) intent.getSerializableExtra("meetingItem"));
        }
    }

    private void onScheduleSuccess(final ScheduledMeetingItem scheduledMeetingItem) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            zMActivity.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onScheduleSuccess") {
                public void run(IUIElement iUIElement) {
                    MeetingInfoActivity.show((ZMActivity) iUIElement, scheduledMeetingItem, true, 104);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void refreshBuddyVCard() {
        MMChatsListAdapter mMChatsListAdapter = this.mAdapter;
        if (mMChatsListAdapter != null) {
            List loadedItems = mMChatsListAdapter.getLoadedItems();
            if (!CollectionsUtil.isListEmpty(loadedItems)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    zoomMessenger.refreshBuddyVCards(loadedItems);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void searchSessionLastMessageCtx() {
        List loadedItems = this.mAdapter.getLoadedItems();
        if (!CollectionsUtil.isListEmpty(loadedItems)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                if (loadedItems.size() > getChildCount()) {
                    loadedItems = loadedItems.subList(loadedItems.size() - getChildCount(), loadedItems.size());
                }
                zoomMessenger.searchSessionLastMessageCtx(loadedItems);
            }
        }
    }

    public void onRefresh(@NonNull List<Long> list) {
        this.mHandler.removeCallbacks(this.mRefreshUpcomingMeetingRunnable);
        if (!CollectionsUtil.isListEmpty(list)) {
            for (Long l : list) {
                if (l != null) {
                    this.mHandler.postDelayed(this.mRefreshUpcomingMeetingRunnable, l.longValue() + 2000);
                }
            }
        }
    }

    public void deleteSessionById(String str) {
        deleteSessionById(str, 100);
    }

    public void deleteSessionById(final String str, long j) {
        postDelayed(new Runnable() {
            public void run() {
                MMChatsListView.this.deleteSessionByIdImpl(str);
            }
        }, j);
    }

    /* access modifiers changed from: private */
    public void deleteSessionByIdImpl(String str) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            boolean z = true;
            if (StringUtil.isSameString(str, zoomMessenger.getContactRequestsSessionID())) {
                PreferenceUtil.saveBooleanValue(PreferenceUtil.SYSTEM_NOTIFICATION_DELETE_FLAG, true);
                int unreadRequestCount = zoomMessenger.getUnreadRequestCount();
                if (zoomMessenger.setAllRequestAsReaded() && unreadRequestCount > 0) {
                    zoomMessenger.syncAllSubScribeReqAsReaded();
                }
            } else {
                ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
                if (sessionById != null) {
                    sessionById.storeMessageDraft(null);
                    sessionById.storeMessageDraftTime(0);
                }
                z = zoomMessenger.deleteSession(str, false);
            }
            if (z) {
                MMChatsListFragment mMChatsListFragment = this.mParentFragment;
                if (mMChatsListFragment != null) {
                    mMChatsListFragment.onChatItemDelete(str);
                } else {
                    loadData(false, false);
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}
