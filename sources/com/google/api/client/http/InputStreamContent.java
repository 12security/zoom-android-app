package com.google.api.client.http;

import com.google.api.client.util.Preconditions;
import java.io.InputStream;

public final class InputStreamContent extends AbstractInputStreamContent {
    private final InputStream inputStream;
    private long length = -1;
    private boolean retrySupported;

    public InputStreamContent(String str, InputStream inputStream2) {
        super(str);
        this.inputStream = (InputStream) Preconditions.checkNotNull(inputStream2);
    }

    public long getLength() {
        return this.length;
    }

    public boolean retrySupported() {
        return this.retrySupported;
    }

    public InputStreamContent setRetrySupported(boolean z) {
        this.retrySupported = z;
        return this;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public InputStreamContent setType(String str) {
        return (InputStreamContent) super.setType(str);
    }

    public InputStreamContent setCloseInputStream(boolean z) {
        return (InputStreamContent) super.setCloseInputStream(z);
    }

    public InputStreamContent setLength(long j) {
        this.length = j;
        return this;
    }
}
