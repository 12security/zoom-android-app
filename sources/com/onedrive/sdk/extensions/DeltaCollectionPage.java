package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseDeltaCollectionPage;
import com.onedrive.sdk.generated.BaseDeltaCollectionResponse;

public class DeltaCollectionPage extends BaseDeltaCollectionPage implements IDeltaCollectionPage {
    public DeltaCollectionPage(BaseDeltaCollectionResponse baseDeltaCollectionResponse, IDeltaRequestBuilder iDeltaRequestBuilder) {
        super(baseDeltaCollectionResponse, iDeltaRequestBuilder);
    }
}
