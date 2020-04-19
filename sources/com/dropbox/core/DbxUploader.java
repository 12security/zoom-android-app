package com.dropbox.core;

import com.dropbox.core.DbxApiException;
import com.dropbox.core.http.HttpRequestor.Response;
import com.dropbox.core.http.HttpRequestor.Uploader;
import com.dropbox.core.stone.StoneSerializer;
import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.util.IOUtil.ReadException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class DbxUploader<R, E, X extends DbxApiException> implements Closeable {
    private boolean closed = false;
    private final StoneSerializer<E> errorSerializer;
    private boolean finished = false;
    private final Uploader httpUploader;
    private final StoneSerializer<R> responseSerializer;
    private final String userId;

    /* access modifiers changed from: protected */
    public abstract X newException(DbxWrappedException dbxWrappedException);

    protected DbxUploader(Uploader uploader, StoneSerializer<R> stoneSerializer, StoneSerializer<E> stoneSerializer2, String str) {
        this.httpUploader = uploader;
        this.responseSerializer = stoneSerializer;
        this.errorSerializer = stoneSerializer2;
        this.userId = str;
    }

    public R uploadAndFinish(InputStream inputStream) throws DbxApiException, DbxException, IOException {
        return uploadAndFinish(inputStream, (ProgressListener) null);
    }

    public R uploadAndFinish(InputStream inputStream, ProgressListener progressListener) throws DbxApiException, DbxException, IOException {
        try {
            this.httpUploader.setProgressListener(progressListener);
            this.httpUploader.upload(inputStream);
            R finish = finish();
            close();
            return finish;
        } catch (ReadException e) {
            throw e.getCause();
        } catch (IOException e2) {
            throw new NetworkIOException(e2);
        } catch (Throwable th) {
            close();
            throw th;
        }
    }

    public R uploadAndFinish(InputStream inputStream, long j) throws DbxApiException, DbxException, IOException {
        return uploadAndFinish(IOUtil.limit(inputStream, j));
    }

    public R uploadAndFinish(InputStream inputStream, long j, ProgressListener progressListener) throws DbxApiException, DbxException, IOException {
        return uploadAndFinish(IOUtil.limit(inputStream, j), progressListener);
    }

    public void close() {
        if (!this.closed) {
            this.httpUploader.close();
            this.closed = true;
        }
    }

    public void abort() {
        this.httpUploader.abort();
    }

    public OutputStream getOutputStream() {
        assertOpenAndUnfinished();
        return this.httpUploader.getBody();
    }

    public R finish() throws DbxApiException, DbxException {
        assertOpenAndUnfinished();
        Response response = null;
        try {
            response = this.httpUploader.finish();
            if (response.getStatusCode() == 200) {
                R deserialize = this.responseSerializer.deserialize(response.getBody());
                if (response != null) {
                    IOUtil.closeQuietly(response.getBody());
                }
                this.finished = true;
                return deserialize;
            } else if (response.getStatusCode() == 409) {
                throw newException(DbxWrappedException.fromResponse(this.errorSerializer, response, this.userId));
            } else {
                throw DbxRequestUtil.unexpectedStatus(response);
            }
        } catch (JsonProcessingException e) {
            String requestId = DbxRequestUtil.getRequestId(response);
            StringBuilder sb = new StringBuilder();
            sb.append("Bad JSON in response: ");
            sb.append(e);
            throw new BadResponseException(requestId, sb.toString(), e);
        } catch (IOException e2) {
            try {
                throw new NetworkIOException(e2);
            } catch (Throwable th) {
                if (response != null) {
                    IOUtil.closeQuietly(response.getBody());
                }
                this.finished = true;
                throw th;
            }
        }
    }

    private void assertOpenAndUnfinished() {
        if (this.closed) {
            throw new IllegalStateException("This uploader is already closed.");
        } else if (this.finished) {
            throw new IllegalStateException("This uploader is already finished and cannot be used to upload more data.");
        }
    }
}
