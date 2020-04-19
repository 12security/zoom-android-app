package com.onedrive.sdk.http;

import com.onedrive.sdk.options.HeaderOption;
import com.onedrive.sdk.options.Option;
import java.net.URL;
import java.util.List;

public interface IHttpRequest {
    void addHeader(String str, String str2);

    List<HeaderOption> getHeaders();

    HttpMethod getHttpMethod();

    List<Option> getOptions();

    URL getRequestUrl();
}
