package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IStringCollectionRequestBuilder;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseStringCollectionPage extends BaseCollectionPage<String, IStringCollectionRequestBuilder> implements IBaseStringCollectionPage {
    public BaseStringCollectionPage(BaseStringCollectionResponse baseStringCollectionResponse, IStringCollectionRequestBuilder iStringCollectionRequestBuilder) {
        super(baseStringCollectionResponse.value, iStringCollectionRequestBuilder);
    }
}
