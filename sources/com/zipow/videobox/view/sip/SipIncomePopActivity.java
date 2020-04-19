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
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.IZoomMessengerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.sip.CmmSIPCallRegResult;
import com.zipow.videobox.sip.SipRingMgr;
import com.zipow.videobox.sip.ZMPhoneSearchHelper;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPLineManager;
import com.zipow.videobox.sip.server.CmmSIPNosManager;
import com.zipow.videobox.sip.server.CmmSIPNosManager.NosSIPCallPopListener;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.CmmSipAudioMgr.PhoneCallListener;
import com.zipow.videobox.sip.server.ISIPLineMgrEventSinkUI.SimpleISIPLineMgrEventSinkListener;
import com.zipow.videobox.sip.server.NosSIPCallItem;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.NotificationMgr;
import java.io.Serializable;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.videomeetings.C4558R;

public class SipIncomePopActivity extends ZMActivity implements OnClickListener, NosSIPCallPopListener {
    private static final String ACTION_ACCEPT = "ACCEPT";
    private static final String ARG_NEED_INIT_MODULE = "ARG_NEED_INIT_MODULE";
    private static final String ARG_NOS_SIP_CALL_ITEM = "ARG_NOS_SIP_CALL_ITEM";
    private static final int LONG_CLICK_EVENT_REPEATCOUNT = 4;
    private static final int MSG_BUDDY_BIG_PICTURE_UPDATE = 11;
    private static final int MSG_BUDDY_INFO_UPDATE = 10;
    private static final int REQUEST_PERMISSION_ACCEPT_CALL = 111;
    private static final int REQUEST_PERMISSION_END_AND_ACCEPT = 112;
    private static final String TAG = "SipIncomePopActivity";
    /* access modifiers changed from: private */
    public int mAction;
    /* access modifiers changed from: private */
    public boolean mActionDone = false;
    /* access modifiers changed from: private */
    public SipIncomeAvatar mAvatarView;
    private ImageView mBtnAcceptCall;
    private ImageView mBtnEndAcceptCall;
    private ImageView mBtnEndCall;
    /* access modifiers changed from: private */
    public NosSIPCallItem mCallItem;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler() {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 10:
                    SipIncomePopActivity.this.updateUI();
                    return;
                case 11:
                    if (SipIncomePopActivity.this.mCallItem != null) {
                        SipIncomePopActivity.this.mAvatarView.displayAvatar(SipIncomePopActivity.this.mCallItem);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private boolean mIsLongClick = false;
    private SimpleISIPLineMgrEventSinkListener mLineListener = new SimpleISIPLineMgrEventSinkListener() {
        public void OnRegisterResult(String str, CmmSIPCallRegResult cmmSIPCallRegResult) {
            super.OnRegisterResult(str, cmmSIPCallRegResult);
            if (SipIncomePopActivity.this.mCallItem != null) {
                CmmSIPNosManager instance = CmmSIPNosManager.getInstance();
                String sid = SipIncomePopActivity.this.mCallItem.getSid();
                String traceId = SipIncomePopActivity.this.mCallItem.getTraceId();
                StringBuilder sb = new StringBuilder();
                sb.append("SipIncomeActivity.OnRegisterResult(),");
                sb.append(str);
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
                sb.append(cmmSIPCallRegResult.getRegStatus());
                instance.printPushCallLog(0, sid, traceId, sb.toString());
            }
            if (cmmSIPCallRegResult.isRegistered() && !SipIncomePopActivity.this.mActionDone && CmmSIPLineManager.getInstance().isLineMatchesNosSIPCall(str, SipIncomePopActivity.this.mCallItem)) {
                switch (SipIncomePopActivity.this.mAction) {
                    case 1:
                        SipIncomePopActivity.this.onIvCloseClick();
                        break;
                    case 2:
                        SipIncomePopActivity.this.onPanelAcceptCall();
                        break;
                    case 3:
                        SipIncomePopActivity.this.onPanelEndCall();
                        break;
                }
            }
        }
    };
    @Nullable
    private IZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onIndicateInfoUpdatedWithJID(String str) {
            if (!SipIncomePopActivity.this.mHandler.hasMessages(10)) {
                SipIncomePopActivity.this.mHandler.sendEmptyMessageDelayed(10, 1000);
            }
        }

        public void onIndicate_BuddyBigPictureDownloaded(String str, int i) {
            super.onIndicate_BuddyBigPictureDownloaded(str, i);
            if (!SipIncomePopActivity.this.mHandler.hasMessages(11)) {
                SipIncomePopActivity.this.mHandler.sendEmptyMessageDelayed(11, 1000);
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
            SipIncomePopActivity.this.onIvCloseClick();
        }
    };
    private long mResumeTimestamp;
    private TextView mTvBuddyName;
    private TextView mTvStatus;
    private TextView mTxtAcceptCall;
    private TextView mTxtCallingFor;
    private TextView mTxtCallingForNumber;
    private TextView mTxtCallingForTitle;
    private TextView mTxtEndAcceptCall;
    private TextView mTxtEndCall;
    private WaitingDialog mWaitingDialog = null;

    private interface ActionType {
        public static final int ACCEPT = 2;
        public static final int CLOSE = 1;
        public static final int DECLINE = 3;
    }

    public static void show(Context context, NosSIPCallItem nosSIPCallItem) {
        show(context, nosSIPCallItem, false);
    }

    public static void show(Context context, NosSIPCallItem nosSIPCallItem, boolean z) {
        if (nosSIPCallItem != null && context != null) {
            Intent intent = new Intent(context, SipIncomePopActivity.class);
            intent.putExtra("ARG_NOS_SIP_CALL_ITEM", nosSIPCallItem);
            intent.putExtra(ARG_NEED_INIT_MODULE, z);
            intent.addFlags(268435456);
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    public static void showForAcceptCall(Context context, NosSIPCallItem nosSIPCallItem) {
        if (nosSIPCallItem != null && context != null) {
            Intent intent = new Intent(context, SipIncomePopActivity.class);
            intent.setAction(ACTION_ACCEPT);
            intent.putExtra("ARG_NOS_SIP_CALL_ITEM", nosSIPCallItem);
            intent.addFlags(268435456);
            ActivityStartHelper.startActivityForeground(context, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        getWindow().addFlags(6848640);
        super.onCreate(bundle);
        setContentView(C4558R.layout.zm_sip_income_pop);
        this.mTvBuddyName = (TextView) findViewById(C4558R.C4560id.tvBuddyName);
        this.mTvStatus = (TextView) findViewById(C4558R.C4560id.tvStatus);
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
        setFinishOnTouchOutside(false);
        findViewById(C4558R.C4560id.iv_close).setOnClickListener(this);
        this.mPanelEndAcceptCall.setOnClickListener(this);
        this.mPanelAcceptCall.setOnClickListener(this);
        this.mPanelEndCall.setOnClickListener(this);
        this.mPanelCallType = findViewById(C4558R.C4560id.panelCallType);
        this.mTxtCallingFor = (TextView) findViewById(C4558R.C4560id.tvCallingFor);
        this.mTxtCallingForTitle = (TextView) findViewById(C4558R.C4560id.tvCallingForTitle);
        this.mTxtCallingForNumber = (TextView) findViewById(C4558R.C4560id.tvCallingForNumber);
        if (getIntent().getBooleanExtra(ARG_NEED_INIT_MODULE, false)) {
            CmmSIPNosManager.getInstance().prepareSipCall();
        }
        Serializable serializableExtra = getIntent().getSerializableExtra("ARG_NOS_SIP_CALL_ITEM");
        if (!(serializableExtra instanceof NosSIPCallItem)) {
            finish();
            return;
        }
        this.mCallItem = (NosSIPCallItem) serializableExtra;
        if (bundle != null) {
            this.mActionDone = bundle.getBoolean("mActionDone");
        }
        setupWindows();
        updateUI();
        if (ACTION_ACCEPT.equals(getIntent().getAction())) {
            this.mPanelAcceptCall.post(new Runnable() {
                public void run() {
                    SipIncomePopActivity.this.mPanelAcceptCall.performClick();
                }
            });
        }
        CmmSIPLineManager.getInstance().addISIPLineMgrEventSinkUI(this.mLineListener);
        ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
        CmmSIPNosManager.getInstance().addNosSIPCallPopListener(this);
        CmmSipAudioMgr.getInstance().addListener(this.mPhoneCallListener);
        checkNosSIPCallItem();
        SipRingMgr.getInstance().startRing(this);
        if (this.mCallItem != null) {
            long currentTimeMillis = System.currentTimeMillis() - this.mCallItem.getTimestamp();
            StringBuilder sb = new StringBuilder();
            sb.append("SipIncomePopActivity.OnCreate(),pbx:");
            sb.append(this.mCallItem.getTimestamp());
            sb.append(",pbx elapse:");
            sb.append(currentTimeMillis);
            CmmSIPNosManager.getInstance().printPushCallLog(0, this.mCallItem.getSid(), this.mCallItem.getTraceId(), sb.toString(), currentTimeMillis);
        }
    }

    private void checkNosSIPCallItem() {
        if (!CmmSIPNosManager.getInstance().isNosSipCallValid(this.mCallItem)) {
            finish();
        }
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
                        SipIncomePopActivity.this.mPanelAcceptCall.performClick();
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        super.onStart();
        this.mAvatarView.start();
    }

    private void setupWindows() {
        LayoutParams attributes = getWindow().getAttributes();
        attributes.width = (int) (((float) UIUtil.getDisplayWidth(this)) * 0.9f);
        getWindow().setAttributes(attributes);
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

    /* access modifiers changed from: protected */
    public void onDestroy() {
        if (this.mCallItem != null) {
            CmmSIPNosManager.getInstance().printPushCallLog(0, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.onDestroy()");
        }
        this.mHandler.removeCallbacksAndMessages(null);
        CmmSIPNosManager.getInstance().removeNosSIPCallPopListener(this);
        super.onDestroy();
        stopRing();
        CmmSIPNosManager.getInstance().removeSipIncomeNotification();
        CmmSIPNosManager.getInstance().clearNosSIPCallItem();
        CmmSIPNosManager.getInstance().setNosSIPCallRinging(false);
        CmmSIPNosManager.getInstance().clearIncomingCallTimeoutMessage();
        CmmSIPLineManager.getInstance().removeISIPLineMgrEventSinkUI(this.mLineListener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        CmmSipAudioMgr.getInstance().removeListener(this.mPhoneCallListener);
        dismissWaitDialog();
    }

    public void onBackPressed() {
        onPanelEndCall();
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        this.mResumeTimestamp = System.currentTimeMillis();
        checkNosSIPCallItem();
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        if (this.mCallItem != null) {
            if (CmmSipAudioMgr.getInstance().isAudioInMeeting() || CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                onMultiCalls();
            } else {
                onOneCall();
            }
            setBuddyInfo();
            setPanelType();
        }
    }

    private void setBuddyInfo() {
        NosSIPCallItem nosSIPCallItem = this.mCallItem;
        if (nosSIPCallItem != null) {
            String fromName = nosSIPCallItem.getFromName();
            if (TextUtils.isEmpty(fromName) || fromName.equals(this.mCallItem.getFrom())) {
                fromName = ZMPhoneSearchHelper.getInstance().getDisplayNameByNumber(this.mCallItem.getFrom());
                if (TextUtils.isEmpty(fromName)) {
                    fromName = this.mCallItem.getFrom();
                }
            }
            this.mTvBuddyName.setText(fromName);
            this.mTvStatus.setText(this.mCallItem.getFrom());
            TextView textView = this.mTvStatus;
            textView.setContentDescription(TextUtils.isEmpty(textView.getText()) ? "" : StringUtil.digitJoin(this.mTvStatus.getText().toString().split(""), PreferencesConstants.COOKIE_DELIMITER));
            this.mAvatarView.displayAvatar(this.mCallItem);
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
            if (this.mCallItem.isCallQueue()) {
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
        if (this.mCallItem.isCallQueue()) {
            i = C4558R.C4559drawable.zm_sip_skip_call;
            i2 = C4558R.string.zm_sip_btn_skip_call_114844;
        }
        this.mBtnEndCall.setImageResource(i);
        this.mTxtEndCall.setText(i2);
        this.mBtnEndCall.setContentDescription(getString(i2));
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C41827 r2 = new EventAction("SipIncomeActivityPermissionResult") {
            public void run(IUIElement iUIElement) {
                if (iUIElement instanceof SipIncomePopActivity) {
                    ((SipIncomePopActivity) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                }
            }
        };
        getEventTaskManager().pushLater("SipIncomeActivityPermissionResult", r2);
    }

    /* access modifiers changed from: protected */
    public void handleRequestPermissionResult(int i, String[] strArr, int[] iArr) {
        if (this.mCallItem != null) {
            CmmSIPNosManager.getInstance().printPushCallLog(3, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.handleRequestPermissionResult()");
        }
        if (strArr != null && iArr != null) {
            if (i == 111 || i == 112) {
                if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                    onPanelAcceptCall();
                } else {
                    onPanelEndCall();
                }
            }
        }
    }

    private void stopRing() {
        SipRingMgr.getInstance().stopRing();
    }

    private void setPanelType() {
        CharSequence charSequence;
        String str;
        int i;
        NosSIPCallItem nosSIPCallItem = this.mCallItem;
        if (nosSIPCallItem != null) {
            int thirdtype = nosSIPCallItem.getThirdtype();
            String str2 = "";
            if (thirdtype == 1) {
                String string = getString(C4558R.string.zm_sip_sla_for_text_82852);
                str2 = this.mCallItem.getThirdname();
                str = this.mCallItem.getThirdnumber();
                charSequence = string;
                i = 0;
            } else if (thirdtype == 2 || thirdtype == 3 || thirdtype == 5) {
                String string2 = getString(C4558R.string.zm_sip_sla_for_text_82852);
                str2 = this.mCallItem.getThirdname();
                str = this.mCallItem.getThirdnumber();
                charSequence = string2;
                i = 0;
            } else if (thirdtype == 4) {
                String string3 = getString(C4558R.string.zm_sip_sla_transfer_text_82852);
                str2 = this.mCallItem.getThirdname();
                str = this.mCallItem.getThirdnumber();
                charSequence = string3;
                i = 0;
            } else if (thirdtype == 6) {
                String string4 = getString(C4558R.string.zm_sip_forward_from_text_128889);
                str2 = this.mCallItem.getThirdname();
                str = this.mCallItem.getThirdnumber();
                charSequence = string4;
                i = 0;
            } else {
                String string5 = getString(C4558R.string.zm_sip_sla_for_text_82852);
                str = this.mCallItem.getCalledNumber();
                charSequence = string5;
                i = 8;
            }
            this.mTxtCallingFor.setText(str2);
            this.mTxtCallingFor.setVisibility(i);
            this.mTxtCallingForNumber.setText(str);
            if (!TextUtils.isEmpty(str)) {
                this.mTxtCallingForNumber.setVisibility(0);
                this.mTxtCallingForNumber.setContentDescription(StringUtil.digitJoin(str.split(""), PreferencesConstants.COOKIE_DELIMITER));
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
        }
    }

    public void onClick(View view) {
        if (view != null) {
            int id = view.getId();
            if (id == C4558R.C4560id.iv_close) {
                onIvCloseClick();
            } else if (id == C4558R.C4560id.panelAcceptCall) {
                onPanelAcceptCall();
            } else if (id == C4558R.C4560id.panelEndCall) {
                onPanelEndCall();
            } else if (id == C4558R.C4560id.panelEndAcceptCall) {
                onClickEndAcceptCall();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onPanelAcceptCall() {
        if (this.mCallItem != null) {
            CmmSIPNosManager.getInstance().printPushCallLog(3, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.onPanelAcceptCall()");
            this.mAction = 2;
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (!CmmSipAudioMgr.getInstance().isAudioInMeeting() && CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                    CmmSIPCallManager.getInstance().checkHoldCurrentCall();
                }
                if (CmmSIPLineManager.getInstance().isLineRegistered(this.mCallItem)) {
                    CmmSIPNosManager.getInstance().inboundCallPushPickup(this.mCallItem);
                    this.mActionDone = true;
                } else {
                    showWaitDialog();
                }
                disableCallBtns();
                return;
            }
            zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 111);
            CmmSIPNosManager.getInstance().printPushCallLog(3, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.onPanelAcceptCall(), request permission");
        }
    }

    /* access modifiers changed from: private */
    public void onPanelEndCall() {
        if (this.mCallItem != null) {
            CmmSIPNosManager.getInstance().printPushCallLog(4, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.onPanelEndCall()");
        }
        this.mAction = 3;
        onEndCall();
        disableCallBtns();
    }

    private void onEndCall() {
        NotificationMgr.removeSipIncomeNotification(this);
        CmmSIPNosManager.getInstance().releaseInboundCall(this.mCallItem);
        this.mActionDone = true;
        finish();
    }

    private void onClickEndAcceptCall() {
        if (this.mCallItem != null) {
            CmmSIPNosManager.getInstance().printPushCallLog(3, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.onClickEndAcceptCall()");
            this.mAction = 2;
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.RECORD_AUDIO") == 0) {
                if (CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
                    CmmSIPCallManager.getInstance().endMeeting();
                } else if (CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                    CmmSIPCallManager.getInstance().hangupCall();
                }
                if (CmmSIPLineManager.getInstance().isLineRegistered(this.mCallItem)) {
                    CmmSIPNosManager.getInstance().inboundCallPushPickup(this.mCallItem);
                    this.mActionDone = true;
                } else {
                    showWaitDialog();
                }
                disableCallBtns();
                return;
            }
            zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 112);
            CmmSIPNosManager.getInstance().printPushCallLog(3, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.onClickEndAcceptCall(), request permission");
        }
    }

    private void disableCallBtns() {
        this.mBtnEndAcceptCall.setEnabled(false);
        this.mBtnAcceptCall.setEnabled(false);
        this.mBtnEndCall.setEnabled(false);
    }

    /* access modifiers changed from: private */
    public void onIvCloseClick() {
        this.mAction = 1;
        NotificationMgr.removeSipIncomeNotification(this);
        finish();
    }

    private void showWaitDialog() {
        if (this.mCallItem != null) {
            CmmSIPNosManager.getInstance().printPushCallLog(3, this.mCallItem.getSid(), this.mCallItem.getTraceId(), "SipIncomeActivity.showWaitDialog()");
        }
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog == null || !waitingDialog.isVisible()) {
            if (this.mWaitingDialog == null) {
                this.mWaitingDialog = WaitingDialog.newInstance(getString(C4558R.string.zm_msg_waiting));
            }
            this.mWaitingDialog.show(getSupportFragmentManager(), "WaitingDialog");
        }
    }

    private void dismissWaitDialog() {
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null && waitingDialog.isVisible()) {
            this.mWaitingDialog.dismiss();
        }
    }

    public void finish() {
        super.finish();
        overridePendingTransition(0, C4558R.anim.zm_fade_out);
    }

    public void cancel(String str) {
        NosSIPCallItem nosSIPCallItem = this.mCallItem;
        if (nosSIPCallItem != null && nosSIPCallItem.getSid() != null && this.mCallItem.getSid().equals(str)) {
            NotificationMgr.removeSipIncomeNotification(this);
            finish();
        }
    }

    public void forceFinish() {
        finish();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 79 || i == 126 || i == 127) {
            if (keyEvent.getRepeatCount() > 4) {
                onPanelEndCall();
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
            onPanelAcceptCall();
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mActionDone", this.mActionDone);
    }
}
