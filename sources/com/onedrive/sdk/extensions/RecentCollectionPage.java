package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseRecentCollectionPage;
import com.onedrive.sdk.generated.BaseRecentCollectionResponse;

public class RecentCollectionPage extends BaseRecentCollectionPage implements IRecentCollectionPage {
    public RecentCollectionPage(BaseRecentCollectionResponse baseRecentCollectionResponse, IRecentRequestBuilder iRecentRequestBuilder) {
        super(baseRecentCollectionResponse, iRecentRequestBuilder);
    }
}
