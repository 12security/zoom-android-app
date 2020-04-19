package com.zipow.videobox.ptapp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.cmmlib.ZoomAppPropData;
import com.zipow.videobox.CmmSavedMeeting;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.VideoBoxApplication.IConfProcessListener;
import com.zipow.videobox.common.p008pt.ILeaveConfCallBack;
import com.zipow.videobox.common.p008pt.ZMNativeSsoCloudInfo;
import com.zipow.videobox.login.model.ZmLoginHelper;
import com.zipow.videobox.mainboard.Mainboard;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.PTAppProtos.IPLocationInfo;
import com.zipow.videobox.ptapp.PTAppProtos.InvitationItem;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration;
import com.zipow.videobox.ptapp.PTAppProtos.SipPhoneIntegration.Builder;
import com.zipow.videobox.ptapp.PTAppProtos.UrlActionData;
import com.zipow.videobox.ptapp.PTAppProtos.ZoomAccount;
import com.zipow.videobox.ptapp.p013mm.CrawlerLinkPreview;
import com.zipow.videobox.ptapp.p013mm.FileInfoChecker;
import com.zipow.videobox.ptapp.p013mm.GroupMemberSynchronizer;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.MMPrivateStickerMgr;
import com.zipow.videobox.ptapp.p013mm.NotificationSettingMgr;
import com.zipow.videobox.ptapp.p013mm.SearchMgr;
import com.zipow.videobox.ptapp.p013mm.UnSupportMessageMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomMessageTemplate;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CallHistoryMgr;
import com.zipow.videobox.sip.SIPConfiguration;
import com.zipow.videobox.sip.server.CmmSIPCallManager;
import com.zipow.videobox.sip.server.ISIPCallAPI;
import com.zipow.videobox.sip.server.ZMPhoneNumberHelper;
import com.zipow.videobox.util.BuildTarget;
import com.zipow.videobox.util.IPCHelper;
import com.zipow.videobox.util.LoginUtil;
import com.zipow.videobox.util.PreferenceUtil;
import com.zipow.videobox.util.ZMDomainUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zipow.mdm.ZoomMdmPolicyProvider;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.ResourcesUtil;
import p021us.zoom.androidlib.util.RootCheckUtils;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.videomeetings.C4558R;
import p021us.zoom.videomeetings.ZMBuildConfig;

public class PTApp {
    private static final String TAG = "PTApp";
    @Nullable
    private static PTApp instance;
    private IMHelper mIMHelper;
    private long mLastUpdateNotesDisplayTime = 0;
    private String mMeetingListLastDisplayedHostId = "";
    private boolean mNeedCheckSwitchCall = false;
    private boolean mNeedFilterCallRoomEventCallbackInMeeting = false;
    private boolean mNeedSwitchVendor = true;
    private boolean mTokenExpired = false;
    private String mUrlAction;
    private boolean mWebSignedOn = false;
    @Nullable
    private ZoomMdmPolicyProvider mZoomMdmPolicyProvider = null;
    private boolean needToReturnToMeetingOnResume = false;
    private boolean showPresentToRoomCancelStatus = false;

    @Nullable
    private native String RCgetCountryCodeByCountryTypeImpl(int i);

    private native int RCgetDefaultCountryTypeByNameImpl(String str);

    private native void RCsetCountryTypeImpl(int i);

    private native void VTLSConfirmAcceptCertItemImpl(VerifyCertEvent verifyCertEvent, boolean z, boolean z2);

    private native int acceptVideoCallImpl(byte[] bArr, String str, boolean z);

    private native boolean beforeLogoutImpl();

    private native boolean canAccessZoomWebserviceImpl();

    private native boolean cancelCallOutImpl();

    @Nullable
    private native String changeUserPasswordImpl(String str, String str2);

    private native boolean checkAgeGatingImpl(String str);

    private native boolean checkForUpdatesImpl(boolean z, boolean z2);

    private native void clearSavedMeetingListImpl();

    private native void configSDKDSCPImpl(int i, int i2, boolean z);

    private native void configZoomDomainImpl(String str);

    private native int confirmAgeGatingImpl(boolean z, int i, String str);

    private native boolean confirmGDPRImpl(boolean z);

    private native int declineVideoCallImpl(byte[] bArr, String str);

    private native void dispatchIdleMessageImpl();

    private native void enableHideFullPhoneNumber4PureCallinUserImpl(boolean z);

    private native boolean forgotPasswordImpl(String str);

    private native long getABContactsHelperHandle();

    @Nullable
    private native String getActiveCallIdImpl();

    @Nullable
    private native byte[] getActiveMeetingItemImpl();

    private native long getActiveMeetingNoImpl();

    private native boolean getAllRoomSystemListImpl(int i, List<RoomDevice> list);

    @Nullable
    private native byte[] getAltHostAtImpl(int i);

    private native int getAltHostCountImpl();

    private native int getAuthInfoImpl(int i, String str, int i2, String[] strArr, String[] strArr2);

    private native long getBuddyHelperHandle();

    private native long getBusinessMessengerHandle();

    private native void getCalendarIntegrationConfigImpl();

    private native long getCallHistoryMgrImpl();

    private native int getCallStatusImpl();

    private native boolean getCloudSwitchListImpl(List<String> list);

    private native long getCurrentUserProfileHandle();

    @Nullable
    private native String getDeviceUserNameImpl();

    private native long getFBAuthHelperHandle();

    private native long getFavoriteMgrHandle();

    private native String getFmtRestrictedLoginDomainImpl();

    private native int getFreeMeetingGiftTimeImpl();

    @Nullable
    private native String getGiftUpgradeUrlImpl();

    private native long getGroupMemberSynchronizerHandle();

    private native long getH323AccessCodeImpl();

    @Nullable
    private native String getH323GatewayImpl();

    @Nullable
    private native String getH323PasswordImpl();

    private native long getIMHelperHandle();

    @Nullable
    private native byte[] getIPLocationImpl(boolean z);

    @Nullable
    private native String getLatestVersionReleaseNoteImpl();

    @Nullable
    private native String getLatestVersionStringImpl();

    private native long getLinkCrawlerImpl();

    private native String getLoginAuthDisplayNameImpl();

    private native String getLoginAuthOpenIdImpl();

    private native String getLoginAuthPicUrlImpl();

    @Nullable
    private native String getMarketplaceURLImpl();

    private native long getMeetingHelperHandle();

    private native String getMinClientVersionImpl();

    private native long getMonitorLogServiceImpl();

    @Nullable
    private native String getMyLocalAddressImpl();

    private native long getNotificationSettingMgrImpl();

    private native long getPBXAddressBookMgrHandle();

    private native int getPTLoginTypeImpl();

    @Nullable
    private native String getPackageCheckSumImpl();

    @Nullable
    private native String getPackageDownloadUrlImpl();

    @Nullable
    private native String getPackageNameImpl();

    @Nullable
    private native String getPhoneSettingUrlImpl(String str);

    private native long getPolicyProviderHandleImpl();

    private native boolean getRoomSystemListImpl(int i, int i2, List<RoomDevice> list);

    @Nullable
    private native ZMNativeSsoCloudInfo getSSOCloudInfoImpl();

    @Nullable
    private native String getSavedGoogleIDImpl();

    @Nullable
    private native CmmSavedMeeting[] getSavedMeetingListImpl();

    private native int getSavedRingCentralPhoneNumberAndExtImpl(String[] strArr, String[] strArr2);

    @Nullable
    private native byte[] getSavedZoomAccountDataImpl();

    private native int getSdkAuthResultImpl();

    private native long getSearchMgrImpl();

    private native long getSettingHelperHandle();

    private native long getSipCallAPIImpl();

    @Nullable
    private native String getURLByTypeImpl(int i);

    private native long getUnsupportMessageMgrImpl();

    @Nullable
    private native String getWebDomainImpl();

    private native String getZMCIDImpl();

    private native long getZMPhoneNumberHelperImpl();

    @Nullable
    private native String getZoomDomainImpl();

    private native long getZoomFileContentMgrImpl();

    private native long getZoomFileInfoCheckerImpl();

    @Nullable
    private native String getZoomInvitationEmailBodyImpl();

    @Nullable
    private native String getZoomInvitationEmailSubjectImpl();

    private native long getZoomMessageTemplateHandle();

    private native long getZoomMessengerHandle();

    private native long getZoomPrivateStickerMgrImpl();

    private native long getZoomProductHelperHandle();

    private native boolean hasBusinessMessengerImpl();

    private native boolean hasPrescheduleMeetingImpl();

    private native int inviteBuddiesToConfImpl(String[] strArr, String[] strArr2, String str, long j, String str2);

    private native boolean inviteCallOutUserImpl(String str, String str2);

    private native int inviteToVideoCallImpl(String str, String str2, int i);

    private native boolean isAuthenticatingImpl();

    private native boolean isAutoReponseONImpl();

    private native boolean isCNMeetingONImpl();

    private native boolean isCallOutInProgressImpl(int[] iArr);

    private native boolean isChangeNameEnabledImpl();

    private native boolean isChangePictureEnabledImpl();

    private native boolean isCloudPBXEnableImpl();

    private native boolean isCorpUserImpl();

    private native boolean isEnableCloudSwitchImpl();

    private native boolean isFeedbackOffImpl();

    private native boolean isFileTransferDisabledImpl();

    private native boolean isFileTypeAllowSendInChatImpl(String str);

    private native boolean isImportPhotosFromDeviceEnableImpl();

    private native boolean isJoinMeetingBySpecialModeEnabledImpl(int i);

    private native boolean isKeepCompanyContactsImpl();

    private native boolean isKeepMeLoggedInImpl();

    private native boolean isPaidUserImpl();

    private native boolean isPremiumFeatureEnabledImpl();

    private native boolean isPublicGmailUserImpl();

    private native boolean isSdkNeedWaterMarkImpl();

    private native boolean isShareDesktopDisabledImpl();

    private native boolean isSipPhoneEnabledImpl();

    private native boolean isStartVideoCallWithRoomSystemEnabledImpl();

    private native boolean isSyncUserGroupONImpl();

    private native boolean isTaiWanZHImpl();

    private native boolean joinFromIconTrayImpl(String str, String str2, long j, String str3, boolean z, boolean z2);

    private native int joinMeetingBySpecialModeImpl(int i, long j, String str, String str2);

    private native int launchCallForWebStartImpl();

    private native void logUICommandImpl(String str, String str2, String str3);

    private native int loginFacebookWithLocalTokenImpl(boolean z, boolean z2);

    private native void loginFacebookWithTokenImpl(String str, long j);

    private native int loginGoogleWithLocalTokenImpl();

    private native int loginGoogleWithTokensImpl(String str, String str2);

    private native int loginRingCentralWithLocalTokenImpl();

    private native int loginSSOWithLocalTokenImpl();

    private native void loginWithFacebookImpl(String str, long j, boolean z);

    private native int loginWithLocalOAuthTokenForRealNameImpl(int i, String str, String str2, String str3, String str4, String str5);

    private native int loginWithLocalOAuthTokenImpl(int i, String str, String str2);

    private native int loginWithOAuthTokenForRealNameImpl(int i, String str, String str2, byte[] bArr, String str3, String str4, String str5);

    private native int loginWithOAuthTokenImpl(int i, String str, String str2, byte[] bArr);

    private native int loginWithPhonePasswdImpl(String str, String str2, byte[] bArr, boolean z);

    private native int loginWithPhoneSmsImpl(String str, String str2, byte[] bArr, boolean z);

    private native int loginWithRingCentralImpl(String str, String str2, String str3, int i, boolean z);

    private native int loginWithSSOKMSTokenImpl(String str, String str2, String str3);

    private native int loginWithSSOTokenImpl(String str);

    private native void loginXmppServerImpl(String str);

    private native int loginZoomImpl(String str, byte[] bArr, boolean z);

    private native int loginZoomWithLocalTokenForTypeImpl(int i);

    private native boolean logoutImpl(int i);

    @Nullable
    private native String modifyCountryCodeImpl(String str);

    @Nullable
    private native String modifyVanityUrlImpl(String str);

    private native int navWebWithDefaultBrowserImpl(int i, String str);

    private native boolean needDoAutoLoginImpl();

    private native boolean needDoWebStartImpl();

    private native boolean needRealNameAuthImpl();

    private native boolean nosClearDeviceTokenImpl();

    private native boolean nosMessageNotificationReceivedImpl(int i, String str, String str2, String str3);

    private native boolean nosNotificationReceivedImpl(String str, String str2);

    private native boolean nosSetDeviceInfoImpl(byte[] bArr);

    /* access modifiers changed from: private */
    public native boolean nosSetDeviceTokenImpl(String str, String str2);

    private native boolean nosUpdateDeviceTokenImpl(String str, String str2, String str3);

    private native void onCancelReloginAndRejoinImpl();

    private native void onUserSkipSignToJoinOptionImpl();

    private native boolean parseAppProtocolImpl(UrlActionInfo urlActionInfo, String str, boolean z);

    @Nullable
    private native byte[] parseURLActionDataImpl(String str);

    private native int parseZoomActionImpl(String str);

    private native boolean presentToRoomImpl(int i, String str, long j, boolean z);

    private native boolean probeUserStatusImpl(String str);

    @Nullable
    private native String querySSOVanityURLImpl(String str);

    private native int requestOAuthTokenWithCodeImpl(int i, String str, String str2);

    private native void resetForReconnectingImpl();

    private native int retryLoginGoogleImpl();

    private native boolean sdkAuthImpl(String str, String str2);

    private native boolean sendActivationEmailImpl(String str, String str2, String str3);

    private native boolean sendFeedbackImpl(String str);

    private native int sendSMSCodeForLoginImpl(String str, String str2);

    private native void setCurrentUIFlagImpl(int i);

    private native void setDeviceUserNameImpl(String str);

    private native void setLanguageIDImpl(String str);

    private native boolean setPasswordImpl(boolean z, String str, String str2, String str3, String str4, String str5);

    private native void setSSOURLImpl(String str, int i);

    private native void setVideoCallWithRoomSystemPrepareStatusImpl(boolean z);

    private native boolean signupImpl(String str, String str2, String str3, String str4, String str5);

    private native int startGroupVideoCallImpl(String[] strArr, String[] strArr2, String str, long j, int i);

    private native boolean startMeetingImpl(long j);

    private native void startScheduleConfirmImpl(long j, boolean z);

    private native int startVideoCallWithRoomSystemImpl(RoomDevice roomDevice, int i, long j);

    private native int stopGroupVideoCallImpl(String str, String str2);

    private native boolean updateSipPhoneStatusImpl(byte[] bArr);

    private native boolean uploadCrashDumpFileImpl(boolean z, int i, String str);

    private native boolean uploadFeedbackImpl(int i, int i2, long j, String str, String str2, boolean z);

    private native void userAgreeLoginDisclaimerImpl();

    private native void userDisagreeLoginDisclaimerImpl();

    private native void userInputUsernamePasswordForProxyImpl(String str, int i, String str2, String str3, boolean z);

    private native boolean userUpdateMyNameImpl(String str, String str2);

    private native boolean userUploadMyPictureImpl(String str);

    private native boolean validateConfNumberImpl(String str);

    public boolean isFacebookImEnabled() {
        return false;
    }

    public boolean isGoogleImEnabled() {
        return false;
    }

    private PTApp() {
    }

    @NonNull
    public static synchronized PTApp getInstance() {
        PTApp pTApp;
        synchronized (PTApp.class) {
            if (instance == null) {
                instance = new PTApp();
            }
            pTApp = instance;
        }
        return pTApp;
    }

    public void initialize() {
        ZoomProductHelper zoomProductHelper = getZoomProductHelper();
        if (zoomProductHelper != null) {
            boolean isLocaleCN = AndroidAppUtil.isLocaleCN(VideoBoxApplication.getNonNullInstance());
            zoomProductHelper.initCurrentLocale(isLocaleCN ? 1 : 0);
            zoomProductHelper.setDeviceJailBreak(RootCheckUtils.isRooted());
            ZMDomainUtil.initMainDomain(isLocaleCN);
        }
        getInstance().refreshMeetingListLastDisplayedHostIdFromDb();
    }

    public boolean autoSignin() {
        boolean z = false;
        if (!NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            return false;
        }
        if (isAuthenticating()) {
            return true;
        }
        if (!BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET)) {
            int pTLoginType = getPTLoginType();
            if (pTLoginType == 2) {
                int loginGoogleWithLocalToken = loginGoogleWithLocalToken();
                if (LoginUtil.ShowRestrictedLoginErrorDlg(loginGoogleWithLocalToken, true)) {
                    LoginUtil.autoLogout(loginGoogleWithLocalToken);
                }
                if (loginGoogleWithLocalToken == 0) {
                    z = true;
                }
                return z;
            } else if (pTLoginType == 0) {
                int loginFacebookWithLocalToken = loginFacebookWithLocalToken();
                if (LoginUtil.ShowRestrictedLoginErrorDlg(loginFacebookWithLocalToken, true)) {
                    LoginUtil.autoLogout(loginFacebookWithLocalToken);
                }
                if (loginFacebookWithLocalToken == 0) {
                    z = true;
                }
                return z;
            } else if (ZmLoginHelper.isUseZoomLogin(pTLoginType)) {
                int loginZoomWithLocalTokenForType = loginZoomWithLocalTokenForType(pTLoginType);
                if (LoginUtil.ShowRestrictedLoginErrorDlg(loginZoomWithLocalTokenForType, true)) {
                    LoginUtil.autoLogout(loginZoomWithLocalTokenForType);
                }
                if (loginZoomWithLocalTokenForType == 0) {
                    z = true;
                }
                return z;
            } else if (pTLoginType != 101) {
                return false;
            } else {
                int loginSSOWithLocalToken = loginSSOWithLocalToken();
                if (LoginUtil.ShowRestrictedLoginErrorDlg(loginSSOWithLocalToken, true)) {
                    LoginUtil.autoLogout(loginSSOWithLocalToken);
                }
                if (loginSSOWithLocalToken == 0) {
                    z = true;
                }
                return z;
            }
        } else if (getPTLoginType() != 98) {
            return false;
        } else {
            int loginRingCentralWithLocalToken = loginRingCentralWithLocalToken();
            if (LoginUtil.ShowRestrictedLoginErrorDlg(loginRingCentralWithLocalToken, true)) {
                LoginUtil.autoLogout(loginRingCentralWithLocalToken);
            }
            if (loginRingCentralWithLocalToken == 0) {
                z = true;
            }
            return z;
        }
    }

    public void setTokenExpired(boolean z) {
        this.mTokenExpired = z;
    }

    public boolean isTokenExpired() {
        return this.mTokenExpired;
    }

    public void setmNeedSwitchVendor(boolean z) {
        this.mNeedSwitchVendor = z;
    }

    public boolean ismNeedSwitchVendor() {
        return this.mNeedSwitchVendor;
    }

    public boolean isAuthenticating() {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            return false;
        }
        return isAuthenticatingImpl();
    }

    public boolean isWebSignedOn() {
        return this.mWebSignedOn;
    }

    public void setWebSignedOn(boolean z) {
        this.mWebSignedOn = z;
    }

    public boolean isNeedToReturnToMeetingOnResume() {
        return this.needToReturnToMeetingOnResume;
    }

    public void setNeedToReturnToMeetingOnResume(boolean z) {
        this.needToReturnToMeetingOnResume = z;
    }

    public void forceLeaveCurrentCall(ILeaveConfCallBack iLeaveConfCallBack) {
        forceLeaveCurrentCall(iLeaveConfCallBack, 0, false);
    }

    public void forceLeaveCurrentCall(@Nullable ILeaveConfCallBack iLeaveConfCallBack, int i, boolean z) {
        stopGroupVideoCall(StringUtil.safeString(getActiveCallId()), String.valueOf(i));
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        if (instance2 != null) {
            instance2.killConfInPtForWait(iLeaveConfCallBack, z);
        }
    }

    public void forceSyncLeaveCurrentCall() {
        forceSyncLeaveCurrentCall(false);
    }

    public void forceSyncLeaveCurrentCall(boolean z) {
        forceSyncLeaveCurrentCall(z, false);
    }

    public void forceSyncLeaveCurrentCall(boolean z, boolean z2) {
        String activeCallId = getActiveCallId();
        int i = 0;
        if (z2 || (activeCallId != null && activeCallId.length() > 0)) {
            stopGroupVideoCall(StringUtil.safeString(activeCallId), String.valueOf(0));
        }
        VideoBoxApplication instance2 = VideoBoxApplication.getInstance();
        while (instance2.isConfProcessRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException unused) {
            }
            i++;
            if (i > 20) {
                instance2.stopConfService();
                if (z) {
                    instance2.notifyConfProcessStopped();
                }
                return;
            }
        }
        instance2.stopConfService();
        if (z) {
            instance2.notifyConfProcessStopped();
        }
    }

    public void tryEndCallForDeclined() {
        String activeCallId = getActiveCallId();
        if (activeCallId != null && activeCallId.length() > 0) {
            stopGroupVideoCall(activeCallId, String.valueOf(41));
        }
    }

    @Nullable
    public PTBuddyHelper getBuddyHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long buddyHelperHandle = getBuddyHelperHandle();
        if (buddyHelperHandle != 0) {
            return new PTBuddyHelper(buddyHelperHandle);
        }
        return null;
    }

    @Nullable
    public PTUserProfile getCurrentUserProfile() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long currentUserProfileHandle = getCurrentUserProfileHandle();
        if (currentUserProfileHandle != 0) {
            return new PTUserProfile(currentUserProfileHandle);
        }
        return null;
    }

    @Nullable
    public FBAuthHelper getFBAuthHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long fBAuthHelperHandle = getFBAuthHelperHandle();
        if (fBAuthHelperHandle != 0) {
            return new FBAuthHelper(fBAuthHelperHandle);
        }
        return null;
    }

    @Nullable
    public IMHelper getIMHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        if (this.mIMHelper == null) {
            long iMHelperHandle = getIMHelperHandle();
            if (iMHelperHandle != 0) {
                this.mIMHelper = new IMHelper(iMHelperHandle);
            }
        }
        return this.mIMHelper;
    }

    @Nullable
    public PTSettingHelper getSettingHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long settingHelperHandle = getSettingHelperHandle();
        if (settingHelperHandle != 0) {
            return new PTSettingHelper(settingHelperHandle);
        }
        return null;
    }

    @Nullable
    public MeetingHelper getMeetingHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long meetingHelperHandle = getMeetingHelperHandle();
        if (meetingHelperHandle != 0) {
            return new MeetingHelper(meetingHelperHandle);
        }
        return null;
    }

    @Nullable
    public FavoriteMgr getFavoriteMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long favoriteMgrHandle = getFavoriteMgrHandle();
        if (favoriteMgrHandle != 0) {
            return new FavoriteMgr(favoriteMgrHandle);
        }
        return null;
    }

    @Nullable
    public ABContactsHelper getABContactsHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long aBContactsHelperHandle = getABContactsHelperHandle();
        if (aBContactsHelperHandle != 0) {
            return new ABContactsHelper(aBContactsHelperHandle);
        }
        return null;
    }

    @Nullable
    public ZoomMessenger getZoomMessenger() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long businessMessengerHandle = getBusinessMessengerHandle();
        if (businessMessengerHandle != 0) {
            return new ZoomMessenger(businessMessengerHandle);
        }
        return null;
    }

    public boolean hasZoomMessenger() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return hasBusinessMessengerImpl();
    }

    public boolean isFileTypeAllowSendInChat(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            str = "";
        }
        if (!isInitialForMainboard()) {
            return false;
        }
        return isFileTypeAllowSendInChatImpl(str);
    }

    public boolean hasContacts() {
        return hasMessenger();
    }

    public boolean hasMessenger() {
        return getInstance().hasZoomMessenger() || isFacebookImEnabled() || isGoogleImEnabled();
    }

    @Nullable
    public ZoomProductHelper getZoomProductHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long zoomProductHelperHandle = getZoomProductHelperHandle();
        if (zoomProductHelperHandle != 0) {
            return new ZoomProductHelper(zoomProductHelperHandle);
        }
        return null;
    }

    @Nullable
    public GroupMemberSynchronizer getGroupMemberSynchronizer() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long groupMemberSynchronizerHandle = getGroupMemberSynchronizerHandle();
        if (groupMemberSynchronizerHandle != 0) {
            return new GroupMemberSynchronizer(groupMemberSynchronizerHandle);
        }
        return null;
    }

    @Nullable
    public ZoomMessageTemplate getZoomMessageTemplate() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long zoomMessageTemplateHandle = getZoomMessageTemplateHandle();
        if (zoomMessageTemplateHandle != 0) {
            return new ZoomMessageTemplate(zoomMessageTemplateHandle);
        }
        return null;
    }

    public void dispatchIdleMessage() {
        dispatchIdleMessageImpl();
    }

    public void loginXmppServer(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            loginXmppServerImpl(str);
            IMHelper iMHelper = getInstance().getIMHelper();
            if (iMHelper != null) {
                iMHelper.setIMLocalStatus(1);
            }
        }
    }

    public void loginWithFacebook(String str, long j) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            loginWithFacebookImpl(str, j, false);
        }
    }

    public void loginWithFacebook(String str, long j, boolean z) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            loginWithFacebookImpl(str, j, z);
        }
    }

    public void loginWithFacebookWithToken(String str, long j) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            loginFacebookWithTokenImpl(str, j);
        }
    }

    public int loginFacebookWithLocalToken() {
        return loginFacebookWithLocalToken(false);
    }

    public int loginFacebookWithLocalToken(boolean z) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        int loginFacebookWithLocalTokenImpl = loginFacebookWithLocalTokenImpl(z, false);
        if (loginFacebookWithLocalTokenImpl == 0) {
            IMHelper iMHelper = getInstance().getIMHelper();
            if (iMHelper != null) {
                iMHelper.setIMLocalStatus(1);
            }
        }
        return loginFacebookWithLocalTokenImpl;
    }

    public boolean logout(int i) {
        return logout(i, true);
    }

    public boolean logout(int i, boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        this.mNeedSwitchVendor = z;
        CmmSIPCallManager.getInstance().logout();
        boolean logoutImpl = logoutImpl(i);
        setWebSignedOn(false);
        setTokenExpired(false);
        IMHelper iMHelper = getIMHelper();
        if (iMHelper != null) {
            iMHelper.setIMLocalStatus(0);
        }
        if (PreferenceUtil.readStringValue(PreferenceUtil.LOCAL_AVATAR, "").length() > 0) {
            PreferenceUtil.saveStringValue(PreferenceUtil.LOCAL_AVATAR, "");
        }
        return logoutImpl;
    }

    public void resetForReconnecting() {
        if (isInitialForMainboard()) {
            resetForReconnectingImpl();
        }
    }

    public boolean joinFromIconTray(@Nullable String str, String str2, long j, @Nullable String str3, boolean z, boolean z2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return joinFromIconTrayImpl(str == null ? "" : str, str2, j, str3 == null ? "" : str3, z, z2);
    }

    public int inviteToVideoCall(String str, String str2, int i) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return inviteToVideoCallImpl(str, str2, i);
    }

    @Nullable
    public String getPhoneSettingUrl(String str) {
        if (!isInitialForMainboard()) {
            return "";
        }
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        return getPhoneSettingUrlImpl(str);
    }

    public int acceptVideoCall(@Nullable InvitationItem invitationItem, String str, boolean z) {
        if (isInitialForMainboard() && invitationItem != null) {
            return acceptVideoCallImpl(invitationItem.toByteArray(), str, z);
        }
        return 1;
    }

    public int declineVideoCall(@Nullable InvitationItem invitationItem, String str) {
        if (isInitialForMainboard() && invitationItem != null) {
            return declineVideoCallImpl(invitationItem.toByteArray(), str);
        }
        return 1;
    }

    public int startGroupVideoCall(String[] strArr, String[] strArr2, String str) {
        return startGroupVideoCall(strArr, strArr2, str, 0, 3);
    }

    public int startGroupVideoCall(@Nullable String[] strArr, @Nullable String[] strArr2, String str, long j, int i) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return startGroupVideoCallImpl(strArr == null ? new String[0] : strArr, strArr2 == null ? new String[0] : strArr2, str, j, i);
    }

    public int inviteBuddiesToConf(@Nullable String[] strArr, @Nullable String[] strArr2, String str, long j, String str2) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return inviteBuddiesToConfImpl(strArr == null ? new String[0] : strArr, strArr2 == null ? new String[0] : strArr2, str, j, str2);
    }

    @Nullable
    public String getActiveCallId() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getActiveCallIdImpl();
    }

    public long getActiveMeetingNo() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getActiveMeetingNoImpl();
    }

    @Nullable
    public MeetingInfoProto getActiveMeetingItem() {
        MeetingInfoProto meetingInfoProto = null;
        if (!isInitialForMainboard()) {
            return null;
        }
        byte[] activeMeetingItemImpl = getActiveMeetingItemImpl();
        if (activeMeetingItemImpl == null || activeMeetingItemImpl.length == 0) {
            return null;
        }
        try {
            meetingInfoProto = MeetingInfoProto.parseFrom(activeMeetingItemImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return meetingInfoProto;
    }

    public int getCallStatus() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        int callStatusImpl = getCallStatusImpl();
        if (callStatusImpl == 0 && (VideoBoxApplication.getInstance().getConfProcessId() > 0 || VideoBoxApplication.getInstance().getConfService() != null)) {
            callStatusImpl = 1;
        }
        return callStatusImpl;
    }

    public boolean hasActiveCall() {
        boolean z = false;
        if (!isInitialForMainboard()) {
            return false;
        }
        int callStatus = getCallStatus();
        if (callStatus == 2 || callStatus == 1) {
            z = true;
        }
        return z;
    }

    public int stopGroupVideoCall(@Nullable String str, @Nullable String str2) {
        if (!isInitialForMainboard() || str == null) {
            return 1;
        }
        if (str2 == null) {
            str2 = "";
        }
        return stopGroupVideoCallImpl(str, str2);
    }

    public boolean probeUserStatus(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            return probeUserStatusImpl(str);
        }
        return false;
    }

    @Nullable
    public String parseConfNumberFromURLAction(String str) {
        if (!isInitialForMainboard()) {
            return null;
        }
        UrlActionData parseURLActionData = parseURLActionData(str);
        return parseURLActionData != null ? parseURLActionData.getConfno() : "";
    }

    @Nullable
    public UrlActionData parseURLActionData(String str) {
        UrlActionData urlActionData = null;
        if (!isInitialForMainboard()) {
            return null;
        }
        byte[] parseURLActionDataImpl = parseURLActionDataImpl(str);
        if (parseURLActionDataImpl == null || parseURLActionDataImpl.length == 0) {
            return null;
        }
        try {
            urlActionData = UrlActionData.parseFrom(parseURLActionDataImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return urlActionData;
    }

    public boolean parseAppProtocol(UrlActionInfo urlActionInfo, String str, boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return parseAppProtocolImpl(urlActionInfo, str, z);
    }

    public boolean validateConfNumber(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return validateConfNumberImpl(str);
    }

    public boolean sendFeedback(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sendFeedbackImpl(str);
    }

    @Nullable
    public String getRecentJid() {
        return PreferenceUtil.readStringValue(PreferenceUtil.RECENT_JID, null);
    }

    public void setRencentJid(String str) {
        PreferenceUtil.saveStringValue(PreferenceUtil.RECENT_JID, str);
    }

    @Nullable
    public String getRecentZoomJid() {
        return PreferenceUtil.readStringValue(PreferenceUtil.RECENT_ZOOM_JID, null);
    }

    public void setRencentZoomJid(String str) {
        PreferenceUtil.saveStringValue(PreferenceUtil.RECENT_ZOOM_JID, str);
    }

    public int loginGoogleWithCodes(String str, String str2) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        int loginGoogleWithTokensImpl = loginGoogleWithTokensImpl(str, str2);
        if (loginGoogleWithTokensImpl == 0) {
            IMHelper iMHelper = getInstance().getIMHelper();
            if (iMHelper != null) {
                iMHelper.setIMLocalStatus(1);
            }
        }
        return loginGoogleWithTokensImpl;
    }

    public int loginGoogleWithLocalToken() {
        if (!isInitialForMainboard()) {
            return 1;
        }
        int loginGoogleWithLocalTokenImpl = loginGoogleWithLocalTokenImpl();
        if (loginGoogleWithLocalTokenImpl == 0) {
            IMHelper iMHelper = getInstance().getIMHelper();
            if (iMHelper != null) {
                iMHelper.setIMLocalStatus(1);
            }
        }
        return loginGoogleWithLocalTokenImpl;
    }

    public int getPTLoginType() {
        if (!isInitialForMainboard()) {
            return 102;
        }
        return getPTLoginTypeImpl();
    }

    public boolean isCurrentLoginTypeSupportFavoriteContacts() {
        if (ZMBuildConfig.BUILD_TARGET != 0) {
            return false;
        }
        int pTLoginType = getPTLoginType();
        if (pTLoginType == 100) {
            ZoomProductHelper zoomProductHelper = getInstance().getZoomProductHelper();
            if (zoomProductHelper != null && 1 == zoomProductHelper.getCurrentVendor()) {
                return false;
            }
        }
        return ZmLoginHelper.isTypeSupportFavoriteContacts(pTLoginType);
    }

    public boolean isKeepMeLoggedIn() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isKeepMeLoggedInImpl();
    }

    public int retryLoginGoogle() {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return retryLoginGoogleImpl();
    }

    public int loginZoom(String str, byte[] bArr, boolean z) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return loginZoomImpl(str, bArr, z);
    }

    public int loginZoomWithLocalTokenForType(int i) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return loginZoomWithLocalTokenForTypeImpl(i);
    }

    public void nos_SetDeviceToken(@Nullable final String str, @Nullable final String str2) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                if (Mainboard.getMainboard() != null && Mainboard.getMainboard().isInitialized()) {
                    PTApp pTApp = PTApp.this;
                    String str = str;
                    if (str == null) {
                        str = "";
                    }
                    String str2 = str2;
                    if (str2 == null) {
                        str2 = "";
                    }
                    pTApp.nosSetDeviceTokenImpl(str, str2);
                }
            }
        });
    }

    public boolean nos_UpdateDeviceToken(@Nullable String str, @Nullable String str2, @Nullable String str3) {
        if (!isInitialForMainboard()) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str3 == null) {
            str3 = "";
        }
        return nosUpdateDeviceTokenImpl(str, str2, str3);
    }

    public boolean nos_NotificationReceived(@Nullable String str, @Nullable String str2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        return nosNotificationReceivedImpl(str, str2);
    }

    public boolean nos_MessageNotificationReceived(int i, @Nullable String str, @Nullable String str2, @Nullable String str3) {
        if (!isInitialForMainboard()) {
            return false;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        if (str3 == null) {
            str3 = "";
        }
        return nosMessageNotificationReceivedImpl(i, str, str2, str3);
    }

    public boolean nos_ClearDeviceToken() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return nosClearDeviceTokenImpl();
    }

    public boolean needDoWebStart() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return needDoWebStartImpl();
    }

    public int launchCallForWebStart() {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return launchCallForWebStartImpl();
    }

    public boolean canAccessZoomWebservice() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return canAccessZoomWebserviceImpl();
    }

    public boolean hasPrescheduleMeeting() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return hasPrescheduleMeetingImpl();
    }

    @Nullable
    public String getZoomInvitationEmailSubject() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getZoomInvitationEmailSubjectImpl();
    }

    @Nullable
    public String getZoomInvitationEmailBody() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getZoomInvitationEmailBodyImpl();
    }

    public boolean signup(String str, String str2, String str3, @Nullable String str4, @Nullable String str5) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str3)) {
            return false;
        }
        return signupImpl(str, str2, str3, str4 == null ? "" : str4, str5);
    }

    public boolean sendActivationEmail(String str, String str2, String str3) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str) && !StringUtil.isEmptyOrNull(str2) && !StringUtil.isEmptyOrNull(str3)) {
            return sendActivationEmailImpl(str, str2, str3);
        }
        return false;
    }

    public boolean forgotPassword(String str) {
        if (isInitialForMainboard() && !StringUtil.isEmptyOrNull(str)) {
            return forgotPasswordImpl(str);
        }
        return false;
    }

    public boolean setPassword(boolean z, String str, String str2, String str3) {
        return setPassword(z, str, str2, str3, null, null);
    }

    public boolean setPassword(boolean z, String str, String str2, String str3, @Nullable String str4, @Nullable String str5) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str3)) {
            return false;
        }
        return setPasswordImpl(z, str, str2, str3, str4 == null ? "" : str4, str5 == null ? "" : str5);
    }

    public boolean isCurrentLoginTypeSupportIM() {
        return getInstance().isFacebookImEnabled() || getInstance().isGoogleImEnabled();
    }

    public boolean isCurrentLoginTypeSupportMyMeetings() {
        return ZmLoginHelper.isTypeSupportMyMeetings(getPTLoginType());
    }

    public boolean isCurrentLoginTypeSupportMyProfileWebPage() {
        return ZmLoginHelper.isTypeSupportMyProfileWebPage(getPTLoginType());
    }

    public boolean isDirectCallAvailable() {
        if (isInitialForMainboard() && hasMessenger()) {
            return true;
        }
        return false;
    }

    @Nullable
    public String getRegisteredPhoneNumber() {
        ABContactsHelper aBContactsHelper = getInstance().getABContactsHelper();
        if (aBContactsHelper != null) {
            return aBContactsHelper.getVerifiedPhoneNumber();
        }
        return null;
    }

    public boolean isPhoneNumberRegistered() {
        return !StringUtil.isEmptyOrNull(getRegisteredPhoneNumber());
    }

    public boolean startMeeting(long j) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return startMeetingImpl(j);
    }

    public void startScheduleConfirm(long j, boolean z) {
        if (isInitialForMainboard()) {
            startScheduleConfirmImpl(j, z);
        }
    }

    public boolean presentToRoom(int i, String str, long j, boolean z) {
        return presentToRoomImpl(i, str, j, z);
    }

    public void stopPresentToRoom(boolean z) {
        setShowPresentToRoomCancelStatus(z);
        if (!presentToRoom(10, "", 0, false) && getInstance().hasActiveCall() && VideoBoxApplication.getNonNullInstance().isConfProcessRunning()) {
            VideoBoxApplication.getNonNullInstance().addConfProcessListener(new IConfProcessListener() {
                public void onConfProcessStarted() {
                }

                public void onConfProcessStopped() {
                    VideoBoxApplication.getNonNullInstance().removeConfProcessListener(this);
                    PTUI.getInstance().presentToRoomStatusUpdate(50);
                }
            });
            forceSyncLeaveCurrentCall(true);
        }
    }

    public void setShowPresentToRoomCancelStatus(boolean z) {
        this.showPresentToRoomCancelStatus = z;
    }

    public boolean isShowPresentToRoomCancelStatus() {
        return this.showPresentToRoomCancelStatus;
    }

    public String getUrlAction() {
        return this.mUrlAction;
    }

    public void setUrlAction(String str) {
        this.mUrlAction = str;
    }

    public boolean isNeedCheckSwitchCall() {
        return this.mNeedCheckSwitchCall;
    }

    public void setNeedCheckSwitchCall(boolean z) {
        if (!z) {
            this.mNeedCheckSwitchCall = false;
        } else if (getInstance().isWebSignedOn()) {
            IPCHelper.getInstance().dispacthPtLoginResultEventToConf();
        } else {
            this.mNeedCheckSwitchCall = true;
        }
    }

    public boolean isNeedFilterCallRoomEventCallbackInMeeting() {
        return this.mNeedFilterCallRoomEventCallbackInMeeting;
    }

    public void setNeedFilterCallRoomEventCallbackInMeeting(boolean z) {
        this.mNeedFilterCallRoomEventCallbackInMeeting = z;
    }

    public String getMeetingListLastDisplayedHostId() {
        return this.mMeetingListLastDisplayedHostId;
    }

    public void refreshMeetingListLastDisplayedHostIdFromDb() {
        ZoomAppPropData instance2 = ZoomAppPropData.getInstance();
        if (instance2 != null) {
            this.mMeetingListLastDisplayedHostId = instance2.queryWithKey(ZoomAppPropData.KEY_MEETINGLIST_FILTER_HOSTID, "");
        }
    }

    @Nullable
    public ZoomAccount getSavedZoomAccount() {
        ZoomAccount zoomAccount;
        if (!isInitialForMainboard()) {
            return null;
        }
        byte[] savedZoomAccountDataImpl = getSavedZoomAccountDataImpl();
        if (savedZoomAccountDataImpl == null) {
            return null;
        }
        try {
            zoomAccount = ZoomAccount.parseFrom(savedZoomAccountDataImpl);
            try {
                if (StringUtil.isEmptyOrNull(zoomAccount.getUserName()) || StringUtil.isEmptyOrNull(zoomAccount.getToken())) {
                    return null;
                }
                return zoomAccount;
            } catch (InvalidProtocolBufferException unused) {
            }
        } catch (InvalidProtocolBufferException unused2) {
            zoomAccount = null;
        }
    }

    public void userInputUsernamePasswordForProxy(String str, int i, String str2, String str3, boolean z) {
        if (isInitialForMainboard()) {
            userInputUsernamePasswordForProxyImpl(str, i, str2, str3, z);
        }
    }

    public int getAuthInfo(int i, String str, int i2, @Nullable String[] strArr, @Nullable String[] strArr2) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str) || i2 <= 0 || strArr == null || strArr.length == 0 || strArr2 == null || strArr2.length == 0) {
            return 0;
        }
        return getAuthInfoImpl(i, str, i2, strArr, strArr2);
    }

    public int navWebWithDefaultBrowser(int i, @Nullable String str) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        if (str == null) {
            str = "";
        }
        return navWebWithDefaultBrowserImpl(i, str);
    }

    public int loginWithSSOToken(@Nullable String str) {
        if (isInitialForMainboard() && str != null) {
            return loginWithSSOTokenImpl(str);
        }
        return 1;
    }

    public int loginWithSSOKMSToken(@Nullable String str, @Nullable String str2, @Nullable String str3) {
        if (isInitialForMainboard() && str != null) {
            return loginWithSSOKMSTokenImpl(str, str2, str3);
        }
        return 1;
    }

    public int loginSSOWithLocalToken() {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return loginSSOWithLocalTokenImpl();
    }

    @Nullable
    public ZMNativeSsoCloudInfo getSSOCloudInfo() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getSSOCloudInfoImpl();
    }

    public void setSSOURL(@Nullable String str, int i) {
        if (isInitialForMainboard()) {
            if (str == null) {
                str = "";
            }
            setSSOURLImpl(str, i);
        }
    }

    @Nullable
    public String getLatestVersionString() {
        Mainboard mainboard = Mainboard.getMainboard();
        return (mainboard == null || !mainboard.isInitialized()) ? "" : getLatestVersionStringImpl();
    }

    @Nullable
    public String getLatestVersionReleaseNote() {
        Mainboard mainboard = Mainboard.getMainboard();
        return (mainboard == null || !mainboard.isInitialized()) ? "" : getLatestVersionReleaseNoteImpl();
    }

    public void setLastUpdateNotesDisplayTime(long j) {
        this.mLastUpdateNotesDisplayTime = j;
    }

    public long getLastUpdateNotesDisplayTime() {
        return this.mLastUpdateNotesDisplayTime;
    }

    public boolean checkForUpdates(boolean z) {
        return checkForUpdates(z, false);
    }

    public boolean checkForUpdates(boolean z, boolean z2) {
        Mainboard mainboard = Mainboard.getMainboard();
        if (mainboard == null || !mainboard.isInitialized()) {
            return false;
        }
        if (z) {
            setLastUpdateNotesDisplayTime(0);
        }
        return checkForUpdatesImpl(true, z2);
    }

    public void setDeviceUserName(String str) {
        if (isInitialForMainboard()) {
            PreferenceUtil.saveBooleanValue(PreferenceUtil.IS_DEVICE_NAME_CUSTOMIZED, true);
            setDeviceUserNameImpl(str);
        }
    }

    @Nullable
    public String getDeviceUserName() {
        if (!isInitialForMainboard()) {
            return "";
        }
        return getDeviceUserNameImpl();
    }

    public void logUICommand(String str, String str2, String str3) {
        if (isInitialForMainboard()) {
            logUICommandImpl(str, str2, str3);
        }
    }

    public boolean user_UploadMyPicture(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return userUploadMyPictureImpl(str);
    }

    public boolean user_UpdateMyName(String str, String str2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return userUpdateMyNameImpl(str, str2);
    }

    public int getAltHostCount() {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return getAltHostCountImpl();
    }

    @Nullable
    public AlterHost getAltHostAt(int i) {
        AlterHost alterHost = null;
        if (!isInitialForMainboard()) {
            return null;
        }
        byte[] altHostAtImpl = getAltHostAtImpl(i);
        if (altHostAtImpl == null || altHostAtImpl.length == 0) {
            return null;
        }
        try {
            alterHost = AlterHost.parseFrom(altHostAtImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return alterHost;
    }

    public boolean isPaidUser() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPaidUserImpl();
    }

    public boolean isCorpUser() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isCorpUserImpl();
    }

    public int getMaxChatGroupBuddyNumber() {
        return isPaidUser() ? 2000 : 200;
    }

    @Nullable
    public String getVendorRegion() {
        ZoomProductHelper zoomProductHelper = getZoomProductHelper();
        if (zoomProductHelper == null || zoomProductHelper.getCurrentVendor() != 1) {
            return null;
        }
        return Locale.CHINA.getCountry();
    }

    @Nullable
    public String getRegionCodeForNameFormating() {
        if (ZMBuildConfig.BUILD_TARGET == 0) {
            return getVendorRegion();
        }
        return ResourcesUtil.getString((Context) VideoBoxApplication.getInstance(), C4558R.string.zm_config_region_code_for_name_formating);
    }

    @Nullable
    public String getPackageCheckSum() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getPackageCheckSumImpl();
    }

    @Nullable
    public String getPackageDownloadUrl() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getPackageDownloadUrlImpl();
    }

    @Nullable
    public String getPackageName() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getPackageNameImpl();
    }

    public boolean isCallOutInProgress(@Nullable int[] iArr) {
        if (!isInitialForMainboard()) {
            return false;
        }
        if (iArr == null || iArr.length == 0) {
            iArr = new int[1];
        }
        return isCallOutInProgressImpl(iArr);
    }

    public int getCallOutStatus() {
        int[] iArr = new int[1];
        isCallOutInProgress(iArr);
        return iArr[0];
    }

    public boolean inviteCallOutUser(String str, @Nullable String str2) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        if (str2 == null) {
            str2 = "";
        }
        return inviteCallOutUserImpl(str, str2);
    }

    public boolean cancelCallOut() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return cancelCallOutImpl();
    }

    public boolean isFeedbackOff() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isFeedbackOffImpl();
    }

    public boolean isCNMeetingON() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isCNMeetingONImpl();
    }

    @Nullable
    public String getURLByType(int i) {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getURLByTypeImpl(i);
    }

    @Nullable
    public String getMyName() {
        String str = "";
        PTUserProfile currentUserProfile = getCurrentUserProfile();
        return currentUserProfile != null ? currentUserProfile.getUserName() : str;
    }

    @Nullable
    public String getZoomDomain() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getZoomDomainImpl();
    }

    public void configZoomDomain(String str) {
        if (isInitialForMainboard()) {
            configZoomDomainImpl(str);
        }
    }

    public boolean sdk_Auth(String str, String str2) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return sdkAuthImpl(str, str2);
    }

    public boolean isSdkNeedWaterMark() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isSdkNeedWaterMarkImpl();
    }

    public int getSdkAuthResult() {
        if (!isInitialForMainboard()) {
            return -1;
        }
        return getSdkAuthResultImpl();
    }

    public void configSdkDSCP(int i, int i2, boolean z) {
        if (isInitialForMainboard()) {
            configSDKDSCPImpl(i, i2, z);
        }
    }

    public void enableHideFullPhoneNumber4PureCallinUser(boolean z) {
        if (isInitialForMainboard()) {
            enableHideFullPhoneNumber4PureCallinUserImpl(z);
        }
    }

    public void setLanguageID(String str) {
        if (isInitialForMainboard()) {
            setLanguageIDImpl(str);
        }
    }

    @Nullable
    public String getH323Gateway() {
        if (!isMainboardInitialized()) {
            return "";
        }
        return getH323GatewayImpl();
    }

    public long getH323AccessCode() {
        if (!isMainboardInitialized()) {
            return 0;
        }
        return getH323AccessCodeImpl();
    }

    @Nullable
    public String getH323Password() {
        if (!isMainboardInitialized()) {
            return "";
        }
        return getH323PasswordImpl();
    }

    public void setLanguageIdAsSystemConfiguration() {
        Locale localDefault = CompatUtils.getLocalDefault();
        StringBuilder sb = new StringBuilder();
        sb.append(localDefault.getLanguage());
        sb.append("-");
        sb.append(localDefault.getCountry());
        setLanguageID(sb.toString());
    }

    public boolean isPublicGmailUser() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPublicGmailUserImpl();
    }

    public boolean isAutoReponseON() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isAutoReponseONImpl();
    }

    @Nullable
    public String querySSOVanityURL(String str) {
        if (!isInitialForMainboard()) {
            return null;
        }
        return querySSOVanityURLImpl(str);
    }

    @Nullable
    public IPLocationInfo getIPLocation(boolean z) {
        if (!isInitialForMainboard()) {
            return null;
        }
        byte[] iPLocationImpl = getIPLocationImpl(z);
        if (iPLocationImpl == null || iPLocationImpl.length == 0) {
            return null;
        }
        try {
            return IPLocationInfo.parseFrom(iPLocationImpl);
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean nos_SetDeviceInfo(@Nullable byte[] bArr) {
        if (isInitialForMainboard() && bArr != null) {
            return nosSetDeviceInfoImpl(bArr);
        }
        return false;
    }

    @Nullable
    public MMFileContentMgr getZoomFileContentMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long zoomFileContentMgrImpl = getZoomFileContentMgrImpl();
        if (zoomFileContentMgrImpl != 0) {
            return new MMFileContentMgr(zoomFileContentMgrImpl);
        }
        return null;
    }

    @Nullable
    public SearchMgr getSearchMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long searchMgrImpl = getSearchMgrImpl();
        if (searchMgrImpl != 0) {
            return new SearchMgr(searchMgrImpl);
        }
        return null;
    }

    @Nullable
    public MMPrivateStickerMgr getZoomPrivateStickerMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long zoomPrivateStickerMgrImpl = getZoomPrivateStickerMgrImpl();
        if (zoomPrivateStickerMgrImpl != 0) {
            return new MMPrivateStickerMgr(zoomPrivateStickerMgrImpl);
        }
        return null;
    }

    public void VTLSConfirmAcceptCertItem(VerifyCertEvent verifyCertEvent, boolean z, boolean z2) {
        if (isInitialForMainboard()) {
            VTLSConfirmAcceptCertItemImpl(verifyCertEvent, z, z2);
        }
    }

    public boolean isSyncUserGroupON() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isSyncUserGroupONImpl();
    }

    public boolean isKeepCompanyContacts() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isKeepCompanyContactsImpl();
    }

    @Nullable
    public String modifyCountryCode(String str) {
        if (!isInitialForMainboard()) {
            return "";
        }
        return modifyCountryCodeImpl(str);
    }

    @Nullable
    public String modifyVanityUrl(String str) {
        if (!isInitialForMainboard()) {
            return "";
        }
        return modifyVanityUrlImpl(str);
    }

    @Nullable
    public String changeUserPassword(String str, String str2) {
        if (!isInitialForMainboard()) {
            return "";
        }
        return changeUserPasswordImpl(str, str2);
    }

    public boolean isPremiumFeatureEnabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isPremiumFeatureEnabledImpl();
    }

    public boolean isChangeNameEnabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isChangeNameEnabledImpl();
    }

    public boolean isChangePictureEnabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isChangePictureEnabledImpl();
    }

    public boolean isImportPhotosFromDeviceEnable() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isImportPhotosFromDeviceEnableImpl();
    }

    @Nullable
    public FileInfoChecker getZoomFileInfoChecker() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long zoomFileInfoCheckerImpl = getZoomFileInfoCheckerImpl();
        if (zoomFileInfoCheckerImpl == 0) {
            return null;
        }
        return new FileInfoChecker(zoomFileInfoCheckerImpl);
    }

    public boolean isFileTransferDisabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isFileTransferDisabledImpl();
    }

    @Nullable
    public String getMyLocalAddress() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getMyLocalAddressImpl();
    }

    @Nullable
    public CallHistoryMgr getCallHistoryMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long callHistoryMgrImpl = getCallHistoryMgrImpl();
        if (callHistoryMgrImpl == 0) {
            return null;
        }
        return new CallHistoryMgr(callHistoryMgrImpl);
    }

    public boolean updateSipPhoneStatus(@Nullable SIPConfiguration sIPConfiguration) {
        if (sIPConfiguration == null || StringUtil.isEmptyOrNull(sIPConfiguration.userName) || !isInitialForMainboard()) {
            return false;
        }
        Builder newBuilder = SipPhoneIntegration.newBuilder();
        newBuilder.setAuthoriztionName(sIPConfiguration.authName);
        newBuilder.setDomain(sIPConfiguration.domain);
        newBuilder.setErrorCode(sIPConfiguration.respCode);
        newBuilder.setErrorString(sIPConfiguration.respDescription == null ? "" : sIPConfiguration.respDescription);
        newBuilder.setPassword(sIPConfiguration.userPassword);
        newBuilder.setProtocol(sIPConfiguration.protocol);
        newBuilder.setProxyServer(sIPConfiguration.proxy);
        newBuilder.setRegisterServer(sIPConfiguration.regServerAddress);
        newBuilder.setStatus(sIPConfiguration.status);
        newBuilder.setUserName(sIPConfiguration.userName);
        return updateSipPhoneStatusImpl(newBuilder.build().toByteArray());
    }

    public boolean isSipPhoneEnabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isSipPhoneEnabledImpl();
    }

    public boolean isCloudPBXEnable() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isCloudPBXEnableImpl();
    }

    @Nullable
    public NotificationSettingMgr getNotificationSettingMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long notificationSettingMgrImpl = getNotificationSettingMgrImpl();
        if (notificationSettingMgrImpl == 0) {
            return null;
        }
        return new NotificationSettingMgr(notificationSettingMgrImpl);
    }

    @Nullable
    public CrawlerLinkPreview getLinkCrawler() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long linkCrawlerImpl = getLinkCrawlerImpl();
        if (linkCrawlerImpl == 0) {
            return null;
        }
        return new CrawlerLinkPreview(linkCrawlerImpl);
    }

    @Nullable
    public ISIPCallAPI getSipCallAPI() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long sipCallAPIImpl = getSipCallAPIImpl();
        if (sipCallAPIImpl == 0) {
            return null;
        }
        return new ISIPCallAPI(sipCallAPIImpl);
    }

    @Nullable
    public ZMPhoneNumberHelper getZMPhoneNumberHelper() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long zMPhoneNumberHelperImpl = getZMPhoneNumberHelperImpl();
        if (zMPhoneNumberHelperImpl == 0) {
            return null;
        }
        return new ZMPhoneNumberHelper(zMPhoneNumberHelperImpl);
    }

    public boolean beforeLogout() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return beforeLogoutImpl();
    }

    @Nullable
    public UnSupportMessageMgr getUnsupportMessageMgr() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long unsupportMessageMgrImpl = getUnsupportMessageMgrImpl();
        if (unsupportMessageMgrImpl == 0) {
            return null;
        }
        return new UnSupportMessageMgr(unsupportMessageMgrImpl);
    }

    @Nullable
    public String getZMCID() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getZMCIDImpl();
    }

    public int sendSMSCodeForLogin(String str, String str2) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return sendSMSCodeForLoginImpl(str, str2);
    }

    public int loginWithPhoneSms(String str, String str2, byte[] bArr, boolean z) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return loginWithPhoneSmsImpl(str, str2, bArr, z);
    }

    public int loginWithPhonePasswd(String str, String str2, byte[] bArr, boolean z) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return loginWithPhonePasswdImpl(str, str2, bArr, z);
    }

    public int loginWithRingCentral(String str, @Nullable String str2, String str3, int i, boolean z) {
        if (!BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET)) {
            return 0;
        }
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str3)) {
            return 1;
        }
        return loginWithRingCentralImpl(str, str2 == null ? "" : str2, str3, i, z);
    }

    public int loginRingCentralWithLocalToken() {
        if (!BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET)) {
            return 0;
        }
        if (!isInitialForMainboard()) {
            return 1;
        }
        if (!isAuthenticating() || getPTLoginType() != 98) {
            return loginRingCentralWithLocalTokenImpl();
        }
        return 0;
    }

    public int getSavedRingCentralPhoneNumberAndExt(@Nullable String[] strArr, @Nullable String[] strArr2) {
        if (!BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET) || !isInitialForMainboard() || strArr == null || strArr.length == 0 || strArr2 == null || strArr2.length == 0) {
            return -1;
        }
        return getSavedRingCentralPhoneNumberAndExtImpl(strArr, strArr2);
    }

    public int RC_getDefaultCountryTypeByName(@Nullable String str) {
        if (!BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET) || !isInitialForMainboard() || str == null) {
            return -1;
        }
        return RCgetDefaultCountryTypeByNameImpl(str);
    }

    @Nullable
    public String RC_getCountryCodeByCountryType(int i) {
        if (!BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET) || !isInitialForMainboard()) {
            return null;
        }
        return RCgetCountryCodeByCountryTypeImpl(i);
    }

    public void RC_setCountryType(int i) {
        if (BuildTarget.isRingCentralLogin(ZMBuildConfig.BUILD_TARGET) && isInitialForMainboard()) {
            RCsetCountryTypeImpl(i);
        }
    }

    private boolean isMainboardInitialized() {
        return isInitialForMainboard();
    }

    @NonNull
    public ArrayList<CmmSavedMeeting> getSavedMeetingList() {
        ArrayList<CmmSavedMeeting> arrayList = new ArrayList<>();
        if (!isInitialForMainboard()) {
            return arrayList;
        }
        CmmSavedMeeting[] savedMeetingListImpl = getSavedMeetingListImpl();
        if (savedMeetingListImpl == null || savedMeetingListImpl.length <= 0) {
            return arrayList;
        }
        for (CmmSavedMeeting cmmSavedMeeting : savedMeetingListImpl) {
            if (cmmSavedMeeting != null) {
                arrayList.add(cmmSavedMeeting);
            }
        }
        return arrayList;
    }

    public void clearSavedMeetingList() {
        if (isInitialForMainboard()) {
            clearSavedMeetingListImpl();
        }
    }

    public boolean confirmGDPR(boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return confirmGDPRImpl(z);
    }

    private boolean isInitialForMainboard() {
        Mainboard mainboard = Mainboard.getMainboard();
        return mainboard != null && mainboard.isInitialized();
    }

    public void getCalendarIntegrationConfig() {
        if (isInitialForMainboard()) {
            getCalendarIntegrationConfigImpl();
        }
    }

    @Nullable
    public String getWebDomain() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getWebDomainImpl();
    }

    @Nullable
    public MonitorLogService getMonitorLogService() {
        if (!isInitialForMainboard()) {
            return null;
        }
        long monitorLogServiceImpl = getMonitorLogServiceImpl();
        if (monitorLogServiceImpl == 0) {
            return null;
        }
        return new MonitorLogService(monitorLogServiceImpl);
    }

    public void onCancelReloginAndRejoin() {
        if (isInitialForMainboard()) {
            onCancelReloginAndRejoinImpl();
        }
    }

    public int getFreeMeetingGiftTime() {
        if (!isInitialForMainboard()) {
            return -1;
        }
        return getFreeMeetingGiftTimeImpl();
    }

    @Nullable
    public String getGiftUpgradeUrl() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getGiftUpgradeUrlImpl();
    }

    @Nullable
    public String getMarketplaceURL() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getMarketplaceURLImpl();
    }

    public boolean isStartVideoCallWithRoomSystemEnabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isStartVideoCallWithRoomSystemEnabledImpl();
    }

    public void setVideoCallWithRoomSystemPrepareStatus(boolean z) {
        if (isInitialForMainboard()) {
            setVideoCallWithRoomSystemPrepareStatusImpl(z);
        }
    }

    public boolean getRoomSystemList(int i, int i2, List<RoomDevice> list) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return getRoomSystemListImpl(i, i2, list);
    }

    public int startVideoCallWithRoomSystem(RoomDevice roomDevice, int i, long j) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return startVideoCallWithRoomSystemImpl(roomDevice, i, j);
    }

    public void setCurrentUIFlag() {
        if (isInitialForMainboard()) {
            setCurrentUIFlagImpl(LoginUtil.getDefaultVendor());
        }
    }

    @Nullable
    public ZoomMdmPolicyProvider getZoomMdmPolicyProvider() {
        ZoomMdmPolicyProvider zoomMdmPolicyProvider = this.mZoomMdmPolicyProvider;
        if (zoomMdmPolicyProvider != null) {
            return zoomMdmPolicyProvider;
        }
        if (!isInitialForMainboard()) {
            return null;
        }
        long policyProviderHandleImpl = getPolicyProviderHandleImpl();
        if (policyProviderHandleImpl == 0) {
            return null;
        }
        this.mZoomMdmPolicyProvider = new ZoomMdmPolicyProvider(policyProviderHandleImpl);
        return this.mZoomMdmPolicyProvider;
    }

    public boolean isEnableCloudSwitch() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isEnableCloudSwitchImpl();
    }

    public boolean getAllRoomSystemList(int i, List<RoomDevice> list) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return getAllRoomSystemListImpl(i, list);
    }

    public int joinMeetingBySpecialMode(int i, long j, String str, String str2) {
        if (!isInitialForMainboard()) {
            return 1;
        }
        return joinMeetingBySpecialModeImpl(i, j, str, str2);
    }

    public boolean isJoinMeetingBySpecialModeEnabled(int i) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isJoinMeetingBySpecialModeEnabledImpl(i);
    }

    public boolean isShareScreenNeedDisabled() {
        return !isWebSignedOn() || ZMPolicyUIHelper.isDisableDirectShare() || isShareDesktopDisabled();
    }

    public boolean isShareDesktopDisabled() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isShareDesktopDisabledImpl();
    }

    public boolean uploadCrashDumpFile(boolean z, int i, String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return uploadCrashDumpFileImpl(z, i, str);
    }

    public boolean uploadFeedback(int i, int i2, long j, String str, String str2, boolean z) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return uploadFeedbackImpl(i, i2, j, str, str2, z);
    }

    public int parseZoomAction(String str) {
        if (!isInitialForMainboard()) {
            return 0;
        }
        return parseZoomActionImpl(str);
    }

    @Nullable
    public String getMinClientVersion() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getMinClientVersionImpl();
    }

    public boolean isTaiWanZH() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return isTaiWanZHImpl();
    }

    public void userAgreeLoginDisclaimer() {
        if (isInitialForMainboard()) {
            userAgreeLoginDisclaimerImpl();
        }
    }

    public void userDisagreeLoginDisclaimer() {
        if (isInitialForMainboard()) {
            userDisagreeLoginDisclaimerImpl();
        }
    }

    public void onUserSkipSignToJoinOption() {
        if (isInitialForMainboard()) {
            onUserSkipSignToJoinOptionImpl();
        }
    }

    public String getFmtRestrictedLoginDomain() {
        return getFmtRestrictedLoginDomainImpl();
    }

    public int requestOAuthTokenWithCode(int i, String str, @NonNull String str2) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str)) {
            return 1;
        }
        return requestOAuthTokenWithCodeImpl(i, str, str2);
    }

    public int loginWithOAuthToken(int i, String str, String str2, @NonNull byte[] bArr) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str)) {
            return 1;
        }
        return loginWithOAuthTokenImpl(i, str, str2, bArr);
    }

    public int loginWithOAuthTokenForRealName(int i, String str, String str2, @NonNull byte[] bArr, @NonNull String str3, @NonNull String str4, @NonNull String str5) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str)) {
            return 1;
        }
        return loginWithOAuthTokenForRealNameImpl(i, str, str2, bArr, str3, str4, str5);
    }

    public int loginWithLocalOAuthToken(int i, String str, String str2) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str)) {
            return 1;
        }
        return loginWithLocalOAuthTokenImpl(i, str, str2);
    }

    public int loginWithLocalOAuthTokenForRealName(int i, String str, @NonNull String str2, @NonNull String str3, @NonNull String str4, @NonNull String str5) {
        if (!isInitialForMainboard() || StringUtil.isEmptyOrNull(str)) {
            return 1;
        }
        return loginWithLocalOAuthTokenForRealNameImpl(i, str, str2, str3, str4, str5);
    }

    @Nullable
    public String getLoginAuthDisplayName() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getLoginAuthDisplayNameImpl();
    }

    public String getLoginAuthPicUrl() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getLoginAuthPicUrlImpl();
    }

    public String getLoginAuthOpenId() {
        if (!isInitialForMainboard()) {
            return null;
        }
        return getLoginAuthOpenIdImpl();
    }

    public boolean needRealNameAuth() {
        if (!isInitialForMainboard()) {
            return false;
        }
        return needRealNameAuthImpl();
    }

    public boolean getCloudSwitchList(List<String> list) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return getCloudSwitchListImpl(list);
    }

    public int confirmAgeGating(boolean z, int i, String str) {
        if (!isInitialForMainboard()) {
            return SBWebServiceErrorCode.SB_ERROR_NOT_LEGAL_AGE;
        }
        return confirmAgeGatingImpl(z, i, str);
    }

    public boolean checkAgeGating(String str) {
        if (!isInitialForMainboard()) {
            return false;
        }
        return checkAgeGatingImpl(str);
    }
}
