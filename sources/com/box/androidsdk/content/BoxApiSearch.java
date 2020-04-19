package com.box.androidsdk.content;

import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequestsSearch.Search;

public class BoxApiSearch extends BoxApi {
    public BoxApiSearch(BoxSession boxSession) {
        super(boxSession);
    }

    public Search getSearchRequest(String str) {
        return new Search(str, getSearchUrl(), this.mSession);
    }

    /* access modifiers changed from: protected */
    public String getSearchUrl() {
        return String.format("%s/search", new Object[]{getBaseUri()});
    }
}
