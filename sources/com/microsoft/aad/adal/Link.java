package com.microsoft.aad.adal;

import com.google.gson.annotations.SerializedName;

final class Link {
    @SerializedName("href")
    private String mHref;
    @SerializedName("rel")
    private String mRel;

    Link() {
    }

    /* access modifiers changed from: 0000 */
    public String getRel() {
        return this.mRel;
    }

    /* access modifiers changed from: 0000 */
    public void setRel(String str) {
        this.mRel = str;
    }

    /* access modifiers changed from: 0000 */
    public String getHref() {
        return this.mHref;
    }

    /* access modifiers changed from: 0000 */
    public void setHref(String str) {
        this.mHref = str;
    }
}
