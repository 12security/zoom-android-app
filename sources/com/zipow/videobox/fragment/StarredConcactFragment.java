package com.zipow.videobox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.StarredMessageActivity;
import com.zipow.videobox.ptapp.IMProtos.SessionMessageInfoMap;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ZMIMUtils;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import com.zipow.videobox.view.p014mm.MMContentFragment;
import com.zipow.videobox.view.p014mm.MMZoomGroup;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.SortUtil;
import p021us.zoom.videomeetings.C4558R;

public class StarredConcactFragment extends ZMDialogFragment {
    public static final int TOTAL_TYPE_COUNT = 8;
    public static final int TYPE_ANNOUNCEMENT = 3;
    public static final int TYPE_CONTACT_REQUEST = 4;
    public static final int TYPE_CONTENT = 2;
    public static final int TYPE_MESSAGE = 1;
    public static final int TYPE_NOTES = 5;
    public static final int TYPE_STARRED = 6;
    public static final int TYPE_SUGGEST = 7;
    @NonNull
    private List<ConcactItem> concactItemss = new ArrayList();
    private ListView listView;
    @Nullable
    private StarredConcactAdapter mAdapter;
    /* access modifiers changed from: private */
    @NonNull
    public List<String> mLoadedConcactItems = new ArrayList();
    @NonNull
    private INotificationSettingUIListener mNotificationSettingUIListener = new SimpleNotificationSettingUIListener() {
        public void OnUnreadBadgeSettingUpdated() {
            StarredConcactFragment.this.updateUI();
        }
    };
    @NonNull
    private IZoomMessengerUIListener zoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void Indicate_BuddyPresenceChanged(String str) {
            StarredConcactFragment.this.updateUI();
        }

        public void onIndicateInfoUpdatedWithJID(String str) {
            StarredConcactFragment.this.updateUI();
        }

        public void notifyStarSessionDataUpdate() {
            super.notifyStarSessionDataUpdate();
            StarredConcactFragment.this.updateData();
            StarredConcactFragment.this.updateUI();
        }

        public void onNotify_ChatSessionUnreadUpdate(String str) {
            StarredConcactFragment.this.updateUI();
        }

        public void notify_ChatSessionResetUnreadCount(String str) {
            StarredConcactFragment.this.updateUI();
        }

        public void Notify_ChatSessionMarkUnreadUpdate(SessionMessageInfoMap sessionMessageInfoMap) {
            StarredConcactFragment.this.updateUI();
        }

        public void onNotify_ChatSessionListUpdate() {
            StarredConcactFragment.this.updateUI();
        }

        public void On_BroadcastUpdate(int i, String str, boolean z) {
            if (i == 3) {
                super.notifyStarSessionDataUpdate();
                StarredConcactFragment.this.updateData();
                StarredConcactFragment.this.updateUI();
            }
        }

        public void indicate_BuddyBlockedByIB(List<String> list) {
            StarredConcactFragment.this.updateData();
            StarredConcactFragment.this.updateUI();
        }
    };

    class ConcactItem {
        /* access modifiers changed from: private */
        @Nullable
        public String sessionID;
        /* access modifiers changed from: private */
        @Nullable
        public String sortKey;
        /* access modifiers changed from: private */
        public int type;

        public ConcactItem(@Nullable IMAddrBookItem iMAddrBookItem, int i) {
            if (iMAddrBookItem != null) {
                this.sessionID = iMAddrBookItem.getJid();
                this.type = i;
                this.sortKey = iMAddrBookItem.getSortKey();
            }
        }

        public ConcactItem(@Nullable MMZoomGroup mMZoomGroup, int i) {
            this.type = i;
            if (mMZoomGroup != null) {
                this.sessionID = mMZoomGroup.getGroupId();
                this.sortKey = mMZoomGroup.getSortKey();
            }
        }

        public ConcactItem(@Nullable ZoomBuddy zoomBuddy, int i) {
            this.type = i;
            if (zoomBuddy != null) {
                this.sessionID = zoomBuddy.getJid();
                if (!zoomBuddy.isPending() || zoomBuddy.isRobot()) {
                    this.sortKey = SortUtil.getSortKey(zoomBuddy.getScreenName(), CompatUtils.getLocalDefault());
                    if (zoomBuddy.isRobot()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append('\"');
                        sb.append(this.sortKey);
                        this.sortKey = sb.toString();
                        return;
                    }
                    return;
                }
                this.sortKey = zoomBuddy.getEmail();
            }
        }
    }

    class StarredConcactAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<ConcactItem> list;
        private Context mContext;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getViewTypeCount() {
            return 8;
        }

        public List<ConcactItem> getList() {
            return this.list;
        }

        public StarredConcactAdapter(Context context, List<ConcactItem> list2) {
            this.mContext = context;
            this.list = list2;
            this.inflater = LayoutInflater.from(context);
        }

        public int getCount() {
            return this.list.size();
        }

        public Object getItem(int i) {
            return this.list.get(i);
        }

        public int getItemViewType(int i) {
            return ((ConcactItem) getItem(i)).type;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            switch (getItemViewType(i)) {
                case 1:
                    return StarredConcactFragment.this.bindMessagesItemView(this.mContext, i, view, viewGroup, this.list);
                case 2:
                    return StarredConcactFragment.this.bindContentItemView(this.mContext, view, viewGroup);
                case 3:
                case 4:
                case 5:
                case 6:
                    return StarredConcactFragment.this.bindStarredItemView(this.mContext, i, view, viewGroup, this.list);
                default:
                    return new View(this.mContext);
            }
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_starred_contact, viewGroup, false);
        this.listView = (ListView) inflate.findViewById(C4558R.C4560id.zm_fragment_starred_contact_listView);
        this.listView.setEmptyView(inflate.findViewById(C4558R.C4560id.zm_fragment_starred_contact_emptyView));
        ZoomMessengerUI.getInstance().addListener(this.zoomMessengerUIListener);
        NotificationSettingUI.getInstance().addListener(this.mNotificationSettingUIListener);
        return inflate;
    }

    public void onStart() {
        super.onStart();
        updateData();
        updateUI();
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mAdapter = new StarredConcactAdapter(getContext(), this.concactItemss);
        this.listView.setAdapter(this.mAdapter);
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(@NonNull AdapterView<?> adapterView, @NonNull View view, int i, long j) {
                ConcactItem concactItem = (ConcactItem) ((StarredConcactAdapter) adapterView.getAdapter()).getItem(i);
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                switch (concactItem.type) {
                    case 1:
                        StarredMessageActivity.launch(view.getContext(), "");
                        return;
                    case 2:
                        MMContentFragment.showAsActivity((ZMActivity) StarredConcactFragment.this.getActivity());
                        return;
                    case 3:
                    case 6:
                        if (zoomMessenger != null) {
                            ZoomChatSession sessionById = zoomMessenger.getSessionById(concactItem.sessionID);
                            if (sessionById == null) {
                                return;
                            }
                            if (sessionById.isGroup()) {
                                MMChatActivity.showAsGroupChat((ZMActivity) StarredConcactFragment.this.getActivity(), concactItem.sessionID);
                                return;
                            }
                            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy());
                            if (fromZoomBuddy == null) {
                                return;
                            }
                            if (fromZoomBuddy.isZoomRoomContact()) {
                                StarredConcactFragment.this.showUserActions(fromZoomBuddy);
                                return;
                            } else {
                                MMChatActivity.showAsOneToOneChat((ZMActivity) StarredConcactFragment.this.getActivity(), fromZoomBuddy, fromZoomBuddy.getJid());
                                return;
                            }
                        } else {
                            return;
                        }
                    case 4:
                        ZMActivity zMActivity = (ZMActivity) StarredConcactFragment.this.getContext();
                        if (zMActivity != null) {
                            SystemNotificationFragment.showAsActivity(zMActivity, 0);
                            return;
                        }
                        return;
                    case 5:
                        if (zoomMessenger != null) {
                            MMChatActivity.showAsOneToOneChat((ZMActivity) StarredConcactFragment.this.getActivity(), zoomMessenger.getMyself());
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        });
        this.listView.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if (i == 0) {
                    StarredConcactFragment.this.refreshBuddyVCard();
                    if (!CollectionsUtil.isListEmpty(StarredConcactFragment.this.mLoadedConcactItems)) {
                        StarredConcactFragment.this.mLoadedConcactItems.clear();
                    }
                }
            }

            public void onScroll(AbsListView absListView, int i, int i2, int i3) {
                if (i == 0 && i2 > 0) {
                    StarredConcactFragment.this.refreshBuddyVCard();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void refreshBuddyVCard() {
        if (!CollectionsUtil.isListEmpty(this.mLoadedConcactItems) && this.listView != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.refreshBuddyVCards(this.mLoadedConcactItems);
            }
        }
    }

    public void showUserActions(@Nullable IMAddrBookItem iMAddrBookItem) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null && PTApp.getInstance().getZoomMessenger() != null) {
            if (iMAddrBookItem == null || !iMAddrBookItem.getIsRobot()) {
                AddrBookItemDetailsActivity.show(zMActivity, iMAddrBookItem, 106);
            } else {
                MMChatActivity.showAsOneToOneChat(zMActivity, iMAddrBookItem, iMAddrBookItem.getJid());
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateData() {
        this.concactItemss.clear();
        List buildConcactList = buildConcactList();
        if (buildConcactList != null) {
            this.concactItemss.addAll(buildConcactList);
        }
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        StarredConcactAdapter starredConcactAdapter = this.mAdapter;
        if (starredConcactAdapter != null) {
            starredConcactAdapter.notifyDataSetChanged();
        }
    }

    private List<ConcactItem> buildConcactList() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomBuddy myself = zoomMessenger.getMyself();
        if (myself == null) {
            return null;
        }
        List starSessionGetAll = zoomMessenger.starSessionGetAll();
        ArrayList arrayList = new ArrayList();
        Map starMessageGetAll = zoomMessenger.starMessageGetAll();
        if (starMessageGetAll != null && !starMessageGetAll.isEmpty()) {
            arrayList.add(new ConcactItem((ZoomBuddy) null, 1));
        }
        if (zoomMessenger.imChatGetOption() != 2) {
            boolean z = zoomMessenger.e2eGetMyOption() == 2;
            MMFileContentMgr zoomFileContentMgr = PTApp.getInstance().getZoomFileContentMgr();
            boolean z2 = zoomFileContentMgr != null && zoomFileContentMgr.getFileContentMgmtOption() == 1;
            if (!z && z2 && !PTApp.getInstance().isFileTransferDisabled()) {
                arrayList.add(new ConcactItem(myself, 2));
            }
        }
        if (!zoomMessenger.isUnstarredContactRequests()) {
            String contactRequestsSessionID = zoomMessenger.getContactRequestsSessionID();
            IMAddrBookItem iMAddrBookItem = new IMAddrBookItem();
            iMAddrBookItem.setJid(contactRequestsSessionID);
            arrayList.add(new ConcactItem(iMAddrBookItem, 4));
        }
        if (zoomMessenger.isUnstarredAnnouncement() && !CollectionsUtil.isListEmpty(zoomMessenger.getBroadcast())) {
            String str = (String) zoomMessenger.getBroadcast().get(0);
            IMAddrBookItem iMAddrBookItem2 = new IMAddrBookItem();
            iMAddrBookItem2.setJid(str);
            arrayList.add(new ConcactItem(iMAddrBookItem2, 3));
        }
        if (starSessionGetAll != null && !starSessionGetAll.isEmpty()) {
            for (int i = 0; i < starSessionGetAll.size(); i++) {
                String str2 = (String) starSessionGetAll.get(i);
                if (!ZMIMUtils.isAnnouncement(str2)) {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(str2);
                    if (sessionById != null) {
                        if (sessionById.isGroup()) {
                            ZoomGroup sessionGroup = sessionById.getSessionGroup();
                            if (sessionGroup != null) {
                                arrayList.add(new ConcactItem(MMZoomGroup.initWithZoomGroup(sessionGroup), 6));
                            }
                        } else if (TextUtils.equals(myself.getJid(), str2)) {
                            arrayList.add(new ConcactItem(myself, 5));
                        } else {
                            ZoomBuddy sessionBuddy = sessionById.getSessionBuddy();
                            if (sessionBuddy != null && !sessionBuddy.isIMBlockedByIB()) {
                                arrayList.add(new ConcactItem(sessionBuddy, 6));
                            }
                        }
                    }
                }
            }
        }
        if (arrayList.size() > 1) {
            Collections.sort(arrayList, new Comparator<ConcactItem>() {
                public int compare(@Nullable ConcactItem concactItem, @Nullable ConcactItem concactItem2) {
                    if (concactItem == null || concactItem2 == null) {
                        return 0;
                    }
                    if (concactItem.type != concactItem2.type) {
                        return concactItem.type - concactItem2.type;
                    }
                    if (TextUtils.isEmpty(concactItem.sortKey) || TextUtils.isEmpty(concactItem2.sortKey)) {
                        return 0;
                    }
                    return SortUtil.fastCompare(concactItem.sortKey, concactItem2.sortKey);
                }
            });
        }
        return arrayList;
    }

    public void onDestroyView() {
        super.onDestroyView();
        ZoomMessengerUI.getInstance().removeListener(this.zoomMessengerUIListener);
        NotificationSettingUI.getInstance().removeListener(this.mNotificationSettingUIListener);
    }

    /* access modifiers changed from: private */
    @Nullable
    public View bindMessagesItemView(Context context, int i, @Nullable View view, ViewGroup viewGroup, @Nullable List<ConcactItem> list) {
        if (list == null || i >= list.size()) {
            return null;
        }
        if (view == null) {
            view = LayoutInflater.from(context).inflate(C4558R.layout.zm_fragment_starred_list_item_messages, viewGroup, false);
        }
        ConcactItem concactItem = (ConcactItem) list.get(i);
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.zm_starred_list_item_Name);
        ((AvatarView) view.findViewById(C4558R.C4560id.zm_starred_list_item_avatarView)).show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_starred_message_avatar, null));
        textView.setText(C4558R.string.zm_mm_lbl_group_starred_message_owp40);
        return view;
    }

    /* access modifiers changed from: private */
    @NonNull
    public View bindContentItemView(Context context, @Nullable View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(C4558R.layout.zm_fragment_starred_list_item_messages, viewGroup, false);
        }
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.zm_starred_list_item_Name);
        ((AvatarView) view.findViewById(C4558R.C4560id.zm_starred_list_item_avatarView)).show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_contents_avatar, null));
        textView.setText(C4558R.string.zm_mm_lbl_group_all_files_52777);
        return view;
    }

    /* access modifiers changed from: private */
    @Nullable
    public View bindStarredItemView(@NonNull Context context, int i, @Nullable View view, ViewGroup viewGroup, @Nullable List<ConcactItem> list) {
        View view2;
        int i2;
        LinearLayout linearLayout;
        int i3;
        String str;
        String str2;
        Context context2 = context;
        int i4 = i;
        List<ConcactItem> list2 = list;
        if (list2 == null || i4 >= list.size()) {
            return null;
        }
        View inflate = view == null ? LayoutInflater.from(context).inflate(C4558R.layout.zm_fragment_starred_list_item, viewGroup, false) : view;
        final ConcactItem concactItem = (ConcactItem) list2.get(i4);
        LinearLayout linearLayout2 = (LinearLayout) inflate.findViewById(C4558R.C4560id.zm_starred_list_item_suggested_linear);
        AvatarView avatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.zm_starred_list_item_avatarView);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.zm_starred_list_item_Name);
        ImageView imageView = (ImageView) inflate.findViewById(C4558R.C4560id.zm_starred_list_item_star_btn);
        imageView.setImageResource(C4558R.C4559drawable.zm_mm_starred_icon_on);
        PresenceStateView presenceStateView = (PresenceStateView) inflate.findViewById(C4558R.C4560id.presenceStateView);
        presenceStateView.setmTxtDeviceTypeGone();
        presenceStateView.setVisibility(8);
        View findViewById = inflate.findViewById(C4558R.C4560id.unreadBubble);
        findViewById.setVisibility(8);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txtNoteBubble);
        textView2.setVisibility(8);
        ImageView imageView2 = (ImageView) inflate.findViewById(C4558R.C4560id.imgErrorMessage);
        TextView textView3 = (TextView) inflate.findViewById(C4558R.C4560id.txtAt);
        textView3.setVisibility(8);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself == null) {
                return null;
            }
            boolean hasFailedMessage = zoomMessenger.hasFailedMessage(concactItem.sessionID != null ? concactItem.sessionID : "");
            view2 = inflate;
            if (TextUtils.equals(concactItem.sessionID, myself.getJid())) {
                IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(myself);
                if (fromZoomBuddy != null) {
                    textView.setEllipsize(TruncateAt.MIDDLE);
                    textView.setText(context2.getString(C4558R.string.zm_mm_msg_my_notes_65147, new Object[]{fromZoomBuddy.getScreenName()}));
                    avatarView.show(fromZoomBuddy.getAvatarParamsBuilder());
                }
                imageView.setImageResource(C4558R.C4559drawable.zm_starred_message_arrow);
                linearLayout = linearLayout2;
                i2 = 8;
                i3 = 0;
            } else {
                linearLayout = linearLayout2;
                if (concactItem.type == 4) {
                    textView.setText(getString(C4558R.string.zm_contact_requests_83123));
                    avatarView.show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_im_contact_request, null));
                    imageView.setContentDescription(context2.getString(C4558R.string.zm_unstar_contact_requests_83123));
                    int unreadRequestCount = zoomMessenger.getUnreadRequestCount();
                    if (unreadRequestCount == 0) {
                        textView2.setVisibility(8);
                    } else {
                        if (unreadRequestCount > 99) {
                            str2 = "99+";
                        } else {
                            str2 = String.valueOf(unreadRequestCount);
                        }
                        textView2.setText(str2);
                        textView2.setVisibility(0);
                        textView2.setContentDescription(context.getResources().getQuantityString(C4558R.plurals.zm_msg_notification_unread_num_8295, unreadRequestCount, new Object[]{"", Integer.valueOf(unreadRequestCount)}));
                    }
                    i2 = 8;
                    i3 = 0;
                } else {
                    ZoomChatSession sessionById = zoomMessenger.getSessionById(concactItem.sessionID);
                    if (sessionById != null) {
                        textView.setEllipsize(TruncateAt.END);
                        if (sessionById.isGroup()) {
                            if (concactItem.type == 3) {
                                textView.setText(getString(C4558R.string.zm_msg_announcements_108966));
                                avatarView.show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_announcement, null));
                                imageView.setContentDescription(context2.getString(C4558R.string.zm_unstar_announcements_108966));
                            } else {
                                imageView.setContentDescription(context2.getString(C4558R.string.zm_accessibility_unstarred_channel_62483));
                                ZoomGroup sessionGroup = sessionById.getSessionGroup();
                                if (sessionGroup != null) {
                                    textView.setText(MMZoomGroup.initWithZoomGroup(sessionGroup).getGroupName());
                                    avatarView.show(new ParamsBuilder().setResource(C4558R.C4559drawable.zm_ic_avatar_group, null));
                                }
                            }
                            if (sessionById.hasUnreadMessageAtMe() || sessionById.hasUnreadedMessageAtAllMembers()) {
                                String string = context2.getString(sessionById.hasUnreadMessageAtMe() ? C4558R.string.zm_mm_msg_at_me_104608 : C4558R.string.zm_mm_msg_at_all_104608);
                                textView3.setVisibility(0);
                                textView3.setText(string);
                            }
                        } else {
                            presenceStateView.setVisibility(0);
                            IMAddrBookItem fromZoomBuddy2 = IMAddrBookItem.fromZoomBuddy(sessionById.getSessionBuddy());
                            if (fromZoomBuddy2 != null) {
                                this.mLoadedConcactItems.remove(fromZoomBuddy2.getJid());
                                this.mLoadedConcactItems.add(fromZoomBuddy2.getJid());
                                if (fromZoomBuddy2.isZoomRoomContact()) {
                                    imageView.setContentDescription(context2.getString(C4558R.string.zm_accessibility_unstarred_room_62483));
                                } else {
                                    imageView.setContentDescription(context2.getString(C4558R.string.zm_accessibility_unstarred_contact_62483));
                                }
                                textView.setText(fromZoomBuddy2.getScreenName());
                                avatarView.show(fromZoomBuddy2.getAvatarParamsBuilder());
                                presenceStateView.setState(fromZoomBuddy2);
                            }
                        }
                        int unreadMessageCountBySetting = sessionById.getUnreadMessageCountBySetting();
                        int unreadMessageCount = sessionById.getUnreadMessageCount();
                        int markUnreadMessageCount = sessionById.getMarkUnreadMessageCount();
                        if (unreadMessageCountBySetting != 0 || unreadMessageCount <= 0 || markUnreadMessageCount > 0 || !sessionById.isGroup()) {
                            findViewById.setVisibility(8);
                        } else {
                            findViewById.setContentDescription(context.getResources().getString(C4558R.string.zm_mm_lbl_new_message_14491));
                            findViewById.setVisibility(0);
                        }
                        if (!sessionById.isGroup()) {
                            unreadMessageCountBySetting = unreadMessageCount;
                        }
                        int i5 = unreadMessageCountBySetting + markUnreadMessageCount;
                        if (i5 == 0) {
                            i3 = 0;
                            i2 = 8;
                        } else if (hasFailedMessage) {
                            i2 = 8;
                            i3 = 0;
                        } else {
                            if (i5 > 99) {
                                str = "99+";
                            } else {
                                str = String.valueOf(i5);
                            }
                            textView2.setText(str);
                            i3 = 0;
                            textView2.setVisibility(0);
                            textView2.setContentDescription(context.getResources().getQuantityString(C4558R.plurals.zm_msg_notification_unread_num_8295, i5, new Object[]{"", Integer.valueOf(i5)}));
                            i2 = 8;
                        }
                        textView2.setVisibility(i2);
                    } else {
                        i2 = 8;
                        i3 = 0;
                    }
                }
            }
            if (!hasFailedMessage) {
                i3 = 8;
            }
            imageView2.setVisibility(i3);
            linearLayout2 = linearLayout;
        } else {
            view2 = inflate;
            i2 = 8;
        }
        linearLayout2.setVisibility(i2);
        imageView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null && zoomMessenger.starSessionSetStar(concactItem.sessionID, !zoomMessenger.isStarSession(concactItem.sessionID))) {
                    StarredConcactFragment.this.updateData();
                    StarredConcactFragment.this.updateUI();
                }
            }
        });
        return view2;
    }
}
