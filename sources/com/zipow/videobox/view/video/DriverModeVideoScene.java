package com.zipow.videobox.view.video;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.GLButton;
import com.zipow.videobox.confapp.GLImage;
import com.zipow.videobox.confapp.RendererUnitInfo;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.meeting.ConfUserInfoEvent;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.TextDrawable;
import java.util.List;
import p021us.zoom.androidlib.util.HeadsetUtil.IHeadsetConnectionListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class DriverModeVideoScene extends AbsVideoScene implements OnClickListener, GLImage.OnClickListener, GLButton.OnClickListener, IHeadsetConnectionListener {
    private static final int ACTIVE_SPEAKER_BOTTOM_MAX_MARGIN = 60;
    private static int ACTIVE_SPEAKER_TEXT_COLOR = 0;
    private static final int ACTIVE_SPEAKER_TEXT_SIZE = 16;
    private static int BTN_DONE_SPEAK_TEXT_COLOR = 0;
    private static final int BTN_SIZE = 170;
    private static int BTN_TAP_SPEAK_TEXT_COLOR = 0;
    private static final int BTN_TEXT_SIZE = 30;
    private static final int INDEX_ACCESSIBILITY_VIEW_AUDIO_SOURCE = 1;
    private static final int INDEX_ACCESSIBILITY_VIEW_LEAVE = 0;
    private static final int LINE_BOTTOM_SPACE_LANDSCAPE = 5;
    private static final int LINE_BOTTOM_SPACE_PORTRAIT = 50;
    private static final int LINE_BUTTON_GAP_MIN = 10;
    private static int LINE_COLOR = 0;
    private static final int LINE_STROKE = 1;
    private static final int MESSAGES_GAP = 3;
    private static int MESSAGE_TEXT_COLOR_MUTED = 0;
    private static int MESSAGE_TEXT_COLOR_UNMUTED = 0;
    private static final int MESSAGE_TEXT_SIZE = 16;
    private static final int SPEAKER_NAME_DISPLAY_TIMEOUT = 2000;
    private static final String TAG = "DriverModeVideoScene";
    private static final int TEXT_PADDING_VERTICAL = 2;
    private static final int TITLE_HORIZONTAL_MARGIN_MIN = 160;
    private static final int TITLE_LINE_GAP = 3;
    private static int TITLE_MARGIN_TOP = 0;
    private static int TITLE_MARGIN_TOP_FOR_TRANSLUCENT_STATUS = 0;
    private static int TITLE_TEXT_COLOR = 0;
    private static final int TITLE_TEXT_SIZE = 48;
    private static final int TITLE_TOP_MARGIN_LANDSCAPE = 2;
    private static final int TITLE_TOP_MARGIN_PORTRAIT = 15;
    @Nullable
    private Bitmap mActiveSpeakerBitmap;
    /* access modifiers changed from: private */
    @Nullable
    public String mActiveSpeakerName = null;
    private TextPaint mActiveSpeakerTextPaint;
    @Nullable
    private Bitmap mAudioMessageBitmap;
    private int mAudioSourceType = -1;
    private TextPaint mBtnTextPaint;
    @Nullable
    private Drawable mDrawableBtnLeave;
    @Nullable
    private Drawable mDrawableSwitchAudioSource;
    @Nullable
    private GLButton mGLBtnLeave;
    @Nullable
    private GLImage mGLBtnMuteUnmute;
    @Nullable
    private GLButton mGLBtnSwitchAudioSource;
    /* access modifiers changed from: private */
    @Nullable
    public GLImage mGLImgActiveSpeaker;
    @Nullable
    private GLImage mGLImgAudioMessage;
    @Nullable
    private GLImage mGLImgLine;
    @Nullable
    private GLImage mGLImgTitle;
    @Nullable
    private GLImage mGLImgVideoMessage;
    @NonNull
    private Handler mHandler = new Handler();
    private boolean mIsHost = false;
    private boolean mIsMicMuted = true;
    /* access modifiers changed from: private */
    public boolean mIsVoIP = false;
    private float mLastDownX = -1.0f;
    private float mLastDownY = -1.0f;
    @Nullable
    private Bitmap mLineBitmap;
    private TextPaint mMessageTextPaint;
    @Nullable
    private Bitmap mMuteUnmuteBtnBitmap;
    private int mResIdSwitchAudioSource = 0;
    @Nullable
    private Runnable mRunnableHideActiveSpeaker = new Runnable() {
        public void run() {
            if (!(DriverModeVideoScene.this.mGLImgActiveSpeaker == null || ConfMgr.getInstance().getVideoObj() == null)) {
                DriverModeVideoScene.this.mGLImgActiveSpeaker.setVisible(false);
                DriverModeVideoScene.this.mActiveSpeakerName = null;
            }
        }
    };
    private ImageButton[] mSwitchSceneButtons;
    @Nullable
    private Bitmap mTitleBitmap;
    private int mTitleTextHeight;
    private TextPaint mTitleTextPaint;
    private Typeface mTypeface;
    @Nullable
    private Bitmap mVideoMessageBitmap;
    private boolean mbConnectAudioManual = false;
    private boolean mbPressed = false;
    private boolean mbUIMuteStatus = true;
    private boolean mbVideoOnPrevDriveMode = false;
    private long mlResumeTime = 0;

    public DriverModeVideoScene(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        super(absVideoSceneMgr);
        initDefaultResources();
    }

    public void setIsVideoOnPrevDrivingMode(boolean z) {
        this.mbVideoOnPrevDriveMode = z;
    }

    public void initDefaultResources() {
        VideoBoxApplication videoBoxApplication = this.mSceneMgr.getmVideoBoxApplication();
        Resources resources = videoBoxApplication.getResources();
        if (resources != null) {
            MESSAGE_TEXT_COLOR_MUTED = resources.getColor(C4558R.color.zm_white);
            MESSAGE_TEXT_COLOR_UNMUTED = resources.getColor(C4558R.color.zm_drivermode_text_color_highlight);
            TITLE_TEXT_COLOR = resources.getColor(C4558R.color.zm_white);
            LINE_COLOR = 939524095;
            BTN_TAP_SPEAK_TEXT_COLOR = resources.getColor(C4558R.color.zm_drivermode_text_color_highlight);
            BTN_DONE_SPEAK_TEXT_COLOR = resources.getColor(C4558R.color.zm_white);
            ACTIVE_SPEAKER_TEXT_COLOR = resources.getColor(C4558R.color.zm_white);
            TITLE_MARGIN_TOP_FOR_TRANSLUCENT_STATUS = UIUtil.getStatusBarHeight(videoBoxApplication);
        }
        this.mTitleTextPaint = new TextPaint();
        this.mTypeface = new TextView(videoBoxApplication).getTypeface();
        this.mTitleTextPaint.setTypeface(this.mTypeface);
        this.mTitleTextPaint.setTextSize((float) UIUtil.sp2px(videoBoxApplication, 48.0f));
        this.mTitleTextPaint.setColor(TITLE_TEXT_COLOR);
        this.mTitleTextPaint.setAntiAlias(true);
        FontMetrics fontMetrics = this.mTitleTextPaint.getFontMetrics();
        this.mTitleTextHeight = ((int) (fontMetrics.bottom - fontMetrics.top)) + UIUtil.dip2px(videoBoxApplication, 4.0f);
        this.mMessageTextPaint = new TextPaint();
        this.mMessageTextPaint.setTypeface(this.mTypeface);
        this.mMessageTextPaint.setTextSize((float) UIUtil.sp2px(videoBoxApplication, 16.0f));
        this.mMessageTextPaint.setAntiAlias(true);
        this.mBtnTextPaint = new TextPaint();
        this.mBtnTextPaint.setTypeface(this.mTypeface);
        this.mBtnTextPaint.setTextSize((float) UIUtil.sp2px(videoBoxApplication, 30.0f));
        this.mBtnTextPaint.setAntiAlias(true);
        this.mActiveSpeakerTextPaint = new TextPaint();
        this.mActiveSpeakerTextPaint.setTypeface(this.mTypeface);
        this.mActiveSpeakerTextPaint.setTextSize((float) UIUtil.sp2px(videoBoxApplication, 16.0f));
        this.mActiveSpeakerTextPaint.setColor(ACTIVE_SPEAKER_TEXT_COLOR);
        this.mActiveSpeakerTextPaint.setAntiAlias(true);
    }

    /* access modifiers changed from: protected */
    public void onCreateUnits() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            createTitleImage(videoObj);
            createLineImage(videoObj);
            createAudioMessageImage(videoObj);
            createVideoMessageImage(videoObj);
            createMuteUnmuteButton(videoObj);
            createActiveSpeakerImage(videoObj);
            createSwitchAudioSourceButton(videoObj);
            createLeaveButton(videoObj);
            if (isVisible()) {
                positionSwitchScenePanel();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onUpdateUnits() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            ConfActivity confActivity = getConfActivity();
            if (confActivity == null || !confActivity.isImmersedModeEnabled() || !UIUtil.isPortraitMode(this.mSceneMgr.getmVideoBoxApplication())) {
                TITLE_MARGIN_TOP = 0;
            } else {
                TITLE_MARGIN_TOP = TITLE_MARGIN_TOP_FOR_TRANSLUCENT_STATUS;
            }
            updateTitleImage(videoObj);
            updateLineImage(videoObj);
            updateAudioMessageImage(videoObj);
            updateVideoMessageImage(videoObj);
            updateMuteUnmuteButton(videoObj);
            updateActiveSpeakerImage(videoObj);
            updateSwitchAudioSourceButton();
            updateLeaveButton();
            if (isVisible()) {
                positionSwitchScenePanel();
                updateAccessibilitySceneDescription();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroyUnits() {
        this.mGLBtnMuteUnmute = null;
        this.mGLImgTitle = null;
        this.mGLImgAudioMessage = null;
        this.mGLImgVideoMessage = null;
        this.mGLImgLine = null;
        this.mGLImgActiveSpeaker = null;
        this.mGLBtnSwitchAudioSource = null;
        this.mGLBtnLeave = null;
        this.mTitleBitmap = null;
        this.mLineBitmap = null;
        this.mActiveSpeakerBitmap = null;
        this.mAudioMessageBitmap = null;
        this.mVideoMessageBitmap = null;
        this.mMuteUnmuteBtnBitmap = null;
        this.mDrawableSwitchAudioSource = null;
        this.mDrawableBtnLeave = null;
        this.mbVideoOnPrevDriveMode = false;
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        updateSwitchScenePanel();
        if (!isVideoPaused()) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null) {
                    this.mIsVoIP = audioStatusObj.getAudiotype() == 0;
                }
                updateMyAudioStatus(myself.getNodeId());
            }
            updateSwitchAudioSourceButton();
        }
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        this.mIsMicMuted = true;
        if (this.mGLBtnMuteUnmute != null) {
            Bitmap muteButtonBitmap = getMuteButtonBitmap(true);
            if (muteButtonBitmap != null) {
                this.mGLBtnMuteUnmute.setBackground(muteButtonBitmap);
            }
        }
        Runnable runnable = this.mRunnableHideActiveSpeaker;
        if (runnable != null) {
            runnable.run();
        }
    }

    public void onGLRendererChanged(VideoRenderer videoRenderer, int i, int i2) {
        this.mTitleBitmap = null;
        this.mLineBitmap = null;
        this.mActiveSpeakerBitmap = null;
        this.mAudioMessageBitmap = null;
        this.mVideoMessageBitmap = null;
        this.mMuteUnmuteBtnBitmap = null;
        this.mDrawableSwitchAudioSource = null;
        this.mDrawableBtnLeave = null;
        super.onGLRendererChanged(videoRenderer, i, i2);
    }

    public void onAutoStartVideo() {
        updateSwitchScenePanel();
    }

    public void onUserCountChangesForShowHideAction() {
        if (!isPreloadStatus()) {
            updateSwitchScenePanel();
        }
    }

    public void onGroupUserEvent(int i, List<ConfUserInfoEvent> list) {
        if (!isPreloadStatus()) {
            updateSwitchScenePanel();
        }
    }

    public void onUserAudioStatus(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null) {
                    this.mIsVoIP = audioStatusObj.getAudiotype() == 0;
                }
            }
            if (!isVideoPaused()) {
                updateMyAudioStatus(j);
                updateSwitchAudioSourceButton();
            }
        }
    }

    public void onAudioTypeChanged(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null) {
                    this.mIsVoIP = audioStatusObj.getAudiotype() == 0;
                    if (this.mIsVoIP && audioStatusObj.getIsMuted()) {
                        ConfActivity confActivity = getConfActivity();
                        if (confActivity != null && this.mbConnectAudioManual) {
                            confActivity.muteAudio(false);
                        }
                    }
                }
                this.mbConnectAudioManual = false;
            }
            if (!isVideoPaused()) {
                updateMyAudioStatus(j);
                updateSwitchAudioSourceButton();
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateMyAudioStatus(long j) {
        ConfMgr instance = ConfMgr.getInstance();
        CmmConfStatus confStatusObj = instance.getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            CmmUser myself = instance.getMyself();
            if (myself != null) {
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj != null) {
                    boolean isMuted = audioStatusObj.getIsMuted();
                    if (this.mIsMicMuted != isMuted) {
                        this.mIsMicMuted = isMuted;
                        this.mAudioMessageBitmap = null;
                        this.mMuteUnmuteBtnBitmap = null;
                        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                        updateAudioMessageImage(videoObj);
                        updateVideoMessageImage(videoObj);
                        updateMuteUnmuteButton(videoObj);
                        updateActiveSpeakerImage(videoObj);
                        getVideoSceneMgr().announceAccessibilityAtActiveScene();
                    }
                }
            }
        }
    }

    public void onUserActiveAudio(long j) {
        if (!isVideoPaused()) {
            ConfMgr instance = ConfMgr.getInstance();
            String talkingUserName = instance.getTalkingUserName();
            if (StringUtil.isEmptyOrNull(talkingUserName)) {
                this.mActiveSpeakerName = null;
                return;
            }
            CmmUser myself = instance.getMyself();
            if (myself != null) {
                if (talkingUserName.contains(StringUtil.safeString(myself.getScreenName())) && isUIMuteStatus() && System.currentTimeMillis() - this.mlResumeTime < 3000) {
                    return;
                }
                if (StringUtil.isSameString(talkingUserName, this.mActiveSpeakerName)) {
                    Runnable runnable = this.mRunnableHideActiveSpeaker;
                    if (runnable != null) {
                        this.mHandler.removeCallbacks(runnable);
                        this.mHandler.postDelayed(this.mRunnableHideActiveSpeaker, 2000);
                    }
                    return;
                }
                this.mActiveSpeakerName = talkingUserName;
                if (this.mActiveSpeakerBitmap != null) {
                    this.mActiveSpeakerBitmap = null;
                }
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    GLImage gLImage = this.mGLImgActiveSpeaker;
                    if (gLImage != null) {
                        gLImage.setVisible(true);
                        updateActiveSpeakerImage(videoObj);
                    }
                }
            }
        }
    }

    private boolean isUIMuteStatus() {
        return this.mbUIMuteStatus;
    }

    public void onShareActiveUser(long j) {
        updateSwitchScenePanel();
    }

    public void onHostChanged(long j, boolean z) {
        boolean z2 = this.mIsHost;
        this.mIsHost = z;
        if (z2 != this.mIsHost) {
            this.mDrawableBtnLeave = null;
            updateLeaveButton();
        }
    }

    /* access modifiers changed from: protected */
    public void onResumeVideo() {
        updateSwitchScenePanel();
        this.mlResumeTime = System.currentTimeMillis();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (!DriverModeVideoScene.this.isVideoPaused() && DriverModeVideoScene.this.isVisible() && DriverModeVideoScene.this.isStarted()) {
                    CmmUser myself = ConfMgr.getInstance().getMyself();
                    if (myself != null) {
                        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                        if (audioStatusObj != null) {
                            DriverModeVideoScene.this.mIsVoIP = audioStatusObj.getAudiotype() == 0;
                        }
                        DriverModeVideoScene.this.updateMyAudioStatus(myself.getNodeId());
                    }
                    DriverModeVideoScene.this.updateSwitchAudioSourceButton();
                }
            }
        }, 300);
    }

    /* access modifiers changed from: protected */
    public void onDraggingIn() {
        super.onDraggingIn();
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            setIsVideoOnPrevDrivingMode(confContext.isVideoOn());
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                boolean z = this.mIsHost;
                this.mIsHost = myself.isHost();
                if (z != this.mIsHost) {
                    this.mDrawableBtnLeave = null;
                    updateLeaveButton();
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onNetworkRestrictionModeChanged(boolean z) {
        updateSwitchScenePanel();
    }

    private void createMuteUnmuteButton(@NonNull VideoSessionMgr videoSessionMgr) {
        Bitmap muteButtonBitmap = getMuteButtonBitmap(true);
        if (muteButtonBitmap != null) {
            this.mGLBtnMuteUnmute = videoSessionMgr.createGLImage(createMuteUnmuteButtonUnitInfo(muteButtonBitmap));
            GLImage gLImage = this.mGLBtnMuteUnmute;
            if (gLImage != null) {
                gLImage.setUnitName("MuteUnmuteButton");
                this.mGLBtnMuteUnmute.setVideoScene(this);
                addUnit(this.mGLBtnMuteUnmute);
                this.mGLBtnMuteUnmute.onCreate();
                this.mGLBtnMuteUnmute.setBackground(muteButtonBitmap);
                this.mGLBtnMuteUnmute.setOnClickListener(this);
                this.mGLBtnMuteUnmute.setVisible(true);
            }
        }
    }

    private void updateMuteUnmuteButton(VideoSessionMgr videoSessionMgr) {
        if (this.mGLBtnMuteUnmute != null) {
            Bitmap muteButtonBitmap = getMuteButtonBitmap(false);
            if (muteButtonBitmap != null) {
                RendererUnitInfo createMuteUnmuteButtonUnitInfo = createMuteUnmuteButtonUnitInfo(muteButtonBitmap);
                this.mGLBtnMuteUnmute.setBackground(muteButtonBitmap);
                this.mGLBtnMuteUnmute.updateUnitInfo(createMuteUnmuteButtonUnitInfo);
                this.mGLBtnMuteUnmute.setVisible(true);
                if (isVisible()) {
                    getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(this.mSceneMgr.getmVideoBoxApplication().getString(this.mIsMicMuted ? C4558R.string.zm_description_tap_speak : C4558R.string.zm_description_done_speaking));
                }
            }
        }
    }

    private void createTitleImage(@NonNull VideoSessionMgr videoSessionMgr) {
        Bitmap titleImageBitmap = getTitleImageBitmap();
        if (titleImageBitmap != null) {
            this.mGLImgTitle = videoSessionMgr.createGLImage(createTitleImageUnitInfo(titleImageBitmap));
            GLImage gLImage = this.mGLImgTitle;
            if (gLImage != null) {
                gLImage.setUnitName("Title");
                this.mGLImgTitle.setVideoScene(this);
                addUnit(this.mGLImgTitle);
                this.mGLImgTitle.onCreate();
                this.mGLImgTitle.setBackground(titleImageBitmap);
                this.mGLImgTitle.setVisible(true);
            }
        }
    }

    private void updateTitleImage(VideoSessionMgr videoSessionMgr) {
        if (this.mGLImgTitle != null) {
            Bitmap titleImageBitmap = getTitleImageBitmap();
            if (titleImageBitmap != null) {
                this.mGLImgTitle.updateUnitInfo(createTitleImageUnitInfo(titleImageBitmap));
                this.mGLImgTitle.setVisible(true);
            }
        }
    }

    private void createAudioMessageImage(@NonNull VideoSessionMgr videoSessionMgr) {
        Bitmap audioMessageImageBitmap = getAudioMessageImageBitmap();
        RendererUnitInfo createAudioMessageImageUnitInfo = createAudioMessageImageUnitInfo(audioMessageImageBitmap);
        if (createAudioMessageImageUnitInfo != null) {
            this.mGLImgAudioMessage = videoSessionMgr.createGLImage(createAudioMessageImageUnitInfo);
            GLImage gLImage = this.mGLImgAudioMessage;
            if (gLImage != null) {
                gLImage.setUnitName("AudioMessage");
                this.mGLImgAudioMessage.setVideoScene(this);
                addUnit(this.mGLImgAudioMessage);
                this.mGLImgAudioMessage.onCreate();
                this.mGLImgAudioMessage.setBackground(audioMessageImageBitmap);
                this.mGLImgAudioMessage.setVisible(true);
            }
        }
    }

    private void updateAudioMessageImage(VideoSessionMgr videoSessionMgr) {
        if (this.mGLImgAudioMessage != null) {
            Bitmap audioMessageImageBitmap = getAudioMessageImageBitmap();
            RendererUnitInfo createAudioMessageImageUnitInfo = createAudioMessageImageUnitInfo(audioMessageImageBitmap);
            if (createAudioMessageImageUnitInfo != null) {
                this.mGLImgAudioMessage.setBackground(audioMessageImageBitmap);
                this.mGLImgAudioMessage.updateUnitInfo(createAudioMessageImageUnitInfo);
                this.mGLImgAudioMessage.setVisible(true);
            }
        }
    }

    private void createVideoMessageImage(@NonNull VideoSessionMgr videoSessionMgr) {
        Bitmap videoMessageImageBitmap = getVideoMessageImageBitmap();
        RendererUnitInfo createVideoMessageImageUnitInfo = createVideoMessageImageUnitInfo(videoMessageImageBitmap);
        if (createVideoMessageImageUnitInfo != null) {
            this.mGLImgVideoMessage = videoSessionMgr.createGLImage(createVideoMessageImageUnitInfo);
            GLImage gLImage = this.mGLImgVideoMessage;
            if (gLImage != null) {
                gLImage.setUnitName("VideoMessage");
                this.mGLImgVideoMessage.setVideoScene(this);
                addUnit(this.mGLImgVideoMessage);
                this.mGLImgVideoMessage.onCreate();
                this.mGLImgVideoMessage.setBackground(videoMessageImageBitmap);
                this.mGLImgVideoMessage.setVisible(this.mbVideoOnPrevDriveMode);
            }
        }
    }

    private void updateVideoMessageImage(VideoSessionMgr videoSessionMgr) {
        if (this.mGLImgVideoMessage != null) {
            Bitmap videoMessageImageBitmap = getVideoMessageImageBitmap();
            RendererUnitInfo createVideoMessageImageUnitInfo = createVideoMessageImageUnitInfo(videoMessageImageBitmap);
            if (createVideoMessageImageUnitInfo != null) {
                this.mGLImgVideoMessage.setBackground(videoMessageImageBitmap);
                this.mGLImgVideoMessage.updateUnitInfo(createVideoMessageImageUnitInfo);
                this.mGLImgVideoMessage.setVisible(this.mbVideoOnPrevDriveMode);
            }
        }
    }

    private void createLineImage(@NonNull VideoSessionMgr videoSessionMgr) {
        this.mLineBitmap = getLineImageBitmap();
        RendererUnitInfo createLineImageUnitInfo = createLineImageUnitInfo(this.mLineBitmap);
        if (createLineImageUnitInfo != null) {
            this.mGLImgLine = videoSessionMgr.createGLImage(createLineImageUnitInfo);
            GLImage gLImage = this.mGLImgLine;
            if (gLImage != null) {
                gLImage.setUnitName("Line");
                this.mGLImgLine.setVideoScene(this);
                addUnit(this.mGLImgLine);
                this.mGLImgLine.onCreate();
                this.mGLImgLine.setBackground(this.mLineBitmap);
                this.mGLImgLine.setVisible(true);
            }
        }
    }

    private void updateLineImage(VideoSessionMgr videoSessionMgr) {
        if (this.mGLImgLine != null) {
            RendererUnitInfo createLineImageUnitInfo = createLineImageUnitInfo(getLineImageBitmap());
            if (createLineImageUnitInfo != null) {
                this.mGLImgLine.updateUnitInfo(createLineImageUnitInfo);
                this.mGLImgLine.setVisible(true);
            }
        }
    }

    private void createActiveSpeakerImage(@NonNull VideoSessionMgr videoSessionMgr) {
        Bitmap activeSpeakerImageBitmap = getActiveSpeakerImageBitmap();
        RendererUnitInfo createActiveSpeakerImageUnitInfo = createActiveSpeakerImageUnitInfo(activeSpeakerImageBitmap);
        if (createActiveSpeakerImageUnitInfo != null) {
            this.mGLImgActiveSpeaker = videoSessionMgr.createGLImage(createActiveSpeakerImageUnitInfo);
            GLImage gLImage = this.mGLImgActiveSpeaker;
            if (gLImage != null) {
                gLImage.setUnitName("ActiveSpeaker");
                this.mGLImgActiveSpeaker.setVideoScene(this);
                addUnit(this.mGLImgActiveSpeaker);
                this.mGLImgActiveSpeaker.onCreate();
                this.mGLImgActiveSpeaker.setBackground(activeSpeakerImageBitmap);
                this.mGLImgActiveSpeaker.setVisible(activeSpeakerImageBitmap != null);
                if (this.mGLImgActiveSpeaker.isVisible()) {
                    Runnable runnable = this.mRunnableHideActiveSpeaker;
                    if (runnable != null) {
                        this.mHandler.removeCallbacks(runnable);
                        this.mHandler.postDelayed(this.mRunnableHideActiveSpeaker, 2000);
                    }
                }
            }
        }
    }

    private void updateActiveSpeakerImage(VideoSessionMgr videoSessionMgr) {
        if (this.mGLImgActiveSpeaker != null) {
            Bitmap activeSpeakerImageBitmap = getActiveSpeakerImageBitmap();
            RendererUnitInfo createActiveSpeakerImageUnitInfo = createActiveSpeakerImageUnitInfo(activeSpeakerImageBitmap);
            if (createActiveSpeakerImageUnitInfo != null) {
                this.mGLImgActiveSpeaker.setBackground(activeSpeakerImageBitmap);
                this.mGLImgActiveSpeaker.updateUnitInfo(createActiveSpeakerImageUnitInfo);
            }
            if (this.mGLImgActiveSpeaker.isVisible()) {
                Runnable runnable = this.mRunnableHideActiveSpeaker;
                if (runnable != null) {
                    this.mHandler.removeCallbacks(runnable);
                    this.mHandler.postDelayed(this.mRunnableHideActiveSpeaker, 2000);
                }
            }
        }
    }

    private Bitmap getMuteButtonBitmap(boolean z) {
        int i;
        ConfActivity confActivity;
        Bitmap bitmap = this.mMuteUnmuteBtnBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        Drawable drawable = getConfActivity().getResources().getDrawable(this.mIsMicMuted ? C4558R.C4559drawable.zm_btn_tap_speak_normal : C4558R.C4559drawable.zm_btn_done_speak_normal);
        int dip2px = UIUtil.dip2px(getConfActivity(), 170.0f);
        try {
            Bitmap createBitmap = Bitmap.createBitmap(dip2px, dip2px, Config.ARGB_8888);
            if (createBitmap == null) {
                return null;
            }
            Canvas canvas = new Canvas(createBitmap);
            int i2 = dip2px - 1;
            drawable.setBounds(0, 0, i2, i2);
            drawable.draw(canvas);
            if (this.mIsMicMuted) {
                confActivity = getConfActivity();
                i = C4558R.string.zm_btn_tap_speak;
            } else {
                confActivity = getConfActivity();
                i = C4558R.string.zm_btn_done_speak;
            }
            String string = confActivity.getString(i);
            this.mBtnTextPaint.setColor(this.mIsMicMuted ? BTN_TAP_SPEAK_TEXT_COLOR : BTN_DONE_SPEAK_TEXT_COLOR);
            int desiredWidth = (int) (StaticLayout.getDesiredWidth(string, this.mBtnTextPaint) + 0.5f);
            int dip2px2 = UIUtil.dip2px(getConfActivity(), 10.0f);
            if (dip2px < desiredWidth + dip2px2) {
                desiredWidth = dip2px - dip2px2;
            }
            StaticLayout staticLayout = new StaticLayout(string, this.mBtnTextPaint, desiredWidth, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            int height = (dip2px - staticLayout.getHeight()) / 2;
            int i3 = (dip2px - desiredWidth) / 2;
            canvas.save();
            canvas.translate((float) i3, (float) height);
            staticLayout.draw(canvas);
            canvas.restore();
            if (z) {
                this.mbUIMuteStatus = this.mIsMicMuted;
            }
            this.mMuteUnmuteBtnBitmap = createBitmap;
            return createBitmap;
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    @NonNull
    private RendererUnitInfo createMuteUnmuteButtonUnitInfo(Bitmap bitmap) {
        int dip2px = UIUtil.dip2px(getConfActivity(), 170.0f);
        int left = getLeft() + ((getWidth() - dip2px) / 2);
        int top = getTop() + ((getHeight() - dip2px) / 2);
        GLImage gLImage = this.mbVideoOnPrevDriveMode ? this.mGLImgVideoMessage : this.mGLImgAudioMessage;
        if (gLImage != null) {
            int bottom = gLImage.getBottom() + UIUtil.dip2px(getConfActivity(), 10.0f);
            if (top < bottom) {
                top = bottom;
            }
        }
        return new RendererUnitInfo(left, top, dip2px, dip2px);
    }

    @Nullable
    private Bitmap getTitleImageBitmap() {
        Bitmap bitmap = this.mTitleBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        this.mTitleBitmap = createTextBmp(getConfActivity().getString(C4558R.string.zm_msg_driving_mode_title_86526), 0, this.mTitleTextPaint, this.mTitleTextHeight);
        return this.mTitleBitmap;
    }

    @NonNull
    private RendererUnitInfo createTitleImageUnitInfo(Bitmap bitmap) {
        int width = getWidth();
        int width2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = width < width2 ? width : width2;
        int dip2px = UIUtil.dip2px(getConfActivity(), 160.0f);
        if (width - width2 < dip2px) {
            i = width - dip2px;
        }
        return new RendererUnitInfo(getLeft() + ((getWidth() - i) / 2), getTop() + TITLE_MARGIN_TOP + UIUtil.dip2px(getConfActivity(), UIUtil.isPortraitMode(getConfActivity()) ? 15.0f : 2.0f), i, (height * i) / width2);
    }

    @NonNull
    private Bitmap getAudioMessageImageBitmap() {
        Bitmap bitmap = this.mAudioMessageBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        String string = getConfActivity().getString(this.mIsMicMuted ? C4558R.string.zm_msg_driving_mode_message_muted : C4558R.string.zm_msg_driving_mode_message_unmuted);
        this.mMessageTextPaint.setColor(this.mIsMicMuted ? MESSAGE_TEXT_COLOR_MUTED : MESSAGE_TEXT_COLOR_UNMUTED);
        this.mAudioMessageBitmap = createMultiLineTextBmp(string, this.mMessageTextPaint, getWidth(), Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        return this.mAudioMessageBitmap;
    }

    private RendererUnitInfo createAudioMessageImageUnitInfo(@NonNull Bitmap bitmap) {
        GLImage gLImage = this.mGLImgLine;
        if (gLImage == null) {
            return null;
        }
        int bottom = gLImage.getBottom();
        int width = getWidth();
        int width2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width >= width2) {
            width = width2;
        }
        return new RendererUnitInfo(getLeft() + ((getWidth() - width) / 2), bottom + UIUtil.dip2px(getConfActivity(), UIUtil.isPortraitMode(getConfActivity()) ? 50.0f : 5.0f), width, (height * width) / width2);
    }

    @NonNull
    private Bitmap getVideoMessageImageBitmap() {
        Bitmap bitmap = this.mVideoMessageBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        String string = this.mSceneMgr.getmVideoBoxApplication().getString(C4558R.string.zm_msg_driving_mode_message_video_stopped);
        this.mMessageTextPaint.setColor(MESSAGE_TEXT_COLOR_MUTED);
        this.mVideoMessageBitmap = createMultiLineTextBmp(string, this.mMessageTextPaint, getWidth(), Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        return this.mVideoMessageBitmap;
    }

    private RendererUnitInfo createVideoMessageImageUnitInfo(@NonNull Bitmap bitmap) {
        GLImage gLImage = this.mGLImgAudioMessage;
        if (gLImage == null) {
            return null;
        }
        int bottom = gLImage.getBottom();
        int width = getWidth();
        int width2 = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width >= width2) {
            width = width2;
        }
        return new RendererUnitInfo(getLeft() + ((getWidth() - width) / 2), bottom + UIUtil.dip2px(getConfActivity(), 3.0f), width, (height * width) / width2);
    }

    @NonNull
    private Bitmap getLineImageBitmap() {
        Bitmap bitmap = this.mLineBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        Bitmap createBitmap = Bitmap.createBitmap(16, UIUtil.dip2px(getConfActivity(), 1.0f), Config.ARGB_8888);
        new Canvas(createBitmap).drawColor(LINE_COLOR);
        return createBitmap;
    }

    private RendererUnitInfo createLineImageUnitInfo(Bitmap bitmap) {
        GLImage gLImage = this.mGLImgTitle;
        if (gLImage == null) {
            return null;
        }
        int bottom = gLImage.getBottom();
        return new RendererUnitInfo(getLeft(), bottom + UIUtil.dip2px(getConfActivity(), 3.0f), getWidth(), UIUtil.dip2px(getConfActivity(), 1.0f));
    }

    private Bitmap getActiveSpeakerImageBitmap() {
        if (StringUtil.isEmptyOrNull(this.mActiveSpeakerName)) {
            return null;
        }
        Bitmap bitmap = this.mActiveSpeakerBitmap;
        if (bitmap != null) {
            return bitmap;
        }
        this.mActiveSpeakerBitmap = createMultiLineTextBmp(getConfActivity().getString(C4558R.string.zm_msg_xxx_is_speaking, new Object[]{this.mActiveSpeakerName}), this.mActiveSpeakerTextPaint, getWidth(), Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        return this.mActiveSpeakerBitmap;
    }

    private RendererUnitInfo createActiveSpeakerImageUnitInfo(@Nullable Bitmap bitmap) {
        int i;
        if (bitmap == null) {
            return new RendererUnitInfo(Integer.MIN_VALUE, 0, 16, 16);
        }
        if (this.mGLBtnMuteUnmute == null) {
            return null;
        }
        int width = getWidth();
        int height = getHeight();
        int bottom = this.mGLBtnMuteUnmute.getBottom() - getTop();
        int width2 = bitmap.getWidth();
        int height2 = bitmap.getHeight();
        int dip2px = UIUtil.dip2px(getConfActivity(), 60.0f);
        int dip2px2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        if (UIUtil.isPortraitMode(getConfActivity())) {
            dip2px2 += UIUtil.dip2px(getConfActivity(), 22.0f);
        }
        int i2 = height - dip2px2;
        int i3 = ((i2 - bottom) - height2) / 2;
        if (i3 > dip2px) {
            i = (i2 - dip2px) + getTop();
        } else {
            i = getTop() + bottom + i3;
        }
        return new RendererUnitInfo(((width - width2) / 2) + getLeft(), i, width2, height2);
    }

    private void createSwitchAudioSourceButton(@NonNull VideoSessionMgr videoSessionMgr) {
        Drawable switchAudioSourceButtonDrawable = getSwitchAudioSourceButtonDrawable();
        this.mGLBtnSwitchAudioSource = videoSessionMgr.createGLButton(createSwitchAudioSourceButtonUnitInfo(switchAudioSourceButtonDrawable));
        GLButton gLButton = this.mGLBtnSwitchAudioSource;
        if (gLButton != null) {
            gLButton.setUnitName("SwitchAudioSource");
            this.mGLBtnSwitchAudioSource.setVideoScene(this);
            addUnit(this.mGLBtnSwitchAudioSource);
            this.mGLBtnSwitchAudioSource.onCreate();
            this.mGLBtnSwitchAudioSource.setBackground(switchAudioSourceButtonDrawable);
            this.mGLBtnSwitchAudioSource.setOnClickListener(this);
            this.mGLBtnSwitchAudioSource.setVisible(!getConfActivity().isToolbarShowing() && getConfActivity().canSwitchAudioSource());
        }
    }

    @Nullable
    private Drawable getSwitchAudioSourceButtonDrawable() {
        int currentAudioSourceType = ConfUI.getInstance().getCurrentAudioSourceType();
        if (this.mAudioSourceType == currentAudioSourceType) {
            GLButton gLButton = this.mGLBtnSwitchAudioSource;
            if (gLButton != null) {
                Drawable backgroundDrawable = gLButton.getBackgroundDrawable();
                if (backgroundDrawable != null) {
                    return backgroundDrawable;
                }
            }
        }
        this.mAudioSourceType = currentAudioSourceType;
        int i = C4558R.C4559drawable.zm_ic_speaker_off;
        switch (this.mAudioSourceType) {
            case 0:
                i = C4558R.C4559drawable.zm_ic_speaker_on;
                break;
            case 1:
                i = C4558R.C4559drawable.zm_ic_speaker_off;
                break;
            case 2:
                i = C4558R.C4559drawable.zm_ic_current_headset;
                break;
            case 3:
                i = C4558R.C4559drawable.zm_ic_current_bluetooth;
                break;
        }
        if (this.mResIdSwitchAudioSource == i) {
            Drawable drawable = this.mDrawableSwitchAudioSource;
            if (drawable != null) {
                return drawable;
            }
        }
        Drawable drawable2 = getConfActivity().getResources().getDrawable(i);
        this.mDrawableSwitchAudioSource = drawable2;
        this.mResIdSwitchAudioSource = i;
        return drawable2;
    }

    /* access modifiers changed from: private */
    public void updateSwitchAudioSourceButton() {
        if (this.mGLBtnSwitchAudioSource != null) {
            boolean z = false;
            if (!getConfActivity().canSwitchAudioSource()) {
                this.mGLBtnSwitchAudioSource.setVisible(false);
                getVideoSceneMgr().onAccessibilityViewUpdated(1);
                return;
            }
            Drawable switchAudioSourceButtonDrawable = getSwitchAudioSourceButtonDrawable();
            RendererUnitInfo createSwitchAudioSourceButtonUnitInfo = createSwitchAudioSourceButtonUnitInfo(switchAudioSourceButtonDrawable);
            this.mGLBtnSwitchAudioSource.setBackground(switchAudioSourceButtonDrawable);
            this.mGLBtnSwitchAudioSource.updateUnitInfo(createSwitchAudioSourceButtonUnitInfo);
            GLButton gLButton = this.mGLBtnSwitchAudioSource;
            if (!getConfActivity().isToolbarShowing() && this.mIsVoIP) {
                z = true;
            }
            gLButton.setVisible(z);
            getVideoSceneMgr().onAccessibilityViewUpdated(1);
            getVideoSceneMgr().announceAccessibilityAtView(1);
        }
    }

    @NonNull
    private RendererUnitInfo createSwitchAudioSourceButtonUnitInfo(@Nullable Drawable drawable) {
        int i;
        int i2;
        if (drawable == null) {
            GLButton gLButton = this.mGLBtnSwitchAudioSource;
            if (gLButton != null) {
                drawable = gLButton.getBackgroundDrawable();
            }
        }
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            i = intrinsicWidth;
        } else {
            i = UIUtil.dip2px(getConfActivity(), 45.0f);
            i2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        }
        return new RendererUnitInfo(getLeft() + UIUtil.dip2px(getConfActivity(), 12.0f), getTop() + TITLE_MARGIN_TOP + UIUtil.dip2px(getConfActivity(), UIUtil.isPortraitMode(getConfActivity()) ? 15.0f : 2.0f), i, i2);
    }

    private void createLeaveButton(@NonNull VideoSessionMgr videoSessionMgr) {
        Drawable leaveButtonDrawable = getLeaveButtonDrawable();
        this.mGLBtnLeave = videoSessionMgr.createGLButton(createLeaveButtonUnitInfo(leaveButtonDrawable));
        GLButton gLButton = this.mGLBtnLeave;
        if (gLButton != null) {
            gLButton.setUnitName("LeaveButton");
            this.mGLBtnLeave.setVideoScene(this);
            addUnit(this.mGLBtnLeave);
            this.mGLBtnLeave.onCreate();
            this.mGLBtnLeave.setBackground(leaveButtonDrawable);
            this.mGLBtnLeave.setOnClickListener(this);
        }
    }

    @NonNull
    private Drawable getLeaveButtonDrawable() {
        Drawable drawable = this.mDrawableBtnLeave;
        if (drawable != null) {
            return drawable;
        }
        VideoBoxApplication videoBoxApplication = this.mSceneMgr.getmVideoBoxApplication();
        String string = videoBoxApplication.getString(this.mIsHost ? C4558R.string.zm_btn_end_meeting : C4558R.string.zm_btn_leave_meeting);
        Typeface typeface = new TextView(videoBoxApplication).getTypeface();
        int color = videoBoxApplication.getResources().getColor(C4558R.color.zm_warn);
        int color2 = videoBoxApplication.getResources().getColor(C4558R.color.zm_warn_pressed);
        int dip2px = UIUtil.dip2px(videoBoxApplication, 5.0f);
        VideoBoxApplication videoBoxApplication2 = videoBoxApplication;
        String str = string;
        Typeface typeface2 = typeface;
        final TextDrawable textDrawable = new TextDrawable(videoBoxApplication2, str, typeface2, (float) UIUtil.sp2px(videoBoxApplication, 18.0f), color);
        TextDrawable textDrawable2 = new TextDrawable(videoBoxApplication2, str, typeface2, (float) UIUtil.sp2px(videoBoxApplication, 18.0f), color2);
        textDrawable.setPadding(0, dip2px, 0, dip2px);
        textDrawable2.setPadding(0, dip2px, 0, dip2px);
        C42293 r0 = new StateListDrawable() {
            public int getIntrinsicWidth() {
                return textDrawable.getIntrinsicWidth();
            }

            public int getIntrinsicHeight() {
                return textDrawable.getIntrinsicHeight();
            }
        };
        r0.addState(new int[]{16842910, -16842919}, textDrawable);
        r0.addState(new int[]{16842910, 16842919}, textDrawable2);
        this.mDrawableBtnLeave = r0;
        return this.mDrawableBtnLeave;
    }

    @NonNull
    private RendererUnitInfo createLeaveButtonUnitInfo(@Nullable Drawable drawable) {
        int i;
        int i2;
        if (drawable == null) {
            GLButton gLButton = this.mGLBtnLeave;
            if (gLButton != null) {
                drawable = gLButton.getBackgroundDrawable();
            }
        }
        if (drawable != null) {
            int intrinsicWidth = drawable.getIntrinsicWidth();
            i2 = drawable.getIntrinsicHeight();
            i = intrinsicWidth;
        } else {
            i = UIUtil.dip2px(getConfActivity(), 45.0f);
            i2 = UIUtil.dip2px(getConfActivity(), 45.0f);
        }
        return new RendererUnitInfo((getRight() - i) - UIUtil.dip2px(getConfActivity(), 12.0f), getTop() + UIUtil.dip2px(getConfActivity(), 12.0f) + TITLE_MARGIN_TOP, i, i2);
    }

    private void updateLeaveButton() {
        if (this.mGLBtnLeave != null) {
            Drawable leaveButtonDrawable = getLeaveButtonDrawable();
            this.mGLBtnLeave.updateUnitInfo(createLeaveButtonUnitInfo(leaveButtonDrawable));
            this.mGLBtnLeave.setBackground(leaveButtonDrawable);
            getVideoSceneMgr().onAccessibilityViewUpdated(0);
        }
    }

    private Bitmap createTextBmp(@NonNull String str, int i, TextPaint textPaint, int i2) {
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        int dip2px = UIUtil.dip2px(this.mSceneMgr.getmVideoBoxApplication(), 6.0f);
        int measureText = ((int) textPaint.measureText(str)) + dip2px + i;
        try {
            Bitmap createBitmap = Bitmap.createBitmap(measureText, i2, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            RectF rectF = new RectF(0.0f, 0.0f, (float) measureText, (float) i2);
            Paint paint = new Paint();
            paint.setColor(-16777216);
            canvas.drawRect(rectF, paint);
            canvas.drawText(str, (float) (i + (dip2px / 2)), ((float) (canvas.getHeight() / 2)) - (((fontMetrics.bottom - fontMetrics.top) / 2.0f) + fontMetrics.top), textPaint);
            return createBitmap;
        } catch (OutOfMemoryError unused) {
            return null;
        }
    }

    private Bitmap createMultiLineTextBmp(CharSequence charSequence, TextPaint textPaint, int i, Alignment alignment, float f, float f2, boolean z) {
        float desiredWidth = StaticLayout.getDesiredWidth(charSequence, textPaint);
        int i2 = i;
        int i3 = ((float) i2) > desiredWidth ? (int) (desiredWidth + 0.5f) : i2;
        StaticLayout staticLayout = new StaticLayout(charSequence, textPaint, i3, alignment, f, f2, z);
        Bitmap createBitmap = Bitmap.createBitmap(i3, staticLayout.getHeight(), Config.ARGB_8888);
        staticLayout.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    private void updateSwitchScenePanel() {
        String str;
        if (!isVideoPaused()) {
            ConfActivity confActivity = getConfActivity();
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
            LinearLayout linearLayout = (LinearLayout) confActivity.findViewById(C4558R.C4560id.panelSwitchSceneButtons);
            this.mSwitchSceneButtons = new ImageButton[10];
            int sceneCount = getVideoSceneMgr().getSceneCount();
            linearLayout.removeAllViews();
            int i = 0;
            int i2 = 0;
            while (true) {
                ImageButton[] imageButtonArr = this.mSwitchSceneButtons;
                if (i2 >= imageButtonArr.length) {
                    break;
                }
                imageButtonArr[i2] = new ImageButton(confActivity);
                this.mSwitchSceneButtons[i2].setBackgroundColor(0);
                this.mSwitchSceneButtons[i2].setImageResource(i2 == 0 ? C4558R.C4559drawable.zm_btn_switch_scene_selected : C4558R.C4559drawable.zm_btn_switch_scene_unselected);
                this.mSwitchSceneButtons[i2].setVisibility(i2 < sceneCount ? 0 : 8);
                this.mSwitchSceneButtons[i2].setOnClickListener(this);
                ImageButton imageButton = this.mSwitchSceneButtons[i2];
                if (i2 == 0) {
                    str = getConfActivity().getString(C4558R.string.zm_description_scene_driving);
                } else {
                    str = ((VideoSceneMgr) getVideoSceneMgr()).getAccessibliltyDescriptionSceneSwitch(i2);
                }
                imageButton.setContentDescription(str);
                linearLayout.addView(this.mSwitchSceneButtons[i2], UIUtil.dip2px(confActivity, 20.0f), UIUtil.dip2px(confActivity, 40.0f));
                i2++;
            }
            positionSwitchScenePanel();
            if (sceneCount <= 1) {
                i = 4;
            }
            findViewById.setVisibility(i);
        }
    }

    private void positionSwitchScenePanel() {
        int height = getHeight() - UIUtil.dip2px(getConfActivity(), 45.0f);
        ConfActivity confActivity = getConfActivity();
        if (confActivity != null) {
            View findViewById = confActivity.findViewById(C4558R.C4560id.panelSwitchScene);
            findViewById.setPadding(0, height, 0, 0);
            findViewById.getParent().requestLayout();
        }
    }

    public void onClick(View view) {
        int i = 0;
        while (true) {
            ImageButton[] imageButtonArr = this.mSwitchSceneButtons;
            if (i < imageButtonArr.length) {
                if (imageButtonArr[i] == view) {
                    switchToScene(i);
                }
                i++;
            } else {
                return;
            }
        }
    }

    private void switchToScene(int i) {
        if (i != 0) {
            getVideoSceneMgr().switchToScene(i);
        }
    }

    public void onClick(GLImage gLImage) {
        if (gLImage == this.mGLBtnMuteUnmute) {
            onClickBtnMuteUnmute();
        }
    }

    public boolean onTouchEvent(@Nullable MotionEvent motionEvent) {
        if (super.onTouchEvent(motionEvent)) {
            return true;
        }
        if (motionEvent == null) {
            return false;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mbPressed = true;
            this.mLastDownX = motionEvent.getX();
            this.mLastDownY = motionEvent.getY();
        } else if (motionEvent.getActionMasked() == 2) {
            float x = motionEvent.getX();
            float y = motionEvent.getY() - this.mLastDownY;
            float dip2px = (float) UIUtil.dip2px(getConfActivity(), 5.0f);
            if (Math.abs(x - this.mLastDownX) >= dip2px || Math.abs(y) >= dip2px) {
                this.mbPressed = false;
            }
        } else if (motionEvent.getActionMasked() == 1 && this.mbPressed) {
            this.mbPressed = false;
            onClickBtnMuteUnmute();
            return true;
        }
        return false;
    }

    private void onClickBtnMuteUnmute() {
        if (isAudioConnected()) {
            ConfActivity confActivity = getConfActivity();
            if (confActivity != null) {
                confActivity.muteAudio(true ^ this.mIsMicMuted);
                return;
            }
            return;
        }
        this.mbConnectAudioManual = true;
        ConfActivity confActivity2 = getConfActivity();
        if (confActivity2 != null) {
            confActivity2.onClickBtnAudio();
        }
    }

    private boolean isAudioConnected() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        boolean z = false;
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null || ConfMgr.getInstance().getConfContext() == null) {
            return false;
        }
        if (2 != audioStatusObj.getAudiotype()) {
            z = true;
        }
        return z;
    }

    @NonNull
    public Rect getBoundsForAccessbilityViewIndex(int i) {
        switch (i) {
            case 0:
                GLButton gLButton = this.mGLBtnLeave;
                if (gLButton != null) {
                    return new Rect(gLButton.getLeft(), this.mGLBtnLeave.getTop(), this.mGLBtnLeave.getRight(), this.mGLBtnLeave.getBottom());
                }
                break;
            case 1:
                GLButton gLButton2 = this.mGLBtnSwitchAudioSource;
                if (gLButton2 != null) {
                    return new Rect(gLButton2.getLeft(), this.mGLBtnSwitchAudioSource.getTop(), this.mGLBtnSwitchAudioSource.getRight(), this.mGLBtnSwitchAudioSource.getBottom());
                }
                break;
        }
        return new Rect();
    }

    public int getAccessbilityViewIndexAt(float f, float f2) {
        GLButton gLButton = this.mGLBtnLeave;
        if (gLButton != null && gLButton.isVisible() && this.mGLBtnLeave.contains(f, f2)) {
            return 0;
        }
        GLButton gLButton2 = this.mGLBtnSwitchAudioSource;
        return (gLButton2 == null || !gLButton2.isVisible() || !this.mGLBtnSwitchAudioSource.contains(f, f2)) ? -1 : 1;
    }

    @NonNull
    private CharSequence getAudioSourceAccessibilityDescription() {
        VideoBoxApplication videoBoxApplication = this.mSceneMgr.getmVideoBoxApplication();
        String str = "";
        switch (this.mAudioSourceType) {
            case 0:
                return videoBoxApplication.getString(C4558R.string.zm_description_btn_audio_source_speaker_phone);
            case 1:
                return videoBoxApplication.getString(C4558R.string.zm_description_btn_audio_source_ear_phone);
            case 2:
                return videoBoxApplication.getString(C4558R.string.zm_description_btn_audio_source_wired);
            case 3:
                return videoBoxApplication.getString(C4558R.string.zm_description_btn_audio_source_bluetooth);
            default:
                return str;
        }
    }

    @NonNull
    public CharSequence getAccessibilityDescriptionForIndex(int i) {
        switch (i) {
            case 0:
                GLButton gLButton = this.mGLBtnLeave;
                if (gLButton != null && gLButton.isVisible()) {
                    return this.mSceneMgr.getmVideoBoxApplication().getString(this.mIsHost ? C4558R.string.zm_btn_end_meeting : C4558R.string.zm_btn_leave_meeting);
                }
            case 1:
                GLButton gLButton2 = this.mGLBtnSwitchAudioSource;
                if (gLButton2 != null && gLButton2.isVisible()) {
                    return getAudioSourceAccessibilityDescription();
                }
        }
        return "";
    }

    public void getAccessibilityVisibleVirtualViews(@NonNull List<Integer> list) {
        GLButton gLButton = this.mGLBtnLeave;
        if (gLButton != null && gLButton.isVisible()) {
            list.add(Integer.valueOf(0));
        }
        GLButton gLButton2 = this.mGLBtnSwitchAudioSource;
        if (gLButton2 != null && gLButton2.isVisible()) {
            list.add(Integer.valueOf(1));
        }
    }

    public void updateAccessibilitySceneDescription() {
        if (getConfActivity() != null) {
            String string = getConfActivity().getString(C4558R.string.zm_description_scene_driving);
            if (this.mbVideoOnPrevDriveMode) {
                StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(getConfActivity().getString(C4558R.string.zm_description_video_stopped));
                string = sb.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(string);
            sb2.append(getConfActivity().getString(this.mIsMicMuted ? C4558R.string.zm_description_tap_speak : C4558R.string.zm_description_done_speaking));
            getVideoSceneMgr().updateAccessibilityDescriptionForActiveScece(sb2.toString());
        }
    }

    public void onClick(GLButton gLButton) {
        ConfActivity confActivity = getConfActivity();
        if (confActivity == null) {
            return;
        }
        if (gLButton == this.mGLBtnSwitchAudioSource) {
            ConfLocalHelper.switchAudio(confActivity);
        } else if (gLButton == this.mGLBtnLeave) {
            confActivity.onClickLeave();
        }
    }

    public void onHeadsetStatusChanged(boolean z, boolean z2) {
        if (isVisible()) {
            updateSwitchAudioSourceButton();
        }
    }

    public void onBluetoothScoAudioStatus(boolean z) {
        if (isVisible()) {
            updateSwitchAudioSourceButton();
        }
    }
}
