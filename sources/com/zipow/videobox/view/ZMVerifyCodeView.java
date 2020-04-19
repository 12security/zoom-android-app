package com.zipow.videobox.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import p021us.zoom.videomeetings.C4558R;

public class ZMVerifyCodeView extends FrameLayout implements OnClickListener {
    private static final long COUNTDOWN_INTERVAL = 1000;
    private static final long MILLIS_IN_FUTURE = 60000;
    /* access modifiers changed from: private */
    public Button mBtnSendCode;
    /* access modifiers changed from: private */
    @Nullable
    public CountDownTimer mCountDownTimer;
    private boolean mEnable;
    private boolean mIsFirstSent;
    /* access modifiers changed from: private */
    public TextView mTxtSending;
    @Nullable
    private VerifyCodeCallBack mVerifyCodeCallBack;

    public interface VerifyCodeCallBack {
        void onClickSendCode();
    }

    public ZMVerifyCodeView(@NonNull Context context) {
        this(context, null);
    }

    public ZMVerifyCodeView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public ZMVerifyCodeView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mEnable = false;
        this.mIsFirstSent = true;
        init();
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.mCountDownTimer = null;
        }
        super.onDetachedFromWindow();
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_verify_code_view, this);
        this.mBtnSendCode = (Button) findViewById(C4558R.C4560id.btnSendCode);
        this.mBtnSendCode.setOnClickListener(this);
        this.mTxtSending = (TextView) findViewById(C4558R.C4560id.txtSending);
    }

    public void onClick(View view) {
        if (view.getId() == C4558R.C4560id.btnSendCode) {
            onClickSendCode();
        }
    }

    public void setmVerifyCodeCallBack(@Nullable VerifyCodeCallBack verifyCodeCallBack) {
        this.mVerifyCodeCallBack = verifyCodeCallBack;
    }

    public void enableSendCode(boolean z) {
        this.mEnable = z;
        if (this.mCountDownTimer == null) {
            updateStatus();
        }
    }

    public void forceEnableSendCode() {
        this.mEnable = true;
        CountDownTimer countDownTimer = this.mCountDownTimer;
        if (countDownTimer != null) {
            countDownTimer.cancel();
            this.mCountDownTimer = null;
        }
        updateStatus();
    }

    /* access modifiers changed from: private */
    public void updateStatus() {
        if (this.mEnable) {
            this.mBtnSendCode.setVisibility(0);
            this.mBtnSendCode.setText(this.mIsFirstSent ? C4558R.string.zm_btn_send_code_109213 : C4558R.string.zm_msg_resend_70707);
            this.mTxtSending.setVisibility(8);
            return;
        }
        this.mBtnSendCode.setVisibility(8);
        this.mTxtSending.setVisibility(0);
        this.mTxtSending.setText(C4558R.string.zm_btn_send_code_109213);
        this.mTxtSending.setTextColor(getResources().getColor(C4558R.color.zm_ui_kit_color_gray_BABACC));
    }

    private void onClickSendCode() {
        Context context = getContext();
        if (context != null) {
            CountDownTimer countDownTimer = this.mCountDownTimer;
            if (countDownTimer != null) {
                countDownTimer.cancel();
                this.mCountDownTimer = null;
            }
            this.mBtnSendCode.setVisibility(8);
            this.mTxtSending.setVisibility(0);
            this.mTxtSending.setText(context.getString(C4558R.string.zm_description_resend_code_seconds_109213, new Object[]{Long.valueOf(60)}));
            this.mTxtSending.setTextColor(getResources().getColor(C4558R.color.zm_ui_kit_color_gray_747487));
            C35891 r3 = new CountDownTimer(MILLIS_IN_FUTURE, 1000) {
                public void onTick(long j) {
                    Context context = ZMVerifyCodeView.this.getContext();
                    if (context != null) {
                        ZMVerifyCodeView.this.mTxtSending.setText(context.getString(C4558R.string.zm_description_resend_code_seconds_109213, new Object[]{Long.valueOf(j / 1000)}));
                    }
                }

                public void onFinish() {
                    if (!(ZMVerifyCodeView.this.mBtnSendCode == null || ZMVerifyCodeView.this.mTxtSending == null)) {
                        ZMVerifyCodeView.this.mBtnSendCode.setVisibility(0);
                        ZMVerifyCodeView.this.mBtnSendCode.setText(C4558R.string.zm_msg_resend_70707);
                        ZMVerifyCodeView.this.mTxtSending.setVisibility(8);
                        ZMVerifyCodeView.this.updateStatus();
                    }
                    ZMVerifyCodeView.this.mCountDownTimer = null;
                }
            };
            this.mCountDownTimer = r3;
            this.mIsFirstSent = false;
            this.mCountDownTimer.start();
            VerifyCodeCallBack verifyCodeCallBack = this.mVerifyCodeCallBack;
            if (verifyCodeCallBack != null) {
                verifyCodeCallBack.onClickSendCode();
            }
        }
    }
}
