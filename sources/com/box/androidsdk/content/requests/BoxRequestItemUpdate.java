package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxSharedLink;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class BoxRequestItemUpdate<E extends BoxItem, R extends BoxRequest<E, R>> extends BoxRequestItem<E, R> {
    public abstract BoxRequestUpdateSharedItem updateSharedLink();

    public BoxRequestItemUpdate(Class<E> cls, String str, String str2, BoxSession boxSession) {
        super(cls, str, str2, boxSession);
        this.mRequestMethod = Methods.PUT;
    }

    protected BoxRequestItemUpdate(BoxRequestItemUpdate boxRequestItemUpdate) {
        super(boxRequestItemUpdate);
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

    public String getDescription() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_DESCRIPTION)) {
            return (String) this.mBodyMap.get(BoxItem.FIELD_DESCRIPTION);
        }
        return null;
    }

    public R setDescription(String str) {
        this.mBodyMap.put(BoxItem.FIELD_DESCRIPTION, str);
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

    public BoxSharedLink getSharedLink() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            return (BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK);
        }
        return null;
    }

    public R setSharedLink(BoxSharedLink boxSharedLink) {
        this.mBodyMap.put(BoxItem.FIELD_SHARED_LINK, boxSharedLink);
        return this;
    }

    public R setIfMatchEtag(String str) {
        return super.setIfMatchEtag(str);
    }

    public String getIfMatchEtag() {
        return super.getIfMatchEtag();
    }

    public List<String> getTags() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_TAGS)) {
            return (List) this.mBodyMap.get(BoxItem.FIELD_TAGS);
        }
        return null;
    }

    public R setTags(List<String> list) {
        JsonArray jsonArray = new JsonArray();
        for (String add : list) {
            jsonArray.add(add);
        }
        this.mBodyMap.put(BoxItem.FIELD_TAGS, jsonArray);
        return this;
    }

    /* access modifiers changed from: protected */
    public void parseHashMapEntry(JsonObject jsonObject, Entry<String, Object> entry) {
        if (((String) entry.getKey()).equals("parent")) {
            jsonObject.add((String) entry.getKey(), parseJsonObject(entry.getValue()));
        } else if (((String) entry.getKey()).equals(BoxItem.FIELD_SHARED_LINK)) {
            if (entry.getValue() == null) {
                jsonObject.add((String) entry.getKey(), (String) null);
            } else {
                jsonObject.add((String) entry.getKey(), parseJsonObject(entry.getValue()));
            }
        } else {
            super.parseHashMapEntry(jsonObject, entry);
        }
    }
}
