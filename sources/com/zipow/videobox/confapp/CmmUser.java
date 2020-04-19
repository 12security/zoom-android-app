package com.zipow.videobox.confapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.confapp.ConfAppProtos.CmmAudioStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmShareStatus;
import com.zipow.videobox.confapp.ConfAppProtos.CmmVideoStatus;
import com.zipow.videobox.util.ZMFacebookUtils;
import java.io.File;
import p021us.zoom.androidlib.util.CompatUtils;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmUser {
    public static final int CLIENT_SUPPORT_BE_ANNOTATE = 4;
    public static final int CLIENT_SUPPORT_SILENT_MODE = 8;
    public static final int CLIENT_SUPPPORT_BE_REMOTE_CONTROL = 1;
    public static final int CLIENT_SUPPPORT_DO_REMOTE_CONTROL = 2;
    private static final String TAG = "CmmUser";
    private long mNativeHandle = 0;

    private native boolean canActAsCCEditorImpl(long j);

    private native boolean canActAsCoHostImpl(long j);

    private native boolean canEditCCImpl(long j);

    private native boolean canRecordImpl(long j);

    private native boolean clientOSSupportRecordImpl(long j);

    private native long getAttendeeIDImpl(long j);

    private native int getAudioConnectStatusImpl(long j);

    @NonNull
    private native byte[] getAudioStatusObjProtoData(long j);

    private native int getClientCapabilityImpl(long j);

    private native int getClientOSTypeImpl(long j);

    @NonNull
    private native String getEmailImpl(long j);

    private native String getEmojiReactionImpl(long j);

    private native int getEmojiReactionSkinToneImpl(long j);

    private native int getEmojiReactionTypeImpl(long j);

    private native int getFeedbackImpl(long j);

    private native int getInterpreterActiveLanImpl(long j);

    private native int[] getInterpreterLansImpl(long j);

    @NonNull
    private native String getLocalPicPathImpl(long j);

    private native long getNodeIdImpl(long j);

    private native int getParticipantActiveLanImpl(long j);

    private native boolean getRaiseHandStateImpl(long j);

    private native long getRaiseHandTimestampImpl(long j);

    @NonNull
    private native String getScreenNameImpl(long j);

    @NonNull
    private native byte[] getShareStatusObjProtoData(long j);

    @NonNull
    private native String getSmallPicPathImpl(long j);

    @NonNull
    private native String getUserFBIDImpl(long j);

    @NonNull
    private native String getUserGUIDImpl(long j);

    @NonNull
    private native String getUserJoinedBIDImpl(long j);

    @NonNull
    private native String getUserZoomIDImpl(long j);

    @NonNull
    private native byte[] getVideoStatusObjProtoData(long j);

    private native boolean inSilentModeImpl(long j);

    private native boolean isBOModeratorImpl(long j);

    private native boolean isBoundTelClientUserImpl(long j);

    private native boolean isCoHostImpl(long j);

    private native boolean isEmojiReactionExpiredImpl(long j);

    private native boolean isFailoverUserImpl(long j);

    private native boolean isGuestImpl(long j);

    private native boolean isH323UserImpl(long j);

    private native boolean isHostImpl(long j);

    private native boolean isInAttentionModeImpl(long j);

    private native boolean isInBOMeetingImpl(long j);

    private native boolean isInterpreterImpl(long j);

    private native boolean isLeavingSilentModeImpl(long j);

    private native boolean isMMRUserImpl(long j);

    private native boolean isNoAudioClientUserImpl(long j);

    private native boolean isNoHostUserImpl(long j);

    private native boolean isPictureDownloadingOKImpl(long j);

    private native boolean isPureCallInUserImpl(long j);

    private native boolean isRecordingImpl(long j);

    private native boolean isSharingPureComputerAudioImpl(long j);

    private native boolean isViewOnlyUserCanTalkImpl(long j);

    private native boolean isViewOnlyUserImpl(long j);

    private native boolean supportSwitchCamImpl(long j);

    private native boolean videoCanMutebyHostImpl(long j);

    private native boolean videoCanUnmuteByHostImpl(long j);

    public CmmUser(long j) {
        this.mNativeHandle = j;
    }

    public long getNodeId() {
        return getNodeIdImpl(this.mNativeHandle);
    }

    public int getClientOSType() {
        return getClientOSTypeImpl(this.mNativeHandle);
    }

    @Nullable
    public String getEmail() {
        return getEmailImpl(this.mNativeHandle);
    }

    @Nullable
    public String getScreenName() {
        return getScreenNameImpl(this.mNativeHandle);
    }

    public boolean containsKeyInScreenName(@Nullable String str) {
        return StringUtil.isEmptyOrNull(str) || StringUtil.safeString(getScreenName()).toLowerCase(CompatUtils.getLocalDefault()).contains(str);
    }

    @Nullable
    public String getLocalPicPath() {
        String localPicPathImpl = getLocalPicPathImpl(this.mNativeHandle);
        if (!StringUtil.isEmptyOrNull(localPicPathImpl)) {
            return localPicPathImpl;
        }
        String vCardFileName = ZMFacebookUtils.getVCardFileName(ConfMgr.getInstance().getPTLoginType(), getUserFBID(), true);
        return (vCardFileName == null || !new File(vCardFileName).exists()) ? "" : vCardFileName;
    }

    public boolean isPictureDownloadingOK() {
        return isPictureDownloadingOKImpl(this.mNativeHandle);
    }

    @Nullable
    public String getSmallPicPath() {
        String smallPicPathImpl = getSmallPicPathImpl(this.mNativeHandle);
        if (!StringUtil.isEmptyOrNull(smallPicPathImpl)) {
            return smallPicPathImpl;
        }
        String vCardFileName = ZMFacebookUtils.getVCardFileName(ConfMgr.getInstance().getPTLoginType(), getUserFBID(), false);
        return (vCardFileName == null || !new File(vCardFileName).exists()) ? "" : vCardFileName;
    }

    public boolean isHostCoHost() {
        return isCoHost() || isHost();
    }

    public long getAttendeeID() {
        return getAttendeeIDImpl(this.mNativeHandle);
    }

    public boolean isMMRUser() {
        return isMMRUserImpl(this.mNativeHandle);
    }

    public boolean isPureCallInUser() {
        return isPureCallInUserImpl(this.mNativeHandle);
    }

    public boolean isH323User() {
        return isH323UserImpl(this.mNativeHandle);
    }

    public boolean isHost() {
        return isHostImpl(this.mNativeHandle);
    }

    public boolean isCoHost() {
        return isCoHostImpl(this.mNativeHandle);
    }

    @NonNull
    public String getUserFBID() {
        return getUserFBIDImpl(this.mNativeHandle);
    }

    @NonNull
    public String getUserZoomID() {
        return getUserZoomIDImpl(this.mNativeHandle);
    }

    public boolean canRecord() {
        return canRecordImpl(this.mNativeHandle);
    }

    @Nullable
    public CmmAudioStatus getAudioStatusObj() {
        try {
            return CmmAudioStatus.parseFrom(getAudioStatusObjProtoData(this.mNativeHandle));
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public CmmVideoStatus getVideoStatusObj() {
        try {
            return CmmVideoStatus.parseFrom(getVideoStatusObjProtoData(this.mNativeHandle));
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    @Nullable
    public CmmShareStatus getShareStatusObj() {
        try {
            return CmmShareStatus.parseFrom(getShareStatusObjProtoData(this.mNativeHandle));
        } catch (InvalidProtocolBufferException unused) {
            return null;
        }
    }

    public boolean isRecording() {
        return isRecordingImpl(this.mNativeHandle);
    }

    public boolean isFailoverUser() {
        return isFailoverUserImpl(this.mNativeHandle);
    }

    public boolean isNoHostUser() {
        return isNoHostUserImpl(this.mNativeHandle);
    }

    public boolean clientOSSupportRecord() {
        return clientOSSupportRecordImpl(this.mNativeHandle);
    }

    public int getClientCapability() {
        return getClientCapabilityImpl(this.mNativeHandle);
    }

    public boolean inSilentMode() {
        return inSilentModeImpl(this.mNativeHandle);
    }

    public boolean isLeavingSilentMode() {
        return isLeavingSilentModeImpl(this.mNativeHandle);
    }

    public boolean getRaiseHandState() {
        return getRaiseHandStateImpl(this.mNativeHandle);
    }

    public boolean videoCanMuteByHost() {
        return videoCanMutebyHostImpl(this.mNativeHandle);
    }

    public boolean videoCanUnmuteByHost() {
        return videoCanUnmuteByHostImpl(this.mNativeHandle);
    }

    public boolean canActAsCoHost() {
        return canActAsCoHostImpl(this.mNativeHandle);
    }

    @Nullable
    public String getUserGUID() {
        return getUserGUIDImpl(this.mNativeHandle);
    }

    @Nullable
    public String getUserJoinedBID() {
        return getUserJoinedBIDImpl(this.mNativeHandle);
    }

    public boolean isInBOMeeting() {
        return isInBOMeetingImpl(this.mNativeHandle);
    }

    public boolean isBOModerator() {
        return isBOModeratorImpl(this.mNativeHandle);
    }

    public boolean supportSwitchCam() {
        return supportSwitchCamImpl(this.mNativeHandle);
    }

    public boolean isInAttentionMode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInAttentionModeImpl(j);
    }

    public int getFeedback() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFeedbackImpl(j);
    }

    public boolean canActAsCCEditor() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canActAsCCEditorImpl(j);
    }

    public boolean canEditCC() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return canEditCCImpl(j);
    }

    public boolean isViewOnlyUserCanTalk() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isViewOnlyUserCanTalkImpl(j);
    }

    public boolean isViewOnlyUser() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isViewOnlyUserImpl(j);
    }

    public boolean isGuest() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isGuestImpl(j);
    }

    public long getRaiseHandTimestamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRaiseHandTimestampImpl(j);
    }

    public boolean isSharingPureComputerAudio() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isSharingPureComputerAudioImpl(j);
    }

    public boolean isInterpreter() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInterpreterImpl(j);
    }

    public boolean isNoAudioClientUser() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isNoAudioClientUserImpl(j);
    }

    public boolean isBoundTelClientUser() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isBoundTelClientUserImpl(j);
    }

    public int[] getInterpreterLans() {
        return getInterpreterLansImpl(this.mNativeHandle);
    }

    public int getInterpreterActiveLan() {
        return getInterpreterActiveLanImpl(this.mNativeHandle);
    }

    public int getParticipantActiveLan() {
        return getParticipantActiveLanImpl(this.mNativeHandle);
    }

    public int getAudioConnectStatus() {
        return getAudioConnectStatusImpl(this.mNativeHandle);
    }

    public int getEmojiReactionType() {
        if (!isEmojiReactionExpiredImpl(this.mNativeHandle)) {
            return getEmojiReactionTypeImpl(this.mNativeHandle);
        }
        return 0;
    }

    public int getEmojiReactionSkinTone() {
        return getEmojiReactionSkinToneImpl(this.mNativeHandle);
    }

    public String getEmojiReactionUnicode() {
        return !isEmojiReactionExpiredImpl(this.mNativeHandle) ? getEmojiReactionImpl(this.mNativeHandle) : "";
    }
}
