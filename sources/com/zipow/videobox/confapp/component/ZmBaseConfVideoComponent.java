package com.zipow.videobox.confapp.component;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.nydus.VideoCapturer;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import com.zipow.videobox.view.video.VideoRenderer;
import com.zipow.videobox.view.video.VideoSceneMgr;
import com.zipow.videobox.view.video.VideoView;
import com.zipow.videobox.view.video.VideoView.Listener;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.khronos.opengles.GL10;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public abstract class ZmBaseConfVideoComponent extends ZmBaseConfComponent implements Listener, Callback {
    private static final int RENDERER_FPS = 25;
    private static final int RENDERER_FPS_LOW_PERFORMANCE = 15;
    protected static final String TAG = "ZmConfVideoComponent";
    protected ZMAlertDialog askStartVideoDlg;
    @NonNull
    protected Handler mHandler = new Handler();
    @NonNull
    private Handler mOptimizeHandler = new Handler() {
        public void handleMessage(Message message) {
            if (message.what != 4) {
                super.handleMessage(message);
                return;
            }
            List list = (List) ZmBaseConfVideoComponent.this.mUserCmdCaches.get(4);
            if (!CollectionsUtil.isCollectionEmpty(list)) {
                ZmBaseConfVideoComponent.this.onBatchUserVideoStatus(list);
            }
        }
    };
    @Nullable
    protected VideoRenderer mRenderer;
    @Nullable
    protected SurfaceView mSvPreview;
    /* access modifiers changed from: private */
    @NonNull
    public SparseArray<List<Long>> mUserCmdCaches = new SparseArray<>();
    @Nullable
    protected VideoView mVideoView;
    protected boolean mbHasSurface;
    protected boolean mbSendingVideo = false;

    public abstract void muteVideo(boolean z);

    public void onActivityPause() {
    }

    public abstract void onMyVideoStatusChanged();

    public void onSaveInstanceState(@NonNull Bundle bundle) {
    }

    public abstract void refreshFeccUI();

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    public ZmBaseConfVideoComponent(@NonNull ConfActivity confActivity) {
        super(confActivity);
    }

    @Nullable
    public VideoView getmVideoView() {
        return this.mVideoView;
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (!this.mbHasSurface) {
            this.mbHasSurface = true;
            onVideoCaptureSurfaceReady(surfaceHolder);
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mbHasSurface = false;
    }

    public void onActivityCreate(Bundle bundle) {
        if (this.mContext != null) {
            this.mVideoView = (VideoView) this.mContext.findViewById(C4558R.C4560id.videoView);
            this.mSvPreview = (SurfaceView) this.mContext.findViewById(C4558R.C4560id.svPreview);
            this.mbHasSurface = false;
            initVideoView();
        }
    }

    public void onActivityDestroy() {
        this.mOptimizeHandler.removeCallbacksAndMessages(null);
        this.mUserCmdCaches.clear();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mVideoView = null;
        this.mSvPreview = null;
        super.onActivityDestroy();
    }

    public void beforeSurfaceDestroyed() {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.stopAllScenes();
        }
    }

    public void beforeGLContextDestroyed() {
        VideoRenderer videoRenderer = this.mRenderer;
        if (videoRenderer != null) {
            videoRenderer.beforeGLContextDestroyed();
            if (this.mRenderer.isRunning()) {
                this.mRenderer.stopRenderer();
            }
        }
    }

    public void onVideoViewDoubleTap(MotionEvent motionEvent) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onDoubleTap(motionEvent);
        }
    }

    public void onVideoViewScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (!ZMConfComponentMgr.getInstance().isMbEditStatus() && this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onScroll(motionEvent, motionEvent2, f, f2);
        }
    }

    public void onVideoViewFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onFling(motionEvent, motionEvent2, f, f2);
        }
    }

    public void onVideoViewSingleTapConfirmed(MotionEvent motionEvent) {
        if (ConfMgr.getInstance().isConfConnected() && this.mAbsVideoSceneMgr != null && !this.mAbsVideoSceneMgr.onVideoViewSingleTapConfirmed(motionEvent)) {
            ZMConfComponentMgr.getInstance().onVideoViewSingleTapConfirmed(motionEvent);
        }
    }

    public void onVideoViewDown(MotionEvent motionEvent) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onDown(motionEvent);
        }
    }

    public boolean onVideoViewTouchEvent(@NonNull MotionEvent motionEvent) {
        ZMConfComponentMgr.getInstance().onVideoViewTouchEvent(motionEvent);
        return this.mAbsVideoSceneMgr != null && this.mAbsVideoSceneMgr.onTouchEvent(motionEvent);
    }

    public boolean onVideoViewHoverEvent(@NonNull MotionEvent motionEvent) {
        if (this.mAbsVideoSceneMgr == null) {
            return true;
        }
        return this.mAbsVideoSceneMgr.onHoverEvent(motionEvent);
    }

    public void onVideoViewDetachedFromWindow() {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onGLRendererNeedDestroy();
        }
    }

    public void setAbsVideoSceneMgr(@Nullable AbsVideoSceneMgr absVideoSceneMgr) {
        this.mAbsVideoSceneMgr = absVideoSceneMgr;
    }

    private void initVideoView() {
        SurfaceView surfaceView = this.mSvPreview;
        if (surfaceView != null && this.mVideoView != null) {
            surfaceView.getHolder().setType(3);
            this.mRenderer = createVideoRenderer();
            this.mVideoView.setPreserveEGLContextOnPause(true);
            this.mVideoView.setRenderer(this.mRenderer);
            this.mVideoView.setListener(this);
        }
    }

    public void initVideoSceneMgr(@NonNull AbsVideoSceneMgr absVideoSceneMgr) {
        this.mAbsVideoSceneMgr = absVideoSceneMgr;
        absVideoSceneMgr.setVideoView(this.mVideoView);
        absVideoSceneMgr.onConfActivityCreated(this.mContext);
        absVideoSceneMgr.onGLRendererCreated(this.mRenderer);
    }

    @NonNull
    private VideoRenderer createVideoRenderer() {
        return new VideoRenderer(this.mVideoView) {
            @NonNull
            private Runnable idleTask = new Runnable() {
                public void run() {
                    if (ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr != null) {
                        ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr.onIdle();
                    }
                }
            };
            long lastIdleTaskTime = 0;

            public void onDrawFrame(GL10 gl10, VideoRenderer videoRenderer) {
                if (ZmBaseConfVideoComponent.this.mContext != null && ZmBaseConfVideoComponent.this.mContext.isActive() && !ConfUI.getInstance().isLeaveComplete()) {
                    long currentTimeMillis = System.currentTimeMillis();
                    long j = this.lastIdleTaskTime;
                    if (currentTimeMillis < j || currentTimeMillis - j > 500) {
                        this.lastIdleTaskTime = currentTimeMillis;
                        ZmBaseConfVideoComponent.this.mContext.runOnUiThread(this.idleTask);
                    }
                }
            }

            private float getProperFPS() {
                return (HardwareUtil.getCPUKernalNumbers() >= 2 || HardwareUtil.getCPUKernelFrequency(0, 2) >= 1400000) ? 25.0f : 15.0f;
            }

            /* access modifiers changed from: protected */
            public void onGLSurfaceCreated() {
                startRenderer(getProperFPS());
            }

            /* access modifiers changed from: protected */
            public void onGLSurfaceChanged(final int i, final int i2) {
                if (!isRunning()) {
                    startRenderer(getProperFPS());
                }
                if (ZmBaseConfVideoComponent.this.mContext != null) {
                    ZmBaseConfVideoComponent.this.mContext.runOnUiThread(new Runnable() {
                        public void run() {
                            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
                            if (videoObj != null) {
                                videoObj.setGLViewSize(i, i2);
                            }
                            ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                            if (shareObj != null) {
                                shareObj.setGLViewSize(i, i2);
                            }
                            if (!C22262.this.isInitialized()) {
                                C22262.this.initialize();
                            }
                            if (ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr != null) {
                                ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr.onGLRendererChanged(this, i, i2);
                            }
                            if (ZmBaseConfVideoComponent.this.mContext != null && ZmBaseConfVideoComponent.this.mContext.isInMultiWindowMode()) {
                                ZMConfComponentMgr.getInstance().onAnnotateViewSizeChanged();
                            }
                        }
                    });
                }
            }

            /* access modifiers changed from: protected */
            public void onSurfaceNeedDestroy() {
                ZmBaseConfVideoComponent.this.onGLRendererDestroyed();
                super.onSurfaceNeedDestroy();
            }
        };
    }

    /* access modifiers changed from: protected */
    public void onVideoCaptureSurfaceReady(SurfaceHolder surfaceHolder) {
        VideoCapturer.getInstance().setSurfaceHolder(surfaceHolder);
    }

    /* access modifiers changed from: protected */
    public void stopVideo() {
        pauseVideo();
        onVideoCaptureSurfaceInvalidated();
        if (!this.mbHasSurface) {
            SurfaceView surfaceView = this.mSvPreview;
            if (surfaceView != null) {
                surfaceView.getHolder().removeCallback(this);
            }
        }
    }

    private void pauseVideo() {
        if (this.mAbsVideoSceneMgr != null && this.mVideoView != null) {
            this.mAbsVideoSceneMgr.onConfActivityPause();
            this.mVideoView.onPause();
            VideoRenderer videoRenderer = this.mRenderer;
            if (videoRenderer != null) {
                videoRenderer.pauseRenderer();
            }
        }
    }

    private void onVideoCaptureSurfaceInvalidated() {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.stopPreviewDevice();
        }
        VideoCapturer.getInstance().onSurfaceInvalidated();
    }

    public void sinkInControlCameraTypeChanged(long j) {
        refreshFeccUI();
    }

    public void sinkUserVideoParticipantUnmuteLater(long j) {
        if (this.mContext != null) {
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById != null) {
                String screenName = userById.getScreenName();
                if (screenName == null) {
                    screenName = "";
                }
                Toast.makeText(this.mContext.getApplicationContext(), this.mContext.getString(C4558R.string.zm_msg_video_xxx_will_start_video_later, new Object[]{screenName}), 1).show();
            }
        }
    }

    public void sinkUserVideoRequestUnmuteByHost(long j) {
        if (this.mContext != null) {
            int i = C4558R.string.zm_msg_video_host_ask_to_start_video;
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById != null && userById.isCoHost()) {
                i = C4558R.string.zm_msg_video_cohost_ask_to_start_video;
            }
            if (!ConfLocalHelper.isInSilentMode() && !ConfLocalHelper.isDirectShareClient()) {
                ZMAlertDialog zMAlertDialog = this.askStartVideoDlg;
                if (zMAlertDialog == null) {
                    this.askStartVideoDlg = new Builder(this.mContext).setTitle(i).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_start_my_video, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ZmBaseConfVideoComponent.this.muteVideo(false);
                        }
                    }).setNegativeButton(C4558R.string.zm_btn_start_my_video_later, (OnClickListener) new OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ConfMgr.getInstance().handleUserCmd(64, 0);
                        }
                    }).create();
                    this.askStartVideoDlg.show();
                } else if (!zMAlertDialog.isShowing()) {
                    this.askStartVideoDlg.show();
                }
            }
        }
    }

    public void sinkUserVideoMutedByHost(long j) {
        if (this.mContext != null && ConfMgr.getInstance().getVideoObj() != null) {
            muteVideo(true);
            String string = this.mContext.getString(C4558R.string.zm_msg_video_muted_by_host);
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            NormalMessageTip.show(this.mContext.getSupportFragmentManager(), TipMessageType.TIP_VIDEO_MUTED_BY_HOST.name(), (String) null, (userById == null || !userById.isCoHost()) ? string : this.mContext.getString(C4558R.string.zm_msg_video_muted_by_cohost), 3000);
        }
    }

    public void sinkUserVideoQualityChanged(long j) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onUserVideoQualityChanged(j);
        }
    }

    public void sinkUserTalkingVideo(final long j) {
        VideoView videoView = this.mVideoView;
        if (videoView != null) {
            videoView.post(new Runnable() {
                public void run() {
                    if (ZmBaseConfVideoComponent.this.mContext != null && ZmBaseConfVideoComponent.this.mContext.isActive() && ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr != null) {
                        ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr.onUserTalkingVideo(j);
                    }
                }
            });
        }
    }

    public void sinkUserVideoDataSizeChanged(long j) {
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.onUserVideoDataSizeChanged(j);
        }
    }

    public void sinkUserActiveVideoForDeck(final long j) {
        refreshFeccUI();
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null || videoObj.isManualMode() || !ConfShareLocalHelper.isOtherScreenSharing() || !isInNormalVideoScene()) {
            VideoView videoView = this.mVideoView;
            if (videoView != null) {
                videoView.post(new Runnable() {
                    public void run() {
                        if (ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr != null && ZmBaseConfVideoComponent.this.mContext != null && ZmBaseConfVideoComponent.this.mContext.isActive()) {
                            ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr.onUserActiveVideoForDeck(j);
                        }
                    }
                });
            }
        } else {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    VideoSceneMgr videoSceneMgr = (VideoSceneMgr) ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr;
                    if (videoSceneMgr != null) {
                        videoSceneMgr.switchToDefaultScene();
                    }
                }
            }, 1000);
        }
    }

    public void sinkUserVideoStatus(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(j)) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself != null) {
                CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
                if (videoStatusObj != null) {
                    this.mbSendingVideo = videoStatusObj.getIsSending();
                }
            }
        }
        List list = (List) this.mUserCmdCaches.get(4);
        if (list == null) {
            list = new ArrayList();
            this.mUserCmdCaches.put(4, list);
        }
        list.add(Long.valueOf(j));
        this.mOptimizeHandler.removeMessages(4);
        this.mOptimizeHandler.sendEmptyMessageDelayed(4, (long) ZMConfiguration.CONF_FRENQUENCE_EVENT_DELAY);
    }

    public void sinkUserActiveVideo(final long j) {
        VideoView videoView = this.mVideoView;
        if (videoView != null) {
            videoView.post(new Runnable() {
                public void run() {
                    if (ZmBaseConfVideoComponent.this.mContext != null && ZmBaseConfVideoComponent.this.mContext.isActive()) {
                        if (ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr == null) {
                            ZMUtils.printFunctionCallStack("Please note : Exception happens");
                        } else {
                            ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr.onActiveVideoChanged(j);
                            ZmBaseConfVideoComponent.this.mAbsVideoSceneMgr.onActiveVideoChangedRefreshUIDirectly(j);
                        }
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onGLRendererDestroyed() {
        this.mHandler.post(new Runnable() {
            public void run() {
                if (ConfUI.getInstance().isLeaveComplete()) {
                    ConfMgr.getInstance().cleanupConf();
                    VideoBoxApplication.getInstance().stopConfService();
                }
                if (ZmBaseConfVideoComponent.this.mContext != null && ZmBaseConfVideoComponent.this.mContext.isInMultiWindowMode()) {
                    ZMConfComponentMgr.getInstance().onAnnotateViewSizeChanged();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void onBatchUserVideoStatus(@Nullable List<Long> list) {
        if (list != null && !list.isEmpty() && list.size() > 0) {
            if (list.size() >= ZMConfiguration.CONF_CACHE_USER_NUMBERS) {
                ArrayList arrayList = new ArrayList(list.subList(0, ZMConfiguration.CONF_CACHE_USER_NUMBERS));
                onGroupUserVideoStatus(arrayList);
                list.removeAll(arrayList);
                this.mOptimizeHandler.removeMessages(4);
                this.mOptimizeHandler.sendEmptyMessageDelayed(4, (long) ZMConfiguration.CONF_FRENQUENCE_EVENT_DELAY);
            } else {
                onGroupUserVideoStatus(list);
                list.clear();
            }
        }
    }

    private void onGroupUserVideoStatus(@Nullable List<Long> list) {
        boolean z;
        if (this.mContext != null) {
            ConfMgr instance = ConfMgr.getInstance();
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            boolean z2 = false;
            if (list == null || confStatusObj == null) {
                z = false;
            } else {
                z = false;
                for (Long longValue : list) {
                    if (confStatusObj.isMyself(longValue.longValue())) {
                        CmmUser myself = ConfMgr.getInstance().getMyself();
                        if (myself != null) {
                            CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
                            if (videoStatusObj != null && !videoStatusObj.getIsSending() && ConfShareLocalHelper.isSharingOut() && ConfMgr.getInstance().getConfDataHelper().getIsVideoOnBeforeShare()) {
                                Toast.makeText(this.mContext.getApplicationContext(), this.mContext.getString(C4558R.string.zm_msg_share_video_stopped_promt), 1).show();
                            }
                        }
                        z = true;
                    } else {
                        z2 = true;
                    }
                }
            }
            CmmConfContext confContext = instance.getConfContext();
            if (z2 && confContext != null && confContext.isMeetingSupportCameraControl()) {
                refreshFeccUI();
            }
            if (this.mContext != null && this.mContext.isActive()) {
                if (z) {
                    if (this.mAbsVideoSceneMgr != null) {
                        this.mAbsVideoSceneMgr.onMyVideoStatusChanged();
                    }
                    onMyVideoStatusChanged();
                }
                if (this.mAbsVideoSceneMgr != null) {
                    this.mAbsVideoSceneMgr.onGroupUserVideoStatus(list);
                    this.mAbsVideoSceneMgr.shouldSwitchActiveSpeakerView();
                }
            }
        }
    }
}
