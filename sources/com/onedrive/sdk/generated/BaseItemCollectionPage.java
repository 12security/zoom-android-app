package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IItemCollectionRequestBuilder;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseItemCollectionPage extends BaseCollectionPage<Item, IItemCollectionRequestBuilder> implements IBaseItemCollectionPage {
    public BaseItemCollectionPage(BaseItemCollectionResponse baseItemCollectionResponse, IItemCollectionRequestBuilder iItemCollectionRequestBuilder) {
        super(baseItemCollectionResponse.value, iItemCollectionRequestBuilder);
    }
}
