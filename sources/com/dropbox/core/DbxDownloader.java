package com.dropbox.core;

import com.dropbox.core.util.IOUtil;
import com.dropbox.core.util.IOUtil.ProgressListener;
import com.dropbox.core.util.IOUtil.WriteException;
import com.dropbox.core.util.ProgressOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbxDownloader<R> implements Closeable {
    private final InputStream body;
    private boolean closed = false;
    private final R result;

    public DbxDownloader(R r, InputStream inputStream) {
        this.result = r;
        this.body = inputStream;
    }

    public R getResult() {
        return this.result;
    }

    public InputStream getInputStream() {
        assertOpen();
        return this.body;
    }

    public R download(OutputStream outputStream) throws DbxException, IOException {
        try {
            IOUtil.copyStreamToStream(getInputStream(), outputStream);
            close();
            return this.result;
        } catch (WriteException e) {
            throw e.getCause();
        } catch (IOException e2) {
            throw new NetworkIOException(e2);
        } catch (Throwable th) {
            close();
            throw th;
        }
    }

    public R download(OutputStream outputStream, ProgressListener progressListener) throws DbxException, IOException {
        return download(new ProgressOutputStream(outputStream, progressListener));
    }

    public void close() {
        if (!this.closed) {
            IOUtil.closeQuietly(this.body);
            this.closed = true;
        }
    }

    private void assertOpen() {
        if (this.closed) {
            throw new IllegalStateException("This downloader is already closed.");
        }
    }
}
