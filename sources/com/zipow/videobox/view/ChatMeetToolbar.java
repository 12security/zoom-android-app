package com.zipow.videobox.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.adapter.ZMLatestMeetingAdapter;
import com.zipow.videobox.view.adapter.ZMLatestMeetingAdapter.InnerItemOnclickListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.androidlib.widget.ZMChildListView;
import p021us.zoom.androidlib.widget.ZMToolbarLayout;
import p021us.zoom.videomeetings.C4558R;

public class ChatMeetToolbar extends BaseMeetingToolbar {
    private IUpComingMeetingCallback mIUpComingMeetingCallback;
    private int mIconSize;
    private ZMToolbarLayout mToolbarLayout;
    private View mViewDivider;
    private ZMChildListView mZMChildListView;
    @Nullable
    private ZMLatestMeetingAdapter mZmLatestMeetingAdapter;

    public interface IUpComingMeetingCallback {
        void onRefresh(List<Long> list);
    }

    public ChatMeetToolbar(Context context) {
        this(context, null);
    }

    public ChatMeetToolbar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void initView(@NonNull Context context) {
        View.inflate(context, C4558R.layout.zm_chat_meet_toolbar, this);
        this.mIconSize = getResources().getDimensionPixelSize(C4558R.dimen.zm_toolbar_size);
        this.mViewDivider = findViewById(C4558R.C4560id.viewDivider);
        this.mZMChildListView = (ZMChildListView) findViewById(C4558R.C4560id.upComingListView);
        this.mToolbarLayout = (ZMToolbarLayout) findViewById(C4558R.C4560id.toolbarLayout);
        this.mBtnJoin = (ToolbarButton) findViewById(C4558R.C4560id.btnJoin);
        setToolbar(this.mBtnJoin, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_blue);
        this.mBtnStart = (ToolbarButton) findViewById(C4558R.C4560id.btnStart);
        setToolbar(this.mBtnStart, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_orange);
        this.mBtnShareScreen = (ToolbarButton) findViewById(C4558R.C4560id.btnShareScreen);
        setToolbar(this.mBtnShareScreen, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_blue);
        this.mBtnShareScreen.setVisibility(ZMIntentUtil.isSupportShareScreen(context) ? 0 : 8);
        this.mBtnCallRoom = (ToolbarButton) findViewById(C4558R.C4560id.btnCallRoom);
        setToolbar(this.mBtnCallRoom, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_blue);
        this.mBtnSchedule = (ToolbarButton) findViewById(C4558R.C4560id.btnSchedule);
        setToolbar(this.mBtnSchedule, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_blue);
        this.mBtnJoin.setOnClickListener(this);
        this.mBtnStart.setOnClickListener(this);
        this.mBtnSchedule.setOnClickListener(this);
        this.mBtnShareScreen.setOnClickListener(this);
        this.mBtnCallRoom.setOnClickListener(this);
        this.mZmLatestMeetingAdapter = new ZMLatestMeetingAdapter(getContext(), new InnerItemOnclickListener() {
            public void itemClick(@NonNull View view) {
                ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) view.getTag();
                if (scheduledMeetingItem != null && ((ZMActivity) ChatMeetToolbar.this.getContext()) != null) {
                    ChatMeetToolbar.this.checkStartMeeting(scheduledMeetingItem);
                }
            }
        });
        this.mZMChildListView.setAdapter(this.mZmLatestMeetingAdapter);
        refresh();
    }

    /* access modifiers changed from: private */
    public void checkStartMeeting(@NonNull final ScheduledMeetingItem scheduledMeetingItem) {
        MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(getContext(), new SimpleOnButtonClickListener() {
            public void onPositiveClick() {
                ChatMeetToolbar.this.startMeeting(scheduledMeetingItem);
            }
        });
    }

    /* access modifiers changed from: private */
    public void startMeeting(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            if (!scheduledMeetingItem.ismIsCanStartMeetingForMySelf()) {
                ConfActivity.checkExistingCallAndJoinMeeting(zMActivity, scheduledMeetingItem.getMeetingNo(), scheduledMeetingItem.getId(), scheduledMeetingItem.getPersonalLink(), scheduledMeetingItem.getPassword());
            } else if (ConfActivity.startMeeting(zMActivity, scheduledMeetingItem.getMeetingNo(), scheduledMeetingItem.getId())) {
                ZMConfEventTracking.logStartMeetingInShortCut(scheduledMeetingItem);
            }
        }
    }

    public void refresh() {
        int i = 8;
        if (!PTApp.getInstance().hasActiveCall() || !VideoBoxApplication.getInstance().isConfProcessRunning()) {
            this.mBtnStart.setVisibility(0);
            this.mBtnJoin.setImageResource(C4558R.C4559drawable.zm_ic_join_meeting);
            setToolbar(this.mBtnJoin, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_blue);
            this.mBtnJoin.setText(C4558R.string.zm_bo_btn_join_bo);
            this.mBtnShareScreen.setVisibility(ZMIntentUtil.isSupportShareScreen(getContext()) ? 0 : 8);
            this.mBtnCallRoom.setVisibility(PTApp.getInstance().isStartVideoCallWithRoomSystemEnabled() ? 0 : 8);
        } else {
            this.mBtnStart.setVisibility(8);
            this.mBtnJoin.setImageResource(C4558R.C4559drawable.zm_ic_back_meeting);
            setToolbar(this.mBtnJoin, this.mIconSize, C4558R.C4559drawable.zm_btn_toolbar_orange);
            this.mBtnJoin.setText(C4558R.string.zm_btn_mm_return_to_conf_21854);
            this.mBtnShareScreen.setVisibility(8);
            this.mBtnCallRoom.setVisibility(8);
        }
        List latestUpcomingMeetingItems = ZmPtUtils.getLatestUpcomingMeetingItems();
        boolean isCollectionEmpty = CollectionsUtil.isCollectionEmpty(latestUpcomingMeetingItems);
        this.mViewDivider.setVisibility(isCollectionEmpty ? 8 : 0);
        ZMChildListView zMChildListView = this.mZMChildListView;
        if (!isCollectionEmpty) {
            i = 0;
        }
        zMChildListView.setVisibility(i);
        this.mZmLatestMeetingAdapter.refresh(latestUpcomingMeetingItems);
        IUpComingMeetingCallback iUpComingMeetingCallback = this.mIUpComingMeetingCallback;
        if (iUpComingMeetingCallback != null) {
            iUpComingMeetingCallback.onRefresh(getRefreshIntervals(latestUpcomingMeetingItems));
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

    public void setmIUpComingMeetingCallback(IUpComingMeetingCallback iUpComingMeetingCallback) {
        this.mIUpComingMeetingCallback = iUpComingMeetingCallback;
    }

    @NonNull
    private List<Long> getRefreshIntervals(@Nullable List<ScheduledMeetingItem> list) {
        ArrayList arrayList = new ArrayList();
        if (list == null || list.size() == 0) {
            return arrayList;
        }
        ZMLatestMeetingAdapter zMLatestMeetingAdapter = this.mZmLatestMeetingAdapter;
        if (zMLatestMeetingAdapter != null && zMLatestMeetingAdapter.getCount() > 0) {
            long currentTimeMillis = System.currentTimeMillis();
            for (ScheduledMeetingItem realStartTime : list) {
                long realStartTime2 = realStartTime.getRealStartTime() - currentTimeMillis;
                if (realStartTime2 < 0) {
                    long j = realStartTime2 + ZMLatestMeetingAdapter.UPCOMING_MEETING_CHECK_INTERVAL;
                    if (j >= 0 && !arrayList.contains(Long.valueOf(j))) {
                        arrayList.add(Long.valueOf(j));
                    }
                } else if (!arrayList.contains(Long.valueOf(realStartTime2))) {
                    arrayList.add(Long.valueOf(realStartTime2));
                    arrayList.add(Long.valueOf(realStartTime2 + ZMLatestMeetingAdapter.UPCOMING_MEETING_CHECK_INTERVAL));
                }
            }
        }
        return arrayList;
    }

    public int getVisibilityBtnCount() {
        int childCount = this.mToolbarLayout.getChildCount();
        for (int i = 0; i < this.mToolbarLayout.getChildCount(); i++) {
            if (this.mToolbarLayout.getChildAt(i).getVisibility() != 0) {
                childCount--;
            }
        }
        return childCount;
    }
}
