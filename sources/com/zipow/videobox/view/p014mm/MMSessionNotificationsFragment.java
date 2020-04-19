package com.zipow.videobox.view.p014mm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomLogEventTracking;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.List;
import p015io.reactivex.annotations.SchedulerSupport;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

/* renamed from: com.zipow.videobox.view.mm.MMSessionNotificationsFragment */
public class MMSessionNotificationsFragment extends ZMDialogFragment implements OnClickListener {
    public static final int ALL_MSG = 0;
    public static final String EXTRA_ARGS_GROUP_JID = "groupJid";
    public static final String GROUP_NOTIFICATION_TYPE = "mGroupNotificationType";
    public static final int NO_MSG = 2;
    public static final int PRIVATE_MSG = 1;
    private static final String TAG = "MMSessionNotificationsFragment";
    @Nullable
    private String mGroupId;
    private int mGroupNotificationType;
    private ImageView mImgAllMsg;
    private ImageView mImgNoMsg;
    private ImageView mImgPrivateMsg;
    private View mPanelAllMsg;
    private View mPanelNoMsg;
    private View mPanelPrivateMsg;

    public static void showAsActivity(Fragment fragment, String str, int i, int i2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("groupJid", str);
            bundle.putInt(GROUP_NOTIFICATION_TYPE, i);
            SimpleActivity.show(fragment, MMSessionNotificationsFragment.class.getName(), bundle, i2, false, 1);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_mm_session_notifications, viewGroup, false);
        this.mPanelAllMsg = inflate.findViewById(C4558R.C4560id.panelAllMsg);
        this.mImgAllMsg = (ImageView) inflate.findViewById(C4558R.C4560id.imgAllMsg);
        this.mPanelPrivateMsg = inflate.findViewById(C4558R.C4560id.panelPrivateMsg);
        this.mImgPrivateMsg = (ImageView) inflate.findViewById(C4558R.C4560id.imgPrivateMsg);
        this.mPanelNoMsg = inflate.findViewById(C4558R.C4560id.panelNoMsg);
        this.mImgNoMsg = (ImageView) inflate.findViewById(C4558R.C4560id.imgNoMsg);
        this.mPanelAllMsg.setOnClickListener(this);
        this.mPanelPrivateMsg.setOnClickListener(this);
        this.mPanelNoMsg.setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        return inflate;
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onResume() {
        super.onResume();
        updateData();
    }

    private void updateData() {
        if (!StringUtil.isEmptyOrNull(this.mGroupId)) {
            int i = this.mGroupNotificationType;
            if (i == 0) {
                this.mImgNoMsg.setVisibility(8);
                this.mImgAllMsg.setVisibility(0);
                this.mImgPrivateMsg.setVisibility(8);
            } else if (i == 1) {
                this.mImgNoMsg.setVisibility(8);
                this.mImgAllMsg.setVisibility(8);
                this.mImgPrivateMsg.setVisibility(0);
            } else if (i == 2) {
                this.mImgNoMsg.setVisibility(0);
                this.mImgAllMsg.setVisibility(8);
                this.mImgPrivateMsg.setVisibility(8);
            }
        }
    }

    private void onNotify_MUCGroupInfoUpdatedImpl(String str) {
        if (StringUtil.isSameString(str, this.mGroupId)) {
            updateData();
        }
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        this.mGroupId = arguments.getString("groupJid");
        this.mGroupNotificationType = arguments.getInt(GROUP_NOTIFICATION_TYPE);
    }

    public void onClick(@Nullable View view) {
        if (view != null) {
            if (view.getId() == C4558R.C4560id.btnBack) {
                dismiss();
            } else if (view == this.mPanelAllMsg) {
                onClickPanelAllMsg();
            } else if (view == this.mPanelNoMsg) {
                onClickPanelNoMsg();
            } else if (view == this.mPanelPrivateMsg) {
                onClickPanelPrivateMsg();
            }
        }
    }

    private void onClickPanelAllMsg() {
        ZoomLogEventTracking.eventTrackNotificationSetting(BoxRequestEvent.STREAM_TYPE_ALL);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || (zoomMessenger.isConnectionGood() && NetworkUtil.hasDataNetwork(getActivity()))) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                List receiveAllMUCSettings = notificationSettingMgr.getReceiveAllMUCSettings();
                if (receiveAllMUCSettings == null || !receiveAllMUCSettings.contains(this.mGroupId)) {
                    notificationSettingMgr.applyMUCSettings(this.mGroupId, 1);
                    dismiss();
                    return;
                }
                return;
            }
            return;
        }
        showConnectionError();
    }

    private void onClickPanelNoMsg() {
        ZoomLogEventTracking.eventTrackNotificationSetting(SchedulerSupport.NONE);
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || (zoomMessenger.isConnectionGood() && NetworkUtil.hasDataNetwork(getActivity()))) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                List disableMUCSettings = notificationSettingMgr.getDisableMUCSettings();
                if (disableMUCSettings == null || !disableMUCSettings.contains(this.mGroupId)) {
                    notificationSettingMgr.applyMUCSettings(this.mGroupId, 3);
                    dismiss();
                    return;
                }
                return;
            }
            return;
        }
        showConnectionError();
    }

    private void onClickPanelPrivateMsg() {
        ZoomLogEventTracking.eventTrackNotificationSetting("mention");
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null || (zoomMessenger.isConnectionGood() && NetworkUtil.hasDataNetwork(getActivity()))) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                List hLMUCSettings = notificationSettingMgr.getHLMUCSettings();
                if (hLMUCSettings == null || !hLMUCSettings.contains(this.mGroupId)) {
                    notificationSettingMgr.applyMUCSettings(this.mGroupId, 2);
                    dismiss();
                    return;
                }
                return;
            }
            return;
        }
        showConnectionError();
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void dismiss() {
        finishFragment(true);
    }
}
