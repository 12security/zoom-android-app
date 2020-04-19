package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxSharedLink;
import com.box.androidsdk.content.models.BoxSharedLink.Access;
import com.box.androidsdk.content.models.BoxSharedLink.Permissions;
import com.box.androidsdk.content.requests.BoxRequest;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.box.androidsdk.content.utils.BoxDateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;

public abstract class BoxRequestUpdateSharedItem<E extends BoxItem, R extends BoxRequest<E, R>> extends BoxRequestItemUpdate<E, R> {
    public BoxRequestUpdateSharedItem updateSharedLink() {
        return this;
    }

    public BoxRequestUpdateSharedItem(Class<E> cls, String str, String str2, BoxSession boxSession) {
        super(cls, str, str2, boxSession);
        this.mRequestMethod = Methods.PUT;
    }

    protected BoxRequestUpdateSharedItem(BoxRequestItemUpdate boxRequestItemUpdate) {
        super(boxRequestItemUpdate);
    }

    public Access getAccess() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            return ((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getAccess();
        }
        return null;
    }

    public R setAccess(Access access) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            linkedHashMap = new LinkedHashMap(((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getPropertiesAsHashMap());
        }
        linkedHashMap.put("access", access);
        this.mBodyMap.put(BoxItem.FIELD_SHARED_LINK, new BoxSharedLink(linkedHashMap));
        return this;
    }

    public Date getUnsharedAt() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            return ((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getUnsharedDate();
        }
        return null;
    }

    public R setUnsharedAt(Date date) throws ParseException {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            linkedHashMap = new LinkedHashMap(((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getPropertiesAsHashMap());
        }
        if (date == null) {
            linkedHashMap.put(BoxSharedLink.FIELD_UNSHARED_AT, null);
        } else {
            linkedHashMap.put(BoxSharedLink.FIELD_UNSHARED_AT, BoxDateFormat.convertToDay(date));
        }
        this.mBodyMap.put(BoxItem.FIELD_SHARED_LINK, new BoxSharedLink(linkedHashMap));
        return this;
    }

    public R setRemoveUnsharedAtDate() throws ParseException {
        return setUnsharedAt(null);
    }

    public String getPassword() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            return ((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getPassword();
        }
        return null;
    }

    public R setPassword(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            linkedHashMap = new LinkedHashMap(((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getPropertiesAsHashMap());
        }
        linkedHashMap.put(BoxSharedLink.FIELD_PASSWORD, str);
        this.mBodyMap.put(BoxItem.FIELD_SHARED_LINK, new BoxSharedLink(linkedHashMap));
        return this;
    }

    /* access modifiers changed from: protected */
    public Boolean getCanDownload() {
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            return ((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getPermissions().getCanDownload();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public R setCanDownload(boolean z) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        if (this.mBodyMap.containsKey(BoxItem.FIELD_SHARED_LINK)) {
            linkedHashMap = new LinkedHashMap(((BoxSharedLink) this.mBodyMap.get(BoxItem.FIELD_SHARED_LINK)).getPropertiesAsHashMap());
        }
        LinkedHashMap linkedHashMap2 = new LinkedHashMap();
        if (linkedHashMap.containsKey("permissions")) {
            linkedHashMap2 = new LinkedHashMap(((Permissions) linkedHashMap.get("permissions")).getPropertiesAsHashMap());
        }
        linkedHashMap2.put(Permissions.FIELD_CAN_DOWNLOAD, Boolean.valueOf(z));
        linkedHashMap.put("permissions", new Permissions(linkedHashMap2));
        this.mBodyMap.put(BoxItem.FIELD_SHARED_LINK, new BoxSharedLink(linkedHashMap));
        return this;
    }
}
