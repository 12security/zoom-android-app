package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseSearchRequestBuilder;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class SearchRequestBuilder extends BaseSearchRequestBuilder implements ISearchRequestBuilder {
    public SearchRequestBuilder(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, str2);
    }
}
