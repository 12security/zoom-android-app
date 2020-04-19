package com.box.androidsdk.content.requests;

import com.box.androidsdk.content.models.BoxFolder;
import com.box.androidsdk.content.models.BoxFolder.SyncState;
import com.box.androidsdk.content.models.BoxItem;
import com.box.androidsdk.content.models.BoxList;
import com.box.androidsdk.content.models.BoxListCollaborations;
import com.box.androidsdk.content.models.BoxListItems;
import com.box.androidsdk.content.models.BoxSession;
import com.box.androidsdk.content.models.BoxUploadEmail;
import com.box.androidsdk.content.models.BoxUploadEmail.Access;
import com.box.androidsdk.content.models.BoxUser;
import com.box.androidsdk.content.requests.BoxRequest.Methods;
import com.eclipsesource.json.JsonObject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class BoxRequestsFolder {

    public static class AddFolderToCollection extends BoxRequestCollectionUpdate<BoxFolder, AddFolderToCollection> {
        public AddFolderToCollection(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFolder.class, str, str3, boxSession);
            setCollectionId(str2);
            this.mRequestMethod = Methods.PUT;
        }

        public AddFolderToCollection setCollectionId(String str) {
            return (AddFolderToCollection) super.setCollectionId(str);
        }
    }

    public static class CopyFolder extends BoxRequestItemCopy<BoxFolder, CopyFolder> {
        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getParentId() {
            return super.getParentId();
        }

        public CopyFolder(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, str3, boxSession);
        }
    }

    public static class CreateFolder extends BoxRequestItem<BoxFolder, CreateFolder> {
        public CreateFolder(String str, String str2, String str3, BoxSession boxSession) {
            super(BoxFolder.class, null, str3, boxSession);
            this.mRequestMethod = Methods.POST;
            setParentId(str);
            setName(str2);
        }

        public String getParentId() {
            if (this.mBodyMap.containsKey("parent")) {
                return (String) this.mBodyMap.get("id");
            }
            return null;
        }

        public CreateFolder setParentId(String str) {
            HashMap hashMap = new HashMap();
            hashMap.put("id", str);
            this.mBodyMap.put("parent", new BoxFolder(hashMap));
            return this;
        }

        public String getName() {
            return (String) this.mBodyMap.get("name");
        }

        public CreateFolder setName(String str) {
            this.mBodyMap.put("name", str);
            return this;
        }
    }

    public static class DeleteFolder extends BoxRequestItemDelete<DeleteFolder> {
        private static final String FALSE = "false";
        private static final String FIELD_RECURSIVE = "recursive";
        private static final String TRUE = "true";

        public /* bridge */ /* synthetic */ String getId() {
            return super.getId();
        }

        public /* bridge */ /* synthetic */ String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }

        public DeleteFolder(String str, String str2, BoxSession boxSession) {
            super(str, str2, boxSession);
            setRecursive(true);
        }

        public DeleteFolder setRecursive(boolean z) {
            this.mQueryMap.put(FIELD_RECURSIVE, z ? TRUE : FALSE);
            return this;
        }

        public Boolean getRecursive() {
            return Boolean.valueOf(TRUE.equals(this.mQueryMap.get(FIELD_RECURSIVE)));
        }
    }

    public static class DeleteFolderFromCollection extends BoxRequestCollectionUpdate<BoxFolder, AddFolderToCollection> {
        public DeleteFolderFromCollection(String str, String str2, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, boxSession);
            setCollectionId(null);
        }
    }

    public static class DeleteTrashedFolder extends BoxRequestItemDelete<DeleteTrashedFolder> {
        public /* bridge */ /* synthetic */ String getId() {
            return super.getId();
        }

        public /* bridge */ /* synthetic */ String getIfMatchEtag() {
            return super.getIfMatchEtag();
        }

        public DeleteTrashedFolder(String str, String str2, BoxSession boxSession) {
            super(str, str2, boxSession);
        }
    }

    public static class GetCollaborations extends BoxRequestItem<BoxListCollaborations, GetCollaborations> {
        public GetCollaborations(String str, String str2, BoxSession boxSession) {
            super(BoxListCollaborations.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class GetFolderInfo extends BoxRequestItem<BoxFolder, GetFolderInfo> {
        public GetFolderInfo(String str, String str2, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public GetFolderInfo setIfNoneMatchEtag(String str) {
            return (GetFolderInfo) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }

        public GetFolderInfo setLimit(int i) {
            this.mQueryMap.put(BoxList.FIELD_LIMIT, String.valueOf(i));
            return this;
        }

        public GetFolderInfo setOffset(int i) {
            this.mQueryMap.put("offset", String.valueOf(i));
            return this;
        }
    }

    public static class GetFolderItems extends BoxRequestItem<BoxListItems, GetFolderItems> {
        private static final String DEFAULT_LIMIT = "1000";
        private static final String DEFAULT_OFFSET = "0";
        private static final String LIMIT = "limit";
        private static final String OFFSET = "offset";

        public GetFolderItems(String str, String str2, BoxSession boxSession) {
            super(BoxListItems.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
            this.mQueryMap.put("limit", DEFAULT_LIMIT);
            this.mQueryMap.put("offset", "0");
        }

        public GetFolderItems setLimit(int i) {
            this.mQueryMap.put("limit", String.valueOf(i));
            return this;
        }

        public GetFolderItems setOffset(int i) {
            this.mQueryMap.put("offset", String.valueOf(i));
            return this;
        }
    }

    public static class GetTrashedFolder extends BoxRequestItem<BoxFolder, GetTrashedFolder> {
        public GetTrashedFolder(String str, String str2, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, boxSession);
            this.mRequestMethod = Methods.GET;
        }

        public GetTrashedFolder setIfNoneMatchEtag(String str) {
            return (GetTrashedFolder) super.setIfNoneMatchEtag(str);
        }

        public String getIfNoneMatchEtag() {
            return super.getIfNoneMatchEtag();
        }
    }

    public static class GetTrashedItems extends BoxRequest<BoxListItems, GetTrashedItems> {
        public GetTrashedItems(String str, BoxSession boxSession) {
            super(BoxListItems.class, str, boxSession);
            this.mRequestMethod = Methods.GET;
        }
    }

    public static class RestoreTrashedFolder extends BoxRequestItemRestoreTrashed<BoxFolder, RestoreTrashedFolder> {
        public /* bridge */ /* synthetic */ String getName() {
            return super.getName();
        }

        public /* bridge */ /* synthetic */ String getParentId() {
            return super.getParentId();
        }

        public RestoreTrashedFolder(String str, String str2, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, boxSession);
        }
    }

    public static class UpdateFolder extends BoxRequestItemUpdate<BoxFolder, UpdateFolder> {
        public UpdateFolder(String str, String str2, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, boxSession);
        }

        public UpdateSharedFolder updateSharedLink() {
            return new UpdateSharedFolder(this);
        }

        public SyncState getSyncState() {
            if (this.mBodyMap.containsKey(BoxFolder.FIELD_SYNC_STATE)) {
                return (SyncState) this.mBodyMap.get(BoxFolder.FIELD_SYNC_STATE);
            }
            return null;
        }

        public UpdateFolder setSyncState(SyncState syncState) {
            this.mBodyMap.put(BoxFolder.FIELD_SYNC_STATE, syncState);
            return this;
        }

        public Access getUploadEmailAccess() {
            if (this.mBodyMap.containsKey(BoxFolder.FIELD_FOLDER_UPLOAD_EMAIL)) {
                return ((BoxUploadEmail) this.mBodyMap.get(BoxFolder.FIELD_FOLDER_UPLOAD_EMAIL)).getAccess();
            }
            return null;
        }

        public UpdateFolder setFolderUploadEmailAccess(Access access) {
            String str;
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            String str2 = "access";
            if (access == null) {
                str = "null";
            } else {
                str = access.toString();
            }
            linkedHashMap.put(str2, str);
            this.mBodyMap.put(BoxFolder.FIELD_FOLDER_UPLOAD_EMAIL, new BoxUploadEmail(linkedHashMap));
            return this;
        }

        public String getOwnedById() {
            if (this.mBodyMap.containsKey(BoxItem.FIELD_OWNED_BY)) {
                return ((BoxUser) this.mBodyMap.get(BoxItem.FIELD_OWNED_BY)).getId();
            }
            return null;
        }

        public UpdateFolder setOwnedById(String str) {
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            linkedHashMap.put("id", str);
            this.mBodyMap.put(BoxItem.FIELD_OWNED_BY, new BoxUser(linkedHashMap));
            return this;
        }

        /* access modifiers changed from: protected */
        public void parseHashMapEntry(JsonObject jsonObject, Entry<String, Object> entry) {
            if (((String) entry.getKey()).equals(BoxFolder.FIELD_FOLDER_UPLOAD_EMAIL)) {
                jsonObject.add((String) entry.getKey(), parseJsonObject(entry.getValue()));
            } else if (((String) entry.getKey()).equals(BoxItem.FIELD_OWNED_BY)) {
                jsonObject.add((String) entry.getKey(), parseJsonObject(entry.getValue()));
            } else if (((String) entry.getKey()).equals(BoxFolder.FIELD_SYNC_STATE)) {
                jsonObject.add((String) entry.getKey(), ((SyncState) entry.getValue()).toString());
            } else {
                super.parseHashMapEntry(jsonObject, entry);
            }
        }
    }

    public static class UpdateSharedFolder extends BoxRequestUpdateSharedItem<BoxFolder, UpdateSharedFolder> {
        public UpdateSharedFolder(String str, String str2, BoxSession boxSession) {
            super(BoxFolder.class, str, str2, boxSession);
        }

        protected UpdateSharedFolder(UpdateFolder updateFolder) {
            super(updateFolder);
        }

        public UpdateSharedFolder setCanDownload(boolean z) {
            return (UpdateSharedFolder) super.setCanDownload(z);
        }

        public Boolean getCanDownload() {
            return super.getCanDownload();
        }
    }
}
