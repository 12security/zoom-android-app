package com.zipow.videobox.sip.server;

import androidx.annotation.Nullable;

public class CmmSIPAudioFileItem {
    private long mNativeHandle;

    private native int getAuidoFileFormatImpl(long j);

    private native int getFileDownloadPercentImpl(long j);

    private native int getFileDurationImpl(long j);

    @Nullable
    private native String getIDImpl(long j);

    @Nullable
    private native String getLocalFileNameImpl(long j);

    @Nullable
    private native String getOwnerIDImpl(long j);

    private native int getOwnerTypeImpl(long j);

    private native boolean isFileDownloadingImpl(long j);

    private native boolean isFileInLocalImpl(long j);

    public CmmSIPAudioFileItem(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public String getID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getIDImpl(j);
    }

    @Nullable
    public String getOwnerID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerIDImpl(j);
    }

    public int getOwnerType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 2;
        }
        return getOwnerTypeImpl(j);
    }

    public int getOwerType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getOwnerTypeImpl(j);
    }

    public int getAuidoFileFormat() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 2;
        }
        return getAuidoFileFormatImpl(j);
    }

    public boolean isFileDownloading() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isFileDownloadingImpl(j);
    }

    public boolean isFileInLocal() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isFileInLocalImpl(j);
    }

    @Nullable
    public String getLocalFileName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocalFileNameImpl(j);
    }

    public int getFileDuration() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFileDurationImpl(j);
    }

    public int getFileDownloadPercent() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFileDownloadPercentImpl(j);
    }

    public void toSIPAudioFileItemBean(@Nullable CmmSIPAudioFileItemBean cmmSIPAudioFileItemBean) {
        if (cmmSIPAudioFileItemBean != null) {
            cmmSIPAudioFileItemBean.setFileInLocal(isFileInLocal());
            cmmSIPAudioFileItemBean.setFileDownloading(isFileDownloading());
            cmmSIPAudioFileItemBean.setFileDownloadPercent(getFileDownloadPercent());
            cmmSIPAudioFileItemBean.setOwnerType(getOwnerType());
            cmmSIPAudioFileItemBean.setOwnerId(getOwnerID());
            cmmSIPAudioFileItemBean.setLocalFileName(getLocalFileName());
            cmmSIPAudioFileItemBean.setFileDuration(getFileDuration());
            cmmSIPAudioFileItemBean.setAudioFileFormat(getAuidoFileFormat());
        }
    }
}
