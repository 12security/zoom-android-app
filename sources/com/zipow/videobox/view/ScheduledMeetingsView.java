package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr.IMeetingStatusListener;
import com.zipow.videobox.ptapp.ZMPTIMeetingMgr.SourceMeetingList;
import p021us.zoom.androidlib.widget.PullDownRefreshListView.PullDownRefreshListener;
import p021us.zoom.videomeetings.C4558R;

public class ScheduledMeetingsView extends LinearLayout implements IMeetingStatusListener, PullDownRefreshListener {
    private ScheduledMeetingsListView mMeetingsListView;
    private View mPanelNoItemMsg;
    private ZMPTIMeetingMgr mZMPTIMeetingMgr = ZMPTIMeetingMgr.getInstance();

    public ScheduledMeetingsView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ScheduledMeetingsView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_scheduled_meetings, this);
        this.mMeetingsListView = (ScheduledMeetingsListView) findViewById(C4558R.C4560id.meetingsListView);
        this.mPanelNoItemMsg = findViewById(C4558R.C4560id.panelNoItemMsg);
        this.mMeetingsListView.setPullDownRefreshListener(this);
        if (!isInEditMode()) {
            updateLoading();
            updateNoItemMsg();
        }
    }

    public void onResume() {
        this.mMeetingsListView.loadMeetingList(true, true);
        this.mZMPTIMeetingMgr.addMySelfToMeetingMgrListener();
        this.mZMPTIMeetingMgr.addIMeetingStatusListener(this);
        updateLoading();
        updateNoItemMsg();
    }

    private void updateLoading() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            meetingHelper.checkIfNeedToListUpcomingMeeting();
            showLoading(meetingHelper.isLoadingMeetingList());
        }
    }

    private void updateNoItemMsg() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            if (!this.mMeetingsListView.isEmpty()) {
                this.mPanelNoItemMsg.setVisibility(8);
            } else if (meetingHelper.isLoadingMeetingList()) {
                this.mPanelNoItemMsg.setVisibility(8);
            } else {
                this.mPanelNoItemMsg.setVisibility(0);
            }
        }
    }

    public void onStop() {
        this.mMeetingsListView.onStop();
        this.mZMPTIMeetingMgr.removeIMeetingStatusListener(this);
        this.mZMPTIMeetingMgr.removeMySelfFromMeetingMgrListener();
    }

    private void showLoading(boolean z) {
        this.mMeetingsListView.showRefreshing(z);
    }

    private void onRefreshingDone() {
        this.mMeetingsListView.notifyRefreshDone();
    }

    public void onPullDownRefresh() {
        refresh();
    }

    public boolean isRefreshing() {
        return this.mMeetingsListView.isRefreshing();
    }

    public void refresh() {
        this.mZMPTIMeetingMgr.pullCalendarIntegrationConfig();
        this.mZMPTIMeetingMgr.pullCloudMeetings();
        updateLoading();
        updateNoItemMsg();
    }

    public void onMeetingListLoadDone(SourceMeetingList sourceMeetingList) {
        onRefreshingDone();
        if (sourceMeetingList == SourceMeetingList.CLOUD) {
            this.mMeetingsListView.loadMeetingList(true, false);
        } else {
            this.mMeetingsListView.loadMeetingList(true, true);
        }
        updateNoItemMsg();
    }

    public void onCallStatusChanged(long j) {
        ScheduledMeetingsListView scheduledMeetingsListView = this.mMeetingsListView;
        if (scheduledMeetingsListView != null) {
            scheduledMeetingsListView.onCallStatusChanged(j);
        }
    }

    public void refreshCalenderIntegeration() {
        ScheduledMeetingsListView scheduledMeetingsListView = this.mMeetingsListView;
        if (scheduledMeetingsListView != null) {
            scheduledMeetingsListView.loadMeetingList(true, true);
        }
    }
}
