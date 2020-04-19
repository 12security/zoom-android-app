package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxJsonObject;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.ContentTypes;
import java.util.Locale;

public abstract class BoxRequestItem<E extends BoxJsonObject, R extends BoxRequest<E, R>> extends BoxRequest<E, R> {
    private static String QUERY_FIELDS = "fields";
    protected String mId;

    public BoxRequestItem(Class<E> cls, String str, String str2, BoxSession boxSession) {
        super(cls, str2, boxSession);
        this.mId = null;
        this.mContentType = ContentTypes.JSON;
        this.mId = str;
    }

    protected BoxRequestItem(BoxRequestItem boxRequestItem) {
        super(boxRequestItem);
        this.mId = null;
    }

    public R setFields(String... strArr) {
        if (strArr.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(strArr[0]);
            for (int i = 1; i < strArr.length; i++) {
                sb.append(String.format(Locale.ENGLISH, ",%s", new Object[]{strArr[i]}));
            }
            this.mQueryMap.put(QUERY_FIELDS, sb.toString());
        }
        return this;
    }

    public String getId() {
        return this.mId;
    }
}
