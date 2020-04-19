package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.sip.SipRingMgr;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.CmmSipAudioMgr.PhoneCallListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.NotificationMgr;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.PresenceStateView;
import com.zipow.videobox.view.SimpleAnimCloseView;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class SipIncomeActivity extends ZMActivity implements OnClickListener {
    private static final String ACTION_ACCEPT = "ACCEPT";
    private static final String ARG_SIP_CALL_ITEM_ID = "callID";
    private static final int LONG_CLICK_EVENT_REPEATCOUNT = 4;
    private static final int MSG_BUDDY_BIG_PICTURE_UPDATE = 11;
    private static final int MSG_BUDDY_INFO_UPDATE = 10;
    private static final int REQUEST_PERMISSION_ACCEPT_CALL = 111;
    private static final int REQUEST_PERMISSION_END_AND_ACCEPT = 112;
    private static final String TAG = "SipIncomeActivity";
    @Nullable
    private IMAddrBookItem mAddrBookItem;
    /* access modifiers changed from: private */
    public SipIncomeAvatar mAvatarView;
    private ImageView mBtnAcceptCall;
    private ImageView mBtnEndAcceptCall;
    private ImageView mBtnEndCall;
    /* access modifiers changed from: private */
    public SimpleAnimCloseView mBtnIgnore;
    @NonNull
    private ISIPCallEventListener mCallEventListener = new SimpleSIPCallEventListener() {
        public void OnCallTerminate(@NonNull String str, int i) {
            super.OnCallTerminate(str, i);
            if (str.equals(SipIncomeActivity.this.mCallId)) {
                SipIncomeActivity.this.finish();
            } else {
                SipIncomeActivity.this.updateUI();
            }
        }

        public void OnCallActionResult(@NonNull String str, int i, boolean z) {
            super.OnCallActionResult(str, i, z);
            if (z && str.equals(SipIncomeActivity.this.mCallId)) {
                switch (i) {
                    case 1:
                    case 2:
                    case 3:
                        SipIncomeActivity.this.finish();
                        return;
                    default:
                        return;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public String mCallId;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 10:
                    SipIncomeActivity.this.updateBuddyInfo();
                    return;
                case 11:
                    if (!TextUtils.isEmpty(SipIncomeActivity.this.mCallId)) {
                        SipIncomeActivity.this.mAvatarView.displayAvatar(SipIncomeActivity.this.mCallId);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mIsLongClick = false;
    @Nullable
    private IZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onIndicateInfoUpdatedWithJID(String str) {
            if (!SipIncomeActivity.this.mHandler.hasMessages(10)) {
                SipIncomeActivity.this.mHandler.sendEmptyMessageDelayed(10, 1000);
            }
        }

        public void onIndicate_BuddyBigPictureDownloaded(String str, int i) {
            super.onIndicate_BuddyBigPictureDownloaded(str, i);
            if (!SipIncomeActivity.this.mHandler.hasMessages(11)) {
                SipIncomeActivity.this.mHandler.sendEmptyMessageDelayed(11, 1000);
            }
        }
    };
    /* access modifiers changed from: private */
    public View mPanelAcceptCall;
    private View mPanelCallType;
    private View mPanelEndAcceptCall;
    private View mPanelEndCall;
    private PhoneCallListener mPhoneCallListener = new PhoneCallListener() {
        public void onPhoneCallIdle() {
        }

        public void onPhoneCallOffHook() {
            SipIncomeActivity.this.onClickedIgnore();
        }
    };
    private PresenceStateView mPresenceStateView;
    private long mResumeTimestamp;
    private TextView mTxtAcceptCall;
    private TextView mTxtBuddyName;
    private TextView mTxtCallingFor;
    private TextView mTxtCallingForNumber;
    private TextView mTxtCallingForTitle;
    private TextView mTxtDialState;
    private TextView mTxtEndAcceptCall;
    private TextView mTxtEndCall;

    public static void show(@NonNull Context context, String str) {
        Intent intent = new Intent(context, SipIncomeActivity.class);
        intent.addFlags(268435456);
        intent.putExtra(ARG_SIP_CALL_ITEM_ID, str);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public static void showForAccept(@NonNull Context context, String str) {
        Intent intent = new Intent(context, SipIncomeActivity.class);
        intent.setAction(ACTION_ACCEPT);
        intent.addFlags(268435456);
        intent.putExtra(ARG_SIP_CALL_ITEM_ID, str);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(6848640);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        setContentView(C4558R.layout.zm_sip_income);
        init();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (ACTION_ACCEPT.equals(intent.getAction())) {
            View view = this.mPanelAcceptCall;
            if (view != null) {
                view.post(new Runnable() {
                    public void run() {
                        SipIncomeActivity.this.mPanelAcceptCall.performClick();
                    }
                });
            }
        }
    }

    private void init() {
        initDatas();
        initViews();
        updateUI();
        checkIgnoreState();
        if (ACTION_ACCEPT.equals(getIntent().getAction())) {
            this.mPanelAcceptCall.post(new Runnable() {
                public void run() {
                    SipIncomeActivity.this.mPanelAcceptCall.performClick();
                }
            });
        }
        CmmSIPCallManager.getInstance().addListener(this.mCallEventListener);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
        CmmSipAudioMgr.getInstance().addListener(this.mPhoneCallListener);
        if (!CmmSIPCallManager.getInstance().isValidIncomingCall(this.mCallId)) {
            finish();
        }
        SipRingMgr.getInstance().startRing(this);
    }

    private void initDatas() {
        this.mCallId = getIntent().getStringExtra(ARG_SIP_CALL_ITEM_ID);
    }

    private void initViews() {
        this.mAvatarView = (SipIncomeAvatar) findViewById(C4558R.C4560id.avatar);
        this.mPanelAcceptCall = findViewById(C4558R.C4560id.panelAcceptCall);
        this.mBtnAcceptCall = (ImageView) findViewById(C4558R.C4560id.btnAcceptCall);
        this.mTxtAcceptCall = (TextView) findViewById(C4558R.C4560id.txtAccpetCall);
        this.mPanelEndCall = findViewById(C4558R.C4560id.panelEndCall);
        this.mBtnEndCall = (ImageView) findViewById(C4558R.C4560id.btnEndCall);
        this.mTxtEndCall = (TextView) findViewById(C4558R.C4560id.txtEndCall);
        this.mPanelEndAcceptCall = findViewById(C4558R.C4560id.panelEndAcceptCall);
        this.mBtnEndAcceptCall = (ImageView) findViewById(C4558R.C4560id.btnEndAcceptCall);
        this.mTxtEndAcceptCall = (TextView) findViewById(C4558R.C4560id.txtEndAcceptCall);
        this.mTxtBuddyName = (TextView) findViewById(C4558R.C4560id.tvBuddyName);
        this.mTxtDialState = (TextView) findViewById(C4558R.C4560id.tvStatus);
        this.mBtnIgnore = (SimpleAnimCloseView) findViewById(C4558R.C4560id.btn_ignore);
        this.mPanelCallType = findViewById(C4558R.C4560id.panelCallType);
        this.mTxtCallingFor = (TextView) findViewById(C4558R.C4560id.tvCallingFor);
        this.mTxtCallingForTitle = (TextView) findViewById(C4558R.C4560id.tvCallingForTitle);
        this.mPresenceStateView = (PresenceStateView) findViewById(C4558R.C4560id.presenceStateView);
        this.mTxtCallingForNumber = (TextView) findViewById(C4558R.C4560id.tvCallingForNumber);
        this.mPanelEndAcceptCall.setOnClickListener(this);
        this.mPanelEndCall.setOnClickListener(this);
        this.mPanelAcceptCall.setOnClickListener(this);
        this.mBtnIgnore.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.panelEndAcceptCall) {
            onClickEndAcceptCall();
        } else if (id == C4558R.C4560id.panelAcceptCall) {
            onClickAcceptCall();
        } else if (id == C4558R.C4560id.panelEndCall) {
            onClickedEndCall();
        } else if (id == C4558R.C4560id.btn_ignore) {
            onClickedIgnore();
        }
    }

    private void onClickAcceptCall() {
        if (isInRinging()) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (CmmSipAudioMgr.getInstance().isAudioInMeeting() || !CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                    CmmSIPCallManager.getInstance().acceptCall(this.mCallId);
                } else {
                    CmmSIPCallManager.getInstance().acceptAndHoldCall(this.mCallId);
                }
                disableCallBtns();
            } else {
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 111);
            }
        }
    }

    private void onClickedEndCall() {
        if (isInRinging()) {
            if (CmmSIPCallManager.getInstance().isCallQueue(this.mCallId)) {
                CmmSIPCallManager.getInstance().skipInCQ(this.mCallId);
            } else {
                CmmSIPCallManager.getInstance().declineCallWithBusy(this.mCallId);
            }
            disableCallBtns();
        }
    }

    private void disableCallBtns() {
        this.mBtnEndAcceptCall.setEnabled(false);
        this.mBtnAcceptCall.setEnabled(false);
        this.mBtnEndCall.setEnabled(false);
    }

    private void onClickEndAcceptCall() {
        if (isInRinging()) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
                    CmmSIPCallManager.getInstance().endMeeting();
                    CmmSIPCallManager.getInstance().acceptCall(this.mCallId);
                } else if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                    CmmSIPCallManager.getInstance().acceptAndEndCall(this.mCallId);
                } else {
                    CmmSIPCallManager.getInstance().acceptCall(this.mCallId);
                }
                disableCallBtns();
            } else {
                zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 112);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickedIgnore() {
        onBackPressed();
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(this.mCallId);
        if (callItemByCallID == null) {
            finish();
        }
        String currentCallID = CmmSIPCallManager.getInstance().getCurrentCallID();
        if (CmmSipAudioMgr.getInstance().isAudioInMeeting() || (!TextUtils.isEmpty(currentCallID) && !this.mCallId.equals(currentCallID))) {
            onMultiCalls();
        } else {
            onOneCall();
        }
        setBuddyInfo(callItemByCallID);
        setPanelType(callItemByCallID);
        this.mBtnIgnore.setText(C4558R.string.zm_sip_sla_btn_ignore_82852);
        this.mBtnIgnore.setContentDescription(getResources().getString(C4558R.string.zm_sip_accessibility_btn_ignore_82852));
        this.mBtnIgnore.setVisibility(CmmSIPCallManager.getInstance().isCloudPBXEnabled() ? 0 : 8);
    }

    /* access modifiers changed from: private */
    public void updateBuddyInfo() {
        CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(this.mCallId);
        if (callItemByCallID != null) {
            setBuddyInfo(callItemByCallID);
        }
    }

    private void onMultiCalls() {
        this.mPanelEndAcceptCall.setVisibility(0);
        this.mBtnAcceptCall.setImageResource(C4558R.C4559drawable.zm_sip_hold_accept);
        if (CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
            this.mTxtAcceptCall.setText(C4558R.string.zm_sip_hold_meeting_accept_108086);
            this.mBtnAcceptCall.setContentDescription(getString(C4558R.string.zm_sip_hold_meeting_accept_108086));
            this.mTxtEndAcceptCall.setText(C4558R.string.zm_sip_end_meeting_accept_108086);
            this.mBtnEndAcceptCall.setImageResource(C4558R.C4559drawable.zm_sip_end_meeting_accept);
            this.mBtnEndAcceptCall.setContentDescription(getString(C4558R.string.zm_sip_end_meeting_accept_108086));
        } else {
            this.mTxtAcceptCall.setText(C4558R.string.zm_sip_hold_accept_61381);
            this.mBtnAcceptCall.setContentDescription(getString(C4558R.string.zm_sip_hold_accept_61381));
            this.mTxtEndAcceptCall.setText(C4558R.string.zm_sip_end_accept_61381);
            this.mBtnEndAcceptCall.setImageResource(C4558R.C4559drawable.zm_sip_end_accept);
            this.mBtnEndAcceptCall.setContentDescription(getString(C4558R.string.zm_sip_end_accept_61381));
        }
        if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            int i = C4558R.C4559drawable.zm_sip_send_voicemail;
            int i2 = C4558R.string.zm_sip_btn_send_voicemail_31368;
            if (CmmSIPCallManager.getInstance().isCallQueue(this.mCallId)) {
                i = C4558R.C4559drawable.zm_sip_skip_call;
                i2 = C4558R.string.zm_sip_btn_skip_call_114844;
            }
            this.mBtnEndCall.setImageResource(i);
            this.mTxtEndCall.setText(i2);
            this.mBtnEndCall.setContentDescription(getString(i2));
            return;
        }
        this.mBtnEndCall.setImageResource(C4558R.C4559drawable.zm_sip_end_call);
        this.mTxtEndCall.setText(C4558R.string.zm_btn_decline);
        this.mBtnEndCall.setContentDescription(getString(C4558R.string.zm_btn_decline));
    }

    private void onOneCall() {
        this.mPanelEndAcceptCall.setVisibility(8);
        this.mBtnAcceptCall.setImageResource(C4558R.C4559drawable.zm_sip_start_call_nodisable);
        this.mTxtAcceptCall.setText(C4558R.string.zm_btn_accept_sip_61381);
        this.mBtnAcceptCall.setContentDescription(getString(C4558R.string.zm_btn_accept_sip_61381));
        int i = C4558R.C4559drawable.zm_sip_end_call;
        int i2 = C4558R.string.zm_sip_btn_decline_61431;
        if (CmmSIPCallManager.getInstance().isCallQueue(this.mCallId)) {
            i = C4558R.C4559drawable.zm_sip_skip_call;
            i2 = C4558R.string.zm_sip_btn_skip_call_114844;
        }
        this.mBtnEndCall.setImageResource(i);
        this.mTxtEndCall.setText(i2);
        this.mBtnEndCall.setContentDescription(getString(i2));
    }

    private void setBuddyInfo(@Nullable CmmSIPCallItem cmmSIPCallItem) {
        if (cmmSIPCallItem != null) {
            this.mTxtBuddyName.setText(CmmSIPCallManager.getInstance().getDisplayName(cmmSIPCallItem));
            this.mTxtDialState.setText(cmmSIPCallItem.getPeerFormatNumber());
            TextView textView = this.mTxtDialState;
            textView.setContentDescription(TextUtils.isEmpty(textView.getText()) ? "" : StringUtil.digitJoin(this.mTxtDialState.getText().toString().split(""), PreferencesConstants.COOKIE_DELIMITER));
            SipIncomeAvatar sipIncomeAvatar = this.mAvatarView;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mTxtBuddyName.getText().toString());
            sb.append(getString(C4558R.string.zm_sip_income_status_text_26673));
            sipIncomeAvatar.setContentDescription(sb.toString());
            this.mAvatarView.displayAvatar(cmmSIPCallItem.getCallID());
        }
    }

    private void setPanelType(@Nullable CmmSIPCallItem cmmSIPCallItem) {
        CharSequence charSequence;
        int i;
        if (!CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            this.mPanelCallType.setVisibility(8);
            return;
        }
        if (cmmSIPCallItem != null) {
            int thirdpartyType = cmmSIPCallItem.getThirdpartyType();
            String thirdpartyName = cmmSIPCallItem.getThirdpartyName();
            String calledNumber = cmmSIPCallItem.getCalledNumber();
            if (TextUtils.isEmpty(calledNumber)) {
                calledNumber = cmmSIPCallItem.getThirdpartyNumber();
            }
            if (thirdpartyType == 1) {
                charSequence = getString(C4558R.string.zm_sip_sla_for_text_82852);
                i = 0;
            } else if (thirdpartyType == 2 || thirdpartyType == 3 || thirdpartyType == 5) {
                charSequence = getString(C4558R.string.zm_sip_sla_for_text_82852);
                i = 0;
            } else if (thirdpartyType == 4) {
                charSequence = getString(C4558R.string.zm_sip_sla_transfer_text_82852);
                i = 0;
            } else if (thirdpartyType == 6) {
                charSequence = getString(C4558R.string.zm_sip_forward_from_text_128889);
                i = 0;
            } else {
                String string = getString(C4558R.string.zm_sip_sla_for_text_82852);
                calledNumber = cmmSIPCallItem.getCalledNumber();
                charSequence = string;
                i = 8;
            }
            this.mTxtCallingFor.setText(thirdpartyName);
            this.mTxtCallingFor.setVisibility(i);
            this.mPresenceStateView.setVisibility(i);
            this.mTxtCallingForNumber.setText(ZMPhoneUtils.formatPhoneNumber(calledNumber));
            if (!TextUtils.isEmpty(calledNumber)) {
                this.mTxtCallingForNumber.setVisibility(0);
                this.mTxtCallingForNumber.setContentDescription(StringUtil.digitJoin(calledNumber.split(""), PreferencesConstants.COOKIE_DELIMITER));
            } else {
                this.mTxtCallingForNumber.setVisibility(8);
                this.mTxtCallingForNumber.setContentDescription("");
            }
            this.mTxtCallingForTitle.setText(charSequence);
            if (this.mTxtCallingFor.getVisibility() == 8 && this.mTxtCallingForNumber.getVisibility() == 8) {
                this.mTxtCallingForTitle.setVisibility(8);
            } else {
                this.mTxtCallingForTitle.setVisibility(0);
            }
            if (i == 0) {
                if (this.mAddrBookItem == null) {
                    ZoomBuddy zoomBuddyByNumber = ZMPhoneSearchHelper.getInstance().getZoomBuddyByNumber(cmmSIPCallItem.getThirdpartyNumber());
                    if (zoomBuddyByNumber != null) {
                        this.mAddrBookItem = IMAddrBookItem.fromZoomBuddy(zoomBuddyByNumber);
                    }
                }
                IMAddrBookItem iMAddrBookItem = this.mAddrBookItem;
                if (iMAddrBookItem != null) {
                    this.mPresenceStateView.setState(iMAddrBookItem);
                    this.mPresenceStateView.setmTxtDeviceTypeGone();
                } else {
                    this.mPresenceStateView.setVisibility(8);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mResumeTimestamp = System.currentTimeMillis();
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        NotificationMgr.removeSipIncomeNotification(this);
        stopRing();
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
        CmmSIPCallManager.getInstance().removeListener(this.mCallEventListener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        CmmSipAudioMgr.getInstance().removeListener(this.mPhoneCallListener);
    }

    private boolean isInRinging() {
        CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(this.mCallId);
        boolean z = false;
        if (callItemByCallID == null) {
            return false;
        }
        if (callItemByCallID.getCallStatus() == 15) {
            z = true;
        }
        return z;
    }

    private void stopRing() {
        SipRingMgr.getInstance().stopRing();
    }

    private void checkIgnoreState() {
        if (ZMPhoneUtils.readBooleanValue(PreferenceUtil.PBX_FIRST_IGNORE, true)) {
            this.mBtnIgnore.initOpen();
            ZMPhoneUtils.saveBooleanValue(PreferenceUtil.PBX_FIRST_IGNORE, false);
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    if (SipIncomeActivity.this.mBtnIgnore != null) {
                        SipIncomeActivity.this.mBtnIgnore.startToClose();
                    }
                }
            }, 5000);
            return;
        }
        this.mBtnIgnore.initClose();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mAvatarView.start();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        super.onStop();
        this.mAvatarView.stop();
        checkToStopRing();
    }

    private void checkToStopRing() {
        SipRingMgr.getInstance().checkStopRingWhenActivityStopped(this.mResumeTimestamp);
    }

    public void onBackPressed() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        if (CmmSIPCallManager.getInstance().isCallQueue(this.mCallId)) {
            CmmSIPCallManager.getInstance().skipInCQ(this.mCallId);
        } else if (instance.isCloudPBXEnabled()) {
            instance.dismissCall(this.mCallId);
        } else {
            CmmSIPCallManager.getInstance().declineCallWithNotAvailable(this.mCallId);
        }
        super.onBackPressed();
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (getEventTaskManager() != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C41728 r2 = new EventAction("SipIncomeActivityPermissionResult") {
                public void run(IUIElement iUIElement) {
                    if (iUIElement instanceof SipIncomeActivity) {
                        ((SipIncomeActivity) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                    }
                }
            };
            getEventTaskManager().pushLater("SipIncomeActivityPermissionResult", r2);
        }
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, @Nullable String[] strArr, @Nullable int[] iArr) {
        if (strArr != null && iArr != null) {
            switch (i) {
                case 111:
                    onClickAcceptCall();
                    break;
                case 112:
                    onClickEndAcceptCall();
                    break;
            }
        }
    }

    public boolean onKeyDown(int i, @NonNull KeyEvent keyEvent) {
        if (i == 79 || i == 126 || i == 127) {
            if (keyEvent.getRepeatCount() > 4) {
                onClickedEndCall();
                this.mIsLongClick = true;
            } else {
                this.mIsLongClick = false;
            }
            return true;
        }
        this.mIsLongClick = false;
        return super.onKeyDown(i, keyEvent);
    }

    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        if (i != 79 && i != 126 && i != 127) {
            return super.onKeyUp(i, keyEvent);
        }
        if (!this.mIsLongClick) {
            onClickAcceptCall();
        }
        return true;
    }
}
