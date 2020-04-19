package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.CallRoomActivity;
import com.zipow.videobox.JoinConfActivity;
import com.zipow.videobox.ScheduleActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.fragment.CallRoomFragment;
import com.zipow.videobox.fragment.HostMeetingFragment_v2;
import com.zipow.videobox.fragment.JoinConfFragment;
import com.zipow.videobox.fragment.MMChatsListFragment;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.ShareScreenDialogHelper;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.UIMgr;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.widget.ZMToast;
import p021us.zoom.videomeetings.C4558R;

public abstract class BaseMeetingToolbar extends LinearLayout implements OnClickListener {
    protected ToolbarButton mBtnCallRoom;
    protected ToolbarButton mBtnJoin;
    protected ToolbarButton mBtnSchedule;
    protected ToolbarButton mBtnShareScreen;
    protected ToolbarButton mBtnStart;
    private MMChatsListFragment mParentFragment;

    /* access modifiers changed from: protected */
    public abstract void initView(Context context);

    public BaseMeetingToolbar(Context context) {
        this(context, null);
    }

    public BaseMeetingToolbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
        setFocusable(false);
    }

    public void refresh() {
        if (PTApp.getInstance().isShowPresentToRoomCancelStatus()) {
            PTApp.getInstance().setShowPresentToRoomCancelStatus(false);
            ZMToast.show(getContext(), getResources().getString(C4558R.string.zm_hint_share_screen_stopped_52777), 1, 17, 1500);
        }
    }

    public void setParentFragment(MMChatsListFragment mMChatsListFragment) {
        this.mParentFragment = mMChatsListFragment;
    }

    public void onClick(@NonNull View view) {
        long id = (long) view.getId();
        if (id == ((long) C4558R.C4560id.btnJoin)) {
            if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
                onClickActionItemJoinById();
            } else {
                onClickActionItemBackToMeeting();
            }
        } else if (id == ((long) C4558R.C4560id.btnStart)) {
            onClickActionItemHostMeeting();
        } else if (id == ((long) C4558R.C4560id.btnSchedule)) {
            onClickBtnScheduleMeeting();
        } else if (id == ((long) C4558R.C4560id.btnShareScreen)) {
            onClickBtnShareScreen();
        } else if (id == ((long) C4558R.C4560id.btnCallRoom)) {
            onClickBtnCallRoom();
        }
    }

    public void onClickBtnShareScreen() {
        ShareScreenDialogHelper.getInstance().showShareScreen((ZMActivity) getContext(), false);
    }

    /* access modifiers changed from: protected */
    public void setToolbar(@NonNull ToolbarButton toolbarButton, int i, int i2) {
        toolbarButton.setTextStyle(1);
        toolbarButton.setIconBackgroundResource(i2);
        toolbarButton.setIconScaleType(ScaleType.CENTER);
        toolbarButton.setIconSize(i, i);
    }

    private void onClickActionItemJoinById() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            if (UIMgr.isLargeMode(zMActivity)) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    JoinConfFragment.showJoinByNumber(supportFragmentManager, null, null);
                }
            } else {
                JoinConfActivity.showJoinByNumber(zMActivity, null, null);
            }
        }
    }

    private void onClickActionItemBackToMeeting() {
        if (!PTApp.getInstance().hasActiveCall()) {
            refresh();
        } else {
            ConfLocalHelper.returnToConf(getContext());
        }
    }

    private void onClickActionItemHostMeeting() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            HostMeetingFragment_v2.showAsActivity(zMActivity);
        }
    }

    private void onClickBtnCallRoom() {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            if (UIMgr.isLargeMode(zMActivity)) {
                FragmentManager supportFragmentManager = zMActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    CallRoomFragment.showJoinByNumber(supportFragmentManager, null, null);
                }
            } else {
                CallRoomActivity.showJoinByNumber(zMActivity, null, null);
            }
        }
    }

    private void onClickBtnScheduleMeeting() {
        ScheduleActivity.show((Fragment) this.mParentFragment, 1002);
    }
}
