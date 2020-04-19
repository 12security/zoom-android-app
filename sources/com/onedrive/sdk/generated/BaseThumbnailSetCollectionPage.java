package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequestBuilder;
import com.onedrive.sdk.extensions.ThumbnailSet;
import com.onedrive.sdk.http.BaseCollectionPage;

public class BaseThumbnailSetCollectionPage extends BaseCollectionPage<ThumbnailSet, IThumbnailSetCollectionRequestBuilder> implements IBaseThumbnailSetCollectionPage {
    public BaseThumbnailSetCollectionPage(BaseThumbnailSetCollectionResponse baseThumbnailSetCollectionResponse, IThumbnailSetCollectionRequestBuilder iThumbnailSetCollectionRequestBuilder) {
        super(baseThumbnailSetCollectionResponse.value, iThumbnailSetCollectionRequestBuilder);
    }
}
