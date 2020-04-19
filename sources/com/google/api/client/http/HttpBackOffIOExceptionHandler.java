package com.google.api.client.http;

import com.google.api.client.util.BackOff;
import com.google.api.client.util.BackOffUtils;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import java.io.IOException;

@Beta
public class HttpBackOffIOExceptionHandler implements HttpIOExceptionHandler {
    private final BackOff backOff;
    private Sleeper sleeper = Sleeper.DEFAULT;

    public HttpBackOffIOExceptionHandler(BackOff backOff2) {
        this.backOff = (BackOff) Preconditions.checkNotNull(backOff2);
    }

    public final BackOff getBackOff() {
        return this.backOff;
    }

    public final Sleeper getSleeper() {
        return this.sleeper;
    }

    public HttpBackOffIOExceptionHandler setSleeper(Sleeper sleeper2) {
        this.sleeper = (Sleeper) Preconditions.checkNotNull(sleeper2);
        return this;
    }

    public boolean handleIOException(HttpRequest httpRequest, boolean z) throws IOException {
        if (!z) {
            return false;
        }
        try {
            return BackOffUtils.next(this.sleeper, this.backOff);
        } catch (InterruptedException unused) {
            return false;
        }
    }
}
