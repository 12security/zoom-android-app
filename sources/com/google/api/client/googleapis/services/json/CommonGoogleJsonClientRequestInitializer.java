package com.google.api.client.googleapis.services.json;

import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.CommonGoogleClientRequestInitializer;
import java.io.IOException;

public class CommonGoogleJsonClientRequestInitializer extends CommonGoogleClientRequestInitializer {
    /* access modifiers changed from: protected */
    public void initializeJsonRequest(AbstractGoogleJsonClientRequest<?> abstractGoogleJsonClientRequest) throws IOException {
    }

    public CommonGoogleJsonClientRequestInitializer() {
    }

    public CommonGoogleJsonClientRequestInitializer(String str) {
        super(str);
    }

    public CommonGoogleJsonClientRequestInitializer(String str, String str2) {
        super(str, str2);
    }

    public final void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
        super.initialize(abstractGoogleClientRequest);
        initializeJsonRequest((AbstractGoogleJsonClientRequest) abstractGoogleClientRequest);
    }
}
