package com.google.api.client.googleapis.media;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.IOUtils;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

public final class MediaHttpDownloader {
    public static final int MAXIMUM_CHUNK_SIZE = 33554432;
    private long bytesDownloaded;
    private int chunkSize = MAXIMUM_CHUNK_SIZE;
    private boolean directDownloadEnabled = false;
    private DownloadState downloadState = DownloadState.NOT_STARTED;
    private long lastBytePos = -1;
    private long mediaContentLength;
    private MediaHttpDownloaderProgressListener progressListener;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;

    public enum DownloadState {
        NOT_STARTED,
        MEDIA_IN_PROGRESS,
        MEDIA_COMPLETE
    }

    public MediaHttpDownloader(HttpTransport httpTransport, HttpRequestInitializer httpRequestInitializer) {
        this.transport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
        this.requestFactory = httpRequestInitializer == null ? httpTransport.createRequestFactory() : httpTransport.createRequestFactory(httpRequestInitializer);
    }

    public void download(GenericUrl genericUrl, OutputStream outputStream) throws IOException {
        download(genericUrl, null, outputStream);
    }

    public void download(GenericUrl genericUrl, HttpHeaders httpHeaders, OutputStream outputStream) throws IOException {
        Preconditions.checkArgument(this.downloadState == DownloadState.NOT_STARTED);
        genericUrl.put("alt", (Object) "media");
        if (this.directDownloadEnabled) {
            updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
            this.mediaContentLength = executeCurrentRequest(this.lastBytePos, genericUrl, httpHeaders, outputStream).getHeaders().getContentLength().longValue();
            this.bytesDownloaded = this.mediaContentLength;
            updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
            return;
        }
        while (true) {
            long j = (this.bytesDownloaded + ((long) this.chunkSize)) - 1;
            long j2 = this.lastBytePos;
            String contentRange = executeCurrentRequest(j2 != -1 ? Math.min(j2, j) : j, genericUrl, httpHeaders, outputStream).getHeaders().getContentRange();
            long nextByteIndex = getNextByteIndex(contentRange);
            setMediaContentLength(contentRange);
            long j3 = this.mediaContentLength;
            if (j3 <= nextByteIndex) {
                this.bytesDownloaded = j3;
                updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
                return;
            }
            this.bytesDownloaded = nextByteIndex;
            updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
        }
    }

    private HttpResponse executeCurrentRequest(long j, GenericUrl genericUrl, HttpHeaders httpHeaders, OutputStream outputStream) throws IOException {
        HttpRequest buildGetRequest = this.requestFactory.buildGetRequest(genericUrl);
        if (httpHeaders != null) {
            buildGetRequest.getHeaders().putAll(httpHeaders);
        }
        if (!(this.bytesDownloaded == 0 && j == -1)) {
            StringBuilder sb = new StringBuilder();
            sb.append("bytes=");
            sb.append(this.bytesDownloaded);
            sb.append("-");
            if (j != -1) {
                sb.append(j);
            }
            buildGetRequest.getHeaders().setRange(sb.toString());
        }
        HttpResponse execute = buildGetRequest.execute();
        try {
            IOUtils.copy(execute.getContent(), outputStream);
            return execute;
        } finally {
            execute.disconnect();
        }
    }

    private long getNextByteIndex(String str) {
        if (str == null) {
            return 0;
        }
        return Long.parseLong(str.substring(str.indexOf(45) + 1, str.indexOf(47))) + 1;
    }

    public MediaHttpDownloader setBytesDownloaded(long j) {
        Preconditions.checkArgument(j >= 0);
        this.bytesDownloaded = j;
        return this;
    }

    public MediaHttpDownloader setContentRange(long j, long j2) {
        Preconditions.checkArgument(j2 >= j);
        setBytesDownloaded(j);
        this.lastBytePos = j2;
        return this;
    }

    @Deprecated
    public MediaHttpDownloader setContentRange(long j, int i) {
        return setContentRange(j, (long) i);
    }

    private void setMediaContentLength(String str) {
        if (str != null && this.mediaContentLength == 0) {
            this.mediaContentLength = Long.parseLong(str.substring(str.indexOf(47) + 1));
        }
    }

    public boolean isDirectDownloadEnabled() {
        return this.directDownloadEnabled;
    }

    public MediaHttpDownloader setDirectDownloadEnabled(boolean z) {
        this.directDownloadEnabled = z;
        return this;
    }

    public MediaHttpDownloader setProgressListener(MediaHttpDownloaderProgressListener mediaHttpDownloaderProgressListener) {
        this.progressListener = mediaHttpDownloaderProgressListener;
        return this;
    }

    public MediaHttpDownloaderProgressListener getProgressListener() {
        return this.progressListener;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public MediaHttpDownloader setChunkSize(int i) {
        Preconditions.checkArgument(i > 0 && i <= 33554432);
        this.chunkSize = i;
        return this;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public long getNumBytesDownloaded() {
        return this.bytesDownloaded;
    }

    public long getLastBytePosition() {
        return this.lastBytePos;
    }

    private void updateStateAndNotifyListener(DownloadState downloadState2) throws IOException {
        this.downloadState = downloadState2;
        MediaHttpDownloaderProgressListener mediaHttpDownloaderProgressListener = this.progressListener;
        if (mediaHttpDownloaderProgressListener != null) {
            mediaHttpDownloaderProgressListener.progressChanged(this);
        }
    }

    public DownloadState getDownloadState() {
        return this.downloadState;
    }

    public double getProgress() {
        long j = this.mediaContentLength;
        if (j == 0) {
            return 0.0d;
        }
        return ((double) this.bytesDownloaded) / ((double) j);
    }
}
