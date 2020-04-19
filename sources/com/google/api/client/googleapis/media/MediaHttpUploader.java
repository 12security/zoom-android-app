package com.google.api.client.googleapis.media;

import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.EmptyContent;
import com.google.api.client.http.GZipEncoding;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.MultipartContent;
import com.google.api.client.util.Beta;
import com.google.api.client.util.ByteStreams;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public final class MediaHttpUploader {
    public static final String CONTENT_LENGTH_HEADER = "X-Upload-Content-Length";
    public static final String CONTENT_TYPE_HEADER = "X-Upload-Content-Type";
    public static final int DEFAULT_CHUNK_SIZE = 10485760;

    /* renamed from: KB */
    private static final int f201KB = 1024;

    /* renamed from: MB */
    static final int f202MB = 1048576;
    public static final int MINIMUM_CHUNK_SIZE = 262144;
    private Byte cachedByte;
    private int chunkSize = DEFAULT_CHUNK_SIZE;
    private InputStream contentInputStream;
    private int currentChunkLength;
    private HttpRequest currentRequest;
    private byte[] currentRequestContentBuffer;
    private boolean directUploadEnabled;
    private boolean disableGZipContent;
    private HttpHeaders initiationHeaders = new HttpHeaders();
    private String initiationRequestMethod = "POST";
    private boolean isMediaContentLengthCalculated;
    private final AbstractInputStreamContent mediaContent;
    private long mediaContentLength;
    String mediaContentLengthStr = "*";
    private HttpContent metadata;
    private MediaHttpUploaderProgressListener progressListener;
    private final HttpRequestFactory requestFactory;
    Sleeper sleeper = Sleeper.DEFAULT;
    private long totalBytesClientSent;
    private long totalBytesServerReceived;
    private final HttpTransport transport;
    private UploadState uploadState = UploadState.NOT_STARTED;

    private static class ContentChunk {
        private final AbstractInputStreamContent content;
        private final String contentRange;

        ContentChunk(AbstractInputStreamContent abstractInputStreamContent, String str) {
            this.content = abstractInputStreamContent;
            this.contentRange = str;
        }

        /* access modifiers changed from: 0000 */
        public AbstractInputStreamContent getContent() {
            return this.content;
        }

        /* access modifiers changed from: 0000 */
        public String getContentRange() {
            return this.contentRange;
        }
    }

    public enum UploadState {
        NOT_STARTED,
        INITIATION_STARTED,
        INITIATION_COMPLETE,
        MEDIA_IN_PROGRESS,
        MEDIA_COMPLETE
    }

    public MediaHttpUploader(AbstractInputStreamContent abstractInputStreamContent, HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
        this.mediaContent = (AbstractInputStreamContent) Preconditions.checkNotNull(abstractInputStreamContent);
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.requestFactory = httpRequestInitializer == null ? httpTransport.createRequestFactory() : httpTransport.createRequestFactory(httpRequestInitializer);
    }

    public HttpResponse upload(GenericUrl genericUrl) throws IOException {
        Preconditions.checkArgument(this.uploadState == UploadState.NOT_STARTED);
        if (this.directUploadEnabled) {
            return directUpload(genericUrl);
        }
        return resumableUpload(genericUrl);
    }

    private HttpResponse directUpload(GenericUrl genericUrl) throws IOException {
        updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
        HttpContent httpContent = this.mediaContent;
        if (this.metadata != null) {
            httpContent = new MultipartContent().setContentParts(Arrays.asList(new HttpContent[]{this.metadata, this.mediaContent}));
            genericUrl.put("uploadType", (Object) "multipart");
        } else {
            genericUrl.put("uploadType", (Object) "media");
        }
        HttpRequest buildRequest = this.requestFactory.buildRequest(this.initiationRequestMethod, genericUrl, httpContent);
        buildRequest.getHeaders().putAll(this.initiationHeaders);
        HttpResponse executeCurrentRequest = executeCurrentRequest(buildRequest);
        try {
            if (isMediaLengthKnown()) {
                this.totalBytesServerReceived = getMediaContentLength();
            }
            updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
            return executeCurrentRequest;
        } catch (Throwable th) {
            executeCurrentRequest.disconnect();
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    private HttpResponse resumableUpload(GenericUrl genericUrl) throws IOException {
        HttpResponse httpResponse;
        HttpResponse executeUploadInitiation = executeUploadInitiation(genericUrl);
        if (!executeUploadInitiation.isSuccessStatusCode()) {
            return executeUploadInitiation;
        }
        try {
            GenericUrl genericUrl2 = new GenericUrl(executeUploadInitiation.getHeaders().getLocation());
            executeUploadInitiation.disconnect();
            this.contentInputStream = this.mediaContent.getInputStream();
            if (!this.contentInputStream.markSupported() && isMediaLengthKnown()) {
                this.contentInputStream = new BufferedInputStream(this.contentInputStream);
            }
            while (true) {
                ContentChunk buildContentChunk = buildContentChunk();
                this.currentRequest = this.requestFactory.buildPutRequest(genericUrl2, null);
                this.currentRequest.setContent(buildContentChunk.getContent());
                this.currentRequest.getHeaders().setContentRange(buildContentChunk.getContentRange());
                new MediaUploadErrorHandler(this, this.currentRequest);
                if (isMediaLengthKnown()) {
                    httpResponse = executeCurrentRequestWithoutGZip(this.currentRequest);
                } else {
                    httpResponse = executeCurrentRequest(this.currentRequest);
                }
                try {
                    if (httpResponse.isSuccessStatusCode()) {
                        this.totalBytesServerReceived = getMediaContentLength();
                        if (this.mediaContent.getCloseInputStream()) {
                            this.contentInputStream.close();
                        }
                        updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
                        return httpResponse;
                    } else if (httpResponse.getStatusCode() != 308) {
                        return httpResponse;
                    } else {
                        String location = httpResponse.getHeaders().getLocation();
                        if (location != null) {
                            genericUrl2 = new GenericUrl(location);
                        }
                        long nextByteIndex = getNextByteIndex(httpResponse.getHeaders().getRange());
                        long j = nextByteIndex - this.totalBytesServerReceived;
                        boolean z = true;
                        Preconditions.checkState(j >= 0 && j <= ((long) this.currentChunkLength));
                        long j2 = ((long) this.currentChunkLength) - j;
                        if (isMediaLengthKnown()) {
                            if (j2 > 0) {
                                this.contentInputStream.reset();
                                if (j != this.contentInputStream.skip(j)) {
                                    z = false;
                                }
                                Preconditions.checkState(z);
                            }
                        } else if (j2 == 0) {
                            this.currentRequestContentBuffer = null;
                        }
                        this.totalBytesServerReceived = nextByteIndex;
                        updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
                    }
                } finally {
                    httpResponse.disconnect();
                }
            }
        } catch (Throwable th) {
            executeUploadInitiation.disconnect();
            throw th;
        }
    }

    private boolean isMediaLengthKnown() throws IOException {
        return getMediaContentLength() >= 0;
    }

    private long getMediaContentLength() throws IOException {
        if (!this.isMediaContentLengthCalculated) {
            this.mediaContentLength = this.mediaContent.getLength();
            this.isMediaContentLengthCalculated = true;
        }
        return this.mediaContentLength;
    }

    private HttpResponse executeUploadInitiation(GenericUrl genericUrl) throws IOException {
        updateStateAndNotifyListener(UploadState.INITIATION_STARTED);
        genericUrl.put("uploadType", (Object) "resumable");
        HttpContent httpContent = this.metadata;
        if (httpContent == null) {
            httpContent = new EmptyContent();
        }
        HttpRequest buildRequest = this.requestFactory.buildRequest(this.initiationRequestMethod, genericUrl, httpContent);
        this.initiationHeaders.set(CONTENT_TYPE_HEADER, (Object) this.mediaContent.getType());
        if (isMediaLengthKnown()) {
            this.initiationHeaders.set(CONTENT_LENGTH_HEADER, (Object) Long.valueOf(getMediaContentLength()));
        }
        buildRequest.getHeaders().putAll(this.initiationHeaders);
        HttpResponse executeCurrentRequest = executeCurrentRequest(buildRequest);
        try {
            updateStateAndNotifyListener(UploadState.INITIATION_COMPLETE);
            return executeCurrentRequest;
        } catch (Throwable th) {
            executeCurrentRequest.disconnect();
            throw th;
        }
    }

    private HttpResponse executeCurrentRequestWithoutGZip(HttpRequest httpRequest) throws IOException {
        new MethodOverride().intercept(httpRequest);
        httpRequest.setThrowExceptionOnExecuteError(false);
        return httpRequest.execute();
    }

    private HttpResponse executeCurrentRequest(HttpRequest httpRequest) throws IOException {
        if (!this.disableGZipContent && !(httpRequest.getContent() instanceof EmptyContent)) {
            httpRequest.setEncoding(new GZipEncoding());
        }
        return executeCurrentRequestWithoutGZip(httpRequest);
    }

    private ContentChunk buildContentChunk() throws IOException {
        int i;
        AbstractInputStreamContent abstractInputStreamContent;
        String str;
        int i2;
        int i3;
        if (isMediaLengthKnown()) {
            i = (int) Math.min((long) this.chunkSize, getMediaContentLength() - this.totalBytesServerReceived);
        } else {
            i = this.chunkSize;
        }
        if (isMediaLengthKnown()) {
            this.contentInputStream.mark(i);
            long j = (long) i;
            abstractInputStreamContent = new InputStreamContent(this.mediaContent.getType(), ByteStreams.limit(this.contentInputStream, j)).setRetrySupported(true).setLength(j).setCloseInputStream(false);
            this.mediaContentLengthStr = String.valueOf(getMediaContentLength());
        } else {
            byte[] bArr = this.currentRequestContentBuffer;
            if (bArr == null) {
                i3 = this.cachedByte == null ? i + 1 : i;
                this.currentRequestContentBuffer = new byte[(i + 1)];
                Byte b = this.cachedByte;
                if (b != null) {
                    this.currentRequestContentBuffer[0] = b.byteValue();
                }
                i2 = 0;
            } else {
                i2 = (int) (this.totalBytesClientSent - this.totalBytesServerReceived);
                System.arraycopy(bArr, this.currentChunkLength - i2, bArr, 0, i2);
                Byte b2 = this.cachedByte;
                if (b2 != null) {
                    this.currentRequestContentBuffer[i2] = b2.byteValue();
                }
                i3 = i - i2;
            }
            int read = ByteStreams.read(this.contentInputStream, this.currentRequestContentBuffer, (i + 1) - i3, i3);
            if (read < i3) {
                int max = i2 + Math.max(0, read);
                if (this.cachedByte != null) {
                    max++;
                    this.cachedByte = null;
                }
                if (this.mediaContentLengthStr.equals("*")) {
                    this.mediaContentLengthStr = String.valueOf(this.totalBytesServerReceived + ((long) max));
                }
                i = max;
            } else {
                this.cachedByte = Byte.valueOf(this.currentRequestContentBuffer[i]);
            }
            abstractInputStreamContent = new ByteArrayContent(this.mediaContent.getType(), this.currentRequestContentBuffer, 0, i);
            this.totalBytesClientSent = this.totalBytesServerReceived + ((long) i);
        }
        this.currentChunkLength = i;
        if (i == 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("bytes */");
            sb.append(this.mediaContentLengthStr);
            str = sb.toString();
        } else {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("bytes ");
            sb2.append(this.totalBytesServerReceived);
            sb2.append("-");
            sb2.append((this.totalBytesServerReceived + ((long) i)) - 1);
            sb2.append("/");
            sb2.append(this.mediaContentLengthStr);
            str = sb2.toString();
        }
        return new ContentChunk(abstractInputStreamContent, str);
    }

    /* access modifiers changed from: 0000 */
    @Beta
    public void serverErrorCallback() throws IOException {
        Preconditions.checkNotNull(this.currentRequest, "The current request should not be null");
        this.currentRequest.setContent(new EmptyContent());
        HttpHeaders headers = this.currentRequest.getHeaders();
        StringBuilder sb = new StringBuilder();
        sb.append("bytes */");
        sb.append(this.mediaContentLengthStr);
        headers.setContentRange(sb.toString());
    }

    private long getNextByteIndex(String str) {
        if (str == null) {
            return 0;
        }
        return Long.parseLong(str.substring(str.indexOf(45) + 1)) + 1;
    }

    public HttpContent getMetadata() {
        return this.metadata;
    }

    public MediaHttpUploader setMetadata(HttpContent httpContent) {
        this.metadata = httpContent;
        return this;
    }

    public HttpContent getMediaContent() {
        return this.mediaContent;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public MediaHttpUploader setDirectUploadEnabled(boolean z) {
        this.directUploadEnabled = z;
        return this;
    }

    public boolean isDirectUploadEnabled() {
        return this.directUploadEnabled;
    }

    public MediaHttpUploader setProgressListener(MediaHttpUploaderProgressListener mediaHttpUploaderProgressListener) {
        this.progressListener = mediaHttpUploaderProgressListener;
        return this;
    }

    public MediaHttpUploaderProgressListener getProgressListener() {
        return this.progressListener;
    }

    public MediaHttpUploader setChunkSize(int i) {
        Preconditions.checkArgument(i > 0 && i % 262144 == 0, "chunkSize must be a positive multiple of 262144.");
        this.chunkSize = i;
        return this;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public boolean getDisableGZipContent() {
        return this.disableGZipContent;
    }

    public MediaHttpUploader setDisableGZipContent(boolean z) {
        this.disableGZipContent = z;
        return this;
    }

    public Sleeper getSleeper() {
        return this.sleeper;
    }

    public MediaHttpUploader setSleeper(Sleeper sleeper2) {
        this.sleeper = sleeper2;
        return this;
    }

    public String getInitiationRequestMethod() {
        return this.initiationRequestMethod;
    }

    public MediaHttpUploader setInitiationRequestMethod(String str) {
        Preconditions.checkArgument(str.equals("POST") || str.equals("PUT") || str.equals("PATCH"));
        this.initiationRequestMethod = str;
        return this;
    }

    public MediaHttpUploader setInitiationHeaders(HttpHeaders httpHeaders) {
        this.initiationHeaders = httpHeaders;
        return this;
    }

    public HttpHeaders getInitiationHeaders() {
        return this.initiationHeaders;
    }

    public long getNumBytesUploaded() {
        return this.totalBytesServerReceived;
    }

    private void updateStateAndNotifyListener(UploadState uploadState2) throws IOException {
        this.uploadState = uploadState2;
        MediaHttpUploaderProgressListener mediaHttpUploaderProgressListener = this.progressListener;
        if (mediaHttpUploaderProgressListener != null) {
            mediaHttpUploaderProgressListener.progressChanged(this);
        }
    }

    public UploadState getUploadState() {
        return this.uploadState;
    }

    public double getProgress() throws IOException {
        Preconditions.checkArgument(isMediaLengthKnown(), "Cannot call getProgress() if the specified AbstractInputStreamContent has no content length. Use  getNumBytesUploaded() to denote progress instead.");
        if (getMediaContentLength() == 0) {
            return 0.0d;
        }
        return ((double) this.totalBytesServerReceived) / ((double) getMediaContentLength());
    }
}
