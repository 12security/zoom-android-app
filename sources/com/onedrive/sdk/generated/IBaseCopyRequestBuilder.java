package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ICopyRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseCopyRequestBuilder extends IRequestBuilder {
    ICopyRequest buildRequest();

    ICopyRequest buildRequest(List<Option> list);
}
