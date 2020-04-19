package com.dropbox.core;

import java.util.concurrent.TimeUnit;

public class RetryException extends DbxException {
    private static final long serialVersionUID = 0;
    private final long backoffMillis;

    public RetryException(String str, String str2) {
        this(str, str2, 0, TimeUnit.MILLISECONDS);
    }

    public RetryException(String str, String str2, long j, TimeUnit timeUnit) {
        super(str, str2);
        this.backoffMillis = timeUnit.toMillis(j);
    }

    public long getBackoffMillis() {
        return this.backoffMillis;
    }
}
