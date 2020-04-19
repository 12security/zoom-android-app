package org.apache.http.p020io;

/* renamed from: org.apache.http.io.HttpTransportMetrics */
public interface HttpTransportMetrics {
    long getBytesTransferred();

    void reset();
}
