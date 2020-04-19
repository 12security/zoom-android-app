package com.zipow.videobox.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ZoomChatInWebinar;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.fragment.ConfChatBuddyChooseFragment;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ZMConfUtil;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatBuddyListView extends ListView {
    private static final int DEFALUT_ROLE = -1;
    private static final int MSG_REFRESH = 1;
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                if (ConfChatBuddyListView.this.needRefresh) {
                    ConfChatBuddyListView.this.loadAllItems();
                    ConfChatBuddyListView.this.needRefresh = false;
                }
                sendEmptyMessageDelayed(1, 1000);
            }
        }
    };
    private WebinarChatBuddyAdapter mWebinarChatBuddyAdapter;
    /* access modifiers changed from: private */
    public boolean needRefresh = false;

    static class WebinarChatBuddyAdapter extends BaseAdapter {
        private Context mContext;
        @NonNull
        private List<ConfChatAttendeeItem> mitems = new ArrayList();

        public long getItemId(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 2;
        }

        WebinarChatBuddyAdapter(Context context) {
            this.mContext = context;
        }

        public void clearAll() {
            this.mitems.clear();
        }

        public int getCount() {
            return this.mitems.size();
        }

        public void addWebinarAttendeeItem(@Nullable ConfChatAttendeeItem confChatAttendeeItem) {
            if (confChatAttendeeItem != null) {
                this.mitems.add(confChatAttendeeItem);
            }
        }

        public Object getItem(int i) {
            return this.mitems.get(i);
        }

        @NonNull
        private View newGroupHeaderView(int i, String str, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"us.zoom.androidlib.widget.QuickSearchListView.header".equals(view.getTag())) {
                view = LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_quick_search_list_items_header, viewGroup, false);
                view.setTag("us.zoom.androidlib.widget.QuickSearchListView.header");
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtHeader);
            if (textView != null) {
                textView.setText(str);
            }
            return view;
        }

        @NonNull
        private View bindView(int i, @NonNull ConfChatAttendeeItem confChatAttendeeItem, @Nullable View view, ViewGroup viewGroup) {
            if (view == null || !"webinarattendees".equals(view.getTag())) {
                view = View.inflate(this.mContext, C4558R.layout.zm_qa_webinar_attendee_item, null);
                view.setTag("webinarattendees");
            }
            if (!"webinarattendees".equals(view.getTag())) {
                LayoutInflater.from(this.mContext).inflate(C4558R.layout.zm_quick_search_list_items_header, viewGroup, false).setTag("webinarattendees");
            }
            TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtRole);
            ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.imgRaiseHand);
            ((TextView) view.findViewById(C4558R.C4560id.txtName)).setText(getItemName(confChatAttendeeItem));
            imageView.setVisibility(8);
            view.setBackgroundResource(C4558R.color.zm_transparent);
            if (confChatAttendeeItem.nodeID == 0 || confChatAttendeeItem.nodeID == 1) {
                textView.setVisibility(8);
            } else {
                CmmUser userById = ConfMgr.getInstance().getUserById(confChatAttendeeItem.nodeID);
                if (userById == null) {
                    ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(confChatAttendeeItem.nodeID);
                    if (zoomQABuddyByNodeId != null && ConfLocalHelper.isGuest(zoomQABuddyByNodeId) && !ConfLocalHelper.isGuestForMyself()) {
                        view.setBackgroundResource(C4558R.C4559drawable.zm_list_selector_guest);
                    }
                    return view;
                }
                textView.setVisibility(0);
                if (ConfLocalHelper.isGuest(userById) && !ConfLocalHelper.isGuestForMyself()) {
                    view.setBackgroundResource(C4558R.C4559drawable.zm_list_selector_guest);
                }
                if (ConfUI.getInstance().isDisplayAsHost(confChatAttendeeItem.nodeID)) {
                    textView.setText(this.mContext.getResources().getString(C4558R.string.zm_lbl_role_host));
                } else if (ConfUI.getInstance().isDisplayAsCohost(confChatAttendeeItem.nodeID)) {
                    textView.setText(this.mContext.getResources().getString(C4558R.string.zm_lbl_role_cohost));
                } else {
                    textView.setVisibility(8);
                }
            }
            return view;
        }

        public int getItemViewType(int i) {
            ConfChatAttendeeItem confChatAttendeeItem = (ConfChatAttendeeItem) getItem(i);
            return (confChatAttendeeItem == null || confChatAttendeeItem.nodeID == -1) ? 1 : 0;
        }

        @Nullable
        public View getView(int i, View view, ViewGroup viewGroup) {
            String str;
            ConfChatAttendeeItem confChatAttendeeItem = (ConfChatAttendeeItem) getItem(i);
            if (getItemViewType(i) != 1) {
                return bindView(i, confChatAttendeeItem, view, viewGroup);
            }
            if (confChatAttendeeItem == null) {
                str = "";
            } else {
                str = getItemName(confChatAttendeeItem);
            }
            return newGroupHeaderView(i, str, view, viewGroup);
        }

        private String getItemName(ConfChatAttendeeItem confChatAttendeeItem) {
            if (confChatAttendeeItem.fakeItemType != 1 || !ConfLocalHelper.canChatWithSilentModePeople()) {
                return confChatAttendeeItem.name;
            }
            return this.mContext.getString(C4558R.string.zm_mi_everyone_in_meeting_122046);
        }
    }

    public ConfChatBuddyListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public ConfChatBuddyListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public ConfChatBuddyListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mWebinarChatBuddyAdapter = new WebinarChatBuddyAdapter(getContext());
        setAdapter(this.mWebinarChatBuddyAdapter);
        loadAllItems();
        this.mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeMessages(1);
        super.onDetachedFromWindow();
    }

    public void onUserAdded(String str) {
        this.needRefresh = true;
    }

    public void onUserRemoved(String str) {
        this.needRefresh = true;
    }

    public void onChattedAttendeeUpdated(long j) {
        this.needRefresh = true;
    }

    public boolean onUserEvent(int i, long j, int i2) {
        this.needRefresh = true;
        return true;
    }

    public void onAttendeeStatusChange() {
        this.needRefresh = true;
    }

    public void onHostOrCoHostChanged() {
        this.needRefresh = true;
    }

    public void onUserGuestStatusChanged() {
        this.needRefresh = true;
    }

    public void loadAllItems() {
        boolean z;
        boolean z2;
        ZoomQAComponent zoomQAComponent;
        ConfMgr instance = ConfMgr.getInstance();
        CmmConfContext confContext = instance.getConfContext();
        if (confContext != null) {
            CmmConfStatus confStatusObj = instance.getConfStatusObj();
            if (confStatusObj != null) {
                BOMgr bOMgr = instance.getBOMgr();
                int i = 0;
                boolean isInBOMeeting = bOMgr != null ? bOMgr.isInBOMeeting() : false;
                this.mWebinarChatBuddyAdapter.clearAll();
                boolean isWebinar = confContext.isWebinar();
                int attendeeChatPriviledge = instance.getConfStatusObj().getAttendeeChatPriviledge();
                if (isWebinar) {
                    ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
                    z = qAComponent == null || qAComponent.isWebinarAttendee();
                } else {
                    CmmUser myself = ConfMgr.getInstance().getMyself();
                    z = myself != null ? !myself.isHost() && !myself.isCoHost() : false;
                }
                ArrayList<ConfChatAttendeeItem> arrayList = new ArrayList<>();
                if (attendeeChatPriviledge == 3) {
                    if (!z) {
                        checkLoadEveryoneInWaitingRoom();
                        checkLoadEveryoneInMeeting();
                    }
                    CmmUserList userList = instance.getUserList();
                    if (userList != null) {
                        ZoomQAComponent qAComponent2 = instance.getQAComponent();
                        if (qAComponent2 != null) {
                            int userCount = userList.getUserCount();
                            while (i < userCount) {
                                CmmUser userAt = userList.getUserAt(i);
                                if (userAt == null) {
                                    zoomQAComponent = qAComponent2;
                                } else {
                                    long nodeId = userAt.getNodeId();
                                    if (z && !userAt.isHost() && !userAt.isCoHost()) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (userAt.isMMRUser()) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (confStatusObj.isMyself(nodeId)) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (userAt.isH323User()) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (userAt.isPureCallInUser()) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (userAt.inSilentMode()) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (userAt.isViewOnlyUserCanTalk()) {
                                        zoomQAComponent = qAComponent2;
                                    } else if (isInBOMeeting || !userAt.isInBOMeeting()) {
                                        String screenName = userAt.getScreenName();
                                        String userJIDByNodeID = qAComponent2.getUserJIDByNodeID(nodeId);
                                        zoomQAComponent = qAComponent2;
                                        ConfChatAttendeeItem confChatAttendeeItem = r10;
                                        ConfChatAttendeeItem confChatAttendeeItem2 = new ConfChatAttendeeItem(screenName, userJIDByNodeID, nodeId, -1);
                                        arrayList.add(confChatAttendeeItem);
                                    } else {
                                        zoomQAComponent = qAComponent2;
                                    }
                                }
                                i++;
                                qAComponent2 = zoomQAComponent;
                            }
                            if (arrayList.size() != 0) {
                                for (ConfChatAttendeeItem addWebinarAttendeeItem : arrayList) {
                                    this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(addWebinarAttendeeItem);
                                }
                            }
                            this.mWebinarChatBuddyAdapter.notifyDataSetChanged();
                            return;
                        }
                        return;
                    }
                    return;
                }
                if (attendeeChatPriviledge == 2) {
                    if (!z || instance.isAllowAttendeeChat()) {
                        ConfChatAttendeeItem confChatAttendeeItem3 = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_webinar_txt_all_panelists), null, 1, -1);
                        this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem3);
                        if (!z) {
                            ConfChatAttendeeItem confChatAttendeeItem4 = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_mi_panelists_and_attendees_11380), null, 0, -1);
                            this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem4);
                        }
                    } else {
                        closeChatOrClearChatList();
                        return;
                    }
                } else if (attendeeChatPriviledge == 1) {
                    if (!isWebinar) {
                        checkLoadEveryoneInWaitingRoom();
                        checkLoadEveryoneInMeeting();
                    } else if (!z || instance.isAllowAttendeeChat()) {
                        ConfChatAttendeeItem confChatAttendeeItem5 = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_webinar_txt_all_panelists), null, 1, -1);
                        this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem5);
                        ConfChatAttendeeItem confChatAttendeeItem6 = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_mi_panelists_and_attendees_11380), null, 0, -1);
                        this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem6);
                    } else {
                        closeChatOrClearChatList();
                        return;
                    }
                } else if (attendeeChatPriviledge == 4) {
                    if (z) {
                        closeChatOrClearChatList();
                        return;
                    } else {
                        checkLoadEveryoneInWaitingRoom();
                        checkLoadEveryoneInMeeting();
                    }
                } else if (attendeeChatPriviledge == 5) {
                    checkLoadEveryoneInWaitingRoom();
                    checkLoadEveryoneInMeeting();
                } else {
                    checkLoadEveryoneInWaitingRoom();
                }
                ZoomQAComponent qAComponent3 = ConfMgr.getInstance().getQAComponent();
                if (qAComponent3 == null || qAComponent3.isWebinarAttendee()) {
                    this.mWebinarChatBuddyAdapter.notifyDataSetChanged();
                    return;
                }
                CmmUserList userList2 = instance.getUserList();
                if (userList2 != null) {
                    CmmConfStatus confStatusObj2 = instance.getConfStatusObj();
                    if (confStatusObj2 != null) {
                        ZoomQAComponent qAComponent4 = instance.getQAComponent();
                        if (qAComponent4 != null) {
                            int userCount2 = userList2.getUserCount();
                            while (i < userCount2) {
                                CmmUser userAt2 = userList2.getUserAt(i);
                                if (userAt2 == null) {
                                    z2 = z;
                                } else {
                                    z2 = z;
                                    long nodeId2 = userAt2.getNodeId();
                                    if (!userAt2.isMMRUser() && !confStatusObj2.isMyself(nodeId2) && !userAt2.isH323User() && !userAt2.isPureCallInUser() && !userAt2.inSilentMode() && !userAt2.isViewOnlyUserCanTalk() && (isInBOMeeting || !userAt2.isInBOMeeting())) {
                                        ConfChatAttendeeItem confChatAttendeeItem7 = r15;
                                        ConfChatAttendeeItem confChatAttendeeItem8 = new ConfChatAttendeeItem(userAt2.getScreenName(), qAComponent4.getUserJIDByNodeID(nodeId2), nodeId2, -1);
                                        arrayList.add(confChatAttendeeItem7);
                                    }
                                }
                                i++;
                                z = z2;
                            }
                            boolean z3 = z;
                            if (arrayList.size() != 0) {
                                if (isWebinar) {
                                    ConfChatAttendeeItem confChatAttendeeItem9 = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_webinar_txt_lable_panelists), null, -1, -1);
                                    this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem9);
                                }
                                for (ConfChatAttendeeItem confChatAttendeeItem10 : arrayList) {
                                    CmmUser myself2 = ConfMgr.getInstance().getMyself();
                                    if (myself2 != null && !myself2.isHost() && !myself2.isCoHost()) {
                                        CmmUser userById = ConfMgr.getInstance().getUserById(confChatAttendeeItem10.nodeID);
                                        if (userById != null) {
                                            if (attendeeChatPriviledge == 5 && !userById.isHost() && !userById.isCoHost()) {
                                            }
                                        }
                                    }
                                    this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem10);
                                }
                            }
                            ZoomChatInWebinar chatInWebinar = instance.getChatInWebinar();
                            if (chatInWebinar == null) {
                                this.mWebinarChatBuddyAdapter.notifyDataSetChanged();
                                return;
                            }
                            if ((z3 && attendeeChatPriviledge == 1) || !z3) {
                                List<ZoomQABuddy> chattedAttendees = chatInWebinar.getChattedAttendees();
                                if (chattedAttendees != null) {
                                    if (chattedAttendees.size() > 0) {
                                        ConfChatAttendeeItem confChatAttendeeItem11 = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_webinar_txt_lable_attendees), null, -1, -1);
                                        this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem11);
                                    }
                                    for (ZoomQABuddy confChatAttendeeItem12 : chattedAttendees) {
                                        this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(new ConfChatAttendeeItem(confChatAttendeeItem12));
                                    }
                                }
                            }
                            this.mWebinarChatBuddyAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    private void checkLoadEveryoneInWaitingRoom() {
        if (ConfLocalHelper.canChatWithSilentModePeople()) {
            ConfChatAttendeeItem confChatAttendeeItem = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_mi_everyone_in_waiting_room_122046), null, 2, -1);
            this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem);
        }
    }

    private void checkLoadEveryoneInMeeting() {
        ConfChatAttendeeItem confChatAttendeeItem = new ConfChatAttendeeItem(getContext().getString(C4558R.string.zm_mi_everyone_122046), null, 0, -1);
        confChatAttendeeItem.setFakeItemType(1);
        this.mWebinarChatBuddyAdapter.addWebinarAttendeeItem(confChatAttendeeItem);
    }

    private void closeChatOrClearChatList() {
        Context context = getContext();
        if (context instanceof ZMActivity) {
            FragmentManager supportFragmentManager = ((ZMActivity) context).getSupportFragmentManager();
            if (supportFragmentManager != null) {
                ConfChatBuddyChooseFragment confChatBuddyChooseFragment = (ConfChatBuddyChooseFragment) supportFragmentManager.findFragmentByTag(ConfChatBuddyChooseFragment.class.getName());
                if (confChatBuddyChooseFragment != null) {
                    confChatBuddyChooseFragment.dismiss();
                }
            }
            return;
        }
        this.mWebinarChatBuddyAdapter.clearAll();
        this.mWebinarChatBuddyAdapter.notifyDataSetChanged();
    }
}
