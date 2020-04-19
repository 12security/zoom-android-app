package com.zipow.videobox.view.sip;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.common.primitives.Ints;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.sip.server.CmmPBXCallHistoryManager;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItem;
import com.zipow.videobox.sip.server.CmmSIPAudioFileItemBean;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.CmmSIPVoiceMailItem;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.SimpleISIPCallRepositoryEventSinkListener;
import com.zipow.videobox.view.sip.ZMSeekBar.OnProgressChangedListener;
import java.io.File;
import java.io.IOException;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class PhonePBXListCoverView extends ListCoverView implements OnClickListener, IHeadsetConnectionListener {
    private static final int MAX_RETRY = 3;
    private static final int MSG_REQUEST_TRANSCRIPT = 2;
    private static final int MSG_UDPATE_PROGRESS = 1;
    private static final long REQUEST_TRANSCRIPT_DELAY = 15000;
    private static final String TAG = "PhonePBXListCoverView";
    private static final int TRANSCRIPT_HEIGHT = 100;
    private static final int TRANSCRIPT_LINE_SPACING = 2;
    private static final int TRANSCRIPT_MARGIN = 56;
    private static final long UPDATE_SEEKUI_DELAY = 200;
    @Nullable
    private AudioManager audioManager;
    private boolean mAutoPlay = false;
    private int mBaseExpandHeight;
    private AudioPlayerControllerButton mBtnAudioPlayer;
    private View mBtnAudioShare;
    /* access modifiers changed from: private */
    public View mCoverContentView;
    /* access modifiers changed from: private */
    @NonNull
    public Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(@NonNull Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 1:
                    PhonePBXListCoverView.this.mHandler.removeMessages(1);
                    PhonePBXListCoverView.this.setSeekUI();
                    PhonePBXListCoverView.this.mHandler.sendEmptyMessageDelayed(1, 200);
                    break;
                case 2:
                    if (PhonePBXListCoverView.this.getTag() != null) {
                        String str = ((PBXCallHistory) PhonePBXListCoverView.this.getTag()).f349id;
                        if (!TextUtils.isEmpty(str)) {
                            if (PhonePBXListCoverView.this.retry = PhonePBXListCoverView.this.retry + 1 >= 3) {
                                PhonePBXListCoverView.this.retry = 0;
                                PhonePBXListCoverView phonePBXListCoverView = PhonePBXListCoverView.this;
                                phonePBXListCoverView.updateTranscriptPanel(false, false, false, phonePBXListCoverView.getResources().getString(C4558R.string.zm_sip_transcribe_fail_61402));
                                break;
                            } else {
                                CmmPBXCallHistoryManager.getInstance().requestForVoiceMailTranscript(str);
                                PhonePBXListCoverView phonePBXListCoverView2 = PhonePBXListCoverView.this;
                                phonePBXListCoverView2.updateTranscriptPanel(false, true, true, phonePBXListCoverView2.getResources().getString(C4558R.string.zm_sip_transcribe_processing_61402));
                                break;
                            }
                        }
                    } else {
                        return;
                    }
                    break;
            }
        }
    };
    private ImageView mImgDeleteCall;
    private ImageView mImgOutCall;
    private boolean mIsMediaPrepared;
    private int mMaxTranscriptExpandHeight;
    private int mMeasure5Height;
    private TextView mMeasureTextView;
    private int mMeasureWidth;
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    @NonNull
    SimpleISIPCallRepositoryEventSinkListener mPBXRepositoryListener = new SimpleISIPCallRepositoryEventSinkListener() {
        public void OnRequestDoneForVoiceMailTranscript(int i, String str, String str2) {
            super.OnRequestDoneForVoiceMailTranscript(i, str, str2);
            switch (i) {
                case 1:
                    PhonePBXListCoverView.this.updateTranscriptPanel(true, false, false, str2);
                    return;
                case 2:
                    PhonePBXListCoverView phonePBXListCoverView = PhonePBXListCoverView.this;
                    phonePBXListCoverView.updateTranscriptPanel(false, true, true, phonePBXListCoverView.getResources().getString(C4558R.string.zm_sip_transcribe_processing_61402));
                    return;
                case 3:
                    PhonePBXListCoverView phonePBXListCoverView2 = PhonePBXListCoverView.this;
                    phonePBXListCoverView2.updateTranscriptPanel(false, false, false, phonePBXListCoverView2.getResources().getString(C4558R.string.zm_sip_transcribe_fail_61402));
                    return;
                case 4:
                    PhonePBXListCoverView phonePBXListCoverView3 = PhonePBXListCoverView.this;
                    phonePBXListCoverView3.updateTranscriptPanel(false, false, false, phonePBXListCoverView3.getResources().getString(C4558R.string.zm_sip_transcribe_fail_61402));
                    return;
                default:
                    return;
            }
        }

        public void OnAudioFileDownloadFinished(String str, int i, int i2) {
            super.OnAudioFileDownloadFinished(str, i, i2);
            CmmSIPAudioFileItem audioFileItemByID = CmmPBXCallHistoryManager.getInstance().getAudioFileItemByID(str, i);
            PBXCallHistory callHistory = PhonePBXListCoverView.this.getCallHistory();
            if (PhonePBXListCoverView.this.isShow() && callHistory != null && callHistory.audioFile.getId().equals(str)) {
                if (audioFileItemByID != null) {
                    audioFileItemByID.toSIPAudioFileItemBean(callHistory.audioFile);
                    if (i2 == 0) {
                        PhonePBXListCoverView.this.onDownloadSuccess();
                    } else {
                        PhonePBXListCoverView.this.onDownloadFail();
                    }
                } else {
                    PhonePBXListCoverView.this.onDownloadFail();
                }
            }
        }

        public void OnAudioFileDownloadProgress(String str, int i, int i2) {
            super.OnAudioFileDownloadProgress(str, i, i2);
            PBXCallHistory callHistory = PhonePBXListCoverView.this.getCallHistory();
            if (PhonePBXListCoverView.this.isShow() && callHistory != null && callHistory.audioFile.getId().equals(str)) {
                PhonePBXListCoverView.this.setDownloadProgress(i2);
            }
        }
    };
    private View mPanelScript;
    private View mPanelScriptContent;
    private View mPanelTranscriptLoading;
    private View mSeeMore;
    private SeekBar mSeekAudioPlayer;
    private ZMSeekBar mSeekAudioPlayer2;
    private TextView mTranscript;
    private ProgressBar mTranscriptLoadingProgress;
    private TextView mTxtAudioPlayerCurrent;
    private TextView mTxtAudioPlayerTotal;
    private TextView mTxtBuddyName;
    private TextView mTxtCallNo;
    private View mTxtCallback;
    private ImageView mTxtDelete;
    private TextView mTxtRecordStartTime;
    private TextView mTxtSpeakerStatus;
    private TextView mTxtTranscriptLoading;
    /* access modifiers changed from: private */
    public int retry = 0;

    public interface CoverViewExpandMode {
        public static final int RECORDING_EXPAND = 0;
        public static final int TRANSCRIPT_EXPAND_FIRST_PHASE = 1;
        public static final int TRANSCRIPT_EXPAND_SECOND_PHASE = 2;
    }

    public void setDownloadProgress(int i) {
    }

    /* access modifiers changed from: private */
    public void setSeekUI() {
        int currentPosition = this.mMediaPlayer.getCurrentPosition();
        this.mTxtAudioPlayerCurrent.setText(TimeUtil.formateDuration((long) (currentPosition / 1000)));
        TextView textView = this.mTxtAudioPlayerCurrent;
        textView.setContentDescription(DescriptionUtil.getTimeContentDescription(textView));
        TextView textView2 = this.mTxtAudioPlayerTotal;
        StringBuilder sb = new StringBuilder();
        sb.append("-");
        sb.append(TimeUtil.formateDuration((long) ((this.mMediaPlayer.getDuration() - currentPosition) / 1000)));
        textView2.setText(sb.toString());
        TextView textView3 = this.mTxtAudioPlayerTotal;
        textView3.setContentDescription(DescriptionUtil.getTimeContentDescription(textView3));
        this.mSeekAudioPlayer.setProgress(currentPosition);
        updateSeekAudioPlayer2(currentPosition);
        if (!this.mMediaPlayer.isPlaying()) {
            this.mBtnAudioPlayer.onPause();
        } else if (!this.mBtnAudioPlayer.isPlaying()) {
            this.mBtnAudioPlayer.onPlay();
        }
    }

    public PhonePBXListCoverView(@NonNull Context context) {
        super(context);
        init();
    }

    public PhonePBXListCoverView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public PhonePBXListCoverView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public PhonePBXListCoverView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        init();
    }

    private void init() {
        ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(C4558R.layout.zm_sip_pbx_history_expand_item, this, true);
        this.mCoverContentView = findViewById(C4558R.C4560id.sip_expand_cover_content);
        this.mPanelScriptContent = findViewById(C4558R.C4560id.panelScriptContent);
        this.mPanelScript = findViewById(C4558R.C4560id.panelScript);
        this.mPanelTranscriptLoading = findViewById(C4558R.C4560id.panelTranscriptLoading);
        this.mImgOutCall = (ImageView) this.mCoverContentView.findViewById(C4558R.C4560id.imgOutCall);
        this.mTxtBuddyName = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.txtBuddyName);
        this.mSeeMore = this.mCoverContentView.findViewById(C4558R.C4560id.seeMore);
        this.mTxtCallNo = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.txtCallNo);
        this.mTranscriptLoadingProgress = (ProgressBar) this.mCoverContentView.findViewById(C4558R.C4560id.pbTranscriptLoadingProgress);
        this.mImgDeleteCall = (ImageView) this.mCoverContentView.findViewById(C4558R.C4560id.imgDeleteCall);
        this.mImgDeleteCall.setVisibility(8);
        this.mTxtRecordStartTime = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.txtRecordStartTime);
        this.mTranscript = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.transcript);
        this.mTxtSpeakerStatus = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.txtSpeakerStatus);
        this.mTxtTranscriptLoading = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.tvTranscriptLoading);
        this.mBtnAudioPlayer = (AudioPlayerControllerButton) this.mCoverContentView.findViewById(C4558R.C4560id.btnAudioPlayer);
        this.mSeekAudioPlayer = (SeekBar) this.mCoverContentView.findViewById(C4558R.C4560id.seekAudioPlayer);
        this.mSeekAudioPlayer2 = (ZMSeekBar) this.mCoverContentView.findViewById(C4558R.C4560id.seekAudioPlayer2);
        this.mTxtAudioPlayerCurrent = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.txtAudioPlayerCurrent);
        this.mTxtAudioPlayerTotal = (TextView) this.mCoverContentView.findViewById(C4558R.C4560id.txtAudioPlayerTotal);
        this.mBtnAudioShare = this.mCoverContentView.findViewById(C4558R.C4560id.btnAudioShare);
        this.mTxtDelete = (ImageView) this.mCoverContentView.findViewById(C4558R.C4560id.txtDelete);
        this.mTxtCallback = this.mCoverContentView.findViewById(C4558R.C4560id.txtCallback);
        this.mCoverContentView.setOnClickListener(this);
        this.mBtnAudioPlayer.setOnClickListener(this);
        this.mBtnAudioShare.setOnClickListener(this);
        this.mTxtCallback.setOnClickListener(this);
        this.mTxtDelete.setOnClickListener(this);
        this.mTxtSpeakerStatus.setOnClickListener(this);
        this.mSeeMore.setOnClickListener(this);
        prepareMeasureTextView();
        updateSeekAudioPlayer2(0);
        this.mMeasureWidth = UIUtil.getDisplayWidth(getContext()) - UIUtil.dip2px(getContext(), 56.0f);
        this.mMaxTranscriptExpandHeight = UIUtil.dip2px(getContext(), 200.0f);
        this.mMeasure5Height = UIUtil.dip2px(getContext(), 100.0f);
    }

    private void prepareMeasureTextView() {
        this.mMeasureTextView = new TextView(getContext());
        this.mMeasureTextView.setTextSize(0, this.mTranscript.getTextSize());
        this.mMeasureTextView.setLayoutParams(new LayoutParams(this.mMeasureWidth, -2));
        this.mMeasureTextView.setLineSpacing((float) UIUtil.sp2px(getContext(), 2.0f), 1.0f);
    }

    private int measureTranscriptHeight(CharSequence charSequence) {
        this.mMeasureTextView.setText(charSequence);
        this.mMeasureTextView.measure(MeasureSpec.makeMeasureSpec(this.mMeasureWidth, Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(0, 0));
        return this.mMeasureTextView.getMeasuredHeight();
    }

    private void initMediaPlayer(@NonNull CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean) {
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setAudioStreamType(3);
        try {
            if (isFileExist(cmmSIPAudioFileItemBean)) {
                preparePlayer(cmmSIPAudioFileItemBean.getLocalFileName());
            }
        } catch (IOException unused) {
        }
        this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                PhonePBXListCoverView.this.mHandler.removeMessages(1);
                PhonePBXListCoverView.this.mMediaPlayer.seekTo(0);
                PhonePBXListCoverView.this.setSeekUI();
            }
        });
        this.mMediaPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                return false;
            }
        });
    }

    public void setViews(View view, View view2) {
        setViews(this.mCoverContentView, view, view2);
    }

    public void bindView(@NonNull PBXCallHistory pBXCallHistory, boolean z) {
        setTag(pBXCallHistory);
        this.mAutoPlay = z;
        if (!pBXCallHistory.highLight || !pBXCallHistory.isAll) {
            this.mTxtBuddyName.setTextColor(getResources().getColor(C4558R.color.zm_call_history_name));
        } else {
            this.mTxtBuddyName.setTextColor(getResources().getColor(C4558R.color.zm_call_history_name_miss));
        }
        if (pBXCallHistory.isAll) {
            if (pBXCallHistory.isInbound) {
                this.mImgOutCall.setVisibility(4);
            } else {
                this.mImgOutCall.setVisibility(0);
                this.mImgOutCall.setImageResource(C4558R.C4559drawable.zm_ic_outgoing_call);
            }
            this.mBtnAudioShare.setContentDescription(getContext().getString(C4558R.string.zm_sip_accessbility_share_recording_67408));
        } else {
            if (pBXCallHistory.highLight) {
                this.mImgOutCall.setVisibility(0);
                this.mImgOutCall.setImageResource(C4558R.C4559drawable.zm_unread_voicemail);
            } else {
                this.mImgOutCall.setVisibility(4);
            }
            this.mBtnAudioShare.setContentDescription(getContext().getString(C4558R.string.zm_sip_accessbility_share_voicemail_67408));
        }
        updateSpeaker(false);
        boolean z2 = true;
        this.mTxtCallback.setEnabled(!pBXCallHistory.isRestricted);
        this.mTxtBuddyName.setText(pBXCallHistory.displayName);
        this.mTxtCallNo.setText(pBXCallHistory.displayNumber);
        this.mTxtCallNo.setContentDescription(DescriptionUtil.getPhoneNumberContentDescription(pBXCallHistory.number));
        this.mTxtRecordStartTime.setText(getRecordStartTime(pBXCallHistory.createTime));
        this.mImgDeleteCall.setOnClickListener(this);
        this.mImgDeleteCall.setTag(pBXCallHistory.f349id);
        initDuration();
        if (pBXCallHistory.isAll) {
            this.mPanelScript.setVisibility(8);
            setDynamicHeight(0);
        } else {
            this.mPanelScript.setVisibility(0);
            setDynamicHeight(1);
            CmmSIPVoiceMailItem voiceMailItemByID = CmmPBXCallHistoryManager.getInstance().getVoiceMailItemByID(pBXCallHistory.f349id);
            if (voiceMailItemByID != null) {
                String transcript = voiceMailItemByID.getTranscript();
                if (!TextUtils.isEmpty(transcript)) {
                    updateTranscriptPanel(true, false, false, transcript);
                } else if (getTag() != null) {
                    String str = ((PBXCallHistory) getTag()).f349id;
                    if (!TextUtils.isEmpty(str)) {
                        CmmPBXCallHistoryManager.getInstance().requestForVoiceMailTranscript(str);
                    }
                } else {
                    return;
                }
            }
        }
        if (pBXCallHistory.audioFile == null || !pBXCallHistory.audioFile.isFileDownloading()) {
            z2 = false;
        }
        if (z2) {
            this.mBtnAudioPlayer.onLoading();
        } else {
            this.mBtnAudioPlayer.onPause();
            if (isFileExist(pBXCallHistory.audioFile)) {
                setDownloadProgress(100);
            } else {
                setDownloadProgress(0);
            }
        }
        if (this.mMediaPlayer == null) {
            initMediaPlayer(pBXCallHistory.audioFile);
        }
        HeadsetUtil.getInstance().addListener(this);
        CmmPBXCallHistoryManager.getInstance().addISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
    }

    private String getDate(long j) {
        if (DateUtils.isToday(j)) {
            return getContext().getString(C4558R.string.zm_today_85318);
        }
        if (TimeUtil.isYesterday(j)) {
            return getContext().getString(C4558R.string.zm_yesterday_85318);
        }
        return DateUtils.formatDateTime(getContext(), j, 131092);
    }

    private String getTime(long j) {
        return TimeUtil.formatTime(getContext(), j);
    }

    private String getRecordStartTime(long j) {
        long j2 = j * 1000;
        StringBuilder sb = new StringBuilder();
        sb.append(getDate(j2));
        sb.append(" , ");
        sb.append(getTime(j2));
        return sb.toString();
    }

    private void initDuration() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null || !this.mIsMediaPrepared) {
            PBXCallHistory pBXCallHistory = (PBXCallHistory) getTag();
            if (pBXCallHistory != null) {
                this.mSeekAudioPlayer.setMax(pBXCallHistory.audioFile.getFileDuration() * 1000);
                this.mSeekAudioPlayer.setProgress(0);
                updateSeekAudioPlayer2(0);
                TextView textView = this.mTxtAudioPlayerTotal;
                StringBuilder sb = new StringBuilder();
                sb.append("-");
                sb.append(TimeUtil.formateDuration((long) pBXCallHistory.audioFile.getFileDuration()));
                textView.setText(sb.toString());
                this.mTxtAudioPlayerCurrent.setText("00:00");
            } else {
                return;
            }
        } else {
            int currentPosition = mediaPlayer.getCurrentPosition();
            this.mTxtAudioPlayerCurrent.setText(TimeUtil.formateDuration((long) (currentPosition / 1000)));
            TextView textView2 = this.mTxtAudioPlayerTotal;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("-");
            sb2.append(TimeUtil.formateDuration((long) ((this.mMediaPlayer.getDuration() - currentPosition) / 1000)));
            textView2.setText(sb2.toString());
            this.mSeekAudioPlayer.setMax(this.mMediaPlayer.getDuration());
            this.mSeekAudioPlayer.setProgress(0);
            updateSeekAudioPlayer2(0);
        }
        TextView textView3 = this.mTxtAudioPlayerCurrent;
        textView3.setContentDescription(DescriptionUtil.getTimeContentDescription(textView3));
        TextView textView4 = this.mTxtAudioPlayerTotal;
        textView4.setContentDescription(DescriptionUtil.getTimeContentDescription(textView4));
    }

    private void updateSeekAudioPlayer2(int i) {
        if (this.mSeekAudioPlayer2.getOnProgressChangedListener() == null) {
            this.mSeekAudioPlayer2.setOnProgressChangedListener(new OnProgressChangedListener() {
                public void getProgressOnActionUp(ZMSeekBar zMSeekBar, int i, float f) {
                }

                public void onProgressChanged(ZMSeekBar zMSeekBar, int i, float f) {
                }

                public void onProgressChangedWhenMoving(ZMSeekBar zMSeekBar, int i, float f) {
                    PhonePBXListCoverView.this.mMediaPlayer.seekTo(i);
                    PhonePBXListCoverView.this.setSeekUI();
                }
            });
        }
        PBXCallHistory callHistory = getCallHistory();
        if (callHistory != null) {
            this.mSeekAudioPlayer2.setEnabled(isFileExist(callHistory.audioFile));
            this.mSeekAudioPlayer2.setmMax((float) (callHistory.audioFile.getFileDuration() * 1000));
        } else {
            this.mSeekAudioPlayer2.setEnabled(false);
        }
        this.mSeekAudioPlayer2.setProgress((float) i);
    }

    /* access modifiers changed from: private */
    public void updateTranscriptPanel(boolean z, boolean z2, boolean z3, String str) {
        if (z3) {
            this.mHandler.sendEmptyMessageDelayed(2, 15000);
        } else if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        int i = 8;
        this.mPanelTranscriptLoading.setVisibility(z ? 8 : 0);
        this.mPanelScriptContent.setVisibility(z ? 0 : 8);
        if (z) {
            this.mTranscript.setText(str);
            int measureTranscriptHeight = measureTranscriptHeight(str);
            this.mTranscript.setHeight(this.mMeasure5Height);
            View view = this.mSeeMore;
            if (measureTranscriptHeight > this.mMeasure5Height) {
                i = 0;
            }
            view.setVisibility(i);
            return;
        }
        this.mTxtTranscriptLoading.setText(str);
        ProgressBar progressBar = this.mTranscriptLoadingProgress;
        if (z2) {
            i = 0;
        }
        progressBar.setVisibility(i);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        PBXCallHistory callHistory = getCallHistory();
        if (id == C4558R.C4560id.btnAudioPlayer) {
            this.mAutoPlay = true;
            if (callHistory != null) {
                btnAudioPlayerClicked(callHistory);
            }
        } else if (id == C4558R.C4560id.btnAudioShare) {
            if (callHistory != null) {
                shareAudioFile(new File(callHistory.audioFile.getLocalFileName()));
            }
        } else if (id == C4558R.C4560id.txtCallback) {
            if (isPlayingAudio()) {
                pausePlayAudio();
                this.mBtnAudioPlayer.onPause();
            }
            if (this.mListView instanceof PhonePBXHistoryListView) {
                if (callHistory != null) {
                    ((PhonePBXHistoryListView) this.mListView).sendSipCallWithCheckError(callHistory.number, callHistory.displayName);
                    if (callHistory.highLight) {
                        CmmPBXCallHistoryManager.getInstance().clearMissedCallHistory();
                    }
                }
            } else if ((this.mListView instanceof PhonePBXVoiceMailListView) && callHistory != null) {
                ((PhonePBXVoiceMailListView) this.mListView).sendSipCallWithCheckError(callHistory.number, callHistory.displayName);
            }
        } else if (id == C4558R.C4560id.txtDelete) {
            if (CmmSIPCallManager.getInstance().checkNetwork(getContext())) {
                dismiss();
                if ((this.mListView instanceof PhonePBXHistoryListView) && callHistory != null) {
                    ((PhonePBXHistoryListView) this.mListView).delete(callHistory.f349id, true);
                    ((PhonePBXHistoryListView) this.mListView).checkDelete();
                } else if ((this.mListView instanceof PhonePBXVoiceMailListView) && callHistory != null) {
                    ((PhonePBXVoiceMailListView) this.mListView).delete(callHistory.f349id, true);
                    ((PhonePBXVoiceMailListView) this.mListView).checkDeleteVoiceMails();
                }
            }
        } else if (id == C4558R.C4560id.txtSpeakerStatus) {
            updateSpeaker(true);
        } else if (id == C4558R.C4560id.seeMore) {
            setDynamicHeight(2);
            this.mSeeMore.setVisibility(8);
            setHideAlpha(100);
            setShowAlpha(100);
            super.start();
        }
    }

    public void start() {
        super.start();
        btnAudioPlayerClicked((PBXCallHistory) getTag());
    }

    public boolean tryToDownloadAudio(@NonNull PBXCallHistory pBXCallHistory) {
        if (pBXCallHistory.audioFile.isFileDownloading()) {
            this.mBtnAudioPlayer.onLoading();
            return false;
        } else if (isPlayingAudio()) {
            pausePlayAudio();
            this.mBtnAudioPlayer.onPause();
            return false;
        } else if (isFileExist(pBXCallHistory.audioFile)) {
            return true;
        } else {
            CmmPBXCallHistoryManager.getInstance().requestDownloadAudioFile(pBXCallHistory.audioFile.getId(), pBXCallHistory.isAll ^ true ? 1 : 0);
            pBXCallHistory.audioFile.setFileDownloading(true);
            this.mBtnAudioPlayer.onLoading();
            updateSeekAudioPlayer2(0);
            return false;
        }
    }

    private void btnAudioPlayerClicked(@NonNull PBXCallHistory pBXCallHistory) {
        if (tryToDownloadAudio(pBXCallHistory)) {
            startPlay();
        }
    }

    private void shareAudioFile(@NonNull File file) {
        if (getCallHistory() == null || !isFileExist(getCallHistory().audioFile)) {
            Toast.makeText(getContext(), C4558R.string.zm_sip_audio_downloading_warn_61381, 0).show();
        } else {
            AndroidAppUtil.shareFile(getContext(), file);
        }
    }

    public boolean isShow() {
        return getVisibility() == 0;
    }

    public void dismissSmooth() {
        onDismiss();
        super.dismissSmooth();
    }

    public void dismiss() {
        onDismiss();
        super.dismiss();
    }

    /* access modifiers changed from: protected */
    public void onAnimationEnded() {
        super.onAnimationEnded();
        if (!this.mShow) {
            onDismiss();
        } else {
            requestCoverFocusForAccessibility(1000);
        }
    }

    private void onDismiss() {
        if (this.mHandler.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        this.retry = 0;
        stopPlayAudio();
        releasePlayer();
        CmmPBXCallHistoryManager.getInstance().removeISIPCallRepositoryEventSinkListener(this.mPBXRepositoryListener);
        HeadsetUtil.getInstance().removeListener(this);
    }

    @Nullable
    public PBXCallHistory getCallHistory() {
        return (PBXCallHistory) getTag();
    }

    public void onDownloadSuccess() {
        setDownloadProgress(100);
        PBXCallHistory callHistory = getCallHistory();
        if (callHistory == null || !isFileExist(callHistory.audioFile)) {
            this.mBtnAudioPlayer.onPause();
            return;
        }
        startPlay();
        updateSeekAudioPlayer2(0);
    }

    public void onDownloadFail() {
        PBXCallHistory callHistory = getCallHistory();
        if (callHistory != null) {
            int i = C4558R.string.zm_sip_recording_download_failed_27110;
            if (!callHistory.isAll) {
                i = C4558R.string.zm_sip_voice_mail_download_failed_27110;
            }
            Toast.makeText(getContext(), i, 1).show();
            requestCoverFocusForAccessibility(5000);
        }
        setDownloadProgress(0);
        this.mBtnAudioPlayer.onPause();
        updateSeekAudioPlayer2(0);
    }

    public void requestCoverFocusForAccessibility(long j) {
        if (AccessibilityUtil.isSpokenFeedbackEnabled(getContext())) {
            postDelayed(new Runnable() {
                public void run() {
                    if (PhonePBXListCoverView.this.isShow()) {
                        PhonePBXListCoverView.this.mCoverContentView.sendAccessibilityEvent(8);
                    }
                }
            }, j);
        }
    }

    private void startPlay() {
        if (startPlayAudio()) {
            this.mBtnAudioPlayer.onPlay();
        } else {
            this.mBtnAudioPlayer.onPause();
        }
    }

    private boolean isFileExist(CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean) {
        String localFileName = cmmSIPAudioFileItemBean.getLocalFileName();
        boolean z = false;
        if (!cmmSIPAudioFileItemBean.isFileInLocal()) {
            return false;
        }
        File file = new File(localFileName);
        if (file.exists() && file.length() > 0) {
            z = true;
        }
        return z;
    }

    private boolean startPlayAudio() {
        if (!this.mIsMediaPrepared) {
            try {
                preparePlayer();
            } catch (IOException unused) {
            }
        }
        if (!this.mIsMediaPrepared || !this.mAutoPlay) {
            return false;
        }
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            return false;
        }
        mediaPlayer.start();
        this.mHandler.sendEmptyMessageDelayed(1, 200);
        if (this.mListView instanceof PhonePBXVoiceMailListView) {
            PBXCallHistory callHistory = getCallHistory();
            if (callHistory != null && !callHistory.isAll && callHistory.highLight) {
                callHistory.highLight = false;
                this.mTxtBuddyName.setTextColor(getResources().getColor(C4558R.color.zm_call_history_name));
                this.mImgOutCall.setVisibility(4);
                ((PhonePBXVoiceMailListView) this.mListView).changeVoiceMailStatusToRead(callHistory.f349id);
            }
        }
        return true;
    }

    private void preparePlayer() throws IOException {
        preparePlayer(getCallHistory().audioFile.getLocalFileName());
    }

    private void preparePlayer(String str) throws IOException {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            if (!this.mIsMediaPrepared) {
                mediaPlayer.setDataSource(str);
                this.mMediaPlayer.prepare();
                this.mIsMediaPrepared = true;
            }
            initDuration();
        }
    }

    public void setPlayAudioPause() {
        if (isPlayingAudio()) {
            pausePlayAudio();
            this.mBtnAudioPlayer.onPause();
        }
    }

    private void pausePlayAudio() {
        this.mHandler.removeMessages(1);
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }

    private void stopPlayAudio() {
        if (this.mIsMediaPrepared && this.mMediaPlayer != null) {
            this.mHandler.removeMessages(1);
            this.mMediaPlayer.stop();
        }
        this.mIsMediaPrepared = false;
    }

    private void releasePlayer() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        this.mMediaPlayer = null;
    }

    private void updateSpeaker(boolean z) {
        if (!z && HeadsetUtil.getInstance().isWiredHeadsetOn()) {
            if (isSpeakerOn()) {
                setSpeakerOnMode();
            } else {
                setSpeakerOffMode();
            }
        } else if (HeadsetUtil.getInstance().isBluetoothHeadsetOn()) {
            changeToEarpieceMode();
            this.mTxtSpeakerStatus.setTextColor(getResources().getColor(C4558R.color.zm_white));
            this.mTxtSpeakerStatus.setBackgroundResource(C4558R.C4559drawable.zm_btn_add_buddy_invite);
            this.mTxtSpeakerStatus.setText(C4558R.string.zm_btn_bluetooth_61381);
            this.mTxtSpeakerStatus.setContentDescription(getResources().getString(C4558R.string.zm_btn_bluetooth_61381));
        } else {
            if (z != isSpeakerOn()) {
                setSpeakerOnMode();
                if (!isSpeakerOn()) {
                    changeToSpeakerMode();
                }
            } else {
                setSpeakerOffMode();
                if (isSpeakerOn()) {
                    changeToEarpieceMode();
                }
            }
        }
    }

    private void setSpeakerOnMode() {
        this.mTxtSpeakerStatus.setText(C4558R.string.zm_btn_speaker_61381);
        this.mTxtSpeakerStatus.setContentDescription(getResources().getString(C4558R.string.zm_mi_speaker_phone));
        this.mTxtSpeakerStatus.setTextColor(getResources().getColor(C4558R.color.zm_white));
        this.mTxtSpeakerStatus.setBackgroundResource(C4558R.C4559drawable.zm_btn_add_buddy_invite);
    }

    private void setSpeakerOffMode() {
        this.mTxtSpeakerStatus.setBackgroundColor(getResources().getColor(C4558R.color.zm_transparent));
        this.mTxtSpeakerStatus.setTextColor(getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB));
        this.mTxtSpeakerStatus.setText(C4558R.string.zm_btn_speaker_61381);
        this.mTxtSpeakerStatus.setContentDescription(getResources().getString(C4558R.string.zm_mi_ear_phone));
    }

    private boolean isPlayingAudio() {
        if (this.mIsMediaPrepared) {
            MediaPlayer mediaPlayer = this.mMediaPlayer;
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    private void resumePlayAudio() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        this.mHandler.sendEmptyMessageDelayed(1, 200);
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        updateSpeaker(false);
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        updateSpeaker(false);
    }

    private void changeToEarpieceMode() {
        getAudioManager().setSpeakerphoneOn(false);
        getAudioManager().setMode(3);
    }

    private void changeToSpeakerMode() {
        if (HeadsetUtil.getInstance().isWiredHeadsetOn()) {
            AudioManager audioManager2 = this.audioManager;
            if (audioManager2 != null) {
                audioManager2.setMicrophoneMute(false);
            }
        }
        getAudioManager().setSpeakerphoneOn(true);
        getAudioManager().setMode(3);
    }

    private boolean isSpeakerOn() {
        return getAudioManager().isSpeakerphoneOn();
    }

    @NonNull
    private AudioManager getAudioManager() {
        if (this.audioManager == null) {
            this.audioManager = (AudioManager) VideoBoxApplication.getInstance().getSystemService("audio");
        }
        return this.audioManager;
    }

    public void setDynamicHeight(int i) {
        switch (i) {
            case 0:
                int measuredHeight = this.mSelectListItemView.getMeasuredHeight();
                setExpandedHeight(getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_phone_call_normal_expand_item_height));
                setCollapsedHeight(measuredHeight);
                return;
            case 1:
                setExpandedHeight(getResources().getDimensionPixelSize(C4558R.dimen.zm_sip_phone_call_expand_item_height));
                setCollapsedHeight(this.mSelectListItemView.getMeasuredHeight());
                return;
            case 2:
                int measureTranscriptHeight = measureTranscriptHeight(this.mTranscript.getText());
                int i2 = this.mMaxTranscriptExpandHeight;
                if (measureTranscriptHeight > i2) {
                    measureTranscriptHeight = i2;
                }
                this.mTranscript.setHeight(measureTranscriptHeight);
                int measuredHeight2 = getMeasuredHeight();
                setExpandedHeight((measureTranscriptHeight + measuredHeight2) - (this.mMaxTranscriptExpandHeight / 2));
                setCollapsedHeight(measuredHeight2);
                return;
            default:
                return;
        }
    }
}
