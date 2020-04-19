package com.zipow.videobox.view.sip;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.dialog.ConfirmAlertDialog;
import com.zipow.videobox.dialog.ConfirmAlertDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CmmPBXFeatureOptionBit;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProto;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.sip.PBXJoinMeetingRequest;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPAPI;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLine;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPNosManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.CmmSipAudioMgr.ICmmSipAudioListener;
import com.zipow.videobox.sip.server.CmmSipAudioMgr.PhoneCallListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.SipPopUtils;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.AudioClip;
import com.zipow.videobox.view.BigRoundListDialog;
import com.zipow.videobox.view.BigRoundListDialog.DialogCallback;
import com.zipow.videobox.view.CallItemCallerIdListItem;
import com.zipow.videobox.view.HoldCallListItem;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.IZMListItemView.IActionClickListener;
import com.zipow.videobox.view.MergeCallListItem;
import com.zipow.videobox.view.MergeSelectCallListItem;
import com.zipow.videobox.view.PresenceStateView;
import com.zipow.videobox.view.ZMListAdapter;
import com.zipow.videobox.view.sip.DialKeyboardView.OnKeyDialListener;
import com.zipow.videobox.view.sip.EndMeetingInPBXDialog.ButtonClickListener;
import com.zipow.videobox.view.sip.HomeKeyMonitorReceiver.SimpleHomekeyListener;
import com.zipow.videobox.view.sip.SipInCallPanelView.OnInCallPanelListener;
import com.zipow.videobox.view.sip.TransferToMeetingDialog.ITransferToMeeting;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkStatusReceiver.SimpleNetworkStatusListener;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.IZMListItem;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SipInCallActivity extends ZMActivity implements OnClickListener, OnKeyDialListener, IHeadsetConnectionListener, ICmmSipAudioListener, IActionClickListener, SimpleHomekeyListener, OnInCallPanelListener, ITransferToMeeting {
    public static final String ACTION_ACCEPT_MEETING_REQUEST = "action_accept_meeting_request";
    public static final String ACTION_RECEIVE_MEETING_REQUEST = "action_receive_meeting_request";
    public static final String ARG_MEETING_REQUEST = "meeting_request";
    public static final long BUDDY_NAME_REFRESH_DELAY = 1000;
    private static final int LONG_CLICK_EVENT_REPEATCOUNT = 4;
    private static final int MSG_ACCEPT_MEETING = 21;
    private static final int MSG_BUDDY_INFO_UPDATE = 10;
    private static final int MSG_CHECK_ERROR_MSG = 3;
    private static final int MSG_DISMISS_ERROR_MSG_BANNER = 2;
    private static final int MSG_REFRESH_CALL_TIME = 1;
    private static final int MSG_SHOW_RECEIVE_MEETING_UI = 20;
    private static final int OOS_DUE_TIME = 60;
    private static final String TAG = "SipInCallActivity";
    private static final int TONE_LENGTH_MS = 150;
    private AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public View mBackToFocusView;
    private TextView mBtnCancelTransfer;
    private Button mBtnCompleteTransfer;
    private View mBtnEndCall;
    private TextView mBtnHideKeyboard;
    private ImageView mBtnMultiAction1;
    private ImageView mBtnMultiAction2;
    private ImageView mBtnMultiMore1;
    private ImageView mBtnMultiMore2;
    private ImageView mBtnOneMore;
    /* access modifiers changed from: private */
    public int mCallMediaStatus = 0;
    private IConfProcessListener mConfProcessListener = new IConfProcessListener() {
        public void onConfProcessStarted() {
        }

        public void onConfProcessStopped() {
            if (SipInCallActivity.this.mMeetAction != null && SipInCallActivity.this.mMeetAction.startMeetingAction()) {
                SipInCallActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SipInCallActivity.this.startMeeting();
                        SipInCallActivity.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (SipInCallActivity.this.mVideoMeetingWaitDialog != null) {
                                    SipInCallActivity.this.mVideoMeetingWaitDialog.dismiss();
                                    SipInCallActivity.this.mVideoMeetingWaitDialog = null;
                                }
                                SipInCallActivity.this.mMeetAction = null;
                            }
                        }, 1000);
                    }
                }, 1000);
            } else if (SipInCallActivity.this.mMeetAction != null && SipInCallActivity.this.mMeetAction.joinMeetingAction()) {
                SipInCallActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SipInCallActivity.this.confirmJoinMeeting(SipInCallActivity.this.mMeetAction.callId);
                        SipInCallActivity.this.mHandler.postDelayed(new Runnable() {
                            public void run() {
                                if (SipInCallActivity.this.mVideoMeetingWaitDialog != null) {
                                    SipInCallActivity.this.mVideoMeetingWaitDialog.dismiss();
                                    SipInCallActivity.this.mVideoMeetingWaitDialog = null;
                                }
                                SipInCallActivity.this.mMeetAction = null;
                            }
                        }, 1000);
                    }
                }, 1000);
            }
        }
    };
    private String mDTMFCallId;
    /* access modifiers changed from: private */
    public String mDTMFNum;
    private Runnable mDTMFReleaseRunnable = new Runnable() {
        public void run() {
            if (SipInCallActivity.this.mDtmfGenerator != null) {
                SipInCallActivity.this.mDtmfGenerator.release();
            }
            SipInCallActivity.this.mDtmfGenerator = null;
        }
    };
    /* access modifiers changed from: private */
    public int mDeviceInfo = 20;
    /* access modifiers changed from: private */
    public ToneGenerator mDtmfGenerator;
    /* access modifiers changed from: private */
    public Runnable mGetFocusRunnable = new Runnable() {
        public void run() {
            if (SipInCallActivity.this.mBackToFocusView != null) {
                AccessibilityUtil.sendAccessibilityFocusEvent(SipInCallActivity.this.mBackToFocusView);
                SipInCallActivity.this.mBackToFocusView = null;
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            int i = message.what;
            if (i != 10) {
                switch (i) {
                    case 1:
                        SipInCallActivity.this.mHandler.removeMessages(1);
                        SipInCallActivity.this.updateCallStateText();
                        SipInCallActivity.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                        return;
                    case 2:
                    case 3:
                        SipInCallActivity.this.checkErrorMessage();
                        return;
                    default:
                        switch (i) {
                            case 20:
                                if (message.obj != null && (message.obj instanceof PBXJoinMeetingRequest)) {
                                    SipInCallActivity.this.onReceivedJoinMeetingRequest(((PBXJoinMeetingRequest) message.obj).getCallId());
                                    return;
                                }
                                return;
                            case 21:
                                if (message.obj != null && (message.obj instanceof PBXJoinMeetingRequest)) {
                                    SipInCallActivity.this.onActionJoinMeeting(((PBXJoinMeetingRequest) message.obj).getCallId());
                                    return;
                                }
                                return;
                            default:
                                return;
                        }
                }
            } else {
                SipInCallActivity.this.updatePanelBuddyInfo();
            }
        }
    };
    private HomeKeyMonitorReceiver mHomeKeyMonitorReceiver;
    private boolean mIsDTMFMode;
    private boolean mIsLongClick = false;
    private DialKeyboardView mKeyboardView;
    /* access modifiers changed from: private */
    public MeetAction mMeetAction;
    @Nullable
    private IZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onIndicateInfoUpdatedWithJID(String str) {
            if (!SipInCallActivity.this.mHandler.hasMessages(10)) {
                SipInCallActivity.this.mHandler.sendEmptyMessageDelayed(10, 1000);
            }
        }
    };
    private PresenceStateView mMultiPresenceStateView1;
    private PresenceStateView mMultiPresenceStateView2;
    private SimpleNetworkStatusListener mNetworkStatusListener = new SimpleNetworkStatusListener() {
        public void networkStatusChanged(boolean z, int i, String str, boolean z2, int i2, String str2) {
            super.networkStatusChanged(z, i, str, z2, i2, str2);
            SipInCallActivity.this.updateUI();
            SipInCallActivity.this.checkErrorMessageDelayed();
            SipMoreActionFragment.dismiss(SipInCallActivity.this);
            CallToCarrierFragment.dismiss(SipInCallActivity.this);
        }
    };
    private BigRoundListDialog mOnActionListDialog;
    private ISIPCallEventListener mOnSipCallEventListener = new SimpleSIPCallEventListener() {
        public void OnNewCallGenerate(String str, int i) {
            super.OnNewCallGenerate(str, i);
            SipInCallActivity.this.checkDialog();
        }

        public void OnCallStatusUpdate(String str, int i) {
            if (i == 26 || i == 33) {
                if (!SipInCallActivity.this.isCurrentCallInRinging()) {
                    SipInCallActivity.this.stopRing();
                }
            } else if (i == 28) {
                if (!SipInCallActivity.this.isCurrentCallInRinging()) {
                    SipInCallActivity.this.stopRing();
                }
                SipInCallActivity.this.mDTMFNum = "";
                SipInCallActivity.this.checkAndShowJoinMeetingUI();
            }
            if (!TextUtils.isEmpty(str) && str.equals(CmmSIPCallManager.getInstance().getCurrentCallID())) {
                SipMoreActionFragment.dismiss(SipInCallActivity.this);
            }
            SipInCallActivity.this.updateUI();
        }

        public void OnMuteCallResult(boolean z) {
            SipInCallActivity.this.updatePanelInCall();
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            SipInCallActivity.this.checkDialog();
            if (!SipInCallActivity.this.isCurrentCallInRinging()) {
                SipInCallActivity.this.stopRing();
            }
            if (!TextUtils.isEmpty(str) && str.equals(CmmSIPCallManager.getInstance().getCurrentCallID())) {
                CallToCarrierFragment.dismiss(SipInCallActivity.this);
            }
            if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                SipInCallActivity.this.updateUI();
                SipInCallActivity.this.mHandler.postDelayed(new Runnable() {
                    public void run() {
                        SipInCallActivity.this.checkAndShowJoinMeetingUI();
                    }
                }, 1000);
                return;
            }
            SipInCallActivity.this.finish();
        }

        public void OnSIPCallServiceStoped(boolean z) {
            super.OnSIPCallServiceStoped(z);
            SipInCallActivity.this.finish();
        }

        public void OnCallRecordingResult(String str, int i, int i2) {
            super.OnCallRecordingResult(str, i, i2);
            if (str != null && str.equals(CmmSIPCallManager.getInstance().getCurrentCallID())) {
                if (!(i2 == 0)) {
                    String recordErrorString = CmmSIPCallManager.getInstance().getRecordErrorString(i2);
                    if (!TextUtils.isEmpty(recordErrorString)) {
                        Toast.makeText(SipInCallActivity.this, recordErrorString, 1).show();
                    }
                }
                SipInCallActivity.this.updatePanelInCall();
            }
        }

        public void OnCallRecordingStatusUpdate(String str, int i) {
            super.OnCallRecordingStatusUpdate(str, i);
            SipInCallActivity.this.updatePanelInCall();
        }

        public void OnCallActionResult(String str, int i, boolean z) {
            super.OnCallActionResult(str, i, z);
            if (!z) {
                switch (i) {
                    case 5:
                        Toast.makeText(SipInCallActivity.this, C4558R.string.zm_sip_hold_failed_27110, 1).show();
                        break;
                    case 6:
                        Toast.makeText(SipInCallActivity.this, C4558R.string.zm_sip_unhold_failed_27110, 1).show();
                        break;
                }
                SipInCallActivity.this.updatePanelInCall();
            }
        }

        public void OnSendDTMFResult(String str, String str2, boolean z) {
            super.OnSendDTMFResult(str, str2, z);
            if (!z) {
                Toast.makeText(SipInCallActivity.this, C4558R.string.zm_sip_dtmf_failed_27110, 1).show();
            }
        }

        public void OnCheckPhoneNumberFailed(String str) {
            super.OnCheckPhoneNumberFailed(str);
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
            if (currentCallItem == null || (instance.isNumberFailed(currentCallItem.getPeerNumber()) && instance.getSipIdCountInCache() == 1)) {
                SipInCallActivity.this.finish();
            }
        }

        public void OnMergeCallResult(boolean z, String str, String str2) {
            super.OnMergeCallResult(z, str, str2);
            SipInCallActivity.this.updateUI();
        }

        public void OnMeetingStartedResult(String str, long j, String str2, boolean z) {
            super.OnMeetingStartedResult(str, j, str2, z);
            SipInCallActivity.this.onMeetingStartedResult(str, j, str2, z);
        }

        public void OnReceivedJoinMeetingRequest(final String str, long j, String str2) {
            super.OnReceivedJoinMeetingRequest(str, j, str2);
            if (SipInCallActivity.this.getEventTaskManager() != null) {
                SipInCallActivity.this.getEventTaskManager().pushLater("ReceivedJoinMeetingRequest", new EventAction() {
                    public void run(IUIElement iUIElement) {
                        SipInCallActivity.this.onReceivedJoinMeetingRequest(str);
                    }
                });
            }
        }

        public void OnMeetingJoinedResult(String str, boolean z) {
            super.OnMeetingJoinedResult(str, z);
            SipInCallActivity.this.onMeetingJoinedResult(str, z);
        }

        public void OnRequestDoneForQueryPBXUserInfo(boolean z) {
            super.OnRequestDoneForQueryPBXUserInfo(z);
            SipInCallActivity.this.updateUI();
        }

        public void OnPBXUserStatusChange(int i) {
            super.OnPBXUserStatusChange(i);
            SipInCallActivity.this.updateUI();
        }

        public void OnCallMediaStatusUpdate(String str, int i, String str2) {
            super.OnCallMediaStatusUpdate(str, i, str2);
            if (i != 1000) {
                SipInCallActivity.this.mCallMediaStatus = i;
                SipInCallActivity.this.checkErrorMessageDelayed();
            } else if (NetworkUtil.hasDataNetwork(SipInCallActivity.this)) {
                SipInCallActivity sipInCallActivity = SipInCallActivity.this;
                sipInCallActivity.showSipErrorMessagePanel(sipInCallActivity.getString(C4558R.string.zm_sip_error_data_99728), 5000, GravityCompat.START, true);
            }
        }

        public void OnAudioDeviceSpecialInfoUpdate(int i, int i2) {
            super.OnAudioDeviceSpecialInfoUpdate(i, i2);
            if (i == 1) {
                int i3 = 20;
                if (SipInCallActivity.this.mDeviceInfo == 4 && i2 == 20) {
                    SipInCallActivity.this.mDeviceInfo = 20;
                    SipInCallActivity sipInCallActivity = SipInCallActivity.this;
                    sipInCallActivity.showSipErrorMessagePanel(sipInCallActivity.getString(C4558R.string.zm_sip_device_connected_113584), 3000, 17, false);
                    return;
                }
                SipInCallActivity sipInCallActivity2 = SipInCallActivity.this;
                if (i2 == 4) {
                    i3 = 4;
                }
                sipInCallActivity2.mDeviceInfo = i3;
                SipInCallActivity.this.checkErrorMessageDelayed();
            }
        }

        public void OnCallRemoteMergerEvent(String str, int i, CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto) {
            super.OnCallRemoteMergerEvent(str, i, cmmSIPCallRemoteMemberProto);
            SipInCallActivity.this.updatePanelBuddyInfo();
            SipInCallActivity.this.checkDialog();
        }

        public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
            SipInCallActivity.this.OnPBXFeatureOptionsChanged(list);
        }

        public void OnRequestDoneForUpdatePBXFeatureOptions(boolean z, List<CmmPBXFeatureOptionBit> list) {
            SipInCallActivity.this.OnRequestDoneForUpdatePBXFeatureOptions(z, list);
        }

        public void OnSwitchCallToCarrierResult(String str, boolean z, int i) {
            super.OnSwitchCallToCarrierResult(str, z, i);
            SipInCallActivity.this.updatePanelInCall();
            SipInCallActivity.this.updateTopPanelTips();
        }

        public void OnCallAutoRecordingEvent(String str, int i) {
            super.OnCallAutoRecordingEvent(str, i);
            if (SipInCallActivity.this.mPanelInCall != null) {
                SipInCallActivity.this.mPanelInCall.OnCallAutoRecordingEvent(i);
            }
            SipInCallActivity.this.updatePanelInCall();
        }

        public void OnTalkingStatusChanged(boolean z) {
            super.OnTalkingStatusChanged(z);
            SipInCallActivity.this.mPanelInCall.togglePanelMuteTalkingState(z);
        }

        public void OnPBXServiceRangeChanged(int i) {
            super.OnPBXServiceRangeChanged(i);
            SipInCallActivity.this.checkErrorMessageDelayed();
        }

        public void OnPBXMediaModeUpdate(@Nullable String str, int i) {
            super.OnPBXMediaModeUpdate(str, i);
            SipInCallActivity.this.mPanelInCall.setMediaMode();
        }

        public void OnPeerJoinMeetingResult(String str, long j, boolean z) {
            super.OnPeerJoinMeetingResult(str, j, z);
            if (!z) {
                SipInCallActivity sipInCallActivity = SipInCallActivity.this;
                sipInCallActivity.showSipErrorMessagePanel(sipInCallActivity.getString(C4558R.string.zm_sip_merge_into_meeting_fail_108093), 5000, 17, true);
            }
        }
    };
    private PresenceStateView mOnePresenceStateView;
    /* access modifiers changed from: private */
    public SipInCallPanelView mPanelInCall;
    private View mPanelMultiBuddyList;
    private View mPanelMultiCall1;
    private View mPanelMultiCall2;
    private View mPanelOneBuddyInfo;
    private View mPanelSipError;
    private View mPanelTransferOption;
    PhoneCallListener mPhoneCallListener = new PhoneCallListener() {
        public void onPhoneCallIdle() {
            SipInCallActivity.this.updatePanelInCall();
        }

        public void onPhoneCallOffHook() {
            SipInCallActivity.this.updatePanelInCall();
        }
    };
    private AudioClip mRingClip = null;
    private View mTopPanelTips;
    private TextView mTxtMultiBuddyName1;
    private TextView mTxtMultiBuddyName2;
    private TextView mTxtMultiDialState1;
    private TextView mTxtMultiDialState2;
    private TextView mTxtOneBuddyName;
    private TextView mTxtOneDialState;
    private TextView mTxtSipError;
    private TextView mTxtTips;
    /* access modifiers changed from: private */
    public Dialog mVideoMeetingWaitDialog;

    private static class MeetAction {
        private int action;
        /* access modifiers changed from: private */
        public String callId;

        public interface Action {
            public static final int JOIN_MEETING = 2;
            public static final int START_MEETING = 1;
        }

        public MeetAction(String str, int i) {
            this.callId = str;
            this.action = i;
        }

        public boolean hasAction() {
            return this.action > 0;
        }

        public boolean startMeetingAction() {
            return this.action == 1;
        }

        public boolean joinMeetingAction() {
            return this.action == 2;
        }
    }

    /* access modifiers changed from: private */
    public void onMeetingJoinedResult(String str, boolean z) {
    }

    public static void show(Context context) {
        Intent intent = new Intent(context, SipInCallActivity.class);
        intent.addFlags(268435456);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void returnToSip(@Nullable Context context) {
        if (context != null) {
            if (!CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                CmmSIPCallManager.getInstance().clearSipCache();
                CmmSIPCallManager.getInstance().removeSipNotification();
                return;
            }
            Intent intent = new Intent(context, SipInCallActivity.class);
            intent.addFlags(131072);
            if (!(context instanceof Activity)) {
                intent.addFlags(268435456);
            }
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    public static void returnToSipForMeetingRequest(Context context, PBXJoinMeetingRequest pBXJoinMeetingRequest) {
        returnToSipForMeetingRequest(context, ACTION_RECEIVE_MEETING_REQUEST, pBXJoinMeetingRequest);
    }

    public static void returnToSipForMeetingRequest(Context context, @NonNull String str, @NonNull PBXJoinMeetingRequest pBXJoinMeetingRequest) {
        if (!CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            CmmSIPCallManager.getInstance().clearSipCache();
            CmmSIPCallManager.getInstance().removeSipNotification();
        } else if (CmmSIPCallManager.getInstance().containsInCache(pBXJoinMeetingRequest.getCallId())) {
            Intent intent = new Intent(context, SipInCallActivity.class);
            intent.setAction(str);
            intent.addFlags(131072);
            if (!(context instanceof Activity)) {
                intent.addFlags(268435456);
            }
            intent.putExtra(ARG_MEETING_REQUEST, pBXJoinMeetingRequest);
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(6815873);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        setContentView(C4558R.layout.zm_sip_in_call);
        this.mPanelSipError = findViewById(C4558R.C4560id.panelSipError);
        this.mTxtSipError = (TextView) findViewById(C4558R.C4560id.txtSipError);
        this.mBtnHideKeyboard = (TextView) findViewById(C4558R.C4560id.btnHideKeyboard);
        this.mKeyboardView = (DialKeyboardView) findViewById(C4558R.C4560id.keyboard);
        this.mPanelInCall = (SipInCallPanelView) findViewById(C4558R.C4560id.panelInCall);
        this.mBtnEndCall = findViewById(C4558R.C4560id.btnEndCall);
        this.mPanelOneBuddyInfo = findViewById(C4558R.C4560id.panelOneBuddy);
        this.mTxtOneBuddyName = (TextView) findViewById(C4558R.C4560id.txtOneBuddyName);
        this.mTxtOneDialState = (TextView) findViewById(C4558R.C4560id.txtOneDialState);
        this.mOnePresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.onePresenceStateView);
        this.mPanelMultiBuddyList = findViewById(C4558R.C4560id.panelMultiBuddy);
        this.mPanelMultiCall1 = findViewById(C4558R.C4560id.panelMultiCall1);
        this.mTxtMultiBuddyName1 = (TextView) findViewById(C4558R.C4560id.txtMultiBuddyName1);
        this.mTxtMultiDialState1 = (TextView) findViewById(C4558R.C4560id.txtMultiDialState1);
        this.mMultiPresenceStateView1 = (PresenceStateView) findViewById(C4558R.C4560id.multiPresenceStateView1);
        this.mPanelMultiCall2 = findViewById(C4558R.C4560id.panelMultiCall2);
        this.mTxtMultiBuddyName2 = (TextView) findViewById(C4558R.C4560id.txtMultiBuddyName2);
        this.mTxtMultiDialState2 = (TextView) findViewById(C4558R.C4560id.txtMultiDialState2);
        this.mMultiPresenceStateView2 = (PresenceStateView) findViewById(C4558R.C4560id.multiPresenceStateView2);
        this.mBtnOneMore = (ImageView) findViewById(C4558R.C4560id.btnOneMore);
        this.mBtnMultiMore1 = (ImageView) findViewById(C4558R.C4560id.btnMultiMore1);
        this.mBtnMultiAction1 = (ImageView) findViewById(C4558R.C4560id.btnMultiAction1);
        this.mBtnMultiMore2 = (ImageView) findViewById(C4558R.C4560id.btnMultiMore2);
        this.mBtnMultiAction2 = (ImageView) findViewById(C4558R.C4560id.btnMultiAction2);
        this.mPanelTransferOption = findViewById(C4558R.C4560id.panelTransferOption);
        this.mBtnCompleteTransfer = (Button) findViewById(C4558R.C4560id.btnCompleteTransfer);
        this.mBtnCancelTransfer = (TextView) findViewById(C4558R.C4560id.btnCancelTransfer);
        this.mTopPanelTips = findViewById(C4558R.C4560id.panelTips);
        this.mTxtTips = (TextView) findViewById(16908299);
        this.mPanelMultiCall2.setOnClickListener(this);
        this.mPanelMultiCall1.setOnClickListener(this);
        this.mBtnOneMore.setOnClickListener(this);
        this.mBtnMultiMore1.setOnClickListener(this);
        this.mBtnMultiAction1.setOnClickListener(this);
        this.mBtnMultiMore2.setOnClickListener(this);
        this.mBtnMultiAction2.setOnClickListener(this);
        this.mTxtOneDialState.setOnClickListener(this);
        this.mBtnCompleteTransfer.setOnClickListener(this);
        this.mBtnCancelTransfer.setOnClickListener(this);
        this.mBtnEndCall.setOnClickListener(this);
        this.mBtnHideKeyboard.setOnClickListener(this);
        this.mKeyboardView.setOnKeyDialListener(this);
        if (bundle != null) {
            this.mDTMFNum = bundle.getString("mDTMFNum");
            this.mIsDTMFMode = bundle.getBoolean("mIsDTMFMode");
            this.mDTMFCallId = bundle.getString("mDTMFCallId");
        }
        this.mPanelInCall.setDTMFMode(this.mIsDTMFMode);
        this.mPanelInCall.setOnInCallPanelListener(this);
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (!instance.hasSipCallsInCache()) {
            finish();
            return;
        }
        instance.addListener(this.mOnSipCallEventListener);
        instance.addNetworkListener(this.mNetworkStatusListener);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
        VideoBoxApplication.getNonNullInstance().addConfProcessListener(this.mConfProcessListener);
        initSpeaker();
        stopFloatWindowService();
        if (instance.isCallout(instance.getCurrentCallID()) && instance.isCurrentCallLocal()) {
            CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
            if (currentCallItem == null || (instance.isNumberFailed(currentCallItem.getPeerNumber()) && instance.getSipIdCountInCache() == 1)) {
                finish();
                return;
            }
        }
        checkAndStartRing();
        this.mHomeKeyMonitorReceiver = new HomeKeyMonitorReceiver(this);
        this.mHomeKeyMonitorReceiver.register(this);
        CmmSipAudioMgr.getInstance().addSipAudioListener(this);
        CmmSipAudioMgr.getInstance().addListener(this.mPhoneCallListener);
        Intent intent = getIntent();
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_ACCEPT_MEETING_REQUEST.equals(action)) {
                actionAcceptMeetingRequest(intent);
            } else if (ACTION_RECEIVE_MEETING_REQUEST.equals(action)) {
                actionReceiveMeetingRequest(intent);
            }
        }
    }

    private void actionReceiveMeetingRequest(Intent intent) {
        Serializable serializableExtra = intent.getSerializableExtra(ARG_MEETING_REQUEST);
        if (serializableExtra != null) {
            PBXJoinMeetingRequest pBXJoinMeetingRequest = (PBXJoinMeetingRequest) serializableExtra;
            this.mHandler.removeMessages(20);
            Message obtainMessage = this.mHandler.obtainMessage();
            obtainMessage.what = 20;
            obtainMessage.obj = pBXJoinMeetingRequest;
            this.mHandler.sendMessageDelayed(obtainMessage, 300);
        }
    }

    private void actionAcceptMeetingRequest(Intent intent) {
        Serializable serializableExtra = intent.getSerializableExtra(ARG_MEETING_REQUEST);
        if (serializableExtra != null) {
            PBXJoinMeetingRequest pBXJoinMeetingRequest = (PBXJoinMeetingRequest) serializableExtra;
            if (!TextUtils.isEmpty(pBXJoinMeetingRequest.getCallId())) {
                this.mHandler.removeMessages(21);
                Message obtainMessage = this.mHandler.obtainMessage();
                obtainMessage.what = 21;
                obtainMessage.obj = pBXJoinMeetingRequest;
                this.mHandler.sendMessageDelayed(obtainMessage, 300);
            }
        }
    }

    private void initSpeaker() {
        if (CmmSIPCallManager.getInstance().getSipIdCountInCache() <= 1) {
            toggleSpeakerState(false);
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        this.mDTMFNum = null;
        this.mIsDTMFMode = false;
        this.mPanelInCall.setDTMFMode(false);
        stopFloatWindowService();
        checkAndStartRing();
        String action = intent.getAction();
        if (ACTION_ACCEPT_MEETING_REQUEST.equals(action)) {
            actionAcceptMeetingRequest(intent);
        } else if (ACTION_RECEIVE_MEETING_REQUEST.equals(action)) {
            actionReceiveMeetingRequest(intent);
        }
    }

    public void onDestroy() {
        this.mHandler.removeCallbacksAndMessages(null);
        BigRoundListDialog bigRoundListDialog = this.mOnActionListDialog;
        if (bigRoundListDialog != null && bigRoundListDialog.isShowing()) {
            this.mOnActionListDialog.dismiss();
            this.mOnActionListDialog = null;
        }
        Dialog dialog = this.mVideoMeetingWaitDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mVideoMeetingWaitDialog.dismiss();
            this.mVideoMeetingWaitDialog = null;
        }
        HomeKeyMonitorReceiver homeKeyMonitorReceiver = this.mHomeKeyMonitorReceiver;
        if (homeKeyMonitorReceiver != null) {
            homeKeyMonitorReceiver.unregister(this);
        }
        super.onDestroy();
        CmmSIPCallManager.getInstance().removeListener(this.mOnSipCallEventListener);
        CmmSIPCallManager.getInstance().removeNetworkListener(this.mNetworkStatusListener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        VideoBoxApplication.getNonNullInstance().removeConfProcessListener(this.mConfProcessListener);
        stopRing();
        CmmSipAudioMgr.getInstance().removeSipAudioListener(this);
        CmmSipAudioMgr.getInstance().removeListener(this.mPhoneCallListener);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        if (bundle != null) {
            bundle.putString("mDTMFNum", this.mDTMFNum);
            bundle.putBoolean("mIsDTMFMode", this.mIsDTMFMode);
            bundle.putString("mDTMFCallId", this.mDTMFCallId);
        }
    }

    private void checkAndStartRing() {
        try {
            if (((AudioManager) getSystemService("audio")) == null) {
                return;
            }
        } catch (Exception unused) {
        }
        if (isCurrentCallInRinging()) {
            if (this.mRingClip == null) {
                this.mRingClip = new AudioClip(C4558R.raw.zm_dudu, 0);
            }
            if (!this.mRingClip.isPlaying()) {
                this.mRingClip.startPlay();
            }
        }
    }

    /* access modifiers changed from: private */
    public boolean isCurrentCallInRinging() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        boolean z = false;
        if (instance.isCallout(instance.getCurrentCallID())) {
            CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
            if (currentCallItem != null) {
                int callStatus = currentCallItem.getCallStatus();
                if (callStatus == 20 || callStatus == 0) {
                    z = true;
                }
                return z;
            }
        }
        return false;
    }

    private void toggleSpeaker() {
        if (HeadsetUtil.getInstance().isBTAndWiredHeadsetsOn()) {
            SipSwitchOutputAudioDialog.showDialog(getSupportFragmentManager());
            return;
        }
        boolean z = !CmmSipAudioMgr.getInstance().isSpeakerOn();
        toggleSpeakerState(z);
        if (HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
            if (!z) {
                startBluetoothSco();
            } else {
                stopBluetoothSco();
            }
        } else if (HeadsetUtil.getInstance().isWiredHeadsetOn() && !z) {
            startWiredHeadset();
        }
        updatePanelInCall();
        checkProximityScreenOffWakeLock();
    }

    private void toggleSpeakerState(boolean z) {
        CmmSIPCallManager.getInstance().toggleSpeakerState(z);
    }

    /* access modifiers changed from: private */
    public void stopRing() {
        AudioClip audioClip = this.mRingClip;
        if (audioClip != null) {
            audioClip.stopPlay();
            this.mRingClip = null;
        }
    }

    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.btnEndCall) {
                onClickEndCall();
            } else if (id == C4558R.C4560id.btnHideKeyboard) {
                onClickTxtHide();
            } else if (id == C4558R.C4560id.panelMultiCall2) {
                onClickMultiCall2();
            } else if (id == C4558R.C4560id.panelMultiCall1) {
                onClickMultiCall1();
            } else if (id == C4558R.C4560id.btnCompleteTransfer) {
                onClickCompleteTransfer();
            } else if (id == C4558R.C4560id.btnCancelTransfer) {
                onClickCancelTransfer();
            } else if (id == C4558R.C4560id.btnOneMore) {
                onClickBtnOneMore();
            } else if (id == C4558R.C4560id.btnMultiMore1) {
                onClickBtnMultiMore1();
            } else if (id == C4558R.C4560id.btnMultiMore2) {
                onClickBtnMultiMore2();
            } else if (id == C4558R.C4560id.btnMultiAction1) {
                onClickBtnMultiAction1();
            } else if (id == C4558R.C4560id.btnMultiAction2) {
                onClickBtnMultiAction2();
            } else if (id == C4558R.C4560id.txtOneDialState) {
                onClickTxtOneDialState();
            }
        }
    }

    private void onClickMultiCall1() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            if (isCurrentCallBindToPanel1()) {
                String callID = currentCallItem.getCallID();
                if (instance.isInJoinMeeingRequest(callID)) {
                    onActionJoinMeeting(callID);
                }
            } else {
                CmmSIPCallItem otherSIPCallItem = instance.getOtherSIPCallItem(currentCallItem);
                if (otherSIPCallItem != null) {
                    String callID2 = otherSIPCallItem.getCallID();
                    if (instance.isInJoinMeeingRequest(callID2)) {
                        onActionJoinMeeting(callID2);
                    } else {
                        resumeCall(callID2);
                    }
                }
            }
        }
    }

    private void onClickMultiCall2() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        int size = instance.getSipCallIds().size();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            boolean isTransferring = instance.isTransferring(currentCallItem);
            if (isTransferring || size == 2) {
                if (isCurrentCallBindToPanel1()) {
                    CmmSIPCallItem otherSIPCallItem = instance.getOtherSIPCallItem(currentCallItem, isTransferring);
                    if (otherSIPCallItem != null) {
                        String callID = otherSIPCallItem.getCallID();
                        if (instance.isInJoinMeeingRequest(callID)) {
                            onActionJoinMeeting(callID);
                        } else {
                            resumeCall(callID);
                        }
                    }
                } else {
                    String callID2 = currentCallItem.getCallID();
                    if (instance.isInJoinMeeingRequest(callID2)) {
                        onActionJoinMeeting(callID2);
                    }
                }
            } else if (size > 2) {
                showOnHoldListDialog();
            }
        }
    }

    private void showOnHoldListDialog() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        final ZMListAdapter zMListAdapter = new ZMListAdapter(this, this);
        zMListAdapter.setShowSelect(false);
        Stack sipCallIds = instance.getSipCallIds();
        int currentIndexInCallCache = instance.getCurrentIndexInCallCache();
        if (sipCallIds != null) {
            for (int size = sipCallIds.size() - 1; size >= 0; size--) {
                if (currentIndexInCallCache != size) {
                    HoldCallListItem holdCallListItem = new HoldCallListItem((String) sipCallIds.get(size));
                    holdCallListItem.init(getApplicationContext());
                    zMListAdapter.addItem(holdCallListItem);
                }
            }
        }
        showMoreActionDialog(this.mTxtMultiBuddyName2.getText().toString(), zMListAdapter, new DialogCallback() {
            public void onCancel() {
            }

            public void onClose() {
            }

            public void onItemSelected(int i) {
                String id = ((HoldCallListItem) zMListAdapter.getItem(i)).getId();
                if (CmmSIPCallManager.getInstance().isInJoinMeeingRequest(id)) {
                    SipInCallActivity.this.onActionJoinMeeting(id);
                    return;
                }
                SipInCallActivity.this.resumeCall(id);
                SipInCallActivity.this.updateUI();
            }
        });
    }

    private void showMergedListDialog(String str) {
        if (!TextUtils.isEmpty(str)) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(str);
            if (callItemByCallID != null) {
                final ZMListAdapter zMListAdapter = new ZMListAdapter(this, this);
                zMListAdapter.setShowSelect(false);
                MergeCallListItem mergeCallListItem = new MergeCallListItem(str);
                mergeCallListItem.init(getApplicationContext());
                zMListAdapter.addItem(mergeCallListItem);
                List remoteMergerMembers = callItemByCallID.getRemoteMergerMembers();
                if (remoteMergerMembers != null && !remoteMergerMembers.isEmpty()) {
                    for (int i = 0; i < remoteMergerMembers.size(); i++) {
                        CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto = (CmmSIPCallRemoteMemberProto) remoteMergerMembers.get(i);
                        zMListAdapter.addItem(new MergeCallListItem(instance.getRemoteMemberDisplayName(cmmSIPCallRemoteMemberProto), cmmSIPCallRemoteMemberProto.getDisplayNumber()));
                    }
                }
                int conferenceParticipantsCount = callItemByCallID.getConferenceParticipantsCount();
                for (int i2 = 0; i2 < conferenceParticipantsCount; i2++) {
                    String conferenceParticipantCallItemByIndex = callItemByCallID.getConferenceParticipantCallItemByIndex(i2);
                    MergeCallListItem mergeCallListItem2 = new MergeCallListItem(conferenceParticipantCallItemByIndex);
                    mergeCallListItem2.init(getApplicationContext());
                    zMListAdapter.addItem(mergeCallListItem2);
                    CmmSIPCallItem callItemByCallID2 = instance.getCallItemByCallID(conferenceParticipantCallItemByIndex);
                    if (callItemByCallID2 != null) {
                        List remoteMergerMembers2 = callItemByCallID2.getRemoteMergerMembers();
                        if (remoteMergerMembers2 != null && !remoteMergerMembers2.isEmpty()) {
                            for (int i3 = 0; i3 < remoteMergerMembers2.size(); i3++) {
                                CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto2 = (CmmSIPCallRemoteMemberProto) remoteMergerMembers2.get(i3);
                                zMListAdapter.addItem(new MergeCallListItem(instance.getRemoteMemberDisplayName(cmmSIPCallRemoteMemberProto2), cmmSIPCallRemoteMemberProto2.getDisplayNumber()));
                            }
                        }
                    }
                }
                zMListAdapter.addItem(new MergeCallListItem(PTApp.getInstance().getMyName(), instance.getCallerId(this, callItemByCallID)));
                showMoreActionDialog(getString(C4558R.string.zm_sip_call_item_callers_title_85311), zMListAdapter, new DialogCallback() {
                    public void onCancel() {
                    }

                    public void onClose() {
                    }

                    public void onItemSelected(int i) {
                        String id = ((MergeCallListItem) zMListAdapter.getItem(i)).getId();
                        if (!TextUtils.isEmpty(id)) {
                            CmmSIPCallManager.getInstance().hangupCall(id);
                            SipInCallActivity.this.updateUI();
                        }
                    }
                });
            }
        }
    }

    private void showMergeSelectDialog() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        final ZMListAdapter zMListAdapter = new ZMListAdapter(this, this);
        zMListAdapter.setShowSelect(false);
        Stack sipCallIds = instance.getSipCallIds();
        int currentIndexInCallCache = instance.getCurrentIndexInCallCache();
        if (sipCallIds != null) {
            for (int size = sipCallIds.size() - 1; size >= 0; size--) {
                if (currentIndexInCallCache != size) {
                    String str = (String) sipCallIds.get(size);
                    if (!instance.isInJoinMeeingRequest(str)) {
                        MergeSelectCallListItem mergeSelectCallListItem = new MergeSelectCallListItem(str);
                        mergeSelectCallListItem.init(getApplicationContext());
                        zMListAdapter.addItem(mergeSelectCallListItem);
                    }
                }
            }
        }
        showMoreActionDialog(getString(C4558R.string.zm_sip_merge_call_title_111496), zMListAdapter, new DialogCallback() {
            public void onCancel() {
            }

            public void onClose() {
            }

            public void onItemSelected(int i) {
                SipInCallActivity.this.mergeCall(((MergeSelectCallListItem) zMListAdapter.getItem(i)).getId());
            }
        });
    }

    /* access modifiers changed from: private */
    public void mergeCall(final String str) {
        if (!CmmSIPCallManager.getInstance().hasMeetings() || !CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
            confirmMergeCall(str);
        } else {
            ConfirmAlertDialog.show(this, getString(C4558R.string.zm_sip_callpeer_inmeeting_title_108086), getString(C4558R.string.zm_sip_merge_call_inmeeting_msg_108086), new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    SipInCallActivity.this.confirmMergeCall(str);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void confirmMergeCall(String str) {
        CmmSIPCallManager.getInstance().mergeCall(CmmSIPCallManager.getInstance().getCurrentCallID(), str);
        updateUI();
    }

    private void showCallerIdDialog(CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem != null) {
            String string = getString(C4558R.string.zm_sip_call_item_callers_title_85311);
            ZMListAdapter zMListAdapter = new ZMListAdapter(this, null);
            zMListAdapter.setShowSelect(false);
            CallItemCallerIdListItem callItemCallerIdListItem = new CallItemCallerIdListItem();
            callItemCallerIdListItem.init(this, CmmSIPCallManager.getInstance().getDisplayName(cmmSIPCallItem), cmmSIPCallItem.getPeerFormatNumber());
            zMListAdapter.addItem(callItemCallerIdListItem);
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            List remoteMergerMembers = cmmSIPCallItem.getRemoteMergerMembers();
            if (remoteMergerMembers != null && !remoteMergerMembers.isEmpty()) {
                for (int i = 0; i < remoteMergerMembers.size(); i++) {
                    CmmSIPCallRemoteMemberProto cmmSIPCallRemoteMemberProto = (CmmSIPCallRemoteMemberProto) remoteMergerMembers.get(i);
                    CallItemCallerIdListItem callItemCallerIdListItem2 = new CallItemCallerIdListItem();
                    callItemCallerIdListItem2.init(this, instance.getRemoteMemberDisplayName(cmmSIPCallRemoteMemberProto), cmmSIPCallRemoteMemberProto.getDisplayNumber());
                    zMListAdapter.addItem(callItemCallerIdListItem2);
                }
            }
            String myName = PTApp.getInstance().getMyName();
            CallItemCallerIdListItem callItemCallerIdListItem3 = new CallItemCallerIdListItem();
            callItemCallerIdListItem3.init(this, myName, CmmSIPCallManager.getInstance().getCallerId(this, cmmSIPCallItem));
            zMListAdapter.addItem(callItemCallerIdListItem3);
            showMoreActionDialog(string, zMListAdapter, null);
        }
    }

    private void showMoreActionDialog(String str, ZMListAdapter zMListAdapter, DialogCallback dialogCallback) {
        if (DialogUtils.isCanShowDialog(this)) {
            BigRoundListDialog bigRoundListDialog = this.mOnActionListDialog;
            if (bigRoundListDialog == null || !bigRoundListDialog.isShowing()) {
                this.mOnActionListDialog = new BigRoundListDialog(this);
                this.mOnActionListDialog.setTitle(str);
                this.mOnActionListDialog.setAdapter(zMListAdapter);
                this.mOnActionListDialog.setDialogCallback(dialogCallback);
                this.mOnActionListDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        SipInCallActivity.this.mHandler.removeCallbacks(SipInCallActivity.this.mGetFocusRunnable);
                        SipInCallActivity.this.mHandler.postDelayed(SipInCallActivity.this.mGetFocusRunnable, 1000);
                    }
                });
                this.mOnActionListDialog.show();
            }
        }
    }

    private void refreshMoreActionDialog() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        ZMListAdapter adapter = this.mOnActionListDialog.getAdapter();
        if (adapter != null) {
            int size = adapter.getList().size();
            ArrayList arrayList = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                IZMListItem item = adapter.getItem(i);
                if ((!(item instanceof MergeSelectCallListItem) || !instance.isInJoinMeeingRequest(((MergeSelectCallListItem) item).getId())) && item != null) {
                    item.init(getApplicationContext());
                    arrayList.add(item);
                }
            }
            if (arrayList.isEmpty()) {
                this.mOnActionListDialog.dismiss();
                return;
            }
            adapter.setList(arrayList);
            adapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void resumeCall(String str) {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            CmmSIPCallManager.getInstance().showErrorDialogImmediately(getString(C4558R.string.zm_title_error), getString(C4558R.string.zm_sip_can_not_unhold_on_phone_call_111899), 0);
            return;
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        instance.resetCurrentCall(str);
        instance.resumeCall(str);
        this.mPanelInCall.setMediaMode();
    }

    /* access modifiers changed from: private */
    public void checkDialog() {
        dismissActionListDialog();
    }

    private void dismissActionListDialog() {
        BigRoundListDialog bigRoundListDialog = this.mOnActionListDialog;
        if (bigRoundListDialog != null && bigRoundListDialog.isShowing()) {
            this.mOnActionListDialog.dismiss();
            this.mOnActionListDialog = null;
        }
    }

    private void onClickCompleteTransfer() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            String relatedCallID = currentCallItem.getRelatedCallID();
            if (!StringUtil.isEmptyOrNull(relatedCallID) && instance.containsInCache(relatedCallID)) {
                String callID = currentCallItem.getCallID();
                if (instance.completeWarmTransfer(callID)) {
                    SipTransferResultActivity.show(this, callID);
                }
            }
        }
    }

    private void onClickCancelTransfer() {
        CmmSIPCallManager.getInstance().cancelWarmTransfer();
    }

    private void onClickBtnOneMore() {
        CmmSIPCallItem currentCallItem = CmmSIPCallManager.getInstance().getCurrentCallItem();
        if (currentCallItem != null) {
            onClickBtnMore(currentCallItem, this.mBtnOneMore);
        }
    }

    private void onClickBtnMultiMore1() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            int sipIdCountInCache = instance.getSipIdCountInCache();
            boolean isTransferring = instance.isTransferring(currentCallItem);
            if (isTransferring || sipIdCountInCache == 2) {
                if (isCurrentCallBindToPanel1()) {
                    onClickBtnMore(currentCallItem, this.mBtnMultiMore1);
                } else {
                    onClickBtnMore(instance.getOtherSIPCallItem(currentCallItem, isTransferring), this.mBtnMultiMore1);
                }
            } else if (sipIdCountInCache > 2) {
                onClickBtnMore(currentCallItem, this.mBtnMultiMore1);
            }
        }
    }

    private void onClickBtnMultiMore2() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            int sipIdCountInCache = instance.getSipIdCountInCache();
            boolean isTransferring = instance.isTransferring(currentCallItem);
            if (isTransferring || sipIdCountInCache == 2) {
                if (isCurrentCallBindToPanel1()) {
                    onClickBtnMore(instance.getOtherSIPCallItem(currentCallItem, isTransferring), this.mBtnMultiMore2);
                } else {
                    onClickBtnMore(currentCallItem, this.mBtnMultiMore2);
                }
            }
        }
    }

    private void onClickBtnMore(CmmSIPCallItem cmmSIPCallItem, View view) {
        if (cmmSIPCallItem != null) {
            this.mBackToFocusView = view;
            if (cmmSIPCallItem.isInConference()) {
                showMergedListDialog(cmmSIPCallItem.getCallID());
            } else {
                showCallerIdDialog(cmmSIPCallItem);
            }
        }
    }

    private void onClickBtnMultiAction1() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            if (!isCurrentCallBindToPanel1()) {
                CmmSIPCallItem otherSIPCallItem = instance.getOtherSIPCallItem(currentCallItem);
                if (otherSIPCallItem != null) {
                    if (instance.isInJoinMeeingRequest(otherSIPCallItem.getCallID())) {
                        onActionJoinMeeting(otherSIPCallItem.getCallID());
                    } else if (otherSIPCallItem.isInConference()) {
                        showMergedListDialog(otherSIPCallItem.getCallID());
                    }
                }
            } else if (instance.isInJoinMeeingRequest(currentCallItem.getCallID())) {
                onActionJoinMeeting(currentCallItem.getCallID());
            } else if (currentCallItem.isInConference()) {
                showMergedListDialog(currentCallItem.getCallID());
            } else {
                selectCallToMerge();
            }
        }
    }

    private void onClickBtnMultiAction2() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null) {
            if (!isCurrentCallBindToPanel1()) {
                String callID = currentCallItem.getCallID();
                if (instance.isInJoinMeeingRequest(callID)) {
                    onActionJoinMeeting(callID);
                } else if (currentCallItem.isInConference()) {
                    showMergedListDialog(callID);
                } else {
                    selectCallToMerge();
                }
            } else {
                CmmSIPCallItem otherSIPCallItem = instance.getOtherSIPCallItem(currentCallItem);
                if (otherSIPCallItem != null) {
                    String callID2 = otherSIPCallItem.getCallID();
                    if (instance.isInJoinMeeingRequest(callID2)) {
                        onActionJoinMeeting(callID2);
                    } else if (otherSIPCallItem.isInConference()) {
                        showMergedListDialog(callID2);
                    }
                }
            }
        }
    }

    private void onClickTxtOneDialState() {
        checkAndShowJoinMeetingUI();
    }

    private void selectCallToMerge() {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            CmmSIPCallManager.getInstance().showErrorDialogImmediately(getString(C4558R.string.zm_title_error), getString(C4558R.string.zm_sip_can_not_merge_call_on_phone_call_111899), 0);
        } else {
            showMergeSelectDialog();
        }
    }

    public void onPause() {
        super.onPause();
        this.mHandler.removeMessages(1);
        HeadsetUtil.getInstance().removeListener(this);
        checkDialog();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        checkAndShowSipFloatWindowNoPermission();
    }

    public void onBackPressed() {
        if (CmmSIPCallManager.getInstance().isCurrentCallLocal()) {
            onClickEndCall();
            return;
        }
        if (checkAndShowSipFloatWindow()) {
            super.onBackPressed();
        }
    }

    public void onResume() {
        super.onResume();
        updateUI();
        stopFloatWindowService();
        HeadsetUtil.getInstance().addListener(this);
        CmmSIPNosManager.getInstance().finishSipIncomePop();
        this.mPanelInCall.setMediaMode();
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        checkProximityScreenOffWakeLock();
    }

    public void checkProximityScreenOffWakeLock() {
        boolean hasWindowFocus = hasWindowFocus();
        boolean isSpeakerOn = CmmSipAudioMgr.getInstance().isSpeakerOn();
        boolean z = HeadsetUtil.getInstance().isBluetoothHeadsetOn() || HeadsetUtil.getInstance().isWiredHeadsetOn();
        if (!hasWindowFocus || isSpeakerOn || z) {
            UIUtil.stopProximityScreenOffWakeLock();
        } else {
            UIUtil.startProximityScreenOffWakeLock(VideoBoxApplication.getGlobalContext());
        }
    }

    private void startBluetoothSco() {
        CmmSipAudioMgr.getInstance().audioStartBluetoothSco();
    }

    private void stopBluetoothSco() {
        CmmSipAudioMgr.getInstance().audioStopBluetoothSco();
    }

    private void startWiredHeadset() {
        CmmSipAudioMgr.getInstance().startWiredHeadset();
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        updatePanelKeybord();
        updatePanelBuddyInfo();
        updatePanelCallBtns();
        updatePanelInCall();
        updateTopPanelTips();
    }

    /* access modifiers changed from: private */
    public void updatePanelBuddyInfo() {
        this.mHandler.removeMessages(1);
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.isCallingout(instance.getCurrentCallID()) || !instance.isMultiCalls()) {
            onOneCall();
        } else {
            onMultiCalls();
        }
    }

    private void onMultiCalls() {
        this.mPanelMultiBuddyList.setVisibility(0);
        this.mPanelOneBuddyInfo.setVisibility(8);
        this.mTxtOneBuddyName.setEllipsize(TruncateAt.END);
        this.mTxtOneDialState.setVisibility(0);
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        String currentCallID = instance.getCurrentCallID();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(currentCallID);
        String buddyName = getBuddyName(callItemByCallID);
        if (callItemByCallID == null) {
            this.mTxtMultiBuddyName1.setText(currentCallID);
            this.mTxtMultiDialState1.setText(C4558R.string.zm_mm_msg_sip_calling_14480);
        } else if (!this.mIsDTMFMode) {
            int sipIdCountInCache = instance.getSipIdCountInCache();
            boolean isTransferring = CmmSIPCallManager.getInstance().isTransferring(callItemByCallID);
            if (isTransferring || sipIdCountInCache == 2) {
                boolean isCurrentCallBindToPanel1 = isCurrentCallBindToPanel1();
                CmmSIPCallItem otherSIPCallItem = instance.getOtherSIPCallItem(callItemByCallID, isTransferring);
                if (isCurrentCallBindToPanel1) {
                    this.mTxtMultiBuddyName1.setSelected(true);
                    this.mTxtMultiDialState1.setSelected(true);
                    this.mTxtMultiBuddyName2.setSelected(false);
                    this.mTxtMultiDialState2.setSelected(false);
                    this.mTxtMultiBuddyName1.setText(buddyName);
                    this.mTxtMultiBuddyName2.setText(getBuddyName(otherSIPCallItem));
                } else {
                    this.mTxtMultiBuddyName1.setSelected(false);
                    this.mTxtMultiDialState1.setSelected(false);
                    this.mTxtMultiBuddyName2.setSelected(true);
                    this.mTxtMultiDialState2.setSelected(true);
                    this.mTxtMultiBuddyName1.setText(getBuddyName(otherSIPCallItem));
                    this.mTxtMultiBuddyName2.setText(buddyName);
                }
                setMultiMoreState(isCurrentCallBindToPanel1, callItemByCallID, otherSIPCallItem);
            } else if (sipIdCountInCache > 2) {
                this.mTxtMultiBuddyName1.setSelected(true);
                this.mTxtMultiDialState1.setSelected(true);
                this.mTxtMultiBuddyName2.setSelected(false);
                this.mTxtMultiDialState2.setSelected(false);
                this.mTxtMultiBuddyName1.setText(buddyName);
                this.mTxtMultiBuddyName2.setText(getString(C4558R.string.zm_sip_phone_calls_on_hold_31368, new Object[]{Integer.valueOf(sipIdCountInCache - 1)}));
                setMultiMoreState(true, callItemByCallID, null);
            }
            this.mHandler.sendEmptyMessage(1);
        } else if (instance.isInCall(callItemByCallID) || instance.isInHold(callItemByCallID) || instance.isAccepted(callItemByCallID)) {
            this.mPanelOneBuddyInfo.setVisibility(0);
            this.mPanelMultiBuddyList.setVisibility(8);
            if (!StringUtil.isEmptyOrNull(this.mDTMFNum)) {
                this.mTxtOneBuddyName.setEllipsize(TruncateAt.START);
                this.mTxtOneDialState.setVisibility(4);
                this.mBtnOneMore.setVisibility(8);
            }
            this.mTxtOneBuddyName.setText(buddyName);
            this.mHandler.sendEmptyMessage(1);
        } else {
            this.mTxtMultiDialState1.setText(C4558R.string.zm_mm_msg_sip_calling_14480);
        }
        updateCallStateText();
    }

    private void setMultiMoreState(boolean z, CmmSIPCallItem cmmSIPCallItem, CmmSIPCallItem cmmSIPCallItem2) {
        ImageView imageView;
        ImageView imageView2;
        if (cmmSIPCallItem != null) {
            this.mBtnMultiMore1.setVisibility(0);
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            int sipIdCountInCache = instance.getSipIdCountInCache();
            boolean isTransferring = instance.isTransferring(cmmSIPCallItem);
            if (sipIdCountInCache == 2 || isTransferring) {
                this.mBtnMultiMore2.setVisibility(0);
            } else {
                this.mBtnMultiMore2.setVisibility(4);
            }
            boolean isInJoinMeeingRequest = instance.isInJoinMeeingRequest(cmmSIPCallItem.getCallID());
            boolean isInJoinMeeingRequest2 = instance.isInJoinMeeingRequest(cmmSIPCallItem2 != null ? cmmSIPCallItem2.getCallID() : "");
            if (!isTransferring || isInJoinMeeingRequest || isInJoinMeeingRequest2) {
                boolean hasConference = instance.hasConference();
                if (z) {
                    imageView = this.mBtnMultiAction1;
                    imageView2 = this.mBtnMultiAction2;
                } else {
                    imageView = this.mBtnMultiAction2;
                    imageView2 = this.mBtnMultiAction1;
                }
                if (isInJoinMeeingRequest) {
                    imageView.setVisibility(0);
                    imageView.setImageResource(C4558R.C4559drawable.zm_sip_btn_join_meeting_request);
                    imageView.setContentDescription(getString(C4558R.string.zm_accessbility_sip_join_meeting_action_53992));
                } else if (hasConference || isTransferring || isInJoinMeeingRequest2 || CmmSIPCallManager.getInstance().isLBREnabled()) {
                    imageView.setVisibility(8);
                } else {
                    imageView.setVisibility(0);
                    imageView.setImageResource(C4558R.C4559drawable.zm_sip_btn_merge_call);
                    imageView.setContentDescription(getString(C4558R.string.zm_accessbility_btn_merge_call_14480));
                }
                if (cmmSIPCallItem2 == null) {
                    imageView2.setVisibility(8);
                } else if (isInJoinMeeingRequest2) {
                    imageView2.setVisibility(0);
                    imageView2.setImageResource(C4558R.C4559drawable.zm_sip_btn_join_meeting_request);
                    imageView2.setContentDescription(getString(C4558R.string.zm_accessbility_sip_join_meeting_action_53992));
                } else {
                    imageView2.setVisibility(8);
                }
                return;
            }
            this.mBtnMultiAction1.setVisibility(8);
            this.mBtnMultiAction2.setVisibility(8);
        }
    }

    private String getCallStateText(CmmSIPCallItem cmmSIPCallItem) {
        String str;
        if (cmmSIPCallItem == null) {
            return "";
        }
        if (this.mIsDTMFMode && !StringUtil.isEmptyOrNull(this.mDTMFNum)) {
            return "";
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.isCallingout(cmmSIPCallItem.getCallID())) {
            return getCallingStateString(cmmSIPCallItem);
        }
        if (instance.isInJoinMeeingRequest(cmmSIPCallItem.getCallID())) {
            return getString(C4558R.string.zm_sip_tap_to_join_meeting_53992);
        }
        long callElapsedTime = instance.getCallElapsedTime(cmmSIPCallItem);
        int thirdpartyType = cmmSIPCallItem.getThirdpartyType();
        int callStatus = cmmSIPCallItem.getCallStatus();
        String currentCallID = instance.getCurrentCallID();
        boolean z = currentCallID != null && currentCallID.equals(cmmSIPCallItem.getCallID());
        if (callStatus == 30 || callStatus == 31) {
            str = getString(C4558R.string.zm_sip_on_remote_hold_53074);
        } else if (z || !instance.isMultiCalls()) {
            if (callStatus == 27 || callStatus == 28 || callStatus == 26) {
                str = TimeUtil.formateDuration(callElapsedTime);
            } else {
                str = getString(C4558R.string.zm_mm_msg_sip_calling_14480);
            }
        } else if (callStatus == 27) {
            str = getString(C4558R.string.zm_sip_call_on_hold_61381);
        } else if (callStatus == 28 || callStatus == 26) {
            str = TimeUtil.formateDuration(callElapsedTime);
        } else {
            str = getString(C4558R.string.zm_mm_msg_sip_calling_14480);
        }
        if (thirdpartyType == 0 && callStatus == 27 && !z && instance.isMultiCalls()) {
            str = getString(C4558R.string.zm_sip_call_on_hold_tap_to_swap_61381, new Object[]{str});
        }
        if (cmmSIPCallItem.isInConference()) {
            return str;
        }
        if (thirdpartyType == 1) {
            str = getString(C4558R.string.zm_sip_call_assistant_82852, new Object[]{str, cmmSIPCallItem.getThirdpartyName()});
        } else if (thirdpartyType == 2 || thirdpartyType == 3 || thirdpartyType == 5) {
            str = getString(C4558R.string.zm_sip_call_queue_82852, new Object[]{str, cmmSIPCallItem.getThirdpartyName()});
        } else if (thirdpartyType == 4) {
            str = getString(C4558R.string.zm_sip_call_transfer_61383, new Object[]{str, cmmSIPCallItem.getThirdpartyName()});
        } else if (thirdpartyType == 6) {
            str = getString(C4558R.string.zm_sip_forward_from_128889, new Object[]{str, cmmSIPCallItem.getThirdpartyName()});
        }
        return str;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:5:0x0020, code lost:
        if (p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1) == false) goto L_0x0022;
     */
    @androidx.annotation.NonNull
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String getCallingStateString(@androidx.annotation.NonNull com.zipow.videobox.sip.server.CmmSIPCallItem r6) {
        /*
            r5 = this;
            java.lang.String r0 = r6.getPeerNumber()
            boolean r1 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r0)
            if (r1 != 0) goto L_0x004e
            java.lang.String r1 = r6.getPeerDisplayName()
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r2 == 0) goto L_0x0022
            com.zipow.videobox.sip.ZMPhoneSearchHelper r1 = com.zipow.videobox.sip.ZMPhoneSearchHelper.getInstance()
            java.lang.String r1 = r1.getDisplayNameByNumber(r0)
            boolean r2 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r1)
            if (r2 != 0) goto L_0x004e
        L_0x0022:
            com.zipow.videobox.ptapp.PTApp r2 = com.zipow.videobox.ptapp.PTApp.getInstance()
            com.zipow.videobox.sip.server.ZMPhoneNumberHelper r2 = r2.getZMPhoneNumberHelper()
            if (r2 == 0) goto L_0x004e
            boolean r1 = r2.isNumberMatched(r0, r1)
            if (r1 != 0) goto L_0x004e
            java.lang.String r6 = r6.getPeerFormatNumber()
            int r1 = p021us.zoom.videomeetings.C4558R.string.zm_mm_msg_sip_calling_number_108017
            r2 = 1
            java.lang.Object[] r2 = new java.lang.Object[r2]
            r3 = 0
            boolean r4 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r6)
            if (r4 == 0) goto L_0x0043
            r6 = r0
        L_0x0043:
            java.lang.String r6 = r6.trim()
            r2[r3] = r6
            java.lang.String r6 = r5.getString(r1, r2)
            return r6
        L_0x004e:
            int r6 = p021us.zoom.videomeetings.C4558R.string.zm_mm_msg_sip_calling_14480
            java.lang.String r6 = r5.getString(r6)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.view.sip.SipInCallActivity.getCallingStateString(com.zipow.videobox.sip.server.CmmSIPCallItem):java.lang.String");
    }

    private Object getCallStateColor(CmmSIPCallItem cmmSIPCallItem) {
        ColorStateList colorStateList = getResources().getColorStateList(C4558R.color.zm_ui_kit_text_color_black_blue);
        if (cmmSIPCallItem == null) {
            return colorStateList;
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        int callStatus = cmmSIPCallItem.getCallStatus();
        boolean z = callStatus == 30 || callStatus == 31;
        if (instance.isMultiCalls()) {
            return z ? Integer.valueOf(getResources().getColor(C4558R.color.zm_ui_kit_color_red_E02828)) : colorStateList;
        }
        return Integer.valueOf(getResources().getColor(z ? C4558R.color.zm_ui_kit_color_red_E02828 : C4558R.color.zm_ui_kit_text_color_black_blue));
    }

    private boolean isCurrentCallBindToPanel1() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        String currentCallID = instance.getCurrentCallID();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(currentCallID);
        boolean z = true;
        if (callItemByCallID == null) {
            return true;
        }
        boolean isTransferring = instance.isTransferring(currentCallID);
        Stack sipCallIds = instance.getSipCallIds();
        if (sipCallIds.size() == 2 || isTransferring) {
            int currentIndexInCallCache = instance.getCurrentIndexInCallCache();
            int size = sipCallIds.size();
            if (isTransferring) {
                String relatedCallID = callItemByCallID.getRelatedCallID();
                int i = 0;
                while (true) {
                    if (i >= size) {
                        i = 0;
                        break;
                    } else if (((String) sipCallIds.get(i)).equals(relatedCallID)) {
                        break;
                    } else {
                        i++;
                    }
                }
                if (currentIndexInCallCache <= i) {
                    z = false;
                }
            } else if (currentIndexInCallCache != 1) {
                z = false;
            }
        }
        return z;
    }

    private void onOneCall() {
        this.mPanelMultiBuddyList.setVisibility(8);
        this.mPanelOneBuddyInfo.setVisibility(0);
        this.mTxtOneBuddyName.setEllipsize(TruncateAt.MARQUEE);
        this.mTxtOneBuddyName.setMarqueeRepeatLimit(-1);
        this.mTxtOneDialState.setVisibility(0);
        CmmSIPCallItem currentCallItem = CmmSIPCallManager.getInstance().getCurrentCallItem();
        if (currentCallItem != null) {
            this.mBtnOneMore.setVisibility(0);
        } else {
            this.mBtnOneMore.setVisibility(8);
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.isInCall(currentCallItem) || instance.isInHold(currentCallItem) || instance.isAccepted(currentCallItem)) {
            this.mTxtOneBuddyName.setVisibility(0);
            if (this.mIsDTMFMode && !StringUtil.isEmptyOrNull(this.mDTMFNum)) {
                this.mTxtOneBuddyName.setEllipsize(TruncateAt.START);
                this.mTxtOneDialState.setVisibility(4);
                this.mBtnOneMore.setVisibility(8);
            }
            this.mTxtOneBuddyName.setText(getBuddyName(currentCallItem));
            this.mHandler.sendEmptyMessage(1);
        } else {
            this.mTxtOneBuddyName.setVisibility(0);
            this.mTxtOneBuddyName.setText(getBuddyName(currentCallItem));
        }
        updateCallStateText();
    }

    private String getBuddyName(CmmSIPCallItem cmmSIPCallItem) {
        if (this.mIsDTMFMode && !StringUtil.isEmptyOrNull(this.mDTMFNum)) {
            return this.mDTMFNum;
        }
        if (cmmSIPCallItem == null) {
            return "";
        }
        return CmmSIPCallManager.getInstance().getSipCallDisplayName(cmmSIPCallItem);
    }

    private void updatePanelCallBtns() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(instance.getCurrentCallID());
        boolean z = (instance.isInCall(callItemByCallID) || instance.isInHold(callItemByCallID) || instance.isAccepted(callItemByCallID)) && instance.isTransferring(callItemByCallID);
        int i = 8;
        this.mBtnEndCall.setVisibility(z ? 8 : 0);
        View view = this.mPanelTransferOption;
        if (z) {
            i = 0;
        }
        view.setVisibility(i);
    }

    private void updatePanelKeybord() {
        if (this.mIsDTMFMode) {
            String currentCallID = CmmSIPCallManager.getInstance().getCurrentCallID();
            String str = this.mDTMFCallId;
            if (str == null || !str.equals(currentCallID)) {
                this.mDTMFNum = "";
            }
            this.mDTMFCallId = currentCallID;
        }
        this.mKeyboardView.setVisibility(this.mIsDTMFMode ? 0 : 4);
        this.mBtnHideKeyboard.setVisibility(this.mKeyboardView.getVisibility());
    }

    /* access modifiers changed from: private */
    public void updatePanelInCall() {
        if (this.mIsDTMFMode) {
            this.mPanelInCall.setVisibility(8);
            return;
        }
        this.mPanelInCall.setVisibility(0);
        this.mPanelInCall.updatePanelInCall();
        if (NetworkUtil.hasDataNetwork(this) && this.mPanelInCall.isPanelHoldEnable()) {
            checkShowHoldTips();
        }
    }

    public void updateTopPanelTips() {
        if (CmmSIPCallManager.getInstance().isInSwitchingToCarrier(CmmSIPCallManager.getInstance().getCurrentCallID())) {
            this.mTopPanelTips.setVisibility(0);
            this.mTxtTips.setText(C4558R.string.zm_pbx_switching_to_carrier_102668);
            return;
        }
        this.mTopPanelTips.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void updateCallStateText() {
        String str;
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(instance.getCurrentCallID());
        if (callItemByCallID != null) {
            if (instance.getCallElapsedTime(callItemByCallID) > 0) {
                String callStateText = getCallStateText(callItemByCallID);
                this.mTxtOneDialState.setText(callStateText);
                updateCallStatColor(this.mTxtOneDialState, callItemByCallID);
                setBuddyPresence(callItemByCallID, this.mOnePresenceStateView, this.mTxtOneDialState);
                int sipIdCountInCache = instance.getSipIdCountInCache();
                boolean isTransferring = instance.isTransferring(callItemByCallID);
                if (sipIdCountInCache == 2 || isTransferring) {
                    if (isTransferring) {
                        str = callItemByCallID.getRelatedCallID();
                    } else {
                        int i = 0;
                        if (instance.getCurrentIndexInCallCache() == 0) {
                            i = 1;
                        }
                        str = (String) instance.getSipCallIds().get(i);
                    }
                    boolean isCurrentCallBindToPanel1 = isCurrentCallBindToPanel1();
                    CmmSIPCallItem callItemByCallID2 = CmmSIPCallManager.getInstance().getCallItemByCallID(str);
                    if (isCurrentCallBindToPanel1) {
                        this.mTxtMultiDialState2.setText(getCallStateText(callItemByCallID2));
                        this.mTxtMultiDialState1.setText(callStateText);
                        updateCallStatColor(this.mTxtMultiDialState2, instance.getCallItemByCallID(str));
                        updateCallStatColor(this.mTxtMultiDialState1, callItemByCallID);
                    } else {
                        this.mTxtMultiDialState1.setText(getCallStateText(callItemByCallID2));
                        this.mTxtMultiDialState2.setText(callStateText);
                        updateCallStatColor(this.mTxtMultiDialState1, callItemByCallID2);
                        updateCallStatColor(this.mTxtMultiDialState2, callItemByCallID);
                    }
                    setMultiBuddyPresence(isCurrentCallBindToPanel1, callItemByCallID, callItemByCallID2);
                } else if (sipIdCountInCache > 2) {
                    this.mTxtMultiDialState1.setText(callStateText);
                    this.mTxtMultiDialState2.setText(C4558R.string.zm_sip_phone_calls_on_hold_to_see_61381);
                    updateCallStatColor(this.mTxtMultiDialState1, callItemByCallID);
                    updateCallStatColor(this.mTxtMultiDialState1, null);
                    setMultiBuddyPresence(true, callItemByCallID, null);
                } else {
                    this.mTxtMultiDialState1.setText("");
                    this.mTxtMultiDialState2.setText("");
                    setBuddyPresenceVisible();
                }
            } else {
                this.mTxtOneDialState.setText("");
                this.mTxtMultiDialState1.setText("");
                this.mTxtMultiDialState2.setText("");
                updateCallStatColor(this.mTxtOneDialState, callItemByCallID);
                setBuddyPresenceVisible();
            }
            View view = this.mPanelMultiCall1;
            StringBuilder sb = new StringBuilder();
            sb.append(DescriptionUtil.getNameContentDescription(this.mTxtMultiBuddyName1));
            sb.append(DescriptionUtil.getTimeContentDescription(this.mTxtMultiDialState1));
            view.setContentDescription(sb.toString());
            View view2 = this.mPanelMultiCall2;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(DescriptionUtil.getNameContentDescription(this.mTxtMultiBuddyName2));
            sb2.append(DescriptionUtil.getTimeContentDescription(this.mTxtMultiDialState2));
            view2.setContentDescription(sb2.toString());
            View view3 = this.mPanelOneBuddyInfo;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(DescriptionUtil.getNameContentDescription(this.mTxtOneBuddyName));
            sb3.append(DescriptionUtil.getTimeContentDescription(this.mTxtOneDialState));
            view3.setContentDescription(sb3.toString());
        }
    }

    private void setMultiBuddyPresence(boolean z, CmmSIPCallItem cmmSIPCallItem, CmmSIPCallItem cmmSIPCallItem2) {
        this.mMultiPresenceStateView1.setVisibility(8);
        this.mMultiPresenceStateView2.setVisibility(8);
        if (z) {
            setBuddyPresence(cmmSIPCallItem, this.mMultiPresenceStateView1, this.mTxtMultiDialState1);
            setBuddyPresence(cmmSIPCallItem2, this.mMultiPresenceStateView2, this.mTxtMultiDialState2);
            return;
        }
        setBuddyPresence(cmmSIPCallItem, this.mMultiPresenceStateView2, this.mTxtMultiDialState2);
        setBuddyPresence(cmmSIPCallItem2, this.mMultiPresenceStateView1, this.mTxtMultiDialState1);
    }

    private void setBuddyPresence(CmmSIPCallItem cmmSIPCallItem, PresenceStateView presenceStateView, TextView textView) {
        if (cmmSIPCallItem == null) {
            presenceStateView.setVisibility(8);
        } else if (cmmSIPCallItem.getThirdpartyType() == 0) {
            presenceStateView.setVisibility(8);
        } else if (!setBuddyPresenceVisible(textView, presenceStateView)) {
            if (cmmSIPCallItem.isInConference()) {
                presenceStateView.setVisibility(8);
                return;
            }
            ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(cmmSIPCallItem.getThirdpartyNumber());
            if (zoomBuddyByNumber == null) {
                presenceStateView.setVisibility(8);
                return;
            }
            IMAddrBookItem fromZoomBuddy = IMAddrBookItem.fromZoomBuddy(zoomBuddyByNumber);
            if (fromZoomBuddy == null) {
                presenceStateView.setVisibility(8);
                return;
            }
            presenceStateView.setState(fromZoomBuddy);
            presenceStateView.setmTxtDeviceTypeGone();
        }
    }

    private boolean setBuddyPresenceVisible(TextView textView, PresenceStateView presenceStateView) {
        if (!TextUtils.isEmpty(textView.getText().toString()) && textView.getVisibility() == 0) {
            return false;
        }
        presenceStateView.setVisibility(8);
        return true;
    }

    private void setBuddyPresenceVisible() {
        setBuddyPresenceVisible(this.mTxtMultiDialState1, this.mMultiPresenceStateView1);
        setBuddyPresenceVisible(this.mTxtMultiDialState2, this.mMultiPresenceStateView2);
        setBuddyPresenceVisible(this.mTxtOneDialState, this.mOnePresenceStateView);
    }

    private void updateCallStatColor(TextView textView, CmmSIPCallItem cmmSIPCallItem) {
        Object callStateColor = getCallStateColor(cmmSIPCallItem);
        if (callStateColor instanceof ColorStateList) {
            textView.setTextColor((ColorStateList) callStateColor);
        } else {
            textView.setTextColor(((Integer) callStateColor).intValue());
        }
    }

    private void onClickTxtHide() {
        this.mIsDTMFMode = false;
        this.mPanelInCall.setDTMFMode(false);
        updateUI();
    }

    private void onClickPanelMute() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        boolean z = !instance.isCallMuted();
        this.mPanelInCall.onClickPanelMute(z);
        instance.muteCall(z);
    }

    private void onClickPanelDtmf() {
        this.mIsDTMFMode = true;
        this.mPanelInCall.setDTMFMode(true);
        updateUI();
    }

    private void onClickPanelSpeakeron() {
        toggleSpeaker();
    }

    public void onClickPanelAddCall() {
        SipDialKeyboardFragment.showAsActivity((ZMActivity) this, 0, 1);
    }

    public void onClickPanelTransfer() {
        String currentCallID = CmmSIPCallManager.getInstance().getCurrentCallID();
        if (!TextUtils.isEmpty(currentCallID)) {
            SipDialKeyboardFragment.showAsActivity(this, 0, 2, currentCallID);
        }
    }

    public void onClickPanelHold() {
        this.mPanelInCall.onClickPanelHold();
    }

    private void onClickRecord() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.isInCall()) {
            boolean isAutoRecordingPoliciesEnable = instance.isAutoRecordingPoliciesEnable(instance.getCurrentCallItem());
            if (instance.isEnableADHocCallRecording() || isAutoRecordingPoliciesEnable) {
                this.mPanelInCall.onClickPanelRecord();
            } else {
                showAdHocRecordDisable();
            }
        }
    }

    private void onClickToMeeting() {
        if (!NetworkUtil.hasDataNetwork(this)) {
            Toast.makeText(this, C4558R.string.zm_sip_error_network_unavailable_99728, 1).show();
            return;
        }
        if (CmmSIPCallManager.getInstance().hasMeetings()) {
            TransferToMeetingDialog.show(this, CmmSIPCallManager.getInstance().getCurrentCallID());
        } else {
            onStartingMeeting();
        }
    }

    private void onStartingMeeting() {
        if (startMeeting()) {
            Dialog dialog = this.mVideoMeetingWaitDialog;
            if (dialog != null && dialog.isShowing()) {
                this.mVideoMeetingWaitDialog.dismiss();
                this.mVideoMeetingWaitDialog = null;
            }
            this.mVideoMeetingWaitDialog = new Builder(this).setMessage(C4558R.string.zm_sip_upgrade_to_meeting_callout_progress_53992).setNegativeButton(C4558R.string.zm_msg_waiting, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    SipInCallActivity.this.mVideoMeetingWaitDialog = null;
                    SipInCallActivity.this.mMeetAction = null;
                }
            }).setCancelable(false).create();
            this.mVideoMeetingWaitDialog.show();
            return;
        }
        Toast.makeText(this, C4558R.string.zm_sip_upgrade_to_meeting_failed_53992, 1).show();
    }

    /* access modifiers changed from: private */
    public boolean startMeeting() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.startMeeting(instance.getCurrentCallID())) {
            return true;
        }
        Toast.makeText(this, C4558R.string.zm_sip_upgrade_to_meeting_failed_53992, 1).show();
        return false;
    }

    /* access modifiers changed from: private */
    public void waitToStartMeeting(String str) {
        this.mMeetAction = new MeetAction(str, 1);
        Dialog dialog = this.mVideoMeetingWaitDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mVideoMeetingWaitDialog.dismiss();
            this.mVideoMeetingWaitDialog = null;
        }
        this.mVideoMeetingWaitDialog = new Builder(this).setMessage(C4558R.string.zm_sip_upgrade_to_meeting_callout_progress_108086).setNegativeButton(C4558R.string.zm_msg_waiting, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SipInCallActivity.this.mVideoMeetingWaitDialog = null;
                SipInCallActivity.this.mMeetAction = null;
            }
        }).setCancelable(false).create();
        this.mVideoMeetingWaitDialog.show();
    }

    /* access modifiers changed from: private */
    public void waitToJoinMeeting(String str) {
        this.mMeetAction = new MeetAction(str, 2);
        Dialog dialog = this.mVideoMeetingWaitDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mVideoMeetingWaitDialog.dismiss();
            this.mVideoMeetingWaitDialog = null;
        }
        this.mVideoMeetingWaitDialog = new Builder(this).setMessage(C4558R.string.zm_sip_upgrade_to_meeting_callout_progress_108086).setNegativeButton(C4558R.string.zm_msg_waiting, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SipInCallActivity.this.mVideoMeetingWaitDialog = null;
                SipInCallActivity.this.mMeetAction = null;
            }
        }).setCancelable(false).create();
        this.mVideoMeetingWaitDialog.show();
    }

    public void onNewMeeting(final String str) {
        if (CmmSIPCallManager.getInstance().hasMeetings()) {
            EndMeetingInPBXDialog.show(this, new ButtonClickListener() {
                public void onNegativeClick() {
                }

                public void onPositiveClick() {
                    SipInCallActivity.this.waitToStartMeeting(str);
                }

                public void onNeutralClick() {
                    SipInCallActivity.this.waitToStartMeeting(str);
                }
            });
        } else {
            onStartingMeeting();
        }
    }

    public void onMergeIntoMeeting(String str) {
        String str2;
        switch (CmmSIPCallManager.getInstance().transferToMeeting(str)) {
            case 1:
                str2 = getString(C4558R.string.zm_sip_merge_into_meeting_fail_get_meeting_info_108093);
                break;
            case 2:
                str2 = getString(C4558R.string.zm_sip_merge_into_meeting_fail_no_meeting_108093);
                break;
            case 3:
                str2 = getString(C4558R.string.zm_sip_merge_into_meeting_fail_108093);
                break;
            default:
                str2 = null;
                break;
        }
        if (!TextUtils.isEmpty(str2)) {
            showSipErrorMessagePanel(str2, 5000, 17, true);
        } else {
            this.mHandler.post(new Runnable() {
                public void run() {
                    SipInCallActivity.this.focusToMeeting();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void focusToMeeting() {
        if (PTApp.getInstance().hasActiveCall()) {
            Intent intent = new Intent(this, IntegrationActivity.class);
            intent.addFlags(268435456);
            intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
            startActivity(intent);
        }
    }

    private void onClickMinimize() {
        if (checkAndShowSipFloatWindow()) {
            finish();
        }
    }

    private void onClickEndCall() {
        stopRing();
        String currentCallID = CmmSIPCallManager.getInstance().getCurrentCallID();
        boolean z = true;
        if (CmmSIPCallManager.getInstance().getSipIdCountInCache() > 1) {
            z = false;
        }
        hangupCall(currentCallID);
        if (z) {
            finish();
        }
    }

    private void hangupCall(String str) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(str);
        if (callItemByCallID != null) {
            if (callItemByCallID.isInConference() && callItemByCallID.getConferenceRole() == 0) {
                int conferenceParticipantsCount = callItemByCallID.getConferenceParticipantsCount();
                for (int i = 0; i < conferenceParticipantsCount; i++) {
                    instance.hangupCall(callItemByCallID.getConferenceParticipantCallItemByIndex(i));
                }
            }
            instance.hangupCall(str);
        }
    }

    public void onKeyDial(String str) {
        if (this.mDTMFNum == null) {
            this.mDTMFNum = "";
        }
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem != null && (instance.isInCall(currentCallItem) || instance.isInHold(currentCallItem) || instance.isAccepted(currentCallItem))) {
            instance.sendDTMF(currentCallItem.getCallID(), str);
            StringBuilder sb = new StringBuilder();
            sb.append(this.mDTMFNum);
            sb.append(str);
            this.mDTMFNum = sb.toString();
            updateUI();
        }
        playTone(str);
    }

    private void playTone(String str) {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) getSystemService("audio");
        }
        int ringerMode = this.mAudioManager.getRingerMode();
        if (ringerMode != 0) {
            int i = 1;
            if (ringerMode != 1 && !StringUtil.isEmptyOrNull(str)) {
                char charAt = str.charAt(0);
                if (charAt == '#') {
                    i = 11;
                } else if (charAt != '*') {
                    switch (charAt) {
                        case '0':
                            i = 0;
                            break;
                        case '1':
                            break;
                        case '2':
                            i = 2;
                            break;
                        case '3':
                            i = 3;
                            break;
                        case '4':
                            i = 4;
                            break;
                        case '5':
                            i = 5;
                            break;
                        case '6':
                            i = 6;
                            break;
                        case '7':
                            i = 7;
                            break;
                        case '8':
                            i = 8;
                            break;
                        case '9':
                            i = 9;
                            break;
                        default:
                            i = 0;
                            break;
                    }
                } else {
                    i = 10;
                }
                try {
                    if (this.mDtmfGenerator == null) {
                        this.mDtmfGenerator = new ToneGenerator(8, 60);
                    }
                    this.mDtmfGenerator.startTone(i, 150);
                    this.mHandler.removeCallbacks(this.mDTMFReleaseRunnable);
                    this.mHandler.postDelayed(this.mDTMFReleaseRunnable, 450);
                } catch (Exception unused) {
                }
            }
        }
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        updatePanelInCall();
        checkProximityScreenOffWakeLock();
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        updatePanelInCall();
    }

    public boolean canSwitchAudioSource() {
        if (!CmmSIPCallManager.getInstance().isInSipAudio()) {
            return false;
        }
        int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
        boolean z = selectedPlayerStreamType == 0 || (selectedPlayerStreamType < 0 && CmmSipAudioMgr.getInstance().isCallOffHook());
        boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(this);
        boolean z2 = HeadsetUtil.getInstance().isBluetoothHeadsetOn() || HeadsetUtil.getInstance().isWiredHeadsetOn();
        if (!z || ((!isFeatureTelephonySupported && !z2) || (CmmSipAudioMgr.getInstance().getMyAudioType() != 0 && !CmmSipAudioMgr.getInstance().isCallOffHook()))) {
            return false;
        }
        return true;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 79) {
            if (keyEvent.getRepeatCount() > 4) {
                onClickEndCall();
                this.mIsLongClick = true;
            } else {
                this.mIsLongClick = false;
            }
            return true;
        }
        this.mIsLongClick = false;
        return super.onKeyDown(i, keyEvent);
    }

    private void stopFloatWindowService() {
        CmmSIPCallManager.getInstance().checkHideSipFloatWindow();
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i == 79) {
            if (!this.mIsLongClick) {
                onClickPanelMute();
            }
            return true;
        }
        if (i == 126 || i == 127) {
            onClickEndCall();
        }
        return super.onKeyUp(i, keyEvent);
    }

    public void onAudioSourceTypeChanged(int i) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.pushLater("Sip.onAudioSourceTypeChanged", new EventAction("Sip.onAudioSourceTypeChanged") {
                public void run(IUIElement iUIElement) {
                    SipInCallActivity.this.updatePanelInCall();
                    SipInCallActivity.this.checkProximityScreenOffWakeLock();
                }
            });
        }
    }

    public void onAction(String str, int i) {
        switch (i) {
            case 1:
                mergeCall(str);
                break;
            case 2:
                CmmSIPCallManager.getInstance().hangupCall(str);
                break;
            case 3:
                resumeCall(str);
                break;
            case 4:
                onActionJoinMeeting(str);
                break;
        }
        checkDialog();
    }

    private boolean checkAndShowSipFloatWindow() {
        return checkAndShowSipFloatWindow(false, true);
    }

    private boolean checkAndShowSipFloatWindowNoPermission() {
        return checkAndShowSipFloatWindow(false, false);
    }

    private boolean checkAndShowSipFloatWindow(boolean z, boolean z2) {
        if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
            if (!OsUtil.isAtLeastM() || Settings.canDrawOverlays(this)) {
                CmmSIPCallManager.getInstance().checkShowSipFloatWindow();
            } else {
                if (z2) {
                    PopupPermissionFragment.showDialog(getSupportFragmentManager(), z);
                }
                return false;
            }
        }
        return true;
    }

    public void onHomeKeyClick() {
        checkAndShowSipFloatWindow(true, true);
    }

    /* access modifiers changed from: private */
    public void onMeetingStartedResult(String str, long j, String str2, boolean z) {
        Dialog dialog = this.mVideoMeetingWaitDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mVideoMeetingWaitDialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void onReceivedJoinMeetingRequest(String str) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        String currentCallID = instance.getCurrentCallID();
        if (!TextUtils.isEmpty(currentCallID)) {
            if (!(instance.getJoinMeetingRequest(currentCallID) != null)) {
                if (instance.getSipIdCountInCache() == 2 || instance.isTransferring(currentCallID)) {
                    updatePanelBuddyInfo();
                } else {
                    BigRoundListDialog bigRoundListDialog = this.mOnActionListDialog;
                    if (bigRoundListDialog != null && bigRoundListDialog.isShowing()) {
                        refreshMoreActionDialog();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void confirmJoinMeeting(String str) {
        if (CmmSIPCallManager.isPhoneCallOffHook()) {
            CmmSIPCallManager.getInstance().showErrorDialogImmediately(getString(C4558R.string.zm_title_error), getString(C4558R.string.zm_sip_can_not_accept_meeting_on_phone_call_111899), 0);
        } else {
            CmmSIPCallManager.getInstance().joinMeeting(str);
        }
    }

    /* access modifiers changed from: private */
    public void onActionJoinMeeting(final String str) {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (instance.isInJoinMeeingRequest(str)) {
            if (!NetworkUtil.hasDataNetwork(this)) {
                Toast.makeText(this, C4558R.string.zm_sip_error_network_unavailable_99728, 1).show();
            } else if (instance.hasMeetings()) {
                EndMeetingInPBXDialog.show(this, new ButtonClickListener() {
                    public void onNegativeClick() {
                    }

                    public void onPositiveClick() {
                        SipInCallActivity.this.waitToJoinMeeting(str);
                    }

                    public void onNeutralClick() {
                        SipInCallActivity.this.waitToJoinMeeting(str);
                    }
                });
            } else {
                confirmJoinMeeting(str);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkAndShowJoinMeetingUI() {
        checkAndShowJoinMeetingUI(CmmSIPCallManager.getInstance().getCurrentCallID());
    }

    private void checkAndShowJoinMeetingUI(String str) {
        if (CmmSIPCallManager.getInstance().isInJoinMeeingRequest(str)) {
            showJoinMeetingUI(str);
        }
    }

    private void showJoinMeetingUI(String str) {
        if (CmmSIPCallManager.getInstance().showJoinMeetingUI(str)) {
            UIUtil.startProximityScreenOffWakeLock(VideoBoxApplication.getGlobalContext());
        }
    }

    private void showSipErrorMessagePanel(String str, int i) {
        showSipErrorMessagePanel(str, 0, i, true);
    }

    /* access modifiers changed from: private */
    public void showSipErrorMessagePanel(String str, long j, int i, boolean z) {
        int i2 = z ? C4409R.color.zm_notification_background : C4558R.color.zm_notification_background_blue;
        UIUtil.renderStatueBar(this, true, i2);
        this.mPanelSipError.setVisibility(0);
        this.mPanelSipError.setBackgroundResource(i2);
        this.mTxtSipError.setTextColor(getResources().getColor(z ? C4558R.color.zm_ui_kit_color_black_232333 : C4558R.color.zm_white));
        this.mTxtSipError.setText(str);
        this.mTxtSipError.setGravity(i);
        if (AccessibilityUtil.isSpokenFeedbackEnabled(this)) {
            AccessibilityUtil.announceForAccessibilityCompat((View) this.mTxtSipError, (CharSequence) str);
        }
        if (j > 0) {
            this.mHandler.removeMessages(2);
            this.mHandler.sendEmptyMessageDelayed(2, j);
        }
    }

    private void hideSipErrorMessagePanel() {
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        this.mPanelSipError.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void checkErrorMessage() {
        this.mHandler.removeMessages(3);
        if (!this.mHandler.hasMessages(2)) {
            if (!NetworkUtil.hasDataNetwork(this)) {
                showSipErrorMessagePanel(getString(C4558R.string.zm_sip_error_network_unavailable_99728), 17);
            } else if (CmmSIPCallManager.getInstance().getServiceRangeState() != 1 || CmmSIPCallManager.getInstance().getOutOfServiceTime() <= 0) {
                if (this.mDeviceInfo != 4) {
                    switch (this.mCallMediaStatus) {
                        case 1001:
                        case 1002:
                        case 1003:
                            showSipErrorMessagePanel(getString(C4558R.string.zm_sip_error_data_99728), GravityCompat.START);
                            break;
                        default:
                            this.mCallMediaStatus = 0;
                            hideSipErrorMessagePanel();
                            break;
                    }
                } else {
                    showSipErrorMessagePanel(getString(C4558R.string.zm_sip_error_device_113584), 17);
                }
            } else {
                int outOfServiceTime = (int) (((CmmSIPCallManager.getInstance().getOutOfServiceTime() + 60000) - System.currentTimeMillis()) / 1000);
                if (outOfServiceTime > 60) {
                    outOfServiceTime = 60;
                }
                if (outOfServiceTime < 0) {
                    outOfServiceTime = 0;
                }
                showSipErrorMessagePanel(getString(C4558R.string.zm_sip_out_of_range_in_call_101964, new Object[]{Integer.valueOf(outOfServiceTime)}), 17);
                this.mHandler.sendEmptyMessageDelayed(3, 1000);
            }
        }
    }

    /* access modifiers changed from: private */
    public void checkErrorMessageDelayed() {
        this.mHandler.removeMessages(3);
        this.mHandler.sendEmptyMessageDelayed(3, 500);
    }

    /* access modifiers changed from: private */
    public void showFirstTimeForSLAHoldPop() {
        View panelHoldView = this.mPanelInCall.getPanelHoldView();
        if (panelHoldView != null) {
            CmmSIPAPI.setFirstTimeForSLAHold();
            SipPopUtils.showFirstTimeForSLAHoldPop(this, panelHoldView, new PopupWindow.OnDismissListener() {
                public void onDismiss() {
                    SipInCallActivity.this.checkShowHoldTips();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void checkShowHoldTips() {
        if (needShowFirstTimeForSLAHoldPop()) {
            CmmSIPAPI.setFirstTimeForSLAHold();
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        SipInCallActivity.this.showFirstTimeForSLAHoldPop();
                    } catch (Exception unused) {
                    }
                }
            }, 500);
        } else if (needShowToggleAudioForUnHoldPop()) {
            CmmSIPAPI.setToggleAudioForUnHoldPromptAsReaded();
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    try {
                        SipInCallActivity.this.showToggleAudioForUnHoldPop();
                    } catch (Exception unused) {
                    }
                }
            }, 500);
        }
    }

    private boolean needShowFirstTimeForSLAHoldPop() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (!CmmSIPAPI.isFirstTimeForSLAHold()) {
            return false;
        }
        CmmSIPCallItem currentCallItem = instance.getCurrentCallItem();
        if (currentCallItem == null) {
            return false;
        }
        String lineId = currentCallItem.getLineId();
        if (TextUtils.isEmpty(lineId)) {
            return false;
        }
        CmmSIPLine lineItemByID = CmmSIPLineManager.getInstance().getLineItemByID(lineId);
        if (lineItemByID == null) {
            return false;
        }
        return lineItemByID.isShared();
    }

    /* access modifiers changed from: private */
    public void showToggleAudioForUnHoldPop() {
        View panelHoldView = this.mPanelInCall.getPanelHoldView();
        if (panelHoldView != null) {
            CmmSIPAPI.setToggleAudioForUnHoldPromptAsReaded();
            SipPopUtils.showToggleAudioForUnHoldPop(this, panelHoldView);
        }
    }

    private boolean needShowToggleAudioForUnHoldPop() {
        if (!CmmSIPAPI.isToggleAudioForUnHoldPromptReaded() && CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
            return CmmSIPCallManager.getInstance().isInLocalHold(CmmSIPCallManager.getInstance().getCurrentCallItem());
        }
        return false;
    }

    public void onPanelItemClick(int i) {
        switch (i) {
            case 0:
                onClickPanelMute();
                return;
            case 1:
                onClickPanelDtmf();
                return;
            case 2:
                onClickPanelSpeakeron();
                return;
            case 3:
                onClickPanelAddCall();
                return;
            case 4:
                onClickPanelHold();
                return;
            case 5:
                onClickPanelTransfer();
                return;
            case 6:
                onClickRecord();
                return;
            case 7:
                onClickToMeeting();
                return;
            case 8:
                onClickMinimize();
                return;
            case 9:
                onClickPanelMore();
                return;
            case 10:
                onClickPanelSwitchToCarrier();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void OnPBXFeatureOptionsChanged(List<CmmPBXFeatureOptionBit> list) {
        if (list != null && list.size() != 0) {
            CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
            if (instance != null) {
                boolean z = false;
                long aDHocCallRecordingBit = instance.getADHocCallRecordingBit();
                if (ZMPhoneUtils.isPBXFeatureOptionChanged(list, aDHocCallRecordingBit)) {
                    CmmPBXFeatureOptionBit pBXFeatureOptionBit = ZMPhoneUtils.getPBXFeatureOptionBit(list, aDHocCallRecordingBit);
                    if (pBXFeatureOptionBit != null && pBXFeatureOptionBit.getAction() == 0) {
                        String currentCallID = instance.getCurrentCallID();
                        if (instance.isInCall() && !instance.isRecordingIdle(currentCallID)) {
                            instance.handleRecording(currentCallID, 1);
                        }
                    }
                    z = true;
                }
                if (ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getHasCallingPlanBit())) {
                    SipMoreActionFragment.dismiss(this);
                    z = true;
                }
                if (z) {
                    updatePanelInCall();
                }
                if (ZMPhoneUtils.isPBXFeatureOptionChanged(list, instance.getLBREnabledBit())) {
                    updatePanelBuddyInfo();
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void OnRequestDoneForUpdatePBXFeatureOptions(boolean z, List<CmmPBXFeatureOptionBit> list) {
        OnPBXFeatureOptionsChanged(list);
    }

    public void showAdHocRecordDisable() {
        if (!CmmSIPCallManager.getInstance().isOldAccount()) {
            CmmSIPCallManager.getInstance().showTipsOnUITop(getString(C4558R.string.zm_pbx_lbl_call_recording_disable_101955));
        }
    }

    private void showSwitchToCarrierRestrictedTips() {
        CmmSIPCallManager.getInstance().showTipsOnUITop(getString(C4558R.string.zm_pbx_switching_to_carrier_disable_102668));
    }

    private void onClickPanelMore() {
        SipMoreActionFragment.show(this, CmmSIPCallManager.getInstance().getCurrentCallID(), new ArrayList(this.mPanelInCall.getMoreActionList()));
    }

    private void onClickPanelSwitchToCarrier() {
        if (!CmmSIPCallManager.getInstance().isEnableHasCallingPlan()) {
            showSwitchToCarrierRestrictedTips();
        } else {
            CallToCarrierFragment.show(this, CmmSIPCallManager.getInstance().getCurrentCallID());
        }
    }

    public void switchToCarrierSendSuccess() {
        updatePanelInCall();
        updateTopPanelTips();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 199 && checkAndShowSipFloatWindowNoPermission()) {
            finish();
        }
    }

    public void finish() {
        super.finish();
    }
}
