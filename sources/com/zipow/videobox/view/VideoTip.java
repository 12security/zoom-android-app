package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.nydus.NydusUtil;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.MediaDevice;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.component.ZMConfComponentMgr;
import java.util.List;
import p021us.zoom.androidlib.app.ZMTipFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class VideoTip extends ZMTipFragment implements OnClickListener {
    public static final String ARG_ANCHOR_ID = "anchorId";
    public static final String ARG_ARROW_DIRECTION = "arrowDirection";
    public static final String ARG_SHOW_NO_CAMERA = "showNoCamera";
    private View mBtnNoCamera;
    private ViewGroup mRootView;
    private boolean mbShowNoCamera = true;

    public static void show(FragmentManager fragmentManager, int i) {
        show(fragmentManager, i, 3, true);
    }

    public static void show(@Nullable FragmentManager fragmentManager, int i, int i2, boolean z) {
        if (fragmentManager != null) {
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            bundle.putInt(ARG_ARROW_DIRECTION, i2);
            bundle.putBoolean(ARG_SHOW_NO_CAMERA, z);
            VideoTip videoTip = new VideoTip();
            videoTip.setArguments(bundle);
            videoTip.show(fragmentManager, VideoTip.class.getName());
        }
    }

    public static boolean isShown(@Nullable FragmentManager fragmentManager) {
        boolean z = false;
        if (fragmentManager == null) {
            return false;
        }
        if (((VideoTip) fragmentManager.findFragmentByTag(VideoTip.class.getName())) != null) {
            z = true;
        }
        return z;
    }

    public static boolean dismiss(@Nullable FragmentManager fragmentManager) {
        if (fragmentManager == null) {
            return false;
        }
        VideoTip videoTip = (VideoTip) fragmentManager.findFragmentByTag(VideoTip.class.getName());
        if (videoTip == null) {
            return false;
        }
        videoTip.dismiss();
        return true;
    }

    public static void updateIfExists(FragmentManager fragmentManager) {
        VideoTip videoTip = (VideoTip) fragmentManager.findFragmentByTag(VideoTip.class.getName());
        if (videoTip != null) {
            videoTip.updateUI();
        }
    }

    public ZMTip onCreateTip(Context context, @NonNull LayoutInflater layoutInflater, Bundle bundle) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(C4558R.layout.zm_video_tip, null);
        this.mBtnNoCamera = viewGroup.findViewById(C4558R.C4560id.btnNoCamera);
        Bundle arguments = getArguments();
        ZMTip zMTip = new ZMTip(context);
        zMTip.addView(viewGroup);
        this.mbShowNoCamera = arguments.getBoolean(ARG_SHOW_NO_CAMERA, true);
        int i = arguments.getInt("anchorId", 0);
        int i2 = arguments.getInt(ARG_ARROW_DIRECTION, 3);
        if (i > 0) {
            View findViewById = getActivity().findViewById(i);
            if (findViewById != null) {
                zMTip.setAnchor(findViewById, i2);
            }
        }
        this.mBtnNoCamera.setOnClickListener(this);
        this.mRootView = viewGroup;
        return zMTip;
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        String str;
        String str2;
        int i;
        if (!ConfMgr.getInstance().isConfConnected()) {
            dismiss();
            return;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            dismiss();
            return;
        }
        CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
        if (videoStatusObj == null) {
            dismiss();
            return;
        }
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        if (videoObj == null) {
            dismiss();
            return;
        }
        ViewGroup viewGroup = this.mRootView;
        if (viewGroup != null) {
            for (int childCount = viewGroup.getChildCount() - 1; childCount >= 0; childCount--) {
                View childAt = this.mRootView.getChildAt(childCount);
                if (childAt != this.mBtnNoCamera) {
                    this.mRootView.removeView(childAt);
                }
            }
            FragmentActivity activity = getActivity();
            if (activity != null) {
                LayoutInflater from = LayoutInflater.from(activity);
                if (from != null) {
                    int frontCameraId = NydusUtil.getFrontCameraId();
                    if (frontCameraId >= 0) {
                        String valueOf = String.valueOf(frontCameraId);
                        TextView textView = (TextView) from.inflate(C4558R.layout.zm_video_tip_item, null);
                        textView.setText(activity.getString(C4558R.string.zm_btn_front_camera));
                        textView.setTag(valueOf);
                        ViewGroup viewGroup2 = this.mRootView;
                        viewGroup2.addView(textView, viewGroup2.getChildCount() - 1);
                        textView.setOnClickListener(this);
                        str = valueOf;
                    } else {
                        str = null;
                    }
                    int backCameraId = NydusUtil.getBackCameraId();
                    if (backCameraId >= 0) {
                        String valueOf2 = String.valueOf(backCameraId);
                        TextView textView2 = (TextView) from.inflate(C4558R.layout.zm_video_tip_item, null);
                        textView2.setText(activity.getString(C4558R.string.zm_btn_back_camera));
                        textView2.setTag(valueOf2);
                        ViewGroup viewGroup3 = this.mRootView;
                        viewGroup3.addView(textView2, viewGroup3.getChildCount() - 1);
                        textView2.setOnClickListener(this);
                        str2 = valueOf2;
                    } else {
                        str2 = null;
                    }
                    List camList = videoObj.getCamList();
                    if (camList != null) {
                        int size = camList.size() - NydusUtil.getNumberOfCameras();
                        int i2 = 0;
                        int i3 = 0;
                        while (i3 < camList.size()) {
                            MediaDevice mediaDevice = (MediaDevice) camList.get(i3);
                            if (mediaDevice == null) {
                                i = i3;
                            } else if (StringUtil.isSameString(str, mediaDevice.getDeviceId())) {
                                i = i3;
                            } else if (StringUtil.isSameString(str2, mediaDevice.getDeviceId())) {
                                i = i3;
                            } else {
                                int i4 = i2 + 1;
                                i = i3;
                                TextView textView3 = (TextView) from.inflate(C4558R.layout.zm_video_tip_item, null);
                                textView3.setText(buildUsbCameraDisplayName(activity, mediaDevice, size, i2, camList));
                                textView3.setTag(mediaDevice.getDeviceId());
                                ViewGroup viewGroup4 = this.mRootView;
                                viewGroup4.addView(textView3, viewGroup4.getChildCount() - 1);
                                textView3.setOnClickListener(this);
                                i2 = i4;
                            }
                            i3 = i + 1;
                        }
                    }
                }
                boolean isSending = videoStatusObj.getIsSending();
                String defaultDevice = videoObj.getDefaultDevice();
                int childCount2 = this.mRootView.getChildCount();
                while (true) {
                    childCount2--;
                    if (childCount2 < 0) {
                        break;
                    }
                    View childAt2 = this.mRootView.getChildAt(childCount2);
                    String str3 = (String) childAt2.getTag();
                    if (str3 != null) {
                        if (!isSending) {
                            childAt2.setVisibility(0);
                        } else if (StringUtil.isSameString(str3, defaultDevice)) {
                            childAt2.setVisibility(8);
                        } else {
                            childAt2.setVisibility(0);
                        }
                    } else if (!isSending || !this.mbShowNoCamera) {
                        childAt2.setVisibility(8);
                    } else {
                        childAt2.setVisibility(0);
                    }
                }
                if (AccessibilityUtil.isSpokenFeedbackEnabled(getActivity()) && this.mRootView.getChildCount() > 0) {
                    View childAt3 = this.mRootView.getChildAt(0);
                    if (childAt3 != null) {
                        childAt3.sendAccessibilityEvent(8);
                    }
                }
            }
        }
    }

    public String buildUsbCameraDisplayName(@NonNull Context context, @NonNull MediaDevice mediaDevice, int i, int i2, @NonNull List<MediaDevice> list) {
        String str;
        String deviceName = mediaDevice.getDeviceName();
        int i3 = 0;
        if (!StringUtil.isEmptyOrNull(deviceName)) {
            int i4 = 0;
            for (MediaDevice mediaDevice2 : list) {
                if (deviceName.equals(mediaDevice2.getDeviceName())) {
                    if (mediaDevice2 == mediaDevice) {
                        i4 = i3;
                    }
                    i3++;
                }
            }
            if (i3 <= 1) {
                return deviceName;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(deviceName);
            sb.append(" (");
            sb.append(i4 + 1);
            sb.append(")");
            return sb.toString();
        }
        if (i > 1) {
            str = context.getString(C4558R.string.zm_btn_usb_camera, new Object[]{String.valueOf(i2 + 1)});
        } else {
            str = context.getString(C4558R.string.zm_btn_usb_camera, new Object[]{""});
        }
        return str;
    }

    public void onClick(@NonNull View view) {
        if (view == this.mBtnNoCamera) {
            onClickBtnNoCamera();
        } else {
            onClickOtherCamera((String) view.getTag());
        }
    }

    private void openCamera(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            if (myself == null) {
                dismiss();
                return;
            }
            CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
            if (videoStatusObj == null) {
                dismiss();
                return;
            }
            VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
            if (videoObj == null) {
                dismiss();
            } else if (((ConfActivity) getActivity()) != null) {
                if (!videoStatusObj.getIsSending()) {
                    ZMConfComponentMgr.getInstance().switchCamera(str);
                } else if (!StringUtil.isSameString(str, videoObj.getDefaultDevice())) {
                    ZMConfComponentMgr.getInstance().switchCamera(str);
                }
            }
        }
    }

    private void onClickOtherCamera(String str) {
        openCamera(str);
        dismiss();
    }

    private void onClickBtnNoCamera() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            dismiss();
            return;
        }
        CmmVideoStatus videoStatusObj = myself.getVideoStatusObj();
        if (videoStatusObj == null) {
            dismiss();
            return;
        }
        if (videoStatusObj.getIsSending()) {
            ZMConfComponentMgr.getInstance().sinkInMuteVideo(true);
        }
        dismiss();
    }
}
