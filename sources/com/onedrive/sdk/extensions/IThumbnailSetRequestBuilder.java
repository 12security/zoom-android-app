package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.IBaseThumbnailSetRequestBuilder;

public interface IThumbnailSetRequestBuilder extends IBaseThumbnailSetRequestBuilder {
    IThumbnailRequestBuilder getThumbnailSize(String str);
}
