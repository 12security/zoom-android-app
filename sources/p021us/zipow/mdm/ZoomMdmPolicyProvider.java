package p021us.zipow.mdm;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

/* renamed from: us.zipow.mdm.ZoomMdmPolicyProvider */
public class ZoomMdmPolicyProvider {
    private static final String TAG = "us.zipow.mdm.ZoomMdmPolicyProvider";
    private long mNativeHandle = 0;

    private native long getAllSourcesImpl(long j, int i);

    private native boolean hasPolicyBySourceImpl(long j, int i, int i2);

    private native boolean hasPolicyImpl(long j, int i);

    private native boolean isPolicyLockedImpl(long j, int i);

    private native boolean onPolicyUpdatedImpl(long j);

    private native boolean queryBooleanPolicyBySourceImpl(long j, int i, int i2);

    private native boolean queryBooleanPolicyImpl(long j, int i);

    private native int queryIntPolicyBySourceImpl(long j, int i, int i2);

    private native int queryIntPolicyImpl(long j, int i);

    @Nullable
    private native String queryStringPolicyBySourceImpl(long j, int i, int i2);

    @Nullable
    private native String queryStringPolicyImpl(long j, int i);

    public ZoomMdmPolicyProvider(long j) {
        this.mNativeHandle = j;
    }

    public boolean isLockPolicy(int i) {
        return hasPolicy(i) && isPolicyLocked(i);
    }

    @RequiresApi(api = 21)
    public boolean onPolicyUpdated() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return onPolicyUpdatedImpl(j);
    }

    public boolean hasPolicy(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return hasPolicyImpl(j, i);
    }

    public boolean isPolicyLocked(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return isPolicyLockedImpl(j, i);
    }

    public boolean queryBooleanPolicy(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return queryBooleanPolicyImpl(j, i);
    }

    public boolean queryBooleanPolicy(int i, int i2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return false;
        }
        return queryBooleanPolicyBySourceImpl(j, i, i2);
    }

    public int queryIntPolicy(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return queryIntPolicyImpl(j, i);
    }

    public int queryIntPolicy(int i, int i2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return -1;
        }
        return queryIntPolicyBySourceImpl(j, i, i2);
    }

    @Nullable
    public String queryStringPolicy(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return queryStringPolicyImpl(j, i);
    }

    @Nullable
    public String queryStringPolicy(int i, int i2) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        return queryStringPolicyBySourceImpl(j, i, i2);
    }

    public long getAllSources(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getAllSourcesImpl(j, i);
    }
}
