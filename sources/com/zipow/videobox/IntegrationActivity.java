package com.zipow.videobox;

import android.app.job.JobInfo.Builder;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import com.google.gson.Gson;
import com.zipow.videobox.dialog.InformationBarriesDialog;
import com.zipow.videobox.fragment.ErrorMsgConfirmDialog;
import com.zipow.videobox.fragment.ErrorMsgConfirmDialog.ErrorInfo;
import com.zipow.videobox.fragment.ErrorMsgDialog;
import com.zipow.videobox.fragment.ServerNamePasswordDialog;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.fragment.SystemNotificationFragment;
import com.zipow.videobox.fragment.VerifyCertFailureDialog;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.IMHelper;
import com.zipow.videobox.ptapp.IMProtos.BuddyItem;
import com.zipow.videobox.ptapp.IMSession;
import com.zipow.videobox.ptapp.IncomingCallManager;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTBuddyHelper;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.VerifyCertEvent;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.SipRingMgr;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPNosManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.NosSIPCallItem;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.IMBuddyItem;
import com.zipow.videobox.view.sip.SipInCallActivity;
import com.zipow.videobox.view.sip.SipIncomeActivity;
import com.zipow.videobox.view.sip.SipIncomePopActivity;
import java.io.Serializable;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class IntegrationActivity extends ZMActivity {
    public static final String ACTION_ACCEPT_CALL;
    public static final String ACTION_CANCEL_SIP_CALL;
    public static final String ACTION_CONFIRM_VERIFY_CERT_FAILURE;
    public static final String ACTION_DECLINE_CALL;
    public static final String ACTION_INPUT_PROXY_NAME_PASS;
    public static final String ACTION_LOGIN_EXPIRED;
    public static final String ACTION_NEW_INCOMING_CALL;
    public static final String ACTION_NOS_CALL;
    public static final String ACTION_PBX_SHOW_UNREAD_MESSAGE;
    public static final String ACTION_RECEIVE_ERROR_CONFIRM_MSG;
    public static final String ACTION_RECEIVE_IM_ERROR_MSG;
    public static final String ACTION_RECEIVE_IM_INFORMATION_BARRIES;
    public static final String ACTION_RETURN_TO_CONF;
    public static final String ACTION_RETURN_TO_SIP;
    public static final String ACTION_RETURN_TO_SIP_ACCEPT;
    public static final String ACTION_RETURN_TO_SIP_DECLINE;
    public static final String ACTION_RETURN_TO_SIP_INCOME;
    public static final String ACTION_SHOW_CALL_NOT_ANSWERED_MESSAGE;
    public static final String ACTION_SHOW_UNREAD_MESSAGE;
    public static final String ACTION_SHOW_UNREAD_MESSAGE_MM;
    public static final String ACTION_SIP_CALL;
    public static final String ACTION_SIP_CALL_FROM_SCHEMA;
    public static final String ACTION_SIP_CALL_MISSED;
    public static final String ARG_ADD_CONTACT = "addContact";
    public static final String ARG_CALL_BODY = "callBody";
    public static final String ARG_CALL_CAPTION = "callCaption";
    public static final String ARG_ENDMEETING_REASON = "endMeetingReason";
    public static final String ARG_ERROR_CONFIRM_MSG = "errorConfirmMsg";
    public static final String ARG_ERROR_CONFIRM_MSG_CODE = "errorConfirmMsgCode";
    public static final String ARG_ERROR_CONFIRM_MSG_FINISH_ONDISMISS = "errorConfirmMsgFinishOnDismiss";
    public static final String ARG_ERROR_CONFIRM_MSG_INTERVAL = "errorConfirmMsgInterval";
    public static final String ARG_ERROR_CONFIRM_TITLE = "errorConfirmTitle";
    public static final String ARG_IM_ERROR_CODE = "imErrorCode";
    public static final String ARG_IM_ERROR_MSG = "imErrorMsg";
    public static final String ARG_INVITATION = "invitation";
    public static final String ARG_INVITATION_MEETINGNO = "meetingNo";
    public static final String ARG_LOGIN_TYPE = "loginType";
    public static final String ARG_NOS_SIP_CALL_ITEM = "ARG_NOS_SIP_CALL_ITEM";
    public static final String ARG_PBX_MESSAGE_PROTO = "pbxMessageSessionProto";
    public static final String ARG_PBX_MESSAGE_SESSION_ID = "pbxMessageSessionId";
    public static final String ARG_PORT = "port";
    public static final String ARG_SERVER = "server";
    public static final String ARG_SIP_CALL_ITEM_ID = "sipCallItemID";
    public static final String ARG_SIP_CALL_PHONE_NUMBER = "sipCallPhoneNumber";
    public static final String ARG_SIP_CALL_URL_ACTION = "sipcallUrlAction";
    public static final String ARG_SIP_CANCEL_SID = "sipCancelSid";
    public static final String ARG_SIP_CAPTION = "sipCaption";
    public static final String ARG_SIP_NEED_INIT_MODULE = "sip_needInitModule";
    public static final String ARG_UNREAD_MESSAGE_SESSION = "unreadMsgSession";
    public static final String ARG_USERNAME = "userName";
    public static final String ARG_VERIFY_CERT_EVENT = "verifyCertEvent";
    private static final String TAG = "IntegrationActivity";

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(IntegrationActivity.class.getName());
        sb.append(".action.RETURN_TO_CONF");
        ACTION_RETURN_TO_CONF = sb.toString();
        StringBuilder sb2 = new StringBuilder();
        sb2.append(IntegrationActivity.class.getName());
        sb2.append(".action.SHOW_UNREAD_MESSAGE");
        ACTION_SHOW_UNREAD_MESSAGE = sb2.toString();
        StringBuilder sb3 = new StringBuilder();
        sb3.append(IntegrationActivity.class.getName());
        sb3.append(".action.SHOW_UNREAD_MESSAGE_MM");
        ACTION_SHOW_UNREAD_MESSAGE_MM = sb3.toString();
        StringBuilder sb4 = new StringBuilder();
        sb4.append(IntegrationActivity.class.getName());
        sb4.append(".action.ACTION_NEW_INCOMING_CALL");
        ACTION_NEW_INCOMING_CALL = sb4.toString();
        StringBuilder sb5 = new StringBuilder();
        sb5.append(IntegrationActivity.class.getName());
        sb5.append(".action.ACTION_ACCEPT_CALL");
        ACTION_ACCEPT_CALL = sb5.toString();
        StringBuilder sb6 = new StringBuilder();
        sb6.append(IntegrationActivity.class.getName());
        sb6.append(".action.ACTION_DECLINE_CALL");
        ACTION_DECLINE_CALL = sb6.toString();
        StringBuilder sb7 = new StringBuilder();
        sb7.append(IntegrationActivity.class.getName());
        sb7.append(".action.ACTION_INPUT_PROXY_NAME_PASS");
        ACTION_INPUT_PROXY_NAME_PASS = sb7.toString();
        StringBuilder sb8 = new StringBuilder();
        sb8.append(IntegrationActivity.class.getName());
        sb8.append(".action.ACTION_SHOW_CALL_NOT_ANSWERED_MESSAGE");
        ACTION_SHOW_CALL_NOT_ANSWERED_MESSAGE = sb8.toString();
        StringBuilder sb9 = new StringBuilder();
        sb9.append(IntegrationActivity.class.getName());
        sb9.append(".action.ACTION_TOKEN_EXPIRED");
        ACTION_LOGIN_EXPIRED = sb9.toString();
        StringBuilder sb10 = new StringBuilder();
        sb10.append(IntegrationActivity.class.getName());
        sb10.append(".action.ACTION_CONFIRM_VERIFY_CERT_FAILURE");
        ACTION_CONFIRM_VERIFY_CERT_FAILURE = sb10.toString();
        StringBuilder sb11 = new StringBuilder();
        sb11.append(IntegrationActivity.class.getName());
        sb11.append(".action.ACTION_RECEIVE_IM_ERROR_MSG");
        ACTION_RECEIVE_IM_ERROR_MSG = sb11.toString();
        StringBuilder sb12 = new StringBuilder();
        sb12.append(IntegrationActivity.class.getName());
        sb12.append(".action.ACTION_RECEIVE_IM_INFORMATION_BARRIES");
        ACTION_RECEIVE_IM_INFORMATION_BARRIES = sb12.toString();
        StringBuilder sb13 = new StringBuilder();
        sb13.append(IntegrationActivity.class.getName());
        sb13.append(".action.ACTION_RECEIVE_ERROR_CONFIRM_MSG");
        ACTION_RECEIVE_ERROR_CONFIRM_MSG = sb13.toString();
        StringBuilder sb14 = new StringBuilder();
        sb14.append(IntegrationActivity.class.getName());
        sb14.append(".action.RETURN_TO_SIP");
        ACTION_RETURN_TO_SIP = sb14.toString();
        StringBuilder sb15 = new StringBuilder();
        sb15.append(IntegrationActivity.class.getName());
        sb15.append(".action.ACTION_NOS_CALL");
        ACTION_NOS_CALL = sb15.toString();
        StringBuilder sb16 = new StringBuilder();
        sb16.append(IntegrationActivity.class.getName());
        sb16.append(".action.ACTION_SIP_CALL");
        ACTION_SIP_CALL = sb16.toString();
        StringBuilder sb17 = new StringBuilder();
        sb17.append(IntegrationActivity.class.getName());
        sb17.append(".action.ACTION_CANCEL_SIP_CALL");
        ACTION_CANCEL_SIP_CALL = sb17.toString();
        StringBuilder sb18 = new StringBuilder();
        sb18.append(IntegrationActivity.class.getName());
        sb18.append(".action.ACTION_SIP_CALL_FROM_SCHEMA");
        ACTION_SIP_CALL_FROM_SCHEMA = sb18.toString();
        StringBuilder sb19 = new StringBuilder();
        sb19.append(IntegrationActivity.class.getName());
        sb19.append(".action.ACTION_SIP_CALL_MISSED");
        ACTION_SIP_CALL_MISSED = sb19.toString();
        StringBuilder sb20 = new StringBuilder();
        sb20.append(IntegrationActivity.class.getName());
        sb20.append(".action.RETURN_TO_SIP_INCOME");
        ACTION_RETURN_TO_SIP_INCOME = sb20.toString();
        StringBuilder sb21 = new StringBuilder();
        sb21.append(IntegrationActivity.class.getName());
        sb21.append(".action.RETURN_TO_SIP.ACCEPT");
        ACTION_RETURN_TO_SIP_ACCEPT = sb21.toString();
        StringBuilder sb22 = new StringBuilder();
        sb22.append(IntegrationActivity.class.getName());
        sb22.append(".action.RETURN_TO_SIP.DECLINE");
        ACTION_RETURN_TO_SIP_DECLINE = sb22.toString();
        StringBuilder sb23 = new StringBuilder();
        sb23.append(IntegrationActivity.class.getName());
        sb23.append(".action.PBX_SHOW_UNREAD_MESSAGE");
        ACTION_PBX_SHOW_UNREAD_MESSAGE = sb23.toString();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        String action = intent.getAction();
        intent.setAction(null);
        setIntent(intent);
        boolean z = ACTION_RETURN_TO_CONF.equals(action) ? handleActionReturnToConf() : ACTION_SHOW_UNREAD_MESSAGE.equals(action) ? handleActionShowUnreadMessage() : ACTION_SHOW_UNREAD_MESSAGE_MM.equals(action) ? handleActionShowUnreadMessageMM() : ACTION_NEW_INCOMING_CALL.equals(action) ? handleActionNewIncomingCall(intent) : ACTION_ACCEPT_CALL.equals(action) ? handleActionAcceptCall() : ACTION_DECLINE_CALL.equals(action) ? handleActionDeclineCall() : ACTION_INPUT_PROXY_NAME_PASS.equals(action) ? handleActionInputProxyNamePass(intent) : ACTION_SHOW_CALL_NOT_ANSWERED_MESSAGE.equals(action) ? handleActionShowCallNotAnswered(intent) : ACTION_LOGIN_EXPIRED.equals(action) ? handleActionLoginExpired(intent) : ACTION_CONFIRM_VERIFY_CERT_FAILURE.equals(action) ? handleActionConfirmVerifyCertFailure(intent) : ACTION_RECEIVE_IM_ERROR_MSG.equals(action) ? handleIMErrorMsg(intent) : ACTION_RECEIVE_IM_INFORMATION_BARRIES.equals(action) ? handleInformationBarries() : ACTION_RECEIVE_ERROR_CONFIRM_MSG.equals(action) ? handleErrorConfirmMsg(intent) : ACTION_RETURN_TO_SIP.equals(action) ? handleActionReturnToSIP() : ACTION_NOS_CALL.equals(action) ? handleActionNosIncomingCall(intent) : ACTION_SIP_CALL.equals(action) ? handleActionSIPIncomingCall(intent) : ACTION_CANCEL_SIP_CALL.equals(action) ? handleActionSIPCancelCall(intent) : ACTION_SIP_CALL_FROM_SCHEMA.equals(action) ? handleActionSIPCallFromSchema(intent) : ACTION_SIP_CALL_MISSED.equals(action) ? handleActionSIPCallMissed(intent) : ACTION_RETURN_TO_SIP_ACCEPT.equals(action) ? handleAcceptSIPCall(intent) : ACTION_RETURN_TO_SIP_DECLINE.equals(action) ? handleDeclineSIPCall(intent) : ACTION_RETURN_TO_SIP_INCOME.equals(action) ? handleReturnIncomeSIPCall(intent) : ACTION_PBX_SHOW_UNREAD_MESSAGE.equals(action) ? handleActionShowPBXUnreadMessage(intent) : true;
        if (z) {
            finish();
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(@Nullable Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_CONFIRM_VERIFY_CERT_FAILURE.equals(action)) {
                handleNewIntentActionConfirmVerifyCertFailure(intent);
            } else if (ACTION_RECEIVE_IM_ERROR_MSG.equals(action)) {
                handleNewIntentActionErrorMsg(intent);
            } else if (ACTION_RECEIVE_ERROR_CONFIRM_MSG.equals(action)) {
                handleNewIntentActionErrorConfirmMsg(intent);
            } else if (ACTION_SIP_CALL.equals(action)) {
                handleActionSIPIncomingCall(intent);
            } else if (ACTION_CANCEL_SIP_CALL.equals(action)) {
                handleActionSIPCancelCall(intent);
            } else if (ACTION_SIP_CALL_FROM_SCHEMA.equals(action)) {
                handleActionSIPCallFromSchema(intent);
            } else if (ACTION_NOS_CALL.equals(action)) {
                handleActionNosIncomingCall(intent);
            }
        }
    }

    private boolean handleActionReturnToConf() {
        ConfLocalHelper.returnToConf(this);
        return true;
    }

    private boolean handleActionReturnToSIP() {
        SipInCallActivity.returnToSip(this);
        return true;
    }

    private boolean handleActionShowUnreadMessage() {
        Intent intent = new Intent(this, IMActivity.class);
        intent.setFlags(67108864);
        intent.setAction(IMActivity.ACTION_SHOW_UNREAD_MESSAGE);
        ActivityStartHelper.startActivity(this, intent, null, null);
        return true;
    }

    private boolean handleActionShowUnreadMessageMM() {
        int i;
        int i2;
        finish();
        if (VideoBoxApplication.getInstance() == null) {
            VideoBoxApplication.initialize(getApplicationContext(), false, 0);
            showIMActivityForUnreadMessage();
            return false;
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null) {
            return false;
        }
        if (!mainboard.isInitialized()) {
            showIMActivityForUnreadMessage();
            return false;
        } else if (UIMgr.isLargeMode(this)) {
            showIMActivityForUnreadMessage();
            return false;
        } else {
            NotificationMgr.removeMessageNotificationMM(this);
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                i2 = zoomMessenger.getTotalUnreadMessageCount();
                i = zoomMessenger.getUnreadRequestCount();
            } else {
                i2 = 0;
                i = 0;
            }
            IMHelper iMHelper = PTApp.getInstance().getIMHelper();
            int unreadMsgCount = iMHelper != null ? iMHelper.getUnreadMsgCount() : 0;
            Intent intent = getIntent();
            if (intent == null || StringUtil.isEmptyOrNull(intent.getStringExtra(ARG_UNREAD_MESSAGE_SESSION))) {
                if (i == 0 && i2 == 0 && unreadMsgCount > 0) {
                    showUnreadMessageThirdPartyIM();
                } else if (i == 0 && i2 > 0 && unreadMsgCount == 0) {
                    int chatSessionCount = zoomMessenger.getChatSessionCount();
                    for (int i3 = 0; i3 < chatSessionCount; i3++) {
                        ZoomChatSession sessionAt = zoomMessenger.getSessionAt(i3);
                        if (sessionAt != null) {
                            int unreadMessageCount = sessionAt.getUnreadMessageCount();
                            if (i2 == unreadMessageCount) {
                                if (sessionAt.isGroup()) {
                                    ZoomGroup sessionGroup = sessionAt.getSessionGroup();
                                    if (sessionGroup == null) {
                                        showIMActivityForUnreadMessage();
                                        return false;
                                    }
                                    String groupID = sessionGroup.getGroupID();
                                    if (StringUtil.isEmptyOrNull(groupID)) {
                                        showIMActivityForUnreadMessage();
                                        return false;
                                    }
                                    startGroupChat(groupID);
                                } else {
                                    ZoomBuddy sessionBuddy = sessionAt.getSessionBuddy();
                                    if (sessionBuddy == null) {
                                        showIMActivityForUnreadMessage();
                                        return false;
                                    }
                                    startOneToOneChat(sessionBuddy);
                                }
                                return false;
                            } else if (unreadMessageCount > 0 && unreadMessageCount < i2) {
                                showIMActivityForUnreadMessage();
                                return false;
                            }
                        }
                    }
                } else if (i > 0 && i2 == 0 && unreadMsgCount == 0) {
                    showSystemNotification();
                } else {
                    showIMActivityForUnreadMessage();
                    return false;
                }
                return false;
            }
            showIMActivityForUnreadMessage();
            return false;
        }
    }

    private void showIMActivityForUnreadMessage() {
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt instanceof IMActivity) && !(inProcessActivityInStackAt instanceof IntegrationActivity) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        Intent intent = new Intent(this, IMActivity.class);
        intent.setFlags(131072);
        intent.setAction(IMActivity.ACTION_SHOW_UNREAD_MESSAGE_MM);
        Intent intent2 = getIntent();
        if (intent2 != null) {
            intent.putExtra(ARG_UNREAD_MESSAGE_SESSION, intent2.getStringExtra(ARG_UNREAD_MESSAGE_SESSION));
            intent.putExtra(ARG_ADD_CONTACT, intent2.getBooleanExtra(ARG_ADD_CONTACT, false));
        }
        ActivityStartHelper.startActivity(this, intent, null, null);
    }

    private void startOneToOneChat(ZoomBuddy zoomBuddy) {
        MMChatActivity.showAsOneToOneChat(this, zoomBuddy);
    }

    private void showSystemNotification() {
        SystemNotificationFragment.showAsActivity(this, 0);
    }

    private void startGroupChat(String str) {
        MMChatActivity.showAsGroupChat(this, str);
    }

    private void showUnreadMessageThirdPartyIM() {
        IMHelper iMHelper = PTApp.getInstance().getIMHelper();
        if (iMHelper != null) {
            PTBuddyHelper buddyHelper = PTApp.getInstance().getBuddyHelper();
            if (buddyHelper != null) {
                int buddyItemCount = buddyHelper.getBuddyItemCount();
                String str = null;
                for (int i = 0; i < buddyItemCount; i++) {
                    String buddyItemJid = buddyHelper.getBuddyItemJid(i);
                    IMSession sessionBySessionName = iMHelper.getSessionBySessionName(buddyItemJid);
                    if (sessionBySessionName != null && sessionBySessionName.getUnreadMessageCount() > 0) {
                        if (str != null) {
                            showIMActivityForUnreadMessage();
                            return;
                        }
                        str = buddyItemJid;
                    }
                }
                if (str != null) {
                    BuddyItem buddyItemByJid = buddyHelper.getBuddyItemByJid(str);
                    if (buddyItemByJid != null) {
                        showChatUI(new IMBuddyItem().parseFromProtoItem(buddyItemByJid));
                    }
                }
            }
        }
    }

    private void showChatUI(@Nullable IMBuddyItem iMBuddyItem) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null && iMBuddyItem != null) {
            Intent intent = new Intent(this, IMChatActivity.class);
            intent.setFlags(131072);
            intent.putExtra("buddyItem", iMBuddyItem);
            intent.putExtra("myName", currentUserProfile.getUserName());
            ActivityStartHelper.startActivity(this, intent, null, null);
        }
    }

    private boolean handleActionSIPCallFromSchema(@Nullable Intent intent) {
        if (intent == null) {
            return true;
        }
        String stringExtra = intent.getStringExtra(ARG_SIP_CALL_PHONE_NUMBER);
        int intExtra = intent.getIntExtra(ARG_SIP_CALL_URL_ACTION, 0);
        if (StringUtil.isEmptyOrNull(stringExtra)) {
            return true;
        }
        CmmSIPNosManager.getInstance().prepareSipCall();
        if (intExtra == 1) {
            showSIPCallDialpad(intent);
        } else if (intExtra == 3) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            if (!instance.isInCall()) {
                String dialNumberFilter = ZMPhoneUtils.dialNumberFilter(stringExtra);
                if (!StringUtil.isEmptyOrNull(dialNumberFilter)) {
                    instance.callPeer(dialNumberFilter);
                }
            }
        }
        return true;
    }

    private void showSIPCallDialpad(@Nullable Intent intent) {
        if (intent != null) {
            int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
            if (inProcessActivityCountInStack > 0) {
                for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                    ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                    if (!(inProcessActivityInStackAt instanceof IMActivity) && !(inProcessActivityInStackAt instanceof IntegrationActivity) && inProcessActivityInStackAt != null) {
                        inProcessActivityInStackAt.finish();
                    }
                }
            }
            Intent intent2 = new Intent(this, IMActivity.class);
            intent2.setFlags(131072);
            intent2.setAction(IMActivity.ACTION_SHOW_SIP_CALL_DIALPAD);
            intent2.putExtra(IMActivity.ARG_SIP_PHONE_NUMBER, intent.getStringExtra(ARG_SIP_CALL_PHONE_NUMBER));
            ActivityStartHelper.startActivityForeground(this, intent2);
        }
    }

    private boolean handleActionNosIncomingCall(Intent intent) {
        ZmPtUtils.handleActionNosIncomingCall(intent.getStringExtra(ARG_CALL_BODY), intent.getStringExtra(ARG_CALL_CAPTION));
        return true;
    }

    private boolean handleActionSIPIncomingCall(Intent intent) {
        NosSIPCallItem nosSIPCallItem = (NosSIPCallItem) intent.getSerializableExtra(ARG_SIP_CAPTION);
        if (nosSIPCallItem == null) {
            return true;
        }
        if (OsUtil.isAtLeastO()) {
            scheduler(intent, 1);
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(getApplicationContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
            PTApp.getInstance().autoSignin();
        }
        CmmSIPNosManager.getInstance().handleDuplicateCheckIncomingPushCall(nosSIPCallItem);
        return true;
    }

    private boolean handleActionSIPCancelCall(Intent intent) {
        String stringExtra = intent.getStringExtra(ARG_SIP_CANCEL_SID);
        if (OsUtil.isAtLeastO()) {
            scheduler(intent, 2);
        }
        if (TextUtils.isEmpty(stringExtra)) {
            return true;
        }
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            if (VideoBoxApplication.getInstance() == null) {
                VideoBoxApplication.initialize(getApplicationContext(), false, 0);
            }
            VideoBoxApplication.getInstance().initPTMainboard();
            PTApp.getInstance().autoSignin();
        }
        CmmSIPNosManager.getInstance().cancelNosSIPCall(stringExtra);
        return true;
    }

    private boolean handleActionSIPCallMissed(Intent intent) {
        if (intent == null) {
            return true;
        }
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt instanceof IMActivity) && !(inProcessActivityInStackAt instanceof IntegrationActivity) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        Intent intent2 = new Intent(this, IMActivity.class);
        intent2.setFlags(131072);
        intent2.setAction(IMActivity.ACTION_SHOW_SIP_CALL_HISTORY);
        ActivityStartHelper.startActivityForeground(this, intent2);
        return true;
    }

    private boolean handleAcceptSIPCall(Intent intent) {
        if (intent == null) {
            return true;
        }
        Serializable serializableExtra = intent.getSerializableExtra(ARG_NOS_SIP_CALL_ITEM);
        if (serializableExtra instanceof NosSIPCallItem) {
            NosSIPCallItem nosSIPCallItem = (NosSIPCallItem) serializableExtra;
            if ((VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) && CmmSIPCallManager.getInstance().isSipRegistered()) {
                if (CmmSIPCallManager.getInstance().hasSipCallsInCache() && !CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
                    CmmSIPCallManager.getInstance().holdCall();
                }
                CmmSIPNosManager.getInstance().inboundCallPushPickup(nosSIPCallItem);
                CmmSIPNosManager.getInstance().checkNosSIPCallRinging(nosSIPCallItem.getSid());
                NotificationMgr.removeSipIncomeNotification(this);
                SipRingMgr.getInstance().stopRing();
                CmmSIPNosManager.getInstance().setNosSIPCallRinging(false);
                CmmSIPNosManager.getInstance().clearIncomingCallTimeoutMessage();
                return true;
            }
            SipIncomePopActivity.showForAcceptCall(this, nosSIPCallItem);
            return true;
        }
        String stringExtra = intent.getStringExtra(ARG_SIP_CALL_ITEM_ID);
        if (TextUtils.isEmpty(stringExtra) || !CmmSIPCallManager.isInit()) {
            return true;
        }
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
            if (!CmmSIPCallManager.getInstance().hasSipCallsInCache() || CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
                CmmSIPCallManager.getInstance().acceptCall(stringExtra);
            } else {
                CmmSIPCallManager.getInstance().acceptAndHoldCall(stringExtra);
            }
            NotificationMgr.removeSipIncomeNotification(this);
            SipRingMgr.getInstance().stopRing();
            return true;
        }
        SipIncomeActivity.showForAccept(this, stringExtra);
        return true;
    }

    private boolean handleDeclineSIPCall(Intent intent) {
        if (intent == null) {
            return true;
        }
        Serializable serializableExtra = intent.getSerializableExtra(ARG_NOS_SIP_CALL_ITEM);
        if (serializableExtra instanceof NosSIPCallItem) {
            NosSIPCallItem nosSIPCallItem = (NosSIPCallItem) serializableExtra;
            CmmSIPNosManager.getInstance().releaseInboundCall(nosSIPCallItem);
            CmmSIPNosManager.getInstance().checkNosSIPCallRinging(nosSIPCallItem.getSid());
            NotificationMgr.removeSipIncomeNotification(this);
            SipRingMgr.getInstance().stopRing();
            CmmSIPNosManager.getInstance().setNosSIPCallRinging(false);
            CmmSIPNosManager.getInstance().clearIncomingCallTimeoutMessage();
            return true;
        }
        String stringExtra = intent.getStringExtra(ARG_SIP_CALL_ITEM_ID);
        if (TextUtils.isEmpty(stringExtra) || !CmmSIPCallManager.isInit()) {
            return true;
        }
        if (CmmSIPCallManager.getInstance().isCallQueue(stringExtra)) {
            CmmSIPCallManager.getInstance().skipInCQ(stringExtra);
        } else {
            CmmSIPCallManager.getInstance().declineCallWithBusy(stringExtra);
        }
        NotificationMgr.removeSipIncomeNotification(this);
        SipRingMgr.getInstance().stopRing();
        return true;
    }

    private boolean handleReturnIncomeSIPCall(Intent intent) {
        if (intent == null) {
            return true;
        }
        String stringExtra = intent.getStringExtra(ARG_NOS_SIP_CALL_ITEM);
        if (!TextUtils.isEmpty(stringExtra)) {
            SipIncomePopActivity.show(this, (NosSIPCallItem) new Gson().fromJson(stringExtra, NosSIPCallItem.class), intent.getBooleanExtra(ARG_SIP_NEED_INIT_MODULE, false));
            return true;
        }
        String stringExtra2 = intent.getStringExtra(ARG_SIP_CALL_ITEM_ID);
        if (TextUtils.isEmpty(stringExtra2) || !CmmSIPCallManager.isInit()) {
            return true;
        }
        SipIncomeActivity.show(this, stringExtra2);
        return true;
    }

    private boolean handleActionShowPBXUnreadMessage(Intent intent) {
        if (intent == null) {
            return true;
        }
        String stringExtra = intent.getStringExtra(ARG_PBX_MESSAGE_SESSION_ID);
        String stringExtra2 = intent.getStringExtra(ARG_PBX_MESSAGE_PROTO);
        if (TextUtils.isEmpty(stringExtra)) {
            return true;
        }
        NotificationMgr.removePBXMessageNotification(this, stringExtra);
        CmmSIPNosManager.getInstance().prepareSipCall();
        if (!CmmSIPCallManager.isInit()) {
            return true;
        }
        int inProcessActivityCountInStack = ZMActivity.getInProcessActivityCountInStack();
        if (inProcessActivityCountInStack > 0) {
            for (int i = inProcessActivityCountInStack - 1; i >= 0; i--) {
                ZMActivity inProcessActivityInStackAt = ZMActivity.getInProcessActivityInStackAt(i);
                if (!(inProcessActivityInStackAt instanceof IMActivity) && !(inProcessActivityInStackAt instanceof IntegrationActivity) && inProcessActivityInStackAt != null) {
                    inProcessActivityInStackAt.finish();
                }
            }
        }
        Intent intent2 = new Intent(this, IMActivity.class);
        intent2.setFlags(131072);
        intent2.setAction(IMActivity.ACTION_PBX_SHOW_UNREAD_MESSAGE);
        intent2.putExtra(IMActivity.ARG_PBX_MESSAGE_SESSION_ID, stringExtra);
        if (!TextUtils.isEmpty(stringExtra2)) {
            intent2.putExtra(IMActivity.ARG_PBX_MESSAGE_PROTO, stringExtra2);
        }
        ActivityStartHelper.startActivityForeground(this, intent2);
        return true;
    }

    @RequiresApi(api = 26)
    private void scheduler(@NonNull Intent intent, int i) {
        if (OsUtil.isAtLeastO()) {
            JobScheduler jobScheduler = (JobScheduler) getSystemService(JobScheduler.class);
            Builder builder = new Builder(i, new ComponentName(this, PBXJobService.class));
            if (intent.getExtras() != null) {
                builder.setTransientExtras(intent.getExtras());
                builder.setOverrideDeadline(100);
                if (jobScheduler != null) {
                    jobScheduler.schedule(builder.build());
                }
            }
        }
    }

    private boolean handleActionNewIncomingCall(Intent intent) {
        InvitationItem invitationItem = (InvitationItem) intent.getSerializableExtra("invitation");
        if (invitationItem != null) {
            ConfActivity.onNewIncomingCall(this, invitationItem);
        }
        return true;
    }

    private boolean handleActionAcceptCall() {
        IncomingCallManager.getInstance().acceptCall(this, false);
        return true;
    }

    private boolean handlerActionSystemNotification() {
        SystemNotificationFragment.showAsActivity(this, 0);
        return true;
    }

    private boolean handleActionDeclineCall() {
        IncomingCallManager.getInstance().declineCall();
        return true;
    }

    private boolean handleActionInputProxyNamePass(Intent intent) {
        ServerNamePasswordDialog.newInstance(intent.getStringExtra(ARG_SERVER), intent.getIntExtra("port", 0), true, true).show(getSupportFragmentManager(), ServerNamePasswordDialog.class.getName());
        return false;
    }

    private boolean handleActionShowCallNotAnswered(Intent intent) {
        showCallNotAnsweredDialog(intent.getStringExtra(ARG_USERNAME));
        return false;
    }

    private boolean handleActionLoginExpired(Intent intent) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null) {
            return false;
        }
        if (!mainboard.isInitialized()) {
            VideoBoxApplication.getInstance().initPTMainboard();
        }
        PTApp.getInstance().setTokenExpired(true);
        LoginUtil.showLoginUI(this, true, 100);
        return true;
    }

    private boolean handleActionConfirmVerifyCertFailure(Intent intent) {
        VerifyCertFailureDialog.newInstance((VerifyCertEvent) intent.getSerializableExtra(ARG_VERIFY_CERT_EVENT), true).show(getSupportFragmentManager(), VerifyCertFailureDialog.class.getName());
        return false;
    }

    private void handleNewIntentActionConfirmVerifyCertFailure(Intent intent) {
        VerifyCertEvent verifyCertEvent = (VerifyCertEvent) intent.getSerializableExtra(ARG_VERIFY_CERT_EVENT);
        if (verifyCertEvent != null) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                VerifyCertFailureDialog verifyCertFailureDialog = (VerifyCertFailureDialog) supportFragmentManager.findFragmentByTag(VerifyCertFailureDialog.class.getName());
                if (verifyCertFailureDialog != null) {
                    verifyCertFailureDialog.onNewVerifyCertFailure(verifyCertEvent);
                }
            }
        }
    }

    private boolean handleIMErrorMsg(Intent intent) {
        String stringExtra = intent.getStringExtra(ARG_IM_ERROR_MSG);
        if (StringUtil.isEmptyOrNull(stringExtra)) {
            return true;
        }
        ErrorMsgDialog.newInstance(stringExtra, intent.getIntExtra(ARG_IM_ERROR_CODE, -1), true).show(getSupportFragmentManager(), ErrorMsgDialog.class.getName());
        return false;
    }

    private boolean handleInformationBarries() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null || !currentUserProfile.isEnableInformationBarrier()) {
            return true;
        }
        InformationBarriesDialog.show((Context) this, C4558R.string.zm_mm_information_barries_dialog_first_time_msg_115072);
        return false;
    }

    private boolean handleErrorConfirmMsg(Intent intent) {
        String stringExtra = intent.getStringExtra(ARG_ERROR_CONFIRM_MSG);
        if (StringUtil.isEmptyOrNull(stringExtra)) {
            return true;
        }
        int intExtra = intent.getIntExtra(ARG_ERROR_CONFIRM_MSG_CODE, -1);
        String stringExtra2 = intent.getStringExtra(ARG_ERROR_CONFIRM_TITLE);
        long longExtra = intent.getLongExtra(ARG_ERROR_CONFIRM_MSG_INTERVAL, 0);
        boolean booleanExtra = intent.getBooleanExtra(ARG_ERROR_CONFIRM_MSG_FINISH_ONDISMISS, true);
        ErrorInfo errorInfo = new ErrorInfo(stringExtra2, stringExtra, intExtra);
        errorInfo.setInterval(longExtra);
        errorInfo.setFinishActivityOnDismiss(booleanExtra);
        ErrorMsgConfirmDialog.newInstance(errorInfo, null).show(getSupportFragmentManager(), ErrorMsgDialog.class.getName());
        return false;
    }

    private void handleNewIntentActionErrorMsg(Intent intent) {
        String stringExtra = intent.getStringExtra(ARG_IM_ERROR_MSG);
        if (!StringUtil.isEmptyOrNull(stringExtra)) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                int intExtra = intent.getIntExtra(ARG_IM_ERROR_CODE, -1);
                ErrorMsgDialog errorMsgDialog = (ErrorMsgDialog) supportFragmentManager.findFragmentByTag(ErrorMsgDialog.class.getName());
                if (errorMsgDialog != null) {
                    errorMsgDialog.onNewErrorMsg(stringExtra, intExtra);
                }
            }
        }
    }

    private void handleNewIntentActionErrorConfirmMsg(Intent intent) {
        String stringExtra = intent.getStringExtra(ARG_ERROR_CONFIRM_MSG);
        if (!StringUtil.isEmptyOrNull(stringExtra)) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            if (supportFragmentManager != null) {
                int intExtra = intent.getIntExtra(ARG_ERROR_CONFIRM_MSG_CODE, -1);
                String stringExtra2 = intent.getStringExtra(ARG_ERROR_CONFIRM_TITLE);
                long longExtra = intent.getLongExtra(ARG_ERROR_CONFIRM_MSG_INTERVAL, 0);
                boolean booleanExtra = intent.getBooleanExtra(ARG_ERROR_CONFIRM_MSG_FINISH_ONDISMISS, true);
                ErrorInfo errorInfo = new ErrorInfo(stringExtra2, stringExtra, intExtra);
                errorInfo.setInterval(longExtra);
                errorInfo.setFinishActivityOnDismiss(booleanExtra);
                ErrorMsgConfirmDialog errorMsgConfirmDialog = (ErrorMsgConfirmDialog) supportFragmentManager.findFragmentByTag(ErrorMsgDialog.class.getName());
                if (errorMsgConfirmDialog != null) {
                    errorMsgConfirmDialog.onNewErrorMsg(errorInfo);
                }
            }
        }
    }

    public static void onNewIncomingCall(@NonNull Context context, InvitationItem invitationItem) {
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.addFlags(268566528);
        intent.setAction(ACTION_NEW_INCOMING_CALL);
        intent.putExtra("invitation", invitationItem);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void acceptNewIncomingCall(@NonNull Context context, InvitationItem invitationItem) {
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setFlags(268435456);
        intent.setAction(ACTION_ACCEPT_CALL);
        intent.putExtra("invitation", invitationItem);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void declineNewIncomingCall(@NonNull Context context, InvitationItem invitationItem) {
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setFlags(268435456);
        intent.setAction(ACTION_DECLINE_CALL);
        intent.putExtra("invitation", invitationItem);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void promptToInputUserNamePasswordForProxyServer(@NonNull Context context, String str, int i) {
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setFlags(268435456);
        intent.setAction(ACTION_INPUT_PROXY_NAME_PASS);
        intent.putExtra(ARG_SERVER, str);
        intent.putExtra("port", i);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void showCallNotAnsweredMessage(@NonNull Context context, String str) {
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setFlags(268435456);
        intent.setAction(ACTION_SHOW_CALL_NOT_ANSWERED_MESSAGE);
        intent.putExtra(ARG_USERNAME, str);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    private void showCallNotAnsweredDialog(@Nullable String str) {
        if (str != null) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_msg_xxx_did_not_answer_93541, new Object[]{str}), true).show(getSupportFragmentManager(), SimpleMessageDialog.class.getSimpleName());
        }
    }

    public static void promptVerifyCertFailureConfirmation(@NonNull VideoBoxApplication videoBoxApplication, VerifyCertEvent verifyCertEvent) {
        Intent intent = new Intent(videoBoxApplication, IntegrationActivity.class);
        intent.setFlags(268435456);
        intent.setAction(ACTION_CONFIRM_VERIFY_CERT_FAILURE);
        intent.putExtra(ARG_VERIFY_CERT_EVENT, verifyCertEvent);
        ActivityStartHelper.startActivityForeground(videoBoxApplication, intent);
    }

    public static void promptIMErrorMsg(@Nullable VideoBoxApplication videoBoxApplication, String str, int i) {
        if (videoBoxApplication != null && !StringUtil.isEmptyOrNull(str)) {
            Intent intent = new Intent(videoBoxApplication, IntegrationActivity.class);
            intent.setFlags(268435456);
            intent.setAction(ACTION_RECEIVE_IM_ERROR_MSG);
            intent.putExtra(ARG_IM_ERROR_MSG, str);
            intent.putExtra(ARG_IM_ERROR_CODE, i);
            ActivityStartHelper.startActivityForeground(videoBoxApplication, intent);
        }
    }

    public static void promptErrorConfirmMsg(VideoBoxApplication videoBoxApplication, String str, int i) {
        promptErrorConfirmMsg(videoBoxApplication, null, str, i, 0, true);
    }

    public static void promptInfomationBarries(@Nullable VideoBoxApplication videoBoxApplication) {
        if (videoBoxApplication != null) {
            Intent intent = new Intent(videoBoxApplication, IntegrationActivity.class);
            intent.setFlags(268435456);
            intent.setAction(ACTION_RECEIVE_IM_INFORMATION_BARRIES);
            ActivityStartHelper.startActivityForeground(videoBoxApplication, intent);
        }
    }

    public static void promptErrorConfirmMsg(@Nullable VideoBoxApplication videoBoxApplication, String str, String str2, int i, long j, boolean z) {
        if (videoBoxApplication != null && !StringUtil.isEmptyOrNull(str2)) {
            Intent intent = new Intent(videoBoxApplication, IntegrationActivity.class);
            intent.setFlags(268435456);
            intent.setAction(ACTION_RECEIVE_ERROR_CONFIRM_MSG);
            if (!TextUtils.isEmpty(str)) {
                intent.putExtra(ARG_ERROR_CONFIRM_TITLE, str);
            }
            intent.putExtra(ARG_ERROR_CONFIRM_MSG, str2);
            intent.putExtra(ARG_ERROR_CONFIRM_MSG_CODE, i);
            intent.putExtra(ARG_ERROR_CONFIRM_MSG_INTERVAL, j);
            intent.putExtra(ARG_ERROR_CONFIRM_MSG_FINISH_ONDISMISS, z);
            ActivityStartHelper.startActivityForeground(videoBoxApplication, intent);
        }
    }

    public static void showSIPCallFromSchema(@Nullable VideoBoxApplication videoBoxApplication, String str, int i) {
        if (videoBoxApplication != null && !StringUtil.isEmptyOrNull(str)) {
            Intent intent = new Intent(VideoBoxApplication.getInstance(), IntegrationActivity.class);
            intent.setFlags(268435456);
            intent.setAction(ACTION_SIP_CALL_FROM_SCHEMA);
            intent.putExtra(ARG_SIP_CALL_PHONE_NUMBER, str);
            intent.putExtra(ARG_SIP_CALL_URL_ACTION, i);
            ActivityStartHelper.startActivityForeground(videoBoxApplication, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
    }
}
