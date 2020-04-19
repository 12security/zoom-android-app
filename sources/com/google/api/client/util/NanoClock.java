package com.google.api.client.util;

public interface NanoClock {
    public static final NanoClock SYSTEM = new NanoClock() {
        public long nanoTime() {
            return System.nanoTime();
        }
    };

    long nanoTime();
}
