package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.dialog.LeaveAlertDialog;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType;
import com.zipow.videobox.util.TimeFormatUtil;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class WaitingJoinView extends LinearLayout implements OnClickListener {
    private Button mBtnLeave = null;
    private Button mBtnLogin = null;
    private String mCustomMeetingId;
    private View mPanelDate = null;
    private View mPanelForScheduler = null;
    private View mPanelTitle;
    private TextView mTxtDate = null;
    private TextView mTxtMeetingId = null;
    private TextView mTxtTime = null;
    private TextView mTxtTopic = null;
    private TextView mTxtWaitingMessage = null;

    public WaitingJoinView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public WaitingJoinView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        inflateLayout();
        this.mBtnLeave = (Button) findViewById(C4558R.C4560id.btnLeave);
        this.mTxtTopic = (TextView) findViewById(C4558R.C4560id.txtTopic);
        this.mTxtMeetingId = (TextView) findViewById(C4558R.C4560id.txtMeetingId);
        this.mTxtTopic = (TextView) findViewById(C4558R.C4560id.txtTopic);
        this.mTxtDate = (TextView) findViewById(C4558R.C4560id.txtDate);
        this.mTxtTime = (TextView) findViewById(C4558R.C4560id.txtTime);
        this.mBtnLogin = (Button) findViewById(C4558R.C4560id.btnLogin);
        this.mPanelForScheduler = findViewById(C4558R.C4560id.panelForScheduler);
        this.mPanelDate = findViewById(C4558R.C4560id.tableRowDate);
        this.mPanelTitle = findViewById(C4558R.C4560id.panelTitle);
        this.mTxtWaitingMessage = (TextView) findViewById(C4558R.C4560id.txtWaiting);
        this.mBtnLeave.setOnClickListener(this);
        this.mBtnLogin.setOnClickListener(this);
        updateData();
    }

    public void setTitlePadding(int i, int i2, int i3, int i4) {
        this.mPanelTitle.setPadding(i, i2, i3, i4);
    }

    /* access modifiers changed from: protected */
    public void inflateLayout() {
        View.inflate(getContext(), C4558R.layout.zm_waiting_join, this);
    }

    public void updateData() {
        if (!isInEditMode()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    this.mTxtTopic.setText(meetingItem.getTopic());
                    if (!StringUtil.isEmptyOrNull(this.mCustomMeetingId)) {
                        this.mTxtMeetingId.setText(this.mCustomMeetingId);
                    } else {
                        this.mTxtMeetingId.setText(StringUtil.formatConfNumber(meetingItem.getMeetingNumber()));
                    }
                    if (meetingItem.getType() == MeetingType.REPEAT) {
                        this.mPanelDate.setVisibility(8);
                        this.mTxtTime.setText(C4558R.string.zm_lbl_time_recurring);
                    } else {
                        this.mTxtDate.setText(TimeFormatUtil.formatDate(getContext(), meetingItem.getStartTime() * 1000, false));
                        this.mTxtTime.setText(TimeFormatUtil.formatTime(getContext(), meetingItem.getStartTime() * 1000));
                    }
                    if (isWebinearViewOnlyWaitingInPracticeStatus()) {
                        this.mTxtWaitingMessage.setText(C4558R.string.zm_msg_waiting_webinear_start);
                    } else if (meetingItem.getProgressingMeetingCount() > 0) {
                        this.mTxtWaitingMessage.setText(C4558R.string.zm_msg_waiting_for_has_in_meeting);
                    } else {
                        this.mTxtWaitingMessage.setText(C4558R.string.zm_msg_waiting_for_scheduler);
                    }
                    if (isWebinearViewOnlyWaitingInPracticeStatus() || VideoBoxApplication.getInstance().isSDKMode()) {
                        View view = this.mPanelForScheduler;
                        if (view != null) {
                            view.setVisibility(8);
                        }
                    }
                }
            }
        }
    }

    public void setCustomMeetingId(String str) {
        this.mCustomMeetingId = str;
    }

    private boolean isWebinearViewOnlyWaitingInPracticeStatus() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return false;
        }
        int meetingWaitStatus = meetingItem.getMeetingWaitStatus();
        if (!confContext.isWebinar() || !ConfMgr.getInstance().isViewOnlyMeeting() || meetingWaitStatus != 3) {
            return false;
        }
        return true;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnLeave) {
            onClickBtnLeave();
        } else if (id == C4558R.C4560id.btnLogin) {
            onClickBtnLogin();
        }
    }

    private void onClickBtnLogin() {
        ConfMgr.getInstance().notifyPTStartLogin("Login to start meeting");
        ((ZMActivity) getContext()).finish();
    }

    private void onClickBtnLeave() {
        new LeaveAlertDialog().show(((ZMActivity) getContext()).getSupportFragmentManager(), LeaveAlertDialog.class.getName());
    }
}
