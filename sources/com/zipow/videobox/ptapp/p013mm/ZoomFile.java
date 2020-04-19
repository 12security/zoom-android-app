package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomFile */
public class ZoomFile {
    private long mNativeHandle = 0;

    @Nullable
    private native byte[] getFileIntegrationShareInfoImpl(long j);

    @Nullable
    private native String getFileNameImpl(long j);

    private native int getFileSizeImpl(long j);

    private native int getFileTransferStateImpl(long j);

    private native int getFileTypeImpl(long j);

    @Nullable
    private native String getFileURLImpl(long j);

    @Nullable
    private native String getLocalPathImpl(long j);

    @Nullable
    private native String getMessageIDImpl(long j);

    @Nullable
    private native String getOwnerImpl(long j);

    @Nullable
    private native String getPicturePreviewPathImpl(long j);

    @Nullable
    private native String getSessionIDImpl(long j);

    private native long getShareInfoImpl(long j);

    private native long getTimeStampImpl(long j);

    private native int getTransferredSizeImpl(long j);

    @Nullable
    private native String getWebFileIDImpl(long j);

    private native boolean isDeletePendingImpl(long j);

    private native boolean isFileDownloadedImpl(long j);

    private native boolean isFileDownloadingImpl(long j);

    private native boolean isScreenShotImpl(long j);

    public ZoomFile(long j) {
        this.mNativeHandle = j;
    }

    public long getNativeHandle() {
        return this.mNativeHandle;
    }

    public int getFileType() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFileTypeImpl(j);
    }

    @Nullable
    public String getLocalPath() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getLocalPathImpl(j);
    }

    @Nullable
    public String getFileURL() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFileURLImpl(j);
    }

    @Nullable
    public String getFileName() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getFileNameImpl(j);
    }

    public boolean isFileDownloading() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isFileDownloadingImpl(j);
    }

    public boolean isFileDownloaded() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isFileDownloadedImpl(j);
    }

    @Nullable
    public String getSessionID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getSessionIDImpl(j);
    }

    public int getFileSize() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFileSizeImpl(j);
    }

    public int getFileTransferState() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFileTransferStateImpl(j);
    }

    public int getTransferredSize() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTransferredSizeImpl(j);
    }

    public long getTimeStamp() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getTimeStampImpl(j);
    }

    @Nullable
    public ZoomFileShareInfo getShareInfo() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long shareInfoImpl = getShareInfoImpl(j);
        if (shareInfoImpl == 0) {
            return null;
        }
        return new ZoomFileShareInfo(shareInfoImpl);
    }

    @Nullable
    public String getOwner() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getOwnerImpl(j);
    }

    @Nullable
    public String getWebFileID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getWebFileIDImpl(j);
    }

    @Nullable
    public String getMessageID() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getMessageIDImpl(j);
    }

    @Nullable
    public String getPicturePreviewPath() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return getPicturePreviewPathImpl(j);
    }

    public boolean isDeletePending() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isDeletePendingImpl(j);
    }

    @Nullable
    public FileIntegrationInfo getFileIntegrationShareInfo() {
        long j = this.mNativeHandle;
        FileIntegrationInfo fileIntegrationInfo = null;
        if (j == 0) {
            return null;
        }
        byte[] fileIntegrationShareInfoImpl = getFileIntegrationShareInfoImpl(j);
        if (fileIntegrationShareInfoImpl == null) {
            return null;
        }
        try {
            fileIntegrationInfo = FileIntegrationInfo.parseFrom(fileIntegrationShareInfoImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileIntegrationInfo;
    }

    public boolean isScreenShot() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isScreenShotImpl(j);
    }
}
