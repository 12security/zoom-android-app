package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import p021us.zoom.videomeetings.C4558R;

public class ConfToolsPanel extends RelativeLayout {
    protected static final int DISPLAY_ANIM_DURATION = 0;
    /* access modifiers changed from: private */
    public Listener mListener;
    protected transient boolean mbVisible = true;

    public interface Listener {
        void onToolbarVisibilityChanged(boolean z);
    }

    public void setConfNumber(long j) {
    }

    public ConfToolsPanel(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ConfToolsPanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ConfToolsPanel(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setFocusable(false);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public Listener getListener() {
        return this.mListener;
    }

    public boolean isVisible() {
        return this.mbVisible;
    }

    public void showToolbar(boolean z, boolean z2) {
        TranslateAnimation translateAnimation;
        TranslateAnimation translateAnimation2;
        View findViewById = findViewById(C4558R.C4560id.panelBottom);
        View findViewById2 = findViewById(C4558R.C4560id.panelTop);
        View findViewById3 = findViewById(C4558R.C4560id.panelTop2);
        final ConfToolbar confToolbar = (ConfToolbar) findViewById(C4558R.C4560id.confToolbar);
        if (findViewById != null && confToolbar != null && findViewById2 != null && findViewById3 != null) {
            boolean z3 = true;
            if ((confToolbar.getVisibility() == 0) == z) {
                if (findViewById2.getVisibility() != 0) {
                    z3 = false;
                }
                if (z3 == z) {
                    this.mbVisible = z;
                    Listener listener = this.mListener;
                    if (listener != null) {
                        listener.onToolbarVisibilityChanged(z);
                    }
                    return;
                }
            }
            if (!z2) {
                int i = 8;
                confToolbar.setVisibility(z ? 0 : 8);
                if (z) {
                    i = 0;
                }
                setVisibilityForTopToolbar(i);
                this.mbVisible = z;
                Listener listener2 = this.mListener;
                if (listener2 != null) {
                    listener2.onToolbarVisibilityChanged(z);
                }
                return;
            }
            if (!z) {
                this.mbVisible = false;
                Listener listener3 = this.mListener;
                if (listener3 != null) {
                    listener3.onToolbarVisibilityChanged(false);
                }
            }
            if (z) {
                setVisibilityForTopToolbar(0);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-findViewById2.getHeight()), 0.0f);
                confToolbar.setVisibility(0);
                translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, (float) confToolbar.getHeight(), 0.0f);
                translateAnimation2.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ConfToolsPanel confToolsPanel = ConfToolsPanel.this;
                        confToolsPanel.mbVisible = true;
                        if (confToolsPanel.mListener != null) {
                            ConfToolsPanel.this.mListener.onToolbarVisibilityChanged(true);
                        }
                    }
                });
            } else {
                translateAnimation2 = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) confToolbar.getHeight());
                translateAnimation2.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        confToolbar.setVisibility(8);
                    }
                });
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-findViewById2.getHeight()));
                translateAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ConfToolsPanel.this.setVisibilityForTopToolbar(8);
                    }
                });
            }
            translateAnimation2.setInterpolator(new DecelerateInterpolator());
            translateAnimation2.setDuration(0);
            findViewById.startAnimation(translateAnimation2);
            translateAnimation.setInterpolator(new DecelerateInterpolator());
            translateAnimation.setDuration(0);
            findViewById2.startAnimation(translateAnimation);
        }
    }

    public void setVisibilityForTopToolbar(int i) {
        View findViewById = findViewById(C4558R.C4560id.panelTop);
        View findViewById2 = findViewById(C4558R.C4560id.panelTop2);
        if (findViewById != null) {
            findViewById.setVisibility(i);
        }
        if (findViewById2 != null) {
            findViewById2.setVisibility(i);
        }
    }
}
