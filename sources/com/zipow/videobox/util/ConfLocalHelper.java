package com.zipow.videobox.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zipow.cmmlib.ZoomAppPropData;
import com.zipow.videobox.ConfActivity;
import com.zipow.videobox.ConfActivityNormal;
import com.zipow.videobox.IntegrationActivity;
import com.zipow.videobox.VideoBoxApplication;
import com.zipow.videobox.common.LeaveConfAction;
import com.zipow.videobox.common.ZMConfiguration;
import com.zipow.videobox.confapp.AudioSessionMgr;
import com.zipow.videobox.confapp.CmmConfContext;
import com.zipow.videobox.confapp.CmmConfStatus;
import com.zipow.videobox.confapp.CmmUser;
import com.zipow.videobox.confapp.CmmUserList;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.confapp.ConfMgr;
import com.zipow.videobox.confapp.ConfUI;
import com.zipow.videobox.confapp.CustomizeInfo;
import com.zipow.videobox.confapp.InterpretationMgr;
import com.zipow.videobox.confapp.RecordMgr;
import com.zipow.videobox.confapp.ShareSessionMgr;
import com.zipow.videobox.confapp.TipMessageType;
import com.zipow.videobox.confapp.VideoSessionMgr;
import com.zipow.videobox.confapp.ZoomRaiseHandInWebinar;
import com.zipow.videobox.confapp.component.ZMConfEnumViewMode;
import com.zipow.videobox.confapp.meeting.ConfParams;
import com.zipow.videobox.confapp.meeting.confhelper.ConfDataHelper;
import com.zipow.videobox.confapp.p009bo.BOMgr;
import com.zipow.videobox.confapp.p010qa.ZoomQABuddy;
import com.zipow.videobox.confapp.param.ZMConfIntentParam;
import com.zipow.videobox.confapp.poll.PollingMgr;
import com.zipow.videobox.dialog.ConfShowCallingMeDialog;
import com.zipow.videobox.dialog.SwitchOutputAudioDialog;
import com.zipow.videobox.dialog.ZMAlertConnectAudioDialog;
import com.zipow.videobox.dialog.ZMRecordingStartDisclaimerDialog;
import com.zipow.videobox.fragment.CallMeByPhoneFragment;
import com.zipow.videobox.fragment.ConfChatFragment;
import com.zipow.videobox.fragment.SelectCountryCodeFragment.CountryCodeItem;
import com.zipow.videobox.fragment.SimpleMessageDialog;
import com.zipow.videobox.fragment.meeting.p011qa.ZMQAAttendeeViewerFragment;
import com.zipow.videobox.fragment.meeting.p011qa.ZMQAPanelistViewerFragment;
import com.zipow.videobox.monitorlog.ZMConfEventTracking;
import com.zipow.videobox.poll.IPollingDoc;
import com.zipow.videobox.ptapp.DummyPolicyIDType;
import com.zipow.videobox.ptapp.MeetingHelper;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost;
import com.zipow.videobox.ptapp.MeetingInfoProtos.AlterHost.Builder;
import com.zipow.videobox.ptapp.MeetingInfoProtos.MeetingInfoProto;
import com.zipow.videobox.ptapp.MeetingInfoProtos.UserPhoneInfo;
import com.zipow.videobox.ptapp.MeetingInfoProtos.UserPhoneInfoList;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTSettingHelper;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.sip.CmmSIPCallFailReason;
import com.zipow.videobox.util.ZMPolicyDataHelper.BooleanQueryResult;
import com.zipow.videobox.view.ConfToolbar;
import com.zipow.videobox.view.IMAddrBookItem;
import com.zipow.videobox.view.NormalMessageTip;
import com.zipow.videobox.view.PListItem;
import com.zipow.videobox.view.PListView.StatusPListItem;
import com.zipow.videobox.view.ScheduledMeetingItem;
import com.zipow.videobox.view.video.AbsVideoScene;
import com.zipow.videobox.view.video.AbsVideoSceneMgr;
import com.zipow.videobox.view.video.ShareVideoScene;
import com.zipow.videobox.view.video.VideoLayoutHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.webrtc.voiceengine.VoiceEnginContext;
import org.webrtc.voiceengine.VoiceEngineCompat;
import p021us.zipow.mdm.ZMPolicyUIHelper;
import p021us.zoom.androidlib.C4409R;
import p021us.zoom.androidlib.app.ZMActivity;
import p021us.zoom.androidlib.app.ZMFragment;
import p021us.zoom.androidlib.util.AccessibilityUtil;
import p021us.zoom.androidlib.util.AndroidAppUtil;
import p021us.zoom.androidlib.util.HeadsetUtil;
import p021us.zoom.androidlib.util.NetworkUtil;
import p021us.zoom.androidlib.util.PhoneNumberUtil;
import p021us.zoom.androidlib.util.StringUtil;
import p021us.zoom.androidlib.util.UIUtil;
import p021us.zoom.androidlib.util.ZMIntentUtil;
import p021us.zoom.androidlib.widget.ZMAlertDialog;
import p021us.zoom.videomeetings.C4558R;

public class ConfLocalHelper {
    private static final String TAG = "com.zipow.videobox.util.ConfLocalHelper";

    public static int errorCodeToLeaveReason(int i) {
        switch (i) {
            case 1:
                return 11;
            case 3:
                return 12;
            case 5:
                return 19;
            case 6:
                return 14;
            case 8:
                return 15;
            case 9:
                return 10;
            case 11:
                return 13;
            case 12:
                return 20;
            case 13:
                return 17;
            case 14:
                return 18;
            case 15:
                return 5;
            case 16:
                return 28;
            case 19:
                return 21;
            case 20:
                return 22;
            case 21:
                return 23;
            case 22:
                return 24;
            case 23:
                return 26;
            default:
                return 30;
        }
    }

    public static boolean getEnabledDrivingMode() {
        return false;
    }

    public static int getOrientation(int i) {
        if (i > 350 || i < 10) {
            return 0;
        }
        if (i > 80 && i < 100) {
            return 90;
        }
        if (i > 170 && i < 190) {
            return 180;
        }
        if (i <= 260 || i >= 280) {
            return -1;
        }
        return SubsamplingScaleImageView.ORIENTATION_270;
    }

    private ConfLocalHelper() {
    }

    public static void refreshViewOnlyToolbar(@NonNull ConfToolbar confToolbar, @Nullable CmmUser cmmUser) {
        ConfMgr instance = ConfMgr.getInstance();
        boolean isConfConnected = instance.isConfConnected();
        int i = ConfToolbar.BUTTON_VIEWONLY;
        if (isConfConnected) {
            CmmConfContext confContext = instance.getConfContext();
            if (confContext != null) {
                if (cmmUser != null) {
                    CmmAudioStatus audioStatusObj = cmmUser.getAudioStatusObj();
                    if (audioStatusObj != null) {
                        i = audioStatusObj.getAudiotype() == 2 ? 450 : cmmUser.isViewOnlyUserCanTalk() ? CmmSIPCallFailReason.kSIPCall_FAIL_482_Loop_Detected : CmmSIPCallFailReason.kSIPCall_FAIL_480_Temporarily_Unavailable;
                    }
                }
                if (confContext.isQANDAOFF()) {
                    i &= -129;
                }
                if (confContext.isChatOff()) {
                    i &= -257;
                } else {
                    int[] unreadChatMessageIndexes = instance.getUnreadChatMessageIndexes();
                    if (unreadChatMessageIndexes != null) {
                        confToolbar.setChatsButton(unreadChatMessageIndexes.length);
                    }
                }
                CmmConfStatus confStatusObj = instance.getConfStatusObj();
                if (confStatusObj != null && !confStatusObj.isAllowRaiseHand()) {
                    i &= -65;
                }
            } else {
                return;
            }
        }
        confToolbar.setButtons(i);
    }

    public static boolean shouldExcludeMsgSender(@Nullable ConfActivity confActivity, long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null || confStatusObj.isMyself(j) || confActivity == null) {
            return true;
        }
        ConfChatFragment confChatFragment = ConfChatFragment.getConfChatFragment(confActivity.getSupportFragmentManager());
        if (confChatFragment != null) {
            return confStatusObj.isSameUser(j, confChatFragment.getUserId());
        }
        return false;
    }

    public static boolean isAudioConnected() {
        boolean z = false;
        if (ConfMgr.getInstance().getAudioObj() == null) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return false;
        }
        if (2 != audioStatusObj.getAudiotype()) {
            z = true;
        }
        return z;
    }

    public static boolean isAudioMuted() {
        if (ConfMgr.getInstance().getAudioObj() == null) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return false;
        }
        return audioStatusObj.getIsMuted();
    }

    public static boolean isAudioUnMuted() {
        if (ConfMgr.getInstance().getAudioObj() == null) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null || audioStatusObj.getAudiotype() == 2) {
            return false;
        }
        return !audioStatusObj.getIsMuted();
    }

    public static boolean connectVoIP() {
        if (ConfMgr.getInstance().getAudioObj() == null) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return false;
        }
        long audiotype = audioStatusObj.getAudiotype();
        if (0 == audiotype) {
            return false;
        }
        if (1 == audiotype) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                confStatusObj.hangUp();
            }
        }
        return turnOnOffAudioSession(true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x001d A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x001e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean turnOnOffAudioSession(boolean r3) {
        /*
            r0 = 0
            if (r3 == 0) goto L_0x0012
            com.zipow.videobox.VideoBoxApplication r1 = com.zipow.videobox.VideoBoxApplication.getNonNullInstance()
            com.zipow.videobox.IPTService r1 = r1.getPTService()
            if (r1 == 0) goto L_0x0012
            boolean r1 = r1.disablePhoneAudio()     // Catch:{ Exception -> 0x0012 }
            goto L_0x0013
        L_0x0012:
            r1 = 0
        L_0x0013:
            com.zipow.videobox.confapp.ConfMgr r2 = com.zipow.videobox.confapp.ConfMgr.getInstance()
            com.zipow.videobox.confapp.AudioSessionMgr r2 = r2.getAudioObj()
            if (r2 != 0) goto L_0x001e
            return r0
        L_0x001e:
            boolean r0 = r2.turnOnOffAudioSession(r3)
            if (r3 == 0) goto L_0x002d
            if (r1 == 0) goto L_0x002d
            com.zipow.videobox.confapp.ConfUI r3 = com.zipow.videobox.confapp.ConfUI.getInstance()
            r3.tryRetrieveMicrophone()
        L_0x002d:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zipow.videobox.util.ConfLocalHelper.turnOnOffAudioSession(boolean):boolean");
    }

    public static void switchAudio(@NonNull ZMActivity zMActivity) {
        if (HeadsetUtil.getInstance().isBTAndWiredHeadsetsOn()) {
            SwitchOutputAudioDialog.showDialog(zMActivity.getSupportFragmentManager());
        } else {
            switchAudioSource(zMActivity);
        }
    }

    public static void switchAudioSource(@NonNull Context context) {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
            boolean z = selectedPlayerStreamType == 0 || (selectedPlayerStreamType < 0 && ConfUI.getInstance().isCallOffHook());
            boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(context);
            boolean z2 = HeadsetUtil.getInstance().isBluetoothHeadsetOn() || HeadsetUtil.getInstance().isWiredHeadsetOn();
            if (z && ((isFeatureTelephonySupported || z2) && (ConfUI.getInstance().isCallOffHook() || getMyAudioType() == 0))) {
                if (!audioObj.getLoudSpeakerStatus() || (HeadsetUtil.getInstance().isBluetoothScoAudioOn() && VoiceEngineCompat.isBluetoothScoSupported())) {
                    toggleSpeakerPhone(true);
                } else {
                    toggleSpeakerPhone(false);
                }
            }
        }
    }

    public static boolean needShowJoinLeaveTip() {
        boolean z;
        boolean z2;
        BooleanQueryResult queryBooleanPolicy = ZMPolicyDataHelper.getInstance().queryBooleanPolicy(DummyPolicyIDType.zPolicy_ShowJoinLeaveTip);
        if (!queryBooleanPolicy.isSuccess()) {
            z = false;
        } else {
            z = queryBooleanPolicy.getResult();
        }
        if (!z || ConfMgr.getInstance().isPutOnHoldOnEntryOn() || ConfMgr.getInstance().isViewOnlyMeeting()) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            z2 = false;
        } else {
            z2 = myself.isViewOnlyUser();
        }
        if (!z2 && !isDirectShareClient()) {
            return true;
        }
        return false;
    }

    public static boolean canControlWaitingRoom() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (myself == null || confStatusObj == null) {
            return false;
        }
        if (!(myself.isHostCoHost() && !myself.inSilentMode()) && !confStatusObj.canIAdmitOthersWhenNoHost()) {
            return false;
        }
        return true;
    }

    public static boolean isInBOMeeting() {
        BOMgr bOMgr = ConfMgr.getInstance().getBOMgr();
        return bOMgr != null && bOMgr.isInBOMeeting();
    }

    public static void switchAudioSource(@NonNull Context context, long j, int i) {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            int selectedPlayerStreamType = VoiceEnginContext.getSelectedPlayerStreamType();
            boolean z = selectedPlayerStreamType == 0 || (selectedPlayerStreamType < 0 && ConfUI.getInstance().isCallOffHook());
            boolean isFeatureTelephonySupported = VoiceEngineCompat.isFeatureTelephonySupported(context);
            HeadsetUtil instance = HeadsetUtil.getInstance();
            boolean z2 = instance.isBluetoothHeadsetOn() || instance.isWiredHeadsetOn();
            if (z && ((isFeatureTelephonySupported || z2) && (j == 0 || ConfUI.getInstance().isCallOffHook()))) {
                if ((i == 3 && instance.isBluetoothHeadsetOn()) || i == 2 || i == 1) {
                    audioObj.setPreferedLoudSpeakerStatus(0);
                } else {
                    audioObj.setPreferedLoudSpeakerStatus(1);
                }
                ConfUI.getInstance().changeAudioOutput(i);
            }
        }
    }

    private static void toggleSpeakerPhone(boolean z) {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            audioObj.setPreferedLoudSpeakerStatus(z ? 1 : 0);
            ConfUI.getInstance().checkOpenLoudSpeaker();
        }
    }

    public static void hostAskUnmute() {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                long confOption = confContext.getConfOption();
                audioObj.setMutebySelfFlag(false);
                if (!ConfMgr.getInstance().handleUserCmd(51, 0)) {
                    audioObj.setMutebySelfFlag(confContext.getOldMuteMyselfFlag(confOption));
                }
            }
        }
    }

    public static boolean canChatWithSilentModePeople() {
        ConfMgr instance = ConfMgr.getInstance();
        CmmConfContext confContext = instance.getConfContext();
        CmmUser myself = ConfMgr.getInstance().getMyself();
        BOMgr bOMgr = instance.getBOMgr();
        CmmUserList userList = instance.getUserList();
        boolean z = false;
        if (confContext == null || myself == null || bOMgr == null || userList == null) {
            return false;
        }
        boolean isWebinar = confContext.isWebinar();
        boolean isHostCoHost = myself.isHostCoHost();
        boolean isInBOMeeting = bOMgr.isInBOMeeting();
        boolean isMMRSupportWaitingRoomMsg = confContext.isMMRSupportWaitingRoomMsg();
        boolean supportPutUserinWaitingListUponEntryFeature = confContext.supportPutUserinWaitingListUponEntryFeature();
        int silentModeUserCount = userList.getSilentModeUserCount();
        if (!isWebinar && !isInBOMeeting && isHostCoHost && supportPutUserinWaitingListUponEntryFeature && isMMRSupportWaitingRoomMsg && silentModeUserCount > 0) {
            z = true;
        }
        return z;
    }

    public static void showTipForUserAction(@NonNull ConfActivity confActivity, int i) {
        if (i == 5) {
            NormalMessageTip.show(confActivity.getSupportFragmentManager(), TipMessageType.TIP_AUDIO_MUTED_BY_HOST.name(), (String) null, confActivity.getString(C4558R.string.zm_msg_muted_by_host_32960), 3000);
        } else if (i == 6) {
            NormalMessageTip.show(confActivity.getSupportFragmentManager(), TipMessageType.TIP_AUDIO_MUTED_BY_HOST_MUTEALL.name(), (String) null, confActivity.getString(C4558R.string.zm_msg_muted_by_host_mute_all_32960), 3000);
        } else if (i == 7) {
            NormalMessageTip.show(confActivity.getSupportFragmentManager(), TipMessageType.TIP_AUDIO_UNMUTED_BY_HOST.name(), (String) null, confActivity.getString(C4558R.string.zm_msg_unmuted_by_host_32960), 3000);
        } else if (i == 8) {
            NormalMessageTip.show(confActivity.getSupportFragmentManager(), TipMessageType.TIP_AUDIO_UNMUTED_BY_HOST_UNMUTEALL.name(), (String) null, confActivity.getString(C4558R.string.zm_msg_unmuted_by_host_unmute_all_32960), 3000);
        }
    }

    public static boolean isViewOnlyButNotSpeakAttendee() {
        return ConfMgr.getInstance().isViewOnlyMeeting() && !isViewOnlyButSpeakAttendee();
    }

    public static boolean isViewOnlyButNotSupportMMR() {
        return ConfMgr.getInstance().isViewOnlyMeeting() && !ConfMgr.getInstance().isViewOnlyClientOnMMR();
    }

    public static boolean isGe2NotCallingOut() {
        ConfMgr instance = ConfMgr.getInstance();
        return instance.getClientUserCount() >= 2 || !instance.isCallingOut();
    }

    public static boolean isViewOnlyButSpeakAttendee() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        return myself != null && myself.isViewOnlyUserCanTalk();
    }

    public static void setAttendeeVideoLayout(int i) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && myself.isHost()) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null && confStatusObj.getAttendeeVideoLayoutMode() != i) {
                confStatusObj.setLiveLayoutMode(i == 0);
            }
        }
    }

    public static void setAttendeeVideoControlMode(int i) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null && myself.isHost()) {
            CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
            if (confStatusObj != null) {
                confStatusObj.setAttendeeVideoControlMode(i);
            }
        }
    }

    public static boolean isHideNoVideoUsers() {
        VideoSessionMgr videoObj = ConfMgr.getInstance().getVideoObj();
        return (videoObj != null && videoObj.hideNoVideoUserInWallView()) || VideoLayoutHelper.getInstance().isHideNoVideoUsersEnabled() || ConfMgr.getInstance().isViewOnlyClientOnMMR();
    }

    public static boolean isNeedShowAttendeeActionList() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        boolean z = false;
        if (myself == null || ConfMgr.getInstance().isViewOnlyMeeting()) {
            return false;
        }
        if (myself.isHost() || myself.isCoHost()) {
            return true;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && !confContext.isChatOff() && !confContext.isPrivateChatOFF()) {
            z = true;
        }
        return z;
    }

    public static boolean isHostCoHostBOModerator() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        return myself != null && (myself.isHost() || myself.isCoHost() || myself.isBOModerator());
    }

    public static boolean isHostCoHost() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        return myself != null && (myself.isHost() || myself.isCoHost());
    }

    public static boolean isHost() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            return ConfUI.getInstance().isDisplayAsHost(myself.getNodeId());
        }
        return false;
    }

    public static boolean isCoHost() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        return myself != null && myself.isCoHost();
    }

    public static boolean isHaisedHand(String str) {
        ZoomRaiseHandInWebinar raiseHandAPIObj = ConfMgr.getInstance().getRaiseHandAPIObj();
        if (raiseHandAPIObj != null) {
            return raiseHandAPIObj.getRaisedHandStatus(str);
        }
        return false;
    }

    public static boolean isTalking(long j) {
        CmmUser userById = ConfMgr.getInstance().getUserById(j);
        boolean z = false;
        if (userById == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = userById.getAudioStatusObj();
        if (audioStatusObj != null && audioStatusObj.getIsTalking()) {
            z = true;
        }
        return z;
    }

    public static boolean isWebinar() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        return confContext != null && confContext.isWebinar();
    }

    public static boolean isGuest(@Nullable CmmUser cmmUser) {
        boolean z = false;
        if (cmmUser == null) {
            return false;
        }
        if (cmmUser.isViewOnlyUserCanTalk()) {
            return isGuestForBuddy(cmmUser.getNodeId());
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.isHighlightGuestFeatureEnabled() && cmmUser.isGuest() && !confContext.amIGuest()) {
            z = true;
        }
        return z;
    }

    public static boolean isGuestForMyself() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        return confContext != null && confContext.amIGuest();
    }

    private static boolean isGuestForBuddy(long j) {
        ZoomQABuddy zoomQABuddyByNodeId = ZMConfUtil.getZoomQABuddyByNodeId(j);
        return zoomQABuddyByNodeId != null && isGuest(zoomQABuddyByNodeId);
    }

    public static boolean isGuest(@Nullable ZoomQABuddy zoomQABuddy) {
        boolean z = false;
        if (zoomQABuddy == null) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && confContext.isHighlightGuestFeatureEnabled() && zoomQABuddy.isGuest() && !confContext.amIGuest()) {
            z = true;
        }
        return z;
    }

    @Nullable
    public static CmmAudioStatus getMySelfAudioStatus() {
        if (!ConfMgr.getInstance().isConfConnected()) {
            return null;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return null;
        }
        return myself.getAudioStatusObj();
    }

    private static String getWaterMarkString() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        StringBuffer stringBuffer = new StringBuffer();
        if (confContext != null && confContext.isSupportConfidentialWaterMarker()) {
            String confidentialWaterMarker = confContext.getConfidentialWaterMarker();
            if (!StringUtil.isEmptyOrNull(confidentialWaterMarker)) {
                stringBuffer.append(confidentialWaterMarker);
            }
        }
        return stringBuffer.toString();
    }

    public static boolean isNeedShowPresenterNameToWaterMark() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        return confContext != null && confContext.needShowPresenterNameToWaterMark();
    }

    @Nullable
    public static Bitmap createWaterMarkBitmap(int i, int i2, int i3, float f) {
        int i4;
        String waterMarkString = getWaterMarkString();
        if (StringUtil.isEmptyOrNull(waterMarkString)) {
            return null;
        }
        if (i < i2) {
            int i5 = i2;
            i2 = (i2 * i2) / i;
            i = i5;
        }
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(new TextView(VideoBoxApplication.getInstance()).getTypeface());
        textPaint.setTextSize((float) UIUtil.sp2px(VideoBoxApplication.getInstance(), 36.0f));
        textPaint.setColor(VideoBoxApplication.getInstance().getResources().getColor(i3));
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Style.FILL);
        int measureText = (int) (textPaint.measureText(waterMarkString) / f);
        StaticLayout staticLayout = new StaticLayout(waterMarkString, textPaint, measureText, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        int height = staticLayout.getHeight();
        if (i >= i2) {
            i4 = (int) (((((double) height) * (((double) i) / ((double) i2))) + ((double) measureText)) * Math.cos(Math.atan((double) (((float) i2) / ((float) i)))));
        } else {
            i4 = (int) (((((double) height) * (((double) i2) / ((double) i))) + ((double) measureText)) * Math.sin(Math.atan((double) (((float) i) / ((float) i2)))));
        }
        int i6 = (i2 * i4) / i;
        try {
            Bitmap createBitmap = Bitmap.createBitmap(i4, i6, Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            Matrix matrix = new Matrix();
            matrix.setRotate(-((float) Math.toDegrees(Math.atan((double) (((float) i6) / ((float) i4))))), (float) (i4 / 2), (float) (i6 / 2));
            canvas.setMatrix(matrix);
            canvas.translate((float) ((i4 - measureText) / 2), (float) ((i6 - height) / 2));
            staticLayout.draw(canvas);
            textPaint.setColor(436207616);
            textPaint.setStyle(Style.STROKE);
            textPaint.setStrokeJoin(Join.ROUND);
            textPaint.setStrokeWidth(0.0f);
            StaticLayout staticLayout2 = new StaticLayout(waterMarkString, textPaint, measureText, Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
            staticLayout2.draw(canvas);
            return createBitmap;
        } catch (Exception unused) {
            return null;
        }
    }

    public static void showChatUI(@Nullable ZMFragment zMFragment, long j) {
        if (zMFragment != null) {
            ZMActivity zMActivity = (ZMActivity) zMFragment.getActivity();
            if (zMActivity != null) {
                showChatUI(zMActivity, j);
            }
        }
    }

    public static void showChatUI(@Nullable ZMActivity zMActivity, long j) {
        if (zMActivity != null) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (j == 0 || (confContext != null && !confContext.isPrivateChatOFF())) {
                if (UIMgr.isLargeMode(zMActivity)) {
                    ConfChatFragment.showAsFragment(zMActivity.getSupportFragmentManager(), j);
                } else {
                    ConfChatFragment.showAsActivity(zMActivity, 0, j);
                }
            }
        }
    }

    public static boolean isSameActiveCall(@NonNull ScheduledMeetingItem scheduledMeetingItem) {
        boolean z = PTApp.getInstance().hasActiveCall() && VideoBoxApplication.getInstance().isConfProcessRunning();
        if (!z) {
            return z;
        }
        boolean z2 = PTApp.getInstance().getActiveMeetingNo() == scheduledMeetingItem.getMeetingNo();
        if (z2) {
            return z2;
        }
        String activeCallId = PTApp.getInstance().getActiveCallId();
        return activeCallId != null && activeCallId.equals(scheduledMeetingItem.getId());
    }

    @Nullable
    public static String getDescAlternativeHosts(@Nullable Context context, long j) {
        if (context == null) {
            return null;
        }
        MeetingHelper meetingHelper = PTApp.getInstance().getMeetingHelper();
        if (meetingHelper == null) {
            return null;
        }
        MeetingInfoProto meetingItemByNumber = meetingHelper.getMeetingItemByNumber(j);
        if (meetingItemByNumber == null) {
            return null;
        }
        return getDescAlternativeHosts(context, meetingItemByNumber.getAlterHostList());
    }

    @Nullable
    public static String getDescAlternativeHosts(@NonNull Context context, @Nullable List<AlterHost> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        Iterator it = list.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AlterHost alterHost = (AlterHost) it.next();
            if (alterHost != null) {
                String firstName = alterHost.getFirstName();
                if (StringUtil.isEmptyOrNull(firstName)) {
                    firstName = alterHost.getLastName();
                } else {
                    sb.append(firstName);
                    if (!StringUtil.isEmptyOrNull(alterHost.getLastName())) {
                        sb.append(OAuth.SCOPE_DELIMITER);
                        sb.append(alterHost.getLastName());
                    }
                }
                if (StringUtil.isEmptyOrNull(firstName)) {
                    sb.append(alterHost.getEmail());
                }
            }
        }
        if (list.size() <= 1) {
            return sb.toString();
        }
        return context.getString(C4558R.string.zm_desc_alterhost_21201, new Object[]{sb.toString(), Integer.valueOf(list.size() - 1)});
    }

    @NonNull
    public static List<AlterHost> transformIMAddrBookItemsToAlterHosts(@Nullable List<IMAddrBookItem> list, @NonNull Set<String> set) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        ArrayList arrayList = new ArrayList();
        if (list != null && !list.isEmpty()) {
            for (IMAddrBookItem iMAddrBookItem : list) {
                Builder newBuilder = AlterHost.newBuilder();
                String safeString = StringUtil.safeString(iMAddrBookItem.getAccountEmail());
                if (iMAddrBookItem.isManualInput()) {
                    set.add(safeString);
                }
                String jid = iMAddrBookItem.getJid();
                newBuilder.setEmail(safeString);
                newBuilder.setPmi(iMAddrBookItem.getPmi());
                newBuilder.setFirstName(StringUtil.safeString(iMAddrBookItem.getScreenName()));
                if (zoomMessenger == null) {
                    arrayList.add(newBuilder.build());
                } else {
                    if (!StringUtil.isEmptyOrNull(jid)) {
                        ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(jid);
                        if (buddyWithJID != null) {
                            newBuilder.setHostID(jid);
                            newBuilder.setFirstName(StringUtil.safeString(buddyWithJID.getFirstName()));
                            newBuilder.setLastName(StringUtil.safeString(buddyWithJID.getLastName()));
                        }
                    }
                    arrayList.add(newBuilder.build());
                }
            }
        }
        return arrayList;
    }

    @NonNull
    public static String formatScheduleMeetingErrorMsg(@Nullable String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return "";
        }
        String[] split = str.split("#|,");
        if (split.length <= 0) {
            return str;
        }
        String str2 = "";
        for (String str3 : split) {
            if (!StringUtil.isEmptyOrNull(str3) && str3.contains("@")) {
                StringBuilder sb = new StringBuilder();
                sb.append(str2);
                sb.append(str3);
                sb.append(PreferencesConstants.COOKIE_DELIMITER);
                str2 = sb.toString();
            }
        }
        if (str2.length() > 1) {
            str2 = str2.substring(0, str2.length() - 1);
        }
        return str2;
    }

    public static void saveAlterHostsForOnlyEmail(@Nullable List<AlterHost> list, @NonNull Set<String> set) {
        if (list != null && !list.isEmpty()) {
            HashSet hashSet = new HashSet();
            for (AlterHost email : list) {
                String email2 = email.getEmail();
                if (set.contains(email2)) {
                    hashSet.add(email2);
                }
            }
            if (!hashSet.isEmpty()) {
                ZoomAppPropData instance = ZoomAppPropData.getInstance();
                if (instance != null) {
                    Set loadHistoryEmailsForAlterHosts = loadHistoryEmailsForAlterHosts();
                    if (loadHistoryEmailsForAlterHosts != null && !loadHistoryEmailsForAlterHosts.isEmpty()) {
                        hashSet.addAll(loadHistoryEmailsForAlterHosts);
                    }
                    instance.setKeyValue(ZoomAppPropData.KEY_ALTERNATE_HOST_CACHE, new Gson().toJson((Object) hashSet, new TypeToken<Set<String>>() {
                    }.getType()));
                }
            }
        }
    }

    @Nullable
    public static Set<String> loadHistoryEmailsForAlterHosts() {
        ZoomAppPropData instance = ZoomAppPropData.getInstance();
        if (instance != null) {
            String queryWithKey = instance.queryWithKey(ZoomAppPropData.KEY_ALTERNATE_HOST_CACHE, null);
            if (!StringUtil.isEmptyOrNull(queryWithKey)) {
                return (Set) new Gson().fromJson(queryWithKey, new TypeToken<Set<String>>() {
                }.getType());
            }
        }
        return null;
    }

    public static void startCMR(ZMActivity zMActivity) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (zMActivity == null || confContext == null || !confContext.needPromptStartRecordingDisclaimer()) {
            startCMR();
            return;
        }
        CustomizeInfo startRecordingDisclaimer = confContext.getStartRecordingDisclaimer();
        if (startRecordingDisclaimer != null) {
            startRecordingDisclaimer.setType(3);
            ZMRecordingStartDisclaimerDialog.showDialog(zMActivity, startRecordingDisclaimer);
        }
    }

    public static boolean startCMR() {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr == null || !recordMgr.canStartCMR() || !recordMgr.startCMR()) {
            return false;
        }
        ZMConfEventTracking.logRecord(true, true);
        return true;
    }

    public static boolean stopRecord(boolean z) {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr == null) {
            return false;
        }
        boolean z2 = recordMgr.isCMRInProgress() || recordMgr.isCMRPaused();
        if (recordMgr.stopRecord(z || recordMgr.isCMRInProgress())) {
            ZMConfEventTracking.logRecord(false, z2);
        }
        return true;
    }

    public static boolean resumeRecord() {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        if (recordMgr != null && recordMgr.canControlCMR() && recordMgr.canControlCMR()) {
            return recordMgr.resumeCMR();
        }
        return false;
    }

    public static boolean pauseRecord() {
        RecordMgr recordMgr = ConfMgr.getInstance().getRecordMgr();
        return recordMgr != null && recordMgr.canControlCMR() && recordMgr.pauseCMR();
    }

    private static int getExtraValue(int i) {
        if (i == 1) {
            return ConfMgr.getInstance().getLastNetworkErrorCode();
        }
        if (i == 9) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null) {
                return confContext.getParticipantLimit();
            }
        }
        return -1;
    }

    public static void handleCallError(@Nullable ConfActivity confActivity, long j) {
        if (confActivity != null && j != 0) {
            CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
            if (confContext != null && j == confContext.getConfNumber()) {
                int launchReason = confContext.getLaunchReason();
                if (confContext.isCall() && launchReason == 1) {
                    if (confContext.getOrginalHost()) {
                        endCall(confActivity);
                    } else {
                        leaveCall(confActivity);
                    }
                }
            }
        }
    }

    public static void leaveCallForErrorCode(@NonNull ConfActivity confActivity, int i) {
        if (i == 10) {
            ConfMgr.getInstance().leaveConference();
            IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.SHOW_JOIN_ERROR.ordinal(), i);
            confActivity.finish(true);
            return;
        }
        boolean z = false;
        if (i == 23) {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(errorCodeToLeaveReason(i)), true, false);
            IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.SHOW_JOIN_ERROR.ordinal(), i);
            confActivity.finish(true);
            return;
        }
        int extraValue = getExtraValue(i);
        ConfMgr instance = ConfMgr.getInstance();
        String valueOf = String.valueOf(errorCodeToLeaveReason(i));
        if (i == 1) {
            z = true;
        }
        instance.notifyConfLeaveReason(valueOf, true, z);
        IPCHelper.getInstance().notifyLeaveAndPerformAction(LeaveConfAction.SHOW_JOIN_ERROR.ordinal(), i, extraValue);
        leaveCall(confActivity);
    }

    public static void leaveConfBeforeConnected(@NonNull ConfActivity confActivity) {
        int confStatus = ConfMgr.getInstance().getConfStatus();
        if (confStatus == 8 || confStatus == 9) {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(8), true);
        } else {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
        }
        leaveCall(confActivity);
    }

    public static void leaveCall(@Nullable ConfActivity confActivity) {
        if (confActivity != null) {
            confActivity.finish(true);
            ConfMgr.getInstance().leaveConference();
        }
    }

    public static void leaveCallWithNotify(ConfActivity confActivity) {
        int confStatus = ConfMgr.getInstance().getConfStatus();
        if (confStatus == 8 || confStatus == 9) {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(8), true);
        } else {
            ConfMgr.getInstance().notifyConfLeaveReason(String.valueOf(1), true);
        }
        leaveCall(confActivity);
    }

    public static void leaveCallWithDialog(final ConfActivity confActivity, String str, String str2) {
        if (confActivity != null) {
            new ZMAlertDialog.Builder(confActivity).setTitle((CharSequence) str).setMessage(str2).setCancelable(false).setPositiveButton(C4409R.string.zm_btn_ok, (OnClickListener) new OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ConfLocalHelper.leaveCall(confActivity);
                }
            }).create().show();
        }
    }

    public static void endCall(@Nullable ConfActivity confActivity) {
        if (confActivity != null) {
            confActivity.finish(true);
            ConfMgr.getInstance().endConference();
        }
    }

    public static void endOtherMeeting() {
        ConfMgr.getInstance().handleConfCmd(69);
    }

    public static String getLiveChannelStreamName() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isLiveOn()) {
            int liveChannelsCount = confStatusObj.getLiveChannelsCount();
            for (int i = 0; i < liveChannelsCount; i++) {
                if (confStatusObj.isLiveChannelsOn(i)) {
                    return confStatusObj.getLiveChannelsName(i);
                }
            }
        }
        return "";
    }

    public static String getLiveChannelStreamUrl() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isLiveOn()) {
            int liveChannelsCount = confStatusObj.getLiveChannelsCount();
            for (int i = 0; i < liveChannelsCount; i++) {
                if (confStatusObj.isLiveChannelsOn(i)) {
                    return confStatusObj.getLiveChannelUrL(i);
                }
            }
        }
        return "";
    }

    public static boolean isInSilentMode() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        return confContext.inSilentMode();
    }

    public static boolean isDirectShareClient() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        return confContext.isDirectShareClient();
    }

    public static void showQA(ConfActivity confActivity) {
        if (ConfMgr.getInstance().isViewOnlyMeeting()) {
            ZMQAAttendeeViewerFragment.showAsActivity(confActivity);
        } else {
            ZMQAPanelistViewerFragment.showAsActivity(confActivity);
        }
    }

    public static boolean tryAutoConnectVoip(boolean z) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z2 = false;
        if (confContext == null || confContext.notSupportVoIP() || ZMPolicyUIHelper.isComputerAudioDisabled() || (!confContext.isCall() && confContext.getLaunchReason() != 1 && !z)) {
            return false;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem != null && !meetingItem.getIsSelfTelephonyOn() && connectVoIP()) {
            z2 = true;
        }
        return z2;
    }

    public static boolean tryAutoConnectAudio(@NonNull ZMActivity zMActivity) {
        int autoConnectAudio = PTSettingHelper.getAutoConnectAudio();
        if (autoConnectAudio == 0) {
            return tryAutoConnectVoip(false);
        }
        if (autoConnectAudio == 1) {
            return tryAutoConnectVoip(false);
        }
        if (autoConnectAudio == 2) {
            return tryAutoCallMyPhone(zMActivity);
        }
        if (autoConnectAudio == 3) {
            switch (NetworkUtil.getDataNetworkType(zMActivity)) {
                case 1:
                    return tryAutoConnectVoip(true);
                case 2:
                    return tryAutoCallMyPhone(zMActivity);
            }
        }
        return false;
    }

    public static int getZoomConfType(@NonNull CmmConfContext cmmConfContext) {
        boolean isAudioOnlyMeeting = cmmConfContext.isAudioOnlyMeeting();
        boolean isShareOnlyMeeting = cmmConfContext.isShareOnlyMeeting();
        boolean isCall = cmmConfContext.isCall();
        return isAudioOnlyMeeting ? isCall ? 0 : 3 : isShareOnlyMeeting ? isCall ? 2 : 4 : isCall ? 1 : 3;
    }

    public static boolean disconnectAudioAndMic() {
        AudioSessionMgr audioObj = ConfMgr.getInstance().getAudioObj();
        if (audioObj == null) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        long j = 2;
        if (audioStatusObj != null) {
            j = audioStatusObj.getAudiotype();
        }
        if (j == 0) {
            audioObj.unSelectMicrophone();
        }
        disconnectAudio();
        ConfUI.getInstance().handleConfInnerEvent(2, (int) j);
        return true;
    }

    public static void disconnectAudio() {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
            if (audioStatusObj != null) {
                long audiotype = audioStatusObj.getAudiotype();
                if (0 == audiotype) {
                    turnOnOffAudioSession(false);
                } else if (1 == audiotype) {
                    CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
                    if (confStatusObj != null) {
                        confStatusObj.hangUp();
                    }
                }
            }
        }
    }

    public static void handleCallOutStatusChanged(@NonNull final ZMActivity zMActivity, long j) {
        int i = (int) j;
        if (i != 0) {
            switch (i) {
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 16:
                    break;
            }
        }
        ConfShowCallingMeDialog.dismiss(zMActivity.getSupportFragmentManager());
        ConfMgr.getInstance().getConfDataHelper().setmIsAutoCalledOrCanceledCall(true);
        if (i == 4 || i == 7 || i == 9) {
            final String autoCallNumber = getAutoCallNumber();
            if (!StringUtil.isEmptyOrNull(autoCallNumber)) {
                DialogUtils.showAlertDialog(zMActivity, zMActivity.getString(C4558R.string.zm_title_fail_to_call_41171), autoCallNumber, C4558R.string.zm_btn_retry, C4558R.string.zm_btn_cancel, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConfLocalHelper.startAutoCall(zMActivity, autoCallNumber);
                    }
                });
            }
        } else if (i == 12 || i == 14 || i == 15) {
            DialogUtils.showAlertDialog(zMActivity, C4558R.string.zm_msg_number_not_support_41171, C4558R.string.zm_btn_ok);
        } else if (i == 5) {
            String autoCallNumber2 = getAutoCallNumber();
            if (!StringUtil.isEmptyOrNull(autoCallNumber2)) {
                DialogUtils.showAlertDialog(zMActivity, zMActivity.getString(C4558R.string.zm_title_fail_to_call_41171), autoCallNumber2, C4558R.string.zm_btn_modify_41171, C4558R.string.zm_btn_cancel, new OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ConfLocalHelper.callMe(zMActivity);
                    }
                });
            }
        }
    }

    public static boolean tryAutoCallMyPhone(@NonNull ZMActivity zMActivity) {
        if (!ConfMgr.getInstance().getConfDataHelper().ismIsAutoCalledOrCanceledCall() && checkIfNeedAutoCallMyPhone()) {
            String autoCallNumber = getAutoCallNumber();
            if (!StringUtil.isEmptyOrNull(autoCallNumber)) {
                CmmUser myself = ConfMgr.getInstance().getMyself();
                if (myself == null) {
                    return false;
                }
                CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
                if (audioStatusObj == null) {
                    return false;
                }
                if (0 == audioStatusObj.getAudiotype()) {
                    turnOnOffAudioSession(false);
                }
                startAutoCall(zMActivity, autoCallNumber);
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public static void startAutoCall(@NonNull ZMActivity zMActivity, @NonNull String str) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null) {
            ConfDataHelper confDataHelper = ConfMgr.getInstance().getConfDataHelper();
            confDataHelper.setmIsAutoCalledOrCanceledCall(true);
            confDataHelper.setmIsNeedHandleCallOutStatusChangedInMeeting(true);
            if (confStatusObj.startCallOut(str)) {
                ConfShowCallingMeDialog.showConfShowCallingMeDialog(zMActivity, str);
                return;
            }
            confDataHelper.setmIsAutoCalledOrCanceledCall(false);
            confDataHelper.setmIsNeedHandleCallOutStatusChangedInMeeting(false);
        }
    }

    private static boolean checkIfNeedAutoCallMyPhone() {
        boolean z = false;
        if (ConfMgr.getInstance().isJoinWithOutAudio()) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null || !confContext.isPTLogin()) {
            return false;
        }
        if ((confContext.getLaunchReason() == 1 && confContext.isCall() && !confContext.isShareOnlyMeeting()) || confContext.getAppContextParams().getBoolean(ConfParams.CONF_PARAM_NO_DIAL_OUT, false)) {
            return false;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null || meetingItem.getTelephonyOff() || meetingItem.getSupportCallOutType() == 0 || meetingItem.getCalloutCountryCodesCount() == 0) {
            return false;
        }
        if (!ConfMgr.getInstance().isViewOnlyMeeting() && !confContext.inSilentMode()) {
            z = true;
        }
        return z;
    }

    @Nullable
    private static String getAutoCallNumber() {
        String readStringValue = PreferenceUtil.readStringValue(PreferenceUtil.CALLME_PHONE_NUMBER, null);
        if (StringUtil.isEmptyOrNull(readStringValue)) {
            return null;
        }
        CountryCodeItem readFromPreference = CountryCodeItem.readFromPreference(PreferenceUtil.CALLME_SELECT_COUNTRY);
        if (readFromPreference == null || StringUtil.isEmptyOrNull(readFromPreference.countryCode)) {
            return null;
        }
        return PhoneNumberUtil.formatNumber(readStringValue, readFromPreference.countryCode);
    }

    public static void callMe(@NonNull ZMActivity zMActivity) {
        StringBuilder sb = new StringBuilder();
        sb.append(zMActivity.getPackageName());
        sb.append(ZMConfIntentParam.ACTION_CALL_MY_PHONE);
        Intent intent = new Intent(sb.toString());
        if (AndroidAppUtil.hasActivityForIntent(zMActivity, intent)) {
            try {
                ActivityStartHelper.startActivityForResult((Activity) zMActivity, intent, 1008);
            } catch (Exception unused) {
            }
        } else {
            CallMeByPhoneFragment.showAsActivity(zMActivity, 1008);
        }
    }

    public static long getMyAudioType() {
        if (!ConfMgr.getInstance().isConfConnected()) {
            return 2;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return 2;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return 2;
        }
        return audioStatusObj.getAudiotype();
    }

    public static boolean isMySelf(long j) {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        return confStatusObj != null && confStatusObj.isMyself(j);
    }

    public static boolean handleMySelfRaisHandAction(@NonNull ZMActivity zMActivity, @Nullable View view) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null || isPromptShowConnectAudio(zMActivity)) {
            return false;
        }
        if (ConfMgr.getInstance().handleUserCmd(36, myself.getNodeId()) && view != null && AccessibilityUtil.isSpokenFeedbackEnabled(zMActivity)) {
            AccessibilityUtil.announceForAccessibilityCompat(view, C4558R.string.zm_description_msg_myself_already_raise_hand_17843);
        }
        return true;
    }

    private static boolean isPromptShowConnectAudio(@NonNull Context context) {
        if (UIUtil.isPhone(context)) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return false;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return false;
        }
        boolean z = audioStatusObj.getAudiotype() == 2 && !meetingItem.getIsSelfTelephonyOn();
        if (z) {
            int pureCallinUserCount = ConfMgr.getInstance().getPureCallinUserCount();
            if (pureCallinUserCount == 0 && isWebinar()) {
                pureCallinUserCount += ConfMgr.getInstance().getViewOnlyTelephonyUserCount();
            }
            z = pureCallinUserCount > 0;
        }
        return z;
    }

    public static boolean isAllowParticipantRename() {
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj == null) {
            return false;
        }
        return confStatusObj.isAllowParticipantRename();
    }

    @NonNull
    public static String getMeetingHostName() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return "";
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if (meetingItem == null) {
            return "";
        }
        String meetingHostName = meetingItem.getMeetingHostName();
        if (meetingHostName == null) {
            meetingHostName = "";
        }
        return meetingHostName;
    }

    public static boolean isShareResulting(String str) {
        PollingMgr pollObj = ConfMgr.getInstance().getPollObj();
        boolean z = false;
        if (pollObj == null) {
            return false;
        }
        IPollingDoc pollingDocById = pollObj.getPollingDocById(str);
        if (pollingDocById != null && pollingDocById.getPollingState() == 3) {
            z = true;
        }
        return z;
    }

    private static int getVideoUserCount() {
        ConfMgr instance = ConfMgr.getInstance();
        int videoUserCount = instance.getVideoUserCount();
        CmmUser myself = instance.getMyself();
        if (instance.getConfDataHelper().ismIsShowMyVideoInGalleryView() || myself == null || !hasVideo(myself)) {
            return videoUserCount;
        }
        if (videoUserCount > 1) {
            videoUserCount--;
        }
        return videoUserCount;
    }

    private static boolean hasVideo(@Nullable CmmUser cmmUser) {
        boolean z = false;
        if (cmmUser == null || cmmUser.isMMRUser() || cmmUser.isPureCallInUser() || cmmUser.inSilentMode()) {
            return false;
        }
        CmmVideoStatus videoStatusObj = cmmUser.getVideoStatusObj();
        if (videoStatusObj != null && videoStatusObj.getIsSending()) {
            z = true;
        }
        return z;
    }

    public static boolean isMeetShowMyVideoButton() {
        boolean isHideNoVideoUsers = isHideNoVideoUsers();
        boolean ismIsShowMyVideoInGalleryView = ConfMgr.getInstance().getConfDataHelper().ismIsShowMyVideoInGalleryView();
        boolean z = false;
        if (isHideNoVideoUsers) {
            CmmUser myself = ConfMgr.getInstance().getMyself();
            int videoUserCount = getVideoUserCount();
            if (myself == null || !hasVideo(myself)) {
                if (videoUserCount >= 2) {
                    z = true;
                }
                return z;
            }
            if ((ismIsShowMyVideoInGalleryView && videoUserCount >= 2) || (!ismIsShowMyVideoInGalleryView && videoUserCount >= 1)) {
                z = true;
            }
            return z;
        }
        if (!ismIsShowMyVideoInGalleryView ? getVideoUserCount() >= 1 : getVideoUserCount() >= 2) {
            z = true;
        }
        return z;
    }

    public static void confirmNamePassword(@NonNull ConfActivity confActivity, String str, String str2) {
        if (!StringUtil.isEmptyOrNull(str)) {
            if (!StringUtil.isEmptyOrNull(str2)) {
                PreferenceUtil.saveStringValue(PreferenceUtil.SCREEN_NAME, str2);
            }
            ConfMgr.getInstance().onUserInputPassword(str, str2, false);
            return;
        }
        PreferenceUtil.saveStringValue(PreferenceUtil.SCREEN_NAME, str2);
        ConfMgr.getInstance().onUserConfirmToJoin(true, str2);
        if (confActivity.getRetainedFragment().ismIsAbleToJoin() && !ConfMgr.getInstance().isCallingOut()) {
            confActivity.switchViewTo(ZMConfEnumViewMode.CONF_VIEW);
        }
    }

    public static boolean isVoIPEnabled() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = true;
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                if (meetingItem.getVoipOff() || ZMPolicyUIHelper.isDisableDeviceAudio()) {
                    z = false;
                }
                return z;
            }
        }
        return true;
    }

    public static boolean isDialInEnabled() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = true;
        if (confContext == null) {
            return true;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if ((meetingItem != null && meetingItem.getTelephonyOff()) || confContext.getAppContextParams().getBoolean(ConfParams.CONF_PARAM_NO_DIAL_IN, false)) {
            return false;
        }
        if (StringUtil.isEmptyOrNull(confContext.getPhoneCallInNumber()) && StringUtil.isEmptyOrNull(confContext.getTollFreeCallInNumber())) {
            z = false;
        }
        return z;
    }

    public static boolean isCallMeEnabled() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = true;
        if (confContext == null) {
            return true;
        }
        MeetingInfoProto meetingItem = confContext.getMeetingItem();
        if ((meetingItem != null && meetingItem.getTelephonyOff()) || confContext.getAppContextParams().getBoolean(ConfParams.CONF_PARAM_NO_DIAL_OUT, false)) {
            return false;
        }
        if ((meetingItem == null || meetingItem.getSupportCallOutType() == 0) && (getUserPhoneInfo() == null || !isPSTNPhoneNumberNotMatchCallout())) {
            z = false;
        }
        return z;
    }

    public static boolean isNoneAudioTypeSupport() {
        return !isVoIPEnabled() && !isDialInEnabled() && !isCallMeEnabled();
    }

    public static boolean isPSTNOnlyUseTelephone() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                return meetingItem.getPstnOnlyUseTelephone();
            }
        }
        return false;
    }

    public static boolean isPSTNUseOwnPhoneNumber() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                return meetingItem.getPstnUseOwnPhoneNumber();
            }
        }
        return false;
    }

    public static boolean isPSTNPhoneNumberNotMatchCallout() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                return meetingItem.getPstnPhoneNumberNotMatchCallout();
            }
        }
        return false;
    }

    public static boolean isOnlyUseTelephoneAndUseOwnPhoneNumber() {
        return isPSTNOnlyUseTelephone() && isPSTNUseOwnPhoneNumber();
    }

    public static boolean isPhoneNumberNotMatchCalloutAndOnlyUseOwnPhone() {
        return getUserPhoneInfo() != null && isPSTNPhoneNumberNotMatchCallout();
    }

    public static boolean isPSTNHideInviteByPhone() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            MeetingInfoProto meetingItem = confContext.getMeetingItem();
            if (meetingItem != null) {
                return meetingItem.getPstnHideInviteByPhone();
            }
        }
        return false;
    }

    @Nullable
    public static UserPhoneInfo getUserPhoneInfo() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext == null) {
            return null;
        }
        UserPhoneInfoList userPhoneInfos = confContext.getUserPhoneInfos();
        if (userPhoneInfos == null) {
            return null;
        }
        List userPhoneInfosList = userPhoneInfos.getUserPhoneInfosList();
        if (userPhoneInfosList == null || userPhoneInfosList.size() == 0) {
            return null;
        }
        return (UserPhoneInfo) userPhoneInfosList.get(0);
    }

    public static boolean hasAudioSourceToConnect() {
        if (!ConfMgr.getInstance().isConfConnected()) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null || ConfMgr.getInstance().getConfContext() == null) {
            return false;
        }
        long audiotype = audioStatusObj.getAudiotype();
        boolean z = true;
        if (2 != audiotype) {
            return true;
        }
        if (!isVoIPEnabled() && !isCallMeEnabled() && !isDialInEnabled()) {
            z = false;
        }
        return z;
    }

    public static boolean isInVideoCompanionMode() {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        return confContext != null && confContext.isInVideoCompanionMode();
    }

    public static boolean hasDisableSendVideoReason(int i) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = false;
        if (confContext == null) {
            return false;
        }
        int disableSendVideoReason = confContext.getDisableSendVideoReason();
        if (disableSendVideoReason == i || (i & disableSendVideoReason) > 0) {
            z = true;
        }
        return z;
    }

    public static boolean hasDisableRecvVideoReason(int i) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        boolean z = false;
        if (confContext == null) {
            return false;
        }
        int disableRecvVideoReason = confContext.getDisableRecvVideoReason();
        if (disableRecvVideoReason == i || (i & disableRecvVideoReason) > 0) {
            z = true;
        }
        return z;
    }

    public static boolean isInterpretationStarted(@Nullable InterpretationMgr interpretationMgr) {
        return interpretationMgr != null && interpretationMgr.isInterpretationEnabled() && interpretationMgr.isInterpretationStarted();
    }

    public static boolean isInterpreter(@Nullable InterpretationMgr interpretationMgr) {
        if (interpretationMgr != null && isInterpretationStarted(interpretationMgr)) {
            return interpretationMgr.isInterpreter();
        }
        return false;
    }

    public static boolean canUseInterpretation(@Nullable InterpretationMgr interpretationMgr) {
        return interpretationMgr != null && interpretationMgr.isInterpretationEnabled() && interpretationMgr.isInterpretationStarted() && !interpretationMgr.isInterpreter() && isUseAudioVOIP();
    }

    public static boolean isUseAudioVOIP() {
        boolean z = false;
        if (ConfMgr.getInstance().isCallingOut()) {
            return false;
        }
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself == null) {
            return false;
        }
        CmmAudioStatus audioStatusObj = myself.getAudioStatusObj();
        if (audioStatusObj == null) {
            return false;
        }
        if (0 == audioStatusObj.getAudiotype()) {
            z = true;
        }
        return z;
    }

    public static void muteAudioByMe(AudioSessionMgr audioSessionMgr) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null) {
            long confOption = confContext.getConfOption();
            audioSessionMgr.setMutebySelfFlag(true);
            if (!audioSessionMgr.stopAudio()) {
                audioSessionMgr.setMutebySelfFlag(confContext.getOldMuteMyselfFlag(confOption));
            }
        }
    }

    public static boolean canShareScreen(Context context) {
        if (!ZMIntentUtil.isSupportShareScreen(context)) {
            return false;
        }
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && !confContext.isShareDesktopDisabled()) {
            return true;
        }
        return false;
    }

    public static boolean checkNetWork(@NonNull Fragment fragment) {
        if (NetworkUtil.hasDataNetwork(VideoBoxApplication.getInstance())) {
            return true;
        }
        SimpleMessageDialog.newInstance(C4558R.string.zm_alert_network_disconnected).show(fragment.getFragmentManager(), SimpleMessageDialog.class.getName());
        return false;
    }

    public static void addPlistItemByCategory(@NonNull PListItem pListItem, @NonNull CmmUser cmmUser, @NonNull ConfUI confUI, @NonNull HashMap<String, List<PListItem>> hashMap, @Nullable CmmConfStatus cmmConfStatus) {
        if (confUI.isDisplayAsHost(pListItem.userId)) {
            List list = (List) hashMap.get(StatusPListItem.Host.name());
            if (list == null) {
                list = new ArrayList();
                hashMap.put(StatusPListItem.Host.name(), list);
            }
            list.add(pListItem);
        } else if (cmmUser.isSharingPureComputerAudio()) {
            List list2 = (List) hashMap.get(StatusPListItem.ComputerAudio.name());
            if (list2 == null) {
                list2 = new ArrayList();
                hashMap.put(StatusPListItem.ComputerAudio.name(), list2);
            }
            list2.add(pListItem);
        } else if (cmmUser.isInterpreter()) {
            List list3 = (List) hashMap.get(StatusPListItem.Interpreter.name());
            if (list3 == null) {
                list3 = new ArrayList();
                hashMap.put(StatusPListItem.Interpreter.name(), list3);
            }
            list3.add(pListItem);
        } else if (confUI.isDisplayAsCohost(pListItem.userId)) {
            List list4 = (List) hashMap.get(StatusPListItem.Cohost.name());
            if (list4 == null) {
                list4 = new ArrayList();
                hashMap.put(StatusPListItem.Cohost.name(), list4);
            }
            list4.add(pListItem);
        } else if (cmmUser.getRaiseHandState()) {
            List list5 = (List) hashMap.get(StatusPListItem.RaisedHands.name());
            if (list5 == null) {
                list5 = new ArrayList();
                hashMap.put(StatusPListItem.RaisedHands.name(), list5);
            }
            list5.add(pListItem);
        } else if (cmmConfStatus == null || !cmmConfStatus.isMyself(pListItem.userId)) {
            CmmAudioStatus audioStatusObj = cmmUser.getAudioStatusObj();
            if (audioStatusObj == null || audioStatusObj.getAudiotype() == 2 || audioStatusObj.getIsMuted()) {
                List list6 = (List) hashMap.get(StatusPListItem.Others.name());
                if (list6 == null) {
                    list6 = new ArrayList();
                    hashMap.put(StatusPListItem.Others.name(), list6);
                }
                list6.add(pListItem);
                return;
            }
            List list7 = (List) hashMap.get(StatusPListItem.UnmuteAudio.name());
            if (list7 == null) {
                list7 = new ArrayList();
                hashMap.put(StatusPListItem.UnmuteAudio.name(), list7);
            }
            list7.add(pListItem);
        } else {
            List list8 = (List) hashMap.get(StatusPListItem.MySelf.name());
            if (list8 == null) {
                list8 = new ArrayList();
                hashMap.put(StatusPListItem.MySelf.name(), list8);
            }
            list8.add(pListItem);
        }
    }

    public static int getPriorityPlistItem(@NonNull PListItem pListItem) {
        CmmUser cmmUser = pListItem.mCmmUser;
        if (cmmUser == null) {
            cmmUser = ConfMgr.getInstance().getUserById(pListItem.userId);
        }
        return getPriorityPlistItem(pListItem, cmmUser);
    }

    public static int getPriorityPlistItem(@NonNull PListItem pListItem, @Nullable CmmUser cmmUser) {
        ConfUI instance = ConfUI.getInstance();
        CmmConfStatus confStatusObj = ConfMgr.getInstance().getConfStatusObj();
        if (confStatusObj != null && confStatusObj.isMyself(pListItem.userId)) {
            return StatusPListItem.MySelf.ordinal();
        }
        if (instance.isDisplayAsHost(pListItem.userId)) {
            return StatusPListItem.Host.ordinal();
        }
        if (cmmUser != null) {
            if (cmmUser.isSharingPureComputerAudio()) {
                return StatusPListItem.ComputerAudio.ordinal();
            }
            if (cmmUser.getRaiseHandState()) {
                return StatusPListItem.RaisedHands.ordinal();
            }
            if (instance.isDisplayAsCohost(pListItem.userId)) {
                return StatusPListItem.Cohost.ordinal();
            }
            if (cmmUser.isInterpreter()) {
                return StatusPListItem.Interpreter.ordinal();
            }
            CmmAudioStatus audioStatusObj = cmmUser.getAudioStatusObj();
            if (!(audioStatusObj == null || audioStatusObj.getAudiotype() == 2 || audioStatusObj.getIsMuted())) {
                return StatusPListItem.UnmuteAudio.ordinal();
            }
        }
        return StatusPListItem.Others.ordinal();
    }

    public static void fillPlistItems(@NonNull HashMap<String, List<PListItem>> hashMap, @NonNull List<PListItem> list, @NonNull List<PListItem> list2) {
        boolean z = false;
        for (StatusPListItem name : StatusPListItem.values()) {
            List list3 = (List) hashMap.get(name.name());
            if (list3 != null && !list3.isEmpty()) {
                if (z) {
                    list2.addAll(list3);
                } else {
                    z = list3.size() + list.size() > ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT;
                    if (z) {
                        int size = ZMConfiguration.MAX_PLIST_REFRESH_NOW_USER_COUNT - list.size();
                        if (size > 0) {
                            list.addAll(list3.subList(0, size));
                        }
                        if (size < list3.size()) {
                            list2.addAll(list3.subList(size, list3.size()));
                        }
                    } else {
                        list.addAll(list3);
                    }
                }
            }
        }
    }

    public static boolean isNeedShowBtnShare(@NonNull ConfParams confParams) {
        CmmConfContext confContext = ConfMgr.getInstance().getConfContext();
        if (confContext != null && !confContext.isScreenShareDisabled() && !confParams.isShareButtonDisabled()) {
            return true;
        }
        return false;
    }

    public static boolean isInRemoteControlMode(@Nullable AbsVideoSceneMgr absVideoSceneMgr) {
        if (absVideoSceneMgr == null) {
            return false;
        }
        AbsVideoScene activeScene = absVideoSceneMgr.getActiveScene();
        if (!(activeScene instanceof ShareVideoScene)) {
            return false;
        }
        return ((ShareVideoScene) activeScene).isInRemoteControlMode();
    }

    public static boolean checkRemoteControlPrivilege() {
        ShareSessionMgr shareObj = ConfMgr.getInstance().getShareObj();
        if (shareObj == null || shareObj.getShareStatus() != 3) {
            return false;
        }
        ConfMgr instance = ConfMgr.getInstance();
        if (instance.getMyself() == null) {
            return false;
        }
        return shareObj.hasRemoteControlPrivilegeWithUserId(instance.getMyself().getNodeId());
    }

    public static void returnToConf(@NonNull Context context) {
        Intent intent = new Intent(context, getConfActivityImplClass(context));
        intent.addFlags(131072);
        if (!(context instanceof Activity)) {
            intent.addFlags(268435456);
        }
        intent.setAction(ZMConfIntentParam.ACTION_RETURN_TO_CONF);
        ActivityStartHelper.startActivityForeground(context, intent);
        ZMConfEventTracking.logBackToMeeting();
    }

    public static void returnConf(Context context) {
        Intent intent = new Intent(context, IntegrationActivity.class);
        intent.setAction(IntegrationActivity.ACTION_RETURN_TO_CONF);
        ActivityStartHelper.startActivityForeground(context, intent);
    }

    @NonNull
    public static Class<?> getConfActivityImplClass(@NonNull Context context) {
        Class<?> cls;
        try {
            cls = Class.forName(context.getString(C4558R.string.zm_config_conf_activity));
        } catch (Exception unused) {
            cls = null;
        }
        return cls == null ? ConfActivityNormal.class : cls;
    }

    public static void performRaiseOrLowerHandAction(@Nullable ZMActivity zMActivity, @Nullable View view) {
        CmmUser myself = ConfMgr.getInstance().getMyself();
        if (myself != null) {
            if (myself.getRaiseHandState()) {
                if (ConfMgr.getInstance().handleUserCmd(37, myself.getNodeId()) && view != null && AccessibilityUtil.isSpokenFeedbackEnabled(zMActivity)) {
                    AccessibilityUtil.announceForAccessibilityCompat(view, C4558R.string.zm_description_msg_myself_already_lower_hand_17843);
                }
            } else if (zMActivity != null && !handleMySelfRaisHandAction(zMActivity, view)) {
                ZMAlertConnectAudioDialog.showConnectAudioDialog(zMActivity, myself.getNodeId());
            }
        }
    }
}
