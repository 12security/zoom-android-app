package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.dialog.LeaveAlertDialog;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class OnHoldView extends LinearLayout implements OnClickListener {
    private View mBtnLeave = null;
    private View mTitleBar = null;
    private TextView mTxtMeetingNumber = null;

    public OnHoldView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public OnHoldView(Context context) {
        super(context);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(getContext(), C4558R.layout.zm_onhold_view, this);
        this.mBtnLeave = findViewById(C4558R.C4560id.btnLeave);
        this.mTxtMeetingNumber = (TextView) findViewById(C4558R.C4560id.txtMeetingNumber);
        this.mTitleBar = findViewById(C4558R.C4560id.vTitleBar);
        this.mBtnLeave.setOnClickListener(this);
        updateData();
    }

    public void setTitlePadding(int i, int i2, int i3, int i4) {
        this.mTitleBar.setPadding(i, i2, i3, i4);
    }

    public void updateData() {
        if (!isInEditMode()) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && !confContext.supportPutUserinWaitingListUponEntryFeature()) {
                MeetingInfoProto meetingItem = confContext.getMeetingItem();
                if (meetingItem != null) {
                    this.mTxtMeetingNumber.setText(StringUtil.formatConfNumber(meetingItem.getMeetingNumber()));
                }
            }
        }
    }

    public void onClick(@NonNull View view) {
        if (view.getId() == C4558R.C4560id.btnLeave) {
            onClickBtnLeave();
        }
    }

    private void onClickBtnLeave() {
        new LeaveAlertDialog().show(((ZMActivity) getContext()).getSupportFragmentManager(), LeaveAlertDialog.class.getName());
    }
}
