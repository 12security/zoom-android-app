package com.zipow.videobox.sip.server;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.PTAppProtos.CmmCallHistoryFilterDataProto;
import com.zipow.videobox.ptapp.PTAppProtos.CmmSIPVoiceMailSharedRelationshipProto;
import com.zipow.videobox.ptapp.PTAppProtos.PBXAudioFileProto;
import com.zipow.videobox.ptapp.PTAppProtos.PBXCallHistoryProto;
import com.zipow.videobox.ptapp.PTAppProtos.PBXVoiceMailHistoryProto;
import com.zipow.videobox.sip.server.ISIPCallRepositoryEventSinkListenerUI.ISIPCallRepositoryEventSinkListener;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmPBXCallHistoryManager {
    private static final String TAG = "CmmPBXCallHistoryManager";
    private static CmmPBXCallHistoryManager instance;

    public static CmmPBXCallHistoryManager getInstance() {
        if (instance == null) {
            synchronized (CmmPBXCallHistoryManager.class) {
                if (instance == null) {
                    instance = new CmmPBXCallHistoryManager();
                }
            }
        }
        return instance;
    }

    private CmmPBXCallHistoryManager() {
    }

    public void setEventSink() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            repositoryController.setEventSink(ISIPCallRepositoryEventSinkListenerUI.getInstance());
        }
    }

    public void clearEventSink() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            repositoryController.clearEventSink();
        }
    }

    private ISIPCallRepositoryController getRepositoryController() {
        if (!CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            return null;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI == null) {
            return null;
        }
        return sipCallAPI.getRepositoryController();
    }

    private ISIPAudioFilePlayer getAudioFilePlayer() {
        if (!CmmSIPCallManager.getInstance().isCloudPBXEnabled()) {
            return null;
        }
        ISIPCallAPI sipCallAPI = PTApp.getInstance().getSipCallAPI();
        if (sipCallAPI != null && sipCallAPI.isInited()) {
            return sipCallAPI.getAudioFilePlayer();
        }
        return null;
    }

    public boolean deletePBXCallHistory(String str) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        return repositoryController.deleteCallHistory(arrayList);
    }

    public boolean deletePBXCallHistory(List<String> list) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            return repositoryController.deleteCallHistory(list);
        }
        return false;
    }

    public void clearPBXCallHistory() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            repositoryController.clearAllCallHistory();
        }
    }

    public int getCallHistoryCount() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return 0;
        }
        return repositoryController.getCallHistoryItemCount();
    }

    public boolean hasNotDeletePendingCallHistory(String str) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.hasNotDeletePendingCallHistory(str);
    }

    @Nullable
    public List<CmmSIPCallHistoryItemBean> getCallHistoryList() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List callHistoryList = repositoryController.getCallHistoryList();
        if (callHistoryList == null) {
            return null;
        }
        int size = callHistoryList.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToCallHistoryItemBean((PBXCallHistoryProto) callHistoryList.get(i)));
        }
        return arrayList;
    }

    @Nullable
    public List<CmmSIPCallHistoryItemBean> getCallHistoryListByID(@Nullable List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List callHistoryListByID = repositoryController.getCallHistoryListByID(list);
        if (callHistoryListByID == null) {
            return null;
        }
        int size = callHistoryListByID.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToCallHistoryItemBean((PBXCallHistoryProto) callHistoryListByID.get(i)));
        }
        return arrayList;
    }

    @Nullable
    public List<CmmSIPCallHistoryItemBean> filterCallHistoryListByID(@Nullable List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List filterCallHistoryListByID = repositoryController.filterCallHistoryListByID(list);
        if (filterCallHistoryListByID == null) {
            return null;
        }
        int size = filterCallHistoryListByID.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToCallHistoryItemBean((PBXCallHistoryProto) filterCallHistoryListByID.get(i)));
        }
        return arrayList;
    }

    @Nullable
    public List<CmmSIPCallHistoryItemBean> getCallHistoryListByPage(String str, int i) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List callHistoryListByPage = repositoryController.getCallHistoryListByPage(str, i);
        if (callHistoryListByPage == null) {
            return null;
        }
        int size = callHistoryListByPage.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i2 = 0; i2 < size; i2++) {
            arrayList.add(protoToCallHistoryItemBean((PBXCallHistoryProto) callHistoryListByPage.get(i2)));
        }
        return arrayList;
    }

    @NonNull
    private CmmSIPCallHistoryItemBean protoToCallHistoryItemBean(PBXCallHistoryProto pBXCallHistoryProto) {
        CmmSIPCallHistoryItemBean cmmSIPCallHistoryItemBean = new CmmSIPCallHistoryItemBean();
        cmmSIPCallHistoryItemBean.setCallDuration(pBXCallHistoryProto.getCallDuration());
        cmmSIPCallHistoryItemBean.setCreateTime(pBXCallHistoryProto.getCreateTime());
        cmmSIPCallHistoryItemBean.setDeletePending(pBXCallHistoryProto.getIsDeletePending());
        cmmSIPCallHistoryItemBean.setId(pBXCallHistoryProto.getId());
        cmmSIPCallHistoryItemBean.setFromPhoneNumber(pBXCallHistoryProto.getFromPhoneNumber());
        cmmSIPCallHistoryItemBean.setFromUserName(pBXCallHistoryProto.getFromUserName());
        cmmSIPCallHistoryItemBean.setInBound(pBXCallHistoryProto.getIsInBound());
        cmmSIPCallHistoryItemBean.setRecordingExist(pBXCallHistoryProto.getIsRecordingExist());
        cmmSIPCallHistoryItemBean.setToPhoneNumber(pBXCallHistoryProto.getToPhoneNumber());
        cmmSIPCallHistoryItemBean.setToUserName(pBXCallHistoryProto.getToUserName());
        cmmSIPCallHistoryItemBean.setResultType(pBXCallHistoryProto.getResultType());
        cmmSIPCallHistoryItemBean.setMissedCall(pBXCallHistoryProto.getIsMissedCall());
        cmmSIPCallHistoryItemBean.setToExtensionID(pBXCallHistoryProto.getToExtensionId());
        cmmSIPCallHistoryItemBean.setFromExtensionID(pBXCallHistoryProto.getFromExtensionId());
        cmmSIPCallHistoryItemBean.setInterceptExtensionID(pBXCallHistoryProto.getInterceptExtensionId());
        cmmSIPCallHistoryItemBean.setInterceptPhoneNumber(pBXCallHistoryProto.getInterceptPhoneNumber());
        cmmSIPCallHistoryItemBean.setInterceptUserName(pBXCallHistoryProto.getInterceptUserName());
        cmmSIPCallHistoryItemBean.setOwnerExtensionID(pBXCallHistoryProto.getOwnerExtensionId());
        cmmSIPCallHistoryItemBean.setOwnerPhoneNumber(pBXCallHistoryProto.getOwnerPhoneNumber());
        cmmSIPCallHistoryItemBean.setOwnerName(pBXCallHistoryProto.getOwnerName());
        cmmSIPCallHistoryItemBean.setCallID(pBXCallHistoryProto.getCallId());
        cmmSIPCallHistoryItemBean.setLineID(pBXCallHistoryProto.getLineId());
        cmmSIPCallHistoryItemBean.setCallType(pBXCallHistoryProto.getCallType());
        cmmSIPCallHistoryItemBean.setDisplayPhoneNumber(pBXCallHistoryProto.getDisplayPhoneNumber());
        cmmSIPCallHistoryItemBean.setDisplayName(pBXCallHistoryProto.getDisplayName());
        cmmSIPCallHistoryItemBean.setRestricted(pBXCallHistoryProto.getIsRestricted());
        cmmSIPCallHistoryItemBean.setOwnerLevel(pBXCallHistoryProto.getOwnerLevel());
        PBXAudioFileProto recordingAudioFile = pBXCallHistoryProto.getIsRecordingExist() ? pBXCallHistoryProto.getRecordingAudioFile() : null;
        if (recordingAudioFile != null) {
            cmmSIPCallHistoryItemBean.setRecordingAudioFile(protoToAudioFileItemBean(recordingAudioFile));
        }
        return cmmSIPCallHistoryItemBean;
    }

    @NonNull
    private CmmSIPAudioFileItemBean protoToAudioFileItemBean(PBXAudioFileProto pBXAudioFileProto) {
        CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean = new CmmSIPAudioFileItemBean();
        cmmSIPAudioFileItemBean.setAudioFileFormat(pBXAudioFileProto.getAudioFileFormat());
        cmmSIPAudioFileItemBean.setFileDownloading(pBXAudioFileProto.getIsFileDownloading());
        cmmSIPAudioFileItemBean.setFileDuration(pBXAudioFileProto.getFileDuration());
        cmmSIPAudioFileItemBean.setFileDownloadPercent(pBXAudioFileProto.getFileDownloadPercent());
        cmmSIPAudioFileItemBean.setFileInLocal(pBXAudioFileProto.getIsFileInLocal());
        cmmSIPAudioFileItemBean.setId(pBXAudioFileProto.getId());
        cmmSIPAudioFileItemBean.setLocalFileName(pBXAudioFileProto.getLocalFileName());
        cmmSIPAudioFileItemBean.setOwnerId(pBXAudioFileProto.getOwnerID());
        cmmSIPAudioFileItemBean.setOwnerType(pBXAudioFileProto.getOwnerType());
        return cmmSIPAudioFileItemBean;
    }

    @Nullable
    public List<CmmSIPVoiceMailItemBean> getVoiceMailHistoryList() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List voiceMailHistoryList = repositoryController.getVoiceMailHistoryList();
        if (voiceMailHistoryList == null) {
            return null;
        }
        int size = voiceMailHistoryList.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToVoiceMailHistoryItemBean((PBXVoiceMailHistoryProto) voiceMailHistoryList.get(i)));
        }
        return arrayList;
    }

    @Nullable
    public List<CmmSIPVoiceMailItemBean> getVoiceMailHistoryListByPage(@Nullable String str, int i) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List voiceMailHistoryListByPage = repositoryController.getVoiceMailHistoryListByPage(str, i);
        if (voiceMailHistoryListByPage == null) {
            return null;
        }
        int size = voiceMailHistoryListByPage.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i2 = 0; i2 < size; i2++) {
            arrayList.add(protoToVoiceMailHistoryItemBean((PBXVoiceMailHistoryProto) voiceMailHistoryListByPage.get(i2)));
        }
        return arrayList;
    }

    @Nullable
    public List<CmmSIPVoiceMailItemBean> getVoiceMailHistoryListByID(@Nullable List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List voiceMailHistoryListByID = repositoryController.getVoiceMailHistoryListByID(list);
        if (voiceMailHistoryListByID == null) {
            return null;
        }
        int size = voiceMailHistoryListByID.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToVoiceMailHistoryItemBean((PBXVoiceMailHistoryProto) voiceMailHistoryListByID.get(i)));
        }
        return arrayList;
    }

    @Nullable
    public List<CmmSIPVoiceMailItemBean> filterVoiceMailHistoryListByID(@Nullable List<String> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List filterVoiceMailHistoryListByID = repositoryController.filterVoiceMailHistoryListByID(list);
        if (filterVoiceMailHistoryListByID == null) {
            return null;
        }
        int size = filterVoiceMailHistoryListByID.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToVoiceMailHistoryItemBean((PBXVoiceMailHistoryProto) filterVoiceMailHistoryListByID.get(i)));
        }
        return arrayList;
    }

    @NonNull
    private CmmSIPVoiceMailItemBean protoToVoiceMailHistoryItemBean(PBXVoiceMailHistoryProto pBXVoiceMailHistoryProto) {
        CmmSIPVoiceMailItemBean cmmSIPVoiceMailItemBean = new CmmSIPVoiceMailItemBean();
        cmmSIPVoiceMailItemBean.setChangeStatusPending(pBXVoiceMailHistoryProto.getIsChangeStatusPending());
        cmmSIPVoiceMailItemBean.setCreateTime(pBXVoiceMailHistoryProto.getCreateTime());
        cmmSIPVoiceMailItemBean.setDeletePending(pBXVoiceMailHistoryProto.getIsDeletePending());
        cmmSIPVoiceMailItemBean.setId(pBXVoiceMailHistoryProto.getId());
        cmmSIPVoiceMailItemBean.setFromPhoneNumber(pBXVoiceMailHistoryProto.getFromPhoneNumber());
        cmmSIPVoiceMailItemBean.setFromUserName(pBXVoiceMailHistoryProto.getFromUserName());
        cmmSIPVoiceMailItemBean.setUnread(pBXVoiceMailHistoryProto.getIsUnread());
        cmmSIPVoiceMailItemBean.setForwardExtensionId(pBXVoiceMailHistoryProto.getForwardExtensionId());
        cmmSIPVoiceMailItemBean.setForwardExtensionName(pBXVoiceMailHistoryProto.getForwardExtensionName());
        cmmSIPVoiceMailItemBean.setForwardExtensionLevel(pBXVoiceMailHistoryProto.getForwardExtensionLevel());
        cmmSIPVoiceMailItemBean.setDisplayPhoneNumber(pBXVoiceMailHistoryProto.getDisplayPhoneNumber());
        cmmSIPVoiceMailItemBean.setDisplayName(pBXVoiceMailHistoryProto.getDisplayName());
        cmmSIPVoiceMailItemBean.setRestricted(pBXVoiceMailHistoryProto.getIsRestricted());
        List audioFileList = pBXVoiceMailHistoryProto.getAudioFileList();
        if (audioFileList != null) {
            int size = audioFileList.size();
            ArrayList arrayList = new ArrayList(size);
            cmmSIPVoiceMailItemBean.setAudioFileList(arrayList);
            for (int i = 0; i < size; i++) {
                arrayList.add(protoToAudioFileItemBean((PBXAudioFileProto) audioFileList.get(i)));
            }
        }
        return cmmSIPVoiceMailItemBean;
    }

    public boolean deleteVoiceMailHistory(String str) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        return repositoryController.deleteVoiceMail(arrayList);
    }

    public boolean deleteVoiceMailHistory(List<String> list) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            return repositoryController.deleteVoiceMail(list);
        }
        return false;
    }

    public void clearVoiceMailHistory() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            repositoryController.clearAllVoiceMail();
        }
    }

    public boolean changeVoiceMailStatusToRead(String str) {
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(str);
        return changeVoiceMailStatus(arrayList, false);
    }

    public boolean changeVoiceMailStatus(List<String> list, boolean z) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            return repositoryController.changeVoiceMailStatus(list, z);
        }
        return false;
    }

    public int getTotalUnreadVoiceMailCount() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            return repositoryController.getTotalUnreadVoiceMailCount();
        }
        return 0;
    }

    public int getMissedCallHistoryCount() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            return repositoryController.getMissedCallHistoryCount();
        }
        return 0;
    }

    public void clearMissedCallHistory() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            repositoryController.clearMissedCallHistory();
        }
    }

    @Nullable
    public CmmSIPCallHistoryItem getCallHistoryItemItemByIndex(int i) {
        if (i < 0) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        return repositoryController.getCallHistoryItemByIndex(i);
    }

    @Nullable
    public CmmSIPCallHistoryItem getCallHistoryItemItemByID(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        return repositoryController.getCallHistoryItemByID(str);
    }

    public int getVoiceMailItemCount() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return 0;
        }
        return repositoryController.getVoiceMailItemCount();
    }

    public boolean hasNotDeletePendingVoiceMail(String str) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.hasNotDeletePendingVoiceMail(str);
    }

    @Nullable
    public CmmSIPVoiceMailItem getVoiceMailItemByIndex(int i) {
        if (i < 0) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        return repositoryController.getVoiceMailItemByIndex(i);
    }

    @Nullable
    public CmmSIPVoiceMailItem getVoiceMailItemByID(String str) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        return repositoryController.getVoiceMailItemByID(str);
    }

    public boolean requestDownloadAudioFile(String str, int i) {
        if (StringUtil.isEmptyOrNull(str)) {
            return false;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.requestDownloadAudioFile(str, i);
    }

    @Nullable
    public CmmSIPAudioFileItem getAudioFileItemByID(String str, int i) {
        if (StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        return repositoryController.getAudioFileItemByID(str, i);
    }

    public boolean requestSyncMoreCallHistory(boolean z) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        if (!repositoryController.isCallHistorySyncStarted()) {
            return repositoryController.requestSyncMoreCallHistory(z);
        }
        return true;
    }

    public boolean hasMorePastCallHistory() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.hasMorePastCallHistory();
    }

    public boolean isCallHistorySyncStarted() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.isCallHistorySyncStarted();
    }

    public boolean requestSyncMoreVoiceMail(boolean z) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        if (!repositoryController.isVoiceMailSyncStarted()) {
            return repositoryController.requestSyncMoreVoiceMail(z);
        }
        return true;
    }

    public boolean requestForVoiceMailTranscript(String str) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.requestForVoiceMailTranscript(str);
    }

    public boolean hasMorePastVoiceMail() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.hasMorePastVoiceMail();
    }

    public boolean isVoiceMailSyncStarted() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.isVoiceMailSyncStarted();
    }

    public void addISIPCallRepositoryEventSinkListener(@Nullable ISIPCallRepositoryEventSinkListener iSIPCallRepositoryEventSinkListener) {
        if (iSIPCallRepositoryEventSinkListener != null) {
            ISIPCallRepositoryEventSinkListenerUI.getInstance().addListener(iSIPCallRepositoryEventSinkListener);
        }
    }

    public void removeISIPCallRepositoryEventSinkListener(@Nullable ISIPCallRepositoryEventSinkListener iSIPCallRepositoryEventSinkListener) {
        if (iSIPCallRepositoryEventSinkListener != null) {
            ISIPCallRepositoryEventSinkListenerUI.getInstance().removeListener(iSIPCallRepositoryEventSinkListener);
        }
    }

    public boolean blockPhoneNumber(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(str);
        return blockPhoneNumber((List<String>) arrayList, str2);
    }

    public boolean blockPhoneNumber(List<String> list, String str) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return false;
        }
        return repositoryController.blockPhoneNumber(list, str);
    }

    public List<CmmSIPVoiceMailSharedRelationshipBean> getVoicemailSharedRelationships() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List voicemailSharedRelationships = repositoryController.getVoicemailSharedRelationships();
        if (voicemailSharedRelationships == null) {
            return null;
        }
        int size = voicemailSharedRelationships.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToCmmSIPVoiceMailSharedRelationshipBean((CmmSIPVoiceMailSharedRelationshipProto) voicemailSharedRelationships.get(i)));
        }
        return arrayList;
    }

    @NonNull
    private CmmSIPVoiceMailSharedRelationshipBean protoToCmmSIPVoiceMailSharedRelationshipBean(CmmSIPVoiceMailSharedRelationshipProto cmmSIPVoiceMailSharedRelationshipProto) {
        CmmSIPVoiceMailSharedRelationshipBean cmmSIPVoiceMailSharedRelationshipBean = new CmmSIPVoiceMailSharedRelationshipBean();
        cmmSIPVoiceMailSharedRelationshipBean.setExtensionId(cmmSIPVoiceMailSharedRelationshipProto.getExtensionId());
        cmmSIPVoiceMailSharedRelationshipBean.setExtensionName(cmmSIPVoiceMailSharedRelationshipProto.getExtensionName());
        cmmSIPVoiceMailSharedRelationshipBean.setExtensionLevel(cmmSIPVoiceMailSharedRelationshipProto.getExtensionLevel());
        cmmSIPVoiceMailSharedRelationshipBean.setChecked(cmmSIPVoiceMailSharedRelationshipProto.getChecked());
        return cmmSIPVoiceMailSharedRelationshipBean;
    }

    @NonNull
    private CmmSIPVoiceMailSharedRelationshipBean newCmmSIPVoiceMailSharedRelationshipBean(int i) {
        CmmSIPVoiceMailSharedRelationshipBean cmmSIPVoiceMailSharedRelationshipBean = new CmmSIPVoiceMailSharedRelationshipBean();
        StringBuilder sb = new StringBuilder();
        sb.append("extensionId ");
        sb.append(i);
        cmmSIPVoiceMailSharedRelationshipBean.setExtensionId(sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("extensionName ");
        sb2.append(i);
        cmmSIPVoiceMailSharedRelationshipBean.setExtensionName(sb2.toString());
        cmmSIPVoiceMailSharedRelationshipBean.setExtensionLevel(i - 1);
        cmmSIPVoiceMailSharedRelationshipBean.setChecked(i % 3 == 0);
        return cmmSIPVoiceMailSharedRelationshipBean;
    }

    public void checkVoicemailSharedRelationship(@Nullable String str, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            ISIPCallRepositoryController repositoryController = getRepositoryController();
            if (repositoryController != null) {
                repositoryController.checkVoicemailSharedRelationship(str, z);
            }
        }
    }

    public List<CmmCallHistoryFilterDataBean> getCallHistoryFilterDataList() {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController == null) {
            return null;
        }
        List callHistoryFilters = repositoryController.getCallHistoryFilters();
        if (callHistoryFilters == null) {
            return null;
        }
        int size = callHistoryFilters.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(protoToCmmCallHistoryFilterDataBean((CmmCallHistoryFilterDataProto) callHistoryFilters.get(i)));
        }
        return arrayList;
    }

    @NonNull
    private CmmCallHistoryFilterDataBean protoToCmmCallHistoryFilterDataBean(CmmCallHistoryFilterDataProto cmmCallHistoryFilterDataProto) {
        CmmCallHistoryFilterDataBean cmmCallHistoryFilterDataBean = new CmmCallHistoryFilterDataBean();
        cmmCallHistoryFilterDataBean.setFilterType(cmmCallHistoryFilterDataProto.getFilterType());
        cmmCallHistoryFilterDataBean.setChecked(cmmCallHistoryFilterDataProto.getIsChecked());
        return cmmCallHistoryFilterDataBean;
    }

    public void checkCallHistoryFilterType(int i, boolean z) {
        ISIPCallRepositoryController repositoryController = getRepositoryController();
        if (repositoryController != null) {
            repositoryController.checkCallHistoryFilterType(i, z);
        }
    }
}
