package com.zipow.videobox.confapp.component;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import com.zipow.annotate.AnnoDataMgr;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.ZoomShareUI;
import com.zipow.videobox.confapp.ZoomShareUI.IZoomShareUIListener;
import com.zipow.videobox.confapp.ZoomShareUI.SimpleZoomShareUIListener;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.confapp.param.ZMConfRequestConstant;
import com.zipow.videobox.dialog.ShareAlertDialog;
import com.zipow.videobox.dialog.SharePermissionAlertDialog;
import com.zipow.videobox.dialog.conf.SelectURLDialog;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.pdf.PDFManager;
import com.zipow.videobox.ptapp.delegate.PTAppDelegation;
import com.zipow.videobox.ptapp.delegate.PTUIDelegation;
import com.zipow.videobox.share.ScreenShareMgr;
import com.zipow.videobox.share.ScreenShareMgr.Listener;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.ConfShareLocalHelper;
import com.zipow.videobox.util.ImageUtil;
import com.zipow.videobox.util.ZMUtils;
import com.zipow.videobox.view.MoreTip;
import com.zipow.videobox.view.OnPresentRoomView;
import com.zipow.videobox.view.ShareTip;
import com.zipow.videobox.view.bookmark.BookmarkListViewFragment;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.NormalVideoScene;
import com.zipow.videobox.view.video.VideoSceneMgr;
import java.io.File;
import java.util.Locale;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMAdapterOsBugHelper;
import p021us.zoom.androidlib.app.ZMFileListActivity;
import p021us.zoom.androidlib.app.ZMLocalFileListAdapter;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.FileUtils;
import p021us.zoom.androidlib.util.IDownloadFileListener;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.ZMAsyncTask;
import p021us.zoom.androidlib.util.ZMAsyncURLDownloadFile;
import p021us.zoom.androidlib.utils.ZmAppUtils;
import p021us.zoom.thirdparty.box.BoxFileListAdapter;
import p021us.zoom.thirdparty.dropbox.DropboxFileListAdapter;
import p021us.zoom.thirdparty.googledrive.GoogleDriveFileListAdapter;
import p021us.zoom.thirdparty.login.util.IPicker;
import p021us.zoom.thirdparty.onedrive.OneDriveBusinessFileListAdapter;
import p021us.zoom.thirdparty.onedrive.OneDriveFileListAdapter;
import p021us.zoom.thirdparty.onedrive.OneDrivePicker;
import p021us.zoom.videomeetings.C4558R;

class ZmConfShareComponent extends ZmBaseConfShareComponent {
    private static final String[] FILTER_EXTENS = {".jpg", ".png", ".gif", ".bmp", ".jpeg", ".pdf"};
    private static final String PREF_SCREEN_INFO_DATA = "screen_info_data";
    private static final String PREF_SHARE_STATUS = "share_status";
    private static final String TAG = "ZmConfShareComponent";
    @Nullable
    private ProgressDialog mDlgLoadingToShare;
    /* access modifiers changed from: private */
    @Nullable
    public ProgressDialog mDownloadFileWaitingDialog = null;
    @NonNull
    private Listener mListener = new Listener() {
        public void onClickStopScreenShare() {
            ZmConfShareComponent.this.handleClickStopScreenShare();
        }

        public void onAnnoStatusChanged() {
            ZmConfShareComponent.this.handleAnnoStatusChanged();
        }
    };
    @Nullable
    private View mPanelAudioSharing;
    private IPicker mPicker;
    /* access modifiers changed from: private */
    public OnPresentRoomView mPresentRoomLayer;
    @Nullable
    private Intent mScreenInfoData;
    /* access modifiers changed from: private */
    @Nullable
    public Runnable mStartShareRunnable = null;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAsyncURLDownloadFile mTaskDownloadFile;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAsyncTask<Uri, Void, Bitmap> mTaskLoadImageToShare;
    /* access modifiers changed from: private */
    @Nullable
    public ZMAsyncTask<Uri, Void, String> mTaskLoadPdfToShare;
    @NonNull
    private IZoomShareUIListener mZoomShareUIListener = new SimpleZoomShareUIListener() {
        public void OnShareSettingTypeChanged(int i) {
            ZmConfShareComponent.this.onShareSettingTypeChanged();
        }

        public void OnShareSourceSendStatusChanged(long j, boolean z) {
            if (ZmConfShareComponent.this.mShareView != null) {
                ZmConfShareComponent.this.mShareView.setSharePauseStatuChanged(z);
            }
        }

        public void OnShareSourceAnnotationSupportPropertyChanged(long j, boolean z) {
            if (!ZmConfShareComponent.this.isStartingShare() || !ZmConfShareComponent.this.mbPresenter) {
                ZmConfShareComponent.this.checkConfSupportOrEnableAnnotate();
                if (ZmConfShareComponent.this.isInShareVideoScene()) {
                    ZmConfShareComponent.this.changeShareViewVisibility();
                }
            }
        }

        public void OnStartViewPureComputerAudio(long j) {
            ZmConfShareComponent.this.onViewPureComputerAudioStatusChanged(j, true);
        }

        public void OnStopViewPureComputerAudio(long j) {
            ZmConfShareComponent.this.onViewPureComputerAudioStatusChanged(j, false);
        }
    };
    public boolean mbMarkedAsGrabShare = false;

    private class DownloadFileListener implements IDownloadFileListener {
        private Uri mInput;

        public DownloadFileListener(Uri uri, long j, String str) {
            this.mInput = uri;
        }

        public void onDownloadCompleted(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, @Nullable Uri uri, @NonNull String str) {
            if (uri != null && uri == this.mInput) {
                ZmConfShareComponent.this.dismissDownloadFileWaitingDialog();
                if (!StringUtil.isEmptyOrNull(str)) {
                    ZmConfShareComponent.this.shareByPathExtension(str);
                }
            }
        }

        public void onDownloadFailed(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, @Nullable Uri uri) {
            if (uri != null && uri == this.mInput && ZmConfShareComponent.this.mContext != null) {
                ZmConfShareComponent.this.dismissDownloadFileWaitingDialog();
                String path = uri.getPath();
                if (StringUtil.isEmptyOrNull(path)) {
                    ShareAlertDialog.showConfDialog(ZmConfShareComponent.this.mContext.getSupportFragmentManager(), ZmConfShareComponent.this.mContext.getString(C4558R.string.zm_msg_load_file_fail_without_name), false);
                } else {
                    String pathLastName = AndroidAppUtil.getPathLastName(path);
                    ShareAlertDialog.showConfDialog(ZmConfShareComponent.this.mContext.getSupportFragmentManager(), ZmConfShareComponent.this.mContext.getString(C4558R.string.zm_msg_load_file_fail, new Object[]{pathLastName}), false);
                }
            }
        }

        public void onDownloadProgress(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, long j, long j2) {
            ZmConfShareComponent.this.updateProgressWaitingDialog(j, j2);
        }

        public void onDownloadCanceled(ZMAsyncURLDownloadFile zMAsyncURLDownloadFile, @Nullable Uri uri) {
            if (uri != null && uri == this.mInput) {
                ZmConfShareComponent.this.dismissDownloadFileWaitingDialog();
            }
        }
    }

    private boolean isShareRequestCode(int i) {
        switch (i) {
            case 1004:
            case 1005:
            case 1010:
            case 1013:
            case 1014:
            case ZMConfRequestConstant.REQUEST_DOCUMENT_BUSINESS_PICKER /*1099*/:
                return true;
            default:
                return false;
        }
    }

    public void onConfReady() {
    }

    public ZmConfShareComponent(@NonNull ConfActivity confActivity) {
        super(confActivity);
    }

    public void onActivityCreate(Bundle bundle) {
        if (this.mContext != null) {
            super.onActivityCreate(bundle);
            this.mPanelAudioSharing = this.mContext.findViewById(C4558R.C4560id.panelAudioShare);
            this.mPresentRoomLayer = (OnPresentRoomView) this.mContext.findViewById(C4558R.C4560id.presentRoom);
            PTUIDelegation.getInstance().addPresentToRoomStatusListener(this.mPresentRoomLayer);
            if (bundle != null) {
                this.mScreenInfoData = (Intent) bundle.getParcelable(PREF_SCREEN_INFO_DATA);
                this.mShareStatus = bundle.getInt(PREF_SHARE_STATUS, 0);
            }
            initialize();
        }
    }

    public void stopShare() {
        if (!ScreenShareMgr.getInstance().isSharing() || !ConfLocalHelper.isDirectShareClient()) {
            if (this.mShareView != null) {
                this.mShareView.stop();
                changeShareViewVisibility();
            }
            this.mbShareWhiteboard = false;
            if (ScreenShareMgr.getInstance().isSharing()) {
                ShareSessionMgr.setAnnotateDisableWhenStopShare();
            }
            stopShareSession();
            if (ScreenShareMgr.getInstance().isSharing()) {
                this.mListener.onAnnoStatusChanged();
                ScreenShareMgr.getInstance().stopShare();
                ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
                if (shareObj != null) {
                    shareObj.DisableAttendeeAnnotationForMySharedContent(AnnoDataMgr.getInstance().getAttendeeAnnotateDisable());
                }
            }
            this.mbReceiveShareData = false;
            return;
        }
        PTAppDelegation.getInstance().stopPresentToRoom(true);
    }

    public void onActivityDestroy() {
        PTUIDelegation.getInstance().removePresentToRoomStatusListener(this.mPresentRoomLayer);
        Runnable runnable = this.mStartShareRunnable;
        if (runnable != null) {
            this.mPresentRoomLayer.removeCallbacks(runnable);
        }
        if (this.mContext != null && ZMAdapterOsBugHelper.getInstance().isNeedListenOverlayPermissionChanged()) {
            ZMAdapterOsBugHelper.getInstance().stopListenOverlayPermissionChange(this.mContext);
        }
        ScreenShareMgr.getInstance().unInitialize();
        if (!ScreenShareMgr.getInstance().isSharing()) {
            stopShare();
        }
        ZoomShareUI.getInstance().removeListener(this.mZoomShareUIListener);
        if (this.mShareView != null) {
            this.mShareView.unregisterAnnotateListener();
        }
        dismissDownloadFileWaitingDialog();
        dismissLoadingToShareDialog();
        super.onActivityDestroy();
        this.mPanelAudioSharing = null;
        this.mPresentRoomLayer = null;
    }

    public void onModeViewChanged(ZMConfEnumViewMode zMConfEnumViewMode) {
        if (zMConfEnumViewMode == ZMConfEnumViewMode.SILENT_VIEW) {
            if (this.mShareStatus == 2) {
                onMyShareStopped();
                this.mShareStatus = 0;
            }
            OnPresentRoomView onPresentRoomView = this.mPresentRoomLayer;
            if (onPresentRoomView != null) {
                onPresentRoomView.finishShare();
                this.mPresentRoomLayer.setVisibility(8);
            }
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.PRESENT_ROOM_LAYER) {
            if (this.mPresentRoomLayer != null) {
                int confStatus = ConfMgr.getInstance().getConfStatus();
                if ((confStatus == 12 || confStatus == 13) && this.mPresentRoomLayer.canShare() && !isStartingShare()) {
                    this.mPresentRoomLayer.startShare();
                }
                this.mPresentRoomLayer.setVisibility(0);
            }
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.WAITING_JOIN_VIEW) {
            OnPresentRoomView onPresentRoomView2 = this.mPresentRoomLayer;
            if (onPresentRoomView2 != null) {
                onPresentRoomView2.setVisibility(8);
            }
        } else if (zMConfEnumViewMode == ZMConfEnumViewMode.CONF_VIEW && this.mPresentRoomLayer != null && ConfLocalHelper.isDirectShareClient()) {
            this.mPresentRoomLayer.setVisibility(8);
        }
    }

    /* access modifiers changed from: private */
    public boolean isStartingShare() {
        return this.mShareStatus == 2 || this.mShareStatus == 1;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        bundle.putInt(PREF_SHARE_STATUS, this.mShareStatus);
        Intent intent = this.mScreenInfoData;
        if (intent != null) {
            bundle.putParcelable(PREF_SCREEN_INFO_DATA, intent);
        }
    }

    public void onActivityPause() {
        ZMAsyncTask<Uri, Void, Bitmap> zMAsyncTask = this.mTaskLoadImageToShare;
        if (zMAsyncTask != null && !zMAsyncTask.isCancelled()) {
            this.mTaskLoadImageToShare.cancel(true);
        }
        ZMAsyncTask<Uri, Void, String> zMAsyncTask2 = this.mTaskLoadPdfToShare;
        if (zMAsyncTask2 != null && !zMAsyncTask2.isCancelled()) {
            this.mTaskLoadPdfToShare.cancel(true);
        }
        this.mTaskLoadImageToShare = null;
        this.mTaskLoadPdfToShare = null;
        dismissLoadingToShareDialog();
        stopDownloadFileTask();
    }

    public void onActivityResume() {
        if (ConfShareLocalHelper.isSharingOut() && !ScreenShareMgr.getInstance().isSharing() && this.mShareView != null) {
            this.mShareView.resume();
        }
        refreshAudioSharing(false);
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj != null) {
            int shareStatus = shareObj.getShareStatus();
            if (ConfMgr.getInstance().getConfStatusObj() != null && this.mRCFloatView != null && shareStatus == 3) {
                if (ConfLocalHelper.checkRemoteControlPrivilege()) {
                    this.mRCFloatView.showRCFloatView(true, false);
                } else {
                    this.mRCFloatView.showRCFloatView(false, false);
                }
            }
        }
    }

    public boolean onActivityResult(int i, int i2, @Nullable Intent intent) {
        if (this.mContext == null) {
            return false;
        }
        if (ConfShareLocalHelper.isSharingOut() && this.mShareView != null && this.mShareView.onActivityResult(i, i2, intent)) {
            return true;
        }
        if (processShareRequest(i, i2, intent)) {
            if (ConfLocalHelper.isDirectShareClient() && i2 != -1) {
                PTAppDelegation.getInstance().stopPresentToRoom(true);
            }
            return true;
        } else if (i != 1020) {
            return false;
        } else {
            if (ZMAdapterOsBugHelper.getInstance().isNeedListenOverlayPermissionChanged()) {
                ZMAdapterOsBugHelper.getInstance().stopListenOverlayPermissionChange(this.mContext);
            }
            if (!OsUtil.isAtLeastN() || Settings.canDrawOverlays(this.mContext) || (ZMAdapterOsBugHelper.getInstance().isNeedListenOverlayPermissionChanged() && ZMAdapterOsBugHelper.getInstance().ismCanDraw())) {
                shareScreen(this.mScreenInfoData);
                return true;
            }
            if (ConfLocalHelper.isDirectShareClient()) {
                PTAppDelegation.getInstance().stopPresentToRoom(true);
            }
            return true;
        }
    }

    public void sinkConfConnecting() {
        if (this.mContext != null && ConfLocalHelper.isDirectShareClient()) {
            OnPresentRoomView onPresentRoomView = this.mPresentRoomLayer;
            if (onPresentRoomView != null) {
                onPresentRoomView.presentToRoomStatusUpdate(20);
            }
            this.mContext.switchViewTo(ZMConfEnumViewMode.PRESENT_ROOM_LAYER);
        }
    }

    private void initialize() {
        ScreenShareMgr.getInstance().initialize(this.mListener);
        ZoomShareUI.getInstance().addListener(this.mZoomShareUIListener);
    }

    public void onAnnotateViewSizeChanged() {
        if (this.mShareView != null) {
            this.mShareView.onAnnotateViewSizeChanged();
        }
    }

    public void onWBPageChanged(int i, int i2, int i3, int i4) {
        if (this.mShareView != null) {
            this.mShareView.updateWBPageNum(i, i2, i3, i4);
        }
    }

    public void onAnnotateStartedUp(boolean z, long j) {
        if (this.mbShareScreen) {
            ScreenShareMgr.getInstance().onAnnotateStartedUp(z, j);
        } else if (this.mShareView != null) {
            this.mShareView.onAnnotateStartedUp(z, j);
        }
    }

    public void onAnnotateShutDown() {
        if (this.mbShareScreen) {
            ScreenShareMgr.getInstance().onAnnotateShutDown();
        } else if (this.mShareView != null) {
            this.mShareView.onAnnotateShutDown();
        }
    }

    public void setPaddingForTranslucentStatus(int i, int i2, int i3, int i4) {
        OnPresentRoomView onPresentRoomView = this.mPresentRoomLayer;
        if (onPresentRoomView != null) {
            onPresentRoomView.setTitlePadding(i, i2, i3, i4);
        }
    }

    public void onToolbarVisibilityChanged(boolean z) {
        if (isInShareVideoScene() && this.mShareView != null) {
            this.mShareView.setVisibleWithConfToolbar(z);
        }
    }

    private void loadImageToShare(Uri uri) {
        this.mTaskLoadImageToShare = new ZMAsyncTask<Uri, Void, Bitmap>() {
            /* access modifiers changed from: protected */
            @Nullable
            public Bitmap doInBackground(Uri... uriArr) {
                Bitmap bitmap;
                try {
                    bitmap = ImageUtil.translateImageAsSmallBitmap(VideoBoxApplication.getNonNullInstance().getApplicationContext(), uriArr[0], 1638400, false);
                } catch (Exception unused) {
                    bitmap = null;
                }
                if (isCancelled()) {
                    return null;
                }
                return bitmap;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(@Nullable Bitmap bitmap) {
                ZmConfShareComponent.this.mTaskLoadImageToShare = null;
                ZmConfShareComponent.this.dismissLoadingToShareDialog();
                if (bitmap != null) {
                    ZmConfShareComponent.this.startShareImage(bitmap);
                }
            }
        };
        showLoadingToShareDialog();
        this.mTaskLoadImageToShare.execute((Params[]) new Uri[]{uri});
    }

    public void startShareImage(Uri uri, boolean z) {
        if (this.mContext != null && this.mShareView != null) {
            if (!z) {
                loadImageToShare(uri);
            } else if (!this.mShareView.setImageUri(uri)) {
                ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_invalid_image, true);
            } else {
                startShare();
            }
        }
    }

    public void shareByPathExtension(@Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str) && this.mContext != null) {
            String lowerCase = str.toLowerCase(Locale.US);
            if (lowerCase.endsWith(".pdf")) {
                startSharePdf(str);
            } else if (lowerCase.endsWith(".gif") || lowerCase.endsWith(".bmp") || lowerCase.endsWith(".jpg") || lowerCase.endsWith(".png")) {
                startShareImage(Uri.fromFile(new File(str)), true);
            } else {
                ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_unsupported_format, true);
            }
        }
    }

    public void startShareWebview(@Nullable String str) {
        if (this.mContext != null) {
            if (!startShareSession()) {
                ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_start_share_fail, true);
                return;
            }
            changeShareViewVisibility();
            if (this.mAbsVideoSceneMgr != null) {
                this.mAbsVideoSceneMgr.stopAllScenes();
            }
            if (this.mShareView != null) {
                this.mShareView.setUrl(str);
            }
        }
    }

    private void asyncDownloadFile(Uri uri, long j, String str) {
        ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = this.mTaskDownloadFile;
        if (zMAsyncURLDownloadFile != null) {
            zMAsyncURLDownloadFile.cancel(true);
            this.mTaskDownloadFile = null;
        }
        if (this.mContext != null) {
            DownloadFileListener downloadFileListener = new DownloadFileListener(uri, j, str);
            ZMAsyncURLDownloadFile zMAsyncURLDownloadFile2 = new ZMAsyncURLDownloadFile(uri, j, str, downloadFileListener);
            this.mTaskDownloadFile = zMAsyncURLDownloadFile2;
            showDownloadFileWaitingDialog(this.mContext.getString(C4558R.string.zm_msg_download_file_size, new Object[]{FileUtils.toFileSizeString(this.mContext, 0)}));
            this.mTaskDownloadFile.execute((Params[]) new Void[0]);
        }
    }

    public void startShareScreen(@Nullable Intent intent) {
        if (intent != null && this.mContext != null) {
            if (!OsUtil.isAtLeastN() || Settings.canDrawOverlays(this.mContext)) {
                shareScreen(intent);
            } else {
                if (ZMAdapterOsBugHelper.getInstance().isNeedListenOverlayPermissionChanged()) {
                    ZMAdapterOsBugHelper.getInstance().startListenOverlayPermissionChange(this.mContext);
                }
                StringBuilder sb = new StringBuilder();
                sb.append("package:");
                sb.append(ZmAppUtils.getHostPackageName(this.mContext));
                Intent intent2 = new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse(sb.toString()));
                this.mScreenInfoData = intent;
                if (!ActivityStartHelper.startActivityForResult((Activity) this.mContext, intent2, 1020)) {
                    ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_start_share_fail, true);
                }
            }
        }
    }

    public void beforeShrinkShareViewSize() {
        if (this.mShareView != null) {
            this.mShareView.stop();
        }
    }

    public boolean isAnnotationDrawingViewVisible() {
        return this.mShareView != null && this.mShareView.isDrawing();
    }

    /* JADX WARNING: type inference failed for: r4v2 */
    /* JADX WARNING: type inference failed for: r4v3, types: [android.net.Uri] */
    /* JADX WARNING: type inference failed for: r4v4, types: [android.net.Uri] */
    /* JADX WARNING: type inference failed for: r4v5, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r4v6, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r4v7, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v9 */
    /* JADX WARNING: type inference failed for: r4v10 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v2
      assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.lang.String, android.net.Uri]
      uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], android.net.Uri, java.lang.String]
      mth insns count: 175
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 4 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean processShareRequest(int r8, int r9, @androidx.annotation.Nullable android.content.Intent r10) {
        /*
            r7 = this;
            com.zipow.videobox.ConfActivity r0 = r7.mContext
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            com.zipow.videobox.ConfActivity r0 = r7.mContext
            androidx.fragment.app.FragmentManager r0 = r0.getSupportFragmentManager()
            com.zipow.videobox.dialog.SharePermissionAlertDialog r2 = r7.buildShareAlertDialogIfNeed()
            boolean r3 = r7.mbMarkedAsGrabShare
            boolean r4 = r7.isShareRequestCode(r8)
            if (r4 == 0) goto L_0x0203
            r7.mbMarkedAsGrabShare = r1
            boolean r4 = com.zipow.videobox.util.ConfLocalHelper.isInSilentMode()
            r5 = 1
            if (r4 == 0) goto L_0x0022
            return r5
        L_0x0022:
            r4 = 0
            r6 = -1
            switch(r8) {
                case 1004: goto L_0x01a9;
                case 1005: goto L_0x014f;
                case 1010: goto L_0x00b9;
                case 1013: goto L_0x00a3;
                case 1014: goto L_0x0029;
                case 1099: goto L_0x0029;
                default: goto L_0x0027;
            }
        L_0x0027:
            goto L_0x0202
        L_0x0029:
            if (r9 != 0) goto L_0x002d
            goto L_0x0202
        L_0x002d:
            if (r9 == r6) goto L_0x003c
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_msg_load_file_fail_without_name
            java.lang.String r8 = r8.getString(r9)
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r0, r8, r1)
            goto L_0x0202
        L_0x003c:
            us.zoom.thirdparty.login.util.IPicker r2 = r7.mPicker
            if (r2 != 0) goto L_0x004f
            java.lang.String[] r2 = FILTER_EXTENS
            r3 = 1099(0x44b, float:1.54E-42)
            if (r8 != r3) goto L_0x0048
            r3 = 1
            goto L_0x0049
        L_0x0048:
            r3 = 0
        L_0x0049:
            us.zoom.thirdparty.login.util.IPicker r2 = p021us.zoom.thirdparty.onedrive.OneDrivePicker.createPicker(r8, r2, r3)
            r7.mPicker = r2
        L_0x004f:
            if (r10 != 0) goto L_0x0053
            goto L_0x0202
        L_0x0053:
            us.zoom.thirdparty.login.util.IPicker r2 = r7.mPicker
            us.zoom.thirdparty.login.util.IPickerResult r8 = r2.getPickerResult(r8, r9, r10)
            if (r8 != 0) goto L_0x0068
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_msg_load_file_fail_without_name
            java.lang.String r8 = r8.getString(r9)
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r0, r8, r1)
            goto L_0x0202
        L_0x0068:
            boolean r9 = r8.acceptFileType()
            if (r9 != 0) goto L_0x007b
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_alert_unsupported_format
            java.lang.String r8 = r8.getString(r9)
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r0, r8, r1)
            goto L_0x0202
        L_0x007b:
            android.net.Uri r9 = r8.getLink()
            boolean r10 = r8.isLocal()
            if (r10 == 0) goto L_0x008e
            java.lang.String r8 = r9.getPath()
            r7.shareByPathExtension(r8)
            goto L_0x0202
        L_0x008e:
            java.lang.String r10 = com.zipow.cmmlib.AppUtil.getCachePath()
            java.lang.String r0 = r8.getName()
            java.lang.String r10 = com.zipow.cmmlib.AppUtil.getShareCachePathByExtension(r10, r0)
            long r0 = r8.getSize()
            r7.asyncDownloadFile(r9, r0, r10)
            goto L_0x0202
        L_0x00a3:
            if (r9 == r6) goto L_0x00a7
            goto L_0x0202
        L_0x00a7:
            if (r2 == 0) goto L_0x00b4
            if (r3 != 0) goto L_0x00b4
            r8 = 4
            r2.setShareInfo(r8, r10)
            r2.show(r0)
            goto L_0x0202
        L_0x00b4:
            r7.startShareScreen(r10)
            goto L_0x0202
        L_0x00b9:
            if (r9 != r6) goto L_0x0128
            if (r10 != 0) goto L_0x00bf
            goto L_0x0202
        L_0x00bf:
            android.net.Uri r8 = r10.getData()
            if (r8 == 0) goto L_0x00cc
            com.zipow.videobox.ConfActivity r9 = r7.mContext
            java.lang.String r4 = p021us.zoom.androidlib.util.FileUtils.getPathFromUri(r9, r8)
            goto L_0x00d8
        L_0x00cc:
            android.os.Bundle r9 = r10.getExtras()
            if (r9 == 0) goto L_0x00d8
            java.lang.String r10 = "selected_file_path"
            java.lang.String r4 = r9.getString(r10)
        L_0x00d8:
            boolean r9 = p021us.zoom.androidlib.util.FileUtils.isInvalid(r4, r8)
            if (r9 == 0) goto L_0x00e6
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_alert_invlid_url
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r8, r0, r9, r5)
            return r5
        L_0x00e6:
            com.zipow.videobox.VideoBoxApplication r9 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            java.lang.String r9 = r9.getPackageName()
            boolean r9 = p021us.zoom.androidlib.util.FileUtils.isLocalPath(r4, r9)
            r10 = 2
            if (r9 == 0) goto L_0x0106
            if (r2 == 0) goto L_0x0101
            if (r3 != 0) goto L_0x0101
            r2.setShareInfo(r10, r4, r5)
            r2.show(r0)
            goto L_0x0202
        L_0x0101:
            r7.shareByPathExtension(r4)
            goto L_0x0202
        L_0x0106:
            if (r2 == 0) goto L_0x0112
            if (r3 != 0) goto L_0x0112
            r2.setShareInfo(r10, r4, r1)
            r2.show(r0)
            goto L_0x0202
        L_0x0112:
            com.zipow.videobox.pdf.PDFManager r9 = com.zipow.videobox.pdf.PDFManager.getInstance()
            com.zipow.videobox.ConfActivity r10 = r7.mContext
            boolean r9 = r9.isValidPDFUri(r10, r8)
            if (r9 == 0) goto L_0x0123
            r7.startSharePDF(r8, r1)
            goto L_0x0202
        L_0x0123:
            r7.startShareImage(r8, r1)
            goto L_0x0202
        L_0x0128:
            if (r9 != 0) goto L_0x0202
            if (r10 != 0) goto L_0x012e
            goto L_0x0202
        L_0x012e:
            android.os.Bundle r8 = r10.getExtras()
            if (r8 != 0) goto L_0x0136
            goto L_0x0202
        L_0x0136:
            java.lang.String r9 = "failed_promt"
            java.lang.String r8 = r8.getString(r9)
            boolean r9 = p021us.zoom.androidlib.util.StringUtil.isEmptyOrNull(r8)
            if (r9 == 0) goto L_0x014a
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_alert_auth_token_failed_msg
            java.lang.String r8 = r8.getString(r9)
        L_0x014a:
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r0, r8, r1)
            goto L_0x0202
        L_0x014f:
            if (r9 != r6) goto L_0x0202
            if (r10 != 0) goto L_0x0154
            return r5
        L_0x0154:
            android.os.Bundle r8 = r10.getExtras()
            if (r8 != 0) goto L_0x015b
            return r5
        L_0x015b:
            java.lang.String r9 = "bookmark_url"
            java.lang.String r8 = r8.getString(r9)
            if (r8 == 0) goto L_0x01a1
            java.lang.String r9 = ""
            java.lang.String r10 = r8.trim()
            boolean r9 = r9.equals(r10)
            if (r9 == 0) goto L_0x0170
            goto L_0x01a1
        L_0x0170:
            java.lang.String r9 = "http://"
            boolean r9 = r8.startsWith(r9)
            if (r9 != 0) goto L_0x0191
            java.lang.String r9 = "https://"
            boolean r9 = r8.startsWith(r9)
            if (r9 != 0) goto L_0x0191
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            java.lang.String r10 = "http://"
            r9.append(r10)
            r9.append(r8)
            java.lang.String r8 = r9.toString()
        L_0x0191:
            if (r2 == 0) goto L_0x019d
            if (r3 != 0) goto L_0x019d
            r9 = 3
            r2.setShareInfo(r9, r8, r5)
            r2.show(r0)
            goto L_0x0202
        L_0x019d:
            r7.startShareWebview(r8)
            goto L_0x0202
        L_0x01a1:
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_alert_invlid_url
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r8, r0, r9, r5)
            return r5
        L_0x01a9:
            if (r10 == 0) goto L_0x01af
            android.net.Uri r4 = r10.getData()
        L_0x01af:
            if (r9 != r6) goto L_0x0202
            if (r4 == 0) goto L_0x0202
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            java.lang.String r8 = p021us.zoom.androidlib.util.FileUtils.getPathFromUri(r8, r4)
            boolean r9 = p021us.zoom.androidlib.util.FileUtils.isInvalid(r8, r4)
            if (r9 == 0) goto L_0x01c7
            com.zipow.videobox.ConfActivity r8 = r7.mContext
            int r9 = p021us.zoom.videomeetings.C4558R.string.zm_alert_invalid_image
            com.zipow.videobox.dialog.ShareAlertDialog.showConfDialog(r8, r0, r9, r5)
            return r5
        L_0x01c7:
            com.zipow.videobox.VideoBoxApplication r9 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            java.lang.String r9 = r9.getPackageName()
            boolean r9 = p021us.zoom.androidlib.util.FileUtils.isLocalPath(r8, r9)
            if (r9 == 0) goto L_0x01e4
            if (r2 == 0) goto L_0x01e0
            if (r3 != 0) goto L_0x01e0
            r2.setShareInfo(r5, r8, r5)
            r2.show(r0)
            goto L_0x0202
        L_0x01e0:
            r7.shareByPathExtension(r8)
            goto L_0x0202
        L_0x01e4:
            if (r2 == 0) goto L_0x01ef
            if (r3 != 0) goto L_0x01ef
            r2.setShareInfo(r5, r8, r1)
            r2.show(r0)
            goto L_0x0202
        L_0x01ef:
            com.zipow.videobox.pdf.PDFManager r8 = com.zipow.videobox.pdf.PDFManager.getInstance()
            com.zipow.videobox.ConfActivity r9 = r7.mContext
            boolean r8 = r8.isValidPDFUri(r9, r4)
            if (r8 == 0) goto L_0x01ff
            r7.startSharePDF(r4, r1)
            goto L_0x0202
        L_0x01ff:
            r7.startShareImage(r4, r1)
        L_0x0202:
            return r5
        L_0x0203:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.confapp.component.ZmConfShareComponent.processShareRequest(int, int, android.content.Intent):boolean");
    }

    public void selectShareType(@NonNull ShareOptionType shareOptionType) {
        if (this.mContext != null && checkShareNetWorkForReady(this.mContext, shareOptionType)) {
            FragmentManager supportFragmentManager = this.mContext.getSupportFragmentManager();
            ShareTip.dismiss(supportFragmentManager);
            ZMConfEventTracking.logShareInMeeting(shareOptionType);
            switch (shareOptionType) {
                case SHARE_IMAGE:
                    AndroidAppUtil.selectImageNoDefault((Activity) this.mContext, C4558R.string.zm_select_a_image, 1004);
                    break;
                case SHARE_URL:
                    SelectURLDialog.show(supportFragmentManager);
                    break;
                case SHARE_BOOKMARK:
                    BookmarkListViewFragment.showAsActivity(this.mContext, new Bundle(), 1005);
                    break;
                case SHARE_BOX:
                    ZMFileListActivity.startFileListActivity((Activity) this.mContext, BoxFileListAdapter.class, 1010, FILTER_EXTENS, (String) null, C4558R.string.zm_btn_share, this.mContext.getString(C4558R.string.zm_msg_file_supported_type_prompt));
                    break;
                case SHARE_DROPBOX:
                    ZMFileListActivity.startFileListActivity((Activity) this.mContext, DropboxFileListAdapter.class, 1010, FILTER_EXTENS, (String) null, C4558R.string.zm_btn_share, this.mContext.getString(C4558R.string.zm_msg_file_supported_type_prompt), ZMUtils.getAuthenticator());
                    break;
                case SHARE_NATIVE_FILE:
                    if (!OsUtil.isAtLeastQ()) {
                        ZMFileListActivity.startFileListActivity((Activity) this.mContext, ZMLocalFileListAdapter.class, 1010, FILTER_EXTENS, (String) null, C4558R.string.zm_btn_share, this.mContext.getString(C4558R.string.zm_msg_file_supported_type_prompt));
                        break;
                    } else {
                        openSystemSAF();
                        break;
                    }
                case SHARE_ONE_DRIVE:
                    if (!OneDrivePicker.hasPicker(this.mContext, false)) {
                        ZMFileListActivity.startFileListActivity((Activity) this.mContext, OneDriveFileListAdapter.class, 1010, FILTER_EXTENS, (String) null, C4558R.string.zm_btn_share, this.mContext.getString(C4558R.string.zm_msg_file_supported_type_prompt));
                        break;
                    } else {
                        this.mPicker = OneDrivePicker.createPicker(1014, FILTER_EXTENS, false);
                        this.mPicker.startPicking((ZMActivity) this.mContext);
                        break;
                    }
                case SHARE_ONE_DRIVE_BUSINESS:
                    if (!OneDrivePicker.hasPicker(this.mContext, true)) {
                        ZMFileListActivity.startFileListActivity((Activity) this.mContext, OneDriveBusinessFileListAdapter.class, 1010, FILTER_EXTENS, (String) null, C4558R.string.zm_btn_share, this.mContext.getString(C4558R.string.zm_msg_file_supported_type_prompt));
                        break;
                    } else {
                        this.mPicker = OneDrivePicker.createPicker(ZMConfRequestConstant.REQUEST_DOCUMENT_BUSINESS_PICKER, FILTER_EXTENS, true);
                        this.mPicker.startPicking((ZMActivity) this.mContext);
                        break;
                    }
                case SHARE_GOOGLE_DRIVE:
                    ZMFileListActivity.startFileListActivity((Activity) this.mContext, GoogleDriveFileListAdapter.class, 1010, FILTER_EXTENS, (String) null, C4558R.string.zm_btn_share, this.mContext.getString(C4558R.string.zm_msg_file_supported_type_prompt));
                    break;
                case SHARE_SCREEN:
                    ScreenShareMgr.getInstance().setIsCustomShare(false);
                    askScreenSharePermission();
                    break;
                case SHARE_WHITEBOARD:
                    startShareWhiteboard();
                    break;
                case SHARE_CUSTOM_SCREEN:
                    ScreenShareMgr.getInstance().setIsCustomShare(true);
                    askScreenSharePermission();
                    break;
            }
        }
    }

    private void openSystemSAF() {
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("*/*");
        ActivityStartHelper.startActivityForResult((Activity) this.mContext, intent, 1010);
    }

    public void showShareChoice() {
        if (this.mContext != null) {
            SharePermissionAlertDialog buildShareAlertDialogIfNeed = buildShareAlertDialogIfNeed();
            if (buildShareAlertDialogIfNeed != null) {
                buildShareAlertDialogIfNeed.show(this.mContext.getSupportFragmentManager());
            } else {
                showShareTip();
            }
        }
    }

    public void refreshAudioSharing(boolean z) {
        if (this.mContext == null || this.mAbsVideoSceneMgr == null) {
            ZMUtils.printFunctionCallStack("Please note : Exception happens");
            return;
        }
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        VideoSceneMgr videoSceneMgr = (VideoSceneMgr) this.mAbsVideoSceneMgr;
        if (shareObj == null || videoSceneMgr == null) {
            showAudioSharingPrompt(false, false);
            return;
        }
        CmmUser userById = ConfMgr.getInstance().getUserById(shareObj.getPureComputerAudioSharingUserID());
        if (userById == null) {
            showAudioSharingPrompt(false, false);
            return;
        }
        TextView textView = (TextView) this.mContext.findViewById(C4558R.C4560id.txtAudioShareInfo);
        if (textView != null) {
            if (!shareObj.isViewingPureComputerAudio()) {
                showAudioSharingPrompt(false, false);
                return;
            }
            AbsVideoScene activeScene = videoSceneMgr.getActiveScene();
            if (activeScene != null) {
                if (!(activeScene instanceof NormalVideoScene) || !ConfShareLocalHelper.isOtherPureAudioSharing() || this.mContext.isToolbarShowing()) {
                    showAudioSharingPrompt(false, z);
                } else {
                    showAudioSharingPrompt(true, z);
                    String screenName = userById.getScreenName();
                    if (StringUtil.isEmptyOrNull(screenName)) {
                        textView.setText(userById.getEmail());
                    } else {
                        textView.setText(this.mContext.getString(C4558R.string.zm_lbl_someone_is_sharing_audio_41468, new Object[]{screenName}));
                    }
                    textView.setCompoundDrawables(null, null, null, null);
                }
            }
        }
    }

    private void startShareWhiteboard() {
        this.mbShareWhiteboard = true;
        if (this.mShareView != null) {
            this.mShareView.setWhiteboardBackground();
            startShare();
        }
    }

    /* access modifiers changed from: private */
    public void onShareSettingTypeChanged() {
        if (this.mContext != null) {
            ShareTip.dismiss(this.mContext.getSupportFragmentManager());
        }
    }

    /* access modifiers changed from: private */
    public void onViewPureComputerAudioStatusChanged(long j, boolean z) {
        doAccessiblityForViewAudioSharingStatus(this.mShareView, j, z);
        refreshAudioSharing(true);
    }

    private void doAccessiblityForViewAudioSharingStatus(View view, long j, boolean z) {
        String str;
        if (this.mContext != null && AccessibilityUtil.isSpokenFeedbackEnabled(this.mContext)) {
            CmmUser userById = ConfMgr.getInstance().getUserById(j);
            if (userById != null) {
                if (z) {
                    str = this.mContext.getString(C4558R.string.zm_accessibility_start_view_audio_sharing_41468, new Object[]{userById.getScreenName()});
                } else {
                    str = this.mContext.getString(C4558R.string.zm_accessibility_stop_view_audio_sharing_41468, new Object[]{userById.getScreenName()});
                }
                AccessibilityUtil.announceForAccessibilityCompat(view, (CharSequence) str);
            }
        }
    }

    private void showAudioSharingPrompt(boolean z, boolean z2) {
        View view = this.mPanelAudioSharing;
        if (view != null) {
            if (z && view.getVisibility() != 0) {
                this.mPanelAudioSharing.setVisibility(0);
                if (z2) {
                    this.mPanelAudioSharing.startAnimation(AnimationUtils.loadAnimation(this.mContext, C4558R.anim.zm_fade_in));
                }
            } else if (!z && this.mPanelAudioSharing.getVisibility() == 0) {
                this.mPanelAudioSharing.setVisibility(8);
                if (z2) {
                    this.mPanelAudioSharing.startAnimation(AnimationUtils.loadAnimation(this.mContext, C4558R.anim.zm_fade_out));
                }
            }
        }
    }

    private boolean stopShareSession() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.stopShare();
    }

    /* access modifiers changed from: private */
    public void handleClickStopScreenShare() {
        if (!ScreenShareMgr.getInstance().isSharing() || !ConfLocalHelper.isDirectShareClient()) {
            stopShare();
            if (this.mContext != null) {
                Intent intent = new Intent(this.mContext, IntegrationActivity.class);
                intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
                ActivityStartHelper.startActivityForeground(this.mContext, intent);
                return;
            }
            return;
        }
        stopShare();
    }

    /* access modifiers changed from: private */
    public void handleAnnoStatusChanged() {
        if (this.mContext != null) {
            MoreTip.updateIfExists(this.mContext.getSupportFragmentManager());
        }
    }

    public void startSharePDF(Uri uri, boolean z) {
        if (!z) {
            loadPdfToShare(uri);
        } else if (uri.toString().startsWith(File.separator)) {
            startSharePdf(uri.toString());
        } else if (this.mContext != null) {
            ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_invlid_url, true);
        }
    }

    private void loadPdfToShare(Uri uri) {
        this.mTaskLoadPdfToShare = new ZMAsyncTask<Uri, Void, String>() {
            /* access modifiers changed from: protected */
            @Nullable
            public String doInBackground(Uri... uriArr) {
                return isCancelled() ? "" : PDFManager.getInstance().savePDFToLocalFile(ZmConfShareComponent.this.mContext, uriArr[0]);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(@Nullable String str) {
                ZmConfShareComponent.this.mTaskLoadPdfToShare = null;
                ZmConfShareComponent.this.dismissLoadingToShareDialog();
                if (!TextUtils.isEmpty(str)) {
                    ZmConfShareComponent.this.startSharePdf(str);
                }
            }
        };
        showLoadingToShareDialog();
        this.mTaskLoadPdfToShare.execute((Params[]) new Uri[]{uri});
    }

    /* access modifiers changed from: private */
    public void startSharePdf(String str) {
        if (this.mContext != null && this.mShareView != null && !StringUtil.isEmptyOrNull(str)) {
            if (!this.mShareView.setPdf(str, null)) {
                ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_invalid_pdf, true);
            } else {
                startShare();
            }
        }
    }

    private boolean startShare() {
        if (this.mContext == null || this.mShareView == null) {
            return false;
        }
        if (!startShareSession()) {
            ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_start_share_fail, true);
            return false;
        }
        changeShareViewVisibility();
        if (this.mAbsVideoSceneMgr != null) {
            this.mAbsVideoSceneMgr.stopAllScenes();
        }
        return true;
    }

    private boolean startShareSession() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return false;
        }
        return shareObj.startShare();
    }

    public void showShareTip() {
        if (this.mContext != null) {
            ShareTip.show(this.mContext.getSupportFragmentManager(), this.mContext.isBottombarShowing() ? C4558R.C4560id.btnShare : 0);
        }
    }

    private boolean checkShareNetWorkForReady(@NonNull ZMActivity zMActivity, ShareOptionType shareOptionType) {
        if ((shareOptionType != ShareOptionType.SHARE_ONE_DRIVE && shareOptionType != ShareOptionType.SHARE_ONE_DRIVE_BUSINESS && shareOptionType != ShareOptionType.SHARE_DROPBOX && shareOptionType != ShareOptionType.SHARE_BOX && shareOptionType != ShareOptionType.SHARE_GOOGLE_DRIVE) || NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            return true;
        }
        SimpleMessageDialog.newInstance(C4558R.string.zm_alert_network_disconnected).show(zMActivity.getSupportFragmentManager(), SimpleMessageDialog.class.getName());
        return false;
    }

    private void askScreenSharePermission() {
        if (this.mContext != null && OsUtil.isAtLeastL()) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager) this.mContext.getSystemService("media_projection");
            if (mediaProjectionManager != null) {
                if (AndroidAppUtil.hasActivityForIntent(this.mContext, mediaProjectionManager.createScreenCaptureIntent())) {
                    try {
                        ActivityStartHelper.startActivityForResult((Activity) this.mContext, mediaProjectionManager.createScreenCaptureIntent(), 1013);
                    } catch (Exception unused) {
                    }
                }
            }
        }
    }

    @Nullable
    private SharePermissionAlertDialog buildShareAlertDialogIfNeed() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        SharePermissionAlertDialog sharePermissionAlertDialog = null;
        if (myself == null || ConfMgr.getInstance().getConfContext() == null) {
            return null;
        }
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null) {
            return null;
        }
        int shareSettingType = shareObj.getShareSettingType();
        boolean z = false;
        boolean z2 = shareSettingType == 2;
        boolean z3 = shareSettingType == 3;
        boolean isShareLocked = ConfMgr.getInstance().isShareLocked();
        boolean z4 = myself.isHost() || myself.isCoHost() || myself.isBOModerator();
        boolean isViewingPureComputerAudio = shareObj.isViewingPureComputerAudio();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isShareDisabledByInfoBarrier()) {
            z = true;
        }
        if (z4 || (!isShareLocked && z2)) {
            if ((ConfShareLocalHelper.isOtherScreenSharing() || ConfShareLocalHelper.isOtherPureAudioSharing()) && (!z4 || !z3)) {
                sharePermissionAlertDialog = SharePermissionAlertDialog.createAlertDialog(3, isViewingPureComputerAudio);
            }
        } else if (isShareLocked) {
            sharePermissionAlertDialog = SharePermissionAlertDialog.createAlertDialog(1, isViewingPureComputerAudio);
        } else if (ConfShareLocalHelper.isOtherScreenSharing() || ConfShareLocalHelper.isOtherPureAudioSharing()) {
            sharePermissionAlertDialog = SharePermissionAlertDialog.createAlertDialog(2, isViewingPureComputerAudio);
        } else if (z) {
            sharePermissionAlertDialog = SharePermissionAlertDialog.createAlertDialog(4, isViewingPureComputerAudio);
        }
        return sharePermissionAlertDialog;
    }

    private void showLoadingToShareDialog() {
        if (this.mContext != null) {
            this.mDlgLoadingToShare = new ProgressDialog(this.mContext);
            this.mDlgLoadingToShare.setIndeterminate(true);
            this.mDlgLoadingToShare.setMessage(this.mContext.getString(C4558R.string.zm_msg_loading_image_to_share));
            this.mDlgLoadingToShare.show();
        }
    }

    /* access modifiers changed from: private */
    public void dismissLoadingToShareDialog() {
        ProgressDialog progressDialog = this.mDlgLoadingToShare;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        this.mDlgLoadingToShare = null;
    }

    public void startShareImage(Bitmap bitmap) {
        if (this.mContext != null && this.mShareView != null) {
            if (!this.mShareView.setImageBitmap(bitmap)) {
                ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_invalid_image, true);
            } else {
                startShare();
            }
        }
    }

    private void showDownloadFileWaitingDialog(String str) {
        if (this.mDownloadFileWaitingDialog == null && this.mContext != null) {
            this.mDownloadFileWaitingDialog = new ProgressDialog(this.mContext);
            this.mDownloadFileWaitingDialog.setOnCancelListener(new OnCancelListener() {
                public void onCancel(DialogInterface dialogInterface) {
                    if (ZmConfShareComponent.this.mTaskDownloadFile != null && !ZmConfShareComponent.this.mTaskDownloadFile.isCancelled()) {
                        ZmConfShareComponent.this.mTaskDownloadFile.cancel(true);
                    }
                    ZmConfShareComponent.this.mTaskDownloadFile = null;
                    ZmConfShareComponent.this.mDownloadFileWaitingDialog = null;
                }
            });
            this.mDownloadFileWaitingDialog.requestWindowFeature(1);
            this.mDownloadFileWaitingDialog.setMessage(str);
            this.mDownloadFileWaitingDialog.setCanceledOnTouchOutside(false);
            this.mDownloadFileWaitingDialog.setCancelable(true);
            this.mDownloadFileWaitingDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void dismissDownloadFileWaitingDialog() {
        ProgressDialog progressDialog = this.mDownloadFileWaitingDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
            this.mDownloadFileWaitingDialog = null;
        }
    }

    /* access modifiers changed from: private */
    public void updateProgressWaitingDialog(long j, long j2) {
        if (this.mDownloadFileWaitingDialog != null && this.mContext != null) {
            if (j > 0) {
                long j3 = (j2 * 100) / j;
                this.mDownloadFileWaitingDialog.setMessage(this.mContext.getString(C4558R.string.zm_msg_download_file_progress, new Object[]{Long.valueOf(j3)}));
            } else {
                this.mDownloadFileWaitingDialog.setMessage(this.mContext.getString(C4558R.string.zm_msg_download_file_size, new Object[]{FileUtils.toFileSizeString(this.mContext, j2)}));
            }
        }
    }

    private void stopDownloadFileTask() {
        ZMAsyncURLDownloadFile zMAsyncURLDownloadFile = this.mTaskDownloadFile;
        if (zMAsyncURLDownloadFile != null && !zMAsyncURLDownloadFile.isCancelled()) {
            this.mTaskDownloadFile.cancel(true);
        }
        dismissDownloadFileWaitingDialog();
        this.mTaskDownloadFile = null;
    }

    private void shareScreen(@Nullable Intent intent) {
        if (intent != null && this.mContext != null) {
            if (startShareSession()) {
                this.mbShareScreen = true;
                if (ConfLocalHelper.isDirectShareClient()) {
                    this.mStartShareRunnable = new Runnable() {
                        public void run() {
                            ZmConfShareComponent.this.mPresentRoomLayer.startShareOK();
                            ZmConfShareComponent.this.mStartShareRunnable = null;
                        }
                    };
                    this.mPresentRoomLayer.postDelayed(this.mStartShareRunnable, 500);
                }
                ScreenShareMgr.getInstance().prepare(intent);
            } else {
                ShareAlertDialog.showConfDialog(this.mContext, this.mContext.getSupportFragmentManager(), C4558R.string.zm_alert_start_share_fail, true);
            }
        }
    }
}
