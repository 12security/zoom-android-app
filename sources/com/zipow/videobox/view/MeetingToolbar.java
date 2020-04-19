package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.ptapp.PTApp;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.videomeetings.C4558R;

public class MeetingToolbar extends BaseMeetingToolbar {
    private int mIconSize;

    public MeetingToolbar(Context context) {
        this(context, null);
    }

    public MeetingToolbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void initView(Context context) {
        View.inflate(context, C4558R.layout.zm_meeting_toolbar, this);
        this.mIconSize = getResources().getDimensionPixelSize(C4558R.dimen.zm_toolbar_big_size);
        this.mBtnJoin = (ToolbarButton) findViewById(C4558R.C4560id.btnJoin);
        setToolbar(this.mBtnJoin, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_blue);
        this.mBtnStart = (ToolbarButton) findViewById(C4558R.C4560id.btnStart);
        setToolbar(this.mBtnStart, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_orange);
        this.mBtnShareScreen = (ToolbarButton) findViewById(C4558R.C4560id.btnShareScreen);
        setToolbar(this.mBtnShareScreen, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_blue);
        this.mBtnSchedule = (ToolbarButton) findViewById(C4558R.C4560id.btnSchedule);
        setToolbar(this.mBtnSchedule, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_blue);
        this.mBtnCallRoom = (ToolbarButton) findViewById(C4558R.C4560id.btnCallRoom);
        setToolbar(this.mBtnCallRoom, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_blue);
        this.mBtnJoin.setOnClickListener(this);
        this.mBtnStart.setOnClickListener(this);
        this.mBtnSchedule.setOnClickListener(this);
        this.mBtnShareScreen.setOnClickListener(this);
        this.mBtnCallRoom.setOnClickListener(this);
        refresh();
    }

    public void refresh() {
        int i = 8;
        if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
            this.mBtnStart.setVisibility(0);
            this.mBtnJoin.setImageResource(C4558R.C4559drawable.zm_ic_big_join_meeting);
            setToolbar(this.mBtnJoin, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_blue);
            this.mBtnJoin.setText(C4558R.string.zm_bo_btn_join_bo);
            this.mBtnShareScreen.setVisibility(ZMIntentUtil.isSupportShareScreen(getContext()) ? 0 : 8);
            ToolbarButton toolbarButton = this.mBtnCallRoom;
            if (PTApp.getInstance().isStartVideoCallWithRoomSystemEnabled()) {
                i = 0;
            }
            toolbarButton.setVisibility(i);
        } else {
            this.mBtnStart.setVisibility(8);
            this.mBtnJoin.setImageResource(C4558R.C4559drawable.zm_ic_big_back_meeting);
            setToolbar(this.mBtnJoin, this.mIconSize, C4558R.C4559drawable.zm_btn_big_toolbar_orange);
            this.mBtnJoin.setText(C4558R.string.zm_btn_mm_return_to_conf_21854);
            this.mBtnShareScreen.setVisibility(8);
            this.mBtnCallRoom.setVisibility(8);
        }
        if (!PTApp.getInstance().isWebSignedOn()) {
            this.mBtnStart.setEnabled(false);
            this.mBtnSchedule.setEnabled(false);
        } else {
            this.mBtnStart.setEnabled(true);
            this.mBtnSchedule.setEnabled(true);
        }
        this.mBtnShareScreen.setEnabled(true ^ PTApp.getInstance().isShareScreenNeedDisabled());
        super.refresh();
    }
}
