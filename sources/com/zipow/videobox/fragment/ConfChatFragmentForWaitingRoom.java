package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.SimpleInMeetingActivity;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ConfUI.SimpleConfUIListener;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.p010qa.ZoomQAComponent;
import com.zipow.videobox.util.ZMConfUtil;
import com.zipow.videobox.view.ConfChatAttendeeItem;
import com.zipow.videobox.view.ConfChatListViewForWaitingRoom;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.videomeetings.C4558R;

public class ConfChatFragmentForWaitingRoom extends ZMDialogFragment implements OnClickListener {
    public static final int DEFALUT_ROLE = -1;
    private static final String EXTRA_CHAT_ITEM = "EXTRA_CHAT_ITEM";
    /* access modifiers changed from: private */
    public ConfChatListViewForWaitingRoom mChatListView;
    @Nullable
    private SimpleConfUIListener mConfUIListener = new SimpleConfUIListener() {
        public boolean onUserEvent(int i, long j, int i2) {
            EventTaskManager nonNullEventTaskManagerOrThrowException = ConfChatFragmentForWaitingRoom.this.getNonNullEventTaskManagerOrThrowException();
            final int i3 = i;
            final long j2 = j;
            final int i4 = i2;
            C25091 r1 = new EventAction() {
                public void run(IUIElement iUIElement) {
                    ConfChatFragmentForWaitingRoom confChatFragmentForWaitingRoom = (ConfChatFragmentForWaitingRoom) iUIElement;
                    if (confChatFragmentForWaitingRoom != null) {
                        confChatFragmentForWaitingRoom.handleOnUserEvent(i3, j2, i4);
                    }
                }
            };
            nonNullEventTaskManagerOrThrowException.push(r1);
            return ConfChatFragmentForWaitingRoom.this.mChatListView.onUserEvent(i, j, i2);
        }

        public boolean onChatMessageReceived(String str, long j, String str2, long j2, String str3, String str4, long j3) {
            boolean onChatMessageReceived = ConfChatFragmentForWaitingRoom.this.mChatListView.onChatMessageReceived(str, j, str2, j2, str3, str4, j3);
            if (ConfChatFragmentForWaitingRoom.this.getActivity() instanceof ConfActivity) {
                ((ConfActivity) ConfChatFragmentForWaitingRoom.this.getActivity()).refreshUnreadChatCount();
            }
            return onChatMessageReceived;
        }

        public boolean onConfStatusChanged2(int i, long j) {
            if (i == 39) {
                if (!(j == 1)) {
                    ConfChatFragmentForWaitingRoom.this.dismiss();
                    return true;
                }
            }
            return false;
        }
    };
    @Nullable
    private ConfChatAttendeeItem mCurrentItem;
    private boolean mIsAttendee;
    private boolean mIsWebinar;

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
    }

    public static void showAsActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            SimpleInMeetingActivity.show(zMActivity, ConfChatFragmentForWaitingRoom.class.getName(), (Bundle) null, 0, false);
        }
    }

    @SuppressLint({"NewApi"})
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_message_for_waiting_room, viewGroup, false);
        this.mChatListView = (ConfChatListViewForWaitingRoom) inflate.findViewById(C4558R.C4560id.chatListView);
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = true;
        this.mIsWebinar = confContext != null && confContext.isWebinar();
        if (this.mIsWebinar) {
            ZoomQAComponent qAComponent = ConfMgr.getInstance().getQAComponent();
            if (qAComponent != null && !qAComponent.isWebinarAttendee()) {
                z = false;
            }
            this.mIsAttendee = z;
        } else {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                if (myself.isHost() || myself.isCoHost()) {
                    z = false;
                }
                this.mIsAttendee = z;
            }
        }
        if (bundle != null) {
            this.mCurrentItem = (ConfChatAttendeeItem) bundle.getSerializable(EXTRA_CHAT_ITEM);
        }
        ConfUI.getInstance().addListener(this.mConfUIListener);
        if (this.mCurrentItem == null) {
            this.mCurrentItem = ConfMgr.getInstance().getConfDataHelper().getmConfChatAttendeeItem();
        }
        refreshTxtCurrentItem();
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        this.mChatListView.updateUI();
        if (getActivity() instanceof ConfActivity) {
            ((ConfActivity) getActivity()).refreshUnreadChatCount();
        }
    }

    public void onPause() {
        this.mChatListView.onParentFragmentPause();
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void handleOnUserEvent(int i, long j, int i2) {
        if (i == 0 && this.mIsWebinar && this.mCurrentItem.nodeID != 0 && this.mCurrentItem.nodeID != 1 && ZMConfUtil.getZoomQABuddyByNodeId(this.mCurrentItem.nodeID) == null) {
            ZoomQABuddy zoomQABuddyByJid = ZMConfUtil.getZoomQABuddyByJid(this.mCurrentItem.jid);
            if (zoomQABuddyByJid != null) {
                this.mCurrentItem = new ConfChatAttendeeItem(zoomQABuddyByJid);
                refreshTxtCurrentItem();
            }
        }
    }

    public void onDestroy() {
        ConfUI.getInstance().removeListener(this.mConfUIListener);
        super.onDestroy();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable(EXTRA_CHAT_ITEM, this.mCurrentItem);
    }

    private void refreshTxtCurrentItem() {
        if (this.mCurrentItem == null) {
            if (this.mIsWebinar) {
                ConfChatAttendeeItem confChatAttendeeItem = new ConfChatAttendeeItem(getString(C4558R.string.zm_webinar_txt_all_panelists), null, 1, -1);
                this.mCurrentItem = confChatAttendeeItem;
            } else if (this.mIsAttendee) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    if (confStatusObj.getAttendeeChatPriviledge() == 3) {
                        CmmUserList userList = ConfMgr.getInstance().getUserList();
                        if (userList != null) {
                            CmmUser hostUser = userList.getHostUser();
                            if (hostUser != null) {
                                ConfChatAttendeeItem confChatAttendeeItem2 = new ConfChatAttendeeItem(hostUser.getScreenName(), null, hostUser.getNodeId(), -1);
                                this.mCurrentItem = confChatAttendeeItem2;
                            }
                        }
                    } else {
                        ConfChatAttendeeItem confChatAttendeeItem3 = new ConfChatAttendeeItem(getString(C4558R.string.zm_mi_everyone_122046), null, 0, -1);
                        this.mCurrentItem = confChatAttendeeItem3;
                    }
                }
            } else {
                ConfChatAttendeeItem confChatAttendeeItem4 = new ConfChatAttendeeItem(getString(C4558R.string.zm_mi_everyone_122046), null, 0, -1);
                this.mCurrentItem = confChatAttendeeItem4;
            }
        }
    }

    public void onClick(@Nullable View view) {
        if (view != null && view.getId() == C4558R.C4560id.btnBack) {
            dismiss();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }
}
