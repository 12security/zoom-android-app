package com.google.api.services.drive;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.googleapis.services.json.CommonGoogleJsonClientRequestInitializer;
import java.io.IOException;

public class DriveRequestInitializer extends CommonGoogleJsonClientRequestInitializer {
    /* access modifiers changed from: protected */
    public void initializeDriveRequest(DriveRequest<?> driveRequest) throws IOException {
    }

    public DriveRequestInitializer() {
    }

    public DriveRequestInitializer(String str) {
        super(str);
    }

    public DriveRequestInitializer(String str, String str2) {
        super(str, str2);
    }

    public final void initializeJsonRequest(AbstractGoogleJsonClientRequest<?> abstractGoogleJsonClientRequest) throws IOException {
        super.initializeJsonRequest(abstractGoogleJsonClientRequest);
        initializeDriveRequest((DriveRequest) abstractGoogleJsonClientRequest);
    }
}
