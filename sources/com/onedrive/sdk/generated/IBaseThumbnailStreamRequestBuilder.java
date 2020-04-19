package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IThumbnailStreamRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseThumbnailStreamRequestBuilder extends IRequestBuilder {
    IThumbnailStreamRequest buildRequest();

    IThumbnailStreamRequest buildRequest(List<Option> list);
}
