package com.zipow.videobox.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ScheduleActivity;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTUI;
import com.zipow.videobox.ptapp.PTUI.IMeetingMgrListener;
import com.zipow.videobox.ptapp.PTUI.IPTUIListener;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.MeetingInvitationUtil;
import com.zipow.videobox.util.TimeFormatUtil;
import com.zipow.videobox.util.UIMgr;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ScheduledMeetingItem;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMDialogFragment;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil.EventRepeatType;
import p021us.zoom.androidlib.util.EventAction;
import p021us.zoom.androidlib.util.IUIElement;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog.Builder;
import p021us.zoom.template.Template;
import p021us.zoom.videomeetings.C4558R;

public class MeetingInfoFragment extends ZMDialogFragment implements OnClickListener, IMeetingMgrListener, IPTUIListener {
    private static final String ARG_AUTO_ADD_INVITEE = "autoAddInvitee";
    private static final String ARG_MEETING_ITEM = "meetingItem";
    private static final int REQUEST_ADD_TO_CALENDAR = 3002;
    private static final int REQUEST_DELETE_MEETING = 3001;
    private Button mBtnAddToCalendar;
    private Button mBtnBack;
    private Button mBtnDeleteMeeting;
    private Button mBtnEdit;
    private Button mBtnJoinFromRoom;
    private Button mBtnSendInvitation;
    private Button mBtnStartMeeting;
    private boolean mHasSendInvitation = false;
    /* access modifiers changed from: private */
    @Nullable
    public ScheduledMeetingItem mMeetingItem;
    private View mPanelDuration;
    private View mPanelPassword;
    private TextView mTxtDuration;
    private TextView mTxtMeetingId;
    private TextView mTxtMeetingIdTitle;
    private TextView mTxtPassword;
    private TextView mTxtTopic;
    private TextView mTxtWhen;

    public static class DeleteMeetingConfirmDialog extends ZMDialogFragment {
        private static final String ARG_MEETING_ITEM = "arg_meeting_item";

        public static void showDialog(FragmentManager fragmentManager, ScheduledMeetingItem scheduledMeetingItem) {
            DeleteMeetingConfirmDialog deleteMeetingConfirmDialog = new DeleteMeetingConfirmDialog();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_MEETING_ITEM, scheduledMeetingItem);
            deleteMeetingConfirmDialog.setArguments(bundle);
            deleteMeetingConfirmDialog.show(fragmentManager, DeleteMeetingConfirmDialog.class.getName());
        }

        public DeleteMeetingConfirmDialog() {
            setCancelable(true);
        }

        public void onStart() {
            super.onStart();
        }

        @NonNull
        public Dialog onCreateDialog(Bundle bundle) {
            String str = "";
            Bundle arguments = getArguments();
            if (arguments != null) {
                ScheduledMeetingItem scheduledMeetingItem = (ScheduledMeetingItem) arguments.getSerializable(ARG_MEETING_ITEM);
                PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
                if (!(currentUserProfile == null || scheduledMeetingItem == null || StringUtil.isSameString(currentUserProfile.getUserID(), scheduledMeetingItem.getHostId()))) {
                    str = StringUtil.isEmptyOrNull(scheduledMeetingItem.getHostName()) ? ZMScheduleUtil.getAltHostEmailById(scheduledMeetingItem.getHostId()) : scheduledMeetingItem.getHostName();
                }
            }
            return new Builder(getActivity()).setTitle((CharSequence) Html.fromHtml(StringUtil.isEmptyOrNull(str) ? getString(C4558R.string.zm_msg_delete_self_meeting_120521) : getString(C4558R.string.zm_msg_delete_other_meeting_120521, str))).setPositiveButton(C4558R.string.zm_btn_delete_meeting, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    DeleteMeetingConfirmDialog.this.onClickYes();
                }
            }).setNegativeButton(C4558R.string.zm_btn_cancel, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            }).create();
        }

        /* access modifiers changed from: private */
        public void onClickYes() {
            MeetingInfoFragment meetingInfoFragment = (MeetingInfoFragment) getParentFragment();
            if (meetingInfoFragment != null) {
                meetingInfoFragment.deleteThisMeeting();
            }
        }
    }

    public void onDataNetworkStatusChanged(boolean z) {
    }

    public void onListCalendarEventsResult(int i) {
    }

    public void onListMeetingResult(int i) {
    }

    public void onPMIEvent(int i, int i2, MeetingInfoProto meetingInfoProto) {
    }

    public void onPTAppCustomEvent(int i, long j) {
    }

    public void onScheduleMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str) {
    }

    public void onStartFailBeforeLaunch(int i) {
    }

    public void onUpdateMeetingResult(int i, MeetingInfoProto meetingInfoProto, String str) {
    }

    public static void showInActivity(ZMActivity zMActivity, ScheduledMeetingItem scheduledMeetingItem, boolean z) {
        MeetingInfoFragment meetingInfoFragment = new MeetingInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_MEETING_ITEM, scheduledMeetingItem);
        bundle.putBoolean(ARG_AUTO_ADD_INVITEE, z);
        meetingInfoFragment.setArguments(bundle);
        zMActivity.getSupportFragmentManager().beginTransaction().add(16908290, meetingInfoFragment, MeetingInfoFragment.class.getName()).commit();
    }

    public static void showDialog(@NonNull FragmentManager fragmentManager, ScheduledMeetingItem scheduledMeetingItem, boolean z) {
        if (getMeetingInfoFragment(fragmentManager) == null) {
            MeetingInfoFragment meetingInfoFragment = new MeetingInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(ARG_MEETING_ITEM, scheduledMeetingItem);
            bundle.putBoolean(ARG_AUTO_ADD_INVITEE, z);
            meetingInfoFragment.setArguments(bundle);
            meetingInfoFragment.show(fragmentManager, MeetingInfoFragment.class.getName());
        }
    }

    @Nullable
    public static MeetingInfoFragment getMeetingInfoFragment(FragmentManager fragmentManager) {
        return (MeetingInfoFragment) fragmentManager.findFragmentByTag(MeetingInfoFragment.class.getName());
    }

    public MeetingInfoFragment() {
        setStyle(1, C4558R.style.ZMDialog);
    }

    public void onStart() {
        super.onStart();
    }

    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(C4558R.layout.zm_meeting_info, null);
        this.mBtnBack = (Button) inflate.findViewById(C4558R.C4560id.btnBack);
        this.mBtnStartMeeting = (Button) inflate.findViewById(C4558R.C4560id.btnStartMeeting);
        this.mBtnJoinFromRoom = (Button) inflate.findViewById(C4558R.C4560id.btnJoinFromRoom);
        this.mBtnSendInvitation = (Button) inflate.findViewById(C4558R.C4560id.btnSendInvitation);
        this.mBtnAddToCalendar = (Button) inflate.findViewById(C4558R.C4560id.btnAddToCalendar);
        this.mBtnDeleteMeeting = (Button) inflate.findViewById(C4558R.C4560id.btnDeleteMeeting);
        this.mBtnEdit = (Button) inflate.findViewById(C4558R.C4560id.btnEdit);
        this.mTxtMeetingIdTitle = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingIdTitle);
        this.mTxtTopic = (TextView) inflate.findViewById(C4558R.C4560id.txtTopic);
        this.mTxtMeetingId = (TextView) inflate.findViewById(C4558R.C4560id.txtMeetingId);
        this.mTxtDuration = (TextView) inflate.findViewById(C4558R.C4560id.txtDuration);
        this.mTxtWhen = (TextView) inflate.findViewById(C4558R.C4560id.txtWhen);
        this.mTxtPassword = (TextView) inflate.findViewById(C4558R.C4560id.txtPassword);
        this.mPanelDuration = inflate.findViewById(C4558R.C4560id.panelDuration);
        this.mPanelPassword = inflate.findViewById(C4558R.C4560id.panelPassword);
        this.mBtnBack.setOnClickListener(this);
        this.mBtnStartMeeting.setOnClickListener(this);
        this.mBtnJoinFromRoom.setOnClickListener(this);
        this.mBtnSendInvitation.setOnClickListener(this);
        this.mBtnAddToCalendar.setOnClickListener(this);
        this.mBtnDeleteMeeting.setOnClickListener(this);
        this.mBtnEdit.setOnClickListener(this);
        int i = 8;
        this.mBtnJoinFromRoom.setVisibility(PTApp.getInstance().isJoinMeetingBySpecialModeEnabled(0) ? 0 : 8);
        Button button = this.mBtnAddToCalendar;
        if (AndroidAppUtil.hasCalendarApp(getActivity())) {
            i = 0;
        }
        button.setVisibility(i);
        if (bundle != null) {
            this.mHasSendInvitation = bundle.getBoolean("mHasSendInvitation", false);
        }
        return inflate;
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnBack) {
            onClickBtnBack();
        } else if (id == C4558R.C4560id.btnEdit) {
            onClickBtnEdit();
        } else if (id == C4558R.C4560id.btnStartMeeting) {
            onClickBtnStartMeeting();
        } else if (id == C4558R.C4560id.btnJoinFromRoom) {
            onClickBtnJoinFromRoom();
        } else if (id == C4558R.C4560id.btnSendInvitation) {
            onClickBtnSendInvitation();
        } else if (id == C4558R.C4560id.btnAddToCalendar) {
            onClickBtnAddToCalendar();
        } else if (id == C4558R.C4560id.btnDeleteMeeting) {
            onClickBtnDeleteMeeting();
        }
    }

    private void onClickBtnDeleteMeeting() {
        DeleteMeetingConfirmDialog.showDialog(getChildFragmentManager(), this.mMeetingItem);
    }

    private void onClickBtnSendInvitation() {
        sendInvitations(-1);
    }

    private void sendInvitations(int i) {
        String str;
        String buildEmailInvitationContent = MeetingInvitationUtil.buildEmailInvitationContent((Context) getActivity(), this.mMeetingItem, true);
        FragmentActivity activity = getActivity();
        int i2 = C4558R.string.zm_title_meeting_invitation_email_topic;
        Object[] objArr = new Object[1];
        ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
        objArr[0] = scheduledMeetingItem == null ? "" : scheduledMeetingItem.getTopic();
        String string = activity.getString(i2, objArr);
        String string2 = getActivity().getString(C4558R.string.zm_lbl_add_invitees);
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            this.mMeetingItem.setInvitationEmailContentWithTime(MeetingInvitationUtil.buildEmailInvitationContent((Context) getActivity(), this.mMeetingItem, true));
            MeetingInfoProto meetingInfo = this.mMeetingItem.toMeetingInfo();
            EventRepeatType zoomRepeatTypeToNativeRepeatType = ScheduledMeetingItem.zoomRepeatTypeToNativeRepeatType(this.mMeetingItem.getRepeatType());
            if (!this.mMeetingItem.isRecurring() || zoomRepeatTypeToNativeRepeatType != EventRepeatType.NONE) {
                String[] strArr = {getActivity().getString(C4558R.string.zm_meeting_invitation_ics_name)};
                if (meetingHelper.createIcsFileFromMeeting(meetingInfo, strArr, TimeZone.getDefault().getID())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("file://");
                    sb.append(strArr[0]);
                    str = sb.toString();
                    String joinMeetingUrl = this.mMeetingItem.getJoinMeetingUrl();
                    long meetingNo = this.mMeetingItem.getMeetingNo();
                    HashMap hashMap = new HashMap();
                    hashMap.put("joinMeetingUrl", joinMeetingUrl);
                    hashMap.put(InviteFragment.ARG_MEETING_ID, String.valueOf(meetingNo));
                    ZMSendMessageFragment.show(getActivity(), getFragmentManager(), null, null, string, buildEmailInvitationContent, new Template(getString(C4558R.string.zm_msg_sms_invite_scheduled_meeting)).format(hashMap), str, string2, i);
                }
            }
        }
        str = null;
        String joinMeetingUrl2 = this.mMeetingItem.getJoinMeetingUrl();
        long meetingNo2 = this.mMeetingItem.getMeetingNo();
        HashMap hashMap2 = new HashMap();
        hashMap2.put("joinMeetingUrl", joinMeetingUrl2);
        hashMap2.put(InviteFragment.ARG_MEETING_ID, String.valueOf(meetingNo2));
        ZMSendMessageFragment.show(getActivity(), getFragmentManager(), null, null, string, buildEmailInvitationContent, new Template(getString(C4558R.string.zm_msg_sms_invite_scheduled_meeting)).format(hashMap2), str, string2, i);
    }

    private void onClickBtnAddToCalendar() {
        if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_CALENDAR") == 0) {
            handleAddToCalendar();
        } else {
            zm_requestPermissions(new String[]{"android.permission.WRITE_CALENDAR"}, 3002);
        }
    }

    @SuppressLint({"MissingPermission"})
    private void handleAddToCalendar() {
        long j;
        long j2;
        String str;
        if (this.mMeetingItem != null) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null) {
                String buildEmailInvitationContent = MeetingInvitationUtil.buildEmailInvitationContent((Context) getActivity(), this.mMeetingItem, false);
                String string = getActivity().getString(C4558R.string.zm_title_meeting_invitation_email_topic, new Object[]{this.mMeetingItem.getTopic()});
                String joinMeetingUrlForInvite = this.mMeetingItem.getJoinMeetingUrlForInvite();
                long startTime = this.mMeetingItem.getStartTime();
                long duration = startTime + ((long) (this.mMeetingItem.getDuration() * 60000));
                long[] queryCalendarEventsForMeeting = AndroidAppUtil.queryCalendarEventsForMeeting(getActivity(), this.mMeetingItem.getMeetingNo(), joinMeetingUrlForInvite);
                long j3 = (queryCalendarEventsForMeeting == null || queryCalendarEventsForMeeting.length <= 0) ? -1 : queryCalendarEventsForMeeting[0];
                String str2 = null;
                if (this.mMeetingItem.isRecurring()) {
                    str2 = AndroidAppUtil.buildCalendarRrule(new Date(startTime), ScheduledMeetingItem.zoomRepeatTypeToNativeRepeatType(this.mMeetingItem.getRepeatType()), new Date(this.mMeetingItem.getRepeatEndTime()));
                }
                if (j3 < 0) {
                    j = startTime;
                    j2 = AndroidAppUtil.addNewCalendarEvent((Context) getActivity(), currentUserProfile.getEmail(), startTime, duration, string, buildEmailInvitationContent, joinMeetingUrlForInvite, str2);
                    str = joinMeetingUrlForInvite;
                } else {
                    j = startTime;
                    str = joinMeetingUrlForInvite;
                    AndroidAppUtil.updateCalendarEvent(getActivity(), j3, j, duration, string, buildEmailInvitationContent, joinMeetingUrlForInvite, str2);
                    j2 = j3;
                }
                if (j2 >= 0) {
                    AndroidAppUtil.viewCalendarEvent(getActivity(), j2, j, duration);
                } else {
                    AndroidAppUtil.createCalendarEvent(getActivity(), j, duration, string, buildEmailInvitationContent, str);
                }
            }
        }
    }

    private void onClickBtnEdit() {
        if (UIMgr.isLargeMode(getActivity())) {
            ScheduleFragment.showEditMeetingDialog(getFragmentManager(), this.mMeetingItem);
        } else {
            ScheduleActivity.showEditMeeting((ZMActivity) getActivity(), 103, this.mMeetingItem);
        }
    }

    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        final int i2 = i;
        final String[] strArr2 = strArr;
        final int[] iArr2 = iArr;
        C28421 r2 = new EventAction("MeetingInfoPermissionResult") {
            public void run(@NonNull IUIElement iUIElement) {
                ((MeetingInfoFragment) iUIElement).handleRequestPermissionResult(i2, strArr2, iArr2);
            }
        };
        getNonNullEventTaskManagerOrThrowException().pushLater("MeetingInfoPermissionResult", r2);
    }

    public void handleRequestPermissionResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (strArr != null && iArr != null) {
            for (int i2 = 0; i2 < strArr.length; i2++) {
                if ("android.permission.WRITE_CALENDAR".equals(strArr[i2]) && iArr[i2] == 0) {
                    if (i == 3001) {
                        handleDeleteMeetingResult();
                        dismiss();
                    } else if (i == 3002) {
                        handleAddToCalendar();
                    }
                }
            }
        }
    }

    private void onClickBtnStartMeeting() {
        if (this.mMeetingItem != null) {
            checkStartMeeting();
        }
    }

    private void onClickBtnJoinFromRoom() {
        if (this.mMeetingItem != null) {
            final ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(zMActivity, new SimpleOnButtonClickListener() {
                    public void onPositiveClick() {
                        ConfActivity.joinFromRoom(zMActivity, MeetingInfoFragment.this.mMeetingItem.getMeetingNo(), MeetingInfoFragment.this.mMeetingItem.getId(), MeetingInfoFragment.this.mMeetingItem.getPassword(), MeetingInfoFragment.this.mMeetingItem.getPersonalLink());
                    }
                });
            }
        }
    }

    private void checkStartMeeting() {
        Context context = getContext();
        if (context != null) {
            MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(context, new SimpleOnButtonClickListener() {
                public void onPositiveClick() {
                    MeetingInfoFragment.this.startMeeting();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void startMeeting() {
        if (this.mMeetingItem != null) {
            ZMActivity zMActivity = (ZMActivity) getActivity();
            if (zMActivity != null) {
                if (!this.mMeetingItem.ismIsCanStartMeetingForMySelf()) {
                    ConfActivity.checkExistingCallAndJoinMeeting(zMActivity, this.mMeetingItem.getMeetingNo(), this.mMeetingItem.getId(), this.mMeetingItem.getPersonalLink(), this.mMeetingItem.getPassword());
                } else if (ConfActivity.startMeeting(zMActivity, this.mMeetingItem.getMeetingNo(), this.mMeetingItem.getId())) {
                    ZMConfEventTracking.logStartMeetingInShortCut(this.mMeetingItem);
                }
            }
        }
    }

    public void deleteThisMeeting() {
        if (this.mMeetingItem != null) {
            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
            if (meetingHelper != null) {
                long meetingNo = this.mMeetingItem.getMeetingNo();
                long originalMeetingNo = this.mMeetingItem.getOriginalMeetingNo();
                if (originalMeetingNo > 0) {
                    meetingNo = originalMeetingNo;
                }
                meetingHelper.deleteMeeting(meetingNo);
            }
        }
    }

    private void onClickBtnBack() {
        dismiss();
    }

    public void dismiss() {
        UIUtil.closeSoftKeyboard(getActivity(), getView());
        finishFragment(true);
    }

    public void onResume() {
        super.onResume();
        loadData();
        PTUI.getInstance().addMeetingMgrListener(this);
        PTUI.getInstance().addPTUIListener(this);
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
        if (scheduledMeetingItem == null || meetingHelper == null || (scheduledMeetingItem.ismIsCanStartMeetingForMySelf() && meetingHelper.getMeetingItemByNumber(this.mMeetingItem.getMeetingNo()) == null)) {
            dismiss();
            return;
        }
        updateUIForCallStatus((long) PTApp.getInstance().getCallStatus());
        Bundle arguments = getArguments();
        if (arguments != null) {
            boolean z = arguments.getBoolean(ARG_AUTO_ADD_INVITEE);
            if (!this.mHasSendInvitation && z) {
                sendInvitations(1);
                this.mHasSendInvitation = true;
            }
        }
    }

    public void onPause() {
        super.onPause();
        PTUI.getInstance().removeMeetingMgrListener(this);
        PTUI.getInstance().removePTUIListener(this);
    }

    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("mHasSendInvitation", this.mHasSendInvitation);
    }

    public void onPTAppEvent(int i, long j) {
        if (i == 22) {
            updateUIForCallStatus(j);
        }
    }

    public void onDeleteMeetingResult(int i) {
        processDeleteMeetingResult(i);
    }

    public void onEditSuccess(ScheduledMeetingItem scheduledMeetingItem) {
        this.mMeetingItem = scheduledMeetingItem;
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putSerializable(ARG_MEETING_ITEM, scheduledMeetingItem);
            sendInvitations(1);
            this.mHasSendInvitation = true;
            loadData();
        }
    }

    private void updateUIForCallStatus(long j) {
        int i;
        ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
        if (scheduledMeetingItem != null) {
            switch ((int) j) {
                case 1:
                    this.mBtnStartMeeting.setText(scheduledMeetingItem.ismIsCanStartMeetingForMySelf() ? C4558R.string.zm_btn_start_meeting : C4558R.string.zm_btn_join_meeting);
                    this.mBtnStartMeeting.setEnabled(false);
                    this.mBtnDeleteMeeting.setEnabled(false);
                    this.mBtnJoinFromRoom.setEnabled(false);
                    break;
                case 2:
                    long activeMeetingNo = PTApp.getInstance().getActiveMeetingNo();
                    String activeCallId = PTApp.getInstance().getActiveCallId();
                    if (activeMeetingNo == this.mMeetingItem.getMeetingNo() || (activeCallId != null && activeCallId.equals(this.mMeetingItem.getId()))) {
                        this.mBtnStartMeeting.setText(C4558R.string.zm_btn_return_to_conf);
                        this.mBtnDeleteMeeting.setEnabled(false);
                        this.mBtnJoinFromRoom.setEnabled(false);
                    } else {
                        Button button = this.mBtnStartMeeting;
                        ScheduledMeetingItem scheduledMeetingItem2 = this.mMeetingItem;
                        button.setText((scheduledMeetingItem2 == null || !scheduledMeetingItem2.ismIsCanStartMeetingForMySelf()) ? C4558R.string.zm_btn_join_meeting : C4558R.string.zm_btn_start_meeting);
                    }
                    this.mBtnStartMeeting.setEnabled(true);
                    break;
                default:
                    Button button2 = this.mBtnStartMeeting;
                    if (scheduledMeetingItem.ismIsCanStartMeetingForMySelf()) {
                        i = C4558R.string.zm_btn_start_meeting;
                    } else {
                        i = C4558R.string.zm_btn_join_meeting;
                    }
                    button2.setText(i);
                    this.mBtnStartMeeting.setEnabled(true);
                    this.mBtnDeleteMeeting.setEnabled(true);
                    this.mBtnJoinFromRoom.setEnabled(true);
                    break;
            }
        }
    }

    private void processDeleteMeetingResult(int i) {
        if (i != 0) {
            SimpleMessageDialog.newInstance(C4558R.string.zm_alert_delete_meeting_failed).show(getChildFragmentManager(), SimpleMessageDialog.class.getName());
        } else if (this.mMeetingItem != null) {
            if (VERSION.SDK_INT < 23 || checkSelfPermission("android.permission.WRITE_CALENDAR") == 0) {
                handleDeleteMeetingResult();
                dismiss();
            } else {
                zm_requestPermissions(new String[]{"android.permission.WRITE_CALENDAR"}, 3001);
            }
        }
    }

    @SuppressLint({"MissingPermission"})
    private void handleDeleteMeetingResult() {
        ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
        if (scheduledMeetingItem != null) {
            long[] queryCalendarEventsForMeeting = AndroidAppUtil.queryCalendarEventsForMeeting(getActivity(), this.mMeetingItem.getMeetingNo(), scheduledMeetingItem.getJoinMeetingUrlForInvite());
            if (queryCalendarEventsForMeeting != null) {
                for (long deleteCalendarEvent : queryCalendarEventsForMeeting) {
                    AndroidAppUtil.deleteCalendarEvent(getActivity(), deleteCalendarEvent);
                }
            }
        }
    }

    private void loadData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mMeetingItem = (ScheduledMeetingItem) arguments.getSerializable(ARG_MEETING_ITEM);
            ScheduledMeetingItem scheduledMeetingItem = this.mMeetingItem;
            if (scheduledMeetingItem != null) {
                this.mTxtTopic.setText(scheduledMeetingItem.getTopic());
                if (this.mMeetingItem.getMeetingNo() != 0) {
                    this.mTxtMeetingId.setText(StringUtil.formatConfNumber(this.mMeetingItem.getMeetingNo()));
                } else {
                    this.mTxtMeetingId.setText(this.mMeetingItem.getPersonalLink());
                }
                if (this.mMeetingItem.isRecurring()) {
                    this.mPanelDuration.setVisibility(8);
                    this.mTxtWhen.setText(C4558R.string.zm_lbl_time_recurring);
                } else {
                    this.mPanelDuration.setVisibility(0);
                    this.mTxtDuration.setText(getString(C4558R.string.zm_lbl_xxx_minutes, Integer.valueOf(this.mMeetingItem.getDuration())));
                    this.mTxtWhen.setText(TimeFormatUtil.formatDateTime(getActivity(), this.mMeetingItem.getStartTime(), true));
                }
                if (this.mMeetingItem.hasPassword()) {
                    this.mPanelPassword.setVisibility(0);
                    this.mTxtPassword.setText(this.mMeetingItem.getPassword());
                    if (this.mMeetingItem.isUsePmiAsMeetingID()) {
                        ScheduledMeetingItem pMIMeetingItem = ZmPtUtils.getPMIMeetingItem();
                        if (pMIMeetingItem != null) {
                            this.mTxtPassword.setText(pMIMeetingItem.getPassword());
                        }
                    }
                } else {
                    this.mPanelPassword.setVisibility(8);
                }
                if (this.mMeetingItem.getExtendMeetingType() == 2 || !this.mMeetingItem.ismIsCanStartMeetingForMySelf()) {
                    this.mBtnEdit.setVisibility(8);
                    this.mBtnDeleteMeeting.setVisibility(8);
                    if (!this.mMeetingItem.ismIsCanStartMeetingForMySelf()) {
                        this.mBtnAddToCalendar.setVisibility(8);
                        this.mBtnSendInvitation.setVisibility(8);
                    }
                }
                TextView textView = this.mTxtMeetingIdTitle;
                if (textView != null) {
                    textView.setText(this.mMeetingItem.ismIsWebinar() ? C4558R.string.zm_lbl_webinar_id2_150183 : C4558R.string.zm_lbl_meeting_id2);
                }
            }
        }
    }
}
