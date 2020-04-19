package com.zipow.videobox.ptapp.p013mm;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import p021us.zoom.androidlib.util.ZMLog;

/* renamed from: com.zipow.videobox.ptapp.mm.VoiceRecorder */
public class VoiceRecorder {
    public static final int ERROR_SERVER_DIED = 100;
    public static final int ERROR_UNKNOWN = 1;
    public static final int INFO_MAX_DURATION_REACHED = 800;
    public static final int INFO_MAX_FILESIZE_REACHED = 801;
    public static final int INFO_UNKNOWN = 1;
    private static final String TAG = "VoiceRecorder";
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public boolean mIsStarted = false;
    /* access modifiers changed from: private */
    public long mLastTimePassed = 0;
    /* access modifiers changed from: private */
    public IVoiceRecorderListener mListener;
    /* access modifiers changed from: private */
    public int mMaxDurationMs;
    /* access modifiers changed from: private */
    public MediaRecorder mMediaRecorder = new MediaRecorder();
    @Nullable
    private OnErrorListener mOnErrorListener = new OnErrorListener() {
        public void onError(MediaRecorder mediaRecorder, int i, int i2) {
            if (VoiceRecorder.this.mListener != null) {
                VoiceRecorder.this.mListener.onError(i, i2);
            }
        }
    };
    @Nullable
    private OnInfoListener mOnInfoListener = new OnInfoListener() {
        public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
            if (VoiceRecorder.this.mListener != null) {
                VoiceRecorder.this.mListener.onInfo(i, i2);
            }
            if (i == 800 || i == 801) {
                VoiceRecorder.this.stopRecord();
            }
        }
    };
    @Nullable
    private String mOutputFile = null;
    /* access modifiers changed from: private */
    public long mTimeStart = 0;

    /* renamed from: com.zipow.videobox.ptapp.mm.VoiceRecorder$IVoiceRecorderListener */
    public interface IVoiceRecorderListener {
        void onError(int i, int i2);

        void onInfo(int i, int i2);

        void onRecordEnd();

        void onTimeUpdate(long j);

        void onVolumeUpdate(float f);
    }

    public void setListener(IVoiceRecorderListener iVoiceRecorderListener) {
        this.mListener = iVoiceRecorderListener;
    }

    public boolean startRecord() {
        try {
            this.mMediaRecorder.start();
            this.mIsStarted = true;
            startTimer();
            return true;
        } catch (Exception e) {
            ZMLog.m281e(TAG, e, "start record failed", new Object[0]);
            try {
                this.mIsStarted = false;
                this.mMediaRecorder.reset();
            } catch (Exception unused) {
            }
            return false;
        }
    }

    private void startTimer() {
        C32933 r0 = new Runnable() {
            private int _absMax = 0;
            private int _count = 0;
            private int mVolumeLast = 0;
            @NonNull
            int[] permutationHigh = {4, 4, 4, 4, 5, 5, 5, 5, 6, 6, 6, 6, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9};
            @NonNull
            int[] permutationLow = {0, 1, 2, 3};

            public void run() {
                if (VoiceRecorder.this.mIsStarted) {
                    if (VoiceRecorder.this.mListener != null) {
                        int i = 0;
                        try {
                            i = VoiceRecorder.this.mMediaRecorder.getMaxAmplitude();
                        } catch (Exception unused) {
                        }
                        int calculateVolume = calculateVolume(i);
                        if (this.mVolumeLast != calculateVolume) {
                            this.mVolumeLast = calculateVolume;
                            VoiceRecorder.this.mListener.onVolumeUpdate(((float) calculateVolume) / 9.0f);
                        }
                        long currentTimeMillis = System.currentTimeMillis() - VoiceRecorder.this.mTimeStart;
                        if (currentTimeMillis > VoiceRecorder.this.mLastTimePassed) {
                            VoiceRecorder.this.mLastTimePassed = currentTimeMillis;
                            VoiceRecorder.this.mListener.onTimeUpdate((500 + currentTimeMillis) / 1000);
                        }
                        if (currentTimeMillis >= ((long) VoiceRecorder.this.mMaxDurationMs)) {
                            VoiceRecorder.this.stopRecord();
                        }
                    }
                    if (VoiceRecorder.this.mIsStarted) {
                        VoiceRecorder.this.mHandler.postDelayed(this, 50);
                    }
                }
            }

            private int calculateVolume(int i) {
                if (i > this._absMax) {
                    this._absMax = i;
                }
                int i2 = this.mVolumeLast;
                int i3 = this._count;
                this._count = i3 + 1;
                if (i3 == 2) {
                    this._count = 0;
                    int i4 = this._absMax;
                    if (i4 < 1000) {
                        i2 = this.permutationLow[i4 / Callback.DEFAULT_SWIPE_ANIMATION_DURATION];
                    } else {
                        int i5 = (i4 / 1000) - 1;
                        if (i5 > 32) {
                            i5 = 32;
                        }
                        i2 = this.permutationHigh[i5];
                    }
                    this._absMax >>= 2;
                }
                return i2;
            }
        };
        this.mTimeStart = System.currentTimeMillis();
        this.mLastTimePassed = 0;
        this.mHandler.postDelayed(r0, 50);
    }

    public boolean stopRecord() {
        try {
            this.mIsStarted = false;
            this.mMediaRecorder.stop();
            this.mMediaRecorder.reset();
            this.mTimeStart = 0;
            this.mLastTimePassed = 0;
            IVoiceRecorderListener iVoiceRecorderListener = this.mListener;
            if (iVoiceRecorderListener != null) {
                iVoiceRecorderListener.onRecordEnd();
            }
            return true;
        } catch (Exception e) {
            ZMLog.m281e(TAG, e, "stopRecord record failed", new Object[0]);
            this.mTimeStart = 0;
            this.mLastTimePassed = 0;
            IVoiceRecorderListener iVoiceRecorderListener2 = this.mListener;
            if (iVoiceRecorderListener2 != null) {
                iVoiceRecorderListener2.onRecordEnd();
            }
            return false;
        } catch (Throwable th) {
            this.mTimeStart = 0;
            this.mLastTimePassed = 0;
            IVoiceRecorderListener iVoiceRecorderListener3 = this.mListener;
            if (iVoiceRecorderListener3 != null) {
                iVoiceRecorderListener3.onRecordEnd();
            }
            throw th;
        }
    }

    public void setMaxDuration(int i) {
        this.mMaxDurationMs = i;
        try {
            this.mMediaRecorder.setMaxDuration(i);
        } catch (Exception unused) {
        }
    }

    public void setOutputFile(@Nullable String str) {
        if (str != null) {
            this.mOutputFile = str;
            try {
                this.mMediaRecorder.setOutputFile(str);
            } catch (Exception unused) {
            }
        }
    }

    @Nullable
    public String getOutputFile() {
        return this.mOutputFile;
    }

    public boolean prepare() {
        try {
            this.mMediaRecorder.setOnErrorListener(this.mOnErrorListener);
            this.mMediaRecorder.setOnInfoListener(this.mOnInfoListener);
            this.mMediaRecorder.setAudioSource(1);
            this.mMediaRecorder.setOutputFormat(3);
            this.mMediaRecorder.setAudioEncoder(1);
            this.mMediaRecorder.prepare();
            return true;
        } catch (Exception e) {
            ZMLog.m281e(TAG, e, "prepare record failed", new Object[0]);
            return false;
        }
    }

    public void release() {
        try {
            this.mMediaRecorder.release();
        } catch (Exception e) {
            ZMLog.m281e(TAG, e, "release recorder failed", new Object[0]);
        }
    }
}
