package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.LinearGradient;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.MyProfileActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.ptapp.LogoutHandler;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.SIPCallEventListenerUI.SimpleSIPCallEventListener;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMBitmapFactory;
import com.zipow.videobox.view.AvatarView;
import com.zipow.videobox.view.AvatarView.ParamsBuilder;
import com.zipow.videobox.view.IMView.OnFragmentShowListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMTip;
import p021us.zoom.videomeetings.C4558R;

public class SettingFragment extends SettingTipFragment implements OnClickListener, IPTUIListener, OnFragmentShowListener {
    private static final String ARG_DISMISS_ON_SIGNOUT = "dismissOnSignout";
    private static final String ARG_NO_TITLE_BAR_BUTTONS = "noTitleBar";
    private static final String TAG = "SettingFragment";
    private AvatarView mAvatarView;
    private View mBtnAbout;
    private Button mBtnBack;
    private View mBtnChats;
    private View mBtnMeeting;
    private View mBtnSignout;
    private ImageView mImgAccountType;
    private ImageView mImgIndicatorAbout;
    private ImageView mImgIndicatorSetProfile;
    private View mOptionAccountEmail;
    private View mOptionIntergreatedPhone;
    private View mOptionMMProfile;
    private View mOptionPhoneNumber;
    private View mPanelAbout;
    private View mPanelChats;
    private View mPanelIntergreatedPhone;
    private View mPanelPhoneNumber;
    private View mPanelProfile;
    @NonNull
    private SimpleSIPCallEventListener mSIPCallEventListener = new SimpleSIPCallEventListener() {
        public void OnPBXUserStatusChange(int i) {
            super.OnPBXUserStatusChange(i);
            SettingFragment.this.updatePanelIntergreatedPhoneUI();
        }
    };
    private TextView mTxtDisplayName;
    private TextView mTxtEmail;
    private TextView mTxtUserType;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i, boolean z) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_DISMISS_ON_SIGNOUT, z);
        SimpleActivity.show(zMActivity, SettingFragment.class.getName(), bundle, i, true, 1);
    }

    public static void show(@NonNull FragmentManager fragmentManager, int i) {
        if (getSettingFragment(fragmentManager) == null) {
            SettingFragment settingFragment = new SettingFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("anchorId", i);
            settingFragment.setArguments(bundle);
            settingFragment.show(fragmentManager, SettingFragment.class.getName());
        }
    }

    @Nullable
    public static SettingFragment getSettingFragment(FragmentManager fragmentManager) {
        return (SettingFragment) fragmentManager.findFragmentByTag(SettingFragment.class.getName());
    }

    public static boolean needShowNewTipsOnSettingsTab(@Nullable Context context) {
        if (context == null) {
            return false;
        }
        long readLongValue = PreferenceUtil.readLongValue(PreferenceUtil.LAST_CLEAR_NEW_TIP_ON_SETTINGS_TAB_TIME, 0);
        if (!hasProfile() && System.currentTimeMillis() - readLongValue > 86400000) {
            return true;
        }
        if (!SettingAboutFragment.needShowAboutTip(context) || System.currentTimeMillis() - readLongValue <= 86400000) {
            return false;
        }
        return true;
    }

    public static void saveNewTipsOnSettingsTabCleared() {
        PreferenceUtil.saveLongValue(PreferenceUtil.LAST_CLEAR_NEW_TIP_ON_SETTINGS_TAB_TIME, System.currentTimeMillis());
    }

    public static void saveNewVersionReadyTime() {
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.NEW_VERSION_ON_SERVER, null);
        String latestVersionString = PTApp.getInstance().getLatestVersionString();
        if (!StringUtil.isSameString(readStringValue, latestVersionString)) {
            PreferenceUtil.saveStringValue(PreferenceUtil.NEW_VERSION_ON_SERVER, latestVersionString);
            PreferenceUtil.saveLongValue(PreferenceUtil.LAST_GET_NEW_VERSION_NOTIFICATION_TIME, System.currentTimeMillis());
        }
    }

    private static boolean needShowSetProfileTip(Context context) {
        if (PreferenceUtil.readLongValue(PreferenceUtil.LAST_SHOW_SET_PROFILE_TIME, 0) <= 0 && !hasProfile()) {
            return true;
        }
        return false;
    }

    private static boolean hasProfile() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            String userName = currentUserProfile.getUserName();
            String pictureLocalPath = currentUserProfile.getPictureLocalPath();
            if (!StringUtil.isEmptyOrNull(userName) || !StringUtil.isEmptyOrNull(pictureLocalPath)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public static SettingFragment createSettingFragment(boolean z, boolean z2) {
        SettingFragment settingFragment = new SettingFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_NO_TITLE_BAR_BUTTONS, z);
        bundle.putBoolean(ARG_DISMISS_ON_SIGNOUT, z2);
        settingFragment.setArguments(bundle);
        return settingFragment;
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_setting, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mOptionMMProfile = inflate.findViewById(C4558R.C4560id.optionMMProfile);
        this.mTxtDisplayName = (TextView) inflate.findViewById(C4558R.C4560id.txtDisplayName);
        this.mAvatarView = (AvatarView) inflate.findViewById(C4558R.C4560id.avatarView);
        this.mBtnMeeting = inflate.findViewById(C4558R.C4560id.btnMeeting);
        this.mBtnAbout = inflate.findViewById(C4558R.C4560id.btnAbout);
        this.mImgIndicatorSetProfile = (ImageView) inflate.findViewById(C4558R.C4560id.imgIndicatorSetProfile);
        this.mImgIndicatorAbout = (ImageView) inflate.findViewById(C4558R.C4560id.imgIndicatorAbout);
        this.mTxtUserType = (TextView) inflate.findViewById(C4558R.C4560id.txtUserType);
        this.mTxtEmail = (TextView) inflate.findViewById(C4558R.C4560id.txtEmail);
        this.mImgAccountType = (ImageView) inflate.findViewById(C4558R.C4560id.imgAccountType);
        this.mOptionAccountEmail = inflate.findViewById(C4558R.C4560id.optionAccountEmail);
        View findViewById = inflate.findViewById(C4558R.C4560id.panelCopyright);
        this.mOptionPhoneNumber = inflate.findViewById(C4558R.C4560id.optionPhoneNumber);
        this.mBtnChats = inflate.findViewById(C4558R.C4560id.btnChats);
        this.mPanelChats = inflate.findViewById(C4558R.C4560id.panelChats);
        this.mPanelProfile = inflate.findViewById(C4558R.C4560id.panelProfile);
        this.mPanelPhoneNumber = inflate.findViewById(C4558R.C4560id.panelPhoneNumber);
        this.mPanelAbout = inflate.findViewById(C4558R.C4560id.panelAbout);
        this.mOptionIntergreatedPhone = inflate.findViewById(C4558R.C4560id.optionIntergreatedPhone);
        this.mPanelIntergreatedPhone = inflate.findViewById(C4558R.C4560id.panelIntergreatedPhone);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean(ARG_NO_TITLE_BAR_BUTTONS, false)) {
            this.mBtnBack.setVisibility(8);
        }
        this.mBtnBack.setOnClickListener(this);
        this.mOptionMMProfile.setOnClickListener(this);
        this.mBtnMeeting.setOnClickListener(this);
        this.mBtnAbout.setOnClickListener(this);
        this.mAvatarView.setOnClickListener(this);
        this.mOptionPhoneNumber.setOnClickListener(this);
        this.mBtnChats.setOnClickListener(this);
        this.mOptionIntergreatedPhone.setOnClickListener(this);
        View view = this.mBtnSignout;
        if (view != null) {
            view.setOnClickListener(this);
        }
        if (ResourcesUtil.getBoolean((Context) getActivity(), C4558R.bool.zm_config_no_copyright, false)) {
            findViewById.setVisibility(8);
        }
        if (!PTApp.getInstance().hasZoomMessenger()) {
            this.mOptionPhoneNumber.setVisibility(8);
        }
        updatePanelIntergreatedPhoneUI();
        return inflate;
    }

    private void updateDisplayName() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            String myName = PTApp.getInstance().getMyName();
            if (StringUtil.isEmptyOrNull(myName)) {
                myName = activity.getString(C4558R.string.zm_mm_lbl_not_set);
            }
            this.mTxtDisplayName.setText(myName);
            if (PTApp.getInstance().isPaidUser()) {
                this.mTxtUserType.setText(getString(PTApp.getInstance().isCorpUser() ? C4558R.string.zm_lbl_profile_user_type_onprem_up_122473 : C4558R.string.zm_lbl_profile_user_type_licensed_up_122473));
                setTextGradient(this.mTxtUserType);
            } else {
                this.mTxtUserType.setText(getString(C4558R.string.zm_lbl_profile_user_type_basic_up_122473));
                this.mTxtUserType.setTextColor(getResources().getColor(C4558R.color.zm_ui_kit_color_blue_0E71EB));
            }
        }
    }

    private void setTextGradient(@NonNull TextView textView) {
        LinearGradient linearGradient = new LinearGradient(0.0f, 0.0f, textView.getPaint().getTextSize() * ((float) textView.getText().length()), textView.getPaint().getTextSize(), new int[]{getResources().getColor(C4558R.color.zm_color_2E8CFF), getResources().getColor(C4558R.color.zm_color_FA6E26)}, new float[]{0.0f, 1.0f}, TileMode.CLAMP);
        textView.getPaint().setShader(linearGradient);
        textView.invalidate();
    }

    private void updateAvatar() {
        if (getActivity() != null) {
            String str = null;
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                str = currentUserProfile.getPictureLocalPath();
            }
            ParamsBuilder paramsBuilder = new ParamsBuilder();
            paramsBuilder.setName(PTApp.getInstance().getMyName(), getMyJid()).setPath(str);
            this.mAvatarView.show(paramsBuilder);
        }
    }

    private String getMyJid() {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger != null) {
            ZoomBuddy myself = zoomMessenger.getMyself();
            if (myself != null) {
                return myself.getJid();
            }
        }
        return null;
    }

    private void updateProfileIndicator() {
        if (needShowSetProfileTip(getActivity())) {
            this.mImgIndicatorSetProfile.setVisibility(0);
        } else {
            this.mImgIndicatorSetProfile.setVisibility(8);
        }
    }

    private void updateAboutIndicator() {
        if (SettingAboutFragment.needShowAboutTip(getActivity())) {
            this.mImgIndicatorAbout.setVisibility(0);
        } else {
            this.mImgIndicatorAbout.setVisibility(8);
        }
    }

    private void updateAccountEmail() {
        int loginType = getLoginType();
        if (ZmLoginHelper.isNormalTypeLogin(loginType)) {
            this.mTxtEmail.setText(ZmLoginHelper.getShowEmail(loginType));
            this.mImgAccountType.setImageResource(getImageIconByLoginType(loginType));
            this.mOptionAccountEmail.setVisibility(0);
        } else if (loginType == 98) {
            this.mOptionAccountEmail.setVisibility(8);
        }
    }

    private void updateMessengerPanel() {
        if (this.mPanelChats != null) {
            if (!PTApp.getInstance().hasZoomMessenger()) {
                this.mPanelChats.setVisibility(8);
            }
            ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
            if (zoomMessenger != null && zoomMessenger.imChatGetOption() == 2) {
                this.mPanelChats.setVisibility(8);
            }
        }
    }

    private int getImageIconByLoginType(int i) {
        if (i == 0) {
            return C4558R.C4559drawable.zm_ic_setting_fb;
        }
        if (i == 2) {
            return C4558R.C4559drawable.zm_ic_setting_google;
        }
        if (i != 11) {
            switch (i) {
                case 21:
                    return C4558R.C4559drawable.ic_login_wechat;
                case 22:
                    return C4558R.C4559drawable.ic_login_qq;
                case 23:
                    return C4558R.C4559drawable.ic_login_alipay;
                default:
                    switch (i) {
                        case 100:
                        case 101:
                            break;
                        default:
                            return C4558R.C4559drawable.zm_ic_setting_nolink;
                    }
            }
        }
        return C4558R.C4559drawable.zm_ic_setting_zoom;
    }

    private int getLoginType() {
        int pTLoginType = PTApp.getInstance().getPTLoginType();
        if (pTLoginType == 100 && PTApp.getInstance().getSavedZoomAccount() == null) {
            return 102;
        }
        return pTLoginType;
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().addPTUIListener(this);
        CmmSIPCallManager.getInstance().addListener(this.mSIPCallEventListener);
        updateDisplayName();
        updateAvatar();
        updateProfileIndicator();
        updateAboutIndicator();
        updateAccountEmail();
        updateMessengerPanel();
        checkLoginState();
        updatePanelIntergreatedPhoneUI();
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
        CmmSIPCallManager.getInstance().removeListener(this.mSIPCallEventListener);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 9 || i == 12) {
            updateDisplayName();
            updateAvatar();
        } else if (i == 1) {
            onLogout();
        }
    }

    private void checkLoginState() {
        if (!PTApp.getInstance().isWebSignedOn()) {
            Bundle arguments = getArguments();
            if (arguments == null || !arguments.getBoolean(ARG_DISMISS_ON_SIGNOUT)) {
                this.mPanelAbout.setVisibility(8);
                this.mPanelChats.setVisibility(8);
                this.mPanelPhoneNumber.setVisibility(8);
                this.mPanelProfile.setVisibility(8);
                SettingAboutFragment settingAboutFragmentInView = SettingAboutFragment.getSettingAboutFragmentInView(this);
                if (settingAboutFragmentInView == null) {
                    SettingAboutFragment.showSettingAboutFragmentInView(this, C4558R.C4560id.panelFragmentContent);
                } else {
                    getChildFragmentManager().beginTransaction().show(settingAboutFragmentInView).commit();
                }
            }
            onLogout();
        }
    }

    private void onLogout() {
        Bundle arguments = getArguments();
        if (arguments != null && arguments.getBoolean(ARG_DISMISS_ON_SIGNOUT)) {
            dismiss();
        }
    }

    public void dismiss() {
        if (getShowsTip()) {
            super.dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    public ZMTip onCreateTip(@NonNull Context context, LayoutInflater layoutInflater, Bundle bundle) {
        ZMTip onCreateTip = super.onCreateTip(context, layoutInflater, bundle);
        onCreateTip.findViewById(C4558R.C4560id.panelOptions).setBackgroundResource(0);
        this.mBtnBack.setVisibility(8);
        return onCreateTip;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnMeeting) {
            onClickBtnMeeting();
        } else if (id == C4558R.C4560id.btnAbout) {
            onClickBtnAbout();
        } else if (id == C4558R.C4560id.optionMMProfile) {
            onClickOptionMMProfile();
        } else if (id == C4558R.C4560id.avatarView) {
            onClickAvatarView();
        } else if (id == C4558R.C4560id.optionPhoneNumber) {
            onClickOptionPhoneNumber();
        } else if (id == C4558R.C4560id.btnChats) {
            onClickBtnChats();
        } else if (id == C4558R.C4560id.optionIntergreatedPhone) {
            onClickOptionIntergeatedPhone();
        }
    }

    private void onClickBtnChats() {
        MMChatSettingsFragment.showAsActivity(this);
    }

    private void onClickOptionIntergeatedPhone() {
        if (CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            IntergreatedPhoneFragment.showAsActivity(this);
        } else {
            SipIntergreatedPhoneFragment.showAsActivity(this);
        }
    }

    private void onClickBtnBack() {
        if (getShowsTip()) {
            dismiss();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }

    private void onClickBtnMeeting() {
        SettingMeetingFragment.showAsActivity(this);
    }

    private void onClickBtnAbout() {
        SettingAboutFragment.showAsActivity(this);
    }

    private void onClickOptionMMProfile() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            MyProfileActivity.show(zMActivity, 0);
            PreferenceUtil.saveLongValue(PreferenceUtil.LAST_SHOW_SET_PROFILE_TIME, System.currentTimeMillis());
        }
    }

    private void onClickAvatarView() {
        if (getActivity() != null) {
            String str = null;
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                str = currentUserProfile.getPictureLocalPath();
            }
            if (ZMBitmapFactory.decodeFile(str) != null) {
                AvatarPreviewFragment.showMyAvatar(this);
            }
        }
    }

    private void onClickOptionPhoneNumber() {
        SettingContactsFragment.showAsActivity(this);
    }

    private void onClickBtnSignout() {
        String str;
        String str2;
        String str3;
        String str4;
        int sipIdCountInCache = CmmSIPCallManager.getInstance().getSipIdCountInCache();
        if (sipIdCountInCache == 1) {
            str4 = getString(C4558R.string.zm_sip_incall_logout_dialog_title_85332);
            str3 = getString(C4558R.string.zm_sip_incall_logout_dialog_msg_85332);
            str2 = getString(C4558R.string.zm_btn_cancel);
            str = getString(C4558R.string.zm_btn_end_call);
        } else if (sipIdCountInCache > 1) {
            str4 = getString(C4558R.string.zm_sip_incall_multi_logout_dialog_title_85332);
            str3 = getString(C4558R.string.zm_sip_incall_multi_logout_dialog_msg_85332);
            str2 = getString(C4558R.string.zm_btn_cancel);
            str = getString(C4558R.string.zm_btn_end_call);
        } else {
            str4 = getString(C4558R.string.zm_alert_logout);
            str2 = getString(C4558R.string.zm_btn_no);
            str = getString(C4558R.string.zm_btn_yes);
            str3 = null;
        }
        new Builder(getActivity()).setCancelable(false).setTitle((CharSequence) str4).setMessage(str3).setNegativeButton(str2, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).setPositiveButton(str, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                SettingFragment.this.signOut();
            }
        }).create().show();
    }

    /* access modifiers changed from: private */
    public void signOut() {
        LogoutHandler.getInstance().startLogout();
        showWaitingDialog(false);
    }

    private void showWaitingDialog(boolean z) {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting);
            newInstance.setCancelable(z);
            newInstance.show(fragmentManager, "WaitingDialog");
        }
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            ZMDialogFragment zMDialogFragment = (ZMDialogFragment) fragmentManager.findFragmentByTag("WaitingDialog");
            if (zMDialogFragment != null) {
                zMDialogFragment.dismissAllowingStateLoss();
            }
        }
    }

    public void onShow() {
        updateMessengerPanel();
    }

    /* access modifiers changed from: private */
    public void updatePanelIntergreatedPhoneUI() {
        if (CmmSIPCallManager.getInstance().isPBXInactive() || !CmmSIPCallManager.getInstance().isSipCallEnabled()) {
            this.mPanelIntergreatedPhone.setVisibility(8);
        } else if (!PTApp.getInstance().isWebSignedOn()) {
            this.mPanelIntergreatedPhone.setVisibility(8);
        } else {
            this.mPanelIntergreatedPhone.setVisibility(0);
        }
    }
}
