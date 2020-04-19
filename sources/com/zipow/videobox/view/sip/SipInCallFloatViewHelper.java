package com.zipow.videobox.view.sip;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.eventbus.ZMReturnToCallEvent;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IConfInvitationListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSipAudioMgr;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.ISIPCallEventListener;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPhoneUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.videomeetings.C4558R;

public class SipInCallFloatViewHelper {
    private static final int MSG_RECEIVE_CONF_INVITATION = 3;
    private static final int MSG_REFRESH_CALL_TIME = 1;
    private static final int MSG_UPDATE_ACCESSIBILITY_CALL_TIME = 2;
    private static final long REFRESH_TIME_DELAY = 1000;
    private static final String TAG = "SipInCallFloatViewHelper";
    /* access modifiers changed from: private */
    public int STATUSBAR_HEIGHT = -1;
    /* access modifiers changed from: private */
    public int WIDTH;
    /* access modifiers changed from: private */
    public boolean isPerformClick = false;
    private boolean isShowing = false;
    private IConfInvitationListener mConfInvitationListener = new IConfInvitationListener() {
        public void onCallAccepted(InvitationItem invitationItem) {
        }

        public void onCallDeclined(InvitationItem invitationItem) {
        }

        public void onConfInvitation(InvitationItem invitationItem) {
            SipInCallFloatViewHelper.this.mHandler.sendEmptyMessageDelayed(3, 1000);
        }
    };
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler() {
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    SipInCallFloatViewHelper.this.mHandler.removeMessages(1);
                    SipInCallFloatViewHelper.this.setTimeStateText();
                    SipInCallFloatViewHelper.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                    return;
                case 2:
                    SipInCallFloatViewHelper.this.mHandler.removeMessages(2);
                    SipInCallFloatViewHelper.this.setUpdateAccessibilityCallTime();
                    SipInCallFloatViewHelper.this.mHandler.sendEmptyMessageDelayed(2, 10000);
                    return;
                case 3:
                    SipInCallFloatViewHelper.this.updateUI();
                    return;
                default:
                    return;
            }
        }
    };
    ImageView mIvInBackgoundState;
    ImageView mIvMeetingNoAudio;
    ImageView mIvUIState;
    private SimpleZoomMessengerUIListener mMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onReceivedCall(String str, String str2, InvitationItem invitationItem) {
            super.onReceivedCall(str, str2, invitationItem);
            SipInCallFloatViewHelper.this.mHandler.sendEmptyMessageDelayed(3, 1000);
        }
    };
    @NonNull
    private ISIPCallEventListener mOnSipCallEventListener = new SimpleSIPCallEventListener() {
        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            SipInCallFloatViewHelper.this.updateUI();
        }

        public void OnCallStatusUpdate(String str, int i) {
            if (i == 27 || i == 30 || i == 31 || i == 28 || i == 26) {
                SipInCallFloatViewHelper.this.mHandler.sendEmptyMessageDelayed(1, 1000);
                if (i == 28) {
                    SipInCallFloatViewHelper.this.mHandler.sendEmptyMessageDelayed(2, 1000);
                }
            }
        }

        public void OnMeetingAudioSessionStatus(boolean z) {
            super.OnMeetingAudioSessionStatus(z);
            SipInCallFloatViewHelper.this.updateUI();
        }
    };
    View mToucherLayout;
    TextView mTvTimeState;
    @Nullable
    WindowManager mWindowManager;
    LayoutParams params;

    /* access modifiers changed from: private */
    public boolean isShowing() {
        return this.isShowing;
    }

    private boolean isViewCreated() {
        return this.mToucherLayout != null;
    }

    /* access modifiers changed from: private */
    public void showImpl() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            this.WIDTH = instance.getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_float_window_width);
            this.STATUSBAR_HEIGHT = UIUtil.getStatusBarHeight(instance);
            createToucher();
            if (isShowing()) {
                CmmSIPCallManager.getInstance().addListener(this.mOnSipCallEventListener);
                ZoomMessengerUI.getInstance().addListener(this.mMessengerUIListener);
                PTUI.getInstance().addConfInvitationListener(this.mConfInvitationListener);
                EventBus.getDefault().register(this);
            }
        }
    }

    public void show() {
        if (isShowing()) {
            if (isViewCreated()) {
                updateUI();
            }
            return;
        }
        this.isShowing = true;
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (SipInCallFloatViewHelper.this.isShowing()) {
                    SipInCallFloatViewHelper.this.showImpl();
                }
            }
        }, 500);
    }

    /* access modifiers changed from: private */
    public void onClickToucher() {
        if (!this.isPerformClick) {
            this.isPerformClick = true;
            boolean isAudioInMeeting = CmmSipAudioMgr.getInstance().isAudioInMeeting();
            boolean isInSipInCallUI = isInSipInCallUI();
            boolean isInMeetingUI = isInMeetingUI();
            boolean hasMeetings = CmmSIPCallManager.getInstance().hasMeetings();
            boolean hasSipCallsInCache = CmmSIPCallManager.getInstance().hasSipCallsInCache();
            if (!hasSipCallsInCache || !hasMeetings) {
                if (hasSipCallsInCache) {
                    backToSip();
                } else if (hasMeetings) {
                    backToMeeting();
                }
            } else if (isInMeetingUI) {
                backToSip();
            } else if (isInSipInCallUI) {
                backToMeeting();
            } else if (isAudioInMeeting) {
                backToMeeting();
            } else {
                backToSip();
            }
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SipInCallFloatViewHelper.this.isPerformClick = false;
                    SipInCallFloatViewHelper.this.updateUI();
                }
            }, 2000);
        }
    }

    private void backToSip() {
        SipInCallActivity.returnToSip(VideoBoxApplication.getInstance());
    }

    private void backToMeeting() {
        if (PTApp.getInstance().hasActiveCall()) {
            Intent intent = new Intent(VideoBoxApplication.getNonNullInstance(), IntegrationActivity.class);
            intent.addFlags(268435456);
            intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
            VideoBoxApplication.getNonNullInstance().startActivity(intent);
        }
    }

    private void createToucher() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            this.params = new LayoutParams();
            this.mWindowManager = null;
            if (!OsUtil.isAtLeastM() || Settings.canDrawOverlays(instance)) {
                this.mWindowManager = (WindowManager) instance.getSystemService("window");
                this.params.type = CompatUtils.getSystemAlertWindowType(2003);
            } else {
                ZMActivity activity = ZMActivity.getActivity(IMActivity.class.getName());
                if (activity != null) {
                    this.mWindowManager = (WindowManager) activity.getSystemService("window");
                    this.params.type = CompatUtils.getSystemAlertWindowType(2);
                } else {
                    hide();
                    return;
                }
            }
            if (this.mWindowManager != null) {
                LayoutParams layoutParams = this.params;
                layoutParams.format = 1;
                layoutParams.flags = 8;
                layoutParams.gravity = 51;
                int[] locationInWindow = getLocationInWindow();
                if (locationInWindow.length == 2) {
                    LayoutParams layoutParams2 = this.params;
                    layoutParams2.x = locationInWindow[0];
                    layoutParams2.y = locationInWindow[1];
                } else {
                    LayoutParams layoutParams3 = this.params;
                    layoutParams3.x = 0;
                    layoutParams3.y = 0;
                }
                LayoutParams layoutParams4 = this.params;
                layoutParams4.width = this.WIDTH;
                layoutParams4.height = -2;
                this.mToucherLayout = LayoutInflater.from(instance).inflate(C4558R.layout.zm_sip_float_window, null);
                this.mWindowManager.addView(this.mToucherLayout, this.params);
                this.mToucherLayout.measure(0, 0);
                this.mToucherLayout.setOnTouchListener(new OnTouchListener() {
                    private boolean consumed;
                    private long endTime = 0;
                    int lastX;
                    int lastY;
                    private long startTime = 0;
                    private int viewHeight;

                    public boolean onTouch(View view, @NonNull MotionEvent motionEvent) {
                        if (SipInCallFloatViewHelper.this.mToucherLayout == null) {
                            return false;
                        }
                        this.viewHeight = SipInCallFloatViewHelper.this.mToucherLayout.getHeight();
                        if (motionEvent.getAction() == 0) {
                            this.consumed = false;
                            this.startTime = System.currentTimeMillis();
                            this.lastX = (int) motionEvent.getRawX();
                            this.lastY = (int) motionEvent.getRawY();
                        } else if (motionEvent.getAction() == 2) {
                            this.consumed = true;
                            if (Math.abs(((float) this.lastX) - motionEvent.getRawX()) < 10.0f && Math.abs(((float) this.lastY) - motionEvent.getRawY()) < 10.0f) {
                                return this.consumed;
                            }
                            this.lastX = (int) motionEvent.getRawX();
                            this.lastY = (int) motionEvent.getRawY();
                            SipInCallFloatViewHelper.this.params.x = ((int) motionEvent.getRawX()) - (SipInCallFloatViewHelper.this.WIDTH / 2);
                            SipInCallFloatViewHelper.this.params.y = (((int) motionEvent.getRawY()) - (this.viewHeight / 2)) - SipInCallFloatViewHelper.this.STATUSBAR_HEIGHT;
                            SipInCallFloatViewHelper.this.mWindowManager.updateViewLayout(SipInCallFloatViewHelper.this.mToucherLayout, SipInCallFloatViewHelper.this.params);
                        } else if (motionEvent.getAction() == 1) {
                            this.endTime = System.currentTimeMillis();
                            if (((double) (this.endTime - this.startTime)) > 100.0d) {
                                this.consumed = true;
                            } else {
                                this.consumed = false;
                            }
                            SipInCallFloatViewHelper sipInCallFloatViewHelper = SipInCallFloatViewHelper.this;
                            sipInCallFloatViewHelper.saveLocationInWindow(sipInCallFloatViewHelper.params.x, SipInCallFloatViewHelper.this.params.y);
                            if (!this.consumed) {
                                SipInCallFloatViewHelper.this.onClickToucher();
                            }
                        }
                        return this.consumed;
                    }
                });
                this.mIvUIState = (ImageView) this.mToucherLayout.findViewById(C4558R.C4560id.ivUIState);
                this.mTvTimeState = (TextView) this.mToucherLayout.findViewById(C4558R.C4560id.time);
                this.mIvInBackgoundState = (ImageView) this.mToucherLayout.findViewById(C4558R.C4560id.ivBackgroundState);
                this.mIvMeetingNoAudio = (ImageView) this.mToucherLayout.findViewById(C4558R.C4560id.ivMeetingNoAudio);
                setTextMarquee(this.mTvTimeState);
                updateUI();
            }
        }
    }

    public void setTextMarquee(@Nullable TextView textView) {
        if (textView != null) {
            textView.setEllipsize(TruncateAt.MARQUEE);
            textView.setSingleLine(true);
            textView.setSelected(true);
            textView.setFocusable(true);
            textView.setFocusableInTouchMode(true);
        }
    }

    /* access modifiers changed from: private */
    public void setTimeStateText() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            if (CmmSIPCallManager.getInstance().hasMeetings()) {
                if (CmmSipAudioMgr.getInstance().isAudioInMeeting()) {
                    if (!CmmSIPCallManager.getInstance().hasSipCallsInCache() || !isInMeetingUI()) {
                        this.mTvTimeState.setText(C4558R.string.zm_sip_inmeeting_108086);
                        setContentDescription(instance.getString(C4558R.string.zm_sip_minimized_call_window_description_92481, new Object[]{this.mTvTimeState.getText().toString()}));
                        return;
                    }
                } else if (isInSipInCallUI()) {
                    this.mTvTimeState.setText(C4558R.string.zm_sip_inmeeting_108086);
                    setContentDescription(instance.getString(C4558R.string.zm_sip_minimized_call_window_description_92481, new Object[]{this.mTvTimeState.getText().toString()}));
                    return;
                }
            }
            CmmSIPCallManager instance2 = CmmSIPCallManager.getInstance();
            int sipIdCountInCache = instance2.getSipIdCountInCache();
            if (sipIdCountInCache > 1) {
                this.mTvTimeState.setText(instance.getString(C4558R.string.zm_sip_count_calls_85332, new Object[]{Integer.valueOf(sipIdCountInCache)}));
                setContentDescription(instance.getString(C4558R.string.zm_sip_minimized_call_window_description_92481, new Object[]{this.mTvTimeState.getText().toString()}));
                return;
            }
            CmmSIPCallItem currentCallItem = instance2.getCurrentCallItem();
            if (instance2.isInLocalHold(currentCallItem)) {
                this.mTvTimeState.setText(C4558R.string.zm_sip_call_on_hold_61381);
            } else if (instance2.isInBothHold(currentCallItem) || instance2.isInRemoteHold(currentCallItem)) {
                this.mTvTimeState.setText(C4558R.string.zm_sip_on_remote_hold_53074);
            } else if (instance2.isInCall(currentCallItem)) {
                long callElapsedTime = (long) currentCallItem.getCallElapsedTime();
                if (callElapsedTime > 0) {
                    this.mTvTimeState.setText(TimeUtil.formateDuration(callElapsedTime));
                } else {
                    this.mTvTimeState.setText("");
                }
                return;
            } else {
                this.mTvTimeState.setText(C4558R.string.zm_sip_call_calling_503);
            }
            setContentDescription(instance.getString(C4558R.string.zm_sip_minimized_call_window_description_92481, new Object[]{this.mTvTimeState.getText().toString()}));
        }
    }

    /* access modifiers changed from: private */
    public void setUpdateAccessibilityCallTime() {
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance != null) {
            CmmSIPCallManager instance2 = CmmSIPCallManager.getInstance();
            CmmSIPCallItem currentCallItem = instance2.getCurrentCallItem();
            if (instance2.isInCall(currentCallItem)) {
                long callElapsedTime = (long) currentCallItem.getCallElapsedTime();
                setContentDescription(instance.getString(C4558R.string.zm_sip_minimized_call_window_description_time_format_92481, new Object[]{Long.valueOf(callElapsedTime / 60), Long.valueOf(callElapsedTime % 60)}));
            }
        }
    }

    private void setContentDescription(String str) {
        CharSequence contentDescription = this.mToucherLayout.getContentDescription();
        if (contentDescription == null || !StringUtil.isSameString(str, contentDescription.toString())) {
            this.mTvTimeState.setContentDescription(str);
            this.mToucherLayout.setContentDescription(str);
        }
    }

    private void initTimeState() {
        CmmSIPCallManager instance = CmmSIPCallManager.getInstance();
        CmmSIPCallItem callItemByCallID = instance.getCallItemByCallID(instance.getCurrentCallID());
        if (callItemByCallID == null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
        }
        if (instance.isInCall(callItemByCallID) || instance.isInHold(callItemByCallID) || instance.isAccepted(callItemByCallID)) {
            this.mHandler.sendEmptyMessage(2);
        } else {
            this.mHandler.removeMessages(2);
        }
        this.mHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void saveLocationInWindow(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.valueOf(i));
        sb.append(PreferencesConstants.COOKIE_DELIMITER);
        sb.append(String.valueOf(i2));
        PreferenceUtil.saveStringValue(PreferenceUtil.PBX_FLOAT_WINDOW_LOCATION, sb.toString());
    }

    @NonNull
    private int[] getLocationInWindow() {
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.PBX_FLOAT_WINDOW_LOCATION, null);
        if (!StringUtil.isEmptyOrNull(readStringValue) && readStringValue.contains(PreferencesConstants.COOKIE_DELIMITER)) {
            String[] split = readStringValue.split(PreferencesConstants.COOKIE_DELIMITER);
            if (split.length == 2) {
                int[] iArr = new int[2];
                try {
                    iArr[0] = Integer.parseInt(split[0]);
                    iArr[1] = Integer.parseInt(split[1]);
                    return iArr;
                } catch (Exception unused) {
                }
            }
        }
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        if (instance == null) {
            return new int[2];
        }
        return new int[]{UIUtil.getDisplayWidth(instance) - this.WIDTH, (UIUtil.getDisplayHeight(instance) - instance.getResources().getDimensionPixelSize(C4558R.dimen.zm_home_page_bottom_tab_bar_height)) - UIUtil.dip2px(instance, 146.0f)};
    }

    public void checkToUpdate() {
        if (isShowing() && isViewCreated()) {
            updateUI();
        }
    }

    public void hide() {
        CmmSIPCallManager.getInstance().removeListener(this.mOnSipCallEventListener);
        ZoomMessengerUI.getInstance().removeListener(this.mMessengerUIListener);
        PTUI.getInstance().removeConfInvitationListener(this.mConfInvitationListener);
        EventBus.getDefault().unregister(this);
        this.mHandler.removeCallbacksAndMessages(null);
        this.isPerformClick = false;
        this.isShowing = false;
        if (isViewCreated()) {
            try {
                if (this.mWindowManager != null) {
                    this.mWindowManager.removeView(this.mToucherLayout);
                }
            } catch (Exception e) {
                ZMLog.m281e(TAG, e, "mWindowManager.removeView(mToucherLayout) exception", new Object[0]);
            }
            this.mToucherLayout = null;
            this.mIvUIState = null;
            this.mTvTimeState = null;
            this.mIvMeetingNoAudio = null;
            this.mWindowManager = null;
            this.params = null;
        }
    }

    public void onUIInactivated() {
        if (isShowing() && isViewCreated()) {
            updateUI();
        }
    }

    public void onUIActivated() {
        if (isShowing() && isViewCreated()) {
            updateUI();
        }
    }

    /* access modifiers changed from: private */
    public void updateUI() {
        if (isShowing() && isViewCreated()) {
            boolean hasSipCallsInCache = CmmSIPCallManager.getInstance().hasSipCallsInCache();
            boolean hasMeetings = CmmSIPCallManager.getInstance().hasMeetings();
            if (!hasSipCallsInCache) {
                hide();
                return;
            }
            this.mIvMeetingNoAudio.setVisibility(8);
            this.mIvInBackgoundState.setVisibility(8);
            boolean isAudioInMeeting = CmmSipAudioMgr.getInstance().isAudioInMeeting();
            boolean isInMeetingUI = isInMeetingUI();
            boolean isInSipInCallUI = isInSipInCallUI();
            if (hasMeetings || !isInSipInCallUI) {
                if (!hasSipCallsInCache || !hasMeetings) {
                    if (hasSipCallsInCache) {
                        if (!isInSipInCallUI) {
                            this.mIvUIState.setImageResource(C4558R.C4559drawable.zm_ic_sip_blue);
                        }
                        this.mTvTimeState.setText("");
                    } else if (hasMeetings) {
                        if (!isInMeetingUI) {
                            this.mIvUIState.setImageResource(C4558R.C4559drawable.zm_ic_meeting_blue);
                            this.mTvTimeState.setText(C4558R.string.zm_sip_inmeeting_108086);
                        } else {
                            this.mTvTimeState.setText("");
                        }
                    }
                } else if (isInMeetingUI) {
                    this.mIvUIState.setImageResource(C4558R.C4559drawable.zm_ic_sip_blue);
                    if (isAudioInMeeting) {
                        this.mTvTimeState.setText(C4558R.string.zm_sip_call_on_hold_61381);
                    } else {
                        this.mTvTimeState.setText("");
                    }
                } else if (isInSipInCallUI) {
                    this.mIvUIState.setImageResource(C4558R.C4559drawable.zm_ic_meeting_blue);
                    if (!isAudioInMeeting) {
                        this.mIvMeetingNoAudio.setVisibility(0);
                    }
                    this.mTvTimeState.setText(C4558R.string.zm_sip_inmeeting_108086);
                } else if (isAudioInMeeting) {
                    this.mIvUIState.setImageResource(C4558R.C4559drawable.zm_ic_meeting_blue);
                    this.mTvTimeState.setText(C4558R.string.zm_sip_inmeeting_108086);
                    this.mIvInBackgoundState.setImageResource(C4558R.C4559drawable.zm_sip_icon_pbx_inbackground);
                    this.mIvInBackgoundState.setVisibility(0);
                } else {
                    this.mTvTimeState.setText("");
                    this.mIvUIState.setImageResource(C4558R.C4559drawable.zm_ic_sip_blue);
                    this.mIvInBackgoundState.setImageResource(C4558R.C4559drawable.zm_sip_icon_meeting_inbackground);
                    this.mIvInBackgoundState.setVisibility(0);
                }
                initTimeState();
                return;
            }
            hide();
        }
    }

    private boolean isInSipInCallUI() {
        return ZMPhoneUtils.isInSipInCallUI() || ZMPhoneUtils.isInConfCallingUI();
    }

    private boolean isInMeetingUI() {
        return ZMPhoneUtils.isInMeetingUI();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ZMReturnToCallEvent zMReturnToCallEvent) {
        backToSip();
        updateUI();
    }
}
