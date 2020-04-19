package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.IMActivity;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.ConfLocalHelper;
import com.zipow.videobox.util.DialogUtils;
import com.zipow.videobox.util.MeetingInvitationUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.TimeFormatUtil;
import com.zipow.videobox.util.ZMDomainUtil;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ScheduledMeetingItem;
import com.zipow.videobox.view.ZMBaseMeetingOptionLayout.MeetingOptionListener;
import com.zipow.videobox.view.ZMScheduleMeetingOptionLayout;
import com.zipow.videobox.view.ZMScheduleMeetingOptionLayout.ScheduleMeetingOptionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.data.CalendarResult;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil.EventRepeatType;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.EventTaskManager;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.androidlib.util.TimeZoneUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.WaitingDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.androidlib.widget.ZMDatePickerDialog;
import p021us.zoom.androidlib.widget.ZMDatePickerDialog.OnDateSetListener;
import p021us.zoom.androidlib.widget.ZMMenuAdapter;
import p021us.zoom.androidlib.widget.ZMSimpleMenuItem;
import p021us.zoom.androidlib.widget.ZMTimePickerDialog;
import p021us.zoom.androidlib.widget.ZMTimePickerDialog.OnTimeSetListener;
import p021us.zoom.videomeetings.C4558R;

public class ScheduleFragment extends ZMDialogFragment implements OnClickListener, IMeetingMgrListener, MeetingOptionListener, ScheduleMeetingOptionListener {
    private static final String ARG_IS_EDIT_MEETING = "isEditMeeting";
    private static final String ARG_MEETING_ITEM = "meetingItem";
    private static final int COLOR_DATE_TIME_INCORRECT = -65536;
    private static final int FREE_MEETING_TIME_OUT = 40;
    private Button mBtnBack;
    /* access modifiers changed from: private */
    public Button mBtnSchedule;
    private CheckedTextView mChkAddToCalendar;
    private CheckedTextView mChkUsePMI;
    private int mColorDateTimeNormal = 0;
    /* access modifiers changed from: private */
    @NonNull
    public Calendar mDateFrom = Calendar.getInstance();
    /* access modifiers changed from: private */
    @Nullable
    public ZMDatePickerDialog mDatePickerDialog;
    /* access modifiers changed from: private */
    public boolean mDateTimeChangedByMannual = false;
    /* access modifiers changed from: private */
    @NonNull
    public Calendar mDateTo = Calendar.getInstance();
    private EditText mEdtTopic;
    private boolean mIsEditMeeting = false;
    @Nullable
    private ScheduledMeetingItem mMeetingItem;
    private View mOptionAddToCalendar;
    private View mOptionEndRepeat;
    private View mOptionRepeat;
    private View mOptionTimeZone;
    private View mOptionUsePMI;
    @NonNull
    private EventRepeatType mRepeatType = EventRepeatType.NONE;
    @Nullable
    private MeetingInfoProto mScheduleMeetingInfo;
    private ScrollView mScrollView;
    private long mTimeEndRepeat = 0;
    /* access modifiers changed from: private */
    @Nullable
    public ZMTimePickerDialog mTimePickerDialog;
    @Nullable
    private String mTimeZoneId;
    /* access modifiers changed from: private */
    public TextView mTxtDate;
    private TextView mTxtEndRepeat;
    private TextView mTxtRepeatType;
    /* access modifiers changed from: private */
    public TextView mTxtTimeFrom;
    /* access modifiers changed from: private */
    public TextView mTxtTimeTo;
    private TextView mTxtTimeZoneName;
    private TextView mTxtTitle;
    private TextView mTxtTopicCannotEditTip;
    private TextView mTxtUsePMI;
    @Nullable
    private MeetingInfoProto mUpdateMeetingInfo;
    @Nullable
    private WaitingDialog mWaitingDialog;
    private ZMScheduleMeetingOptionLayout mZMScheduleMeetingOptionLayout;

    static class ReportOptionMenuItem extends ZMSimpleMenuItem {
        public ReportOptionMenuItem(EventRepeatType eventRepeatType, String str, boolean z) {
            super(eventRepeatType.ordinal(), str, null, z);
        }

        @Nullable
        public EventRepeatType getItemType() {
            int action = getAction();
            EventRepeatType[] values = EventRepeatType.values();
            if (action >= values.length || action < 0) {
                return null;
            }
            return values[action];
        }
    }

    @NonNull
    public Fragment getFragmentContext() {
        return this;
    }

    public void onDeleteMeetingResult(int i) {
    }

    public void onListCalendarEventsResult(int i) {
    }

    public void onListMeetingResult(int i) {
    }

    public void onPMIEvent(int i, int i2, MeetingInfoProto meetingInfoProto) {
    }

    public void onStartFailBeforeLaunch(int i) {
    }

    public ScheduleFragment() {
        setStyle(1, C4558R.style.ZMDialog_HideSoftKeyboard);
    }

    public static void showInActivity(ZMActivity zMActivity) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        scheduleFragment.setArguments(new Bundle());
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, scheduleFragment, ScheduleFragment.class.getName()).commit();
    }

    public static void showEditMeetingInActivity(ZMActivity zMActivity, ScheduledMeetingItem scheduledMeetingItem) {
        ScheduleFragment scheduleFragment = new ScheduleFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_IS_EDIT_MEETING, true);
        bundle.putSerializable(ARG_MEETING_ITEM, scheduledMeetingItem);
        scheduleFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, scheduleFragment, ScheduleFragment.class.getName()).commit();
    }

    public static void showDialog(@NonNull FragmentManager fragmentManager) {
        if (getScheduleFragment(fragmentManager) == null) {
            ScheduleFragment scheduleFragment = new ScheduleFragment();
            scheduleFragment.setArguments(new Bundle());
            scheduleFragment.show(fragmentManager, ScheduleFragment.class.getName());
        }
    }

    public static void showEditMeetingDialog(@NonNull FragmentManager fragmentManager, ScheduledMeetingItem scheduledMeetingItem) {
        if (getScheduleFragment(fragmentManager) == null) {
            ScheduleFragment scheduleFragment = new ScheduleFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(ARG_IS_EDIT_MEETING, true);
            bundle.putSerializable(ARG_MEETING_ITEM, scheduledMeetingItem);
            scheduleFragment.setArguments(bundle);
            scheduleFragment.show(fragmentManager, ScheduleFragment.class.getName());
        }
    }

    @Nullable
    public static ScheduleFragment getScheduleFragment(FragmentManager fragmentManager) {
        return (ScheduleFragment) fragmentManager.findFragmentByTag(ScheduleFragment.class.getName());
    }

    public void onActivityResult(int i, int i2, @Nullable Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 2000) {
            ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout = this.mZMScheduleMeetingOptionLayout;
            if (zMScheduleMeetingOptionLayout != null) {
                zMScheduleMeetingOptionLayout.onActivityResult(i, i2, intent);
            }
        } else if (intent != null && i2 == -1) {
            String stringExtra = intent.getStringExtra(TimeZonePickerFragment.TIME_ZONE_SELECTED_ID);
            if (!StringUtil.isEmptyOrNull(stringExtra)) {
                this.mTimeZoneId = stringExtra;
                setTimeZone(stringExtra);
                updateUIStatus();
            }
        }
    }

    public void onStart() {
        super.onStart();
    }

    @SuppressLint({"NewApi"})
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        boolean z;
        View inflate = layoutInflater.inflate(C4558R.layout.zm_schedule, null);
        this.mScrollView = (ScrollView) inflate.findViewById(C4558R.C4560id.scrollView);
        this.mTxtTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtTitle);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnSchedule = (Button) inflate.findViewById(C4558R.C4560id.btnSchedule);
        this.mEdtTopic = (EditText) inflate.findViewById(C4558R.C4560id.edtTopic);
        this.mTxtTopicCannotEditTip = (TextView) inflate.findViewById(C4558R.C4560id.txtTopicCannotEditTip);
        this.mChkAddToCalendar = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkAddToCalendar);
        this.mOptionAddToCalendar = inflate.findViewById(C4558R.C4560id.optionAddToCalendar);
        this.mOptionRepeat = inflate.findViewById(C4558R.C4560id.optionRepeat);
        this.mOptionEndRepeat = inflate.findViewById(C4558R.C4560id.optionEndRepeat);
        this.mTxtDate = (TextView) inflate.findViewById(C4558R.C4560id.txtDate);
        this.mTxtTimeFrom = (TextView) inflate.findViewById(C4558R.C4560id.txtTimeFrom);
        this.mTxtTimeTo = (TextView) inflate.findViewById(C4558R.C4560id.txtTimeTo);
        this.mTxtRepeatType = (TextView) inflate.findViewById(C4558R.C4560id.txtRepeatType);
        this.mTxtEndRepeat = (TextView) inflate.findViewById(C4558R.C4560id.txtEndRepeat);
        this.mOptionUsePMI = inflate.findViewById(C4558R.C4560id.optionUsePMI);
        this.mChkUsePMI = (CheckedTextView) inflate.findViewById(C4558R.C4560id.chkUsePMI);
        this.mTxtUsePMI = (TextView) inflate.findViewById(C4558R.C4560id.txtUsePMI);
        this.mOptionTimeZone = inflate.findViewById(C4558R.C4560id.optionTimeZone);
        this.mTxtTimeZoneName = (TextView) inflate.findViewById(C4558R.C4560id.txtTimeZone);
        this.mZMScheduleMeetingOptionLayout = (ZMScheduleMeetingOptionLayout) inflate.findViewById(C4558R.C4560id.zmMeetingOptions);
        this.mZMScheduleMeetingOptionLayout.setIsRecurring(getIsRecurringMeeting());
        this.mZMScheduleMeetingOptionLayout.setmMeetingOptionListener(this);
        this.mZMScheduleMeetingOptionLayout.setScheduleMeetingOptionListener(this);
        this.mZMScheduleMeetingOptionLayout.hideAdvancedOptions();
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            z = false;
        } else {
            z = currentUserProfile.isSupportFeatureEnablePaidUserForCN();
        }
        if (!PTApp.getInstance().isPaidUser() && z) {
            inflate.findViewById(C4558R.C4560id.txtTip).setVisibility(0);
        }
        View findViewById = inflate.findViewById(C4558R.C4560id.optionDate);
        View findViewById2 = inflate.findViewById(C4558R.C4560id.optionTimeFrom);
        View findViewById3 = inflate.findViewById(C4558R.C4560id.optionTimeTo);
        this.mColorDateTimeNormal = this.mTxtDate.getTextColors().getDefaultColor();
        this.mBtnBack.setOnClickListener(this);
        this.mBtnSchedule.setOnClickListener(this);
        findViewById.setOnClickListener(this);
        findViewById2.setOnClickListener(this);
        findViewById3.setOnClickListener(this);
        this.mOptionAddToCalendar.setOnClickListener(this);
        this.mOptionRepeat.setOnClickListener(this);
        this.mOptionEndRepeat.setOnClickListener(this);
        this.mOptionUsePMI.setOnClickListener(this);
        this.mOptionTimeZone.setOnClickListener(this);
        Date date = new Date(System.currentTimeMillis() + 3600000);
        this.mDateFrom = Calendar.getInstance();
        this.mDateFrom.setTime(date);
        this.mDateFrom.set(12, 0);
        this.mDateFrom.set(13, 0);
        this.mDateTo = Calendar.getInstance();
        this.mDateTo.setTime(date);
        this.mDateTo.set(12, 30);
        this.mDateTo.set(13, 0);
        this.mEdtTopic.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void afterTextChanged(Editable editable) {
                ScheduleFragment.this.mBtnSchedule.setEnabled(ScheduleFragment.this.validateInput());
            }
        });
        initViewData(bundle);
        this.mZMScheduleMeetingOptionLayout.initRetainedFragment();
        return inflate;
    }

    private void initViewData(@Nullable Bundle bundle) {
        this.mTimeZoneId = TimeZone.getDefault().getID();
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            boolean isEnableNotStoreMeetingTopic = currentUserProfile.isEnableNotStoreMeetingTopic();
            if (isEnableNotStoreMeetingTopic) {
                this.mEdtTopic.setEnabled(false);
                this.mEdtTopic.setText(C4558R.string.zm_lbl_meeting_default_topic_121401);
                this.mEdtTopic.setTextColor(getResources().getColor(C4558R.color.zm_color_BCBCBD));
                this.mTxtTopicCannotEditTip.setVisibility(0);
            } else {
                this.mEdtTopic.setHint(getTopicByName(PTApp.getInstance().getMyName()));
                this.mEdtTopic.setText(null);
                this.mTxtTopicCannotEditTip.setVisibility(8);
            }
            Bundle arguments = getArguments();
            if (arguments != null) {
                this.mIsEditMeeting = arguments.getBoolean(ARG_IS_EDIT_MEETING);
                this.mMeetingItem = (ScheduledMeetingItem) arguments.getSerializable(ARG_MEETING_ITEM);
                ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
                if (scheduledMeetingItem != null) {
                    if (!isEnableNotStoreMeetingTopic) {
                        this.mEdtTopic.setHint(scheduledMeetingItem.getTopic());
                        this.mEdtTopic.setText(this.mMeetingItem.getTopic());
                    }
                    setCheckedUsePmi(this.mMeetingItem.isUsePmiAsMeetingID());
                    if (this.mMeetingItem.isRecurring()) {
                        this.mRepeatType = ScheduledMeetingItem.zoomRepeatTypeToNativeRepeatType(this.mMeetingItem.getRepeatType());
                        this.mTimeEndRepeat = this.mMeetingItem.getRepeatEndTime();
                    } else {
                        this.mOptionEndRepeat.setVisibility(8);
                    }
                    this.mDateFrom.setTimeInMillis(this.mMeetingItem.getStartTime());
                    this.mDateTo.setTimeInMillis(this.mMeetingItem.getStartTime() + ((long) (this.mMeetingItem.getDuration() * 60000)));
                    this.mTimeZoneId = this.mMeetingItem.getTimeZoneId();
                } else {
                    boolean isUsePmi = ZMScheduleUtil.isUsePmi(currentUserProfile);
                    setCheckedUsePmi(isUsePmi);
                    if (isUsePmi && this.mZMScheduleMeetingOptionLayout.getPmiMeetingItem() != null) {
                        this.mTimeZoneId = this.mZMScheduleMeetingOptionLayout.getPmiMeetingItem().getTimeZoneId();
                    }
                }
                this.mChkAddToCalendar.setChecked(PreferenceUtil.readBooleanValue(PreferenceUtil.SCHEDULE_OPT_ADD_TO_CALENDAR, true));
                if (this.mIsEditMeeting) {
                    this.mTxtTitle.setText(C4558R.string.zm_title_edit_meeting);
                }
                EditText editText = this.mEdtTopic;
                editText.setSelection(editText.getText().length(), this.mEdtTopic.getText().length());
                if (bundle != null) {
                    this.mRepeatType = (EventRepeatType) bundle.getSerializable("mRepeatType");
                    this.mTimeEndRepeat = bundle.getLong("mTimeEndRepeat");
                    this.mDateTimeChangedByMannual = bundle.getBoolean("mDateTimeChangedByMannual");
                    Calendar calendar = (Calendar) bundle.getSerializable("mDateFrom");
                    if (calendar != null) {
                        this.mDateFrom = calendar;
                    }
                    Calendar calendar2 = (Calendar) bundle.getSerializable("mDateTo");
                    if (calendar2 != null) {
                        this.mDateTo = calendar2;
                    }
                    this.mTimeZoneId = bundle.getString("mTimeZoneId");
                    this.mChkAddToCalendar.setChecked(bundle.getBoolean("addToCalendar"));
                    setCheckedUsePmi(bundle.getBoolean("usePMI"));
                }
                TimeZone timeZoneById = TimeZoneUtil.getTimeZoneById(this.mTimeZoneId);
                this.mDateFrom.setTimeZone(timeZoneById);
                this.mDateTo.setTimeZone(timeZoneById);
                this.mTxtTimeZoneName.setText(TimeZoneUtil.getFullName(this.mTimeZoneId));
                if (this.mChkUsePMI.isChecked()) {
                    ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout = this.mZMScheduleMeetingOptionLayout;
                    zMScheduleMeetingOptionLayout.initViewData(zMScheduleMeetingOptionLayout.getPmiMeetingItem());
                } else {
                    this.mZMScheduleMeetingOptionLayout.initViewData(this.mMeetingItem);
                }
                this.mZMScheduleMeetingOptionLayout.restoreSaveInstance(bundle);
                updateUIStatus();
                this.mZMScheduleMeetingOptionLayout.initPassword(this.mChkUsePMI.isChecked(), this.mMeetingItem);
            }
        }
    }

    private Date getBeginTime() {
        Date time = this.mDateFrom.getTime();
        time.setSeconds(0);
        return time;
    }

    private int getDurationInMinutes() {
        checkEndTime();
        return (int) ((this.mDateTo.getTimeInMillis() - this.mDateFrom.getTimeInMillis()) / 60000);
    }

    private String getTopic() {
        if (!TextUtils.isEmpty(this.mEdtTopic.getText())) {
            return this.mEdtTopic.getText().toString();
        }
        return this.mEdtTopic.getHint() != null ? this.mEdtTopic.getHint().toString() : null;
    }

    private boolean getIsRecurringMeeting() {
        return this.mRepeatType != EventRepeatType.NONE;
    }

    public void onResume() {
        super.onResume();
        updateUIStatus();
        PTUI.getInstance().addMeetingMgrListener(this);
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeMeetingMgrListener(this);
        this.mZMScheduleMeetingOptionLayout.dismissPopupWindow();
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putSerializable("mRepeatType", this.mRepeatType);
        bundle.putLong("mTimeEndRepeat", this.mTimeEndRepeat);
        bundle.putBoolean("mDateTimeChangedByMannual", this.mDateTimeChangedByMannual);
        bundle.putSerializable("mDateFrom", this.mDateFrom);
        bundle.putSerializable("mDateTo", this.mDateTo);
        bundle.putBoolean("addToCalendar", this.mChkAddToCalendar.isChecked());
        bundle.putBoolean("usePMI", this.mChkUsePMI.isChecked());
        bundle.putString("mTimeZoneId", this.mTimeZoneId);
        ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout = this.mZMScheduleMeetingOptionLayout;
        if (zMScheduleMeetingOptionLayout != null) {
            zMScheduleMeetingOptionLayout.onSaveInstanceState(bundle);
        }
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.optionDate) {
            onClickBtnDate();
        } else if (id == C4558R.C4560id.optionTimeFrom) {
            onClickBtnTimeFrom();
        } else if (id == C4558R.C4560id.optionTimeTo) {
            onClickBtnTimeTo();
        } else if (id == C4558R.C4560id.btnSchedule) {
            onClickBtnSchedule();
        } else if (id == C4558R.C4560id.optionUsePMI) {
            onClickUsePMI();
        } else if (id == C4558R.C4560id.optionAddToCalendar) {
            onClickChkAddToCalendar();
        } else if (id == C4558R.C4560id.optionRepeat) {
            onClickRepeat();
        } else if (id == C4558R.C4560id.optionEndRepeat) {
            onClickEndRepeat();
        } else if (id == C4558R.C4560id.optionTimeZone) {
            onClickPickTimeZone();
        }
    }

    private void onClickEndRepeat() {
        Date date;
        long j = this.mTimeEndRepeat;
        if (j <= 0) {
            date = getBeginTime();
            switch (this.mRepeatType) {
                case DAILY:
                case WORKDAY:
                    date.setTime(date.getTime() + 864000000);
                    break;
                case WEEKLY:
                    date.setTime(date.getTime() + 604800000);
                    break;
                case BIWEEKLY:
                    date.setTime(date.getTime() + 1209600000);
                    break;
                case MONTHLY:
                    int month = date.getMonth();
                    if (month >= 11) {
                        date.setYear(date.getYear() + 1);
                        break;
                    } else {
                        date.setMonth(month + 1);
                        break;
                    }
                case YEARLY:
                    date.setYear(date.getYear() + 1);
                    break;
            }
        } else {
            date = new Date(j);
        }
        EndRepeatFragment.showDialog(getChildFragmentManager(), date);
    }

    public void onSelectEndRepeat(@NonNull Date date) {
        this.mTimeEndRepeat = date.getTime();
        updateUIStatus();
    }

    private void onClickRepeat() {
        ZMActivity zMActivity = (ZMActivity) getActivity();
        if (zMActivity != null) {
            boolean z = false;
            final ZMMenuAdapter zMMenuAdapter = new ZMMenuAdapter(zMActivity, false);
            zMMenuAdapter.addItem(new ReportOptionMenuItem(EventRepeatType.NONE, getString(C4558R.string.zm_lbl_repeat_never_in_list), this.mRepeatType == EventRepeatType.NONE));
            zMMenuAdapter.addItem(new ReportOptionMenuItem(EventRepeatType.DAILY, getString(C4558R.string.zm_lbl_repeat_daily_in_list), this.mRepeatType == EventRepeatType.DAILY));
            zMMenuAdapter.addItem(new ReportOptionMenuItem(EventRepeatType.WEEKLY, getString(C4558R.string.zm_lbl_repeat_weekly_in_list), this.mRepeatType == EventRepeatType.WEEKLY));
            zMMenuAdapter.addItem(new ReportOptionMenuItem(EventRepeatType.BIWEEKLY, getString(C4558R.string.zm_lbl_repeat_biweekly_in_list), this.mRepeatType == EventRepeatType.BIWEEKLY));
            zMMenuAdapter.addItem(new ReportOptionMenuItem(EventRepeatType.MONTHLY, getString(C4558R.string.zm_lbl_repeat_monthly_in_list), this.mRepeatType == EventRepeatType.MONTHLY));
            EventRepeatType eventRepeatType = EventRepeatType.YEARLY;
            String string = getString(C4558R.string.zm_lbl_repeat_yearly_in_list);
            if (this.mRepeatType == EventRepeatType.YEARLY) {
                z = true;
            }
            zMMenuAdapter.addItem(new ReportOptionMenuItem(eventRepeatType, string, z));
            zMMenuAdapter.setShowSelectedStatus(true);
            ZMAlertDialog create = new Builder(zMActivity).setTitle(C4558R.string.zm_lbl_repeat).setAdapter(zMMenuAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ScheduleFragment.this.onSelectRepeatOptionMenuItem((ReportOptionMenuItem) zMMenuAdapter.getItem(i));
                }
            }).create();
            create.setCanceledOnTouchOutside(true);
            create.show();
        }
    }

    /* access modifiers changed from: private */
    public void onSelectRepeatOptionMenuItem(@Nullable ReportOptionMenuItem reportOptionMenuItem) {
        if (reportOptionMenuItem != null) {
            EventRepeatType itemType = reportOptionMenuItem.getItemType();
            if (itemType != null) {
                onSelectRepeatType(itemType);
            }
        }
    }

    private void onClickBtnDate() {
        if (this.mDatePickerDialog == null && this.mTimePickerDialog == null) {
            ZMDatePickerDialog zMDatePickerDialog = new ZMDatePickerDialog(getActivity(), new OnDateSetListener() {
                public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                    ScheduleFragment.this.mDatePickerDialog = null;
                    ScheduleFragment.this.mDateFrom.set(1, i);
                    ScheduleFragment.this.mDateFrom.set(2, i2);
                    ScheduleFragment.this.mDateFrom.set(5, i3);
                    ScheduleFragment.this.mDateTo.set(1, i);
                    ScheduleFragment.this.mDateTo.set(2, i2);
                    ScheduleFragment.this.mDateTo.set(5, i3);
                    ScheduleFragment.this.mDateTimeChangedByMannual = true;
                    ScheduleFragment.this.mBtnSchedule.setEnabled(ScheduleFragment.this.validateInput());
                    ScheduleFragment.this.mTxtDate.setText(TimeUtil.formatDate((Context) ScheduleFragment.this.getActivity(), ScheduleFragment.this.mDateFrom));
                }
            }, this.mDateFrom.get(1), this.mDateFrom.get(2), this.mDateFrom.get(5));
            this.mDatePickerDialog = zMDatePickerDialog;
            this.mDatePickerDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    ScheduleFragment.this.mDatePickerDialog = null;
                }
            });
            this.mDatePickerDialog.show();
        }
    }

    /* access modifiers changed from: private */
    public void updateTime(Calendar calendar, TextView textView, int i, int i2) {
        calendar.set(11, i);
        calendar.set(12, i2);
        this.mDateTimeChangedByMannual = true;
        this.mBtnSchedule.setEnabled(validateInput());
        textView.setText(TimeUtil.formatTime((Context) getActivity(), calendar));
    }

    private void onClickBtnTimeFrom() {
        if (this.mDatePickerDialog == null && this.mTimePickerDialog == null) {
            ZMTimePickerDialog zMTimePickerDialog = new ZMTimePickerDialog(getActivity(), new OnTimeSetListener() {
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    ScheduleFragment.this.mTimePickerDialog = null;
                    ScheduleFragment scheduleFragment = ScheduleFragment.this;
                    scheduleFragment.validateDuration(true, scheduleFragment.mDateFrom, ScheduleFragment.this.mTxtTimeFrom, i, i2);
                }
            }, this.mDateFrom.get(11), this.mDateFrom.get(12), DateFormat.is24HourFormat(getActivity()));
            this.mTimePickerDialog = zMTimePickerDialog;
            this.mTimePickerDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    ScheduleFragment.this.mTimePickerDialog = null;
                }
            });
            this.mTimePickerDialog.show();
        }
    }

    private void setTimeZone(String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            TimeZone timeZone = TimeZone.getTimeZone(str);
            this.mDateFrom.setTimeZone(timeZone);
            this.mDateTo.setTimeZone(timeZone);
        }
    }

    private void checkEndTime() {
        int i = this.mDateFrom.get(1);
        int i2 = this.mDateFrom.get(2);
        int i3 = this.mDateFrom.get(5);
        this.mDateTo.set(1, i);
        this.mDateTo.set(2, i2);
        this.mDateTo.set(5, i3);
        if (!this.mDateTo.after(this.mDateFrom)) {
            this.mDateTo.add(5, 1);
        }
    }

    private void onClickBtnTimeTo() {
        if (this.mDatePickerDialog == null && this.mTimePickerDialog == null) {
            ZMTimePickerDialog zMTimePickerDialog = new ZMTimePickerDialog(getActivity(), new OnTimeSetListener() {
                public void onTimeSet(TimePicker timePicker, int i, int i2) {
                    ScheduleFragment.this.mTimePickerDialog = null;
                    ScheduleFragment scheduleFragment = ScheduleFragment.this;
                    scheduleFragment.validateDuration(false, scheduleFragment.mDateTo, ScheduleFragment.this.mTxtTimeTo, i, i2);
                }
            }, this.mDateTo.get(11), this.mDateTo.get(12), DateFormat.is24HourFormat(getActivity()));
            this.mTimePickerDialog = zMTimePickerDialog;
            this.mTimePickerDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    ScheduleFragment.this.mTimePickerDialog = null;
                }
            });
            this.mTimePickerDialog.show();
        }
    }

    private void onClickUsePMI() {
        setCheckedUsePmi(!this.mChkUsePMI.isChecked());
        if (!this.mChkUsePMI.isChecked()) {
            this.mZMScheduleMeetingOptionLayout.initViewData(this.mMeetingItem);
        } else if (this.mZMScheduleMeetingOptionLayout.getPmiMeetingItem() != null) {
            ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout = this.mZMScheduleMeetingOptionLayout;
            zMScheduleMeetingOptionLayout.initViewData(zMScheduleMeetingOptionLayout.getPmiMeetingItem());
            ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout2 = this.mZMScheduleMeetingOptionLayout;
            zMScheduleMeetingOptionLayout2.updatePasswordWithPmiSetting(zMScheduleMeetingOptionLayout2.getPmiMeetingItem());
        }
        this.mZMScheduleMeetingOptionLayout.updateUIStatus();
        this.mZMScheduleMeetingOptionLayout.onUsePMIChange(this.mChkUsePMI.isChecked());
    }

    private void setCheckedUsePmi(boolean z) {
        this.mChkUsePMI.setChecked(z);
        this.mZMScheduleMeetingOptionLayout.setIsUsePmiChecked(z);
    }

    private void onClickPickTimeZone() {
        TimeZonePickerFragment.show(this, null, 2000);
    }

    private void onClickChkAddToCalendar() {
        CheckedTextView checkedTextView = this.mChkAddToCalendar;
        checkedTextView.setChecked(!checkedTextView.isChecked());
    }

    private String getTopicByName(@Nullable String str) {
        if (str == null) {
            return "";
        }
        if (str.endsWith("s")) {
            return getString(C4558R.string.zm_lbl_xxx_s_meeting_no_s, str);
        }
        return getString(C4558R.string.zm_lbl_xxx_s_meeting_s, str);
    }

    private void updateUIStatus() {
        this.mTxtDate.setText(TimeUtil.formatDate((Context) getActivity(), this.mDateFrom));
        this.mTxtTimeFrom.setText(TimeUtil.formatTime((Context) getActivity(), this.mDateFrom));
        this.mTxtTimeTo.setText(TimeUtil.formatTime((Context) getActivity(), this.mDateTo));
        this.mTxtTimeZoneName.setText(TimeZoneUtil.getFullName(this.mTimeZoneId));
        this.mOptionEndRepeat.setVisibility(getIsRecurringMeeting() ? 0 : 8);
        if (this.mTimeEndRepeat > 0) {
            this.mTxtEndRepeat.setText(TimeFormatUtil.formatDate(getActivity(), this.mTimeEndRepeat, true));
        } else {
            this.mTxtEndRepeat.setText(C4558R.string.zm_lbl_end_repeat_never);
        }
        switch (this.mRepeatType) {
            case DAILY:
            case WORKDAY:
                this.mTxtRepeatType.setText(C4558R.string.zm_lbl_repeat_daily);
                break;
            case WEEKLY:
                this.mTxtRepeatType.setText(C4558R.string.zm_lbl_repeat_weekly);
                break;
            case BIWEEKLY:
                this.mTxtRepeatType.setText(C4558R.string.zm_lbl_repeat_biweekly);
                break;
            case MONTHLY:
                this.mTxtRepeatType.setText(C4558R.string.zm_lbl_repeat_monthly);
                break;
            case YEARLY:
                this.mTxtRepeatType.setText(C4558R.string.zm_lbl_repeat_yearly);
                break;
            case NONE:
                this.mTxtRepeatType.setText(C4558R.string.zm_lbl_repeat_never);
                break;
        }
        long pMINumber = ZmPtUtils.getPMINumber();
        this.mTxtUsePMI.setText(StringUtil.formatConfNumber(pMINumber, String.valueOf(pMINumber).length() > 10 ? ResourcesUtil.getInteger((Context) getActivity(), C4558R.integer.zm_config_long_meeting_id_format_type, 0) : 0));
        if (!isPMIEnabled() || !this.mZMScheduleMeetingOptionLayout.isShowOptionUsePMI()) {
            this.mOptionUsePMI.setVisibility(8);
        } else {
            this.mOptionUsePMI.setVisibility(0);
        }
        this.mZMScheduleMeetingOptionLayout.updateUIStatus(this.mIsEditMeeting);
        this.mZMScheduleMeetingOptionLayout.setIsRecurring(getIsRecurringMeeting());
        this.mBtnSchedule.setEnabled(validateInput());
    }

    private boolean isPMIEnabled() {
        return ResourcesUtil.getBoolean((Context) getActivity(), C4558R.bool.zm_config_pmi_enabled, true);
    }

    private void onClickBtnBack() {
        dismissCancel();
    }

    private void onClickBtnSchedule() {
        if (this.mZMScheduleMeetingOptionLayout.checkMeetingPassword((ZMActivity) getActivity(), this.mScrollView, this.mChkUsePMI.isChecked())) {
            UIUtil.closeSoftKeyboard(getActivity(), this.mBtnSchedule);
            if (validateInput()) {
                if (!NetworkUtil.hasDataNetwork(getActivity())) {
                    showNormalErrorOrTimeoutDialog();
                } else {
                    scheduleMeeting();
                }
            }
        }
    }

    private void scheduleMeeting() {
        boolean z;
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            MeetingInfoProto.Builder newBuilder = MeetingInfoProto.newBuilder();
            newBuilder.setTopic(getTopic());
            newBuilder.setType(getIsRecurringMeeting() ? MeetingType.REPEAT : MeetingType.SCHEDULE);
            newBuilder.setStartTime(getBeginTime().getTime() / 1000);
            newBuilder.setDuration(getDurationInMinutes());
            newBuilder.setTimeZoneId(getTimeZoneId());
            if (this.mZMScheduleMeetingOptionLayout.isShowOptionUsePMI()) {
                newBuilder.setUsePmiAsMeetingID(this.mChkUsePMI.isChecked());
            } else {
                newBuilder.setUsePmiAsMeetingID(false);
            }
            if (getIsRecurringMeeting()) {
                newBuilder.setRepeatType(ScheduledMeetingItem.nativeRepeatTypeToZoomRepeatType(this.mRepeatType));
                newBuilder.setRepeatEndTime(this.mTimeEndRepeat / 1000);
            }
            if (this.mIsEditMeeting) {
                ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
                if (scheduledMeetingItem != null) {
                    newBuilder.setId(scheduledMeetingItem.getId());
                    newBuilder.setMeetingNumber(this.mMeetingItem.getMeetingNo());
                    newBuilder.setMeetingStatus(this.mMeetingItem.getMeetingStatus());
                    newBuilder.setInviteEmailContent(this.mMeetingItem.getInvitationEmailContent());
                    newBuilder.setOriginalMeetingNumber(this.mMeetingItem.getOriginalMeetingNo());
                    newBuilder.setMeetingHostID(this.mMeetingItem.getHostId());
                }
            }
            this.mZMScheduleMeetingOptionLayout.fillMeetingOptions(newBuilder, currentUserProfile);
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                if (this.mIsEditMeeting) {
                    z = meetingHelper.editMeeting(newBuilder.build(), getTimeZoneId());
                } else {
                    z = meetingHelper.scheduleMeeting(newBuilder.build(), getTimeZoneId(), this.mZMScheduleMeetingOptionLayout.getmScheduleForId());
                }
                if (z) {
                    showWaitingDialog(this.mIsEditMeeting ? C4558R.string.zm_msg_waiting_edit_meeting : C4558R.string.zm_msg_scheduling);
                } else {
                    showNormalErrorOrTimeoutDialog();
                }
                saveOptionsAsDefault();
            }
        }
    }

    private void saveOptionsAsDefault() {
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_ADD_TO_CALENDAR, this.mChkAddToCalendar.isChecked());
        PreferenceUtil.saveBooleanValue(PreferenceUtil.SCHEDULE_OPT_USE_PMI, this.mChkUsePMI.isChecked());
        ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout = this.mZMScheduleMeetingOptionLayout;
        if (zMScheduleMeetingOptionLayout != null) {
            zMScheduleMeetingOptionLayout.saveMeetingOptionsAsDefault();
        }
    }

    @Nullable
    private String getTimeZoneId() {
        return this.mTimeZoneId;
    }

    public void onScheduleMeetingResult(int i, @Nullable MeetingInfoProto meetingInfoProto, @NonNull String str) {
        dismissWaitingDialog();
        this.mScheduleMeetingInfo = meetingInfoProto;
        if (i == 0) {
            ZMScheduleMeetingOptionLayout zMScheduleMeetingOptionLayout = this.mZMScheduleMeetingOptionLayout;
            if (zMScheduleMeetingOptionLayout != null) {
                zMScheduleMeetingOptionLayout.saveAlterHostsForOnlyEmail();
            }
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                if (currentUserProfile.isEnableAddToGoogleCalendarForMobile() && meetingInfoProto != null && !StringUtil.isEmptyOrNull(meetingInfoProto.getGoogleCalendarUrl())) {
                    UIUtil.openURL(getContext(), meetingInfoProto.getGoogleCalendarUrl());
                    ZMConfEventTracking.logScheduleMeetingOnSuccess(meetingInfoProto, ZMConfEventTracking.TAG_WEB_GOOGLE_CALENDAR);
                    dismissScheduleSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
                } else if (this.mChkAddToCalendar.isChecked()) {
                    if (meetingInfoProto != null) {
                        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_CALENDAR") == 0) {
                            addMeetingInfoToCalendar(meetingInfoProto);
                            dismissScheduleSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
                        } else {
                            zm_requestPermissions(new String[]{"android.permission.WRITE_CALENDAR"}, 2002);
                        }
                    }
                } else if (meetingInfoProto != null) {
                    ZMConfEventTracking.logScheduleMeetingOnSuccess(meetingInfoProto, null);
                    dismissScheduleSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
                }
            }
        } else if (i == 5003) {
            showNormalErrorOrTimeoutDialog();
        } else {
            showErrorCodeDialog(i, str);
        }
    }

    public void onUpdateMeetingResult(int i, @Nullable MeetingInfoProto meetingInfoProto, @NonNull String str) {
        dismissWaitingDialog();
        this.mUpdateMeetingInfo = meetingInfoProto;
        if (i == 0) {
            if (this.mChkAddToCalendar.isChecked()) {
                if (meetingInfoProto != null) {
                    if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_CALENDAR") == 0) {
                        updateMeetingInfoToCalendar(meetingInfoProto);
                        dismissEditSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
                    } else {
                        zm_requestPermissions(new String[]{"android.permission.WRITE_CALENDAR"}, 2003);
                    }
                }
            } else if (meetingInfoProto != null) {
                dismissEditSuccess(ScheduledMeetingItem.fromMeetingInfo(meetingInfoProto));
            }
        } else if (i == 5003) {
            showNormalErrorOrTimeoutDialog();
        } else {
            showErrorCodeDialog(i, str);
        }
    }

    @SuppressLint({"MissingPermission"})
    private void addMeetingInfoToCalendar(@Nullable MeetingInfoProto meetingInfoProto) {
        MeetingInfoProto meetingInfoProto2 = meetingInfoProto;
        if (meetingInfoProto2 != null) {
            String string = getString(C4558R.string.zm_title_meeting_invitation_email_topic, meetingInfoProto.getTopic());
            String joinMeetingUrl = meetingInfoProto.getJoinMeetingUrl();
            long startTime = meetingInfoProto.getStartTime() * 1000;
            long duration = startTime + ((long) (meetingInfoProto.getDuration() * 60000));
            String buildEmailInvitationContent = MeetingInvitationUtil.buildEmailInvitationContent((Context) getActivity(), meetingInfoProto2, false);
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            String email = currentUserProfile != null ? currentUserProfile.getEmail() : null;
            String buildCalendarRrule = meetingInfoProto.getType() == MeetingType.REPEAT ? AndroidAppUtil.buildCalendarRrule(new Date(startTime), ScheduledMeetingItem.zoomRepeatTypeToNativeRepeatType(meetingInfoProto.getRepeatType()), new Date(1000 * meetingInfoProto.getRepeatEndTime())) : null;
            CalendarResult calendarResult = new CalendarResult();
            if (AndroidAppUtil.addNewCalendarEvent(getActivity(), calendarResult, email, startTime, duration, string, buildEmailInvitationContent, joinMeetingUrl, buildCalendarRrule) >= 0) {
                ZMConfEventTracking.logScheduleMeetingOnSuccess(meetingInfoProto2, calendarResult.getmAccountType());
            } else {
                ZMConfEventTracking.logScheduleMeetingOnSuccess(meetingInfoProto2, null);
            }
        }
    }

    private void updateMeetingInfoToCalendar(MeetingInfoProto meetingInfoProto) {
        String string = getString(C4558R.string.zm_title_meeting_invitation_email_topic, meetingInfoProto.getTopic());
        String joinMeetingUrl = meetingInfoProto.getJoinMeetingUrl();
        long startTime = meetingInfoProto.getStartTime() * 1000;
        long duration = startTime + ((long) (meetingInfoProto.getDuration() * 60000));
        String buildEmailInvitationContent = MeetingInvitationUtil.buildEmailInvitationContent((Context) getActivity(), meetingInfoProto, false);
        long[] queryCalendarEventsForMeeting = AndroidAppUtil.queryCalendarEventsForMeeting(getActivity(), meetingInfoProto.getMeetingNumber(), joinMeetingUrl);
        long j = (queryCalendarEventsForMeeting == null || queryCalendarEventsForMeeting.length <= 0) ? -1 : queryCalendarEventsForMeeting[0];
        String str = null;
        if (meetingInfoProto.getType() == MeetingType.REPEAT) {
            str = AndroidAppUtil.buildCalendarRrule(new Date(startTime), ScheduledMeetingItem.zoomRepeatTypeToNativeRepeatType(meetingInfoProto.getRepeatType()), new Date(1000 * meetingInfoProto.getRepeatEndTime()));
        }
        if (j >= 0) {
            AndroidAppUtil.updateCalendarEvent(getActivity(), j, startTime, duration, string, buildEmailInvitationContent, joinMeetingUrl, str);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        EventTaskManager eventTaskManager = getEventTaskManager();
        if (eventTaskManager != null) {
            final int i2 = i;
            final String[] strArr2 = strArr;
            final int[] iArr2 = iArr;
            C29509 r2 = new EventAction("SchedulePermissionResult") {
                public void run(@NonNull IUIElement iUIElement) {
                    ((ScheduleFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
                }
            };
            eventTaskManager.pushLater("SchedulePermissionResult", r2);
        }
    }

    public void handleRequestPermissionResult(int i, @NonNull String[] strArr, int[] iArr) {
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if ("android.permission.WRITE_CALENDAR".equals(strArr[i2])) {
                if (i == 2002) {
                    MeetingInfoProto meetingInfoProto = this.mScheduleMeetingInfo;
                    if (meetingInfoProto != null) {
                        if (iArr[i2] == 0) {
                            addMeetingInfoToCalendar(meetingInfoProto);
                        }
                        dismissScheduleSuccess(ScheduledMeetingItem.fromMeetingInfo(this.mScheduleMeetingInfo));
                    }
                }
                if (i == 2003) {
                    MeetingInfoProto meetingInfoProto2 = this.mUpdateMeetingInfo;
                    if (meetingInfoProto2 != null) {
                        if (iArr[i2] == 0) {
                            updateMeetingInfoToCalendar(meetingInfoProto2);
                        }
                        dismissEditSuccess(ScheduledMeetingItem.fromMeetingInfo(this.mUpdateMeetingInfo));
                    }
                }
            }
        }
    }

    private void showErrorCodeDialog(int i, @NonNull String str) {
        if (i == 1113 || i == 1114 || i == 1115) {
            SimpleMessageDialog.newInstance(getString(C4558R.string.zm_alert_msg_alterhost_51824, ConfLocalHelper.formatScheduleMeetingErrorMsg(str))).show(getFragmentManager(), SimpleMessageDialog.class.getName());
        } else if (i == 3402) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                SimpleMessageDialog.newInstance(getString(C4558R.string.zm_password_rule_not_meet_136699)).showNow(fragmentManager, SimpleMessageDialog.class.getName());
            }
        } else {
            SimpleMessageDialog.newInstance(getString(this.mIsEditMeeting ? C4558R.string.zm_msg_edit_meeting_failed_unknown_error : C4558R.string.zm_msg_schedule_failed_unknown_error, Integer.valueOf(i))).show(getFragmentManager(), SimpleMessageDialog.class.getName());
        }
    }

    private void showNormalErrorOrTimeoutDialog() {
        SimpleMessageDialog.newInstance(this.mIsEditMeeting ? C4558R.string.zm_msg_edit_meeting_failed_normal_or_timeout : C4558R.string.zm_msg_schedule_failed_normal_or_timeout).show(getFragmentManager(), SimpleMessageDialog.class.getName());
    }

    /* access modifiers changed from: private */
    public boolean validateInput() {
        return validateTopic() && validateDate() && validateBeginTime() && validateEndTime() && validateEndRepeatTime(this.mTimeEndRepeat, this.mDateFrom.getTime()) && this.mZMScheduleMeetingOptionLayout.validate3rdPartyAudioInfo();
    }

    private boolean validateTopic() {
        if (!StringUtil.isEmptyOrNull(getTopic())) {
            return true;
        }
        this.mEdtTopic.requestFocus();
        return false;
    }

    private boolean validateDate() {
        if (!this.mDateTimeChangedByMannual) {
            return true;
        }
        Calendar instance = Calendar.getInstance(this.mDateFrom.getTimeZone());
        int i = instance.get(1);
        int i2 = instance.get(2);
        int i3 = instance.get(5);
        int i4 = this.mDateFrom.get(1);
        int i5 = this.mDateFrom.get(2);
        int i6 = this.mDateFrom.get(5);
        if (i4 < i || ((i4 == i && i5 < i2) || (i4 == i && i5 == i2 && i6 < i3))) {
            this.mTxtDate.setTextColor(-65536);
            return false;
        }
        this.mTxtDate.setTextColor(this.mColorDateTimeNormal);
        return true;
    }

    private boolean validateBeginTime() {
        if (!this.mDateTimeChangedByMannual) {
            return true;
        }
        if (this.mDateFrom.before(Calendar.getInstance())) {
            this.mTxtTimeFrom.setTextColor(-65536);
            return false;
        }
        this.mTxtTimeFrom.setTextColor(this.mColorDateTimeNormal);
        return true;
    }

    private boolean validateEndTime() {
        checkEndTime();
        if (this.mDateTo.before(Calendar.getInstance())) {
            this.mTxtTimeTo.setTextColor(-65536);
            return false;
        }
        this.mTxtTimeTo.setTextColor(this.mColorDateTimeNormal);
        return true;
    }

    private boolean validateEndRepeatTime(long j, @NonNull Date date) {
        if (this.mRepeatType == EventRepeatType.NONE || this.mRepeatType == EventRepeatType.UNKNOWN) {
            return true;
        }
        if (j > date.getTime() || j <= 0) {
            this.mTxtEndRepeat.setTextColor(this.mColorDateTimeNormal);
            return true;
        }
        this.mTxtEndRepeat.setTextColor(-65536);
        return false;
    }

    /* access modifiers changed from: private */
    public void validateDuration(boolean z, @NonNull Calendar calendar, @NonNull TextView textView, int i, int i2) {
        boolean z2;
        long j;
        long j2;
        if (!PTApp.getInstance().isPaidUser()) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeZone(calendar.getTimeZone());
            instance.setTimeInMillis(calendar.getTimeInMillis());
            instance.set(11, i);
            instance.set(12, i2);
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile == null) {
                z2 = false;
            } else {
                z2 = currentUserProfile.isSupportFeatureEnablePaidUserForCN();
            }
            if (z) {
                j2 = this.mDateTo.getTimeInMillis();
                j = instance.getTimeInMillis();
            } else {
                j2 = instance.getTimeInMillis();
                j = this.mDateFrom.getTimeInMillis();
            }
            if (((int) ((j2 - j) / 60000)) >= 40 && !z2) {
                ZMActivity zMActivity = (ZMActivity) getActivity();
                if (zMActivity != null && zMActivity.isActive()) {
                    String string = zMActivity.getString(C4558R.string.zm_title_time_limit_meeting_63921, new Object[]{ZMDomainUtil.getZmUrlWebServerWWW()});
                    String string2 = zMActivity.getString(C4558R.string.zm_btn_ok);
                    final Calendar calendar2 = calendar;
                    final TextView textView2 = textView;
                    final int i3 = i;
                    final int i4 = i2;
                    C294010 r2 = new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ScheduleFragment.this.updateTime(calendar2, textView2, i3, i4);
                        }
                    };
                    DialogUtils.showAlertDialog(zMActivity, string, string2, (DialogInterface.OnClickListener) r2);
                    return;
                }
            }
        }
        updateTime(calendar, textView, i, i2);
    }

    public void dismissCancel() {
        this.mZMScheduleMeetingOptionLayout.dismissPopupWindow();
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(0);
    }

    public void dismissScheduleSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, getView());
            this.mZMScheduleMeetingOptionLayout.dismissPopupWindow();
            if (getShowsDialog()) {
                if (activity instanceof IMActivity) {
                    ((IMActivity) activity).onScheduleSuccess(scheduledMeetingItem);
                }
                super.dismiss();
            } else {
                Intent intent = new Intent();
                intent.putExtra(ARG_MEETING_ITEM, scheduledMeetingItem);
                activity.setResult(-1, intent);
                activity.finish();
            }
        }
    }

    public void dismissEditSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            UIUtil.closeSoftKeyboard(activity, getView());
            if (getShowsDialog()) {
                MeetingInfoFragment meetingInfoFragment = MeetingInfoFragment.getMeetingInfoFragment(activity.getSupportFragmentManager());
                if (meetingInfoFragment != null) {
                    meetingInfoFragment.onEditSuccess(scheduledMeetingItem);
                }
                super.dismiss();
            } else {
                Intent intent = new Intent();
                intent.putExtra(ARG_MEETING_ITEM, scheduledMeetingItem);
                activity.setResult(-1, intent);
                activity.finish();
            }
        }
    }

    public void onSelectRepeatType(@NonNull EventRepeatType eventRepeatType) {
        this.mRepeatType = eventRepeatType;
        updateUIStatus();
    }

    private void showWaitingDialog(int i) {
        if (this.mWaitingDialog == null) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null && ((WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName())) == null) {
                this.mWaitingDialog = WaitingDialog.newInstance(i);
                this.mWaitingDialog.show(getFragmentManager(), WaitingDialog.class.getName());
            }
        }
    }

    private void dismissWaitingDialog() {
        WaitingDialog waitingDialog = this.mWaitingDialog;
        if (waitingDialog != null) {
            waitingDialog.dismiss();
            this.mWaitingDialog = null;
            return;
        }
        FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager != null) {
            WaitingDialog waitingDialog2 = (WaitingDialog) fragmentManager.findFragmentByTag(WaitingDialog.class.getName());
            if (waitingDialog2 != null) {
                waitingDialog2.dismiss();
            }
        }
    }

    public void onOptionChanged() {
        this.mBtnSchedule.setEnabled(validateInput());
    }

    public void onScheduleForChanged(boolean z, String str) {
        this.mOptionUsePMI.setVisibility(z ? 0 : 8);
        this.mEdtTopic.setHint(getTopicByName(str));
        EditText editText = this.mEdtTopic;
        editText.setSelection(editText.getText().length());
    }

    public boolean getChkUsePMI() {
        return this.mChkUsePMI.isChecked();
    }

    public boolean isEditMeeting() {
        return this.mIsEditMeeting && this.mMeetingItem != null;
    }

    @Nullable
    public ScheduledMeetingItem getMeetingItem() {
        return this.mMeetingItem;
    }

    public void onUiChangePmiSetting(boolean z) {
        if (z) {
            showPmiChangeTip();
        }
    }

    private void showPmiChangeTip() {
        new Builder(getActivity()).setTitle(C4558R.string.zm_lbl_use_pmi).setMessage(C4558R.string.zm_msg_pmi_setting_change_92505).setCancelable(false).setPositiveButton(C4558R.string.zm_btn_ok, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).create().show();
        this.mZMScheduleMeetingOptionLayout.setIsAlreadyTipPmiChange(true);
    }
}
