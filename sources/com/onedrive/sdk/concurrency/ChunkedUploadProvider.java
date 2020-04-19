package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.extensions.ChunkedUploadResult;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.extensions.UploadSession;
import com.onedrive.sdk.options.Option;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;
import java.util.List;

public class ChunkedUploadProvider<UploadType> {
    private static final int DEFAULT_CHUNK_SIZE = 5242880;
    private static final int MAXIMUM_CHUNK_SIZE = 62914560;
    private static final int MAXIMUM_RETRY_TIMES = 3;
    private static final int REQUIRED_CHUNK_SIZE_INCREMENT = 327680;
    private final IOneDriveClient mClient;
    private final InputStream mInputStream;
    private int mReadSoFar;
    private final ChunkedUploadResponseHandler<UploadType> mResponseHandler;
    private final int mStreamSize;
    private final String mUploadUrl;

    public ChunkedUploadProvider(UploadSession uploadSession, IOneDriveClient iOneDriveClient, InputStream inputStream, int i, Class<UploadType> cls) {
        if (uploadSession == null) {
            throw new InvalidParameterException("Upload session is null.");
        } else if (iOneDriveClient == null) {
            throw new InvalidParameterException("OneDrive client is null.");
        } else if (inputStream == null) {
            throw new InvalidParameterException("Input stream is null.");
        } else if (i > 0) {
            this.mClient = iOneDriveClient;
            this.mReadSoFar = 0;
            this.mInputStream = inputStream;
            this.mStreamSize = i;
            this.mUploadUrl = uploadSession.uploadUrl;
            this.mResponseHandler = new ChunkedUploadResponseHandler<>(cls);
        } else {
            throw new InvalidParameterException("Stream size should larger than 0.");
        }
    }

    public void upload(List<Option> list, IProgressCallback<UploadType> iProgressCallback, int... iArr) throws IOException {
        int i = iArr.length > 0 ? iArr[0] : DEFAULT_CHUNK_SIZE;
        int i2 = 3;
        if (iArr.length > 1) {
            i2 = iArr[1];
        }
        if (i % REQUIRED_CHUNK_SIZE_INCREMENT != 0) {
            throw new IllegalArgumentException("Chunk size must be a multiple of 320 KiB");
        } else if (i <= MAXIMUM_CHUNK_SIZE) {
            byte[] bArr = new byte[i];
            while (this.mReadSoFar < this.mStreamSize) {
                int read = this.mInputStream.read(bArr);
                if (read != -1) {
                    ChunkedUploadRequest chunkedUploadRequest = new ChunkedUploadRequest(this.mUploadUrl, this.mClient, list, bArr, read, i2, this.mReadSoFar, this.mStreamSize);
                    ChunkedUploadResult upload = chunkedUploadRequest.upload(this.mResponseHandler);
                    if (upload.uploadCompleted()) {
                        int i3 = this.mStreamSize;
                        iProgressCallback.progress((long) i3, (long) i3);
                        iProgressCallback.success(upload.getItem());
                        return;
                    }
                    if (upload.chunkCompleted()) {
                        iProgressCallback.progress((long) this.mReadSoFar, (long) this.mStreamSize);
                    } else if (upload.hasError()) {
                        iProgressCallback.failure(upload.getError());
                        return;
                    }
                    this.mReadSoFar += read;
                } else {
                    return;
                }
            }
        } else {
            throw new IllegalArgumentException("Please set chunk size smaller than 60 MiB");
        }
    }
}
