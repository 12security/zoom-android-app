package com.zipow.videobox.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.MeetingInfoActivity;
import com.zipow.videobox.ScheduleActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUI.SimpleMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.ScheduledMeetingItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class HostMeetingFragment_v2 extends ZMDialogFragment implements OnClickListener, IPTUIListener {
    public static final int REQUEST_SCHEDULE = 100;
    private final String TAG = HostMeetingFragment_v2.class.getSimpleName();
    private View mBtnBack;
    private View mBtnScheduleMeeting;
    private Button mBtnStartMeeting;
    private View mBtnUpcomingMeetings;
    private CheckedTextView mChkUsePMI;
    private CheckedTextView mChkVideoOn;
    @Nullable
    private IMeetingMgrListener mMeetingMgrListener = null;
    private View mOptionUsePMI;
    private View mOptionVideoOn;
    private TextView mTxtPMI;

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        SimpleActivity.show(zMActivity, HostMeetingFragment_v2.class.getName(), new Bundle(), 0, true);
    }

    public static void showAsActivity(Fragment fragment) {
        SimpleActivity.show(fragment, HostMeetingFragment_v2.class.getName(), new Bundle(), 0, true);
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_host_meeting_v2, viewGroup, false);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mChkVideoOn = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkVideoOn);
        this.mChkUsePMI = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkUsePMI);
        this.mTxtPMI = (TextView) inflate.findViewById(C4558R.C4560id.txtPMI);
        this.mBtnStartMeeting = (Button) inflate.findViewById(C4558R.C4560id.btnStartMeeting);
        this.mBtnUpcomingMeetings = inflate.findViewById(C4558R.C4560id.btnUpcomingMeetings);
        this.mBtnScheduleMeeting = inflate.findViewById(C4558R.C4560id.btnScheduleMeeting);
        this.mOptionUsePMI = inflate.findViewById(C4558R.C4560id.optionUsePMI);
        this.mOptionVideoOn = inflate.findViewById(C4558R.C4560id.optionVideoOn);
        if (bundle == null) {
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                this.mChkVideoOn.setChecked(meetingHelper.alwaysMobileVideoOn());
                setPmiChecked(meetingHelper.alwaysUsePMI());
            }
        } else {
            boolean z = bundle.getBoolean("videoOn", true);
            boolean z2 = bundle.getBoolean("usePMI", false);
            this.mChkVideoOn.setChecked(z);
            setPmiChecked(z2);
        }
        this.mBtnBack.setOnClickListener(this);
        this.mBtnStartMeeting.setOnClickListener(this);
        this.mBtnUpcomingMeetings.setOnClickListener(this);
        this.mBtnScheduleMeeting.setOnClickListener(this);
        this.mOptionVideoOn.setOnClickListener(this);
        this.mOptionUsePMI.setOnClickListener(this);
        return inflate;
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("videoOn", this.mChkVideoOn.isChecked());
        bundle.putBoolean("usePMI", this.mChkUsePMI.isChecked());
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
        PTUI.getInstance().removeMeetingMgrListener(this.mMeetingMgrListener);
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().addPTUIListener(this);
        if (this.mMeetingMgrListener == null) {
            this.mMeetingMgrListener = new SimpleMeetingMgrListener() {
                public void onListMeetingResult(int i) {
                    HostMeetingFragment_v2.this.updatePMI();
                }
            };
        }
        PTUI.getInstance().addMeetingMgrListener(this.mMeetingMgrListener);
        updatePMI();
        updateBtnStartMeeting();
        updateBtnSchedule();
    }

    private int getLoginType() {
        int pTLoginType = PTApp.getInstance().getPTLoginType();
        if (pTLoginType == 100 && PTApp.getInstance().getSavedZoomAccount() == null) {
            return 102;
        }
        return pTLoginType;
    }

    /* access modifiers changed from: private */
    public void updatePMI() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper == null) {
            this.mTxtPMI.setText("");
            return;
        }
        MeetingInfoProto pmiMeetingItem = meetingHelper.getPmiMeetingItem();
        if (pmiMeetingItem == null) {
            this.mTxtPMI.setText("");
            return;
        }
        long meetingNumber = pmiMeetingItem.getMeetingNumber();
        this.mTxtPMI.setText(StringUtil.formatConfNumber(meetingNumber, String.valueOf(meetingNumber).length() > 10 ? ResourcesUtil.getInteger((Context) getActivity(), C4558R.integer.zm_config_long_meeting_id_format_type, 0) : 0));
        this.mChkUsePMI.isChecked();
        if (ZmLoginHelper.isNormalTypeLogin(getLoginType())) {
            this.mOptionUsePMI.setVisibility(0);
        } else {
            this.mOptionUsePMI.setVisibility(8);
        }
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100 && i2 == -1 && intent != null) {
            onScheduleSuccess((ScheduledMeetingItem) intent.getSerializableExtra("meetingItem"));
        }
    }

    private void onScheduleSuccess(final ScheduledMeetingItem scheduledMeetingItem) {
        if (((ZMActivity) getActivity()) != null) {
            getNonNullEventTaskManagerOrThrowException().push(new EventAction("onScheduleSuccess") {
                public void run(IUIElement iUIElement) {
                    MeetingInfoActivity.show((ZMActivity) iUIElement, scheduledMeetingItem, true, 104);
                }
            });
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnStartMeeting) {
            onClickBtnStartMeeting();
        } else if (id == C4558R.C4560id.btnUpcomingMeetings) {
            onClickBtnUpcomingMeetings();
        } else if (id == C4558R.C4560id.btnScheduleMeeting) {
            onClickBtnScheduleMeeting();
        } else if (id == C4558R.C4560id.optionVideoOn) {
            onClickChkVideoOn();
        } else if (id == C4558R.C4560id.optionUsePMI) {
            onClickChkUsePMI();
        }
    }

    private void onClickBtnStartMeeting() {
        int callStatus = PTApp.getInstance().getCallStatus();
        if (callStatus == 0) {
            startMeeting();
        } else if (callStatus == 2) {
            backToMeeting();
        }
    }

    private void backToMeeting() {
        if (!PTApp.getInstance().hasActiveCall()) {
            updateBtnStartMeeting();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ConfLocalHelper.returnToConf(activity);
        }
    }

    private void startMeeting() {
        checkStartMeeting();
    }

    private void checkStartMeeting() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(activity, new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    HostMeetingFragment_v2.this.onStartMeeting();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onStartMeeting() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            boolean isChecked = this.mChkVideoOn.isChecked();
            boolean isChecked2 = this.mChkUsePMI.isChecked();
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                meetingHelper.setAlwaysMobileVideoOn(isChecked);
                meetingHelper.setAlwaysUsePMI(isChecked2);
            }
            int startConference = ConfActivity.startConference(zMActivity, isChecked ? 3 : 4);
            if (startConference != 0) {
                StartHangoutFailedDialog.show(zMActivity.getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), startConference);
            } else {
                ZMConfEventTracking.logStartMeeting(isChecked, isChecked2);
            }
        }
    }

    private void onClickBtnUpcomingMeetings() {
        IMMyMeetingsFragment.showAsActivity((Fragment) this);
    }

    private void onClickBtnScheduleMeeting() {
        ScheduleActivity.show((Fragment) this, 100);
    }

    private void onClickChkVideoOn() {
        CheckedTextView checkedTextView = this.mChkVideoOn;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private void onClickChkUsePMI() {
        setPmiChecked(!this.mChkUsePMI.isChecked());
        updatePMI();
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

    private void onCallStatusChanged(long j) {
        if (isResumed()) {
            updateBtnStartMeetingForCallStatus((int) j);
        }
    }

    private void onWebLogin(long j) {
        if (isResumed()) {
            updateBtnStartMeeting();
            updateBtnSchedule();
        }
    }

    private void updateBtnStartMeetingForCallStatus(int i) {
        switch (i) {
            case 1:
                this.mBtnStartMeeting.setEnabled(false);
                this.mBtnStartMeeting.setText(C4558R.string.zm_btn_start_a_meeting);
                return;
            case 2:
                this.mBtnStartMeeting.setEnabled(true);
                this.mBtnStartMeeting.setText(C4558R.string.zm_btn_return_to_conf);
                return;
            default:
                this.mBtnStartMeeting.setEnabled(getCanStartMeeting());
                this.mBtnStartMeeting.setText(C4558R.string.zm_btn_start_a_meeting);
                return;
        }
    }

    private void updateBtnStartMeeting() {
        updateBtnStartMeetingForCallStatus(PTApp.getInstance().getCallStatus());
    }

    private void updateBtnSchedule() {
        this.mBtnScheduleMeeting.setEnabled(getCanScheduleMeeting());
    }

    private boolean getCanStartMeeting() {
        PTApp instance = PTApp.getInstance();
        return instance.hasPrescheduleMeeting() || instance.canAccessZoomWebservice();
    }

    private boolean getCanScheduleMeeting() {
        return PTApp.getInstance().canAccessZoomWebservice();
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 0) {
            onWebLogin(j);
        } else if (i == 22) {
            onCallStatusChanged(j);
        }
    }

    private boolean alwaysUsePMIEnabledOnWebByDefault() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return false;
        }
        return currentUserProfile.alwaysUsePMIEnabledOnWebByDefault();
    }

    private void setPmiChecked(boolean z) {
        if (alwaysUsePMIEnabledOnWebByDefault()) {
            this.mChkUsePMI.setChecked(true);
            this.mChkUsePMI.setEnabled(false);
            this.mOptionUsePMI.setEnabled(false);
            return;
        }
        this.mChkUsePMI.setChecked(z);
        this.mChkUsePMI.setEnabled(true);
        this.mOptionUsePMI.setEnabled(true);
    }
}
