package com.box.androidsdk.content.models;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.util.Map;

public class BoxEntity extends BoxJsonObject {
    public static final String FIELD_ID = "id";
    public static final String FIELD_ITEM_ID = "item_id";
    public static final String FIELD_ITEM_TYPE = "item_type";
    public static final String FIELD_TYPE = "type";
    private static final long serialVersionUID = 1626798809346520004L;

    public BoxEntity() {
    }

    public BoxEntity(Map<String, Object> map) {
        super(map);
    }

    public String getId() {
        String str = (String) this.mProperties.get("id");
        return str == null ? (String) this.mProperties.get("item_id") : str;
    }

    public String getType() {
        String str = (String) this.mProperties.get("type");
        return str == null ? (String) this.mProperties.get(FIELD_ITEM_TYPE) : str;
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        String name = member.getName();
        JsonValue value = member.getValue();
        if (name.equals("id")) {
            this.mProperties.put("id", value.asString());
        } else if (name.equals("type")) {
            this.mProperties.put("type", value.asString());
        } else if (name.equals(FIELD_ITEM_TYPE)) {
            this.mProperties.put(FIELD_ITEM_TYPE, value.asString());
        } else if (name.equals("item_id")) {
            this.mProperties.put("item_id", value.asString());
        } else {
            super.parseJSONMember(member);
        }
    }

    public static BoxEntity createEntityFromJson(String str) {
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.createFromJson(str);
        if (boxEntity.getType() == null) {
            return boxEntity;
        }
        if (boxEntity.getType().equals(BoxCollection.TYPE)) {
            BoxCollection boxCollection = new BoxCollection();
            boxCollection.createFromJson(str);
            return boxCollection;
        } else if (boxEntity.getType().equals("comment")) {
            BoxComment boxComment = new BoxComment();
            boxComment.createFromJson(str);
            return boxComment;
        } else if (boxEntity.getType().equals(BoxCollaboration.TYPE)) {
            BoxCollaboration boxCollaboration = new BoxCollaboration();
            boxCollaboration.createFromJson(str);
            return boxCollaboration;
        } else if (boxEntity.getType().equals("enterprise")) {
            BoxEnterprise boxEnterprise = new BoxEnterprise();
            boxEnterprise.createFromJson(str);
            return boxEnterprise;
        } else if (boxEntity.getType().equals("file_version")) {
            BoxFileVersion boxFileVersion = new BoxFileVersion();
            boxFileVersion.createFromJson(str);
            return boxFileVersion;
        } else if (boxEntity.getType().equals("event")) {
            BoxEnterpriseEvent boxEnterpriseEvent = new BoxEnterpriseEvent();
            boxEnterpriseEvent.createFromJson(str);
            return boxEnterpriseEvent;
        } else {
            BoxItem createBoxItemFromJson = BoxItem.createBoxItemFromJson(str);
            if (createBoxItemFromJson != null) {
                return createBoxItemFromJson;
            }
            BoxCollaborator createCollaboratorFromJson = BoxCollaborator.createCollaboratorFromJson(str);
            if (createCollaboratorFromJson != null) {
                return createCollaboratorFromJson;
            }
            return null;
        }
    }

    public static BoxEntity createEntityFromJson(JsonObject jsonObject) {
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.createFromJson(jsonObject);
        if (boxEntity.getType() == null) {
            return boxEntity;
        }
        if (boxEntity.getType().equals(BoxCollection.TYPE)) {
            BoxCollection boxCollection = new BoxCollection();
            boxCollection.createFromJson(jsonObject);
            return boxCollection;
        } else if (boxEntity.getType().equals("comment")) {
            BoxComment boxComment = new BoxComment();
            boxComment.createFromJson(jsonObject);
            return boxComment;
        } else if (boxEntity.getType().equals(BoxCollaboration.TYPE)) {
            BoxCollaboration boxCollaboration = new BoxCollaboration();
            boxCollaboration.createFromJson(jsonObject);
            return boxCollaboration;
        } else if (boxEntity.getType().equals("enterprise")) {
            BoxEnterprise boxEnterprise = new BoxEnterprise();
            boxEnterprise.createFromJson(jsonObject);
            return boxEnterprise;
        } else if (boxEntity.getType().equals("file_version")) {
            BoxFileVersion boxFileVersion = new BoxFileVersion();
            boxFileVersion.createFromJson(jsonObject);
            return boxFileVersion;
        } else if (boxEntity.getType().equals("event")) {
            BoxEnterpriseEvent boxEnterpriseEvent = new BoxEnterpriseEvent();
            boxEnterpriseEvent.createFromJson(jsonObject);
            return boxEnterpriseEvent;
        } else {
            BoxItem createBoxItemFromJson = BoxItem.createBoxItemFromJson(jsonObject);
            if (createBoxItemFromJson != null) {
                return createBoxItemFromJson;
            }
            BoxCollaborator createCollaboratorFromJson = BoxCollaborator.createCollaboratorFromJson(jsonObject);
            if (createCollaboratorFromJson != null) {
                return createCollaboratorFromJson;
            }
            return null;
        }
    }
}
