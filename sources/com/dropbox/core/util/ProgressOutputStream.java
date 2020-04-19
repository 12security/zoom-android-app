package com.dropbox.core.util;

import com.dropbox.core.util.IOUtil.ProgressListener;
import java.io.IOException;
import java.io.OutputStream;

public class ProgressOutputStream extends OutputStream {
    private int completed;
    private ProgressListener listener;
    private OutputStream underlying;

    public ProgressOutputStream(OutputStream outputStream) {
        this.underlying = outputStream;
        this.completed = 0;
    }

    public ProgressOutputStream(OutputStream outputStream, ProgressListener progressListener) {
        this(outputStream);
        this.listener = progressListener;
    }

    public void setListener(ProgressListener progressListener) {
        this.listener = progressListener;
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        this.underlying.write(bArr, i, i2);
        track(i2);
    }

    public void write(byte[] bArr) throws IOException {
        this.underlying.write(bArr);
        track(bArr.length);
    }

    public void write(int i) throws IOException {
        this.underlying.write(i);
        track(1);
    }

    public void flush() throws IOException {
        this.underlying.flush();
    }

    public void close() throws IOException {
        this.underlying.close();
    }

    private void track(int i) {
        this.completed += i;
        ProgressListener progressListener = this.listener;
        if (progressListener != null) {
            progressListener.onProgress((long) this.completed);
        }
    }
}
