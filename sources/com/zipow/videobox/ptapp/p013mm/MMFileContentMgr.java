package com.zipow.videobox.ptapp.p013mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.protobuf.InvalidProtocolBufferException;
import com.zipow.videobox.ptapp.PTAppProtos.FileQueryResult;
import com.zipow.videobox.view.p014mm.MMZoomFile;
import com.zipow.videobox.view.p014mm.MMZoomShareAction;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.ptapp.mm.MMFileContentMgr */
public class MMFileContentMgr {
    private long mNativeHandle = 0;

    private native boolean cancelFileTransferImpl(long j, String str, String str2);

    @Nullable
    private native String deleteFileByWebFileIDImpl(long j, String str);

    private native void destroyFileObjectImpl(long j, long j2);

    @Nullable
    private native String downloadFileImpl(long j, String str, String str2);

    @Nullable
    private native String downloadImgPreviewImpl(long j, String str);

    @Nullable
    private native String forwardFileMessageImpl(long j, String str, String str2, String str3);

    private native int getFileContentMgmtOptionImpl(long j);

    private native long getFileWithMessageIDImpl(long j, String str, String str2);

    private native long getFileWithWebFileIDImpl(long j, String str);

    @Nullable
    private native byte[] queryAllFilesImpl(long j, long j2, int i);

    @Nullable
    private native byte[] queryAllImagesImpl(long j, long j2, int i);

    @Nullable
    private native byte[] queryFilesForSessionImpl(long j, String str, long j2, int i);

    @Nullable
    private native byte[] queryFilesSharedWithMeImpl(long j, String str, long j2, int i);

    @Nullable
    private native byte[] queryImagesForSessionImpl(long j, String str, long j2, int i);

    @Nullable
    private native byte[] queryImagesSharedWithMeImpl(long j, String str, long j2, int i);

    @Nullable
    private native byte[] queryOwnedFilesImpl(long j, String str, long j2, int i);

    @Nullable
    private native byte[] queryOwnedImageFilesImpl(long j, String str, long j2, int i);

    @NonNull
    private native String renameFileByWebFileIDImpl(long j, String str, String str2);

    @Nullable
    private native String shareFileImpl(long j, String str, String str2);

    @Nullable
    private native String syncFileInfoByFileIDImpl(long j, String str);

    @Nullable
    private native String unshareFileImpl(long j, String str, List<String> list);

    @Nullable
    private native String uploadFileImpl(long j, String str);

    public MMFileContentMgr(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public FileQueryResult queryFilesForSession(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryFilesForSessionImpl = queryFilesForSessionImpl(j2, str, j, i);
        if (queryFilesForSessionImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryFilesForSessionImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryImagesForSession(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryImagesForSessionImpl = queryImagesForSessionImpl(j2, str, j, i);
        if (queryImagesForSessionImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryImagesForSessionImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryOwnedFiles(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryOwnedFilesImpl = queryOwnedFilesImpl(j2, str, j, i);
        if (queryOwnedFilesImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryOwnedFilesImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryOwnedImageFiles(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryOwnedImageFilesImpl = queryOwnedImageFilesImpl(j2, str, j, i);
        if (queryOwnedImageFilesImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryOwnedImageFilesImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryFilesSharedWithMe(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryFilesSharedWithMeImpl = queryFilesSharedWithMeImpl(j2, str, j, i);
        if (queryFilesSharedWithMeImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryFilesSharedWithMeImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryImagesSharedWithMe(String str, long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryImagesSharedWithMeImpl = queryImagesSharedWithMeImpl(j2, str, j, i);
        if (queryImagesSharedWithMeImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryImagesSharedWithMeImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryAllFiles(long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryAllFilesImpl = queryAllFilesImpl(j2, j, i);
        if (queryAllFilesImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryAllFilesImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public FileQueryResult queryAllImages(long j, int i) {
        long j2 = this.mNativeHandle;
        FileQueryResult fileQueryResult = null;
        if (j2 == 0) {
            return null;
        }
        byte[] queryAllImagesImpl = queryAllImagesImpl(j2, j, i);
        if (queryAllImagesImpl == null) {
            return null;
        }
        try {
            fileQueryResult = FileQueryResult.parseFrom(queryAllImagesImpl);
        } catch (InvalidProtocolBufferException unused) {
        }
        return fileQueryResult;
    }

    @Nullable
    public ZoomFile getFileWithMessageID(String str, String str2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        long fileWithMessageIDImpl = getFileWithMessageIDImpl(this.mNativeHandle, str, str2);
        if (fileWithMessageIDImpl != 0) {
            return new ZoomFile(fileWithMessageIDImpl);
        }
        return null;
    }

    @Nullable
    public ZoomFile getFileWithWebFileID(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        long fileWithWebFileIDImpl = getFileWithWebFileIDImpl(this.mNativeHandle, str);
        if (fileWithWebFileIDImpl != 0) {
            return new ZoomFile(fileWithWebFileIDImpl);
        }
        return null;
    }

    public boolean cancelFileTransfer(String str, String str2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return false;
        }
        return cancelFileTransferImpl(this.mNativeHandle, str, str2);
    }

    @Nullable
    public String downloadImgPreview(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return downloadImgPreviewImpl(this.mNativeHandle, str);
    }

    @Nullable
    public String uploadFile(String str) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return uploadFileImpl(this.mNativeHandle, str);
    }

    @Nullable
    public String downloadFile(String str, String str2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str) || StringUtil.isEmptyOrNull(str2)) {
            return null;
        }
        return downloadFileImpl(this.mNativeHandle, str, str2);
    }

    @Nullable
    public String deleteFile(@Nullable MMZoomFile mMZoomFile, String str) {
        String str2;
        MMZoomShareAction mMZoomShareAction = null;
        if (mMZoomFile == null) {
            return null;
        }
        if (TextUtils.isEmpty(str)) {
            str2 = deleteFileByWebFileID(mMZoomFile.getWebID());
        } else {
            List<MMZoomShareAction> shareAction = mMZoomFile.getShareAction();
            if (shareAction == null) {
                return null;
            }
            for (MMZoomShareAction mMZoomShareAction2 : shareAction) {
                if (StringUtil.isSameString(mMZoomShareAction2.getSharee(), str)) {
                    mMZoomShareAction = mMZoomShareAction2;
                }
            }
            ArrayList arrayList = new ArrayList();
            if (mMZoomShareAction == null || TextUtils.isEmpty(mMZoomShareAction.getSharee())) {
                arrayList.add(str);
            } else {
                arrayList.add(mMZoomShareAction.getSharee());
            }
            str2 = unshareFile(mMZoomFile.getWebID(), arrayList);
        }
        return str2;
    }

    @Nullable
    public String deleteFileByWebFileID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return deleteFileByWebFileIDImpl(j, str);
    }

    @Nullable
    public String forwardFileMessage(String str, String str2, String str3) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return forwardFileMessageImpl(j, str, str2, str3);
    }

    @Nullable
    public String shareFile(String str, String str2) {
        if (this.mNativeHandle == 0 || StringUtil.isEmptyOrNull(str2) || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return shareFileImpl(this.mNativeHandle, str, str2);
    }

    @Nullable
    public String unshareFile(String str, @Nullable List<String> list) {
        if (this.mNativeHandle == 0 || list == null || list.size() == 0 || StringUtil.isEmptyOrNull(str)) {
            return null;
        }
        return unshareFileImpl(this.mNativeHandle, str, list);
    }

    @Nullable
    public String renameFileByWebFileID(String str, String str2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return renameFileByWebFileIDImpl(j, str, str2);
    }

    public void destroyFileObject(@Nullable ZoomFile zoomFile) {
        long j = this.mNativeHandle;
        if (j != 0 && zoomFile != null) {
            destroyFileObjectImpl(j, zoomFile.getNativeHandle());
        }
    }

    @Nullable
    public String syncFileInfoByFileID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return syncFileInfoByFileIDImpl(j, str);
    }

    public int getFileContentMgmtOption() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getFileContentMgmtOptionImpl(j);
    }
}
