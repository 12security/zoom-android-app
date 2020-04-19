package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IThumbnailRequest;
import com.onedrive.sdk.extensions.IThumbnailStreamRequestBuilder;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseThumbnailRequestBuilder extends IRequestBuilder {
    IThumbnailRequest buildRequest();

    IThumbnailRequest buildRequest(List<Option> list);

    IThumbnailStreamRequestBuilder getContent();
}
