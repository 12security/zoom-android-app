package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxComment;
import com.box.androidsdk.content.models.BoxEntity;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import java.util.LinkedHashMap;

abstract class BoxRequestCommentAdd<E extends BoxComment, R extends BoxRequest<E, R>> extends BoxRequestItem<E, R> {
    public BoxRequestCommentAdd(Class<E> cls, String str, BoxSession boxSession) {
        super(cls, null, str, boxSession);
        this.mRequestMethod = Methods.POST;
    }

    public String getMessage() {
        return (String) this.mBodyMap.get("message");
    }

    public R setMessage(String str) {
        this.mBodyMap.put("message", str);
        return this;
    }

    public String getItemId() {
        if (this.mBodyMap.containsKey("item")) {
            return (String) this.mBodyMap.get("id");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public R setItemId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.mBodyMap.containsKey("item")) {
            linkedHashMap = new LinkedHashMap(((BoxEntity) this.mBodyMap.get("item")).getPropertiesAsHashMap());
        }
        linkedHashMap.put("id", str);
        this.mBodyMap.put("item", new BoxEntity(linkedHashMap));
        return this;
    }

    public String getItemType() {
        if (this.mBodyMap.containsKey("item")) {
            return (String) this.mBodyMap.get("type");
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public R setItemType(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.mBodyMap.containsKey("item")) {
            linkedHashMap = new LinkedHashMap(((BoxEntity) this.mBodyMap.get("item")).getPropertiesAsHashMap());
        }
        linkedHashMap.put("type", str);
        this.mBodyMap.put("item", new BoxEntity(linkedHashMap));
        return this;
    }
}
