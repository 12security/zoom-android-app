package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ISearchRequestBuilder;
import com.onedrive.sdk.extensions.Item;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseSearchCollectionPage extends BaseCollectionPage<Item, ISearchRequestBuilder> implements IBaseSearchCollectionPage {
    public BaseSearchCollectionPage(BaseSearchCollectionResponse baseSearchCollectionResponse, ISearchRequestBuilder iSearchRequestBuilder) {
        super(baseSearchCollectionResponse.value, iSearchRequestBuilder);
    }
}
