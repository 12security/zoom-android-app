package com.zipow.videobox.confapp.p009bo;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.confapp.bo.BOStatusChangeMgrOnPT */
public class BOStatusChangeMgrOnPT {
    private static final String TAG = "BOStatusChangeMgrOnPT";
    @Nullable
    private static BOStatusChangeMgrOnPT instance;
    @Nullable
    private final Context mContext = VideoBoxApplication.getInstance();
    @NonNull
    private Runnable mForceHideRunnable = new Runnable() {
        public void run() {
            BOStatusChangeMgrOnPT.this.hideStatusChangeUI();
        }
    };
    private Handler mHandler = new Handler();
    private ImageView mJoiningIcon;
    private TextView mJoningPrompt;
    private ImageView mLeavingIcon;
    private TextView mLeavingPrompt;
    private View mStatusChangeView;
    /* access modifiers changed from: private */
    public ImageView mWaitingAnimation;
    private final WindowManager mWindowManager = ((WindowManager) this.mContext.getSystemService("window"));
    private boolean mbAddedView = false;

    @NonNull
    public static BOStatusChangeMgrOnPT getInstance() {
        if (instance == null) {
            instance = new BOStatusChangeMgrOnPT();
        }
        return instance;
    }

    private void init() {
        this.mStatusChangeView = View.inflate(this.mContext, C4558R.layout.zm_bo_status_change, null);
        this.mJoiningIcon = (ImageView) this.mStatusChangeView.findViewById(C4558R.C4560id.joiningImage);
        this.mLeavingIcon = (ImageView) this.mStatusChangeView.findViewById(C4558R.C4560id.leavingImage);
        this.mWaitingAnimation = (ImageView) this.mStatusChangeView.findViewById(C4558R.C4560id.waitingAnimation);
        this.mJoningPrompt = (TextView) this.mStatusChangeView.findViewById(C4558R.C4560id.txtJoiningPrompt);
        this.mLeavingPrompt = (TextView) this.mStatusChangeView.findViewById(C4558R.C4560id.txtLeavingPrompt);
        this.mWaitingAnimation.setImageResource(C4558R.C4559drawable.zm_bo_connecting);
    }

    private void release() {
        this.mStatusChangeView = null;
        this.mJoiningIcon = null;
        this.mLeavingIcon = null;
        this.mWaitingAnimation = null;
        this.mJoningPrompt = null;
        this.mLeavingPrompt = null;
    }

    @NonNull
    private LayoutParams getLayoutParams() {
        LayoutParams layoutParams = new LayoutParams();
        if (VERSION.SDK_INT < 25 || !Settings.canDrawOverlays(this.mContext)) {
            layoutParams.type = CompatUtils.getSystemAlertWindowType(2005);
        } else {
            layoutParams.type = CompatUtils.getSystemAlertWindowType(2003);
        }
        layoutParams.flags |= 264;
        layoutParams.format = 1;
        layoutParams.height = -1;
        layoutParams.width = -1;
        layoutParams.gravity = 17;
        return layoutParams;
    }

    public void handleStatusChangeStart(boolean z, int i, String str) {
        showStatusChangeUI(z, i, str);
    }

    public void handleStatusChangeCompeleted() {
        hideStatusChangeUI();
    }

    private void showStatusChangeUI(boolean z, int i, String str) {
        if (this.mWindowManager != null && !this.mbAddedView) {
            init();
            try {
                this.mWindowManager.addView(this.mStatusChangeView, getLayoutParams());
                this.mbAddedView = true;
                if (z) {
                    this.mJoiningIcon.setVisibility(0);
                    this.mJoningPrompt.setVisibility(0);
                    this.mLeavingIcon.setVisibility(8);
                    this.mLeavingPrompt.setVisibility(8);
                    if (i == 1) {
                        this.mJoningPrompt.setText(this.mContext.getString(C4558R.string.zm_bo_lbl_join_by_host_prompt, new Object[]{str}));
                    } else {
                        this.mJoningPrompt.setText(this.mContext.getString(C4558R.string.zm_bo_lbl_joining_prompt, new Object[]{str}));
                    }
                } else {
                    this.mJoiningIcon.setVisibility(8);
                    this.mJoningPrompt.setVisibility(8);
                    this.mLeavingIcon.setVisibility(0);
                    this.mLeavingPrompt.setVisibility(0);
                }
                this.mStatusChangeView.setVisibility(0);
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Drawable drawable = BOStatusChangeMgrOnPT.this.mWaitingAnimation.getDrawable();
                        if (drawable instanceof AnimationDrawable) {
                            ((AnimationDrawable) drawable).start();
                        }
                    }
                });
                this.mHandler.postDelayed(this.mForceHideRunnable, 10000);
            } catch (Exception unused) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void hideStatusChangeUI() {
        WindowManager windowManager = this.mWindowManager;
        if (windowManager != null) {
            View view = this.mStatusChangeView;
            if (view != null) {
                if (this.mbAddedView) {
                    windowManager.removeView(view);
                    this.mbAddedView = false;
                }
                this.mHandler.removeCallbacks(this.mForceHideRunnable);
                release();
            }
        }
    }
}
