package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.dialog.conf.ZMPasswordRulePopview;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.Builder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.SimpleMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ScheduledMeetingItem;
import com.zipow.videobox.view.ZMBaseMeetingOptionLayout.MeetingOptionListener;
import com.zipow.videobox.view.ZMBaseMeetingOptionLayout.PasswordKeyListener;
import com.zipow.videobox.view.ZMPMIMeetingOptionLayout;
import com.zipow.videobox.view.ZMPMIMeetingOptionLayout.PMIEditMeetingListener;
import java.util.TimeZone;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMCheckedTextView;
import p021us.zoom.videomeetings.C4558R;

public class ZMPMIEditFragment extends ZMFragment implements OnClickListener, MeetingOptionListener, PMIEditMeetingListener {
    private static final int REQUEST_MODIFY_ID = 100;
    private LinearLayout edtPasswordLinear;
    private boolean isInitView = false;
    private Button mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnSave;
    private ZMCheckedTextView mChkMeetingPassword;
    /* access modifiers changed from: private */
    public EditText mEdtPassword;
    @Nullable
    private ScheduledMeetingItem mMeetingItem;
    private SimpleMeetingMgrListener mMeetingMgrListener;
    private View mOptionMeetingPassword;
    /* access modifiers changed from: private */
    public ZMPasswordRulePopview mPasswordRulePopviewDialog;
    private ScrollView mScrollView;
    private TextView mTxtConfNumber;
    private ZMPMIMeetingOptionLayout mZMPMIMeetingOptionLayout;

    @NonNull
    public Fragment getFragmentContext() {
        return this;
    }

    public boolean isEditMeeting() {
        return true;
    }

    public static void showInActivity(@Nullable ZMActivity zMActivity) {
        if (zMActivity != null) {
            ZMPMIEditFragment newInstance = newInstance();
            newInstance.setArguments(new Bundle());
            zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, newInstance, ZMPMIEditFragment.class.getName()).commit();
        }
    }

    @NonNull
    public static ZMPMIEditFragment newInstance() {
        return new ZMPMIEditFragment();
    }

    @Nullable
    public static ZMPMIEditFragment findFragment(@Nullable ZMActivity zMActivity) {
        if (zMActivity == null) {
            return null;
        }
        return (ZMPMIEditFragment) zMActivity.getSupportFragmentManager().findFragmentByTag(ZMPMIEditFragment.class.getName());
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
    }

    public void onPause() {
        super.onPause();
        dismissPopupWindow();
        PTUI.getInstance().removeMeetingMgrListener(this.mMeetingMgrListener);
    }

    public void onResume() {
        super.onResume();
        if (this.mMeetingMgrListener == null) {
            this.mMeetingMgrListener = new SimpleMeetingMgrListener() {
                public void onListMeetingResult(int i) {
                    ZMPMIEditFragment.this.onListMeetingResult(i);
                }

                public void onPMIEvent(int i, int i2, @NonNull MeetingInfoProto meetingInfoProto) {
                    ZMPMIEditFragment.this.onPMIEvent(i, i2, meetingInfoProto);
                }
            };
        }
        PTUI.getInstance().addMeetingMgrListener(this.mMeetingMgrListener);
        updateUI();
    }

    private void updateUI() {
        if (this.mMeetingItem == null) {
            this.mMeetingItem = ZmPtUtils.getPMIMeetingItem();
            if (this.mMeetingItem != null) {
                this.isInitView = true;
                EditText editText = this.mEdtPassword;
                editText.setSelection(editText.getText().length());
            }
        } else {
            this.mMeetingItem = ZmPtUtils.getPMIMeetingItem();
        }
        ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
        if (scheduledMeetingItem != null) {
            long meetingNo = scheduledMeetingItem.getMeetingNo();
            int i = 0;
            if (String.valueOf(meetingNo).length() > 10) {
                i = ResourcesUtil.getInteger((Context) getActivity(), C4558R.integer.zm_config_long_meeting_id_format_type, 0);
            }
            this.mTxtConfNumber.setText(StringUtil.formatConfNumber(meetingNo, i));
        }
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            ZMPMIMeetingOptionLayout zMPMIMeetingOptionLayout = this.mZMPMIMeetingOptionLayout;
            if (zMPMIMeetingOptionLayout != null) {
                zMPMIMeetingOptionLayout.checkShowVideoOptions(meetingHelper.alwaysUsePMI());
            }
        }
        ZMPMIMeetingOptionLayout zMPMIMeetingOptionLayout2 = this.mZMPMIMeetingOptionLayout;
        if (zMPMIMeetingOptionLayout2 != null) {
            zMPMIMeetingOptionLayout2.updateUIStatus();
        }
        this.mBtnSave.setEnabled(validateInput());
        setPasswordOnTouch();
    }

    private void initPassword() {
        if (ZmPtUtils.isNeedHidePassword(true)) {
            updateChkMeetingPassword(false, false);
            return;
        }
        ScheduledMeetingItem meetingItem = getMeetingItem();
        if (meetingItem == null) {
            meetingItem = ZmPtUtils.getPMIMeetingItem();
        }
        if (ZmPtUtils.isLockedPassword(true, this.mZMPMIMeetingOptionLayout.isEnableJBH())) {
            updateChkMeetingPassword(true, false);
        } else if ((meetingItem == null || StringUtil.isEmptyOrNull(meetingItem.getPassword())) && (!this.mZMPMIMeetingOptionLayout.isEnableJBH() || !ZmPtUtils.isRequiredWebPassword(true, true))) {
            updateChkMeetingPassword(false, true);
        } else {
            updateChkMeetingPassword(true, true);
        }
    }

    private void updateChkMeetingPassword(boolean z, boolean z2) {
        this.mChkMeetingPassword.setChecked(z);
        this.mChkMeetingPassword.setEnabled(z2);
        this.mOptionMeetingPassword.setEnabled(z2);
        updatePasswordView();
    }

    private void updatePasswordView() {
        this.edtPasswordLinear.setVisibility(this.mChkMeetingPassword.isChecked() ? 0 : 8);
        if (this.edtPasswordLinear.getVisibility() == 0 && StringUtil.isEmptyOrNull(getPassword())) {
            ScheduledMeetingItem meetingItem = getMeetingItem();
            if (meetingItem == null) {
                meetingItem = ZmPtUtils.getPMIMeetingItem();
            }
            String str = "";
            if (meetingItem != null) {
                str = meetingItem.getPassword();
            }
            EditText editText = this.mEdtPassword;
            if (StringUtil.isEmptyOrNull(str)) {
                str = "";
            }
            editText.setText(str);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1) {
            dismiss(false);
            return;
        }
        ZMPMIMeetingOptionLayout zMPMIMeetingOptionLayout = this.mZMPMIMeetingOptionLayout;
        if (zMPMIMeetingOptionLayout != null) {
            zMPMIMeetingOptionLayout.onActivityResult(i, i2, intent);
        }
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_pmi_new_edit, viewGroup, false);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSave = (Button) inflate.findViewById(C4558R.C4560id.btnSave);
        this.mTxtConfNumber = (TextView) inflate.findViewById(C4558R.C4560id.txtConfNumber);
        this.edtPasswordLinear = (LinearLayout) inflate.findViewById(C4558R.C4560id.edtPasswordLinear);
        this.mChkMeetingPassword = (ZMCheckedTextView) inflate.findViewById(C4558R.C4560id.chkMeetingPassword);
        this.mOptionMeetingPassword = inflate.findViewById(C4558R.C4560id.optionMeetingPassword);
        this.mEdtPassword = (EditText) inflate.findViewById(C4558R.C4560id.edtPassword);
        this.mScrollView = (ScrollView) inflate.findViewById(C4558R.C4560id.scrollView);
        this.mZMPMIMeetingOptionLayout = (ZMPMIMeetingOptionLayout) inflate.findViewById(C4558R.C4560id.zmPmiMeetingOptions);
        this.mZMPMIMeetingOptionLayout.setmMeetingOptionListener(this);
        this.mZMPMIMeetingOptionLayout.setmPMIEditMeetingListener(this);
        this.mEdtPassword.setKeyListener(new PasswordKeyListener());
        this.mBtnSave.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        this.mOptionMeetingPassword.setOnClickListener(this);
        this.mEdtPassword.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ZMPMIEditFragment.this.mBtnSave.setEnabled(ZMPMIEditFragment.this.validateInput());
                if (ZMPMIEditFragment.this.mPasswordRulePopviewDialog != null && ZMPMIEditFragment.this.mPasswordRulePopviewDialog.isPopupWindowShowing()) {
                    ZMPMIEditFragment.this.mPasswordRulePopviewDialog.onPasswordChange(editable.toString());
                }
            }
        });
        this.mZMPMIMeetingOptionLayout.restoreSaveInstance(bundle);
        updateUI();
        this.mZMPMIMeetingOptionLayout.initViewData(this.mMeetingItem);
        this.mZMPMIMeetingOptionLayout.showAdvancedOptions();
        this.mZMPMIMeetingOptionLayout.initRetainedFragment();
        if (this.isInitView) {
            initPassword();
            this.isInitView = false;
        }
        return inflate;
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void setPasswordOnTouch() {
        this.mEdtPassword.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ZMPMIEditFragment.this.showPasswordRuleView();
                return false;
            }
        });
        this.mEdtPassword.setOnHoverListener(new OnHoverListener() {
            public boolean onHover(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 9 && AccessibilityUtil.isSpokenFeedbackEnabled(ZMPMIEditFragment.this.getContext())) {
                    ZMPMIEditFragment.this.showPasswordRuleView();
                    ZMPMIEditFragment.this.mEdtPassword.setSelection(ZMPMIEditFragment.this.mEdtPassword.getText().toString().length());
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showPasswordRuleView() {
        if (this.mPasswordRulePopviewDialog == null) {
            Context context = getContext();
            if (context != null) {
                this.mPasswordRulePopviewDialog = new ZMPasswordRulePopview(context);
            }
        }
        ZMPasswordRulePopview zMPasswordRulePopview = this.mPasswordRulePopviewDialog;
        if (zMPasswordRulePopview != null && zMPasswordRulePopview.showAsDropDown(this.edtPasswordLinear)) {
            this.mPasswordRulePopviewDialog.onPasswordChange(this.mEdtPassword.getText().toString());
        }
    }

    private void dismissPopupWindow() {
        ZMPasswordRulePopview zMPasswordRulePopview = this.mPasswordRulePopviewDialog;
        if (zMPasswordRulePopview != null) {
            zMPasswordRulePopview.dismissPopupWindow();
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        ZMPMIMeetingOptionLayout zMPMIMeetingOptionLayout = this.mZMPMIMeetingOptionLayout;
        if (zMPMIMeetingOptionLayout != null) {
            zMPMIMeetingOptionLayout.onSaveInstanceState(bundle);
        }
    }

    public void onClick(View view) {
        if (view == this.mBtnBack) {
            onClickBtnBack();
        } else if (view == this.mBtnSave) {
            onClickBtnSave();
        } else if (view == this.mOptionMeetingPassword) {
            onClickMeetingPassword();
        }
    }

    private void onClickMeetingPassword() {
        ZMCheckedTextView zMCheckedTextView = this.mChkMeetingPassword;
        zMCheckedTextView.setChecked(!zMCheckedTextView.isChecked());
        updatePasswordView();
        if (this.mPasswordRulePopviewDialog != null && !this.mChkMeetingPassword.isChecked()) {
            this.mPasswordRulePopviewDialog.dismissPopupWindow();
        }
    }

    private boolean checkMeetingPassword(ZMActivity zMActivity, @NonNull ScrollView scrollView, boolean z) {
        if (this.mChkMeetingPassword.isChecked()) {
            String password = getPassword();
            if (StringUtil.isEmptyOrNull(password) || ZMScheduleUtil.validateMeetingPassword(password) != 0) {
                int[] iArr = {0, 0};
                this.mEdtPassword.getLocationOnScreen(iArr);
                int[] iArr2 = {0, 0};
                scrollView.getLocationInWindow(iArr2);
                scrollView.smoothScrollTo(0, (scrollView.getScrollY() + iArr[1]) - iArr2[1]);
                this.mEdtPassword.requestFocus();
                if (StringUtil.isEmptyOrNull(password)) {
                    DialogUtils.showAlertDialog(zMActivity, C4558R.string.zm_title_password_required_17552, C4558R.string.zm_msg_password_required_17552, C4558R.string.zm_btn_ok);
                } else {
                    DialogUtils.showAlertDialog(zMActivity, C4558R.string.zm_password_rule_not_meet_136699, C4558R.string.zm_btn_ok);
                }
                return false;
            }
        }
        return true;
    }

    private void onClickBtnSave() {
        if (checkMeetingPassword((ZMActivity) getActivity(), this.mScrollView, true)) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mBtnSave);
            if (this.mMeetingItem != null) {
                if (!NetworkUtil.hasDataNetwork(getActivity())) {
                    showNormalErrorOrTimeoutDialog();
                    return;
                }
                PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                if (currentUserProfile != null) {
                    Builder newBuilder = MeetingInfoProto.newBuilder();
                    newBuilder.setTopic(this.mMeetingItem.getTopic());
                    if (this.edtPasswordLinear.getVisibility() == 0) {
                        newBuilder.setPassword(getPassword());
                    }
                    newBuilder.setType(this.mMeetingItem.getMeetingType());
                    newBuilder.setStartTime(this.mMeetingItem.getStartTime() / 1000);
                    newBuilder.setDuration(this.mMeetingItem.getDuration());
                    newBuilder.setRepeatType(this.mMeetingItem.getRepeatType());
                    newBuilder.setRepeatEndTime(this.mMeetingItem.getRepeatEndTime() / 1000);
                    newBuilder.setId(this.mMeetingItem.getId());
                    newBuilder.setMeetingNumber(this.mMeetingItem.getMeetingNo());
                    newBuilder.setMeetingStatus(this.mMeetingItem.getMeetingStatus());
                    newBuilder.setInviteEmailContent(this.mMeetingItem.getInvitationEmailContent());
                    newBuilder.setExtendMeetingType(this.mMeetingItem.getExtendMeetingType());
                    ZMPMIMeetingOptionLayout zMPMIMeetingOptionLayout = this.mZMPMIMeetingOptionLayout;
                    if (zMPMIMeetingOptionLayout != null) {
                        zMPMIMeetingOptionLayout.fillMeetingOptions(newBuilder, currentUserProfile);
                    }
                    MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
                    if (meetingHelper != null) {
                        if (meetingHelper.editMeeting(newBuilder.build(), TimeZone.getDefault().getID())) {
                            showWaitingDialog();
                        } else {
                            showNormalErrorOrTimeoutDialog();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onListMeetingResult(int i) {
        updateUI();
    }

    /* access modifiers changed from: private */
    public void onPMIEvent(int i, int i2, @NonNull MeetingInfoProto meetingInfoProto) {
        dismissWaitingDialog();
        if (i2 == 0) {
            dismissEditSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
        } else if (i2 == 5003) {
            showNormalErrorOrTimeoutDialog();
        } else {
            showErrorCodeDialog(i2);
        }
    }

    private void showWaitingDialog() {
        WaitingDialog newInstance = WaitingDialog.newInstance(C4558R.string.zm_msg_waiting_edit_meeting);
        newInstance.setCancelable(true);
        newInstance.show(getFragmentManager(), WaitingDialog.class.getName());
    }

    private void dismissWaitingDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog != null) {
                waitingDialog.dismiss();
            }
        }
    }

    private void dismissEditSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            Intent intent = new Intent();
            intent.putExtra("meetingItem", scheduledMeetingItem);
            zMActivity.setResult(-1, intent);
            zMActivity.finish();
        }
    }

    private void onClickBtnBack() {
        dismiss(true);
    }

    private void dismiss(boolean z) {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
            if (!z) {
                activity.overridePendingTransition(0, 0);
            }
        }
    }

    private String getPassword() {
        return this.mEdtPassword.getText().toString();
    }

    private void showErrorCodeDialog(int i) {
        if (i == 3402) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                SimpleMessageDialog.newInstance(getString(C4558R.string.zm_password_rule_not_meet_136699)).showNow(fragmentManager, SimpleMessageDialog.class.getName());
                return;
            }
            return;
        }
        SimpleMessageDialog.newInstance(getString(C4558R.string.zm_msg_edit_meeting_failed_unknown_error, Integer.valueOf(i))).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    private void showNormalErrorOrTimeoutDialog() {
        SimpleMessageDialog.newInstance(C4558R.string.zm_msg_edit_meeting_failed_normal_or_timeout).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    private void updatePasswordHint() {
        if (ZmPtUtils.isRequiredPasswordForUpdateMeeting(this.mZMPMIMeetingOptionLayout.isEnableJBH(), true)) {
            this.mEdtPassword.setHint(C4558R.string.zm_lbl_password_schedule_required_47451);
        } else {
            this.mEdtPassword.setHint(C4558R.string.zm_lbl_password_schedule_127873);
        }
    }

    public void onOptionChanged() {
        this.mBtnSave.setEnabled(validateInput());
    }

    @Nullable
    public ScheduledMeetingItem getMeetingItem() {
        return this.mMeetingItem;
    }

    public void onJBHChange() {
        if (!ZmPtUtils.isPMIRequirePasswordOff(true)) {
            initPassword();
        }
    }

    /* access modifiers changed from: private */
    public boolean validateInput() {
        return this.mZMPMIMeetingOptionLayout.validate3rdPartyAudioInfo();
    }
}
