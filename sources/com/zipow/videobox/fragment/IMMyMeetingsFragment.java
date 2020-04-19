package com.zipow.videobox.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.ICalendarAuthListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr.IPTUIStatusListener;
import com.zipow.videobox.view.IMView.OnFragmentShowListener;
import com.zipow.videobox.view.ScheduledMeetingsView;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.videomeetings.C4558R;

public class IMMyMeetingsFragment extends ZMDialogFragment implements OnClickListener, IPTUIStatusListener, OnFragmentShowListener, ICalendarAuthListener {
    public static final String ARG_SHOW_BACK_BUTTON = "showBackButton";
    private View mBtnBack;
    private View mBtnRefresh;
    private ScheduledMeetingsView mScheduledMeetingsView;
    private TextView mTxtCalAuthExpiredMsg;
    @NonNull
    private ZMPTIMeetingMgr mZMPTIMeetingMgr = ZMPTIMeetingMgr.getInstance();

    public void onRefreshMyNotes() {
    }

    public void onWebLogin(long j) {
    }

    public static void showAsActivity(@NonNull ZMActivity zMActivity) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackButton", true);
        SimpleActivity.show(zMActivity, IMMyMeetingsFragment.class.getName(), bundle, 0, true);
    }

    public static void showAsActivity(Fragment fragment) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("showBackButton", true);
        SimpleActivity.show(fragment, IMMyMeetingsFragment.class.getName(), bundle, 0, true);
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mZMPTIMeetingMgr.clearPullingFlags();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        boolean z = false;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_my_meetings, viewGroup, false);
        this.mBtnBack = inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnRefresh = inflate.findViewById(C4558R.C4560id.btnRefresh);
        this.mTxtCalAuthExpiredMsg = (TextView) inflate.findViewById(C4558R.C4560id.txtCalAuthExpiredMsg);
        this.mScheduledMeetingsView = (ScheduledMeetingsView) inflate.findViewById(C4558R.C4560id.scheduledMeetingsView);
        this.mBtnRefresh.setOnClickListener(this);
        this.mTxtCalAuthExpiredMsg.setOnClickListener(this);
        this.mBtnBack.setOnClickListener(this);
        Bundle arguments = getArguments();
        if (arguments != null) {
            z = arguments.getBoolean("showBackButton", false);
        }
        if (!z) {
            this.mBtnBack.setVisibility(4);
        }
        return inflate;
    }

    public void onPause() {
        super.onPause();
        this.mZMPTIMeetingMgr.removeIPTUIStatusListener(this);
        this.mZMPTIMeetingMgr.removeMySelfFromPTUIListener();
        PTUI.getInstance().removeCalendarAuthListener(this);
    }

    public void onResume() {
        super.onResume();
        this.mZMPTIMeetingMgr.addMySelfToPTUIListener();
        this.mZMPTIMeetingMgr.addIPTUIStatusListener(this);
        PTUI.getInstance().addCalendarAuthListener(this);
        ScheduledMeetingsView scheduledMeetingsView = this.mScheduledMeetingsView;
        if (scheduledMeetingsView != null) {
            scheduledMeetingsView.onResume();
        }
        this.mZMPTIMeetingMgr.pullCalendarIntegrationConfig();
    }

    public void onStop() {
        super.onStop();
        ScheduledMeetingsView scheduledMeetingsView = this.mScheduledMeetingsView;
        if (scheduledMeetingsView != null) {
            scheduledMeetingsView.onStop();
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnRefresh) {
            onClickBtnRefresh();
        } else if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.txtCalAuthExpiredMsg) {
            onClickReconnectCalendar();
        }
    }

    private void onClickReconnectCalendar() {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            String calendarUrl = currentUserProfile.getCalendarUrl();
            if (!StringUtil.isEmptyOrNull(calendarUrl)) {
                Context context = getContext();
                if (context != null) {
                    UIUtil.openURL(context, calendarUrl);
                }
            }
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

    private void onClickBtnRefresh() {
        ScheduledMeetingsView scheduledMeetingsView = this.mScheduledMeetingsView;
        if (scheduledMeetingsView != null && !scheduledMeetingsView.isRefreshing()) {
            this.mScheduledMeetingsView.refresh();
        }
    }

    public void onCalendarConfigReady(long j) {
        ScheduledMeetingsView scheduledMeetingsView = this.mScheduledMeetingsView;
        if (scheduledMeetingsView != null) {
            scheduledMeetingsView.refreshCalenderIntegeration();
        }
    }

    public void onCallStatusChanged(long j) {
        if (isResumed()) {
            ScheduledMeetingsView scheduledMeetingsView = this.mScheduledMeetingsView;
            if (scheduledMeetingsView != null) {
                scheduledMeetingsView.onCallStatusChanged(j);
            }
        }
    }

    public void onShow() {
        onClickBtnRefresh();
    }

    public void onCalendarAuthResult(int i) {
        if (i == 0) {
            this.mTxtCalAuthExpiredMsg.setVisibility(8);
        } else {
            this.mTxtCalAuthExpiredMsg.setVisibility(0);
        }
    }
}
