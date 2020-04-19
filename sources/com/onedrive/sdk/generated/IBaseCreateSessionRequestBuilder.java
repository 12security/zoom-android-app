package com.onedrive.sdk.generated;

import com.onedrive.sdk.extensions.ICreateSessionRequest;
import com.onedrive.sdk.http.IRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public interface IBaseCreateSessionRequestBuilder extends IRequestBuilder {
    ICreateSessionRequest buildRequest();

    ICreateSessionRequest buildRequest(List<Option> list);
}
