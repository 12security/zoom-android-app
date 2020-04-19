package com.zipow.videobox.ptapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AvailableDialinCountry;
import com.zipow.videobox.ptapp.PTAppProtos.CountryCodelistProto;
import com.zipow.videobox.ptapp.PTAppProtos.LoginMeetingAuthProtoList;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;

public class PTUserProfile {
    private static final String TAG = PTBuddyHelper.class.getSimpleName();
    private long mNativeHandle = 0;

    private native boolean IsEnableForcePMIJBHWithPasswordImpl(long j);

    private native boolean IsEnableLinkPreviewImpl(long j);

    private native boolean alwaysEnableJoinBeforeHostByDefaultImpl(long j);

    private native boolean alwaysPreFillRandomPasswordImpl(long j);

    private native boolean alwaysTurnOnAttendeeVideoByDefaultImpl(long j);

    private native boolean alwaysTurnOnHostVideoByDefaultImpl(long j);

    private native boolean alwaysUse3rdPartyAsDefaultAudioImpl(long j);

    private native boolean alwaysUseBothAsDefaultAudioImpl(long j);

    private native boolean alwaysUsePMIEnabledOnWebByDefaultImpl(long j);

    private native boolean alwaysUseTelephonyAsDefaultAudioImpl(long j);

    private native boolean alwaysUseVoipOnlyAsDefaultAudioImpl(long j);

    private native boolean canAccessGoogleCalendarImpl(long j);

    private native boolean canAccessOutlookExchangeImpl(long j);

    @Nullable
    private native String getAccountLocalPicPathImpl(long j, int i);

    @NonNull
    private native String getAccountNameImpl(long j, int i);

    @Nullable
    private native byte[] getAvailableDiallinCountryImpl(long j);

    @Nullable
    private native String getBigPictureUrlImpl(long j);

    @Nullable
    private native String getCalendarUrlImpl(long j);

    @Nullable
    private native byte[] getCallinCountryCodesImpl(long j);

    @Nullable
    private native String getCompanyNameImpl(long j);

    @Nullable
    private native String getDefaultCallinTollCountryImpl(long j);

    @Nullable
    private native String getDepartmentImpl(long j);

    @Nullable
    private native String getEmailImpl(long j);

    @Nullable
    private native String getFirstNameImpl(long j);

    @Nullable
    private native String getJobTitleImpl(long j);

    @Nullable
    private native String getLastNameImpl(long j);

    @Nullable
    private native String getLocationImpl(long j);

    @Nullable
    private native byte[] getMeetingAuthsImpl(long j);

    private native int getMeetingPasswordMinLengthImpl(long j);

    private native long getMeetingPasswordRulesOptionImpl(long j);

    @Nullable
    private native String getMyTelephoneInfoImpl(long j);

    private native String getOauthNicknameImpl(long j);

    @Nullable
    private native String getPMIVanityURLImpl(long j);

    @Nullable
    private native String getPictureLocalPathImpl(long j);

    @Nullable
    private native String getRandomPasswordImpl(long j);

    @Nullable
    private native String getRestrictJoinUserDomainsImpl(long j);

    private native long getRoomMeetingIDImpl(long j);

    private native int getSSOEnforceLogoutTimeInMinsImpl(long j);

    private native long getSSOLoginWithPasswordTimeImpl(long j);

    @Nullable
    private native byte[] getSipPhoneIntegrationImpl(long j);

    @Nullable
    private native String getSmallPictureUrlImpl(long j);

    @Nullable
    private native String getUpgradeLinkImpl(long j);

    @Nullable
    private native String getUserIDImpl(long j);

    @Nullable
    private native String getUserNameImpl(long j);

    private native boolean hasCalendarAccountConfiguredImpl(long j);

    private native boolean hasSelfTelephonyImpl(long j);

    private native boolean isCalendarConfigurationChangedImpl(long j);

    private native boolean isDefaultEnableCloudRecordingImpl(long j);

    private native boolean isDefaultEnableListMeetingInPublicEventListImpl(long j);

    private native boolean isDefaultEnableMuteUponEntryImpl(long j);

    private native boolean isDefaultEnableOnlyAuthUsersCanJoinImpl(long j);

    private native boolean isDefaultEnableRecordingImpl(long j);

    private native boolean isDefaultScheduleUsePMIImpl(long j);

    private native boolean isDisablePSTNImpl(long j);

    private native boolean isEnableAddMeetingToPublicCalendarEventImpl(long j);

    private native boolean isEnableAddToGoogleCalendarForMobileImpl(long j);

    private native boolean isEnableAudioWatermarkImpl(long j);

    private native boolean isEnableCloudRecordingImpl(long j);

    private native boolean isEnableDisplayEveryoneMeetingListImpl(long j);

    private native boolean isEnableInformationBarrierImpl(long j);

    private native boolean isEnableLanguageInterpretationImpl(long j);

    private native boolean isEnableLocalRecordingImpl(long j);

    private native boolean isEnableNotStoreMeetingTopicImpl(long j);

    private native boolean isEnablePMIRequirePasswordImpl(long j);

    private native boolean isEnableRequirePasswordImpl(long j);

    private native boolean isEnableWaitingRoomImpl(long j);

    private native boolean isEnableZoomCalendarImpl(long j);

    private native boolean isKubiEnabledImpl(long j);

    private native boolean isLockAddMeetingToPublicCalendarEventImpl(long j);

    private native boolean isLockAudioTypeImpl(long j);

    private native boolean isLockAudioWatermarkImpl(long j);

    private native boolean isLockAutomaticRecordingImpl(long j);

    private native boolean isLockHostVideoImpl(long j);

    private native boolean isLockInstantMeetingUsePMIImpl(long j);

    private native boolean isLockJoinBeforeHostImpl(long j);

    private native boolean isLockMuteUponEntryImpl(long j);

    private native boolean isLockOnlyAuthUsersCanJoinImpl(long j);

    private native boolean isLockPMIRequirePasswordImpl(long j);

    private native boolean isLockParticipantsImpl(long j);

    private native boolean isLockScheduleRequirePasswordImpl(long j);

    private native boolean isLockWaitingRoomImpl(long j);

    private native boolean isLockWatermarkedImpl(long j);

    private native boolean isScheduleAudioBothDisabledImpl(long j);

    private native boolean isSupportFeatureEnablePaidUserForCNImpl(long j);

    private native boolean isSupportFeatureRequirePasswordImpl(long j);

    private native boolean isSupportJbhPriorTimeImpl(long j);

    private native boolean isSupportOnlyAuthUsersCanJoinImpl(long j);

    private native long validateMeetingPasswordImpl(String str, long j);

    public PTUserProfile(long j) {
        this.mNativeHandle = j;
    }

    public String getOauthNickname() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOauthNicknameImpl(j);
    }

    @Nullable
    public String getUserName() {
        return getUserNameImpl(this.mNativeHandle);
    }

    @Nullable
    public String getFirstName() {
        return getFirstNameImpl(this.mNativeHandle);
    }

    @Nullable
    public String getLastName() {
        return getLastNameImpl(this.mNativeHandle);
    }

    @Nullable
    public String getUserID() {
        return getUserIDImpl(this.mNativeHandle);
    }

    @Nullable
    public String getEmail() {
        return getEmailImpl(this.mNativeHandle);
    }

    @Nullable
    public String getSmallPictureUrl() {
        return getSmallPictureUrlImpl(this.mNativeHandle);
    }

    @Nullable
    public String getBigPictureUrl() {
        return getBigPictureUrlImpl(this.mNativeHandle);
    }

    @Nullable
    public String getPictureLocalPath() {
        return getPictureLocalPathImpl(this.mNativeHandle);
    }

    @Nullable
    public String getAccountName() {
        return getAccountName(102);
    }

    @Nullable
    public String getAccountName(int i) {
        return getAccountNameImpl(this.mNativeHandle, i);
    }

    @Nullable
    public String getAccountLocalPicPath() {
        return getAccountLocalPicPath(102);
    }

    @Nullable
    public String getAccountLocalPicPath(int i) {
        return getAccountLocalPicPathImpl(this.mNativeHandle, i);
    }

    @Nullable
    public String getCompanyName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCompanyNameImpl(j);
    }

    @Nullable
    public String getDepartment() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getDepartmentImpl(j);
    }

    @Nullable
    public String getJobTitle() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getJobTitleImpl(j);
    }

    @Nullable
    public String getLocation() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocationImpl(j);
    }

    public boolean isDisablePSTN() {
        return isDisablePSTNImpl(this.mNativeHandle);
    }

    @Nullable
    public String getPMIVanityURL() {
        return getPMIVanityURLImpl(this.mNativeHandle);
    }

    public long getRoomMeetingID() {
        return getRoomMeetingIDImpl(this.mNativeHandle);
    }

    public boolean hasSelfTelephony() {
        return hasSelfTelephonyImpl(this.mNativeHandle);
    }

    @Nullable
    public String getMyTelephoneInfo() {
        return getMyTelephoneInfoImpl(this.mNativeHandle);
    }

    public boolean alwaysUse3rdPartyAsDefaultAudio() {
        return alwaysUse3rdPartyAsDefaultAudioImpl(this.mNativeHandle);
    }

    public boolean alwaysPreFillRandomPassword() {
        return alwaysPreFillRandomPasswordImpl(this.mNativeHandle);
    }

    public boolean alwaysUseTelephonyAsDefaultAudio() {
        return alwaysUseTelephonyAsDefaultAudioImpl(this.mNativeHandle);
    }

    public boolean alwaysTurnOnHostVideoByDefault() {
        return alwaysTurnOnHostVideoByDefaultImpl(this.mNativeHandle);
    }

    public boolean alwaysTurnOnAttendeeVideoByDefault() {
        return alwaysTurnOnAttendeeVideoByDefaultImpl(this.mNativeHandle);
    }

    public boolean alwaysUseVoipOnlyAsDefaultAudio() {
        return alwaysUseVoipOnlyAsDefaultAudioImpl(this.mNativeHandle);
    }

    @Nullable
    public String getRandomPassword() {
        return getRandomPasswordImpl(this.mNativeHandle);
    }

    public int getMeetingPasswordMinLength() {
        return getMeetingPasswordMinLengthImpl(this.mNativeHandle);
    }

    public long getMeetingPasswordRulesOption() {
        return getMeetingPasswordRulesOptionImpl(this.mNativeHandle);
    }

    public long validateMeetingPassword(String str) {
        return validateMeetingPasswordImpl(str, this.mNativeHandle);
    }

    public boolean isKubiEnabled() {
        return isKubiEnabledImpl(this.mNativeHandle);
    }

    @Nullable
    public String getRestrictJoinUserDomains() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return "";
        }
        return getRestrictJoinUserDomainsImpl(j);
    }

    public boolean alwaysEnableJoinBeforeHostByDefault() {
        return alwaysEnableJoinBeforeHostByDefaultImpl(this.mNativeHandle);
    }

    public boolean alwaysUseBothAsDefaultAudio() {
        return alwaysUseBothAsDefaultAudioImpl(this.mNativeHandle);
    }

    public boolean alwaysUsePMIEnabledOnWebByDefault() {
        return alwaysUsePMIEnabledOnWebByDefaultImpl(this.mNativeHandle);
    }

    @Nullable
    public CountryCodelistProto getCallinCountryCodes() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            return CountryCodelistProto.parseFrom(getCallinCountryCodesImpl(j));
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public String getDefaultCallinTollCountry() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return "";
        }
        return getDefaultCallinTollCountryImpl(j);
    }

    public int getSSOEnforceLogoutTimeInMins() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSSOEnforceLogoutTimeInMinsImpl(j);
    }

    public boolean isScheduleAudioBothDisabled() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isScheduleAudioBothDisabledImpl(j);
    }

    @Nullable
    public SipPhoneIntegration getSipPhoneIntegration() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] sipPhoneIntegrationImpl = getSipPhoneIntegrationImpl(j);
        if (sipPhoneIntegrationImpl == null) {
            return null;
        }
        try {
            return SipPhoneIntegration.parseFrom(sipPhoneIntegrationImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean isEnableLinkPreview() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return IsEnableLinkPreviewImpl(j);
    }

    @Nullable
    public AvailableDialinCountry getAvailableDiallinCountry() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        try {
            return AvailableDialinCountry.parseFrom(getAvailableDiallinCountryImpl(j));
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean isEnableForcePMIJBHWithPassword() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return IsEnableForcePMIJBHWithPasswordImpl(j);
    }

    public boolean isEnableZoomCalendar() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableZoomCalendarImpl(j);
    }

    public boolean hasCalendarAccountConfigured() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasCalendarAccountConfiguredImpl(j);
    }

    public boolean isCalendarConfigurationChanged() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isCalendarConfigurationChangedImpl(j);
    }

    @Nullable
    public String getCalendarUrl() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCalendarUrlImpl(j);
    }

    public boolean canAccessOutlookExchange() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canAccessOutlookExchangeImpl(j);
    }

    public boolean canAccessGoogleCalendar() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canAccessGoogleCalendarImpl(j);
    }

    public boolean isEnableAddToGoogleCalendarForMobile() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableAddToGoogleCalendarForMobileImpl(j);
    }

    @Nullable
    public String getUpgradeLink() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getUpgradeLinkImpl(j);
    }

    public boolean isEnableAddMeetingToPublicCalendarEvent() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableAddMeetingToPublicCalendarEventImpl(j);
    }

    public boolean isDefaultEnableListMeetingInPublicEventList() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDefaultEnableListMeetingInPublicEventListImpl(j);
    }

    public boolean isDefaultEnableMuteUponEntry() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDefaultEnableMuteUponEntryImpl(j);
    }

    public boolean isDefaultScheduleUsePMI() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDefaultScheduleUsePMIImpl(j);
    }

    public boolean isLockInstantMeetingUsePMI() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockInstantMeetingUsePMIImpl(j);
    }

    public boolean isLockHostVideo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockHostVideoImpl(j);
    }

    public boolean isLockParticipants() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockParticipantsImpl(j);
    }

    public boolean isLockAudioType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockAudioTypeImpl(j);
    }

    public boolean isLockJoinBeforeHost() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockJoinBeforeHostImpl(j);
    }

    public boolean isLockMuteUponEntry() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockMuteUponEntryImpl(j);
    }

    public boolean isLockWatermarked() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockWatermarkedImpl(j);
    }

    public boolean isLockAutomaticRecording() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockAutomaticRecordingImpl(j);
    }

    public boolean isLockAddMeetingToPublicCalendarEvent() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockAddMeetingToPublicCalendarEventImpl(j);
    }

    public boolean isEnableLocalRecording() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableLocalRecordingImpl(j);
    }

    public boolean isEnableCloudRecording() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableCloudRecordingImpl(j);
    }

    public boolean isDefaultEnableRecording() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDefaultEnableRecordingImpl(j);
    }

    public boolean isDefaultEnableCloudRecording() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDefaultEnableCloudRecordingImpl(j);
    }

    public boolean isLockAudioWatermark() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockAudioWatermarkImpl(j);
    }

    public boolean isEnableAudioWatermark() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableAudioWatermarkImpl(j);
    }

    public boolean isEnableRequirePassword() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableRequirePasswordImpl(j);
    }

    public boolean isEnablePMIRequirePassword() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnablePMIRequirePasswordImpl(j);
    }

    public boolean isLockScheduleRequirePassword() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockScheduleRequirePasswordImpl(j);
    }

    public boolean isLockPMIRequirePassword() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockPMIRequirePasswordImpl(j);
    }

    public boolean isSupportFeatureRequirePassword() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSupportFeatureRequirePasswordImpl(j);
    }

    public boolean isEnableLanguageInterpretation() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableLanguageInterpretationImpl(j);
    }

    public boolean isEnableWaitingRoom() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableWaitingRoomImpl(j);
    }

    public boolean isLockWaitingRoom() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockWaitingRoomImpl(j);
    }

    public boolean isEnableNotStoreMeetingTopic() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableNotStoreMeetingTopicImpl(j);
    }

    public boolean isSupportJbhPriorTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSupportJbhPriorTimeImpl(j);
    }

    public boolean isEnableDisplayEveryoneMeetingList() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableDisplayEveryoneMeetingListImpl(j);
    }

    public long getSSOLoginWithPasswordTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getSSOLoginWithPasswordTimeImpl(j);
    }

    @Nullable
    public LoginMeetingAuthProtoList getMeetingAuths() {
        byte[] meetingAuthsImpl = getMeetingAuthsImpl(this.mNativeHandle);
        LoginMeetingAuthProtoList loginMeetingAuthProtoList = null;
        if (meetingAuthsImpl == null || meetingAuthsImpl.length == 0) {
            return null;
        }
        try {
            loginMeetingAuthProtoList = LoginMeetingAuthProtoList.parseFrom(meetingAuthsImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return loginMeetingAuthProtoList;
    }

    public boolean isDefaultEnableOnlyAuthUsersCanJoin() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDefaultEnableOnlyAuthUsersCanJoinImpl(j);
    }

    public boolean isLockOnlyAuthUsersCanJoin() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLockOnlyAuthUsersCanJoinImpl(j);
    }

    public boolean isSupportOnlyAuthUsersCanJoin() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSupportOnlyAuthUsersCanJoinImpl(j);
    }

    public boolean isSupportFeatureEnablePaidUserForCN() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSupportFeatureEnablePaidUserForCNImpl(j);
    }

    public boolean isEnableInformationBarrier() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isEnableInformationBarrierImpl(j);
    }
}
