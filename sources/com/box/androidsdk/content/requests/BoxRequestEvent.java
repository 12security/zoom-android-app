package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.BoxException;
import com.box.androidsdk.content.models.BoxJsonObject;
import com.box.androidsdk.content.models.BoxListEvents;
import com.box.androidsdk.content.models.BoxObject;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.BoxRequestHandler;
import com.box.androidsdk.content.requests.BoxRequest.ContentTypes;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.IStreamPosition;
import java.util.Collection;

abstract class BoxRequestEvent<E extends BoxJsonObject, R extends BoxRequest<E, R>> extends BoxRequest<E, R> {
    public static final String FIELD_LIMIT = "stream_limit";
    public static final String FIELD_STREAM_POSITION = "stream_position";
    public static final String FIELD_STREAM_TYPE = "stream_type";
    public static final String STREAM_TYPE_ALL = "all";
    public static final String STREAM_TYPE_CHANGES = "changes";
    public static final String STREAM_TYPE_SYNC = "sync";
    /* access modifiers changed from: private */
    public boolean mFilterDuplicates = true;
    private E mListEvents;

    public BoxRequestEvent(Class<E> cls, String str, BoxSession boxSession) {
        super(cls, str, boxSession);
        this.mRequestUrlString = str;
        this.mRequestMethod = Methods.GET;
        setRequestHandler(new BoxRequestHandler<BoxRequestEvent>(this) {
            public <T extends BoxObject> T onResponse(Class<T> cls, BoxHttpResponse boxHttpResponse) throws IllegalAccessException, InstantiationException, BoxException {
                if (boxHttpResponse.getResponseCode() == 429) {
                    return retryRateLimited(boxHttpResponse);
                }
                String contentType = boxHttpResponse.getContentType();
                T t = (BoxObject) cls.newInstance();
                if (t instanceof BoxListEvents) {
                    ((BoxListEvents) t).setFilterDuplicates(BoxRequestEvent.this.mFilterDuplicates);
                }
                if ((t instanceof BoxJsonObject) && contentType.contains(ContentTypes.JSON.toString())) {
                    String stringBody = boxHttpResponse.getStringBody();
                    stringBody.charAt(stringBody.indexOf("event") - 1);
                    stringBody.charAt(stringBody.indexOf("user") - 1);
                    ((BoxJsonObject) t).createFromJson(stringBody);
                }
                return t;
            }
        });
    }

    public R setStreamPosition(String str) {
        this.mQueryMap.put(FIELD_STREAM_POSITION, str);
        return this;
    }

    /* access modifiers changed from: protected */
    public R setStreamType(String str) {
        this.mQueryMap.put(FIELD_STREAM_TYPE, str);
        return this;
    }

    public R setLimit(int i) {
        this.mQueryMap.put(FIELD_LIMIT, Integer.toString(i));
        return this;
    }

    public R setFilterDuplicates(boolean z) {
        this.mFilterDuplicates = z;
        return this;
    }

    public R setPreviousListEvents(E e) {
        this.mListEvents = e;
        setStreamPosition(((IStreamPosition) this.mListEvents).getNextStreamPosition().toString());
        return this;
    }

    public E send() throws BoxException {
        if (this.mListEvents == null) {
            return (BoxJsonObject) super.send();
        }
        E e = (BoxJsonObject) super.send();
        ((Collection) this.mListEvents).addAll((Collection) super.send());
        Collection collection = (Collection) e;
        collection.clear();
        collection.addAll((Collection) this.mListEvents);
        return e;
    }
}
