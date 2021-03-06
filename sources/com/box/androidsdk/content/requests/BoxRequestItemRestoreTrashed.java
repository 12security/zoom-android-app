package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.eclipsesource.json.JsonObject;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

abstract class BoxRequestItemRestoreTrashed<E extends BoxItem, R extends BoxRequest<E, R>> extends BoxRequestItem<E, R> {
    public BoxRequestItemRestoreTrashed(Class<E> cls, String str, String str2, BoxSession boxSession) {
        super(cls, str, str2, boxSession);
        this.mRequestMethod = Methods.POST;
    }

    public String getName() {
        if (this.mBodyMap.containsKey("name")) {
            return (String) this.mBodyMap.get("name");
        }
        return null;
    }

    public R setName(String str) {
        this.mBodyMap.put("name", str);
        return this;
    }

    public String getParentId() {
        if (this.mBodyMap.containsKey("parent")) {
            return ((BoxFolder) this.mBodyMap.get("parent")).getId();
        }
        return null;
    }

    public R setParentId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", str);
        this.mBodyMap.put("parent", new BoxFolder(linkedHashMap));
        return this;
    }

    /* access modifiers changed from: protected */
    public void parseHashMapEntry(JsonObject jsonObject, Entry<String, Object> entry) {
        if (((String) entry.getKey()).equals("parent")) {
            jsonObject.add((String) entry.getKey(), parseJsonObject(entry.getValue()));
        } else {
            super.parseHashMapEntry(jsonObject, entry);
        }
    }
}
