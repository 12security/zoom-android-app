package com.microsoft.services.msa;

import android.net.Uri;
import android.text.TextUtils;

enum Config {
    INSTANCE;
    
    private Uri apiUri;
    private String apiVersion;

    public Uri getApiUri() {
        return this.apiUri;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setApiUri(Uri uri) {
        this.apiUri = uri;
    }

    public void setApiVersion(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.apiVersion = str;
            return;
        }
        throw new AssertionError();
    }
}
