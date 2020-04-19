package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;

public class BoxApi {
    protected String mBaseUploadUri = BoxConstants.BASE_UPLOAD_URI;
    protected String mBaseUri = BoxConstants.BASE_URI;
    protected BoxSession mSession;

    public BoxApi(BoxSession boxSession) {
        this.mSession = boxSession;
    }

    /* access modifiers changed from: protected */
    public String getBaseUri() {
        BoxSession boxSession = this.mSession;
        if (boxSession == null || boxSession.getAuthInfo() == null || this.mSession.getAuthInfo().getBaseDomain() == null) {
            return this.mBaseUri;
        }
        return String.format(BoxConstants.BASE_URI_TEMPLATE, new Object[]{this.mSession.getAuthInfo().getBaseDomain()});
    }

    /* access modifiers changed from: protected */
    public String getBaseUploadUri() {
        BoxSession boxSession = this.mSession;
        if (boxSession == null || boxSession.getAuthInfo() == null || this.mSession.getAuthInfo().getBaseDomain() == null) {
            return this.mBaseUploadUri;
        }
        return String.format(BoxConstants.BASE_UPLOAD_URI_TEMPLATE, new Object[]{this.mSession.getAuthInfo().getBaseDomain()});
    }
}
