package com.zipow.videobox.view.sip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPCallItem;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.ActivityStartHelper;
import java.lang.ref.WeakReference;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class SipTransferResultActivity extends ZMActivity {
    private static final String ARG_CALLID = "call_id";
    private static final int MSG_TIMER = 1;
    private static final int MSG_TRANSFER_EXPIRE = 5;
    private static final int MSG_TRANSFER_FAIL = 3;
    private static final int MSG_TRANSFER_SUCCESS = 4;
    private String mCallId;
    @NonNull
    private Handler mHandler = new TransferHandler(this);
    private ImageView mIvTransferResult;
    private ProgressBar mProgressBar;
    @NonNull
    SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnCallTransferResult(String str, int i) {
            super.OnCallTransferResult(str, i);
            SipTransferResultActivity.this.onTransferResult(i);
        }

        public void OnCallTerminate(String str, int i) {
            super.OnCallTerminate(str, i);
            SipTransferResultActivity.this.startTimer(3);
        }
    };
    /* access modifiers changed from: private */
    public TextView mTimerView;
    private TextView mTvTransferResult;

    private static class TransferHandler extends Handler {
        private WeakReference<SipTransferResultActivity> activity;

        public TransferHandler(SipTransferResultActivity sipTransferResultActivity) {
            this.activity = new WeakReference<>(sipTransferResultActivity);
        }

        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            SipTransferResultActivity sipTransferResultActivity = (SipTransferResultActivity) this.activity.get();
            if (sipTransferResultActivity != null) {
                int i = message.what;
                if (i != 1) {
                    switch (i) {
                        case 3:
                            sipTransferResultActivity.setTransferFail();
                            break;
                        case 4:
                            sipTransferResultActivity.setTransferSuccess();
                            break;
                        case 5:
                            sipTransferResultActivity.mTimerView.setVisibility(0);
                            sipTransferResultActivity.startTimer(9, 3);
                            break;
                    }
                } else if (message.arg1 > 0) {
                    sipTransferResultActivity.setTimerViewState(message.arg1);
                    sipTransferResultActivity.dispatchDelayTimer(message.arg1, message.arg2);
                } else if (message.arg2 <= 0) {
                    sipTransferResultActivity.finish();
                } else {
                    sipTransferResultActivity.mTimerView.setVisibility(8);
                    sendEmptyMessage(message.arg2);
                }
            }
        }
    }

    public static void show(@NonNull Context context, String str) {
        Intent intent = new Intent(context, SipTransferResultActivity.class);
        intent.putExtra(ARG_CALLID, str);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(2097280);
        UIUtil.renderStatueBar(this, true, C4409R.color.zm_ui_kit_color_white_ffffff);
        this.mCallId = getIntent().getStringExtra(ARG_CALLID);
        setContentView(C4558R.layout.zm_sip_transfer_result_activity);
        this.mTvTransferResult = (TextView) findViewById(C4558R.C4560id.tvResult);
        this.mProgressBar = (ProgressBar) findViewById(C4558R.C4560id.progress);
        this.mIvTransferResult = (ImageView) findViewById(C4558R.C4560id.ivResult);
        this.mTimerView = (TextView) findViewById(C4558R.C4560id.tv_timer);
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
        if (isTerminated()) {
            finish();
        }
        this.mProgressBar.setVisibility(0);
        this.mIvTransferResult.setVisibility(8);
        this.mTvTransferResult.setText(C4558R.string.zm_sip_transferring_31432);
        this.mHandler.sendEmptyMessageDelayed(5, 1000);
    }

    public void onTransferResult(int i) {
        if (i == 1) {
            postTransferFail();
        } else {
            postTransferSuccess();
        }
    }

    /* access modifiers changed from: private */
    public void setTimerViewState(int i) {
        this.mTimerView.setText(getString(C4558R.string.zm_sip_transfer_timer_101964, new Object[]{Integer.valueOf(i)}));
    }

    /* access modifiers changed from: private */
    public void setTransferFail() {
        this.mTvTransferResult.setText(getString(C4558R.string.zm_sip_transfer_fail_31432));
        this.mIvTransferResult.setImageResource(C4558R.C4559drawable.zm_sip_ic_transfer_fail);
        this.mProgressBar.setVisibility(8);
        this.mIvTransferResult.setVisibility(0);
        this.mTimerView.setVisibility(8);
        startTimer(3);
    }

    /* access modifiers changed from: private */
    public void setTransferSuccess() {
        this.mTvTransferResult.setText(getString(C4558R.string.zm_sip_transfer_success_31432));
        this.mIvTransferResult.setImageResource(C4558R.C4559drawable.zm_ic_selected_big);
        this.mProgressBar.setVisibility(8);
        this.mIvTransferResult.setVisibility(0);
        this.mTimerView.setVisibility(8);
        startTimer(3);
    }

    private void postTransferSuccess() {
        this.mHandler.removeMessages(5);
        this.mHandler.sendEmptyMessage(4);
    }

    private void postTransferFail() {
        this.mHandler.removeMessages(5);
        this.mHandler.sendEmptyMessage(3);
    }

    /* access modifiers changed from: private */
    public void startTimer(int i) {
        startTimer(i, -1);
    }

    /* access modifiers changed from: private */
    public void startTimer(int i, int i2) {
        this.mHandler.removeMessages(1);
        Message message = new Message();
        message.what = 1;
        message.arg1 = i;
        message.arg2 = i2;
        this.mHandler.sendMessage(message);
    }

    /* access modifiers changed from: private */
    public void dispatchDelayTimer(int i, int i2) {
        this.mHandler.removeMessages(1);
        Message message = new Message();
        message.what = 1;
        message.arg1 = i - 1;
        message.arg2 = i2;
        this.mHandler.sendMessageDelayed(message, 1000);
    }

    public void onDestroy() {
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
        this.mHandler.removeMessages(1);
        this.mHandler.removeMessages(5);
        this.mHandler.removeMessages(4);
        this.mHandler.removeMessages(3);
        super.onDestroy();
    }

    private boolean isTerminated() {
        boolean z = true;
        if (TextUtils.isEmpty(this.mCallId)) {
            return true;
        }
        CmmSIPCallItem callItemByCallID = CmmSIPCallManager.getInstance().getCallItemByCallID(this.mCallId);
        if (callItemByCallID == null) {
            return true;
        }
        if (callItemByCallID.getCallStatus() != 29) {
            z = false;
        }
        return z;
    }
}
