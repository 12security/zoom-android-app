package com.zipow.videobox.sdk;

import androidx.annotation.Nullable;

public abstract class PromptProxyServerTask {
    @Nullable
    private String mName = null;

    public abstract void run();

    public PromptProxyServerTask(@Nullable String str) {
        this.mName = str;
    }

    @Nullable
    public String getName() {
        return this.mName;
    }
}
