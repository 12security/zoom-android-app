package com.box.androidsdk.content.models;

import com.box.androidsdk.content.utils.BoxDateFormat;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

public abstract class BoxCollaborator extends BoxEntity {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final String FIELD_CREATED_AT = "created_at";
    public static final String FIELD_MODIFIED_AT = "modified_at";
    public static final String FIELD_NAME = "name";
    private static final long serialVersionUID = 4995483369186543255L;

    public BoxCollaborator() {
    }

    public BoxCollaborator(Map<String, Object> map) {
        super(map);
    }

    public String getName() {
        return (String) this.mProperties.get("name");
    }

    public Date getCreatedAt() {
        return (Date) this.mProperties.get("created_at");
    }

    public Date getModifiedAt() {
        return (Date) this.mProperties.get("modified_at");
    }

    /* access modifiers changed from: protected */
    public void parseJSONMember(Member member) {
        try {
            JsonValue value = member.getValue();
            if (member.getName().equals("name")) {
                this.mProperties.put("name", value.asString());
            } else if (member.getName().equals("created_at")) {
                this.mProperties.put("created_at", BoxDateFormat.parse(value.asString()));
            } else {
                if (member.getName().equals("modified_at")) {
                    this.mProperties.put("modified_at", BoxDateFormat.parse(value.asString()));
                    return;
                }
                super.parseJSONMember(member);
            }
        } catch (ParseException unused) {
        }
    }

    public static BoxCollaborator createCollaboratorFromJson(String str) {
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.createFromJson(str);
        if (boxEntity.getType().equals("user")) {
            BoxUser boxUser = new BoxUser();
            boxUser.createFromJson(str);
            return boxUser;
        } else if (!boxEntity.getType().equals(BoxGroup.TYPE)) {
            return null;
        } else {
            BoxGroup boxGroup = new BoxGroup();
            boxGroup.createFromJson(str);
            return boxGroup;
        }
    }

    public static BoxCollaborator createCollaboratorFromJson(JsonObject jsonObject) {
        BoxEntity boxEntity = new BoxEntity();
        boxEntity.createFromJson(jsonObject);
        if (boxEntity.getType().equals("user")) {
            BoxUser boxUser = new BoxUser();
            boxUser.createFromJson(jsonObject);
            return boxUser;
        } else if (!boxEntity.getType().equals(BoxGroup.TYPE)) {
            return null;
        } else {
            BoxGroup boxGroup = new BoxGroup();
            boxGroup.createFromJson(jsonObject);
            return boxGroup;
        }
    }
}
