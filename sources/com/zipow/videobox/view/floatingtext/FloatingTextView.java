package com.zipow.videobox.view.floatingtext;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import com.zipow.videobox.view.floatingtext.FloatingText.Builder;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class FloatingTextView extends FrameLayout {
    private Builder mBuilder;
    private boolean mIsMeasured = false;
    private boolean mLocationInitialed = false;
    private View mReleateView;

    private void init() {
    }

    public FloatingTextView(Context context) {
        super(context);
        init();
    }

    public FloatingTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public FloatingTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_float_text_view, this);
        TextView textView = (TextView) findViewById(C4558R.C4560id.textMsg);
        if (textView != null) {
            textView.setText(this.mBuilder.getText());
        }
    }

    public void setFloatingTextBuilder(Builder builder) {
        this.mBuilder = builder;
        inflateLayout();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mBuilder != null) {
            initLocation();
            this.mIsMeasured = true;
        }
    }

    public void draw(Canvas canvas) {
        if (this.mIsMeasured && this.mLocationInitialed) {
            super.draw(canvas);
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mIsMeasured && this.mLocationInitialed) {
            super.onDraw(canvas);
        }
    }

    /* access modifiers changed from: private */
    public void initLocation() {
        int i;
        int i2;
        if (!this.mLocationInitialed) {
            if (this.mReleateView != null) {
                Rect rect = new Rect();
                this.mReleateView.getGlobalVisibleRect(rect);
                int[] iArr = new int[2];
                ((ViewGroup) getParent()).getLocationOnScreen(iArr);
                rect.offset(-iArr[0], (-iArr[1]) + this.mBuilder.getWindowOffset());
                i = rect.top - getMeasuredHeight();
                i2 = rect.left + ((this.mReleateView.getWidth() - getMeasuredWidth()) / 2);
            } else {
                i = (UIUtil.getDisplayHeight(getContext()) - getMeasuredHeight()) / 2;
                i2 = (UIUtil.getDisplayWidth(getContext()) - getMeasuredWidth()) / 2;
            }
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.topMargin = i;
            layoutParams.leftMargin = i2;
            setLayoutParams(layoutParams);
        }
        this.mLocationInitialed = true;
    }

    public void starAnimation(View view) {
        this.mReleateView = view;
        if (this.mIsMeasured) {
            initLocation();
        } else {
            getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    if (VERSION.SDK_INT >= 16) {
                        FloatingTextView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        FloatingTextView.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                    FloatingTextView.this.initLocation();
                }
            });
        }
        setTranslationY(0.0f);
        setAlpha(1.0f);
        ValueAnimator ofFloat = ObjectAnimator.ofFloat(new float[]{1.0f, 0.8f});
        long j = (long) CmmSIPCallFailReason.kSIPCall_FAIL_600_Busy_Everywhere;
        ofFloat.setDuration(j);
        ofFloat.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                FloatingTextView.this.setScaleX(1.0f);
                FloatingTextView.this.setScaleY(1.0f);
            }
        });
        ofFloat.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FloatingTextView.this.setScaleX(((Float) valueAnimator.getAnimatedValue()).floatValue());
                FloatingTextView.this.setScaleY(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        ValueAnimator ofFloat2 = ObjectAnimator.ofFloat(new float[]{0.0f, -200.0f});
        ofFloat2.setDuration(j);
        ofFloat2.setStartDelay(50);
        ofFloat2.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FloatingTextView.this.setTranslationY(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        ofFloat2.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                super.onAnimationEnd(animator);
                FloatingTextView.this.setTranslationY(0.0f);
                FloatingTextView.this.setAlpha(0.0f);
            }
        });
        ValueAnimator ofFloat3 = ObjectAnimator.ofFloat(new float[]{1.0f, 0.0f});
        ofFloat3.setDuration(j);
        ofFloat3.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                FloatingTextView.this.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        ofFloat.start();
        ofFloat3.start();
        ofFloat2.start();
    }
}
