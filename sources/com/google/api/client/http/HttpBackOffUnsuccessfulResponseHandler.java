package com.google.api.client.http;

import com.google.api.client.util.BackOff;
import com.google.api.client.util.BackOffUtils;
import com.google.api.client.util.Beta;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.Sleeper;
import java.io.IOException;

@Beta
public class HttpBackOffUnsuccessfulResponseHandler implements HttpUnsuccessfulResponseHandler {
    private final BackOff backOff;
    private BackOffRequired backOffRequired = BackOffRequired.ON_SERVER_ERROR;
    private Sleeper sleeper = Sleeper.DEFAULT;

    @Beta
    public interface BackOffRequired {
        public static final BackOffRequired ALWAYS = new BackOffRequired() {
            public boolean isRequired(HttpResponse httpResponse) {
                return true;
            }
        };
        public static final BackOffRequired ON_SERVER_ERROR = new BackOffRequired() {
            public boolean isRequired(HttpResponse httpResponse) {
                return httpResponse.getStatusCode() / 100 == 5;
            }
        };

        boolean isRequired(HttpResponse httpResponse);
    }

    public HttpBackOffUnsuccessfulResponseHandler(BackOff backOff2) {
        this.backOff = (BackOff) Preconditions.checkNotNull(backOff2);
    }

    public final BackOff getBackOff() {
        return this.backOff;
    }

    public final BackOffRequired getBackOffRequired() {
        return this.backOffRequired;
    }

    public HttpBackOffUnsuccessfulResponseHandler setBackOffRequired(BackOffRequired backOffRequired2) {
        this.backOffRequired = (BackOffRequired) Preconditions.checkNotNull(backOffRequired2);
        return this;
    }

    public final Sleeper getSleeper() {
        return this.sleeper;
    }

    public HttpBackOffUnsuccessfulResponseHandler setSleeper(Sleeper sleeper2) {
        this.sleeper = (Sleeper) Preconditions.checkNotNull(sleeper2);
        return this;
    }

    public final boolean handleResponse(HttpRequest httpRequest, HttpResponse httpResponse, boolean z) throws IOException {
        if (z && this.backOffRequired.isRequired(httpResponse)) {
            try {
                return BackOffUtils.next(this.sleeper, this.backOff);
            } catch (InterruptedException unused) {
            }
        }
        return false;
    }
}
