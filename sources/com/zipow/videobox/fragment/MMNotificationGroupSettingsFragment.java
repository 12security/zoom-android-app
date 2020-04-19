package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettingItem;
import com.zipow.videobox.ptapp.IMProtos.MUCNotifySettings;
import com.zipow.videobox.ptapp.NotificationSettingUI;
import com.zipow.videobox.ptapp.NotificationSettingUI.INotificationSettingUIListener;
import com.zipow.videobox.ptapp.NotificationSettingUI.SimpleNotificationSettingUIListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import com.zipow.videobox.ptapp.p013mm.GroupAction;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomGroup;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class MMNotificationGroupSettingsFragment extends ZMDialogFragment implements OnClickListener {
    private static final String ARGS_MUC_TYPE = "mucType";
    private static final String ARGS_SESSION_ID = "sessionId";
    public static final String RESULT_MUC_TYPE = "mucType";
    public static final String RESULT_SESSION_ID = "sessionId";
    private View mBtnBack;
    private ImageView mImgAllMsg;
    private ImageView mImgNotificationNo;
    private ImageView mImgNotificationPrivate;
    @NonNull
    private INotificationSettingUIListener mListener = new SimpleNotificationSettingUIListener() {
        public void OnMUCSettingUpdated() {
            MMNotificationGroupSettingsFragment.this.OnMUCSettingUpdated();
        }
    };
    private int mMUCtype = 0;
    private View mPanelAllMsg;
    private View mPanelNoMsg;
    private View mPanelPrivateMsg;
    private View mPanelRestDefault;
    @Nullable
    private String mSessionId;
    private TextView mTxtTitle;
    @NonNull
    private SimpleZoomMessengerUIListener mZoomMessengerUIListener = new SimpleZoomMessengerUIListener() {
        public void onGroupAction(int i, @NonNull GroupAction groupAction, String str) {
            MMNotificationGroupSettingsFragment.this.onGroupAction(i, groupAction, str);
        }

        public void On_NotifyGroupDestroy(String str, String str2, long j) {
            MMNotificationGroupSettingsFragment.this.On_NotifyGroupDestroy(str, str2, j);
        }
    };

    public static void showAsActivity(@Nullable Fragment fragment, String str, int i, int i2) {
        if (fragment != null && !StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("sessionId", str);
            bundle.putInt("mucType", i);
            SimpleActivity.show(fragment, MMNotificationGroupSettingsFragment.class.getName(), bundle, i2);
        }
    }

    public static void showAsActivity(@Nullable Fragment fragment, String str) {
        if (fragment != null && !StringUtil.isEmptyOrNull(str)) {
            Bundle bundle = new Bundle();
            bundle.putString("sessionId", str);
            SimpleActivity.show(fragment, MMNotificationGroupSettingsFragment.class.getName(), bundle, 0);
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_notification_group_detail, viewGroup, false);
        this.mPanelAllMsg = inflate.findViewById(C4558R.C4560id.panelAllMsg);
        this.mImgAllMsg = (ImageView) inflate.findViewById(C4558R.C4560id.imgAllMsg);
        this.mPanelPrivateMsg = inflate.findViewById(C4558R.C4560id.panelPrivateMsg);
        this.mImgNotificationPrivate = (ImageView) inflate.findViewById(C4558R.C4560id.imgNotificationPrivate);
        this.mPanelNoMsg = inflate.findViewById(C4558R.C4560id.panelNoMsg);
        this.mImgNotificationNo = (ImageView) inflate.findViewById(C4558R.C4560id.imgNotificationNo);
        this.mPanelRestDefault = inflate.findViewById(C4558R.C4560id.panelRestDefault);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mPanelAllMsg.setOnClickListener(this);
        this.mPanelPrivateMsg.setOnClickListener(this);
        this.mPanelNoMsg.setOnClickListener(this);
        this.mPanelRestDefault.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mSessionId = arguments.getString("sessionId");
            this.mMUCtype = arguments.getInt("mucType");
        }
        if (StringUtil.isEmptyOrNull(this.mSessionId)) {
            dismiss();
        }
    }

    public void onResume() {
        super.onResume();
        NotificationSettingUI.getInstance().addListener(this.mListener);
        ZoomMessengerUI.getInstance().addListener(this.mZoomMessengerUIListener);
        updateUI();
    }

    public void onPause() {
        ZoomMessengerUI.getInstance().removeListener(this.mZoomMessengerUIListener);
        NotificationSettingUI.getInstance().removeListener(this.mListener);
        super.onPause();
    }

    /* access modifiers changed from: private */
    public void onGroupAction(int i, GroupAction groupAction, String str) {
        checkAmIInGroup(groupAction.getGroupId());
    }

    /* access modifiers changed from: private */
    public void On_NotifyGroupDestroy(String str, String str2, long j) {
        checkAmIInGroup(str);
    }

    /* access modifiers changed from: private */
    public void OnMUCSettingUpdated() {
        updateUI();
    }

    private void checkAmIInGroup(String str) {
        if (StringUtil.isSameString(str, this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(str);
                if (groupById == null || !groupById.amIInGroup()) {
                    dismiss();
                } else {
                    updateUI();
                }
            }
        }
    }

    private void updateUI() {
        NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
        if (notificationSettingMgr != null && !StringUtil.isEmptyOrNull(this.mSessionId)) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null) {
                ZoomGroup groupById = zoomMessenger.getGroupById(this.mSessionId);
                if (groupById != null) {
                    this.mTxtTitle.setText(groupById.getGroupDisplayName(getActivity()));
                    MUCNotifySettings mUCSettings = notificationSettingMgr.getMUCSettings();
                    if (mUCSettings != null) {
                        int i = this.mMUCtype;
                        if (i == 0) {
                            int i2 = i;
                            for (int i3 = 0; i3 < mUCSettings.getItemsCount(); i3++) {
                                MUCNotifySettingItem items = mUCSettings.getItems(i3);
                                if (StringUtil.isSameString(items.getSessionId(), this.mSessionId)) {
                                    i2 = items.getType();
                                }
                            }
                            i = i2;
                        }
                        int i4 = 8;
                        if (i != 0) {
                            switch (i) {
                                case 1:
                                    this.mImgNotificationNo.setVisibility(8);
                                    this.mImgAllMsg.setVisibility(0);
                                    this.mImgNotificationPrivate.setVisibility(8);
                                    break;
                                case 2:
                                    this.mImgNotificationNo.setVisibility(8);
                                    this.mImgAllMsg.setVisibility(8);
                                    this.mImgNotificationPrivate.setVisibility(0);
                                    break;
                                case 3:
                                    this.mImgNotificationNo.setVisibility(0);
                                    this.mImgAllMsg.setVisibility(8);
                                    this.mImgNotificationPrivate.setVisibility(8);
                                    break;
                            }
                        } else {
                            int[] blockAllSettings = notificationSettingMgr.getBlockAllSettings();
                            if (blockAllSettings == null) {
                                this.mImgNotificationNo.setVisibility(8);
                                this.mImgAllMsg.setVisibility(0);
                                this.mImgNotificationPrivate.setVisibility(8);
                                return;
                            }
                            int i5 = blockAllSettings[0];
                            int i6 = blockAllSettings[1];
                            this.mImgAllMsg.setVisibility((i5 == 1 && i6 == 1) ? 0 : 8);
                            this.mImgNotificationNo.setVisibility(i5 == 2 ? 0 : 8);
                            ImageView imageView = this.mImgNotificationPrivate;
                            if (i5 == 1 && i6 == 4) {
                                i4 = 0;
                            }
                            imageView.setVisibility(i4);
                        }
                    }
                }
            }
        }
    }

    public void dismiss() {
        finishFragment(true);
    }

    public void onClick(View view) {
        if (view == this.mPanelAllMsg) {
            onClickPanelAllMsg();
        } else if (view == this.mPanelNoMsg) {
            onClickPanelNoMsg();
        } else if (view == this.mPanelPrivateMsg) {
            onClickPanelPrivateMsg();
        } else if (view == this.mPanelRestDefault) {
            onClickPanelRestDefault();
        } else if (view == this.mBtnBack) {
            onClickBtnBack();
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    private void onClickPanelRestDefault() {
        if (this.mMUCtype == 0) {
            NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
            if (notificationSettingMgr != null) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.mSessionId);
                notificationSettingMgr.resetMUCSettings(arrayList);
                dismiss();
            }
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Intent intent = new Intent();
                intent.putExtra("mucType", 0);
                intent.putExtra("sessionId", this.mSessionId);
                activity.setResult(-1, intent);
            }
            dismiss();
        }
    }

    private void onClickPanelAllMsg() {
        if (this.mMUCtype == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || (zoomMessenger.isConnectionGood() && NetworkUtil.hasDataNetwork(getActivity()))) {
                NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                if (notificationSettingMgr != null) {
                    List receiveAllMUCSettings = notificationSettingMgr.getReceiveAllMUCSettings();
                    if (receiveAllMUCSettings == null || !receiveAllMUCSettings.contains(this.mSessionId)) {
                        notificationSettingMgr.applyMUCSettings(this.mSessionId, 1);
                        updateUI();
                    }
                }
            } else {
                showConnectionError();
            }
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Intent intent = new Intent();
                intent.putExtra("mucType", 1);
                intent.putExtra("sessionId", this.mSessionId);
                activity.setResult(-1, intent);
                dismiss();
            }
        }
    }

    private void onClickPanelNoMsg() {
        if (this.mMUCtype == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || (zoomMessenger.isConnectionGood() && NetworkUtil.hasDataNetwork(getActivity()))) {
                NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                if (notificationSettingMgr != null) {
                    List disableMUCSettings = notificationSettingMgr.getDisableMUCSettings();
                    if (disableMUCSettings == null || !disableMUCSettings.contains(this.mSessionId)) {
                        notificationSettingMgr.applyMUCSettings(this.mSessionId, 3);
                        updateUI();
                    }
                }
            } else {
                showConnectionError();
            }
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Intent intent = new Intent();
                intent.putExtra("mucType", 3);
                intent.putExtra("sessionId", this.mSessionId);
                activity.setResult(-1, intent);
            }
            dismiss();
        }
    }

    private void onClickPanelPrivateMsg() {
        if (this.mMUCtype == 0) {
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger == null || (zoomMessenger.isConnectionGood() && NetworkUtil.hasDataNetwork(getActivity()))) {
                NotificationSettingMgr notificationSettingMgr = PTApp.getInstance().getNotificationSettingMgr();
                if (notificationSettingMgr != null) {
                    List hLMUCSettings = notificationSettingMgr.getHLMUCSettings();
                    if (hLMUCSettings == null || !hLMUCSettings.contains(this.mSessionId)) {
                        notificationSettingMgr.applyMUCSettings(this.mSessionId, 2);
                        updateUI();
                    }
                }
            } else {
                showConnectionError();
            }
        } else {
            FragmentActivity activity = getActivity();
            if (activity != null) {
                Intent intent = new Intent();
                intent.putExtra("mucType", 2);
                intent.putExtra("sessionId", this.mSessionId);
                activity.setResult(-1, intent);
            }
            dismiss();
        }
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }
}
