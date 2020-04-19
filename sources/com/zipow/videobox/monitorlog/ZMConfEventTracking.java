package com.zipow.videobox.monitorlog;

import androidx.annotation.NonNull;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.meeting.confhelper.ShareOptionType;
import com.zipow.videobox.monitorlog.MonitorLogEvent.MonitorLogEventBuilder;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto.MeetingType;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.util.ZmPtUtils;
import com.zipow.videobox.view.ScheduledMeetingItem;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.GalleryVideoScene;
import com.zipow.videobox.view.video.NormalVideoScene;
import p021us.zoom.androidlib.util.StringUtil;

public class ZMConfEventTracking {
    public static final String TAG_WEB_GOOGLE_CALENDAR = "web google calendar";

    public static void logBackToMeeting() {
        new MonitorLogEventBuilder().newBasicInfo(0, 0, 2).putBooleanAttribute(12, true).create().write();
    }

    public static void logStartMeetingInShortCut(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        new MonitorLogEventBuilder().newBasicInfo(0, 0, 0, 3).putBooleanAttribute(12, false).putBooleanAttribute(18, scheduledMeetingItem.isRecurring()).putBooleanAttribute(19, !scheduledMeetingItem.isHostVideoOff()).putBooleanAttribute(20, !scheduledMeetingItem.isAttendeeVideoOff()).putBooleanAttribute(25, scheduledMeetingItem.getCanJoinBeforeHost()).putBooleanAttribute(27, scheduledMeetingItem.isUsePmiAsMeetingID()).putBooleanAttribute(22, ZmPtUtils.isRequiredPasswordForUpdateMeeting(scheduledMeetingItem.getCanJoinBeforeHost(), scheduledMeetingItem.isUsePmiAsMeetingID())).putIntAttribute(21, ZmPtUtils.getMeetingDefaultAudioOption(PTApp.getInstance().getCurrentUserProfile(), scheduledMeetingItem)).putLongAttribute(15, scheduledMeetingItem.getStartTime()).putLongAttribute(16, (long) scheduledMeetingItem.getDuration()).putStringAttribute(1, scheduledMeetingItem.getTimeZoneId()).create().write();
    }

    public static void logStartMeetingInUpcomingMeeting(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        new MonitorLogEventBuilder().newBasicInfo(0, 0, 0, 4).putBooleanAttribute(12, false).putBooleanAttribute(18, scheduledMeetingItem.isRecurring()).putBooleanAttribute(19, !scheduledMeetingItem.isHostVideoOff()).putBooleanAttribute(20, !scheduledMeetingItem.isAttendeeVideoOff()).putBooleanAttribute(25, scheduledMeetingItem.getCanJoinBeforeHost()).putBooleanAttribute(27, scheduledMeetingItem.isUsePmiAsMeetingID()).putBooleanAttribute(22, ZmPtUtils.isRequiredPasswordForUpdateMeeting(scheduledMeetingItem.getCanJoinBeforeHost(), scheduledMeetingItem.isUsePmiAsMeetingID())).putIntAttribute(21, ZmPtUtils.getMeetingDefaultAudioOption(PTApp.getInstance().getCurrentUserProfile(), scheduledMeetingItem)).putLongAttribute(15, scheduledMeetingItem.getStartTime()).putLongAttribute(16, (long) scheduledMeetingItem.getDuration()).putStringAttribute(1, scheduledMeetingItem.getTimeZoneId()).create().write();
    }

    public static void logStartMeetingInShortCut(long j) {
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper != null) {
            MeetingInfoProto meetingItemByNumber = meetingHelper.getMeetingItemByNumber(j);
            if (meetingItemByNumber != null) {
                boolean z = false;
                MonitorLogEventBuilder putBooleanAttribute = new MonitorLogEventBuilder().newBasicInfo(0, 0, 0, 3).putBooleanAttribute(12, false);
                if (meetingItemByNumber.getType() == MeetingType.REPEAT) {
                    z = true;
                }
                putBooleanAttribute.putBooleanAttribute(18, z).putBooleanAttribute(19, !meetingItemByNumber.getHostVideoOff()).putBooleanAttribute(20, !meetingItemByNumber.getAttendeeVideoOff()).putBooleanAttribute(25, meetingItemByNumber.getCanJoinBeforeHost()).putBooleanAttribute(27, meetingItemByNumber.getUsePmiAsMeetingID()).putBooleanAttribute(22, ZmPtUtils.isRequiredPasswordForUpdateMeeting(meetingItemByNumber.getCanJoinBeforeHost(), meetingItemByNumber.getUsePmiAsMeetingID())).putIntAttribute(21, ZmPtUtils.getMeetingDefaultAudioOption(PTApp.getInstance().getCurrentUserProfile(), meetingItemByNumber)).putLongAttribute(15, meetingItemByNumber.getStartTime()).putLongAttribute(16, (long) meetingItemByNumber.getDuration()).putStringAttribute(1, meetingItemByNumber.getTimeZoneId()).create().write();
            }
        }
    }

    public static void logStartMeeting(boolean z, boolean z2) {
        new MonitorLogEventBuilder().newBasicInfo(0, 0, 0, z ^ true ? 1 : 0).putBooleanAttribute(12, false).putBooleanAttribute(14, z).putBooleanAttribute(27, z2).create().write();
    }

    public static void logJoinMeeting(boolean z, boolean z2, boolean z3) {
        new MonitorLogEventBuilder().newBasicInfo(0, z ? 0 : 2, 0, 2).putBooleanAttribute(12, false).putBooleanAttribute(13, z2).putBooleanAttribute(14, z3).create().write();
    }

    public static void logScheduleMeetingOnSuccess(@NonNull MeetingInfoProto meetingInfoProto, String str) {
        boolean z = false;
        MonitorLogEventBuilder putBooleanAttribute = new MonitorLogEventBuilder().newBasicInfo(0, 0, 1).putBooleanAttribute(12, false);
        if (meetingInfoProto.getType() == MeetingType.REPEAT) {
            z = true;
        }
        MonitorLogEventBuilder putStringAttribute = putBooleanAttribute.putBooleanAttribute(18, z).putBooleanAttribute(19, !meetingInfoProto.getHostVideoOff()).putBooleanAttribute(20, !meetingInfoProto.getAttendeeVideoOff()).putBooleanAttribute(25, meetingInfoProto.getCanJoinBeforeHost()).putBooleanAttribute(27, meetingInfoProto.getUsePmiAsMeetingID()).putBooleanAttribute(22, ZmPtUtils.isRequiredPasswordForUpdateMeeting(meetingInfoProto.getCanJoinBeforeHost(), meetingInfoProto.getUsePmiAsMeetingID())).putIntAttribute(21, ZmPtUtils.getMeetingDefaultAudioOption(PTApp.getInstance().getCurrentUserProfile(), meetingInfoProto)).putLongAttribute(15, meetingInfoProto.getStartTime()).putLongAttribute(16, (long) meetingInfoProto.getDuration()).putStringAttribute(1, meetingInfoProto.getTimeZoneId());
        if (!StringUtil.isEmptyOrNull(str)) {
            putStringAttribute.putStringAttribute(26, str);
        }
        putStringAttribute.create().write();
    }

    public static void logToggleVideo(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 1, 4, z ? 6 : 7).putBooleanAttribute(12, true).create().write();
    }

    public static void logToggleAudio(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 1, 4, z ? 8 : 9).putBooleanAttribute(12, true).create().write();
    }

    public static void logRecord(boolean z, boolean z2) {
        new MonitorLogEventBuilder().newBasicInfo(0, 1, 8, z ? 15 : 16).putBooleanAttribute(35, z2).create().write();
    }

    public static void logInviteToMeeting(int i) {
        new MonitorLogEventBuilder().newBasicInfo(0, 1, 5, i).create().write();
    }

    public static void logShareInMeeting(@NonNull ShareOptionType shareOptionType) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (audioStatusObj != null) {
                long audiotype = audioStatusObj.getAudiotype();
                boolean z = true;
                MonitorLogEventBuilder newBasicInfo = new MonitorLogEventBuilder().newBasicInfo(0, 1, 10, 19);
                if (audiotype == 2) {
                    z = false;
                }
                newBasicInfo.putBooleanAttribute(13, z).putStringAttribute(38, shareOptionType.toString()).create().write();
            }
        }
    }

    public static void logSwitchModeViewInMeeting(AbsVideoScene absVideoScene) {
        int i = absVideoScene instanceof NormalVideoScene ? 17 : absVideoScene instanceof GalleryVideoScene ? 18 : -1;
        if (i != -1) {
            new MonitorLogEventBuilder().newBasicInfo(0, 1, 9, i).create().write();
        }
    }

    public static void logAppDisclaimer(int i) {
        new MonitorLogEventBuilder().newBasicInfo(0, 0, 49, i).create().write();
    }

    public static void logRecordingDisclaimer(int i) {
        new MonitorLogEventBuilder().newBasicInfo(0, 1, 50, i).create().write();
    }

    private static String getIsWebinarAttr() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        return (confContext == null || !confContext.isWebinar()) ? "0" : "1";
    }

    public static void eventTrackInMeetingSettingLockMeeting(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 51, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingEnableWaitingRoom(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 52, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingShareScreen(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 10, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingRenameThemselves(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 53, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingShareContentWatermark(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 54, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingAnnotate(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 55, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingShowAnnotatorNames(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 56, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingChatWith(int i) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 29, i).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingStartVideo(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 57, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackInMeetingSettingRaiseHand(boolean z) {
        new MonitorLogEventBuilder().newBasicInfo(0, 11, 58, z ? 51 : 52).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackParticipantsPanelInviteToMeeting() {
        new MonitorLogEventBuilder().newBasicInfo(0, 13, 5, 59).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }

    public static void eventTrackParticipantMenuRemove() {
        new MonitorLogEventBuilder().newBasicInfo(0, 12, 59, 59).putStringAttribute(62, getIsWebinarAttr()).create().write();
    }
}
