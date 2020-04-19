package com.zipow.videobox.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.kubi.IKubiService;
import com.zipow.videobox.kubi.KubiContract;
import com.zipow.videobox.kubi.KubiDevice;
import com.zipow.videobox.kubi.KubiServiceManager;
import com.zipow.videobox.kubi.KubiServiceManager.IKubiServiceConnectionListener;
import com.zipow.videobox.kubi.SettingMeetingKubiItem;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ActivityStartHelper;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMPolicyDataHelper;
import java.util.ArrayList;
import java.util.Iterator;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.HardwareUtil;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class SettingMeetingFragment extends ZMDialogFragment implements OnClickListener, IKubiServiceConnectionListener {
    private static final int REQUEST_AUTO_CONNECT_AUDIO = 1020;
    private static final int REQUEST_ENABLE_BT = 1017;
    private static final int REQUEST_LOCATION_PERMISSION_FOR_KUBI = 1018;
    private static final int REQUEST_REACTION_SKIN_TONE = 1021;
    private static final String TAG = "SettingMeetingFragment";
    @Nullable
    private BroadcastReceiver mBluetoothStateReceiver;
    private Button mBtnBack;
    private CheckedTextView mChkAutoMuteMic;
    private CheckedTextView mChkClosedCaption;
    private CheckedTextView mChkDriveMode;
    private CheckedTextView mChkEnableDrivingMode;
    private CheckedTextView mChkEnableKubiRobot;
    private CheckedTextView mChkEnableOriginalAudio;
    private CheckedTextView mChkNotOpenCamera;
    private CheckedTextView mChkShowJoinLeaveTip;
    private CheckedTextView mChkShowNoVideo;
    private CheckedTextView mChkShowTimer;
    private CheckedTextView mChkTurnOnAutoCopyMeetingLink;
    private CheckedTextView mChkTurnOnVideoWithoutPreview;
    @NonNull
    private Handler mHandler = new Handler();
    @Nullable
    private ArrayList<KubiDevice> mKubiDevices;
    @Nullable
    private BroadcastReceiver mKubiMsgReceiver;
    private View mOptionAutoConnectAudio;
    private View mOptionAutoMuteMic;
    private View mOptionClosedCaption;
    private View mOptionDriveMode;
    private View mOptionEnableDrivingMode;
    private View mOptionEnableKubiRobot;
    private View mOptionEnableOriginalAudio;
    private View mOptionNotOpenCamera;
    private View mOptionReactionSkinTone;
    private View mOptionShowJoinLeaveTip;
    private View mOptionShowNoVideo;
    private View mOptionShowTimer;
    private View mOptionTurnOnAutoCopyMeetingLink;
    private View mOptionTurnOnVideoWithoutPreview;
    private View mPanelAvailableKubis;
    private View mPanelEnableKubiRobot;
    private ViewGroup mPanelKubisContainer;
    private View mProgressScanKubi;
    private TextView mTxtAutoConnectAudioSelection;
    private TextView mTxtEnableKubiRobotInstructions;

    private void onKubiDeviceFound(KubiDevice kubiDevice) {
    }

    public void onKubiServiceDisconnected() {
    }

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, SettingMeetingFragment.class.getName(), new Bundle(), 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_setting_meeting, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mChkEnableDrivingMode = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkEnableDrivingMode);
        this.mChkAutoMuteMic = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAutoMuteMic);
        this.mChkNotOpenCamera = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkNotOpenCamera);
        this.mPanelEnableKubiRobot = inflate.findViewById(C4558R.C4560id.panelEnableKubiRobot);
        this.mChkEnableKubiRobot = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkEnableKubiRobot);
        this.mTxtEnableKubiRobotInstructions = (TextView) inflate.findViewById(C4558R.C4560id.txtEnableKubiRobotInstructions);
        this.mChkClosedCaption = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkClosedCaption);
        this.mChkShowTimer = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowTimer);
        this.mChkDriveMode = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkDriveMode);
        this.mChkShowNoVideo = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowNoVideo);
        this.mChkShowJoinLeaveTip = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowJoinLeaveTip);
        this.mChkEnableOriginalAudio = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkOriginalAudio);
        this.mOptionEnableDrivingMode = inflate.findViewById(C4558R.C4560id.optionEnableDrivingMode);
        this.mOptionAutoMuteMic = inflate.findViewById(C4558R.C4560id.optionAutoMuteMic);
        this.mOptionNotOpenCamera = inflate.findViewById(C4558R.C4560id.optionNotOpenCamera);
        this.mOptionEnableKubiRobot = inflate.findViewById(C4558R.C4560id.optionEnableKubiRobot);
        this.mOptionClosedCaption = inflate.findViewById(C4558R.C4560id.optionCloseCaption);
        this.mOptionShowTimer = inflate.findViewById(C4558R.C4560id.optionShowTimer);
        this.mOptionDriveMode = inflate.findViewById(C4558R.C4560id.optionDriveMode);
        this.mOptionAutoConnectAudio = inflate.findViewById(C4558R.C4560id.optionAutoConnectAudio);
        this.mOptionShowNoVideo = inflate.findViewById(C4558R.C4560id.optionShowNoVideo);
        this.mOptionShowJoinLeaveTip = inflate.findViewById(C4558R.C4560id.optionShowJoinLeaveTip);
        this.mOptionReactionSkinTone = inflate.findViewById(C4558R.C4560id.optionReactionSkinTone);
        this.mOptionEnableOriginalAudio = inflate.findViewById(C4558R.C4560id.optionEnableOriginalAudio);
        this.mTxtAutoConnectAudioSelection = (TextView) inflate.findViewById(C4558R.C4560id.txtAutoConnectAudioSelection);
        this.mPanelAvailableKubis = inflate.findViewById(C4558R.C4560id.panelAvailableKubis);
        this.mOptionTurnOnVideoWithoutPreview = inflate.findViewById(C4558R.C4560id.optionTurnOnVideoWithoutPreview);
        this.mChkTurnOnVideoWithoutPreview = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkTurnOnVideoWithoutPreview);
        this.mOptionTurnOnAutoCopyMeetingLink = inflate.findViewById(C4558R.C4560id.optionTurnOnAutoCopyMeetingLink);
        this.mChkTurnOnAutoCopyMeetingLink = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkTurnOnAutoCopyMeetingLink);
        this.mProgressScanKubi = inflate.findViewById(C4558R.C4560id.progressScanKubi);
        this.mPanelKubisContainer = (ViewGroup) inflate.findViewById(C4558R.C4560id.panelKubisContainer);
        this.mChkEnableDrivingMode.setChecked(getEnabledDrivingMode());
        this.mChkAutoMuteMic.setChecked(PTSettingHelper.AlwaysMuteMicWhenJoinVoIP());
        this.mChkEnableKubiRobot.setChecked(getEnabledKubiRobot());
        this.mChkClosedCaption.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.CLOSED_CAPTION_ENABLED, true));
        this.mChkShowTimer.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.SHOW_TIMER_ENABLED, false));
        this.mChkDriveMode.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.DRIVE_MODE_ENABLED, true));
        this.mChkTurnOnVideoWithoutPreview.setChecked(!PTSettingHelper.NeverConfirmVideoPrivacyWhenJoinMeeting());
        this.mChkTurnOnAutoCopyMeetingLink.setChecked(ZMPolicyUIHelper.getAutoCopyLink());
        this.mBtnBack.setOnClickListener(this);
        this.mOptionEnableDrivingMode.setOnClickListener(this);
        this.mOptionAutoMuteMic.setOnClickListener(this);
        this.mOptionNotOpenCamera.setOnClickListener(this);
        this.mOptionEnableKubiRobot.setOnClickListener(this);
        this.mOptionClosedCaption.setOnClickListener(this);
        this.mOptionShowTimer.setOnClickListener(this);
        this.mOptionDriveMode.setOnClickListener(this);
        this.mOptionTurnOnVideoWithoutPreview.setOnClickListener(this);
        this.mOptionTurnOnAutoCopyMeetingLink.setOnClickListener(this);
        this.mOptionAutoConnectAudio.setOnClickListener(this);
        this.mOptionShowNoVideo.setOnClickListener(this);
        this.mOptionShowJoinLeaveTip.setOnClickListener(this);
        this.mOptionReactionSkinTone.setOnClickListener(this);
        this.mOptionEnableOriginalAudio.setOnClickListener(this);
        if (!isKubiSupported()) {
            this.mPanelEnableKubiRobot.setVisibility(8);
            this.mTxtEnableKubiRobotInstructions.setVisibility(8);
        }
        ZMPolicyUIHelper.applyNotOpenCamera(this.mChkNotOpenCamera, this.mOptionNotOpenCamera);
        ZMPolicyUIHelper.applyAutoHideNoVideoUsers(this.mChkShowNoVideo, this.mOptionShowNoVideo);
        ZMPolicyUIHelper.applyShowJoinLeaveTip(this.mChkShowJoinLeaveTip, this.mOptionShowJoinLeaveTip);
        ZMPolicyUIHelper.applyOriginalAudio(this.mChkEnableOriginalAudio, this.mOptionEnableOriginalAudio);
        return inflate;
    }

    private boolean isKubiSupported() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        boolean z = currentUserProfile != null && currentUserProfile.isKubiEnabled();
        boolean z2 = UIUtil.isTablet(getActivity()) && HardwareUtil.isBluetoothLESupported(getActivity());
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().checkStartKubiService();
        KubiServiceManager instance = KubiServiceManager.getInstance(getActivity());
        if (instance != null) {
            instance.addConnectionListener(this);
        }
        if (isKubiSupported()) {
            registerKubiReceiver();
            registerBluetoothStatusReceiver();
            this.mTxtEnableKubiRobotInstructions.setVisibility(0);
        }
        this.mPanelAvailableKubis.setVisibility(8);
        if (getEnabledKubiRobot()) {
            showKubiDevices();
            startScanKubis(true);
        }
        updateAutoConnectAudio();
    }

    private void registerBluetoothStatusReceiver() {
        FragmentActivity activity = getActivity();
        if (activity != null && this.mBluetoothStateReceiver == null) {
            this.mBluetoothStateReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, @NonNull Intent intent) {
                    SettingMeetingFragment.this.onBluetoothStateChanged(intent);
                }
            };
            activity.registerReceiver(this.mBluetoothStateReceiver, new IntentFilter("android.bluetooth.adapter.action.STATE_CHANGED"));
        }
    }

    /* access modifiers changed from: private */
    public void onBluetoothStateChanged(Intent intent) {
        String action = intent.getAction();
        if (action != null && action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", Integer.MIN_VALUE);
            if (intExtra != 10) {
                if (intExtra == 12 && getEnabledKubiRobot()) {
                    startScanKubis(false);
                }
            } else if (getEnabledKubiRobot()) {
                this.mKubiDevices = null;
                this.mPanelAvailableKubis.setVisibility(8);
                this.mTxtEnableKubiRobotInstructions.setVisibility(0);
            }
        }
    }

    private void registerKubiReceiver() {
        FragmentActivity activity = getActivity();
        if (activity != null && this.mKubiMsgReceiver == null) {
            this.mKubiMsgReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    SettingMeetingFragment.this.onKubiMessageReceived(intent);
                }
            };
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(KubiContract.ACTION_KUBI_CONNECTION_STATUS);
            intentFilter.addAction(KubiContract.ACTION_KUBI_DEVICE_FOUND);
            intentFilter.addAction(KubiContract.ACTION_KUBI_MANAGER_FAILED);
            intentFilter.addAction(KubiContract.ACTION_KUBI_MANAGER_STATUS_CHANGED);
            intentFilter.addAction(KubiContract.ACTION_KUBI_SCAN_COMPLETE);
            BroadcastReceiver broadcastReceiver = this.mKubiMsgReceiver;
            StringBuilder sb = new StringBuilder();
            sb.append(activity.getPackageName());
            sb.append(".permission.KUBI_MESSAGE");
            activity.registerReceiver(broadcastReceiver, intentFilter, sb.toString(), this.mHandler);
        }
    }

    private void unregisterKubiReceiver() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            BroadcastReceiver broadcastReceiver = this.mKubiMsgReceiver;
            if (broadcastReceiver != null) {
                activity.unregisterReceiver(broadcastReceiver);
            }
            this.mKubiMsgReceiver = null;
        }
    }

    private void unregisterBluetoothStatusReceiver() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            BroadcastReceiver broadcastReceiver = this.mBluetoothStateReceiver;
            if (broadcastReceiver != null) {
                activity.unregisterReceiver(broadcastReceiver);
            }
            this.mBluetoothStateReceiver = null;
        }
    }

    /* access modifiers changed from: private */
    public void onKubiMessageReceived(@Nullable Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (KubiContract.ACTION_KUBI_CONNECTION_STATUS.equals(action)) {
                onKubiDeviceConnectionStatus(intent.getBooleanExtra(KubiContract.EXTRA_CONNECTED, false));
            } else if (KubiContract.ACTION_KUBI_DEVICE_FOUND.equals(action)) {
                onKubiDeviceFound((KubiDevice) intent.getParcelableExtra(KubiContract.EXTRA_DEVICE));
            } else if (KubiContract.ACTION_KUBI_MANAGER_FAILED.equals(action)) {
                onKubiManagerFailed(intent.getIntExtra(KubiContract.EXTRA_REASON, 0));
            } else if (KubiContract.ACTION_KUBI_MANAGER_STATUS_CHANGED.equals(action)) {
                onKubiManagerStatusChanged(intent.getIntExtra(KubiContract.EXTRA_OLD_STATUS, 0), intent.getIntExtra(KubiContract.EXTRA_NEW_STATUS, 0));
            } else if (KubiContract.ACTION_KUBI_SCAN_COMPLETE.equals(action)) {
                onKubiScanComplete(intent.getParcelableArrayListExtra(KubiContract.EXTRA_DEVICES));
            }
        }
    }

    private void onKubiDeviceConnectionStatus(boolean z) {
        showKubiDevices();
    }

    private void onKubiManagerFailed(int i) {
        this.mProgressScanKubi.setVisibility(8);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                if (SettingMeetingFragment.this.isResumed() && SettingMeetingFragment.this.checkBluetoothStatus()) {
                    SettingMeetingFragment.this.startScanKubis(false);
                }
            }
        }, 3000);
    }

    private void onKubiManagerStatusChanged(int i, int i2) {
        if (i != 0 && i2 == 0 && checkBluetoothStatus()) {
            startScanKubis(true);
        }
    }

    private void onKubiScanComplete(@Nullable ArrayList<KubiDevice> arrayList) {
        this.mKubiDevices = arrayList;
        showKubiDevices();
        KubiDevice connectedKubi = getConnectedKubi();
        if ((arrayList == null || arrayList.size() == 0) && connectedKubi == null) {
            startScanKubis(true);
            return;
        }
        this.mProgressScanKubi.setVisibility(8);
        if (arrayList != null && arrayList.size() > 0 && connectedKubi == null) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    SettingMeetingFragment.this.connectNearestKubi();
                }
            }, 500);
        }
    }

    /* access modifiers changed from: private */
    public void connectNearestKubi() {
        if (this.mKubiDevices != null && isResumed() && getEnabledKubiRobot()) {
            KubiDevice nearestKubi = getNearestKubi(this.mKubiDevices);
            if (nearestKubi != null) {
                connectKubi(getKubiItemForDevice(nearestKubi), nearestKubi);
            }
        }
    }

    @Nullable
    private KubiDevice getNearestKubi(@Nullable ArrayList<KubiDevice> arrayList) {
        KubiDevice kubiDevice = null;
        if (arrayList == null) {
            return null;
        }
        int i = Integer.MIN_VALUE;
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            KubiDevice kubiDevice2 = (KubiDevice) it.next();
            if (kubiDevice2 != null) {
                int rssi = kubiDevice2.getRSSI();
                if (i < rssi) {
                    kubiDevice = kubiDevice2;
                    i = rssi;
                }
            }
        }
        return kubiDevice;
    }

    @Nullable
    private SettingMeetingKubiItem getKubiItemForDevice(@Nullable KubiDevice kubiDevice) {
        if (kubiDevice == null) {
            return null;
        }
        int childCount = this.mPanelKubisContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            SettingMeetingKubiItem settingMeetingKubiItem = (SettingMeetingKubiItem) this.mPanelKubisContainer.getChildAt(i);
            if (settingMeetingKubiItem != null && kubiDevice.equals(settingMeetingKubiItem.getKubiDevice())) {
                return settingMeetingKubiItem;
            }
        }
        return null;
    }

    private KubiDevice getConnectedKubi() {
        KubiServiceManager instance = KubiServiceManager.getInstance(getActivity());
        if (instance == null) {
            return null;
        }
        IKubiService kubiService = instance.getKubiService();
        if (kubiService == null) {
            return null;
        }
        try {
            if (kubiService.getKubiStatus() == 4) {
                return kubiService.getCurrentKubi();
            }
        } catch (RemoteException unused) {
        }
        return null;
    }

    private void showKubiDevices() {
        this.mPanelKubisContainer.removeAllViews();
        KubiDevice connectedKubi = getConnectedKubi();
        if (connectedKubi != null) {
            this.mPanelKubisContainer.addView(createKubiItem(connectedKubi, 2));
        }
        ArrayList<KubiDevice> arrayList = this.mKubiDevices;
        if (arrayList != null) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                final KubiDevice kubiDevice = (KubiDevice) it.next();
                if (kubiDevice != null && !kubiDevice.equals(connectedKubi)) {
                    final SettingMeetingKubiItem createKubiItem = createKubiItem(kubiDevice, 0);
                    this.mPanelKubisContainer.addView(createKubiItem);
                    createKubiItem.setOnClickListener(new OnClickListener() {
                        public void onClick(View view) {
                            SettingMeetingFragment.this.connectKubi(createKubiItem, kubiDevice);
                        }
                    });
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void connectKubi(@Nullable SettingMeetingKubiItem settingMeetingKubiItem, KubiDevice kubiDevice) {
        KubiServiceManager instance = KubiServiceManager.getInstance(getActivity());
        if (instance != null) {
            IKubiService kubiService = instance.getKubiService();
            if (kubiService != null) {
                try {
                    kubiService.connectToKubi(kubiDevice);
                    if (settingMeetingKubiItem != null) {
                        settingMeetingKubiItem.setKubiStatus(1);
                    }
                    KubiDevice connectedKubi = getConnectedKubi();
                    if (connectedKubi != null) {
                        SettingMeetingKubiItem kubiItemForDevice = getKubiItemForDevice(connectedKubi);
                        if (kubiItemForDevice != null) {
                            kubiItemForDevice.setKubiStatus(0);
                        }
                    }
                } catch (RemoteException unused) {
                }
            }
        }
    }

    @NonNull
    private SettingMeetingKubiItem createKubiItem(KubiDevice kubiDevice, int i) {
        SettingMeetingKubiItem settingMeetingKubiItem = new SettingMeetingKubiItem(getActivity());
        settingMeetingKubiItem.setKubiDevice(kubiDevice);
        settingMeetingKubiItem.setKubiStatus(i);
        return settingMeetingKubiItem;
    }

    public void onPause() {
        super.onPause();
        KubiServiceManager instance = KubiServiceManager.getInstance(getActivity());
        if (instance != null) {
            instance.removeConnectionListener(this);
        }
        unregisterKubiReceiver();
        unregisterBluetoothStatusReceiver();
    }

    public void onKubiServiceConnected(IKubiService iKubiService) {
        startScanKubis(true);
    }

    /* access modifiers changed from: private */
    public void startScanKubis(boolean z) {
        if (getEnabledKubiRobot()) {
            if (z && !checkBluetoothStatus()) {
                turnOnBluetoothForKubi();
            } else if (checkLocationPermissionForKubi()) {
                doScanKubis();
            } else {
                requestLocationPermissionForKubi();
            }
        }
    }

    private void doScanKubis() {
        KubiServiceManager instance = KubiServiceManager.getInstance(getActivity());
        if (instance == null) {
            this.mProgressScanKubi.setVisibility(8);
            return;
        }
        IKubiService kubiService = instance.getKubiService();
        if (kubiService == null) {
            this.mProgressScanKubi.setVisibility(8);
            return;
        }
        try {
            kubiService.findAllKubiDevices();
            this.mPanelAvailableKubis.setVisibility(0);
            this.mTxtEnableKubiRobotInstructions.setVisibility(8);
            this.mProgressScanKubi.setVisibility(0);
        } catch (RemoteException unused) {
        }
    }

    private boolean getEnabledDrivingMode() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper == null) {
            return false;
        }
        return settingHelper.isDriveModeSettingOn();
    }

    private void saveEnabledDrivingMode(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null && !settingHelper.setDriveMode(z)) {
            this.mChkEnableDrivingMode.setChecked(getEnabledDrivingMode());
        }
    }

    private void saveAutoMuteMic(boolean z) {
        PTSettingHelper.SetAlwaysMuteMicWhenJoinVoIP(z);
        this.mChkAutoMuteMic.setChecked(z);
    }

    private void saveNotOpenCamera(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            settingHelper.setNeverStartVideoWhenJoinMeeting(z);
            this.mChkNotOpenCamera.setChecked(z);
        }
    }

    private void saveIsShowTimerEnabled(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SHOW_TIMER_ENABLED, z);
    }

    private void saveIsDriveModeEnabled(boolean z) {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.DRIVE_MODE_ENABLED, z);
    }

    private boolean getEnabledKubiRobot() {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper == null) {
            return false;
        }
        return settingHelper.getIsKubiDeviceEnabled();
    }

    private void saveEnableKubiRobot(boolean z) {
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            settingHelper.saveIsKubiDeviceEnabled(z);
            boolean enabledKubiRobot = getEnabledKubiRobot();
            this.mChkEnableKubiRobot.setChecked(enabledKubiRobot);
            KubiServiceManager instance = KubiServiceManager.getInstance(getActivity());
            if (enabledKubiRobot) {
                instance.startKubiService(KubiContract.ACTION_START_KUBI_SERVICE_NO_AUTO_CONNECT);
                instance.connectKubiService(false);
                showKubiDevices();
            } else {
                instance.stopKubiService();
                this.mPanelAvailableKubis.setVisibility(8);
                this.mTxtEnableKubiRobotInstructions.setVisibility(0);
                this.mKubiDevices = null;
            }
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.optionEnableDrivingMode) {
            onClickChkEnableDrivingMode();
        } else if (id == C4558R.C4560id.optionAutoMuteMic) {
            onClickChkAutoMuteMic();
        } else if (id == C4558R.C4560id.optionNotOpenCamera) {
            onClickChkNotOpenCamera();
        } else if (id == C4558R.C4560id.optionEnableKubiRobot) {
            onClickChkEnableKubiRobot();
        } else if (id == C4558R.C4560id.optionCloseCaption) {
            onClickChkClosedCaption();
        } else if (id == C4558R.C4560id.optionShowTimer) {
            onClickShowTimer();
        } else if (id == C4558R.C4560id.optionDriveMode) {
            onClickDriveMode();
        } else if (id == C4558R.C4560id.optionTurnOnVideoWithoutPreview) {
            onClickTurnOnVideoWithoutPreview();
        } else if (id == C4558R.C4560id.optionAutoConnectAudio) {
            onClickAutoConnectAudio();
        } else if (id == C4558R.C4560id.optionTurnOnAutoCopyMeetingLink) {
            onClickTurnOnAutoCopyMeetingLink();
        } else if (id == C4558R.C4560id.optionShowNoVideo) {
            onClickShowNoVideo();
        } else if (id == C4558R.C4560id.optionShowJoinLeaveTip) {
            onClickShowJoinLeaveTip();
        } else if (id == C4558R.C4560id.optionReactionSkinTone) {
            onClickReactionSkinTone();
        } else if (id == C4558R.C4560id.optionEnableOriginalAudio) {
            onClickEnableOriginalAudio();
        }
    }

    private void onClickReactionSkinTone() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            MeetingReactionSkinToneFragment.showAsActivity(zMActivity, 1021);
        }
    }

    private void onClickEnableOriginalAudio() {
        CheckedTextView checkedTextView = this.mChkEnableOriginalAudio;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        PTSettingHelper.SetOriginalSoundChangable(this.mChkEnableOriginalAudio.isChecked());
    }

    private void onClickShowJoinLeaveTip() {
        CheckedTextView checkedTextView = this.mChkShowJoinLeaveTip;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        ZMPolicyDataHelper.getInstance().setBooleanValue(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip, this.mChkShowJoinLeaveTip.isChecked());
    }

    private void onClickShowNoVideo() {
        CheckedTextView checkedTextView = this.mChkShowNoVideo;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        PTSettingHelper.SetHideNoVideoUserInWallView(!this.mChkShowNoVideo.isChecked());
    }

    private void onClickTurnOnAutoCopyMeetingLink() {
        CheckedTextView checkedTextView = this.mChkTurnOnAutoCopyMeetingLink;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        ZMPolicyDataHelper.getInstance().setBooleanValue(DummyPolicyIDType.zPolicy_ShowInviteUrl, this.mChkTurnOnAutoCopyMeetingLink.isChecked());
    }

    private void onClickAutoConnectAudio() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            AutoConnectAudioFragment.showAsActivity(zMActivity, 1020);
        }
    }

    private void onClickBtnBack() {
        if (getShowsDialog()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onClickChkEnableDrivingMode() {
        CheckedTextView checkedTextView = this.mChkEnableDrivingMode;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        saveEnabledDrivingMode(this.mChkEnableDrivingMode.isChecked());
    }

    private void onClickChkAutoMuteMic() {
        CheckedTextView checkedTextView = this.mChkAutoMuteMic;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        saveAutoMuteMic(this.mChkAutoMuteMic.isChecked());
    }

    private void onClickChkNotOpenCamera() {
        CheckedTextView checkedTextView = this.mChkNotOpenCamera;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        saveNotOpenCamera(this.mChkNotOpenCamera.isChecked());
    }

    private void onClickChkEnableKubiRobot() {
        CheckedTextView checkedTextView = this.mChkEnableKubiRobot;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        saveEnableKubiRobot(this.mChkEnableKubiRobot.isChecked());
    }

    private void onClickChkClosedCaption() {
        CheckedTextView checkedTextView = this.mChkClosedCaption;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        PreferenceUtil.saveBooleanValue(PreferenceUtil.CLOSED_CAPTION_ENABLED, this.mChkClosedCaption.isChecked());
    }

    private void onClickShowTimer() {
        CheckedTextView checkedTextView = this.mChkShowTimer;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        saveIsShowTimerEnabled(this.mChkShowTimer.isChecked());
    }

    private void onClickDriveMode() {
        CheckedTextView checkedTextView = this.mChkDriveMode;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        saveIsDriveModeEnabled(this.mChkDriveMode.isChecked());
    }

    private void onClickTurnOnVideoWithoutPreview() {
        CheckedTextView checkedTextView = this.mChkTurnOnVideoWithoutPreview;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        PTSettingHelper.SetNeverConfirmVideoPrivacyWhenJoinMeeting(!this.mChkTurnOnVideoWithoutPreview.isChecked());
    }

    /* access modifiers changed from: private */
    public boolean checkBluetoothStatus() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null) {
            return false;
        }
        return defaultAdapter.isEnabled();
    }

    private boolean checkLocationPermissionForKubi() {
        if (VERSION.SDK_INT > 22 && checkSelfPermission("android.permission.ACCESS_FINE_LOCATION") != 0) {
            return false;
        }
        return true;
    }

    private void requestLocationPermissionForKubi() {
        new Builder(getActivity()).setMessage(C4558R.string.zm_kubi_request_location_permission).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingMeetingFragment.this.zm_requestPermissions(new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1018);
            }
        }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
    }

    private void turnOnBluetoothForKubi() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            PackageManager packageManager = activity.getPackageManager();
            if (packageManager == null || packageManager.checkPermission("android.permission.BLUETOOTH_ADMIN", activity.getPackageName()) != 0) {
                ActivityStartHelper.startActivityForResult((Fragment) this, new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 1017);
            } else {
                ZMAlertDialog create = new Builder(activity).setTitle(C4558R.string.zm_kubi_bluetooth_turn_on_request).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SettingMeetingFragment.this.turnOnBluetooth();
                    }
                }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SettingMeetingFragment.this.onTurnOnBluetoothRejected();
                    }
                }).create();
                create.setCanceledOnTouchOutside(true);
                create.show();
            }
        }
    }

    /* access modifiers changed from: private */
    public void turnOnBluetooth() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter != null && !defaultAdapter.isEnabled()) {
            defaultAdapter.enable();
        }
    }

    /* access modifiers changed from: private */
    public void onTurnOnBluetoothRejected() {
        this.mPanelAvailableKubis.setVisibility(8);
        this.mTxtEnableKubiRobotInstructions.setVisibility(0);
    }

    private void updateAutoConnectAudio() {
        this.mTxtAutoConnectAudioSelection.setText(autoType2str(PTSettingHelper.getAutoConnectAudio()));
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1017 && i2 == -1) {
            startScanKubis(false);
        }
    }

    public void onRequestPermissionsResult(final int i, @NonNull final String[] strArr, @NonNull final int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        getNonNullEventTaskManagerOrThrowException().push(new EventAction() {
            public void run(@NonNull IUIElement iUIElement) {
                ((SettingMeetingFragment) iUIElement).handleRequestPermissionResult(i, strArr, iArr);
            }
        });
    }

    /* access modifiers changed from: private */
    public void handleRequestPermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if (1018 == i && "android.permission.ACCESS_FINE_LOCATION".equals(strArr[i2]) && iArr[i2] == 0) {
                    startScanKubis(true);
                }
            }
        }
    }

    private String autoType2str(int i) {
        switch (i) {
            case 0:
                return getString(C4558R.string.zm_lbl_auto_connect_audio_off_92027);
            case 1:
                if (!ZMPolicyUIHelper.isComputerAudioDisabled()) {
                    return getString(C4558R.string.zm_lbl_auto_connect_audio_internet_112245);
                }
                return getString(C4558R.string.zm_lbl_auto_connect_audio_off_92027);
            case 2:
                if (PTSettingHelper.canSetAutoCallMyPhone()) {
                    return PTSettingHelper.getAutoCallPhoneNumber(getContext(), "");
                }
                return getString(C4558R.string.zm_lbl_auto_connect_audio_off_92027);
            case 3:
                if (ZMPolicyUIHelper.isComputerAudioDisabled() || !PTSettingHelper.canSetAutoCallMyPhone()) {
                    return getString(C4558R.string.zm_lbl_auto_connect_audio_off_92027);
                }
                return getString(C4558R.string.zm_lbl_auto_connect_audio_auto_select_abbr_92027);
            default:
                return "";
        }
    }
}
