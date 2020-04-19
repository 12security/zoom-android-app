package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IThumbnailSetRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseThumbnailSetRequestBuilder extends IRequestBuilder {
    IThumbnailSetRequest buildRequest();

    IThumbnailSetRequest buildRequest(List<Option> list);
}
