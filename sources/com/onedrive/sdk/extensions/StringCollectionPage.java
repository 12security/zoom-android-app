package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseStringCollectionPage;
import com.onedrive.sdk.generated.BaseStringCollectionResponse;

public class StringCollectionPage extends BaseStringCollectionPage implements IStringCollectionPage {
    public StringCollectionPage(BaseStringCollectionResponse baseStringCollectionResponse, IStringCollectionRequestBuilder iStringCollectionRequestBuilder) {
        super(baseStringCollectionResponse, iStringCollectionRequestBuilder);
    }
}
