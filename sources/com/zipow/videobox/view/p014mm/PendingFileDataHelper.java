package com.zipow.videobox.view.p014mm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.cmmlib.AppUtil;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack;
import com.zipow.videobox.ptapp.PrivateStickerUICallBack.SimpleZoomPrivateStickerUIListener;
import com.zipow.videobox.ptapp.ZoomMessengerUI;
import com.zipow.videobox.ptapp.ZoomMessengerUI.SimpleZoomMessengerUIListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.PendingFileDataHelper */
public class PendingFileDataHelper {
    public static final String CONTENT_FILE_LOCAL_PATH = "contentFile";
    private static final String TAG = ZoomMessengerUI.class.getSimpleName();
    private static PendingFileDataHelper instance;
    @NonNull
    private HashMap<String, PendingFileInfo> mDownloadPendingFileInfos = new HashMap<>();
    @NonNull
    private ArrayList<String> mUploadFailedFile = new ArrayList<>();
    @NonNull
    private HashMap<String, PendingFileInfo> mUploadPendingFileInfos = new HashMap<>();

    /* renamed from: com.zipow.videobox.view.mm.PendingFileDataHelper$PendingFileInfo */
    public static class PendingFileInfo {
        int bitPerSecond;
        int completeSize;
        boolean isSticker;
        String name;
        String path;
        int ratio;
        String reqId;
        long timestamp;
        int totalSize;

        public String getReqId() {
            return this.reqId;
        }

        public int getRatio() {
            return this.ratio;
        }
    }

    private PendingFileDataHelper() {
        ZoomMessengerUI.getInstance().addListener(new SimpleZoomMessengerUIListener() {
            public void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3) {
                PendingFileDataHelper.this.FT_UploadToMyList_OnProgress(str, i, i2, i3);
            }

            public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
                PendingFileDataHelper.this.FT_DownloadByFileID_OnProgress(str, str2, i, i2, i3);
            }

            public void Indicate_FileDownloaded(String str, String str2, int i) {
                PendingFileDataHelper.this.Indicate_FileDownloaded(str, str2, i);
            }

            public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
                PendingFileDataHelper.this.Indicate_UploadToMyFiles_Sent(str, str2, i);
            }

            public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
                PendingFileDataHelper.this.FT_OnDownloadByFileIDTimeOut(str, str2);
            }

            public void FT_UploadToMyList_TimeOut(String str) {
                PendingFileDataHelper.this.FT_UploadToMyList_TimeOut(str);
            }
        });
        PrivateStickerUICallBack.getInstance().addListener(new SimpleZoomPrivateStickerUIListener() {
            public void OnNewStickerUploaded(String str, int i, String str2) {
                PendingFileDataHelper.this.OnNewStickerUploaded(str, i, str2);
            }
        });
    }

    public static PendingFileDataHelper getInstance() {
        if (instance == null) {
            instance = new PendingFileDataHelper();
        }
        return instance;
    }

    /* access modifiers changed from: private */
    public void FT_UploadToMyList_OnProgress(String str, int i, int i2, int i3) {
        PendingFileInfo pendingFileInfo = (PendingFileInfo) this.mUploadPendingFileInfos.get(str);
        if (pendingFileInfo != null) {
            pendingFileInfo.bitPerSecond = i3;
            pendingFileInfo.ratio = i;
            pendingFileInfo.completeSize = i2;
        }
    }

    /* access modifiers changed from: private */
    public void FT_DownloadByFileID_OnProgress(String str, String str2, int i, int i2, int i3) {
        PendingFileInfo pendingFileInfo = (PendingFileInfo) this.mDownloadPendingFileInfos.get(str2);
        if (pendingFileInfo == null) {
            pendingFileInfo = new PendingFileInfo();
            this.mDownloadPendingFileInfos.put(str2, pendingFileInfo);
        }
        pendingFileInfo.reqId = str;
        pendingFileInfo.bitPerSecond = i3;
        pendingFileInfo.ratio = i;
        pendingFileInfo.completeSize = i2;
    }

    /* access modifiers changed from: private */
    public void Indicate_FileDownloaded(String str, String str2, int i) {
        this.mDownloadPendingFileInfos.remove(str);
    }

    @Nullable
    public PendingFileInfo getDownloadPendingInfoByWebFileId(String str) {
        return (PendingFileInfo) this.mDownloadPendingFileInfos.get(str);
    }

    /* access modifiers changed from: private */
    public void Indicate_UploadToMyFiles_Sent(String str, String str2, int i) {
        PendingFileInfo pendingFileInfo = (PendingFileInfo) this.mUploadPendingFileInfos.remove(str);
        if (i != 0 && pendingFileInfo != null) {
            addUploadFailedFile(pendingFileInfo.path);
        }
    }

    /* access modifiers changed from: private */
    public void FT_OnDownloadByFileIDTimeOut(String str, String str2) {
        this.mDownloadPendingFileInfos.remove(str);
    }

    /* access modifiers changed from: private */
    public void FT_UploadToMyList_TimeOut(String str) {
        PendingFileInfo pendingFileInfo = (PendingFileInfo) this.mUploadPendingFileInfos.remove(str);
        if (pendingFileInfo != null) {
            addUploadFailedFile(pendingFileInfo.path);
        }
    }

    /* access modifiers changed from: private */
    public void OnNewStickerUploaded(String str, int i, String str2) {
        this.mUploadPendingFileInfos.remove(str);
    }

    @NonNull
    public ArrayList<String> getUploadFailedFiles() {
        return this.mUploadFailedFile;
    }

    public void addUploadFailedFile(String str) {
        if (this.mUploadFailedFile.size() < 5) {
            this.mUploadFailedFile.add(str);
        }
    }

    public void clearUploadFailedFiles() {
        this.mUploadFailedFile.clear();
    }

    public void addUploadPendingFile(String str, String str2, int i, String str3, boolean z) {
        PendingFileInfo pendingFileInfo = new PendingFileInfo();
        pendingFileInfo.reqId = str;
        pendingFileInfo.name = str2;
        pendingFileInfo.path = str3;
        pendingFileInfo.timestamp = System.currentTimeMillis();
        pendingFileInfo.totalSize = i;
        pendingFileInfo.isSticker = z;
        this.mUploadPendingFileInfos.put(str, pendingFileInfo);
    }

    public void removeUploadPendingFile(String str) {
        this.mUploadPendingFileInfos.remove(str);
    }

    public void removeDownloadPendingFile(String str) {
        this.mDownloadPendingFileInfos.remove(str);
    }

    @NonNull
    public List<PendingFileInfo> getUploadPendingFileInfos() {
        ArrayList arrayList = new ArrayList();
        for (PendingFileInfo pendingFileInfo : this.mUploadPendingFileInfos.values()) {
            if (!pendingFileInfo.isSticker) {
                arrayList.add(pendingFileInfo);
            }
        }
        return arrayList;
    }

    @NonNull
    public List<PendingFileInfo> getUploadPendingStickerInfos() {
        ArrayList arrayList = new ArrayList();
        for (PendingFileInfo pendingFileInfo : this.mUploadPendingFileInfos.values()) {
            if (pendingFileInfo.isSticker) {
                arrayList.add(pendingFileInfo);
            }
        }
        return arrayList;
    }

    public int getUploadPendingFileSize() {
        return getUploadPendingFileInfos().size();
    }

    public boolean isFileUploadNow(String str) {
        return this.mUploadPendingFileInfos.containsKey(str);
    }

    public static String getContenFilePreviewPath(String str) {
        File file = new File(AppUtil.getDataPath(), CONTENT_FILE_LOCAL_PATH);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        File file2 = new File(file, "preview");
        if (!file2.exists() && !file2.mkdirs()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("preview-");
        sb.append(str);
        return new File(file2, sb.toString()).getAbsolutePath();
    }

    public static String getContenFilePath(String str, String str2) {
        String dataPath = AppUtil.getDataPath();
        File file = new File(dataPath, CONTENT_FILE_LOCAL_PATH);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(dataPath);
        sb.append(File.separator);
        sb.append(CONTENT_FILE_LOCAL_PATH);
        sb.append(File.separator);
        sb.append(str);
        sb.append("-");
        sb.append(str2);
        return sb.toString();
    }

    public static String getContentLocalImgDir() {
        String dataPath = AppUtil.getDataPath();
        File file = new File(dataPath, CONTENT_FILE_LOCAL_PATH);
        if (!file.exists() && !file.mkdirs()) {
            return null;
        }
        File file2 = new File(dataPath, "localImg");
        if (file2.exists() || file2.mkdirs()) {
            return file2.getAbsolutePath();
        }
        return null;
    }

    public static String getContenFileRandomPath() {
        String contentLocalImgDir = getContentLocalImgDir();
        if (StringUtil.isEmptyOrNull(contentLocalImgDir)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("url-");
        sb.append(UUID.randomUUID().toString());
        return new File(contentLocalImgDir, sb.toString()).getAbsolutePath();
    }
}
