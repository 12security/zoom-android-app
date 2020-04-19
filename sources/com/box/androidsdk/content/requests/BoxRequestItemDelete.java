package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxVoid;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.Methods;

abstract class BoxRequestItemDelete<R extends BoxRequest<BoxVoid, R>> extends BoxRequest<BoxVoid, R> {
    protected String mId;

    public BoxRequestItemDelete(String str, String str2, BoxSession boxSession) {
        super(BoxVoid.class, str2, boxSession);
        this.mId = str;
        this.mRequestMethod = Methods.DELETE;
    }

    public String getId() {
        return this.mId;
    }

    public R setIfMatchEtag(String str) {
        return super.setIfMatchEtag(str);
    }

    public String getIfMatchEtag() {
        return super.getIfMatchEtag();
    }
}
