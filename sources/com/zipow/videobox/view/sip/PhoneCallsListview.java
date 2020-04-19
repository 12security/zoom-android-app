package com.zipow.videobox.view.sip;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.zipow.videobox.AddrBookItemDetailsActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.MMChatActivity;
import com.zipow.videobox.fragment.InviteFragment.InviteFailedDialog;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistory;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class PhoneCallsListview extends ListView implements OnItemClickListener {
    private static final String TAG = "PhoneCallsListview";
    private PhoneCallsAdapter mAdapter;
    @Nullable
    private ZMAlertDialog mContextMenuDialog;
    private boolean mIsShowMissedHistory = false;
    private PhoneCallFragment mParentFragment;

    public static class CallsListMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_AUDIO_CALL = 2;
        public static final int ACTION_CHAT = 1;
        public static final int ACTION_INVITE_CALL = 4;
        public static final int ACTION_SIP_CALL = 0;
        public static final int ACTION_VIDEO_CALL = 3;
        public static final int ACTION_VIEW_PROFILE = 5;
        /* access modifiers changed from: private */
        @Nullable
        public String jid;
        /* access modifiers changed from: private */
        @Nullable
        public String name;
        /* access modifiers changed from: private */
        @Nullable
        public String number;

        public CallsListMenuItem(String str, int i) {
            super(i, str);
        }
    }

    public PhoneCallsListview(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public PhoneCallsListview(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        this.mAdapter = new PhoneCallsAdapter(getContext(), this);
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
    }

    public void setShowMissedHistory(boolean z) {
        this.mIsShowMissedHistory = z;
    }

    public void setParentFragment(PhoneCallFragment phoneCallFragment) {
        this.mParentFragment = phoneCallFragment;
    }

    public void setDeleteMode(boolean z) {
        this.mAdapter.setDeleteMode(z);
        this.mAdapter.notifyDataSetChanged();
    }

    public void loadAllRecentCalls() {
        CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
        if (callHistoryMgr != null) {
            List sipCallHistory = callHistoryMgr.getSipCallHistory(this.mIsShowMissedHistory);
            if (sipCallHistory != null) {
                Collections.reverse(sipCallHistory);
                this.mAdapter.updateData(sipCallHistory);
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void updateZoomBuddyInfo(@NonNull List<String> list) {
        this.mAdapter.updateZoomBuddyInfo(list);
    }

    public void onDeleteHistoryCall(String str) {
        if (this.mAdapter.removeCall(str)) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void deleteHistoryCall(String str) {
        CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
        if (callHistoryMgr != null && callHistoryMgr.deleteCallHistory(str)) {
            PhoneCallFragment phoneCallFragment = this.mParentFragment;
            if (phoneCallFragment != null) {
                phoneCallFragment.onCallHistoryDeleted(str);
            }
            if (this.mAdapter.removeCall(str)) {
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        String str;
        boolean z;
        if (this.mAdapter != null && !this.mParentFragment.isInEditMode()) {
            CallHistory item = this.mAdapter.getItem(i);
            if (item != null) {
                ZMActivity zMActivity = (ZMActivity) getContext();
                if (zMActivity != null && !CmmSIPCallManager.getInstance().isLoginConflict()) {
                    dismissContextMenuDialog();
                    String str2 = null;
                    if (item.getType() == 3) {
                        String number = item.getNumber();
                        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                        if (zoomMessenger != null) {
                            ZoomBuddy buddyWithSipPhone = zoomMessenger.getBuddyWithSipPhone(number);
                            if (buddyWithSipPhone != null) {
                                str2 = buddyWithSipPhone.getJid();
                                z = buddyWithSipPhone.isZoomRoom();
                                String str3 = str2;
                                str2 = number;
                                str = str3;
                            }
                        }
                        z = false;
                        String str32 = str2;
                        str2 = number;
                        str = str32;
                    } else {
                        str = item.getDirection() == 2 ? item.getCalleeJid() : item.getCallerJid();
                        if (!StringUtil.isEmptyOrNull(str)) {
                            ZoomMessenger zoomMessenger2 = PTApp.getInstance().getZoomMessenger();
                            if (zoomMessenger2 != null) {
                                ZoomBuddy buddyWithJID = zoomMessenger2.getBuddyWithJID(str);
                                if (buddyWithJID != null) {
                                    str2 = buddyWithJID.getSipPhoneNumber();
                                    z = buddyWithJID.isZoomRoom();
                                }
                            }
                        }
                        str = null;
                        z = false;
                    }
                    final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
                    ArrayList arrayList = new ArrayList();
                    if (!StringUtil.isEmptyOrNull(str2) && NetworkUtil.hasDataNetwork(getContext())) {
                        CallsListMenuItem callsListMenuItem = new CallsListMenuItem(zMActivity.getString(C4558R.string.zm_msg_call_phonenum, new Object[]{str2}), 0);
                        callsListMenuItem.number = str2;
                        callsListMenuItem.name = item.getZOOMDisplayName();
                        arrayList.add(callsListMenuItem);
                    }
                    if (PTApp.getInstance().hasMessenger() && ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(str2) != null) {
                        CallsListMenuItem callsListMenuItem2 = new CallsListMenuItem(zMActivity.getString(C4558R.string.zm_sip_view_profile_94136), 5);
                        callsListMenuItem2.number = str2;
                        arrayList.add(callsListMenuItem2);
                    }
                    if (!StringUtil.isEmptyOrNull(str)) {
                        if (!z && PTApp.getInstance().hasMessenger() && PTApp.getInstance().getZoomMessenger().imChatGetOption() != 2) {
                            CallsListMenuItem callsListMenuItem3 = new CallsListMenuItem(zMActivity.getString(C4558R.string.zm_btn_mm_chat), 1);
                            callsListMenuItem3.jid = str;
                            arrayList.add(callsListMenuItem3);
                        }
                        switch ((int) ((long) PTApp.getInstance().getCallStatus())) {
                            case 1:
                                break;
                            case 2:
                                CallsListMenuItem callsListMenuItem4 = new CallsListMenuItem(zMActivity.getString(C4558R.string.zm_btn_invite_to_conf), 4);
                                callsListMenuItem4.jid = str;
                                arrayList.add(callsListMenuItem4);
                                break;
                            default:
                                CallsListMenuItem callsListMenuItem5 = new CallsListMenuItem(zMActivity.getString(C4558R.string.zm_btn_video_call), 3);
                                callsListMenuItem5.jid = str;
                                arrayList.add(callsListMenuItem5);
                                CallsListMenuItem callsListMenuItem6 = new CallsListMenuItem(zMActivity.getString(C4558R.string.zm_btn_audio_call), 2);
                                callsListMenuItem6.jid = str;
                                arrayList.add(callsListMenuItem6);
                                break;
                        }
                    }
                    if (arrayList.size() > 0) {
                        zMMenuAdapter.addAll((List<MenuItemType>) arrayList);
                        this.mContextMenuDialog = new Builder(zMActivity).setAdapter(zMMenuAdapter, new OnClickListener() {
                            public void onClick(DialogInterface dialogInterface, int i) {
                                PhoneCallsListview.this.onSelectContextMenuItem((CallsListMenuItem) zMMenuAdapter.getItem(i));
                            }
                        }).create();
                        this.mContextMenuDialog.setCanceledOnTouchOutside(true);
                        this.mContextMenuDialog.show();
                    }
                }
            }
        }
    }

    private void dismissContextMenuDialog() {
        ZMAlertDialog zMAlertDialog = this.mContextMenuDialog;
        if (zMAlertDialog != null && zMAlertDialog.isShowing()) {
            this.mContextMenuDialog.dismiss();
            this.mContextMenuDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void onSelectContextMenuItem(@Nullable CallsListMenuItem callsListMenuItem) {
        if (callsListMenuItem != null) {
            int action = callsListMenuItem.getAction();
            if (action == 0) {
                if (!StringUtil.isEmptyOrNull(callsListMenuItem.number)) {
                    sendSipCall(callsListMenuItem.number, callsListMenuItem.name);
                }
            } else if (action == 5) {
                AddrBookItemDetailsActivity.show((Fragment) this.mParentFragment, ZMPhoneSearchHelper.getInstance().getIMAddrBookItemByNumber(callsListMenuItem.number), 106);
            } else if (action == 1) {
                if (!StringUtil.isEmptyOrNull(callsListMenuItem.jid)) {
                    ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                    if (zoomMessenger != null) {
                        MMChatActivity.showAsOneToOneChat((ZMActivity) getContext(), IMAddrBookItem.fromZoomBuddy(zoomMessenger.getBuddyWithJID(callsListMenuItem.jid)), callsListMenuItem.jid);
                    }
                }
            } else if (action == 2) {
                if (!StringUtil.isEmptyOrNull(callsListMenuItem.jid)) {
                    startAudioCall(callsListMenuItem.jid);
                }
            } else if (action == 3) {
                if (!StringUtil.isEmptyOrNull(callsListMenuItem.jid)) {
                    startVideoCall(callsListMenuItem.jid);
                }
            } else if (action == 4 && !StringUtil.isEmptyOrNull(callsListMenuItem.jid)) {
                inviteToConf(callsListMenuItem.jid);
            }
        }
    }

    private void sendSipCall(@Nullable String str, String str2) {
        if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
            this.mParentFragment.onPickSipResult(str, str2);
        }
    }

    private void inviteToConf(String str) {
        int callStatus = PTApp.getInstance().getCallStatus();
        if (callStatus == 1 || callStatus == 2) {
            inviteABContact(str);
        }
    }

    private void startVideoCall(String str) {
        if (PTApp.getInstance().getCallStatus() == 0) {
            callABContact(1, str);
        }
    }

    private void startAudioCall(String str) {
        if (PTApp.getInstance().getCallStatus() == 0) {
            callABContact(0, str);
        }
    }

    private void callABContact(int i, String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Activity activity = (Activity) getContext();
            if (activity != null) {
                int inviteToVideoCall = ConfActivity.inviteToVideoCall(activity, str, i);
                if (inviteToVideoCall != 0) {
                    StartHangoutFailedDialog.show(((ZMActivity) activity).getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), inviteToVideoCall);
                }
            }
        }
    }

    private void inviteABContact(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Activity activity = (Activity) getContext();
            if (activity != null) {
                if (PTAppDelegation.getInstance().inviteBuddiesToConf(new String[]{str}, null, PTApp.getInstance().getActiveCallId(), PTApp.getInstance().getActiveMeetingNo(), activity.getString(C4558R.string.zm_msg_invitation_message_template)) != 0) {
                    onSentInvitationFailed();
                } else {
                    onSentInvitationDone(activity);
                }
            }
        }
    }

    private void onSentInvitationFailed() {
        new InviteFailedDialog().show(((ZMActivity) getContext()).getSupportFragmentManager(), InviteFailedDialog.class.getName());
    }

    private void onSentInvitationDone(@NonNull Activity activity) {
        ConfLocalHelper.returnToConf(activity);
        activity.finish();
    }

    public void onLoginConflict() {
        dismissContextMenuDialog();
    }
}
