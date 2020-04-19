package com.zipow.videobox.view.p014mm;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.ptapp.p013mm.VoiceRecorder;
import com.zipow.videobox.ptapp.p013mm.VoiceRecorder.IVoiceRecorderListener;
import java.io.File;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.VoiceRecordView */
public class VoiceRecordView extends LinearLayout implements OnClickListener {
    private static final int MAX_VOICE_DURATION_MS = 60000;
    private static final int MIN_VOICE_LENGTH = 1000;
    private static final String TAG = "VoiceRecordView";
    private Button mBtnHoldToTalk;
    private Handler mHandler = new Handler();
    private ImageView mImgVoiceRcdHint;
    /* access modifiers changed from: private */
    public boolean mIsCanceled = false;
    private boolean mIsReleaseToCancel = false;
    private OnVoiceRecordListener mOnVoiceRecordListener;
    private ProgressBar mProgressBarStartingRecording;
    private String mRecordDir;
    private TextView mTxtRcdHintText;
    /* access modifiers changed from: private */
    public VoiceRecorder mVoiceRecorder;

    /* renamed from: com.zipow.videobox.view.mm.VoiceRecordView$OnVoiceRecordListener */
    public interface OnVoiceRecordListener {
        void onRecordEnd(String str, long j);
    }

    public void onClick(View view) {
    }

    public VoiceRecordView(Context context) {
        super(context);
        init();
    }

    public VoiceRecordView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public VoiceRecordView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public void initRecordInfo(@NonNull OnVoiceRecordListener onVoiceRecordListener, String str, @NonNull Button button) {
        this.mOnVoiceRecordListener = onVoiceRecordListener;
        this.mRecordDir = str;
        this.mBtnHoldToTalk = button;
        this.mBtnHoldToTalk.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return VoiceRecordView.this.onTouchBtnHoldToTalk(motionEvent);
            }
        });
    }

    /* access modifiers changed from: private */
    public boolean onTouchBtnHoldToTalk(MotionEvent motionEvent) {
        switch (motionEvent.getActionMasked()) {
            case 0:
                startRecord();
                break;
            case 1:
            case 3:
                stopRecord(this.mIsReleaseToCancel);
                break;
            case 2:
                if (motionEvent.getY() >= 0.0f) {
                    if (this.mIsReleaseToCancel) {
                        showRecordHint();
                        break;
                    }
                } else {
                    showRecordCancelHint();
                    break;
                }
                break;
        }
        return true;
    }

    private void init() {
        View.inflate(getContext(), C4558R.layout.zm_mm_voice_hint, this);
        this.mImgVoiceRcdHint = (ImageView) findViewById(C4558R.C4560id.imgVoiceRcdHint);
        this.mProgressBarStartingRecording = (ProgressBar) findViewById(C4558R.C4560id.progressBarStartingRecording);
        this.mTxtRcdHintText = (TextView) findViewById(C4558R.C4560id.txtRcdHintText);
        setOnClickListener(this);
    }

    private void showRecordHint() {
        if (this.mVoiceRecorder != null) {
            this.mImgVoiceRcdHint.setVisibility(0);
            this.mProgressBarStartingRecording.setVisibility(8);
            this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_voice_rcd_hint_icon);
        } else {
            this.mImgVoiceRcdHint.setVisibility(8);
            this.mProgressBarStartingRecording.setVisibility(0);
        }
        this.mTxtRcdHintText.setText(C4558R.string.zm_mm_msg_rcd_hint_move_up_to_cancel);
        this.mTxtRcdHintText.setBackgroundResource(0);
        this.mIsReleaseToCancel = false;
    }

    private void showRecordCancelHint() {
        this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_voice_rcd_cancel_icon);
        this.mTxtRcdHintText.setText(C4558R.string.zm_mm_msg_rcd_hint_release_to_cancel);
        this.mIsReleaseToCancel = true;
    }

    private void startRecord() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (VERSION.SDK_INT < 23 || zMActivity.checkSelfPermission("android.permission.RECORD_AUDIO") != -1) {
            this.mBtnHoldToTalk.setPressed(true);
            this.mBtnHoldToTalk.setText(C4558R.string.zm_mm_btn_release_to_send);
            if (zMActivity != null) {
                zMActivity.setRequestedOrientation(UIUtil.getCurrentOrientation(zMActivity) == 2 ? 6 : 7);
            }
            setVisibility(0);
            this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_voice_rcd_hint_icon);
            showRecordHint();
            this.mIsCanceled = false;
            this.mHandler.post(new Runnable() {
                public void run() {
                    if (!VoiceRecordView.this.mIsCanceled) {
                        VoiceRecordView.this.startRecordImediately();
                    }
                }
            });
            return;
        }
        zMActivity.zm_requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 0);
    }

    /* access modifiers changed from: private */
    public void startRecordImediately() {
        if (this.mRecordDir == null) {
            this.mRecordDir = AppUtil.getCachePath();
        }
        String createTempFile = AppUtil.createTempFile("voice", this.mRecordDir, "amr");
        this.mVoiceRecorder = new VoiceRecorder();
        this.mVoiceRecorder.setMaxDuration(60000);
        this.mVoiceRecorder.setOutputFile(createTempFile);
        this.mVoiceRecorder.setListener(new IVoiceRecorderListener() {
            private long _length = 0;

            public void onInfo(int i, int i2) {
            }

            public void onError(int i, int i2) {
                if (VoiceRecordView.this.mVoiceRecorder != null) {
                    String outputFile = VoiceRecordView.this.mVoiceRecorder.getOutputFile();
                    VoiceRecordView.this.mVoiceRecorder.release();
                    VoiceRecordView.this.mVoiceRecorder = null;
                    VoiceRecordView.this.onVoiceRecordEnd(false, outputFile, this._length);
                }
            }

            public void onTimeUpdate(long j) {
                this._length = j;
            }

            public void onVolumeUpdate(float f) {
                VoiceRecordView.this.updateVolumeImage(f);
            }

            public void onRecordEnd() {
                if (VoiceRecordView.this.mVoiceRecorder != null) {
                    String outputFile = VoiceRecordView.this.mVoiceRecorder.getOutputFile();
                    VoiceRecordView.this.mVoiceRecorder.release();
                    VoiceRecordView.this.mVoiceRecorder = null;
                    if (VoiceRecordView.this.mIsCanceled) {
                        if (outputFile != null) {
                            File file = new File(outputFile);
                            if (file.exists()) {
                                file.delete();
                            }
                        }
                        VoiceRecordView.this.onVoiceRecordCanceled();
                    } else {
                        VoiceRecordView.this.onVoiceRecordEnd(true, outputFile, this._length);
                    }
                }
            }
        });
        if (!this.mVoiceRecorder.prepare()) {
            this.mVoiceRecorder.release();
            this.mVoiceRecorder = null;
            onVoiceRecordEnd(false, createTempFile, 0);
        } else if (this.mVoiceRecorder.startRecord()) {
            onStartRecordSuccess();
        } else {
            this.mVoiceRecorder.release();
            this.mVoiceRecorder = null;
            onVoiceRecordEnd(false, createTempFile, 0);
        }
    }

    private void onStartRecordSuccess() {
        showRecordHint();
    }

    /* access modifiers changed from: private */
    public void updateVolumeImage(float f) {
        if (!this.mIsReleaseToCancel) {
            this.mImgVoiceRcdHint.setVisibility(0);
            this.mProgressBarStartingRecording.setVisibility(8);
            switch (Math.round(f * 7.0f)) {
                case 0:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_voice_rcd_hint_icon);
                    break;
                case 1:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp1);
                    break;
                case 2:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp2);
                    break;
                case 3:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp3);
                    break;
                case 4:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp4);
                    break;
                case 5:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp5);
                    break;
                case 6:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp6);
                    break;
                case 7:
                    this.mImgVoiceRcdHint.setImageResource(C4558R.C4559drawable.zm_amp7);
                    break;
            }
        }
    }

    private void stopRecord(final boolean z) {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (VoiceRecordView.this.mVoiceRecorder != null) {
                    VoiceRecordView.this.mIsCanceled = z;
                    VoiceRecordView.this.mVoiceRecorder.stopRecord();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onVoiceRecordEnd(boolean z, String str, long j) {
        if (isAttachedToWindow()) {
            stopRecordUI();
            if (!z) {
                if (!StringUtil.isEmptyOrNull(str)) {
                    File file = new File(str);
                    if (file.exists()) {
                        file.delete();
                    }
                }
                Context context = getContext();
                if (context != null) {
                    Toast.makeText(context, C4558R.string.zm_mm_msg_record_voice_failed, 0).show();
                }
            } else if (!StringUtil.isEmptyOrNull(str)) {
                if (j * 1000 < 1000) {
                    Context context2 = getContext();
                    if (context2 != null) {
                        Toast.makeText(context2, C4558R.string.zm_mm_msg_audio_too_short, 0).show();
                        return;
                    }
                    return;
                }
                OnVoiceRecordListener onVoiceRecordListener = this.mOnVoiceRecordListener;
                if (onVoiceRecordListener != null) {
                    onVoiceRecordListener.onRecordEnd(str, j);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onVoiceRecordCanceled() {
        stopRecordUI();
    }

    private void stopRecordUI() {
        this.mBtnHoldToTalk.setPressed(false);
        this.mBtnHoldToTalk.setText(C4558R.string.zm_mm_btn_hold_to_talk);
        setVisibility(8);
        this.mIsReleaseToCancel = false;
    }
}
