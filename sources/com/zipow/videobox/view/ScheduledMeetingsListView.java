package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import com.zipow.videobox.MeetingInfoActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.fragment.ZMPMIEditFragment;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ZmPtUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.PullDownRefreshListView;
import p021us.zoom.videomeetings.C4558R;

public class ScheduledMeetingsListView extends PullDownRefreshListView implements OnItemClickListener {
    private static final String TAG = "ScheduledMeetingsListView";
    private ScheduledMeetingsListAdapter mAdapter;
    private HashMap<String, Long> mLabels = new HashMap<>();

    public void onStop() {
    }

    public ScheduledMeetingsListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    public ScheduledMeetingsListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public ScheduledMeetingsListView(Context context) {
        super(context);
        initView();
    }

    public boolean isEmpty() {
        ScheduledMeetingsListAdapter scheduledMeetingsListAdapter = this.mAdapter;
        if (scheduledMeetingsListAdapter == null) {
            return true;
        }
        return scheduledMeetingsListAdapter.isEmpty();
    }

    private void initView() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            meetingHelper.setFilterPerson(PTApp.getInstance().getMeetingListLastDisplayedHostId());
        }
        this.mAdapter = new ScheduledMeetingsListAdapter(getContext());
        if (isInEditMode()) {
            _editmode_loadAllItems(this.mAdapter);
        } else {
            loadMeetingList(true, true);
        }
        setAdapter(this.mAdapter);
        setOnItemClickListener(this);
    }

    private void _editmode_loadAllItems(ScheduledMeetingsListAdapter scheduledMeetingsListAdapter) {
        int i = 0;
        while (i < 10) {
            ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
            StringBuilder sb = new StringBuilder();
            sb.append("My Meeting ");
            int i2 = i + 1;
            sb.append(i2);
            scheduledMeetingItem.setTopic(sb.toString());
            scheduledMeetingItem.setMeetingNo((long) (100000001 + i));
            scheduledMeetingItem.setMeetingType(i % 3 == 0 ? MeetingType.REPEAT : MeetingType.SCHEDULE);
            scheduledMeetingsListAdapter.addItem(scheduledMeetingItem);
            i = i2;
        }
    }

    public void loadMeetingList(boolean z, boolean z2) {
        this.mAdapter.clear();
        this.mLabels.clear();
        boolean z3 = false;
        if (z2 && ZmPtUtils.isCalendarServiceReady()) {
            LongSparseArray longSparseArray = new LongSparseArray();
            if (z) {
                loadZoomCloudMeetings(longSparseArray);
            }
            loadCalendarMeetings(longSparseArray);
        } else if (z) {
            loadZoomCloudMeetings();
        }
        Context context = getContext();
        if (context != null) {
            addLabels(context.getString(C4558R.string.zm_lbl_recurring_meeting));
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                z3 = currentUserProfile.isEnableDisplayEveryoneMeetingList();
            }
            if (PTApp.getInstance().getAltHostCount() > 0 && z3) {
                ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
                scheduledMeetingItem.setIsHostByLabel(true);
                this.mAdapter.addItem(scheduledMeetingItem);
            }
            this.mAdapter.sort();
            this.mAdapter.notifyDataSetChanged();
        }
    }

    private void loadZoomCloudMeetings() {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            Context context = getContext();
            if (context != null) {
                String string = context.getString(C4558R.string.zm_today_85318);
                String string2 = context.getString(C4558R.string.zm_lbl_tomorrow_75475);
                String string3 = context.getString(C4558R.string.zm_lbl_recurring_meeting);
                int filteredMeetingCount = meetingHelper.getFilteredMeetingCount();
                for (int i = 0; i < filteredMeetingCount; i++) {
                    MeetingInfoProto filteredMeetingItemByIndex = meetingHelper.getFilteredMeetingItemByIndex(i);
                    if (filteredMeetingItemByIndex != null) {
                        ScheduledMeetingItem fromMeetingInfo = ScheduledMeetingItem.fromMeetingInfo(filteredMeetingItemByIndex);
                        addItemAndGetLabel(true, fromMeetingInfo, string, string2, string3);
                        addRecMeetOccursTodayToToday(fromMeetingInfo, filteredMeetingItemByIndex, string);
                        if (i == 0 && !ZmPtUtils.isShouldHideAddCalendar()) {
                            addItemAndGetLabel(false, ScheduledMeetingItem.createAddCalendarItem(), null, null, null);
                        }
                    }
                }
            }
        }
    }

    private void loadZoomCloudMeetings(@NonNull LongSparseArray<ScheduledMeetingItem> longSparseArray) {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            Context context = getContext();
            if (context != null) {
                String string = context.getString(C4558R.string.zm_today_85318);
                String string2 = context.getString(C4558R.string.zm_lbl_tomorrow_75475);
                String string3 = context.getString(C4558R.string.zm_lbl_recurring_meeting);
                int filteredMeetingCount = meetingHelper.getFilteredMeetingCount();
                for (int i = 0; i < filteredMeetingCount; i++) {
                    MeetingInfoProto filteredMeetingItemByIndex = meetingHelper.getFilteredMeetingItemByIndex(i);
                    if (filteredMeetingItemByIndex == null) {
                        LongSparseArray<ScheduledMeetingItem> longSparseArray2 = longSparseArray;
                    } else {
                        ScheduledMeetingItem fromMeetingInfo = ScheduledMeetingItem.fromMeetingInfo(filteredMeetingItemByIndex);
                        addItemAndGetLabel(true, fromMeetingInfo, string, string2, string3);
                        addRecMeetOccursTodayToToday(fromMeetingInfo, filteredMeetingItemByIndex, string);
                        longSparseArray.put(fromMeetingInfo.getMeetingNo(), fromMeetingInfo);
                        if (i == 0 && !ZmPtUtils.isShouldHideAddCalendar()) {
                            addItemAndGetLabel(false, ScheduledMeetingItem.createAddCalendarItem(), null, null, null);
                        }
                    }
                }
            }
        }
    }

    private void addItemAndGetLabel(boolean z, @NonNull ScheduledMeetingItem scheduledMeetingItem, @Nullable String str, @Nullable String str2, @Nullable String str3) {
        this.mAdapter.addItem(scheduledMeetingItem);
        if (z) {
            long realStartTime = scheduledMeetingItem.getRealStartTime();
            if (!TimeUtil.isToday(realStartTime) || (!scheduledMeetingItem.ismIsRecCopy() && (scheduledMeetingItem.getExtendMeetingType() == 1 || scheduledMeetingItem.isRecurring()))) {
                if (!TimeUtil.isTomorrow(realStartTime) || scheduledMeetingItem.isRecurring()) {
                    if (!scheduledMeetingItem.isRecurring()) {
                        Context context = getContext();
                        if (context != null) {
                            String formatYearMonthDay = TimeUtil.formatYearMonthDay(context, realStartTime);
                            if (!this.mLabels.containsKey(formatYearMonthDay)) {
                                this.mLabels.put(formatYearMonthDay, Long.valueOf(TimeUtil.getDayZeroTime(realStartTime)));
                            }
                        }
                    } else if (scheduledMeetingItem.getExtendMeetingType() != 1 && !StringUtil.isEmptyOrNull(str3) && !this.mLabels.containsKey(str3)) {
                        this.mLabels.put(str3, Long.valueOf(TimeUtil.getDayZeroTime(realStartTime)));
                    }
                } else if (!StringUtil.isEmptyOrNull(str2) && !this.mLabels.containsKey(str2)) {
                    this.mLabels.put(str2, Long.valueOf(TimeUtil.getDayZeroTime(realStartTime)));
                }
            } else if (!StringUtil.isEmptyOrNull(str) && !this.mLabels.containsKey(str)) {
                this.mLabels.put(str, Long.valueOf(TimeUtil.getDayZeroTime(realStartTime)));
            }
        }
    }

    private void addLabels(@NonNull String str) {
        if (getContext() != null) {
            for (Entry entry : this.mLabels.entrySet()) {
                String str2 = (String) entry.getKey();
                long longValue = ((Long) entry.getValue()).longValue();
                ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
                scheduledMeetingItem.setmLabel(str2);
                if (str2.equalsIgnoreCase(str)) {
                    scheduledMeetingItem.setMeetingType(MeetingType.REPEAT);
                }
                scheduledMeetingItem.setStartTime(longValue);
                scheduledMeetingItem.setmIsLabel(true);
                addItemAndGetLabel(false, scheduledMeetingItem, null, null, null);
            }
        }
    }

    private void addRecMeetOccursTodayToToday(@NonNull ScheduledMeetingItem scheduledMeetingItem, @NonNull MeetingInfoProto meetingInfoProto, @NonNull String str) {
        long startTimeBaseCurrentTime = ZmPtUtils.getStartTimeBaseCurrentTime(System.currentTimeMillis(), scheduledMeetingItem);
        if (scheduledMeetingItem.isRecurring() && scheduledMeetingItem.getStartTime() > 0 && scheduledMeetingItem.getRepeatType() != 0 && TimeUtil.isToday(startTimeBaseCurrentTime)) {
            ScheduledMeetingItem fromMeetingInfo = ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto);
            fromMeetingInfo.setmIsRecCopy(true);
            fromMeetingInfo.setmRecCopyStartTime(startTimeBaseCurrentTime);
            addItemAndGetLabel(true, fromMeetingInfo, str, null, null);
        }
    }

    private void loadCalendarMeetings(@NonNull LongSparseArray<ScheduledMeetingItem> longSparseArray) {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null && !meetingHelper.needFilterOutCalendarEvents()) {
            List<ScheduledMeetingItem> meetingListFromCalEvents = ZmPtUtils.getMeetingListFromCalEvents(meetingHelper.getCalendarEvents(), longSparseArray);
            if (!CollectionsUtil.isListEmpty(meetingListFromCalEvents)) {
                Context context = getContext();
                if (context != null) {
                    String string = context.getString(C4558R.string.zm_today_85318);
                    String string2 = context.getString(C4558R.string.zm_lbl_tomorrow_75475);
                    String string3 = context.getString(C4558R.string.zm_lbl_recurring_meeting);
                    for (ScheduledMeetingItem addItemAndGetLabel : meetingListFromCalEvents) {
                        addItemAndGetLabel(true, addItemAndGetLabel, string, string2, string3);
                    }
                }
            }
        }
    }

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
        ZMActivity zMActivity = (ZMActivity) getContext();
        if (zMActivity != null) {
            Object itemAtPosition = getItemAtPosition(i);
            if (itemAtPosition instanceof ScheduledMeetingItem) {
                ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) itemAtPosition;
                if (scheduledMeetingItem.ismIsZoomMeeting()) {
                    if (scheduledMeetingItem.getExtendMeetingType() == 1) {
                        SimpleActivity.show(zMActivity, ZMPMIEditFragment.class.getName(), (Bundle) null, 0, true);
                    } else if (scheduledMeetingItem.getExtendMeetingType() == -999) {
                        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                        if (currentUserProfile != null && !StringUtil.isEmptyOrNull(currentUserProfile.getCalendarUrl())) {
                            UIUtil.openURL(getContext(), currentUserProfile.getCalendarUrl());
                        }
                    } else {
                        MeetingInfoActivity.show(zMActivity, scheduledMeetingItem, false, 104);
                    }
                }
            }
        }
    }

    public void onCallStatusChanged(long j) {
        loadMeetingList(true, true);
    }
}
