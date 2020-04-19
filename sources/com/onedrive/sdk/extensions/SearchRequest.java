package com.onedrive.sdk.extensions;

import com.onedrive.sdk.generated.BaseSearchRequest;
import com.onedrive.sdk.options.Option;
import java.util.List;

public class SearchRequest extends BaseSearchRequest implements ISearchRequest {
    public SearchRequest(String str, IOneDriveClient iOneDriveClient, List<Option> list, String str2) {
        super(str, iOneDriveClient, list, str2);
    }
}
