package com.zipow.videobox.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.AddrBookSettingActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.p013mm.ZMBuddySyncInstance;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.videomeetings.C4558R;

public class SettingMessengerFragment extends ZMDialogFragment implements OnClickListener {
    private static final int REQUEST_ENABLE_ADDRBOOK = 100;
    private Button mBtnBack;
    private CheckedTextView mChkAlertImMsg;
    private CheckedTextView mChkAlertSound;
    private CheckedTextView mChkAlertVibrate;
    private CheckedTextView mChkDisableAddonNotification;
    private CheckedTextView mChkEnableAddrBook;
    private CheckedTextView mChkShowLinkPreviewDetail;
    private CheckedTextView mChkShowOfflineBuddies;
    private View mImgNotificationIdle;
    private View mImgNotificationInstant;
    private View mOptionAlertImMsg;
    private View mOptionAlertSound;
    private View mOptionAlertVibrate;
    private View mOptionDisableAddonNotification;
    private View mOptionShowLinkPreviewDetail;
    private View mOptionShowOfflineBuddies;
    private View mPanelAlertImMsg;
    private View mPanelAlertOptions;
    private View mPanelNotification;
    private View mPanelNotificationIdle;
    private View mPanelNotificationInstant;
    private View mTxtAlertOptionDes;

    public static void showAsActivity(@Nullable Fragment fragment) {
        if (fragment != null) {
            SimpleActivity.show(fragment, SettingMessengerFragment.class.getName(), new Bundle(), 0);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_setting_messenger, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mChkAlertImMsg = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAlertImMsg);
        this.mChkEnableAddrBook = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkEnableAddrBook);
        this.mChkShowOfflineBuddies = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowOfflineBuddies);
        this.mChkAlertSound = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAlertSound);
        this.mChkAlertVibrate = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAlertVibrate);
        this.mChkDisableAddonNotification = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkDisableAddonNotification);
        this.mPanelAlertOptions = inflate.findViewById(C4558R.C4560id.panelAlertOptions);
        this.mOptionShowOfflineBuddies = inflate.findViewById(C4558R.C4560id.optionShowOfflineBuddies);
        this.mOptionAlertImMsg = inflate.findViewById(C4558R.C4560id.optionAlertImMsg);
        this.mOptionAlertSound = inflate.findViewById(C4558R.C4560id.optionAlertSound);
        this.mOptionAlertVibrate = inflate.findViewById(C4558R.C4560id.optionAlertVibrate);
        this.mPanelAlertImMsg = inflate.findViewById(C4558R.C4560id.panelAlertImMsg);
        this.mTxtAlertOptionDes = inflate.findViewById(C4558R.C4560id.txtAlertOptionDes);
        this.mOptionDisableAddonNotification = inflate.findViewById(C4558R.C4560id.optionDisableAddonNotification);
        this.mOptionShowLinkPreviewDetail = inflate.findViewById(C4558R.C4560id.optionShowLinkPreviewDetail);
        this.mChkShowLinkPreviewDetail = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkShowLinkPreviewDetail);
        this.mPanelNotification = inflate.findViewById(C4558R.C4560id.panelNotification);
        this.mPanelNotificationInstant = inflate.findViewById(C4558R.C4560id.panelNotificationInstant);
        this.mImgNotificationInstant = inflate.findViewById(C4558R.C4560id.imgNotificationInstant);
        this.mPanelNotificationIdle = inflate.findViewById(C4558R.C4560id.panelNotificationIdle);
        this.mImgNotificationIdle = inflate.findViewById(C4558R.C4560id.imgNotificationIdle);
        this.mBtnBack.setOnClickListener(this);
        this.mOptionAlertImMsg.setOnClickListener(this);
        this.mChkEnableAddrBook.setOnClickListener(this);
        this.mOptionShowOfflineBuddies.setOnClickListener(this);
        this.mOptionAlertSound.setOnClickListener(this);
        this.mOptionAlertVibrate.setOnClickListener(this);
        this.mChkDisableAddonNotification.setOnClickListener(this);
        this.mPanelNotificationInstant.setOnClickListener(this);
        this.mPanelNotificationIdle.setOnClickListener(this);
        this.mOptionShowLinkPreviewDetail.setOnClickListener(this);
        return inflate;
    }

    private void updateUI() {
        if (PTApp.getInstance().getZoomMessenger() != null) {
            if (!PTApp.getInstance().hasMessenger() || PTApp.getInstance().getZoomMessenger().imChatGetOption() == 2) {
                this.mOptionShowLinkPreviewDetail.setVisibility(8);
            } else {
                this.mOptionShowLinkPreviewDetail.setVisibility(0);
            }
            this.mChkShowLinkPreviewDetail.setChecked(isImLlinkPreviewDescription());
        }
    }

    private void saveShowOfflineBuddies(boolean z) {
        PTSettingHelper.saveShowOfflineBuddies(z);
        this.mChkShowOfflineBuddies.setChecked(PTSettingHelper.getShowOfflineBuddies());
        ZMBuddySyncInstance.getInsatance().requestBuddyListUpdate();
    }

    private boolean getPlayAlertSound() {
        if (PTApp.getInstance().getSettingHelper() != null) {
            return PTSettingHelper.getPlayAlertSound();
        }
        return true;
    }

    private void savePlayAlertSound(boolean z) {
        if (PTApp.getInstance().getSettingHelper() != null) {
            PTSettingHelper.savePlayAlertSound(z);
        }
        this.mChkAlertSound.setChecked(getPlayAlertSound());
    }

    private boolean getPlayAlertVibrate() {
        if (PTApp.getInstance().getSettingHelper() != null) {
            return PTSettingHelper.getPlayAlertVibrate();
        }
        return true;
    }

    private void saveImLlinkPreviewDescription(boolean z) {
        if (PTApp.getInstance().getSettingHelper() != null) {
            PTSettingHelper.saveImLlinkPreviewDescription(z);
        }
        this.mChkShowLinkPreviewDetail.setChecked(isImLlinkPreviewDescription());
    }

    private boolean isImLlinkPreviewDescription() {
        if (PTApp.getInstance().getSettingHelper() != null) {
            return PTSettingHelper.isImLlinkPreviewDescription();
        }
        return true;
    }

    private void savePlayAlertVibrate(boolean z) {
        if (PTApp.getInstance().getSettingHelper() != null) {
            PTSettingHelper.savePlayAlertVibrate(z);
        }
        this.mChkAlertVibrate.setChecked(getPlayAlertVibrate());
    }

    private void showConnectionError() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Toast.makeText(activity, C4558R.string.zm_msg_disconnected_try_again, 1).show();
        }
    }

    public void onResume() {
        super.onResume();
        this.mChkEnableAddrBook.setChecked(isPhoneNumberRegistered());
        updateUI();
    }

    private boolean isPhoneNumberRegistered() {
        return PTApp.getInstance().isPhoneNumberRegistered();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.optionAlertImMsg) {
            onClickChkAlertImMsg();
        } else if (id == C4558R.C4560id.chkEnableAddrBook) {
            onClickChkEnableAddrBook();
        } else if (id == C4558R.C4560id.optionShowOfflineBuddies) {
            onClickChkShowOfflineBuddies();
        } else if (id == C4558R.C4560id.optionAlertSound) {
            onClickChkAlertSound();
        } else if (id == C4558R.C4560id.optionAlertVibrate) {
            onClickChkAlertVibrate();
        } else if (id != C4558R.C4560id.chkDisableAddonNotification) {
            if (id == C4558R.C4560id.panelNotificationInstant) {
                onClickPanelNotificationInstant();
            } else if (id == C4558R.C4560id.panelNotificationIdle) {
                onClickPanelNotificationIdle();
            } else if (id == C4558R.C4560id.optionShowLinkPreviewDetail) {
                onClickOptionShowLinkPreviewDetail();
            }
        }
    }

    private void onClickOptionShowLinkPreviewDetail() {
        saveImLlinkPreviewDescription(!this.mChkShowLinkPreviewDetail.isChecked());
    }

    private void onClickPanelNotificationInstant() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood() || !NetworkUtil.hasDataNetwork(getActivity())) {
                showConnectionError();
                return;
            }
            PTSettingHelper.saveShowChatMessageReminder(4);
            updateUI();
        }
    }

    private void onClickPanelNotificationIdle() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood() || !NetworkUtil.hasDataNetwork(getActivity())) {
                showConnectionError();
                return;
            }
            PTSettingHelper.saveShowChatMessageReminder(5);
            updateUI();
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

    private void onClickChkAlertImMsg() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            if (!zoomMessenger.isConnectionGood() || !NetworkUtil.hasDataNetwork(getActivity())) {
                showConnectionError();
                return;
            }
            int i = 2;
            if (zoomMessenger.blockAll_Get() == 2) {
                i = 5;
            }
            PTSettingHelper.saveShowChatMessageReminder(i);
            updateUI();
        }
    }

    private void onClickChkEnableAddrBook() {
        AddrBookSettingActivity.show((Fragment) this, 100);
    }

    private void onClickChkShowOfflineBuddies() {
        saveShowOfflineBuddies(!this.mChkShowOfflineBuddies.isChecked());
    }

    private void onClickChkAlertSound() {
        savePlayAlertSound(!this.mChkAlertSound.isChecked());
    }

    private void onClickChkAlertVibrate() {
        savePlayAlertVibrate(!this.mChkAlertVibrate.isChecked());
    }
}
