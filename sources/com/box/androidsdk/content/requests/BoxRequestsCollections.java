package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxListCollections;
import com.box.androidsdk.content.models.BoxListItems;
import com.box.androidsdk.content.models.BoxSession;

public class BoxRequestsCollections {

    public static class GetCollectionItems extends BoxRequestList<BoxListItems, GetCollectionItems> {
        public GetCollectionItems(String str, BoxSession boxSession) {
            super(BoxListItems.class, null, str, boxSession);
        }
    }

    public static class GetCollections extends BoxRequestList<BoxListCollections, GetCollections> {
        public GetCollections(String str, BoxSession boxSession) {
            super(BoxListCollections.class, null, str, boxSession);
        }
    }
}
