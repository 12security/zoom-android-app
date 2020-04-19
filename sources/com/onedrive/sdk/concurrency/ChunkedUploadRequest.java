package com.onedrive.sdk.concurrency;

import com.onedrive.sdk.core.ClientException;
import com.onedrive.sdk.core.OneDriveErrorCodes;
import com.onedrive.sdk.extensions.ChunkedUploadResult;
import com.onedrive.sdk.extensions.IOneDriveClient;
import com.onedrive.sdk.http.BaseRequest;
import com.onedrive.sdk.http.HttpMethod;
import com.onedrive.sdk.http.IHttpRequest;
import com.onedrive.sdk.http.IStatefulResponseHandler;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class ChunkedUploadRequest {
    private static final String CONTENT_RANGE_FORMAT = "bytes %1$d-%2$d/%3$d";
    private static final String CONTENT_RANGE_HEADER_NAME = "Content-Range";
    private static final int RETRY_DELAY = 2000;
    private final BaseRequest mBaseRequest;
    private final byte[] mData;
    private final int mMaxRetry;
    private int mRetryCount = 0;

    public ChunkedUploadRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, byte[] bArr, int i, int i2, int i3, int i4) {
        int i5 = i;
        this.mData = new byte[i5];
        byte[] bArr2 = bArr;
        System.arraycopy(bArr, 0, this.mData, 0, i);
        this.mMaxRetry = i2;
        C17861 r0 = new BaseRequest(str, iOneDriveClient, list, ChunkedUploadResult.class) {
        };
        this.mBaseRequest = r0;
        this.mBaseRequest.setHttpMethod(HttpMethod.PUT);
        this.mBaseRequest.addHeader("Content-Range", String.format(CONTENT_RANGE_FORMAT, new Object[]{Integer.valueOf(i3), Integer.valueOf((i3 + i5) - 1), Integer.valueOf(i4)}));
    }

    public <UploadType> ChunkedUploadResult upload(ChunkedUploadResponseHandler<UploadType> chunkedUploadResponseHandler) {
        ChunkedUploadResult chunkedUploadResult;
        while (true) {
            int i = this.mRetryCount;
            if (i >= this.mMaxRetry) {
                return new ChunkedUploadResult(new ClientException("Upload session failed to many times.", null, OneDriveErrorCodes.UploadSessionIncomplete));
            }
            try {
                Thread.sleep((long) (i * 2000 * i));
            } catch (InterruptedException e) {
                this.mBaseRequest.getClient().getLogger().logError("Exception while waiting upload file retry", e);
            }
            try {
                chunkedUploadResult = (ChunkedUploadResult) this.mBaseRequest.getClient().getHttpProvider().send((IHttpRequest) this.mBaseRequest, ChunkedUploadResult.class, this.mData, (IStatefulResponseHandler<Result, DeserializeType>) chunkedUploadResponseHandler);
            } catch (ClientException unused) {
                this.mBaseRequest.getClient().getLogger().logDebug("Request failed with, retry if necessary.");
                chunkedUploadResult = null;
            }
            if (chunkedUploadResult != null && chunkedUploadResult.chunkCompleted()) {
                return chunkedUploadResult;
            }
            this.mRetryCount++;
        }
    }
}
