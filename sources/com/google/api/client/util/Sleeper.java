package com.google.api.client.util;

public interface Sleeper {
    public static final Sleeper DEFAULT = new Sleeper() {
        public void sleep(long j) throws InterruptedException {
            Thread.sleep(j);
        }
    };

    void sleep(long j) throws InterruptedException;
}
