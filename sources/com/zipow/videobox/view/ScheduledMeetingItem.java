package com.zipow.videobox.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.SimpleActivity;
import com.zipow.videobox.dialog.ChooseHostDialog;
import com.zipow.videobox.dialog.ChooseHostDialog.MeetingHostByItem;
import com.zipow.videobox.dialog.ChooseHostDialog.OnItemSelectedListener;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog;
import com.zipow.videobox.dialog.MeetingInSipCallConfirmDialog.SimpleOnButtonClickListener;
import com.zipow.videobox.fragment.InviteFragment;
import com.zipow.videobox.fragment.ZMPMIEditFragment;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.Builder;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.GoogCalendarEvent;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.util.MeetingInvitationUtil;
import com.zipow.videobox.util.ZMScheduleUtil;
import com.zipow.videobox.view.p014mm.message.FontStyleHelper;
import java.io.Serializable;
import java.util.HashMap;
import java.util.TimeZone;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMSendMessageFragment;
import p021us.zoom.androidlib.util.AndroidAppUtil.EventRepeatType;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.TimeUtil;
import p021us.zoom.template.Template;
import p021us.zoom.videomeetings.C4558R;

public class ScheduledMeetingItem implements Serializable, OnClickListener {
    public static final int ExtendMeetingType_NONE = -999;
    private static final long serialVersionUID = 1;
    private boolean mAttendeeVideoOff;
    @Nullable
    private String mCallInNumber = null;
    private boolean mCanJoinBeforeHost;
    private int mDuration;
    private int mExtendMeetingType = 0;
    @Nullable
    private String mH323Gateway = null;
    @Nullable
    private String mHostEmail = null;
    @Nullable
    private String mHostId = null;
    @Nullable
    private String mHostName = null;
    private boolean mHostVideoOff;
    private String mId;
    private String mInvitationEmailContent;
    private String mInvitationEmailContentWithTime;
    private boolean mIsCNMeetingOn;
    private boolean mIsCanStartMeetingForMySelf = true;
    private boolean mIsEnableAudioWaterMark;
    private boolean mIsEnableCloudRecording;
    private boolean mIsEnableLanguageInterpretation = false;
    private boolean mIsEnableLocalRecording;
    private boolean mIsEnableMeetingToPublic;
    private boolean mIsEnableWaitingRoom = false;
    private boolean mIsHostByLabel = false;
    private boolean mIsLabel = false;
    private boolean mIsRecCopy = false;
    private boolean mIsShareOnlyMeeting = false;
    private boolean mIsSupportWaitingRoom = false;
    private boolean mIsWebRecurrenceMeeting = false;
    private boolean mIsWebinar = false;
    private boolean mIsZoomMeeting = true;
    private int mJbhTime;
    @Nullable
    private String mJoinMeetingUrl = null;
    private String mJoinMeetingUrlForInvite;
    private String mLabel = "";
    private long mMeetingNo;
    private int mMeetingStatus;
    private MeetingType mMeetingType = MeetingType.SCHEDULE;
    private boolean mOnlySignJoin;
    private long mOriginalMeetingNo;
    private String mOtherTeleConfInfo;
    private boolean mPSTNEnabled = false;
    private String mPassword;
    private String mPersonalLink;
    private long mRecCopyStartTime;
    private long mRepeatEndTime;
    private int mRepeatType = 0;
    private boolean mSelfTelephoneOn;
    private long mStartTime;
    private boolean mTelephonyOff;
    private String mTimeZoneId;
    private String mTopic;
    private boolean mUsePmiAsMeetingID;
    private boolean mVoipOff;

    public static int nativeRepeatTypeToZoomRepeatType(EventRepeatType eventRepeatType) {
        switch (eventRepeatType) {
            case DAILY:
            case WORKDAY:
                return 1;
            case WEEKLY:
                return 2;
            case BIWEEKLY:
                return 3;
            case MONTHLY:
                return 4;
            case YEARLY:
                return 5;
            default:
                return 0;
        }
    }

    @NonNull
    public static EventRepeatType zoomRepeatTypeToNativeRepeatType(int i) {
        switch (i) {
            case 1:
                return EventRepeatType.DAILY;
            case 2:
                return EventRepeatType.WEEKLY;
            case 3:
                return EventRepeatType.BIWEEKLY;
            case 4:
                return EventRepeatType.MONTHLY;
            case 5:
                return EventRepeatType.YEARLY;
            default:
                return EventRepeatType.NONE;
        }
    }

    @NonNull
    public static ScheduledMeetingItem createAddCalendarItem() {
        ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
        scheduledMeetingItem.setExtendMeetingType(ExtendMeetingType_NONE);
        return scheduledMeetingItem;
    }

    @NonNull
    public static ScheduledMeetingItem fromMeetingInfo(@NonNull MeetingInfoProto meetingInfoProto) {
        ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
        scheduledMeetingItem.setTopic(meetingInfoProto.getTopic());
        scheduledMeetingItem.setStartTime(meetingInfoProto.getStartTime() * 1000);
        scheduledMeetingItem.setDuration(meetingInfoProto.getDuration());
        scheduledMeetingItem.setMeetingType(meetingInfoProto.getType());
        scheduledMeetingItem.setMeetingNo(meetingInfoProto.getMeetingNumber());
        scheduledMeetingItem.setPassword(meetingInfoProto.getPassword());
        scheduledMeetingItem.setId(meetingInfoProto.getId());
        scheduledMeetingItem.setMeetingStatus(meetingInfoProto.getMeetingStatus());
        scheduledMeetingItem.setInvitationEmailContent(meetingInfoProto.getInviteEmailContent());
        scheduledMeetingItem.setInvitationEmailContentWithTime(meetingInfoProto.getInviteEmailContentWithTime());
        scheduledMeetingItem.setCanJoinBeforeHost(meetingInfoProto.getCanJoinBeforeHost());
        scheduledMeetingItem.setRepeatType(meetingInfoProto.getRepeatType());
        scheduledMeetingItem.setRepeatEndTime(meetingInfoProto.getRepeatEndTime() * 1000);
        scheduledMeetingItem.setJoinMeetingUrl(meetingInfoProto.getJoinMeetingUrl());
        scheduledMeetingItem.setCallInNumber(meetingInfoProto.getCallinNumber());
        scheduledMeetingItem.setPSTNEnabled(meetingInfoProto.getPSTNEnabled());
        scheduledMeetingItem.setH323Gateway(meetingInfoProto.getH323Gateway());
        scheduledMeetingItem.setHostId(meetingInfoProto.getMeetingHostID());
        scheduledMeetingItem.setHostName(meetingInfoProto.getMeetingHostName());
        scheduledMeetingItem.setIsShareOnlyMeeting(meetingInfoProto.getIsShareOnlyMeeting());
        scheduledMeetingItem.setmIsWebinar(meetingInfoProto.getIsWebinar());
        scheduledMeetingItem.setExtendMeetingType(meetingInfoProto.getExtendMeetingType());
        scheduledMeetingItem.setHostVideoOff(meetingInfoProto.getHostVideoOff());
        scheduledMeetingItem.setAttendeeVideoOff(meetingInfoProto.getAttendeeVideoOff());
        scheduledMeetingItem.setVoipOff(meetingInfoProto.getVoipOff());
        scheduledMeetingItem.setTelephonyOff(meetingInfoProto.getTelephonyOff());
        scheduledMeetingItem.setOtherTeleConfInfo(meetingInfoProto.getOtherTeleConfInfo());
        scheduledMeetingItem.setSelfTelephoneOn(meetingInfoProto.getIsSelfTelephonyOn());
        scheduledMeetingItem.setUsePmiAsMeetingID(meetingInfoProto.getUsePmiAsMeetingID());
        scheduledMeetingItem.setOriginalMeetingNo(meetingInfoProto.getOriginalMeetingNumber());
        scheduledMeetingItem.setCNMeetingOn(meetingInfoProto.getIsCnMeeting());
        scheduledMeetingItem.setTimeZoneId(meetingInfoProto.getTimeZoneId());
        scheduledMeetingItem.setOnlySignJoin(meetingInfoProto.getIsOnlySignJoin());
        scheduledMeetingItem.setmIsEnableMeetingToPublic(meetingInfoProto.getIsEnableMeetingToPublic());
        scheduledMeetingItem.setmIsEnableCloudRecording(meetingInfoProto.getIsEnableAutoRecordingCloud());
        scheduledMeetingItem.setmIsEnableLocalRecording(meetingInfoProto.getIsEnableAutoRecordingLocal());
        scheduledMeetingItem.setmIsEnableAudioWaterMark(meetingInfoProto.getIsEnableAudioWatermark());
        scheduledMeetingItem.setmIsWebRecurrenceMeeting(meetingInfoProto.getIsWebRecurrenceMeeting());
        scheduledMeetingItem.setmIsEnableLanguageInterpretation(meetingInfoProto.getIsEnableLanguageInterpretation());
        scheduledMeetingItem.setEnableWaitingRoom(meetingInfoProto.getIsEnableWaitingRoom());
        scheduledMeetingItem.setSupportWaitingRoom(meetingInfoProto.getIsSupportWaitingRoom());
        scheduledMeetingItem.setJbhTime(meetingInfoProto.getJbhPriorTime());
        scheduledMeetingItem.setJoinMeetingUrlForInvite(meetingInfoProto.getJoinMeetingUrlForInvite());
        return scheduledMeetingItem;
    }

    @NonNull
    public static ScheduledMeetingItem fromMeetingItem(ScheduledMeetingItem scheduledMeetingItem) {
        ScheduledMeetingItem scheduledMeetingItem2 = new ScheduledMeetingItem();
        scheduledMeetingItem2.setTopic(scheduledMeetingItem.getTopic());
        scheduledMeetingItem2.setStartTime(scheduledMeetingItem.getStartTime());
        scheduledMeetingItem2.setDuration(scheduledMeetingItem.getDuration());
        scheduledMeetingItem2.setMeetingType(scheduledMeetingItem.getMeetingType());
        scheduledMeetingItem2.setMeetingNo(scheduledMeetingItem.getMeetingNo());
        scheduledMeetingItem2.setPassword(scheduledMeetingItem.getPassword());
        scheduledMeetingItem2.setId(scheduledMeetingItem.getId());
        scheduledMeetingItem2.setMeetingStatus(scheduledMeetingItem.getMeetingStatus());
        scheduledMeetingItem2.setInvitationEmailContent(scheduledMeetingItem.getInvitationEmailContent());
        scheduledMeetingItem2.setInvitationEmailContentWithTime(scheduledMeetingItem.getInvitationEmailContentWithTime());
        scheduledMeetingItem2.setCanJoinBeforeHost(scheduledMeetingItem.getCanJoinBeforeHost());
        scheduledMeetingItem2.setRepeatType(scheduledMeetingItem.getRepeatType());
        scheduledMeetingItem2.setRepeatEndTime(scheduledMeetingItem.getRepeatEndTime());
        scheduledMeetingItem2.setJoinMeetingUrl(scheduledMeetingItem.getJoinMeetingUrl());
        scheduledMeetingItem2.setCallInNumber(scheduledMeetingItem.getCallInNumber());
        scheduledMeetingItem2.setPSTNEnabled(scheduledMeetingItem.isPSTNEnabled());
        scheduledMeetingItem2.setH323Gateway(scheduledMeetingItem.getH323Gateway());
        scheduledMeetingItem2.setHostId(scheduledMeetingItem.getHostId());
        scheduledMeetingItem2.setHostName(scheduledMeetingItem.getHostName());
        scheduledMeetingItem2.setIsShareOnlyMeeting(scheduledMeetingItem.isShareOnlyMeeting());
        scheduledMeetingItem2.setmIsWebinar(scheduledMeetingItem.ismIsWebinar());
        scheduledMeetingItem2.setExtendMeetingType(scheduledMeetingItem.getExtendMeetingType());
        scheduledMeetingItem2.setHostVideoOff(scheduledMeetingItem.isHostVideoOff());
        scheduledMeetingItem2.setAttendeeVideoOff(scheduledMeetingItem.isAttendeeVideoOff());
        scheduledMeetingItem2.setVoipOff(scheduledMeetingItem.isVoipOff());
        scheduledMeetingItem2.setTelephonyOff(scheduledMeetingItem.isTelephonyOff());
        scheduledMeetingItem2.setOtherTeleConfInfo(scheduledMeetingItem.getOtherTeleConfInfo());
        scheduledMeetingItem2.setSelfTelephoneOn(scheduledMeetingItem.isSelfTelephoneOn());
        scheduledMeetingItem2.setUsePmiAsMeetingID(scheduledMeetingItem.isUsePmiAsMeetingID());
        scheduledMeetingItem2.setOriginalMeetingNo(scheduledMeetingItem.getOriginalMeetingNo());
        scheduledMeetingItem2.setCNMeetingOn(scheduledMeetingItem.isCnMeetingOn());
        scheduledMeetingItem2.setTimeZoneId(scheduledMeetingItem.getTimeZoneId());
        scheduledMeetingItem2.setOnlySignJoin(scheduledMeetingItem.isOnlySignJoin());
        scheduledMeetingItem2.setmIsEnableMeetingToPublic(scheduledMeetingItem.ismIsEnableMeetingToPublic());
        scheduledMeetingItem2.setmIsEnableCloudRecording(scheduledMeetingItem.ismIsEnableCloudRecording());
        scheduledMeetingItem2.setmIsEnableLocalRecording(scheduledMeetingItem.ismIsEnableLocalRecording());
        scheduledMeetingItem2.setmIsEnableAudioWaterMark(scheduledMeetingItem.ismIsEnableAudioWaterMark());
        scheduledMeetingItem2.setmIsWebRecurrenceMeeting(scheduledMeetingItem.ismIsWebRecurrenceMeeting());
        scheduledMeetingItem2.setmIsEnableLanguageInterpretation(scheduledMeetingItem.isEnableLanguageInterpretation());
        scheduledMeetingItem2.setEnableWaitingRoom(scheduledMeetingItem.isEnableWaitingRoom());
        scheduledMeetingItem2.setSupportWaitingRoom(scheduledMeetingItem.isSupportWaitingRoom());
        scheduledMeetingItem2.setJbhTime(scheduledMeetingItem.getJbhTime());
        scheduledMeetingItem2.setJoinMeetingUrlForInvite(scheduledMeetingItem.getJoinMeetingUrlForInvite());
        return scheduledMeetingItem2;
    }

    @NonNull
    public static ScheduledMeetingItem fromGoogCalendarEventForNotZoomMeeting(GoogCalendarEvent googCalendarEvent) {
        ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
        scheduledMeetingItem.setmIsZoomMeeting(false);
        scheduledMeetingItem.setmIsCanStartMeetingForMySelf(false);
        scheduledMeetingItem.setTopic(googCalendarEvent.getSummary());
        scheduledMeetingItem.setJoinMeetingUrl(googCalendarEvent.getLocation());
        long stringToMilliseconds = TimeUtil.stringToMilliseconds(googCalendarEvent.getStartTime(), TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE);
        long stringToMilliseconds2 = TimeUtil.stringToMilliseconds(googCalendarEvent.getEndTime(), TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE);
        if (stringToMilliseconds > 0 && stringToMilliseconds2 > 0) {
            scheduledMeetingItem.setStartTime(stringToMilliseconds);
            scheduledMeetingItem.setDuration((int) ((stringToMilliseconds2 - stringToMilliseconds) / 60000));
        }
        scheduledMeetingItem.setMeetingNo(googCalendarEvent.getMeetNo());
        return scheduledMeetingItem;
    }

    @NonNull
    public static ScheduledMeetingItem fromGoogCalendarEvent(GoogCalendarEvent googCalendarEvent, boolean z) {
        ScheduledMeetingItem scheduledMeetingItem = new ScheduledMeetingItem();
        scheduledMeetingItem.setmIsCanStartMeetingForMySelf(z);
        scheduledMeetingItem.setTopic(googCalendarEvent.getSummary());
        scheduledMeetingItem.setJoinMeetingUrl(googCalendarEvent.getLocation());
        long stringToMilliseconds = TimeUtil.stringToMilliseconds(googCalendarEvent.getStartTime(), TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE);
        long stringToMilliseconds2 = TimeUtil.stringToMilliseconds(googCalendarEvent.getEndTime(), TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z, TimeUtil.DATE_FORMAT_YYYY_MM_DD_T_HH_MM_SS_Z_WITH_QUOTE);
        if (stringToMilliseconds > 0 && stringToMilliseconds2 > 0) {
            scheduledMeetingItem.setStartTime(stringToMilliseconds);
            scheduledMeetingItem.setDuration((int) ((stringToMilliseconds2 - stringToMilliseconds) / 60000));
        }
        scheduledMeetingItem.setMeetingNo(googCalendarEvent.getMeetNo());
        scheduledMeetingItem.setPersonalLink(googCalendarEvent.getPersonalLink());
        scheduledMeetingItem.setPassword(googCalendarEvent.getMeetPassword());
        return scheduledMeetingItem;
    }

    @NonNull
    public MeetingInfoProto toMeetingInfo() {
        Builder newBuilder = MeetingInfoProto.newBuilder();
        newBuilder.setTopic(getTopic());
        newBuilder.setStartTime(getStartTime() / 1000);
        newBuilder.setDuration(getDuration());
        newBuilder.setType(getMeetingType());
        newBuilder.setMeetingNumber(getMeetingNo());
        newBuilder.setPassword(getPassword());
        newBuilder.setId(getId());
        newBuilder.setMeetingStatus(getMeetingStatus());
        newBuilder.setInviteEmailContent(getInvitationEmailContent());
        newBuilder.setInviteEmailContentWithTime(getInvitationEmailContentWithTime());
        newBuilder.setCanJoinBeforeHost(getCanJoinBeforeHost());
        newBuilder.setRepeatType(getRepeatType());
        newBuilder.setRepeatEndTime(getRepeatEndTime() / 1000);
        newBuilder.setJoinMeetingUrl(getJoinMeetingUrl());
        newBuilder.setCallinNumber(getCallInNumber());
        newBuilder.setPSTNEnabled(isPSTNEnabled());
        newBuilder.setH323Gateway(getH323Gateway());
        newBuilder.setMeetingHostID(getHostId());
        newBuilder.setIsWebinar(ismIsWebinar());
        newBuilder.setMeetingHostName(getHostName());
        newBuilder.setExtendMeetingType(getExtendMeetingType());
        newBuilder.setHostVideoOff(isHostVideoOff());
        newBuilder.setAttendeeVideoOff(isAttendeeVideoOff());
        newBuilder.setVoipOff(isVoipOff());
        newBuilder.setTelephonyOff(isTelephonyOff());
        newBuilder.setOtherTeleConfInfo(getOtherTeleConfInfo());
        newBuilder.setIsSelfTelephonyOn(isSelfTelephoneOn());
        newBuilder.setUsePmiAsMeetingID(isUsePmiAsMeetingID());
        newBuilder.setOriginalMeetingNumber(getOriginalMeetingNo());
        newBuilder.setIsOnlySignJoin(isOnlySignJoin());
        newBuilder.setIsEnableMeetingToPublic(ismIsEnableMeetingToPublic());
        newBuilder.setIsEnableAutoRecordingCloud(ismIsEnableCloudRecording());
        newBuilder.setIsEnableAutoRecordingLocal(ismIsEnableLocalRecording());
        newBuilder.setIsEnableAudioWatermark(ismIsEnableAudioWaterMark());
        newBuilder.setIsWebRecurrenceMeeting(ismIsWebRecurrenceMeeting());
        newBuilder.setIsEnableLanguageInterpretation(isEnableLanguageInterpretation());
        newBuilder.setIsEnableWaitingRoom(isEnableWaitingRoom());
        newBuilder.setIsSupportWaitingRoom(isSupportWaitingRoom());
        newBuilder.setJbhPriorTime(getJbhTime());
        newBuilder.setJoinMeetingUrlForInvite(getJoinMeetingUrlForInvite());
        return newBuilder.build();
    }

    public boolean ismIsZoomMeeting() {
        return this.mIsZoomMeeting;
    }

    public void setmIsZoomMeeting(boolean z) {
        this.mIsZoomMeeting = z;
    }

    public boolean ismIsLabel() {
        return this.mIsLabel;
    }

    public void setmIsLabel(boolean z) {
        this.mIsLabel = z;
    }

    public boolean isHostByLabel() {
        return this.mIsHostByLabel;
    }

    public void setIsHostByLabel(boolean z) {
        this.mIsHostByLabel = z;
    }

    public String getmLabel() {
        return this.mLabel;
    }

    public void setmLabel(String str) {
        this.mLabel = str;
    }

    public boolean ismIsRecCopy() {
        return this.mIsRecCopy;
    }

    public void setmIsRecCopy(boolean z) {
        this.mIsRecCopy = z;
    }

    public long getmRecCopyStartTime() {
        return this.mRecCopyStartTime;
    }

    public void setmRecCopyStartTime(long j) {
        this.mRecCopyStartTime = j;
    }

    public String getTopic() {
        return this.mTopic;
    }

    public void setTopic(String str) {
        this.mTopic = str;
    }

    public long getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(long j) {
        this.mStartTime = j;
    }

    public long getRealStartTime() {
        if (this.mIsRecCopy) {
            return this.mRecCopyStartTime;
        }
        return this.mStartTime;
    }

    public long getMeetingNo() {
        return this.mMeetingNo;
    }

    public void setMeetingNo(long j) {
        this.mMeetingNo = j;
    }

    public String getPersonalLink() {
        return this.mPersonalLink;
    }

    public void setPersonalLink(String str) {
        this.mPersonalLink = str;
    }

    public boolean isRecurring() {
        return this.mMeetingType == MeetingType.REPEAT;
    }

    public MeetingType getMeetingType() {
        return this.mMeetingType;
    }

    public void setMeetingType(MeetingType meetingType) {
        this.mMeetingType = meetingType;
    }

    public int getDuration() {
        return this.mDuration;
    }

    public void setDuration(int i) {
        this.mDuration = i;
    }

    public String getPassword() {
        return this.mPassword;
    }

    public void setPassword(String str) {
        this.mPassword = str;
    }

    public boolean hasPassword() {
        return !StringUtil.isEmptyOrNull(getPassword());
    }

    public String getId() {
        return this.mId;
    }

    public void setId(String str) {
        this.mId = str;
    }

    public int getMeetingStatus() {
        return this.mMeetingStatus;
    }

    public void setMeetingStatus(int i) {
        this.mMeetingStatus = i;
    }

    public String getInvitationEmailContent() {
        return this.mInvitationEmailContent;
    }

    public String getInvitationEmailContentWithTime() {
        return this.mInvitationEmailContentWithTime;
    }

    public void setInvitationEmailContent(String str) {
        this.mInvitationEmailContent = str;
    }

    public void setInvitationEmailContentWithTime(String str) {
        this.mInvitationEmailContentWithTime = str;
    }

    public boolean getCanJoinBeforeHost() {
        return this.mCanJoinBeforeHost;
    }

    public void setCanJoinBeforeHost(boolean z) {
        this.mCanJoinBeforeHost = z;
    }

    public void setRepeatType(int i) {
        this.mRepeatType = i;
    }

    public int getRepeatType() {
        return this.mRepeatType;
    }

    public void setRepeatEndTime(long j) {
        this.mRepeatEndTime = j;
    }

    public long getRepeatEndTime() {
        return this.mRepeatEndTime;
    }

    @Nullable
    public String getJoinMeetingUrl() {
        return this.mJoinMeetingUrl;
    }

    public void setJoinMeetingUrl(@Nullable String str) {
        this.mJoinMeetingUrl = str;
    }

    @Nullable
    public String getCallInNumber() {
        return this.mCallInNumber;
    }

    public void setCallInNumber(@Nullable String str) {
        this.mCallInNumber = str;
    }

    @Nullable
    public String getH323Gateway() {
        return this.mH323Gateway;
    }

    @Nullable
    public String[] getH323Gateways() {
        if (StringUtil.isEmptyOrNull(this.mH323Gateway)) {
            return null;
        }
        return this.mH323Gateway.split(";");
    }

    public void setH323Gateway(@Nullable String str) {
        this.mH323Gateway = str;
    }

    public boolean isPSTNEnabled() {
        return this.mPSTNEnabled;
    }

    public void setPSTNEnabled(boolean z) {
        this.mPSTNEnabled = z;
    }

    @Nullable
    public String getHostName() {
        return this.mHostName;
    }

    public void setHostName(@Nullable String str) {
        this.mHostName = str;
    }

    @Nullable
    public String getHostId() {
        return this.mHostId;
    }

    public void setHostId(@Nullable String str) {
        this.mHostId = str;
    }

    @Nullable
    public String getHostEmail() {
        return this.mHostEmail;
    }

    public void setHostEmail(@Nullable String str) {
        this.mHostEmail = str;
    }

    public boolean isShareOnlyMeeting() {
        return this.mIsShareOnlyMeeting;
    }

    public void setIsShareOnlyMeeting(boolean z) {
        this.mIsShareOnlyMeeting = z;
    }

    public boolean ismIsWebinar() {
        return this.mIsWebinar;
    }

    public void setmIsWebinar(boolean z) {
        this.mIsWebinar = z;
    }

    public int getExtendMeetingType() {
        return this.mExtendMeetingType;
    }

    public void setExtendMeetingType(int i) {
        this.mExtendMeetingType = i;
    }

    public boolean isHostVideoOff() {
        return this.mHostVideoOff;
    }

    public void setHostVideoOff(boolean z) {
        this.mHostVideoOff = z;
    }

    public boolean isAttendeeVideoOff() {
        return this.mAttendeeVideoOff;
    }

    public void setAttendeeVideoOff(boolean z) {
        this.mAttendeeVideoOff = z;
    }

    public boolean isVoipOff() {
        return this.mVoipOff;
    }

    public void setVoipOff(boolean z) {
        this.mVoipOff = z;
    }

    public boolean isTelephonyOff() {
        return this.mTelephonyOff;
    }

    public void setTelephonyOff(boolean z) {
        this.mTelephonyOff = z;
    }

    public String getOtherTeleConfInfo() {
        return this.mOtherTeleConfInfo;
    }

    public void setOtherTeleConfInfo(String str) {
        this.mOtherTeleConfInfo = str;
    }

    public boolean isSelfTelephoneOn() {
        return this.mSelfTelephoneOn;
    }

    public void setSelfTelephoneOn(boolean z) {
        this.mSelfTelephoneOn = z;
    }

    public boolean isUsePmiAsMeetingID() {
        return this.mUsePmiAsMeetingID;
    }

    public void setUsePmiAsMeetingID(boolean z) {
        this.mUsePmiAsMeetingID = z;
    }

    public long getOriginalMeetingNo() {
        return this.mOriginalMeetingNo;
    }

    public void setOriginalMeetingNo(long j) {
        this.mOriginalMeetingNo = j;
    }

    public boolean isCnMeetingOn() {
        return this.mIsCNMeetingOn;
    }

    public void setCNMeetingOn(boolean z) {
        this.mIsCNMeetingOn = z;
    }

    public void setTimeZoneId(String str) {
        this.mTimeZoneId = str;
    }

    public String getTimeZoneId() {
        return this.mTimeZoneId;
    }

    public boolean isOnlySignJoin() {
        return this.mOnlySignJoin;
    }

    public void setOnlySignJoin(boolean z) {
        this.mOnlySignJoin = z;
    }

    public boolean ismIsEnableMeetingToPublic() {
        return this.mIsEnableMeetingToPublic;
    }

    public void setmIsEnableMeetingToPublic(boolean z) {
        this.mIsEnableMeetingToPublic = z;
    }

    public boolean ismIsEnableLocalRecording() {
        return this.mIsEnableLocalRecording;
    }

    public void setmIsEnableLocalRecording(boolean z) {
        this.mIsEnableLocalRecording = z;
    }

    public boolean ismIsEnableCloudRecording() {
        return this.mIsEnableCloudRecording;
    }

    public void setmIsEnableCloudRecording(boolean z) {
        this.mIsEnableCloudRecording = z;
    }

    public boolean ismIsEnableAudioWaterMark() {
        return this.mIsEnableAudioWaterMark;
    }

    public void setmIsEnableAudioWaterMark(boolean z) {
        this.mIsEnableAudioWaterMark = z;
    }

    public boolean isEnableLanguageInterpretation() {
        return this.mIsEnableLanguageInterpretation;
    }

    public void setmIsEnableLanguageInterpretation(boolean z) {
        this.mIsEnableLanguageInterpretation = z;
    }

    public boolean isEnableWaitingRoom() {
        return this.mIsEnableWaitingRoom;
    }

    public void setEnableWaitingRoom(boolean z) {
        this.mIsEnableWaitingRoom = z;
    }

    public boolean isSupportWaitingRoom() {
        return this.mIsSupportWaitingRoom;
    }

    public void setSupportWaitingRoom(boolean z) {
        this.mIsSupportWaitingRoom = z;
    }

    public int getJbhTime() {
        return this.mJbhTime;
    }

    public void setJbhTime(int i) {
        this.mJbhTime = i;
    }

    public String getJoinMeetingUrlForInvite() {
        return this.mJoinMeetingUrlForInvite;
    }

    public void setJoinMeetingUrlForInvite(String str) {
        this.mJoinMeetingUrlForInvite = str;
    }

    public boolean ismIsCanStartMeetingForMySelf() {
        return this.mIsCanStartMeetingForMySelf;
    }

    public void setmIsCanStartMeetingForMySelf(boolean z) {
        this.mIsCanStartMeetingForMySelf = z;
    }

    public boolean ismIsWebRecurrenceMeeting() {
        return this.mIsWebRecurrenceMeeting;
    }

    public void setmIsWebRecurrenceMeeting(boolean z) {
        this.mIsWebRecurrenceMeeting = z;
    }

    public View getView(@NonNull Context context, View view, ViewGroup viewGroup) {
        if (this.mIsLabel) {
            if (view == null || !"label".equals(view.getTag())) {
                view = LayoutInflater.from(context).inflate(C4558R.layout.zm_item_schedule_label, viewGroup, false);
                view.setTag("label");
            }
            ((TextView) view.findViewById(C4558R.C4560id.txtLabel)).setText(this.mLabel);
            return view;
        } else if (this.mIsHostByLabel) {
            if (view == null || !"hostByLabel".equals(view.getTag())) {
                view = LayoutInflater.from(context).inflate(C4558R.layout.zm_item_schedule_host_by_label, viewGroup, false);
                view.setTag("hostByLabel");
            }
            bindHostByLabelView(context, view);
            return view;
        } else {
            int i = this.mExtendMeetingType;
            if (i == 1) {
                if (view == null || !"pmi".equals(view.getTag())) {
                    view = LayoutInflater.from(context).inflate(C4558R.layout.zm_scheduled_meeting_item_pmi, viewGroup, false);
                    view.setTag("pmi");
                }
                bindViewPMI(context, view);
                return view;
            } else if (i != -999) {
                if (view == null || !"meetingItem".equals(view.getTag())) {
                    view = LayoutInflater.from(context).inflate(C4558R.layout.zm_item_schedule_meeting, viewGroup, false);
                    view.setTag("meetingItem");
                }
                bindView(context, view);
                return view;
            } else if (view != null && "meetingActionItem".equals(view.getTag())) {
                return view;
            } else {
                View inflate = LayoutInflater.from(context).inflate(C4558R.layout.zm_schedule_meeting_add_calendar_item, viewGroup, false);
                inflate.setTag("meetingActionItem");
                return inflate;
            }
        }
    }

    private void bindHostByLabelView(@NonNull final Context context, View view) {
        View findViewById = view.findViewById(C4558R.C4560id.hostByView);
        final TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtLabel);
        final String meetingListLastDisplayedHostId = PTApp.getInstance().getMeetingListLastDisplayedHostId();
        String altHostNameById = ZMScheduleUtil.getAltHostNameById(context, meetingListLastDisplayedHostId);
        String string = context.getString(C4558R.string.zm_lbl_host_by_title_101105, new Object[]{altHostNameById});
        findViewById.setContentDescription(context.getString(C4558R.string.zm_accessibility_host_by_btn_101105, new Object[]{altHostNameById}));
        textView.setText(string);
        findViewById.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Context context = context;
                if (context instanceof ZMActivity) {
                    ChooseHostDialog.show(((ZMActivity) context).getSupportFragmentManager(), meetingListLastDisplayedHostId, new OnItemSelectedListener() {
                        public void onItemSelected(MeetingHostByItem meetingHostByItem) {
                            MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
                            if (meetingHelper != null) {
                                meetingHelper.setFilterPerson(meetingHostByItem.getPersonId());
                                textView.setText(context.getString(C4558R.string.zm_lbl_host_by_title_101105, new Object[]{meetingHostByItem.getLabel()}));
                                PTApp.getInstance().refreshMeetingListLastDisplayedHostIdFromDb();
                            }
                        }
                    });
                }
            }
        });
    }

    private void bindView(@NonNull Context context, View view) {
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtTime);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtMeetingId);
        Button button = (Button) view.findViewById(C4558R.C4560id.btnStart);
        ImageView imageView = (ImageView) view.findViewById(C4558R.C4560id.ivArrow);
        ((TextView) view.findViewById(C4558R.C4560id.txtTopic)).setText(getTopic());
        if (!isRecurring() || ismIsRecCopy()) {
            textView.setVisibility(0);
            if (textView.isInEditMode()) {
                textView.setText("2012/11/22 10:00 am");
                textView.setTextColor(context.getResources().getColor(C4558R.color.zm_meetinglistitem_time_normal));
            } else {
                String formateHourAmPm = TimeUtil.formateHourAmPm(getRealStartTime());
                if (!StringUtil.isEmptyOrNull(formateHourAmPm)) {
                    textView.setText(formateHourAmPm.replace(OAuth.SCOPE_DELIMITER, FontStyleHelper.SPLITOR));
                } else {
                    textView.setVisibility(4);
                }
            }
        } else {
            textView.setVisibility(4);
        }
        if (!this.mIsZoomMeeting) {
            textView2.setText(C4558R.string.zm_description_not_zoom_meeting_63007);
            button.setVisibility(8);
            return;
        }
        int i = C4558R.string.zm_lbl_meeting_id;
        if (this.mIsWebinar && !this.mIsWebRecurrenceMeeting) {
            i = C4558R.string.zm_lbl_webinar_id_75475;
        }
        long j = 0;
        if (this.mMeetingNo != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getText(i));
            sb.append(OAuth.SCOPE_DELIMITER);
            sb.append(StringUtil.formatConfNumber(this.mMeetingNo));
            textView2.setText(sb.toString());
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(context.getText(i));
            sb2.append(OAuth.SCOPE_DELIMITER);
            sb2.append(this.mPersonalLink);
            textView2.setText(sb2.toString());
        }
        if (!view.isInEditMode()) {
            j = PTApp.getInstance().getActiveMeetingNo();
        }
        String activeCallId = view.isInEditMode() ? "" : PTApp.getInstance().getActiveCallId();
        int callStatus = PTApp.getInstance().getCallStatus();
        if (j != getMeetingNo() && (activeCallId == null || !activeCallId.equals(getId()))) {
            button.setText(C4558R.string.zm_btn_start);
        } else if (callStatus == 2) {
            button.setText(C4558R.string.zm_btn_back);
        } else {
            button.setText(C4558R.string.zm_btn_start);
        }
        if (!this.mIsCanStartMeetingForMySelf) {
            button.setVisibility(8);
            imageView.setVisibility(0);
        } else {
            button.setVisibility(0);
            imageView.setVisibility(8);
        }
        boolean z = true;
        if (callStatus == 1) {
            z = false;
        }
        button.setEnabled(z);
        button.setOnClickListener(this);
    }

    private void bindViewPMI(Context context, View view) {
        TextView textView = (TextView) view.findViewById(C4558R.C4560id.txtMeetingNo);
        Button button = (Button) view.findViewById(C4558R.C4560id.btnStart);
        Button button2 = (Button) view.findViewById(C4558R.C4560id.btnInvite);
        Button button3 = (Button) view.findViewById(C4558R.C4560id.btnEdit);
        TextView textView2 = (TextView) view.findViewById(C4558R.C4560id.txtVanityURL);
        long meetingNo = getMeetingNo();
        textView.setText(StringUtil.formatConfNumber(meetingNo, String.valueOf(meetingNo).length() > 10 ? ResourcesUtil.getInteger(context, C4558R.integer.zm_config_long_meeting_id_format_type, 0) : 0));
        long activeMeetingNo = view.isInEditMode() ? 0 : PTApp.getInstance().getActiveMeetingNo();
        String activeCallId = view.isInEditMode() ? "" : PTApp.getInstance().getActiveCallId();
        int callStatus = PTApp.getInstance().getCallStatus();
        if (activeMeetingNo != getMeetingNo() && (activeCallId == null || !activeCallId.equals(getId()))) {
            button.setText(C4558R.string.zm_btn_start);
        } else if (callStatus == 2) {
            button.setText(C4558R.string.zm_btn_back);
        } else {
            button.setText(C4558R.string.zm_btn_start);
        }
        String str = null;
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile != null) {
            str = currentUserProfile.getPMIVanityURL();
        }
        if (StringUtil.isEmptyOrNull(str)) {
            textView2.setVisibility(8);
        } else {
            textView2.setVisibility(0);
            textView2.setText(str);
        }
        boolean z = true;
        if (callStatus == 1) {
            z = false;
        }
        button.setEnabled(z);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    public void onClick(@NonNull View view) {
        int id = view.getId();
        if (id == C4558R.C4560id.btnStart) {
            onClickBtnStart(view.getContext());
        } else if (id == C4558R.C4560id.btnInvite) {
            onClickBtnInvite(view.getContext());
        } else if (id == C4558R.C4560id.btnEdit) {
            onClickBtnEdit(view);
        }
    }

    private void onClickBtnStart(@NonNull Context context) {
        checkJoinMeeting(context);
    }

    private void checkJoinMeeting(@NonNull final Context context) {
        MeetingInSipCallConfirmDialog.checkExistingSipCallAndIfNeedShow(context, new SimpleOnButtonClickListener() {
            public void onPositiveClick() {
                ScheduledMeetingItem.this.onMeeting(context);
            }
        });
    }

    /* access modifiers changed from: private */
    public void onMeeting(@NonNull Context context) {
        if (this.mIsCanStartMeetingForMySelf) {
            if (ConfActivity.startMeeting((ZMActivity) context, this.mMeetingNo, this.mId)) {
                ZMConfEventTracking.logStartMeetingInUpcomingMeeting(this);
            }
        } else if (context instanceof ZMActivity) {
            ConfActivity.checkExistingCallAndJoinMeeting((ZMActivity) context, getMeetingNo(), getId(), getPersonalLink(), getPassword());
        }
    }

    private void onClickBtnInvite(@NonNull Context context) {
        sendInvitations(context, -1);
    }

    private void sendInvitations(@NonNull Context context, int i) {
        String str;
        String buildEmailInvitationContent = MeetingInvitationUtil.buildEmailInvitationContent(context, this, true);
        String string = context.getString(C4558R.string.zm_title_meeting_invitation_email_topic, new Object[]{getTopic()});
        String string2 = context.getString(C4558R.string.zm_lbl_add_invitees);
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            setInvitationEmailContentWithTime(MeetingInvitationUtil.buildEmailInvitationContent(context, this, true));
            MeetingInfoProto meetingInfo = toMeetingInfo();
            EventRepeatType zoomRepeatTypeToNativeRepeatType = zoomRepeatTypeToNativeRepeatType(this.mRepeatType);
            if (this.mExtendMeetingType != 1 && (!isRecurring() || zoomRepeatTypeToNativeRepeatType != EventRepeatType.NONE)) {
                String[] strArr = {context.getString(C4558R.string.zm_meeting_invitation_ics_name)};
                if (meetingHelper.createIcsFileFromMeeting(meetingInfo, strArr, TimeZone.getDefault().getID())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("file://");
                    sb.append(strArr[0]);
                    str = sb.toString();
                    String joinMeetingUrl = getJoinMeetingUrl();
                    long meetingNo = getMeetingNo();
                    HashMap hashMap = new HashMap();
                    hashMap.put("joinMeetingUrl", joinMeetingUrl);
                    hashMap.put(InviteFragment.ARG_MEETING_ID, String.valueOf(meetingNo));
                    Context context2 = context;
                    ZMSendMessageFragment.show(context2, ((ZMActivity) context).getSupportFragmentManager(), null, null, string, buildEmailInvitationContent, new Template(context.getString(C4558R.string.zm_msg_sms_invite_scheduled_meeting)).format(hashMap), str, string2, i);
                }
            }
        }
        str = null;
        String joinMeetingUrl2 = getJoinMeetingUrl();
        long meetingNo2 = getMeetingNo();
        HashMap hashMap2 = new HashMap();
        hashMap2.put("joinMeetingUrl", joinMeetingUrl2);
        hashMap2.put(InviteFragment.ARG_MEETING_ID, String.valueOf(meetingNo2));
        Context context22 = context;
        ZMSendMessageFragment.show(context22, ((ZMActivity) context).getSupportFragmentManager(), null, null, string, buildEmailInvitationContent, new Template(context.getString(C4558R.string.zm_msg_sms_invite_scheduled_meeting)).format(hashMap2), str, string2, i);
    }

    private void onClickBtnEdit(View view) {
        ZMActivity zMActivity = (ZMActivity) view.getContext();
        if (zMActivity != null) {
            SimpleActivity.show(zMActivity, ZMPMIEditFragment.class.getName(), (Bundle) null, 0, true);
        }
    }
}
