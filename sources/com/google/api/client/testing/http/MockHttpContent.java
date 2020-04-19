package com.google.api.client.testing.http;

import com.google.api.client.http.HttpContent;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

@Beta
public class MockHttpContent implements HttpContent {
    private byte[] content = new byte[0];
    private long length = -1;
    private String type;

    public boolean retrySupported() {
        return true;
    }

    public long getLength() throws IOException {
        return this.length;
    }

    public String getType() {
        return this.type;
    }

    public void writeTo(OutputStream outputStream) throws IOException {
        outputStream.write(this.content);
        outputStream.flush();
    }

    public final byte[] getContent() {
        return this.content;
    }

    public MockHttpContent setContent(byte[] bArr) {
        this.content = (byte[]) Preconditions.checkNotNull(bArr);
        return this;
    }

    public MockHttpContent setLength(long j) {
        Preconditions.checkArgument(j >= -1);
        this.length = j;
        return this;
    }

    public MockHttpContent setType(String str) {
        this.type = str;
        return this;
    }
}
