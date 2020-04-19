package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.GroupsGetInfoItem */
public final class GroupsGetInfoItem {
    private Tag _tag;
    /* access modifiers changed from: private */
    public GroupFullInfo groupInfoValue;
    /* access modifiers changed from: private */
    public String idNotFoundValue;

    /* renamed from: com.dropbox.core.v2.team.GroupsGetInfoItem$Serializer */
    static class Serializer extends UnionSerializer<GroupsGetInfoItem> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupsGetInfoItem groupsGetInfoItem, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupsGetInfoItem.tag()) {
                case ID_NOT_FOUND:
                    jsonGenerator.writeStartObject();
                    writeTag("id_not_found", jsonGenerator);
                    jsonGenerator.writeFieldName("id_not_found");
                    StoneSerializers.string().serialize(groupsGetInfoItem.idNotFoundValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case GROUP_INFO:
                    jsonGenerator.writeStartObject();
                    writeTag("group_info", jsonGenerator);
                    Serializer.INSTANCE.serialize(groupsGetInfoItem.groupInfoValue, jsonGenerator, true);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupsGetInfoItem.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupsGetInfoItem deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            boolean z;
            String str;
            GroupsGetInfoItem groupsGetInfoItem;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
                z = true;
            } else {
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
                z = false;
            }
            if (str != null) {
                if ("id_not_found".equals(str)) {
                    expectField("id_not_found", jsonParser);
                    groupsGetInfoItem = GroupsGetInfoItem.idNotFound((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("group_info".equals(str)) {
                    groupsGetInfoItem = GroupsGetInfoItem.groupInfo(Serializer.INSTANCE.deserialize(jsonParser, true));
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown tag: ");
                    sb.append(str);
                    throw new JsonParseException(jsonParser, sb.toString());
                }
                if (!z) {
                    skipFields(jsonParser);
                    expectEndObject(jsonParser);
                }
                return groupsGetInfoItem;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupsGetInfoItem$Tag */
    public enum Tag {
        ID_NOT_FOUND,
        GROUP_INFO
    }

    private GroupsGetInfoItem() {
    }

    private GroupsGetInfoItem withTag(Tag tag) {
        GroupsGetInfoItem groupsGetInfoItem = new GroupsGetInfoItem();
        groupsGetInfoItem._tag = tag;
        return groupsGetInfoItem;
    }

    private GroupsGetInfoItem withTagAndIdNotFound(Tag tag, String str) {
        GroupsGetInfoItem groupsGetInfoItem = new GroupsGetInfoItem();
        groupsGetInfoItem._tag = tag;
        groupsGetInfoItem.idNotFoundValue = str;
        return groupsGetInfoItem;
    }

    private GroupsGetInfoItem withTagAndGroupInfo(Tag tag, GroupFullInfo groupFullInfo) {
        GroupsGetInfoItem groupsGetInfoItem = new GroupsGetInfoItem();
        groupsGetInfoItem._tag = tag;
        groupsGetInfoItem.groupInfoValue = groupFullInfo;
        return groupsGetInfoItem;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isIdNotFound() {
        return this._tag == Tag.ID_NOT_FOUND;
    }

    public static GroupsGetInfoItem idNotFound(String str) {
        if (str != null) {
            return new GroupsGetInfoItem().withTagAndIdNotFound(Tag.ID_NOT_FOUND, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getIdNotFoundValue() {
        if (this._tag == Tag.ID_NOT_FOUND) {
            return this.idNotFoundValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.ID_NOT_FOUND, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isGroupInfo() {
        return this._tag == Tag.GROUP_INFO;
    }

    public static GroupsGetInfoItem groupInfo(GroupFullInfo groupFullInfo) {
        if (groupFullInfo != null) {
            return new GroupsGetInfoItem().withTagAndGroupInfo(Tag.GROUP_INFO, groupFullInfo);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public GroupFullInfo getGroupInfoValue() {
        if (this._tag == Tag.GROUP_INFO) {
            return this.groupInfoValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.GROUP_INFO, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.idNotFoundValue, this.groupInfoValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GroupsGetInfoItem)) {
            return false;
        }
        GroupsGetInfoItem groupsGetInfoItem = (GroupsGetInfoItem) obj;
        if (this._tag != groupsGetInfoItem._tag) {
            return false;
        }
        switch (this._tag) {
            case ID_NOT_FOUND:
                String str = this.idNotFoundValue;
                String str2 = groupsGetInfoItem.idNotFoundValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case GROUP_INFO:
                GroupFullInfo groupFullInfo = this.groupInfoValue;
                GroupFullInfo groupFullInfo2 = groupsGetInfoItem.groupInfoValue;
                if (groupFullInfo != groupFullInfo2 && !groupFullInfo.equals(groupFullInfo2)) {
                    z = false;
                }
                return z;
            default:
                return false;
        }
    }

    public String toString() {
        return Serializer.INSTANCE.serialize(this, false);
    }

    public String toStringMultiline() {
        return Serializer.INSTANCE.serialize(this, true);
    }
}
