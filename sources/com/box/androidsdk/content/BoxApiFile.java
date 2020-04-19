package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsFile.AddCommentToFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.AddFileToCollection;
import com.box.androidsdk.content.requests.BoxRequestsFile.CopyFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.DeleteFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.DeleteFileFromCollection;
import com.box.androidsdk.content.requests.BoxRequestsFile.DeleteFileVersion;
import com.box.androidsdk.content.requests.BoxRequestsFile.DeleteTrashedFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.DownloadFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.DownloadThumbnail;
import com.box.androidsdk.content.requests.BoxRequestsFile.GetFileComments;
import com.box.androidsdk.content.requests.BoxRequestsFile.GetFileInfo;
import com.box.androidsdk.content.requests.BoxRequestsFile.GetFileVersions;
import com.box.androidsdk.content.requests.BoxRequestsFile.GetTrashedFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.PromoteFileVersion;
import com.box.androidsdk.content.requests.BoxRequestsFile.RestoreTrashedFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.UpdateFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.UpdatedSharedFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.UploadFile;
import com.box.androidsdk.content.requests.BoxRequestsFile.UploadNewVersion;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Locale;

public class BoxApiFile extends BoxApi {
    public BoxApiFile(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getFilesUrl() {
        return String.format(Locale.ENGLISH, "%s/files", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getFileInfoUrl(String str) {
        return String.format(Locale.ENGLISH, "%s/%s", new Object[]{getFilesUrl(), str});
    }

    /* access modifiers changed from: protected */
    public String getFileCopyUrl(String str) {
        Locale locale = Locale.ENGLISH;
        StringBuilder sb = new StringBuilder();
        sb.append(getFileInfoUrl(str));
        sb.append("/copy");
        return String.format(locale, sb.toString(), new Object[0]);
    }

    /* access modifiers changed from: protected */
    public String getFileUploadUrl() {
        return String.format(Locale.ENGLISH, "%s/files/content", new Object[]{getBaseUploadUri()});
    }

    /* access modifiers changed from: protected */
    public String getFileUploadNewVersionUrl(String str) {
        return String.format(Locale.ENGLISH, "%s/files/%s/content", new Object[]{getBaseUploadUri(), str});
    }

    /* access modifiers changed from: protected */
    public String getTrashedFileUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileInfoUrl(str));
        sb.append("/trash");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getFileCommentsUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileInfoUrl(str));
        sb.append(BoxApiComment.COMMENTS_ENDPOINT);
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getFileVersionsUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileInfoUrl(str));
        sb.append("/versions");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getPromoteFileVersionUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileVersionsUrl(str));
        sb.append("/current");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getDeleteFileVersionUrl(String str, String str2) {
        return String.format(Locale.ENGLISH, "%s/%s", new Object[]{getFileVersionsUrl(str), str2});
    }

    /* access modifiers changed from: protected */
    public String getFileDownloadUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileInfoUrl(str));
        sb.append("/content");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getThumbnailFileDownloadUrl(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(getFileInfoUrl(str));
        sb.append("/thumbnail.png");
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public String getCommentUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUri());
        sb.append(BoxApiComment.COMMENTS_ENDPOINT);
        return sb.toString();
    }

    public GetFileInfo getInfoRequest(String str) {
        return new GetFileInfo(str, getFileInfoUrl(str), this.mSession);
    }

    public UpdateFile getUpdateRequest(String str) {
        return new UpdateFile(str, getFileInfoUrl(str), this.mSession);
    }

    public CopyFile getCopyRequest(String str, String str2) {
        return new CopyFile(str, str2, getFileCopyUrl(str), this.mSession);
    }

    public UpdateFile getRenameRequest(String str, String str2) {
        UpdateFile updateFile = new UpdateFile(str, getFileInfoUrl(str), this.mSession);
        updateFile.setName(str2);
        return updateFile;
    }

    public UpdateFile getMoveRequest(String str, String str2) {
        UpdateFile updateFile = new UpdateFile(str, getFileInfoUrl(str), this.mSession);
        updateFile.setParentId(str2);
        return updateFile;
    }

    public DeleteFile getDeleteRequest(String str) {
        return new DeleteFile(str, getFileInfoUrl(str), this.mSession);
    }

    public UpdatedSharedFile getCreateSharedLinkRequest(String str) {
        return (UpdatedSharedFile) new UpdatedSharedFile(str, getFileInfoUrl(str), this.mSession).setAccess(null);
    }

    public UpdateFile getDisableSharedLinkRequest(String str) {
        return (UpdateFile) new UpdateFile(str, getFileInfoUrl(str), this.mSession).setSharedLink(null);
    }

    public AddCommentToFile getAddCommentRequest(String str, String str2) {
        return new AddCommentToFile(str, str2, getCommentUrl(), this.mSession);
    }

    public UploadFile getUploadRequest(InputStream inputStream, String str, String str2) {
        UploadFile uploadFile = new UploadFile(inputStream, str, str2, getFileUploadUrl(), this.mSession);
        return uploadFile;
    }

    public UploadFile getUploadRequest(File file, String str) {
        return new UploadFile(file, str, getFileUploadUrl(), this.mSession);
    }

    public UploadNewVersion getUploadNewVersionRequest(InputStream inputStream, String str) {
        return new UploadNewVersion(inputStream, getFileUploadNewVersionUrl(str), this.mSession);
    }

    public UploadNewVersion getUploadNewVersionRequest(File file, String str) {
        try {
            UploadNewVersion uploadNewVersionRequest = getUploadNewVersionRequest((InputStream) new FileInputStream(file), str);
            uploadNewVersionRequest.setUploadSize(file.length());
            uploadNewVersionRequest.setModifiedDate(new Date(file.lastModified()));
            return uploadNewVersionRequest;
        } catch (FileNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public DownloadFile getDownloadRequest(File file, String str) throws IOException {
        if (file.exists()) {
            return new DownloadFile(file, getFileDownloadUrl(str), this.mSession);
        }
        throw new FileNotFoundException();
    }

    public DownloadFile getDownloadRequest(OutputStream outputStream, String str) {
        return new DownloadFile(outputStream, getFileDownloadUrl(str), this.mSession);
    }

    public DownloadThumbnail getDownloadThumbnailRequest(File file, String str) throws IOException {
        if (file.exists()) {
            return new DownloadThumbnail(file, getThumbnailFileDownloadUrl(str), this.mSession);
        }
        throw new FileNotFoundException();
    }

    public DownloadThumbnail getDownloadThumbnailRequest(OutputStream outputStream, String str) {
        return new DownloadThumbnail(outputStream, getThumbnailFileDownloadUrl(str), this.mSession);
    }

    public GetTrashedFile getTrashedFileRequest(String str) {
        return new GetTrashedFile(str, getTrashedFileUrl(str), this.mSession);
    }

    public DeleteTrashedFile getDeleteTrashedFileRequest(String str) {
        return new DeleteTrashedFile(str, getTrashedFileUrl(str), this.mSession);
    }

    public RestoreTrashedFile getRestoreTrashedFileRequest(String str) {
        return new RestoreTrashedFile(str, getFileInfoUrl(str), this.mSession);
    }

    public GetFileComments getCommentsRequest(String str) {
        return new GetFileComments(str, getFileCommentsUrl(str), this.mSession);
    }

    public GetFileVersions getVersionsRequest(String str) {
        return new GetFileVersions(str, getFileVersionsUrl(str), this.mSession);
    }

    public PromoteFileVersion getPromoteVersionRequest(String str, String str2) {
        return new PromoteFileVersion(str, str2, getPromoteFileVersionUrl(str), this.mSession);
    }

    public DeleteFileVersion getDeleteVersionRequest(String str, String str2) {
        return new DeleteFileVersion(str2, getDeleteFileVersionUrl(str, str2), this.mSession);
    }

    public AddFileToCollection getAddToCollectionRequest(String str, String str2) {
        return new AddFileToCollection(str, str2, getFileInfoUrl(str), this.mSession);
    }

    public DeleteFileFromCollection getDeleteFromCollectionRequest(String str) {
        return new DeleteFileFromCollection(str, getFileInfoUrl(str), this.mSession);
    }
}
