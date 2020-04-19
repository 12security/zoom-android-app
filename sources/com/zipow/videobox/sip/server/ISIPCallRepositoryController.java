package com.zipow.videobox.sip.server;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTAppProtos.CmmCallHistoryFilterDataProto;
import com.zipow.videobox.ptapp.PTAppProtos.CmmCallHistoryFilterDataProtoList;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPVoiceMailSharedRelationshipProto;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPVoiceMailSharedRelationshipProtoList;
import com.zipow.videobox.ptapp.PTAppProtos.PBXCallHistoryList;
import com.zipow.videobox.ptapp.PTAppProtos.PBXCallHistoryProto;
import com.zipow.videobox.ptapp.PTAppProtos.PBXVoiceMailHistoryList;
import com.zipow.videobox.ptapp.PTAppProtos.PBXVoiceMailHistoryProto;
import java.util.List;
import p021us.zoom.androidlib.util.CollectionsUtil;
import p021us.zoom.androidlib.util.StringUtil;

public class ISIPCallRepositoryController {
    private long mNativeHandle;

    private native boolean blockPhoneNumberImpl(long j, List<String> list, String str);

    private native boolean changeVoiceMailStatusImpl(long j, List<String> list, boolean z);

    private native void checkCallHistoryFilterTypeImpl(long j, int i, boolean z);

    private native void checkVoicemailSharedRelationshipImpl(long j, String str, boolean z);

    private native void clearAllCallHistoryImpl(long j);

    private native void clearAllVoiceMailImpl(long j);

    private native void clearEventSinkImpl(long j);

    private native boolean clearMissedCallHistoryImpl(long j);

    private native boolean deleteCallHistoryImpl(long j, List<String> list);

    private native boolean deleteVoiceMailImpl(long j, List<String> list);

    @Nullable
    private native byte[] filterCallHistoryListByIDImpl(long j, List<String> list);

    @Nullable
    private native byte[] filterVoiceMailHistoryListByIDImpl(long j, List<String> list);

    private native long getAudioFileItemByIDImpl(long j, String str, int i);

    private native long getAudioFileItemByPathImpl(long j, String str, int i);

    @Nullable
    private native byte[] getCallHistoryFiltersImpl(long j);

    private native long getCallHistoryItemByIDImpl(long j, String str);

    private native long getCallHistoryItemByIndexImpl(long j, int i);

    private native int getCallHistoryItemCountImpl(long j);

    @Nullable
    private native byte[] getCallHistoryListByIDImpl(long j, List<String> list);

    @Nullable
    private native byte[] getCallHistoryListByPageImpl(long j, String str, int i);

    @Nullable
    private native byte[] getCallHistoryListImpl(long j);

    private native int getMissedCallHistoryCountImpl(long j);

    private native int getTotalUnreadVoiceMailCountImpl(long j);

    @Nullable
    private native byte[] getVoiceMailHistoryListByIDImpl(long j, List<String> list);

    @Nullable
    private native byte[] getVoiceMailHistoryListByPageImpl(long j, String str, int i);

    @Nullable
    private native byte[] getVoiceMailHistoryListImpl(long j);

    private native long getVoiceMailItemByIDImpl(long j, String str);

    private native long getVoiceMailItemByIndexImpl(long j, int i);

    private native int getVoiceMailItemCountImpl(long j);

    @Nullable
    private native byte[] getVoicemailSharedRelationshipsImpl(long j);

    private native boolean hasMorePastCallHistoryImpl(long j);

    private native boolean hasMorePastVoiceMailImpl(long j);

    private native boolean hasNotDeletePendingCallHistoryImpl(long j, String str);

    private native boolean hasNotDeletePendingVoiceMailImpl(long j, String str);

    private native boolean isCallHistorySyncStartedImpl(long j);

    private native boolean isVoiceMailSyncStartedImpl(long j);

    private native boolean requestDownloadAudioFileImpl(long j, String str, int i);

    private native boolean requestForVoiceMailTranscriptImpl(long j, String str);

    private native boolean requestSyncMoreCallHistoryImpl(long j, boolean z);

    private native boolean requestSyncMoreVoiceMailImpl(long j, boolean z);

    private native void setEventSinkImpl(long j, long j2);

    public ISIPCallRepositoryController(long j) {
        this.mNativeHandle = j;
    }

    public void setEventSink(@NonNull ISIPCallRepositoryEventSinkListenerUI iSIPCallRepositoryEventSinkListenerUI) {
        long j = this.mNativeHandle;
        if (j != 0) {
            setEventSinkImpl(j, iSIPCallRepositoryEventSinkListenerUI.getNativeHandle());
        }
    }

    public void clearEventSink() {
        long j = this.mNativeHandle;
        if (j != 0) {
            clearEventSinkImpl(j);
        }
    }

    public int getCallHistoryItemCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getCallHistoryItemCountImpl(j);
    }

    public boolean hasNotDeletePendingCallHistory(@Nullable String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasNotDeletePendingCallHistoryImpl(j, StringUtil.safeString(str));
    }

    @Nullable
    public CmmSIPCallHistoryItem getCallHistoryItemByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long callHistoryItemByIndexImpl = getCallHistoryItemByIndexImpl(j, i);
        if (callHistoryItemByIndexImpl == 0) {
            return null;
        }
        return new CmmSIPCallHistoryItem(callHistoryItemByIndexImpl);
    }

    @Nullable
    public CmmSIPCallHistoryItem getCallHistoryItemByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long callHistoryItemByIDImpl = getCallHistoryItemByIDImpl(j, StringUtil.safeString(str));
        if (callHistoryItemByIDImpl == 0) {
            return null;
        }
        return new CmmSIPCallHistoryItem(callHistoryItemByIDImpl);
    }

    @Nullable
    public List<PBXCallHistoryProto> getCallHistoryListByPage(@Nullable String str, int i) {
        PBXCallHistoryList pBXCallHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] callHistoryListByPageImpl = getCallHistoryListByPageImpl(j, StringUtil.safeString(str), i);
        if (callHistoryListByPageImpl == null || callHistoryListByPageImpl.length <= 0) {
            return null;
        }
        try {
            pBXCallHistoryList = PBXCallHistoryList.parseFrom(callHistoryListByPageImpl);
        } catch (Exception unused) {
            pBXCallHistoryList = null;
        }
        if (pBXCallHistoryList != null) {
            return pBXCallHistoryList.getCallhistorysList();
        }
        return null;
    }

    @Nullable
    public List<PBXCallHistoryProto> getCallHistoryList() {
        PBXCallHistoryList pBXCallHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] callHistoryListImpl = getCallHistoryListImpl(j);
        if (callHistoryListImpl == null || callHistoryListImpl.length <= 0) {
            return null;
        }
        try {
            pBXCallHistoryList = PBXCallHistoryList.parseFrom(callHistoryListImpl);
        } catch (Exception unused) {
            pBXCallHistoryList = null;
        }
        if (pBXCallHistoryList != null) {
            return pBXCallHistoryList.getCallhistorysList();
        }
        return null;
    }

    @Nullable
    public List<PBXCallHistoryProto> getCallHistoryListByID(List<String> list) {
        PBXCallHistoryList pBXCallHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] callHistoryListByIDImpl = getCallHistoryListByIDImpl(j, list);
        if (callHistoryListByIDImpl == null || callHistoryListByIDImpl.length <= 0) {
            return null;
        }
        try {
            pBXCallHistoryList = PBXCallHistoryList.parseFrom(callHistoryListByIDImpl);
        } catch (Exception unused) {
            pBXCallHistoryList = null;
        }
        if (pBXCallHistoryList != null) {
            return pBXCallHistoryList.getCallhistorysList();
        }
        return null;
    }

    @Nullable
    public List<PBXCallHistoryProto> filterCallHistoryListByID(List<String> list) {
        PBXCallHistoryList pBXCallHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] filterCallHistoryListByIDImpl = filterCallHistoryListByIDImpl(j, list);
        if (filterCallHistoryListByIDImpl == null || filterCallHistoryListByIDImpl.length <= 0) {
            return null;
        }
        try {
            pBXCallHistoryList = PBXCallHistoryList.parseFrom(filterCallHistoryListByIDImpl);
        } catch (Exception unused) {
            pBXCallHistoryList = null;
        }
        if (pBXCallHistoryList != null) {
            return pBXCallHistoryList.getCallhistorysList();
        }
        return null;
    }

    public boolean clearMissedCallHistory() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return clearMissedCallHistoryImpl(j);
    }

    public int getMissedCallHistoryCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getMissedCallHistoryCountImpl(j);
    }

    public boolean requestSyncMoreCallHistory(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return requestSyncMoreCallHistoryImpl(j, z);
    }

    public boolean hasMorePastCallHistory() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasMorePastCallHistoryImpl(j);
    }

    public boolean isCallHistorySyncStarted() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isCallHistorySyncStartedImpl(j);
    }

    public boolean deleteCallHistory(@Nullable List<String> list) {
        if (this.mNativeHandle == 0 || list == null || list.isEmpty()) {
            return false;
        }
        return deleteCallHistoryImpl(this.mNativeHandle, list);
    }

    public void clearAllCallHistory() {
        long j = this.mNativeHandle;
        if (j != 0) {
            clearAllCallHistoryImpl(j);
        }
    }

    public int getTotalUnreadVoiceMailCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTotalUnreadVoiceMailCountImpl(j);
    }

    public int getVoiceMailItemCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getVoiceMailItemCountImpl(j);
    }

    public boolean hasNotDeletePendingVoiceMail(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasNotDeletePendingVoiceMailImpl(j, StringUtil.safeString(str));
    }

    @Nullable
    public CmmSIPVoiceMailItem getVoiceMailItemByIndex(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long voiceMailItemByIndexImpl = getVoiceMailItemByIndexImpl(j, i);
        if (voiceMailItemByIndexImpl == 0) {
            return null;
        }
        return new CmmSIPVoiceMailItem(voiceMailItemByIndexImpl);
    }

    @Nullable
    public CmmSIPVoiceMailItem getVoiceMailItemByID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long voiceMailItemByIDImpl = getVoiceMailItemByIDImpl(j, StringUtil.safeString(str));
        if (voiceMailItemByIDImpl == 0) {
            return null;
        }
        return new CmmSIPVoiceMailItem(voiceMailItemByIDImpl);
    }

    @Nullable
    public List<PBXVoiceMailHistoryProto> getVoiceMailHistoryList() {
        PBXVoiceMailHistoryList pBXVoiceMailHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] voiceMailHistoryListImpl = getVoiceMailHistoryListImpl(j);
        if (voiceMailHistoryListImpl == null || voiceMailHistoryListImpl.length <= 0) {
            return null;
        }
        try {
            pBXVoiceMailHistoryList = PBXVoiceMailHistoryList.parseFrom(voiceMailHistoryListImpl);
        } catch (Exception unused) {
            pBXVoiceMailHistoryList = null;
        }
        if (pBXVoiceMailHistoryList != null) {
            return pBXVoiceMailHistoryList.getVoiceMailsList();
        }
        return null;
    }

    @Nullable
    public List<PBXVoiceMailHistoryProto> getVoiceMailHistoryListByPage(String str, int i) {
        PBXVoiceMailHistoryList pBXVoiceMailHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] voiceMailHistoryListByPageImpl = getVoiceMailHistoryListByPageImpl(j, StringUtil.safeString(str), i);
        if (voiceMailHistoryListByPageImpl == null || voiceMailHistoryListByPageImpl.length <= 0) {
            return null;
        }
        try {
            pBXVoiceMailHistoryList = PBXVoiceMailHistoryList.parseFrom(voiceMailHistoryListByPageImpl);
        } catch (Exception unused) {
            pBXVoiceMailHistoryList = null;
        }
        if (pBXVoiceMailHistoryList != null) {
            return pBXVoiceMailHistoryList.getVoiceMailsList();
        }
        return null;
    }

    @Nullable
    public List<PBXVoiceMailHistoryProto> getVoiceMailHistoryListByID(List<String> list) {
        PBXVoiceMailHistoryList pBXVoiceMailHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] voiceMailHistoryListByIDImpl = getVoiceMailHistoryListByIDImpl(j, list);
        if (voiceMailHistoryListByIDImpl == null || voiceMailHistoryListByIDImpl.length <= 0) {
            return null;
        }
        try {
            pBXVoiceMailHistoryList = PBXVoiceMailHistoryList.parseFrom(voiceMailHistoryListByIDImpl);
        } catch (Exception unused) {
            pBXVoiceMailHistoryList = null;
        }
        if (pBXVoiceMailHistoryList != null) {
            return pBXVoiceMailHistoryList.getVoiceMailsList();
        }
        return null;
    }

    @Nullable
    public List<PBXVoiceMailHistoryProto> filterVoiceMailHistoryListByID(List<String> list) {
        PBXVoiceMailHistoryList pBXVoiceMailHistoryList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] filterVoiceMailHistoryListByIDImpl = filterVoiceMailHistoryListByIDImpl(j, list);
        if (filterVoiceMailHistoryListByIDImpl == null || filterVoiceMailHistoryListByIDImpl.length <= 0) {
            return null;
        }
        try {
            pBXVoiceMailHistoryList = PBXVoiceMailHistoryList.parseFrom(filterVoiceMailHistoryListByIDImpl);
        } catch (Exception unused) {
            pBXVoiceMailHistoryList = null;
        }
        if (pBXVoiceMailHistoryList != null) {
            return pBXVoiceMailHistoryList.getVoiceMailsList();
        }
        return null;
    }

    public boolean requestSyncMoreVoiceMail(boolean z) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return requestSyncMoreVoiceMailImpl(j, z);
    }

    public boolean hasMorePastVoiceMail() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasMorePastVoiceMailImpl(j);
    }

    public boolean isVoiceMailSyncStarted() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isVoiceMailSyncStartedImpl(j);
    }

    public boolean deleteVoiceMail(@Nullable List<String> list) {
        if (this.mNativeHandle == 0 || list == null || list.isEmpty()) {
            return false;
        }
        return deleteVoiceMailImpl(this.mNativeHandle, list);
    }

    public void clearAllVoiceMail() {
        long j = this.mNativeHandle;
        if (j != 0) {
            clearAllVoiceMailImpl(j);
        }
    }

    public boolean changeVoiceMailStatus(@Nullable List<String> list, boolean z) {
        if (this.mNativeHandle == 0 || list == null || list.isEmpty()) {
            return false;
        }
        return changeVoiceMailStatusImpl(this.mNativeHandle, list, z);
    }

    public boolean requestForVoiceMailTranscript(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return requestForVoiceMailTranscriptImpl(j, StringUtil.safeString(str));
    }

    public boolean requestDownloadAudioFile(String str, int i) {
        if (this.mNativeHandle != 0 && !StringUtil.isEmptyOrNull(str)) {
            return requestDownloadAudioFileImpl(this.mNativeHandle, StringUtil.safeString(str), i);
        }
        return false;
    }

    @Nullable
    public CmmSIPAudioFileItem getAudioFileItemByID(String str, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long audioFileItemByIDImpl = getAudioFileItemByIDImpl(j, StringUtil.safeString(str), i);
        if (audioFileItemByIDImpl == 0) {
            return null;
        }
        return new CmmSIPAudioFileItem(audioFileItemByIDImpl);
    }

    @Nullable
    public CmmSIPAudioFileItem getAudioFileItemByPath(String str, int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long audioFileItemByPathImpl = getAudioFileItemByPathImpl(j, StringUtil.safeString(str), i);
        if (audioFileItemByPathImpl == 0) {
            return null;
        }
        return new CmmSIPAudioFileItem(audioFileItemByPathImpl);
    }

    public boolean blockPhoneNumber(List<String> list, String str) {
        if (CollectionsUtil.isListEmpty(list)) {
            return false;
        }
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return blockPhoneNumberImpl(j, list, StringUtil.safeString(str));
    }

    @Nullable
    public List<CmmSIPVoiceMailSharedRelationshipProto> getVoicemailSharedRelationships() {
        CmmSIPVoiceMailSharedRelationshipProtoList cmmSIPVoiceMailSharedRelationshipProtoList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] voicemailSharedRelationshipsImpl = getVoicemailSharedRelationshipsImpl(j);
        if (voicemailSharedRelationshipsImpl == null || voicemailSharedRelationshipsImpl.length <= 0) {
            return null;
        }
        try {
            cmmSIPVoiceMailSharedRelationshipProtoList = CmmSIPVoiceMailSharedRelationshipProtoList.parseFrom(voicemailSharedRelationshipsImpl);
        } catch (Exception unused) {
            cmmSIPVoiceMailSharedRelationshipProtoList = null;
        }
        if (cmmSIPVoiceMailSharedRelationshipProtoList != null) {
            return cmmSIPVoiceMailSharedRelationshipProtoList.getRelationshipListList();
        }
        return null;
    }

    public void checkVoicemailSharedRelationship(@Nullable String str, boolean z) {
        if (this.mNativeHandle != 0 && !TextUtils.isEmpty(str)) {
            checkVoicemailSharedRelationshipImpl(this.mNativeHandle, StringUtil.safeString(str), z);
        }
    }

    @Nullable
    public List<CmmCallHistoryFilterDataProto> getCallHistoryFilters() {
        CmmCallHistoryFilterDataProtoList cmmCallHistoryFilterDataProtoList;
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        byte[] callHistoryFiltersImpl = getCallHistoryFiltersImpl(j);
        if (callHistoryFiltersImpl == null || callHistoryFiltersImpl.length <= 0) {
            return null;
        }
        try {
            cmmCallHistoryFilterDataProtoList = CmmCallHistoryFilterDataProtoList.parseFrom(callHistoryFiltersImpl);
        } catch (Exception unused) {
            cmmCallHistoryFilterDataProtoList = null;
        }
        if (cmmCallHistoryFilterDataProtoList != null) {
            return cmmCallHistoryFilterDataProtoList.getFilterDataListList();
        }
        return null;
    }

    public void checkCallHistoryFilterType(int i, boolean z) {
        long j = this.mNativeHandle;
        if (j != 0) {
            checkCallHistoryFilterTypeImpl(j, i, z);
        }
    }
}
