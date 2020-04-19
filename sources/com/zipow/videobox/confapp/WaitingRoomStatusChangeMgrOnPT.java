package com.zipow.videobox.confapp;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.videomeetings.C4558R;

public class WaitingRoomStatusChangeMgrOnPT {
    private static final String TAG = "WaitingRoomStatusChangeMgrOnPT";
    @Nullable
    private static WaitingRoomStatusChangeMgrOnPT instance;
    @Nullable
    private final Context mContext = VideoBoxApplication.getInstance();
    @NonNull
    private Runnable mForceHideRunnable = new Runnable() {
        public void run() {
            WaitingRoomStatusChangeMgrOnPT.this.hideStatusChangeUI();
        }
    };
    private Handler mHandler = new Handler();
    private TextView mPrompt;
    private View mStatusChangeView;
    private final WindowManager mWindowManager = ((WindowManager) this.mContext.getSystemService("window"));
    private boolean mbAddedView = false;

    @NonNull
    public static WaitingRoomStatusChangeMgrOnPT getInstance() {
        if (instance == null) {
            instance = new WaitingRoomStatusChangeMgrOnPT();
        }
        return instance;
    }

    private void init() {
        this.mStatusChangeView = View.inflate(this.mContext, C4558R.layout.zm_waiting_room_status_change, null);
        this.mPrompt = (TextView) this.mStatusChangeView.findViewById(C4558R.C4560id.txtPrompt);
    }

    private void release() {
        this.mStatusChangeView = null;
        this.mPrompt = null;
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

    public void handleStatusChangeStart() {
        showStatusChangeUI();
    }

    public void handleStatusChangeComplete() {
        hideStatusChangeUI();
    }

    private void showStatusChangeUI() {
        if (this.mWindowManager != null && !this.mbAddedView) {
            init();
            try {
                this.mWindowManager.addView(this.mStatusChangeView, getLayoutParams());
                this.mbAddedView = true;
                this.mPrompt.setVisibility(0);
                this.mStatusChangeView.setVisibility(0);
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
