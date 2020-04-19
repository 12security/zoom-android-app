package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseItemCollectionPage;
import com.onedrive.sdk.generated.BaseItemCollectionResponse;

public class ItemCollectionPage extends BaseItemCollectionPage implements IItemCollectionPage {
    public ItemCollectionPage(BaseItemCollectionResponse baseItemCollectionResponse, IItemCollectionRequestBuilder iItemCollectionRequestBuilder) {
        super(baseItemCollectionResponse, iItemCollectionRequestBuilder);
    }
}
