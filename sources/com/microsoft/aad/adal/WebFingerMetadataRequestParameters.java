package com.microsoft.aad.adal;

import java.net.URL;

final class WebFingerMetadataRequestParameters {
    private final URL mDomain;
    private final DRSMetadata mMetadata;

    WebFingerMetadataRequestParameters(URL url, DRSMetadata dRSMetadata) {
        this.mDomain = url;
        this.mMetadata = dRSMetadata;
    }

    /* access modifiers changed from: 0000 */
    public URL getDomain() {
        return this.mDomain;
    }

    /* access modifiers changed from: 0000 */
    public DRSMetadata getDrsMetadata() {
        return this.mMetadata;
    }
}
