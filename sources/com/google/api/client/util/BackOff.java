package com.google.api.client.util;

import java.io.IOException;

public interface BackOff {
    public static final long STOP = -1;
    public static final BackOff STOP_BACKOFF = new BackOff() {
        public long nextBackOffMillis() throws IOException {
            return -1;
        }

        public void reset() throws IOException {
        }
    };
    public static final BackOff ZERO_BACKOFF = new BackOff() {
        public long nextBackOffMillis() throws IOException {
            return 0;
        }

        public void reset() throws IOException {
        }
    };

    long nextBackOffMillis() throws IOException;

    void reset() throws IOException;
}
