package com.dropbox.core.p005v2.files;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxWrappedException;
import com.dropbox.core.LocalizedText;
import com.dropbox.core.http.HttpRequestor.Header;
import com.dropbox.core.p005v2.DbxRawClientV2;
import com.dropbox.core.p005v2.async.PollArg;
import com.dropbox.core.p005v2.async.PollArg.Serializer;
import com.dropbox.core.p005v2.async.PollError;
import com.dropbox.core.p005v2.async.PollErrorException;
import com.dropbox.core.p005v2.fileproperties.AddPropertiesArg;
import com.dropbox.core.p005v2.fileproperties.AddPropertiesError;
import com.dropbox.core.p005v2.fileproperties.AddPropertiesErrorException;
import com.dropbox.core.p005v2.fileproperties.GetTemplateArg;
import com.dropbox.core.p005v2.fileproperties.GetTemplateResult;
import com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupError;
import com.dropbox.core.p005v2.fileproperties.InvalidPropertyGroupErrorException;
import com.dropbox.core.p005v2.fileproperties.ListTemplateResult;
import com.dropbox.core.p005v2.fileproperties.OverwritePropertyGroupArg;
import com.dropbox.core.p005v2.fileproperties.PropertyGroup;
import com.dropbox.core.p005v2.fileproperties.PropertyGroupUpdate;
import com.dropbox.core.p005v2.fileproperties.RemovePropertiesArg;
import com.dropbox.core.p005v2.fileproperties.RemovePropertiesError;
import com.dropbox.core.p005v2.fileproperties.RemovePropertiesErrorException;
import com.dropbox.core.p005v2.fileproperties.TemplateError;
import com.dropbox.core.p005v2.fileproperties.TemplateErrorException;
import com.dropbox.core.p005v2.fileproperties.UpdatePropertiesArg;
import com.dropbox.core.p005v2.fileproperties.UpdatePropertiesError;
import com.dropbox.core.p005v2.fileproperties.UpdatePropertiesErrorException;
import com.dropbox.core.stone.StoneSerializers;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

/* renamed from: com.dropbox.core.v2.files.DbxUserFilesRequests */
public class DbxUserFilesRequests {
    private final DbxRawClientV2 client;

    public DbxUserFilesRequests(DbxRawClientV2 dbxRawClientV2) {
        this.client = dbxRawClientV2;
    }

    /* access modifiers changed from: 0000 */
    public Metadata alphaGetMetadata(AlphaGetMetadataArg alphaGetMetadataArg) throws AlphaGetMetadataErrorException, DbxException {
        try {
            return (Metadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/alpha/get_metadata", alphaGetMetadataArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new AlphaGetMetadataErrorException("2/files/alpha/get_metadata", e.getRequestId(), e.getUserMessage(), (AlphaGetMetadataError) e.getErrorValue());
        }
    }

    @Deprecated
    public Metadata alphaGetMetadata(String str) throws AlphaGetMetadataErrorException, DbxException {
        return alphaGetMetadata(new AlphaGetMetadataArg(str));
    }

    @Deprecated
    public AlphaGetMetadataBuilder alphaGetMetadataBuilder(String str) {
        return new AlphaGetMetadataBuilder(this, AlphaGetMetadataArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public AlphaUploadUploader alphaUpload(CommitInfoWithProperties commitInfoWithProperties) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new AlphaUploadUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getContent(), "2/files/alpha/upload", commitInfoWithProperties, false, Serializer.INSTANCE), this.client.getUserId());
    }

    @Deprecated
    public AlphaUploadUploader alphaUpload(String str) throws DbxException {
        return alphaUpload(new CommitInfoWithProperties(str));
    }

    @Deprecated
    public AlphaUploadBuilder alphaUploadBuilder(String str) {
        return new AlphaUploadBuilder(this, CommitInfoWithProperties.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public RelocationResult copyV2(RelocationArg relocationArg) throws RelocationErrorException, DbxException {
        try {
            return (RelocationResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/copy_v2", relocationArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RelocationErrorException("2/files/copy_v2", e.getRequestId(), e.getUserMessage(), (RelocationError) e.getErrorValue());
        }
    }

    public RelocationResult copyV2(String str, String str2) throws RelocationErrorException, DbxException {
        return copyV2(new RelocationArg(str, str2));
    }

    public CopyV2Builder copyV2Builder(String str, String str2) {
        return new CopyV2Builder(this, RelocationArg.newBuilder(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public Metadata copy(RelocationArg relocationArg) throws RelocationErrorException, DbxException {
        try {
            return (Metadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/copy", relocationArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RelocationErrorException("2/files/copy", e.getRequestId(), e.getUserMessage(), (RelocationError) e.getErrorValue());
        }
    }

    @Deprecated
    public Metadata copy(String str, String str2) throws RelocationErrorException, DbxException {
        return copy(new RelocationArg(str, str2));
    }

    @Deprecated
    public CopyBuilder copyBuilder(String str, String str2) {
        return new CopyBuilder(this, RelocationArg.newBuilder(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public RelocationBatchLaunch copyBatch(RelocationBatchArg relocationBatchArg) throws DbxApiException, DbxException {
        try {
            return (RelocationBatchLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/copy_batch", relocationBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"copy_batch\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public RelocationBatchLaunch copyBatch(List<RelocationPath> list) throws DbxApiException, DbxException {
        return copyBatch(new RelocationBatchArg(list));
    }

    public CopyBatchBuilder copyBatchBuilder(List<RelocationPath> list) {
        return new CopyBatchBuilder(this, RelocationBatchArg.newBuilder(list));
    }

    /* access modifiers changed from: 0000 */
    public RelocationBatchJobStatus copyBatchCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (RelocationBatchJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/copy_batch/check", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/files/copy_batch/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public RelocationBatchJobStatus copyBatchCheck(String str) throws PollErrorException, DbxException {
        return copyBatchCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public GetCopyReferenceResult copyReferenceGet(GetCopyReferenceArg getCopyReferenceArg) throws GetCopyReferenceErrorException, DbxException {
        try {
            return (GetCopyReferenceResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/copy_reference/get", getCopyReferenceArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetCopyReferenceErrorException("2/files/copy_reference/get", e.getRequestId(), e.getUserMessage(), (GetCopyReferenceError) e.getErrorValue());
        }
    }

    public GetCopyReferenceResult copyReferenceGet(String str) throws GetCopyReferenceErrorException, DbxException {
        return copyReferenceGet(new GetCopyReferenceArg(str));
    }

    /* access modifiers changed from: 0000 */
    public SaveCopyReferenceResult copyReferenceSave(SaveCopyReferenceArg saveCopyReferenceArg) throws SaveCopyReferenceErrorException, DbxException {
        try {
            return (SaveCopyReferenceResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/copy_reference/save", saveCopyReferenceArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SaveCopyReferenceErrorException("2/files/copy_reference/save", e.getRequestId(), e.getUserMessage(), (SaveCopyReferenceError) e.getErrorValue());
        }
    }

    public SaveCopyReferenceResult copyReferenceSave(String str, String str2) throws SaveCopyReferenceErrorException, DbxException {
        return copyReferenceSave(new SaveCopyReferenceArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public CreateFolderResult createFolderV2(CreateFolderArg createFolderArg) throws CreateFolderErrorException, DbxException {
        try {
            return (CreateFolderResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/create_folder_v2", createFolderArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CreateFolderErrorException("2/files/create_folder_v2", e.getRequestId(), e.getUserMessage(), (CreateFolderError) e.getErrorValue());
        }
    }

    public CreateFolderResult createFolderV2(String str) throws CreateFolderErrorException, DbxException {
        return createFolderV2(new CreateFolderArg(str));
    }

    public CreateFolderResult createFolderV2(String str, boolean z) throws CreateFolderErrorException, DbxException {
        return createFolderV2(new CreateFolderArg(str, z));
    }

    /* access modifiers changed from: 0000 */
    public FolderMetadata createFolder(CreateFolderArg createFolderArg) throws CreateFolderErrorException, DbxException {
        try {
            return (FolderMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/create_folder", createFolderArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new CreateFolderErrorException("2/files/create_folder", e.getRequestId(), e.getUserMessage(), (CreateFolderError) e.getErrorValue());
        }
    }

    @Deprecated
    public FolderMetadata createFolder(String str) throws CreateFolderErrorException, DbxException {
        return createFolder(new CreateFolderArg(str));
    }

    @Deprecated
    public FolderMetadata createFolder(String str, boolean z) throws CreateFolderErrorException, DbxException {
        return createFolder(new CreateFolderArg(str, z));
    }

    /* access modifiers changed from: 0000 */
    public CreateFolderBatchLaunch createFolderBatch(CreateFolderBatchArg createFolderBatchArg) throws DbxApiException, DbxException {
        try {
            return (CreateFolderBatchLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/create_folder_batch", createFolderBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"create_folder_batch\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public CreateFolderBatchLaunch createFolderBatch(List<String> list) throws DbxApiException, DbxException {
        return createFolderBatch(new CreateFolderBatchArg(list));
    }

    public CreateFolderBatchBuilder createFolderBatchBuilder(List<String> list) {
        return new CreateFolderBatchBuilder(this, CreateFolderBatchArg.newBuilder(list));
    }

    /* access modifiers changed from: 0000 */
    public CreateFolderBatchJobStatus createFolderBatchCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (CreateFolderBatchJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/create_folder_batch/check", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/files/create_folder_batch/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public CreateFolderBatchJobStatus createFolderBatchCheck(String str) throws PollErrorException, DbxException {
        return createFolderBatchCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public DeleteResult deleteV2(DeleteArg deleteArg) throws DeleteErrorException, DbxException {
        try {
            return (DeleteResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/delete_v2", deleteArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DeleteErrorException("2/files/delete_v2", e.getRequestId(), e.getUserMessage(), (DeleteError) e.getErrorValue());
        }
    }

    public DeleteResult deleteV2(String str) throws DeleteErrorException, DbxException {
        return deleteV2(new DeleteArg(str));
    }

    public DeleteResult deleteV2(String str, String str2) throws DeleteErrorException, DbxException {
        if (str2 != null) {
            if (str2.length() < 9) {
                throw new IllegalArgumentException("String 'parentRev' is shorter than 9");
            } else if (!Pattern.matches("[0-9a-f]+", str2)) {
                throw new IllegalArgumentException("String 'parentRev' does not match pattern");
            }
        }
        return deleteV2(new DeleteArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public Metadata delete(DeleteArg deleteArg) throws DeleteErrorException, DbxException {
        try {
            return (Metadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/delete", deleteArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DeleteErrorException("2/files/delete", e.getRequestId(), e.getUserMessage(), (DeleteError) e.getErrorValue());
        }
    }

    @Deprecated
    public Metadata delete(String str) throws DeleteErrorException, DbxException {
        return delete(new DeleteArg(str));
    }

    @Deprecated
    public Metadata delete(String str, String str2) throws DeleteErrorException, DbxException {
        if (str2 != null) {
            if (str2.length() < 9) {
                throw new IllegalArgumentException("String 'parentRev' is shorter than 9");
            } else if (!Pattern.matches("[0-9a-f]+", str2)) {
                throw new IllegalArgumentException("String 'parentRev' does not match pattern");
            }
        }
        return delete(new DeleteArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public DeleteBatchLaunch deleteBatch(DeleteBatchArg deleteBatchArg) throws DbxApiException, DbxException {
        try {
            return (DeleteBatchLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/delete_batch", deleteBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"delete_batch\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public DeleteBatchLaunch deleteBatch(List<DeleteArg> list) throws DbxApiException, DbxException {
        return deleteBatch(new DeleteBatchArg(list));
    }

    /* access modifiers changed from: 0000 */
    public DeleteBatchJobStatus deleteBatchCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (DeleteBatchJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/delete_batch/check", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/files/delete_batch/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public DeleteBatchJobStatus deleteBatchCheck(String str) throws PollErrorException, DbxException {
        return deleteBatchCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public DbxDownloader<FileMetadata> download(DownloadArg downloadArg, List<Header> list) throws DownloadErrorException, DbxException {
        try {
            return this.client.downloadStyle(this.client.getHost().getContent(), "2/files/download", downloadArg, false, list, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DownloadErrorException("2/files/download", e.getRequestId(), e.getUserMessage(), (DownloadError) e.getErrorValue());
        }
    }

    public DbxDownloader<FileMetadata> download(String str) throws DownloadErrorException, DbxException {
        return download(new DownloadArg(str), Collections.emptyList());
    }

    public DbxDownloader<FileMetadata> download(String str, String str2) throws DownloadErrorException, DbxException {
        if (str2 != null) {
            if (str2.length() < 9) {
                throw new IllegalArgumentException("String 'rev' is shorter than 9");
            } else if (!Pattern.matches("[0-9a-f]+", str2)) {
                throw new IllegalArgumentException("String 'rev' does not match pattern");
            }
        }
        return download(new DownloadArg(str, str2), Collections.emptyList());
    }

    public DownloadBuilder downloadBuilder(String str) {
        return new DownloadBuilder(this, str);
    }

    /* access modifiers changed from: 0000 */
    public DbxDownloader<DownloadZipResult> downloadZip(DownloadZipArg downloadZipArg, List<Header> list) throws DownloadZipErrorException, DbxException {
        try {
            return this.client.downloadStyle(this.client.getHost().getContent(), "2/files/download_zip", downloadZipArg, false, list, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DownloadZipErrorException("2/files/download_zip", e.getRequestId(), e.getUserMessage(), (DownloadZipError) e.getErrorValue());
        }
    }

    public DbxDownloader<DownloadZipResult> downloadZip(String str) throws DownloadZipErrorException, DbxException {
        return downloadZip(new DownloadZipArg(str), Collections.emptyList());
    }

    public DownloadZipBuilder downloadZipBuilder(String str) {
        return new DownloadZipBuilder(this, str);
    }

    /* access modifiers changed from: 0000 */
    public Metadata getMetadata(GetMetadataArg getMetadataArg) throws GetMetadataErrorException, DbxException {
        try {
            return (Metadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/get_metadata", getMetadataArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetMetadataErrorException("2/files/get_metadata", e.getRequestId(), e.getUserMessage(), (GetMetadataError) e.getErrorValue());
        }
    }

    public Metadata getMetadata(String str) throws GetMetadataErrorException, DbxException {
        return getMetadata(new GetMetadataArg(str));
    }

    public GetMetadataBuilder getMetadataBuilder(String str) {
        return new GetMetadataBuilder(this, GetMetadataArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public DbxDownloader<FileMetadata> getPreview(PreviewArg previewArg, List<Header> list) throws PreviewErrorException, DbxException {
        try {
            return this.client.downloadStyle(this.client.getHost().getContent(), "2/files/get_preview", previewArg, false, list, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PreviewErrorException("2/files/get_preview", e.getRequestId(), e.getUserMessage(), (PreviewError) e.getErrorValue());
        }
    }

    public DbxDownloader<FileMetadata> getPreview(String str) throws PreviewErrorException, DbxException {
        return getPreview(new PreviewArg(str), Collections.emptyList());
    }

    public DbxDownloader<FileMetadata> getPreview(String str, String str2) throws PreviewErrorException, DbxException {
        if (str2 != null) {
            if (str2.length() < 9) {
                throw new IllegalArgumentException("String 'rev' is shorter than 9");
            } else if (!Pattern.matches("[0-9a-f]+", str2)) {
                throw new IllegalArgumentException("String 'rev' does not match pattern");
            }
        }
        return getPreview(new PreviewArg(str, str2), Collections.emptyList());
    }

    public GetPreviewBuilder getPreviewBuilder(String str) {
        return new GetPreviewBuilder(this, str);
    }

    /* access modifiers changed from: 0000 */
    public GetTemporaryLinkResult getTemporaryLink(GetTemporaryLinkArg getTemporaryLinkArg) throws GetTemporaryLinkErrorException, DbxException {
        try {
            return (GetTemporaryLinkResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/get_temporary_link", getTemporaryLinkArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetTemporaryLinkErrorException("2/files/get_temporary_link", e.getRequestId(), e.getUserMessage(), (GetTemporaryLinkError) e.getErrorValue());
        }
    }

    public GetTemporaryLinkResult getTemporaryLink(String str) throws GetTemporaryLinkErrorException, DbxException {
        return getTemporaryLink(new GetTemporaryLinkArg(str));
    }

    /* access modifiers changed from: 0000 */
    public GetTemporaryUploadLinkResult getTemporaryUploadLink(GetTemporaryUploadLinkArg getTemporaryUploadLinkArg) throws DbxApiException, DbxException {
        try {
            return (GetTemporaryUploadLinkResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/get_temporary_upload_link", getTemporaryUploadLinkArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"get_temporary_upload_link\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public GetTemporaryUploadLinkResult getTemporaryUploadLink(CommitInfo commitInfo) throws DbxApiException, DbxException {
        return getTemporaryUploadLink(new GetTemporaryUploadLinkArg(commitInfo));
    }

    public GetTemporaryUploadLinkResult getTemporaryUploadLink(CommitInfo commitInfo, double d) throws DbxApiException, DbxException {
        if (d < 60.0d) {
            throw new IllegalArgumentException("Number 'duration' is smaller than 60.0");
        } else if (d <= 14400.0d) {
            return getTemporaryUploadLink(new GetTemporaryUploadLinkArg(commitInfo, d));
        } else {
            throw new IllegalArgumentException("Number 'duration' is larger than 14400.0");
        }
    }

    /* access modifiers changed from: 0000 */
    public DbxDownloader<FileMetadata> getThumbnail(ThumbnailArg thumbnailArg, List<Header> list) throws ThumbnailErrorException, DbxException {
        try {
            return this.client.downloadStyle(this.client.getHost().getContent(), "2/files/get_thumbnail", thumbnailArg, false, list, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ThumbnailErrorException("2/files/get_thumbnail", e.getRequestId(), e.getUserMessage(), (ThumbnailError) e.getErrorValue());
        }
    }

    public DbxDownloader<FileMetadata> getThumbnail(String str) throws ThumbnailErrorException, DbxException {
        return getThumbnail(new ThumbnailArg(str), Collections.emptyList());
    }

    public GetThumbnailBuilder getThumbnailBuilder(String str) {
        return new GetThumbnailBuilder(this, ThumbnailArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public GetThumbnailBatchResult getThumbnailBatch(GetThumbnailBatchArg getThumbnailBatchArg) throws GetThumbnailBatchErrorException, DbxException {
        try {
            return (GetThumbnailBatchResult) this.client.rpcStyle(this.client.getHost().getContent(), "2/files/get_thumbnail_batch", getThumbnailBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new GetThumbnailBatchErrorException("2/files/get_thumbnail_batch", e.getRequestId(), e.getUserMessage(), (GetThumbnailBatchError) e.getErrorValue());
        }
    }

    public GetThumbnailBatchResult getThumbnailBatch(List<ThumbnailArg> list) throws GetThumbnailBatchErrorException, DbxException {
        return getThumbnailBatch(new GetThumbnailBatchArg(list));
    }

    /* access modifiers changed from: 0000 */
    public ListFolderResult listFolder(ListFolderArg listFolderArg) throws ListFolderErrorException, DbxException {
        try {
            return (ListFolderResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/list_folder", listFolderArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFolderErrorException("2/files/list_folder", e.getRequestId(), e.getUserMessage(), (ListFolderError) e.getErrorValue());
        }
    }

    public ListFolderResult listFolder(String str) throws ListFolderErrorException, DbxException {
        return listFolder(new ListFolderArg(str));
    }

    public ListFolderBuilder listFolderBuilder(String str) {
        return new ListFolderBuilder(this, ListFolderArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public ListFolderResult listFolderContinue(ListFolderContinueArg listFolderContinueArg) throws ListFolderContinueErrorException, DbxException {
        try {
            return (ListFolderResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/list_folder/continue", listFolderContinueArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFolderContinueErrorException("2/files/list_folder/continue", e.getRequestId(), e.getUserMessage(), (ListFolderContinueError) e.getErrorValue());
        }
    }

    public ListFolderResult listFolderContinue(String str) throws ListFolderContinueErrorException, DbxException {
        return listFolderContinue(new ListFolderContinueArg(str));
    }

    /* access modifiers changed from: 0000 */
    public ListFolderGetLatestCursorResult listFolderGetLatestCursor(ListFolderArg listFolderArg) throws ListFolderErrorException, DbxException {
        try {
            return (ListFolderGetLatestCursorResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/list_folder/get_latest_cursor", listFolderArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFolderErrorException("2/files/list_folder/get_latest_cursor", e.getRequestId(), e.getUserMessage(), (ListFolderError) e.getErrorValue());
        }
    }

    public ListFolderGetLatestCursorResult listFolderGetLatestCursor(String str) throws ListFolderErrorException, DbxException {
        return listFolderGetLatestCursor(new ListFolderArg(str));
    }

    public ListFolderGetLatestCursorBuilder listFolderGetLatestCursorBuilder(String str) {
        return new ListFolderGetLatestCursorBuilder(this, ListFolderArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public ListFolderLongpollResult listFolderLongpoll(ListFolderLongpollArg listFolderLongpollArg) throws ListFolderLongpollErrorException, DbxException {
        try {
            return (ListFolderLongpollResult) this.client.rpcStyle(this.client.getHost().getNotify(), "2/files/list_folder/longpoll", listFolderLongpollArg, true, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListFolderLongpollErrorException("2/files/list_folder/longpoll", e.getRequestId(), e.getUserMessage(), (ListFolderLongpollError) e.getErrorValue());
        }
    }

    public ListFolderLongpollResult listFolderLongpoll(String str) throws ListFolderLongpollErrorException, DbxException {
        return listFolderLongpoll(new ListFolderLongpollArg(str));
    }

    public ListFolderLongpollResult listFolderLongpoll(String str, long j) throws ListFolderLongpollErrorException, DbxException {
        if (j < 30) {
            throw new IllegalArgumentException("Number 'timeout' is smaller than 30L");
        } else if (j <= 480) {
            return listFolderLongpoll(new ListFolderLongpollArg(str, j));
        } else {
            throw new IllegalArgumentException("Number 'timeout' is larger than 480L");
        }
    }

    /* access modifiers changed from: 0000 */
    public ListRevisionsResult listRevisions(ListRevisionsArg listRevisionsArg) throws ListRevisionsErrorException, DbxException {
        try {
            return (ListRevisionsResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/list_revisions", listRevisionsArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new ListRevisionsErrorException("2/files/list_revisions", e.getRequestId(), e.getUserMessage(), (ListRevisionsError) e.getErrorValue());
        }
    }

    public ListRevisionsResult listRevisions(String str) throws ListRevisionsErrorException, DbxException {
        return listRevisions(new ListRevisionsArg(str));
    }

    public ListRevisionsBuilder listRevisionsBuilder(String str) {
        return new ListRevisionsBuilder(this, ListRevisionsArg.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public RelocationResult moveV2(RelocationArg relocationArg) throws RelocationErrorException, DbxException {
        try {
            return (RelocationResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/move_v2", relocationArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RelocationErrorException("2/files/move_v2", e.getRequestId(), e.getUserMessage(), (RelocationError) e.getErrorValue());
        }
    }

    public RelocationResult moveV2(String str, String str2) throws RelocationErrorException, DbxException {
        return moveV2(new RelocationArg(str, str2));
    }

    public MoveV2Builder moveV2Builder(String str, String str2) {
        return new MoveV2Builder(this, RelocationArg.newBuilder(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public Metadata move(RelocationArg relocationArg) throws RelocationErrorException, DbxException {
        try {
            return (Metadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/move", relocationArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RelocationErrorException("2/files/move", e.getRequestId(), e.getUserMessage(), (RelocationError) e.getErrorValue());
        }
    }

    @Deprecated
    public Metadata move(String str, String str2) throws RelocationErrorException, DbxException {
        return move(new RelocationArg(str, str2));
    }

    @Deprecated
    public MoveBuilder moveBuilder(String str, String str2) {
        return new MoveBuilder(this, RelocationArg.newBuilder(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public RelocationBatchLaunch moveBatch(RelocationBatchArg relocationBatchArg) throws DbxApiException, DbxException {
        try {
            return (RelocationBatchLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/move_batch", relocationBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"move_batch\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public RelocationBatchLaunch moveBatch(List<RelocationPath> list) throws DbxApiException, DbxException {
        return moveBatch(new RelocationBatchArg(list));
    }

    public MoveBatchBuilder moveBatchBuilder(List<RelocationPath> list) {
        return new MoveBatchBuilder(this, RelocationBatchArg.newBuilder(list));
    }

    /* access modifiers changed from: 0000 */
    public RelocationBatchJobStatus moveBatchCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (RelocationBatchJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/move_batch/check", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/files/move_batch/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public RelocationBatchJobStatus moveBatchCheck(String str) throws PollErrorException, DbxException {
        return moveBatchCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public void permanentlyDelete(DeleteArg deleteArg) throws DeleteErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/files/permanently_delete", deleteArg, false, Serializer.INSTANCE, StoneSerializers.void_(), Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new DeleteErrorException("2/files/permanently_delete", e.getRequestId(), e.getUserMessage(), (DeleteError) e.getErrorValue());
        }
    }

    public void permanentlyDelete(String str) throws DeleteErrorException, DbxException {
        permanentlyDelete(new DeleteArg(str));
    }

    public void permanentlyDelete(String str, String str2) throws DeleteErrorException, DbxException {
        if (str2 != null) {
            if (str2.length() < 9) {
                throw new IllegalArgumentException("String 'parentRev' is shorter than 9");
            } else if (!Pattern.matches("[0-9a-f]+", str2)) {
                throw new IllegalArgumentException("String 'parentRev' does not match pattern");
            }
        }
        permanentlyDelete(new DeleteArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public void propertiesAdd(AddPropertiesArg addPropertiesArg) throws AddPropertiesErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/files/properties/add", addPropertiesArg, false, AddPropertiesArg.Serializer.INSTANCE, StoneSerializers.void_(), AddPropertiesError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new AddPropertiesErrorException("2/files/properties/add", e.getRequestId(), e.getUserMessage(), (AddPropertiesError) e.getErrorValue());
        }
    }

    @Deprecated
    public void propertiesAdd(String str, List<PropertyGroup> list) throws AddPropertiesErrorException, DbxException {
        propertiesAdd(new AddPropertiesArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public void propertiesOverwrite(OverwritePropertyGroupArg overwritePropertyGroupArg) throws InvalidPropertyGroupErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/files/properties/overwrite", overwritePropertyGroupArg, false, OverwritePropertyGroupArg.Serializer.INSTANCE, StoneSerializers.void_(), InvalidPropertyGroupError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new InvalidPropertyGroupErrorException("2/files/properties/overwrite", e.getRequestId(), e.getUserMessage(), (InvalidPropertyGroupError) e.getErrorValue());
        }
    }

    @Deprecated
    public void propertiesOverwrite(String str, List<PropertyGroup> list) throws InvalidPropertyGroupErrorException, DbxException {
        propertiesOverwrite(new OverwritePropertyGroupArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public void propertiesRemove(RemovePropertiesArg removePropertiesArg) throws RemovePropertiesErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/files/properties/remove", removePropertiesArg, false, RemovePropertiesArg.Serializer.INSTANCE, StoneSerializers.void_(), RemovePropertiesError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RemovePropertiesErrorException("2/files/properties/remove", e.getRequestId(), e.getUserMessage(), (RemovePropertiesError) e.getErrorValue());
        }
    }

    @Deprecated
    public void propertiesRemove(String str, List<String> list) throws RemovePropertiesErrorException, DbxException {
        propertiesRemove(new RemovePropertiesArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public GetTemplateResult propertiesTemplateGet(GetTemplateArg getTemplateArg) throws TemplateErrorException, DbxException {
        try {
            return (GetTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/properties/template/get", getTemplateArg, false, GetTemplateArg.Serializer.INSTANCE, GetTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/files/properties/template/get", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    @Deprecated
    public GetTemplateResult propertiesTemplateGet(String str) throws TemplateErrorException, DbxException {
        return propertiesTemplateGet(new GetTemplateArg(str));
    }

    @Deprecated
    public ListTemplateResult propertiesTemplateList() throws TemplateErrorException, DbxException {
        try {
            return (ListTemplateResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/properties/template/list", null, false, StoneSerializers.void_(), ListTemplateResult.Serializer.INSTANCE, TemplateError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new TemplateErrorException("2/files/properties/template/list", e.getRequestId(), e.getUserMessage(), (TemplateError) e.getErrorValue());
        }
    }

    /* access modifiers changed from: 0000 */
    public void propertiesUpdate(UpdatePropertiesArg updatePropertiesArg) throws UpdatePropertiesErrorException, DbxException {
        try {
            this.client.rpcStyle(this.client.getHost().getApi(), "2/files/properties/update", updatePropertiesArg, false, UpdatePropertiesArg.Serializer.INSTANCE, StoneSerializers.void_(), UpdatePropertiesError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new UpdatePropertiesErrorException("2/files/properties/update", e.getRequestId(), e.getUserMessage(), (UpdatePropertiesError) e.getErrorValue());
        }
    }

    @Deprecated
    public void propertiesUpdate(String str, List<PropertyGroupUpdate> list) throws UpdatePropertiesErrorException, DbxException {
        propertiesUpdate(new UpdatePropertiesArg(str, list));
    }

    /* access modifiers changed from: 0000 */
    public FileMetadata restore(RestoreArg restoreArg) throws RestoreErrorException, DbxException {
        try {
            return (FileMetadata) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/restore", restoreArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new RestoreErrorException("2/files/restore", e.getRequestId(), e.getUserMessage(), (RestoreError) e.getErrorValue());
        }
    }

    public FileMetadata restore(String str, String str2) throws RestoreErrorException, DbxException {
        return restore(new RestoreArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public SaveUrlResult saveUrl(SaveUrlArg saveUrlArg) throws SaveUrlErrorException, DbxException {
        try {
            return (SaveUrlResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/save_url", saveUrlArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SaveUrlErrorException("2/files/save_url", e.getRequestId(), e.getUserMessage(), (SaveUrlError) e.getErrorValue());
        }
    }

    public SaveUrlResult saveUrl(String str, String str2) throws SaveUrlErrorException, DbxException {
        return saveUrl(new SaveUrlArg(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public SaveUrlJobStatus saveUrlCheckJobStatus(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (SaveUrlJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/save_url/check_job_status", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/files/save_url/check_job_status", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public SaveUrlJobStatus saveUrlCheckJobStatus(String str) throws PollErrorException, DbxException {
        return saveUrlCheckJobStatus(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public SearchResult search(SearchArg searchArg) throws SearchErrorException, DbxException {
        try {
            return (SearchResult) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/search", searchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new SearchErrorException("2/files/search", e.getRequestId(), e.getUserMessage(), (SearchError) e.getErrorValue());
        }
    }

    public SearchResult search(String str, String str2) throws SearchErrorException, DbxException {
        return search(new SearchArg(str, str2));
    }

    public SearchBuilder searchBuilder(String str, String str2) {
        return new SearchBuilder(this, SearchArg.newBuilder(str, str2));
    }

    /* access modifiers changed from: 0000 */
    public UploadUploader upload(CommitInfo commitInfo) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new UploadUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getContent(), "2/files/upload", commitInfo, false, Serializer.INSTANCE), this.client.getUserId());
    }

    public UploadUploader upload(String str) throws DbxException {
        return upload(new CommitInfo(str));
    }

    public UploadBuilder uploadBuilder(String str) {
        return new UploadBuilder(this, CommitInfo.newBuilder(str));
    }

    /* access modifiers changed from: 0000 */
    public UploadSessionAppendV2Uploader uploadSessionAppendV2(UploadSessionAppendArg uploadSessionAppendArg) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new UploadSessionAppendV2Uploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getContent(), "2/files/upload_session/append_v2", uploadSessionAppendArg, false, Serializer.INSTANCE), this.client.getUserId());
    }

    public UploadSessionAppendV2Uploader uploadSessionAppendV2(UploadSessionCursor uploadSessionCursor) throws DbxException {
        return uploadSessionAppendV2(new UploadSessionAppendArg(uploadSessionCursor));
    }

    public UploadSessionAppendV2Uploader uploadSessionAppendV2(UploadSessionCursor uploadSessionCursor, boolean z) throws DbxException {
        return uploadSessionAppendV2(new UploadSessionAppendArg(uploadSessionCursor, z));
    }

    /* access modifiers changed from: 0000 */
    public UploadSessionAppendUploader uploadSessionAppend(UploadSessionCursor uploadSessionCursor) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new UploadSessionAppendUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getContent(), "2/files/upload_session/append", uploadSessionCursor, false, Serializer.INSTANCE), this.client.getUserId());
    }

    @Deprecated
    public UploadSessionAppendUploader uploadSessionAppend(String str, long j) throws DbxException {
        return uploadSessionAppend(new UploadSessionCursor(str, j));
    }

    /* access modifiers changed from: 0000 */
    public UploadSessionFinishUploader uploadSessionFinish(UploadSessionFinishArg uploadSessionFinishArg) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new UploadSessionFinishUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getContent(), "2/files/upload_session/finish", uploadSessionFinishArg, false, Serializer.INSTANCE), this.client.getUserId());
    }

    public UploadSessionFinishUploader uploadSessionFinish(UploadSessionCursor uploadSessionCursor, CommitInfo commitInfo) throws DbxException {
        return uploadSessionFinish(new UploadSessionFinishArg(uploadSessionCursor, commitInfo));
    }

    /* access modifiers changed from: 0000 */
    public UploadSessionFinishBatchLaunch uploadSessionFinishBatch(UploadSessionFinishBatchArg uploadSessionFinishBatchArg) throws DbxApiException, DbxException {
        try {
            return (UploadSessionFinishBatchLaunch) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/upload_session/finish_batch", uploadSessionFinishBatchArg, false, Serializer.INSTANCE, Serializer.INSTANCE, StoneSerializers.void_());
        } catch (DbxWrappedException e) {
            String requestId = e.getRequestId();
            LocalizedText userMessage = e.getUserMessage();
            StringBuilder sb = new StringBuilder();
            sb.append("Unexpected error response for \"upload_session/finish_batch\":");
            sb.append(e.getErrorValue());
            throw new DbxApiException(requestId, userMessage, sb.toString());
        }
    }

    public UploadSessionFinishBatchLaunch uploadSessionFinishBatch(List<UploadSessionFinishArg> list) throws DbxApiException, DbxException {
        return uploadSessionFinishBatch(new UploadSessionFinishBatchArg(list));
    }

    /* access modifiers changed from: 0000 */
    public UploadSessionFinishBatchJobStatus uploadSessionFinishBatchCheck(PollArg pollArg) throws PollErrorException, DbxException {
        try {
            return (UploadSessionFinishBatchJobStatus) this.client.rpcStyle(this.client.getHost().getApi(), "2/files/upload_session/finish_batch/check", pollArg, false, Serializer.INSTANCE, Serializer.INSTANCE, PollError.Serializer.INSTANCE);
        } catch (DbxWrappedException e) {
            throw new PollErrorException("2/files/upload_session/finish_batch/check", e.getRequestId(), e.getUserMessage(), (PollError) e.getErrorValue());
        }
    }

    public UploadSessionFinishBatchJobStatus uploadSessionFinishBatchCheck(String str) throws PollErrorException, DbxException {
        return uploadSessionFinishBatchCheck(new PollArg(str));
    }

    /* access modifiers changed from: 0000 */
    public UploadSessionStartUploader uploadSessionStart(UploadSessionStartArg uploadSessionStartArg) throws DbxException {
        DbxRawClientV2 dbxRawClientV2 = this.client;
        return new UploadSessionStartUploader(dbxRawClientV2.uploadStyle(dbxRawClientV2.getHost().getContent(), "2/files/upload_session/start", uploadSessionStartArg, false, Serializer.INSTANCE), this.client.getUserId());
    }

    public UploadSessionStartUploader uploadSessionStart() throws DbxException {
        return uploadSessionStart(new UploadSessionStartArg());
    }

    public UploadSessionStartUploader uploadSessionStart(boolean z) throws DbxException {
        return uploadSessionStart(new UploadSessionStartArg(z));
    }
}
