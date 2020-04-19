package com.box.androidsdk.content.models;

import android.text.TextUtils;
import com.box.androidsdk.content.models.BoxCollaboration.Role;
import com.box.androidsdk.content.models.BoxSharedLink.Access;
import com.box.androidsdk.content.models.BoxSharedLink.Permissions;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class BoxFolder extends BoxItem {
    public static final String[] ALL_FIELDS = {"type", "id", BoxItem.FIELD_SEQUENCE_ID, BoxItem.FIELD_ETAG, "name", "created_at", "modified_at", BoxItem.FIELD_DESCRIPTION, "size", BoxItem.FIELD_PATH_COLLECTION, "created_by", "modified_by", BoxItem.FIELD_TRASHED_AT, BoxItem.FIELD_PURGED_AT, "content_created_at", "content_modified_at", BoxItem.FIELD_OWNED_BY, BoxItem.FIELD_SHARED_LINK, FIELD_FOLDER_UPLOAD_EMAIL, "parent", BoxItem.FIELD_ITEM_STATUS, FIELD_ITEM_COLLECTION, FIELD_SYNC_STATE, FIELD_HAS_COLLABORATIONS, "permissions", FIELD_CAN_NON_OWNERS_INVITE, FIELD_IS_EXTERNALLY_OWNED, BoxItem.FIELD_ALLOWED_SHARED_LINK_ACCESS_LEVELS, FIELD_ALLOWED_INVITEE_ROLES};
    public static final String FIELD_ALLOWED_INVITEE_ROLES = "allowed_invitee_roles";
    public static final String FIELD_CAN_NON_OWNERS_INVITE = "can_non_owners_invite";
    public static final String FIELD_CONTENT_CREATED_AT = "content_created_at";
    public static final String FIELD_CONTENT_MODIFIED_AT = "content_modified_at";
    public static final String FIELD_FOLDER_UPLOAD_EMAIL = "folder_upload_email";
    public static final String FIELD_HAS_COLLABORATIONS = "has_collaborations";
    public static final String FIELD_IS_EXTERNALLY_OWNED = "is_externally_owned";
    public static final String FIELD_ITEM_COLLECTION = "item_collection";
    public static final String FIELD_SIZE = "size";
    public static final String FIELD_SYNC_STATE = "sync_state";
    public static final String TYPE = "folder";
    private static final long serialVersionUID = 8020073615785970254L;

    public enum Permission {
        CAN_DOWNLOAD(Permissions.FIELD_CAN_DOWNLOAD),
        CAN_UPLOAD("can_upload"),
        CAN_RENAME("can_rename"),
        CAN_DELETE("can_delete"),
        CAN_SHARE("can_share"),
        CAN_INVITE_COLLABORATOR("can_invite_collaborator"),
        CAN_SET_SHARE_ACCESS("can_set_share_access");
        
        private final String mValue;

        private Permission(String str) {
            this.mValue = str;
        }

        public static Permission fromString(String str) {
            Permission[] values;
            if (!TextUtils.isEmpty(str)) {
                for (Permission permission : values()) {
                    if (str.equalsIgnoreCase(permission.toString())) {
                        return permission;
                    }
                }
            }
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No enum with text %s found", new Object[]{str}));
        }

        public String toString() {
            return this.mValue;
        }
    }

    public enum SyncState {
        SYNCED(BoxItem.FIELD_SYNCED),
        NOT_SYNCED("not_synced"),
        PARTIALLY_SYNCED("partially_synced");
        
        private final String mValue;

        private SyncState(String str) {
            this.mValue = str;
        }

        public static SyncState fromString(String str) {
            SyncState[] values;
            if (!TextUtils.isEmpty(str)) {
                for (SyncState syncState : values()) {
                    if (str.equalsIgnoreCase(syncState.toString())) {
                        return syncState;
                    }
                }
            }
            throw new IllegalArgumentException(String.format(Locale.ENGLISH, "No enum with text %s found", new Object[]{str}));
        }

        public String toString() {
            return this.mValue;
        }
    }

    public BoxFolder() {
    }

    public BoxFolder(Map<String, Object> map) {
        super(map);
    }

    public static BoxFolder createFromId(String str) {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("id", str);
        linkedHashMap.put("type", TYPE);
        return new BoxFolder(linkedHashMap);
    }

    public BoxUploadEmail getUploadEmail() {
        return (BoxUploadEmail) this.mProperties.get(FIELD_FOLDER_UPLOAD_EMAIL);
    }

    public Boolean getHasCollaborations() {
        return (Boolean) this.mProperties.get(FIELD_HAS_COLLABORATIONS);
    }

    public SyncState getSyncState() {
        return (SyncState) this.mProperties.get(FIELD_SYNC_STATE);
    }

    public EnumSet<Permission> getPermissions() {
        return (EnumSet) this.mProperties.get("permissions");
    }

    public Boolean getCanNonOwnersInvite() {
        return (Boolean) this.mProperties.get(FIELD_CAN_NON_OWNERS_INVITE);
    }

    public BoxListItems getItemCollection() {
        if (this.mProperties.containsKey(FIELD_ITEM_COLLECTION)) {
            return (BoxListItems) this.mProperties.get(FIELD_ITEM_COLLECTION);
        }
        return null;
    }

    public Boolean getIsExternallyOwned() {
        return (Boolean) this.mProperties.get(FIELD_IS_EXTERNALLY_OWNED);
    }

    public ArrayList<Access> getAllowedSharedLinkAccessLevels() {
        return (ArrayList) this.mProperties.get(BoxItem.FIELD_ALLOWED_SHARED_LINK_ACCESS_LEVELS);
    }

    public ArrayList<Role> getAllowedInviteeRoles() {
        return (ArrayList) this.mProperties.get(FIELD_ALLOWED_INVITEE_ROLES);
    }

    public Date getContentCreatedAt() {
        return super.getContentCreatedAt();
    }

    public Long getSize() {
        return super.getSize();
    }

    public Date getContentModifiedAt() {
        return super.getContentModifiedAt();
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals(FIELD_FOLDER_UPLOAD_EMAIL)) {
            BoxUploadEmail boxUploadEmail = new BoxUploadEmail();
            boxUploadEmail.createFromJson(value.asObject());
            this.mProperties.put(FIELD_FOLDER_UPLOAD_EMAIL, boxUploadEmail);
        } else if (name.equals(FIELD_HAS_COLLABORATIONS)) {
            this.mProperties.put(FIELD_HAS_COLLABORATIONS, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_SYNC_STATE)) {
            this.mProperties.put(FIELD_SYNC_STATE, SyncState.fromString(value.asString()));
        } else if (name.equals("permissions")) {
            this.mProperties.put("permissions", parsePermissions(value.asObject()));
        } else if (name.equals(FIELD_CAN_NON_OWNERS_INVITE)) {
            this.mProperties.put(FIELD_CAN_NON_OWNERS_INVITE, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_ITEM_COLLECTION)) {
            JsonObject asObject = value.asObject();
            BoxListItems boxListItems = new BoxListItems();
            boxListItems.createFromJson(asObject);
            this.mProperties.put(FIELD_ITEM_COLLECTION, boxListItems);
        } else if (name.equals(FIELD_IS_EXTERNALLY_OWNED)) {
            this.mProperties.put(FIELD_IS_EXTERNALLY_OWNED, Boolean.valueOf(value.asBoolean()));
        } else if (name.equals(FIELD_ALLOWED_INVITEE_ROLES)) {
            JsonArray asArray = value.asArray();
            ArrayList arrayList = new ArrayList();
            Iterator it = asArray.iterator();
            while (it.hasNext()) {
                arrayList.add(Role.fromString(((JsonValue) it.next()).asString()));
            }
            this.mProperties.put(FIELD_ALLOWED_INVITEE_ROLES, arrayList);
        } else {
            super.parseJSONMember(member);
        }
    }

    private EnumSet<Permission> parsePermissions(JsonObject jsonObject) {
        EnumSet<Permission> noneOf = EnumSet.noneOf(Permission.class);
        Iterator it = jsonObject.iterator();
        while (it.hasNext()) {
            Member member = (Member) it.next();
            JsonValue value = member.getValue();
            if (!value.isNull() && value.asBoolean()) {
                String name = member.getName();
                if (name.equals(Permissions.FIELD_CAN_DOWNLOAD)) {
                    noneOf.add(Permission.CAN_DOWNLOAD);
                } else if (name.equals("can_upload")) {
                    noneOf.add(Permission.CAN_UPLOAD);
                } else if (name.equals("can_rename")) {
                    noneOf.add(Permission.CAN_RENAME);
                } else if (name.equals("can_delete")) {
                    noneOf.add(Permission.CAN_DELETE);
                } else if (name.equals("can_share")) {
                    noneOf.add(Permission.CAN_SHARE);
                } else if (name.equals("can_invite_collaborator")) {
                    noneOf.add(Permission.CAN_INVITE_COLLABORATOR);
                } else if (name.equals("can_set_share_access")) {
                    noneOf.add(Permission.CAN_SET_SHARE_ACCESS);
                }
            }
        }
        return noneOf;
    }
}
