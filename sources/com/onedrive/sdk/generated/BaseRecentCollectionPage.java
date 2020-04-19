package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IRecentRequestBuilder;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseRecentCollectionPage extends BaseCollectionPage<Item, IRecentRequestBuilder> implements IBaseRecentCollectionPage {
    public BaseRecentCollectionPage(BaseRecentCollectionResponse baseRecentCollectionResponse, IRecentRequestBuilder iRecentRequestBuilder) {
        super(baseRecentCollectionResponse.value, iRecentRequestBuilder);
    }
}
