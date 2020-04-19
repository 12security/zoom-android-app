package com.onedrive.sdk.extensions;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.http.OneDriveServiceException;

public class ChunkedUploadResult<UploadType> {
    private final ClientException mError;
    private final UploadSession mSession;
    private final UploadType mUploadedItem;

    public ChunkedUploadResult(UploadType uploadtype) {
        this.mUploadedItem = uploadtype;
        this.mSession = null;
        this.mError = null;
    }

    public ChunkedUploadResult(UploadSession uploadSession) {
        this.mSession = uploadSession;
        this.mUploadedItem = null;
        this.mError = null;
    }

    public ChunkedUploadResult(ClientException clientException) {
        this.mError = clientException;
        this.mUploadedItem = null;
        this.mSession = null;
    }

    public ChunkedUploadResult(OneDriveServiceException oneDriveServiceException) {
        this(new ClientException(oneDriveServiceException.getMessage(true), oneDriveServiceException, OneDriveErrorCodes.UploadSessionFailed));
    }

    public boolean chunkCompleted() {
        return (this.mUploadedItem == null && this.mSession == null) ? false : true;
    }

    public boolean uploadCompleted() {
        return this.mUploadedItem != null;
    }

    public boolean hasError() {
        return this.mError != null;
    }

    public UploadType getItem() {
        return this.mUploadedItem;
    }

    public UploadSession getSession() {
        return this.mSession;
    }

    public ClientException getError() {
        return this.mError;
    }
}
