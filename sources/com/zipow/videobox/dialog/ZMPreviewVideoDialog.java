package com.zipow.videobox.dialog;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.zipow.nydus.NydusUtil;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.PreferenceUtil;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.OsUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ViewPressEffectHelper;
import p021us.zoom.androidlib.widget.ZMCheckedTextView;
import p021us.zoom.videomeetings.C4558R;

public class ZMPreviewVideoDialog extends ZMDialogFragment implements Callback, OnClickListener {
    private static float DEFAULT_MOBILE_RATIO = 1.3333334f;
    private static final String TAG = "com.zipow.videobox.dialog.ZMPreviewVideoDialog";
    private float mBestRatio = DEFAULT_MOBILE_RATIO;
    private View mBtnJoinWithoutVideo;
    @Nullable
    private Camera mCamera;
    private ZMCheckedTextView mChkTurnOnVideoWithoutPreview;
    private int mFailedTimes = 0;
    /* access modifiers changed from: private */
    public SurfaceHolder mHolder;
    private boolean mIsSurfaceReady = false;
    private View mPanelBottom;
    private View mPanelSurfaceHolder;
    private View mPanelTop;
    /* access modifiers changed from: private */
    public SurfaceView mSvPreview;
    private View mTitle;
    private boolean requestPermission = true;

    public void surfaceCreated(SurfaceHolder surfaceHolder) {
    }

    public static ZMPreviewVideoDialog show(@NonNull ZMActivity zMActivity) {
        FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
        if (supportFragmentManager == null) {
            return null;
        }
        dismiss(supportFragmentManager);
        if (VERSION.SDK_INT == 26) {
            zMActivity.setRequestedOrientation(7);
        } else {
            zMActivity.setRequestedOrientation(1);
        }
        ZMPreviewVideoDialog zMPreviewVideoDialog = new ZMPreviewVideoDialog();
        zMPreviewVideoDialog.show(supportFragmentManager, TAG);
        return zMPreviewVideoDialog;
    }

    public static void dismiss(FragmentManager fragmentManager) {
        ZMPreviewVideoDialog zMPreviewVideoDialog = (ZMPreviewVideoDialog) fragmentManager.findFragmentByTag(TAG);
        if (zMPreviewVideoDialog != null) {
            zMPreviewVideoDialog.dismiss();
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setStyle(0, C4558R.style.ZMDialog_NoTitle);
        checkRatio();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        setCancelable(false);
        View inflate = LayoutInflater.from(getActivity()).inflate(C4558R.layout.zm_preview_video, null, false);
        this.mPanelTop = inflate.findViewById(C4558R.C4560id.panelTopBar);
        this.mPanelBottom = inflate.findViewById(C4558R.C4560id.panelBottom);
        this.mTitle = inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mSvPreview = (SurfaceView) inflate.findViewById(C4558R.C4560id.svPreview);
        this.mPanelSurfaceHolder = inflate.findViewById(C4558R.C4560id.panelSurfaceHolder);
        this.mChkTurnOnVideoWithoutPreview = (ZMCheckedTextView) inflate.findViewById(C4558R.C4560id.chkTurnOnVideoWithoutPreview);
        this.mBtnJoinWithoutVideo = inflate.findViewById(C4558R.C4560id.btnJoinWithoutVideo);
        ViewPressEffectHelper.attach(this.mBtnJoinWithoutVideo);
        this.mBtnJoinWithoutVideo.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnJoinWithVideo).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.optionTurnOnVideoWithoutPreview).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnLeave).setOnClickListener(this);
        initUI();
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.optionTurnOnVideoWithoutPreview) {
            ZMCheckedTextView zMCheckedTextView = this.mChkTurnOnVideoWithoutPreview;
            zMCheckedTextView.setChecked(!zMCheckedTextView.isChecked());
        } else if (id == C4558R.C4560id.btnLeave) {
            ConfActivity confActivity = (ConfActivity) getActivity();
            if (confActivity != null) {
                ConfLocalHelper.leaveConfBeforeConnected(confActivity);
            }
        } else if (id == C4558R.C4560id.btnJoinWithoutVideo) {
            stopPreview();
            joinMeeting(false);
        } else if (id == C4558R.C4560id.btnJoinWithVideo) {
            stopPreview();
            joinMeeting(true);
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        if (this.mIsSurfaceReady) {
            this.mHolder = this.mSvPreview.getHolder();
            startPreview();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        stopPreview();
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.mIsSurfaceReady = true;
        if (isResumed()) {
            this.mHolder = surfaceHolder;
            startPreview();
        }
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mIsSurfaceReady = false;
        stopPreview();
    }

    public void startPreview() {
        final ConfActivityNormal confActivityNormal = (ConfActivityNormal) getActivity();
        if (confActivityNormal != null && this.mCamera == null && this.mHolder != null) {
            if (OsUtil.isAtLeastM() && confActivityNormal.zm_checkSelfPermission("android.permission.CAMERA") != 0) {
                if (this.requestPermission) {
                    this.requestPermission = false;
                    confActivityNormal.requestPermission("android.permission.CAMERA", 1017, 0);
                }
            } else if (!PreferenceUtil.readBooleanValue(PreferenceUtil.CAMERA_IS_FREEZED, false) && NydusUtil.getNumberOfCameras() != 0) {
                int frontCameraId = NydusUtil.getFrontCameraId();
                if (frontCameraId < 0) {
                    frontCameraId = 0;
                }
                try {
                    this.mCamera = Camera.open(frontCameraId);
                    this.mCamera.setPreviewDisplay(this.mHolder);
                    int cameraDisplayOrientation = setCameraDisplayOrientation(confActivityNormal, frontCameraId, this.mCamera);
                    Size previewSize = getPreviewSize(this.mCamera, DEFAULT_MOBILE_RATIO);
                    if (cameraDisplayOrientation % 180 == 90) {
                        int i = previewSize.width;
                        previewSize.width = previewSize.height;
                        previewSize.height = i;
                    }
                    this.mCamera.startPreview();
                    relayoutSurfaceView(confActivityNormal);
                } catch (Exception unused) {
                    Camera camera = this.mCamera;
                    if (camera != null) {
                        camera.release();
                    }
                    this.mCamera = null;
                    int i2 = this.mFailedTimes + 1;
                    this.mFailedTimes = i2;
                    if (i2 < 4) {
                        confActivityNormal.getWindow().getDecorView().postDelayed(new Runnable() {
                            public void run() {
                                if (confActivityNormal.isActive()) {
                                    ZMPreviewVideoDialog zMPreviewVideoDialog = ZMPreviewVideoDialog.this;
                                    zMPreviewVideoDialog.mHolder = zMPreviewVideoDialog.mSvPreview.getHolder();
                                    ZMPreviewVideoDialog.this.startPreview();
                                }
                            }
                        }, 300);
                    }
                }
            }
        }
    }

    private void checkRatio() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            int displayWidth = UIUtil.getDisplayWidth(activity);
            int displayHeight = UIUtil.getDisplayHeight(activity);
            float dimensionPixelSize = (float) (displayWidth - (getResources().getDimensionPixelSize(C4558R.dimen.zm_preview_width_margin) * 2));
            int dimensionPixelSize2 = (getResources().getDimensionPixelSize(C4558R.dimen.zm_height_64dp) * 4) + UIUtil.getStatusBarHeight(activity);
            int i = displayHeight - ((int) (this.mBestRatio * dimensionPixelSize));
            if (i < dimensionPixelSize2 && ((float) displayHeight) / ((float) displayWidth) <= 1.7777778f) {
                DEFAULT_MOBILE_RATIO = ((float) i) / dimensionPixelSize;
                if (DEFAULT_MOBILE_RATIO < 1.0f) {
                    DEFAULT_MOBILE_RATIO = 1.0f;
                }
            }
        }
    }

    private Size getPreviewSize(Camera camera, float f) {
        Parameters parameters = camera.getParameters();
        List supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        Size size = null;
        float f2 = Float.MAX_VALUE;
        int i = 0;
        while (true) {
            if (i >= supportedPreviewSizes.size()) {
                break;
            }
            Size size2 = (Size) supportedPreviewSizes.get(i);
            if (size2 != null) {
                float f3 = ((float) size2.width) / ((float) size2.height);
                if (f3 == f) {
                    size = size2;
                    break;
                }
                float abs = Math.abs(f3 - f);
                if (f2 > abs) {
                    size = size2;
                    f2 = abs;
                }
            }
            i++;
        }
        if (size != null) {
            this.mBestRatio = ((float) size.width) / ((float) size.height);
            parameters.setPreviewSize(size.width, size.height);
            camera.setParameters(parameters);
        }
        return parameters.getPreviewSize();
    }

    private void relayoutSurfaceView(@NonNull Activity activity) {
        int i;
        int displayWidth = UIUtil.getDisplayWidth(activity);
        int displayHeight = UIUtil.getDisplayHeight(activity);
        LayoutParams layoutParams = (LayoutParams) this.mSvPreview.getLayoutParams();
        int dimensionPixelSize = displayWidth - (getResources().getDimensionPixelSize(C4558R.dimen.zm_preview_width_margin) * 2);
        int i2 = (int) (((float) dimensionPixelSize) * this.mBestRatio);
        if (this.mPanelTop.getHeight() == 0 || this.mPanelBottom.getHeight() == 0) {
            i = (getResources().getDimensionPixelSize(C4558R.dimen.zm_height_64dp) * 4) + UIUtil.getStatusBarHeight(activity);
        } else {
            i = this.mPanelTop.getHeight() + this.mPanelBottom.getHeight() + getResources().getDimensionPixelSize(C4558R.dimen.zm_height_64dp) + UIUtil.getStatusBarHeight(activity);
        }
        if (displayHeight - i2 < i) {
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mTitle.getLayoutParams();
            layoutParams2.topMargin = 0;
            this.mTitle.setLayoutParams(layoutParams2);
            i2 = (displayHeight - i) - getResources().getDimensionPixelSize(C4558R.dimen.zm_dialog_radius_normal);
        }
        layoutParams.width = dimensionPixelSize;
        layoutParams.height = i2;
        this.mSvPreview.setLayoutParams(layoutParams);
        this.mSvPreview.getParent().requestLayout();
    }

    private int setCameraDisplayOrientation(@NonNull Activity activity, int i, @NonNull Camera camera) {
        int i2;
        CameraInfo cameraInfo = new CameraInfo();
        NydusUtil.getCameraInfo(i, cameraInfo);
        int realCameraOrientation = NydusUtil.getRealCameraOrientation(i);
        int i3 = 0;
        switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
            case 1:
                i3 = 90;
                break;
            case 2:
                i3 = 180;
                break;
            case 3:
                i3 = SubsamplingScaleImageView.ORIENTATION_270;
                break;
        }
        if (cameraInfo.facing == 1) {
            i2 = (360 - ((realCameraOrientation + i3) % 360)) % 360;
        } else {
            i2 = ((realCameraOrientation - i3) + 360) % 360;
        }
        camera.setDisplayOrientation(i2);
        return i2;
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:3|4|5|6|7|9) */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x0008 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void stopPreview() {
        /*
            r1 = this;
            android.hardware.Camera r0 = r1.mCamera
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            r0.stopPreview()     // Catch:{ Exception -> 0x0008 }
        L_0x0008:
            android.hardware.Camera r0 = r1.mCamera     // Catch:{ Exception -> 0x000d }
            r0.release()     // Catch:{ Exception -> 0x000d }
        L_0x000d:
            r0 = 0
            r1.mCamera = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.dialog.ZMPreviewVideoDialog.stopPreview():void");
    }

    private void initUI() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            relayoutSurfaceView(activity);
        }
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            this.mChkTurnOnVideoWithoutPreview.setChecked(!videoObj.neverConfirmVideoPrivacyWhenJoinMeeting());
        }
        this.mPanelSurfaceHolder.setVisibility(0);
        SurfaceHolder holder = this.mSvPreview.getHolder();
        holder.setType(3);
        holder.addCallback(this);
    }

    private void joinMeeting(boolean z) {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj != null) {
            videoObj.setNeverConfirmVideoPrivacyWhenJoinMeeting(!this.mChkTurnOnVideoWithoutPreview.isChecked());
            ConfMgr.getInstance().onUserConfirmVideoPrivacy(z);
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.setRequestedOrientation(-1);
        }
        dismiss();
    }
}
