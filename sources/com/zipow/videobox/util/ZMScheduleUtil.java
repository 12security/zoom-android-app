package com.zipow.videobox.util;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.confapp.meeting.PasswordItem;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.LoginMeetingAuthProto;
import com.zipow.videobox.ptapp.PTAppProtos.LoginMeetingAuthProtoList;
import com.zipow.videobox.ptapp.PTUserProfile;
import com.zipow.videobox.ptapp.dataitem.LoginMeetingAuthItem;
import com.zipow.videobox.view.ScheduledMeetingItem;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;

public class ZMScheduleUtil {
    public static final String ARG_DELETED_METHOD_AUTH_ID = "extra_deleted_method_auth_id";
    public static final String ARG_DOMAIN_EDIT_LOCK = "extra_domain_edit_lock";
    public static final String ARG_JOIN_USER_TYPE = "extra_join_user_type";
    public static final String ARG_MEETING_AUTH_ID = "extra_meeting_auth_id";
    public static final String ARG_MEETING_AUTH_ITEM = "extra_meeting_auth_item";
    public static final String ARG_MEETING_AUTH_LIST = "extra_meeting_auth_list";
    public static final String ARG_SPECIFIED_DOMAINS = "extra_specified_domains";
    public static final int JBH_TIME_DEFAULT = 5;
    public static final int REQUEST_ADD_CALENDAR_EVENT = 2002;
    public static final int REQUEST_EDIT_DOMAINS = 2007;
    public static final int REQUEST_JOIN_USER_TYPE = 2001;
    public static final int REQUEST_PICK_TIME_ZONE = 2000;
    public static final int REQUEST_SELECT_ALTERNATIVE_HOST = 2004;
    public static final int REQUEST_SELECT_AUDIO_OPTION = 2005;
    public static final int REQUEST_SELECT_JBH_TIME = 2006;
    public static final int REQUEST_UPDATE_CALENDAR_EVENT = 2003;

    public static boolean isUsePmi(@Nullable PTUserProfile pTUserProfile) {
        if (pTUserProfile == null) {
            return false;
        }
        return PreferenceUtil.readBooleanValue(PreferenceUtil.SCHEDULE_OPT_USE_PMI, pTUserProfile.isDefaultScheduleUsePMI());
    }

    public static String getAltHostNameById(@NonNull Context context, @NonNull String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return context.getString(C4558R.string.zm_lbl_everyone_101105);
        }
        if (PTApp.getInstance().isWebSignedOn()) {
            PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
            if (currentUserProfile != null && str.equalsIgnoreCase(currentUserProfile.getUserID())) {
                return context.getString(C4558R.string.zm_lbl_content_me);
            }
        }
        PTApp instance = PTApp.getInstance();
        int altHostCount = instance.getAltHostCount();
        for (int i = 0; i < altHostCount; i++) {
            AlterHost altHostAt = instance.getAltHostAt(i);
            if (altHostAt != null && str.equalsIgnoreCase(altHostAt.getHostID())) {
                return StringUtil.formatPersonName(altHostAt.getFirstName(), altHostAt.getLastName(), PTApp.getInstance().getRegionCodeForNameFormating());
            }
        }
        return context.getString(C4558R.string.zm_lbl_everyone_101105);
    }

    @NonNull
    public static String getAltHostEmailById(@NonNull String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        PTApp instance = PTApp.getInstance();
        int altHostCount = instance.getAltHostCount();
        for (int i = 0; i < altHostCount; i++) {
            AlterHost altHostAt = instance.getAltHostAt(i);
            if (altHostAt != null && str.equalsIgnoreCase(altHostAt.getHostID())) {
                return altHostAt.getEmail();
            }
        }
        return "";
    }

    public static boolean isDiffAlterList(List<AlterHost> list, List<AlterHost> list2) {
        if (list != null && list2 == null) {
            return true;
        }
        if (list == null && list2 != null) {
            return true;
        }
        if (list == null) {
            return false;
        }
        if (list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!((AlterHost) list.get(i)).getEmail().equalsIgnoreCase(((AlterHost) list2.get(i)).getEmail())) {
                return true;
            }
        }
        return false;
    }

    public static boolean isDiffStringList(List<String> list, List<String> list2) {
        if (list != null && list2 == null) {
            return true;
        }
        if (list == null && list2 != null) {
            return true;
        }
        if (list == null) {
            return false;
        }
        if (list.size() != list2.size()) {
            return true;
        }
        for (int i = 0; i < list.size(); i++) {
            if (!StringUtil.isSameString((String) list.get(i), (String) list2.get(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEnableWaitingRoomInitalInCreate(@Nullable PTUserProfile pTUserProfile) {
        if (pTUserProfile == null) {
            return false;
        }
        if (pTUserProfile.isLockWaitingRoom()) {
            return pTUserProfile.isEnableWaitingRoom();
        }
        return PreferenceUtil.readBooleanValue(PreferenceUtil.SCHEDULE_OPT_ENABLE_WAITING_ROOM, pTUserProfile.isEnableWaitingRoom());
    }

    public static boolean isEnableWaitingRoomInitalInEdit(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (scheduledMeetingItem.isSupportWaitingRoom()) {
            return scheduledMeetingItem.isEnableWaitingRoom();
        }
        return pTUserProfile.isEnableWaitingRoom();
    }

    public static boolean isHostVideoOnInitalInCreate(@Nullable PTUserProfile pTUserProfile) {
        if (pTUserProfile == null) {
            return false;
        }
        if (pTUserProfile.isLockHostVideo()) {
            return pTUserProfile.alwaysTurnOnHostVideoByDefault();
        }
        return PreferenceUtil.readBooleanValue(PreferenceUtil.SCHEDULE_OPT_HOST_VIDEO_ON, pTUserProfile.alwaysTurnOnHostVideoByDefault());
    }

    public static boolean isHostVideoOnInitalInEdit(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (pTUserProfile.isLockHostVideo()) {
            return pTUserProfile.alwaysTurnOnHostVideoByDefault();
        }
        return !scheduledMeetingItem.isHostVideoOff();
    }

    public static boolean isAttendeeVideoOnInitalInCreate(@Nullable PTUserProfile pTUserProfile) {
        if (pTUserProfile == null) {
            return false;
        }
        if (pTUserProfile.isLockParticipants()) {
            return pTUserProfile.alwaysTurnOnAttendeeVideoByDefault();
        }
        return PreferenceUtil.readBooleanValue(PreferenceUtil.SCHEDULE_OPT_ATTENDEE_VIDEO_ON, pTUserProfile.alwaysTurnOnAttendeeVideoByDefault());
    }

    public static boolean isAttendeeVideoOnInitalInEdit(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (pTUserProfile.isLockParticipants()) {
            return pTUserProfile.alwaysTurnOnAttendeeVideoByDefault();
        }
        return !scheduledMeetingItem.isAttendeeVideoOff();
    }

    public static boolean isEnableJBHInitalInCreate(@Nullable PTUserProfile pTUserProfile) {
        if (pTUserProfile == null) {
            return false;
        }
        if (pTUserProfile.isLockJoinBeforeHost()) {
            return pTUserProfile.alwaysEnableJoinBeforeHostByDefault();
        }
        return PreferenceUtil.readBooleanValue(PreferenceUtil.SCHEDULE_OPT_JBH, pTUserProfile.alwaysEnableJoinBeforeHostByDefault());
    }

    public static boolean isEnableJBHInitalInEdit(@NonNull PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (pTUserProfile.isLockJoinBeforeHost()) {
            return pTUserProfile.alwaysEnableJoinBeforeHostByDefault();
        }
        return scheduledMeetingItem.getCanJoinBeforeHost();
    }

    public static boolean isDefaultEnableOnlyAuthUsersCanJoin(@Nullable PTUserProfile pTUserProfile) {
        if (pTUserProfile == null) {
            return false;
        }
        if (pTUserProfile.isLockOnlyAuthUsersCanJoin()) {
            return pTUserProfile.isDefaultEnableOnlyAuthUsersCanJoin();
        }
        if (!PreferenceUtil.containsKey(PreferenceUtil.SCHEDULE_OPT_JOIN_USER_TYPE)) {
            return pTUserProfile.isDefaultEnableOnlyAuthUsersCanJoin();
        }
        if (PreferenceUtil.readIntValue(PreferenceUtil.SCHEDULE_OPT_JOIN_USER_TYPE, 1) == 1) {
            return false;
        }
        return true;
    }

    public static boolean isDefaultEnableOnlyAuthUsersCanJoinInEdit(@Nullable PTUserProfile pTUserProfile, @NonNull ScheduledMeetingItem scheduledMeetingItem) {
        if (pTUserProfile == null) {
            return false;
        }
        if (pTUserProfile.isLockOnlyAuthUsersCanJoin()) {
            return pTUserProfile.isDefaultEnableOnlyAuthUsersCanJoin();
        }
        return scheduledMeetingItem.isOnlySignJoin();
    }

    @NonNull
    public static ArrayList<CharSequence> getDomainListFromStr(@NonNull String str) {
        String[] split;
        ArrayList<CharSequence> arrayList = new ArrayList<>();
        for (String str2 : str.split(PreferencesConstants.COOKIE_DELIMITER)) {
            if (!TextUtils.isEmpty(str2)) {
                arrayList.add(str2);
            }
        }
        return arrayList;
    }

    public static int getDomainListSizeFromStr(@NonNull String str) {
        return getDomainListFromStr(str).size();
    }

    public static ArrayList<LoginMeetingAuthItem> getAuthList(@NonNull PTUserProfile pTUserProfile) {
        ArrayList<LoginMeetingAuthItem> arrayList = new ArrayList<>();
        LoginMeetingAuthProtoList meetingAuths = pTUserProfile.getMeetingAuths();
        if (meetingAuths != null) {
            for (LoginMeetingAuthProto loginMeetingAuthItem : meetingAuths.getMeetingAuthsList()) {
                arrayList.add(new LoginMeetingAuthItem(loginMeetingAuthItem));
            }
        }
        return arrayList;
    }

    @Nullable
    public static LoginMeetingAuthItem getAuthItemById(@NonNull List<LoginMeetingAuthItem> list, @Nullable String str) {
        if (!StringUtil.isEmptyOrNull(str)) {
            for (LoginMeetingAuthItem loginMeetingAuthItem : list) {
                if (str.equalsIgnoreCase(loginMeetingAuthItem.getAuthId())) {
                    return loginMeetingAuthItem;
                }
            }
        }
        for (LoginMeetingAuthItem loginMeetingAuthItem2 : list) {
            if (loginMeetingAuthItem2.isUiSelect()) {
                return loginMeetingAuthItem2;
            }
        }
        return null;
    }

    public static List<PasswordItem> getPasswordRules() {
        ArrayList arrayList = new ArrayList();
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return arrayList;
        }
        long meetingPasswordRulesOption = currentUserProfile.getMeetingPasswordRulesOption();
        if ((meetingPasswordRulesOption & 1) != 0) {
            PasswordItem passwordItem = new PasswordItem();
            passwordItem.setCorrect(false);
            passwordItem.setRuleTxt(VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_password_rule_only_number_136699));
            passwordItem.setRuleType(1);
            arrayList.add(passwordItem);
        }
        if ((meetingPasswordRulesOption & 2) != 0) {
            PasswordItem passwordItem2 = new PasswordItem();
            passwordItem2.setCorrect(false);
            int meetingPasswordMinLength = currentUserProfile.getMeetingPasswordMinLength();
            passwordItem2.setRuleTxt(VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_password_rule_min_leanth_136699, new Object[]{Integer.valueOf(meetingPasswordMinLength)}));
            passwordItem2.setRuleType(2);
            arrayList.add(passwordItem2);
        }
        if ((meetingPasswordRulesOption & 4) != 0) {
            PasswordItem passwordItem3 = new PasswordItem();
            passwordItem3.setCorrect(false);
            passwordItem3.setRuleTxt(VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_password_rule_has_alphabet_136699));
            passwordItem3.setRuleType(4);
            arrayList.add(passwordItem3);
        }
        if ((meetingPasswordRulesOption & 8) != 0) {
            PasswordItem passwordItem4 = new PasswordItem();
            passwordItem4.setCorrect(false);
            passwordItem4.setRuleTxt(VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_password_rule_has_number_136699));
            passwordItem4.setRuleType(8);
            arrayList.add(passwordItem4);
        }
        if ((meetingPasswordRulesOption & 16) != 0) {
            PasswordItem passwordItem5 = new PasswordItem();
            passwordItem5.setCorrect(false);
            passwordItem5.setRuleTxt(VideoBoxApplication.getNonNullInstance().getString(C4558R.string.zm_password_rule_has_special_136699));
            passwordItem5.setRuleType(16);
            arrayList.add(passwordItem5);
        }
        return arrayList;
    }

    public static void updateRulesItem(String str, List<PasswordItem> list) {
        long validateMeetingPassword = validateMeetingPassword(str);
        for (PasswordItem passwordItem : list) {
            passwordItem.setCorrect((passwordItem.getRuleType() & validateMeetingPassword) == 0);
        }
    }

    public static long validateMeetingPassword(@NonNull String str) {
        PTUserProfile currentUserProfile = PTApp.getInstance().getCurrentUserProfile();
        if (currentUserProfile == null) {
            return 255;
        }
        return currentUserProfile.validateMeetingPassword(str);
    }
}
