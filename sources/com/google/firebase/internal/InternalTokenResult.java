package com.google.firebase.internal;

import androidx.annotation.Nullable;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.internal.Objects;

@KeepForSdk
/* compiled from: com.google.firebase:firebase-common@@16.1.0 */
public class InternalTokenResult {
    private String token;

    @KeepForSdk
    public InternalTokenResult(@Nullable String str) {
        this.token = str;
    }

    @KeepForSdk
    @Nullable
    public String getToken() {
        return this.token;
    }

    public int hashCode() {
        return Objects.hashCode(this.token);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof InternalTokenResult)) {
            return false;
        }
        return Objects.equal(this.token, ((InternalTokenResult) obj).token);
    }

    public String toString() {
        return Objects.toStringHelper(this).add("token", this.token).toString();
    }
}
