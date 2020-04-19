package com.google.api.client.testing.util;

import com.google.api.client.util.Beta;
import com.google.api.client.util.Sleeper;

@Beta
public class MockSleeper implements Sleeper {
    private int count;
    private long lastMillis;

    public void sleep(long j) throws InterruptedException {
        this.count++;
        this.lastMillis = j;
    }

    public final int getCount() {
        return this.count;
    }

    public final long getLastMillis() {
        return this.lastMillis;
    }
}
