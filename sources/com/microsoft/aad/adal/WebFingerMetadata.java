package com.microsoft.aad.adal;

import com.google.gson.annotations.SerializedName;
import java.util.List;

final class WebFingerMetadata {
    @SerializedName("links")
    private List<Link> mLinks;
    @SerializedName("subject")
    private String mSubject;

    WebFingerMetadata() {
    }

    /* access modifiers changed from: 0000 */
    public String getSubject() {
        return this.mSubject;
    }

    /* access modifiers changed from: 0000 */
    public void setSubject(String str) {
        this.mSubject = str;
    }

    /* access modifiers changed from: 0000 */
    public List<Link> getLinks() {
        return this.mLinks;
    }

    /* access modifiers changed from: 0000 */
    public void setLinks(List<Link> list) {
        this.mLinks = list;
    }
}
