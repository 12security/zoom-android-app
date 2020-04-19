package com.zipow.videobox.sip.server;

public class CmmSIPAudioFileItemBean {
    private int audioFileFormat;
    private int fileDownloadPercent;
    private int fileDuration;

    /* renamed from: id */
    private String f327id;
    private boolean isFileDownloading;
    private boolean isFileInLocal;
    private String localFileName;
    private String ownerId;
    private int ownerType;

    public String getId() {
        return this.f327id;
    }

    public void setId(String str) {
        this.f327id = str;
    }

    public String getOwnerId() {
        return this.ownerId;
    }

    public void setOwnerId(String str) {
        this.ownerId = str;
    }

    public int getOwnerType() {
        return this.ownerType;
    }

    public void setOwnerType(int i) {
        this.ownerType = i;
    }

    public int getAudioFileFormat() {
        return this.audioFileFormat;
    }

    public void setAudioFileFormat(int i) {
        this.audioFileFormat = i;
    }

    public boolean isFileDownloading() {
        return this.isFileDownloading;
    }

    public void setFileDownloading(boolean z) {
        this.isFileDownloading = z;
    }

    public boolean isFileInLocal() {
        return this.isFileInLocal;
    }

    public void setFileInLocal(boolean z) {
        this.isFileInLocal = z;
    }

    public String getLocalFileName() {
        return this.localFileName;
    }

    public void setLocalFileName(String str) {
        this.localFileName = str;
    }

    public int getFileDuration() {
        return this.fileDuration;
    }

    public void setFileDuration(int i) {
        this.fileDuration = i;
    }

    public int getFileDownloadPercent() {
        return this.fileDownloadPercent;
    }

    public void setFileDownloadPercent(int i) {
        this.fileDownloadPercent = i;
    }
}
