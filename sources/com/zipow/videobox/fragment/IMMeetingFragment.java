package com.zipow.videobox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.JoinConfActivity;
import com.zipow.videobox.MeetingInfoActivity;
import com.zipow.videobox.ScheduleActivity;
import com.zipow.videobox.SettingActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.view.IMView.StartHangoutFailedDialog;
import com.zipow.videobox.view.ScheduledMeetingItem;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMMeetingFragment extends ZMFragment implements OnClickListener, IPTUIListener {
    private final String TAG = IMMeetingFragment.class.getSimpleName();
    private Button mBtnJoinConf;
    private Button mBtnMyMeetings;
    private Button mBtnReturnToConf;
    private Button mBtnSchedule;
    private View mBtnSetting;
    private Button mBtnStartConf;

    private void showMyInfo() {
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i = C4558R.layout.zm_imview_meeting;
        FragmentActivity activity = getActivity();
        if (!UIMgr.isLargeMode(activity) && UIUtil.getDisplayMinWidthInDip(getActivity()) < 500.0f && UIUtil.isLandscapeMode(activity)) {
            i = C4558R.layout.zm_imview_meeting_line;
        }
        View inflate = layoutInflater.inflate(i, viewGroup, false);
        this.mBtnStartConf = (Button) inflate.findViewById(C4558R.C4560id.btnStartConf);
        this.mBtnReturnToConf = (Button) inflate.findViewById(C4558R.C4560id.btnReturnToConf);
        this.mBtnJoinConf = (Button) inflate.findViewById(C4558R.C4560id.btnJoinConf);
        this.mBtnSchedule = (Button) inflate.findViewById(C4558R.C4560id.btnSchedule);
        this.mBtnMyMeetings = (Button) inflate.findViewById(C4558R.C4560id.btnMyMeetings);
        ViewGroup viewGroup2 = (ViewGroup) inflate.findViewById(C4558R.C4560id.toolbar);
        this.mBtnSetting = viewGroup2.findViewById(C4558R.C4560id.btnSetting);
        TextView textView = (TextView) viewGroup2.findViewById(C4558R.C4560id.txtTitle);
        if (UIMgr.isLargeMode(activity)) {
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
        }
        if (UIMgr.isLargeMode(getActivity())) {
            viewGroup2.setVisibility(8);
        }
        this.mBtnStartConf.setOnClickListener(this);
        this.mBtnReturnToConf.setOnClickListener(this);
        this.mBtnJoinConf.setOnClickListener(this);
        this.mBtnSchedule.setOnClickListener(this);
        this.mBtnMyMeetings.setOnClickListener(this);
        this.mBtnSetting.setOnClickListener(this);
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnJoinConf) {
            onClickBtnJoinById();
        } else if (id == C4558R.C4560id.btnStartConf) {
            onClickBtnStartConf();
        } else if (id == C4558R.C4560id.btnReturnToConf) {
            onClickBtnReturnToConf();
        } else if (id == C4558R.C4560id.btnSchedule) {
            onClickBtnSchedule();
        } else if (id == C4558R.C4560id.btnMyMeetings) {
            onClickBtnMyMeetings();
        } else if (id == C4558R.C4560id.btnSetting) {
            onClickBtnSetting();
        }
    }

    private void updateButtons() {
        if (getView() != null) {
            if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
                this.mBtnJoinConf.setEnabled(true);
                this.mBtnStartConf.setVisibility(0);
                this.mBtnStartConf.setEnabled(checkStartButtonEnabled());
                this.mBtnReturnToConf.setVisibility(8);
            } else {
                this.mBtnJoinConf.setEnabled(false);
                this.mBtnStartConf.setVisibility(8);
                this.mBtnReturnToConf.setVisibility(0);
            }
            this.mBtnSchedule.setEnabled(checkScheduleButtonEnabled());
        }
    }

    private boolean checkStartButtonEnabled() {
        PTApp instance = PTApp.getInstance();
        return instance.hasPrescheduleMeeting() || instance.canAccessZoomWebservice();
    }

    private boolean checkScheduleButtonEnabled() {
        return PTApp.getInstance().canAccessZoomWebservice();
    }

    public void onResume() {
        super.onResume();
        PTUI.getInstance().addPTUIListener(this);
        updateButtons();
        showMyInfo();
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onStart() {
        super.onStart();
        updateButtons();
        showMyInfo();
    }

    public void onWebLogin() {
        updateButtons();
    }

    public void onMyInfoReady() {
        showMyInfo();
    }

    public void onMyPictureReady() {
        showMyInfo();
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 22) {
            onCallStatusChanged(j);
        }
    }

    private void onCallStatusChanged(long j) {
        if (getView() != null) {
            switch ((int) j) {
                case 1:
                case 2:
                    this.mBtnJoinConf.setEnabled(false);
                    this.mBtnStartConf.setVisibility(8);
                    this.mBtnReturnToConf.setVisibility(0);
                    break;
                default:
                    this.mBtnJoinConf.setEnabled(true);
                    this.mBtnStartConf.setVisibility(0);
                    this.mBtnStartConf.setEnabled(true);
                    this.mBtnReturnToConf.setVisibility(8);
                    break;
            }
        }
    }

    public void onScheduleSuccess(final ScheduledMeetingItem scheduledMeetingItem) {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            zMActivity.getNonNullEventTaskManagerOrThrowException().push(new EventAction("onScheduleSuccess") {
                public void run(IUIElement iUIElement) {
                    MeetingInfoActivity.show((ZMActivity) iUIElement, scheduledMeetingItem, true, 104);
                }
            });
        }
    }

    private void onClickBtnStartConf() {
        if (getView() != null) {
            checkStartConf();
        }
    }

    private void checkStartConf() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(activity, new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    IMMeetingFragment.this.startConf();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startConf() {
        int startConference = ConfActivity.startConference(getActivity());
        if (startConference == 0) {
            this.mBtnStartConf.setEnabled(false);
            ZMConfEventTracking.logStartMeeting(true, false);
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            StartHangoutFailedDialog.show(activity.getSupportFragmentManager(), StartHangoutFailedDialog.class.getName(), startConference);
        }
    }

    private void onClickBtnReturnToConf() {
        if (!PTApp.getInstance().hasActiveCall()) {
            updateButtons();
            return;
        }
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ConfLocalHelper.returnToConf(activity);
        }
    }

    private void onClickBtnJoinById() {
        if (UIMgr.isLargeMode(getActivity())) {
            JoinConfFragment.showJoinByNumber(getFragmentManager(), null, null);
        } else {
            JoinConfActivity.showJoinByNumber(getActivity(), null, null);
        }
    }

    private void onClickBtnSchedule() {
        if (UIMgr.isLargeMode(getActivity())) {
            ScheduleFragment.showDialog(getFragmentManager());
            return;
        }
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            ScheduleActivity.show(zMActivity, 103);
        }
    }

    private void onClickBtnMyMeetings() {
        IMMyMeetingsFragment.showAsActivity((Fragment) this);
    }

    private void onClickBtnSetting() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            SettingActivity.show(zMActivity, 0);
        }
        PTApp.getInstance().checkForUpdates(false);
    }
}
