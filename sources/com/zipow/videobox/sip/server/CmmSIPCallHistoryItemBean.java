package com.zipow.videobox.sip.server;

import androidx.annotation.NonNull;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmSIPCallHistoryItemBean implements ICmmPBXHistoryItemBean {
    private int callDuration;
    private String callID;
    private int callType;
    private long createTime;
    private String displayName;
    private String displayPhoneNumber;
    private String fromExtensionID;
    private String fromPhoneNumber;
    private String fromUserName;

    /* renamed from: id */
    private String f328id;
    private String interceptExtensionID;
    private String interceptPhoneNumber;
    private String interceptUserName;
    private boolean isDeletePending;
    private boolean isInBound;
    private boolean isMissedCall;
    private boolean isRecordingExist;
    private boolean isRestricted;
    private String lineID;
    private String ownerExtensionID;
    private int ownerLevel;
    private String ownerName;
    private String ownerPhoneNumber;
    private String phoneNumberContentDescription;
    private CmmSIPAudioFileItemBean recordingAudioFile;
    private int resultType;
    private String slaInfo;
    private String toExtensionID;
    private String toPhoneNumber;
    private String toUserName;

    public String getId() {
        return this.f328id;
    }

    public void setId(String str) {
        this.f328id = str;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public boolean isRestricted() {
        return this.isRestricted;
    }

    public void setRestricted(boolean z) {
        this.isRestricted = z;
    }

    public void setCreateTime(long j) {
        this.createTime = j;
    }

    public int getCallDuration() {
        return this.callDuration;
    }

    public void setCallDuration(int i) {
        this.callDuration = i;
    }

    public int getResultType() {
        return this.resultType;
    }

    public void setResultType(int i) {
        this.resultType = i;
    }

    public boolean isInBound() {
        return this.isInBound;
    }

    public void setInBound(boolean z) {
        this.isInBound = z;
    }

    public String getFromUserName() {
        return this.fromUserName;
    }

    public void setFromUserName(String str) {
        this.fromUserName = str;
    }

    public String getFromPhoneNumber() {
        return this.fromPhoneNumber;
    }

    public void setFromPhoneNumber(String str) {
        this.fromPhoneNumber = str;
    }

    public String getToUserName() {
        return this.toUserName;
    }

    public void setToUserName(String str) {
        this.toUserName = str;
    }

    public String getToPhoneNumber() {
        return this.toPhoneNumber;
    }

    public void setToPhoneNumber(String str) {
        this.toPhoneNumber = str;
    }

    public boolean isRecordingExist() {
        return this.isRecordingExist;
    }

    public void setRecordingExist(boolean z) {
        this.isRecordingExist = z;
    }

    public CmmSIPAudioFileItemBean getRecordingAudioFile() {
        return this.recordingAudioFile;
    }

    public void setRecordingAudioFile(CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean) {
        this.recordingAudioFile = cmmSIPAudioFileItemBean;
    }

    public String getToExtensionID() {
        return this.toExtensionID;
    }

    public void setToExtensionID(String str) {
        this.toExtensionID = str;
    }

    public String getFromExtensionID() {
        return this.fromExtensionID;
    }

    public void setFromExtensionID(String str) {
        this.fromExtensionID = str;
    }

    public String getLineID() {
        return this.lineID;
    }

    public void setLineID(String str) {
        this.lineID = str;
    }

    public String getCallID() {
        return this.callID;
    }

    public void setCallID(String str) {
        this.callID = str;
    }

    public String getInterceptExtensionID() {
        return this.interceptExtensionID;
    }

    public void setInterceptExtensionID(String str) {
        this.interceptExtensionID = str;
    }

    public String getInterceptPhoneNumber() {
        return this.interceptPhoneNumber;
    }

    public void setInterceptPhoneNumber(String str) {
        this.interceptPhoneNumber = str;
    }

    public String getInterceptUserName() {
        return this.interceptUserName;
    }

    public void setInterceptUserName(String str) {
        this.interceptUserName = str;
    }

    public String getOwnerPhoneNumber() {
        return this.ownerPhoneNumber;
    }

    public void setOwnerPhoneNumber(String str) {
        this.ownerPhoneNumber = str;
    }

    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(String str) {
        this.ownerName = str;
    }

    public String getOwnerExtensionID() {
        return this.ownerExtensionID;
    }

    public void setOwnerExtensionID(String str) {
        this.ownerExtensionID = str;
    }

    public int getCallType() {
        return this.callType;
    }

    public void setCallType(int i) {
        this.callType = i;
    }

    public boolean isDeletePending() {
        return this.isDeletePending;
    }

    public void setDeletePending(boolean z) {
        this.isDeletePending = z;
    }

    public boolean isMissedCall() {
        return this.isMissedCall;
    }

    public void setMissedCall(boolean z) {
        this.isMissedCall = z;
    }

    public String getPhoneNumberContentDescription() {
        return this.phoneNumberContentDescription;
    }

    public void setPhoneNumberContentDescription(String str) {
        this.phoneNumberContentDescription = str;
    }

    public String getDisplayPhoneNumber() {
        if (StringUtil.isEmptyOrNull(this.displayPhoneNumber)) {
            return this.isInBound ? this.fromPhoneNumber : this.toPhoneNumber;
        }
        return this.displayPhoneNumber;
    }

    public void setDisplayPhoneNumber(String str) {
        this.displayPhoneNumber = str;
    }

    public void setSlaInfo(String str) {
        this.slaInfo = str;
    }

    public String getSlaInfo() {
        return this.slaInfo;
    }

    public boolean isSLAType() {
        return this.callType == 1 && this.ownerLevel == 1;
    }

    public boolean isSLGLevel() {
        return this.ownerLevel == 7;
    }

    public boolean isCQLevel() {
        return this.ownerLevel == 2;
    }

    public String getDisplayName() {
        if (StringUtil.isEmptyOrNull(this.displayName)) {
            return this.isInBound ? this.fromUserName : this.toUserName;
        }
        return this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }

    public int getOwnerLevel() {
        return this.ownerLevel;
    }

    public void setOwnerLevel(int i) {
        this.ownerLevel = i;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CmmSIPCallHistoryItemBean{id='");
        sb.append(this.f328id);
        sb.append('\'');
        sb.append(", isInBound=");
        sb.append(this.isInBound);
        sb.append(", callType=");
        sb.append(this.callType);
        sb.append(", fromExtensionID='");
        sb.append(this.fromExtensionID);
        sb.append('\'');
        sb.append(", fromUserName='");
        sb.append(this.fromUserName);
        sb.append('\'');
        sb.append(", toExtensionID='");
        sb.append(this.toExtensionID);
        sb.append('\'');
        sb.append(", toUserName='");
        sb.append(this.toUserName);
        sb.append('\'');
        sb.append(", interceptExtensionID='");
        sb.append(this.interceptExtensionID);
        sb.append('\'');
        sb.append(", interceptUserName='");
        sb.append(this.interceptUserName);
        sb.append('\'');
        sb.append(", ownerExtensionID='");
        sb.append(this.ownerExtensionID);
        sb.append('\'');
        sb.append(", ownerName='");
        sb.append(this.ownerName);
        sb.append('\'');
        sb.append(", ownerLevel='");
        sb.append(this.ownerLevel);
        sb.append('\'');
        sb.append(", createTime=");
        sb.append(this.createTime);
        sb.append('}');
        return sb.toString();
    }
}
