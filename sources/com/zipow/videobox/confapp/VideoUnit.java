package com.zipow.videobox.confapp;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.BitmapCacheMgr;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.IRendererUnit.PicInfo;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.RaiseHandTip;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.MeetingReactionMgr;
import p021us.zoom.androidlib.util.IListener;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMLog;
import p021us.zoom.videomeetings.C4558R;

public class VideoUnit implements IVideoUnit, IListener {
    /* access modifiers changed from: private */
    public static final int[] AUDIO_CONNECT_STATUS_IDS = {C4558R.C4559drawable.ic_audio_connect_status_0, C4558R.C4559drawable.ic_audio_connect_status_1, C4558R.C4559drawable.ic_audio_connect_status_2, C4558R.C4559drawable.ic_audio_connect_status_3, C4558R.C4559drawable.ic_audio_connect_status_4, C4558R.C4559drawable.ic_audio_connect_status_5, C4558R.C4559drawable.ic_audio_connect_status_6, C4558R.C4559drawable.ic_audio_connect_status_7};
    private static final int AVATAR_TYPE_BIG = 2;
    private static final int AVATAR_TYPE_NONE = 0;
    private static final int AVATAR_TYPE_SMALL = 1;
    public static final int AudioConnectStatus_NONE = -1;
    public static final int BORDER_TYPE_ACTIVE = 1;
    public static final int BORDER_TYPE_LOCKED = 2;
    public static final int BORDER_TYPE_NORMAL = 0;
    private static final int MAX_AVATAR_AREA = 160000;
    private static final int PIC_AUDIO_CONNECT_STATUS = 11;
    private static final int PIC_AUDIO_OFF = 3;
    private static final int PIC_AVATAR = 0;
    private static final int PIC_BORDER_BOTTOM = 9;
    private static final int PIC_BORDER_LEFT = 6;
    private static final int PIC_BORDER_RIGHT = 8;
    private static final int PIC_BORDER_TOP = 7;
    private static final int PIC_MEETING_REACTION = 10;
    private static final int PIC_NETWORK_STATUS = 5;
    private static final int PIC_USERNAME = 1;
    private static final int PIC_WATER_MARK = 4;
    public static final int SSB_MC_FREEWAY_INDEX = 1;
    private static final int TEXT_PADDING_VERTICAL = 2;
    private static final int TEXT_SIZE = 16;
    public static final int TYPE_ACTIVE = 1;
    public static final int TYPE_ATTENDEE = 0;
    private static int s_textHeight;
    private static TextPaint s_textPaint;
    private static int s_waterMarkTopMargin;
    @NonNull
    private String TAG = VideoUnit.class.getSimpleName();
    private Runnable mAudioConnectStatusCheckRunnable = new Runnable() {
        public void run() {
            if (!VideoUnit.this.mIsDestroyed) {
                VideoUnit.this.showAudioConnectStatus();
                switch (VideoUnit.this.mCurAudioConnectStatus) {
                    case 0:
                    case 2:
                    case 3:
                        VideoBoxApplication.getNonNullInstance().runOnMainThreadDelayed(VideoUnit.this.mAudioConnectStatusFinishRunnable, 1000);
                        break;
                    case 1:
                        VideoUnit.this.mAudioConnectStatusResIndex++;
                        if (VideoUnit.this.mAudioConnectStatusResIndex >= VideoUnit.AUDIO_CONNECT_STATUS_IDS.length) {
                            VideoUnit.this.mAudioConnectStatusResIndex = 0;
                        }
                        VideoUnit.this.refreshAudioConnectingStatus();
                        break;
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Runnable mAudioConnectStatusFinishRunnable = new Runnable() {
        public void run() {
            VideoUnit.this.hiddenAudioConnectStatus();
        }
    };
    int mAudioConnectStatusResIndex = 0;
    boolean mAudioConnectStatusShowing = false;
    private long mAudioType = 0;
    @Nullable
    private String mAvatarBig;
    @Nullable
    private String mAvatarSmall;
    private int mAvatarType = 0;
    private int mBkColor = 0;
    private int mBorderType = 0;
    private boolean mCanShowAudioOff = false;
    private boolean mCanShowNetworkStatus = false;
    private boolean mCanShowWaterMark = false;
    @NonNull
    private final VideoBoxApplication mContext;
    int mCurAudioConnectStatus = -1;
    @Nullable
    Boolean mCurInMainVideo = null;
    private boolean mHasAvatar = false;
    private boolean mHasBorder = false;
    private boolean mHasWaterMark = false;
    private int mHeight;
    private boolean mIsBorderVisible = false;
    /* access modifiers changed from: private */
    public boolean mIsDestroyed = false;
    private boolean mIsFloating = false;
    private boolean mIsH323User = false;
    private boolean mIsKeyUnit;
    private boolean mIsMainVideo = false;
    private boolean mIsMySelf = false;
    private boolean mIsPaused = false;
    private boolean mIsPureCallInUser = false;
    private boolean mIsUserNameVisible = true;
    int mLastAudioConnectStatus = -1;
    Boolean mLastInMainVideo = null;
    private int mLeft;
    private int mNetworkStatus = -1;
    private long mPausedUserId = 0;
    @Nullable
    private PicInfo mPiAudioConnectStatus;
    @Nullable
    private PicInfo mPiAudioOff;
    @Nullable
    private PicInfo mPiAvatar;
    @Nullable
    private PicInfo mPiBorderBottom;
    @Nullable
    private PicInfo mPiBorderLeft;
    @Nullable
    private PicInfo mPiBorderRight;
    @Nullable
    private PicInfo mPiBorderTop;
    @Nullable
    private PicInfo mPiMeetingReaction;
    @Nullable
    private PicInfo mPiNetworkStatus;
    @Nullable
    private PicInfo mPiUserName;
    @Nullable
    private PicInfo mPiWaterMark;
    private long mRenderInfo;
    private long mShowType = -1;
    private int mTop;
    private int mType = 0;
    @Nullable
    private String mUnitName = null;
    private long mUserId = 0;
    @Nullable
    private String mUserName;
    private AbsVideoScene mVideoScene;
    private int mVideoType = 2;
    private int mWidth;
    private boolean mbAudioOff = false;
    private boolean mbNetworkRestrictionMode = false;
    private boolean mbPreviewing = false;
    private boolean mbShowingVideo = false;
    private boolean mbUserNameResourceHasLeftPaddingForAudioOff = false;
    private boolean mbUserNameResourceHasLeftPaddingForNetworkStatus = false;
    private String meetingReactionAccTxt = "";

    public static void initDefaultResources() {
        s_textPaint = new TextPaint();
        VideoBoxApplication instance = VideoBoxApplication.getInstance();
        s_textPaint.setTypeface(new TextView(instance).getTypeface());
        s_textPaint.setTextSize((float) UIUtil.sp2px(instance, 16.0f));
        s_textPaint.setColor(-1);
        s_textPaint.setAntiAlias(true);
        FontMetrics fontMetrics = s_textPaint.getFontMetrics();
        s_textHeight = ((int) (fontMetrics.bottom - fontMetrics.top)) + UIUtil.dip2px(instance, 4.0f);
        s_waterMarkTopMargin = UIUtil.dip2px(instance, 8.0f);
    }

    public VideoUnit(@NonNull VideoBoxApplication videoBoxApplication, boolean z, long j, @Nullable RendererUnitInfo rendererUnitInfo) {
        this.mContext = videoBoxApplication;
        this.mIsKeyUnit = z;
        this.mRenderInfo = j;
        if (rendererUnitInfo != null) {
            this.mLeft = rendererUnitInfo.left;
            this.mTop = rendererUnitInfo.top;
            this.mWidth = rendererUnitInfo.width;
            this.mHeight = rendererUnitInfo.height;
        }
    }

    public void setVideoScene(AbsVideoScene absVideoScene) {
        this.mVideoScene = absVideoScene;
    }

    public void setUnitName(String str) {
        this.mUnitName = str;
        if (StringUtil.isEmptyOrNull(this.mUnitName)) {
            this.TAG = VideoUnit.class.getSimpleName();
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(VideoUnit.class.getSimpleName());
        sb.append(":");
        sb.append(this.mUnitName);
        this.TAG = sb.toString();
    }

    @Nullable
    public String getUnitName() {
        return this.mUnitName;
    }

    public boolean isKeyUnit() {
        return this.mIsKeyUnit;
    }

    public long getRendererInfo() {
        return this.mRenderInfo;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getTop() {
        return this.mTop;
    }

    public int getRight() {
        return this.mLeft + this.mWidth;
    }

    public int getBottom() {
        return this.mTop + this.mHeight;
    }

    public int getmVideoType() {
        return this.mVideoType;
    }

    public boolean isPointInUnit(float f, float f2) {
        return f >= ((float) getLeft()) && f <= ((float) getRight()) && f2 >= ((float) getTop()) && f2 <= ((float) getBottom());
    }

    public boolean isMySelf() {
        return this.mIsMySelf;
    }

    public void pause() {
        if (!this.mIsPaused) {
            this.mIsPaused = true;
            this.mShowType = 0;
            if (this.mbShowingVideo) {
                stopVideo(false);
            }
        }
    }

    public void resume() {
        if (this.mIsPaused) {
            this.mIsPaused = false;
            long j = this.mPausedUserId;
            if (j != 0) {
                setUser(j);
                return;
            }
            long j2 = this.mUserId;
            if (j2 != 0) {
                setUser(j2);
                startVideo();
            }
        }
    }

    public boolean isPaused() {
        return this.mIsPaused;
    }

    public void setUserNameVisible(boolean z) {
        setUserNameVisible(z, false, false);
    }

    public void setUserNameVisible(boolean z, boolean z2) {
        setUserNameVisible(z, true, z2);
    }

    private void setUserNameVisible(boolean z, boolean z2, boolean z3) {
        if (this.mIsUserNameVisible != z) {
            this.mIsUserNameVisible = z;
            if (z2) {
                this.mCanShowNetworkStatus = z3;
            }
            if (this.mUserId != 0) {
                if (this.mIsUserNameVisible) {
                    showUserName();
                } else {
                    removeUserName();
                }
                if (z2) {
                    updateNetworkStatus();
                }
            }
        }
    }

    public boolean isUserNameVisible() {
        return this.mIsUserNameVisible;
    }

    public boolean isMainVideo() {
        return this.mIsMainVideo;
    }

    public void setMainVideo(boolean z) {
        this.mIsMainVideo = z;
    }

    public void setBorderVisible(boolean z) {
        if (this.mIsBorderVisible != z || this.mHasBorder != z) {
            this.mIsBorderVisible = z;
            if (this.mIsBorderVisible) {
                showBorder();
            } else {
                removeBorder();
            }
        }
    }

    public void setBorderType(int i) {
        if (this.mBorderType != i && this.mVideoScene.getVideoRenderer() != null) {
            this.mPiBorderLeft = null;
            this.mPiBorderTop = null;
            this.mPiBorderRight = null;
            this.mPiBorderBottom = null;
            this.mBorderType = i;
            if (this.mIsBorderVisible) {
                showBorder();
            }
        }
    }

    public void setBackgroundColor(int i) {
        if (i != this.mBkColor && this.mVideoScene != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                videoObj.setRendererBackgroudColor(this.mRenderInfo, i);
                this.mBkColor = i;
            }
        }
    }

    public boolean isBorderVisible() {
        return this.mIsBorderVisible;
    }

    public void setCanShowAudioOff(boolean z) {
        this.mCanShowAudioOff = z;
        if (this.mUserId != 0) {
            updateAudioOff();
        }
    }

    public boolean getCanShowAudioOff() {
        return this.mCanShowAudioOff;
    }

    public boolean getCanShowNetworkStatus() {
        return this.mCanShowNetworkStatus;
    }

    public void setCanShowWaterMark(boolean z) {
        if (UIMgr.getShowWatermarkOnVideo() || isSdkUserNeedShowWaterMark()) {
            this.mCanShowWaterMark = z;
            if ((this.mUserId != 0 && !this.mHasAvatar) || this.mbPreviewing) {
                showWaterMark(this.mCanShowWaterMark);
            }
        }
    }

    public boolean getCanShowWaterMark() {
        return this.mCanShowWaterMark;
    }

    private boolean isSdkUserNeedShowWaterMark() {
        if (!this.mContext.isSDKMode()) {
            return false;
        }
        ConfActivity confActivity = this.mVideoScene.getConfActivity();
        if (confActivity == null) {
            return false;
        }
        return confActivity.getConfParams().isShowSdkWaterMark();
    }

    public void setIsFloating(boolean z) {
        this.mIsFloating = z;
    }

    public boolean isFloating() {
        return this.mIsFloating;
    }

    public void updateUnitInfo(@Nullable RendererUnitInfo rendererUnitInfo) {
        if (rendererUnitInfo != null && !isSameInfo(rendererUnitInfo) && this.mVideoScene != null) {
            boolean z = true;
            boolean z2 = (this.mWidth == rendererUnitInfo.width && this.mHeight == rendererUnitInfo.height) ? false : true;
            this.mLeft = rendererUnitInfo.left;
            this.mTop = rendererUnitInfo.top;
            this.mWidth = rendererUnitInfo.width;
            this.mHeight = rendererUnitInfo.height;
            if (z2) {
                if (this.mVideoScene.getVideoRenderer() != null) {
                    this.mPiUserName = null;
                    this.mPiBorderLeft = null;
                    this.mPiBorderTop = null;
                    this.mPiBorderRight = null;
                    this.mPiBorderBottom = null;
                    this.mPiAvatar = null;
                } else {
                    return;
                }
            }
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                videoObj.updateUnitLayout(this.mRenderInfo, rendererUnitInfo);
                if (isBorderVisible()) {
                    showBorder();
                }
                showAudioOff(this.mUserId != 0 && this.mbAudioOff && this.mCanShowAudioOff);
                showWaterMark(this.mHasWaterMark);
                if (this.mUserId == 0 || this.mNetworkStatus == -1 || !this.mCanShowNetworkStatus) {
                    z = false;
                }
                showNetworkStatus(z, false);
                if (this.mUserId != 0 && isUserNameVisible()) {
                    showUserName();
                }
                if (this.mHasAvatar) {
                    showAvatar();
                }
                checkAudioConnectStatus();
                MeetingReactionMgr.getInstance().checkShowMeetingReaction(this);
            }
        }
    }

    private boolean isSameInfo(@Nullable RendererUnitInfo rendererUnitInfo) {
        boolean z = false;
        if (rendererUnitInfo == null) {
            return false;
        }
        if (this.mLeft == rendererUnitInfo.left && this.mTop == rendererUnitInfo.top && this.mWidth == rendererUnitInfo.width && this.mHeight == rendererUnitInfo.height) {
            z = true;
        }
        return z;
    }

    public void setType(int i) {
        this.mType = i;
        if (isBorderVisible()) {
            showBorder();
        }
    }

    public int getType() {
        return this.mType;
    }

    public void setUser(long j) {
        if (!this.mVideoScene.isPreloadStatus() && j != 0) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                if (videoObj.isSameVideo(this.mUserId, j)) {
                    updateRenderContainer(j);
                }
                if (!videoObj.isSameVideo(this.mUserId, j) || !this.mbShowingVideo) {
                    subscribeVideo(j);
                }
                updateAudioOff();
                updateNetworkStatus();
                checkAudioConnectStatus();
                CmmUser userById = ConfMgr.getInstance().getUserById(j);
                if (userById != null && this.mVideoScene.getVideoRenderer() != null) {
                    String userDisplayName = getUserDisplayName(userById);
                    if (!StringUtil.isSameString(userDisplayName, this.mUserName)) {
                        this.mPiUserName = null;
                        this.mUserName = userDisplayName;
                    }
                }
            }
        }
    }

    @Nullable
    private String getUserDisplayName(@Nullable CmmUser cmmUser) {
        if (cmmUser == null) {
            return null;
        }
        return cmmUser.getScreenName();
    }

    private void updateRenderContainer(long j) {
        if (!isPaused()) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                if (confStatusObj != null) {
                    this.mVideoType = videoObj.getVideoTypeByID(j);
                    if (j == 1 || videoObj.isSelectedUser(j)) {
                        CmmUser userById = ConfMgr.getInstance().getUserById(j);
                        if (userById != null) {
                            if (this.mVideoScene.getVideoRenderer() != null) {
                                this.mPiAvatar = null;
                                this.mPiUserName = null;
                                this.mUserName = getUserDisplayName(userById);
                                this.mAvatarSmall = userById.getSmallPicPath();
                                this.mAvatarBig = userById.getLocalPicPath();
                                this.mIsPureCallInUser = userById.isPureCallInUser();
                                this.mIsH323User = userById.isH323User();
                                if (isUserNameVisible()) {
                                    showUserName();
                                }
                            } else {
                                return;
                            }
                        }
                    } else if (((long) this.mVideoType) == this.mShowType) {
                        return;
                    }
                    boolean isMyself = confStatusObj.isMyself(this.mUserId);
                    int i = this.mVideoType;
                    if (i == 0) {
                        if (isMyself) {
                            stopVideo(true);
                        }
                        clearRenderer();
                        if (!isMyself || (!videoObj.isVideoStarted() && !videoObj.isPreviewing())) {
                            this.mShowType = (long) this.mVideoType;
                            showAvatar();
                        }
                    } else if (((long) i) != this.mShowType) {
                        this.mShowType = (long) i;
                        if (isMyself) {
                            startVideo();
                        }
                    }
                }
            }
        }
    }

    private void subscribeVideo(long j) {
        if (j != 0) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    long j2 = this.mUserId;
                    if (j2 != 0 && !videoObj.isSameVideo(j2, j)) {
                        removeUser();
                    } else if (this.mbPreviewing) {
                        removeUser(!confStatusObj.isMyself(j));
                    }
                    if (isPaused()) {
                        this.mPausedUserId = j;
                        return;
                    }
                    this.mPausedUserId = 0;
                    if (j == 1 || videoObj.querySubStatus(j)) {
                        this.mUserId = j;
                        startVideo();
                        if (this.mVideoScene.getVideoRenderer() != null) {
                            this.mPiAvatar = null;
                            this.mPiUserName = null;
                            CmmUser userById = ConfMgr.getInstance().getUserById(j);
                            if (userById != null) {
                                this.mUserName = getUserDisplayName(userById);
                                this.mAvatarSmall = userById.getSmallPicPath();
                                this.mAvatarBig = userById.getLocalPicPath();
                                this.mIsPureCallInUser = userById.isPureCallInUser();
                                this.mIsH323User = userById.isH323User();
                            }
                            long j3 = this.mUserId;
                            if (j3 == 0) {
                                this.mIsMySelf = false;
                            } else {
                                this.mIsMySelf = confStatusObj.isMyself(j3);
                            }
                            this.mVideoType = videoObj.getVideoTypeByID(this.mUserId);
                            if (this.mVideoType < 0) {
                                this.mVideoType = 2;
                            }
                            int i = this.mVideoType;
                            if (i == 0) {
                                clearRenderer();
                                if (!this.mIsMySelf || (!videoObj.isVideoStarted() && !videoObj.isPreviewing())) {
                                    this.mShowType = (long) this.mVideoType;
                                    showAvatar();
                                }
                            } else {
                                this.mShowType = (long) i;
                            }
                            if (isBorderVisible()) {
                                showBorder();
                            }
                            if (isUserNameVisible()) {
                                showUserName();
                            }
                            MeetingReactionMgr.getInstance().checkShowMeetingReaction(this);
                            return;
                        }
                        return;
                    }
                    this.mUserId = 0;
                }
            }
        }
    }

    @Nullable
    private Bitmap createAvatarBitmap() {
        Bitmap bitmap;
        Resources resources = this.mContext.getResources();
        if (this.mIsPureCallInUser || this.mIsH323User) {
            Drawable drawable = resources.getDrawable(this.mIsPureCallInUser ? C4558R.C4559drawable.zm_phone_inmeeting : C4558R.C4559drawable.zm_h323_inmeeting);
            int min = Math.min(Math.min(getWidth(), getHeight()), drawable.getIntrinsicWidth());
            Bitmap createBitmap = Bitmap.createBitmap(min, min, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            drawable.setBounds(0, 0, min, min);
            drawable.draw(canvas);
            this.mAvatarType = 2;
            bitmap = createBitmap;
        } else if (!StringUtil.isEmptyOrNull(this.mAvatarBig)) {
            bitmap = ZMBitmapFactory.decodeFile(this.mAvatarBig, MAX_AVATAR_AREA, false, false);
            if (bitmap != null) {
                this.mAvatarType = 2;
            } else if (!StringUtil.isEmptyOrNull(this.mAvatarSmall)) {
                bitmap = ZMBitmapFactory.decodeFile(this.mAvatarSmall, MAX_AVATAR_AREA, false, false);
                this.mAvatarType = 1;
            }
        } else if (!StringUtil.isEmptyOrNull(this.mAvatarSmall)) {
            bitmap = ZMBitmapFactory.decodeFile(this.mAvatarSmall, MAX_AVATAR_AREA, false, false);
            if (bitmap != null) {
                this.mAvatarType = 1;
            } else if (!StringUtil.isEmptyOrNull(this.mAvatarBig)) {
                bitmap = ZMBitmapFactory.decodeFile(this.mAvatarBig, MAX_AVATAR_AREA, false, false);
                this.mAvatarType = 1;
            }
        } else {
            bitmap = null;
        }
        if (bitmap == null) {
            Drawable drawable2 = resources.getDrawable(C4558R.C4559drawable.zm_conf_no_avatar);
            int intrinsicWidth = drawable2.getIntrinsicWidth();
            int intrinsicHeight = drawable2.getIntrinsicHeight();
            try {
                bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
            } catch (OutOfMemoryError unused) {
            }
            if (bitmap != null) {
                Canvas canvas2 = new Canvas(bitmap);
                drawable2.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
                drawable2.draw(canvas2);
            }
            this.mAvatarType = 0;
        }
        return bitmap;
    }

    @Nullable
    private Bitmap createUserNameBitmap() {
        int i;
        Bitmap bitmap;
        if (this.mUserName == null) {
            return null;
        }
        if (!this.mbAudioOff || !this.mCanShowAudioOff) {
            this.mbUserNameResourceHasLeftPaddingForAudioOff = false;
            i = 0;
        } else {
            i = getAudioOffWidth() + 0;
            this.mbUserNameResourceHasLeftPaddingForAudioOff = true;
        }
        if (this.mNetworkStatus == -1 || !this.mCanShowNetworkStatus) {
            this.mbUserNameResourceHasLeftPaddingForNetworkStatus = false;
        } else {
            i += getNetworkStatusWidth();
            this.mbUserNameResourceHasLeftPaddingForNetworkStatus = true;
        }
        int audioConnectStatusWidth = getAudioConnectStatusWidth();
        if (isMainVideo() || audioConnectStatusWidth == 0) {
            bitmap = createTextBmp(this.mUserName, i, this.mWidth, audioConnectStatusWidth);
        } else {
            bitmap = createTextBmp(this.mContext.getResources().getString(C4558R.string.zm_lbl_connecting_to_audio_123338), i, this.mWidth, audioConnectStatusWidth);
        }
        return bitmap;
    }

    private int getAudioOffWidth() {
        return this.mContext.getResources().getDrawable(this.mAudioType == 0 ? C4558R.C4559drawable.zm_audio_off_small : C4558R.C4559drawable.zm_phone_off_small).getIntrinsicWidth() + UIUtil.dip2px(this.mContext, 2.0f);
    }

    private int getNetworkStatusWidth() {
        return this.mContext.getResources().getDrawable(C4558R.C4559drawable.zm_network_good).getIntrinsicWidth() + UIUtil.dip2px(this.mContext, 2.0f);
    }

    public long getUser() {
        return this.mUserId;
    }

    public void removeUser() {
        removeUser(true);
    }

    public void removeUser(boolean z) {
        this.mPausedUserId = 0;
        if (this.mbPreviewing) {
            stopPreview(z);
        }
        if (this.mUserId != 0) {
            stopVideo(false);
            this.mUserId = 0;
            this.mUserName = null;
            this.mIsMySelf = false;
            this.mbAudioOff = false;
            this.mNetworkStatus = -1;
            if (this.mVideoScene != null && this.mUserId == 0) {
                removeAvatar();
                removeUserName();
                removeMeetingReaction();
                showAudioOff(false);
                showWaterMark(false);
                showNetworkStatus(false, false);
            }
        }
    }

    public boolean isVideoShowing() {
        return this.mbShowingVideo;
    }

    public void startVideo() {
        int i;
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            this.mbShowingVideo = true;
            int px2dip = (int) UIUtil.px2dip(this.mContext, getHeight());
            switch (this.mType) {
                case 0:
                    videoObj.showAttendeeVideo(this.mRenderInfo, this.mUserId, this.mbNetworkRestrictionMode ? 90 : px2dip, this.mIsFloating);
                    break;
                case 1:
                    if (isFloating()) {
                        i = 90;
                    } else {
                        videoObj.enable180p(false);
                        i = this.mbNetworkRestrictionMode ? 360 : px2dip;
                    }
                    videoObj.showActiveVideo(this.mRenderInfo, this.mUserId, i);
                    break;
            }
        }
    }

    public void stopVideo(boolean z) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            this.mbShowingVideo = false;
            if (this.mUserId != 0) {
                videoObj.stopShowVideo(this.mRenderInfo);
            }
            this.mShowType = -1;
            if (z && this.mVideoScene != null) {
                long j = this.mRenderInfo;
                if (j != 0) {
                    videoObj.clearRenderer(j);
                }
            }
        }
    }

    public void setNetworkRestrictionMode(boolean z, boolean z2) {
        if (z2) {
            stopVideo(false);
        }
        this.mbNetworkRestrictionMode = z;
        if (z2) {
            startVideo();
        }
    }

    public void startPreview(String str) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            if (videoObj.startPreviewDevice(this.mRenderInfo, str)) {
                this.mbPreviewing = true;
                this.mbShowingVideo = true;
            }
            if (this.mCanShowWaterMark) {
                showWaterMark(true);
            }
        }
    }

    public void stopPreview(boolean z) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.stopPreviewDevice(this.mRenderInfo);
            if (this.mUserId == 0) {
                this.mbShowingVideo = false;
            }
            this.mbPreviewing = false;
            removeBorder();
            if (z) {
                long j = this.mRenderInfo;
                if (j != 0) {
                    videoObj.clearRenderer(j);
                }
            }
        }
    }

    public void clearRenderer() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.clearRenderer(this.mRenderInfo);
        }
    }

    public void onIdle() {
        if (this.mUserId != 0 && this.mVideoType == 2 && this.mHasAvatar && !isPaused()) {
            removeAvatar();
        }
        if ((this.mUserId != 0 || this.mbPreviewing) && this.mCanShowWaterMark && !this.mHasWaterMark && this.mVideoType == 2 && !isPaused()) {
            showWaterMark(this.mCanShowWaterMark);
        }
        if (isBorderVisible() && !this.mHasBorder) {
            showBorder();
        }
    }

    public void onCreate() {
        this.mIsDestroyed = false;
        MeetingReactionMgr.getInstance().registerUnit(this);
    }

    public void onDestroy() {
        removeAvatar();
        removeBorder();
        removeUserName();
        removeMeetingReaction();
        hiddenAudioConnectStatus();
        showAudioOff(false);
        showWaterMark(false);
        showNetworkStatus(false, false);
        MeetingReactionMgr.getInstance().unregisterUnit(this);
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.destroyVideoUnit(this);
            this.mIsDestroyed = true;
        }
    }

    public void onGLViewSizeChanged(int i, int i2) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.glViewSizeChanged(this.mRenderInfo, i, i2);
        }
    }

    public String getMeetingReactionAccTxt() {
        return this.meetingReactionAccTxt;
    }

    public void showMeetingReaction(int i, int i2) {
        if (!this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                this.meetingReactionAccTxt = MeetingReactionMgr.getInstance().getAccTxt(i);
                PicInfo picInfo = this.mPiMeetingReaction;
                if (picInfo == null || picInfo.intExtra != (i << (i2 + 16))) {
                    showMeetingReactionWithNewBitmap(i, i2, videoObj);
                } else {
                    showMeetingReactionWithCachedBitmap(this.mPiMeetingReaction, videoObj);
                }
            }
        }
    }

    public void showMeetingReaction(@NonNull String str) {
        if (!this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                this.meetingReactionAccTxt = MeetingReactionMgr.getInstance().getAccTxt(str);
                PicInfo picInfo = this.mPiMeetingReaction;
                if (picInfo == null || !picInfo.strExtra.equals(str)) {
                    showMeetingReactionWithNewBitmap(str, videoObj);
                } else {
                    showMeetingReactionWithCachedBitmap(this.mPiMeetingReaction, videoObj);
                }
            }
        }
    }

    private void showMeetingReactionWithNewBitmap(int i, int i2, @NonNull VideoSessionMgr videoSessionMgr) {
        int i3;
        int i4;
        int i5 = i;
        int i6 = i2;
        if (this.mWidth != 0 && this.mHeight != 0) {
            float emojiHWRatio = MeetingReactionMgr.getInstance().getEmojiHWRatio(i5, i6);
            if (emojiHWRatio != 0.0f) {
                int min = Math.min(this.mWidth, this.mHeight) / 3;
                int i7 = (int) (((float) min) * emojiHWRatio);
                if (this.mIsMainVideo) {
                    i4 = UIUtil.dip2px(this.mContext, 72.0f);
                    i3 = UIUtil.dip2px(this.mContext, 72.0f);
                } else {
                    i4 = UIUtil.dip2px(this.mContext, 48.0f);
                    i3 = UIUtil.dip2px(this.mContext, 48.0f);
                }
                if (min > i4) {
                    i7 = (int) (((float) i4) * emojiHWRatio);
                    min = i4;
                }
                if (i7 > i3) {
                    min = (int) (((float) i3) / emojiHWRatio);
                    i7 = i3;
                }
                if (min != 0 && i7 != 0) {
                    Bitmap meetingReactionBitmap = MeetingReactionMgr.getInstance().getMeetingReactionBitmap(i5, i6, min, i7);
                    if (meetingReactionBitmap != null) {
                        int width = meetingReactionBitmap.getWidth();
                        int height = meetingReactionBitmap.getHeight();
                        Rect meetingReactionPosition = getMeetingReactionPosition(width, height);
                        videoSessionMgr.removePic(this.mRenderInfo, 10);
                        long addPic = videoSessionMgr.addPic(this.mRenderInfo, 10, meetingReactionBitmap, 255, 0, meetingReactionPosition.left, meetingReactionPosition.top, meetingReactionPosition.right, meetingReactionPosition.bottom);
                        if (addPic != 0) {
                            int i8 = i5 << (i6 + 16);
                            PicInfo picInfo = new PicInfo(addPic, width, height, i8);
                            this.mPiMeetingReaction = picInfo;
                        }
                        meetingReactionBitmap.recycle();
                    }
                }
            }
        }
    }

    private void showMeetingReactionWithNewBitmap(@NonNull String str, @NonNull VideoSessionMgr videoSessionMgr) {
        int i;
        int i2;
        String str2 = str;
        if (this.mWidth != 0 && this.mHeight != 0) {
            float emojiHWRatio = MeetingReactionMgr.getInstance().getEmojiHWRatio(str2);
            if (emojiHWRatio != 0.0f) {
                int min = Math.min(this.mWidth, this.mHeight) / 3;
                int i3 = (int) (((float) min) * emojiHWRatio);
                if (this.mIsMainVideo) {
                    i2 = UIUtil.dip2px(this.mContext, 72.0f);
                    i = UIUtil.dip2px(this.mContext, 72.0f);
                } else {
                    i2 = UIUtil.dip2px(this.mContext, 48.0f);
                    i = UIUtil.dip2px(this.mContext, 48.0f);
                }
                if (min > i2) {
                    i3 = (int) (((float) i2) * emojiHWRatio);
                    min = i2;
                }
                if (i3 > i) {
                    min = (int) (((float) i) / emojiHWRatio);
                    i3 = i;
                }
                if (min != 0 && i3 != 0) {
                    Bitmap meetingReactionBitmap = MeetingReactionMgr.getInstance().getMeetingReactionBitmap(str2, min, i3);
                    if (meetingReactionBitmap != null) {
                        int width = meetingReactionBitmap.getWidth();
                        int height = meetingReactionBitmap.getHeight();
                        Rect meetingReactionPosition = getMeetingReactionPosition(width, height);
                        videoSessionMgr.removePic(this.mRenderInfo, 10);
                        long j = this.mRenderInfo;
                        long j2 = j;
                        long addPic = videoSessionMgr.addPic(j2, 10, meetingReactionBitmap, 255, 0, meetingReactionPosition.left, meetingReactionPosition.top, meetingReactionPosition.right, meetingReactionPosition.bottom);
                        if (addPic != 0) {
                            PicInfo picInfo = new PicInfo(addPic, width, height, str);
                            this.mPiMeetingReaction = picInfo;
                        }
                        meetingReactionBitmap.recycle();
                    }
                }
            }
        }
    }

    private void showMeetingReactionWithCachedBitmap(@NonNull PicInfo picInfo, @NonNull VideoSessionMgr videoSessionMgr) {
        int i = picInfo.bmpWidth;
        int i2 = picInfo.bmpHeight;
        if (i != 0 && i2 != 0) {
            Rect meetingReactionPosition = getMeetingReactionPosition(i, i2);
            videoSessionMgr.movePic2(this.mRenderInfo, 10, meetingReactionPosition.left, meetingReactionPosition.top, meetingReactionPosition.right, meetingReactionPosition.bottom);
        }
    }

    private Rect getMeetingReactionPosition(int i, int i2) {
        Rect rect = new Rect();
        if (this.mIsMainVideo) {
            rect.bottom = (this.mHeight - calcSpace4RaiseHandTip()) - UIUtil.dip2px(this.mContext, 60.0f);
            rect.top = rect.bottom - i2;
            rect.left = (this.mWidth - i) / 2;
            rect.right = rect.left + i;
        } else {
            rect.left = Math.min(this.mWidth / 12, UIUtil.dip2px(this.mContext, 16.0f));
            rect.right = rect.left + i;
            rect.top = Math.min(this.mHeight / 12, UIUtil.dip2px(this.mContext, 16.0f));
            rect.bottom = rect.top + i2;
        }
        return rect;
    }

    public void removeMeetingReaction() {
        if (!this.mIsDestroyed && this.mPiMeetingReaction != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.removePic(this.mRenderInfo, 10)) {
                this.mPiMeetingReaction = null;
                this.meetingReactionAccTxt = "";
            }
        }
    }

    private void showAvatar() {
        int i;
        int i2;
        int i3;
        if (!this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                Bitmap bitmap = null;
                PicInfo picInfo = this.mPiAvatar;
                if (picInfo != null) {
                    i2 = picInfo.bmpWidth;
                    i = this.mPiAvatar.bmpHeight;
                } else {
                    bitmap = createAvatarBitmap();
                    if (bitmap != null) {
                        i2 = bitmap.getWidth();
                        i = bitmap.getHeight();
                    } else {
                        return;
                    }
                }
                if (i2 != 0 && i != 0) {
                    if (this.mAvatarType == 2) {
                        i3 = UIUtil.dip2px(this.mContext, 200.0f);
                    } else {
                        i3 = UIUtil.dip2px(this.mContext, 60.0f);
                    }
                    if (i2 < i3) {
                        i = (i * i3) / i2;
                        if (i < i3) {
                            i2 = (i3 * i3) / i;
                            i = i3;
                        } else {
                            i2 = i3;
                        }
                    }
                    int i4 = this.mWidth;
                    if (i2 > i4) {
                        i = (i * i4) / i2;
                        i2 = i4;
                    }
                    int i5 = this.mHeight;
                    if (i > i5) {
                        i2 = (i2 * i5) / i;
                        i = i5;
                    }
                    int i6 = (this.mWidth - i2) / 2;
                    int i7 = i6 + i2;
                    int i8 = (this.mHeight - i) / 2;
                    int i9 = i8 + i;
                    if (this.mPiAvatar == null) {
                        videoObj.removePic(this.mRenderInfo, 0);
                        long addPic = videoObj.addPic(this.mRenderInfo, 0, bitmap, 255, 0, i6, i8, i7, i9);
                        if (!(addPic == 0 || bitmap == null)) {
                            this.mPiAvatar = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                        }
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    } else {
                        videoObj.movePic2(this.mRenderInfo, 0, i6, i8, i7, i9);
                    }
                    this.mHasAvatar = true;
                    if (!this.mCanShowWaterMark || !this.mContext.isSDKMode()) {
                        showWaterMark(false);
                    } else {
                        showWaterMark(true);
                    }
                }
            }
        }
    }

    private void removeAvatar() {
        if (!this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.removePic(this.mRenderInfo, 0)) {
                this.mPiAvatar = null;
                this.mHasAvatar = false;
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x002e, code lost:
        if (r13.mbUserNameResourceHasLeftPaddingForNetworkStatus == (r13.mNetworkStatus != -1)) goto L_0x0030;
     */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x008a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showUserName() {
        /*
            r13 = this;
            boolean r0 = r13.mIsDestroyed
            if (r0 == 0) goto L_0x0005
            return
        L_0x0005:
            com.zipow.videobox.confapp.ConfMgr r0 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.VideoSessionMgr r1 = r0.getVideoObj()
            if (r1 != 0) goto L_0x0010
            return
        L_0x0010:
            com.zipow.videobox.confapp.IRendererUnit$PicInfo r0 = r13.mPiUserName
            r2 = 1
            r3 = 0
            if (r0 == 0) goto L_0x003a
            boolean r0 = r13.mCanShowAudioOff
            if (r0 == 0) goto L_0x0020
            boolean r0 = r13.mbUserNameResourceHasLeftPaddingForAudioOff
            boolean r4 = r13.mbAudioOff
            if (r0 != r4) goto L_0x003a
        L_0x0020:
            boolean r0 = r13.mCanShowNetworkStatus
            if (r0 == 0) goto L_0x0030
            boolean r0 = r13.mbUserNameResourceHasLeftPaddingForNetworkStatus
            int r4 = r13.mNetworkStatus
            r5 = -1
            if (r4 == r5) goto L_0x002d
            r4 = 1
            goto L_0x002e
        L_0x002d:
            r4 = 0
        L_0x002e:
            if (r0 != r4) goto L_0x003a
        L_0x0030:
            com.zipow.videobox.confapp.IRendererUnit$PicInfo r0 = r13.mPiUserName
            int r0 = r0.bmpWidth
            com.zipow.videobox.confapp.IRendererUnit$PicInfo r4 = r13.mPiUserName
            int r4 = r4.bmpHeight
            r12 = r3
            goto L_0x004d
        L_0x003a:
            r13.mPiUserName = r3
            android.graphics.Bitmap r0 = r13.createUserNameBitmap()
            if (r0 != 0) goto L_0x0043
            return
        L_0x0043:
            int r3 = r0.getWidth()
            int r4 = r0.getHeight()
            r12 = r0
            r0 = r3
        L_0x004d:
            r8 = 0
            int r3 = r13.getHeight()
            int r9 = r3 - r4
            int r11 = r13.getHeight()
            com.zipow.videobox.confapp.IRendererUnit$PicInfo r3 = r13.mPiUserName
            if (r3 != 0) goto L_0x008a
            long r3 = r13.mRenderInfo
            r1.removePic(r3, r2)
            long r2 = r13.mRenderInfo
            r4 = 1
            r6 = 128(0x80, float:1.794E-43)
            r7 = 0
            r5 = r12
            r10 = r0
            long r0 = r1.addPic(r2, r4, r5, r6, r7, r8, r9, r10, r11)
            r2 = 0
            int r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r2 == 0) goto L_0x0084
            if (r12 == 0) goto L_0x0084
            com.zipow.videobox.confapp.IRendererUnit$PicInfo r2 = new com.zipow.videobox.confapp.IRendererUnit$PicInfo
            int r3 = r12.getWidth()
            int r4 = r12.getHeight()
            r2.<init>(r0, r3, r4)
            r13.mPiUserName = r2
        L_0x0084:
            if (r12 == 0) goto L_0x0094
            r12.recycle()
            goto L_0x0094
        L_0x008a:
            long r2 = r13.mRenderInfo
            r4 = 1
            r5 = 0
            r6 = r9
            r7 = r0
            r8 = r11
            r1.movePic2(r2, r4, r5, r6, r7, r8)
        L_0x0094:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.confapp.VideoUnit.showUserName():void");
    }

    @Nullable
    private Bitmap createTextBmp(String str, int i, int i2, int i3) {
        TextPaint textPaint = s_textPaint;
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        int dip2px = UIUtil.dip2px(this.mContext, 6.0f);
        String ellipseName = ellipseName(str, textPaint, ((i2 - i) - i3) - dip2px);
        int measureText = i3 + ((int) textPaint.measureText(ellipseName)) + dip2px + i;
        int i4 = s_textHeight;
        if (measureText <= i2) {
            i2 = measureText;
        }
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i2, i4, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            RectF rectF = new RectF(0.0f, 0.0f, (float) i2, (float) i4);
            Paint paint = new Paint();
            paint.setColor(-16777216);
            canvas.drawRect(rectF, paint);
            canvas.drawText(ellipseName, (float) (i + (dip2px / 2)), ((float) (canvas.getHeight() / 2)) - (((fontMetrics.bottom - fontMetrics.top) / 2.0f) + fontMetrics.top), textPaint);
            return createBitmap;
        } catch (Throwable unused) {
            return null;
        }
    }

    private void removeUserName() {
        if (!this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null && videoObj.removePic(this.mRenderInfo, 1)) {
                this.mPiUserName = null;
            }
        }
    }

    private void showBorder() {
        Bitmap bitmap;
        int i;
        boolean z;
        if (!this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                int width = getWidth() + 0;
                int height = getHeight() + 0;
                PicInfo picInfo = this.mPiBorderLeft;
                Bitmap bitmap2 = null;
                if (picInfo == null || this.mPiBorderRight == null) {
                    Bitmap createVBorderBitmap = createVBorderBitmap();
                    if (createVBorderBitmap != null) {
                        bitmap = createVBorderBitmap;
                        i = createVBorderBitmap.getWidth();
                    } else {
                        return;
                    }
                } else {
                    i = picInfo.bmpWidth;
                    bitmap = null;
                }
                if (this.mPiBorderLeft == null) {
                    videoObj.removePic(this.mRenderInfo, 6);
                    long addPic = videoObj.addPic(this.mRenderInfo, 6, bitmap, 255, 0, 0, 0, i + 0, height);
                    if (addPic != 0) {
                        this.mPiBorderLeft = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                    }
                } else {
                    videoObj.movePic2(this.mRenderInfo, 6, 0, 0, i + 0, height);
                }
                if (this.mPiBorderRight == null) {
                    videoObj.removePic(this.mRenderInfo, 8);
                    long addPic2 = videoObj.addPic(this.mRenderInfo, 8, bitmap, 255, 0, width - i, 0, width, height);
                    if (!(addPic2 == 0 || bitmap == null)) {
                        this.mPiBorderRight = new PicInfo(addPic2, bitmap.getWidth(), bitmap.getHeight());
                    }
                } else {
                    videoObj.movePic2(this.mRenderInfo, 8, width - i, 0, width, height);
                }
                if (this.mPiBorderTop == null || this.mPiBorderBottom == null) {
                    Bitmap createHBorderBitmap = createHBorderBitmap();
                    if (createHBorderBitmap != null) {
                        bitmap2 = createHBorderBitmap;
                    } else {
                        return;
                    }
                }
                if (this.mPiBorderTop == null) {
                    videoObj.removePic(this.mRenderInfo, 7);
                    long addPic3 = videoObj.addPic(this.mRenderInfo, 7, bitmap2, 255, 0, 1, 0, width - 1, i + 0);
                    if (addPic3 != 0) {
                        this.mPiBorderTop = new PicInfo(addPic3, bitmap2.getWidth(), bitmap2.getHeight());
                    }
                } else {
                    videoObj.movePic2(this.mRenderInfo, 7, 0, 0, width, i + 0);
                }
                if (this.mPiBorderBottom == null) {
                    videoObj.removePic(this.mRenderInfo, 9);
                    long addPic4 = videoObj.addPic(this.mRenderInfo, 9, bitmap2, 255, 0, 1, height - i, width - 1, height);
                    if (addPic4 == 0 || bitmap2 == null) {
                        z = true;
                    } else {
                        this.mPiBorderBottom = new PicInfo(addPic4, bitmap2.getWidth(), bitmap2.getHeight());
                        z = true;
                    }
                } else {
                    z = true;
                    videoObj.movePic2(this.mRenderInfo, 9, 0, height - i, width, height);
                }
                this.mHasBorder = z;
                if (bitmap != null) {
                    bitmap.recycle();
                }
                if (bitmap2 != null) {
                    bitmap2.recycle();
                }
            }
        }
    }

    private Bitmap createHBorderBitmap() {
        int i;
        int dip2px = (int) ((float) UIUtil.dip2px(this.mContext, 2.0f));
        if (dip2px <= 0) {
            dip2px = 1;
        }
        Bitmap createBitmap = Bitmap.createBitmap(getWidth(), dip2px, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        switch (this.mBorderType) {
            case 1:
                i = C4558R.color.zm_video_border_active;
                break;
            case 2:
                i = C4558R.color.zm_video_border_locked;
                break;
            default:
                i = C4558R.color.zm_video_border_normal;
                break;
        }
        canvas.drawColor(this.mContext.getResources().getColor(i));
        return createBitmap;
    }

    private Bitmap createVBorderBitmap() {
        int i;
        int dip2px = (int) ((float) UIUtil.dip2px(this.mContext, 2.0f));
        int i2 = 1;
        if (dip2px <= 0) {
            dip2px = 1;
        }
        int height = getHeight();
        if (height > 0) {
            i2 = height;
        }
        Bitmap createBitmap = Bitmap.createBitmap(dip2px, i2, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        switch (this.mBorderType) {
            case 1:
                i = C4558R.color.zm_video_border_active;
                break;
            case 2:
                i = C4558R.color.zm_video_border_locked;
                break;
            default:
                i = C4558R.color.zm_video_border_normal;
                break;
        }
        canvas.drawColor(this.mContext.getResources().getColor(i));
        return createBitmap;
    }

    private void removeBorder() {
        if (this.mHasBorder && !this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                if (videoObj.removePic(this.mRenderInfo, 6)) {
                    this.mPiBorderLeft = null;
                }
                if (videoObj.removePic(this.mRenderInfo, 7)) {
                    this.mPiBorderTop = null;
                }
                if (videoObj.removePic(this.mRenderInfo, 8)) {
                    this.mPiBorderRight = null;
                }
                if (videoObj.removePic(this.mRenderInfo, 9)) {
                    this.mPiBorderBottom = null;
                }
                this.mHasBorder = false;
            }
        }
    }

    private String ellipseName(String str, @NonNull TextPaint textPaint, int i) {
        return StringUtil.ellipseString(str, i, textPaint);
    }

    private void showAudioOff(boolean z) {
        Bitmap bitmap;
        int i;
        int i2;
        boolean z2 = !getCanShowAudioOff() ? false : z;
        if (this.mVideoScene != null) {
            if (this.mIsUserNameVisible && this.mUserId != 0) {
                showUserName();
            }
            if (!this.mIsDestroyed) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    if (!z2) {
                        if (videoObj.removePic(this.mRenderInfo, 3)) {
                            this.mPiAudioOff = null;
                        }
                        return;
                    }
                    PicInfo picInfo = this.mPiAudioOff;
                    if (picInfo != null) {
                        i2 = picInfo.bmpWidth;
                        i = this.mPiAudioOff.bmpHeight;
                        bitmap = null;
                    } else {
                        Bitmap createAudioOffBitmap = createAudioOffBitmap();
                        if (createAudioOffBitmap != null) {
                            int width = createAudioOffBitmap.getWidth();
                            i = createAudioOffBitmap.getHeight();
                            bitmap = createAudioOffBitmap;
                            i2 = width;
                        } else {
                            return;
                        }
                    }
                    int dip2px = UIUtil.dip2px(this.mContext, 2.0f);
                    int networkStatusWidth = (!this.mCanShowNetworkStatus || this.mNetworkStatus == -1) ? dip2px : dip2px + getNetworkStatusWidth();
                    int i3 = i2 + networkStatusWidth;
                    int height = (getHeight() - i) - ((s_textHeight - i) / 2);
                    int i4 = height + i;
                    if (this.mPiAudioOff == null) {
                        videoObj.removePic(this.mRenderInfo, 3);
                        long addPic = videoObj.addPic(this.mRenderInfo, 3, bitmap, 255, 0, networkStatusWidth, height, i3, i4);
                        if (!(addPic == 0 || bitmap == null)) {
                            this.mPiAudioOff = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                        }
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    } else {
                        videoObj.movePic2(this.mRenderInfo, 3, networkStatusWidth, height, i3, i4);
                    }
                }
            }
        }
    }

    private Bitmap createAudioOffBitmap() {
        Drawable drawable = this.mContext.getResources().getDrawable(this.mAudioType == 0 ? C4558R.C4559drawable.zm_audio_off_small : C4558R.C4559drawable.zm_phone_off_small);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public void onUserAudioStatus() {
        if (getCanShowAudioOff()) {
            updateAudioOff();
        }
        checkAudioConnectStatus();
    }

    public void onUserVideoStatus() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            long j = this.mUserId;
            if (j != 0) {
                this.mVideoType = videoObj.getVideoTypeByID(j);
            }
        }
    }

    private void updateAudioOff() {
        CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
        if (userById != null) {
            CmmAudioStatus audioStatusObj = userById.getAudioStatusObj();
            if (audioStatusObj != null) {
                long j = this.mAudioType;
                this.mAudioType = audioStatusObj.getAudiotype();
                this.mbAudioOff = audioStatusObj.getIsMuted() && this.mAudioType != 2;
                if (this.mAudioType != j) {
                    if (this.mVideoScene.getVideoRenderer() != null) {
                        this.mPiAudioOff = null;
                    } else {
                        return;
                    }
                }
                showAudioOff(this.mbAudioOff);
            }
        }
    }

    public void onNetworkStatusChanged() {
        if (getCanShowNetworkStatus()) {
            updateNetworkStatus();
        }
        updateVideoSizeInfo();
    }

    private void updateVideoSizeInfo() {
        if (ZMLog.isLogEnabled() && this.mType == 1 && isUserNameVisible()) {
            CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
            if (userById != null && this.mVideoScene.getVideoRenderer() != null) {
                String userDisplayName = getUserDisplayName(userById);
                if (!StringUtil.isSameString(userDisplayName, this.mUserName)) {
                    this.mPiUserName = null;
                    this.mUserName = userDisplayName;
                    showUserName();
                }
            }
        }
    }

    private void updateNetworkStatus() {
        CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
        if (userById != null) {
            CmmVideoStatus videoStatusObj = userById.getVideoStatusObj();
            if (videoStatusObj != null) {
                int i = this.mNetworkStatus;
                this.mNetworkStatus = videoStatusObj.getVideoQuality();
                if (this.mNetworkStatus != i) {
                    if (this.mVideoScene.getVideoRenderer() != null) {
                        this.mPiNetworkStatus = null;
                    } else {
                        return;
                    }
                }
                boolean z = true;
                boolean z2 = (i == -1 && this.mNetworkStatus != -1) || (i != -1 && this.mNetworkStatus == -1);
                if (this.mNetworkStatus == -1) {
                    z = false;
                }
                showNetworkStatus(z, z2);
            }
        }
    }

    public void updateAvatar() {
        this.mPiAvatar = null;
        CmmUser userById = ConfMgr.getInstance().getUserById(this.mUserId);
        if (userById != null) {
            this.mAvatarSmall = userById.getSmallPicPath();
            this.mAvatarBig = userById.getLocalPicPath();
        }
        if (this.mHasAvatar) {
            showAvatar();
        }
    }

    private void showNetworkStatus(boolean z, boolean z2) {
        Bitmap bitmap;
        int i;
        int i2;
        boolean z3 = !getCanShowNetworkStatus() ? false : z;
        if (this.mVideoScene != null) {
            if (this.mIsUserNameVisible && this.mUserId != 0 && z2) {
                showUserName();
            }
            if (this.mbAudioOff && this.mCanShowAudioOff && z2) {
                showAudioOff(true);
            }
            if (!this.mIsDestroyed) {
                VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                if (videoObj != null) {
                    if (!z3) {
                        if (videoObj.removePic(this.mRenderInfo, 5)) {
                            this.mPiNetworkStatus = null;
                        }
                        return;
                    }
                    PicInfo picInfo = this.mPiNetworkStatus;
                    if (picInfo != null) {
                        i2 = picInfo.bmpWidth;
                        i = this.mPiNetworkStatus.bmpHeight;
                        bitmap = null;
                    } else {
                        Bitmap createNetworkStatusBitmap = createNetworkStatusBitmap();
                        if (createNetworkStatusBitmap != null) {
                            int width = createNetworkStatusBitmap.getWidth();
                            i = createNetworkStatusBitmap.getHeight();
                            bitmap = createNetworkStatusBitmap;
                            i2 = width;
                        } else {
                            return;
                        }
                    }
                    int dip2px = UIUtil.dip2px(this.mContext, 2.0f);
                    int i3 = i2 + dip2px;
                    int height = (getHeight() - i) - ((s_textHeight - i) / 2);
                    int i4 = height + i;
                    if (this.mPiNetworkStatus == null) {
                        videoObj.removePic(this.mRenderInfo, 5);
                        long addPic = videoObj.addPic(this.mRenderInfo, 5, bitmap, 255, 0, dip2px, height, i3, i4);
                        if (!(addPic == 0 || bitmap == null)) {
                            this.mPiNetworkStatus = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                        }
                        if (bitmap != null) {
                            bitmap.recycle();
                        }
                    } else {
                        videoObj.movePic2(this.mRenderInfo, 5, dip2px, height, i3, i4);
                    }
                }
            }
        }
    }

    private Bitmap createNetworkStatusBitmap() {
        int i;
        switch (this.mNetworkStatus) {
            case 0:
                i = C4558R.C4559drawable.zm_network_bad;
                break;
            case 1:
                i = C4558R.C4559drawable.zm_network_normal;
                break;
            default:
                i = C4558R.C4559drawable.zm_network_good;
                break;
        }
        Drawable drawable = this.mContext.getResources().getDrawable(i);
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    private int getTopbarHeight() {
        AbsVideoScene absVideoScene = this.mVideoScene;
        if (absVideoScene == null) {
            return 0;
        }
        ConfActivity confActivity = absVideoScene.getConfActivity();
        if (confActivity == null) {
            return 0;
        }
        return confActivity.getTopBarHeight();
    }

    private int calcSpace4RaiseHandTip() {
        AbsVideoScene absVideoScene = this.mVideoScene;
        if (absVideoScene == null) {
            return 0;
        }
        ConfActivity confActivity = absVideoScene.getConfActivity();
        if (confActivity == null || !RaiseHandTip.isShown(confActivity.getSupportFragmentManager())) {
            return 0;
        }
        return RaiseHandTip.sHeight + UIUtil.dip2px(this.mContext, 5.0f);
    }

    private int getVideoSceneHeight() {
        AbsVideoScene absVideoScene = this.mVideoScene;
        if (absVideoScene == null) {
            return 0;
        }
        return absVideoScene.getHeight();
    }

    private int getVideoViewLocationOnScreenY() {
        if (this.mVideoScene == null) {
            return 0;
        }
        return ZMConfComponentMgr.getInstance().getVideoViewLocationonScrennY();
    }

    private int getWaterMarkTopMargin() {
        int topbarHeight = getTopbarHeight();
        int videoViewLocationOnScreenY = getVideoViewLocationOnScreenY();
        int statusBarHeight = UIUtil.getStatusBarHeight(this.mContext);
        int i = s_waterMarkTopMargin;
        if (getTop() < topbarHeight) {
            return (topbarHeight - getTop()) + s_waterMarkTopMargin;
        }
        return (videoViewLocationOnScreenY + getTop() >= statusBarHeight || !UIUtil.isImmersedModeSupported() || !UIUtil.isPortraitMode(this.mContext)) ? i : (statusBarHeight - getTop()) + s_waterMarkTopMargin;
    }

    private void showWaterMark(boolean z) {
        int i;
        Bitmap bitmap;
        int i2;
        if ((z && !getCanShowWaterMark()) || this.mVideoScene == null) {
            return;
        }
        if ((z || this.mPiWaterMark != null) && !this.mIsDestroyed) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                if (!z) {
                    if (videoObj.removePic(this.mRenderInfo, 4)) {
                        this.mPiWaterMark = null;
                        this.mHasWaterMark = false;
                    }
                    return;
                }
                PicInfo picInfo = this.mPiWaterMark;
                if (picInfo != null) {
                    i = picInfo.bmpWidth;
                    i2 = this.mPiWaterMark.bmpHeight;
                    bitmap = null;
                } else {
                    Bitmap createWaterMarkBitmap = createWaterMarkBitmap();
                    if (createWaterMarkBitmap != null) {
                        int width = createWaterMarkBitmap.getWidth();
                        i2 = createWaterMarkBitmap.getHeight();
                        bitmap = createWaterMarkBitmap;
                        i = width;
                    } else {
                        return;
                    }
                }
                int sqrt = i * i2 > (getWidth() * getHeight()) / 64 ? (int) Math.sqrt((double) (((getWidth() * getHeight()) * i) / (i2 * 64))) : i;
                int i3 = (i2 * sqrt) / i;
                int round = Math.round((float) (((double) getWidth()) * 0.03d));
                int waterMarkTopMargin = getWaterMarkTopMargin();
                int width2 = (getWidth() - round) - sqrt;
                int i4 = width2 + sqrt;
                int i5 = waterMarkTopMargin + i3;
                if (this.mPiWaterMark == null) {
                    videoObj.removePic(this.mRenderInfo, 4);
                    long addPic = videoObj.addPic(this.mRenderInfo, 4, bitmap, 255, 0, width2, waterMarkTopMargin, i4, i5);
                    if (!(addPic == 0 || bitmap == null)) {
                        this.mHasWaterMark = true;
                        this.mPiWaterMark = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                    }
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                } else {
                    videoObj.movePic2(this.mRenderInfo, 4, width2, waterMarkTopMargin, i4, i5);
                }
            }
        }
    }

    private Bitmap createWaterMarkBitmap() {
        Drawable drawable;
        VideoBoxApplication videoBoxApplication = this.mContext;
        if (videoBoxApplication.isSDKMode()) {
            drawable = videoBoxApplication.getResources().getDrawable(C4558R.C4559drawable.zm_watermark_sdk);
        } else {
            drawable = videoBoxApplication.getResources().getDrawable(C4558R.C4559drawable.zm_watermark);
        }
        int intrinsicWidth = drawable.getIntrinsicWidth();
        int intrinsicHeight = drawable.getIntrinsicHeight();
        Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        drawable.draw(canvas);
        return createBitmap;
    }

    public void checkAudioConnectStatus() {
        CmmUser userById = ConfMgr.getInstance().getUserById(getUser());
        if (userById != null) {
            int audioConnectStatus = userById.getAudioConnectStatus();
            if (this.mAudioConnectStatusShowing) {
                this.mCurAudioConnectStatus = audioConnectStatus;
                return;
            }
            if (audioConnectStatus == 1) {
                this.mPiUserName = null;
                this.mAudioConnectStatusShowing = true;
                this.mCurAudioConnectStatus = audioConnectStatus;
                showAudioConnectStatus();
                refreshAudioConnectingStatus();
            }
        }
    }

    public int getAudioConnectStatusWidth() {
        if (this.mCurAudioConnectStatus == -1) {
            return 0;
        }
        return this.mContext.getResources().getDrawable(C4558R.C4559drawable.ic_audio_connect_status_0).getIntrinsicWidth();
    }

    /* access modifiers changed from: private */
    public void refreshAudioConnectingStatus() {
        VideoBoxApplication.getNonNullInstance().runOnMainThreadDelayed(this.mAudioConnectStatusCheckRunnable, 125);
    }

    /* access modifiers changed from: private */
    public void showAudioConnectStatus() {
        if (!this.mIsDestroyed && isVideoShowing() && this.mVideoScene != null) {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                this.mCurInMainVideo = Boolean.valueOf(isMainVideo());
                if (this.mCurInMainVideo != this.mLastInMainVideo) {
                    this.mPiUserName = null;
                }
                if (this.mIsUserNameVisible && this.mUserId != 0) {
                    showUserName();
                }
                int[] iArr = AUDIO_CONNECT_STATUS_IDS;
                int i = this.mAudioConnectStatusResIndex;
                int i2 = iArr[i];
                switch (this.mCurAudioConnectStatus) {
                    case -1:
                    case 0:
                        hiddenAudioConnectStatus();
                        return;
                    case 1:
                        i2 = iArr[i];
                        break;
                    case 2:
                        i2 = C4558R.C4559drawable.ic_audio_connect_status_success;
                        break;
                    case 3:
                        i2 = C4558R.C4559drawable.ic_audio_connect_status_fail;
                        break;
                }
                if (this.mCurInMainVideo.booleanValue()) {
                    ConfActivity confActivity = this.mVideoScene.getConfActivity();
                    if (confActivity != null) {
                        confActivity.refreshMainVideoAudioStatus(this.mLastAudioConnectStatus, this.mCurAudioConnectStatus, i2, this.mUserName);
                    } else {
                        return;
                    }
                } else {
                    showAudioConnectStatusInGallayMode(videoObj, i2);
                }
                this.mLastAudioConnectStatus = this.mCurAudioConnectStatus;
                this.mLastInMainVideo = this.mCurInMainVideo;
            }
        }
    }

    private void showAudioConnectStatusInGallayMode(VideoSessionMgr videoSessionMgr, int i) {
        PicInfo picInfo = this.mPiUserName;
        if (picInfo != null) {
            int i2 = picInfo.bmpWidth;
            PicInfo picInfo2 = this.mPiAudioConnectStatus;
            if (picInfo2 != null) {
                int i3 = picInfo2.bmpWidth;
                int i4 = this.mPiAudioConnectStatus.bmpHeight;
            }
            Bitmap bitmap = BitmapCacheMgr.getInstance().getBitmap(i);
            if (bitmap != null) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                UIUtil.dip2px(this.mContext, 2.0f);
                if (this.mCanShowNetworkStatus && this.mNetworkStatus != -1) {
                    getNetworkStatusWidth();
                }
                int audioConnectStatusWidth = this.mPiUserName.bmpWidth - getAudioConnectStatusWidth();
                int i5 = audioConnectStatusWidth + width;
                int height2 = (getHeight() - height) - ((s_textHeight - height) / 2);
                int i6 = height2 + height;
                videoSessionMgr.removePic(this.mRenderInfo, 11);
                long addPic = videoSessionMgr.addPic(this.mRenderInfo, 11, bitmap, 255, 0, audioConnectStatusWidth, height2, i5, i6);
                if (addPic != 0) {
                    this.mPiAudioConnectStatus = new PicInfo(addPic, bitmap.getWidth(), bitmap.getHeight());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void hiddenAudioConnectStatus() {
        VideoBoxApplication.getNonNullInstance().removeCallbacks(this.mAudioConnectStatusCheckRunnable);
        VideoBoxApplication.getNonNullInstance().removeCallbacks(this.mAudioConnectStatusFinishRunnable);
        this.mAudioConnectStatusShowing = false;
        this.mCurAudioConnectStatus = -1;
        if (isMainVideo()) {
            ConfActivity confActivity = this.mVideoScene.getConfActivity();
            if (confActivity != null) {
                confActivity.hiddenMainVideoAudioStatus();
            }
        } else {
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj != null) {
                if (videoObj.removePic(this.mRenderInfo, 11)) {
                    this.mPiAudioConnectStatus = null;
                }
                if (this.mIsUserNameVisible && this.mUserId != 0) {
                    this.mPiUserName = null;
                    showUserName();
                }
            }
        }
    }

    public String getAccessibilityDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        CmmUser userById = ConfMgr.getInstance().getUserById(getUser());
        if (userById == null) {
            return sb.toString();
        }
        String screenName = userById.getScreenName();
        switch (this.mCurAudioConnectStatus) {
            case 1:
                screenName = this.mContext.getResources().getString(C4558R.string.zm_lbl_someone_is_connecting_audio_and_not_hear_123338, new Object[]{screenName});
                break;
            case 2:
                screenName = this.mContext.getResources().getString(C4558R.string.zm_lbl_someone_connected_audio_123338, new Object[]{screenName});
                break;
            case 3:
                screenName = this.mContext.getResources().getString(C4558R.string.zm_lbl_someone_did_not_connect_audio_123338, new Object[]{screenName});
                break;
        }
        sb.append(screenName);
        sb.append(PreferencesConstants.COOKIE_DELIMITER);
        sb.append(getMeetingReactionAccTxt());
        return sb.toString();
    }
}
