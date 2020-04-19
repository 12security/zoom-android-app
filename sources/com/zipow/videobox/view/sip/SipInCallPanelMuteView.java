package com.zipow.videobox.view.sip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.zipow.videobox.view.sip.SipInCallPanelView.PanelButtonItem;
import p021us.zoom.videomeetings.C4558R;

public class SipInCallPanelMuteView extends FrameLayout {
    private static final int PANEL_MUTE_MAX_LEVEL = 9500;
    private static final int PANEL_MUTE_MIN_LEVEL = 4500;
    /* access modifiers changed from: private */
    public ValueAnimator mMutePanelValueAnimator;
    /* access modifiers changed from: private */
    public ImageView mPanelImage;
    /* access modifiers changed from: private */
    public boolean mPanelMuteTalkingState;
    private TextView mPanelText;
    private View mPanelView;
    /* access modifiers changed from: private */
    public Runnable mUpdatePanelMuteTalkingStateRunnable = new Runnable() {
        public void run() {
            SipInCallPanelMuteView sipInCallPanelMuteView = SipInCallPanelMuteView.this;
            sipInCallPanelMuteView.removeCallbacks(sipInCallPanelMuteView.mUpdatePanelMuteTalkingStateRunnable);
            Integer num = (Integer) SipInCallPanelMuteView.this.mMutePanelValueAnimator.getAnimatedValue();
            if (num == null || num.intValue() < SipInCallPanelMuteView.PANEL_MUTE_MIN_LEVEL) {
                num = Integer.valueOf(SipInCallPanelMuteView.PANEL_MUTE_MIN_LEVEL);
            }
            if (num.intValue() > SipInCallPanelMuteView.PANEL_MUTE_MAX_LEVEL) {
                num = Integer.valueOf(SipInCallPanelMuteView.PANEL_MUTE_MAX_LEVEL);
            }
            SipInCallPanelMuteView.this.mMutePanelValueAnimator.cancel();
            int i = (num.intValue() == SipInCallPanelMuteView.PANEL_MUTE_MAX_LEVEL || !SipInCallPanelMuteView.this.mPanelMuteTalkingState) ? SipInCallPanelMuteView.PANEL_MUTE_MIN_LEVEL : SipInCallPanelMuteView.PANEL_MUTE_MAX_LEVEL;
            SipInCallPanelMuteView.this.mMutePanelValueAnimator.setIntValues(new int[]{num.intValue(), i});
            SipInCallPanelMuteView.this.mMutePanelValueAnimator.setDuration((long) ((i == SipInCallPanelMuteView.PANEL_MUTE_MAX_LEVEL ? SipInCallPanelMuteView.PANEL_MUTE_MAX_LEVEL - num.intValue() : num.intValue() - 4500) / 20));
            SipInCallPanelMuteView.this.mMutePanelValueAnimator.start();
        }
    };

    public SipInCallPanelMuteView(@NonNull Context context) {
        super(context);
        initViews();
    }

    public SipInCallPanelMuteView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews();
    }

    public SipInCallPanelMuteView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initViews();
    }

    @RequiresApi(api = 21)
    public SipInCallPanelMuteView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initViews();
    }

    private void initViews() {
        inflate(getContext(), C4558R.layout.zm_sip_in_call_panel_item_view, this);
        this.mPanelView = findViewById(C4558R.C4560id.panelView);
        this.mPanelImage = (ImageView) findViewById(C4558R.C4560id.panelImage);
        this.mPanelText = (TextView) findViewById(C4558R.C4560id.panelText);
    }

    public void bindView(PanelButtonItem panelButtonItem) {
        if (panelButtonItem != null) {
            this.mPanelImage.setImageDrawable(panelButtonItem.getIcon());
            this.mPanelImage.setContentDescription(panelButtonItem.getLabel());
            this.mPanelImage.setEnabled(!panelButtonItem.isDisable());
            this.mPanelImage.setSelected(panelButtonItem.isSelected());
            this.mPanelText.setSelected(panelButtonItem.isSelected());
            this.mPanelText.setEnabled(!panelButtonItem.isDisable());
            this.mPanelText.setText(panelButtonItem.getLabel());
        }
    }

    public void togglePanelMuteTalkingState(boolean z) {
        if (this.mPanelMuteTalkingState != z) {
            this.mPanelMuteTalkingState = z;
            if (this.mMutePanelValueAnimator == null) {
                this.mMutePanelValueAnimator = new ValueAnimator();
                this.mMutePanelValueAnimator.addUpdateListener(new AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        if (SipInCallPanelMuteView.this.mPanelImage != null) {
                            SipInCallPanelMuteView.this.mPanelImage.setImageLevel(((Integer) valueAnimator.getAnimatedValue()).intValue());
                        }
                    }
                });
                this.mMutePanelValueAnimator.addListener(new AnimatorListenerAdapter() {
                    public void onAnimationEnd(Animator animator) {
                        if (SipInCallPanelMuteView.this.mPanelMuteTalkingState) {
                            SipInCallPanelMuteView sipInCallPanelMuteView = SipInCallPanelMuteView.this;
                            sipInCallPanelMuteView.post(sipInCallPanelMuteView.mUpdatePanelMuteTalkingStateRunnable);
                        }
                    }
                });
            }
            postDelayed(this.mUpdatePanelMuteTalkingStateRunnable, 100);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ValueAnimator valueAnimator = this.mMutePanelValueAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        removeCallbacks(this.mUpdatePanelMuteTalkingStateRunnable);
    }
}
