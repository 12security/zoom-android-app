package com.dropbox.core;

import java.util.concurrent.TimeUnit;

public class RateLimitException extends RetryException {
    private static final long serialVersionUID = 0;

    public RateLimitException(String str, String str2, long j, TimeUnit timeUnit) {
        super(str, str2, j, timeUnit);
    }
}
