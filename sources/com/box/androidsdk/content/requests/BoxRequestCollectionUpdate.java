package com.box.androidsdk.content.requests;

import android.text.TextUtils;
import com.box.androidsdk.content.models.BoxCollection;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxListCollections;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

abstract class BoxRequestCollectionUpdate<E extends BoxItem, R extends BoxRequest<E, R>> extends BoxRequestItem<E, R> {
    protected static final String FIELD_COLLECTIONS = "collections";

    public BoxRequestCollectionUpdate(Class<E> cls, String str, String str2, BoxSession boxSession) {
        super(cls, str, str2, boxSession);
        this.mRequestMethod = Methods.PUT;
    }

    /* access modifiers changed from: protected */
    public R setCollectionId(String str) {
        BoxListCollections boxListCollections = new BoxListCollections();
        if (!TextUtils.isEmpty(str)) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("id", str);
            boxListCollections.add(new BoxCollection(linkedHashMap));
        }
        this.mBodyMap.put(FIELD_COLLECTIONS, boxListCollections);
        return this;
    }

    /* access modifiers changed from: protected */
    public void parseHashMapEntry(JsonObject jsonObject, Entry<String, Object> entry) {
        if (((String) entry.getKey()).equals(FIELD_COLLECTIONS)) {
            if (entry.getValue() != null && (entry.getValue() instanceof BoxListCollections)) {
                BoxListCollections boxListCollections = (BoxListCollections) entry.getValue();
                JsonArray jsonArray = new JsonArray();
                Iterator it = boxListCollections.iterator();
                while (it.hasNext()) {
                    jsonArray.add(JsonValue.readFrom(((BoxCollection) it.next()).toJson()));
                }
                jsonObject.add((String) entry.getKey(), (JsonValue) jsonArray);
            }
            return;
        }
        super.parseHashMapEntry(jsonObject, entry);
    }
}
