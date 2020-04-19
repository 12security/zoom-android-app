package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomSubscribeRequest;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.IMAddrBookItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSystemNotificationListView */
public class MMSystemNotificationListView extends ListView implements OnItemLongClickListener, OnItemClickListener {
    private static final String TAG = "MMSystemNotificationListView";
    private MMNewFriendRequestsListAdapter mAdapter;
    /* access modifiers changed from: private */
    public boolean mIsImChatDisabled = false;
    private ZMDialogFragment mParentFragment;

    /* renamed from: com.zipow.videobox.view.mm.MMSystemNotificationListView$MMNewFriendRequestsListAdapter */
    private static class MMNewFriendRequestsListAdapter extends BaseAdapter {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int ITEM_TYPE_NORMAL = 0;
        @Nullable
        private Context mContext;
        @NonNull
        private List<ZoomSubscribeRequest> mItems = new ArrayList();
        private MMSystemNotificationListView mListView;

        public long getItemId(int i) {
            return (long) i;
        }

        public int getItemViewType(int i) {
            return 0;
        }

        public int getViewTypeCount() {
            return 1;
        }

        static {
            Class<MMSystemNotificationListView> cls = MMSystemNotificationListView.class;
        }

        public MMNewFriendRequestsListAdapter(@Nullable Context context, MMSystemNotificationListView mMSystemNotificationListView) {
            this.mContext = context;
            this.mListView = mMSystemNotificationListView;
        }

        public void clear() {
            this.mItems.clear();
        }

        public void addItem(ZoomSubscribeRequest zoomSubscribeRequest) {
            updateItem(zoomSubscribeRequest);
        }

        public void updateItem(@Nullable ZoomSubscribeRequest zoomSubscribeRequest) {
            if (zoomSubscribeRequest != null && zoomSubscribeRequest.getRequestStatus() != 3) {
                this.mItems.add(zoomSubscribeRequest);
            }
        }

        public int getCount() {
            return this.mItems.size();
        }

        @Nullable
        public ZoomSubscribeRequest getItem(int i) {
            if (i < 0 || i >= getCount()) {
                return null;
            }
            return (ZoomSubscribeRequest) this.mItems.get(i);
        }

        @Nullable
        public View getView(int i, @Nullable View view, ViewGroup viewGroup) {
            View view2;
            final int i2 = i;
            if (i2 < 0 || i2 >= getCount()) {
                return null;
            }
            ZoomSubscribeRequest item = getItem(i);
            if (view == null || !"MMSystemNotificationListViewItem".equals(view.getTag())) {
                view2 = View.inflate(this.mContext, C4558R.layout.zm_system_notification_item, null);
                view2.setTag("MMSystemNotificationListViewItem");
            } else {
                view2 = view;
            }
            AvatarView avatarView = (AvatarView) view2.findViewById(C4558R.C4560id.avatarView);
            TextView textView = (TextView) view2.findViewById(C4558R.C4560id.txtScreenName);
            TextView textView2 = (TextView) view2.findViewById(C4558R.C4560id.txtDescription);
            TextView textView3 = (TextView) view2.findViewById(C4558R.C4560id.txtEmail);
            View findViewById = view2.findViewById(C4558R.C4560id.btnAccept);
            View findViewById2 = view2.findViewById(C4558R.C4560id.btnDecline);
            View findViewById3 = view2.findViewById(C4558R.C4560id.txtDeclined);
            View findViewById4 = view2.findViewById(C4558R.C4560id.txtChat);
            View findViewById5 = view2.findViewById(C4558R.C4560id.txtpending);
            int requestStatus = item.getRequestStatus();
            int i3 = 8;
            findViewById3.setVisibility(requestStatus == 2 ? 0 : 8);
            findViewById4.setVisibility((this.mListView.mIsImChatDisabled || requestStatus != 1) ? 8 : 0);
            findViewById4.setEnabled(item.isMyBuddy());
            if (item.getRequestType() == 0) {
                findViewById.setVisibility(requestStatus == 0 ? 0 : 8);
                findViewById2.setVisibility(requestStatus == 0 ? 0 : 8);
                findViewById5.setVisibility(8);
                textView2.setText(requestStatus == 1 ? C4558R.string.zm_description_contact_request_accept_byme : C4558R.string.zm_description_recive_contact_request);
            } else {
                findViewById5.setVisibility(requestStatus == 0 ? 0 : 8);
                textView2.setText(requestStatus == 1 ? C4558R.string.zm_description_contact_request_accept_byother : C4558R.string.zm_description_sent_contact_request);
                findViewById.setVisibility(8);
                findViewById2.setVisibility(8);
            }
            IMAddrBookItem iMAddrBookItem = item.getIMAddrBookItem();
            if (iMAddrBookItem != null) {
                loadAvatar(iMAddrBookItem, avatarView);
                String accountEmail = ((iMAddrBookItem.isPending() || item.getRequestStatus() == 2) && item.getRequestType() != 0) ? iMAddrBookItem.getAccountEmail() : iMAddrBookItem.getScreenName();
                if (StringUtil.isEmptyOrNull(accountEmail)) {
                    accountEmail = iMAddrBookItem.getScreenName();
                }
                textView.setText(accountEmail);
                if (iMAddrBookItem.getAccountEmail() != null) {
                    i3 = 0;
                }
                textView3.setVisibility(i3);
                textView3.setText(iMAddrBookItem.getAccountEmail());
            }
            findViewById.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMNewFriendRequestsListAdapter.this.acknowledgeSubscription(i2, true);
                }
            });
            findViewById2.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    MMNewFriendRequestsListAdapter.this.acknowledgeSubscription(i2, false);
                }
            });
            return view2;
        }

        private void loadAvatar(@NonNull IMAddrBookItem iMAddrBookItem, @Nullable AvatarView avatarView) {
            if (avatarView != null && !avatarView.isInEditMode()) {
                avatarView.show(iMAddrBookItem.getAvatarParamsBuilder());
            }
        }

        /* access modifiers changed from: private */
        public void acknowledgeSubscription(int i, boolean z) {
            ZoomSubscribeRequest item = getItem(i);
            if (item != null) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (!zoomMessenger.isConnectionGood() || !NetworkUtil.hasDataNetwork(this.mContext)) {
                        showConnectionError(this.mContext);
                        return;
                    }
                    zoomMessenger.ackBuddySubscribe(item.getRequestJID(), z);
                    this.mListView.reloadAllItems();
                }
            }
        }

        private void showConnectionError(@Nullable Context context) {
            if (context instanceof Activity) {
                Toast.makeText(context, C4558R.string.zm_msg_disconnected_try_again, 1).show();
            }
        }
    }

    /* renamed from: com.zipow.videobox.view.mm.MMSystemNotificationListView$SubscribeRequestContextMenuItem */
    static class SubscribeRequestContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_DELETE = 0;

        public SubscribeRequestContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public MMSystemNotificationListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public MMSystemNotificationListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public MMSystemNotificationListView(Context context) {
        super(context);
        initView();
    }

    public void onZoomMessengerNotifySubscribeRequest() {
        reloadAllItems();
        if (isParentFragmentResumed()) {
            NotificationMgr.playNotificationVibrate(getContext());
        }
    }

    public void onIndicateBuddyListUpdated() {
        reloadAllItems();
    }

    public void onIndicateInfoUpdatedWithJID(String str) {
        reloadAllItems();
    }

    public void onNotifySubscribeRequestUpdated(String str) {
        reloadAllItems();
    }

    public void setParentFragment(ZMDialogFragment zMDialogFragment) {
        this.mParentFragment = zMDialogFragment;
    }

    public boolean isParentFragmentResumed() {
        ZMDialogFragment zMDialogFragment = this.mParentFragment;
        if (zMDialogFragment == null) {
            return false;
        }
        return zMDialogFragment.isResumed();
    }

    public void reloadAllItems() {
        MMNewFriendRequestsListAdapter mMNewFriendRequestsListAdapter = this.mAdapter;
        if (mMNewFriendRequestsListAdapter != null) {
            mMNewFriendRequestsListAdapter.clear();
            loadAllItems(this.mAdapter);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void initView() {
        this.mAdapter = new MMNewFriendRequestsListAdapter(getContext(), this);
        setAdapter(this.mAdapter);
        setOnItemLongClickListener(this);
        setOnItemClickListener(this);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || zoomMessenger.imChatGetOption() == 2) {
            this.mIsImChatDisabled = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void loadAllItems(@NonNull MMNewFriendRequestsListAdapter mMNewFriendRequestsListAdapter) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            int subscribeRequestCount = zoomMessenger.getSubscribeRequestCount();
            int unreadRequestCount = zoomMessenger.getUnreadRequestCount();
            for (int i = 0; i < subscribeRequestCount; i++) {
                ZoomSubscribeRequest subscribeRequestAt = zoomMessenger.getSubscribeRequestAt(i);
                if (!(subscribeRequestAt == null || subscribeRequestAt.getRequestStatus() == 3)) {
                    String requestJID = subscribeRequestAt.getRequestJID();
                    if (!StringUtil.isEmptyOrNull(requestJID)) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(requestJID);
                        if (buddyWithJID != null) {
                            if (StringUtil.isEmptyOrNull(buddyWithJID.getScreenName())) {
                                zoomMessenger.refreshBuddyVCard(requestJID, true);
                            }
                            subscribeRequestAt.setIMAddrBookItem(IMAddrBookItem.fromZoomBuddy(buddyWithJID));
                            subscribeRequestAt.setMyBuddy(zoomMessenger.isMyContact(requestJID));
                            mMNewFriendRequestsListAdapter.addItem(subscribeRequestAt);
                        }
                    }
                }
            }
            if (zoomMessenger.setAllRequestAsReaded() && unreadRequestCount > 0) {
                zoomMessenger.syncAllSubScribeReqAsReaded();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectDeleteRequest(@Nullable ZoomSubscribeRequest zoomSubscribeRequest) {
        if (zoomSubscribeRequest != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.deleteSubscribeRequest(zoomSubscribeRequest.getRequestIndex());
                reloadAllItems();
            }
        }
    }

    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long j) {
        final ZoomSubscribeRequest item = this.mAdapter.getItem(i);
        if (item == null) {
            return false;
        }
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity == null) {
            return false;
        }
        IMAddrBookItem iMAddrBookItem = item.getIMAddrBookItem();
        if (iMAddrBookItem == null) {
            return false;
        }
        ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
        ArrayList arrayList = new ArrayList();
        String screenName = iMAddrBookItem.getScreenName();
        arrayList.add(new SubscribeRequestContextMenuItem(zMActivity.getString(C4558R.string.zm_system_notification_delete_reqeust), 0));
        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
        ZMAlertDialog create = new Builder(zMActivity).setTitle((CharSequence) screenName).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                MMSystemNotificationListView.this.onSelectDeleteRequest(item);
            }
        }).create();
        create.setCanceledOnTouchOutside(true);
        create.show();
        return true;
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        if (!this.mIsImChatDisabled) {
            ZoomSubscribeRequest item = this.mAdapter.getItem(i);
            if (item != null && item.isMyBuddy() && item.getRequestStatus() == 1) {
                showChatUI(item);
            }
        }
    }

    private void showChatUI(@Nullable ZoomSubscribeRequest zoomSubscribeRequest) {
        if (zoomSubscribeRequest != null) {
            Context context = getContext();
            if (context instanceof ZMActivity) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(zoomSubscribeRequest.getRequestJID());
                    if (buddyWithJID != null) {
                        MMChatActivity.showAsOneToOneChat((ZMActivity) context, buddyWithJID);
                    }
                }
            }
        }
    }
}
