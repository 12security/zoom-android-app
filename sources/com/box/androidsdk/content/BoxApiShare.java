package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxSharedLinkSession;
import com.box.androidsdk.content.requests.BoxRequestsShare.GetSharedLink;

public class BoxApiShare extends BoxApi {
    /* access modifiers changed from: protected */
    public String getSharedItemsUrl() {
        return String.format("%s/shared_items", new Object[]{getBaseUri()});
    }

    public BoxApiShare(BoxSession boxSession) {
        super(boxSession);
    }

    public GetSharedLink getSharedLinkRequest(String str) {
        BoxSharedLinkSession boxSharedLinkSession = new BoxSharedLinkSession(str, this.mSession);
        boxSharedLinkSession.setSharedLink(str);
        return new GetSharedLink(getSharedItemsUrl(), boxSharedLinkSession);
    }
}
