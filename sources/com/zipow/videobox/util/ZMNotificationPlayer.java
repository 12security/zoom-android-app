package com.zipow.videobox.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.view.AudioClip;
import java.util.Timer;
import java.util.TimerTask;

public class ZMNotificationPlayer {
    private static final String TAG = "ZMNotificationPlayer";
    private static final int TIMEOUT_VALUE = 60000;
    private static final long[] VIBRATES = {2000, 1000, 2000, 1000};
    @Nullable
    private static ZMNotificationPlayer mInstance = null;
    /* access modifiers changed from: private */
    @Nullable
    public static OnPlayTimeOutListener mListener = null;
    @Nullable
    private AudioClip mClip = null;
    @NonNull
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Nullable
    private Timer mTimer = null;
    @Nullable
    private Vibrator mVibrator = null;

    interface OnPlayTimeOutListener {
        void onPlayTimeout();
    }

    public static void setOnPlayTimeoutListener(OnPlayTimeOutListener onPlayTimeOutListener) {
        mListener = onPlayTimeOutListener;
    }

    private ZMNotificationPlayer() {
    }

    @NonNull
    public static synchronized ZMNotificationPlayer getInstance() {
        ZMNotificationPlayer zMNotificationPlayer;
        synchronized (ZMNotificationPlayer.class) {
            if (mInstance == null) {
                mInstance = new ZMNotificationPlayer();
            }
            zMNotificationPlayer = mInstance;
        }
        return zMNotificationPlayer;
    }

    public void play(@NonNull Context context, int i, int i2) {
        play(context, i, i2, -1);
    }

    public void play(@NonNull Context context, long[] jArr) {
        play(context, jArr, -1);
    }

    public void play(@NonNull Context context, long[] jArr, long j) {
        play(context, -1, -1, jArr, j);
    }

    public void play(@NonNull Context context, int i, int i2, long j) {
        play(context, i, i2, null, j);
    }

    public void play(@NonNull Context context, int i, int i2, @Nullable long[] jArr, long j) {
        stop();
        if (i > 0) {
            this.mClip = new AudioClip(i, i2);
            this.mClip.startPlay();
        }
        if (jArr != null) {
            this.mVibrator = (Vibrator) context.getSystemService("vibrator");
            Vibrator vibrator = this.mVibrator;
            if (vibrator != null) {
                vibrator.vibrate(jArr, 0);
            }
        }
        if (j > 0) {
            this.mTimer = new Timer();
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    ZMNotificationPlayer.this.onTimeOut();
                }
            }, j);
        }
    }

    /* access modifiers changed from: private */
    public void onTimeOut() {
        stop();
        this.mHandler.post(new Runnable() {
            public void run() {
                if (ZMNotificationPlayer.mListener != null) {
                    ZMNotificationPlayer.mListener.onPlayTimeout();
                }
            }
        });
    }

    public void stop() {
        Timer timer = this.mTimer;
        if (timer != null) {
            timer.cancel();
            this.mTimer = null;
        }
        Vibrator vibrator = this.mVibrator;
        if (vibrator != null) {
            vibrator.cancel();
            this.mVibrator = null;
        }
        AudioClip audioClip = this.mClip;
        if (audioClip != null) {
            if (audioClip.isPlaying()) {
                this.mClip.stopPlay();
            }
            this.mClip = null;
        }
    }
}
