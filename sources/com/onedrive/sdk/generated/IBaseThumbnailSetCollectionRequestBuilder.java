package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IThumbnailSetCollectionRequest;
import com.onedrive.sdk.extensions.IThumbnailSetRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseThumbnailSetCollectionRequestBuilder extends IRequestBuilder {
    IThumbnailSetCollectionRequest buildRequest();

    IThumbnailSetCollectionRequest buildRequest(List<Option> list);

    IThumbnailSetRequestBuilder byId(String str);
}
