package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseSearchCollectionPage;
import com.onedrive.sdk.generated.BaseSearchCollectionResponse;

public class SearchCollectionPage extends BaseSearchCollectionPage implements ISearchCollectionPage {
    public SearchCollectionPage(BaseSearchCollectionResponse baseSearchCollectionResponse, ISearchRequestBuilder iSearchRequestBuilder) {
        super(baseSearchCollectionResponse, iSearchRequestBuilder);
    }
}
