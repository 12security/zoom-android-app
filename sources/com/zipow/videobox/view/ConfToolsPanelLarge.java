package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import com.zipow.videobox.view.ConfToolsPanel.Listener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ConfToolsPanelLarge extends ConfToolsPanel {
    public ConfToolsPanelLarge(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public ConfToolsPanelLarge(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ConfToolsPanelLarge(Context context) {
        super(context);
    }

    public void showToolbar(boolean z, boolean z2) {
        TranslateAnimation translateAnimation;
        final ConfToolbar confToolbar = (ConfToolbar) findViewById(C4558R.C4560id.confToolbar);
        int i = 0;
        if ((confToolbar.getVisibility() == 0) != z) {
            if (!z2) {
                if (!z) {
                    i = 8;
                }
                confToolbar.setVisibility(i);
                this.mbVisible = z;
                Listener listener = getListener();
                if (listener != null) {
                    listener.onToolbarVisibilityChanged(z);
                }
                return;
            }
            if (!z) {
                this.mbVisible = false;
                Listener listener2 = getListener();
                if (listener2 != null) {
                    listener2.onToolbarVisibilityChanged(false);
                }
            }
            if (z) {
                setVisibility(0);
                confToolbar.setVisibility(0);
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, (float) (-getHeight()), 0.0f);
                translateAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ConfToolsPanelLarge confToolsPanelLarge = ConfToolsPanelLarge.this;
                        confToolsPanelLarge.mbVisible = true;
                        Listener listener = confToolsPanelLarge.getListener();
                        if (listener != null) {
                            listener.onToolbarVisibilityChanged(true);
                        }
                    }
                });
            } else {
                translateAnimation = new TranslateAnimation(0.0f, 0.0f, 0.0f, (float) (-getHeight()));
                translateAnimation.setAnimationListener(new AnimationListener() {
                    public void onAnimationRepeat(Animation animation) {
                    }

                    public void onAnimationStart(Animation animation) {
                    }

                    public void onAnimationEnd(Animation animation) {
                        ConfToolsPanelLarge.this.setVisibility(8);
                        confToolbar.setVisibility(8);
                    }
                });
            }
            translateAnimation.setInterpolator(new DecelerateInterpolator());
            translateAnimation.setDuration(0);
            startAnimation(translateAnimation);
        }
    }

    public void setConfNumber(long j) {
        ((TextView) findViewById(C4558R.C4560id.txtTitle)).setText(getContext().getString(C4558R.string.zm_title_conf_long, new Object[]{StringUtil.formatConfNumber(j)}));
    }
}
