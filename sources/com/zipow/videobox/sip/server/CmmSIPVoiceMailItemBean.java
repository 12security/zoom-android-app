package com.zipow.videobox.sip.server;

import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

public class CmmSIPVoiceMailItemBean implements ICmmPBXHistoryItemBean {
    private List<CmmSIPAudioFileItemBean> audioFileList;
    private long createTime;
    private String displayName;
    private String displayPhoneNumber;
    private String forwardExtensionId;
    private int forwardExtensionLevel;
    private String forwardExtensionName;
    private String fromPhoneNumber;
    private String fromUserName;

    /* renamed from: id */
    private String f331id;
    private boolean isChangeStatusPending;
    private boolean isDeletePending;
    private boolean isRestricted;
    private boolean isUnread;
    private String phoneNumberContentDescription;

    public String getId() {
        return this.f331id;
    }

    public void setId(String str) {
        this.f331id = str;
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

    public boolean isUnread() {
        return this.isUnread;
    }

    public void setUnread(boolean z) {
        this.isUnread = z;
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

    public List<CmmSIPAudioFileItemBean> getAudioFileList() {
        return this.audioFileList;
    }

    public void setAudioFileList(List<CmmSIPAudioFileItemBean> list) {
        this.audioFileList = list;
    }

    public boolean isDeletePending() {
        return this.isDeletePending;
    }

    public void setDeletePending(boolean z) {
        this.isDeletePending = z;
    }

    public boolean isChangeStatusPending() {
        return this.isChangeStatusPending;
    }

    public void setChangeStatusPending(boolean z) {
        this.isChangeStatusPending = z;
    }

    public String getPhoneNumberContentDescription() {
        return this.phoneNumberContentDescription;
    }

    public void setPhoneNumberContentDescription(String str) {
        this.phoneNumberContentDescription = str;
    }

    public String getForwardExtensionId() {
        return this.forwardExtensionId;
    }

    public void setForwardExtensionId(String str) {
        this.forwardExtensionId = str;
    }

    public String getForwardExtensionName() {
        return this.forwardExtensionName;
    }

    public void setForwardExtensionName(String str) {
        this.forwardExtensionName = str;
    }

    public int getForwardExtensionLevel() {
        return this.forwardExtensionLevel;
    }

    public void setForwardExtensionLevel(int i) {
        this.forwardExtensionLevel = i;
    }

    public String getDisplayPhoneNumber() {
        return StringUtil.isEmptyOrNull(this.displayPhoneNumber) ? this.fromPhoneNumber : this.displayPhoneNumber;
    }

    public void setDisplayPhoneNumber(String str) {
        this.displayPhoneNumber = str;
    }

    public String getDisplayName() {
        return StringUtil.isEmptyOrNull(this.displayName) ? this.fromUserName : this.displayName;
    }

    public void setDisplayName(String str) {
        this.displayName = str;
    }
}
