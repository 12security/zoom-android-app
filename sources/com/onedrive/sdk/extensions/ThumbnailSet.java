package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseThumbnailSet;

public class ThumbnailSet extends BaseThumbnailSet {
    public Thumbnail getCustomThumbnail(String str) {
        if (getRawObject().has(str)) {
            return (Thumbnail) getSerializer().deserializeObject(getRawObject().get(str).toString(), Thumbnail.class);
        }
        return null;
    }
}
