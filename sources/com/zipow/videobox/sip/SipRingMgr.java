package com.zipow.videobox.sip;

import android.content.Context;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.view.AudioClip;
import p021us.zoom.videomeetings.C4558R;

public class SipRingMgr {
    public static final int STOP_RING_DELAY = 500;
    private static final String TAG = "SipRingMgr";
    private static final long[] VIBRATES = {2000, 1000, 2000, 1000};
    private static SipRingMgr instance;
    @Nullable
    private AudioClip mRingClip = null;
    @Nullable
    private Vibrator mVibrator;

    public static SipRingMgr getInstance() {
        if (instance == null) {
            synchronized (SipRingMgr.class) {
                if (instance == null) {
                    instance = new SipRingMgr();
                }
            }
        }
        return instance;
    }

    private SipRingMgr() {
    }

    public void startRing(@Nullable Context context) {
        if (context != null) {
            if (SIPAudioUtil.willRing(context)) {
                if (this.mRingClip == null) {
                    if (!CmmSIPCallManager.isInit() || !CmmSIPCallManager.getInstance().hasSipCallsInCache()) {
                        this.mRingClip = new AudioClip(C4558R.raw.zm_ring, 2);
                    } else {
                        this.mRingClip = new AudioClip(C4558R.raw.zm_ring, 0);
                    }
                }
                if (!this.mRingClip.isPlaying()) {
                    this.mRingClip.startPlay();
                }
            }
            if (SIPAudioUtil.willVibrate(context)) {
                if (this.mVibrator == null) {
                    this.mVibrator = (Vibrator) context.getSystemService("vibrator");
                }
                Vibrator vibrator = this.mVibrator;
                if (vibrator != null && vibrator.hasVibrator()) {
                    this.mVibrator.vibrate(VIBRATES, 0);
                }
            }
        }
    }

    public void stopRing() {
        AudioClip audioClip = this.mRingClip;
        if (audioClip != null) {
            if (audioClip.isPlaying()) {
                this.mRingClip.stopPlay();
            }
            this.mRingClip = null;
        }
        Vibrator vibrator = this.mVibrator;
        if (vibrator != null) {
            vibrator.cancel();
            this.mVibrator = null;
        }
    }

    public void checkStopRingWhenActivityStopped(long j) {
        if (System.currentTimeMillis() - j > 500) {
            stopRing();
        }
    }
}
