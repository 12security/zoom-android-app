package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;
import java.util.List;

/* renamed from: com.zipow.videobox.ptapp.mm.ICloudSIPCallNumber */
public class ICloudSIPCallNumber {
    private long mNativeHandler;

    @Nullable
    private native String getCompanyNumberImpl(long j);

    @Nullable
    private native List<String> getDirectNumberImpl(long j);

    @Nullable
    private native String getExtensionImpl(long j);

    @Nullable
    private native List<String> getFormattedDirectNumberImpl(long j);

    private native boolean isSameCompanyImpl(long j, String str);

    public ICloudSIPCallNumber(long j) {
        this.mNativeHandler = j;
    }

    @Nullable
    public List<String> getDirectNumber() {
        long j = this.mNativeHandler;
        if (j == 0) {
            return null;
        }
        return getDirectNumberImpl(j);
    }

    @Nullable
    public List<String> getFormattedDirectNumber() {
        long j = this.mNativeHandler;
        if (j == 0) {
            return null;
        }
        return getFormattedDirectNumberImpl(j);
    }

    @Nullable
    public String getExtension() {
        long j = this.mNativeHandler;
        if (j == 0) {
            return null;
        }
        return getExtensionImpl(j);
    }

    @Nullable
    public String getCompanyNumber() {
        long j = this.mNativeHandler;
        if (j == 0) {
            return null;
        }
        return getCompanyNumberImpl(j);
    }

    public boolean isSameCompany(String str) {
        long j = this.mNativeHandler;
        if (j == 0) {
            return false;
        }
        return isSameCompanyImpl(j, str);
    }
}
