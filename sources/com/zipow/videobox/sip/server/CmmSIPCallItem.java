package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProto;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPCallRemoteMemberProtoList;
import java.util.List;

public class CmmSIPCallItem {
    private long mNativeHandle;

    private native int getCallElapsedTimeImpl(long j);

    private native long getCallGenerateTimeImpl(long j);

    private native int getCallGenerateTypeImpl(long j);

    @Nullable
    private native String getCallIDImpl(long j);

    private native int getCallRecordingStatusImpl(long j);

    private native long getCallStartTimeImpl(long j);

    private native int getCallStatusImpl(long j);

    @Nullable
    private native String getCalledNumberImpl(long j);

    @Nullable
    private native String getCallerIDImpl(long j);

    @Nullable
    private native String getConferenceHostCallidImpl(long j);

    @Nullable
    private native String getConferenceParticipantCallItemByIndexImpl(long j, int i);

    private native int getConferenceParticipantsCountImpl(long j);

    private native int getConferenceRoleImpl(long j);

    private native int getCountryCodeImpl(long j);

    private native int getLastActionReasonImpl(long j);

    private native int getLastActionTypeImpl(long j);

    @Nullable
    private native String getLineIdImpl(long j);

    @Nullable
    private native String getPeerDisplayNameImpl(long j);

    @Nullable
    private native String getPeerFormatNumberImpl(long j);

    @Nullable
    private native String getPeerNumberImpl(long j);

    @Nullable
    private native String getPeerURIImpl(long j);

    private native long getRealTimePoliciesImpl(long j);

    @Nullable
    private native String getRelatedCallIDImpl(long j);

    @Nullable
    private native byte[] getRemoteMergerMembersImpl(long j);

    @Nullable
    private native String getSidImpl(long j);

    @Nullable
    private native String getThirdpartyNameImpl(long j);

    @Nullable
    private native String getThirdpartyNumberImpl(long j);

    private native int getThirdpartyTypeImpl(long j);

    private native boolean isDismissedImpl(long j);

    private native boolean isExecutingActionImpl(long j);

    private native boolean isInConferenceImpl(long j);

    private native boolean isNeedRingImpl(long j);

    public CmmSIPCallItem(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getCallID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCallIDImpl(j);
    }

    @Nullable
    public String getLineId() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLineIdImpl(j);
    }

    @Nullable
    public String getCallerID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCallerIDImpl(j);
    }

    @Nullable
    public String getPeerURI() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerURIImpl(j);
    }

    @Nullable
    public String getPeerNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerNumberImpl(j);
    }

    @Nullable
    public String getPeerFormatNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerFormatNumberImpl(j);
    }

    @Nullable
    public String getPeerDisplayName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPeerDisplayNameImpl(j);
    }

    public int getCountryCode() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getCountryCodeImpl(j);
    }

    @Nullable
    public String getCalledNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getCalledNumberImpl(j);
    }

    public int getCallGenerate() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getCallGenerateTypeImpl(j);
    }

    public int getCallStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 21;
        }
        return getCallStatusImpl(j);
    }

    public int getCallRecordingStatus() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallRecordingStatusImpl(j);
    }

    public int getLastActionType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 7;
        }
        return getLastActionTypeImpl(j);
    }

    public int getLastActionReason() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 10;
        }
        return getLastActionReasonImpl(j);
    }

    public boolean isExecutingAction() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isExecutingActionImpl(j);
    }

    public boolean isNeedRing() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isNeedRingImpl(j);
    }

    public boolean isDismissed() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDismissedImpl(j);
    }

    public long getCallGenerateTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallGenerateTimeImpl(j);
    }

    public long getCallStartTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallStartTimeImpl(j);
    }

    public long getRealTimePolicies() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getRealTimePoliciesImpl(j);
    }

    public int getCallElapsedTime() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallElapsedTimeImpl(j);
    }

    @Nullable
    public String getRelatedCallID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getRelatedCallIDImpl(j);
    }

    @Nullable
    public String getSid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSidImpl(j);
    }

    @Nullable
    public String getThirdpartyName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getThirdpartyNameImpl(j);
    }

    @Nullable
    public String getThirdpartyNumber() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getThirdpartyNumberImpl(j);
    }

    public int getThirdpartyType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getThirdpartyTypeImpl(j);
    }

    public boolean isInConference() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isInConferenceImpl(j);
    }

    public int getConferenceRole() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return getConferenceRoleImpl(j);
    }

    public int getConferenceParticipantsCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getConferenceParticipantsCountImpl(j);
    }

    @Nullable
    public String getConferenceParticipantCallItemByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getConferenceParticipantCallItemByIndexImpl(j, i);
    }

    @Nullable
    public String getConferenceHostCallid() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getConferenceHostCallidImpl(j);
    }

    public boolean isIncomingCall() {
        return getCallGenerate() == 0;
    }

    public static String generateLocalId(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("@[");
        sb.append(str);
        sb.append("]@");
        return sb.toString();
    }

    public static boolean isLocal(@Nullable String str) {
        return str != null && str.startsWith("@[") && str.endsWith("]@");
    }

    @Nullable
    public List<CmmSIPCallRemoteMemberProto> getRemoteMergerMembers() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] remoteMergerMembersImpl = getRemoteMergerMembersImpl(j);
        if (remoteMergerMembersImpl == null || remoteMergerMembersImpl.length <= 0) {
            return null;
        }
        try {
            CmmSIPCallRemoteMemberProtoList parseFrom = CmmSIPCallRemoteMemberProtoList.parseFrom(remoteMergerMembersImpl);
            if (parseFrom == null) {
                return null;
            }
            return parseFrom.getMemberListList();
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            return null;
        }
    }
}
