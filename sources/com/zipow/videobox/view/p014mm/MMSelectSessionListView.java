package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.fragment.MMSelectSessionFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.BuddyNameUtil;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.MemCache;
import com.zipow.videobox.view.IMAddrBookItem;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMSelectSessionListView */
public class MMSelectSessionListView extends ListView implements OnItemClickListener {
    private static final String TAG = "MMSelectSessionListView";
    /* access modifiers changed from: private */
    public MMSelectSessionListAdapter mAdapter;
    @NonNull
    private MemCache<String, Drawable> mAvatarCache = new MemCache<>(10);
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mLoaded = false;
    /* access modifiers changed from: private */
    public MMSelectSessionFragment mParentFragment;
    @Nullable
    private Runnable mTaskLazyNotifyDataSetChanged = null;

    public MMSelectSessionListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMSelectSessionListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMSelectSessionListView(Context context) {
        super(context);
        initView();
    }

    public void setParentFragment(MMSelectSessionFragment mMSelectSessionFragment) {
        this.mParentFragment = mMSelectSessionFragment;
    }

    public boolean isParentFragmentResumed() {
        MMSelectSessionFragment mMSelectSessionFragment = this.mParentFragment;
        if (mMSelectSessionFragment == null) {
            return false;
        }
        return mMSelectSessionFragment.isResumed();
    }

    public void onParentFragmentResume() {
        loadData(false);
        this.mAdapter.notifyDataSetChanged();
    }

    public void onParentFragmentStart() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void onParentFragmentPause() {
        this.mAvatarCache.clear();
    }

    private void initView() {
        this.mAdapter = new MMSelectSessionListAdapter(getContext());
        this.mAdapter.setAvatarCache(this.mAvatarCache);
        if (isInEditMode()) {
            _editmode_loadData(this.mAdapter);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
    }

    private void _editmode_loadData(@NonNull MMSelectSessionListAdapter mMSelectSessionListAdapter) {
        int i = 0;
        while (i < 5) {
            MMSelectSessionListItem mMSelectSessionListItem = new MMSelectSessionListItem();
            mMSelectSessionListItem.setSessionId(String.valueOf(i));
            StringBuilder sb = new StringBuilder();
            sb.append("Buddy ");
            i++;
            sb.append(i);
            mMSelectSessionListItem.setTitle(sb.toString());
            mMSelectSessionListItem.setIsGroup(false);
            mMSelectSessionListAdapter.addItem(mMSelectSessionListItem);
        }
    }

    public void loadData(boolean z) {
        if (!this.mLoaded || !z) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                this.mAdapter.clear();
                int chatSessionCount = zoomMessenger.getChatSessionCount();
                for (int i = 0; i < chatSessionCount; i++) {
                    ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i);
                    if (sessionAt != null) {
                        addSessionToAdapter(this.mAdapter, sessionAt, zoomMessenger, false);
                    }
                }
                this.mLoaded = true;
            }
        }
    }

    public void notifyDataSetChanged() {
        notifyDataSetChanged(false);
    }

    public void notifyDataSetChanged(boolean z) {
        if (z) {
            this.mAdapter.setLazyLoadAvatarDisabled(true);
            postDelayed(new Runnable() {
                public void run() {
                    MMSelectSessionListView.this.mAdapter.setLazyLoadAvatarDisabled(false);
                }
            }, 1000);
        }
        this.mAdapter.notifyDataSetChanged();
    }

    public void onIndicateBuddyInfoUpdated(String str) {
        if (!this.mParentFragment.isResumed()) {
            loadData(false);
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int chatsItemsCount = this.mAdapter.getChatsItemsCount();
            boolean z = false;
            for (int i = 0; i < chatsItemsCount; i++) {
                MMSelectSessionListItem chatsItem = this.mAdapter.getChatsItem(i);
                if (chatsItem != null && chatsItem.isBuddyWithPhoneNumberInSession(str)) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(chatsItem.getSessionId());
                    this.mAvatarCache.removeItem(chatsItem.getSessionId());
                    if (sessionById != null) {
                        addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, true);
                    }
                    z = true;
                }
            }
            if (z && this.mParentFragment.isResumed()) {
                lazyNotifyDataSetChanged();
            }
        }
    }

    public void onIndicateBuddyInfoUpdatedWithJID(String str) {
        if (!this.mParentFragment.isResumed()) {
            loadData(false);
            return;
        }
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int chatsItemsCount = this.mAdapter.getChatsItemsCount();
            boolean z = false;
            for (int i = 0; i < chatsItemsCount; i++) {
                MMSelectSessionListItem chatsItem = this.mAdapter.getChatsItem(i);
                if (chatsItem != null && chatsItem.isBuddyWithJIDInSession(str)) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(chatsItem.getSessionId());
                    this.mAvatarCache.removeItem(chatsItem.getSessionId());
                    if (sessionById != null) {
                        addSessionToAdapter(this.mAdapter, sessionById, zoomMessenger, true);
                    }
                    z = true;
                }
            }
            if (z && this.mParentFragment.isResumed()) {
                lazyNotifyDataSetChanged();
            }
        }
    }

    private void lazyNotifyDataSetChanged() {
        if (this.mTaskLazyNotifyDataSetChanged == null) {
            this.mTaskLazyNotifyDataSetChanged = new Runnable() {
                public void run() {
                    if (MMSelectSessionListView.this.mParentFragment.isResumed()) {
                        MMSelectSessionListView.this.notifyDataSetChanged(true);
                    }
                }
            };
        }
        this.mHandler.removeCallbacks(this.mTaskLazyNotifyDataSetChanged);
        this.mHandler.postDelayed(this.mTaskLazyNotifyDataSetChanged, 1000);
    }

    public void filter(String str) {
        this.mAdapter.filter(str);
    }

    private void addSessionToAdapter(@NonNull MMSelectSessionListAdapter mMSelectSessionListAdapter, ZoomChatSession zoomChatSession, ZoomMessenger zoomMessenger, boolean z) {
        ZoomBuddy zoomBuddy;
        String str;
        boolean isGroup = zoomChatSession.isGroup();
        if (isGroup) {
            ZoomGroup sessionGroup = zoomChatSession.getSessionGroup();
            if (sessionGroup != null) {
                String groupDisplayName = sessionGroup.getGroupDisplayName(getContext());
                zoomBuddy = null;
                str = groupDisplayName;
            } else {
                return;
            }
        } else {
            zoomBuddy = zoomChatSession.getSessionBuddy();
            if (zoomBuddy != null) {
                str = BuddyNameUtil.getBuddyDisplayName(zoomBuddy, null);
            } else {
                return;
            }
        }
        MMSelectSessionListItem mMSelectSessionListItem = new MMSelectSessionListItem();
        mMSelectSessionListItem.setSessionId(zoomChatSession.getSessionId());
        mMSelectSessionListItem.setTitle(str);
        mMSelectSessionListItem.setIsGroup(isGroup);
        if (!isGroup) {
            mMSelectSessionListItem.setAvatar(zoomBuddy.getLocalPicturePath());
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomBuddy);
            if (fromZoomBuddy != null) {
                fromZoomBuddy.setJid(zoomBuddy.getJid());
                mMSelectSessionListItem.setFromContact(fromZoomBuddy);
            }
        }
        ZoomMessage lastMessage = zoomChatSession.getLastMessage();
        if (lastMessage == null) {
            mMSelectSessionListItem.setTimeStamp(0);
            if (z) {
                mMSelectSessionListAdapter.removeItem(zoomChatSession.getSessionId());
            }
            if (zoomChatSession.isGroup()) {
                mMSelectSessionListAdapter.addItem(mMSelectSessionListItem);
                return;
            }
            return;
        }
        mMSelectSessionListItem.setTimeStamp(lastMessage.getStamp());
        mMSelectSessionListAdapter.addItem(mMSelectSessionListItem);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object item = this.mAdapter.getItem(i);
        if (item instanceof MMSelectSessionListItem) {
            onClickChatItem((MMSelectSessionListItem) item);
        }
    }

    private void onClickChatItem(@NonNull MMSelectSessionListItem mMSelectSessionListItem) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomChatSession sessionById = zoomMessenger.getSessionById(mMSelectSessionListItem.getSessionId());
            if (sessionById != null) {
                if (sessionById.isGroup()) {
                    ZoomGroup sessionGroup = sessionById.getSessionGroup();
                    if (sessionGroup != null) {
                        String groupID = sessionGroup.getGroupID();
                        if (!StringUtil.isEmptyOrNull(groupID)) {
                            startGroupChat(groupID);
                        }
                    }
                } else {
                    ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                    if (sessionBuddy != null && sessionBuddy.getAccountStatus() == 0) {
                        startOneToOneChat(sessionBuddy);
                    }
                }
            }
        }
    }

    private void startOneToOneChat(ZoomBuddy zoomBuddy) {
        MMSelectSessionFragment mMSelectSessionFragment = this.mParentFragment;
        if (mMSelectSessionFragment != null) {
            mMSelectSessionFragment.startOneToOneChat(zoomBuddy);
        }
    }

    private void startGroupChat(String str) {
        MMSelectSessionFragment mMSelectSessionFragment = this.mParentFragment;
        if (mMSelectSessionFragment != null) {
            mMSelectSessionFragment.startGroupChat(str);
        }
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
                switch (groupAction.getActionType()) {
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        this.mAvatarCache.removeItem(groupId);
                        break;
                }
                if (isParentFragmentResumed()) {
                    this.mAdapter.notifyDataSetChanged();
                }
            }
        }
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
            this.mAvatarCache.removeItem(str);
            if (isParentFragmentResumed()) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }
}
