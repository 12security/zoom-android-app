package com.zipow.videobox.ptapp.p013mm;

import androidx.annotation.Nullable;

/* renamed from: com.zipow.videobox.ptapp.mm.ZoomBuddySearchData */
public class ZoomBuddySearchData {
    private long mNativeHandle = 0;

    /* renamed from: com.zipow.videobox.ptapp.mm.ZoomBuddySearchData$SearchKey */
    public static class SearchKey {
        private boolean isSearchByEmail;
        private String key;

        public SearchKey(String str, boolean z) {
            this.key = str;
            this.isSearchByEmail = z;
        }

        public boolean isSearchByEmail() {
            return this.isSearchByEmail;
        }

        public String getKey() {
            return this.key;
        }
    }

    private native long getBuddyAtImpl(long j, int i);

    private native long getBuddyByJIDImpl(long j, String str);

    private native int getBuddyCountImpl(long j);

    @Nullable
    private native String getSearchKeyImpl(long j, boolean[] zArr);

    private native void invalidateDataImpl(long j);

    public ZoomBuddySearchData(long j) {
        this.mNativeHandle = j;
    }

    @Nullable
    public SearchKey getSearchKey() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        boolean[] zArr = {false};
        String searchKeyImpl = getSearchKeyImpl(j, zArr);
        if (searchKeyImpl == null) {
            return null;
        }
        return new SearchKey(searchKeyImpl, zArr[0]);
    }

    public int getBuddyCount() {
        long j = this.mNativeHandle;
        if (j == 0) {
            return 0;
        }
        return getBuddyCountImpl(j);
    }

    public void invalidateData() {
        long j = this.mNativeHandle;
        if (j != 0) {
            invalidateDataImpl(j);
        }
    }

    @Nullable
    public ZoomBuddy getBuddyAt(int i) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyAtImpl = getBuddyAtImpl(j, i);
        if (buddyAtImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyAtImpl);
    }

    @Nullable
    public ZoomBuddy getBuddyByJID(String str) {
        long j = this.mNativeHandle;
        if (j == 0) {
            return null;
        }
        long buddyByJIDImpl = getBuddyByJIDImpl(j, str);
        if (buddyByJIDImpl == 0) {
            return null;
        }
        return new ZoomBuddy(buddyByJIDImpl);
    }
}
