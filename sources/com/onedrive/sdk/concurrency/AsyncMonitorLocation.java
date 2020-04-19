package com.onedrive.sdk.concurrency;

public class AsyncMonitorLocation {
    private final String mMonitorLocation;

    public AsyncMonitorLocation(String str) {
        this.mMonitorLocation = str;
    }

    public String getLocation() {
        return this.mMonitorLocation;
    }
}
