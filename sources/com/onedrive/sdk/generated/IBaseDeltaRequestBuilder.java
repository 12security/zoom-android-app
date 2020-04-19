package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.IDeltaRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseDeltaRequestBuilder extends IRequestBuilder {
    IDeltaRequest buildRequest();

    IDeltaRequest buildRequest(List<Option> list);
}
