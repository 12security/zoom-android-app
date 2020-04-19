package com.zipow.videobox.view.p014mm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.fragment.IMAddrBookListFragment;
import com.zipow.videobox.fragment.InviteFragment.InviteFailedDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.AllBuddyInfo;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.QuickSearchListView;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMContactsRoomsListView */
public class MMContactsRoomsListView extends QuickSearchListView implements OnItemClickListener, OnScrollListener {
    private static final int MSG_REFRESH_BUDDY_VCARDS = 1;
    private static final String TAG = "MMContactsRoomsListView";
    private MMContactsRoomAdapter mAdapter;
    @NonNull
    private Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what == 1) {
                MMContactsRoomsListView.this.refreshBuddyVcards();
                sendEmptyMessageDelayed(1, 2000);
            }
        }
    };
    private IMAddrBookListFragment mParentFragment;

    /* renamed from: com.zipow.videobox.view.mm.MMContactsRoomsListView$RoomListContextMenuItem */
    public static class RoomListContextMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_AUDIO_CALL = 1;
        public static final int ACTION_INVITE_CALL = 2;
        public static final int ACTION_STAR_ROOM = 3;
        public static final int ACTION_UNSTAR_ROOM = 4;
        public static final int ACTION_VIDEO_CALL = 0;

        public RoomListContextMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
    }

    public MMContactsRoomsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public MMContactsRoomsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public MMContactsRoomsListView(Context context) {
        super(context);
        init();
    }

    private void init() {
        this.mAdapter = new MMContactsRoomAdapter(getContext());
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
        getListView().setOnScrollListener(this);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mHandler.sendEmptyMessageDelayed(1, 2000);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mHandler.removeMessages(1);
        super.onDetachedFromWindow();
    }

    public void filter(String str) {
        this.mAdapter.filter(str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003c  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x004f A[EDGE_INSN: B:30:0x004f->B:22:0x004f ?: BREAK  
    EDGE_INSN: B:30:0x004f->B:22:0x004f ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBuddyInfoUpdate(@androidx.annotation.Nullable java.util.List<java.lang.String> r4, @androidx.annotation.Nullable java.util.List<java.lang.String> r5) {
        /*
            r3 = this;
            com.zipow.videobox.ptapp.mm.ZMBuddySyncInstance r0 = com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance.getInsatance()
            boolean r1 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r4)
            r2 = 1
            if (r1 != 0) goto L_0x0029
            java.util.Iterator r4 = r4.iterator()
        L_0x000f:
            boolean r1 = r4.hasNext()
            if (r1 == 0) goto L_0x0029
            java.lang.Object r1 = r4.next()
            java.lang.String r1 = (java.lang.String) r1
            com.zipow.videobox.view.IMAddrBookItem r1 = r0.getBuddyByJid(r1)
            if (r1 == 0) goto L_0x000f
            boolean r1 = r1.isZoomRoomContact()
            if (r1 == 0) goto L_0x000f
            r4 = 1
            goto L_0x002a
        L_0x0029:
            r4 = 0
        L_0x002a:
            if (r4 != 0) goto L_0x004f
            boolean r1 = p021us.zoom.androidlib.util.CollectionsUtil.isListEmpty(r5)
            if (r1 != 0) goto L_0x004f
            java.util.Iterator r5 = r5.iterator()
        L_0x0036:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L_0x004f
            java.lang.Object r1 = r5.next()
            java.lang.String r1 = (java.lang.String) r1
            com.zipow.videobox.view.IMAddrBookItem r1 = r0.getBuddyByJid(r1)
            if (r1 == 0) goto L_0x0036
            boolean r1 = r1.isZoomRoomContact()
            if (r1 == 0) goto L_0x0036
            r4 = 1
        L_0x004f:
            if (r4 == 0) goto L_0x0054
            r3.refreshAllData()
        L_0x0054:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.p014mm.MMContactsRoomsListView.onBuddyInfoUpdate(java.util.List, java.util.List):void");
    }

    public void refreshAllData() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            this.mAdapter.clearAll();
            AllBuddyInfo allRooms = zoomMessenger.getAllRooms();
            if (allRooms == null) {
                this.mAdapter.notifyDataSetChanged();
                return;
            }
            List<String> starSessionGetAll = zoomMessenger.starSessionGetAll();
            if (starSessionGetAll != null) {
                for (String sessionById : starSessionGetAll) {
                    ZoomChatSession sessionById2 = zoomMessenger.getSessionById(sessionById);
                    if (sessionById2 != null) {
                        IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(sessionById2.getSessionBuddy());
                        if (fromZoomBuddy != null && fromZoomBuddy.isZoomRoomContact()) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(QuickSearchListView.STARRED_GROUP_CATEGORY_CHAR);
                            sb.append(fromZoomBuddy.getSortKey());
                            fromZoomBuddy.setSortKey(sb.toString());
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append(QuickSearchListView.STARRED_GROUP_CATEGORY_CHAR);
                            sb2.append(fromZoomBuddy.getJid());
                            fromZoomBuddy.setJid(sb2.toString());
                            this.mAdapter.addOrUpdateItem(fromZoomBuddy);
                        }
                    }
                }
            }
            for (int i = 0; i < allRooms.getJidsCount(); i++) {
                IMAddrBookItem iMAddrBookItem = new IMAddrBookItem(allRooms.getJids(i), allRooms.getScreenName(i), allRooms.getPhoneNumber(i), allRooms.getIsBuddy(i), allRooms.getIsDesktopOnLine(i), allRooms.getIsMobileOnLine(i), allRooms.getEmail(i), allRooms.getIsZoomRoom(i), allRooms.getSipPhoneNumber(i));
                this.mAdapter.addOrUpdateItem(iMAddrBookItem);
            }
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setParentFragment(IMAddrBookListFragment iMAddrBookListFragment) {
        this.mParentFragment = iMAddrBookListFragment;
    }

    public void setEmptyView(View view) {
        getListView().setEmptyView(view);
    }

    public int getCount() {
        return this.mAdapter.getCount();
    }

    public boolean hasRooms() {
        return this.mAdapter.hasRooms();
    }

    public boolean isParentFragmentResumed() {
        IMAddrBookListFragment iMAddrBookListFragment = this.mParentFragment;
        if (iMAddrBookListFragment == null) {
            return false;
        }
        return iMAddrBookListFragment.isResumed();
    }

    /* access modifiers changed from: private */
    public void refreshBuddyVcards() {
        List waitRefreshJids = this.mAdapter.getWaitRefreshJids();
        HashSet hashSet = new HashSet();
        int childCount = getListView().getChildCount() * 2;
        for (int size = waitRefreshJids.size() - 1; size >= 0; size--) {
            hashSet.add(waitRefreshJids.get(size));
            if (hashSet.size() >= childCount) {
                break;
            }
        }
        if (hashSet.size() != 0) {
            this.mAdapter.clearWaitRefreshJids();
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.addAll(hashSet);
                zoomMessenger.refreshBuddyVCards(arrayList);
                if (arrayList.size() > 0 && zoomMessenger.isAnyBuddyGroupLarge()) {
                    zoomMessenger.getBuddiesPresence(arrayList, false);
                }
            }
        }
    }

    private void onItemClick(@Nullable final IMAddrBookItem iMAddrBookItem) {
        if (iMAddrBookItem != null) {
            Context context = getContext();
            if (context != null) {
                final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
                ArrayList arrayList = new ArrayList();
                String screenName = iMAddrBookItem.getScreenName();
                arrayList.add(new RoomListContextMenuItem(context.getString(C4558R.string.zm_btn_video_call), 0));
                arrayList.add(new RoomListContextMenuItem(context.getString(C4558R.string.zm_btn_audio_call), 1));
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    if (zoomMessenger.isStarSession(iMAddrBookItem.getJid()) || (!TextUtils.isEmpty(iMAddrBookItem.getSortKey()) && iMAddrBookItem.getSortKey().charAt(0) == '!')) {
                        arrayList.add(new RoomListContextMenuItem(context.getString(C4558R.string.zm_mm_unstarred_zoom_room_65147), 4));
                    } else {
                        arrayList.add(new RoomListContextMenuItem(context.getString(C4558R.string.zm_mm_starred_zoom_room_65147), 3));
                    }
                }
                if (PTApp.getInstance().getCallStatus() == 2) {
                    arrayList.add(new RoomListContextMenuItem(context.getString(C4558R.string.zm_btn_invite_to_conf), 2));
                }
                zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                ZMAlertDialog create = new Builder(context).setTitle((CharSequence) screenName).setAdapter(zMMenuAdapter, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MMContactsRoomsListView.this.onSelectContextMenuItem(iMAddrBookItem, (RoomListContextMenuItem) zMMenuAdapter.getItem(i));
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@NonNull IMAddrBookItem iMAddrBookItem, RoomListContextMenuItem roomListContextMenuItem) {
        String str;
        if (roomListContextMenuItem.getAction() == 1) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                callABContact(0, iMAddrBookItem);
            }
        } else if (roomListContextMenuItem.getAction() == 0) {
            if (PTApp.getInstance().getCallStatus() == 0) {
                callABContact(1, iMAddrBookItem);
            }
        } else if (roomListContextMenuItem.getAction() == 2) {
            int callStatus = PTApp.getInstance().getCallStatus();
            if (callStatus == 1 || callStatus == 2) {
                inviteABContact(iMAddrBookItem);
            }
        } else if (roomListContextMenuItem.getAction() == 3) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.starSessionSetStar(iMAddrBookItem.getJid(), true)) {
                refreshAllData();
            }
        } else if (roomListContextMenuItem.getAction() == 4) {
            ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
            if (TextUtils.isEmpty(iMAddrBookItem.getJid()) || TextUtils.isEmpty(iMAddrBookItem.getSortKey()) || iMAddrBookItem.getSortKey().charAt(0) != '!') {
                str = iMAddrBookItem.getJid();
            } else {
                str = iMAddrBookItem.getJid().replace(iMAddrBookItem.getSortKey(), "");
            }
            if (zoomMessenger2 != null && zoomMessenger2.starSessionSetStar(str, false)) {
                refreshAllData();
            }
        }
    }

    private void callABContact(int i, @NonNull IMAddrBookItem iMAddrBookItem) {
        String str;
        Context context = getContext();
        if (context instanceof Activity) {
            if (TextUtils.isEmpty(iMAddrBookItem.getJid()) || TextUtils.isEmpty(iMAddrBookItem.getSortKey()) || iMAddrBookItem.getSortKey().charAt(0) != '!') {
                str = iMAddrBookItem.getJid();
            } else {
                str = iMAddrBookItem.getJid().replace(iMAddrBookItem.getSortKey(), "");
            }
            Activity activity = (Activity) context;
            int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, str, i);
            if (inviteToVideoCall != 0) {
                StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
            }
        }
    }

    private void inviteABContact(@NonNull IMAddrBookItem iMAddrBookItem) {
        String str;
        Context context = getContext();
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            if (!StringUtil.isEmptyOrNull(iMAddrBookItem.getJid())) {
                if (TextUtils.isEmpty(iMAddrBookItem.getJid()) || TextUtils.isEmpty(iMAddrBookItem.getSortKey()) || iMAddrBookItem.getSortKey().charAt(0) != '!') {
                    str = iMAddrBookItem.getJid();
                } else {
                    str = iMAddrBookItem.getJid().replace(iMAddrBookItem.getSortKey(), "");
                }
                if (PTAppDelegation.getInstance().inviteBuddiesToConf(new String[]{str}, null, PTApp.getInstance().getActiveCallId(), PTApp.getInstance().getActiveMeetingNo(), activity.getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                    onSentInvitationFailed();
                } else {
                    onSentInvitationDone(activity);
                }
            }
        }
    }

    private void onSentInvitationFailed() {
        Context context = getContext();
        if (context instanceof ZMActivity) {
            new InviteFailedDialog().show(((ZMActivity) context).getSupportFragmentManager(), InviteFailedDialog.class.getName());
        }
    }

    private void onSentInvitationDone(@NonNull Activity activity) {
        ConfLocalHelper.returnToConf(activity);
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        Object itemAtPosition = getItemAtPosition(i);
        if (itemAtPosition instanceof IMAddrBookItem) {
            onItemClick((IMAddrBookItem) itemAtPosition);
        }
    }

    public void updateBuddyInfoWithJid(String str) {
        MMContactsRoomAdapter mMContactsRoomAdapter = this.mAdapter;
        if (mMContactsRoomAdapter != null && mMContactsRoomAdapter.isContainRoom(str)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str);
                if (buddyWithJID != null) {
                    IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(buddyWithJID);
                    if (fromZoomBuddy != null) {
                        this.mAdapter.addOrUpdateItem(fromZoomBuddy);
                        this.mAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }

    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 2) {
            this.mHandler.removeMessages(1);
        } else if (!this.mHandler.hasMessages(1)) {
            this.mHandler.sendEmptyMessageDelayed(1, 1000);
        }
    }
}
