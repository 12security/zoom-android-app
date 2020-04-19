package com.google.api.client.util;

import java.io.IOException;

@Beta
public final class BackOffUtils {
    public static boolean next(Sleeper sleeper, BackOff backOff) throws InterruptedException, IOException {
        long nextBackOffMillis = backOff.nextBackOffMillis();
        if (nextBackOffMillis == -1) {
            return false;
        }
        sleeper.sleep(nextBackOffMillis);
        return true;
    }

    private BackOffUtils() {
    }
}
