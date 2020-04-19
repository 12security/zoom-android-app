package com.microsoft.aad.adal;

import java.util.ArrayList;
import java.util.List;

final class InstanceDiscoveryMetadata {
    private final List<String> mAliases = new ArrayList();
    private final boolean mIsValidated;
    private final String mPreferredCache;
    private final String mPreferredNetwork;

    InstanceDiscoveryMetadata(boolean z) {
        this.mIsValidated = z;
        this.mPreferredNetwork = null;
        this.mPreferredCache = null;
    }

    InstanceDiscoveryMetadata(String str, String str2, List<String> list) {
        this.mPreferredNetwork = str;
        this.mPreferredCache = str2;
        this.mAliases.addAll(list);
        this.mIsValidated = true;
    }

    InstanceDiscoveryMetadata(String str, String str2) {
        this.mPreferredNetwork = str;
        this.mPreferredCache = str2;
        this.mIsValidated = true;
    }

    /* access modifiers changed from: 0000 */
    public String getPreferredNetwork() {
        return this.mPreferredNetwork;
    }

    /* access modifiers changed from: 0000 */
    public String getPreferredCache() {
        return this.mPreferredCache;
    }

    /* access modifiers changed from: 0000 */
    public List<String> getAliases() {
        return this.mAliases;
    }

    /* access modifiers changed from: 0000 */
    public boolean isValidated() {
        return this.mIsValidated;
    }
}
