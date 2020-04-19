package com.zipow.videobox.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnHoverListener;
import android.view.View.OnTouchListener;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.dialog.conf.ZMPasswordRulePopview;
import com.zipow.videobox.fragment.JbhTimeSelectFragment;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.Builder;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ZMBaseMeetingOptionLayout.PasswordKeyListener;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.videomeetings.C4558R;

public class ZMScheduleMeetingOptionLayout extends ZMBaseMeetingOptionLayout {
    boolean isUsePmi;
    private CheckedTextView mChkLanguageInterpretation;
    private CheckedTextView mChkMeetingPassword;
    private CheckedTextView mChkPublicCalendar;
    /* access modifiers changed from: private */
    public EditText mEdtPassword;
    /* access modifiers changed from: private */
    public boolean mIsAlreadyTipPmiChange;
    private boolean mIsEditMeeting;
    private boolean mIsRecurring;
    /* access modifiers changed from: private */
    public boolean mIsUsePmiChecked;
    private int mJbhTime;
    private LinearLayout mLinearPassword;
    private View mOptionJbhTime;
    private View mOptionLanguageInterpretation;
    private View mOptionMeetingPassword;
    private View mOptionPublicCalendar;
    private View mOptionScheduleFor;
    @Nullable
    private String mScheduleForId;
    @Nullable
    private String mScheduleForName;
    /* access modifiers changed from: private */
    public ScheduleMeetingOptionListener mScheduleMeetingOptionListener;
    private TextView mTxtJbhTime;
    private TextView mTxtScheduleFor;

    static class ScheduleForMenuItem extends ZMSimpleMenuItem {
        public static final int ACTION_MYSELF = 0;
        public static final int ACTION_OTHER = 1;
        private String mAltHostId;

        public ScheduleForMenuItem(int i, String str, String str2) {
            super(i, str);
            this.mAltHostId = str2;
        }

        public String getAltHostId() {
            return this.mAltHostId;
        }
    }

    public interface ScheduleMeetingOptionListener {
        boolean getChkUsePMI();

        void onScheduleForChanged(boolean z, String str);

        void onUiChangePmiSetting(boolean z);
    }

    public ZMScheduleMeetingOptionLayout(Context context) {
        this(context, null);
    }

    public ZMScheduleMeetingOptionLayout(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mJbhTime = 5;
        this.mIsRecurring = false;
        this.mScheduleForId = null;
        this.mScheduleForName = null;
        this.isUsePmi = false;
        this.mIsUsePmiChecked = false;
        this.mIsAlreadyTipPmiChange = false;
    }

    public void setScheduleMeetingOptionListener(ScheduleMeetingOptionListener scheduleMeetingOptionListener) {
        this.mScheduleMeetingOptionListener = scheduleMeetingOptionListener;
    }

    public int getLayout() {
        return C4558R.layout.zm_schedule_meeting_options;
    }

    public void hideAdvancedOptions() {
        super.hideAdvancedOptions();
        this.mOptionScheduleFor.setVisibility(8);
        this.mOptionPublicCalendar.setVisibility(8);
        this.mOptionLanguageInterpretation.setVisibility(8);
    }

    public void onClick(@NonNull View view) {
        super.onClick(view);
        int id = view.getId();
        if (id == C4558R.C4560id.optionScheduleFor) {
            onClickScheduleFor();
        } else if (id == C4558R.C4560id.optionPublicCalendar) {
            onClickOptionPublicCalendar();
        } else if (id == C4558R.C4560id.optionLanguageInterpretation) {
            onClickOptionLanguageInterpretation();
        } else if (id == C4558R.C4560id.optionMeetingPassword) {
            onClickOptionMeetingPassword();
        } else if (id == C4558R.C4560id.optionJbhTime) {
            Context context = getContext();
            if (context instanceof ZMActivity) {
                Bundle bundle = new Bundle();
                bundle.putInt(JbhTimeSelectFragment.SELECTED_JBH_TIME, this.mJbhTime);
                JbhTimeSelectFragment.show((ZMActivity) context, bundle, 2006);
            }
        } else if (id == C4558R.C4560id.optionEnableJBH) {
            updateOptionJbhTimeVisibility();
        }
        if (id == C4558R.C4560id.optionEnableJBH || id == C4558R.C4560id.optionHostVideo || id == C4558R.C4560id.optionAttendeeVideo || id == C4558R.C4560id.optionAutoRecording || id == C4558R.C4560id.optionMeetingPassword || id == C4558R.C4560id.optionEnableWaitingRoom || id == C4558R.C4560id.optionAudioWaterMark) {
            checkPmiSettingChangeByUi();
        }
    }

    private void onClickOptionLanguageInterpretation() {
        CheckedTextView checkedTextView = this.mChkLanguageInterpretation;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    public void showAdvancedOptions() {
        super.showAdvancedOptions();
        this.mOptionScheduleFor.setVisibility(PTApp.getInstance().getAltHostCount() <= 0 ? 8 : 0);
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            if (currentUserProfile.isEnableAddMeetingToPublicCalendarEvent()) {
                this.mOptionPublicCalendar.setVisibility(0);
            } else {
                this.mOptionPublicCalendar.setVisibility(8);
            }
            updateOptionJbhTimeVisibility();
            updateLanguageInterpretation();
        }
    }

    private void updateLanguageInterpretation() {
        this.mOptionLanguageInterpretation.setVisibility(8);
        if (isShowAdvancedOptions()) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null && currentUserProfile.isEnableLanguageInterpretation() && !this.isUsePmi) {
                this.mOptionLanguageInterpretation.setVisibility(0);
            }
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"ClickableViewAccessibility"})
    public void init() {
        super.init();
        this.mOptionScheduleFor = findViewById(C4558R.C4560id.optionScheduleFor);
        this.mTxtScheduleFor = (TextView) findViewById(C4558R.C4560id.txtScheduleFor);
        this.mOptionPublicCalendar = findViewById(C4558R.C4560id.optionPublicCalendar);
        this.mChkPublicCalendar = (CheckedTextView) findViewById(C4558R.C4560id.chkPublicCalendar);
        this.mChkLanguageInterpretation = (CheckedTextView) findViewById(C4558R.C4560id.chkLanguageInterpretation);
        this.mOptionLanguageInterpretation = findViewById(C4558R.C4560id.optionLanguageInterpretation);
        this.mOptionJbhTime = findViewById(C4558R.C4560id.optionJbhTime);
        this.mTxtJbhTime = (TextView) findViewById(C4558R.C4560id.txtJbhTime);
        this.mOptionScheduleFor.setOnClickListener(this);
        this.mOptionPublicCalendar.setOnClickListener(this);
        this.mOptionLanguageInterpretation.setOnClickListener(this);
        this.mOptionJbhTime.setOnClickListener(this);
        this.mChkMeetingPassword = (CheckedTextView) findViewById(C4558R.C4560id.chkMeetingPassword);
        this.mOptionMeetingPassword = findViewById(C4558R.C4560id.optionMeetingPassword);
        this.mOptionMeetingPassword.setOnClickListener(this);
        this.mLinearPassword = (LinearLayout) findViewById(C4558R.C4560id.linearPassword);
        this.mEdtPassword = (EditText) findViewById(C4558R.C4560id.edtPassword);
        this.mEdtPassword.setKeyListener(new PasswordKeyListener());
        this.mEdtPassword.addTextChangedListener(this.mInputWatcher);
        final GestureDetector gestureDetector = new GestureDetector(getContext(), new SimpleOnGestureListener() {
            public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
                if (ZMScheduleMeetingOptionLayout.this.mIsUsePmiChecked && !ZMScheduleMeetingOptionLayout.this.mIsAlreadyTipPmiChange && ZMScheduleMeetingOptionLayout.this.mScheduleMeetingOptionListener != null) {
                    ZMScheduleMeetingOptionLayout.this.mScheduleMeetingOptionListener.onUiChangePmiSetting(true);
                }
                return super.onSingleTapConfirmed(motionEvent);
            }
        });
        this.mEdtPassword.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                gestureDetector.onTouchEvent(motionEvent);
                ZMScheduleMeetingOptionLayout.this.showPasswordRuleView();
                return false;
            }
        });
        setEdtPasswordOnHover();
    }

    private void setEdtPasswordOnHover() {
        this.mEdtPassword.setOnHoverListener(new OnHoverListener() {
            public boolean onHover(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 9 && AccessibilityUtil.isSpokenFeedbackEnabled(ZMScheduleMeetingOptionLayout.this.getContext())) {
                    ZMScheduleMeetingOptionLayout.this.showPasswordRuleView();
                    ZMScheduleMeetingOptionLayout.this.mEdtPassword.setSelection(ZMScheduleMeetingOptionLayout.this.mEdtPassword.getText().toString().length());
                }
                return false;
            }
        });
    }

    /* access modifiers changed from: private */
    public void showPasswordRuleView() {
        if (this.mPasswordRulePopviewDialog == null) {
            this.mPasswordRulePopviewDialog = new ZMPasswordRulePopview(getContext());
        }
        if (this.mPasswordRulePopviewDialog.showAsDropDown(this.mLinearPassword)) {
            this.mPasswordRulePopviewDialog.onPasswordChange(this.mEdtPassword.getText().toString());
        }
    }

    public void initViewData(@Nullable ScheduledMeetingItem scheduledMeetingItem) {
        super.initViewData(scheduledMeetingItem);
        this.mOptionLanguageInterpretation.setVisibility(8);
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            if (scheduledMeetingItem != null) {
                if (currentUserProfile.isLockAddMeetingToPublicCalendarEvent()) {
                    this.mChkPublicCalendar.setChecked(currentUserProfile.isDefaultEnableListMeetingInPublicEventList());
                } else {
                    this.mChkPublicCalendar.setChecked(scheduledMeetingItem.ismIsEnableMeetingToPublic());
                }
                this.mScheduleForId = scheduledMeetingItem.getHostId();
                this.mScheduleForName = scheduledMeetingItem.getHostName();
                this.mJbhTime = scheduledMeetingItem.getJbhTime();
            } else {
                this.mChkPublicCalendar.setChecked(currentUserProfile.isDefaultEnableListMeetingInPublicEventList());
                this.mJbhTime = PreferenceUtil.readIntValue(PreferenceUtil.SCHEDULE_VAL_JBH_TIME, 5);
            }
            EditText editText = this.mEdtPassword;
            editText.setSelection(editText.getText().length(), this.mEdtPassword.getText().length());
        }
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("mScheduleForId", this.mScheduleForId);
        bundle.putString("mScheduleForName", this.mScheduleForName);
        bundle.putBoolean("mChkLanguageInterpretation", this.mChkLanguageInterpretation.isChecked());
        bundle.putInt("mJbhTime", this.mJbhTime);
    }

    public void restoreSaveInstance(@Nullable Bundle bundle) {
        super.restoreSaveInstance(bundle);
        if (bundle != null) {
            this.mScheduleForId = bundle.getString("mScheduleForId");
            this.mScheduleForName = bundle.getString("mScheduleForName");
            this.mChkLanguageInterpretation.setChecked(bundle.getBoolean("mChkLanguageInterpretation"));
            this.mJbhTime = bundle.getInt("mJbhTime", 5);
        }
    }

    public void fillMeetingOptions(@NonNull Builder builder, @NonNull PTUserProfile pTUserProfile) {
        LinearLayout linearLayout = this.mLinearPassword;
        builder.setPassword((linearLayout == null || linearLayout.getVisibility() != 0) ? "" : getPassword());
        super.fillMeetingOptions(builder, pTUserProfile);
        if (pTUserProfile.isEnableAddMeetingToPublicCalendarEvent()) {
            builder.setIsEnableMeetingToPublic(this.mChkPublicCalendar.isChecked());
        }
        if (this.mOptionLanguageInterpretation.getVisibility() == 0) {
            builder.setIsEnableLanguageInterpretation(this.mChkLanguageInterpretation.isChecked());
        }
        if (this.mOptionJbhTime.getVisibility() == 0) {
            builder.setJbhPriorTime(this.mJbhTime);
        }
    }

    public void saveMeetingOptionsAsDefault() {
        super.saveMeetingOptionsAsDefault();
        PreferenceUtil.saveIntValue(PreferenceUtil.SCHEDULE_VAL_JBH_TIME, this.mJbhTime);
    }

    public void initMeetingOptions(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        super.initMeetingOptions(pTUserProfile, scheduledMeetingItem);
        this.isUsePmi = scheduledMeetingItem.isUsePmiAsMeetingID() || ZMScheduleUtil.isUsePmi(pTUserProfile);
        this.mChkLanguageInterpretation.setChecked(scheduledMeetingItem.isEnableLanguageInterpretation());
    }

    /* access modifiers changed from: protected */
    public void onClickEnableJBH() {
        super.onClickEnableJBH();
        ScheduleMeetingOptionListener scheduleMeetingOptionListener = this.mScheduleMeetingOptionListener;
        if (scheduleMeetingOptionListener != null && scheduleMeetingOptionListener.getChkUsePMI()) {
            updatePasswordHint(this.mScheduleMeetingOptionListener.getChkUsePMI());
        }
    }

    /* access modifiers changed from: protected */
    public void checkLockOptions() {
        super.checkLockOptions();
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            boolean isLockAddMeetingToPublicCalendarEvent = currentUserProfile.isLockAddMeetingToPublicCalendarEvent();
            this.mOptionPublicCalendar.setEnabled(!isLockAddMeetingToPublicCalendarEvent);
            this.mChkPublicCalendar.setEnabled(!isLockAddMeetingToPublicCalendarEvent);
        }
    }

    public boolean isUiChangePmiSetting(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (isPmiPasswordEnabled(scheduledMeetingItem) == this.mChkMeetingPassword.isChecked() && this.mEdtPassword.getText().toString().equals(scheduledMeetingItem.getPassword())) {
            return super.isUiChangePmiSetting(scheduledMeetingItem);
        }
        return true;
    }

    private boolean isPmiPasswordEnabled(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (ZmPtUtils.isNeedHidePassword(true)) {
            return false;
        }
        return ZmPtUtils.isLockedPassword(true, isEnableJBH()) || !StringUtil.isEmptyOrNull(scheduledMeetingItem.getPassword()) || (isEnableJBH() && ZmPtUtils.isRequiredWebPassword(true, true));
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 2006 && intent != null && i2 == -1) {
            this.mJbhTime = intent.getIntExtra(JbhTimeSelectFragment.SELECTED_JBH_TIME, 5);
            updateJbhTime(this.mJbhTime);
        }
    }

    public boolean isShowOptionUsePMI() {
        return StringUtil.isEmptyOrNull(this.mScheduleForId) || StringUtil.isSameString(ZmPtUtils.getMyZoomId(), this.mScheduleForId);
    }

    public void updatePasswordText(ScheduledMeetingItem scheduledMeetingItem) {
        if (this.mChkMeetingPassword.isChecked()) {
            if (scheduledMeetingItem != null && !StringUtil.isEmptyOrNull(scheduledMeetingItem.getPassword())) {
                this.mEdtPassword.setText(scheduledMeetingItem.getPassword());
            } else if (this.mScheduleMeetingOptionListener.getChkUsePMI()) {
                if (getPmiMeetingItem() != null) {
                    this.mEdtPassword.setText(!StringUtil.isEmptyOrNull(getPmiMeetingItem().getPassword()) ? getPmiMeetingItem().getPassword() : "");
                }
            } else if (StringUtil.isEmptyOrNull(getPassword())) {
                PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                if (currentUserProfile != null) {
                    this.mEdtPassword.setText(currentUserProfile.getRandomPassword());
                }
            }
        }
    }

    public void initPassword(boolean z, ScheduledMeetingItem scheduledMeetingItem) {
        if (!this.mIsEditMeeting) {
            updatePasswordHint(z);
        } else if (ZmPtUtils.isNeedHidePassword(z)) {
            updateChkMeetingPassword(false, false, scheduledMeetingItem);
        } else if (ZmPtUtils.isLockedPassword(z, isEnableJBH())) {
            updateChkMeetingPassword(true, false, scheduledMeetingItem);
        } else if (scheduledMeetingItem.hasPassword()) {
            updateChkMeetingPassword(true, true, scheduledMeetingItem);
        } else {
            updateChkMeetingPassword(false, true, scheduledMeetingItem);
        }
    }

    public void onUsePMIChange(boolean z) {
        updatePasswordHint(z);
        this.isUsePmi = z;
        updateLanguageInterpretation();
        updateOptionJbhTimeVisibility();
    }

    public void updateOptionJbhTimeVisibility() {
        this.mOptionJbhTime.setVisibility(8);
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null && !this.mIsUsePmiChecked && isShowAdvancedOptions() && isEnableJBH() && currentUserProfile.isSupportJbhPriorTime() && !this.mIsRecurring) {
            this.mOptionJbhTime.setVisibility(0);
        }
    }

    public void updatePasswordWithPmiSetting(ScheduledMeetingItem scheduledMeetingItem) {
        if (ZmPtUtils.isNeedHidePassword(true)) {
            updateChkMeetingPassword(false, false, scheduledMeetingItem);
            return;
        }
        if (scheduledMeetingItem == null) {
            scheduledMeetingItem = ZmPtUtils.getPMIMeetingItem();
        }
        if (ZmPtUtils.isLockedPassword(true, isEnableJBH())) {
            updateChkMeetingPassword(true, false, scheduledMeetingItem);
        } else if ((scheduledMeetingItem == null || StringUtil.isEmptyOrNull(scheduledMeetingItem.getPassword())) && (!isEnableJBH() || !ZmPtUtils.isRequiredWebPassword(true, true))) {
            updateChkMeetingPassword(false, true, scheduledMeetingItem);
        } else {
            updateChkMeetingPassword(true, true, scheduledMeetingItem);
        }
    }

    private void updatePasswordHint(boolean z) {
        if (ZmPtUtils.isNeedHidePassword(z)) {
            updateChkMeetingPassword(false, false, null);
        } else if (ZmPtUtils.isRequiredPasswordForUpdateMeeting(isEnableJBH(), z)) {
            updateChkMeetingPassword(true, false, null);
        } else if (ZmPtUtils.isRequiredWebPassword(isEnableJBH(), z) || isPMIOffAndHavePassword(z)) {
            updateChkMeetingPassword(true, true, null);
        } else {
            updateChkMeetingPassword(false, true, null);
        }
    }

    public boolean checkMeetingPassword(ZMActivity zMActivity, @NonNull ScrollView scrollView, boolean z) {
        CheckedTextView checkedTextView = this.mChkMeetingPassword;
        if (checkedTextView != null && checkedTextView.isChecked()) {
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

    private void updateChkMeetingPassword(boolean z, boolean z2, ScheduledMeetingItem scheduledMeetingItem) {
        this.mChkMeetingPassword.setChecked(z);
        this.mChkMeetingPassword.setEnabled(z2);
        this.mOptionMeetingPassword.setEnabled(z2);
        updatePasswordView(scheduledMeetingItem);
    }

    private void updatePasswordView(ScheduledMeetingItem scheduledMeetingItem) {
        this.mLinearPassword.setVisibility(this.mChkMeetingPassword.isChecked() ? 0 : 8);
        if (this.mLinearPassword.getVisibility() == 0) {
            updatePasswordText(scheduledMeetingItem);
        }
    }

    private boolean isPMIOffAndHavePassword(boolean z) {
        boolean z2 = false;
        if (!ZmPtUtils.isPMIRequirePasswordOff(z)) {
            return false;
        }
        ScheduledMeetingItem pMIMeetingItem = ZmPtUtils.getPMIMeetingItem();
        if (pMIMeetingItem != null && !StringUtil.isEmptyOrNull(pMIMeetingItem.getPassword())) {
            z2 = true;
        }
        return z2;
    }

    @Nullable
    public String getmScheduleForId() {
        return this.mScheduleForId;
    }

    public void updateUIStatus(boolean z) {
        this.mIsEditMeeting = z;
        if (this.mScheduleForName == null || StringUtil.isSameString(ZmPtUtils.getMyZoomId(), this.mScheduleForId)) {
            this.mTxtScheduleFor.setText(C4558R.string.zm_lbl_schedule_for_myself);
        } else {
            this.mTxtScheduleFor.setText(this.mScheduleForName);
        }
        if (PTApp.getInstance().getAltHostCount() <= 0) {
            this.mOptionScheduleFor.setVisibility(8);
        } else {
            this.mOptionScheduleFor.setEnabled(!z);
        }
        updateJbhTime(this.mJbhTime);
        updateUIStatus();
    }

    public void checkPmiSettingChangeByUi() {
        if (this.mIsUsePmiChecked && !this.mIsAlreadyTipPmiChange && this.mScheduleMeetingOptionListener != null && getPmiMeetingItem() != null) {
            this.mScheduleMeetingOptionListener.onUiChangePmiSetting(isUiChangePmiSetting(getPmiMeetingItem()));
        }
    }

    private void updateJbhTime(int i) {
        this.mJbhTime = i;
        Context context = getContext();
        if (context != null) {
            if (i == 0) {
                this.mTxtJbhTime.setText(context.getString(C4558R.string.zm_lbl_anytime_115416));
            } else if (i == 5 || i == 10 || i == 15) {
                this.mTxtJbhTime.setText(context.getString(C4558R.string.zm_lbl_min_115416, new Object[]{Integer.valueOf(i)}));
            } else {
                this.mTxtJbhTime.setText(context.getString(C4558R.string.zm_lbl_min_115416, new Object[]{Integer.valueOf(5)}));
            }
        }
    }

    private String getPassword() {
        return this.mEdtPassword.getText().toString();
    }

    private void onClickOptionPublicCalendar() {
        CheckedTextView checkedTextView = this.mChkPublicCalendar;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickOptionMeetingPassword() {
        CheckedTextView checkedTextView = this.mChkMeetingPassword;
        checkedTextView.setChecked(!checkedTextView.isChecked());
        if (this.mPasswordRulePopviewDialog != null && !this.mChkMeetingPassword.isChecked()) {
            this.mPasswordRulePopviewDialog.dismissPopupWindow();
        }
        updatePasswordView(null);
    }

    private void onClickScheduleFor() {
        Context context = getContext();
        if (context != null) {
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(context, false);
            zMMenuAdapter.addItem(new ScheduleForMenuItem(0, context.getString(C4558R.string.zm_lbl_schedule_for_myself), null));
            PTApp instance = PTApp.getInstance();
            int altHostCount = instance.getAltHostCount();
            for (int i = 0; i < altHostCount; i++) {
                AlterHost altHostAt = instance.getAltHostAt(i);
                if (altHostAt != null) {
                    zMMenuAdapter.addItem(new ScheduleForMenuItem(1, StringUtil.formatPersonName(altHostAt.getFirstName(), altHostAt.getLastName(), PTApp.getInstance().getRegionCodeForNameFormating()), altHostAt.getHostID()));
                }
            }
            ZMAlertDialog create = new ZMAlertDialog.Builder(context).setTitle(C4558R.string.zm_lbl_schedule_for).setAdapter(zMMenuAdapter, new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ZMScheduleMeetingOptionLayout.this.onSelectScheduleForMenuItem((ScheduleForMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectScheduleForMenuItem(@NonNull ScheduleForMenuItem scheduleForMenuItem) {
        String str;
        boolean z = true;
        if (!ResourcesUtil.getBoolean(getContext(), C4558R.bool.zm_config_pmi_enabled, true) || scheduleForMenuItem.getAction() != 0) {
            this.mScheduleForId = scheduleForMenuItem.getAltHostId();
            this.mScheduleForName = scheduleForMenuItem.getLabel();
            this.mTxtScheduleFor.setText(this.mScheduleForName);
            str = this.mScheduleForName;
            z = false;
        } else {
            this.mScheduleForId = null;
            this.mScheduleForName = null;
            this.mTxtScheduleFor.setText(C4558R.string.zm_lbl_schedule_for_myself);
            str = PTApp.getInstance().getMyName();
        }
        ScheduleMeetingOptionListener scheduleMeetingOptionListener = this.mScheduleMeetingOptionListener;
        if (scheduleMeetingOptionListener != null) {
            scheduleMeetingOptionListener.onScheduleForChanged(z, str);
        }
    }

    public void setIsUsePmiChecked(boolean z) {
        this.mIsUsePmiChecked = z;
    }

    public void setIsRecurring(boolean z) {
        this.mIsRecurring = z;
        updateOptionJbhTimeVisibility();
    }

    public void setIsAlreadyTipPmiChange(boolean z) {
        this.mIsAlreadyTipPmiChange = z;
    }

    public void dismissPopupWindow() {
        if (this.mPasswordRulePopviewDialog != null) {
            this.mPasswordRulePopviewDialog.dismissPopupWindow();
        }
    }
}
