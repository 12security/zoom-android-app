package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.RobotCommandList;
import com.zipow.videobox.ptapp.IMProtos.RoomDeviceInfo;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomBuddy */
public class ZoomBuddy {
    private long mNativeHandle = 0;

    private native int getAccountStatusImpl(long j);

    private native int getBuddyTypeImpl(long j);

    private native long getCloudSIPCallNumberImpl(long j);

    @Nullable
    private native String getCompanyNameImpl(long j);

    @Nullable
    private native String getDepartmentImpl(long j);

    private native int getE2EAbilityImpl(long j, int i);

    @Nullable
    private native String getEmailImpl(long j);

    @Nullable
    private native String getFirstNameImpl(long j);

    @Nullable
    private native String getIntroductionImpl(long j);

    @Nullable
    private native String getJidImpl(long j);

    @Nullable
    private native String getJobTitleImpl(long j);

    private native int getLastMatchScoreImpl(long j);

    @Nullable
    private native String getLastNameImpl(long j);

    @Nullable
    private native String getLocalBigPicturePathImpl(long j);

    @Nullable
    private native String getLocalPicturePathImpl(long j);

    @Nullable
    private native String getLocationImpl(long j);

    private native long getMeetingNumberImpl(long j);

    @Nullable
    private native String getPhoneNumberImpl(long j);

    private native int getPresenceImpl(long j);

    private native int getPresenceStatusImpl(long j);

    @Nullable
    private native String getProfileCountryCodeImpl(long j);

    @Nullable
    private native String getProfilePhoneNumberImpl(long j);

    @Nullable
    private native String getRobotCmdPrefixImpl(long j);

    @Nullable
    private native byte[] getRobotCommandsImpl(long j);

    @Nullable
    private native byte[] getRoomDeviceInfoImpl(long j);

    @Nullable
    private native String getScreenNameImpl(long j);

    @Nullable
    private native String getSignatureImpl(long j);

    @Nullable
    private native String getSipPhoneNumberImpl(long j);

    @Nullable
    private native String getVanityUrlImpl(long j);

    private native boolean hasOnlineE2EResourceImpl(long j);

    private native boolean isAvailableAlertImpl(long j);

    private native boolean isAvailableImpl(long j);

    private native boolean isClientSupportsE2EImpl(long j);

    private native boolean isDesktopOnlineImpl(long j);

    private native boolean isIMBlockedByIBImpl(long j);

    private native boolean isLegencyBuddyImpl(long j);

    private native boolean isMeetingBlockedByIBImpl(long j);

    private native boolean isMobileOnlineImpl(long j);

    private native boolean isNoneFriendImpl(long j);

    private native boolean isPadOnlineImpl(long j);

    private native boolean isPendingImpl(long j);

    private native boolean isPictureDownloadedImpl(long j);

    private native boolean isPresenceSyncedImpl(long j);

    private native boolean isRobotImpl(long j);

    private native boolean isRoomDeviceImpl(long j);

    private native boolean isZoomRoomImpl(long j);

    private native boolean strictMatchImpl(long j, List<String> list, boolean z, boolean z2);

    public ZoomBuddy(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getJid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getJidImpl(j);
    }

    @Nullable
    public String getPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPhoneNumberImpl(j);
    }

    @Nullable
    public String getScreenName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String screenNameImpl = getScreenNameImpl(j);
        if (screenNameImpl != null) {
            screenNameImpl = screenNameImpl.trim();
        }
        return screenNameImpl;
    }

    @Nullable
    public String getFirstName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String firstNameImpl = getFirstNameImpl(j);
        if (firstNameImpl != null) {
            firstNameImpl = firstNameImpl.trim();
        }
        return firstNameImpl;
    }

    @Nullable
    public String getLastName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String lastNameImpl = getLastNameImpl(j);
        if (lastNameImpl != null) {
            lastNameImpl = lastNameImpl.trim();
        }
        return lastNameImpl;
    }

    @Nullable
    public String getEmail() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        String emailImpl = getEmailImpl(j);
        if (!StringUtil.isValidEmailAddress(emailImpl)) {
            return null;
        }
        return emailImpl;
    }

    public boolean isPictureDownloaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPictureDownloadedImpl(j);
    }

    @Nullable
    public String getLocalPicturePath() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocalPicturePathImpl(j);
    }

    @Nullable
    public String getLocalBigPicturePath() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocalBigPicturePathImpl(j);
    }

    public int getPresence() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getPresenceImpl(j);
    }

    public int getPresenceStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getPresenceStatusImpl(j);
    }

    @Nullable
    public String getSignature() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSignatureImpl(j);
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

    public boolean isMobileOnline() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMobileOnlineImpl(j);
    }

    public boolean isAvailableAlert() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAvailableAlertImpl(j);
    }

    public boolean isDesktopOnline() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDesktopOnlineImpl(j);
    }

    public boolean isPresenceSynced() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPresenceSyncedImpl(j);
    }

    public boolean isPadOnline() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPadOnlineImpl(j);
    }

    public boolean isPending() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPendingImpl(j);
    }

    public boolean isAvailable() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isAvailableImpl(j);
    }

    public boolean isNoneFriend() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isNoneFriendImpl(j);
    }

    public boolean isLegencyBuddy() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isLegencyBuddyImpl(j);
    }

    public int getE2EAbility(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 1;
        }
        return getE2EAbilityImpl(j, i);
    }

    public int getLastMatchScore() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getLastMatchScoreImpl(j);
    }

    public boolean strictMatch(List<String> list, boolean z, boolean z2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return strictMatchImpl(j, list, z, z2);
    }

    public boolean isClientSupportsE2E() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isClientSupportsE2EImpl(j);
    }

    public boolean isZoomRoom() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isZoomRoomImpl(j);
    }

    public long getMeetingNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMeetingNumberImpl(j);
    }

    @Nullable
    public String getVanityUrl() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getVanityUrlImpl(j);
    }

    public boolean isRobot() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isRobotImpl(j);
    }

    @Nullable
    public String getRobotCmdPrefix() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRobotCmdPrefixImpl(j);
    }

    @Nullable
    public String getProfileCountryCode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getProfileCountryCodeImpl(j);
    }

    @Nullable
    public String getProfilePhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getProfilePhoneNumberImpl(j);
    }

    public boolean hasOnlineE2EResource() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasOnlineE2EResourceImpl(j);
    }

    @Nullable
    public String getSipPhoneNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSipPhoneNumberImpl(j);
    }

    @Nullable
    public ICloudSIPCallNumber getCloudSIPCallNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long cloudSIPCallNumberImpl = getCloudSIPCallNumberImpl(j);
        if (cloudSIPCallNumberImpl == 0) {
            return null;
        }
        return new ICloudSIPCallNumber(cloudSIPCallNumberImpl);
    }

    public int getAccountStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getAccountStatusImpl(j);
    }

    public int getBuddyType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyTypeImpl(j);
    }

    @Nullable
    public String getIntroduction() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIntroductionImpl(j);
    }

    @Nullable
    public RobotCommandList getRobotCommands() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] robotCommandsImpl = getRobotCommandsImpl(j);
        if (robotCommandsImpl == null || robotCommandsImpl.length == 0) {
            return null;
        }
        try {
            return RobotCommandList.parseFrom(robotCommandsImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public RoomDeviceInfo getRoomDeviceInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] roomDeviceInfoImpl = getRoomDeviceInfoImpl(j);
        if (roomDeviceInfoImpl == null) {
            return null;
        }
        try {
            return RoomDeviceInfo.parseFrom(roomDeviceInfoImpl);
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean getIsRoomDevice() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isRoomDeviceImpl(j);
    }

    public boolean isIMBlockedByIB() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isIMBlockedByIBImpl(j);
    }

    public boolean isMeetingBlockedByIB() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isMeetingBlockedByIBImpl(j);
    }
}
