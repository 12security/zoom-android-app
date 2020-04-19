package com.zipow.videobox.ptapp;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.CmmTime;
import com.zipow.videobox.CallingActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr.DndSetting;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistory;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.PBXJoinMeetingRequest;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.view.sip.SipInCallActivity;
import java.util.Calendar;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.videomeetings.C4558R;

public class IncomingCallManager {
    private static final String TAG = "IncomingCallManager";
    @Nullable
    private static IncomingCallManager instance;
    private Context mContext;
    @Nullable
    private InvitationItem mCurrentInvitation;

    private IncomingCallManager() {
    }

    @NonNull
    public static synchronized IncomingCallManager getInstance() {
        IncomingCallManager incomingCallManager;
        synchronized (IncomingCallManager.class) {
            if (instance == null) {
                instance = new IncomingCallManager();
            }
            incomingCallManager = instance;
        }
        return incomingCallManager;
    }

    public void initialize(Context context) {
        this.mContext = context;
        if (this.mContext == null) {
            throw new NullPointerException("context is null");
        }
    }

    public void onConfInvitation(@Nullable InvitationItem invitationItem) {
        if (invitationItem != null) {
            if (isInDndNow()) {
                declineCall(invitationItem);
                return;
            }
            PTApp instance2 = PTApp.getInstance();
            if (instance2.isAutoReponseON()) {
                ZoomMessenger zoomMessenger = instance2.getZoomMessenger();
                if (zoomMessenger != null && zoomMessenger.isAutoAcceptBuddy(invitationItem.getSenderJID())) {
                    if (this.mCurrentInvitation != null) {
                        declineCall();
                    }
                    this.mCurrentInvitation = invitationItem;
                    acceptCall(this.mContext, true);
                    return;
                }
            }
            if (!(ZMActivity.getFrontActivity() instanceof CallingActivity) || this.mCurrentInvitation == null) {
                this.mCurrentInvitation = invitationItem;
                if (!PTApp.getInstance().hasActiveCall()) {
                    CallingActivity.show(this.mContext, invitationItem);
                } else {
                    IntegrationActivity.onNewIncomingCall(VideoBoxApplication.getInstance(), invitationItem);
                }
            }
        }
    }

    @Nullable
    public InvitationItem getCurrentCall() {
        return this.mCurrentInvitation;
    }

    public void acceptCall(Context context, boolean z) {
        if (this.mCurrentInvitation != null) {
            PTApp.getInstance().forceSyncLeaveCurrentCall();
            PTApp.getInstance().dispatchIdleMessage();
            if (!isFromPbxCall()) {
                ConfActivity.acceptCall(context, this.mCurrentInvitation, z);
                CmmSIPCallManager.getInstance().onAcceptMeeting();
            } else {
                PBXJoinMeetingRequest joinMeetingRequest = CmmSIPCallManager.getInstance().getJoinMeetingRequest(this.mCurrentInvitation.getPbxCallId());
                if (joinMeetingRequest != null) {
                    SipInCallActivity.returnToSipForMeetingRequest(context, SipInCallActivity.ACTION_ACCEPT_MEETING_REQUEST, joinMeetingRequest);
                }
            }
            if (this.mCurrentInvitation != null) {
                this.mCurrentInvitation = null;
            }
        }
    }

    public void onAcceptEventFromPTEvent(InvitationItem invitationItem) {
        if (!(this.mCurrentInvitation == null || invitationItem == null || invitationItem.getMeetingNumber() == 0 || invitationItem.getMeetingNumber() != this.mCurrentInvitation.getMeetingNumber())) {
            this.mCurrentInvitation = null;
        }
    }

    public void onDeclineEventFromPTEvent(InvitationItem invitationItem) {
        if (!(this.mCurrentInvitation == null || invitationItem == null || invitationItem.getMeetingNumber() == 0 || invitationItem.getMeetingNumber() != this.mCurrentInvitation.getMeetingNumber())) {
            this.mCurrentInvitation = null;
        }
    }

    public boolean isFromPbxCall() {
        return isFromPbxCall(this.mCurrentInvitation);
    }

    public boolean isFromPbxCall(@Nullable InvitationItem invitationItem) {
        return invitationItem != null && !TextUtils.isEmpty(invitationItem.getPbxCallId());
    }

    public boolean declineCall() {
        InvitationItem invitationItem = this.mCurrentInvitation;
        if (invitationItem == null) {
            return false;
        }
        boolean declineCall = declineCall(invitationItem);
        if (declineCall) {
            this.mCurrentInvitation = null;
        }
        return declineCall;
    }

    public boolean declineCall(@Nullable InvitationItem invitationItem) {
        if (invitationItem == null || this.mContext == null) {
            return false;
        }
        if (isFromPbxCall(invitationItem)) {
            CmmSIPCallManager.getInstance().hangupCall(invitationItem.getPbxCallId());
        }
        Resources resources = this.mContext.getResources();
        if (resources == null) {
            return false;
        }
        PTApp.getInstance().declineVideoCall(invitationItem, resources.getString(C4558R.string.zm_msg_decline_call));
        return true;
    }

    private boolean isInDndNow() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        boolean z = false;
        if (notificationSettingMgr == null) {
            return false;
        }
        long[] snoozeSettings = notificationSettingMgr.getSnoozeSettings();
        if (snoozeSettings == null) {
            return false;
        }
        if (snoozeSettings[2] - CmmTime.getMMNow() > 0) {
            return true;
        }
        DndSetting dndSettings = notificationSettingMgr.getDndSettings();
        if (dndSettings == null || !dndSettings.isEnable()) {
            return false;
        }
        Calendar start = dndSettings.getStart();
        Calendar end = dndSettings.getEnd();
        Calendar instance2 = Calendar.getInstance();
        if (start == null || !start.after(end)) {
            if (instance2.after(start) && instance2.before(end)) {
                z = true;
            }
            return z;
        }
        if (instance2.after(start) || instance2.before(end)) {
            z = true;
        }
        return z;
    }

    private void insertCallHistory(int i, String str, String str2, String str3, boolean z, String str4) {
        CallHistoryMgr callHistoryMgr = PTApp.getInstance().getCallHistoryMgr();
        if (callHistoryMgr != null) {
            CallHistory callHistory = new CallHistory();
            callHistory.setType(z ? 1 : 2);
            callHistory.setState(i);
            callHistory.setCallerJid(str);
            callHistory.setCallerDisplayName(str2);
            callHistory.setId(str4);
            callHistory.setNumber(str3);
            callHistory.setDirection(1);
            callHistory.setTime(CmmTime.getMMNow());
            callHistoryMgr.addCallHistory(callHistory);
        }
    }

    public void insertDeclineCallMsg() {
        if (this.mCurrentInvitation != null && this.mContext != null) {
        }
    }

    public void handleCallActionMessage(String str, String str2, String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        insertCallActionMessage(str, str2, str3, str4, str5, j, i, str6, j2, j3, j4, z);
        if (i == 54) {
            if (PTApp.getInstance().hasActiveCall()) {
                IPCHelper.getInstance().onNewIncomingCallCanceled(j4);
            }
            NotificationMgr.removeNotification(VideoBoxApplication.getNonNullInstance(), 11);
        }
    }

    public void insertCallActionMessage(String str, String str2, @Nullable String str3, String str4, String str5, long j, int i, String str6, long j2, long j3, long j4, boolean z) {
        int i2;
        int i3 = i;
        long j5 = j4;
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        Resources resources = this.mContext.getResources();
        if (zoomMessenger != null && resources != null) {
            String str7 = str;
            ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(str7);
            boolean isZoomRoom = buddyWithJID != null ? buddyWithJID.isZoomRoom() : false;
            String str8 = str3 == null ? "" : str3;
            String str9 = i3 == 55 ? str5 : str7;
            boolean z2 = !TextUtils.isEmpty(str8);
            switch (i3) {
                case 50:
                    zoomMessenger.insertSystemMessage(str8, str9, resources.getString(C4558R.string.zm_mm_miss_call), j, isZoomRoom, i, str6, j2, j3, false);
                    int i4 = z2 ? 5 : 1;
                    StringBuilder sb = new StringBuilder();
                    sb.append(j5);
                    sb.append("");
                    insertCallHistory(i4, str, str2, sb.toString(), z, str6);
                    break;
                case 51:
                    zoomMessenger.insertSystemMessage(str8, str9, resources.getString(C4558R.string.zm_mm_accepted_call_35364), j, i, str6, j2, j3);
                    int i5 = z2 ? 5 : 2;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(j5);
                    sb2.append("");
                    insertCallHistory(i5, str, str2, sb2.toString(), z, str6);
                    break;
                case 52:
                    zoomMessenger.insertSystemMessage(str8, str9, resources.getString(C4558R.string.zm_mm_declined_call), j, i, str6, j2, j3);
                    int i6 = z2 ? 5 : 1;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(j5);
                    sb3.append("");
                    insertCallHistory(i6, str, str2, sb3.toString(), z, str6);
                    break;
                case 54:
                    zoomMessenger.insertSystemMessage(str8, str9, resources.getString(C4558R.string.zm_mm_cancel_call_46218), j, false, i, str6, j2, j3, false);
                    int i7 = z2 ? 5 : 0;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(j5);
                    sb4.append("");
                    insertCallHistory(i7, str, str2, sb4.toString(), z, str6);
                    break;
                case 55:
                    zoomMessenger.insertSystemMessage(str8, str9, resources.getString(C4558R.string.zm_msg_calling_out_54639), j, true, i, str6, j2, j3, false);
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append(j5);
                    sb5.append("");
                    insertCallHistory(5, str, str2, sb5.toString(), z, str6);
                    break;
                default:
                    zoomMessenger.insertSystemMessage(str8, str9, resources.getString(C4558R.string.zm_mm_unknow_call_35364), j, i, str6, j2, j3);
                    if (z2) {
                        i2 = 5;
                    } else {
                        i2 = 0;
                    }
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(j5);
                    sb6.append("");
                    insertCallHistory(i2, str, str2, sb6.toString(), z, str6);
                    break;
            }
        }
    }

    public void ignoreCall() {
        if (this.mCurrentInvitation != null) {
            this.mCurrentInvitation = null;
        }
    }

    public void onCallTimeout() {
        if (this.mCurrentInvitation != null) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                zoomMessenger.notifyMissedCall(this.mCurrentInvitation.getMeetingNumber());
                declineCall();
            }
        }
    }

    public void clearCurrentInvitation() {
        this.mCurrentInvitation = null;
    }
}
