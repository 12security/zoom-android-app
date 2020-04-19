package com.zipow.videobox.confapp.meeting.confhelper;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.dialog.LiveStreamDialog;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.view.NormalMessageTip;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class LiveStreamComponent {
    private static final String TAG = "LiveStreamComponent";
    private final ConfActivity mConfActivity;
    private final View mPanelLiveStream;
    private final TextView mTxtLiveStreamName;

    public LiveStreamComponent(ConfActivity confActivity, View view) {
        this.mConfActivity = confActivity;
        this.mPanelLiveStream = view.findViewById(C4558R.C4560id.panelLiveStream);
        this.mTxtLiveStreamName = (TextView) view.findViewById(C4558R.C4560id.txtLiveName);
        this.mPanelLiveStream.setVisibility(8);
        this.mPanelLiveStream.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                LiveStreamComponent.this.onClickLiveStream();
            }
        });
    }

    public void onHostCohostChanged(ConfActivity confActivity) {
        LiveStreamDialog.refresh(confActivity);
    }

    public boolean onConfStatusChanged2(int i, long j) {
        if (i == 47) {
            sinkLiveStreamStatusChange();
            return true;
        } else if (i != 49) {
            return false;
        } else {
            sinkLiveStreamStartTimeOut((int) j);
            return true;
        }
    }

    private void sinkLiveStreamStatusChange() {
        EventTaskManager eventTaskManager = this.mConfActivity.getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.pushLater(new EventAction("onLiveStreamStatusChange") {
                public void run(IUIElement iUIElement) {
                    LiveStreamComponent.this.onLiveStreamStatusChange();
                }
            });
        }
    }

    private void sinkLiveStreamStartTimeOut(final int i) {
        EventTaskManager eventTaskManager = this.mConfActivity.getEventTaskManager();
        if (eventTaskManager != null) {
            eventTaskManager.pushLater(new EventAction("onLiveStreamStartTimeOut") {
                public void run(IUIElement iUIElement) {
                    LiveStreamComponent.this.onLiveStreamStartTimeOut(i);
                }
            });
        }
    }

    public void onLiveStreamStatusChange() {
        if (this.mConfActivity.isInDriveMode()) {
            this.mPanelLiveStream.setVisibility(8);
        } else if (ConfLocalHelper.isDirectShareClient()) {
            this.mPanelLiveStream.setVisibility(8);
        } else {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                if (ConfMgr.getInstance().isViewOnlyMeeting()) {
                    this.mPanelLiveStream.setVisibility(8);
                    return;
                }
                if (confStatusObj.isLiveOn()) {
                    this.mPanelLiveStream.setVisibility(0);
                    String liveChannelStreamName = ConfLocalHelper.getLiveChannelStreamName();
                    this.mTxtLiveStreamName.setText(this.mConfActivity.getString(C4558R.string.zm_lbl_live_stream_info, new Object[]{liveChannelStreamName}));
                } else if (confStatusObj.isLiveConnecting()) {
                    this.mPanelLiveStream.setVisibility(0);
                    this.mTxtLiveStreamName.setText(this.mConfActivity.getString(C4558R.string.zm_lbl_live_connecting));
                } else {
                    this.mPanelLiveStream.setVisibility(8);
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void onClickLiveStream() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isLiveOn()) {
            if (ConfLocalHelper.isHostCoHost() || !StringUtil.isEmptyOrNull(ConfLocalHelper.getLiveChannelStreamUrl())) {
                LiveStreamDialog.show(this.mConfActivity);
            }
        }
    }

    /* access modifiers changed from: private */
    public void onLiveStreamStartTimeOut(int i) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            String liveChannelsName = confStatusObj.getLiveChannelsName(i);
            if (StringUtil.isEmptyOrNull(liveChannelsName)) {
                liveChannelsName = "";
            }
            NormalMessageTip.show(this.mConfActivity.getSupportFragmentManager(), TipMessageType.TIP_LIVE_STREAM_START_FAIL.name(), (String) null, this.mConfActivity.getString(C4558R.string.zm_alert_live_streaming_failed, new Object[]{liveChannelsName}), 3000);
        }
    }
}
