package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsCollections.GetCollectionItems;
import com.box.androidsdk.content.requests.BoxRequestsCollections.GetCollections;

public class BoxApiCollection extends BoxApi {
    public BoxApiCollection(BoxSession boxSession) {
        super(boxSession);
    }

    /* access modifiers changed from: protected */
    public String getCollectionsUrl() {
        return String.format("%s/collections", new Object[]{getBaseUri()});
    }

    /* access modifiers changed from: protected */
    public String getCollectionItemsUrl(String str) {
        return String.format("%s/%s/items", new Object[]{getCollectionsUrl(), str});
    }

    public GetCollections getCollectionsRequest() {
        return new GetCollections(getCollectionsUrl(), this.mSession);
    }

    public GetCollectionItems getItemsRequest(String str) {
        return new GetCollectionItems(getCollectionItemsUrl(str), this.mSession);
    }
}
