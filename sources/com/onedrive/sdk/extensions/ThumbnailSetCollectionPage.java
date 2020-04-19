package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailSetCollectionPage;
import com.onedrive.sdk.generated.BaseThumbnailSetCollectionResponse;

public class ThumbnailSetCollectionPage extends BaseThumbnailSetCollectionPage implements IThumbnailSetCollectionPage {
    public ThumbnailSetCollectionPage(BaseThumbnailSetCollectionResponse baseThumbnailSetCollectionResponse, IThumbnailSetCollectionRequestBuilder iThumbnailSetCollectionRequestBuilder) {
        super(baseThumbnailSetCollectionResponse, iThumbnailSetCollectionRequestBuilder);
    }
}
