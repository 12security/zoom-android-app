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
import java.util.List;

/* renamed from: com.dropbox.core.v2.team.GroupsSelector */
public final class GroupsSelector {
    private Tag _tag;
    /* access modifiers changed from: private */
    public List<String> groupExternalIdsValue;
    /* access modifiers changed from: private */
    public List<String> groupIdsValue;

    /* renamed from: com.dropbox.core.v2.team.GroupsSelector$Serializer */
    static class Serializer extends UnionSerializer<GroupsSelector> {
        public static final Serializer INSTANCE = new Serializer();

        Serializer() {
        }

        public void serialize(GroupsSelector groupsSelector, JsonGenerator jsonGenerator) throws IOException, JsonGenerationException {
            switch (groupsSelector.tag()) {
                case GROUP_IDS:
                    jsonGenerator.writeStartObject();
                    writeTag("group_ids", jsonGenerator);
                    jsonGenerator.writeFieldName("group_ids");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupsSelector.groupIdsValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                case GROUP_EXTERNAL_IDS:
                    jsonGenerator.writeStartObject();
                    writeTag("group_external_ids", jsonGenerator);
                    jsonGenerator.writeFieldName("group_external_ids");
                    StoneSerializers.list(StoneSerializers.string()).serialize(groupsSelector.groupExternalIdsValue, jsonGenerator);
                    jsonGenerator.writeEndObject();
                    return;
                default:
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unrecognized tag: ");
                    sb.append(groupsSelector.tag());
                    throw new IllegalArgumentException(sb.toString());
            }
        }

        public GroupsSelector deserialize(JsonParser jsonParser) throws IOException, JsonParseException {
            String str;
            boolean z;
            GroupsSelector groupsSelector;
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
                if ("group_ids".equals(str)) {
                    expectField("group_ids", jsonParser);
                    groupsSelector = GroupsSelector.groupIds((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
                } else if ("group_external_ids".equals(str)) {
                    expectField("group_external_ids", jsonParser);
                    groupsSelector = GroupsSelector.groupExternalIds((List) StoneSerializers.list(StoneSerializers.string()).deserialize(jsonParser));
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
                return groupsSelector;
            }
            throw new JsonParseException(jsonParser, "Required field missing: .tag");
        }
    }

    /* renamed from: com.dropbox.core.v2.team.GroupsSelector$Tag */
    public enum Tag {
        GROUP_IDS,
        GROUP_EXTERNAL_IDS
    }

    private GroupsSelector() {
    }

    private GroupsSelector withTag(Tag tag) {
        GroupsSelector groupsSelector = new GroupsSelector();
        groupsSelector._tag = tag;
        return groupsSelector;
    }

    private GroupsSelector withTagAndGroupIds(Tag tag, List<String> list) {
        GroupsSelector groupsSelector = new GroupsSelector();
        groupsSelector._tag = tag;
        groupsSelector.groupIdsValue = list;
        return groupsSelector;
    }

    private GroupsSelector withTagAndGroupExternalIds(Tag tag, List<String> list) {
        GroupsSelector groupsSelector = new GroupsSelector();
        groupsSelector._tag = tag;
        groupsSelector.groupExternalIdsValue = list;
        return groupsSelector;
    }

    public Tag tag() {
        return this._tag;
    }

    public boolean isGroupIds() {
        return this._tag == Tag.GROUP_IDS;
    }

    public static GroupsSelector groupIds(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupsSelector().withTagAndGroupIds(Tag.GROUP_IDS, list);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public List<String> getGroupIdsValue() {
        if (this._tag == Tag.GROUP_IDS) {
            return this.groupIdsValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.GROUP_IDS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public boolean isGroupExternalIds() {
        return this._tag == Tag.GROUP_EXTERNAL_IDS;
    }

    public static GroupsSelector groupExternalIds(List<String> list) {
        if (list != null) {
            for (String str : list) {
                if (str == null) {
                    throw new IllegalArgumentException("An item in list is null");
                }
            }
            return new GroupsSelector().withTagAndGroupExternalIds(Tag.GROUP_EXTERNAL_IDS, list);
        }
        throw new IllegalArgumentException("Value is null");
    }

    public List<String> getGroupExternalIdsValue() {
        if (this._tag == Tag.GROUP_EXTERNAL_IDS) {
            return this.groupExternalIdsValue;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Invalid tag: required Tag.GROUP_EXTERNAL_IDS, but was Tag.");
        sb.append(this._tag.name());
        throw new IllegalStateException(sb.toString());
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{this._tag, this.groupIdsValue, this.groupExternalIdsValue});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (obj == this) {
            return true;
        }
        if (obj == null || !(obj instanceof GroupsSelector)) {
            return false;
        }
        GroupsSelector groupsSelector = (GroupsSelector) obj;
        if (this._tag != groupsSelector._tag) {
            return false;
        }
        switch (this._tag) {
            case GROUP_IDS:
                List<String> list = this.groupIdsValue;
                List<String> list2 = groupsSelector.groupIdsValue;
                if (list != list2 && !list.equals(list2)) {
                    z = false;
                }
                return z;
            case GROUP_EXTERNAL_IDS:
                List<String> list3 = this.groupExternalIdsValue;
                List<String> list4 = groupsSelector.groupExternalIdsValue;
                if (list3 != list4 && !list3.equals(list4)) {
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
