package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IShareCollectionRequestBuilder;
import com.onedrive.sdk.extensions.Share;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseShareCollectionPage extends BaseCollectionPage<Share, IShareCollectionRequestBuilder> implements IBaseShareCollectionPage {
    public BaseShareCollectionPage(BaseShareCollectionResponse baseShareCollectionResponse, IShareCollectionRequestBuilder iShareCollectionRequestBuilder) {
        super(baseShareCollectionResponse.value, iShareCollectionRequestBuilder);
    }
}
