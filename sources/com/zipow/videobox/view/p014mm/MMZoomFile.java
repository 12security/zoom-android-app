package com.zipow.videobox.view.p014mm;

import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.zipow.videobox.ptapp.IMProtos.FileIntegrationInfo;
import com.zipow.videobox.ptapp.PTApp;
import com.zipow.videobox.ptapp.p013mm.MMFileContentMgr;
import com.zipow.videobox.ptapp.p013mm.ZoomBuddy;
import com.zipow.videobox.ptapp.p013mm.ZoomChatSession;
import com.zipow.videobox.ptapp.p013mm.ZoomFile;
import com.zipow.videobox.ptapp.p013mm.ZoomFileShareInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessage.FileTransferInfo;
import com.zipow.videobox.ptapp.p013mm.ZoomMessenger;
import com.zipow.videobox.ptapp.p013mm.ZoomShareAction;
import com.zipow.videobox.util.ImageUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import p021us.zoom.androidlib.util.StringUtil;

/* renamed from: com.zipow.videobox.view.mm.MMZoomFile */
public class MMZoomFile implements Serializable {
    private static final long serialVersionUID = 1;
    private int bitPerSecond;
    private int completeSize;
    private String fileIntegrationDownloadUrl;
    private String fileIntegrationFileName;
    private long fileIntegrationFileSize;
    private String fileIntegrationId;
    private String fileIntegrationPreviewUrl;
    private String fileIntegrationThumbnailUrl;
    private int fileIntegrationType;
    @Nullable
    private String fileName;
    private int fileSize;
    private int fileTransferState;
    private int fileType;
    private String fileURL;
    private boolean isDeletePending;
    private boolean isDisabled;
    private boolean isFileDownloaded;
    private boolean isFileDownloading;
    private boolean isPending;
    @Nullable
    private String localPath;
    private List<FileMatchInfo> mMatchInfos;
    @NonNull
    private List<String> operatorAbleSessions = new ArrayList();
    @Nullable
    private String ownerJid;
    @Nullable
    private String ownerName;
    @Nullable
    private String picturePreviewPath;
    private int ratio;
    private String reqId;
    private String sessionID;
    private List<MMZoomShareAction> shareAction;
    private long shareTimeStamp;
    private boolean showAllShareActions;
    private long timeStamp;
    private int transferredSize;
    private boolean uploadFailed;
    private String webID;

    /* renamed from: com.zipow.videobox.view.mm.MMZoomFile$FileMatchInfo */
    public static class FileMatchInfo {
        String mContent;
        @NonNull
        List<HighlightPosition> mHighlightPositions = new ArrayList();
        int mType;
    }

    /* renamed from: com.zipow.videobox.view.mm.MMZoomFile$HighlightPosition */
    public static class HighlightPosition {
        int end;
        int start;
    }

    public static MMZoomFile initWithMessage(String str, String str2) {
        ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
        if (zoomMessenger == null) {
            return null;
        }
        ZoomChatSession sessionById = zoomMessenger.getSessionById(str);
        if (sessionById == null) {
            return null;
        }
        ZoomMessage messageById = sessionById.getMessageById(str2);
        if (messageById == null) {
            return null;
        }
        FileInfo fileInfo = messageById.getFileInfo();
        if (fileInfo == null) {
            return null;
        }
        FileTransferInfo fileTransferInfo = messageById.getFileTransferInfo();
        if (fileTransferInfo == null) {
            return null;
        }
        MMZoomFile mMZoomFile = new MMZoomFile();
        mMZoomFile.fileName = fileInfo.name;
        if (TextUtils.isEmpty(mMZoomFile.fileName)) {
            mMZoomFile.fileName = messageById.getMessageID();
        }
        mMZoomFile.bitPerSecond = (int) fileTransferInfo.bitsPerSecond;
        mMZoomFile.completeSize = (int) fileTransferInfo.transferredSize;
        mMZoomFile.fileSize = (int) fileInfo.size;
        boolean z = true;
        switch (messageById.getMessageType()) {
            case 1:
                mMZoomFile.fileType = 1;
                break;
            case 2:
                mMZoomFile.fileType = 2;
                break;
            case 5:
                mMZoomFile.fileType = 4;
                break;
            case 6:
                mMZoomFile.fileType = 5;
                break;
            case 13:
                mMZoomFile.fileType = 6;
                break;
            case 15:
                mMZoomFile.fileType = 7;
                FileIntegrationInfo fileIntegrationShareInfo = messageById.getFileIntegrationShareInfo();
                if (fileIntegrationShareInfo != null) {
                    mMZoomFile.fileIntegrationId = fileIntegrationShareInfo.getId();
                    mMZoomFile.fileIntegrationType = fileIntegrationShareInfo.getType();
                    mMZoomFile.fileIntegrationFileName = fileIntegrationShareInfo.getFileName();
                    mMZoomFile.fileIntegrationFileSize = fileIntegrationShareInfo.getFileSize();
                    mMZoomFile.fileIntegrationPreviewUrl = fileIntegrationShareInfo.getPreviewUrl();
                    mMZoomFile.fileIntegrationDownloadUrl = fileIntegrationShareInfo.getDownloadUrl();
                    mMZoomFile.fileIntegrationThumbnailUrl = fileIntegrationShareInfo.getThumbnailUrl();
                    break;
                }
                break;
            default:
                mMZoomFile.fileType = 100;
                break;
        }
        mMZoomFile.fileTransferState = fileTransferInfo.state;
        mMZoomFile.localPath = messageById.getLocalFilePath();
        mMZoomFile.picturePreviewPath = messageById.getPicturePreviewPath();
        mMZoomFile.ownerJid = messageById.getSenderID();
        mMZoomFile.ownerName = messageById.getSenderName();
        mMZoomFile.timeStamp = messageById.getStamp();
        if (fileTransferInfo.state == 16) {
            mMZoomFile.isFileDownloaded = ImageUtil.isValidImageFile(mMZoomFile.getLocalPath());
        } else {
            mMZoomFile.isFileDownloaded = fileTransferInfo.state == 13;
        }
        if (fileTransferInfo.state != 10) {
            z = false;
        }
        mMZoomFile.isFileDownloading = z;
        return mMZoomFile;
    }

    @NonNull
    public static MMZoomFile initWithZoomFile(@NonNull ZoomFile zoomFile, @NonNull MMFileContentMgr mMFileContentMgr) {
        MMZoomFile mMZoomFile = new MMZoomFile(zoomFile);
        mMFileContentMgr.destroyFileObject(zoomFile);
        return mMZoomFile;
    }

    public MMZoomFile() {
    }

    protected MMZoomFile(@Nullable ZoomFile zoomFile) {
        if (zoomFile != null) {
            setFileDownloaded(zoomFile.isFileDownloaded());
            setFileDownloading(zoomFile.isFileDownloading());
            setFileSize(zoomFile.getFileSize());
            setFileTransferState(zoomFile.getFileTransferState());
            setFileType(zoomFile.getFileType());
            setFileName(zoomFile.getFileName());
            setFileURL(zoomFile.getFileURL());
            setLocalPath(zoomFile.getLocalPath());
            setSessionID(zoomFile.getSessionID());
            setTransferredSize(zoomFile.getTransferredSize());
            setTimeStamp(zoomFile.getTimeStamp());
            setOwnerJid(zoomFile.getOwner());
            setWebID(zoomFile.getWebFileID());
            setDeletePending(zoomFile.isDeletePending());
            setPicturePreviewPath(zoomFile.getPicturePreviewPath());
            ZoomFileShareInfo shareInfo = zoomFile.getShareInfo();
            if (shareInfo != null) {
                long shareActionCount = shareInfo.getShareActionCount();
                ArrayList arrayList = new ArrayList();
                for (long j = 0; j < shareActionCount; j++) {
                    ZoomShareAction shareAction2 = shareInfo.getShareAction(j);
                    if (shareAction2 != null) {
                        arrayList.add(MMZoomShareAction.createWithZoomShareAction(shareAction2));
                    }
                }
                setShareAction(arrayList);
            }
            if (!StringUtil.isEmptyOrNull(this.ownerJid)) {
                ZoomMessenger zoomMessenger = PTApp.getInstance().getZoomMessenger();
                if (zoomMessenger != null) {
                    ZoomBuddy buddyWithJID = zoomMessenger.getBuddyWithJID(this.ownerJid);
                    if (buddyWithJID != null) {
                        setOwnerName(buddyWithJID.getScreenName());
                    }
                } else {
                    return;
                }
            }
            FileIntegrationInfo fileIntegrationShareInfo = zoomFile.getFileIntegrationShareInfo();
            if (fileIntegrationShareInfo != null) {
                setFileIntegrationId(fileIntegrationShareInfo.getId());
                setFileIntegrationType(fileIntegrationShareInfo.getType());
                setFileIntegrationFileName(fileIntegrationShareInfo.getFileName());
                setFileIntegrationFileSize(fileIntegrationShareInfo.getFileSize());
                setFileIntegrationPreviewUrl(fileIntegrationShareInfo.getPreviewUrl());
                setFileIntegrationDownloadUrl(fileIntegrationShareInfo.getDownloadUrl());
                setFileIntegrationThumbnailUrl(fileIntegrationShareInfo.getThumbnailUrl());
            }
        }
    }

    public int getRatio() {
        return this.ratio;
    }

    public void setRatio(int i) {
        this.ratio = i;
    }

    public int getCompleteSize() {
        return this.completeSize;
    }

    public void setCompleteSize(int i) {
        this.completeSize = i;
    }

    public int getBitPerSecond() {
        return this.bitPerSecond;
    }

    public void setBitPerSecond(int i) {
        this.bitPerSecond = i;
    }

    @Nullable
    public String getLocalPath() {
        return this.localPath;
    }

    public void setLocalPath(@Nullable String str) {
        this.localPath = str;
    }

    public String getFileURL() {
        return this.fileURL;
    }

    public void setFileURL(String str) {
        this.fileURL = str;
    }

    @Nullable
    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(@Nullable String str) {
        this.fileName = str;
    }

    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String str) {
        this.sessionID = str;
    }

    public int getTransferredSize() {
        return this.transferredSize;
    }

    public void setTransferredSize(int i) {
        this.transferredSize = i;
    }

    public int getFileType() {
        return this.fileType;
    }

    public void setFileType(int i) {
        this.fileType = i;
    }

    public boolean isFileDownloading() {
        return this.isFileDownloading;
    }

    public void setFileDownloading(boolean z) {
        this.isFileDownloading = z;
    }

    public boolean isFileDownloaded() {
        return this.isFileDownloaded;
    }

    public void setFileDownloaded(boolean z) {
        this.isFileDownloaded = z;
    }

    public int getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(int i) {
        this.fileSize = i;
    }

    public int getFileTransferState() {
        return this.fileTransferState;
    }

    public void setFileTransferState(int i) {
        this.fileTransferState = i;
    }

    public long getTimeStamp() {
        return this.timeStamp;
    }

    public void setTimeStamp(long j) {
        this.timeStamp = j;
    }

    public List<MMZoomShareAction> getShareAction() {
        return this.shareAction;
    }

    public void setShareAction(List<MMZoomShareAction> list) {
        this.shareAction = list;
    }

    @Nullable
    public String getOwnerJid() {
        return this.ownerJid;
    }

    public long getLastedShareTime(String str) {
        long j;
        List<MMZoomShareAction> list = this.shareAction;
        if (list == null || list.size() <= 0) {
            j = 0;
        } else {
            j = 0;
            for (MMZoomShareAction mMZoomShareAction : this.shareAction) {
                if (StringUtil.isEmptyOrNull(str) || StringUtil.isSameString(str, mMZoomShareAction.getSharee())) {
                    long shareTime = mMZoomShareAction.getShareTime();
                    if (shareTime > j) {
                        j = shareTime;
                    }
                }
            }
        }
        return j <= 0 ? getTimeStamp() : j;
    }

    public long getLastedShareTime() {
        return getLastedShareTime(null);
    }

    public void setOwnerJid(@Nullable String str) {
        this.ownerJid = str;
    }

    @Nullable
    public String getOwnerName() {
        return this.ownerName;
    }

    public void setOwnerName(@Nullable String str) {
        this.ownerName = str;
    }

    public String getWebID() {
        return this.webID;
    }

    public void setWebID(String str) {
        this.webID = str;
    }

    @Nullable
    public String getPicturePreviewPath() {
        return this.picturePreviewPath;
    }

    public void setPicturePreviewPath(@Nullable String str) {
        this.picturePreviewPath = str;
    }

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean z) {
        this.isPending = z;
    }

    public boolean isUploadFailed() {
        return this.uploadFailed;
    }

    public void setUploadFailed(boolean z) {
        this.uploadFailed = z;
    }

    public String getReqId() {
        return this.reqId;
    }

    public void setReqId(String str) {
        this.reqId = str;
    }

    public boolean isDeletePending() {
        return this.isDeletePending;
    }

    public void setDeletePending(boolean z) {
        this.isDeletePending = z;
    }

    public boolean isShowAllShareActions() {
        return this.showAllShareActions;
    }

    public void setShowAllShareActions(boolean z) {
        this.showAllShareActions = z;
    }

    public List<FileMatchInfo> getMatchInfos() {
        return this.mMatchInfos;
    }

    public void setMatchInfos(List<FileMatchInfo> list) {
        this.mMatchInfos = list;
    }

    @NonNull
    public List<String> getOperatorAbleSessions() {
        return this.operatorAbleSessions;
    }

    public void addOperatorAbleSession(String str) {
        this.operatorAbleSessions.add(str);
    }

    public boolean isDisabled() {
        return this.isDisabled;
    }

    public void setDisabled(boolean z) {
        this.isDisabled = z;
    }

    public String getFileIntegrationId() {
        return this.fileIntegrationId;
    }

    public void setFileIntegrationId(String str) {
        this.fileIntegrationId = str;
    }

    public int getFileIntegrationType() {
        return this.fileIntegrationType;
    }

    public void setFileIntegrationType(int i) {
        this.fileIntegrationType = i;
    }

    public long getFileIntegrationFileSize() {
        return this.fileIntegrationFileSize;
    }

    public void setFileIntegrationFileSize(long j) {
        this.fileIntegrationFileSize = j;
    }

    public String getFileIntegrationFileName() {
        return this.fileIntegrationFileName;
    }

    public void setFileIntegrationFileName(String str) {
        this.fileIntegrationFileName = str;
    }

    public String getFileIntegrationPreviewUrl() {
        return this.fileIntegrationPreviewUrl;
    }

    public void setFileIntegrationPreviewUrl(String str) {
        this.fileIntegrationPreviewUrl = str;
    }

    public String getFileIntegrationThumbnailUrl() {
        return this.fileIntegrationThumbnailUrl;
    }

    public void setFileIntegrationThumbnailUrl(String str) {
        this.fileIntegrationThumbnailUrl = str;
    }

    public String getFileIntegrationDownloadUrl() {
        return this.fileIntegrationDownloadUrl;
    }

    public void setFileIntegrationDownloadUrl(String str) {
        this.fileIntegrationDownloadUrl = str;
    }

    public String getFileIntegrationUrl() {
        if (!StringUtil.isEmptyOrNull(this.fileIntegrationPreviewUrl)) {
            return this.fileIntegrationPreviewUrl;
        }
        if (!StringUtil.isEmptyOrNull(this.fileIntegrationDownloadUrl)) {
            return this.fileIntegrationDownloadUrl;
        }
        return !StringUtil.isEmptyOrNull(this.fileIntegrationThumbnailUrl) ? this.fileIntegrationThumbnailUrl : "";
    }

    public boolean isIntegrationType() {
        return this.fileType == 7;
    }
}
