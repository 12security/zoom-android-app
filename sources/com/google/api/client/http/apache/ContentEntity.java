package com.google.api.client.http.apache;

import com.google.api.client.util.Preconditions;
import com.google.api.client.util.StreamingContent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;

final class ContentEntity extends AbstractHttpEntity {
    private final long contentLength;
    private final StreamingContent streamingContent;

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return true;
    }

    ContentEntity(long j, StreamingContent streamingContent2) {
        this.contentLength = j;
        this.streamingContent = (StreamingContent) Preconditions.checkNotNull(streamingContent2);
    }

    public InputStream getContent() {
        throw new UnsupportedOperationException();
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        if (this.contentLength != 0) {
            this.streamingContent.writeTo(outputStream);
        }
    }
}
