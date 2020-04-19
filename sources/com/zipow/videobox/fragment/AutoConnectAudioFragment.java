package com.zipow.videobox.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.SimpleActivity.ExtListener;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.videomeetings.C4558R;

public class AutoConnectAudioFragment extends ZMDialogFragment implements OnClickListener, ExtListener {
    private static final int REQUEST_SELECT_PHONE_NUMBER = 1019;
    private static final String SELECTED_TYPE = "select_type";
    private static final String TAG = "AutoConnectAudioFragment";
    public static final int TYPE_AUTO_SELECT = 3;
    public static final int TYPE_CALL_ME = 2;
    public static final int TYPE_INTERNET = 1;
    public static final int TYPE_OFF = 0;
    private View panelPhoneNumber;
    private int selectedType;
    private ImageView tickImgAutoSelect;
    private ImageView tickImgCallMe;
    private ImageView tickImgInternet;
    private ImageView tickImgOff;
    private TextView txtCallMe;
    private TextView txtMyPhoneNumber;

    public void onKeyboardClosed() {
    }

    public void onKeyboardOpen() {
    }

    public boolean onSearchRequested() {
        return false;
    }

    public boolean onTipLayerTouched() {
        return false;
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity, int i) {
        Bundle bundle = new Bundle();
        SimpleActivity.show(zMActivity, AutoConnectAudioFragment.class.getName(), bundle, i, false, 1);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            this.selectedType = bundle.getInt("select_type", 0);
        } else {
            this.selectedType = PTSettingHelper.getAutoConnectAudio();
        }
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i = 0;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_fragment_auto_connect_audio, viewGroup, false);
        View findViewById = inflate.findViewById(C4558R.C4560id.panel_internet);
        View findViewById2 = inflate.findViewById(C4558R.C4560id.panel_call_me);
        View findViewById3 = inflate.findViewById(C4558R.C4560id.panel_auto_select);
        TextView textView = (TextView) inflate.findViewById(C4558R.C4560id.txt_auto_select_description);
        TextView textView2 = (TextView) inflate.findViewById(C4558R.C4560id.txt_call_me_description);
        inflate.findViewById(C4558R.C4560id.btnBack).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.panel_off).setOnClickListener(this);
        inflate.findViewById(C4558R.C4560id.option_my_phone_number).setOnClickListener(this);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        findViewById3.setOnClickListener(this);
        findViewById.setVisibility(shouldShowInternetPanel() ? 0 : 8);
        findViewById2.setVisibility(shouldShowCallMePanel() ? 0 : 8);
        findViewById3.setVisibility(shouldShowAutoSelectPanel() ? 0 : 8);
        textView.setVisibility(shouldShowAutoSelectPanel() ? 0 : 8);
        if (!shouldShowCallMePanel()) {
            i = 8;
        }
        textView2.setVisibility(i);
        this.tickImgOff = (ImageView) inflate.findViewById(C4558R.C4560id.img_off);
        this.tickImgInternet = (ImageView) inflate.findViewById(C4558R.C4560id.img_internet);
        this.tickImgCallMe = (ImageView) inflate.findViewById(C4558R.C4560id.img_call_me);
        this.tickImgAutoSelect = (ImageView) inflate.findViewById(C4558R.C4560id.img_auto_select);
        this.panelPhoneNumber = inflate.findViewById(C4558R.C4560id.panel_auto_connect_my_phone_number);
        this.txtMyPhoneNumber = (TextView) inflate.findViewById(C4558R.C4560id.txt_my_phone_number);
        this.txtCallMe = (TextView) inflate.findViewById(C4558R.C4560id.txt_call_me);
        return inflate;
    }

    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("select_type", this.selectedType);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.panel_off) {
            onClickOff();
        } else if (id == C4558R.C4560id.panel_internet) {
            onClickInternet();
        } else if (id == C4558R.C4560id.panel_call_me) {
            onClickCallMe();
        } else if (id == C4558R.C4560id.panel_auto_select) {
            onClickAutoSelect();
        } else if (id == C4558R.C4560id.option_my_phone_number) {
            onClickMyPhoneNumber();
        }
    }

    private void onClickBtnBack() {
        if (needSetPhoneNumber()) {
            showNullNumberAlert();
        } else {
            dismiss();
        }
    }

    private void onClickOff() {
        this.selectedType = 0;
        updateUI();
    }

    private void onClickInternet() {
        this.selectedType = 1;
        updateUI();
    }

    private void onClickCallMe() {
        this.selectedType = 2;
        updateUI();
        if (isEmptyPhoneNumber()) {
            showSelectNumberFragment();
        }
    }

    private void onClickAutoSelect() {
        this.selectedType = 3;
        updateUI();
        if (isEmptyPhoneNumber()) {
            showSelectNumberFragment();
        }
    }

    private void onClickMyPhoneNumber() {
        showSelectNumberFragment();
    }

    public void dismiss() {
        PTSettingHelper.saveAutoConnectAudio(this.selectedType);
        PTSettingHelper settingHelper = PTApp.getInstance().getSettingHelper();
        if (settingHelper != null) {
            boolean z = true;
            if (this.selectedType != 1) {
                z = false;
            }
            settingHelper.setAlwaysUseVoIPWhenJoinMeeting(z);
        }
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    private void updateUI() {
        int i = 8;
        this.tickImgOff.setVisibility(shouldShowOffTick() ? 0 : 8);
        this.tickImgInternet.setVisibility(shouldShowInternetTick() ? 0 : 8);
        this.tickImgCallMe.setVisibility(shouldShowCallMeTick() ? 0 : 8);
        this.tickImgAutoSelect.setVisibility(shouldShowAutoSelectTick() ? 0 : 8);
        View view = this.panelPhoneNumber;
        if (shouldShowCallMeTick() || shouldShowAutoSelectTick()) {
            i = 0;
        }
        view.setVisibility(i);
        this.txtMyPhoneNumber.setText(PTSettingHelper.getAutoCallPhoneNumber(getContext(), getString(C4558R.string.zm_mm_lbl_not_set)));
        String autoCallPhoneNumber = PTSettingHelper.getAutoCallPhoneNumber(getContext(), null);
        if (this.selectedType != 2 || autoCallPhoneNumber == null) {
            this.txtCallMe.setText(C4558R.string.zm_lbl_auto_connect_audio_call_me_92027);
        } else {
            this.txtCallMe.setText(getString(C4558R.string.zm_lbl_auto_connect_audio_call_me_with_number_92027, autoCallPhoneNumber));
        }
    }

    private boolean isEmptyPhoneNumber() {
        return StringUtil.isEmptyOrNull(PTSettingHelper.getAutoCallPhoneNumber(getContext(), ""));
    }

    private boolean needSetPhoneNumber() {
        return shouldShowPhoneNumberPanel() && isEmptyPhoneNumber();
    }

    private void showNullNumberAlert() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            new Builder(activity).setTitle(C4558R.string.zm_lbl_auto_connect_audio_alert_title_92027).setMessage(C4558R.string.zm_lbl_auto_connect_audio_alert_message_92027).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AutoConnectAudioFragment.this.showSelectNumberFragment();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) null).create().show();
        }
    }

    /* access modifiers changed from: private */
    public void showSelectNumberFragment() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            SelectCallOutNumberFragment.showAsActivity(zMActivity, 1019);
        }
    }

    private boolean shouldShowCallMePanel() {
        return PTSettingHelper.canSetAutoCallMyPhone();
    }

    private boolean shouldShowInternetPanel() {
        return !ZMPolicyUIHelper.isComputerAudioDisabled();
    }

    private boolean shouldShowAutoSelectPanel() {
        return shouldShowInternetPanel() && shouldShowCallMePanel();
    }

    private boolean shouldShowPhoneNumberPanel() {
        return shouldShowCallMeTick() || shouldShowAutoSelectTick();
    }

    private boolean shouldShowOffTick() {
        int i = this.selectedType;
        if (i == 0) {
            return true;
        }
        if (i == 1 && !shouldShowInternetPanel()) {
            return true;
        }
        if (this.selectedType == 2 && !shouldShowCallMePanel()) {
            return true;
        }
        if (this.selectedType != 3 || shouldShowAutoSelectPanel()) {
            return false;
        }
        return true;
    }

    private boolean shouldShowInternetTick() {
        return this.selectedType == 1 && shouldShowInternetPanel();
    }

    private boolean shouldShowCallMeTick() {
        return this.selectedType == 2 && shouldShowCallMePanel();
    }

    private boolean shouldShowAutoSelectTick() {
        return this.selectedType == 3 && shouldShowAutoSelectPanel();
    }

    public boolean onBackPressed() {
        onClickBtnBack();
        return true;
    }
}
