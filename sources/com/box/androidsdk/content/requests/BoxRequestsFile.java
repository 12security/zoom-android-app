package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.models.BoxComment;
import com.box.androidsdk.content.models.BoxDownload;
import com.box.androidsdk.content.models.BoxFile;
import com.box.androidsdk.content.models.BoxFileVersion;
import com.box.androidsdk.content.models.BoxListComments;
import com.box.androidsdk.content.models.BoxListFileVersions;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxVoid;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class BoxRequestsFile {

    public static class AddCommentToFile extends BoxRequestCommentAdd<BoxComment, AddCommentToFile> {
        public /* bridge */ /* synthetic */ String getItemId() {
            return super.getItemId();
        }

        public /* bridge */ /* synthetic */ String getItemType() {
            return super.getItemType();
        }

        public /* bridge */ /* synthetic */ String getMessage() {
            return super.getMessage();
        }

        public AddCommentToFile(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxComment.class, str3, boxSession);
            setItemId(str);
            setItemType(BoxFile.TYPE);
            setMessage(str2);
        }
    }

    public static class AddFileToCollection extends BoxRequestCollectionUpdate<BoxFile, AddFileToCollection> {
        public AddFileToCollection(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFile.class, str, str3, boxSession);
            setCollectionId(str2);
        }

        public AddFileToCollection setCollectionId(String str) {
            return (AddFileToCollection) super.setCollectionId(str);
        }
    }

    public static class CopyFile extends BoxRequestItemCopy<BoxFile, CopyFile> {
        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getParentId() {
            return super.getParentId();
        }

        public CopyFile(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFile.class, str, str2, str3, boxSession);
        }
    }

    public static class DeleteFile extends BoxRequestItemDelete<DeleteFile> {
        public /* bridge */ /* synthetic */ String getId() {
            return super.getId();
        }

        public /* bridge */ /* synthetic */ String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }

        public DeleteFile(String str, String str2, BoxSession boxSession) {
            super(str, str2, boxSession);
        }
    }

    public static class DeleteFileFromCollection extends BoxRequestCollectionUpdate<BoxFile, DeleteFileFromCollection> {
        public DeleteFileFromCollection(String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, str, str2, boxSession);
            setCollectionId(null);
        }
    }

    public static class DeleteFileVersion extends BoxRequest<BoxVoid, DeleteFileVersion> {
        private final String mVersionId;

        public DeleteFileVersion(String str, String str2, BoxSession boxSession) {
            super(BoxVoid.class, str2, boxSession);
            this.mRequestMethod = Methods.DELETE;
            this.mVersionId = str;
        }

        public String getVersionId() {
            return this.mVersionId;
        }
    }

    public static class DeleteTrashedFile extends BoxRequestItemDelete<DeleteTrashedFile> {
        public /* bridge */ /* synthetic */ String getId() {
            return super.getId();
        }

        public /* bridge */ /* synthetic */ String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }

        public DeleteTrashedFile(String str, String str2, BoxSession boxSession) {
            super(str, str2, boxSession);
        }
    }

    public static class DownloadFile extends BoxRequestDownload<BoxDownload, DownloadFile> {
        public DownloadFile(OutputStream outputStream, String str, BoxSession boxSession) {
            super(BoxDownload.class, outputStream, str, boxSession);
        }

        public DownloadFile(File file, String str, BoxSession boxSession) {
            super(BoxDownload.class, file, str, boxSession);
        }
    }

    public static class DownloadThumbnail extends BoxRequestDownload<BoxDownload, DownloadThumbnail> {
        private static final String FIELD_MAX_HEIGHT = "max_height";
        private static final String FIELD_MAX_WIDTH = "max_width";
        private static final String FIELD_MIN_HEIGHT = "min_height";
        private static final String FIELD_MIN_WIDTH = "min_width";
        public static int SIZE_128 = 128;
        public static int SIZE_256 = 256;
        public static int SIZE_32 = 32;
        public static int SIZE_64 = 64;

        public DownloadThumbnail(OutputStream outputStream, String str, BoxSession boxSession) {
            super(BoxDownload.class, outputStream, str, boxSession);
        }

        public DownloadThumbnail(File file, String str, BoxSession boxSession) {
            super(BoxDownload.class, file, str, boxSession);
        }

        public DownloadThumbnail setMinWidth(int i) {
            this.mQueryMap.put(FIELD_MIN_WIDTH, Integer.toString(i));
            return this;
        }

        public DownloadThumbnail setMaxWidth(int i) {
            this.mQueryMap.put(FIELD_MAX_WIDTH, Integer.toString(i));
            return this;
        }

        public DownloadThumbnail setMinHeight(int i) {
            this.mQueryMap.put(FIELD_MIN_HEIGHT, Integer.toString(i));
            return this;
        }

        public DownloadThumbnail setMaxHeight(int i) {
            this.mQueryMap.put(FIELD_MAX_HEIGHT, Integer.toString(i));
            return this;
        }

        public DownloadThumbnail setMinSize(int i) {
            setMinWidth(i);
            setMinHeight(i);
            return this;
        }
    }

    public static class GetFileComments extends BoxRequestItem<BoxListComments, GetFileComments> {
        public GetFileComments(String str, String str2, BoxSession boxSession) {
            super(BoxListComments.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class GetFileInfo extends BoxRequestItem<BoxFile, GetFileInfo> {
        public GetFileInfo(String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public GetFileInfo setIfNoneMatchEtag(String str) {
            return (GetFileInfo) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }
    }

    public static class GetFileVersions extends BoxRequestItem<BoxListFileVersions, GetFileVersions> {
        public GetFileVersions(String str, String str2, BoxSession boxSession) {
            super(BoxListFileVersions.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
            setFields(BoxFileVersion.ALL_FIELDS);
        }
    }

    public static class GetTrashedFile extends BoxRequestItem<BoxFile, GetTrashedFile> {
        public GetTrashedFile(String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public GetTrashedFile setIfNoneMatchEtag(String str) {
            return (GetTrashedFile) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }
    }

    public static class PromoteFileVersion extends BoxRequestItem<BoxFileVersion, PromoteFileVersion> {
        public PromoteFileVersion(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFileVersion.class, str, str3, boxSession);
            this.mRequestMethod = Methods.POST;
            setVersionId(str2);
        }

        public PromoteFileVersion setVersionId(String str) {
            this.mBodyMap.put("type", "file_version");
            this.mBodyMap.put("id", str);
            return this;
        }
    }

    public static class RestoreTrashedFile extends BoxRequestItemRestoreTrashed<BoxFile, RestoreTrashedFile> {
        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getParentId() {
            return super.getParentId();
        }

        public RestoreTrashedFile(String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, str, str2, boxSession);
        }
    }

    public static class UpdateFile extends BoxRequestItemUpdate<BoxFile, UpdateFile> {
        public UpdateFile(String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, str, str2, boxSession);
        }

        public UpdatedSharedFile updateSharedLink() {
            return new UpdatedSharedFile(this);
        }
    }

    public static class UpdatedSharedFile extends BoxRequestUpdateSharedItem<BoxFile, UpdatedSharedFile> {
        public UpdatedSharedFile(String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, str, str2, boxSession);
        }

        protected UpdatedSharedFile(UpdateFile updateFile) {
            super(updateFile);
        }

        public UpdatedSharedFile setCanDownload(boolean z) {
            return (UpdatedSharedFile) super.setCanDownload(z);
        }

        public Boolean getCanDownload() {
            return super.getCanDownload();
        }
    }

    public static class UploadFile extends BoxRequestUpload<BoxFile, UploadFile> {
        String mDestinationFolderId;

        public UploadFile(InputStream inputStream, String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFile.class, inputStream, str3, boxSession);
            this.mRequestUrlString = str3;
            this.mRequestMethod = Methods.POST;
            this.mFileName = str;
            this.mStream = inputStream;
            this.mDestinationFolderId = str2;
        }

        public UploadFile(File file, String str, String str2, BoxSession boxSession) {
            super(BoxFile.class, null, str2, boxSession);
            this.mRequestUrlString = str2;
            this.mRequestMethod = Methods.POST;
            this.mDestinationFolderId = str;
            this.mFileName = file.getName();
            this.mFile = file;
            this.mUploadSize = file.length();
            this.mModifiedDate = new Date(file.lastModified());
        }

        /* access modifiers changed from: protected */
        public BoxRequestMultipart createMultipartRequest() throws IOException, BoxException {
            BoxRequestMultipart createMultipartRequest = super.createMultipartRequest();
            createMultipartRequest.putField("parent_id", this.mDestinationFolderId);
            return createMultipartRequest;
        }

        public String getFileName() {
            return this.mFileName;
        }

        public UploadFile setFileName(String str) {
            this.mFileName = str;
            return this;
        }

        public String getDestinationFolderId() {
            return this.mDestinationFolderId;
        }
    }

    public static class UploadNewVersion extends BoxRequestUpload<BoxFile, UploadNewVersion> {
        public UploadNewVersion(InputStream inputStream, String str, BoxSession boxSession) {
            super(BoxFile.class, inputStream, str, boxSession);
        }

        public UploadNewVersion setIfMatchEtag(String str) {
            return (UploadNewVersion) super.setIfMatchEtag(str);
        }

        public String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }
    }
}
