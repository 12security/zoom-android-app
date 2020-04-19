package com.dropbox.core.p005v2.team;

import com.dropbox.core.stone.StoneSerializers;
import com.dropbox.core.stone.UnionSerializer;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.google.firebase.analytics.FirebaseAnalytics.Param;
import java.io.IOException;
import java.util.Arrays;

/* renamed from: com.dropbox.core.v2.team.GroupSelector */
public final class GroupSelector {
    private Tag _tag;
    /* access modifiers changed from: private */
    public String groupExternalIdValue;
    /* access modifiers changed from: private */
    public String groupIdValue;

    /* renamed from: com.dropbox.core.v2.team.GroupSelector$Serializer */
    static class Serializer extends UnionSerializer<GroupSelector> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupSelector groupSelector, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupSelector.tag()) {
                case GROUP_ID:
                    jsonGenerator.writeStartObject();
                    writeTag(Param.GROUP_ID, jsonGenerator);
                    jsonGenerator.writeFieldName(Param.GROUP_ID);
                    StoneSerializers.string().serialize(groupSelector.groupIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case GROUP_EXTERNAL_ID:
                    jsonGenerator.writeStartObject();
                    writeTag("group_external_id", jsonGenerator);
                    jsonGenerator.writeFieldName("group_external_id");
                    StoneSerializers.string().serialize(groupSelector.groupExternalIdValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupSelector.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupSelector deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupSelector groupSelector;
            if (jsonParser.getCurrentToken() == JsonToken.VALUE_STRING) {
                z = true;
                str = getStringValue(jsonParser);
                jsonParser.nextToken();
            } else {
                z = false;
                expectStartObject(jsonParser);
                str = readTag(jsonParser);
            }
            if (str != null) {
                if (Param.GROUP_ID.equals(str)) {
                    expectField(Param.GROUP_ID, jsonParser);
                    groupSelector = GroupSelector.groupId((String) StoneSerializers.string().deserialize(jsonParser));
                } else if ("group_external_id".equals(str)) {
                    expectField("group_external_id", jsonParser);
                    groupSelector = GroupSelector.groupExternalId((String) StoneSerializers.string().deserialize(jsonParser));
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
                return groupSelector;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupSelector$Tag */
    public enum Tag {
        GROUP_ID,
        GROUP_EXTERNAL_ID
    }

    private GroupSelector() {
    }

    private GroupSelector withTag(Tag tag) {
        GroupSelector groupSelector = new GroupSelector();
        groupSelector._tag = tag;
        return groupSelector;
    }

    private GroupSelector withTagAndGroupId(Tag tag, String str) {
        GroupSelector groupSelector = new GroupSelector();
        groupSelector._tag = tag;
        groupSelector.groupIdValue = str;
        return groupSelector;
    }

    private GroupSelector withTagAndGroupExternalId(Tag tag, String str) {
        GroupSelector groupSelector = new GroupSelector();
        groupSelector._tag = tag;
        groupSelector.groupExternalIdValue = str;
        return groupSelector;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isGroupId() {
        return this._tag == Tag.GROUP_ID;
    }

    public static GroupSelector groupId(String str) {
        if (str != null) {
            return new GroupSelector().withTagAndGroupId(Tag.GROUP_ID, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getGroupIdValue() {
        if (this._tag == Tag.GROUP_ID) {
            return this.groupIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.GROUP_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isGroupExternalId() {
        return this._tag == Tag.GROUP_EXTERNAL_ID;
    }

    public static GroupSelector groupExternalId(String str) {
        if (str != null) {
            return new GroupSelector().withTagAndGroupExternalId(Tag.GROUP_EXTERNAL_ID, str);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public String getGroupExternalIdValue() {
        if (this._tag == Tag.GROUP_EXTERNAL_ID) {
            return this.groupExternalIdValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.GROUP_EXTERNAL_ID, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.groupIdValue, this.groupExternalIdValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GroupSelector)) {
            return false;
        }
        GroupSelector groupSelector = (GroupSelector) obj;
        if (this._tag != groupSelector._tag) {
            return false;
        }
        switch (this._tag) {
            case GROUP_ID:
                String str = this.groupIdValue;
                String str2 = groupSelector.groupIdValue;
                if (str != str2 && !str.equals(str2)) {
                    z = false;
                }
                return z;
            case GROUP_EXTERNAL_ID:
                String str3 = this.groupExternalIdValue;
                String str4 = groupSelector.groupExternalIdValue;
                if (str3 != str4 && !str3.equals(str4)) {
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
