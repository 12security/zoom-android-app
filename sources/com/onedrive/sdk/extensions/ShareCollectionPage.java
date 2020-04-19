package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseShareCollectionPage;
import com.onedrive.sdk.generated.BaseShareCollectionResponse;

public class ShareCollectionPage extends BaseShareCollectionPage implements IShareCollectionPage {
    public ShareCollectionPage(BaseShareCollectionResponse baseShareCollectionResponse, IShareCollectionRequestBuilder iShareCollectionRequestBuilder) {
        super(baseShareCollectionResponse, iShareCollectionRequestBuilder);
    }
}
