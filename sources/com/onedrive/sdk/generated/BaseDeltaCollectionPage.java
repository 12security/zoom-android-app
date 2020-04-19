package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IDeltaRequestBuilder;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseDeltaCollectionPage extends BaseCollectionPage<Item, IDeltaRequestBuilder> implements IBaseDeltaCollectionPage {
    public BaseDeltaCollectionPage(BaseDeltaCollectionResponse baseDeltaCollectionResponse, IDeltaRequestBuilder iDeltaRequestBuilder) {
        super(baseDeltaCollectionResponse.value, iDeltaRequestBuilder);
    }
}
